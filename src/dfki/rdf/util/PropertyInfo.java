package dfki.rdf.util;

import java.util.*;


public class PropertyInfo
{
//------------------------------------------------------------------------------
private String m_sName;
private boolean m_bMultiValue;
private Object m_singleValue;
private Collection m_collMultiValue;

//------------------------------------------------------------------------------
public PropertyInfo( String name, boolean hasMultiValue )
{
    m_sName = name;
    m_bMultiValue = hasMultiValue;
    m_singleValue = null;
    m_collMultiValue = ( hasMultiValue  ?  new HashSet()  :  null );  // UNORDERED, LIST DUE TO USING SET !!!!!!!!!!!
}

//------------------------------------------------------------------------------
public String getName()
{
    return m_sName;
}

//------------------------------------------------------------------------------
public boolean hasMultiValue()
{
    return m_bMultiValue;
}

//------------------------------------------------------------------------------
public Object getValue()
{
    ////System.out.println( "* PropertyInfo(" + m_sName + ").getValue()" );
    if( m_bMultiValue )
        return m_collMultiValue;
    else
        return m_singleValue;
}

//------------------------------------------------------------------------------
public void clearValue()
{
    ////System.out.println( "* PropertyInfo(" + m_sName + ").clearValue()" );
    if( m_bMultiValue )
        m_collMultiValue.clear();
    else
        m_singleValue = null;
}

//------------------------------------------------------------------------------
public void putValue( Object value )
{
    ////System.out.println( "* PropertyInfo(" + m_sName + ").putValue(" + (value!=null ? "<"+value.getClass().getName()+">" : "null" + ")") );
    if( m_bMultiValue )
        m_collMultiValue.add( value );
    else
        m_singleValue = value;
}

//------------------------------------------------------------------------------
public void setValues( Collection values )
{
    ////System.out.println( "* PropertyInfo(" + m_sName + ").setValues(" + (value!=null ? "<"+value.getClass().getName()+">" : "null" + ")") );
    if( !m_bMultiValue )
        throw new Error( "implementation failure; multi value and collection expected in PropertyInfo.setValues()" );

    m_collMultiValue.clear();
    m_collMultiValue.addAll( values );
}

//------------------------------------------------------------------------------
public boolean isEmpty()
{
    if( m_bMultiValue )
        return m_collMultiValue.isEmpty();
    else
        return( m_singleValue == null );
}

//------------------------------------------------------------------------------
} // end of class PropertyInfo

