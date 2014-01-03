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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.ComponentContext;

/**
 * The basic abstract parent class for all Lunifera's OSGi Declarative Service
 * based components.
 * <p>
 * It has a custom activate() method that set informations collected about the
 * component and has some common useful methods.<br>
 * 
 * @since 0.0.1
 * @author Cristiano Gavião
 * 
 */
public abstract class AbstractComponentBasic {

    public static final String COMPONENT_DESCRIPTION = "component.description";

    /**
     * The component ID is defined by DS bundle.
     */
    private ComponentContext componentContext;

    /**
     * The component ID is defined by the component's developer.
     */
    private String componentDescription;

    /**
     * The component ID is defined by DS bundle.
     */
    private Long componentId;

    /**
     * The component ID is defined by the component's developer.
     */
    private String componentName;

    /**
     * DS needs a default constructor.
     */
    public AbstractComponentBasic() {
    }

    /**
     * Constructor created just to help unit testing. It is not used by OSGi.
     * 
     * @param componentContext
     */
    public AbstractComponentBasic(ComponentContext componentContext) {
        super();
        this.componentContext = componentContext;
    }

    /**
     * This is the main activation method of DS components.
     * <p>
     * 
     * @param context
     */
    protected void activate(ComponentContext context) throws Exception {

        // save bundleContext reference...
        componentContext = context;

        // set common component attributes
        componentId = (Long) getProperties().get(
                ComponentConstants.COMPONENT_ID);
        componentName = (String) getProperties().get(
                ComponentConstants.COMPONENT_NAME);
        componentDescription = (String) getProperties().get(
                COMPONENT_DESCRIPTION);
    }

    /**
     * This is the main deactivation method of DS components.
     * 
     * @param context
     */
    protected void deactivate(ComponentContext context) throws Exception {

        componentDescription = null;
        componentName = null;
        componentId = null;
        componentContext = null;

    }

    /**
     * This method returns the associated BundleContext.
     * 
     * @return
     */
    protected final BundleContext getBundleContext() {
        return getComponentContext().getBundleContext();
    }

    /**
     * This method returns the associated ComponentContext.
     * 
     * @return the ComponentContext.
     */
    protected final ComponentContext getComponentContext() {
        return componentContext;
    }

    /**
     * Return the component description
     * 
     * @return
     */
    protected final String getDescription() {
        return (componentDescription != null ? componentDescription : "");
    }

    /**
     * 
     * 
     * @return
     */
    protected final Long getId() {
        return (componentId != null ? componentId : 0);
    }

    /**
     * The name of the registered component.
     * <p>
     * This value is set after the component be activated by DS.
     * 
     * @return the component's name.
     */
    protected final String getName() {
        return (componentName != null ? componentName : "");
    }

    /**
     * Return the properties map associated with the component instance.
     * 
     * @return the component's properties map.
     */
    protected final Dictionary<String, Object> getProperties() {
        return getComponentContext().getProperties();
    }

    /**
     * This is the main activation method of DS components.
     * <p>
     * 
     * @param context
     */
    protected void modified(ComponentContext context) throws Exception {

        activate(context);
    }

    @Override
    public final String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
