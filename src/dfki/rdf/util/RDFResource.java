package dfki.rdf.util;

import java.io.Serializable;
import java.util.Collection;
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
abstract public static class WalkerController
{
    /**
     * (shortest) path from the starting resource to the current resource
     * including the used properties (edges) between the resources.<br>
     * this list carries information about the current state of the recursion:
     * from the starting resource to the current resource.<br>
     * <b>note:</b> this path does not only grow linearly, but changes even
     * on the head!
     * @see #lstWalk
     */
    public LinkedList/*RDFResource*/ lstPath = new LinkedList();

    /**
     * list (path) of all (up to now) visited resources and
     * the used properties (edges) between the resources.<br>
     * this list carries (cronological) information about complete (up to now)
     * walk through the resources graph.
     * @see #lstPath
     */
    public LinkedList/*RDFResource*/ lstWalk = new LinkedList();

    /**
     * override this method for special walking behavior.<br>
     * e.g. always returning false will implement just no walking at all,
     * whereas always returning true will walk every way available
     * (maybe endlessly).<br>
     * when going from from resource to some other resource, this method is
     * called to check whether the outgoing property (edge) can be walked on.<br>
     * the <b>default</b> behavior (if this method is not overridden) is:
     * the ways is not blocked, try to walk further!
     */ 
    public boolean walkingAllowed( RDFResource source, String prop )
    {
        return true;
    }
    
    /**
     * override this method for special walking behavior.<br>
     * e.g. always returning false will implement just no walking at all,
     * whereas always returning true will walk every way available
     * (maybe endlessly).<br>
     * after {@link #walkingAllowed(RDFResource,String)} tells us, the road
     * is free, this method tells us if the place we want to go to is free.
     * only if this test is positive we can go further.<br>
     * the <b>default</b> behavior (if this method is not overridden) is:
     * every resource is visited exactly once, i.e. if a resource has been
     * visited, every walk to this resource is not allowed.<br>
     * another meaningful implementation would be to walk all available "ways"
     * exactly once, whereas a "way" is something like this:
     * <code>source --&gt; prop --&gt; dest</code>.
     */ 
    public boolean walkingAllowed( RDFResource source, String prop, RDFResource dest )
    {
        return !alreadyVisitedOnWalk( dest );
        // return !alreadyWalkedThatWay( source, prop, dest );
    }

    /**
     * override this method if you want to be informed about the concrete
     * roads the walker walks along.<br>
     * this method is called while walking from one resource to other resource(s).
     * the destination resource(s) is/are not yet reached, but you can be sure,
     * that <i>all</i> destinations in <code>dest</code> will be reached!<br>
     */
    public void walkingAlongProperty( RDFResource source, String prop, Collection/*RDFResources*/ dest )
    {
    }
    
    /**
     * override this method if you want to be informed about the concrete
     * roads the walker walks along.<br>
     * this method is called when walking from one resource to other resource(s) is finished.
     * the destination resource(s) have all been reached.
     */
    public void returningFromProperty( RDFResource source, String prop, Collection/*RDFResources*/ dest )
    {
    }
    
    /**
     * override this method to specify the behavior of the walker -
     * this method is what a walker is about.<br>
     */
    abstract public void arriving( RDFResource currentResource );

    /**
     * you only need to override this method in special cases:
     * this method is called when the walker returns and leaves
     * a resource.
     * that resource will never be arrived again, but maybe it
     * will be revisited.
     * @see #revisiting
     */
    public void leaving( RDFResource currentResource )
    {
    }

    /**
     * this method is called when a resource is visited once again. 
     * @see #leavingAgain
     */
    public void arrivingAgain( RDFResource currentResource )
    {
    }

    /**
     * this method is called when leaving a resource which has been revisited. 
     * @see #arrivingAgain
     */
    public void leavingAgain( RDFResource currentResource )
    {
    }

    //-------------------------------------------------------------------------
    
    protected String getLastProperty()
    {
        if( lstPath.size() > 1 )
            return (String)lstPath.get( lstPath.size()-2 );
        else
            return null;
    }
    
    protected RDFResource getCurrentResource()
    {
        return (RDFResource)lstPath.getLast();
    }
    
    protected boolean alreadyVisitedOnPath( RDFResource res )
    {
        return ( lstPath.indexOf( res ) > 0  &&  lstPath.indexOf( res ) < lstPath.size()-1 );
    }
    
    protected boolean alreadyVisitedOnWalk( RDFResource res )
    {
        return ( lstWalk.indexOf( res ) > 0  &&  lstWalk.indexOf( res ) < lstWalk.size()-1 );
    }
    
