package de.dfki.rdf.test.jena2java;

import java.util.Collection;
import java.util.LinkedList;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.dfki.rdf.util.RDFTool;


/**
 * A concept is any unit of thought that can be defined or described.
 */
public class SkosConcept   extends JenaResource
{
    public SkosConcept( Resource res )
    {
        super( res );
    }
    
    public SkosConcept()
    {
        super();
    }
    
    public SkosConcept( String uri )
    {
        super( uri );
    }
    
    /**
     * Use this property to indicate a preferred label for a resource.  If the resource is a concept in some conceptual scheme, then it is strongly recommended that the preferred label be a unique label within that scheme.
     */
    public void setPrefLabel( String prefLabel )
    {
        RDFTool.setSingleValue( m_res, GNO_SKOS.prefLabel, prefLabel );
    }

    /**
     * Use this property to indicate a preferred label for a resource.  If the resource is a concept in some conceptual scheme, then it is strongly recommended that the preferred label be a unique label within that scheme.
     */
    public String getPrefLabel()
    {
        return RDFTool.readValue( m_res, GNO_SKOS.prefLabel );
    }

    /**
     * Returns the rdfs:label of this resource. 
     * If no label has been set, the 'prefLabel' will be returned.
     */
    public String getRdfsLabel()
    {
        String sLabel = super.getRdfsLabel();
        if( sLabel != null )
            return sLabel;
        else
            return getPrefLabel();
    }

    /**
     * Use this property to indicate an alternative (non-preferred) label for a resource.
     */
    public void setAltLabel( String altLabel )
    {
        RDFTool.setSingleValue( m_res, GNO_SKOS.altLabel, altLabel );
    }

    /**
     * Use this property to indicate an alternative (non-preferred) label for a resource.
     */
    public String getAltLabel()
    {
        return RDFTool.readValue( m_res, GNO_SKOS.altLabel );
    }
    
    /**
     * Use this property to state that a concept is a part of some concept scheme.  A concept may be a part of more than one scheme.
     */
    public void setInScheme( SkosConceptScheme scheme )
    {
        m_res.addProperty( GNO_SKOS.inScheme, scheme );
    }
    
    /**
     * Use this property to state that a concept is a part of some concept scheme.  A concept may be a part of more than one scheme.
     */
    public SkosConceptScheme getInScheme()
    {
        Statement stmt = m_res.getProperty( GNO_SKOS.inScheme );
        if( stmt == null ) return null;
        Resource res = stmt.getResource();
        if( res == null ) return null;
        if( !(res instanceof SkosConceptScheme) )
            res = new SkosConceptScheme( res );
        return (SkosConceptScheme)res;
    }

    /**
     * This is the super-property of all properties used to make statements about how concepts within the same conceptual scheme relate to each other.
     */
    public void addSemanticRelation( SkosConcept concept )
    {
        m_res.addProperty( GNO_SKOS.semanticRelation, concept );
    }
    
    /**
     * This is the super-property of all properties used to make statements about how concepts within the same conceptual scheme relate to each other.
     */
    public Collection/*SkosConcept*/ listSemanticRelation()
    {
        Collection/*SkosConcept*/ result = new LinkedList();
        StmtIterator it = m_res.listProperties( GNO_SKOS.semanticRelation );
        while( it.hasNext() )
        {
            Statement stmt = (Statement)it.nextStatement();
            Resource res = stmt.getResource();
            if( !(res instanceof SkosConcept) )
                res = new SkosConcept( res );
            result.add( res );
        }
        return result;
    }

    /**
     * This property is the inverse of the 'broader' property.
     */
    public void addNarrower( SkosConcept concept )
    {
        m_res.addProperty( GNO_SKOS.narrower, concept );
    }
    
    /**
     * This property is the inverse of the 'broader' property.
     */
    public Collection/*SkosConcept*/ listNarrower()
    {
        Collection/*SkosConcept*/ result = new LinkedList();
        StmtIterator it = m_res.listProperties( GNO_SKOS.narrower );
        while( it.hasNext() )
        {
            Statement stmt = (Statement)it.nextStatement();
            Resource res = stmt.getResource();
            if( !(res instanceof SkosConcept) )
                res = new SkosConcept( res );
            result.add( res );
        }
        return result;
    }

    /**
     * This property carries weak semantics.  It may be used to state that the object is in some way more general in meaning than the subject.  Essentially it provides a means of organising concepts into a hierarchy (tree), without being restrictive about the exact semantic implications of the hierarchical structure itself.  Extend this property to create properties that carry stronger semantics, but may be reduced to a hierarchical structure for simple visual displays.
     */
    public void addBroader( SkosConcept concept )
    {
        m_res.addProperty( GNO_SKOS.broader, concept );
    }
    
    /**
     * This property carries weak semantics.  It may be used to state that the object is in some way more general in meaning than the subject.  Essentially it provides a means of organising concepts into a hierarchy (tree), without being restrictive about the exact semantic implications of the hierarchical structure itself.  Extend this property to create properties that carry stronger semantics, but may be reduced to a hierarchical structure for simple visual displays.
     */
    public Collection/*SkosConcept*/ listBroader()
    {
        Collection/*SkosConcept*/ result = new LinkedList();
        StmtIterator it = m_res.listProperties( GNO_SKOS.broader );
        while( it.hasNext() )
        {
            Statement stmt = (Statement)it.nextStatement();
            Resource res = stmt.getResource();
            if( !(res instanceof SkosConcept) )
                res = new SkosConcept( res );
            result.add( res );
        }
        return result;
    }

    /**
     * This property carries weak semantics.  It may be used to state that that the object is in some way related to the subject, and the relationship is NOT to be treated as hierarchical.  Essentially it provides a means of linking concepts in different branches of a hierarchy (tree).  Extend this property to create properties with stronger semantics, but may still be reduced to an associative structure for simple visual display.
     */
    public void addRelated( SkosConcept concept )
    {
        m_res.addProperty( GNO_SKOS.related, concept );
    }
    
    /**
     * This property carries weak semantics.  It may be used to state that that the object is in some way related to the subject, and the relationship is NOT to be treated as hierarchical.  Essentially it provides a means of linking concepts in different branches of a hierarchy (tree).  Extend this property to create properties with stronger semantics, but may still be reduced to an associative structure for simple visual display.
     */
    public Collection/*SkosConcept*/ listRelated()
    {
        Collection/*SkosConcept*/ result = new LinkedList();
        StmtIterator it = m_res.listProperties( GNO_SKOS.related );
        while( it.hasNext() )
        {
            Statement stmt = (Statement)it.nextStatement();
            Resource res = stmt.getResource();
            if( !(res instanceof SkosConcept) )
                res = new SkosConcept( res );
            result.add( res );
        }
        return result;
    }

    /**
     * This property is the inverse of the 'subject' property.
     */
    public void addIsSubjectOf( Resource res )
    {
        m_res.addProperty( GNO_SKOS.isSubjectOf, res );
    }
    
    /**
     * This property is the inverse of the 'subject' property.
     */
    public Collection/*Resource*/ listIsSubjectOf()
    {
        Collection/*Resource*/ result = new LinkedList();
        StmtIterator it = m_res.listProperties( GNO_SKOS.isSubjectOf );
        while( it.hasNext() )
        {
            Statement stmt = (Statement)it.nextStatement();
            Resource res = stmt.getResource();
            result.add( res );
        }
        return result;
    }

} // end of class SkosConcept

