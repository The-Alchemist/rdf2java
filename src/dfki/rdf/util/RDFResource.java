package dfki.rdf.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.w3c.rdf.model.Resource;
import org.w3c.rdf.vocabulary.rdf_schema_200001.RDFS;

import dfki.rdf.util.nice.tinyxmldoc.TinyXMLDocument;
import dfki.rdf.util.nice.tinyxmldoc.TinyXMLElement;
import dfki.rdf.util.nice.tinyxmldoc.TinyXMLTextNode;
import dfki.util.debug.Debug;
import dfki.util.rdf.RDF;


public class RDFResource   extends Object   
                           implements Resource, Serializable
{
//----------------------------------------------------------------------------------------------------
public final static String DEBUG_MODULE = "rdf2java";

private String m_namespace;
private String m_localName;
private String m_uri;
private String m_label;     //SS:2004-08-05

protected PropertyStore m_propertyStore;

//----------------------------------------------------------------------------------------------------
public static Debug debug()
{
    return Debug.forModule( DEBUG_MODULE );
}

//----------------------------------------------------------------------------------------------------
protected RDFResource ()
{
    m_namespace = null;
    m_localName = null;
    m_uri = null;
    m_label = null;
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
public void putLabel (String label)
{
    m_label = label;
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
    return ( m_label != null  ?  m_label  :  getURI() );
}

//----------------------------------------------------------------------------------------------------
public String toString ()
{
    if( getLabel() != null )
        return getLabel();
    else
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

//----------------------------------------------------------------------------------------------------
public PropertyStore getPropertyStore()
{
    if( m_propertyStore == null )
        m_propertyStore = new PropertyStore( getClass() );
    return m_propertyStore;
}




//----------------------------------------------------------------------------------------------------
public String toStringAsRDF()
{
    return toStringAsRDF( null, null, ToStringAsRdfWalkerController.DEFAULT_TO_STRING_CONTROLLER );
}

public String toStringAsRDF( Map/*String->String*/ mapPkg2NS, String sRdfsNamespace,
                             ToStringAsRdfWalkerController.ToStringController tsc )
{
    
    ToStringAsRdfWalkerController wc = new ToStringAsRdfWalkerController( mapPkg2NS, sRdfsNamespace, tsc );
    JavaGraphWalker walker = new JavaGraphWalker( wc );
    walker.walk( this );
    
    return wc.xmlDoc.serialize();
}

static public String toStringAsRDF( Collection/*RDFResource*/ collResources,
                                    Map/*String->String*/ mapPkg2NS, String sRdfsNamespace,
                                    ToStringAsRdfWalkerController.ToStringController tsc )
{
    ToStringAsRdfWalkerController wc = new ToStringAsRdfWalkerController( mapPkg2NS, sRdfsNamespace, tsc );
    JavaGraphWalker walker = new JavaGraphWalker( wc );

    for( Iterator it = collResources.iterator(); it.hasNext(); )
    {
        RDFResource res = (RDFResource)it.next();
        walker.walk( res );
    }
    
    return wc.xmlDoc.serialize();
}


//----------------------------------------------------------------------------------------------------
public String toStringPacked()
{
    return toStringPacked( null, null, ToStringPackedWalkerController.DEFAULT_TO_STRING_CONTROLLER );
}

public String toStringPacked( Map/*String->String*/ mapPkg2NS, String sRdfsNamespace,
                              ToStringPackedWalkerController.ToStringController tsc )
{
    
    ToStringPackedWalkerController wc = new ToStringPackedWalkerController( mapPkg2NS, sRdfsNamespace, tsc );
    JavaGraphWalker walker = new JavaGraphWalker( wc );
    walker.walk( this );
    
    return wc.serialzeAsString();
}

static public String toStringPacked( Collection/*RDFResource*/ collResources,
                                     Map/*String->String*/ mapPkg2NS, String sRdfsNamespace,
                                     ToStringPackedWalkerController.ToStringController tsc )
{
    ToStringPackedWalkerController wc = new ToStringPackedWalkerController( mapPkg2NS, sRdfsNamespace, tsc );
    JavaGraphWalker walker = new JavaGraphWalker( wc );

    for( Iterator it = collResources.iterator(); it.hasNext(); )
    {
        RDFResource res = (RDFResource)it.next();
        walker.walk( res );
    }
    
    return wc.serialzeAsString();
}


//----------------------------------------------------------------------------------------------------
public static String getClassPackage( Class cls )
{
    String className = cls.getName();
    int p = className.lastIndexOf( '.' );
    return className.substring( 0, p );
}

public static String getClassName( Class cls )
{
    String className = cls.getName();
    int p = className.lastIndexOf( '.' );
    return className.substring( p+1 );
}


//----------------------------------------------------------------------------------------------------
} // end of class RDFResource

