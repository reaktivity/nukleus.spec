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
    @ScriptProperty("routeAuthorization 0x0001_000000000000L")
    public void shouldRouteServerWithAuthenticationRequired() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/nukleus",
        "route/server/controller"
    })
    @ScriptProperty("routeAuthorization 0x0001_00000000000cL")
    public void shouldRouteServerWithRolesRequired() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/multiple.routes/nukleus",
        "route/server/multiple.routes/controller"
    })
    public void shouldRouteServerMultipleRoutes() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/multiple.authorizations/nukleus",
        "route/server/multiple.authorizations/controller"
    })
    public void shouldRouteServerMultipleAuthorizations() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/multiple.extensions/nukleus",
        "route/server/multiple.extensions/controller"
    })
    public void shouldRouteServerByExtension() throws Exception
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
    public void shouldUnrouteServer() throws Exception
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
    @ScriptProperty("routeAuthorization 0x0001_000000000000L")
    public void shouldUnrouteServerWithAuthorization() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/multiple.routes/nukleus",
        "route/server/multiple.routes/controller",
        "unroute/server/multiple.routes/nukleus",
        "unroute/server/multiple.routes/controller"
    })
    public void shouldUnrouteServerMultipleRoutes() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/multiple.authorizations/nukleus",
        "route/server/multiple.authorizations/controller",
        "unroute/server/multiple.authorizations/nukleus",
        "unroute/server/multiple.authorizations/controller"
    })
    public void shouldUnrouteServerByAuthorization() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route/server/multiple.extensions/nukleus",
        "route/server/multiple.extensions/controller",
        "unroute/server/multiple.extensions/nukleus",
        "unroute/server/multiple.extensions/controller"
    })
    public void shouldUnrouteServerByExtension() throws Exception
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
        "unresolve/fails.unknown.domain.or.role/nukleus",
        "unresolve/fails.unknown.domain.or.role/controller"
    })
    public void shouldFailToUnresolveUnknownRole() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "freeze/nukleus",
        "freeze/controller"
    })
    public void shouldFreeze() throws Exception
    {
        k3po.finish();
    }
}
