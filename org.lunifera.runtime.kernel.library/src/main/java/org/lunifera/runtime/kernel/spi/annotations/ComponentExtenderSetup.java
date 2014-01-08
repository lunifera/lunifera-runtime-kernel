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
package org.lunifera.runtime.kernel.spi.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.lunifera.runtime.kernel.spi.extenders.AbstractComponentExtender;
import org.lunifera.runtime.kernel.spi.extenders.ContributionItemResourceType;
import org.lunifera.runtime.kernel.spi.extenders.ExtensionHandlingStrategy;
import org.osgi.framework.Bundle;

/**
 * Annotation used to configure an Extender component based on the
 * {@link AbstractComponentExtender}.
 * 
 * @author Cristiano Gavião
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentExtenderSetup {

    /**
     * Defines the type of the manifest header item required by the extender.
     * 
     * @return
     */
    ContributionItemResourceType contributionItemResourceType() default ContributionItemResourceType.PROPERTIES_FILE_RESOURCE;

    /**
     * The manifest header name that identifies a contributor bundle.
     * 
     * @return the name of manifest header that will be used to identify a
     *         contributor bundle.
     */
    String manifestHeaderName();

    /**
     * The bit mask of the {@code OR}ing of the bundle states to be tracked.
     * {@code eg: Bundle.ACTIVE|Bundle.RESOLVED}
     * 
     * @return
     */
    int stateMask() default Bundle.ACTIVE;

    /**
     * Defines how a extension bundle should be processed.
     * 
     * @return the default extension strategy.
     */
    ExtensionHandlingStrategy strategy() default ExtensionHandlingStrategy.PER_ITEM;

    /**
     * The default ContributionHandlerService service to reference. This will
     * used to compose a filter to track the right service.
     */
    String targetContributionHandlerService() default "org.lunifera.runtime.kernel.spi.extenders.handlers.ComponentContributionHandlerPropertiesFileResourcePerItemWithConfigAdmin";
}
