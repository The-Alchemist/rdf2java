package dfki.frodo.wwf.rdfs2class.task;

import java.util.*;


public class ExecutableObject
    extends __Task__
{
    //------------------------------------------------------------------------------
    Collection m_parentTask = new HashSet();

    public void putParentTask (Task p_parentTask)
    {
        m_parentTask.add(p_parentTask);
    }

    public Collection getParentTask ()
    {
        return m_parentTask;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (!m_parentTask.isEmpty()) {
            sb.append(sIndent+"-> parentTask:\n");
            Iterator it_parentTask = m_parentTask.iterator();
            while (it_parentTask.hasNext()) {
                sb.append( sIndent+"       " + ((dfki.rdf.util.THING)it_parentTask.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.THING)it_parentTask.next()).toString(sIndent+"       ") );
            }
        }
    }

    //------------------------------------------------------------------------------
}

