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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.prefs.PreferencesService;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract class for components that needs the logging, event admin,
 * preference and some others OSGi compendium services bounded at activation
 * time.
 * <p>
 * Unfortunately, DS Annotations don't support inheritance, so any DS annotation
 * setup can't be made here in an abstract class, only in its children.<br>
 * For this reason we are using a ServiceTracker to reference the Logging
 * service instead of using DS for this parent class.
 * <p>
 * This way, a children component just need to extend this class and call
 * getLoggerService(), getPreferencesService() or getEventAdminService() methods
 * to get references to compendium services.
 * <p>
 * Implementors could choose to use only DS ignoring the creation of trackers.
 * To do this, he must override each of service bind methods and add a @Reference
 * annotation for each. <br>
 * As DS calls the bindX() method before the activate() this class will not open
 * a tracker if the service were bound before by DS.
 * 
 * @since 0.0.1
 * @author Cristiano Gavião
 * 
 */
public abstract class AbstractComponentCompendium extends
        AbstractComponentBasic {

    /**
     * Holds an atomic reference of a {@link EventAdmin} service.
     */
    private final AtomicReference<EventAdmin> eventAdminServiceRef = new AtomicReference<EventAdmin>();

    /**
     * Service tracker for the {@link EventAdmin} service.
     */
    private ServiceTracker<EventAdmin, EventAdmin> eventAdminServiceTracker;

    /**
     * Holds an atomic reference for the logging service {@link Logger}. Repair
     * that this service is not the one provided by OSGi, we are using the SLF4J
     * interface that is more complete and give us more flexibilities on report.
     */
    private final AtomicReference<Logger> loggerServiceRef = new AtomicReference<Logger>();

    /**
     * A service tracker for the logging service.
     */
    private ServiceTracker<Logger, Logger> loggerServiceTracker;

    /**
     * Service tracker for the {@link PreferencesService} service.
     */
    private ServiceTracker<PreferencesService, PreferencesService> preferenceServiceTracker;

    /**
     * Holds an atomic reference of a {@link PreferencesService} service.
     */
    private final AtomicReference<PreferencesService> preferencesServiceRef = new AtomicReference<PreferencesService>();

    /**
     * DS needs a default constructor.
     * 
     */
    public AbstractComponentCompendium() {
    }

    /**
     * Constructor created just to help unit testing. It is not used by OSGi.
     * 
     * @param componentContext
     */
    protected AbstractComponentCompendium(ComponentContext componentContext) {
        super(componentContext);
    }

    @Override
    public void activate(ComponentContext context)
            throws ExceptionComponentLifecycle {

        trace("({}) - Activating component '{}'.", getId(), getName());

        super.activate(context);

        openCompendiumServiceTrackers();

    }

    /**
     * 
     * @param eventAdmin
     */
    protected void bindEventAdmin(EventAdmin eventAdmin) {
        this.eventAdminServiceRef.set(eventAdmin);
        trace("({}) - Bound EventAdmin for component '{}'.", getId(), this
                .getClass().getSimpleName());
    }

    /**
     * 
     * @param logger
     */
    protected void bindLoggerService(Logger logger) {
        this.loggerServiceRef.set(logger);
        trace("({}) - Bound Logger for component '{}'.", getId(), this
                .getClass().getName());
    }

    /**
     * 
     * @param preferencesService
     */
    protected void bindPreferencesService(PreferencesService preferencesService) {
        this.preferencesServiceRef.set(preferencesService);
        trace("({}) - Bound PreferencesService for component '{}'.", getId(),
                this.getClass().getName());
    }

    /**
     * 
     */
    protected final void closeCompendiumServicesTrackers() {
        if (eventAdminServiceTracker != null) {
            eventAdminServiceTracker.close();
            eventAdminServiceTracker = null;
        }
        if (preferenceServiceTracker != null) {
            preferenceServiceTracker.close();
            preferenceServiceTracker = null;
        }
        if (loggerServiceTracker != null) {
            loggerServiceTracker.close();
            loggerServiceTracker = null;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.components.AbstractComponentBasic#deactivate
     * (org.osgi.service.component.ComponentContext)
     */
    @Override
    public void deactivate(ComponentContext context)
            throws ExceptionComponentLifecycle {
        debug("({}) - deactivating component '{}'.", getId(), getName());

        closeCompendiumServicesTrackers();

        super.deactivate(context);
    }

    protected final void debug(String msg) {
        getLoggerService().debug(msg);
    }

    protected final void debug(String format, Object... arguments) {
        getLoggerService().debug(format, arguments);

    }

    protected final void error(String msg) {
        getLoggerService().error(msg);
    }

    protected final void error(String format, Object... arguments) {
        getLoggerService().error(format, arguments);
    }

    protected void error(String format, Throwable throwable) {
        getLoggerService().error(format, throwable);
    }

    /**
     * A method that returns the event admin service instance.
     * 
     * @return the event admin service instance
     */
    public final EventAdmin getEventAdminService() {
        return eventAdminServiceRef.get();
    }

    /**
     * 
     * 
     * @return
     */
    public final Logger getLoggerService() {
        if (loggerServiceRef.get() == null) {
            Logger logger = LoggerFactory
                    .getLogger(AbstractComponentKernelManageable.class);
            return logger;
        }
        return loggerServiceRef.get();
    }

    /**
     * A method that returns the preference service instance.
     * 
     * @return the preference service instance
     */
    public final PreferencesService getPreferencesService() {

        return preferencesServiceRef.get();
    }

    protected final void info(String msg) {
        getLoggerService().info(msg);

    }

    protected final void info(String format, Object... arguments) {
        getLoggerService().info(format, arguments);
    }

    /**
     * @throws Exception
     * 
     */
    private void openCompendiumServiceTrackers() throws ExceptionComponentLifecycle {
        if (loggerServiceRef.get() == null) {
            openTrackerForLoggingService();
        }

        if (eventAdminServiceRef.get() == null) {
            openTrackerForEventAdminService();
        }

        if (preferencesServiceRef.get() == null) {
            openTrackerForPreferenceService();
        }
    }

    /**
     * Hook method used to setup all needed ServiceTracker (when not using DS)
     * 
     * @throws Exception
     * @nooverride
     */
    protected boolean openTrackerForEventAdminService()
            throws ExceptionComponentLifecycle {
        if (eventAdminServiceTracker == null) {
            Filter filter;
            try {
                filter = FrameworkUtil
                        .createFilter("(objectclass=org.osgi.service.event.EventAdmin)");
            } catch (InvalidSyntaxException e) {
                throw new ExceptionComponentLifecycle(e);
            }
            eventAdminServiceTracker = new ServiceTracker<>(getBundleContext(),
                    filter,
                    new ServiceTrackerCustomizer<EventAdmin, EventAdmin>() {

                        @Override
                        public EventAdmin addingService(
                                ServiceReference<EventAdmin> reference) {
                            EventAdmin eventAdmin = getBundleContext()
                                    .getService(reference);
                            bindEventAdmin(eventAdmin);

                            return eventAdmin;
                        }

                        @Override
                        public void modifiedService(
                                ServiceReference<EventAdmin> reference,
                                EventAdmin service) {

                            if (getEventAdminService().equals(service)) {
                                addingService(reference);
                            }
                        }

                        @Override
                        public void removedService(
                                ServiceReference<EventAdmin> reference,
                                EventAdmin service) {
                            unbindEventAdmin(service);
                        }
                    });
            eventAdminServiceTracker.open();
        }
        return true;
    }

    /**
     * 
     * @return
     * @throws Exception
     * @nooverride
     */
    protected boolean openTrackerForLoggingService()
            throws ExceptionComponentLifecycle {
        if (loggerServiceTracker == null) {
            Filter filter;
            try {
                filter = FrameworkUtil
                        .createFilter("(objectclass=org.slf4j.Logger)");
            } catch (InvalidSyntaxException e) {
                throw new ExceptionComponentLifecycle(e);
            }
            loggerServiceTracker = new ServiceTracker<>(getBundleContext(),
                    filter, new ServiceTrackerCustomizer<Logger, Logger>() {

                        @Override
                        public Logger addingService(
                                ServiceReference<Logger> reference) {
                            Logger logger = getBundleContext().getService(
                                    reference);
                            bindLoggerService(logger);
                            return logger;
                        }

                        @Override
                        public void modifiedService(
                                ServiceReference<Logger> reference,
                                Logger service) {
                            if (getLoggerService().equals(service)) {
                                addingService(reference);
                            }
                            System.err.println("SERVICE MODIFICATION !!!!");
                        }

                        @Override
                        public void removedService(
                                ServiceReference<Logger> reference,
                                Logger service) {
                            unbindLoggerService(service);
                        }
                    });
            loggerServiceTracker.open();
        }
        return true;
    }

    /**
     * 
     * @return
     * @throws Exception
     * @nooverride
     */
    protected boolean openTrackerForPreferenceService()
            throws ExceptionComponentLifecycle {
        if (preferenceServiceTracker == null) {
            Filter filter;
            try {
                filter = FrameworkUtil
                        .createFilter("(objectclass=org.osgi.service.prefs.PreferencesService)");
            } catch (InvalidSyntaxException e) {
                throw new ExceptionComponentLifecycle(e);
            }
            preferenceServiceTracker = new ServiceTracker<>(
                    getBundleContext(),
                    filter,
                    new ServiceTrackerCustomizer<PreferencesService, PreferencesService>() {

                        @Override
                        public PreferencesService addingService(
                                ServiceReference<PreferencesService> reference) {
                            PreferencesService preferencesService = getBundleContext()
                                    .getService(reference);
                            bindPreferencesService(preferencesService);
                            return preferencesService;
                        }

                        @Override
                        public void modifiedService(
                                ServiceReference<PreferencesService> reference,
                                PreferencesService service) {
                            if (getPreferencesService().equals(service)) {
                                addingService(reference);
                            }
                        }

                        @Override
                        public void removedService(
                                ServiceReference<PreferencesService> reference,
                                PreferencesService service) {
                            unbindPreferencesService(service);
                        }
                    });
            preferenceServiceTracker.open();
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.service.event.EventAdmin#postEvent()
     */
    protected final void postEvent(String eventTopic) {
        Map<String, Object> properties = new HashMap<String, Object>();
        postEvent(eventTopic, properties);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.service.event.EventAdmin#postEvent()
     */
    protected final void postEvent(String eventTopic, Map<String, ?> properties) {
        Event event = new Event(eventTopic, properties);
        getEventAdminService().postEvent(event);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.service.event.EventAdmin#postEvent()
     */
    protected final void postEvent(String eventTopic, String context) {
        Map<String, Object> properties = new HashMap<String, Object>();
        // properties.put(LUNIFERA_RUNTIME_EVENT_PROPERTY_CONTEXT, context);
        postEvent(eventTopic, properties);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.service.event.EventAdmin#sendEvent()
     */
    protected final void sendEvent(String eventTopic) {
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        sendEvent(eventTopic, properties);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.service.event.EventAdmin#sendEvent()
     */
    protected final void sendEvent(String eventTopic,
            Dictionary<String, ?> properties) {
        Event event = new Event(eventTopic, properties);
        getEventAdminService().sendEvent(event);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.service.event.EventAdmin#sendEvent()
     */
    protected final void sendEvent(String eventTopic, String context) {
        Dictionary<String, Object> properties = new Hashtable<String, Object>();
        // properties.put(LUNIFERA_RUNTIME_EVENT_PROPERTY_CONTEXT, context);
        sendEvent(eventTopic, properties);

    }

    protected final void trace(String msg) {
        getLoggerService().trace(msg);
    }

    protected final void trace(String format, Object... arguments) {
        getLoggerService().trace(format, arguments);
    }

    /**
     * Method called by the DS or other to unbind an instance of
     * {@link EventAdmin} service.
     * 
     * @param eventAdmin
     */
    protected final void unbindEventAdmin(EventAdmin eventAdmin) {
        trace("({}) - Unbound EventAdmin for component '{}'.", getId(), this
                .getClass().getName());
        eventAdminServiceRef.compareAndSet(eventAdmin, null);
    }

    /**
     * Method called by the DS or other to unbind an instance of {@link Logger}
     * service.
     * 
     * @param logger
     */
    protected final void unbindLoggerService(Logger logger) {
        trace("({}) - Unbound Logger for component '{}'.", getId(), this
                .getClass().getName());
        loggerServiceRef.compareAndSet(logger, null);
    }

    /**
     * Method called by the DS or other to unbind an instance of
     * {@link PreferencesService} service.
     * 
     * @param preferencesService
     */
    protected final void unbindPreferencesService(
            PreferencesService preferencesService) {
        trace("({}) - Unbound PreferencesService for component '{}'.", getId(),
                this.getClass().getName());
        preferencesServiceRef.compareAndSet(preferencesService, null);
    }

    protected void warn(String msg) {
        getLoggerService().warn(msg);
    }

    protected void warn(String format, Object... arguments) {
        getLoggerService().warn(format, arguments);
    }
}
