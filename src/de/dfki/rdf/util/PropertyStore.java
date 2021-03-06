package de.dfki.rdf.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.w3c.rdf.model.ModelException;
import org.w3c.rdf.model.Resource;


public class PropertyStore   implements Serializable
{
//------------------------------------------------------------------------------
private Map/*String->PropertyInfo*/ m_mapProperty2Info;
private Class m_cls;
private Resource m_rdfsClass;

//------------------------------------------------------------------------------
public PropertyStore( Class cls, Resource rdfsClass )
{
    m_mapProperty2Info = new HashMap();
    m_cls = cls;
    m_rdfsClass = rdfsClass;
}

//------------------------------------------------------------------------------
public void addProperty( PropertyInfo pi )
{
    String sPropName = pi.getName();
    try 
    {
        if(     m_rdfsClass != null && pi.getNamespace() != null &&
                !m_rdfsClass.getNamespace().equals( pi.getNamespace() ) )
            sPropName = pi.getNamespace() + pi.getName();
    }
    catch( ModelException ex ) {}
    
    m_mapProperty2Info.put( sPropName, pi );
}

//------------------------------------------------------------------------------
public void removeProperty( String sPropertyName )
{
    m_mapProperty2Info.remove( sPropertyName );
}

//------------------------------------------------------------------------------
public Set/*PropertyInfo*/ getProperties()
{
    return m_mapProperty2Info.keySet();
}

//------------------------------------------------------------------------------
public PropertyInfo getPropertyInfo(String sPropertyName)
{
    PropertyInfo pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName );
    if( pi == null )
    {
        // second try
        int iHash = sPropertyName.lastIndexOf( '#' );
        if( iHash >= 0 )
            pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName.substring( iHash+1 ) );
        if( pi == null )
            System.err.println( "unknown property: " + sPropertyName + " (cls: " + m_cls + "; RDFS class: " + m_rdfsClass + ")" );
    }
    return pi;
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
    if( pi == null )
    {
        // second try
        int iHash = sPropertyName.lastIndexOf( '#' );
        if( iHash >= 0 )
            pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName.substring( iHash+1 ) );
        if( pi == null )
        {
            System.err.println( "unknown property: " + sPropertyName + " (cls: " + m_cls + "; RDFS class: " + m_rdfsClass + ")" );
            return null;
        }
    }
    return pi.getValue();
}

//------------------------------------------------------------------------------
public void putPropertyValue (String sPropertyName, Object value)
{
    PropertyInfo pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName );
    if( pi == null )
    {
        // second try
        int iHash = sPropertyName.lastIndexOf( '#' );
        if( iHash >= 0 )
            pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName.substring( iHash+1 ) );
        if( pi == null )
        {
            System.err.println( "unknown property: " + sPropertyName + " (cls: " + m_cls + "; RDFS class: " + m_rdfsClass + ")" );
            return;
        }
    }
    pi.putValue( value );
}

//------------------------------------------------------------------------------
public void setPropertyValues (String sPropertyName, Collection values)
{
    PropertyInfo pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName );
    if( pi == null )
    {
        // second try
        int iHash = sPropertyName.lastIndexOf( '#' );
        if( iHash >= 0 )
            pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName.substring( iHash+1 ) );
        if( pi == null )
        {
            System.err.println( "unknown property: " + sPropertyName + " (cls: " + m_cls + "; RDFS class: " + m_rdfsClass + ")" );
            return;
        }
    }
    pi.setValues( values );
}

//------------------------------------------------------------------------------
public void clearPropertyValue (String sPropertyName)
{
    PropertyInfo pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName );
    if( pi == null )
    {
        // second try
        int iHash = sPropertyName.lastIndexOf( '#' );
        if( iHash >= 0 )
            pi = (PropertyInfo)m_mapProperty2Info.get( sPropertyName.substring( iHash+1 ) );
        if( pi == null )
        {
            System.err.println( "unknown property: " + sPropertyName + " (cls: " + m_cls + "; RDFS class: " + m_rdfsClass + ")" );
            return;
        }
    }
    pi.clearValue();
}

//------------------------------------------------------------------------------
public String toString()
{
    StringBuffer sb = new StringBuffer();
    for( Iterator it = getPropertyInfos().iterator(); it.hasNext(); )
    {
        PropertyInfo pi = (PropertyInfo)it.next();
        sb.append( pi.toString() + "\n" );
    }
    return sb.toString();
}

//------------------------------------------------------------------------------
} // end of class PropertyStore

