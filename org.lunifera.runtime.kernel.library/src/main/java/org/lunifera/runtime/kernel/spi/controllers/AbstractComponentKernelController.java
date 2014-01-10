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
package org.lunifera.runtime.kernel.spi.controllers;

import java.util.concurrent.atomic.AtomicReference;

import org.lunifera.runtime.kernel.api.components.ExceptionComponentLifecycle;
import org.lunifera.runtime.kernel.spi.components.AbstractComponentKernelManageable;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.coordinator.Coordination;
import org.osgi.service.coordinator.Coordinator;
import org.osgi.service.coordinator.Participant;
import org.osgi.service.event.Event;

/**
 * This is an abstract class that should be used by Kernel Controllers DS based
 * components.
 * 
 * @since 0.0.1
 * @author Cristiano Gavião
 */
public abstract class AbstractComponentKernelController extends
        AbstractComponentKernelManageable {

    /**
     * This class is used to track the first level boot coordination.
     * 
     * @since 0.0.1
     * @author Cristiano Gavião
     * 
     */
    class FirstLevelBootParticipant implements Participant {

        @Override
        public void ended(Coordination coordination) throws Exception {

            // called when the first level boot have finished.
            // doSecondLevelActivation();
        }

        @Override
        public void failed(Coordination coordination) throws Exception {
            // if first level was ok I should rollback it ?
            rollbackFirstLevelActivation();
        }
    }

    private final AtomicReference<Coordinator> coordinatorService = new AtomicReference<>();

    /**
     * DS needs a default constructor.
     */
    public AbstractComponentKernelController() {
    }

    /**
     * Constructor created just to help unit testing. It is not used by OSGi.
     * 
     * @param componentContext
     */
    protected AbstractComponentKernelController(
            ComponentContext componentContext) {
        super(componentContext);
    }

    protected void bindCoordinator(Coordinator coordinatorService) {
        this.coordinatorService.set(coordinatorService);
        getLoggerService().trace("Bound Coordinator for component '{}'.",
                this.getClass().getName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.components.AbstractComponentManageable
     * #doFirstLevelActivation()
     */
    @Override
    protected void doFirstLevelActivation() throws ExceptionComponentLifecycle {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.components.AbstractComponentManageable
     * #doProcessRuntimeAnnotations(j)
     */
    @Override
    protected void doProcessRuntimeAnnotations() throws Exception {
    }

    protected Coordinator getCoordinator() {
        return this.coordinatorService.get();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lunifera.runtime.kernel.api.components.ManageableComponent#
     * handleKernelNotification(org.osgi.service.event.Event)
     */
    @Override
    public void handleKernelNotification(Event event) {

    }

    /**
     *
     */
    protected void rollbackFirstLevelActivation() {

    }

    protected void unbindCoordinator(Coordinator coordinatorService) {
        getLoggerService().trace("Unbound Coordinator for component '{}'.",
                this.getClass().getName());
        this.coordinatorService.compareAndSet(coordinatorService, null);
    }

}
