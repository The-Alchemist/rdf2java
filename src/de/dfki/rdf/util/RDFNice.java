package de.dfki.rdf.util;

import java.io.*;
import java.util.*;

import de.dfki.rdf.util.nice.*;
import de.dfki.rdf.util.nice.tinyxmldoc.*;
import de.dfki.util.debug.Debug;

import de.dfki.util.rdf.RDF;
import de.dfki.util.rdfs.RDFS;

import org.w3c.rdf.model.*;
import org.w3c.rdf.syntax.RDFParser;
import org.w3c.rdf.util.RDFFactory;



/**
 * <p>
 * RDFNice - serializes RDF models hierarchically
 * </p><p>
 * The serialization / transformation of an RDF model to a hierarchical
 * XML model has to be done as follows:
 * <ol>
 *   <li> You start by calling one of the constructors. You can decide whether
 *        to read in an RDF file or to pass an already existing RDF model
 *   </li>
 *   <li> Optionally you can set up values for some predicates (in the RDF
 *        model) via method {@link #setPredValue setPredValue}.
 *   </li>
 *   <li> Then you call {@link #createNiceXML createNiceXML} to build the
 *        hierarchical XML model, which does what this class is about.
 *   </li>
 *   <li> Finally you can serialize the constructed (internal) XML model
 *        via {@link #save save} or one of the <code>serializeTo</code> methods.
 *   </li>
 * </ol>
 * </p><p>
 * Alternatively to the procedure above you can just call the {@link #main main}
 * method which transforms an existing RDF file to another RDF file.
 * </p><p>
 * <b>Note:</b> The internal XML data structure can not be retrieved.
 * </p>
 */
