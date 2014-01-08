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
package org.lunifera.tests.integration.samples.bundle.extendee;

import org.lunifera.runtime.kernel.api.components.AbstractComponentCompendium;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.prefs.PreferencesService;
import org.slf4j.Logger;

@Component(enabled = true,
        service = AnComponentWithReferencesToCompendiumServices.class,
        configurationPid = "service2",
        configurationPolicy = ConfigurationPolicy.REQUIRE)
public class AnComponentWithReferencesToCompendiumServices extends
        AbstractComponentCompendium {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.components.AbstractComponentCompendium
     * #bindEventAdmin(org.osgi.service.event.EventAdmin)
     */
    @Reference
    @Override
    protected void bindEventAdmin(EventAdmin eventAdmin) {
        super.bindEventAdmin(eventAdmin);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.components.AbstractComponentCompendium
     * #bindLoggerService(org.slf4j.Logger)
     */
    @Override
    @Reference
    protected void bindLoggerService(Logger logger) {
        super.bindLoggerService(logger);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.components.AbstractComponentCompendium
     * #bindPreferencesService(org.osgi.service.prefs.PreferencesService)
     */
    @Override
    @Reference
    protected void bindPreferencesService(PreferencesService preferencesService) {
        super.bindPreferencesService(preferencesService);
    }
}
