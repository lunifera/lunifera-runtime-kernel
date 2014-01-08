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
/**
 *
 */
package org.lunifera.runtime.kernel.api;

/**
 * @since 0.0.1
 * @author cvgaviao
 * 
 */
public interface KernelConstants {

    String COMPONENT_SUPPRESS_TRACKERS = "lunifera.component.suppress.trackers";

    String TOPIC_ACTIVATION_2ND_END = "";
    String TOPIC_ACTIVATION_2ND_START = "";

    String TOPIC_RESOURCE_ADDED = "";
    String TOPIC_RESOURCE_CHANGED = "";
    String TOPIC_RESOURCE_REMOVED = "";
    
    String EXTENDER_SERVICE_LOOKUP_STATE_MASK = "lunifera.extender.lookup.state.mask";
    String EXTENDER_SERVICE_ATTR_MANIFEST_HEADER_NAME = "lunifera.extender.manifest.header.name";
    String EXTENDER_SERVICE_ATTR_CONTRIBUTION_ITEM_RESOURCE_TYPE = "lunifera.extender.contribution.item.resource.type";
    String EXTENDER_SERVICE_ATTR_CONTRIBUTION_HANDLER_SERVICE = "lunifera.extender.contribution.handler";
    String EXTENDER_SERVICE_ATTR_EXTENSION_HANDLING_STRATEGY = "lunifera.extender.handling.strategy";

    String LUNIFERA_LOGGING_LEVEL = "lunifera.logging.level";

    String LUNIFERA_LOGGER_NAME = "lunifera.logger.name";

    public static final String COMPONENT_DESCRIPTION = "component.description";
}
