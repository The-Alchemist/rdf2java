package de.dfki.rdf.test.jena2java;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;


/**
 * A JenaResource is a wrapper around some real jena resource.
 * The idea is to abstract as much as possible from low-level
 * RDF operations. Instead of thinking in triples or graph structures,
 * we think of types Java objects having typed properties. Access these
 * properties by simple invoking respective getters and putters.  
 */
public class JenaResource implements Resource
{
    protected Resource m_res;

    
    /**
     * Initializes the internal jena resource of this JenaResource.
	 * Note: A JenaResource is just a wrapper around the real jena resource.
     */
    public JenaResource( Resource res )
    {
        m_res = res;
    }
    
    /**
     * Sets this resource. As a JenaResource is just a wrapper around a 
     * jena resource, that resource has to be set somewhen.
     */
	public void setResource( Resource res )
	{
		m_res = res;
	}

	/**
	 * Gets the internal jena resource of this JenaResource.
	 * Note: A JenaResource is just a wrapper around the real jena resource.
	 */
	public Resource getResource()
	{
		return m_res;
	}

	/**
	 * Returns a short string representation of this JenaResource.<br>
	 * 1. If this resource has a rdfs:label, then this will be returned.<br>
	 * 2. If a URI has been assigned to this resource, then this will be returned.<br>
	 * 3. If all of these cases fail, then super.toString() is used providing at
	 * least some string representation.  
	 */
	public String toString()
	{
	    try
	    {
	        String sLabel = getProperty( RDFS.label ).getString();
	        if( sLabel != null ) 
	            return sLabel;
	        String sURI = getURI();
	        if( sURI != null)
	            return sURI;
	    }
	    catch( Exception e )
	    {
	    }
        return super.toString();
	}
	
	
	//-------------------------------------------------------------------------
	//				com.hp.hpl.jena.rdf.model.Resource
	//-------------------------------------------------------------------------	

