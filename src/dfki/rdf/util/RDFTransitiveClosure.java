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


public class RDFTransitiveClosure
{
//-----------------------------------------------------------------------------
static public void main( String[] args )
{
	try
	{
		new RDFTransitiveClosure().go( args );
	}
	catch( Exception ex )
	{
		System.out.println( ex );
		ex.printStackTrace();
	}
}

//------------------------------------------------------------------------------
private RDFTransitiveClosure()   throws Exception
{
}

//------------------------------------------------------------------------------
private void go( String[] args )   throws Exception
{
	m_rdfFactory = RDF.factory();
	m_rdfParser = m_rdfFactory.createParser();

	String sRDFFile = args[0];
	Model m1 = loadRDF( sRDFFile );
	Model m2 = m_rdfFactory.createModel();
	
	NodeFactory nodeFactory1 = m1.getNodeFactory();
	for( int i = 1; i < args.length; i++ )
	{
		String sResource = args[i];
		Resource res = nodeFactory1.createResource( sResource );
		include( m1, res, m2 );
	}
	
	saveRDF( m2, sRDFFile + ".closure.rdf" );
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
private void include( Model m1, Resource res, Model m2 )   throws Exception
{
	Model mOutgoing = m1.find( res, null, null );
	if( copyStatements( mOutgoing, m2 ) )
	{	
		for( Enumeration enum = mOutgoing.elements(); enum.hasMoreElements(); )
		{
			Statement st = (Statement)enum.nextElement(); 
			Resource p = st.predicate();
			include( m1, p, m2 );
			RDFNode o = st.object();
			if( o instanceof Resource )
				include( m1, (Resource)o, m2 );
		}
	}

	Model mIncoming = m1.find( null, null, res );
	if( copyStatements( mIncoming, m2 ) )
	{	
		for( Enumeration enum = mIncoming.elements(); enum.hasMoreElements(); )
		{
			Statement st = (Statement)enum.nextElement(); 
			Resource s = st.subject();
			include( m1, s, m2 );
			Resource p = st.predicate();
			include( m1, p, m2 );
		}
	}
}

//------------------------------------------------------------------------------
private boolean copyStatements( Model mFrom, Model mTo )   throws Exception
{
	boolean bCopiedSomething = false;
	NodeFactory nodeFactory2 = mTo.getNodeFactory();

	for( Enumeration enum = mFrom.elements(); enum.hasMoreElements(); )
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

		Statement st2 = nodeFactory2.createStatement( s2, p2, o2 );
		if( !mTo.contains( st2 ) )
		{	
			mTo.add( st2 );
			bCopiedSomething = true;
		}
    }
	return bCopiedSomething;
}


//------------------------------------------------------------------------------

private RDFFactory m_rdfFactory;
private RDFParser m_rdfParser;


//------------------------------------------------------------------------------
} // end of class RDFTransitiveClosure

