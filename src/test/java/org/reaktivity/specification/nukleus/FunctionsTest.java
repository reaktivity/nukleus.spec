/**
 * Copyright 2016-2017 The Reaktivity Project
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

import static org.reaktivity.specification.nukleus.Functions.string;

import org.agrona.DirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.junit.Test;
import org.reaktivity.specification.core.internal.types.StringFW;

import org.junit.Assert;

public class FunctionsTest
{
    @Test
    public void headerTest()
    {
        String expectedString = "abcdefghijklmnopqrstuvwzyz";
        DirectBuffer buffer = new UnsafeBuffer(string(expectedString));
        StringFW actual = new StringFW().wrap(buffer, 0, buffer.capacity());
        Assert.assertEquals(expectedString, actual.asString());
        Assert.assertEquals(expectedString.length(), actual.sizeof() - 2);
    }
}
