package dfki.frodo.wwf.rdfs2class.audit;

import java.util.*;


public class BasicAuditObject
    extends __Audit__
{
    //------------------------------------------------------------------------------
    String m_timestamp;

    public void putTimestamp (String p_timestamp)
    {
        m_timestamp = p_timestamp;
    }

    public String getTimestamp ()
    {
        return m_timestamp;
    }

    //------------------------------------------------------------------------------
    AuditEvent m_eventCode;

    public void putEventCode (AuditEvent p_eventCode)
    {
        m_eventCode = p_eventCode;
    }

    public AuditEvent getEventCode ()
    {
        return m_eventCode;
    }

    //------------------------------------------------------------------------------
    dfki.frodo.wwf.rdfs2class.time.TimeObject m_time;

    public void putTime (dfki.frodo.wwf.rdfs2class.time.TimeObject p_time)
    {
        m_time = p_time;
    }

    public dfki.frodo.wwf.rdfs2class.time.TimeObject getTime ()
    {
        return m_time;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_timestamp != null) {
            sb.append(sIndent+"-> timestamp: "+m_timestamp+"\n");
        }
        if (m_eventCode != null) {
            sb.append(sIndent+"-> eventCode:\n"+sIndent+"       "+m_eventCode.toStringShort()+"\n");
            // sb.append(sIndent+"-> eventCode:\n"+m_eventCode.toString(sIndent+"       "));
        }
        if (m_time != null) {
            sb.append(sIndent+"-> time:\n"+sIndent+"       "+m_time.toStringShort()+"\n");
            // sb.append(sIndent+"-> time:\n"+m_time.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

