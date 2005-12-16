package de.dfki.rdf.util.nice.tinyxmldoc;

import java.util.Iterator;
import java.util.TreeMap;

import de.dfki.rdf.util.nice.TinyXMLSerializer;
import de.dfki.rdf.util.nice.TinyXMLSerializerImpl;


public class TinyXMLDocument
{
//------------------------------------------------------------------------------
private TinyXMLElement m_docElement;
private TreeMap/*String->String*/ m_mapNamespace2Prefix = new TreeMap();

public String RDF_NAMESPACE     = null;
public String RDFS_NAMESPACE    = null;

//------------------------------------------------------------------------------
public TinyXMLDocument()
{
    this( null, null );
}

public TinyXMLDocument( String sRdfNamespace, String sRdfsNamespace )
{
    try {
        RDF_NAMESPACE     = sRdfNamespace;
        RDFS_NAMESPACE    = sRdfsNamespace;

        if( RDF_NAMESPACE == null )     RDF_NAMESPACE   = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
        if( RDFS_NAMESPACE == null )    RDFS_NAMESPACE  = "http://www.w3.org/TR/1999/PR-rdf-schema-19990303#";

        declareNamespacePrefix( RDF_NAMESPACE, "rdf" );
        declareNamespacePrefix( RDFS_NAMESPACE, "rdfs" );
    }
    catch( Exception ex )
    {
        throw new Error( "exception occurred: " + ex );
    }
}

//------------------------------------------------------------------------------
public void setDocumentElement( TinyXMLElement docElement )
{
    m_docElement = docElement;
}

//------------------------------------------------------------------------------
public TinyXMLElement getDocumentElement()
{
    return m_docElement;
}

//------------------------------------------------------------------------------
public TinyXMLElement createElement( String sTagName )
{
    TinyXMLElement el = new TinyXMLElement( this, sTagName );
    return el;
}

//------------------------------------------------------------------------------
public TinyXMLTextNode createTextNode( String sText )
{
    TinyXMLTextNode txt = new TinyXMLTextNode( this, sText );
    return txt;
}

//------------------------------------------------------------------------------
public TinyXMLCDATA createCDATA( String sText )
{
    TinyXMLCDATA txt = new TinyXMLCDATA( this, sText );
    return txt;
}

//------------------------------------------------------------------------------
public String getNamespace( String sURI )
{
    int pos = sURI.indexOf( '#' );
    if( pos >= 0 )
        return sURI.substring( 0, pos+1 );  // namespace includes '#', too!
    else
        return sURI;  // assume: everything is namespace
        ////old: throw new Error( "implementation error: no namespace for uri '" + sURI + "'" );
}

//------------------------------------------------------------------------------
public String getLocalName( String sURI )
{
    int pos = sURI.indexOf( '#' );
    if( pos >= 0 )
        return sURI.substring( pos + 1 );
    else
        return sURI;  // no namespace
}

//------------------------------------------------------------------------------
public void declareNamespacePrefix( String sNamespace, String sPrefix )
{
    m_mapNamespace2Prefix.put( sNamespace, sPrefix );
}

//------------------------------------------------------------------------------
String getPrefix( String sNamespace )
{
    String sPrefix = (String)m_mapNamespace2Prefix.get( sNamespace );
    if( sPrefix == null )
    {
        sPrefix = calcPrefix( sNamespace );
        if( sPrefix != null )
            m_mapNamespace2Prefix.put( sNamespace, sPrefix );
    }
    return sPrefix;
}

//------------------------------------------------------------------------------
private String calcPrefix( String sNamespace )
{
    int pos = sNamespace.lastIndexOf( '/' );
    if( pos < 0  ||  sNamespace.charAt( sNamespace.length()-1 ) != '#' )
        return null;  //old: return "ns_" + Integer.toHexString( sNamespace.hashCode() );
    return sNamespace.substring( pos+1, sNamespace.length()-1 );
}

//------------------------------------------------------------------------------
String uri2qname( String sURI )
{
    if( sURI == null ) return null;
    String sPrefix = getPrefix( getNamespace( sURI ) );
    String sLocalName = getLocalName( sURI );
    if( sPrefix != null )
        return sPrefix + ":" + sLocalName;
    else
        return sLocalName;
}

//------------------------------------------------------------------------------
String uri2entityref( String sURI )
{
    if( sURI == null ) return null;
    String sPrefix = getPrefix( getNamespace( sURI ) );
    String sLocalName = getLocalName( sURI );
    if( sPrefix != null )
        return "&" + sPrefix + ";" + sLocalName;
    else
        return sLocalName;
}

//------------------------------------------------------------------------------
public String serialize()
{
    try
    {
        TinyXMLSerializer s = new TinyXMLSerializerImpl();
        TinyXMLElement el = getDocumentElement();
        el.serialize( s );
        declareNamespaces( s );
        s.flush();
        return s.toString();
    }
    catch( Error e )
    {
        e.printStackTrace();
        throw e;
    }
}

//------------------------------------------------------------------------------
private void declareNamespaces( TinyXMLSerializer s )
{
    for( Iterator it = m_mapNamespace2Prefix.keySet().iterator(); it.hasNext(); )
    {
        String sNamespace = (String)it.next();
        String sPrefix = (String)m_mapNamespace2Prefix.get( sNamespace );
        s.declareNamespacePrefix( sPrefix, sNamespace );
    }
}

//------------------------------------------------------------------------------
} // end of class TinyXMLDocument

