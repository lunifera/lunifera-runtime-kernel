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

import org.lunifera.runtime.kernel.api.components.AbstractComponentCompendium;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

@Component(enabled = true,
        service = AnComponentFactory.class,
        configurationPid = "service1",
        configurationPolicy = ConfigurationPolicy.REQUIRE)
public class AnComponentFactory extends
        AbstractComponentCompendium {

}
