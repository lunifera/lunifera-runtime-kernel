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
package org.lunifera.runtime.kernel.spi.extenders.handlers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

import java.net.URL;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lunifera.runtime.kernel.spi.extenders.ContributionItemResourceType;
import org.lunifera.runtime.kernel.spi.extenders.ContributorBundleTrackerObject;
import org.lunifera.runtime.kernel.spi.extenders.ContributorBundleTrackerObject.ContributionItem;
import org.lunifera.runtime.kernel.spi.extenders.ExceptionComponentExtenderSetup;
import org.lunifera.runtime.kernel.spi.extenders.ExtensionHandlingStrategy;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class ComponentContributionHandlerPropertiesFileResourcePerItemWithConfigAdminTest {

    static class ConcreteHandler extends
            AbstractComponentFactoryContributionHandlerPropertiesFileResourcePerItem {

        public ConcreteHandler() {
            super();
        }

        public ConcreteHandler(
                ContributionItemResourceType contributionItemResourceType,
                ExtensionHandlingStrategy extensionHandlingStrategy) {
            super(contributionItemResourceType, extensionHandlingStrategy);
        }

    }

    ConcreteHandler handler;

    @Before
    public void setup() {
        handler = new ConcreteHandler();
    }

    @Test
    public void testCreatePropertiesMapFromHeaderValues() {
        Map<String, String> actual = handler
                .createPropertiesMapFromHeaderValues("keyone=value1,keytwo=value2");

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("keyone", "value1");
        expected.put("keytwo", "value2");

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void testExtractMapFromPropertiesResourceFile() throws Exception {
        URL url = this.getClass().getResource("/test.config");
        Map<String, String> actual = handler
                .extractMapFromPropertiesResourceFile(url);

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("keyone", "value1");
        expected.put("keytwo", "value2");

        assertThat(actual, equalTo(expected));
    }

    @Test(expected = ExceptionComponentExtenderSetup.class)
    public void testFailExtractMapFromPropertiesResourceFile() throws Exception {
        URL url = new URL("http://lunifera.org/test.config");
        handler.extractMapFromPropertiesResourceFile(url);
    }

    @Test
    public void testIsValidForeignURL() throws Exception {
        String url1 = "http://lunifera.org/test.conf";
        String url2 = "https://lunifera.org/test.conf";
        String url3 = "ftp://lunifera.org/test.conf";
        String url4 = "lunifera.org/test.conf";
        String url5 = "ftp://lunifera.org\test.conf";

        assertThat(handler.isValidForeignURL(url1), is(true));
        assertThat(handler.isValidForeignURL(url2), is(true));
        assertThat(handler.isValidForeignURL(url3), is(true));
        assertThat(handler.isValidForeignURL(url4), is(false));
        assertThat(handler.isValidForeignURL(url5), is(false));
    }

    @Test(expected = ExceptionComponentExtenderSetup.class)
    public void testNullUrlFailExtractMapFromPropertiesResourceFile()
            throws Exception {
        URL url = this.getClass().getResource("/test");
        handler.extractMapFromPropertiesResourceFile(url);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testOnContributorBundleAddition_PROPERTIES_FILE_RESOURCE_PER_HEADER_ITEM()
            throws Exception {
        Bundle mockBundle = mock(Bundle.class);
        ContributorBundleTrackerObject contributorBundleTrackerObject;
        ConcreteHandler spyHandler = spy(new ConcreteHandler(
                ContributionItemResourceType.PROPERTIES_FILE_RESOURCE,
                ExtensionHandlingStrategy.PER_ITEM));

        URL url = this.getClass().getResource("/test.config");
        assertNotNull(url);

        when(mockBundle.getHeaders()).thenReturn(mock(Dictionary.class));
        when(mockBundle.getHeaders().get("My_Header")).thenReturn(
                url.toString() + ",test.config");
        when(mockBundle.getSymbolicName()).thenReturn("my.bundle");
        when(mockBundle.getVersion()).thenReturn(new Version("1.0.0"));
        doReturn(url).when(mockBundle).getEntry("test.config");

        List<String> foundInBundle = Lists.newArrayList("test.config");
        
        doReturn(foundInBundle).when(spyHandler).discoverResourcesInBundle(mockBundle, "/test.config");
        contributorBundleTrackerObject = spyHandler
                .onContributorBundleAddition(mockBundle, "My_Header",
                        "/test.config");

        assertNotNull(contributorBundleTrackerObject);
        assertThat(contributorBundleTrackerObject.getContributionItemList(),
                hasSize(1));
        ContributionItem item0 = contributorBundleTrackerObject
                .getContributionItemList().get(0);
        assertThat(item0.getPropertiesMap().keySet(), hasSize(5));
        ContributionItem item1 = contributorBundleTrackerObject
                .getContributionItemList().get(0);
        assertThat(item1.getPropertiesMap().keySet(), hasSize(5));
    }
}