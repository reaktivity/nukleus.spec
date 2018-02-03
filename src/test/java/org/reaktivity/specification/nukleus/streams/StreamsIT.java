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
package org.reaktivity.specification.nukleus.streams;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.RuleChain.outerRule;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.ScriptProperty;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;
import org.reaktivity.specification.nukleus.NukleusRule;

public class StreamsIT
{
    private final K3poRule k3po = new K3poRule();

    private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

    private final TestRule nukleus = new NukleusRule().directory("target/nukleus-itests");

    @Rule
    public final TestRule chain = outerRule(nukleus).around(k3po).around(timeout);

    @Test
    @Specification({
        "connection.established.unsecure/client",
        "connection.established.unsecure/server"
    })
    @ScriptProperty("serverConnect \"nukleus://example/streams/source\"")
    public void shouldEstablishConnection() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Ignore("Awaiting release of k3po-nukleus-ext with support for option nukleus:authorization")
    @Test
    @Specification({
        "multiple.connections.established/client",
        "multiple.connections.established/server"
    })
    @ScriptProperty("serverConnect \"nukleus://example/streams/source\"")
    public void shouldEstablishMultipleConnections() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Ignore("Awaiting release of k3po-nukleus-ext with support for option nukleus:authorization")
    @Test
    @Specification({
        "connection.established/client",
        "connection.established/server"
    })
    @ScriptProperty({"serverConnect \"nukleus://example/streams/source\"",
                     "routeAuthorization [0x01 0x00 0xc0]"})
    public void shouldEstablishAuthorizedConnection() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Ignore("Awaiting release of k3po-nukleus-ext with support for option nukleus:authorization")
    @Test
    @Specification({
        "connection.refused.not.authorized/client",
        "connection.refused.not.authorized/server"
    })
    @ScriptProperty("serverConnect \"nukleus://example/streams/source\"")
    public void shoulResetConnectionWhenNotAuthorized() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

    @Test
    @Specification({
        "connection.refused.unknown.route.ref/client",
        "connection.refused.unknown.route.ref/server"
    })
    @ScriptProperty("serverConnect \"nukleus://example/streams/source\"")
    public void shoulResetConnectionWhenNotRouted() throws Exception
    {
        k3po.start();
        k3po.notifyBarrier("ROUTED_SERVER");
        k3po.finish();
    }

}
