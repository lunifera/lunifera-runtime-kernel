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
package org.lunifera.runtime.kernel.spi.extenders.handlers;

import org.lunifera.runtime.kernel.api.KernelConstants;
import org.lunifera.runtime.kernel.api.components.AbstractComponentCompendium;
import org.lunifera.runtime.kernel.api.components.ExceptionComponentLifecycle;
import org.lunifera.runtime.kernel.spi.extenders.ContributionHandlerService;
import org.lunifera.runtime.kernel.spi.extenders.ContributionItemResourceType;
import org.lunifera.runtime.kernel.spi.extenders.ExtensionHandlingStrategy;
import org.osgi.service.component.ComponentContext;

public abstract class AbstractComponentFactoryContributionHandler extends
        AbstractComponentCompendium implements ContributionHandlerService {

    private ExtensionHandlingStrategy extensionHandlingStrategy;
    private ContributionItemResourceType contributionItemResourceType;

    /**
     * A default constructor is required by the OSGi Declarative Service.
     */
    public AbstractComponentFactoryContributionHandler() {
    }

    /**
     * Constructor created just to help unit testing. It is not used by OSGi.
     * 
     * @param contributionItemResourceType
     * @param extensionHandlingStrategy
     */
    protected AbstractComponentFactoryContributionHandler(
            ContributionItemResourceType contributionItemResourceType,
            ExtensionHandlingStrategy extensionHandlingStrategy) {
        this.extensionHandlingStrategy = extensionHandlingStrategy;
        this.contributionItemResourceType = contributionItemResourceType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.components.AbstractComponentCompendium
     * #activate(org.osgi.service.component.ComponentContext)
     */
    @Override
    public void activate(ComponentContext context)
            throws ExceptionComponentLifecycle {
        super.activate(context);

        String contributorManifestHeaderItemTypeStr = (String) getProperties()
                .get(KernelConstants.EXTENDER_SERVICE_ATTR_CONTRIBUTION_ITEM_RESOURCE_TYPE);
        String extensionHandlingStrategyStr = (String) getProperties()
                .get(KernelConstants.EXTENDER_SERVICE_ATTR_EXTENSION_HANDLING_STRATEGY);

        this.extensionHandlingStrategy = ExtensionHandlingStrategy
                .fromString(extensionHandlingStrategyStr);
        this.contributionItemResourceType = ContributionItemResourceType
                .fromString(contributorManifestHeaderItemTypeStr);
    }

    /**
     * @return the extensionHandlingStrategy
     */
    @Override
    public final ExtensionHandlingStrategy getExtensionHandlingStrategy() {
        return extensionHandlingStrategy;
    }

    /**
     * @return the contributionItemResourceType
     */
    @Override
    public final ContributionItemResourceType getManifestHeaderItemType() {
        return contributionItemResourceType;
    }
}
