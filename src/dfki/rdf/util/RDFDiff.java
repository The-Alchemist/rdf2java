package dfki.rdf.util;

import java.io.*;
import java.util.*;

import dfki.util.rdfs.RDFS;
import dfki.util.rdf.RDF;
import org.w3c.rdf.model.*;
import org.w3c.rdf.syntax.RDFParser;
import org.w3c.rdf.syntax.RDFSerializer;
import org.w3c.rdf.util.RDFFactory;


/**
 * <p>
 * RDFDiff - calculates difference between two RDF files.
 * </p><p>
 * This is a command-line-only application, i.e. the only public method
 * is {@link #main}.
 * </p><p>
 * Calling RDFDiff results in two more RDF files. If you called, e.g.,
 * <code>RDFDiff abc.rdf xyz.rdf</code>, then
 * the newly created file <code>abc.rdf.diff.rdf</code> will contain all
 * statements of <code>abc.rdf</code> minus the ones in <code>xyz.rdf</code>.
 * Analogously a file <code>xyz.rdf.diff.rdf</code> is created, too.
 */
public class RDFDiff
{
/*******************************************************************************
 * Usage: <code>RDFDiff &lt;rdf-file1&gt; &lt;rdf-file2&gt;</code>
 */
static public void main( String[] args )
{
    try
    {
        if( args.length != 2 ) {
            System.out.println( "usage: RDFDiff <rdf-file1> <rdf-file2>" );
            return;
        }
        (new RDFDiff()).go( args );
    }
    catch( Exception ex )
    {
        System.out.println( ex );
        ex.printStackTrace();
    }
}

//------------------------------------------------------------------------------
private RDFDiff()   throws Exception
{
    m_rdfFactory = RDF.factory();
    m_rdfParser = m_rdfFactory.createParser();
}

//------------------------------------------------------------------------------
private void go( String[] args )   throws Exception
{
    m_sFilename1 = args[0];
    m_sFilename2 = args[1];
    m_sFilenameDiff1minus2 = m_sFilename1 + ".diff.rdf";
    m_sFilenameDiff2minus1 = m_sFilename2 + ".diff.rdf";

    Model m1 = loadRDF( m_sFilename1 );
    Model m2 = loadRDF( m_sFilename2 );

    Model m1minus2 = diff( m1, m2 );
    Model m2minus1 = diff( m2, m1 );
    saveRDF( m1minus2, m_sFilenameDiff1minus2 );
    saveRDF( m2minus1, m_sFilenameDiff2minus1 );
}

//------------------------------------------------------------------------------
private Model loadRDF( String sRDFFilename )   throws Exception
{
    Model model = m_rdfFactory.createModel();
    RDF.parse( "file:" + sRDFFilename, m_rdfParser, model );

    System.out.println( "*** " + sRDFFilename + " loaded; " + model.size() + " statements read in ***" );

    return model;
}

//------------------------------------------------------------------------------
private void saveRDF( Model model, String sRDFFilename )   throws Exception
{
    RDFSerializer rdfSerializer = m_rdfFactory.createSerializer();
    dfki.util.rdf.RDF.saveModel( model, sRDFFilename, rdfSerializer );

    System.out.println( "*** " + sRDFFilename + " saved; " + model.size() + " statements written ***" );
}

//------------------------------------------------------------------------------
private Model diff( Model m1, Model m2 )   throws Exception
{
    Model mDiff = m1.duplicate();
    remove( mDiff, m2 );

    return mDiff;
}

//------------------------------------------------------------------------------
private void remove( Model mDest, Model mSource )   throws Exception
{
    for( Enumeration enum = mSource.elements(); enum.hasMoreElements(); )
    {
        Statement st = (Statement)enum.nextElement();
        remove( mDest, st );
    }
}

//------------------------------------------------------------------------------
private void remove( Model model, Statement st )   throws Exception
{
    for( Enumeration enum = model.elements(); enum.hasMoreElements(); )
    {
        Statement stInModel = (Statement)enum.nextElement();
        if( equal( stInModel, st ) )
            model.remove( stInModel );
    }
}

//------------------------------------------------------------------------------
private boolean equal( Statement st1, Statement st2 )   throws Exception
{
    String s1 = st1.subject().getURI();
    String s2 = st2.subject().getURI();
    if( !s1.equals( s2 ) )
        return false;

    String p1 = st1.predicate().getURI();
    String p2 = st2.predicate().getURI();
    if( !p1.equals( p2 ) )
        return false;

    String o1 = ( st1.object() instanceof Resource  ?  ((Resource)st1.object()).getURI()  :  st1.object().getLabel() );
    String o2 = ( st2.object() instanceof Resource  ?  ((Resource)st2.object()).getURI()  :  st2.object().getLabel() );
    if( !o1.equals( o2 ) )
        return false;

    return true;
}

//------------------------------------------------------------------------------
private void add( Model mDest, Model mSource )   throws Exception
{
    for( Enumeration enum = mSource.elements(); enum.hasMoreElements(); )
    {
        Statement st = (Statement)enum.nextElement();
        mDest.add( st );
    }
}


//------------------------------------------------------------------------------
private String m_sFilename1;
private String m_sFilename2;
private String m_sFilenameDiff1minus2;
private String m_sFilenameDiff2minus1;

private RDFFactory m_rdfFactory;
private RDFParser m_rdfParser;
private NodeFactory m_nodeFactory;


//------------------------------------------------------------------------------
} // end of class RDFDiff

