package dfki.rdf.util;

import java.io.Serializable;
import java.util.*;


public class PropertyStore   implements Serializable
{
//------------------------------------------------------------------------------
private Map/*String->PropertyInfo*/ m_mapProperty2Info;
private Class m_cls;

//------------------------------------------------------------------------------
public PropertyStore( Class cls )
{
    m_mapProperty2Info = new HashMap();
    m_cls = cls;
}

//------------------------------------------------------------------------------
public void addProperty( PropertyInfo pi )
{
    m_mapProperty2Info.put( pi.getName(), pi );
}

//------------------------------------------------------------------------------
public void removeProperty( String sPropertyName )
{
    m_mapProperty2Info.remove( sPropertyName );
}

//------------------------------------------------------------------------------
public Set getProperties()
{
    return m_mapProperty2Info.keySet();
}

//------------------------------------------------------------------------------
public PropertyInfo getPropertyInfo(String sPropertyName)
{
    return (PropertyInfo)m_mapProperty2Info.get(sPropertyName);
}

//------------------------------------------------------------------------------
public Collection/*PropertyInfo*/ getPropertyInfos()
{
    return m_mapProperty2Info.values();
}

//------------------------------------------------------------------------------
public Object getPropertyValue (String sPropertyName)
{
    PropertyInfo pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName );
    return pi.getValue();
}

//------------------------------------------------------------------------------
public void putPropertyValue (String sPropertyName, Object value)
{
    PropertyInfo pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName );
    pi.putValue( value );
}

//------------------------------------------------------------------------------
public void setPropertyValues (String sPropertyName, Collection values)
{
    PropertyInfo pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName );
    pi.setValues( values );
}

//------------------------------------------------------------------------------
public void clearPropertyValue (String sPropertyName)
{
    PropertyInfo pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName );
    pi.clearValue();
}

//------------------------------------------------------------------------------
} // end of class PropertyStore

