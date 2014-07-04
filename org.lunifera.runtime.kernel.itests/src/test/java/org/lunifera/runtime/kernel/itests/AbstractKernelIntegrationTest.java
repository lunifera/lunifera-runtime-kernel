package org.lunifera.runtime.kernel.itests;

import static org.knowhowlab.osgi.testing.assertions.BundleAssert.assertBundleAvailable;
import static org.knowhowlab.osgi.testing.assertions.ServiceAssert.assertServiceAvailable;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.knowhowlab.osgi.testing.assertions.BundleAssert;
import org.knowhowlab.osgi.testing.assertions.OSGiAssert;
import org.knowhowlab.osgi.testing.assertions.ServiceAssert;
import org.knowhowlab.osgi.testing.assertions.cmpn.ConfigurationAdminAssert;
import org.lunifera.runtime.kernel.itests.samples.AnComponentFactory;
import org.lunifera.runtime.utils.paxexam.junit.AbstractPaxexamIntegrationTestClass;
import org.ops4j.pax.exam.ProbeBuilder;
import org.ops4j.pax.exam.TestProbeBuilder;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.Version;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.coordinator.Coordinator;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.prefs.PreferencesService;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public abstract class AbstractKernelIntegrationTest extends
		AbstractPaxexamIntegrationTestClass {

	public void ensureCompendiumServicesAreFunctional()
			throws InvalidSyntaxException {
		assertServiceAvailable(EventAdmin.class);
		assertServiceAvailable(PreferencesService.class, 1, TimeUnit.SECONDS);
		assertServiceAvailable(ConfigurationAdmin.class);
		assertServiceAvailable(Coordinator.class);
	}


	public void ensureLuniferaBundlesAreAvailable() {
		assertBundleAvailable("org.knowhowlab.osgi.testing.utils", new Version(
				"1.2.2"));
		assertBundleAvailable("org.lunifera.runtime.kernel.api");
		assertBundleAvailable("org.lunifera.runtime.kernel.spi");
		assertBundleAvailable("org.lunifera.runtime.kernel.controllers");
		assertBundleAvailable("org.lunifera.runtime.utils.osgi");
		assertBundleAvailable("slf4j.api");
	}

	@Before
	public void init() throws Exception {
		OSGiAssert.setDefaultBundleContext(bc);
		ServiceAssert.setDefaultBundleContext(bc);
		BundleAssert.setDefaultBundleContext(bc);
		ConfigurationAdminAssert.setDefaultBundleContext(bc);

	//	ensureLuniferaBundlesAreAvailable();
	//	ensureCompendiumServicesAreFunctional();
	}

	@ProbeBuilder
	public TestProbeBuilder probeConfiguration(TestProbeBuilder probe) {
		probe.setHeader(Constants.BUNDLE_SYMBOLICNAME, "ITEST.KERNEL.BUNDLE");
		probe.ignorePackageOf(AnComponentFactory.class);
		return probe;
	}
}
