package dfki.rdf.util;

import org.w3c.rdf.model.Resource;


public class RDFResource   implements Resource
{

    protected RDFResource ()
    {
        setURI( null, null );  // hope, this doesn't last for long...
    }

    public RDFResource (Resource res)      throws org.w3c.rdf.model.ModelException
    {
        setURI(res.getNamespace(), res.getLocalName());
    }

    public RDFResource (String namespace, String localName)
    {
        setURI(namespace, localName);
    }

    public RDFResource (String uri)
    {
        setURI(uri);
    }


    protected void setURI (String namespace, String localName)
    {
        m_namespace = namespace;
        m_localName = localName;
        m_uri = m_namespace + m_localName;
    }

    protected void setURI (String uri)
    {
        // guess namespace and localname
        int pos = uri.indexOf("#");
        if (pos >= 0)
            setURI( uri.substring(0, pos+1), uri.substring(pos+1) );
        else  // no namespace; this should NOT be allowed, really, should it?
            setURI( null, uri );
    }

    public String getNamespace ()
    {
        return m_namespace;
    }

    public String getLocalName ()
    {
        return m_localName;
    }

    public String getURI ()
    {
        return m_uri;
    }

    public String getLabel ()
    {
        return getURI();
    }


    public String toString ()
    {
        return "dfki.rdf.util.RDFResource(" + getURI() + ")";
    }

    public String toStringShort ()
    {
        return getURI();
    }


    private String m_namespace;
    private String m_localName;
    private String m_uri;
}

