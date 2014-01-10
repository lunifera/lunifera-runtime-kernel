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
package org.lunifera.runtime.kernel.internal.logging;

import org.lunifera.runtime.kernel.api.logging.LuniferaFrameworkLoggingManagementService;
import org.lunifera.runtime.kernel.spi.controllers.AbstractComponentKernelController;
import org.lunifera.runtime.kernel.spi.logging.ManagementServiceFrameworkLogging;
import org.osgi.service.component.annotations.Component;

@Component(enabled = true, immediate = true)
public class ComponentControllerFrameworkLoggingManagement extends
        AbstractComponentKernelController implements
        ManagementServiceFrameworkLogging, LuniferaFrameworkLoggingManagementService {

    /* (non-Javadoc)
     * @see org.lunifera.runtime.kernel.spi.logging.ManagementServiceFrameworkLogging#setLogLevel(java.lang.String, java.lang.String)
     */
    @Override
    public void setLogLevel(String logName, String level) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see org.lunifera.runtime.kernel.spi.logging.ManagementServiceFrameworkLogging#setRootLogLevel(java.lang.String)
     */
    @Override
    public void setRootLogLevel(String level) {
        // TODO Auto-generated method stub
        
    }

}
