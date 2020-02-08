/**
 * Copyright 2016-2020 The Reaktivity Project
 *
 * The Reaktivity Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.reaktivity.specification.nukleus;

import static org.agrona.BitUtil.align;
import static org.agrona.IoUtil.createEmptyFile;
import static org.agrona.IoUtil.mapExistingFile;
import static org.agrona.IoUtil.unmap;

import java.io.File;
import java.nio.MappedByteBuffer;
import java.util.Random;

import org.agrona.BitUtil;
import org.agrona.CloseHelper;
import org.agrona.concurrent.AtomicBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.broadcast.BroadcastBufferDescriptor;
import org.agrona.concurrent.ringbuffer.ManyToOneRingBuffer;
import org.agrona.concurrent.ringbuffer.RingBuffer;
import org.agrona.concurrent.ringbuffer.RingBufferDescriptor;
import org.kaazing.k3po.lang.el.Function;
import org.kaazing.k3po.lang.el.spi.FunctionMapperSpi;

public final class Functions
{
    private static final Random RANDOM = new Random();

    private static final int CONTROL_VERSION = 1;

    private static final int FIELD_OFFSET_VERSION = 0;
    private static final int FIELD_SIZE_VERSION = BitUtil.SIZE_OF_INT;

    private static final int FIELD_OFFSET_COMMAND_BUFFER_LENGTH = FIELD_OFFSET_VERSION + FIELD_SIZE_VERSION;
    private static final int FIELD_SIZE_COMMAND_BUFFER_LENGTH = BitUtil.SIZE_OF_INT;

    private static final int FIELD_OFFSET_RESPONSE_BUFFER_LENGTH =
            FIELD_OFFSET_COMMAND_BUFFER_LENGTH + FIELD_SIZE_COMMAND_BUFFER_LENGTH;
    private static final int FIELD_SIZE_RESPONSE_BUFFER_LENGTH = BitUtil.SIZE_OF_INT;

    private static final int FIELD_OFFSET_COUNTER_LABELS_BUFFER_LENGTH =
            FIELD_OFFSET_RESPONSE_BUFFER_LENGTH + FIELD_SIZE_RESPONSE_BUFFER_LENGTH;
    private static final int FIELD_SIZE_COUNTER_LABELS_BUFFER_LENGTH = BitUtil.SIZE_OF_INT;

    private static final int FIELD_OFFSET_COUNTER_VALUES_BUFFER_LENGTH =
            FIELD_OFFSET_COUNTER_LABELS_BUFFER_LENGTH + FIELD_SIZE_COUNTER_LABELS_BUFFER_LENGTH;
    private static final int FIELD_SIZE_COUNTER_VALUES_BUFFER_LENGTH = BitUtil.SIZE_OF_INT;

    private static final int END_OF_META_DATA_OFFSET = align(
            FIELD_OFFSET_COUNTER_VALUES_BUFFER_LENGTH + FIELD_SIZE_COUNTER_VALUES_BUFFER_LENGTH, BitUtil.CACHE_LINE_LENGTH);

    @Function
    public static Helper directory(
        String configPath)
    {
        return new Helper(new File(configPath));
    }

    @Function
    public static Long newReferenceId()
    {
        return RANDOM.nextLong();
    }

    @Function
    public static Long newStreamId()
    {
        return RANDOM.nextLong();
    }

    @Function
    public static Long newCorrelationId()
    {
        return RANDOM.nextLong();
    }

    public static final class Helper
    {
        private final File configDirectory;

        private int ringCapacity;
        private int broadcastCapacity;

        private Helper(
            File configDirectory)
        {
            this.configDirectory = configDirectory;
        }

        public Helper controlCapacity(
            int ringCapacity,
            int broadcastCapacity)
        {
            this.ringCapacity = ringCapacity;
            this.broadcastCapacity = broadcastCapacity;
            return this;
        }

        public ControlHelper controlNew()
        {
            return new ControlHelper.Eager(true, new File(configDirectory, "control"), ringCapacity, broadcastCapacity);
        }

        public ControlHelper control()
        {
            return new ControlHelper.Deferred(false, new File(configDirectory, "control"), ringCapacity, broadcastCapacity);
        }

        public abstract static class ControlHelper implements AutoCloseable
        {
            private long correlationId;

            public abstract AtomicBuffer getNukleus();

            public abstract AtomicBuffer getController();

            public final long nextCorrelationId()
            {
                RingBuffer ring = new ManyToOneRingBuffer(getNukleus());
                correlationId = ring.nextCorrelationId();
                return correlationId;
            }

            public final long correlationId()
            {
                return correlationId;
            }

            private static final class Eager extends ControlHelper
            {
                private final MappedByteBuffer buffer;
                private final AtomicBuffer nukleus;
                private final AtomicBuffer controller;

                private Eager(
                    boolean overwrite,
                    File location,
                    int commandBufferCapacity,
                    int responseBufferCapacity)
                {
                    File controlFile = location.getAbsoluteFile();
                    int counterLabelsBufferCapacity = 0;
                    int counterValuesBufferCapacity = 0;

                    if (overwrite)
                    {
                        int commandBufferLength = commandBufferCapacity + RingBufferDescriptor.TRAILER_LENGTH;
                        int responseBufferLength = responseBufferCapacity + BroadcastBufferDescriptor.TRAILER_LENGTH;
                        int counterLabelsBufferLength = counterLabelsBufferCapacity;
                        int counterValuesBufferLength = counterValuesBufferCapacity;

                        CloseHelper.close(createEmptyFile(controlFile, END_OF_META_DATA_OFFSET +
                                commandBufferLength + responseBufferLength +
                                counterLabelsBufferLength + counterValuesBufferLength));

                        MappedByteBuffer metadata = mapExistingFile(controlFile, "metadata", 0, END_OF_META_DATA_OFFSET);
                        metadata.putInt(FIELD_OFFSET_VERSION, CONTROL_VERSION);
                        metadata.putInt(FIELD_OFFSET_COMMAND_BUFFER_LENGTH, commandBufferCapacity);
                        metadata.putInt(FIELD_OFFSET_RESPONSE_BUFFER_LENGTH, responseBufferCapacity);
                        metadata.putInt(FIELD_OFFSET_COUNTER_LABELS_BUFFER_LENGTH, counterLabelsBufferCapacity);
                        metadata.putInt(FIELD_OFFSET_COUNTER_VALUES_BUFFER_LENGTH, counterValuesBufferCapacity);
                        unmap(metadata);
                    }
                    else
                    {
                        MappedByteBuffer metadata = mapExistingFile(controlFile, "metadata", 0, END_OF_META_DATA_OFFSET);
                        assert CONTROL_VERSION == metadata.getInt(FIELD_OFFSET_VERSION);
                        commandBufferCapacity = metadata.getInt(FIELD_OFFSET_COMMAND_BUFFER_LENGTH);
                        responseBufferCapacity = metadata.getInt(FIELD_OFFSET_RESPONSE_BUFFER_LENGTH);
                        counterLabelsBufferCapacity = metadata.getInt(FIELD_OFFSET_COUNTER_LABELS_BUFFER_LENGTH);
                        counterValuesBufferCapacity = metadata.getInt(FIELD_OFFSET_COUNTER_VALUES_BUFFER_LENGTH);
                        unmap(metadata);
                    }

                    int commandBufferLength = commandBufferCapacity + RingBufferDescriptor.TRAILER_LENGTH;
                    int responseBufferLength = responseBufferCapacity + BroadcastBufferDescriptor.TRAILER_LENGTH;

                    int commandBufferOffset = END_OF_META_DATA_OFFSET;
                    this.buffer = mapExistingFile(controlFile, "commands");
                    this.nukleus = new UnsafeBuffer(buffer, commandBufferOffset, commandBufferLength);

                    int responseBufferOffset = commandBufferOffset + commandBufferLength;
                    this.controller = new UnsafeBuffer(buffer, responseBufferOffset, responseBufferLength);
                }

                @Override
                public AtomicBuffer getNukleus()
                {
                    return nukleus;
                }

                @Override
                public AtomicBuffer getController()
                {
                    return controller;
                }

                @Override
                public void close()
                {
                    unmap(buffer);
                }

                @Override
                public String toString()
                {
                    return String.format("controlCapacity(%d, %d)", nukleus.capacity(), controller.capacity());
                }
            }

            private static final class Deferred extends ControlHelper
            {
                private final boolean overwrite;
                private final File location;
                private final int ringCapacity;
                private final int broadcastCapacity;

                private Eager delegate;

                private Deferred(
                    boolean overwrite,
                    File location,
                    int ringCapacity,
                    int broadcastCapacity)
                {
                    this.overwrite = overwrite;
                    this.location = location;
                    this.ringCapacity = ringCapacity;
                    this.broadcastCapacity = broadcastCapacity;
                }

                @Override
                public AtomicBuffer getNukleus()
                {
                    ensureInitialized();
                    return delegate.nukleus;
                }

                @Override
                public AtomicBuffer getController()
                {
                    ensureInitialized();
                    return delegate.controller;
                }

                @Override
                public void close() throws Exception
                {
                    if (delegate != null)
                    {
                        delegate.close();
                    }
                }

                @Override
                public String toString()
                {
                    return String.format("controlCapacity(%d, %d)", ringCapacity, broadcastCapacity);
                }

                void ensureInitialized()
                {
                    if (delegate == null)
                    {
                        delegate = new Eager(overwrite, location, ringCapacity, broadcastCapacity);
                    }
                }
            }
        }
    }

    public static class Mapper extends FunctionMapperSpi.Reflective
    {
        public Mapper()
        {
            super(Functions.class);
        }

        @Override
        public String getPrefixName()
        {
            return "nuklei";
        }
    }

    private Functions()
    {
        // utility
    }
}
