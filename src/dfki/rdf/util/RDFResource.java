package dfki.rdf.util;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import org.w3c.rdf.model.Resource;


public class RDFResource   extends Object   implements Resource
{
//----------------------------------------------------------------------------------------------------
private String m_namespace;
private String m_localName;
private String m_uri;
protected PropertyStore m_propertyStore;


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
    m_uri = ( m_namespace != null  ?  m_namespace + m_localName  :  m_localName );
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
public String toString (String sIndent)
{
    return sIndent + toString();
}


//----------------------------------------------------------------------------------------------------
/** Gets the class name of this object.
  */
protected String getClassName ()
{
    return getClassNameShort() + " (" + getClass().getName() + ")";
}

//----------------------------------------------------------------------------------------------------
/** Gets a short version (without package) of the class name of this object.
  */
protected String getClassNameShort ()
{
    String sClassName = getClass().getName();
    int pos = sClassName.lastIndexOf('.');
    if (pos >= 0)
        return sClassName.substring(pos+1);
    else
        return sClassName;
}

//----------------------------------------------------------------------------------------------------
/** Gets a string showing the address of this object in hex notation.
  * <br>
  * The string is prefixed with a <code>'@'</code> character.
  */
public String getAddress ()
{
    return "@" + Integer.toHexString(super.hashCode());
}

//----------------------------------------------------------------------------------------------------
/** Gets a string showing the address of this object in hex notation.
  * <br>
  * The string is <b>not</b> prefixed with a <code>'@'</code> character.
  */
public String getAddressOnlyHex ()
{
    return Integer.toHexString(super.hashCode());
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
    if ( other != null  &&  (other instanceof org.w3c.rdf.model.Resource) )
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
    if (getURI() != null)
        return getURI().hashCode();
    else
        return super.hashCode();
}

//----------------------------------------------------------------------------------------------------
private static Map/*Class -> Collection of String*/ mapCls2PropertiesCache = new HashMap();

public Collection/*String*/ getProperties ()
{
    return getPropertiesOfThisClass();
}

protected Collection/*String*/ getPropertiesOfThisClass ()
{
    Class cls = getClass();
    Collection collProps = (Collection)RDFResource.mapCls2PropertiesCache.get( cls );
    if (collProps == null)
    {
        if( m_propertyStore != null )
            collProps = m_propertyStore.getProperties();
        else
            collProps = RDF2Java.getProperties( cls );
        RDFResource.mapCls2PropertiesCache.put( cls, collProps );
    }
    return collProps;
}

//------------------------------------------------------------------------------
public PropertyStore createPropertyStore()
{
    if( m_propertyStore == null )
        m_propertyStore = new PropertyStore( getClass() );
    return m_propertyStore;
}

//----------------------------------------------------------------------------------------------------
public PropertyStore getPropertyStore()
{
    return m_propertyStore;
}

//----------------------------------------------------------------------------------------------------
} // end of class RDFResource

