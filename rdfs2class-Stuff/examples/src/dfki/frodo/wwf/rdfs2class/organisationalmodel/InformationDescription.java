package dfki.frodo.wwf.rdfs2class.organisationalmodel;

import java.util.*;


public class InformationDescription
    extends __OrganisationalModel__
{
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
    String m_infoObjectURI;

    public void putInfoObjectURI (String p_infoObjectURI)
    {
        m_infoObjectURI = p_infoObjectURI;
    }

    public String getInfoObjectURI ()
    {
        return m_infoObjectURI;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_informalDescription != null) {
            sb.append(sIndent+"-> informalDescription: "+m_informalDescription+"\n");
        }
        if (m_infoObjectURI != null) {
            sb.append(sIndent+"-> infoObjectURI: "+m_infoObjectURI+"\n");
        }
    }

    //------------------------------------------------------------------------------
}

