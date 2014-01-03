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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(MockitoJUnitRunner.class)
public class ComponentBasicLifecycleTest {

    static class AnComponent extends AbstractComponentBasic {

        public AnComponent() {
            super();
        }

        public AnComponent(ComponentContext componentContext) {
            super(componentContext);
        }
    }

    static Logger logger = LoggerFactory
            .getLogger(ComponentBasicLifecycleTest.class);

    @Mock
    ComponentContext componentContext;

    AnComponent concreteComponent;

    @Test
    public void ensureComponentLifecycleIsOk() throws Exception {

        ensureComponentWasActivatedProperly();
        ensureComponentWasModifiedProperly();
        ensureComponentWasDeactivateProperly();
    }

    public void ensureComponentWasActivatedProperly() throws Exception {
        assertThat(concreteComponent, notNullValue());
        assertThat(concreteComponent.getComponentContext(), notNullValue());

        concreteComponent.activate(componentContext);
        assertThat(concreteComponent.getId(), equalTo(121212l));
        assertThat(concreteComponent.getName(), equalTo("Mocked Component"));
        assertThat(concreteComponent.getDescription(),
                equalTo("Mocked Component Description"));
    }

    public void ensureComponentWasDeactivateProperly() throws Exception {
        concreteComponent.deactivate(componentContext);
        assertThat(concreteComponent.getComponentContext(), nullValue());
        assertThat(concreteComponent.getId(), equalTo(0L));
        assertThat(concreteComponent.getName(), equalTo(""));
        assertThat(concreteComponent.getDescription(), equalTo(""));
    }

    public void ensureComponentWasModifiedProperly() throws Exception {

        Dictionary<String, Object> dict = new Hashtable<>();
        dict.put(ComponentConstants.COMPONENT_ID, 121212l);
        dict.put(ComponentConstants.COMPONENT_NAME, "Mocked modified component");
        dict.put(AbstractComponentCompendium.COMPONENT_DESCRIPTION,
                "Mocked modified Component Description");

        when(componentContext.getProperties()).thenReturn(dict);

        concreteComponent.modified(componentContext);
        assertThat(concreteComponent.getId(), equalTo(121212l));
        assertThat(concreteComponent.getName(),
                equalTo("Mocked modified component"));
        assertThat(concreteComponent.getDescription(),
                equalTo("Mocked modified Component Description"));
    }

    private Dictionary<String, Object> getMockProperties() {
        Dictionary<String, Object> dict = new Hashtable<>();
        dict.put(ComponentConstants.COMPONENT_ID, 121212l);
        dict.put(ComponentConstants.COMPONENT_NAME, "Mocked Component");
        dict.put(AbstractComponentCompendium.COMPONENT_DESCRIPTION,
                "Mocked Component Description");

        return dict;
    }

    @Before
    public void setup() {
        concreteComponent = spy(new AnComponent(componentContext));
        when(componentContext.getProperties()).thenReturn(getMockProperties());
    }
}
