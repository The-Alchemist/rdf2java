package dfki.rdf.modelrep;

import java.util.*;
import java.io.*;


public class Task   extends ExecutableObject
{
//----------------------------------------------------------------------------------------------------

ArrayList m_alSubtasks = new ArrayList();
ExecutableObject m_eoPred;
String m_sState = "unactivated";

//----------------------------------------------------------------------------------------------------
public void putSubtask (ExecutableObject eoSubtask)
{
    m_alSubtasks.add(eoSubtask);
}

//----------------------------------------------------------------------------------------------------
public void putPred (ExecutableObject eoPred)
{
    m_eoPred = eoPred;
}

//----------------------------------------------------------------------------------------------------
public void putState (String sState)
{
    m_sState = sState;
}

//----------------------------------------------------------------------------------------------------
public Set getSubtask ()
{
    return new HashSet(m_alSubtasks);
}

//----------------------------------------------------------------------------------------------------
public ExecutableObject getPred ()
{
    return m_eoPred;
}

//----------------------------------------------------------------------------------------------------
public String getState ()
{
    return m_sState;
}

//----------------------------------------------------------------------------------------------------
public String toString (String sIndent)
{
    String sAttrIndent = sIndent + " -> ";
    String sSubtaskIndent = sIndent + "        ";

    StringBuffer buffer = new StringBuffer();
    buffer.append(sIndent + "Task \"" + m_sName + "\" " + getAddress() + "\n");

    if (m_sPrecond != null)
        buffer.append(sAttrIndent + "precond = \"" + m_sPrecond + "\"\n");
    if (m_eoPred != null)
        buffer.append(sAttrIndent + "pred = \"" + m_eoPred.m_sName + "\" " + m_eoPred.getAddress() + "\n");
    if (m_sState != null)
        buffer.append(sAttrIndent + "state = \"" + m_sState + "\"\n");

    for (int i = 0; i < m_alSubtasks.size(); i++)
    {
        buffer.append(sAttrIndent + "subtask:\n");
        ExecutableObject eoSubtask = (ExecutableObject)m_alSubtasks.get(i);
        buffer.append(eoSubtask.toString(sSubtaskIndent));
    }

    return buffer.toString();
}

//----------------------------------------------------------------------------------------------------
public boolean isActivated ()
{
    return m_sState.equals("activated");
}

//----------------------------------------------------------------------------------------------------
public boolean isFinished ()
{
    return m_sState.equals("finished");
}

//----------------------------------------------------------------------------------------------------
protected boolean canActivate (ExecutableObject eoSucc)
{
    return isFinished();
}

//----------------------------------------------------------------------------------------------------
public boolean isProcessible ()
{
    if (!m_sState.equals("unactivated"))
        return false;

    if (m_eoPred != null)
        return m_eoPred.canActivate(this);
    else
        return true;
}

//----------------------------------------------------------------------------------------------------
protected void printToDOTFile (PrintWriter pw)
{
    pw.print("\t\"" + getName() + "\" [");
    if (isProcessible())
        pw.print("style=filled, color=yellow");
    else
    if (isActivated())
        pw.print("style=filled, color=orange");
    else
    if (isFinished())
        pw.print("style=filled, color=lightblue");
    else
        pw.print("style=filled, color=lightgray");

//    pw.print(", shape=box");
    pw.print(", shape=record, label=\"" + getName() +
             "|state: " + m_sState +
             "|isProcessible: " + isProcessible() + "\"");

    pw.println("];");

    if (m_eoPred != null)
        pw.println("\t\"" + m_eoPred.getName() + "\" -> \"" + getName() + "\";");

    // now the subtasks...
    for (int i = 0; i < m_alSubtasks.size(); i++)
    {
        ExecutableObject eoSubtask = (ExecutableObject)m_alSubtasks.get(i);
        eoSubtask.printToDOTFile(pw);
        pw.println("\t\"" + getName() + "\" -> \"" + eoSubtask.getName() + "\" [style=dashed, label=\"sub\"];");
    }
}

//----------------------------------------------------------------------------------------------------
}

