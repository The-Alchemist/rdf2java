package dfki.frodo.wwf.rdfs2class.time;

import java.util.*;


public class Duration
    extends __Time__
{
    //------------------------------------------------------------------------------
    TimeObject m_end;

    public void putEnd (TimeObject p_end)
    {
        m_end = p_end;
    }

    public TimeObject getEnd ()
    {
        return m_end;
    }

    //------------------------------------------------------------------------------
    String m_timeSpanHours;

    public void putTimeSpanHours (String p_timeSpanHours)
    {
        m_timeSpanHours = p_timeSpanHours;
    }

    public String getTimeSpanHours ()
    {
        return m_timeSpanHours;
    }

    //------------------------------------------------------------------------------
    String m_timeSpanMinutes;

    public void putTimeSpanMinutes (String p_timeSpanMinutes)
    {
        m_timeSpanMinutes = p_timeSpanMinutes;
    }

    public String getTimeSpanMinutes ()
    {
        return m_timeSpanMinutes;
    }

    //------------------------------------------------------------------------------
    String m_timeSpanYears;

    public void putTimeSpanYears (String p_timeSpanYears)
    {
        m_timeSpanYears = p_timeSpanYears;
    }

    public String getTimeSpanYears ()
    {
        return m_timeSpanYears;
    }

    //------------------------------------------------------------------------------
    TimeObject m_start;

    public void putStart (TimeObject p_start)
    {
        m_start = p_start;
    }

    public TimeObject getStart ()
    {
        return m_start;
    }

    //------------------------------------------------------------------------------
    String m_timeSpanDays;

    public void putTimeSpanDays (String p_timeSpanDays)
    {
        m_timeSpanDays = p_timeSpanDays;
    }

    public String getTimeSpanDays ()
    {
        return m_timeSpanDays;
    }

    //------------------------------------------------------------------------------
    String m_timeSpanMonths;

    public void putTimeSpanMonths (String p_timeSpanMonths)
    {
        m_timeSpanMonths = p_timeSpanMonths;
    }

    public String getTimeSpanMonths ()
    {
        return m_timeSpanMonths;
    }

    //------------------------------------------------------------------------------
    public void toString (StringBuffer sb, String sIndent)
    {
        if (m_end != null) {
            sb.append(sIndent+"-> end:\n"+sIndent+"       "+m_end.toStringShort()+"\n");
            // sb.append(sIndent+"-> end:\n"+m_end.toString(sIndent+"       "));
        }
        if (m_timeSpanHours != null) {
            sb.append(sIndent+"-> timeSpanHours: "+m_timeSpanHours+"\n");
        }
        if (m_timeSpanMinutes != null) {
            sb.append(sIndent+"-> timeSpanMinutes: "+m_timeSpanMinutes+"\n");
        }
        if (m_timeSpanYears != null) {
            sb.append(sIndent+"-> timeSpanYears: "+m_timeSpanYears+"\n");
        }
        if (m_start != null) {
            sb.append(sIndent+"-> start:\n"+sIndent+"       "+m_start.toStringShort()+"\n");
            // sb.append(sIndent+"-> start:\n"+m_start.toString(sIndent+"       "));
        }
        if (m_timeSpanDays != null) {
            sb.append(sIndent+"-> timeSpanDays: "+m_timeSpanDays+"\n");
        }
        if (m_timeSpanMonths != null) {
            sb.append(sIndent+"-> timeSpanMonths: "+m_timeSpanMonths+"\n");
        }
    }

    //------------------------------------------------------------------------------
}