    protected boolean alreadyWalkedThatWay( RDFResource source, String prop, RDFResource dest )
    {
        RDFResource last = null;
        for( Iterator it = lstWalk.iterator(); it.hasNext(); )
        {
            RDFResource s = last;
            if( !it.hasNext() ) break;
            String p = (String)it.next();
            if( !it.hasNext() ) break;
            RDFResource d = (RDFResource)it.next();
            if( source.equals( s ) && prop.equals( p ) && dest.equals( d ) )
                return true;
            last = d;
        }
        return false;
    }
    
} // end of inner class WalkerController


public void walk( WalkerController wc )
{
    walk( null, wc );
}

//----------------------------------------------------------------------------------------------------
public void walk( String sLastProperty, WalkerController wc )
{
    wc.lstPath.addLast( sLastProperty );
    wc.lstPath.addLast( this );
    
    wc.lstWalk.addLast( sLastProperty );
    wc.lstWalk.addLast( this );

    boolean bAlreadyVisited = wc.alreadyVisitedOnWalk( this ); 
    if( bAlreadyVisited )
        wc.arrivingAgain( this );
    else
        wc.arriving( this );
    
    
    for( Iterator itProperties = m_propertyStore.getProperties().iterator(); itProperties.hasNext(); )
    {
        String sPropName = (String)itProperties.next();
        if( !wc.walkingAllowed( this, sPropName ) )
            continue;
        
        PropertyInfo pi = m_propertyStore.getPropertyInfo( sPropName );
        Collection/*RDFResource*/ collDestinations = new LinkedList();
        if( pi.hasMultiValue() )
        {
            Collection collPropValues = (Collection)pi.getValue();
            for( Iterator it = collPropValues.iterator(); it.hasNext(); )
            {
                Object value = it.next();
                if( !(value instanceof RDFResource) )
                    continue;
                RDFResource resDest = (RDFResource)value;
                if( wc.walkingAllowed( this, sPropName, resDest ) )
                    collDestinations.add( resDest );
            }
        }
        else
        {
            Object value = pi.getValue();
            if( value != null && (value instanceof RDFResource) )
            {
                if( wc.walkingAllowed( this, sPropName, (RDFResource)value ) )
                    collDestinations.add( value );
            }
        }

        if( collDestinations.size() > 0 )
        {
            wc.walkingAlongProperty( this, sPropName, collDestinations );
            for( Iterator it = collDestinations.iterator(); it.hasNext(); )
            {
                RDFResource resDest = (RDFResource)it.next();
                resDest.walk( sPropName, wc );
            }
            wc.returningFromProperty( this, sPropName, collDestinations );
        }
         
    }
    
    if( bAlreadyVisited )
        wc.leavingAgain( this );
    else
        wc.leaving( this );
    
    if( wc.lstPath.removeLast() !=  this )
        debug().error( "implementation failure in THING.walk" );
    if( wc.lstPath.removeLast() !=  sLastProperty )
        debug().error( "implementation failure in THING.walk" );
    
    wc.lstWalk.addLast( "inv(" + sLastProperty + ")" );
    if( wc.lstPath.size() > 0 ) wc.lstWalk.addLast( wc.lstPath.getLast() );
}


//----------------------------------------------------------------------------------------------------
public String toStringAsRDF()
{
    return toStringAsRDF( null, null );
}

