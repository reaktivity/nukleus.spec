/**
 * Copyright 2016-2019 The Reaktivity Project
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

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static org.agrona.BitUtil.SIZE_OF_BYTE;
import static org.agrona.BitUtil.SIZE_OF_SHORT;

import java.nio.ByteBuffer;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.agrona.BitUtil;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.kaazing.k3po.lang.el.Function;
import org.kaazing.k3po.lang.el.spi.FunctionMapperSpi;
import org.reaktivity.specification.nukleus.internal.types.String16FW;
import org.reaktivity.specification.nukleus.internal.types.StringFW;
import org.reaktivity.specification.nukleus.internal.types.control.Capability;

public final class CoreFunctions
{
    private static final ThreadLocal<StringFW.Builder> STRING_RW = ThreadLocal.withInitial(StringFW.Builder::new);
    private static final ThreadLocal<String16FW.Builder> STRING16_RW = ThreadLocal.withInitial(String16FW.Builder::new);

    @Function
    public static byte[] fromHex(
        String text)
    {
        return BitUtil.fromHex(text);
    }

    @Function
    public static Random random()
    {
        return ThreadLocalRandom.current();
    }

    @Function
    public static byte[] string(
        String text)
    {
        int capacity = SIZE_OF_BYTE + ofNullable(text).orElse("").length() * 2 + 1;
        MutableDirectBuffer writeBuffer = new UnsafeBuffer(new byte[capacity]);
        StringFW string = STRING_RW.get()
                                   .wrap(writeBuffer, 0, writeBuffer.capacity())
                                   .set(text, UTF_8)
                                   .build();

        final byte[] array = new byte[string.sizeof()];
        string.buffer().getBytes(0, array);
        return array;
    }

    @Function
    public static byte[] string16(
        String text)
    {
        int capacity = SIZE_OF_SHORT + ofNullable(text).orElse("").length() * 2 + 1;
        MutableDirectBuffer writeBuffer = new UnsafeBuffer(new byte[capacity]);
        String16FW string16 = STRING16_RW.get()
                                         .wrap(writeBuffer, 0, writeBuffer.capacity())
                                         .set(text, UTF_8)
                                         .build();

        final byte[] array = new byte[string16.sizeof()];
        string16.buffer().getBytes(0, array);
        return array;
    }

    @Function
    public static byte capabilities(
        String capability,
        String... optionalCapabilities)
    {
        return of(capability, optionalCapabilities);
    }

    private static byte of(
        String name,
        String... optionalNames)
    {
        byte capabilityMask = 0x00;
        capabilityMask |= 1 << Capability.valueOf(name).ordinal();
        for (int i = 0; i < optionalNames.length; i++)
        {
            capabilityMask |= 1 << Capability.valueOf(optionalNames[i]).ordinal();
        }
        return capabilityMask;
    }

    public static class Mapper extends FunctionMapperSpi.Reflective
    {
        public Mapper()
        {
            super(CoreFunctions.class);
        }

        @Override
        public String getPrefixName()
        {
            return "core";
        }
    }

    private CoreFunctions()
    {
        // utility
    }
}
