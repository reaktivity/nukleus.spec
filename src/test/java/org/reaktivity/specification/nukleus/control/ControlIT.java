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
        "route.explicit/input/none/nukleus",
        "route.explicit/input/none/controller"
    })
    public void shouldRouteExplicitInputNone() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route.explicit/input/new/nukleus",
        "route.explicit/input/new/controller"
    })
    public void shouldRouteExplicitInputNew() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route.explicit/input/established/nukleus",
        "route.explicit/input/established/controller"
    })
    public void shouldRouteExplicitInputEstablished() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route.generated/input/none/nukleus",
        "route.generated/input/none/controller"
    })
    public void shouldRouteGeneratedInputNone() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route.generated/input/new/nukleus",
        "route.generated/input/new/controller"
    })
    public void shouldRouteGeneratedInputNew() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route.generated/input/established/nukleus",
        "route.generated/input/established/controller"
    })
    public void shouldRouteGeneratedInputEstablished() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route.explicit/output/none/nukleus",
        "route.explicit/output/none/controller"
    })
    public void shouldRouteExplicitOutputNone() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route.explicit/output/new/nukleus",
        "route.explicit/output/new/controller"
    })
    public void shouldRouteExplicitOutputNew() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route.explicit/output/established/nukleus",
        "route.explicit/output/established/controller"
    })
    public void shouldRouteExplicitOutputEstablished() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route.generated/output/none/nukleus",
        "route.generated/output/none/controller"
    })
    public void shouldRouteGeneratedOutputNone() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route.generated/output/new/nukleus",
        "route.generated/output/new/controller"
    })
    public void shouldRouteGeneratedOutputNew() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "route.generated/output/established/nukleus",
        "route.generated/output/established/controller"
    })
    public void shouldRouteGeneratedOutputEstablished() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "unroute/input/none/nukleus",
        "unroute/input/none/controller"
    })
    public void shouldUnrouteInputNone() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "unroute/input/new/nukleus",
        "unroute/input/new/controller"
    })
    public void shouldUnrouteInputNew() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "unroute/input/established/nukleus",
        "unroute/input/established/controller"
    })
    public void shouldUnrouteInputEstablished() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "unroute/output/none/nukleus",
        "unroute/output/none/controller"
    })
    public void shouldUnrouteOutputNone() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "unroute/output/new/nukleus",
        "unroute/output/new/controller"
    })
    public void shouldUnrouteOutputNew() throws Exception
    {
        k3po.finish();
    }

    @Test
    @Specification({
        "unroute/output/established/nukleus",
        "unroute/output/established/controller"
    })
    public void shouldUnrouteOutputEstablished() throws Exception
    {
        k3po.finish();
    }
}
