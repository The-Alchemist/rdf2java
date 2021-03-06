package de.dfki.rdf.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dfki.util.debug.Debug;


/** Root object of all Java-Objects generated by the {@link RDFS2Class RDFS2Class} tool.
  * <p>
  * Every generated Java object corresponds to a specific RDFS class.
  * If such an RDFS class doesn't have a super class, i.e. has a <code>rdfs:subClassOf</code> link
  * to another RDFS class, then the generated Java class for this RDFS class <code>extends</code>
  * this <code>THING</code> class.
  * <p>
  * This class does already contain some usefull slots and methods, such as for example
  * the getter and putter method of the <code>URI</code> of the corresponding RDF object.
  * <p>
  * @author  Sven.Schwarz@dfki.de
  * @version 1.0
  */
public class THING   extends RDFResource
                     implements Serializable
{
//----------------------------------------------------------------------------------------------------
public final static String DEBUG_MODULE = "rdf2java";
final static protected String DEFAULT_NAMESPACE = "http://dfki.rdf.util.rdf2java/default#";

//----------------------------------------------------------------------------------------------------
public static Debug debug()
{
    return Debug.forModule( DEBUG_MODULE );
}

//----------------------------------------------------------------------------------------------------
/** used in method <code>updateRDFResourceSlots</code> --
  * shows if this THING has already been visited **/
private long m_lLastUpdateNumber = 0;


//----------------------------------------------------------------------------------------------------
/** <code>toString()<code> stuff...
  */
public String toString ()
{
    return super.toString();
    //SS:2004-08-05: return toString("");
}

//----------------------------------------------------------------------------------------------------
/** <code>toString()<code> stuff...
  */
public String toString (String sIndent)
{
    return toString_userDefined(sIndent, true);
}

//----------------------------------------------------------------------------------------------------
/** <code>toString()<code> stuff...
  */
public String toString (String sIndent, boolean bIndentDirectly)
{
    StringBuffer sb = new StringBuffer();
    if (bIndentDirectly)
        sb.append(sIndent);
    sb.append(getClassName() + " " + getAddress() + " ");
    if (getURI() != null)
        sb.append("URI=\"" + getURI() + "\" ");
//    sb.append("{");
    sb.append("\n");
    toString(sb, sIndent);
//    sb.append(sIndent + "}\n");
    return sb.toString();
}

//----------------------------------------------------------------------------------------------------
/** <code>toString()<code> stuff: <b>this method is overloaded for subclasses of <code>THING</code>!</b>
  */
public void toString (StringBuffer sb, String sIndent)  // overload this method
{
}

//----------------------------------------------------------------------------------------------------
public String toString_userDefined (String sIndent, boolean bIndentDirectly)
{
    return toString(sIndent, bIndentDirectly);
}

//----------------------------------------------------------------------------------------------------
/** <code>toString()<code> stuff: <b>this method can be overloaded for subclasses of <code>THING</code>!</b>
  */
public String toStringShort ()                          // overload this method
{
    return "<" + getClassNameShort() + " " +
           getAddress() +
           ( getURI() != null  ?  " URI=\"" + getURI() + "\""  :  "") +
           ">";
}

//----------------------------------------------------------------------------------------------------
/** Creates a new URI (with default namespace) and {@link #putURI stores} this URI in this object.
  */
public String makeNewURI ()
{
    return makeNewURI(DEFAULT_NAMESPACE);
}

//----------------------------------------------------------------------------------------------------
/** Creates a new URI (with given namespace) and {@link #putURI stores} this URI in this object.
  */
public String makeNewURI (String sNamespace)
{
    String sNewURI = sNamespace + "id_" + prefixForToday() + "_" + getAddressOnlyHex();
    putURI(sNewURI);
    return sNewURI;
}

//----------------------------------------------------------------------------------------------------
public static String prefixForToday ()
{
    java.text.SimpleDateFormat dateFormatter = new java.text.SimpleDateFormat( "yyyyMMdd'_'HHmmss" );
    return dateFormatter.format( new Date() );
}

//----------------------------------------------------------------------------------------------------
/** Creates a new URI for this object, stores it in the object and in the given map.
  */
public void addToMap (Map mapObjects, String sNamespace)
{
    try {
        if (getURI() == null)
            makeNewURI(sNamespace);
        mapObjects.put(getURI(), this);
    }
    catch (Exception ex)
    {
    	Logger.getLogger("de.dfki.rdf.util").log(Level.SEVERE, "addToMap", ex);
    }
}

//----------------------------------------------------------------------------------------------------
/** Creates a new URI for this object, stores it in the object and in the given map.
  */
public void addToMap (Map mapObjects)
{
    addToMap(mapObjects, DEFAULT_NAMESPACE);
}

//----------------------------------------------------------------------------------------------------
public Object getPropertyValue (String sPropertyName)
{
    if( m_propertyStore != null )
        return m_propertyStore.getPropertyValue( sPropertyName );
    else
        return RDF2Java.getPropertyValue( this, sPropertyName );
}

//----------------------------------------------------------------------------------------------------
public void putPropertyValue (String sPropertyName, Object value)
{
    if( m_propertyStore != null )
        m_propertyStore.putPropertyValue( sPropertyName, value );
    else
        RDF2Java.putPropertyValue( this, sPropertyName, value );
}

//----------------------------------------------------------------------------------------------------
public void clearPropertyValues (String sPropertyName)
{
    if( m_propertyStore != null )
        m_propertyStore.clearPropertyValue( sPropertyName );
    else
        RDF2Java.clearPropertyValues( this, sPropertyName );
}

//----------------------------------------------------------------------------------------------------
public void updateRDFResourceSlots (KnowledgeBase kbCachedObjects, long lUpdateNumber)
{
    try
    {
        if (m_lLastUpdateNumber == lUpdateNumber) return;  // already visited this THING
        m_lLastUpdateNumber = lUpdateNumber;

        //SS:2002-12-18, 2003-01-07:
        if( kbCachedObjects.get( getURI() ) == null )
            kbCachedObjects.put( this );

        Collection collProperties = getProperties();
        for (Iterator itProperties = collProperties.iterator(); itProperties.hasNext(); )
        {
            String sPropertyName = (String)itProperties.next();
            Object objPropValue = getPropertyValue( sPropertyName );
            if (objPropValue == null)
                continue;
            if (objPropValue instanceof Collection)
            {
                Collection listOldValues = new LinkedList( (Collection)objPropValue );
                clearPropertyValues( sPropertyName );
                for (Iterator itPropValues = listOldValues.iterator(); itPropValues.hasNext(); )
                {
                    Object objPropValueElement = itPropValues.next();
                    if (objPropValueElement instanceof RDFResource)
                    {
                        RDFResource res = (RDFResource)objPropValueElement;
                        Object cachedObject = kbCachedObjects.get(res.getURI());
                        if (cachedObject != null && (cachedObject instanceof THING))
                        {
                            putPropertyValue( sPropertyName, cachedObject );
                            ((THING)cachedObject).updateRDFResourceSlots( kbCachedObjects, lUpdateNumber );
                            continue;
                        }
                    }
                    // nothing better available => take the old slot value again
                    putPropertyValue( sPropertyName, objPropValueElement );
                    if (objPropValueElement instanceof THING)
                        ((THING)objPropValueElement).updateRDFResourceSlots( kbCachedObjects, lUpdateNumber );
                }
            }
            else
            if (objPropValue instanceof RDFResource)
            {
                if (objPropValue instanceof THING)
                {
                    ((THING)objPropValue).updateRDFResourceSlots( kbCachedObjects, lUpdateNumber );
                    continue;
                }

                try {
                    RDFResource propValue = (RDFResource)objPropValue;
                    Object cachedObject = kbCachedObjects.get(propValue.getURI());
                    if (cachedObject != null && (cachedObject instanceof THING))
                    {
                        putPropertyValue( sPropertyName, cachedObject );
                        ((THING)cachedObject).updateRDFResourceSlots( kbCachedObjects, lUpdateNumber );
                        continue;
                    }
                    // nothing better available => take the old slot value again
                    putPropertyValue( sPropertyName, propValue );
                }
                catch (Exception ex) {
                    System.out.println("Exception (" + ex.getClass() + ") occurred: "+ex.getMessage());
                    ex.printStackTrace();
                    throw new Error(ex.getMessage());
                }
            }
            else
            if ( !(objPropValue instanceof String) )
                throw new Error("Wrong class for objValue in dfki.rdf.util.THING . updateRDFResourceSlots; class=" + objPropValue.getClass());
        }
    }
    catch (Exception ex)
    {
    	Logger.getLogger("de.dfki.rdf.util").log(Level.SEVERE, "updateRDFResourceSlots", ex);
    }
}

//----------------------------------------------------------------------------------------------------
public void assign (THING newThing, KnowledgeBase kb)
{
    try
    {
        Collection collProperties = getProperties();
        for (Iterator itProperties = collProperties.iterator(); itProperties.hasNext(); )
        {
            String sPropertyName = (String)itProperties.next();
            Object objPropValue = getPropertyValue( sPropertyName );
            if (objPropValue instanceof Collection)
            {
                LinkedList lstOldValues = new LinkedList( (Collection)objPropValue );

                Object objPropNewValue = newThing.getPropertyValue( sPropertyName );
                LinkedList lstNewValues = new LinkedList( (Collection)objPropNewValue );

                clearPropertyValues( sPropertyName );  // clear old values
                assignValues( lstOldValues, lstNewValues, sPropertyName, kb );
            }
            else
            {
                // 2 cases:  (a) objPropValue == null  OR  (b) objPropValue is no Collection

                LinkedList lstOldValues = new LinkedList();
                if (objPropValue != null) lstOldValues.add( objPropValue );

                LinkedList lstNewValues = new LinkedList();
                Object objPropNewValue = newThing.getPropertyValue( sPropertyName );
                if (objPropNewValue != null) lstNewValues.add( objPropNewValue );

                if (objPropValue != null)
                {
                    // clear old value
                    putPropertyValue( sPropertyName, null );  // is *now* possible :-)
                }

                assignValues(lstOldValues, lstNewValues, sPropertyName, kb);
            }
        }
    }
    catch (Exception ex)
    {
    	Logger.getLogger("de.dfki.rdf.util").log(Level.SEVERE, "assign", ex);
    }
}

//----------------------------------------------------------------------------------------------------
void assignValues (Collection collOldValues, Collection collNewValues,
                   String sPropertyName, KnowledgeBase kb)   throws Exception
{
    for (Iterator itOldValues = collOldValues.iterator(); itOldValues.hasNext(); )
    {
        Object oldValue = itOldValues.next();
        if ( oldValue == null )
            continue;
        if ( !(oldValue instanceof RDFResource) )
            continue;  // slot value is a LITERAL => handle that case below => assign NEW slot value (overwrite old value)
        Object newValue = find(collNewValues, ((RDFResource)oldValue).getURI());

        // if newValue == null, then the old slot value (subA_this) has to be removed
        // as this is already done (in the calling assign method), nothing has to be done in that case
        if (newValue == null)
            continue;

        // if the new slot value gives us an increase of quality, we take it!
        // this can only be the case iff newValue is a THING (and not an URI reference)
        // as this case is handled deeper below, too, we only do the other case here:
        // A L T H O U G H :   note, that this disturbs the order of the slot values !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if ( newValue instanceof THING )
            continue;  // newValue is not removed from collNewValues and will therfore be handled again below

        // insert the newer slot value (that is the old one)
        putPropertyValue( sPropertyName, oldValue );
        // mark, that we've already handled that new slot value (inspected below)
        remove(collNewValues, ((RDFResource)newValue).getURI());

        //  //2003-01-07: a first try
        //  if( oldValue instanceof THING )
        //      kb.put( oldValue );
    }

    // if collNewValues still contains some slot values => add them all
    for (Iterator itNewValues = collNewValues.iterator(); itNewValues.hasNext(); )
    {
        Object newValue = itNewValues.next();
        // insert the newer slot value (that is the new one)
        putPropertyValue( sPropertyName, newValue );

        if (newValue instanceof THING)
            kb.put( newValue );     // now the new Java object exists in the knowledge base, too
    }
}

//----------------------------------------------------------------------------------------------------
public static Object find (Collection collOther, String sURI)
{
    for (Iterator itOthers = collOther.iterator(); itOthers.hasNext(); )
    {
        Object other = itOthers.next();
        if ( (other instanceof RDFResource) &&
             ((RDFResource)other).getURI().equals(sURI) )
            return other;
    }
    return null;  // not found
}

//----------------------------------------------------------------------------------------------------
public static void remove (Collection coll, String sURI)
{
    for (Iterator it = coll.iterator(); it.hasNext(); )
    {
        Object obj = it.next();
        if ( (obj instanceof RDFResource) &&
             ((RDFResource)obj).getURI().equals(sURI) )
        {
            it.remove();
            return;
        }
    }
}



//----------------------------------------------------------------------------------------------------
public static class DeepCopyController
{
    public Map/*String->THING*/ mapOldThingNewThing = new HashMap();

    /**
     * path along the copied (old) THINGS
     */
    public LinkedList/*THING*/ lstCopyPath = new LinkedList();

    /**
     * override this method for special copy behavior - 
     * e.g. always returning false will implement shallow copying.
     * <br>note: <code>sourceObject</code> is the object to be copied <i>from</i>.  
     */ 
    public boolean allowDeepCopy( Object sourceObject, String sSourcePropertyName )
    {
        // maybe test sSourcePropertyName   and/or   sourceObject   and/or   sourceObject class
        return true;
    }
    
    /**
     * override this method to realize some special behavior for some property/properties.
     * <br>return true for default behavior and false if you want to handle that property
     * yourself. in the latter case you will most probably handle that property in some
     * if-clause in this (overridden) method.
     */
    public boolean allowDefaultHandlingForProperty( Object sourceObject, Object targetObject, String sSourcePropertyName )
    {
        return true;
    }
    
    /**
     * override this method to handle the case when properties differ 
     * between source and target object - 
     * this method is invoked when the target object does <i>not</i> contain a property,
     * the source object contains. 
     */
    public void targetMissesProperty( Object sourceObject, Object targetObject, String sSourcePropertyName )
    {
        debug().error( "*** targetMissesProperty( " + sourceObject.getClass().getName() + ", " + targetObject.getClass().getName() + ", " + sSourcePropertyName + ")" );
    }

    /**
     * override this method to handle the case when properties differ 
     * between source and target object - 
     * this method is invoked when the target object contains a property,
     * the source object does <i>not</i> contain. 
     */
    public void sourceMissesProperty( Object sourceObject, Object targetObject, String sTargetPropertyName )
    {
        debug().error( "*** sourceMissesProperty( " + sourceObject.getClass().getName() + ", " + targetObject.getClass().getName() + ", " + sTargetPropertyName + ")" );
    }
    
    /** 
     * override this method <i>only</i> if you want to realize a different 
     * behavior for creating new THINGs <b>or</b>
     * if you want to get informed about the creation of THINGs during a
     * deep copy.   
     */
    public THING createNewThing( Class cls )   throws CloneNotSupportedException
    {
        try
        {
            THING newThing = (THING)cls.newInstance();
            return newThing;
        }
        catch( InstantiationException e )
        {
            CloneNotSupportedException ex = new CloneNotSupportedException( e.getMessage() );
            ex.initCause( e );
            throw ex;            
        }
        catch( IllegalAccessException e )
        {
            CloneNotSupportedException ex = new CloneNotSupportedException( e.getMessage() );
            ex.initCause( e );
            throw ex;
        }
    }
    
    /** 
     * This flag controls the URIs of newly created resources.<br> 
     * <code>keepExistingURIs</code> = true: URIs are copied 
     *   from the source resources<br>
     * <code>keepExistingURIs</code> = false: all cloned resources
     *   get a new URI if the source resources has one.  
     */
    public boolean keepExistingURIs = false;
    
} // end of inner class DeepCopyController


//----------------------------------------------------------------------------------------------------
public THING deepCopy( DeepCopyController dcc )   
      throws CloneNotSupportedException
{
    THING newThing = (THING)dcc.mapOldThingNewThing.get( this );
    if( newThing != null )
        return newThing;   // this thing has been cloned already
    
    if( this.getURI() == null )
    {
        // This THING has no URI => two alternatives:
        // (1)  (Deep) copy nevertheless; the new (copied) THING will have no URI, too.
        //      ->  This is our case, see below!
        // (2)  Reference the same THING (i.e. this THING) and stop copying here.
        //      ->  We did *not* chose this alternative.
    }

    newThing = dcc.createNewThing( this.getClass() );
    
    deepCopyTo( newThing, dcc );
    return newThing;
}

//----------------------------------------------------------------------------------------------------
public THING deepCopy( Class clsNewThing, DeepCopyController dcc )   
      throws CloneNotSupportedException
{
    THING newThing = (THING)dcc.createNewThing( clsNewThing );
    this.deepCopyTo( newThing, dcc );
    return newThing;
}

//----------------------------------------------------------------------------------------------------
public void deepCopyTo( THING newThing, DeepCopyController dcc )   
      throws CloneNotSupportedException
{
    dcc.lstCopyPath.addLast( this );
    
    if( this.getURI() != null )
    {
        if(         !dcc.keepExistingURIs && newThing.getURI() == null )
            newThing.makeNewURI();
        else if(     dcc.keepExistingURIs && newThing.getURI() == null )
            newThing.putURI( this.getURI() );
    }
    
    dcc.mapOldThingNewThing.put( this, newThing );
    
    for( Iterator itProperties = m_propertyStore.getProperties().iterator(); itProperties.hasNext(); )
    {
        String sPropName = (String)itProperties.next();
        PropertyInfo pi = m_propertyStore.getPropertyInfo( sPropName );
        if( newThing.m_propertyStore.getPropertyInfo( sPropName ) != null )
        {
            // newThing contains this property, too
            if( pi.hasMultiValue() )
                deepCopy_multiValueProperty( newThing, pi, dcc );
            else
                deepCopy_singleValueProperty( newThing, pi.getName(), pi.getValue(), dcc );
        }
        else
        {
            // newThing does *not* contain this property
            dcc.targetMissesProperty( this, newThing, sPropName );
        }
    }
    
    for( Iterator itTargetProperties = newThing.m_propertyStore.getProperties().iterator(); 
         itTargetProperties.hasNext(); )
    {
        String sTargetPropName = (String)itTargetProperties.next();
        PropertyInfo pi = m_propertyStore.getPropertyInfo( sTargetPropName );
        if( this.m_propertyStore.getPropertyInfo( sTargetPropName ) == null )
        {
            // newThing contains a property, which this (the old thing) does *not* contain
            dcc.sourceMissesProperty( this, newThing, sTargetPropName );
        }
    }

    if( dcc.lstCopyPath.removeLast() !=  this )
        debug().error( "implementation failure in THING.deepCopyTo" );
}

//----------------------------------------------------------------------------------------------------
protected void deepCopy_multiValueProperty( THING newThing, PropertyInfo pi,
                                            DeepCopyController dcc )   
        throws CloneNotSupportedException
{
    String sPropName = pi.getName();
    Collection collPropValues = (Collection)pi.getValue();
    for( Iterator it = collPropValues.iterator(); it.hasNext(); )
    {
        Object value = it.next();
        deepCopy_singleValueProperty( newThing, sPropName, value, dcc );
    }
}

//----------------------------------------------------------------------------------------------------
protected void deepCopy_singleValueProperty( THING newThing, String sPropName, Object value, 
                                             DeepCopyController dcc ) 
        throws CloneNotSupportedException
{
    if( dcc.allowDefaultHandlingForProperty( this, newThing, sPropName ) )
    {
        Object newValue = value;
        if( dcc.allowDeepCopy( this, sPropName ) )
        {
            if( value instanceof THING )
            {
                newValue = ((THING)value).deepCopy( dcc );
            }
        }
        newThing.putPropertyValue( sPropName, newValue );
    }
}


//----------------------------------------------------------------------------------------------------
}

