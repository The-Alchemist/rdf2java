package dfki.rdf.util;

import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

import dfki.util.rdfs.RDFS;
import dfki.util.rdf.RDF;
import org.w3c.rdf.model.*;
import org.w3c.rdf.syntax.RDFParser;
import org.w3c.rdf.syntax.RDFSerializer;
import org.w3c.rdf.util.RDFFactory;


public class Dummy
{

static public void main( String[] args )
{
    try
    {
    	for( int i = 0; i < args.length; i++ )
    	{
    		try {
    			(new Dummy()).go( args[i] );
    		}
    		catch( Exception e ) {
    			JOptionPane.showMessageDialog( null, args[i] + "\n(" + e.getClass() + ")" + e.getMessage() + "\n" + e, "Exception occurred", JOptionPane.OK_OPTION );
    		}
    	}
    }
    catch( Exception ex )
    {
        System.out.println( ex );
        ex.printStackTrace();
    }
}

//------------------------------------------------------------------------------
private Dummy()   throws Exception
{
    m_rdfFactory = RDF.factory();
    m_rdfParser = m_rdfFactory.createParser();
}

//------------------------------------------------------------------------------
private void go( String sRDFFile )   throws Exception
{
    Model m1 = loadRDF( sRDFFile );
    Model m2 = convert( m1 );

    saveRDF( m2, sRDFFile );
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
private Model convert( Model m1 )   throws Exception
{
	Model m2 = m_rdfFactory.createModel();
	NodeFactory nodeFactory2 = m2.getNodeFactory();
    for( Enumeration enum = m1.elements(); enum.hasMoreElements(); )
    {
    	Statement st = (Statement)enum.nextElement(); 
		Resource s = st.subject();
		Resource p = st.predicate();
		RDFNode o = st.object();

		Resource s2 = nodeFactory2.createResource( s.getURI() );
		Resource p2 = nodeFactory2.createResource( p.getURI() );
		RDFNode o2;
		if( o instanceof Resource )
			o2 = nodeFactory2.createResource( ((Resource)o).getURI() );
		else
			o2 = nodeFactory2.createLiteral( o.getLabel() );

		
		if( p.getURI().equals( "http://km.dfki.de/model/wwf#memoUser" ) )
			p2 = nodeFactory2.createResource( "http://km.dfki.de/model/infotype#authors" );
		if( p.getURI().equals( "http://km.dfki.de/model/wwf#memoTitle" ) )
			p2 = nodeFactory2.createResource( "http://km.dfki.de/model/infotype#title" );
		if( p.getURI().equals( "http://km.dfki.de/model/infotype#contents" ) )
			p2 = nodeFactory2.createResource( "http://km.dfki.de/model/infotype#topics" );

		if( (o instanceof Resource) && ((Resource)o).getURI().equals( "http://km.dfki.de/model/wwf#Memo" ) )
			o2 = nodeFactory2.createResource( "http://km.dfki.de/model/infotype#Memo" );			

		if( (o instanceof Resource) && ((Resource)o).getURI().equals( "http://km.dfki.de/model/infotype#HtmlDocument" ) )
			o2 = nodeFactory2.createResource( "http://km.dfki.de/model/infotype#Document" );			

			
		Statement st2 = nodeFactory2.createStatement( s2, p2, o2 );
		m2.add( st2 );
    }

    return m2;
}



//------------------------------------------------------------------------------


private RDFFactory m_rdfFactory;
private RDFParser m_rdfParser;
private NodeFactory m_nodeFactory;


//------------------------------------------------------------------------------
} // end of class RDFDiff

