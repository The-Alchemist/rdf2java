package dfki.rdf.util;

import java.util.*;
import java.lang.reflect.*;


public class RDF2Java
{
//----------------------------------------------------------------------------------------------------
public static String extractPropertyName (String sMethodName)
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
static Method getMethod (Class cls, String sMethodName, Class[] aPars)
{
    Method[] aMethods = cls.getMethods();
    for (int i = 0; i < aMethods.length; i++)
    {
        Method m = aMethods[i];
        if (!m.getName().equals(sMethodName)) continue;
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
} // end of class RDF2Java

