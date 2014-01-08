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
package org.lunifera.runtime.kernel.internal.controller.configurations.cm;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.felix.service.command.Descriptor;
import org.lunifera.runtime.kernel.api.components.AbstractComponentCompendium;
import org.lunifera.runtime.kernel.api.controller.configurations.ConfigurationManagementService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @since 0.0.1
 * @author Cristiano Gavião
 */
@Component(
        enabled = true,
        service = ComponentCommandKernelConfigurations.class,
        property = {
                "osgi.command.scope=lunifera.configuration",
                "osgi.command.function=displayFactoryConfiguration,displayConfiguration,putProperty" })
public class ComponentCommandKernelConfigurations extends
        AbstractComponentCompendium {

    private final AtomicReference<ConfigurationManagementService> configurationManagementService = new AtomicReference<>();

    @Reference(policy = ReferencePolicy.DYNAMIC)
    protected void bindConfigurationManagementService(
            ConfigurationManagementService configurationManagementService) {
        this.configurationManagementService.set(configurationManagementService);
        trace("({}) - Bound ConfigurationManagementService for component '{}'.",
                getId(), getName());
    }

    protected ConfigurationManagementService getConfigurationManagementService() {
        return this.configurationManagementService.get();
    }

    @Descriptor("Display one factory configuration.")
    public void displayFactoryConfiguration(String factoryPid) {
        System.out.println(getConfigurationManagementService()
                .displayFactoryConfiguration(factoryPid));
    }

    @Descriptor("Display one configuration.")
    public void displayConfiguration(String pid) {
        System.out.println(getConfigurationManagementService()
                .displayConfiguration(pid));
    }

    @Descriptor("Add a property and its value inside the specified pid.")
    public void putProperty(String pid, String propertyName, String value) {

    }

    protected void unbindConfigurationManagementService(
            ConfigurationManagementService configurationManagementService) {
        trace("({}) - Unbound ConfigurationManagementService for component '{}'.",
                getId(), getName());
        this.configurationManagementService.compareAndSet(
                configurationManagementService, null);
    }
}
