package de.dfki.rdf.util;

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

import de.dfki.util.debug.Debug;


public class RDFResource   extends Object   
                           implements Resource, Serializable
{
//----------------------------------------------------------------------------------------------------
public final static String DEBUG_MODULE = "rdf2java";

private static Model ms_serializationModel = ModelFactory.createDefaultModel();

transient private String m_namespace;
transient private String m_localName;
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
	String ns = res.getNamespace();
	String local = res.getLocalName();
	if( ns != null || local != null)
		putURI(ns, local);
	else
		putURI( null );
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
	if( namespace == null ) namespace = "";
	if( localName == null ) localName = "";
    m_namespace = new String(namespace);
    m_localName = new String(localName);
    m_uri = m_namespace + m_localName;
}

//----------------------------------------------------------------------------------------------------
public void putURI (String uri)
{
	m_uri = uri;
	m_namespace = null;
	m_localName = null;
}

//----------------------------------------------------------------------------------------------------
public void putLabel (String label)
{
    m_label = label;
}

//----------------------------------------------------------------------------------------------------
public String getNamespace ()
{
	if( m_namespace == null && m_uri != null)
	{
	    // guess namespace
	    int pos = m_uri.indexOf("#");
	    if (pos >= 0)
	        m_namespace = new String(m_uri.substring(0, pos+1));
	    else  // no namespace; this should NOT be allowed, really, should it?
	        m_namespace = "";	
	}
    return m_namespace;
}

