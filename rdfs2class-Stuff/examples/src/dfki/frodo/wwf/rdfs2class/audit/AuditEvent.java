package dfki.frodo.wwf.rdfs2class.audit;

import java.util.*;


public class AuditEvent
    extends __Audit__
{
    //------------------------------------------------------------------------------
    String m_eventName;

    public void putEventName (String p_eventName)
    {
        m_eventName = p_eventName;
    }

    public String getEventName ()
    {
        return m_eventName;
    }

    //------------------------------------------------------------------------------
    String m_informalDescription;

    public void putInformalDescription (String p_informalDescription)
    {
        m_informalDescription = p_informalDescription;
    }

    public String getInformalDescription ()
    {
        return m_informalDescription;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_eventName != null) {
            sb.append(sIndent+"-> eventName: "+m_eventName+"\n");
        }
        if (m_informalDescription != null) {
            sb.append(sIndent+"-> informalDescription: "+m_informalDescription+"\n");
        }
    }

    //------------------------------------------------------------------------------
}

