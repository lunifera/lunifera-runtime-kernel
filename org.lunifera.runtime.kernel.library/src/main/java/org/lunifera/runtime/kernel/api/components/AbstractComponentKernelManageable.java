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
package org.lunifera.runtime.kernel.api.components;

import java.util.Dictionary;
import java.util.Hashtable;

import org.lunifera.runtime.kernel.spi.services.KernelManageableService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.coordinator.Coordination;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

/**
 * An abstract class for components that should be managed by Lunifera kernel
 * using two levels of activation.
 * <p>
 * The first level activation is started by the DS component.<br>
 * The second level is performed by the kernel, after a successful
 * authentication and authorization processes.
 * 
 * @since 0.0.1
 * @author Cristiano Gavião
 * 
 */
public abstract class AbstractComponentKernelManageable extends
        AbstractComponentCompendium implements KernelManageableService {

    /**
     * Class used to notify the components from kernel events.
     * 
     */
    class KernelNotificationEventHandler implements EventHandler {
        @Override
        public void handleEvent(Event event) {
            // String reportTitle = (String) event.getProperty("title");
            // String reportPath = (String) event.getProperty("path");
            trace("handling event '{}' for component '{}'.", event.getTopic(),
                    getId());
            handleKernelNotification(event);
        }
    }

    /**
     * DS needs a default constructor.
     */
    public AbstractComponentKernelManageable() {
    }

    /**
     * Constructor created just to help unit testing. It is not used by OSGi.
     * 
     * @param componentContext
     */
    protected AbstractComponentKernelManageable(
            ComponentContext componentContext) {
        super(componentContext);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.components.AbstractComponentCompendium
     * #activate(org.osgi.service.component.ComponentContext)
     */
    @Override
    public final void activate(ComponentContext context)
            throws ExceptionComponentLifecycle {

        super.activate(context);

        if (getClass().getAnnotations().length > 0) {
            trace("({}) - Starting annotations processing...", getId());
            doProcessRuntimeAnnotations();
        }

        if (!getProperties().isEmpty()) {
            trace("({}) - Starting properties processing...", getId());
            doProcessProperties(getProperties());
        }

        trace("({}) - Openning service trackers...", getId());
        doOpenServiceTrackers();

        trace("({}) - Registering kernel event handlers of component...",
                getId());
        doRegisterKernelManagementEventHandler();

        trace("({}) - Starting first level activation of component...", getId());
        doFirstLevelActivation();

        debug("({}) - First level activation of component '{}' was executed with success.",
                getId(), getName());

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.components.AbstractComponentCompendium
     * #deactivate(org.osgi.service.component.ComponentContext)
     */
    @Override
    public final void deactivate(ComponentContext context)
            throws ExceptionComponentLifecycle {

        trace("deactivating component '{}'.", getId());

        doFirstLevelDeactivation();

        doCloseServiceTrackers();

        doUnregisterKernelManagementEventHandler();

        super.deactivate(context);
    }

    /**
     * 
     */
    protected void doCloseServiceTrackers() {
        // must be implemented by concrete classes when needed.

    }

    /**
     * This method should be implemented by concrete children.
     * 
     * @throws ExceptionComponentLifecycle
     * 
     */
    protected abstract void doFirstLevelActivation()
            throws ExceptionComponentLifecycle;

    /**
     * @throws ExceptionComponentLifecycle
     * 
     */
    protected void doFirstLevelDeactivation()
            throws ExceptionComponentLifecycle {
        // should be implemented by concrete children.
    }

    protected void doOpenServiceTrackers() throws ExceptionComponentLifecycle {

    }

    /**
     * @param properties
     */
    protected void doProcessProperties(Dictionary<String, Object> properties) {
        // should be implemented by concrete children.
    }

    /**
     * This method is a hook that could be used to process the annotations
     * tagged in the component class declaration.
     * <p>
     * As this hook method will be called at the beginning of the activation
     * procedure, values encountered here could be hold in class variables and
     * used by other methods of the component. <br>
     * Must be implemented by children concrete components when needed.
     * 
     * @throws Exception
     */
    protected abstract void doProcessRuntimeAnnotations()
            throws ExceptionComponentLifecycle;

    /**
     * This method will register the EventHandler that will start the 2nd level
     * activation.
     */
    private void doRegisterKernelManagementEventHandler() {
        String[] topics = new String[] { "org/lunifera/runtime/kernel/activation/2ND" };

        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put(EventConstants.EVENT_TOPIC, topics);
        // props.put(EventConstants.EVENT_FILTER, "(title=samplereport)");
        getBundleContext().registerService(EventHandler.class.getName(),
                new KernelNotificationEventHandler(), props);

    }

    /**
     *
     */
    private void doUnregisterKernelManagementEventHandler() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.service.coordinator.Participant#ended(org.osgi.service.coordinator
     * .Coordination)
     */
    @Override
    public void ended(Coordination coordination) throws Exception {
        String name = coordination.getName();

        doKernelProcedure(name);

    }

    /**
     * @param name
     */
    protected void doKernelProcedure(String name) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.service.coordinator.Participant#failed(org.osgi.service.coordinator
     * .Coordination)
     */
    @Override
    public void failed(Coordination coordination) throws Exception {
        // means that the any kernel coordination was unsuccessful.

        // check if we need to stop the component
        Throwable e = coordination.getFailure();
        if (e instanceof Exception) {
            getComponentContext().disableComponent(getName());
        }
    }

    /**
     * @param event
     */
    protected void handleKernelNotification(Event event) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.components.AbstractComponentBasic#modified
     * (org.osgi.service.component.ComponentContext)
     */
    @Override
    protected void modified(ComponentContext context) throws Exception {
        super.modified(context);

        doCloseServiceTrackers();

        doOpenServiceTrackers();
    }
}
