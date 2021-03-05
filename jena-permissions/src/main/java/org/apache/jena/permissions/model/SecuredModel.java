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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.permissions.graph.SecuredGraph;
import org.apache.jena.permissions.graph.SecuredPrefixMapping;
import org.apache.jena.permissions.model.impl.SecuredNodeIterator;
import org.apache.jena.permissions.model.impl.SecuredRSIterator;
import org.apache.jena.permissions.model.impl.SecuredResIterator;
import org.apache.jena.permissions.model.impl.SecuredStatementIterator;
import org.apache.jena.rdf.model.*;
import org.apache.jena.shared.AddDeniedException;
import org.apache.jena.shared.AuthenticationRequiredException;
import org.apache.jena.shared.DeleteDeniedException;
import org.apache.jena.shared.Lock;
import org.apache.jena.shared.PropertyNotFoundException;
import org.apache.jena.shared.ReadDeniedException;
import org.apache.jena.shared.UpdateDeniedException;
import org.apache.jena.shared.PrefixMapping;

/**
 * The interface for secured Model instances.
 * 
 * Use the SecuredModel.Factory to create instances
 */
public interface SecuredModel extends Model, SecuredPrefixMapping {

	@Override
	public SecuredModel abort();

	/**
	 * @sec.graph Update
	 * @sec.triple Create for each statement as a triple.
	 * @throws UpdateDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel add(final List<Statement> statements)
			throws AddDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Update
	 * @sec.triple Create for each statement in the securedModel as a triple.
	 * @throws UpdateDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel add(final Model m)
			throws AddDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Update
	 * @sec.triple Create the statement as a triple
	 * @throws UpdateDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel add(final Statement s)
			throws AddDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Update
	 * @sec.triple Create all the statements as triples.
	 * @throws UpdateDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel add(final Statement[] statements)
			throws AddDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Update
	 * @sec.triple Create all the statements as triples.
	 * @throws UpdateDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel add(final StmtIterator iter)
			throws AddDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	@Override
	public SecuredModel begin();
	
	@Override
	public <T> T calculateInTxn(Supplier<T> action);
	
	@Override
	public void close();

	@Override
	public SecuredModel commit();

	/**
	 * @sec.graph Read
	 * @sec.triple Read Triple( s, p, SecNode.ANY )
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public boolean contains(final Resource s, final Property p)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Read
	 * @sec.triple Read Triple( s, p, o )
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public boolean contains(final Resource s, final Property p, final RDFNode o)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Read
	 * @sec.triple Read s as a triple with null replaced by SecNode.ANY
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public boolean contains(final Statement s) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Read
	 * @sec.triple Read every statement in securedModel.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public boolean containsAll(final Model model) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Read
	 * @sec.triple Read every statement
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public boolean containsAll(final StmtIterator iter) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Read
	 * @sec.triple Read any statement in securedModel to be included in check,
	 *             if no statement in securedModel can be read will return
	 *             false;
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public boolean containsAny(final Model model) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Read
	 * @sec.triple Read any statement in iter to be included in check, if no
	 *             statement in iter can be read will return false;
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public boolean containsAny(final StmtIterator iter) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Read
	 * @sec.triple Read Triple( s, p, resource) where Triple(s,p,resource) is in
	 *             the securedModel.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public boolean containsResource(final RDFNode r) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Update
	 * @throws UpdateDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredRDFList createList()
			throws AddDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Update
	 * @sec.triple Create Triple( RDF.nil, SecNode.IGNORE, SecNode.IGNORE)
	 * @sec.triple Create for each member Triple(SecNode.ANY,
	 *             RDF.first.asNode(), member.asNode())
	 * @sec.triple Create Triple(SecNode.ANY, RDF.rest.asNode(), SecNode.ANY)
	 * @throws UpdateDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredRDFList createList(final Iterator<? extends RDFNode> members)
			throws AddDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Update
	 * @sec.triple Create Triple( RDF.nil, SecNode.IGNORE, SecNode.IGNORE)
	 * @sec.triple Create for each member Triple(SecNode.ANY,
	 *             RDF.first.asNode(), member.asNode())
	 * @sec.triple Create Triple(SecNode.ANY, RDF.rest.asNode(), SecNode.ANY)
	 * @throws UpdateDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredRDFList createList(final RDFNode...members)
			throws AddDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	@Override
	public Literal createLiteral(String v, String language);

    /**
        Create a literal from a String value. An existing literal
        of the right value may be returned, or a fresh one created.
        The use of the wellFormed flag is to create typed literals of
        type rdf:XMLLiteral, without error checking. This should
        only be use when the lexical form is known to already be
        in exclusive canonical XML.

       @param v the lexical form of the literal
       @param wellFormed true if the Literal is well formed XML, in the lexical space of rdf:XMLLiteral
       @return a new literal
     */
	@Override
    public Literal createLiteral(String v, boolean wellFormed);
	
