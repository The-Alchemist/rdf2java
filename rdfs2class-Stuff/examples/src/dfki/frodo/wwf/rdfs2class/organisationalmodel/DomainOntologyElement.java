package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class DomainOntologyElement
    extends __OrganisationalModel__
{
    //------------------------------------------------------------------------------
    String m_description;

    public void putDescription (String p_description)
    {
        m_description = p_description;
    }

    public String getDescription ()
    {
        return m_description;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_description != null) {
            sb.append(sIndent+"-> description: "+m_description+"\n");
        }
    }

    //------------------------------------------------------------------------------
}

