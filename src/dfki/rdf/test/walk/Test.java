package dfki.rdf.test.walk;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.w3c.rdf.model.Resource;
import org.w3c.rdf.util.RDFUtil;
import org.w3c.rdf.vocabulary.rdf_schema_200001.RDFS;

import dfki.rdf.util.KnowledgeBase;
import dfki.rdf.util.PropertyInfo;
import dfki.rdf.util.RDF2Java;
import dfki.rdf.util.RDFExport;
import dfki.rdf.util.RDFImport;
import dfki.rdf.util.RDFResource;
import dfki.rdf.util.RDFS2Class;
import dfki.rdf.util.nice.tinyxmldoc.TinyXMLDocument;
import dfki.rdf.util.nice.tinyxmldoc.TinyXMLElement;
import dfki.util.rdf.RDF;

public class Test
{
//---------------------------------------------------------------------------
dfki.rdf.util.KnowledgeBase m_kbCachedObjects;

HashMap m_mapNS2Pkg;
HashMap m_mapPkg2NS;

RDFImport m_rdfImport;
RDFExport m_rdfExport;

static final String NAMESPACE = "http://dfki.rdf.test/walk#";
static final String PACKAGE   = "dfki.rdf.test.walk";


//---------------------------------------------------------------------------
public static void main (String[] args)
{
    Test test1 = new Test();
    test1.go();
}

//---------------------------------------------------------------------------
Test ()
{
    m_kbCachedObjects = new KnowledgeBase();

    m_mapNS2Pkg = new HashMap();
    m_mapPkg2NS = new HashMap();
    m_mapNS2Pkg.put( NAMESPACE, PACKAGE );
    m_mapPkg2NS.put( PACKAGE, NAMESPACE );

    m_rdfImport = new dfki.rdf.util.RDFImport( m_mapNS2Pkg );
    m_rdfExport = new RDFExport( NAMESPACE, m_mapPkg2NS );
}

//---------------------------------------------------------------------------
void go ()
{
    Map mapObjects = readin( "walk" );
    // printoutMapValues("walk", mapObjects);
    m_kbCachedObjects.putAll( mapObjects );
    System.out.println( "knowledgeBase:\n" + m_kbCachedObjects );
    
//    go_1();
//    go_2();
//    go_3();
    go_4();
}

//---------------------------------------------------------------------------
void go_1()
{
    System.out.println( "\n\n\ngo_1:\n" );
    RDFResource resHomer = (RDFResource)m_kbCachedObjects.get( NAMESPACE + "Homer" );
    resHomer.walk( new RDFResource.WalkerController() {

        public void arriving( RDFResource currentResource )
        {
            System.out.print( "arriving   : " );
            for( int i = 0; i < lstPath.size(); i++ )
            {
                String s = ( (i % 2) == 0  ?  (String)lstPath.get( i )  :  ((RDFResource)lstPath.get( i )).getLocalName() );
                System.out.print( "    " + s );
            }
            System.out.println();
            // System.out.println( "action( " + currentResource.getLocalName() + " )" );
        }

        public void leaving( RDFResource currentResource )
        {
            System.out.print( "leaving    : " );
            for( int i = 0; i < lstPath.size(); i++ )
            {
                String s = ( (i % 2) == 0  ?  (String)lstPath.get( i )  :  ((RDFResource)lstPath.get( i )).getLocalName() );
                System.out.print( "    " + s );
            }
            System.out.println();
            // System.out.println( "action( " + currentResource.getLocalName() + " )" );
        }

        public void arrivingAgain( RDFResource currentResource )
        {
            System.out.print( "arriving2  : " );
            for( int i = 0; i < lstPath.size(); i++ )
            {
                String s = ( (i % 2) == 0  ?  (String)lstPath.get( i )  :  ((RDFResource)lstPath.get( i )).getLocalName() );
                System.out.print( "    " + s );
            }
            System.out.println();
            // System.out.println( "action( " + currentResource.getLocalName() + " )" );
        }
        
        public void leavingAgain( RDFResource currentResource )
        {
            System.out.print( "leaving2   : " );
            for( int i = 0; i < lstPath.size(); i++ )
            {
                Object o = lstPath.get( i );
                if( o instanceof RDFResource )
                {
                    RDFResource r = (RDFResource)o;
                    String s  =( alreadyVisitedOnWalk( r )  ?  "("+r.getLocalName()+")"  :  "<"+r.getLocalName()+">" );
                    System.out.print( "    " + s );
                }
                else
                {
                    System.out.print( "    " + (String)o );
                }
            }
            System.out.println();
        }        
    });
}

//---------------------------------------------------------------------------
void go_2()
{
    System.out.println( "\n\n\ngo_2:\n" );
    RDFResource resHomer = (RDFResource)m_kbCachedObjects.get( NAMESPACE + "Homer" );
    RDFResource.WalkerController wc = new RDFResource.WalkerController() {

        public void arriving( RDFResource currentResource )
        {
            System.out.print( "arriving   : " );
            for( int i = 0; i < lstPath.size(); i++ )
            {
                Object o = lstPath.get( i );
                if( o instanceof RDFResource )
                {
                    RDFResource r = (RDFResource)o;
                    String s  =( alreadyVisitedOnWalk( r )  ?  "("+r.getLocalName()+")"  :  "<"+r.getLocalName()+">" );
                    System.out.print( "    " + s );
                }
                else
                {
                    System.out.print( "    " + (String)o );
                }
            }
            System.out.println();
        }
        
        public void leaving( RDFResource currentResource )
        {
            System.out.print( "leaving    : " );
            for( int i = 0; i < lstPath.size(); i++ )
            {
                Object o = lstPath.get( i );
                if( o instanceof RDFResource )
                {
                    RDFResource r = (RDFResource)o;
                    String s  =( alreadyVisitedOnWalk( r )  ?  "("+r.getLocalName()+")"  :  "<"+r.getLocalName()+">" );
                    System.out.print( "    " + s );
                }
                else
                {
                    System.out.print( "    " + (String)o );
                }
            }
            System.out.println();
        }
        
        public void arrivingAgain( RDFResource currentResource )
        {
            System.out.print( "arriving2  : " );
            for( int i = 0; i < lstPath.size(); i++ )
            {
                Object o = lstPath.get( i );
                if( o instanceof RDFResource )
                {
                    RDFResource r = (RDFResource)o;
                    String s  =( alreadyVisitedOnWalk( r )  ?  "("+r.getLocalName()+")"  :  "<"+r.getLocalName()+">" );
                    System.out.print( "    " + s );
                }
                else
                {
                    System.out.print( "    " + (String)o );
                }
            }
            System.out.println();
        }
        
        public void leavingAgain( RDFResource currentResource )
        {
            System.out.print( "leaving2   : " );
            for( int i = 0; i < lstPath.size(); i++ )
            {
                Object o = lstPath.get( i );
                if( o instanceof RDFResource )
                {
                    RDFResource r = (RDFResource)o;
                    String s  =( alreadyVisitedOnWalk( r )  ?  "("+r.getLocalName()+")"  :  "<"+r.getLocalName()+">" );
                    System.out.print( "    " + s );
                }
                else
                {
                    System.out.print( "    " + (String)o );
                }
            }
            System.out.println();
        }
        
        public boolean walkingAllowed( RDFResource source, String prop, RDFResource dest )
        {
            if( alreadyWalkedThatWay( source, prop, dest ) ) 
                return false;
            if( prop.equals( "hasMother" ) || prop.equals( "hasFather" ) ) 
                return false;
            return true;
        }
    };
    resHomer.walk( wc );
    
    System.out.println( "\nlstWalk:" );
    for( Iterator it = wc.lstWalk.iterator(); it.hasNext(); )
    {
        Object o = it.next();
        String s = ( o instanceof RDFResource  ?  ((RDFResource)o).getLocalName()  :  (String)o );
        System.out.println( "    " + s );
    }
}

//---------------------------------------------------------------------------
void go_3()
{
    System.out.println( "\n\n\ngo_3:\n" );
    RDFResource resHomer = (RDFResource)m_kbCachedObjects.get( NAMESPACE + "Homer" );
    RDFResource.WalkerController wc = new RDFResource.WalkerController() {

        public void arriving( RDFResource currentResource )
        {
            for( int i = 0; i < lstPath.size(); i++ )
            {
                System.out.print( "    " );
            }
            System.out.print( "<" + currentResource.getLocalName() );
            Collection/*PropertyInfo*/ collPropInfos = currentResource.getPropertyStore().getPropertyInfos();
            for( Iterator it = collPropInfos.iterator(); it.hasNext(); )
            {
                PropertyInfo pi = (PropertyInfo)it.next();
                if( pi.getValue() == null ) continue;
                if(     pi.getValueType() == PropertyInfo.VT_STRING ||
                        pi.getValueType() == PropertyInfo.VT_SYMBOL )
                    System.out.print( " " + pi.getName() + "=\"" + (String)pi.getValue() + "\"" );
            }
            System.out.println( ">" );
        }
        
        Set/*RDFResource*/ setLeftResources = new HashSet();
        
        public void leaving( RDFResource currentResource )
        {
            for( int i = 0; i < lstPath.size(); i++ )
            {
                System.out.print( "    " );
            }
            System.out.println( "</" + currentResource.getLocalName() + ">" );
            setLeftResources.add( currentResource );
        }
        
        public void walkingAlongProperty( RDFResource source, String prop, Collection/*Object*/ values )
        {
            for( int i = 0; i < lstPath.size(); i++ )
            {
                System.out.print( "    " );
            }
            System.out.print( "    [" + prop + "]:    " );
            for( Iterator it = values.iterator(); it.hasNext(); )
                System.out.print( "  " + ((RDFResource)it.next()).getLocalName() );
            System.out.println();
        }
        
        public void returningFromProperty( RDFResource source, String prop, Collection/*Object*/ values )
        {
            for( int i = 0; i < lstPath.size(); i++ )
            {
                System.out.print( "    " );
            }
            System.out.println( "    [/" + prop + "]" );
        }
        
        public void arrivingAgain( RDFResource currentResource )
        {
            for( int i = 0; i < lstPath.size(); i++ )
            {
                System.out.print( "    " );
            }
            System.out.println( "(" + currentResource.getLocalName() + ")" );
        }
        
        public void leavingAgain( RDFResource currentResource )
        {
        }
        
        public boolean walkingAllowed( RDFResource source, String prop, RDFResource dest )
        {
            if( alreadyVisitedOnWalk( source ) )
            {
                if( alreadyVisitedOnPath( source ) )
                    return false;
                else
                    return !setLeftResources.contains( source );
            }
            return true;
        }
    };
    resHomer.walk( wc );
    
    System.out.println( "\nlstWalk:" );
    for( Iterator it = wc.lstWalk.iterator(); it.hasNext(); )
    {
        Object o = it.next();
        String s = ( o instanceof RDFResource  ?  ((RDFResource)o).getLocalName()  :  (String)o );
        System.out.println( "    " + s );
    }
}

//---------------------------------------------------------------------------
void go_4()
{
    System.out.println( "\n\n\ngo_4:\n" );
    RDFResource resHomer = (RDFResource)m_kbCachedObjects.get( NAMESPACE + "Homer" );
    
    // String sAsRDF = resHomer.toStringAsRDF( m_mapPkg2NS, null );
    String sAsRDF = resHomer.toStringAsRDF( m_mapPkg2NS, RDFS._Namespace );
    System.out.println( "resHomer.toStringAsRDF():\n" + sAsRDF );

    try
    {
        String sFilename = "testdata/walk/walk_go4.rdf";
        PrintWriter w = new PrintWriter( new FileOutputStream( sFilename ) );
        w.write( sAsRDF );
        w.close();
    }
    catch( FileNotFoundException e )
    {
        e.printStackTrace();
    }
}


//---------------------------------------------------------------------------
Map readin (String sLocalName)
{
    String sFilename = "testdata/walk/" + sLocalName + ".rdf";
    System.out.println("\n readin(" + sFilename + ")");

    try
    {
        m_rdfImport.reinit();
        Map mapObjects = m_rdfImport.importObjects(new java.io.FileReader(sFilename), m_kbCachedObjects);
        return mapObjects;
    }
    catch (Exception ex)
    {
        System.out.println("Exception in readin: " + ex.getMessage());
        ex.printStackTrace();
        System.exit(1);
        return null;
    }
}

//---------------------------------------------------------------------------
} // end of class Test