//----------------------------------------------------------------------------------------------------
public String getLocalName ()
{
	if( m_localName == null && m_uri != null)
	{
	    // guess localname
	    int pos = m_uri.indexOf("#");
	    if (pos >= 0)
	        m_localName = new String(m_uri.substring(pos+1));
	    else  // no namespace; this should NOT be allowed, really, should it?
	        m_localName = "";
	}	
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
    return m_label;
    //// return ( m_label != null  ?  m_label  :  getURI() );
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
        m_propertyStore = new PropertyStore( getClass(), getRDFSClass() );
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
public com.hp.hpl.jena.rdf.model.Resource asJenaResource()
{
  return asJenaResource( null, ms_serializationModel, null );
}

//----------------------------------------------------------------------------------------------------
public com.hp.hpl.jena.rdf.model.Resource asJenaResource( Model model )
{
  return asJenaResource( null, model, null );
}

//----------------------------------------------------------------------------------------------------
public com.hp.hpl.jena.rdf.model.Resource asJenaResource( Map/*String->String*/ mapPkg2NS )
{
    return asJenaResource( mapPkg2NS, ms_serializationModel, null );
}

//----------------------------------------------------------------------------------------------------
//public com.hp.hpl.jena.rdf.model.Resource asJenaResource( Map/*String->String*/ mapPkg2NS, Model model )
//{
//    com.hp.hpl.jena.rdf.model.Resource res;
//    if( getURI() != null )
//        res = model.createResource( getURI() );
//    else
//        res = model.createResource( new AnonId( getAddressOnlyHex() ) );
//    if( getLabel() != null ) 
//        res.addProperty( RDFS.label, getLabel() );
//
//    String sClassURI = null;
//    if( getRDFSClass() != null )
//    {
//        try{ sClassURI = getRDFSClass().getURI(); } 
//        catch( ModelException ex ) {}
//    }
//    if( sClassURI == null )
//    {
//        if( mapPkg2NS == null ) 
//            return res;
//        String sClassNamespace = (String)mapPkg2NS.get( RDFResource.getClassPackage( getClass() ) );    
//        sClassURI = sClassNamespace + RDFResource.getClassName( getClass() );
//    }
//    
//    com.hp.hpl.jena.rdf.model.Resource resClass = model.getResource( sClassURI );
//    if( resClass == null )
//        resClass = model.createResource( sClassURI );
//    res.addProperty( RDF.type, resClass );
//    
//    PropertyStore ps = getPropertyStore();
//    for( Iterator it = ps.getPropertyInfos().iterator(); it.hasNext(); )
//    {
//        PropertyInfo pi = (PropertyInfo)it.next();
//        String sPropLocalName = pi.getName();
//        String sPropNamespace = pi.getNamespace();
//        if( sPropNamespace == null )
//        {
//            String sPropPackage = getClassPackage( getClass() );
//            if( mapPkg2NS == null ) continue;
//            sPropNamespace = (String)mapPkg2NS.get( sPropPackage );
//            if( sPropNamespace == null ) continue;
//        }
//        Property prop = model.createProperty( sPropNamespace, sPropLocalName );
//        Object value = pi.getValue();
//        if( value == null ) continue;
//        if( pi.hasMultiValue() )
//        {
//            Collection collValues = (Collection)value;
//            if( collValues.size() > 0 )
//            {
//                for( Iterator itValues = collValues.iterator(); itValues.hasNext(); )
//                {
//                    Object oneValue = itValues.next();
//                    if( oneValue instanceof RDFResource )
//                    {
//                        com.hp.hpl.jena.rdf.model.Resource resValue = null;
//                        if( ((RDFResource)oneValue).getURI() != null )
//                            resValue = model.createResource( ((RDFResource)oneValue).getURI() );
//                        else if( (oneValue instanceof THING) )
//                            resValue = ((THING)oneValue).asJenaResource( mapPkg2NS, model );
//                        
//                        if( resValue != null )
//                            res.addProperty( prop, resValue );
//                    }
//                    else
//                    {
//                        Literal litValue = model.createLiteral( oneValue );
//                        res.addProperty( prop, litValue );
//                    }
//                }
//            }
//        }
//        else
//        {
//            if( value instanceof RDFResource )
//            {
//                com.hp.hpl.jena.rdf.model.Resource resValue = null;
//                if( ((RDFResource)value).getURI() != null )
//                    resValue = model.createResource( ((RDFResource)value).getURI() );
//                else if( (value instanceof THING) )
//                    resValue = ((THING)value).asJenaResource( mapPkg2NS, model );
//                
//                if( resValue != null )
//                    res.addProperty( prop, resValue );
//            }
//            else
//            {
//                Literal litValue = model.createLiteral( value );
//                res.addProperty( prop, litValue );
//            }
//        }
//    }
//    
//    return res;
//}

//----------------------------------------------------------------------------------------------------
public com.hp.hpl.jena.rdf.model.Resource asJenaResource( 
        Map/*String->String*/ mapPkg2NS, Model model, JenaAsResourceController ctrl )
{
    Map mapThing2JenaRes = new HashMap();
    if( model == null )
        model = ms_serializationModel;
    if( ctrl == null )
        ctrl = new JenaAsResourceController();
    return asJenaResource( mapPkg2NS, model, ctrl, mapThing2JenaRes );
}

public com.hp.hpl.jena.rdf.model.Resource asJenaResource( 
        Map/*String->String*/ mapPkg2NS, Model model, JenaAsResourceController ctrl, 
        Map mapThing2JenaRes )
{

    com.hp.hpl.jena.rdf.model.Resource res = (com.hp.hpl.jena.rdf.model.Resource)mapThing2JenaRes.get( this );
    if( res != null )
        return res;
    
    if( getURI() != null )
        res = model.createResource( getURI() );
    else
        res = model.createResource( new AnonId( getAddressOnlyHex() ) );
    mapThing2JenaRes.put( this, res );
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
    
    com.hp.hpl.jena.rdf.model.Resource resClass = model.createResource( sClassURI );
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
        Property prop = model.createProperty( sPropNamespace, sPropLocalName );
        if( ctrl.hideProperty( this, prop ) ) continue;
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
                        com.hp.hpl.jena.rdf.model.Resource resValue = null;
                        if( ((RDFResource)oneValue).getURI() != null )
                        {
                            if( (oneValue instanceof THING) && ctrl.expandProperty( this, prop, (RDFResource)oneValue ) )
                                resValue = ((RDFResource)oneValue).asJenaResource( mapPkg2NS, model, ctrl, mapThing2JenaRes );
                            else 
                                resValue = model.createResource( ((RDFResource)oneValue).getURI() );
                        }
                        else if( (oneValue instanceof THING) )
                            resValue = ((RDFResource)oneValue).asJenaResource( mapPkg2NS, model, ctrl, mapThing2JenaRes );
                        
                        if( resValue != null )
                            res.addProperty( prop, resValue );
                    }
                    else
                    {
                        Literal litValue = model.createLiteral( oneValue );
                        res.addProperty( prop, litValue );
                    }
                }
            }
        }
        else
        {
            if( value instanceof RDFResource )
            {
                com.hp.hpl.jena.rdf.model.Resource resValue = null;
                if( ((RDFResource)value).getURI() != null )
                {
                    if( (value instanceof THING) && ctrl.expandProperty( this, prop, (RDFResource)value ) )
                        resValue = ((RDFResource)value).asJenaResource( mapPkg2NS, model, ctrl, mapThing2JenaRes );
                    else 
                        resValue = model.createResource( ((RDFResource)value).getURI() );
                }
                else if( (value instanceof THING) )
                    resValue = ((RDFResource)value).asJenaResource( mapPkg2NS, model, ctrl, mapThing2JenaRes );
                
                if( resValue != null )
                    res.addProperty( prop, resValue );
            }
            else
            {
                Literal litValue = model.createLiteral( value );
                res.addProperty( prop, litValue );
            }
        }
    }
    
    return res;
}


public static class JenaAsResourceController
{
    /**
     * returns true if that property shall be hidden, false otherwise.
     * you can distinguish properties outgoing from different sources.
     */
    public boolean hideProperty( RDFResource source, Property prop )
    {
        return false;
    }

    /**
     * returns true if that property shall be expanded (recursion), false otherwise.
     * you can distinguish properties outgoing from different sources and incoming
     * to specific destinations.
     */
    public boolean expandProperty( RDFResource source, Property prop, RDFResource dest )
    {
        return true;
    }
    
} // end of class JenaAsResourceController 

//----------------------------------------------------------------------------------------------------
} // end of class RDFResource

