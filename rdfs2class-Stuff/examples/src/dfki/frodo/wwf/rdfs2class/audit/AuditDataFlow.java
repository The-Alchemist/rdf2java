package dfki.frodo.wwf.rdfs2class.audit;

import java.util.*;


public class AuditDataFlow
    extends BasicAuditObject
{
    //------------------------------------------------------------------------------
    dfki.rdf.util.THING m_oldValue;

    public void putOldValue (dfki.rdf.util.THING p_oldValue)
    {
        m_oldValue = p_oldValue;
    }

    public dfki.rdf.util.THING getOldValue ()
    {
        return m_oldValue;
    }

    //------------------------------------------------------------------------------
    String m_variableName;

    public void putVariableName (String p_variableName)
    {
        m_variableName = p_variableName;
    }

    public String getVariableName ()
    {
        return m_variableName;
    }

    //------------------------------------------------------------------------------
    dfki.rdf.util.THING m_newValue;

    public void putNewValue (dfki.rdf.util.THING p_newValue)
    {
        m_newValue = p_newValue;
    }

    public dfki.rdf.util.THING getNewValue ()
    {
        return m_newValue;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_oldValue != null) {
            sb.append(sIndent+"-> oldValue:\n"+sIndent+"       "+m_oldValue.toStringShort()+"\n");
            // sb.append(sIndent+"-> oldValue:\n"+m_oldValue.toString(sIndent+"       "));
        }
        if (m_variableName != null) {
            sb.append(sIndent+"-> variableName: "+m_variableName+"\n");
        }
        if (m_newValue != null) {
            sb.append(sIndent+"-> newValue:\n"+sIndent+"       "+m_newValue.toStringShort()+"\n");
            // sb.append(sIndent+"-> newValue:\n"+m_newValue.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

