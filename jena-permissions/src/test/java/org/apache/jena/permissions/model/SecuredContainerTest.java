/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jena.permissions.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.function.Supplier;

import org.apache.jena.permissions.MockSecurityEvaluator;
import org.apache.jena.permissions.SecurityEvaluator;
import org.apache.jena.permissions.SecurityEvaluator.Action;
import org.apache.jena.permissions.SecurityEvaluatorParameters;
import org.apache.jena.permissions.model.impl.SecuredContainerImpl;
import org.apache.jena.rdf.model.Container;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.shared.AccessDeniedException;
import org.apache.jena.shared.ReadDeniedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(value = SecurityEvaluatorParameters.class)
public class SecuredContainerTest extends SecuredResourceTest {

	public SecuredContainerTest(final MockSecurityEvaluator securityEvaluator) {
		super(securityEvaluator);
	}

	private SecuredContainer getSecuredContainer() {
		return (SecuredContainer) getSecuredRDFNode();
	}

	@Override
	@Before
	public void setup() {
		super.setup();
		final Container container = baseModel.createBag(SecuredRDFNodeTest.s.getURI());
		container.add("SomeDummyItem");
		setSecuredRDFNode(SecuredContainerImpl.getInstance(securedModel, container), container);
	}

	@Test
	public void test() {
		try {
			getSecuredContainer().size();
			if (!securityEvaluator.evaluate(Action.Read)) {
				Assert.fail("Should have thrown ReadDeniedException Exception");
			}
		} catch (final ReadDeniedException e) {
			if (securityEvaluator.evaluate(Action.Read)) {
				Assert.fail(String.format("Should not have thrown ReadDeniedException Exception: %s - %s", e,
						e.getTriple()));
			}
		}
	}

	/**
	 * @sec.graph Update
	 * @sec.triple Create SecTriple( this, RDF.li, o );
	 */
	@Test
	public void testAdd() {
		final Set<Action> perms = SecurityEvaluator.Util.asSet(new Action[] { Action.Update, Action.Create });
		try {
			getSecuredContainer().add(true);
			if (!securityEvaluator.evaluate(perms)) {
				Assert.fail("Should have thrown AccessDeniedException");
			}
		} catch (final AccessDeniedException e) {
			if (securityEvaluator.evaluate(perms)) {
				Assert.fail(String.format("Should not have thrown AccessDeniedException: %s - %s", e, e.getTriple()));
			}
		}

		try {
			getSecuredContainer().add('c');
			if (!securityEvaluator.evaluate(perms)) {
				Assert.fail("Should have thrown AccessDeniedException Exception");
			}
		} catch (final AccessDeniedException e) {
			if (securityEvaluator.evaluate(perms)) {
				Assert.fail(String.format("Should not have thrown AccessDeniedException Exception: %s - %s", e,
						e.getTriple()));
			}
		}

		try {
			getSecuredContainer().add(3.14D);
			if (!securityEvaluator.evaluate(perms)) {
				Assert.fail("Should have thrown AccessDeniedException Exception");
			}
		} catch (final AccessDeniedException e) {
			if (securityEvaluator.evaluate(perms)) {
				Assert.fail(String.format("Should not have thrown AccessDeniedException Exception: %s - %s", e,
						e.getTriple()));
			}
		}

		try {
			getSecuredContainer().add(3.14F);
			if (!securityEvaluator.evaluate(perms)) {
				Assert.fail("Should have thrown AccessDeniedException Exception");
			}
		} catch (final AccessDeniedException e) {
			if (securityEvaluator.evaluate(perms)) {
				Assert.fail(String.format("Should not have thrown AccessDeniedException Exception: %s - %s", e,
						e.getTriple()));
			}
		}

		try {
			getSecuredContainer().add(2L);
			if (!securityEvaluator.evaluate(perms)) {
				Assert.fail("Should have thrown AccessDeniedException Exception");
			}
		} catch (final AccessDeniedException e) {
			if (securityEvaluator.evaluate(perms)) {
				Assert.fail(String.format("Should not have thrown AccessDeniedException Exception: %s - %s", e,
						e.getTriple()));
			}
		}

		final Object o = Integer.valueOf("1234");
		try {
			getSecuredContainer().add(o);
			if (!securityEvaluator.evaluate(perms)) {
				Assert.fail("Should have thrown AccessDeniedException Exception");
			}
		} catch (final AccessDeniedException e) {
			if (securityEvaluator.evaluate(perms)) {
				Assert.fail(String.format("Should not have thrown AccessDeniedException Exception: %s - %s", e,
						e.getTriple()));
			}
		}

		try {
			getSecuredContainer().add(ResourceFactory.createResource("http://example.com/testResource"));
			if (!securityEvaluator.evaluate(perms)) {
				Assert.fail("Should have thrown AccessDeniedException Exception");
			}
		} catch (final AccessDeniedException e) {
			if (securityEvaluator.evaluate(perms)) {
				Assert.fail(String.format("Should not have thrown AccessDeniedException Exception: %s - %s", e,
						e.getTriple()));
			}
		}

		try {
			getSecuredContainer().add("foo");
			if (!securityEvaluator.evaluate(perms)) {
				Assert.fail("Should have thrown AccessDeniedException Exception");
			}
		} catch (final AccessDeniedException e) {
			if (securityEvaluator.evaluate(perms)) {
				Assert.fail(String.format("Should not have thrown AccessDeniedException Exception: %s - %s", e,
						e.getTriple()));
			}
		}

		try {
			getSecuredContainer().add("dos", "esp");
			if (!securityEvaluator.evaluate(perms)) {
				Assert.fail("Should have thrown AccessDeniedException Exception");
			}
		} catch (final AccessDeniedException e) {
			if (securityEvaluator.evaluate(perms)) {
				Assert.fail(String.format("Should not have thrown AccessDeniedException Exception: %s - %s", e,
						e.getTriple()));
			}
		}

	}
	
