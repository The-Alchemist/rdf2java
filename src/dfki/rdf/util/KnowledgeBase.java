package dfki.rdf.util;

import java.util.*;


public class KnowledgeBase
{
//---------------------------------------------------------------------------
HashMap m_mapObjects = new HashMap();

//---------------------------------------------------------------------------
public KnowledgeBase ()
{
}

//---------------------------------------------------------------------------
public void clear ()
{
    m_mapObjects.clear();
}

//---------------------------------------------------------------------------
public Object put (String sURIKey, Object obj)
{
    return m_mapObjects.put(sURIKey, obj);
}

//---------------------------------------------------------------------------
public Object put (Object objKey, Object objValue)
{
    try {
        if (objKey instanceof String)
            return put( (String)objKey, objValue );
        else
        if (objKey instanceof org.w3c.rdf.model.Resource)
            return put( ((org.w3c.rdf.model.Resource)objKey).getURI(), objValue );
        else
            throw new Error("Wrong class (" + objKey.getClass() + ") used as key in dfki.rdf.util.KnowledgeBase . put");
    }
    catch (org.w3c.rdf.model.ModelException ex) {
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
    return m_mapObjects.remove(sURI);
}

//---------------------------------------------------------------------------
public Object get (Object key)
{
    try {
        if (key instanceof org.w3c.rdf.model.Resource)
            key = ((org.w3c.rdf.model.Resource)key).getURI();
        else
        if ( !(key instanceof String) )
            throw new Error("Wrong class in dfki.rdf.util.KnowledgeBase . get; key was no String (not even a Resource)");
        return m_mapObjects.get(key);
    }
    catch (org.w3c.rdf.model.ModelException ex) {
        throw new Error("ModelException occurred in dfki.rdf.util.KnowledgeBase . get: " + ex.getMessage());
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
    return m_mapObjects.values();
}

//---------------------------------------------------------------------------
public Set keySet ()
{
    return m_mapObjects.keySet();
}

//---------------------------------------------------------------------------
public String toString ()
{
    StringBuffer sb = new StringBuffer();
    sb.append("----  KB [begin]  ---------------------------------------------------------\n");
    for (Iterator it = m_mapObjects.keySet().iterator(); it.hasNext(); )
    {
        Object key = it.next();
        Object obj = m_mapObjects.get(key);
        sb.append("\n::: " + key + " :::\n" + obj + "\n");
    }
    sb.append("----  KB [end]  -----------------------------------------------------------\n");
    return sb.toString();
}

//---------------------------------------------------------------------------
public void updateRDFResourceSlots ()
{
    for (Iterator it = m_mapObjects.values().iterator(); it.hasNext(); )
    {
        Object obj = it.next();
        if (obj instanceof THING)
            ((THING)obj).updateRDFResourceSlots(this);
    }
}

//---------------------------------------------------------------------------
public void assign (THING thingToAssign)
{
    THING thingOld = (THING)get(thingToAssign.getURI());
    if (thingOld != null)
        thingOld.assign(thingToAssign, this);
    else
        put(thingToAssign);

    updateRDFResourceSlots();
}

//---------------------------------------------------------------------------
public void assignAllThings (Collection coll)
{
    for (Iterator it = coll.iterator(); it.hasNext(); )
    {
        Object obj = it.next();
        if (obj instanceof THING)
            assign( (THING)obj );
    }
}

//---------------------------------------------------------------------------
} // end of class KnowledgeBase

