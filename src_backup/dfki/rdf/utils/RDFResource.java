package dfki.rdf.utils;

import org.w3c.rdf.model.Resource;


public class RDFResource   implements Resource
{
    String namespace;
    String localName;
    public Resource storedRes;

    public RDFResource (Resource res)      throws org.w3c.rdf.model.ModelException
    {
        namespace = res.getNamespace();
        localName = res.getLocalName();
        storedRes = res;
    }

    public String getNamespace ()
    {
        return namespace;
    }

    public String getLocalName ()
    {
        return localName;
    }

    public String getURI ()
    {
        return namespace + localName;
    }

    public String getLabel ()
    {
        return getURI();
    }

    public String toString ()
    {
        return "dfki.rdf.utils.RDFResource(" + getURI() + ")";
    }

    public String toStringShort ()
    {
        return getURI();
    }
}

