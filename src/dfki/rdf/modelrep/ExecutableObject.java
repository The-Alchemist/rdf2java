package dfki.rdf.modelrep;

import java.io.*;


public class ExecutableObject   extends ModelRepObject
{
//----------------------------------------------------------------------------------------------------
String m_sName;
String m_sPrecond;

//----------------------------------------------------------------------------------------------------
public void putName (String sName)
{
    m_sName = sName;
}

//----------------------------------------------------------------------------------------------------
public void putPrecond (String sPrecond)
{
    m_sPrecond = sPrecond;
}

//----------------------------------------------------------------------------------------------------
public String getName ()
{
    return m_sName;
}

//----------------------------------------------------------------------------------------------------
public String getPrecond ()
{
    return m_sPrecond;
}

//----------------------------------------------------------------------------------------------------
public String toString ()
{
    return toString("");
}

//----------------------------------------------------------------------------------------------------
public String toString (String sIndent)
{
    return "### you must overload ExecutableObject.toString(String) ###";
}

//----------------------------------------------------------------------------------------------------
protected String getShortClassName ()
{
    String sClassName = getClass().getName();
    int pos = sClassName.lastIndexOf('.');
    if (pos >= 0)
        return sClassName.substring(pos+1);
    else
        return sClassName;
}

//----------------------------------------------------------------------------------------------------
protected String getAddress ()
{
    return "@" + Integer.toHexString(this.hashCode());
}

//----------------------------------------------------------------------------------------------------
public boolean isActivated ()
{
    System.out.println("### you must overload ExecutableObject.isActivated() tclass="+getShortClassName()+" \taddr="+getAddress()+" ###");
    return false;
}

//----------------------------------------------------------------------------------------------------
public boolean isFinished ()
{
    System.out.println("### you must overload ExecutableObject.isFinished() tclass="+getShortClassName()+" \taddr="+getAddress()+" ###");
    return false;
}

//----------------------------------------------------------------------------------------------------
protected boolean canActivate (ExecutableObject eoSucc)
{
    System.out.println("### you must overload ExecutableObject.canActivate(ExecutableObject) tclass="+getShortClassName()+" \taddr="+getAddress()+" ###");
    return false;
}

//----------------------------------------------------------------------------------------------------
public boolean isProcessible ()
{
    System.out.println("### you must overload ExecutableObject.isProcessible() tclass="+getShortClassName()+" \taddr="+getAddress()+" ###");
    return false;
}

//----------------------------------------------------------------------------------------------------
protected void printToDOTFile (PrintWriter pw)
{
    System.out.println("### you must overload ExecutableObject.printToDOTFile(PrintWriter) tclass="+getShortClassName()+" \taddr="+getAddress()+" ###");
}

//----------------------------------------------------------------------------------------------------
}

