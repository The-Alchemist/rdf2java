package dfki.rdf.modelrep;

import java.util.*;
import java.io.*;


public class Split   extends ExecutableObject
{
//----------------------------------------------------------------------------------------------------

ExecutableObject m_eoPred;
ArrayList m_alSplitObjects = new ArrayList();

//----------------------------------------------------------------------------------------------------
public void putPred (ExecutableObject eoPred)
{
    m_eoPred = eoPred;
}

//----------------------------------------------------------------------------------------------------
public void putSplits (ExecutableObject eoSplitObject)
{
    m_alSplitObjects.add(eoSplitObject);
}

//----------------------------------------------------------------------------------------------------
public ExecutableObject getPred ()
{
    return m_eoPred;
}

//----------------------------------------------------------------------------------------------------
public Set getSplits ()
{
    return new HashSet(m_alSplitObjects);
}

//----------------------------------------------------------------------------------------------------
public String toString (String sIndent)
{
    String sAttrIndent = sIndent + " -> ";
    String sSubtaskIndent = sIndent + "        ";

    StringBuffer buffer = new StringBuffer();
    buffer.append(sIndent + getShortClassName() + " " + getAddress() + "\n");

    if (m_eoPred != null)
        buffer.append(sAttrIndent + "pred = \"" + m_eoPred.m_sName + "\" " + m_eoPred.getAddress() + "\n");

    for (int i = 0; i < m_alSplitObjects.size(); i++)
    {
        ExecutableObject eoSplitObject = (ExecutableObject)m_alSplitObjects.get(i);
        buffer.append(sAttrIndent + "splits = \"" + eoSplitObject.m_sName + "\" " + eoSplitObject.getAddress() + "\n");
    }

    return buffer.toString();
}

//----------------------------------------------------------------------------------------------------
public boolean isActivated ()
{
    return ( m_eoPred != null && m_eoPred.isFinished() );
}

//----------------------------------------------------------------------------------------------------
public boolean isFinished ()
{
    return false;  // a Split is never finished?? (only activated) -- really???
}

//----------------------------------------------------------------------------------------------------
public boolean isProcessible ()
{
    return false;  // a Split is never processible (only activated)
}

//----------------------------------------------------------------------------------------------------
protected void printToDOTFile (PrintWriter pw)
{
    pw.println("\t\"" + getName() + "\" [shape=ellipse, label=\"(" + getShortClassName() + ") " + getName() + "\"];");

    if (m_eoPred != null)
        pw.println("\t\"" + m_eoPred.getName() + "\" -> \"" + getName() + "\";");
}

//----------------------------------------------------------------------------------------------------
}

