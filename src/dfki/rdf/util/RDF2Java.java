package dfki.rdf.util;

import java.util.*;
import java.lang.reflect.*;


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
static String makeMethodName (String sMethodPrefix, String sPropertyName)
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
static public Collection/*String*/ getProperties (Class cls)
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
} // end of class RDF2Java

