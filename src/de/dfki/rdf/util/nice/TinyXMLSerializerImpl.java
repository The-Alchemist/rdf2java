package de.dfki.rdf.util.nice;

import java.util.Iterator;
import java.util.TreeMap;


public class TinyXMLSerializerImpl implements TinyXMLSerializer
{
//------------------------------------------------------------------------------
private TreeMap m_mapPrefix2Namespace;
private StringBuffer m_sbDocumentElement;
private boolean m_bWaitingForAttributes;
private boolean m_bFirstElement;
private String m_sFirstElementQName;
private int m_iIndent;
private final static int INDENT_SIZE = 4;
private boolean m_bFlushed = false;


//------------------------------------------------------------------------------
public TinyXMLSerializerImpl()
{
    initialize();
}

//------------------------------------------------------------------------------
private void initialize()
{
    m_mapPrefix2Namespace = new TreeMap();
    m_sbDocumentElement = new StringBuffer();
    m_bWaitingForAttributes = false;
    m_bFirstElement = true;  // document element
    m_iIndent = 0;
}

//------------------------------------------------------------------------------
//                      TinyXMLSerializer implementation
//------------------------------------------------------------------------------
public void declareNamespacePrefix( String prefix, String namespace )
{
    m_mapPrefix2Namespace.put( prefix, namespace );
}

//------------------------------------------------------------------------------
public void startElement( String qname )
{
    if( m_bFirstElement )
    {
        m_bFirstElement = false;
        m_sFirstElementQName = qname;
        increaseIndent();
        return;
    }

    if( m_bWaitingForAttributes )
        m_sbDocumentElement.append( ">\n" );
    indent();
    m_sbDocumentElement.append( "<" + qname );
    m_bWaitingForAttributes = true;
    increaseIndent();
}

//------------------------------------------------------------------------------
public void endElement( String qname )
{
    decreaseIndent();
    if( m_bWaitingForAttributes )
        m_sbDocumentElement.append( "/>\n" );
    else
    {
        indent();
        m_sbDocumentElement.append( "</" + qname + ">\n" );
    }
    m_bWaitingForAttributes = false;
}

//------------------------------------------------------------------------------
public void putAttribute( String qname, String value )
{
    if ( !m_bWaitingForAttributes )
        throw new Error( "implementation failure" );
    if( qname.equals( "rdf:about" )  ||  qname.equals( "rdf:resource" ) )
        m_sbDocumentElement.append( " " );
    else
    {
        m_sbDocumentElement.append( "\n" );
        indent();
    }
    m_sbDocumentElement.append( " " + qname + "=\"" + value + "\"" );
}

//------------------------------------------------------------------------------
public void putAttributeElement( String qnameElement, String qnameAttr, String value )
{
    if( m_bWaitingForAttributes )
        m_sbDocumentElement.append( ">\n" );
    m_bWaitingForAttributes = false;
    indent();
    m_sbDocumentElement.append( "<" + qnameElement + " " + qnameAttr + "=\"" + value + "\"" + "/>\n" );
}

//------------------------------------------------------------------------------
public void putText( String text )
{
    if( m_bWaitingForAttributes )
        m_sbDocumentElement.append( ">\n" );
    m_bWaitingForAttributes = false;
    indent();
    m_sbDocumentElement.append( text + "\n" );
}

//------------------------------------------------------------------------------
public void putCDATA( String text )
{
    if( m_bWaitingForAttributes )
        m_sbDocumentElement.append( ">" );
    m_bWaitingForAttributes = false;
    m_sbDocumentElement.append( "<![CDATA[" + text + "]]>\n" );
}

//------------------------------------------------------------------------------
public void putTextElement( String qnameElement, String text )
{
    if( m_bWaitingForAttributes )
        m_sbDocumentElement.append( ">\n" );
    m_bWaitingForAttributes = false;
    indent();
    m_sbDocumentElement.append( "<" + qnameElement + ">" + text + "</" + qnameElement + ">\n" );
}

//------------------------------------------------------------------------------
public void flush()
{
    if( m_bFlushed )
        return;

    String sTemp = m_sbDocumentElement.toString();
    m_sbDocumentElement = new StringBuffer();
    m_iIndent = 0;

    m_sbDocumentElement.append( "<?xml version='1.0' encoding='ISO-8859-1'?>\n" );
    m_sbDocumentElement.append( "<!DOCTYPE " + m_sFirstElementQName + " [\n" );
    increaseIndent();
    for( Iterator it = m_mapPrefix2Namespace.keySet().iterator(); it.hasNext(); )
    {
        String prefix = (String)it.next();
        String namespace = (String)m_mapPrefix2Namespace.get( prefix );
        indent();
        m_sbDocumentElement.append( "<!ENTITY " + prefix + " '" + namespace + "'>\n" );
    }
    decreaseIndent();
    m_sbDocumentElement.append( "\n]>\n" );

    m_sbDocumentElement.append( "<" + m_sFirstElementQName + " " );
    increaseIndent();
    for( Iterator it = m_mapPrefix2Namespace.keySet().iterator(); it.hasNext(); )
    {
        String prefix = (String)it.next();
        m_sbDocumentElement.append( "\n" );
        indent();
        m_sbDocumentElement.append( "xmlns:" + prefix + "=\"&" + prefix + ";\"" );
    }
    decreaseIndent();
    m_sbDocumentElement.append( ">\n\n" );

    increaseIndent();
    m_sbDocumentElement.append( sTemp );
}


//------------------------------------------------------------------------------
//
//------------------------------------------------------------------------------
public String toString()
{
    return m_sbDocumentElement.toString();
}


//------------------------------------------------------------------------------
//                      indentation
//------------------------------------------------------------------------------
private void increaseIndent()
{
    m_iIndent += INDENT_SIZE;
}

//------------------------------------------------------------------------------
private void decreaseIndent()
{
    m_iIndent -= INDENT_SIZE;
    if( m_iIndent < 0 ) m_iIndent = 0;
}

//------------------------------------------------------------------------------
private void indent()
{
    StringBuffer sb = new StringBuffer();
    for( int i = 0; i < m_iIndent; i++ )
        sb.append( ' ' );
    m_sbDocumentElement.append( sb.toString() );
}


//------------------------------------------------------------------------------
} // end of class TinyXMLSerializerImpl

