package dfki.rdf.util.filter;

import dfki.rdf.util.RDFFilter;
import dfki.rdf.util.RDFResource;
import dfki.rdf.util.THING;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;



public class FrodoRDFFilter   implements RDFFilter
{
//------------------------------------------------------------------------------
public FrodoRDFFilter( Collection/*String*/ primaryClasses,
                       Collection/*String*/ propertyValueClassesToFollow,
                       Collection/*String*/ propertyValueClassesToNotFollow)
{
    m_primaryClasses = primaryClasses;
    m_propertyValueClassesToFollow = propertyValueClassesToFollow;
    m_propertyValueClassesToNotFollow = propertyValueClassesToNotFollow;
}

//------------------------------------------------------------------------------
public Collection/*RDFResource*/ filter( Collection/*RDFResource*/ things )
{
    Collection collFilterResult = new HashSet();
    for (Iterator it = things.iterator(); it.hasNext(); )
    {
        Object obj = it.next();
        if( !(obj instanceof THING) )
            continue;
        if( !isClassContainedIn( obj.getClass().getName(), m_primaryClasses ) )
            continue;

        include( collFilterResult, (THING)obj,
                 m_propertyValueClassesToFollow,
                 m_propertyValueClassesToNotFollow);
    }
    return collFilterResult;
}

//------------------------------------------------------------------------------
private void include( Collection/*RDFResource*/ collFilterResult, THING thing,
                      Collection/*String*/ propertyValueClassesToFollow,
                      Collection/*String*/ propertyValueClassesToNotFollow)
{
    if( collFilterResult.contains( thing ) )
        return;
    collFilterResult.add( thing );

    Collection/*String*/ collProps = thing.getProperties();
    for (Iterator itProps = collProps.iterator(); itProps.hasNext(); )
    {
        String sProp = (String)itProps.next();
        Object propValue = thing.getPropertyValue( sProp );
        HashSet setValues = new HashSet();
        if( propValue instanceof Collection )
            setValues.addAll( (Collection)propValue );
        else
            setValues.add( propValue );

        for( Iterator itValues = setValues.iterator(); itValues.hasNext(); )
        {
            Object value = itValues.next();
            if ( !(value instanceof THING) )
                continue;
            String sValueClassName = value.getClass().getName();

            // if allowedClasses are not specified (null) => all classes are allowed
            if ( propertyValueClassesToFollow != null &&
                 !isClassContainedIn( sValueClassName, propertyValueClassesToFollow ) )
                 continue; // class is *not* an "allowed class" => skip

            if ( propertyValueClassesToNotFollow != null &&
                 isClassContainedIn( sValueClassName, propertyValueClassesToNotFollow ) )
                 continue; // class *is* a "disallowed class" => skip

            // ok, include the value for export => recursion!
            include( collFilterResult, (THING)value,
                     propertyValueClassesToFollow,
                     propertyValueClassesToNotFollow );

        }
    }
}

//------------------------------------------------------------------------------
private static boolean isClassContainedIn( String sClsName,
                                           Collection/*String*/ collAllowedClsNames )
{
    for (Iterator it = collAllowedClsNames.iterator(); it.hasNext(); )
    {
        String sAllowedClsName = (String)it.next();
        if ( sAllowedClsName.endsWith( "*" ) )
        {
            String sMatchString = sAllowedClsName.substring( 0, sAllowedClsName.length()-1 );
            if ( sClsName.startsWith( sMatchString ) )
                return true;
        }
        else
        if (sAllowedClsName.equals( sClsName ) )
        {
            return true;
        }
    }
    return false;  // not found
}


//------------------------------------------------------------------------------
private Collection/*String*/ m_primaryClasses;
private Collection/*String*/ m_propertyValueClassesToFollow;
private Collection/*String*/ m_propertyValueClassesToNotFollow;


//------------------------------------------------------------------------------
} // end of class FrodoRDFFilter

