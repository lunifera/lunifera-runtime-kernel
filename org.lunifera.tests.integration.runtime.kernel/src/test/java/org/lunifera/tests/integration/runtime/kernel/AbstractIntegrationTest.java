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
package org.lunifera.tests.integration.runtime.kernel;

import static org.knowhowlab.osgi.testing.assertions.BundleAssert.assertBundleAvailable;
import static org.knowhowlab.osgi.testing.assertions.BundleAssert.assertBundleState;
import static org.knowhowlab.osgi.testing.assertions.ServiceAssert.assertServiceAvailable;
import static org.knowhowlab.osgi.testing.utils.FilterUtils.and;
import static org.knowhowlab.osgi.testing.utils.FilterUtils.create;
import static org.knowhowlab.osgi.testing.utils.FilterUtils.eq;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.cleanCaches;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.url;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.OptionUtils.combine;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.knowhowlab.osgi.testing.assertions.BundleAssert;
import org.knowhowlab.osgi.testing.assertions.OSGiAssert;
import org.knowhowlab.osgi.testing.assertions.ServiceAssert;
import org.knowhowlab.osgi.testing.assertions.cmpn.ConfigurationAdminAssert;
import org.knowhowlab.osgi.testing.utils.FilterUtils;
import org.lunifera.runtime.kernel.api.logging.LoggingManagementService;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.PathUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.Version;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.coordinator.Coordinator;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.prefs.PreferencesService;
import org.slf4j.Logger;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public abstract class AbstractIntegrationTest {

    // the name of the system property which captures the jacoco coverage agent
    // command
    // if specified then agent would be specified otherwise ignored
    protected static final String COVERAGE_COMMAND = "coverage.command";

    private static Option addCodeCoverageOption() {
        String coverageCommand = System.getProperty(COVERAGE_COMMAND);
        if (coverageCommand != null) {
            return CoreOptions.vmOption(coverageCommand);
        }
        return null;
    }

    /**
     * Runner config
     * 
     * @return config
     */
    protected static Option[] baseConfiguration(Option... extraOptions) {
        Option[] options = options(
                 cleanCaches(),

                when(isEquinox()).useOptions(
                        mavenBundle("org.lunifera.osgi",
                                "org.eclipse.osgi.services",
                                "3.4.0.v20131120-1328").startLevel(1),
                        mavenBundle("org.lunifera.osgi",
                                "org.eclipse.equinox.ds",
                                "1.4.200.v20131126-2331").startLevel(2),
                        mavenBundle("org.lunifera.osgi",
                                "org.eclipse.equinox.cm",
                                "1.1.0.v20131021-1936").startLevel(2),
                        mavenBundle("org.lunifera.osgi",
                                "org.eclipse.equinox.common",
                                "3.6.200.v20130402-1505"),
                        mavenBundle("org.lunifera.osgi",
                                "org.eclipse.equinox.coordinator",
                                "1.3.0.v20131122-1810"),
                        mavenBundle("org.lunifera.osgi",
                                "org.eclipse.equinox.util",
                                "1.0.500.v20130404-1337"),
                        mavenBundle("org.lunifera.osgi",
                                "org.eclipse.equinox.preferences",
                                "3.5.100.v20130422-1538").startLevel(3),
                        mavenBundle("org.lunifera.osgi",
                                "org.eclipse.equinox.event",
                                "1.3.100.v20131021-1935").startLevel(2)),
                when(isFelix()).useOptions(
                        mavenBundle("org.apache.felix",
                                "org.apache.felix.configadmin", "1.8.0")
                                .startLevel(2),
                        mavenBundle("org.apache.felix",
                                "org.apache.felix.eventadmin", "1.3.2")
                                .startLevel(2),
                        mavenBundle("org.apache.felix",
                                "org.apache.felix.prefs", "1.0.6")
                                .startLevel(2),
                        mavenBundle("org.apache.felix", "org.apache.felix.scr")
                                .startLevel(2)),
                junitBundles(),
                addCodeCoverageOption(),
                systemProperty("lunifera.logging.level=debug").value("trace"),
                mavenBundle("org.knowhowlab.osgi",
                        "org.knowhowlab.osgi.testing.utils", "1.2.2"),
                mavenBundle("org.knowhowlab.osgi",
                        "org.knowhowlab.osgi.testing.assertions", "1.2.2"),
                mavenBundle("org.apache.felix",
                        "org.apache.felix.gogo.runtime", "0.10.0"),
                mavenBundle("org.apache.shiro", "shiro-core", "1.2.2"),
                mavenBundle("com.google.guava", "guava", "15.0"),
                mavenBundle("ch.qos.logback", "logback-core", "1.0.13"),
                mavenBundle("ch.qos.logback", "logback-classic", "1.0.13"),
                mavenBundle("org.slf4j", "slf4j-api", "1.7.5"),
                mavenBundle("commons-lang", "commons-lang", "2.6"),
                url("reference:file:"
                        + PathUtils.getBaseDir()
                        + "/../org.lunifera.tests.integration.samples.bundle.extendee/target/classes"),
                url("reference:file:"
                        + PathUtils.getBaseDir()
                        + "/../org.lunifera.runtime.kernel.library/target/classes"),
                url("reference:file:"
                        + PathUtils.getBaseDir()
                        + "/../org.lunifera.runtime.kernel.logging.slf4j/target/classes"),
                url("reference:file:"
                        + PathUtils.getBaseDir()
                        + "/../org.lunifera.runtime.kernel.security.shiro.core/target/classes"),
                url("reference:file:"
                        + PathUtils.getBaseDir()
                        + "/../org.lunifera.runtime.kernel.controller.configurations/target/classes"));
        if (extraOptions != null) {
            options = combine(options, extraOptions);
        }

        return options;
    }

    public static boolean isEquinox() {
        return "equinox".equals(System.getProperty("pax.exam.framework"));
    }

    public static boolean isFelix() {
        return "felix".equals(System.getProperty("pax.exam.framework"));
    }

    /**
     * Injected BundleContext
     */
    @Inject
    protected BundleContext bc;

    @Before
    public void init() throws Exception {
        OSGiAssert.setDefaultBundleContext(bc);
        ServiceAssert.setDefaultBundleContext(bc);
        BundleAssert.setDefaultBundleContext(bc);
        ConfigurationAdminAssert.setDefaultBundleContext(bc);
        
        ensureLuniferaBundlesAreAvailable();
        ensureLuniferaBundlesAreActive();
        ensureCompendiumServicesAreFunctional();
    }
    
    public void ensureLuniferaBundlesAreActive() {

        assertBundleState(Bundle.ACTIVE, 1);
        assertBundleState(Bundle.ACTIVE, "org.knowhowlab.osgi.testing.utils",
                5, TimeUnit.SECONDS);
        assertBundleState(Bundle.ACTIVE, "org.lunifera.runtime.kernel.library");
        assertBundleState(Bundle.ACTIVE,
                "org.lunifera.runtime.kernel.controller.configurations");
        assertBundleState(Bundle.ACTIVE,
                "org.lunifera.runtime.kernel.logging.slf4j");
        assertBundleState(Bundle.ACTIVE,
                "org.lunifera.runtime.kernel.security.shiro.core");
        assertBundleState(Bundle.ACTIVE,
                "org.lunifera.tests.integration.samples.bundle.extendee");

    }

    public void ensureLuniferaBundlesAreAvailable() {
        assertBundleAvailable("org.knowhowlab.osgi.testing.utils", new Version(
                "1.2.2"));
        assertBundleAvailable("org.lunifera.runtime.kernel.library");
        assertBundleAvailable("org.lunifera.runtime.kernel.controller.configurations");
        assertBundleAvailable("org.lunifera.runtime.kernel.logging.slf4j");
        assertBundleAvailable("org.lunifera.runtime.kernel.security.shiro.core");
        assertBundleAvailable("slf4j.api");
        assertBundleAvailable("org.apache.shiro.core");
        assertBundleAvailable("org.lunifera.tests.integration.samples.bundle.extendee");

    }

    public void ensureCompendiumServicesAreFunctional() throws InvalidSyntaxException {
        assertServiceAvailable(EventAdmin.class);
        assertServiceAvailable(PreferencesService.class, 1, TimeUnit.SECONDS);
        assertServiceAvailable(ConfigurationAdmin.class);
        assertServiceAvailable(Coordinator.class);
        assertServiceAvailable(Logger.class);
        assertServiceAvailable(
                FilterUtils.create(org.slf4j.ILoggerFactory.class), 1,
                TimeUnit.SECONDS);

        // asserts that test service with custom properties is available
        assertServiceAvailable(and(
                create(LoggingManagementService.class),
                eq("component.name",
                        "org.lunifera.runtime.kernel.internal.logging.slf4j.ComponentFactorySlf4jLogger")));
    }
}
