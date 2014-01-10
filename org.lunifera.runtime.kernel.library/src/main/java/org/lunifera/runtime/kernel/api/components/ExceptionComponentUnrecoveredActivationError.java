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

import org.lunifera.runtime.kernel.api.AbstractUnrecoverableException;

public class ExceptionComponentUnrecoveredActivationError extends AbstractUnrecoverableException {

    private static final long serialVersionUID = 4800465886577999271L;

    public ExceptionComponentUnrecoveredActivationError() {
        super();
    }

    /**
     * Construct a new ExceptionRuntimeAddon with the specified message.
     *
     * @param message
     *            The message for the exception.
     */
    public ExceptionComponentUnrecoveredActivationError(String message) {
        super(message);
    }

    /**
     * Construct a new ExceptionRuntimeAddon with the specified message and
     * cause.
     *
     * @param message
     *            The message for the exception.
     * @param cause
     *            The cause of the exception. May be {@code null}.
     */
    public ExceptionComponentUnrecoveredActivationError(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct a new ExceptionRuntimeAddon with the specified cause.
     *
     * @param cause
     *            The cause of the exception. May be {@code null}.
     */
    public ExceptionComponentUnrecoveredActivationError(Throwable cause) {
        super(cause);
    }
}
