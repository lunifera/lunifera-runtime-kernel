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

import java.util.Enumeration;
import java.util.Map;

import org.lunifera.runtime.kernel.api.components.AbstractComponentCompendium;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

import com.google.common.collect.Maps;

@Component(enabled = true, service = InterfaceForTesting.class,
        configurationPid = "service1",
        configurationPolicy = ConfigurationPolicy.REQUIRE)
public class AnComponentWithoutReferencesToCompendiumServices extends
        AbstractComponentCompendium implements InterfaceForTesting {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.tests.integration.samples.bundle.extendee.InterfaceForTesting
     * #getValue(java.lang.String)
     */
    @Override
    public String getValue(String index) {

        return (String) getProperties().get(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.tests.integration.samples.bundle.extendee.InterfaceForTesting
     * #getPropertiesFromComponent()
     */
    @Override
    public Map<String, String> getPropertiesFromComponent() {

        Map<String, String> map = Maps.newHashMap();
        Enumeration<String> keys = getProperties().keys();

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            Object value = getProperties().get(key);
            if (value instanceof String) {
                map.put(key, (String) value);
            }
        }

        return map;
    }

}
