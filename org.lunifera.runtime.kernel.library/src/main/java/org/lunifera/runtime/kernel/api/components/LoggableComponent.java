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

import org.slf4j.Logger;

public interface LoggableComponent {

    public void debug(String msg);

    public void debug(String format, Object... arguments);

    public void error(String msg);

    public void error(String format, Object... arguments);

    public void error(String format, Throwable throwable);

    public Logger getLoggerService();

    public void info(String msg);

    public void info(String format, Object... arguments);

    public void trace(String msg);

    public void trace(String format, Object... arguments);

    public void warn(String msg);

    public void warn(String format, Object... arguments);

}
