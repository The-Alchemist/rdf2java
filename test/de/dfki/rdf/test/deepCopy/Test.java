package de.dfki.rdf.test.deepCopy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.dfki.rdf.util.RDFImport;
import de.dfki.rdf.util.THING;


public class Test
{
    final String NAMESPACE  = "http://dfki.org/rdf2java/deepCopyExample#";
    final String PACKAGE    = "dfki.rdf.test.deepCopy";
    
    Map mapObjects;
    
    

    public static void main( String[] args )   throws Exception
    {
        new Test().go();
    }
    
    private void go()   throws Exception
    {
        readin();
        // deepCopy_1();
        // deepCopy_2();
        deepCopy_3();
    }
    
    
    private void readin()   throws Exception
    {
        Map mapNamespace2Package = new HashMap();
        mapNamespace2Package.put( NAMESPACE, PACKAGE );
        RDFImport rdfImport = new RDFImport( mapNamespace2Package );

        // 1. import from RDF file "example1.rdf"
        mapObjects = rdfImport.importObjects( "testdata/deepCopy/deepCopy.rdf" );
        printout( mapObjects );
    }
    
    
    private void deepCopy_1()   throws Exception
    {
        THING.DeepCopyController dcc = new THING.DeepCopyController();
        THING oldSimple = (THING)mapObjects.get( NAMESPACE + "deepCopy_00007" );
        THING newSimple = oldSimple.deepCopy( dcc );
        printout( dcc.mapOldThingNewThing );
    }
    
    
    private void deepCopy_2()   throws Exception
    {
        THING.DeepCopyController dcc = new THING.DeepCopyController();
        THING oldHomer = (THING)mapObjects.get( NAMESPACE + "deepCopy_00008" );
        THING newHomer = oldHomer.deepCopy( dcc );
        printout( dcc.mapOldThingNewThing );
    }
    
    
    private void deepCopy_3()   throws Exception
    {
        THING.DeepCopyController dcc = new THING.DeepCopyController() {
            public boolean allowDeepCopy( Object sourceObject, String sSourcePropertyName )
            {
                return true;    // deep copy everywhere
            }
            public boolean allowDefaultHandlingForProperty( Object sourceObject, Object targetObject, String sSourcePropertyName )
            {
                THING.debug().warning( "*** allowDefaultHandlingForProperty:   sourceObject: " + sourceObject.getClass().getName() + ";   sSourcePropertyName=" + sSourcePropertyName );                
                return true;
            }
            public void targetMissesProperty( Object sourceObject, Object targetObject, String sSourcePropertyName )
            {
                if( sourceObject.getClass().equals( ComplexClass.class ) && sSourcePropertyName.equals( "complexSingleValueSlot" ) )
                {
                    THING.debug().warning( "*** targetMissesProperty:   handling special case ComplexClass . complexSingleValueSlot " );
                    ComplexClass value = ((ComplexClass)sourceObject).GetComplexSingleValueSlot();
                    try
                    {
                        ComplexClass2 newValue2 = (ComplexClass2)mapOldThingNewThing.get( (ComplexClass)value );
                        if( newValue2 == null )
                        {
                            newValue2 = (ComplexClass2) ((ComplexClass)value).deepCopy( ComplexClass2.class, this );
                        }
                        ((ComplexClass2)targetObject).putComplexSingleValueSlot2( newValue2 );
                        THING.debug().warning( "*** targetMissesProperty:   ((ComplexClass2)targetObject).putComplexSingleValueSlot2( newValue2 );" );
                    }
                    catch( CloneNotSupportedException e )
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        
        // try disabling/enabling the following line:
        // dcc.keepExistingURIs = true;
        
        THING oldHomer = (THING)mapObjects.get( NAMESPACE + "deepCopy_00008" );
        ComplexClass2 newHomer2 = (ComplexClass2) oldHomer.deepCopy( ComplexClass2.class, dcc );
        //// printout( mapObjects );
        printout( dcc.mapOldThingNewThing );
    }
    
    
    public static void printout( Map mapObjects )
    {
        System.out.println( "\n\n---------------------------------------------------------------------------------------------------------\n\n" );
        for( Iterator it = mapObjects.keySet().iterator(); it.hasNext(); )
        {
            String sURI = (String)it.next();
            Object obj = mapObjects.get( sURI );
            System.out.println( "\n########## " + sURI + " ##########\n" + obj );
        }
    }

    
} // end of class Test

