package dfki.rdf.util;

import org.w3c.rdf.model.Resource;


public class RDFResource   implements Resource
{
//----------------------------------------------------------------------------------------------------
private String m_namespace;
private String m_localName;
private String m_uri;


//----------------------------------------------------------------------------------------------------
protected RDFResource ()
{
    m_namespace = null;
    m_localName = null;
    m_uri = null;
}

//----------------------------------------------------------------------------------------------------
public RDFResource (Resource res)      throws org.w3c.rdf.model.ModelException
{
    putURI(res.getNamespace(), res.getLocalName());
}

//----------------------------------------------------------------------------------------------------
public RDFResource (String namespace, String localName)
{
    putURI(namespace, localName);
}

//----------------------------------------------------------------------------------------------------
public RDFResource (String uri)
{
    putURI(uri);
}

//----------------------------------------------------------------------------------------------------
public void putURI (String namespace, String localName)
{
    m_namespace = namespace;
    m_localName = localName;
    m_uri = m_namespace + m_localName;
}

//----------------------------------------------------------------------------------------------------
public void putURI (String uri)
{
    // guess namespace and localname
    int pos = uri.indexOf("#");
    if (pos >= 0)
        putURI( uri.substring(0, pos+1), uri.substring(pos+1) );
    else  // no namespace; this should NOT be allowed, really, should it?
        putURI( null, uri );
}

//----------------------------------------------------------------------------------------------------
public String getNamespace ()
{
    return m_namespace;
}

//----------------------------------------------------------------------------------------------------
public String getLocalName ()
{
    return m_localName;
}

//----------------------------------------------------------------------------------------------------
public String getURI ()
{
    return m_uri;
}

//----------------------------------------------------------------------------------------------------
public String getLabel ()
{
    return getURI();
}

//----------------------------------------------------------------------------------------------------
public String toString ()
{
    return "RDFResource(" + getURI() + ")";
}

//----------------------------------------------------------------------------------------------------
public String toStringShort ()
{
    return "RDFResource(" + getURI() + ")";
}

//----------------------------------------------------------------------------------------------------
public boolean equals (Object other)
{
    // objects are hereby declared as identical iff their URIs are equal
    if (other instanceof org.w3c.rdf.model.Resource)
    {
        try { return getURI().equals(((org.w3c.rdf.model.Resource)other).getURI()); }
        catch (Exception ex) { return false; }
    }
    else
        return false;
}

//----------------------------------------------------------------------------------------------------
public int hashCode ()
{
    return getURI().hashCode();
}

//----------------------------------------------------------------------------------------------------
} // end of class RDFResource

