
package de.dfki.rdf.util;

import java.io.*;
import java.util.*;

import de.dfki.util.rdf.RDF;
import org.w3c.rdf.model.*;
import org.w3c.rdf.syntax.RDFParser;
import org.w3c.rdf.syntax.RDFSerializer;
import org.w3c.rdf.util.RDFFactory;


public class RDFExtract
{

    static public void main( String[] args )
    {
        try
        {
            if( args.length != 3 )
            {
                System.out.println( "usage: RDFDiff <rdf-file> <export-rdf-file> <subject-namespace>\n" +
                                    "       extracts all statements where subject starts with <subject-namespace>" );
                return;
            }
            (new RDFExtract()).go( args );
        }
        catch( Exception ex )
        {
            System.out.println( ex );
            ex.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------
    private RDFExtract() throws Exception
    {
        m_rdfFactory = RDF.factory();
        m_rdfParser = m_rdfFactory.createParser();
    }

    //------------------------------------------------------------------------------
    private void go( String[] args ) throws Exception
    {
        m_sFilename        = args[0];
        m_sFilenameExtract = args[1];
        m_sClsUriPattern   = args[2];

        Model modelOrig = loadRDF( m_sFilename );
        Model modelExtract = extract( modelOrig, m_sClsUriPattern );
        saveRDF( modelExtract, m_sFilenameExtract );
    }

    //------------------------------------------------------------------------------
    private Model loadRDF( String sRDFFilename ) throws Exception
    {
        Model model = m_rdfFactory.createModel();
        RDF.parse( "file:" + sRDFFilename, m_rdfParser, model );

        System.out.println( "*** " + sRDFFilename + " loaded; " + model.size()
                + " statements read in ***" );

        return model;
    }

    //------------------------------------------------------------------------------
    private void saveRDF( Model model, String sRDFFilename ) throws Exception
    {
        RDFSerializer rdfSerializer = m_rdfFactory.createSerializer();
        RDF.saveModel( model, sRDFFilename, rdfSerializer );

        System.out.println( "*** " + sRDFFilename + " saved; " + model.size()
                + " statements written ***" );
    }

    //------------------------------------------------------------------------------
    private Model extract( Model modelOrig, String sClsUriPattern ) throws Exception
    {
        Model modelExtract = m_rdfFactory.createModel();
        
        for( Enumeration enum = modelOrig.elements(); enum.hasMoreElements(); )
        {
            Statement st = (Statement)enum.nextElement();
            Resource res = st.subject();
            if( !res.getURI().startsWith( sClsUriPattern ) ) continue;
            System.out.println( st );
            modelExtract.add( st );
        }

        return modelExtract;
    }


    //------------------------------------------------------------------------------
    private boolean equal( Statement st1, Statement st2 ) throws Exception
    {
        String s1 = st1.subject().getURI();
        String s2 = st2.subject().getURI();
        if( !s1.equals( s2 ) ) return false;

        String p1 = st1.predicate().getURI();
        String p2 = st2.predicate().getURI();
        if( !p1.equals( p2 ) ) return false;

        String o1 = (st1.object() instanceof Resource ? ((Resource) st1
                .object()).getURI() : st1.object().getLabel());
        String o2 = (st2.object() instanceof Resource ? ((Resource) st2
                .object()).getURI() : st2.object().getLabel());
        if( !o1.equals( o2 ) ) return false;

        return true;
    }


    //------------------------------------------------------------------------------
    private String m_sFilename;
    private String m_sFilenameExtract;
    private String m_sClsUriPattern;

    private RDFFactory m_rdfFactory;
    private RDFParser m_rdfParser;
    private NodeFactory m_nodeFactory;


    //------------------------------------------------------------------------------
} // end of class RDFExtract

