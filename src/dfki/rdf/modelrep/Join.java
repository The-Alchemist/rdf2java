package dfki.rdf.modelrep;

import java.util.*;
import java.io.*;


public class Join   extends ExecutableObject
{
//----------------------------------------------------------------------------------------------------

ArrayList m_alJoinObjects = new ArrayList();

//----------------------------------------------------------------------------------------------------
public void putJoins (ExecutableObject eoJoinObject)
{
    m_alJoinObjects.add(eoJoinObject);
}

//----------------------------------------------------------------------------------------------------
public Set getJoins ()
{
    return new HashSet(m_alJoinObjects);
}

//----------------------------------------------------------------------------------------------------
public String toString (String sIndent)
{
    String sAttrIndent = sIndent + " -> ";
    String sSubtaskIndent = sIndent + "        ";

    StringBuffer buffer = new StringBuffer();
    buffer.append(sIndent + getShortClassName() + " " + getAddress() + "\n");

    for (int i = 0; i < m_alJoinObjects.size(); i++)
    {
        ExecutableObject eoJoinObject = (ExecutableObject)m_alJoinObjects.get(i);
        buffer.append(sAttrIndent + "joins = \"" + eoJoinObject.m_sName + "\" " + eoJoinObject.getAddress() + "\n");
    }

    return buffer.toString();
}

//----------------------------------------------------------------------------------------------------
protected boolean canActivate (ExecutableObject eoSucc)
{
    return isActivated();
}

//----------------------------------------------------------------------------------------------------
public boolean isFinished ()
{
    return false;  // a Join is never finished?? (only activated) -- really???
}

//----------------------------------------------------------------------------------------------------
public boolean isProcessible ()
{
    return false;  // a Join is never processible (only activated)
}

//----------------------------------------------------------------------------------------------------
protected void printToDOTFile (PrintWriter pw)
{                                                  // , peripheries=2
    pw.println("\t\"" + getName() + "\" [shape=ellipse, label=\"(" + getShortClassName() + ") " + getName() + "\"];");

    for (int i = 0; i < m_alJoinObjects.size(); i++)
    {
        ExecutableObject eoJoinObject = (ExecutableObject)m_alJoinObjects.get(i);
        pw.println("\t\"" + eoJoinObject.getName() + "\" -> \"" + getName() + "\";");
    }
}

//----------------------------------------------------------------------------------------------------
}