public String toStringAsRDF( final Map/*String->String*/ mapPkg2NS, String sRdfsNamespace )
{
    final RDFResource resPredAbout = new RDFResource( RDF.DEFAULT_SYNTAX_NAMESPACE, "about" );
    final RDFResource resPredResource = new RDFResource( RDF.DEFAULT_SYNTAX_NAMESPACE, "resource" );
    
    final TinyXMLDocument xmlDoc = new TinyXMLDocument( null, sRdfsNamespace );
    final TinyXMLElement elDoc = xmlDoc.createElement( RDF.DEFAULT_SYNTAX_NAMESPACE + "RDF" );
    xmlDoc.setDocumentElement( elDoc );
    
    final Map/*RDFResource->TinyXMLElement*/ mapResource2XMLElement = new HashMap();
    
    RDFResource.WalkerController wc = new RDFResource.WalkerController() {

        public void arriving( RDFResource currentResource )
        {
            TinyXMLElement elParent = elDoc;
            if( lstPath.size() > 2 ) 
                elParent = (TinyXMLElement)mapResource2XMLElement.get( lstPath.get( lstPath.size()-3 ) );
            
            TinyXMLElement elAppendHere = elParent;

            Class cls = currentResource.getClass();
            String sClsPackage = getClassPackage( cls );
            String sClsName = getClassName( cls );
            String sNamespace = ( mapPkg2NS != null  ?  (String)mapPkg2NS.get( sClsPackage )  :  "http://" + sClsPackage + "#" );

            String sLastProperty = getLastProperty();
            if( sLastProperty != null ) 
            {
                RDFResource resLastProperty = new RDFResource( sNamespace, sLastProperty );
                TinyXMLElement elLastProperty = xmlDoc.createElement( resLastProperty.getURI() );
                elParent.appendChild( elLastProperty );
                elAppendHere = elLastProperty;
            }

            RDFResource resCls = new RDFResource( sNamespace, sClsName );
            
            TinyXMLElement elInst = xmlDoc.createElement( resCls.getURI() );
            elInst.setAttribute( resPredAbout.getURI(), currentResource );
            elAppendHere.appendChild( elInst );
            
            mapResource2XMLElement.put( currentResource, elInst );

            if(     currentResource.getLabel() != null &&
                    !currentResource.getLabel().equals( currentResource.getURI() ) )
            {
                RDFResource resProperty = new RDFResource( xmlDoc.RDFS_NAMESPACE, "label" );
                elInst.setAttribute( resProperty.getURI(), currentResource.getLabel() );
            }
            Collection/*PropertyInfo*/ collPropInfos = currentResource.getPropertyStore().getPropertyInfos();
            for( Iterator it = collPropInfos.iterator(); it.hasNext(); )
            {
                PropertyInfo pi = (PropertyInfo)it.next();
                if( pi.getValue() == null ) continue;
                if(     pi.getValueType() == PropertyInfo.VT_STRING ||
                        pi.getValueType() == PropertyInfo.VT_SYMBOL )
                {
                    RDFResource resProperty = new RDFResource( sNamespace, pi.getName() );
                    appendSlot( elInst, resProperty, (String)pi.getValue() );
                }
            }
        }

        private void appendSlot( TinyXMLElement elInst, RDFResource resPred, String sValue )
        {
            if( TinyXMLTextNode.containsIllegalChars( sValue ) )
            {
                TinyXMLElement elSlot = xmlDoc.createElement( resPred.getURI() );
                elInst.appendChild( elSlot );
                TinyXMLTextNode txtValue = createTextNode( sValue );
                elSlot.appendChild( txtValue );
            }
            else
            {
                elInst.setAttribute( resPred.getNamespace() + resPred.getLocalName(), sValue );
            }
        }

        private TinyXMLTextNode createTextNode( String sText )
        {
            if( TinyXMLTextNode.containsIllegalChars( sText ) )
                return xmlDoc.createCDATA( sText );
            else
                return xmlDoc.createTextNode( sText );
        }

        Set/*RDFResource*/ setProcessedResources = new HashSet();
        
        public void leaving( RDFResource currentResource )
        {
            setProcessedResources.add( currentResource );
        }
        
        public void arrivingAgain( RDFResource currentResource )
        {
            if( lstPath.size() <= 3 )
                throw new Error( "failure in RDFResource.walk" );
            
            TinyXMLElement elParent = (TinyXMLElement)mapResource2XMLElement.get( lstPath.get( lstPath.size()-3 ) );
            
            String sLastProperty = getLastProperty();
            if( sLastProperty != null ) 
            {
                Class cls = currentResource.getClass();
                String sClsPackage = getClassPackage( cls );
                String sNamespace = ( mapPkg2NS != null  ?  (String)mapPkg2NS.get( sClsPackage )  :  "http://" + sClsPackage + "#" );
                RDFResource resLastProperty = new RDFResource( sNamespace, sLastProperty );
                
                TinyXMLElement elLastProperty = xmlDoc.createElement( resLastProperty.getURI() );
                elParent.appendChild( elLastProperty );
                
                elLastProperty.setAttribute( resPredResource.getURI(), currentResource );
            }
            else
                throw new Error( "failure in RDFResource.walk" );
        }
        
        public boolean walkingAllowed( RDFResource source, String prop, RDFResource dest )
        {
            if( alreadyVisitedOnWalk( source ) )
            {
                if( alreadyVisitedOnPath( source ) )
                    return false;
                else
                    return !setProcessedResources.contains( source );
            }
            return true;
        }
    };
    this.walk( wc );
    
    return xmlDoc.serialize();

}

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

