package dfki.rdf.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.rdf.model.ModelException;
import org.w3c.rdf.model.Resource;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import dfki.util.debug.Debug;


public class RDFResource   extends Object   
                           implements Resource, Serializable
{
//----------------------------------------------------------------------------------------------------
public final static String DEBUG_MODULE = "rdf2java";

private static Model ms_serializationModel = ModelFactory.createDefaultModel();

private String m_namespace;
private String m_localName;
private String m_uri;
private String m_label;         //SS:2004-08-05
private Resource m_rdfsClass;   //SS:2004-09-21

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
    m_rdfsClass = null;
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
public void putRDFSClass( Resource rdfsClass )
{
    m_rdfsClass = rdfsClass;
}

//----------------------------------------------------------------------------------------------------
public Resource getRDFSClass()
{
    return m_rdfsClass;
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
    // objects are hereby declared as identical   iff   one of the following applies: 
    // (a) their URIs are not null AND equal
    // (b) their URIs are     null AND they are the same objects, 
    //                                 i.e. their hashCode's (pointers) are the same
    try 
    { 
        if ( other != null  &&  (other instanceof org.w3c.rdf.model.Resource) )
        {
            org.w3c.rdf.model.Resource resOther = (org.w3c.rdf.model.Resource)other;
            if( getURI() != null )
                return( getURI().equals( resOther.getURI() ) );
            if( getURI() == null  &&  resOther.getURI() == null )
                return( hashCode() == resOther.hashCode() );
        }
    }
    catch (Exception ex) {}
    // fallout: no chance for a positive equals left
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
public String toStringAsRdf()
{
    return toStringAsRDF( null, null, ToStringController.NONRECURSIVE_TOSTRING_CONTROLLER );
}

public String toStringAsRdfRecursive()
{
    return toStringAsRDF( null, null, ToStringController.RECURSIVE_TOSTRING_CONTROLLER );
}

public String toStringAsRDF( Map/*String->String*/ mapPkg2NS, String sRdfsNamespace,
                             ToStringController tsc )
{
    
    ToStringAsRdfWalkerController wc = new ToStringAsRdfWalkerController( mapPkg2NS, sRdfsNamespace, tsc );
    JavaGraphWalker walker = new JavaGraphWalker( wc );
    walker.walk( this );
    
    return wc.serialzeXMLDocumentAsString();
}

static public String toStringAsRDF( Collection/*RDFResource*/ collResources,
                                    Map/*String->String*/ mapPkg2NS, String sRdfsNamespace,
                                    ToStringController tsc )
{
    ToStringAsRdfWalkerController wc = new ToStringAsRdfWalkerController( mapPkg2NS, sRdfsNamespace, tsc );
    JavaGraphWalker walker = new JavaGraphWalker( wc );

    for( Iterator it = collResources.iterator(); it.hasNext(); )
    {
        RDFResource res = (RDFResource)it.next();
        walker.walk( res );
    }
    
    return wc.serialzeXMLDocumentAsString();
}


//----------------------------------------------------------------------------------------------------
public String toStringPacked()
{
    return toStringPacked( null, null, ToStringController.NONRECURSIVE_TOSTRING_CONTROLLER );
}

public String toStringPackedRecursive()
{
    return toStringPacked( null, null, ToStringController.RECURSIVE_TOSTRING_CONTROLLER );
}

public String toStringPacked( Map/*String->String*/ mapPkg2NS, String sRdfsNamespace,
                              ToStringController tsc )
{
    
    ToStringPackedWalkerController wc = new ToStringPackedWalkerController( mapPkg2NS, sRdfsNamespace, tsc );
    JavaGraphWalker walker = new JavaGraphWalker( wc );
    walker.walk( this );
    
    return wc.serialzeAsString();
}

static public String toStringPacked( Collection/*RDFResource*/ collResources,
                                     Map/*String->String*/ mapPkg2NS, String sRdfsNamespace,
                                     ToStringController tsc )
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
public com.hp.hpl.jena.rdf.model.Resource asJenaResource( Map/*String->String*/ mapPkg2NS )
{
    com.hp.hpl.jena.rdf.model.Resource res;
    if( getURI() != null )
        res = ms_serializationModel.createResource( getURI() );
    else
        res = ms_serializationModel.createResource( new AnonId( getAddressOnlyHex() ) );
    if( getLabel() != null ) 
        res.addProperty( RDFS.label, getLabel() );

    String sClassURI = null;
    if( getRDFSClass() != null )
    {
        try{ sClassURI = getRDFSClass().getURI(); } 
        catch( ModelException ex ) {}
    }
    if( sClassURI == null )
    {
        if( mapPkg2NS == null ) 
            return res;
        String sClassNamespace = (String)mapPkg2NS.get( RDFResource.getClassPackage( getClass() ) );    
        sClassURI = sClassNamespace + RDFResource.getClassName( getClass() );
    }
    
    com.hp.hpl.jena.rdf.model.Resource resClass = ms_serializationModel.getResource( sClassURI );
    if( resClass == null )
        resClass = ms_serializationModel.createResource( sClassURI );
    res.addProperty( RDF.type, resClass );
    
    PropertyStore ps = getPropertyStore();
    for( Iterator it = ps.getPropertyInfos().iterator(); it.hasNext(); )
    {
        PropertyInfo pi = (PropertyInfo)it.next();
        String sPropLocalName = pi.getName();
        String sPropNamespace = pi.getNamespace();
        if( sPropNamespace == null )
        {
            String sPropPackage = getClassPackage( getClass() );
            if( mapPkg2NS == null ) continue;
            sPropNamespace = (String)mapPkg2NS.get( sPropPackage );
            if( sPropNamespace == null ) continue;
        }
        Property prop = ms_serializationModel.createProperty( sPropNamespace, sPropLocalName );
        Object value = pi.getValue();
        if( value == null ) continue;
        if( pi.hasMultiValue() )
        {
            Collection collValues = (Collection)value;
            if( collValues.size() > 0 )
            {
                for( Iterator itValues = collValues.iterator(); itValues.hasNext(); )
                {
                    Object oneValue = itValues.next();
                    if( oneValue instanceof RDFResource )
                    {
                        com.hp.hpl.jena.rdf.model.Resource resValue = ms_serializationModel.createResource( ((RDFResource)oneValue).getURI() );
                        res.addProperty( prop, resValue );
                    }
                    else
                    {
                        Literal litValue = ms_serializationModel.createLiteral( oneValue );
                        res.addProperty( prop, litValue );
                    }
                }
            }
        }
        else
        {
            if( value instanceof RDFResource )
            {
                com.hp.hpl.jena.rdf.model.Resource resValue = ms_serializationModel.createResource( ((RDFResource)value).getURI() );
                res.addProperty( prop, resValue );
            }
            else
            {
                Literal litValue = ms_serializationModel.createLiteral( value );
                res.addProperty( prop, litValue );
            }
        }
    }
    
    return res;
}


//----------------------------------------------------------------------------------------------------
} // end of class RDFResource

