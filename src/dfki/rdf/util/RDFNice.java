package dfki.rdf.util;

import java.io.*;
import java.util.*;

import dfki.util.debug.Debug;

import dfki.util.rdfs.RDFS;
import dfki.util.rdf.RDF;
import org.w3c.rdf.model.*;
import org.w3c.rdf.syntax.RDFParser;
import org.w3c.rdf.util.RDFFactory;

import dfki.rdf.util.nice.*;
import dfki.rdf.util.nice.tinyxmldoc.*;


public class RDFNice
{
//------------------------------------------------------------------------------
RDF.Syntax m_rdfURIs;
RDFS.URIs m_rdfsURIs;
RDFFactory m_rdfFactory;
RDFParser m_rdfParser;
NodeFactory m_nodeFactory;

Resource m_resRDFSLiteral;
Resource m_resRDFSResCls;
Resource m_resRDFSPredSubClassOf;
Resource m_resPredType;
Resource m_resPredResource;
Resource m_resPredAbout;

String m_sSourceFile;
String m_sDestFile;
Model m_model;
Model m_modelRest;

Map/*String->Double*/ m_mapPred2Value = new HashMap();

TinyXMLDocument m_xmlDoc;
TinyXMLElement m_elDoc;


//------------------------------------------------------------------------------
public RDFNice()   throws Exception
{
    m_rdfURIs = RDF.syntax();
    m_rdfsURIs = RDFS.getURIs();
    m_rdfFactory = RDF.factory();
    m_rdfParser = m_rdfFactory.createParser();
    m_nodeFactory = m_rdfFactory.getNodeFactory();

    m_resRDFSLiteral = m_nodeFactory.createResource(m_rdfsURIs.literal());
    m_resRDFSResCls = m_nodeFactory.createResource(m_rdfsURIs._class());
    m_resRDFSPredSubClassOf = m_nodeFactory.createResource(m_rdfsURIs.subClassOf());
    m_resPredType = RDF.syntax().type();

    m_resPredResource = m_nodeFactory.createResource( m_rdfURIs.namespace() + "resource" );
    m_resPredAbout = m_nodeFactory.createResource( m_rdfURIs.namespace() + "about" );
}

//------------------------------------------------------------------------------
static public void main( String[] args )
{
    try
    {
        if( args.length < 1 ) {
            System.out.println( "missing parameter: <rdf-file>" );
            return;
        }
        (new RDFNice()).go( args );
    }
    catch( Exception ex )
    {
        System.out.println( ex );
        ex.printStackTrace();
    }
}

//------------------------------------------------------------------------------
private void go( String[] args )   throws Exception
{
    m_sSourceFile = args[0];
    m_sDestFile = m_sSourceFile + ".nice.rdf";

    for( int i = 1; i < args.length; i += 2 )
    {
        String sPred = args[i];
        Double dValue = new Double( args[i+1] );
        m_mapPred2Value.put( sPred, dValue );
    }

    loadRDF( m_sSourceFile );
    createNiceXML();
    saveNiceXML();
}

//------------------------------------------------------------------------------
private double getValueForPred( String sPred )
{
    Double d = (Double)m_mapPred2Value.get( sPred );
    return ( d != null  ?  d.doubleValue()  :  0.0 );
}

//------------------------------------------------------------------------------
private void loadRDF( String sRDFFile )   throws Exception
{
    m_model = m_rdfFactory.createModel();
    RDF.parse( "file:" + sRDFFile, m_rdfParser, m_model );

    System.out.println( "*** " + m_model.size() + " statements read in ***" );

    m_modelRest = m_model.duplicate();
}

//------------------------------------------------------------------------------
private void createNiceXML()   throws Exception
{
    createDocumentElement();  // creates m_elDoc, too

    while( true )
    {
        Resource resTopInstance = takeNextInstance();
        if( resTopInstance == null )
            break;
        appendInstance( resTopInstance, m_elDoc, new HashSet() );
    }

}

//------------------------------------------------------------------------------
private TinyXMLElement createElement( Resource res )   throws Exception
{
    TinyXMLElement el = m_xmlDoc.createElement( res.getURI() );
    return el;
}

//------------------------------------------------------------------------------
private TinyXMLTextNode createTextNode( String sText )   throws Exception
{
    TinyXMLTextNode textNode = m_xmlDoc.createTextNode( sText );
    return textNode;
}

//------------------------------------------------------------------------------
private void appendInstance( Resource resInstance, TinyXMLElement elAppendHere,
                             Set/*String*/ setProcessedResources )   throws Exception
{
    removeInstanceFromRDFModelRest( resInstance );
    if( setProcessedResources.contains( resInstance.getURI() ) )
    {
        elAppendHere.setAttribute( m_resPredResource.getURI(), resInstance );
        return;
    }
    setProcessedResources.add( resInstance.getURI() );

    Resource resCls = findCls( resInstance );
    TinyXMLElement elInst = createElement( resCls );
    elInst.setAttribute( m_resPredAbout.getURI(), resInstance );
    elAppendHere.appendChild( elInst );


    Model mSlots = m_model.find( resInstance, null, null );
    while( true )
    {
        Statement st = takeNextSlotStatement( mSlots );
        if( st == null )
            break;

        Resource resPred = st.predicate();
        RDFNode rdfnodeValue = st.object();
        if( rdfnodeValue instanceof Resource )
        {
            Resource resValue = (Resource)rdfnodeValue;
            Resource resClsChild = findCls( resValue );
            double dValue = getValueForPred( resPred.getURI() );
            if( dValue >= 0  &&  resClsChild  != null )
            {
                TinyXMLElement elSlot = createElement( resPred );
                elInst.appendChild( elSlot );
                appendInstance( resValue, elSlot, setProcessedResources );  // recursion
            }
            else
            {
                appendSlot( elInst, resPred, resValue );
            }
        }
        else
        {
            appendSlot( elInst, resPred, rdfnodeValue.getLabel() );
        }
    }
}

//------------------------------------------------------------------------------
private Statement takeNextSlotStatement( Model mSlotsRest )   throws Exception
{
    Statement stBest = null;
    double dBest = 0.0;
    for( Enumeration enumSlots = mSlotsRest.elements(); enumSlots.hasMoreElements(); )
    {
        Statement st = (Statement)enumSlots.nextElement();
        Resource resPred = st.predicate();
        if(     resPred.getURI().equals( m_resPredType.getURI()           ) ||
                resPred.getURI().equals( m_rdfsURIs.namespace() + "label" ) )               // ??? !!! ??? !!! ??? !!! ??? !!! ???
            continue;
        double dVal = getValueForPred( resPred.getURI() );
        if( stBest == null  ||  dVal > dBest )
        {
            dBest = dVal;
            stBest = st;
        }
    }
    if( stBest != null )
        mSlotsRest.remove( stBest );
    return stBest;
}

//------------------------------------------------------------------------------
private void appendSlot( TinyXMLElement elInst, Resource resPred, Resource resValue )   throws Exception
{
    TinyXMLElement elSlot = createElement( resPred );
    elInst.appendChild( elSlot );
    elSlot.setAttribute( m_resPredResource.getURI(), resValue );
}

//------------------------------------------------------------------------------
private void appendSlot( TinyXMLElement elInst, Resource resPred, String sValue )   throws Exception
{
    elInst.setAttribute( resPred.getNamespace() + resPred.getLocalName(), sValue );
}

//------------------------------------------------------------------------------
private Resource takeNextInstance()   throws Exception
{
    Model mInstances = m_modelRest.find( null, m_resPredType, null );
    if (mInstances.size() <= 0)
        return null;

    Statement stBestInstance = null;
    Resource resBestInstance = null;
    double dBestInstance = 0.0;
    for( Enumeration enumInst = mInstances.elements(); enumInst.hasMoreElements(); )
    {
        Statement st = (Statement)enumInst.nextElement();
        Resource resSubject = (Resource)st.subject();
        double dInstanceValue = 0.0;
        Model mTest = m_model.find( null, null, resSubject );
        int nrLinksToInst = 0;
        for( Enumeration enum = mTest.elements(); enum.hasMoreElements(); )
        {
            Statement s = (Statement)enum.nextElement();
            String sPred = s.predicate().getURI();
            dInstanceValue -= getValueForPred( sPred );  // NEGATIVE!!!
            ////System.out.println( "     " + (-getValueForPred( sPred )) + " # " + sPred );
        }
        mTest = m_model.find( resSubject, null, null );
        int nrLinksFromInst = 0;
        for( Enumeration enum = mTest.elements(); enum.hasMoreElements(); )
        {
            Statement s = (Statement)enum.nextElement();
            String sPred = s.predicate().getURI();
            dInstanceValue += getValueForPred( sPred );
            ////System.out.println( "     " + getValueForPred( sPred ) + " # " + sPred );
        }
        ////System.out.println( "   # " + dInstanceValue + " # " + resSubject.getURI() );
        if( stBestInstance == null  ||  dInstanceValue > dBestInstance )
        {
            dBestInstance = dInstanceValue;
            resBestInstance = resSubject;
            stBestInstance = st;
        }
    }

    if( stBestInstance == null )
        return null;

    m_modelRest.remove( stBestInstance );
    return resBestInstance;
}

//------------------------------------------------------------------------------
private void removeInstanceFromRDFModelRest( Resource resInstance )   throws Exception
{
    Model m = m_modelRest.find( resInstance, m_resPredType, null );
    for( Enumeration enum = m.elements(); enum.hasMoreElements(); )
    {
        Statement st = (Statement)enum.nextElement();
        m_modelRest.remove( st );
    }
}

//------------------------------------------------------------------------------
private Collection/*Resource*/ findTopInstances()   throws Exception
{
    HashSet setTopResources = new HashSet();
    Model mInstances = m_model.find( null, m_resPredType, null );
    for( Enumeration enumInstances = mInstances.elements(); enumInstances.hasMoreElements(); )
    {
        Statement st = (Statement)enumInstances.nextElement();
        Resource resSubject = (Resource)st.subject();
        Model mTest = m_model.find( null, null, resSubject );
        if( mTest.size() <= 0 )
            setTopResources.add( resSubject );
    }
    return setTopResources;
}

//------------------------------------------------------------------------------
private Collection/*Resource*/ findInstances()   throws Exception
{
    HashSet setResources = new HashSet();
    Model mInstances = m_model.find( null, m_resPredType, null );
    for( Enumeration enumInstances = mInstances.elements(); enumInstances.hasMoreElements(); )
    {
        Statement st = (Statement)enumInstances.nextElement();
        Resource resSubject = (Resource)st.subject();
        setResources.add( resSubject );
    }
    return setResources;
}

//------------------------------------------------------------------------------
private Resource findCls( Resource resObject )   throws Exception
{
    Model mCls = m_model.find( resObject, m_resPredType, null );
    if( mCls == null  ||  mCls.isEmpty() )
        return null;
    return (Resource)((Statement)RDF.get1( mCls )).object();
}

//------------------------------------------------------------------------------
private void createDocumentElement()   throws Exception
{
    m_xmlDoc = new TinyXMLDocument();
    m_elDoc = m_xmlDoc.createElement( m_rdfURIs.namespace() + "RDF" );
    m_xmlDoc.setDocumentElement( m_elDoc );
}

//------------------------------------------------------------------------------
private void saveNiceXML()   throws Exception
{
    PrintWriter pw = new PrintWriter( new FileOutputStream( m_sDestFile ) );
    pw.print( m_xmlDoc.serialize() );
    pw.flush();
    pw.close();
}

//------------------------------------------------------------------------------
} // end of class RDFNice

