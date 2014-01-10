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

import java.util.Set;

import org.lunifera.runtime.kernel.spi.services.KernelManageableService;
import org.osgi.resource.Capability;

/**
 * Represents a controller component into the Kernel.
 * <p>
 * The Lunifera's Kernel is composed by a number of backstage components with
 * specific features called Controllers.<br>
 * They provides certain capabilities where the Kernel is built under and won't
 * work well without them.
 * <p>
 * At container activation the bootstrap component is the one responsible to
 * coordinate a <b>Sanity Check</b> in the Kernel. <br>
 * That is, it will load the current runtime model data, then check all
 * installed and already active controllers and then call the second level
 * activation of each one that is authorized.
 * 
 * @since 0.0.1
 * @author Cristiano Gavião
 */
public interface RuntimeController extends KernelManageableService {

    Set<Capability> getCapabilities();

}
