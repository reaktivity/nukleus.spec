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
package org.reaktivity.specification.nukleus.control;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.rules.RuleChain.outerRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;
import org.kaazing.k3po.junit.annotation.ScriptProperty;
import org.kaazing.k3po.junit.annotation.Specification;
import org.kaazing.k3po.junit.rules.K3poRule;

public class ControlIT
{
    private final K3poRule k3po = new K3poRule();

    private final TestRule timeout = new DisableOnDebug(new Timeout(5, SECONDS));

    @Rule
    public final TestRule chain = outerRule(k3po).around(timeout);

    @Test
    @Specification({
        "route/client/nukleus",
        "route/client/controller"
    })
    public void shouldRouteClient() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/proxy/nukleus",
        "route/proxy/controller"
    })
    public void shouldRouteProxy() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/nukleus",
        "route/server/controller"
    })
    public void shouldRouteServer() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/nukleus",
        "route/server/controller"
    })
    @ScriptProperty("authorization [0x01 0x00 0x00 0x00 0x00 0x00 0x00 0x00]")
    public void shouldRouteServerWithAuthenticationRequired() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/nukleus",
        "route/server/controller"
    })
    @ScriptProperty("authorization [0x01 0x00 0xc0 0x00 0x00 0x00 0x00 0x00]")
    public void shouldRouteServerWithAuthenticatedRolesRequired() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/client/nukleus",
        "route/client/controller",
        "unroute/client/nukleus",
        "unroute/client/controller"
    })
    public void shouldUnrouteClient() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/proxy/nukleus",
        "route/proxy/controller",
        "unroute/proxy/nukleus",
        "unroute/proxy/controller"
    })
    public void shouldUnrouteProxy() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/nukleus",
        "route/server/controller",
        "unroute/server/nukleus",
        "unroute/server/controller"
    })
    @ScriptProperty("authorization [0x01 0x00 0xc0 0x00 0x00 0x00 0x00 0x00]")
    public void shouldUnrouteServerWithAuthorization() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/nukleus",
        "route/server/controller",
        "unroute/server/nukleus",
        "unroute/server/controller"
    })
    public void shouldUnrouteServerUnsecure() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "resolve/no.roles/nukleus",
        "resolve/no.roles/controller"
    })
    public void shouldResolveWithoutRoles() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "resolve/with.roles/nukleus",
        "resolve/with.roles/controller"
    })
    public void shouldResolveWithRoles() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "resolve/fails.too.many.roles/nukleus",
        "resolve/fails.too.many.roles/controller"
    })
    public void shouldFailToResolveMoreThan48Roles() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "resolve/with.roles/nukleus",
        "resolve/with.roles/controller",
        "unresolve/succeeds/nukleus",
        "unresolve/succeeds/controller"
    })
    public void shouldUnresolve() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "unresolve/fails.unknown.role/nukleus",
        "unresolve/fails.unknown.role/controller"
    })
    public void shouldFailToUnresolveUnknownRole() throws Exception
    {
        k3po.finish();
    }

}
