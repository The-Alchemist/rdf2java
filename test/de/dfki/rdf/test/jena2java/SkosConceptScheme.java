package de.dfki.rdf.test.jena2java;

import java.util.Collection;
import java.util.LinkedList;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.dfki.rdf.util.RDFTool;


/**
 * A concept scheme is a collection of concepts.
 */
public class SkosConceptScheme   extends JenaResourceWrapper
{
    public SkosConceptScheme( Resource res )
    {
        super( res );
    }
    
    public SkosConceptScheme()
    {
        super();
    }
    
    public SkosConceptScheme( String uri )
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
     * Use this property to indicate that a concept is a top concept in a specific concept scheme.
     */
    public void addHasTopConcept( SkosConcept concept )
    {
        addProperty( GNO_SKOS.hasTopConcept, concept );
    }
    
    /**
     * Use this property to indicate that a concept is a top concept in a specific concept scheme.
     */
    public Collection/*Resource*/ listHasTopConcept()
    {
        Collection/*Resource*/ result = new LinkedList();
        StmtIterator it = m_res.listProperties( GNO_SKOS.hasTopConcept );
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

} // end of class SkosConceptScheme

