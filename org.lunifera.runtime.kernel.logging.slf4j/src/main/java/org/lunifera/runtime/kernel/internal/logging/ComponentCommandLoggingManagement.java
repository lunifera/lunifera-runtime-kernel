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

import java.util.concurrent.atomic.AtomicReference;

import org.apache.felix.service.command.Descriptor;
import org.lunifera.runtime.kernel.api.components.AbstractComponentCommand;
import org.lunifera.runtime.kernel.spi.logging.ManagementServiceFrameworkLogging;
import org.lunifera.runtime.kernel.spi.services.CommandService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * 
 * @author Cristiano Gavião
 * 
 */
@Component(enabled = true, immediate = true,
        service = CommandService.class, property = {
                "osgi.command.scope=lunifera.logging",
                "osgi.command.function=setLevel" })
public class ComponentCommandLoggingManagement extends AbstractComponentCommand {

//    private final AtomicReference<ManagementServiceFrameworkLogging> loggingManagementServiceRef = new AtomicReference<>();
//
//    @Reference
//    protected void bindLoggingManagentService(ManagementServiceFrameworkLogging logger) {
//        loggingManagementServiceRef.set(logger);
//        trace("Bound ManagementServiceFrameworkLogging for component '{}'.", this
//                .getClass().getName());
//    }
//
//    ManagementServiceFrameworkLogging getLoggingManagementService() {
//        return loggingManagementServiceRef.get();
//    }
//
//    @Descriptor("Set the log level of the informed logger.")
//    public void setLogLevel(String name, String level) {
//        getLoggingManagementService().setLogLevel(name, level);
//    }
//
//    @Descriptor("Set the current root logger log level.")
//    public void setRootLogLevel(String level) {
//        getLoggingManagementService().setRootLogLevel(level);
//    }
//
//    protected void unbindLoggingManagentService(ManagementServiceFrameworkLogging logger) {
//        trace("Unbound ManagementServiceFrameworkLogging for component '{}'.", this
//                .getClass().getName());
//        loggingManagementServiceRef.compareAndSet(logger, null);
//    }
}
