package dfki.rdf.modelrep;

import java.util.*;
import java.io.*;


public class ModelRep
{
//----------------------------------------------------------------------------------------------------

ArrayList m_alObjects = new ArrayList();


//----------------------------------------------------------------------------------------------------
public void putObject (Object obj)
{
    m_alObjects.add(obj);
}

//----------------------------------------------------------------------------------------------------
public Set getObject ()
{
    return new HashSet(m_alObjects);
}

//----------------------------------------------------------------------------------------------------
public String toString ()
{
    StringBuffer sb = new StringBuffer();
    sb.append("objects:\n");
    sb.append("============================================================\n");
    for (int i = 0; i < m_alObjects.size(); i++)
    {
        if (i > 0)
           sb.append("------------------------------------------------------------\n");
        sb.append(m_alObjects.get(i).toString());
    }
    sb.append("============================================================\n");

    sb.append("\nactivated objects:\n");
    for (int i = 0; i < m_alObjects.size(); i++)
    {
        ExecutableObject exeobj = (ExecutableObject)m_alObjects.get(i);
        if (exeobj.isActivated())
            sb.append(" * " + exeobj.m_sName + "\n");
    }

    sb.append("\nprocessible objects:\n");
    for (int i = 0; i < m_alObjects.size(); i++)
    {
        ExecutableObject exeobj = (ExecutableObject)m_alObjects.get(i);
        if (exeobj.isProcessible())
            sb.append(" * " + exeobj.m_sName + "\n");
    }


    return sb.toString();
}

//----------------------------------------------------------------------------------------------------
public void createDOTFile (String sFilename)
{
    try {
        FileOutputStream fos = new FileOutputStream(sFilename);
        PrintWriter pw = new PrintWriter(fos);

        pw.println("digraph ModelRep {");
        pw.println("\tgraph[rankdir=LR];\n");

        for (int i = 0; i < m_alObjects.size(); i++)
        {
            ExecutableObject exeobj = (ExecutableObject)m_alObjects.get(i);
            exeobj.printToDOTFile(pw);
        }

        pw.println("}");

        pw.flush();
        pw.close();
    }
    catch (Exception ex)
    {
        System.out.println("### exception occurred in ModelRep.createDOTFile: "+ex.getMessage());
        ex.printStackTrace();
    }
}

//----------------------------------------------------------------------------------------------------
public ExecutableObject findObjectWithName (String sName)
{
    for (int i = 0; i < m_alObjects.size(); i++)
    {
        ExecutableObject exeobj = (ExecutableObject)m_alObjects.get(i);
        if (exeobj.getName().equals(sName))
            return exeobj;
    }
    // not found :-(
    return null;
}

//----------------------------------------------------------------------------------------------------
public boolean activate (String sName)
{
//    System.out.println("# pressed activate "+sName);
    ExecutableObject exeobj = findObjectWithName(sName);
    if (exeobj == null)
        return false;
    if (!(exeobj instanceof Task)) {
        System.out.println("tried to activate something that's not a task -- ignored");
        return false;
    }
    Task T = (Task)exeobj;
    if (!T.m_sState.equals("unactivated")) {
        System.out.println("tried to activate a task that's not unactivated -- ignored");
        return false;
    }
    ((Task)exeobj).m_sState = "activated";
    return true;
}

//----------------------------------------------------------------------------------------------------
public boolean finish (String sName)
{
//    System.out.println("# pressed finish "+sName);
    ExecutableObject exeobj = findObjectWithName(sName);
    if (exeobj == null)
        return false;
    if (!(exeobj instanceof Task)) {
        System.out.println("tried to finish something that's not a task -- ignored");
        return false;
    }
    Task T = (Task)exeobj;
    ((Task)exeobj).m_sState = "finished";
    return true;
}

//----------------------------------------------------------------------------------------------------
public boolean edit (String sName)
{
//    System.out.println("# pressed edit "+sName);
    ExecutableObject exeobj = findObjectWithName(sName);
    if (exeobj == null)
        return false;
    if (!(exeobj instanceof Task)) {
        System.out.println("tried to edit something that's not a task -- ignored ### this is NOT YET supported...");
        return false;
    }
    Task T = (Task)exeobj;
    TaskEditorFrm frmEditTask = new TaskEditorFrm(null, this, T);
    frmEditTask.setVisible(true);

    boolean bChangedSomething = false;
    if (frmEditTask.closedWithOK())
    {
        String sNewName = frmEditTask.getValue("name");
        String sNewState = frmEditTask.getValue("state");
        System.out.println("name : " + sNewName);
        System.out.println("state: " + sNewState);

        if (!sNewName.equals(T.getName()))
        {
            T.putName(sNewName);
            bChangedSomething = true;
        }
        if (!sNewState.equals(T.getState()))
        {
            T.putState(sNewState);
            bChangedSomething = true;
        }
    }

    return bChangedSomething;
}

//----------------------------------------------------------------------------------------------------
}

