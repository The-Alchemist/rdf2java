package dfki.rdf.util.nice.tinyxmldoc;

import java.util.*;

import dfki.rdf.util.nice.TinyXMLSerializer;
import dfki.rdf.util.nice.TinyXMLSerializerImpl;

import dfki.util.rdf.RDF;
import dfki.util.rdfs.RDFS;


public class TinyXMLDocument
{
//------------------------------------------------------------------------------
private TinyXMLElement m_docElement;
private TreeMap/*String->String*/ m_mapNamespace2Prefix = new TreeMap();


//------------------------------------------------------------------------------
public TinyXMLDocument()
{
    try {
        declareNamespacePrefix( RDF.syntax().namespace(), "rdf" );
        declareNamespacePrefix( RDFS.getURIs().namespace(), "rdfs" );
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
public String getNamespace( String sURI )
{
    int pos = sURI.indexOf( '#' );
    if( pos >= 0 )
        return sURI.substring( 0, pos+1 );  // namespace includes '#', too!
    else
        throw new Error( "implementation error: no namespace for uri '" + sURI + "'" );
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
        m_mapNamespace2Prefix.put( sNamespace, sPrefix );
    }
    return sPrefix;
}

//------------------------------------------------------------------------------
private String calcPrefix( String sNamespace )
{
    int pos = sNamespace.lastIndexOf( '/' );
    if( pos < 0  ||  sNamespace.charAt( sNamespace.length()-1 ) != '#' )
        return "ns_" + Integer.toHexString( sNamespace.hashCode() );
    return sNamespace.substring( pos+1, sNamespace.length()-1 );
}

//------------------------------------------------------------------------------
String uri2qname( String sURI )
{
    String sPrefix = getPrefix( getNamespace( sURI ) );
    String sLocalName = getLocalName( sURI );
    return sPrefix + ":" + sLocalName;
}

//------------------------------------------------------------------------------
String uri2entityref( String sURI )
{
    String sPrefix = getPrefix( getNamespace( sURI ) );
    String sLocalName = getLocalName( sURI );
    return "&" + sPrefix + ";" + sLocalName;
}

//------------------------------------------------------------------------------
public String serialize()
{
     TinyXMLSerializer s = new TinyXMLSerializerImpl();
     TinyXMLElement el = getDocumentElement();
     el.serialize( s );
     declareNamespaces( s );
     s.flush();
     return s.toString();
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

