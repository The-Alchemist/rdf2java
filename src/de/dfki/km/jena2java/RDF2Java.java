package de.dfki.km.jena2java;

import java.lang.reflect.Method;
import java.util.Date;


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
static String makeMethodName (String sMethodPrefix, String sPropertyNamespace, String sPropertyName)
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

