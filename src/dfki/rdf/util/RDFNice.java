package dfki.rdf.util;

import java.io.*;
import java.util.*;

import dfki.util.debug.Debug;

import dfki.util.rdfs.RDFS;
import dfki.util.rdf.RDF;
import org.w3c.rdf.model.*;
import org.w3c.rdf.syntax.RDFParser;
import org.w3c.rdf.util.RDFFactory;

import org.w3c.dom.*;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;

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

String m_sSourceFile;
String m_sDestFile;
Model m_model;
Model m_modelRest;

Map/*String->Double*/ m_mapPred2Value = new HashMap();

DOMImplementation m_xmlDOM;
Document m_xmlDoc;
Element m_elDoc;

Map/*String->String*/ m_mapNS2Prefix;


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

    m_mapNS2Prefix = new TreeMap();
    m_mapNS2Prefix.put( m_rdfURIs.namespace(), "rdf" );
    m_mapNS2Prefix.put( m_rdfsURIs.namespace(), "rdfs" );
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

    m_modelRest = m_model.duplicate();
}

//------------------------------------------------------------------------------
private String getPrefix( String sURI )   throws Exception
{
    Resource res = m_nodeFactory.createResource( sURI );
    return getPrefix( res );
}

//------------------------------------------------------------------------------
private String getPrefix( Resource res )   throws Exception
{
    String sNS = res.getNamespace();
    String sPrefix = (String)m_mapNS2Prefix.get( sNS );
    if (sPrefix == null)
    {
        int pos = 0;
        for (int i = 0; i >= 0; i = sNS.indexOf( '/', i+1 ) )
            pos = i;
        sPrefix = sNS.substring( pos+1, sNS.length()-1 );
//        sPrefix = "ns" + Integer.toString( m_mapNS2Prefix.size() + 1 );
        m_mapNS2Prefix.put( sNS, sPrefix );
//        EntityReference er = m_xmlDoc.createEntityReference( "das:test" );
    }
    return sPrefix;
}

//------------------------------------------------------------------------------
private String res2qname( Resource res )   throws Exception
{
    String sPrefix = getPrefix( res );
    return sPrefix + ":" + res.getLocalName();
}

//------------------------------------------------------------------------------
private String uri2qnameRef( String sURI )   throws Exception
{
    Resource res = m_nodeFactory.createResource( sURI );
    String sPrefix = getPrefix( res );
    return "&" + sPrefix + ";" + res.getLocalName();
}

//------------------------------------------------------------------------------
private void createDocumentElement()   throws Exception
{
    m_xmlDOM = DOMImplementationRegistry.getDOMImplementation( "XML" );
    DocumentType docType = m_xmlDOM.createDocumentType( "rdf:RDF", null, null );
//    DocumentType docType = m_xmlDOM.createDocumentType( "rdf:RDF", "rdf", "http://rdf#RDF" );
    m_xmlDoc = m_xmlDOM.createDocument( m_rdfURIs.namespace(), "rdf:RDF", docType );
    m_elDoc = m_xmlDoc.getDocumentElement();
}

//------------------------------------------------------------------------------
private void createNiceXML()   throws Exception
{
    createDocumentElement();  // creates m_elDoc

    ////System.out.println( "\n" );
    while( true )
    {
        Resource resTopInstance = takeNextInstance();
        if( resTopInstance == null )
            break;
        appendInstance( resTopInstance, m_elDoc, new HashSet() );
    }

}

//------------------------------------------------------------------------------
private Element createElement( Resource res )   throws Exception
{
    Element el = m_xmlDoc.createElementNS( res.getNamespace(), res2qname( res ) );
    return el;
}

//------------------------------------------------------------------------------
private Text createTextNode( String sText )   throws Exception
{
    Text textNode = m_xmlDoc.createTextNode( sText );
    return textNode;
}

