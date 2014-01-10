/*******************************************************************************
 * Copyright (c) 2013, 2014 C4biz Softwares ME, Loetz KG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Cristiano Gavião - initial API and implementation
 *******************************************************************************/
package org.lunifera.runtime.kernel.internal.logging;

import org.lunifera.runtime.kernel.spi.annotations.ComponentExtenderSetup;
import org.lunifera.runtime.kernel.spi.extenders.AbstractComponentExtender;
import org.lunifera.runtime.kernel.spi.extenders.ComponentExtenderService;
import org.lunifera.runtime.kernel.spi.extenders.ContributionHandlerService;
import org.osgi.framework.Filter;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.util.tracker.ServiceTracker;

/**
 * 
 * 
 * @since 0.0.1
 * @author Cristiano Gavião
 */
@Component(enabled = true, immediate=true, service = ComponentExtenderService.class)
@ComponentExtenderSetup(manifestHeaderName = "Lunifera-Logging")
public class ComponentExtenderFrameworkLoggingSetup extends
        AbstractComponentExtender {

    /**
     * A default constructor is required by the OSGi Declarative Service.
     */
    public ComponentExtenderFrameworkLoggingSetup() {
        super();
    }

    /**
     * Constructor created just to help unit testing. It is not used by OSGi.
     * 
     * @param compendiumServices
     * @param contributionHandlerService
     */
    protected ComponentExtenderFrameworkLoggingSetup(
            ComponentContext componentContext,
            ContributionHandlerService contributionHandlerService,
            Filter contributionHandlerServiceFilter,
            ServiceTracker<ContributionHandlerService, ContributionHandlerService> contributionHandlerServiceTracker) {
        super(componentContext, contributionHandlerService,
                contributionHandlerServiceFilter,
                contributionHandlerServiceTracker);
    }
}
