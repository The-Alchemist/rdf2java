package dfki.rdf.util.nice.tinyxmldoc;

import dfki.rdf.util.nice.TinyXMLSerializer;


public class TinyXMLTextNode   extends TinyXMLNode
{
//------------------------------------------------------------------------------
private String m_sText;

//------------------------------------------------------------------------------
public TinyXMLTextNode( TinyXMLDocument doc )
{
    super( doc );
}

//------------------------------------------------------------------------------
public TinyXMLTextNode( TinyXMLDocument doc, String sText )
{
    super( doc );
    setText( sText );
}

//------------------------------------------------------------------------------
public void setText( String sText )
{
    m_sText = sText;
}

//------------------------------------------------------------------------------
public String getText()
{
    return m_sText;
}

//------------------------------------------------------------------------------
public void serialize( TinyXMLSerializer s )
{
    //FIXME?
    s.putText( getText() );
}

//------------------------------------------------------------------------------
} // end of class TinyXMLTextNode

