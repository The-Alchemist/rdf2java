package de.dfki.rdf.test.jena2java;

import java.util.Collection;
import java.util.LinkedList;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.dfki.rdf.util.RDFTool;


public class DomainConcept   extends JenaResource
{
	public DomainConcept( Resource res )
	{
		super( res );
	}
	
	
    public void putName (String name)
    {
        RDFTool.setSingleValue( m_res, DOMAIN.name, name );
        RDFTool.setSingleValue( m_res, RDFS.label, name );				// this is too specific !?
    }

    public String getName ()
    {
        return RDFTool.readValue( m_res, DOMAIN.name );
    }

    
    public void putDirectSubConcepts( DomainConcept domainConcept )
    {
        m_res.addProperty( DOMAIN.directSubConcepts, domainConcept );
    }
    
    public Collection/*DomainConcept*/ getDirectSubConcepts()
    {
        Collection/*JenaResource*/ result = new LinkedList();
        StmtIterator it = m_res.listProperties( DOMAIN.directSubConcepts );
        while( it.hasNext() )
        {
            Statement stmt = (Statement)it.nextStatement();
            Resource res = stmt.getResource();
            if( !(res instanceof JenaResource) )
                res = new DomainConcept( res );
            result.add( res );
        }
        return result;
    }

} // end of class DomainConcept

