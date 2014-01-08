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
 * @author Cristiano Gavião
 * 
 */
public class AbstractRecoverableException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -5008320674580057079L;

    /**
     * 
     */
    public AbstractRecoverableException() {
    }

    /**
     * @param message
     */
    public AbstractRecoverableException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public AbstractRecoverableException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public AbstractRecoverableException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public AbstractRecoverableException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
