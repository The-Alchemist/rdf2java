package dfki.rdf.util;

import java.util.*;
import org.w3c.rdf.model.*;
import java.lang.reflect.*;


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
public class THING
{
//----------------------------------------------------------------------------------------------------
/** the <code>URI</code> of the corresponding RDF object
  */
String m_sURI;

//----------------------------------------------------------------------------------------------------
/** Sets the <code>URI</code> of the corresponding RDF object.
  */
public void putURI (String sURI)
{
    m_sURI = sURI;
}

//----------------------------------------------------------------------------------------------------
/** Gets the <code>URI</code> of the corresponding RDF object.
  */
public String getURI ()
{
    return m_sURI;
}

//----------------------------------------------------------------------------------------------------
/** the <code>label</code> of the corresponding RDF object (Protege)
  */
String m_sLabel;

//----------------------------------------------------------------------------------------------------
/** Sets the <code>label</code> of the corresponding RDF object (Protege).
  */
public void putLabel (String sLabel)
{
    m_sLabel = sLabel;
}

//----------------------------------------------------------------------------------------------------
/** Gets the <code>label</code> of the corresponding RDF object (Protege).
  */
public String getLabel ()
{
    return m_sLabel;
}

//----------------------------------------------------------------------------------------------------
/** <code>toString()<code> stuff...
  */
public String toString ()
{
    return toString("");
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
    if (m_sURI != null)
        sb.append("URI=\"" + m_sURI + "\" ");
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
    return getClassNameShort() + " " +
           getAddress() +
           ( m_sURI != null  ?  " URI=\"" + m_sURI + "\""  :  "");
}

//----------------------------------------------------------------------------------------------------
/** Gets the class name of this object.
  * <br>
  * <b>Note:</b> The method must be declared protected in order to avoid
  * unwanted slot generation when using <code>RDFExport</code> (Java-to-RDF)
  * of Michael Sintek's <code>rdf2java</code> tool
  */
protected String getClassName ()
{
    return getClassNameShort() + " (" + getClass().getName() + ")";
}

//----------------------------------------------------------------------------------------------------
/** Gets a short version (without package) of the class name of this object.
  * <br>
  * <b>Note:</b> The method must be declared protected in order to avoid
  * unwanted slot generation when using <code>RDFExport</code> (Java-to-RDF)
  * of Michael Sintek's <code>rdf2java</code> tool
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
  * The string is prefixed with a <code>'@'</code> character.<br>
  * <b>Note:</b> The method must be declared protected in order to avoid
  * unwanted slot generation when using <code>RDFExport</code> (Java-to-RDF)
  * of Michael Sintek's <code>rdf2java</code> tool
  */
protected String getAddress ()
{
    return "@" + Integer.toHexString(this.hashCode());
}

//----------------------------------------------------------------------------------------------------
/** Gets a string showing the address of this object in hex notation.
  * <br>
  * The string is <b>not</b> prefixed with a <code>'@'</code> character.<br>
  * <b>Note:</b> The method must be declared protected in order to avoid
  * unwanted slot generation when using <code>RDFExport</code> (Java-to-RDF)
  * of Michael Sintek's <code>rdf2java</code> tool
  */
protected String getAddressOnlyHex ()
{
    return Integer.toHexString(this.hashCode());
}

//----------------------------------------------------------------------------------------------------
/** Creates a new URI (with default namespace) and {@link #putURI stores} this URI in this object.
  */
public String makeNewURI ()
{
    return makeNewURI("http://dfki.frodo/default#");
}

//----------------------------------------------------------------------------------------------------
/** Creates a new URI (with given namespace) and {@link #putURI stores} this URI in this object.
  */
public String makeNewURI (String sNamespace)
{
    Date date = new Date();
    String sNewURI = sNamespace + "id_" + date.getTime() + "_" + getAddressOnlyHex();
    putURI(sNewURI);
    return sNewURI;
}

//----------------------------------------------------------------------------------------------------
/** Creates a new URI for this object, stores it in the object and in the given map.
  */
public void addToMap (Map mapObjects, String sNamespace)
{
    try {
        makeNewURI(sNamespace);
        Resource resNewURI = dfki.util.rdf.RDF.factory().getNodeFactory().createResource(getURI());
        mapObjects.put(resNewURI, this);
    }
    catch (Exception ex)
    {
        System.out.println("Exception occurred: "+ex.getMessage());
        ex.printStackTrace();
        throw new Error(ex.getMessage());
    }
}

//----------------------------------------------------------------------------------------------------
/** Creates a new URI for this object, stores it in the object and in the given map.
  */
public void addToMap (Map mapObjects)
{
    addToMap(mapObjects, "http://dfki.frodo/default#");
}

//----------------------------------------------------------------------------------------------------
public boolean equals (Object other)
{
    // objects are hereby declared as identical iff their URIs are equal
    if (other instanceof THING)
    {
        if (m_sURI != null && ((THING)other).m_sURI != null)
            return m_sURI.equals(((THING)other).m_sURI);
        else
            return false;  // sorry, no chance left :-(
    }
    else
    if (other instanceof org.w3c.rdf.model.Resource)
    {
        try { return m_sURI.equals(((org.w3c.rdf.model.Resource)other).getURI()); }
        catch (Exception ex) { return false; }
    }
    else
        return false;
}

//----------------------------------------------------------------------------------------------------
public void updateRDFResourceSlots (dfki.rdf.util.KnowledgeBase kbCachedObjects)
{
    Method[] aMethods = getClass().getMethods();
    for (int i = 0; i < aMethods.length; i++)
    {
        Method method = aMethods[i];
        String sMethodName = method.getName();
        if (!sMethodName.startsWith("Get") || !sMethodName.endsWith("__asURI"))
            continue;
        Class[] aParameterTypes = method.getParameterTypes();
        if (aParameterTypes.length > 0)
            continue;
        Object objPropValueAsURI = null;
        try {
            objPropValueAsURI = method.invoke(this, null);
        } catch (Exception ex) {
            System.out.println("dfki.rdf.util.THING . updateRDFResourceSlots: Exception occurred" + ex);
        }
        if (objPropValueAsURI == null) continue;
        String sPropertyName = calcMethodNameToPropertyName(sMethodName);
        String sPutMethodName = calcPropertyNameToMethodNameWithoutURI("put", sPropertyName);
        if (objPropValueAsURI instanceof dfki.rdf.util.RDFResource)
        {
            try {
                Object cachedObject = kbCachedObjects.get(objPropValueAsURI);
                if (cachedObject == null || (cachedObject instanceof dfki.rdf.util.RDFResource)) continue;
                Method methodPutterNormal = getClass().getMethod( sPutMethodName, new Class[] { cachedObject.getClass() } );
                Method methodPutterURI = getClass().getMethod( sPutMethodName, new Class[] { dfki.rdf.util.RDFResource.class } );
                methodPutterNormal.invoke( this, new Object[] { cachedObject } );
                methodPutterURI.invoke( this, new Object[] { null } );
            }
            catch (Exception ex) {
                throw new Error(ex.getMessage());
            }
        }
        else
        if (objPropValueAsURI instanceof java.util.Collection)
        {
            try {
                for (Iterator itURIs = ((java.util.Collection)objPropValueAsURI).iterator(); itURIs.hasNext(); )
                {
                    dfki.rdf.util.RDFResource uri = (dfki.rdf.util.RDFResource)itURIs.next();
                    Object cachedObject = kbCachedObjects.get(uri);
                    if (cachedObject == null || (cachedObject instanceof dfki.rdf.util.RDFResource)) continue;
                    Method methodPutterNormal = getClass().getMethod( sPutMethodName, new Class[] { cachedObject.getClass() } );
                    methodPutterNormal.invoke( this, new Object[] { cachedObject } );
                    itURIs.remove();
                }
            }
            catch (Exception ex) {
                throw new Error(ex.getMessage());
            }
        }
        else
            throw new Error("Wrong class for objValue in dfki.rdf.util.THING . updateRDFResourceSlots");
    }
}

String calcMethodNameToPropertyName (String sMethodName)
{
    // getXXYYZZ --> XXYYZZ
    if (sMethodName.startsWith("get_"))
        return sMethodName.substring(4);
    else
        return Character.toLowerCase(sMethodName.charAt(3)) + sMethodName.substring(4);
}

String calcPropertyNameToMethodNameWithoutURI (String sMethodPrefix, String sPropName)
{
    if (Character.isLowerCase(sPropName.charAt(0)))
        return sMethodPrefix + Character.toUpperCase(sPropName.charAt(0))
                             + sPropName.substring(1);
    else
        return sMethodPrefix + "_" + sPropName;
}


//----------------------------------------------------------------------------------------------------
public void assign (THING newThing, KnowledgeBase kb)
{
    try
    {
        Method[] aMethods = getClass().getMethods();
        for (int i = 0; i < aMethods.length; i++)
        {
            Method method = aMethods[i];
            String sMethodName = method.getName();
            if (!sMethodName.startsWith("get"))
                continue;
            if (sMethodName.equals("getClass") || sMethodName.equals("getURI") || sMethodName.equals("getLabel"))
                continue;
            Class[] aParameterTypes = method.getParameterTypes();
            if (aParameterTypes.length > 0)
                continue;

            String sPropertyName = calcMethodNameToPropertyName(sMethodName);
            String sPutMethodName = calcPropertyNameToMethodNameWithoutURI("put", sPropertyName);
            String sClearMethodName = calcPropertyNameToMethodNameWithoutURI("clear", sPropertyName);
            String sGetMethodName = calcPropertyNameToMethodNameWithoutURI("get", sPropertyName);
            String sGetAsURIMethodName = calcPropertyNameToMethodNameWithoutURI("Get", sPropertyName + "__asURI");

            Method methodPutAsURI = getClass().getMethod( sPutMethodName, new Class[] { dfki.rdf.util.RDFResource.class } );
            Method methodClear = getClass().getMethod( sClearMethodName, new Class[] { } );
            Method methodGet = getClass().getMethod( sGetMethodName, new Class[] { } );
            Method methodGetAsURI = getClass().getMethod( sGetAsURIMethodName, new Class[] { } );

            Class clsReturnType = methodGet.getReturnType();
            if (Collection.class.isAssignableFrom(clsReturnType))
            {
                Object value = methodGet.invoke( this, null );
                LinkedList lstOldValues = new LinkedList( (Collection)value );
                value = methodGetAsURI.invoke( this, null );
                lstOldValues.addAll( (Collection)value );

                methodClear.invoke( this, null );

                value = methodGet.invoke( newThing, null );
                LinkedList lstNewValues = new LinkedList( (Collection)value );
                value = methodGetAsURI.invoke( newThing, null );
                lstNewValues.addAll( (Collection)value );

                assignValues(lstOldValues, lstNewValues, sPutMethodName, kb);
            }
            else
            {
                LinkedList lstOldValues = new LinkedList();
                Object value = methodGet.invoke( this, null );
                if (value != null) lstOldValues.add( value );
                value = methodGetAsURI.invoke( this, null );
                if (value != null) lstOldValues.add( value );
                // assert( lstOldValues.size() > 1 )

                methodClear.invoke( this, null );

                LinkedList lstNewValues = new LinkedList();
                value = methodGet.invoke( newThing, null );
                if (value != null) lstNewValues.add( value );
                value = methodGetAsURI.invoke( newThing, null );
                if (value != null) lstNewValues.add( value );
                // assert( lstNewValues.size() > 1 )

                assignValues(lstOldValues, lstNewValues, sPutMethodName, kb);
            }
        }
    }
    catch (Exception ex)
    {
        throw new Error(ex.getMessage());
    }
}

//----------------------------------------------------------------------------------------------------
void assignValues (Collection collOldValues, Collection collNewValues,
                   String sPutMethodName, KnowledgeBase kb)   throws Exception
{
    for (Iterator itOldValues = collOldValues.iterator(); itOldValues.hasNext(); )
    {
        Object oldValue = itOldValues.next();
        Object newValue = find(collNewValues, getURI(oldValue));

        // if newValue == null, then the old slot value (subA_this) has to be removed
        // as this is already done (via method clearXXX), nothing has to be done in that case
        if (newValue == null)
            continue;

        // if the new slot value gives us an increase of quality, we take it!
        // this can only be the case iff newValue is a THING (and not an URI reference)
        // as this case is handled deeper below, too, we only do the other case here:
        // A L T H O U G H :   note, that this disturbs the order of the slot values !!!
        // OR WELL... DOES IT REALLY ???????????????????????????????????????????????????
        if ( !(newValue instanceof dfki.rdf.util.THING) )
        {
            Method methodPut = getClass().getMethod( sPutMethodName, new Class[] { newValue.getClass() } );
            methodPut.invoke( this, new Object[] { newValue } );  // insert the newer slot value
            // mark, that we've already handled that new slot value for later
            remove(collNewValues, getURI(newValue));
        }
    }

    // if collNewValues still contains some slot values => add them all
    for (Iterator itNewValues = collNewValues.iterator(); itNewValues.hasNext(); )
    {
        Object newValue = itNewValues.next();

        Method[] aMethods = getClass().getMethods();
        Method methodPut = getClass().getMethod( sPutMethodName, new Class[] { newValue.getClass() } );
        methodPut.invoke( this, new Object[] { newValue } );  // insert the newer slot value

        if (newValue instanceof dfki.rdf.util.THING)
            kb.put( newValue );     // now the new Java object exists in the knowledge base, too
    }
}

//----------------------------------------------------------------------------------------------------
public static String getURI (Object obj)
{
    if (obj instanceof dfki.rdf.util.RDFResource)
        return ((dfki.rdf.util.RDFResource)obj).getURI();
    else
    if (obj instanceof dfki.rdf.util.THING)
        return ((dfki.rdf.util.THING)obj).getURI();
    else
        throw new Error("implementation error");
}

//----------------------------------------------------------------------------------------------------
public static Object find (Collection collOther, String sURI)
{
    for (Iterator itOthers = collOther.iterator(); itOthers.hasNext(); )
    {
        Object other = itOthers.next();
        if ( (other instanceof dfki.rdf.util.RDFResource) && getURI(other).equals(sURI) )
            return other;
        else
        if ( (other instanceof dfki.rdf.util.THING) && getURI(other).equals(sURI) )
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
        if ( (obj instanceof dfki.rdf.util.RDFResource) &&
             ((dfki.rdf.util.RDFResource)obj).getURI().equals(sURI) )
        {
            it.remove();
            return;
        }
    }
}

//----------------------------------------------------------------------------------------------------
}

