package de.dfki.rdf.util;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.dfki.util.debug.Debug;


/**
 * A KnowledgeBase maintains a set of "known" THINGS.
 * The KnowledgeBase also knows about mapping packages to namespaces and vice versa.
 * You can put THINGS into and remove THINGS from this knowledge base.<br>
 * But the most important this is, you can also {@link #assign assign} THINGS to it.
 * This allows for an unspecified update of THINGS in the knowledge base. If you
 * receive some RDF containing updates for your knowledge, then this class will
 * help you with updating your graph in the knowledge base.  
 */
public class KnowledgeBase
{
//---------------------------------------------------------------------------
public final static String DEBUG_MODULE = "KnowledgeBase";
HashMap m_mapObjects = new HashMap();
private final static boolean MEASURE_TIME = false;

private Map/*String->String*/ m_mapNS2Pkg;
private Map/*String->String*/ m_mapPkg2NS;

private static KnowledgeBase ms_staticKnowledgeBase;


//---------------------------------------------------------------------------
public KnowledgeBase ()
{
}

//---------------------------------------------------------------------------
static
{
    ms_staticKnowledgeBase = new KnowledgeBase();
}

//---------------------------------------------------------------------------
/**
 * Returns the <b>static</b> KnowledgeBase object - 
 * this can be used to realise blackboard-like knowledge sharing.
 */
public static KnowledgeBase getStaticKnowledgeBase()
{
    return ms_staticKnowledgeBase;
}

//---------------------------------------------------------------------------
synchronized public void setNS2PkgMap( Map mapNS2Pkg )
{
	if( mapNS2Pkg == null ) return;
    if( m_mapNS2Pkg == null )
        m_mapNS2Pkg = mapNS2Pkg;
    else
        m_mapNS2Pkg.putAll( mapNS2Pkg );
}

//---------------------------------------------------------------------------
synchronized public void setPkg2NSMap( Map mapPkg2NS )
{
	if( mapPkg2NS == null ) return;
    if( m_mapPkg2NS == null )
        m_mapPkg2NS = mapPkg2NS;
    else
        m_mapPkg2NS.putAll( mapPkg2NS );
}

//---------------------------------------------------------------------------
public void clear ()
{
    synchronized( m_mapObjects )
    {
        m_mapObjects.clear();
    }
}

//---------------------------------------------------------------------------
public Object put (String sURIKey, Object obj)
{
    synchronized( m_mapObjects )
    {
        return m_mapObjects.put(sURIKey, obj);
    }
}

//---------------------------------------------------------------------------
public Object put (Object objKey, Object objValue)
{
    try 
    {
        if (objKey instanceof String)
            return put( (String)objKey, objValue );
        else 
        if (objKey instanceof org.w3c.rdf.model.Resource)
            return put( ((org.w3c.rdf.model.Resource)objKey).getURI(), objValue );
        else
            throw new Error("Wrong class (" + objKey.getClass() + ") used as key in dfki.rdf.util.KnowledgeBase . put");
    }
    catch (org.w3c.rdf.model.ModelException ex) {
        System.out.println("ModelException occurred in dfki.rdf.util.KnowledgeBase . put:\n" + ex.getMessage());
        ex.printStackTrace();
        throw new Error("ModelException occurred in dfki.rdf.util.KnowledgeBase . put:\n" + ex.getMessage());
    }
}

//---------------------------------------------------------------------------
public Object put (RDFResource obj)
{
    String sURI = obj.getURI();
    if (sURI == null) throw new Error("tried to put an obj in the map w/o URI in dfki.rdf.util.KnowledgeBase . put");
    return put(sURI, obj);
}

//---------------------------------------------------------------------------
protected Object put (Object obj)
{
    if (obj instanceof RDFResource)
        return put( (RDFResource)obj );
    else
        throw new Error("Wrong class in dfki.rdf.util.KnowledgeBase . put; class should have been a subclass dfki.rdf.util.RDFResource");
}

//---------------------------------------------------------------------------
public Object remove (String sURI)
{
    synchronized( m_mapObjects )
    {
        return m_mapObjects.remove(sURI);
    }
}

//---------------------------------------------------------------------------
public Object get (Object key)
{
    synchronized( m_mapObjects )
    {
        try {
            if (key == null)
                return null;
            if (key instanceof org.w3c.rdf.model.Resource)
                key = ((org.w3c.rdf.model.Resource)key).getURI();
            else
            if ( !(key instanceof String) )
                throw new Error("Wrong class in dfki.rdf.util.KnowledgeBase . get; key was no String (not even a Resource)");
            return m_mapObjects.get(key);
        }
        catch (org.w3c.rdf.model.ModelException ex) {
            System.out.println("ModelException occurred in dfki.rdf.util.KnowledgeBase . get: " + ex.getMessage());
            ex.printStackTrace();
            throw new Error("ModelException occurred in dfki.rdf.util.KnowledgeBase . get: " + ex.getMessage());
        }
    }
}    

//---------------------------------------------------------------------------
public void putAll (Map map)
{
    // for security reasons we better iterate :-(
    for (Iterator it = map.keySet().iterator(); it.hasNext(); )
    {
        Object objKey = it.next();
        Object objValue = map.get(objKey);
        put(objKey, objValue);
    }
}

//---------------------------------------------------------------------------
public Collection values ()
{
    synchronized( m_mapObjects )
    {
        return new HashSet( m_mapObjects.values() );
    }
}

//---------------------------------------------------------------------------
public Set keySet ()
{
    synchronized( m_mapObjects )
    {
        return new HashSet( m_mapObjects.keySet() );
    }
}

//---------------------------------------------------------------------------
public String toString ()
{
    synchronized( m_mapObjects )
    {
        StringBuffer sb = new StringBuffer();
        sb.append("----  KB [begin]  ---------------------------------------------------------\n");
        for (Iterator it = keySet().iterator(); it.hasNext(); )
        {
            Object key = it.next();
            Object obj = m_mapObjects.get(key);
            sb.append("\n::: " + key + " :::\n" + obj + "\n");
        }
        sb.append("----  KB [end]  -----------------------------------------------------------\n");
        return sb.toString();
    }
}    

//---------------------------------------------------------------------------
public void updateRDFResourceSlots ()
{
    Date dateNow = new Date();
    long lUpdateNumber = dateNow.getTime();

    long iTimeBeginning = ( MEASURE_TIME  ?  lUpdateNumber  :  0 );

    for (Iterator it = values().iterator(); it.hasNext(); )
    {
        Object obj = it.next();
        if (obj instanceof THING)
            ((THING)obj).updateRDFResourceSlots( this, lUpdateNumber );
    }

    if( MEASURE_TIME )
    {
        long iTimeEnd = new Date().getTime();
        if( iTimeEnd > iTimeBeginning )
            debug().message( "updateRDFResourceSlots: " + (iTimeEnd - iTimeBeginning) + " millis" );
    }
}

//---------------------------------------------------------------------------
public Collection/*THING*/ findSubjects( String sPropertyPkg, String sPropertyName, Object value )
{
    return findSubjects( sPropertyPkg, sPropertyName, value, false );
}

//---------------------------------------------------------------------------
public THING findFirstSubject( String sPropertyPkg, String sPropertyName, Object value )
{
    Collection coll = findSubjects( sPropertyPkg, sPropertyName, value, true );
    if( coll == null  ||  coll.isEmpty() ) return null;
    return (THING)coll.iterator().next();
}

//---------------------------------------------------------------------------
private Collection/*THING*/ findSubjects( String sPropertyPkg, String sPropertyName, Object value, boolean bOnlyOneResultNeeded )
{
    long iTimeBeginning = ( MEASURE_TIME  ?  new Date().getTime()  :  0 );

    HashSet/*THING*/ setSubjects = new HashSet();
    boolean bFoundOne = false;
    for (Iterator it = values().iterator(); it.hasNext(); )
    {
        Object obj = it.next();
        if ( !(obj instanceof THING) )
            continue;
        if( sPropertyPkg != null  &&  !obj.getClass().getPackage().equals( sPropertyPkg ) )
            continue;
        THING thing = (THING)obj;
        Collection collProperties = thing.getProperties();
        for (Iterator itProperties = collProperties.iterator(); itProperties.hasNext(); )
        {
            String sThingPropertyName = (String)itProperties.next();
            if( sPropertyName != null  &&  !sThingPropertyName.equals( sPropertyName ) )
                continue;
            Object objThingPropertyValue = thing.getPropertyValue( sThingPropertyName );
            if( objThingPropertyValue == null )
                continue;
            if( objThingPropertyValue instanceof Collection )
            {
                if( ((Collection)objThingPropertyValue).contains( value ) )
                {
                    setSubjects.add( thing );
                    bFoundOne = true;
                    break;
                }
            }
            else  // singe value slot
            {
                if( objThingPropertyValue.equals( value ) )
                {
                    setSubjects.add( thing );
                    bFoundOne = true;
                    break;
                }
            }
        }
        if( bOnlyOneResultNeeded && bFoundOne )
            break;
    }

    if( MEASURE_TIME )
    {
        long iTimeEnd = new Date().getTime();
        if( iTimeEnd > iTimeBeginning )
            debug().message( "findSubjects: " + (iTimeEnd - iTimeBeginning) + " millis" );
    }

    return setSubjects;
}

//---------------------------------------------------------------------------
public Collection/*THING*/ findInstances( Class cls )
{
    if( cls == null )
        return null;  // hey, what do you want?
    HashSet/*THING*/ setInstances = new HashSet();
    for (Iterator it = values().iterator(); it.hasNext(); )
    {
        Object obj = it.next();
        if( obj == null  ||  !(obj instanceof THING)  ||  !cls.isAssignableFrom( obj.getClass() ) )
            continue;
        setInstances.add( obj );
    }
    return setInstances;
}

//---------------------------------------------------------------------------
public void assign (THING thingToAssign)
{
    assign( thingToAssign, true /*update resource slots*/ );
}

//---------------------------------------------------------------------------
public void assign (THING thingToAssign, boolean bUpdateResourceSlots)
{
    long iTimeBeginning = ( MEASURE_TIME  ?  new Date().getTime()  :  0 );

    THING thingOld = (THING)get( thingToAssign.getURI() );
    if (thingOld != null)
        thingOld.assign( thingToAssign, this );
    else
    {
        put( thingToAssign );
        //  thingToAssign.assign( thingToAssign, this );  //2003-01-07: a first try
    }

    long iTimeAfterAssign = ( MEASURE_TIME  ?  new Date().getTime()  :  0 );

    if (bUpdateResourceSlots)
        updateRDFResourceSlots();

    if( MEASURE_TIME )
    {
        long iTimeEnd = new Date().getTime();
        if( iTimeEnd > iTimeBeginning )
            debug().message( "assign: " + (iTimeEnd         - iTimeBeginning)   + " millis (assign only: "
                                        + (iTimeAfterAssign - iTimeBeginning)   + " millis, updateRDFResourceSlots: "
                                        + (iTimeEnd         - iTimeAfterAssign) + " millis)" );
    }
}

//---------------------------------------------------------------------------
public void assignAllThings (Collection coll)
{
    long iTimeBeginning = ( MEASURE_TIME  ?  new Date().getTime()  :  0 );
    if( MEASURE_TIME ) debug().message( "\nassignAll: beginning..." );

    for (Iterator it = coll.iterator(); it.hasNext(); )
    {
        Object obj = it.next();
        if (obj instanceof THING)
            assign( (THING)obj, false /*defer resource slots*/ );
    }

    long iTimeAfterAssign = ( MEASURE_TIME  ?  new Date().getTime()  :  0 );

    updateRDFResourceSlots();

    if( MEASURE_TIME )
    {
        long iTimeEnd = new Date().getTime();
        debug().message( "assignAll: " + (iTimeEnd         - iTimeBeginning)   + " millis (assign only: "
                                       + (iTimeAfterAssign - iTimeBeginning)   + " millis, updateRDFResourceSlots: "
                                       + (iTimeEnd         - iTimeAfterAssign) + " millis)" );
    }
}

//---------------------------------------------------------------------------
private Debug debug()
{
    return Debug.forModule( DEBUG_MODULE );
}

//---------------------------------------------------------------------------
} // end of class KnowledgeBase

