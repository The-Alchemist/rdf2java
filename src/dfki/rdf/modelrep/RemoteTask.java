package dfki.rdf.modelrep;

import java.util.*;
import java.io.*;


public class RemoteTask   extends Task
{
//----------------------------------------------------------------------------------------------------
public String toString (String sIndent)
{
    String sAttrIndent = sIndent + " -> ";
    String sSubtaskIndent = sIndent + "        ";

    StringBuffer buffer = new StringBuffer();
    buffer.append(sIndent + "RemoteTask \"" + m_sName + "\" " + getAddress() + "\n");

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

//    pw.print(", shape=record, label=\"" + getName() +
//             "|state: " + m_sState +
//             "|isProcessible: " + isProcessible() + "\"");
    pw.print(", shape=record, label=\"" + getName() + " (remote)" +
             "|id: ...id...\"");

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

