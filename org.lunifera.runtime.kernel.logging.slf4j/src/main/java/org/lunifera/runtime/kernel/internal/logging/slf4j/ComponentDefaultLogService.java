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
package org.lunifera.runtime.kernel.internal.logging.slf4j;

import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

@SuppressWarnings("rawtypes")
@Component(enabled = true, servicefactory = true,
        property = { "service.ranking:Integer=2147483647" })
public class ComponentDefaultLogService implements LogService {

    private Logger logger = null;
    private ILoggerFactory loggerFactory = null;

    @Activate
    void activate(ComponentContext ctx) {
        String loggerName = "noname";
        if (ctx.getUsingBundle() != null) {
            loggerName = ctx.getUsingBundle().getSymbolicName();
        }
        logger = loggerFactory.getLogger(loggerName);
    }

    @Reference()
    protected void bindILoggerFactory(ILoggerFactory loggerFactory) {
        this.loggerFactory = loggerFactory;
    }

    private void callSlf4jLog(int level, String message, Throwable e) {
        switch (level) {
        case LogService.LOG_DEBUG:
            logger.debug(message, e);
            break;
        case LogService.LOG_ERROR:
            logger.error(message, e);
            break;
        case LogService.LOG_WARNING:
            logger.warn(message, e);
            break;
        default:
            logger.info(message, e);
            break;
        }
    }

    @Deactivate
    void deactivate(ComponentContext ctx) {

    }

    @Override
    public void log(int level, String message) {
        callSlf4jLog(level, message, null);
    }

    @Override
    public void log(int level, String message, Throwable exception) {
        callSlf4jLog(level, message, exception);
    }

    @Override
    public void log(ServiceReference sr, int level, String message) {
        if (sr != null) {
            callSlf4jLog(level, "[" + sr.toString() + "]" + message, null);
        } else {
            callSlf4jLog(level, message, null);
        }

    }

    @Override
    public void log(ServiceReference sr, int level, String message,
            Throwable exception) {
        if (sr != null) {
            callSlf4jLog(level, "[" + sr.toString() + "]" + message, exception);
        } else {
            callSlf4jLog(level, message, exception);
        }

    }

    protected void unbindILoggerFactory(ILoggerFactory loggerFactory) {
        if (this.loggerFactory == loggerFactory) {
            this.loggerFactory = null;
        }
    }
}
