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
package org.lunifera.runtime.kernel.internal.configuration;

import static org.osgi.framework.Constants.SERVICE_PID;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.lunifera.runtime.kernel.api.configuration.LuniferaFrameworkConfigurationManagementService;
import org.lunifera.runtime.kernel.spi.configuration.ManagementServiceFrameworkConfiguration;
import org.lunifera.runtime.kernel.spi.controllers.AbstractComponentKernelController;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(immediate = true)
public class ComponentControllerFrameworkConfigurationManagement extends
        AbstractComponentKernelController implements
        LuniferaFrameworkConfigurationManagementService,
        ManagementServiceFrameworkConfiguration {

    private ConfigurationAdmin configurationAdmin;

    @Reference(policy = ReferencePolicy.STATIC)
    protected void bindConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = configurationAdmin;
        trace("Bound ConfigurationAdmin Service.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #deleteFactoryProperties(java.lang.String, java.lang.String)
     */
    @Override
    public void deleteFactoryProperties(String factoryPid, String pid) {

        Configuration configuration;
        try {
            configuration = findFactoryConfiguration(factoryPid, pid);
            if (configuration != null) {
                try {
                    configuration.delete();
                } catch (IOException e) {
                    error("Error on setup Configuration Service", e);
                }
            } else {
                debug("no configuration for factoryPid '" + factoryPid
                        + "' and pid '" + pid + "'");
            }
        } catch (IOException e1) {
            error("no configuration for factoryPid '" + factoryPid
                    + "' and pid '" + pid + "'", e1);
        }

    }

    private Configuration findConfiguration(String pid) throws IOException {
        // As ConfigurationAdmin.getConfiguration creates the configuration if
        // it is not yet there, we check its existence first
        try {
            Configuration[] configurations = getConfigurationAdminService()
                    .listConfigurations("(service.pid=" + pid + ")");
            if (configurations != null && configurations.length > 0) {
                return configurations[0];
            }
        } catch (InvalidSyntaxException e) {
            error("Error in the pid filter...", e);
        }

        return null;
    }

    private Configuration findFactoryConfiguration(String factoryPid, String pid)
            throws IOException {
        // As ConfigurationAdmin.getConfiguration creates the configuration if
        // it is not yet there, we check its existence first
        try {
            String filter = "";
            if (pid != null && !pid.isEmpty()) {
                filter = "(&(service.factoryPid=" + factoryPid
                        + ") (service.pid=" + pid + "))";
            } else {
                filter = "(service.factoryPid=" + factoryPid + ")";
            }

            Configuration[] configurations = getConfigurationAdminService()
                    .listConfigurations(filter);
            if (configurations != null && configurations.length > 0) {
                return configurations[0];
            }
        } catch (InvalidSyntaxException e) {
            error("Error in the pid filter...", e);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #deleteProperties(java.lang.String)
     */
    @Override
    public void deleteProperties(String pid) {
        Configuration configuration;
        try {
            configuration = findConfiguration(pid);
            if (configuration != null) {
                try {
                    configuration.delete();
                } catch (IOException e) {
                    error("Error on setup Configuration Service", e);
                }
            } else {
                debug("no configuration for pid '" + pid + "'");
            }
        } catch (IOException e1) {
            error("no configuration for pid '" + pid + "'", e1);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lunifera.runtime.kernel.api.configuration.
     * LuniferaFrameworkConfigurationManagementService
     * #displayConfiguration(java.lang.String)
     */
    @Override
    public String displayConfiguration(String pid) {
        StringBuilder builder = new StringBuilder();

        return builder.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #displayFactoryConfiguration(java.lang.String)
     */
    @Override
    public String displayFactoryConfiguration(String factoryPid) {
        StringBuilder builder = new StringBuilder();

        try {
            String filter = "";
            if (factoryPid == null || factoryPid.isEmpty()) {
                filter = "(service.factoryPid=*)";
            } else {
                filter = "(service.factoryPid=" + factoryPid + ")";
            }

            Configuration[] configurations = getConfigurationAdminService()
                    .listConfigurations(filter);
            if (configurations != null && configurations.length > 0) {
                for (int i = 0; i < configurations.length; i++) {
                    builder.append("  * Factory PID= '" + factoryPid
                            + "', PID = '" + configurations[i].getPid()
                            + "', Location= '"
                            + configurations[i].getBundleLocation() + "'");
                    builder.append("Properties:"
                            + configurations[i].getProperties().toString());
                }
            }
        } catch (Exception e) {
            error("Error displaying Configuration for '" + factoryPid + "'.", e);
        }
        return builder.toString();
    }

    protected ConfigurationAdmin getConfigurationAdminService() {
        return configurationAdmin;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #getFactoryProperties(java.lang.String, java.lang.String)
     */
    @Override
    public Dictionary<String, Object> getFactoryProperties(String factoryPid,
            String pid) {
        try {
            Dictionary<String, Object> allProperties = new Hashtable<String, Object>();
            String filter = "";
            if (pid != null && !pid.isEmpty()) {
                filter = "(&(service.factoryPid=" + factoryPid
                        + ") (service.pid=" + pid + "))";
            } else {
                filter = "(service.factoryPid=" + factoryPid + ")";
            }

            Configuration[] configurations = getConfigurationAdminService()
                    .listConfigurations(filter);
            if (configurations != null && configurations.length > 0) {
                for (Configuration configuration : configurations) {
                    Enumeration<String> keys = configuration.getProperties()
                            .keys();
                    while (keys.hasMoreElements()) {
                        String object = keys.nextElement();
                        String value = (String) configuration.getProperties()
                                .get(object);
                        allProperties.put(object, value);
                    }
                }
                return allProperties;
            }
        } catch (InvalidSyntaxException e) {
            error("Error on setup Configuration Service", e);
        } catch (IOException e) {
            error("Error on setup Configuration Service", e);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #getFactoryProperty(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String getFactoryProperty(String factoryPid, String pid,
            String propertyName) {
        Configuration configuration;
        try {
            configuration = findFactoryConfiguration(factoryPid, pid);
            if (configuration != null) {
                return (String) configuration.getProperties().get(propertyName);
            } else {
                warn("no configuration for FactoryPID: '"
                        + factoryPid
                        + "' and PID: '"
                        + pid
                        + "' (use 'initializeFactoryConfigurationStore' to create one)");
            }
        } catch (IOException e) {
            error("Error on setup Configuration Service", e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #getProperties(java.lang.String)
     */
    @Override
    public Dictionary<String, Object> getProperties(String pid) {
        try {
            Dictionary<String, Object> allProperties = new Hashtable<String, Object>();
            Configuration[] configurations = getConfigurationAdminService()
                    .listConfigurations("(service.pid=" + pid + ")");
            if (configurations != null && configurations.length > 0) {
                for (Configuration configuration : configurations) {
                    Enumeration<String> keys = configuration.getProperties()
                            .keys();
                    while (keys.hasMoreElements()) {
                        String object = keys.nextElement();
                        String value = (String) configuration.getProperties()
                                .get(object);
                        allProperties.put(object, value);
                    }
                }
                return allProperties;
            }
        } catch (InvalidSyntaxException e) {
            error("Error on setup Configuration Service", e);
        } catch (IOException e) {
            error("Error on setup Configuration Service", e);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #getProperty(java.lang.String, java.lang.String)
     */
    @Override
    public String getProperty(String pid, String propertyName) {
        Configuration configuration;
        try {
            configuration = findConfiguration(pid);
            if (configuration != null) {
                return (String) configuration.getProperties().get(propertyName);
            } else {
                warn("no configuration for pid '"
                        + pid
                        + "' (use 'initializeFactoryConfigurationStore' to create one)");
            }
        } catch (IOException e) {
            error("Error on setup Configuration Service", e);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #initializeConfigurationStore(java.lang.String, java.util.Dictionary)
     */
    @Override
    public void initializeConfigurationStore(String pid,
            Dictionary<String, Object> properties) {
        Configuration configuration;
        Dictionary<String, Object> rproperties;
        try {
            configuration = configurationAdmin.getConfiguration(pid, null);
            // Ensure update is called, when properties are null; otherwise
            // configuration will not
            // be returned when listConfigurations is called (see specification
            // 104.15.3.7)
            if (configuration.getProperties() == null) {
                if (properties == null) {
                    rproperties = new Hashtable<String, Object>();
                } else {
                    rproperties = properties;
                }
                configuration.update(rproperties);
                debug("Initialized store under PID: '" + pid
                        + "', with this properties: " + rproperties.toString());
            } else {
                warn("Configuration for PID was already initialized: ' " + pid
                        + "'.");
            }
        } catch (IOException e) {
            error("Error on setup Configuration Service", e);
        }
    }

    /**
     * Returns the configuration for the given externalPid or <code>null</code>
     * if no configuration could be found.
     * 
     * @param factoryPid
     * @param externalPid
     * @return
     * @throws IOException
     */
    protected Configuration findFactoryConfigurationForExternalPid(
            String factoryPid, String externalPid) throws IOException {
        try {
            String filter = "";
            if (externalPid != null && !externalPid.isEmpty()) {
                filter = "(&(service.factoryPid=" + factoryPid + ") ("
                        + EXTERNAL_PID + "=" + externalPid + "))";
            } else {
                filter = "(service.factoryPid=" + factoryPid + ")";
            }

            Configuration[] configurations = getConfigurationAdminService()
                    .listConfigurations(filter);
            if (configurations != null && configurations.length > 0) {
                return configurations[0];
            }
        } catch (InvalidSyntaxException e) {
            error("Error in the factory Pid filter...", e);
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #initializeFactoryConfigurationStore(java.lang.String, java.lang.String,
     * java.util.Dictionary)
     */
    @Override
    public String initializeFactoryConfigurationStore(String factoryPid,
            String externalPid, Dictionary<String, Object> properties) {
        Configuration configuration;
        Dictionary<String, Object> rproperties;
        String pid = null;
        try {

            // try to find an existing configuration
            configuration = findFactoryConfigurationForExternalPid(factoryPid,
                    externalPid);
            if (configuration == null) {
                // create a new configuration
                configuration = configurationAdmin.createFactoryConfiguration(
                        factoryPid, null);
            }
            pid = configuration.getPid();

            if (properties == null) {
                rproperties = new Hashtable<String, Object>();
            } else {
                rproperties = properties;
            }

            // add the externalPid as a property
            if (externalPid != null && !externalPid.isEmpty()) {
                rproperties.put(EXTERNAL_PID, externalPid);
            }
            configuration.update(rproperties);

            // / just for test
            // displayFactoryConfiguration(factoryPid);

            debug("Initialized store under FactoryPID: '" + factoryPid
                    + "' and PID: '" + configuration.getPid()
                    + "' , with this properties: " + rproperties.toString());

        } catch (IOException e) {
            error("Error on setup Configuration Service", e);
        }
        return pid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #putFactoryProperties(java.lang.String, java.lang.String,
     * java.util.Dictionary)
     */
    @Override
    public void putFactoryProperties(String factoryPid, String pid,
            Dictionary<String, Object> properties) {
        Configuration config;
        try {
            config = findFactoryConfiguration(factoryPid, pid);
            if (config == null) {
                error("no configuration for FactoryPID: '"
                        + factoryPid
                        + "' and PID: '"
                        + pid
                        + "' (use 'initializeFactoryConfigurationStore' to create one)");
                return;
            }

            if (properties != null) {
                properties.put(SERVICE_FACTORYPID, factoryPid);
                properties.put(SERVICE_PID, pid);
                config.update(properties);
            }
        } catch (IOException e) {
            error("Error on setup Configuration Service", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #putFactoryProperty(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.Object)
     */
    @Override
    public void putFactoryProperty(String factoryPid, String pid,
            String propertyName, Object value) {
        Configuration config;
        try {
            config = findFactoryConfiguration(factoryPid, pid);
            if (config == null) {
                error("no configuration for for FactoryPID: '"
                        + factoryPid
                        + "' and PID: '"
                        + pid
                        + "' (use 'initializeFactoryConfigurationStore' to create one)");
                return;
            }
            if (value != null) {
                Dictionary<String, Object> properties = config.getProperties();
                if (properties == null) {
                    properties = new Hashtable<String, Object>();
                }
                properties.put(propertyName, value);
                properties.put(SERVICE_FACTORYPID, factoryPid);
                properties.put(SERVICE_PID, pid);
                config.update(properties);
            }
        } catch (IOException e) {
            error("no configuration for pid '"
                    + pid
                    + "' (use 'initializeFactoryConfigurationStore' to create one)");

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #putProperties(java.lang.String, java.util.Dictionary)
     */
    @Override
    public void putProperties(String pid, Dictionary<String, Object> properties) {
        Configuration config;
        try {
            config = findConfiguration(pid);
            if (config == null) {
                error("no configuration for pid '"
                        + pid
                        + "' (use 'initializeConfigurationStore' to create one)");
                return;
            }

            if (properties != null) {

                config.update(properties);
            }
        } catch (IOException e) {
            error("Error on setup Configuration Service", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.lunifera.runtime.kernel.api.configurations.ConfigurationManagementService
     * #putProperty(java.lang.String, java.lang.String, java.lang.Object)
     */
    @Override
    public void putProperty(String pid, String propertyName, Object value) {
        Configuration config;
        try {
            config = findConfiguration(pid);
            if (config == null) {
                error("no configuration for pid '"
                        + pid
                        + "' (use 'initializeConfigurationStore' to create one)");
                return;
            }
            if (value != null) {
                Dictionary<String, Object> properties = config.getProperties();
                if (properties == null){
                    properties = new Hashtable<String, Object>();
                }
                properties.put(propertyName, value);
                config.update(properties);
            }
        } catch (IOException e) {
            error("no configuration for pid '" + pid
                    + "' (use 'initializeConfigurationStore' to create one)");

        }

    }

    protected void unbindConfigurationAdmin(
            ConfigurationAdmin configurationAdmin) {
        if (this.configurationAdmin == configurationAdmin){
            this.configurationAdmin = null;
        }
        trace("Unbound ConfigurationAdminService");
    }

}
