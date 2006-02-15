package de.dfki.rdf.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;


public class RDF2Java
{
//----------------------------------------------------------------------------------------------------
static String extractPropertyName (String sMethodName)
{
    // getProp --> prop
    // get_Prop -> Prop   (also possible: get_prop --> prop)
    if (!sMethodName.startsWith("get")) throw new Error("Expected 'get' prefix in RDF2Java.extractPropertyName");
    if (sMethodName.startsWith("get_"))
        return sMethodName.substring(4);
    else
        return Character.toLowerCase(sMethodName.charAt(3)) + sMethodName.substring(4);
}

//----------------------------------------------------------------------------------------------------
public static String makeMethodName (String sMethodPrefix, String sPropertyName)
{
    // prop --> getProp
    // Prop --> get_Prop
    if (Character.isLowerCase(sPropertyName.charAt(0)))
        return sMethodPrefix + Character.toUpperCase(sPropertyName.charAt(0))
                             + sPropertyName.substring(1);
    else
        return sMethodPrefix + "_" + sPropertyName;
}

//----------------------------------------------------------------------------------------------------
public static String makeMethodName (String sMethodPrefix, String sPropertyNamespace, String sPropertyName)
{
    String sMembervarName = sPropertyName;
    if( sPropertyNamespace != null )
        sMembervarName = RDF2Java.namespace2abbrev( sPropertyNamespace ) + '_' + sPropertyName;
    return makeMethodName( sMethodPrefix, sMembervarName );
}

//----------------------------------------------------------------------------------------------------
static Method getMethod (Class cls, String sMethodName, Class[] aPars)
{
    Method[] aMethods = cls.getMethods();
    for (int i = 0; i < aMethods.length; i++)
    {
        Method m = aMethods[i];
        if (!m.getName().equals(sMethodName)) continue;
        if (aPars == null) return m;  // omit par check => match depends only on method name
        if (!areAssignableFrom( m.getParameterTypes(), aPars )) continue;
        return m;
    }
    return null;  // not found
}

static boolean areAssignableFrom (Class[] pars1, Class[] pars2)
{
    if (pars1.length != pars2.length) return false;
    for (int i = 0; i < pars1.length; i++)
    {
        if (!pars1[i].isAssignableFrom(pars2[i])) return false;
    }
    return true;  // pars1 ARE assignable from pars2
}

//----------------------------------------------------------------------------------------------------
public static Collection/*String*/ getProperties (Class cls)
{
    Collection collProperties = new HashSet();
    Method[] aMethods = cls.getMethods();
    for (int i = 0; i < aMethods.length; i++)
    {
        Method method = aMethods[i];
        String sMethodName = method.getName();
        if (method.getDeclaringClass().equals(RDFResource.class))
            continue;
        if (!sMethodName.startsWith("get"))
            continue;
        if (sMethodName.equals("getClass"))
            continue;
        Class[] aParameterTypes = method.getParameterTypes();
        if (aParameterTypes.length > 0)
            continue;

        String sPropertyName = RDF2Java.extractPropertyName(sMethodName);
        collProperties.add(sPropertyName);
    }
    return collProperties;
}

//----------------------------------------------------------------------------------------------------
public static Method[] getPropertyMethods (Class cls)
{
    Collection/*String*/ collProperties = getProperties(cls);
    Method[] aMethods = new Method[collProperties.size()];
    int i = 0;
    for (Iterator itProperties = collProperties.iterator(); itProperties.hasNext(); i++)
    {
        String sPropertyName = (String)itProperties.next();
        String sGetMethodName = makeMethodName("get", sPropertyName);
        aMethods[i] = getMethod( cls, sGetMethodName, new Class[0] );
    }
    return aMethods;
}

//----------------------------------------------------------------------------------------------------
public static Object getPropertyValue (THING thing, String sPropertyName)
{
    try {
        String sMethodNameGet = makeMethodName("get", sPropertyName);
        Method methodGet = getMethod( thing.getClass(), sMethodNameGet, new Class[0] );
        Object value = methodGet.invoke( thing, (Object[])null );
        return value;
    }
    catch (Exception ex) {
        throw new RuntimeException( "Exception " + ex.getClass() + " occurred in RDF2Java.getPropertyValue: " + ex.getMessage() );
    }
}

//----------------------------------------------------------------------------------------------------
//public static Collection getPropertyValues (THING thing, String sPropertyName)
//{
//    return (Collection)getPropertyValue( thing, sPropertyName );
//}

//----------------------------------------------------------------------------------------------------
public static void putPropertyValue (THING thing, String sPropertyName, Object value)
{
    try {
        String sMethodNamePut = makeMethodName("put", sPropertyName);
        Class[] aclsPars = ( value != null  ?  new Class[]{ value.getClass() }  :  null );
        Method methodPut = getMethod( thing.getClass(), sMethodNamePut, aclsPars );
        methodPut.invoke( thing, new Object[] { value } );
    }
    catch (Exception ex) {
        throw new RuntimeException( "Exception " + ex.getClass() + " occurred in RDF2Java.putPropertyValue: " + ex.getMessage() );
    }
}

//----------------------------------------------------------------------------------------------------
public static void clearPropertyValues (THING thing, String sPropertyName)
{
    try {
        String sMethodNameClear = makeMethodName("clear", sPropertyName);
        Method methodClear = getMethod( thing.getClass(), sMethodNameClear, new Class[0] );
        methodClear.invoke( thing, (Object[])null );
    }
    catch (Exception ex) {
        throw new RuntimeException( "Exception " + ex.getClass() + " occurred in RDF2Java.clearPropertyValue: " + ex.getMessage() );
    }
}

//----------------------------------------------------------------------------------------------------
public static String namespace2abbrev( String sNamespace )
{
    if( sNamespace.endsWith( "#" ) ) sNamespace = sNamespace.substring( 0, sNamespace.length()-1 );
    
    int posLastSlash = sNamespace.lastIndexOf( '/' );
    int posLastDot   = sNamespace.lastIndexOf( '.' );
    int posLastColon = sNamespace.lastIndexOf( ':' );
    int posLastHash  = sNamespace.lastIndexOf( '#' );
    
    int pos = Math.max( posLastSlash, Math.max( posLastDot, Math.max( posLastColon, posLastHash ) ) );  
    if( posLastSlash >= 0 ) return sNamespace.substring( pos+1 );

    return "" + new Date().getTime();
}

//----------------------------------------------------------------------------------------------------
} // end of class RDF2Java

