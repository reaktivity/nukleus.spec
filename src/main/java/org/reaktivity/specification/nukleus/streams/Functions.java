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
package org.reaktivity.specification.nukleus.streams;

import static org.agrona.IoUtil.mapExistingFile;
import static org.agrona.IoUtil.unmap;

import java.io.File;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.util.Random;

import org.agrona.concurrent.AtomicBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.RingBufferDescriptor;
import org.kaazing.k3po.lang.el.Function;
import org.kaazing.k3po.lang.el.spi.FunctionMapperSpi;

@Deprecated
public final class Functions
{
    private static final Random RANDOM = new Random();

    @Function
    public static byte[] newReferenceId()
    {
        // positive
        return longToBytesNative(RANDOM.nextLong() & 0x3fffffffffffffffL);
    }

    @Function
    public static byte[] newInitialStreamId()
    {
        // odd, positive, non-zero
        return longToBytesNative((RANDOM.nextLong() & 0x3fffffffffffffffL) | 0x0000000000000001L);
    }

    @Function
    public static byte[] newReplyStreamId()
    {
        // even, positive, non-zero
        long value;
        do
        {
            value = RANDOM.nextLong() & 0x3ffffffffffffffeL;
        }
        while (value == 0L);

        return longToBytesNative(value);
    }

    @Function
    public static Layout map(String filename, int streamCapacity)
    {
        return new DeferredLayout(new File(filename), streamCapacity);
    }

    private abstract static class Layout implements AutoCloseable
    {
        public abstract AtomicBuffer getBuffer();
    }

    public static final class EagerLayout extends Layout
    {
        private final MappedByteBuffer byteBuffer;
        private final AtomicBuffer atomicBuffer;

        public EagerLayout(
            File location,
            int streamCapacity)
        {
            File absolute = location.getAbsoluteFile();
            int length = streamCapacity + RingBufferDescriptor.TRAILER_LENGTH;
            this.byteBuffer = mapExistingFile(absolute, location.getAbsolutePath());
            this.atomicBuffer = new UnsafeBuffer(byteBuffer, 0, length);
        }

        @Override
        public AtomicBuffer getBuffer()
        {
            return atomicBuffer;
        }

        @Override
        public void close()
        {
            unmap(byteBuffer);
        }
    }

    public static final class DeferredLayout extends Layout
    {
        private final File location;
        private final int streamCapacity;

        private EagerLayout delegate;

        public DeferredLayout(
            File location,
            int streamCapacity)
        {
            this.location = location;
            this.streamCapacity = streamCapacity;
        }

        @Override
        public AtomicBuffer getBuffer()
        {
            ensureInitialized();
            return delegate.atomicBuffer;
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
            return String.format("Layout [%s]", location);
        }

        void ensureInitialized()
        {
            if (delegate == null)
            {
                delegate = new EagerLayout(location, streamCapacity);
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
            return "streams";
        }
    }

    private static byte[] longToBytesNative(long value)
    {
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN)
        {
            return new byte[]
            {
                (byte) (value >> 56),
                (byte) (value >> 48),
                (byte) (value >> 40),
                (byte) (value >> 32),
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value
            };
        }
        else
        {
            return new byte[]
            {
                (byte) value,
                (byte) (value >> 8),
                (byte) (value >> 16),
                (byte) (value >> 24),
                (byte) (value >> 32),
                (byte) (value >> 40),
                (byte) (value >> 48),
                (byte) (value >> 56)
            };
        }
    }

    private Functions()
    {
        // utility
    }
}
