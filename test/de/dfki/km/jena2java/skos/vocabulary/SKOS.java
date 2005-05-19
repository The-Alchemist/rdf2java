package de.dfki.km.jena2java.skos.vocabulary;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

import de.dfki.km.jena2java.ObjectTracker;
/**
 * Vocabulary definitions for Jena/RDF resources
 * THIS FILE MAY BE RE-GENERATED - DO NOT EDIT!
 * @author Auto-generated by rdf2java on Thu May 19 16:01:34 CEST 2005
 */

public class SKOS
{
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();

    /** <p>The namespace of the vocabalary as a string</p> */
   public static final String NS = "http://www.w3.org/2004/02/skos/core#";

    /** <p>The namespace of the vocabalary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );

    /** <p>This property carries weak semantics.  It may be used to state that
     *  that the object is in some way related to the subject, and the
     *  relationship is NOT to be treated as hierarchical.  Essentially it
     *  provides a means of linking concepts in different branches of a
     *  hierarchy (tree).  Extend this property to create properties with
     *  stronger semantics, but may still be reduced to an associative
     *  structure for simple visual display.</p>
     */
    public static final Property related = m_model.createProperty( "http://www.w3.org/2004/02/skos/core#related");

    /** <p>Use this property to state that a concept is a part of some concept
     *  scheme.  A concept may be a part of more than one scheme.</p>
     */
    public static final Property inScheme = m_model.createProperty( "http://www.w3.org/2004/02/skos/core#inScheme");

    /** <p>This property is the inverse of the 'broader' property.</p> */
    public static final Property narrower = m_model.createProperty( "http://www.w3.org/2004/02/skos/core#narrower");

    /** <p>This property carries weak semantics.  It may be used to state that
     *  the object is in some way more general in meaning than the subject.
     *  Essentially it provides a means of organising concepts into a
     *  hierarchy (tree), without being restrictive about the exact semantic
     *  implications of the hierarchical structure itself.  Extend this
     *  property to create properties that carry stronger semantics, but may
     *  be reduced to a hierarchical structure for simple visual displays.</p>
     */
    public static final Property broader = m_model.createProperty( "http://www.w3.org/2004/02/skos/core#broader");

    /** <p>is subject of</p> */
    public static final Property isSubjectOf = m_model.createProperty( "http://www.w3.org/2004/02/skos/core#isSubjectOf");

    /** <p>dummy property with alien namespace</p> */
    public static final Property dummy_dummyProperty = m_model.createProperty( "http://schwarz.km.dfki.de/dummy#dummyProperty");

    /** <p>This is the super-property of all properties used to make statements
     *  about how concepts within the same conceptual scheme relate to each
     *  other.</p>
     */
    public static final Property semanticRelation = m_model.createProperty( "http://www.w3.org/2004/02/skos/core#semanticRelation");

    /** <p>Use this property to indicate a preferred label for a resource.  If
     *  the resource is a concept in some conceptual scheme, then it is
     *  strongly recommended that the preferred label be a unique label within
     *  that scheme.</p>
     */
    public static final Property prefLabel = m_model.createProperty( "http://www.w3.org/2004/02/skos/core#prefLabel");

    /** <p>Use this property to indicate that a concept is a top concept in a
     *  specific concept scheme.</p>
     */
    public static final Property hasTopConcept = m_model.createProperty( "http://www.w3.org/2004/02/skos/core#hasTopConcept");

    /** <p>Use this property to indicate an alternative (non-preferred) label for
     *  a resource.</p>
     */
    public static final Property altLabel = m_model.createProperty( "http://www.w3.org/2004/02/skos/core#altLabel");

    /** <p>Use this property to indicate an alternative (non-preferred) symbolic
     *  representation for a resource.</p>
     */
    public static final Property altSymbol = m_model.createProperty( "http://www.w3.org/2004/02/skos/core#altSymbol");

    /** <p>Use this property to indicate a preferred symbolic representation for
     *  a resource.</p>
     */
    public static final Property prefSymbol = m_model.createProperty( "http://www.w3.org/2004/02/skos/core#prefSymbol");

    /** <p>A concept is any unit of thought that can be defined or described.</p> */
    public static final Resource Concept = m_model.createResource( "http://www.w3.org/2004/02/skos/core#Concept");

    /** <p>A concept scheme is a collection of concepts.</p> */
    public static final Resource ConceptScheme = m_model.createResource( "http://www.w3.org/2004/02/skos/core#ConceptScheme");

    /** Registers this package's classes with an {@link ObjectTracker}.
     *  If you forget to do this, Java wrapper instances of this package's classes generated by
     *  <code>ObjectTracker.getInstance</code> will be of the generic <code>JenaResourceWrapper</code> type.
     */
    public static void register(ObjectTracker tracker)
    {
        tracker.addClass( Concept , de.dfki.km.jena2java.skos.vocabulary.Concept.class );
        tracker.addClass( ConceptScheme , de.dfki.km.jena2java.skos.vocabulary.ConceptScheme.class );
    }
}

