package de.dfki.rdf.test.datetime;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.dfki.rdf.util.RDFExport;
import de.dfki.rdf.util.RDFImport;
import de.dfki.rdf.util.THING;


public class Test
{
    final String NAMESPACE  = "http://de.dfki.rdf.test/datetime#";
    final String PACKAGE    = "de.dfki.rdf.test.datetime";
    final String FILENAME   = "testdata/datetime/humans.rdf";
    
    Map mapObjects;
    
    

    public static void main( String[] args )   throws Exception
    {
        new Test().go();
    }
    
    private void go()   throws Exception
    {
        readin();
        
        for( int i = 0; i < 10; i++ )
            breedNewHuman();
        
        writeout();
    }
    
    
    private void readin()   throws Exception
    {
        File f = new File( FILENAME );
        if( !f.exists() ) 
        {
            mapObjects = new HashMap();
            return;
        }
        
        Map mapNamespace2Package = new HashMap();
        mapNamespace2Package.put( NAMESPACE, PACKAGE );
        RDFImport rdfImport = new RDFImport( mapNamespace2Package );
        
        mapObjects = rdfImport.importObjects( FILENAME );
        
        for( Iterator it = mapObjects.values().iterator(); it.hasNext(); )
        {
            Human human = (Human)it.next();
            System.out.println( human.GetDateOfBirth() + "\t" + human.getName() );
        }
        System.out.println();
    }
    
    private void breedNewHuman()
    {
        Human human = new Human();
        human.makeNewURI();
        human.putName( Test.genName() );
        human.putDateOfBirth( new Date() /*now!*/ );
        mapObjects.put( human.getURI(), human );
        System.out.println( "given birth to \"" + human.getName() + "\"" );
    }
    
    void writeout ()
    {
        try
        {
            Map mapPkg2NS = new HashMap();
            mapPkg2NS.put( PACKAGE, NAMESPACE );
            RDFExport rdfExport = new RDFExport( NAMESPACE, mapPkg2NS );
            
            rdfExport.exportObjects( mapObjects.values(), FILENAME );
        }
        catch (Exception ex)
        {
            System.out.println("Exception in writeout: " + ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    
    final static String[] SYLLABLES = { 
            "bar", "car", "di", "fi", "ga", "han", "klau", "le", "mo", "na", "pia", "ro", "si", "tel" };
    
    static private String genName()
    {
        int nrSyl = (int)( Math.random() * 3.0 ) + 3;
        StringBuffer sb = new StringBuffer();
        for( int i = 0; i < nrSyl; i++)
        {
            int s = (int)( Math.random() * (double)SYLLABLES.length );
            String syl = SYLLABLES[ s ];
            sb.append( syl );
        }
        String sName = sb.toString();
        sName = "" + Character.toUpperCase( sName.charAt(0) ) + sName.substring( 1 );
        return sName;
    }
    
} // end of class Test