	@Override
	public SecuredProperty createProperty(final String nameSpace, final String localName);

	/**
	 * @sec.graph Update
	 * @sec.triple Read s as a triple
	 * @sec.triple create Triple( SecNode.Future, RDF.subject, t.getSubject() )
	 * @sec.triple create Triple( SecNode.Future, RDF.subject, t.getPredicate()
	 *             )
	 * @sec.triple create Triple( SecNode.Future, RDF.subject, t.getObject() )
	 * @throws UpdateDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public ReifiedStatement createReifiedStatement(final Statement s)
			throws AddDeniedException, ReadDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Update
	 * @sec.triple Read s as a triple
	 * @sec.triple create Triple( uri, RDF.subject, t.getSubject() )
	 * @sec.triple create Triple( uri, RDF.subject, t.getPredicate() )
	 * @sec.triple create Triple( uri, RDF.subject, t.getObject() )
	 * @throws UpdateDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public ReifiedStatement createReifiedStatement(final String uri, final Statement s)
			throws AddDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Update
	 * @sec.triple Read s as a triple
	 * @sec.triple Create Triple( SecNode.FUTURE, SecNode.IGNORE, SecNode.IGNORE
	 *             )
	 * @throws UpdateDeniedException
	 * @throws ReadDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredResource createResource()
			throws AddDeniedException, UpdateDeniedException, ReadDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Update
	 * @sec.triple Read s as a triple
	 * @sec.triple Create Triple( Anonymous(id), SecNode.IGNORE, SecNode.IGNORE
	 *             )
	 * @throws UpdateDeniedException
	 * @throws ReadDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredResource createResource(final AnonId id)
			throws AddDeniedException, UpdateDeniedException, ReadDeniedException, AuthenticationRequiredException;

	@Override
	public SecuredResource createResource(final String uri);

	@Override
	public SecuredResource createResource(final Statement uri);

	/**
	 * @sec.graph Update
	 * @sec.triple Create Triple( s, p, o )
	 * @throws UpdateDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredStatement createStatement(final Resource s, final Property p, final RDFNode o)
			throws AddDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	@Override
	public SecuredLiteral createTypedLiteral(final Object value);

	@Override
	public SecuredLiteral createTypedLiteral(final Object value, final RDFDatatype dtype);

	@Override
	public SecuredLiteral createTypedLiteral(final String lex, final RDFDatatype dtype);

	/**
	 * @sec.graph Read
	 * @sec.triple Read for every triple contributed to the difference.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public Model difference(final Model model) throws ReadDeniedException, AuthenticationRequiredException;

	@Override
	public boolean equals(Object m);
	
	@Override
	public void executeInTxn(Runnable action );

	/**
	 * @sec.graph Read if statement exists
	 * @sec.graph Update if statement does not exist
	 * @sec.triple Read s as a triple
	 * @sec.triple Read Triple( result, RDF.subject, s.getSubject() ) if
	 *             reification existed
	 * @sec.triple Read Triple( result, RDF.predicate, s.getPredicate() ) if
	 *             reification existed
	 * @sec.triple Read Triple( result, RDF.object, s.getObject() ) if
	 *             reification existed
	 * @sec.triple Create Triple( result, RDF.subject, s.getSubject() ) if
	 *             reification did not exist.
	 * @sec.triple Create Triple( result, RDF.predicate, s.getPredicate() ) if
	 *             reification did not exist
	 * @sec.triple Create Triple( result, RDF.object, s.getObject() ) if
	 *             reification did not exist
	 * @throws ReadDeniedException
	 * @throws UpdateDeniedException
	 * @throws AddDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredReifiedStatement getAnyReifiedStatement(final Statement s)
			throws AddDeniedException, ReadDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	@Override
	public Lock getLock();
	
	/**
	 * @sec.graph Read
	 * @sec.triple Read on the returned statement.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredStatement getProperty(final Resource s, final Property p)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Read
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public Property getProperty(final String nameSpace, final String localName)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * @sec.graph Read
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredStatement getProperty(final Resource s, final Property p, final String lang)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * . If the PropertyNotFoundException was thrown by the enclosed
	 * securedModel and the user can not read Triple(s, p, SecNode.ANY)
	 * AccessDeniedException is thrown, otherwise the PropertyNotFoundException
	 * will be thrown.
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on the returned statement
	 * @sec.triple Read on Triple(s, p, SecNode.ANY) if
	 *             PropertyNotFoundException was thrown
	 * @throws ReadDeniedException
	 * @throws PropertyNotFoundException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredStatement getRequiredProperty(final Resource s, final Property p)
			throws PropertyNotFoundException, ReadDeniedException, AuthenticationRequiredException;

	/**
	 * . If the PropertyNotFoundException was thrown by the enclosed
	 * securedModel and the user can not read Triple(s, p, SecNode.ANY)
	 * AccessDeniedException is thrown, otherwise the PropertyNotFoundException
	 * will be thrown.
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on the returned statement
	 * @sec.triple Read on Triple(s, p, SecNode.ANY) if
	 *             PropertyNotFoundException was thrown
	 * @throws ReadDeniedException
	 * @throws PropertyNotFoundException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredStatement getRequiredProperty(final Resource s, final Property p, String lang)
			throws ReadDeniedException, PropertyNotFoundException, AuthenticationRequiredException;

	@Override
	public SecuredResource getResource(final String uri);

	
	@Override
	public boolean independent();
	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on all triples contributed to the new securedModel.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public Model intersection(final Model model) throws ReadDeniedException, AuthenticationRequiredException;

	
	@Override
	public boolean isClosed();
	
	/**
	 * 
	 * @sec.graph Read
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public boolean isEmpty() throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read all compared triples. Triples that can not be read will
	 *             not be compared.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public boolean isIsomorphicWith(final Model g) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on s as triple
	 * @sec.triple Read on at least one set reified statements.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public boolean isReified(final Statement s) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public NsIterator listNameSpaces() throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on each RDFNode returned
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredNodeIterator<RDFNode> listObjects() throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on each RDFNode returned
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredNodeIterator<RDFNode> listObjectsOfProperty(final Property p)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on each RDFNode returned
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredNodeIterator<RDFNode> listObjectsOfProperty(final Resource s, final Property p)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on each Reified statement returned
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredRSIterator listReifiedStatements() throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on each Reified statement returned
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredRSIterator listReifiedStatements(final Statement st)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read at least one Triple( resource, p, o ) for each resource
	 *             returned;
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredResIterator listResourcesWithProperty(final Property p)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read at least one Triple( resource, p, o ) for each resource
	 *             returned
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredResIterator listResourcesWithProperty(final Property p, final RDFNode o)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on all triples returned
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredStatementIterator listStatements() throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on all triples returned
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredStatementIterator listStatements(final Resource s, final Property p, final RDFNode o)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on all triples returned
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredStatementIterator listStatements(final Selector s)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read at least one Triple( resource, p, o ) for each resource
	 *             returned
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredResIterator listSubjects() throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read at least one Triple( resource, p, o ) for each resource
	 *             returned
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredResIterator listSubjectsWithProperty(final Property p)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read at least one Triple( resource, p, o ) for each resource
	 *             returned
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredResIterator listSubjectsWithProperty(final Property p, final RDFNode o)
			throws ReadDeniedException, AuthenticationRequiredException;

	
	@Override
	public SecuredModel notifyEvent(final Object e);

	/**
	 * 
	 * @sec.graph Read
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public String qnameFor(final String uri) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public Model query(final Selector s) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @throws UpdateDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel read(final InputStream in, final String base)
			throws UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @throws UpdateDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel read(final InputStream in, final String base, final String lang)
			throws UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @throws UpdateDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel read(final Reader reader, final String base)
			throws UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @throws UpdateDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel read(final Reader reader, final String base, final String lang)
			throws UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @throws UpdateDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel read(final String url) throws UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @throws UpdateDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel read(final String url, final String lang)
			throws UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @throws UpdateDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel read(final String url, final String base, final String lang)
			throws UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * Listener will be filtered to only report events that the user can see.
	 * 
	 * @sec.graph Read
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel register(final ModelChangedListener listener)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @sec.triple Delete on every statement in statements.
	 * @throws UpdateDeniedException
	 * @throws DeleteDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel remove(final List<Statement> statements)
			throws DeleteDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @sec.triple Delete on every statement in baseModel.
	 * @throws UpdateDeniedException
	 * @throws DeleteDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel remove(final Model m)
			throws DeleteDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @sec.triple Delete on Triple( s, p, o )
	 * @throws UpdateDeniedException
	 * @throws DeleteDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel remove(final Resource s, final Property p, final RDFNode o)
			throws DeleteDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @sec.triple Delete on statement.
	 * @throws UpdateDeniedException
	 * @throws DeleteDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel remove(final Statement s)
			throws DeleteDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @sec.triple Delete on every statement in statements.
	 * @throws UpdateDeniedException
	 * @throws DeleteDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel remove(final Statement[] statements)
			throws DeleteDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @sec.triple Delete on every statement in iter.
	 * @throws UpdateDeniedException
	 * @throws DeleteDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel remove(final StmtIterator iter)
			throws DeleteDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @sec.triple Delete on every statement in the securedModel
	 * @throws UpdateDeniedException
	 * @throws DeleteDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel removeAll()
			throws DeleteDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @sec.triple Delete on every statement identified by Triple( s,p,o)
	 * @throws UpdateDeniedException
	 * @throws DeleteDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel removeAll(final Resource s, final Property p, final RDFNode r)
			throws DeleteDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @sec.triple Delete on every reification statement for each statement in
	 *             statements.
	 * @throws UpdateDeniedException
	 * @throws DeleteDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public void removeAllReifications(final Statement s)
			throws DeleteDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @throws UpdateDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel removeNsPrefix(final String prefix)
			throws UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Update
	 * @sec.triple Delete on every reification statement fore each statement in
	 *             rs.
	 * @throws UpdateDeniedException
	 * @throws DeleteDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public void removeReification(final ReifiedStatement rs)
			throws DeleteDeniedException, UpdateDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public String shortForm(final String uri) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public long size() throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on all statements contributed to the union.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public Model union(final Model model) throws ReadDeniedException, AuthenticationRequiredException;

	@Override
	public SecuredModel unregister(final ModelChangedListener listener);

	@Override
	public SecuredResource wrapAsResource(final Node n);

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on all statements that are written.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel write(final OutputStream out) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on all statements that are written.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel write(final OutputStream out, final String lang)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on all statements that are written.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel write(final OutputStream out, final String lang, final String base)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on all statements that are written.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel write(final Writer writer) throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on all statements that are written.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel write(final Writer writer, final String lang)
			throws ReadDeniedException, AuthenticationRequiredException;

	/**
	 * 
	 * @sec.graph Read
	 * @sec.triple Read on all statements that are written.
	 * @throws ReadDeniedException
	 * @throws AuthenticationRequiredException
	 *             if user is not authenticated and is required to be.
	 */
	@Override
	public SecuredModel write(final Writer writer, final String lang, final String base)
			throws ReadDeniedException, AuthenticationRequiredException;

	// Override return type for methods inherited from PrefixMapping
	@Override SecuredModel setNsPrefix( String prefix, String uri );
	@Override SecuredModel clearNsPrefixMap();
	@Override SecuredModel setNsPrefixes( PrefixMapping other );
	@Override SecuredModel setNsPrefixes( Map<String, String> map );
	@Override SecuredModel withDefaultMappings( PrefixMapping map );
}
