package dfki.rdf.util;

import java.util.*;


public class PropertyInfo
{
//------------------------------------------------------------------------------
private String m_sName;
private boolean m_bMultiValue;
private Object m_singleValue;
private Collection m_collMultiValue;

private int m_iValueType = VT_UNKNOWN;
public static final int VT_UNKNOWN  = 0;
public static final int VT_STRING   = 1;
public static final int VT_INSTANCE = 2;
public static final int VT_SYMBOL   = 3;

private Collection/*Class*/ m_collAllowedValueClasses;
private String[] m_asAllowedSymbols;


//------------------------------------------------------------------------------
public static PropertyInfo createStringProperty(
                                String name, boolean hasMultiValue )
{
    return new PropertyInfo( name, VT_STRING, null, null, hasMultiValue );
}

//------------------------------------------------------------------------------
/** <code>allowedValueClasses</code> may be set to null,
  * meaning there are no value class restrictions.
  */
public static PropertyInfo createInstanceProperty(
                                String name, Class[] allowedValueClasses,
                                boolean hasMultiValue )
{
    return new PropertyInfo( name, VT_INSTANCE, allowedValueClasses, null, hasMultiValue );
}

//------------------------------------------------------------------------------
public static PropertyInfo createSymbolProperty(
                                String name, String[] allowedSymbols,
                                boolean hasMultiValue )
{
    return createSymbolProperty( name, allowedSymbols, null, hasMultiValue );
}

//------------------------------------------------------------------------------
public static PropertyInfo createSymbolProperty(
                                String name, String[] allowedSymbols,
                                String[] defaultValues,
                                boolean hasMultiValue )
{
    PropertyInfo pi = new PropertyInfo( name, VT_SYMBOL, null, allowedSymbols, hasMultiValue );
    if( defaultValues != null && defaultValues.length > 0 )
    {
        if( hasMultiValue )
            pi.setValues( Arrays.asList( defaultValues ) );
        else
            pi.putValue( defaultValues[0] );
    }
    return pi;
}

//------------------------------------------------------------------------------
private PropertyInfo( String name, int valueType,
                     Class[] allowedValueClasses, String[] allowedSymbols,
                     boolean hasMultiValue )
{
    m_sName = name;
    m_iValueType = valueType;
    m_collAllowedValueClasses = new HashSet();
    if( m_iValueType == VT_STRING )
        m_collAllowedValueClasses.add( String.class );

    if( allowedValueClasses != null )
        setAllowedValueClasses( allowedValueClasses, true/*don't care*/ );

    if( allowedSymbols != null )
        m_asAllowedSymbols = allowedSymbols;

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
public int getValueType()
{
    return m_iValueType;
}

//------------------------------------------------------------------------------
/**
 * Sets / adds allowed value classes to this property;
 * if append is true, the given classes are appended, otherwise the given
 * classes are the only allowed classes.<br>
 * <b>Note:</b> The inhere stored set of allowed classes are just for information
 * purpose; there will be no checking done in methods like <code>putValue</code>!
 */
public void setAllowedValueClasses( Collection/*Class*/ allowedClasses, boolean append)
{
    if( !append )
        m_collAllowedValueClasses.clear();
    if( allowedClasses != null )
        m_collAllowedValueClasses.addAll( allowedClasses );
}

//------------------------------------------------------------------------------
/**
 * Sets / adds allowed value classes to this property;
 * if append is true, the given classes are appended, otherwise the given
 * classes are the only allowed classes.<br>
 * <b>Note:</b> The inhere stored set of allowed classes are just for information
 * purpose; there will be no checking done in methods like <code>putValue</code>!
 */
public void setAllowedValueClasses( Class[] allowedClasses, boolean append)
{
    setAllowedValueClasses( Arrays.asList( allowedClasses ), append );
}

//------------------------------------------------------------------------------
public Collection/*Class*/ getAllowedValueClasses()
{
    return m_collAllowedValueClasses;
}

//------------------------------------------------------------------------------
public boolean isValueClassAllowed( Class clsValue )
{
    return m_collAllowedValueClasses.contains( clsValue );
}

//------------------------------------------------------------------------------
public String[] getAllowedSymbols()
{
    return m_asAllowedSymbols;
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

