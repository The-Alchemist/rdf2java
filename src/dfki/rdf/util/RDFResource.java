package dfki.rdf.util;

import org.w3c.rdf.model.Resource;


public class RDFResource   implements Resource
{
    String namespace;
    String localName;
    ////public Resource storedRes;

    public RDFResource (Resource res)      throws org.w3c.rdf.model.ModelException
    {
        namespace = res.getNamespace();
        localName = res.getLocalName();
        ////storedRes = res;
    }

    public RDFResource (String namespace, String localName)
    {
        this.namespace = namespace;
        this.localName = localName;
        ////storedRes = null;  //FIXME: hope, this won't get critical somewhen...
    }

    public RDFResource (String uri)
    {
        // guess namespace and localname
        int pos = uri.indexOf("#");
        if (pos >= 0)
        {
            namespace = uri.substring(0, pos+1);
            localName = uri.substring(pos+1);
        }
        else
        {   // no namespace; this should NOT be allowed, really, should it?
            namespace = null;
            localName = uri;
        }
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
        return "dfki.rdf.util.RDFResource(" + getURI() + ")";
    }

    public String toStringShort ()
    {
        return getURI();
    }
}

