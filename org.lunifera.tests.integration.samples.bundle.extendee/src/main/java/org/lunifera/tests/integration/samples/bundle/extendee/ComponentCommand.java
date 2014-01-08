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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.lunifera.runtime.kernel.api.components.AbstractComponentCompendium;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @since 0.0.1
 * @author Cristiano Gavião
 */
@Component(enabled = true, service = ComponentCommand.class, property = {
        "osgi.command.scope=lunifera.test", "osgi.command.function=list" })
public class ComponentCommand extends AbstractComponentCompendium {

    InterfaceForTesting interfaceForTesting;

    public InterfaceForTesting getInterfaceForTesting() {
        return interfaceForTesting;
    }

    @Reference(policy = ReferencePolicy.DYNAMIC)
    public void bindInterfaceForTesting(InterfaceForTesting interfaceForTesting) {
        this.interfaceForTesting = interfaceForTesting;

        System.out.println("BIND OK");
    }

    public void unbindInterfaceForTesting(
            InterfaceForTesting interfaceForTesting) {
        if (this.interfaceForTesting == interfaceForTesting) {
            this.interfaceForTesting = null;
            System.out.println("UNBIND OK");
        }
    }

    public void list() {
        Map<String, String> map = getInterfaceForTesting().getPropertiesFromComponent();
        Set<String> keys = map.keySet();

        System.out.println("Properties:");
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
            String key = iterator.next();
            String value = map.get(key);
            System.out.println("  - " + key + "=" + value);
        }
    }

}
