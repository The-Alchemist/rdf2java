package dfki.frodo.wwf.rdfs2class.audit;

import java.util.*;


public class AuditEventOccurence
    extends BasicAuditObject
{
    //------------------------------------------------------------------------------
    dfki.rdf.util.THING m_issuedBy;

    public void putIssuedBy (dfki.rdf.util.THING p_issuedBy)
    {
        m_issuedBy = p_issuedBy;
    }

    public dfki.rdf.util.THING getIssuedBy ()
    {
        return m_issuedBy;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_issuedBy != null) {
            sb.append(sIndent+"-> issuedBy:\n"+sIndent+"       "+m_issuedBy.toStringShort()+"\n");
            // sb.append(sIndent+"-> issuedBy:\n"+m_issuedBy.toString(sIndent+"       "));
        }
    }

    //------------------------------------------------------------------------------
}

