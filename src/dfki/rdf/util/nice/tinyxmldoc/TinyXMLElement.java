package dfki.rdf.util.nice.tinyxmldoc;

import java.util.*;

import dfki.rdf.util.nice.TinyXMLSerializer;

import org.w3c.rdf.model.Resource;
import dfki.util.rdf.RDF;


public class TinyXMLElement   extends TinyXMLNode
{
//------------------------------------------------------------------------------
private String m_sTagName;
private Vector/*TinyXMLNode*/ m_vChildren = new Vector();
private TreeMap/*String->Object*/ m_mapAttr2Value = new TreeMap();

//------------------------------------------------------------------------------
public TinyXMLElement( TinyXMLDocument doc, String sTagName )
{
    super( doc );
    setTagName( sTagName );
}

//------------------------------------------------------------------------------
public void setTagName( String sTagName )
{
    m_sTagName = sTagName;
}

//------------------------------------------------------------------------------
public String getTagName()
{
    return m_sTagName;
}

//------------------------------------------------------------------------------
public void appendChild( TinyXMLNode child )
{
    m_vChildren.add( child );
    child.setParent( this );
}

//------------------------------------------------------------------------------
public Collection/*TinyXMLNode*/ getChildren()
{
    return m_vChildren;
}

//------------------------------------------------------------------------------
public void setAttribute( String attrName, Object attrValue )
{
    m_mapAttr2Value.put( attrName, attrValue );
}

//------------------------------------------------------------------------------
public Object getAttributeValue( String attrName )
{
    return m_mapAttr2Value.get( attrName );  // may be null!
}

//------------------------------------------------------------------------------
public Collection/*String*/ getAttributes()
{
    return m_mapAttr2Value.keySet();
}

//------------------------------------------------------------------------------
public void serialize( TinyXMLSerializer s )
{
    try
    {
        s.startElement( getDocument().uri2qname( getTagName() ) );

        final String rdf_about = RDF.syntax().namespace() + "about";
        Object value = getAttributeValue( rdf_about );
        serializeAttr( s, rdf_about, value );
        for( Iterator itAttr = getAttributes().iterator(); itAttr.hasNext(); )
        {
            String sAttr = (String)itAttr.next();
            if( sAttr.equals( rdf_about ) )
                continue;  // we have that already
            value = getAttributeValue( sAttr );
            serializeAttr( s, sAttr, value );
        }

        for( Iterator itChildren = getChildren().iterator(); itChildren.hasNext(); )
        {
            TinyXMLNode child = (TinyXMLNode)itChildren.next();
            child.serialize( s );
        }

        s.endElement( getDocument().uri2qname( getTagName() ) );
    }
    catch( Exception ex )
    {
        throw new Error( "exception occurred: " + ex );
    }
}

//------------------------------------------------------------------------------
public void serializeAttr( TinyXMLSerializer s, String sAttr, Object value )   throws Exception
{
    if( value == null )
        return;

    String sValue;
    if( value instanceof String )
        sValue = (String)value;
    else
    if( value instanceof Resource )
        sValue = getDocument().uri2entityref( ((Resource)value).getURI() );
    else
        throw new Error( "implementation error" );

    if( sValue != null )
        s.putAttribute( getDocument().uri2qname( sAttr ), sValue );
}

//------------------------------------------------------------------------------
} // end of class TinyXMLElement

