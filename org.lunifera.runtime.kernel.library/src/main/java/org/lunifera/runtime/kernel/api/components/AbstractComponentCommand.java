/**
 * 
 */
package org.lunifera.runtime.kernel.api.components;

import org.lunifera.runtime.kernel.spi.services.CommandService;
import org.osgi.service.component.ComponentContext;

/**
 * @since 0.0.1
 * @author Cristiano Gavião
 * 
 */
public class AbstractComponentCommand extends AbstractComponentCompendium
        implements CommandService {

    /**
     * DS needs a default constructor.
     */
    public AbstractComponentCommand() {
    }

    /**
     * Constructor created just to help unit testing. It is not used by OSGi.
     * 
     * @param componentContext
     */
    public AbstractComponentCommand(ComponentContext componentContext) {
        super(componentContext);
    }

}
