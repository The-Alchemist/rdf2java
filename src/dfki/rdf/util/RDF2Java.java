package dfki.rdf.util;


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
} // end of class RDF2Java