public class RDFNice
{
/*******************************************************************************
 * Initializes RDFNice with a given RDF model.
 */
public RDFNice( Model rdfModel )
{
    this();
    m_model = rdfModel;
}

/*******************************************************************************
 * Initializes RDFNice with the contents of an RDF file.
 * @param sRDFFilename the filename of the RDF file
 */
public RDFNice( String sRDFFilename )
{
    this();
    loadFromFile( sRDFFilename );
}

/*******************************************************************************
 * Initializes RDFNice with the contents in a reader.
 * The reader is expected to deliver an XML serialization of an RDF model.
 */
public RDFNice( Reader reader )
{
    this();
    loadFromReader( reader );
}

//------------------------------------------------------------------------------
private RDFNice()
{
    try
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
    catch( Exception ex )
    {
        debug().error( "exception occurred: " + ex );
    }
}

/*******************************************************************************
 * Usage: <code>RDFNice &lt;rdf-file&gt; { &lt;predicate&gt; &lt;value&gt; }*</code>
 * <br>
 * The filename of the resulting RDF file is just the name of the source file
 * plus the postfix ".nice.rdf".
 */
static public void main( String[] args )
{
    if( args.length < 1 ) {
        System.out.println( "missing parameter: <rdf-file>" );
        return;
    }
    RDFNice app = new RDFNice( args[0] );
    String sDestFile = args[0] + ".nice.rdf";
    for( int i = 1; i < args.length; i += 2 )
    {
        String sPred = args[i];
        double dValue = Double.parseDouble( args[i+1] );
        app.setPredValue( sPred, dValue );
    }
    app.createNiceXML();
    app.save( sDestFile );
    ////System.out.println( "\n" + app.serializeToString() );
}

/*******************************************************************************
 * Constructs the hierarchical (internal) XML structure for the nice RDF.
 */
public void createNiceXML()
{
    if( m_bCreateNiceCalled )
        return;
    m_bCreateNiceCalled = true;

    try
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
    catch( Exception ex )
    {
        debug().error( "exception occurred: " + ex );
    }
}

/*******************************************************************************
 * Saves the hierarchical XML structure to a file.
 * @param sFilename the filename of the resulting RDF file
 */
public void save( String sFilename )
{
    try
    {
        createNiceXML();
        PrintWriter pw = new PrintWriter( new FileOutputStream( sFilename ) );
        serializeTo( pw );
    }
    catch( Exception ex )
    {
        debug().error( "exception occurred: " + ex );
    }
}

/*******************************************************************************
 * Serializes the hierarchical XML structure to the given writer.
 */
public void serializeTo( Writer w )
{
    try
    {
        createNiceXML();
        w.write( m_xmlDoc.serialize() );
        w.flush();
        w.close();
    }
    catch( Exception ex )
    {
        debug().error( "exception occurred: " + ex );
    }
}

/*******************************************************************************
 * Serializes the hierarchical XML structure to a string, which is returned.
 */
public String serializeToString()
{
    StringWriter sw = new StringWriter();
    serializeTo( sw );
    return sw.toString();
}

/*******************************************************************************
 * Gets the value for a specific predecessor.
 */
public double getPredValue( String sPred )
{
    Double d = (Double)m_mapPred2Value.get( sPred );
    return ( d != null  ?  d.doubleValue()  :  0.0 );
}

/*******************************************************************************
 * Sets the value of a specific predecessor.
 */
public void setPredValue( String sPred, double dValue )
{
    m_mapPred2Value.put( sPred, new Double( dValue ) );
}

//------------------------------------------------------------------------------
private void loadFromFile( String sRDFFile )
{
    try
    {
        m_model = m_rdfFactory.createModel();
        RDF.parse( "file:" + sRDFFile, m_rdfParser, m_model );
        ////System.out.println( "*** " + m_model.size() + " statements read in ***" );
        m_modelRest = m_model.duplicate();
    }
    catch( Exception ex )
    {
        debug().error( "exception occurred: " + ex );
        ex.printStackTrace();
    }
}

//------------------------------------------------------------------------------
private void loadFromReader( Reader reader )
{
    try
    {
        m_model = m_rdfFactory.createModel();
        RDF.parse( "http://dfki.rdf.util.rdfnice/default#load_in_by_reader", reader, m_rdfParser, m_model );
        ////System.out.println( "*** " + m_model.size() + " statements read in ***" );
        m_modelRest = m_model.duplicate();
    }
    catch( Exception ex )
    {
        debug().error( "exception occurred: " + ex );
        ex.printStackTrace();
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
    if( TinyXMLTextNode.containsIllegalChars( sText ) )
        return m_xmlDoc.createCDATA( sText );
    else
        return m_xmlDoc.createTextNode( sText );
}

//------------------------------------------------------------------------------
private void appendInstance( Resource resInstance, TinyXMLElement elAppendHere,
                             Set/*String*/ setProcessedResources )   throws Exception
{
    boolean bInstanceWasStillInList = removeInstanceFromRDFModelRest( resInstance );
    if(     setProcessedResources.contains( resInstance.getURI() ) ||
            !bInstanceWasStillInList )
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
            double dValue = getPredValue( resPred.getURI() );
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
        double dVal = getPredValue( resPred.getURI() );
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
    if( TinyXMLTextNode.containsIllegalChars( sValue ) )
    {
        TinyXMLElement elSlot = createElement( resPred );
        elInst.appendChild( elSlot );
        TinyXMLTextNode txtValue = createTextNode( sValue );
        elSlot.appendChild( txtValue );
    }
    else
    {
        elInst.setAttribute( resPred.getNamespace() + resPred.getLocalName(), sValue );
    }
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
            dInstanceValue -= getPredValue( sPred );  // NEGATIVE!!!
            ////System.out.println( "     " + (-getPredValue( sPred )) + " # " + sPred );
        }
        mTest = m_model.find( resSubject, null, null );
        int nrLinksFromInst = 0;
        for( Enumeration enum = mTest.elements(); enum.hasMoreElements(); )
        {
            Statement s = (Statement)enum.nextElement();
            String sPred = s.predicate().getURI();
            dInstanceValue += getPredValue( sPred );
            ////System.out.println( "     " + getPredValue( sPred ) + " # " + sPred );
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

    // m_modelRest.remove( stBestInstance );
    
    return resBestInstance;
}

//------------------------------------------------------------------------------
private boolean removeInstanceFromRDFModelRest( Resource resInstance )   throws Exception
{
    Model m = m_modelRest.find( resInstance, m_resPredType, null );
    boolean bFoundAndRemovedFromList = false;
    for( Enumeration enum = m.elements(); enum.hasMoreElements(); )
    {
        Statement st = (Statement)enum.nextElement();
        m_modelRest.remove( st );
        bFoundAndRemovedFromList = true;
    }
    return bFoundAndRemovedFromList;
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
private static Debug debug()
{
    return Debug.forModule( "RDFNice" );
}


//------------------------------------------------------------------------------
private RDF.Syntax m_rdfURIs;
private RDFS.URIs m_rdfsURIs;
private RDFFactory m_rdfFactory;
private RDFParser m_rdfParser;
private NodeFactory m_nodeFactory;

private Resource m_resRDFSLiteral;
private Resource m_resRDFSResCls;
private Resource m_resRDFSPredSubClassOf;
private Resource m_resPredType;
private Resource m_resPredResource;
private Resource m_resPredAbout;

private Model m_model;
private Model m_modelRest;

private Map/*String->Double*/ m_mapPred2Value = new HashMap();

private TinyXMLDocument m_xmlDoc;
private TinyXMLElement m_elDoc;

private boolean m_bCreateNiceCalled = false;

//------------------------------------------------------------------------------
} // end of class RDFNice

