/* CVS $Id$ */
package de.dfki.rdf.test.jena2java; 
import com.hp.hpl.jena.rdf.model.*;
 
/**
 * Vocabulary definitions from E:\java\epos_context\model\context_all_newRdfsNamespace.rdfs 
 * @author Auto-generated by schemagen on 01 Dez 2004 16:36 
 */
public class DOMAIN {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabalary as a string ({@value})</p> */
    public static final String NS = "http://km.dfki.de/domain#";
    
    /** <p>The namespace of the vocabalary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabalary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    public static final Property synonyms = m_model.createProperty( "http://km.dfki.de/domain#synonyms" );
    
    public static final Property directSubConcepts = m_model.createProperty( "http://km.dfki.de/domain#directSubConcepts" );
    
    public static final Property directSuperConcepts = m_model.createProperty( "http://km.dfki.de/domain#directSuperConcepts" );
    
    public static final Property relatedTo = m_model.createProperty( "http://km.dfki.de/domain#relatedTo" );
    
    public static final Property name = m_model.createProperty( "http://km.dfki.de/domain#name" );
    
    public static final Property keyword = m_model.createProperty( "http://km.dfki.de/domain#keyword" );
    
    public static final Resource Keyword = m_model.createResource( "http://km.dfki.de/domain#Keyword" );
    
    public static final Resource DomainConcept = m_model.createResource( "http://km.dfki.de/domain#DomainConcept" );
    
    public static final Resource __DOMAIN__ = m_model.createResource( "http://km.dfki.de/domain#__DOMAIN__" );
    
}
