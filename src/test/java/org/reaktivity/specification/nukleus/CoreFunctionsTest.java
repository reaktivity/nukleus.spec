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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.agrona.DirectBuffer;
import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.junit.Test;
import org.reaktivity.specification.nukleus.internal.types.String16FW;
import org.reaktivity.specification.nukleus.internal.types.StringFW;

public class CoreFunctionsTest
{
    @Test
    public void shouldEncodeString()
    {
        byte[] array = CoreFunctions.string("value");

        DirectBuffer buffer = new UnsafeBuffer(array);
        StringFW string = new StringFW().wrap(buffer, 0, buffer.capacity());

        assertEquals("value", string.asString());
    }

    @Test
    public void shouldEncodeNullString()
    {
        byte[] array = CoreFunctions.string(null);

        DirectBuffer buffer = new UnsafeBuffer(array);
        StringFW string = new StringFW().wrap(buffer, 0, buffer.capacity());

        assertNull(string.asString());
    }

    @Test
    public void shouldEncodeEmptyString()
    {
        byte[] array = CoreFunctions.string("");

        MutableDirectBuffer buffer = new UnsafeBuffer(new byte[array.length + 1]);
        buffer.putBytes(0, array);
        StringFW string = new StringFW().wrap(buffer, 0, buffer.capacity());

        assertEquals("", string.asString());
    }

    @Test
    public void shouldEncodeString16()
    {
        byte[] array = CoreFunctions.string16("value");

        DirectBuffer buffer = new UnsafeBuffer(array);
        String16FW string = new String16FW().wrap(buffer, 0, buffer.capacity());

        assertEquals("value", string.asString());
    }

    @Test
    public void shouldEncodeNullString16()
    {
        byte[] array = CoreFunctions.string16(null);

        DirectBuffer buffer = new UnsafeBuffer(array);
        String16FW string = new String16FW().wrap(buffer, 0, buffer.capacity());

        assertNull(string.asString());
    }

    @Test
    public void shouldEncodeEmptyString16()
    {
        byte[] array = CoreFunctions.string16("");

        MutableDirectBuffer buffer = new UnsafeBuffer(new byte[array.length + 1]);
        buffer.putBytes(0, array);
        String16FW string = new String16FW().wrap(buffer, 0, buffer.capacity());

        assertEquals("", string.asString());
    }

    @Test
    public void shouldMaskChallengeCapability()
    {
        final byte challengeMask = CoreFunctions.capabilities("CHALLENGE");
        assertEquals(0x01, challengeMask);
    }
}