//------------------------------------------------------------------------------
private Attr addAttribute( Element el, String sNamespace, String sLocalName, String sValue )
{
    Attr attr = m_xmlDoc.createAttributeNS( sNamespace, sLocalName );
    attr.setNodeValue( sValue );
    el.setAttributeNodeNS( attr );
    return attr;
}

//------------------------------------------------------------------------------
private void appendInstance( Resource resInstance, Element elAppendHere,
                             Set/*String*/ setProcessedResources )   throws Exception
{
    removeInstanceFromRDFModelRest( resInstance );
    if( setProcessedResources.contains( resInstance.getURI() ) )
    {
        addAttribute( elAppendHere, m_rdfURIs.namespace(), "resource", uri2qnameRef( resInstance.getURI() ) );
        return;
    }
    setProcessedResources.add( resInstance.getURI() );

    Resource resCls = findCls( resInstance );
    System.out.println( "(" + resCls + ") " + resInstance );
    String sPrefix = getPrefix( resCls );

    Element elInst = createElement( resCls );
    addAttribute( elInst, m_rdfURIs.namespace(), "about", uri2qnameRef( resInstance.getURI() ) );
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
                Element elSlot = createElement( resPred );
                elInst.appendChild( elSlot );
                appendInstance( resValue, elSlot, setProcessedResources );  // recursion
            }
            else
                appendSlot( resInstance, elInst, resPred, resValue );
        }
        else
            appendSlot( resInstance, elInst, resPred, rdfnodeValue.getLabel() );
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
                resPred.getURI().equals( m_rdfsURIs.namespace() + "label" ) )
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
private void appendSlot( Resource resInstance, Element elInst, Resource resPred, Resource resValue )   throws Exception
{
    Element elSlot = createElement( resPred );
    elInst.appendChild( elSlot );
    addAttribute( elSlot, m_rdfURIs.namespace(), "resource", uri2qnameRef( resValue.getURI() ) );
}

//------------------------------------------------------------------------------
private void appendSlot( Resource resInstance, Element elInst, Resource resPred, String sValue )   throws Exception
{
    Element elSlot = createElement( resPred );
    elInst.appendChild( elSlot );
    Text textNode = createTextNode( sValue );
    elSlot.appendChild( textNode );

    //    addAttribute( elInst, getPrefix( resPred ), resPred.getLocalName(), sValue );
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
private void saveNiceXML()   throws Exception
{
    ////System.out.println( "\n\nmap:\n" + m_mapNS2Prefix );

/***
    DocumentType docType = m_xmlDoc.getDoctype();
    NamedNodeMap nnm = docType.getEntities();
//    nnm.setNamedItemNS( m_xmlDoc.createElementNS( m_rdfURIs.namespace(), "rdf:RDF" ) );
    String sNS = "http://dfki.frodo.wwf/task#";
    String sAbrev = (String)m_mapNS2Prefix.get( sNS );
    org.apache.xerces.dom.EntityImpl e = (org.apache.xerces.dom.EntityImpl)((org.apache.xerces.dom.CoreDocumentImpl)m_xmlDoc).createEntity( sAbrev );
    e.setBaseURI( sNS );
    nnm.setNamedItemNS( e );
//    nnm = docType.getNotations();
//    nnm.setNamedItemNS( m_xmlDoc.createElementNS( m_rdfURIs.namespace(), "rdf:RDF" ) );

    String sInternalSubset = docType.getInternalSubset();
***/


    PrintWriter pw = new PrintWriter( new FileOutputStream( m_sDestFile ) );
    OutputFormat outputFormat = new OutputFormat( m_xmlDoc, "ISO-8859-1", true );
        outputFormat.setLineWidth( 0 );
    XMLSerializer xmlSerializer = new XMLSerializer( pw, outputFormat );
    xmlSerializer.serialize( m_xmlDoc );
    pw.flush();
}

//------------------------------------------------------------------------------
} // end of class RDFNice