	private void testContains( Supplier<Boolean> supplier, boolean expected)
	{
		try {
			boolean actual = supplier.get();
			if (!shouldRead()) {
				Assert.fail("Should have thrown ReadDeniedException Exception");
			}
			if (securityEvaluator.evaluate(Action.Read))
			{
				assertEquals( expected, actual);
			} else {
				assertEquals( false, actual );
			}
		} catch (final ReadDeniedException e) {
			if (shouldRead()) {
				Assert.fail(String.format("Should not have thrown ReadDeniedException Exception: %s - %s", e,
						e.getTriple()));
			}
		}
		
	}

	@Test
	public void testContains() {
		final Object o = Integer.valueOf("1234");
		final Resource r = ResourceFactory.createResource("http://example.com/testResource");
		testContains( ()->getSecuredContainer().contains(true), false );
		testContains( ()->getSecuredContainer().contains('c'), false );
		testContains( ()->getSecuredContainer().contains(3.14D), false );
		testContains( ()->getSecuredContainer().contains(3.14F), false );
		testContains( ()->getSecuredContainer().contains(2L), false);
		testContains( ()->getSecuredContainer().contains(o), false );
		testContains( ()->getSecuredContainer().contains(r), false );
		testContains( ()->getSecuredContainer().contains("foo"), false );
		testContains( ()->getSecuredContainer().contains("dos", "esp"), false );
		
		Container container = (Container) getBaseRDFNode();
		container.add( true );
		container.add('c');
		container.add(3.14D);
		container.add(3.14F);
		container.add(2L);
		container.add(o);
		container.add(r);
		container.add("foo");
		container.add("dos", "esp");

		testContains( ()->getSecuredContainer().contains(true), true );
		testContains( ()->getSecuredContainer().contains('c'), true );
		testContains( ()->getSecuredContainer().contains(3.14D), true );
		testContains( ()->getSecuredContainer().contains(3.14F), true );
		testContains( ()->getSecuredContainer().contains(2L), true);
		testContains( ()->getSecuredContainer().contains(o), true );
		testContains( ()->getSecuredContainer().contains(r), true );
		testContains( ()->getSecuredContainer().contains("foo"), true );
		testContains( ()->getSecuredContainer().contains("dos", "esp"), true );
	}

	@Test
	public void testIterator() {
		try {
			NodeIterator iter = getSecuredContainer().iterator();
			if (!shouldRead()) {
				Assert.fail("Should have thrown ReadDeniedException Exception");
			}
			if (securityEvaluator.evaluate(Action.Read))
			{
				assertTrue( iter.hasNext());
			} else {
				assertFalse( iter.hasNext() );
			}
		} catch (final ReadDeniedException e) {
			if (shouldRead()) {
				Assert.fail(String.format("Should not have thrown ReadDeniedException Exception: %s - %s", e,
						e.getTriple()));
			}
		}
	}

	@Test
	public void testRemove() {
		final Set<Action> perms = SecurityEvaluator.Util.asSet(new Action[] { Action.Update, Action.Delete });
		final Statement s = baseModel.listStatements().next();
		try {
			getSecuredContainer().remove(s);
			if (!securityEvaluator.evaluate(perms)) {
				Assert.fail("Should have thrown AccessDeniedException");
			}
		} catch (final AccessDeniedException e) {
			if (securityEvaluator.evaluate(perms)) {
				Assert.fail(String.format("Should not have thrown AccessDeniedException: %s - %s", e, e.getTriple()));
			}
		}

	}

}
