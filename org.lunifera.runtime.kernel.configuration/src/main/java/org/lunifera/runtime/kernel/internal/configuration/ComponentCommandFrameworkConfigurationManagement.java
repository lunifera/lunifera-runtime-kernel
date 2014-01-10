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
package org.lunifera.runtime.kernel.internal.configuration;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.felix.service.command.Descriptor;
import org.lunifera.runtime.kernel.api.configuration.LuniferaFrameworkConfigurationManagementService;
import org.lunifera.runtime.kernel.spi.components.AbstractComponentCommand;
import org.lunifera.runtime.kernel.spi.services.CommandService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @since 0.0.1
 * @author Cristiano Gavião
 */
@Component(
        enabled = true,
        immediate = true,
        service = CommandService.class,
        property = {
                "osgi.command.scope=lunifera.configuration",
                "osgi.command.function=displayFactoryConfiguration,displayConfiguration,putProperty" })
public class ComponentCommandFrameworkConfigurationManagement extends
        AbstractComponentCommand {

    private final AtomicReference<LuniferaFrameworkConfigurationManagementService> luniferaFrameworkConfigurationManagementService = new AtomicReference<>();

    @Reference(policy = ReferencePolicy.DYNAMIC)
    protected void bindConfigurationManagementService(
            LuniferaFrameworkConfigurationManagementService luniferaFrameworkConfigurationManagementService) {
        this.luniferaFrameworkConfigurationManagementService.set(luniferaFrameworkConfigurationManagementService);
        trace("({}) - Bound LuniferaFrameworkConfigurationManagementService for component '{}'.",
                getId(), getName());
    }

    protected LuniferaFrameworkConfigurationManagementService getConfigurationManagementService() {
        return this.luniferaFrameworkConfigurationManagementService.get();
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
            LuniferaFrameworkConfigurationManagementService luniferaFrameworkConfigurationManagementService) {
        trace("({}) - Unbound LuniferaFrameworkConfigurationManagementService for component '{}'.",
                getId(), getName());
        this.luniferaFrameworkConfigurationManagementService.compareAndSet(
                luniferaFrameworkConfigurationManagementService, null);
    }
}