	/*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#getId()
     */
    public AnonId getId()
    {
        return m_res.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#getNode()
     */
    public Node getNode()
    {
        return m_res.getNode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#getURI()
     */
    public String getURI()
    {
        return m_res.getURI();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#getNameSpace()
     */
    public String getNameSpace()
    {
        return m_res.getNameSpace();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#getLocalName()
     */
    public String getLocalName()
    {
        return m_res.getLocalName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#isAnon()
     */
    public boolean isAnon()
    {
        return m_res.isAnon();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#getRequiredProperty(com.hp.hpl.jena.rdf.model.Property)
     */
    public Statement getRequiredProperty( Property p )
    {
        return m_res.getRequiredProperty(p);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#getProperty(com.hp.hpl.jena.rdf.model.Property)
     */
    public Statement getProperty( Property p )
    {
        return m_res.getProperty(p);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#listProperties(com.hp.hpl.jena.rdf.model.Property)
     */
    public StmtIterator listProperties( Property p )
    {
        return m_res.listProperties(p);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#listProperties()
     */
    public StmtIterator listProperties()
    {
        return m_res.listProperties();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#addProperty(com.hp.hpl.jena.rdf.model.Property,
     *      boolean)
     */
    public Resource addProperty( Property p, boolean o )
    {
        return m_res.addProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#addProperty(com.hp.hpl.jena.rdf.model.Property,
     *      long)
     */
    public Resource addProperty( Property p, long o )
    {
        return m_res.addProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#addProperty(com.hp.hpl.jena.rdf.model.Property,
     *      char)
     */
    public Resource addProperty( Property p, char o )
    {
        return m_res.addProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#addProperty(com.hp.hpl.jena.rdf.model.Property,
     *      float)
     */
    public Resource addProperty( Property p, float o )
    {
        return m_res.addProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#addProperty(com.hp.hpl.jena.rdf.model.Property,
     *      double)
     */
    public Resource addProperty( Property p, double o )
    {
        return m_res.addProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#addProperty(com.hp.hpl.jena.rdf.model.Property,
     *      java.lang.String)
     */
    public Resource addProperty( Property p, String o )
    {
        return m_res.addProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#addProperty(com.hp.hpl.jena.rdf.model.Property,
     *      java.lang.String, java.lang.String)
     */
    public Resource addProperty( Property p, String o, String l )
    {
        return m_res.addProperty(p, o, l);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#addProperty(com.hp.hpl.jena.rdf.model.Property,
     *      java.lang.Object)
     */
    public Resource addProperty( Property p, Object o )
    {
        return m_res.addProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#addProperty(com.hp.hpl.jena.rdf.model.Property,
     *      com.hp.hpl.jena.rdf.model.RDFNode)
     */
    public Resource addProperty( Property p, RDFNode o )
    {
        return m_res.addProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#hasProperty(com.hp.hpl.jena.rdf.model.Property)
     */
    public boolean hasProperty( Property p )
    {
        return m_res.hasProperty(p);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#hasProperty(com.hp.hpl.jena.rdf.model.Property,
     *      boolean)
     */
    public boolean hasProperty( Property p, boolean o )
    {
        return m_res.hasProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#hasProperty(com.hp.hpl.jena.rdf.model.Property,
     *      long)
     */
    public boolean hasProperty( Property p, long o )
    {
        return m_res.hasProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#hasProperty(com.hp.hpl.jena.rdf.model.Property,
     *      char)
     */
    public boolean hasProperty( Property p, char o )
    {
        return m_res.hasProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#hasProperty(com.hp.hpl.jena.rdf.model.Property,
     *      float)
     */
    public boolean hasProperty( Property p, float o )
    {
        return m_res.hasProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#hasProperty(com.hp.hpl.jena.rdf.model.Property,
     *      double)
     */
    public boolean hasProperty( Property p, double o )
    {
        return m_res.hasProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#hasProperty(com.hp.hpl.jena.rdf.model.Property,
     *      java.lang.String)
     */
    public boolean hasProperty( Property p, String o )
    {
        return m_res.hasProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#hasProperty(com.hp.hpl.jena.rdf.model.Property,
     *      java.lang.String, java.lang.String)
     */
    public boolean hasProperty( Property p, String o, String l )
    {
        return m_res.hasProperty(p, o, l);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#hasProperty(com.hp.hpl.jena.rdf.model.Property,
     *      java.lang.Object)
     */
    public boolean hasProperty( Property p, Object o )
    {
        return m_res.hasProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#hasProperty(com.hp.hpl.jena.rdf.model.Property,
     *      com.hp.hpl.jena.rdf.model.RDFNode)
     */
    public boolean hasProperty( Property p, RDFNode o )
    {
        return m_res.hasProperty(p, o);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#removeProperties()
     */
    public Resource removeProperties()
    {
        return m_res.removeProperties();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#removeAll(com.hp.hpl.jena.rdf.model.Property)
     */
    public Resource removeAll( Property p )
    {
        return m_res.removeAll(p);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#begin()
     */
    public Resource begin()
    {
        return m_res.begin();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#abort()
     */
    public Resource abort()
    {
        return m_res.abort();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#commit()
     */
    public Resource commit()
    {
        return m_res.commit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.Resource#getModel()
     */
    public Model getModel()
    {
        return m_res.getModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.RDFNode#as(java.lang.Class)
     */
    public RDFNode as( Class view )
    {
        return m_res.as(view);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.RDFNode#canAs(java.lang.Class)
     */
    public boolean canAs( Class view )
    {
        return m_res.canAs(view);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.RDFNode#inModel(com.hp.hpl.jena.rdf.model.Model)
     */
    public RDFNode inModel( Model m )
    {
        return m_res.inModel(m);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.rdf.model.RDFNode#visitWith(com.hp.hpl.jena.rdf.model.RDFVisitor)
     */
    public Object visitWith( RDFVisitor rv )
    {
        return m_res.visitWith(rv);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.hp.hpl.jena.graph.FrontsNode#asNode()
     */
    public Node asNode()
    {
        return m_res.asNode();

    }

} // end of class JenaResource

