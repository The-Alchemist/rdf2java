package dfki.frodo.wwf.rdfs2class.time;

import java.util.*;


public class TimeObject
    extends __Time__
{
    //------------------------------------------------------------------------------
    String m_year;

    public void putYear (String p_year)
    {
        m_year = p_year;
    }

    public String getYear ()
    {
        return m_year;
    }

    //------------------------------------------------------------------------------
    String m_second;

    public void putSecond (String p_second)
    {
        m_second = p_second;
    }

    public String getSecond ()
    {
        return m_second;
    }

    //------------------------------------------------------------------------------
    String m_minutes;

    public void putMinutes (String p_minutes)
    {
        m_minutes = p_minutes;
    }

    public String getMinutes ()
    {
        return m_minutes;
    }

    //------------------------------------------------------------------------------
    String m_month;

    public void putMonth (String p_month)
    {
        m_month = p_month;
    }

    public String getMonth ()
    {
        return m_month;
    }

    //------------------------------------------------------------------------------
    String m_day;

    public void putDay (String p_day)
    {
        m_day = p_day;
    }

    public String getDay ()
    {
        return m_day;
    }

    //------------------------------------------------------------------------------
    String m_hour;

    public void putHour (String p_hour)
    {
        m_hour = p_hour;
    }

    public String getHour ()
    {
        return m_hour;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_year != null) {
            sb.append(sIndent+"-> year: "+m_year+"\n");
        }
        if (m_second != null) {
            sb.append(sIndent+"-> second: "+m_second+"\n");
        }
        if (m_minutes != null) {
            sb.append(sIndent+"-> minutes: "+m_minutes+"\n");
        }
        if (m_month != null) {
            sb.append(sIndent+"-> month: "+m_month+"\n");
        }
        if (m_day != null) {
            sb.append(sIndent+"-> day: "+m_day+"\n");
        }
        if (m_hour != null) {
            sb.append(sIndent+"-> hour: "+m_hour+"\n");
        }
    }

    //------------------------------------------------------------------------------
}

