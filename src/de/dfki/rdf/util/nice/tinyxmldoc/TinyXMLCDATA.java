package de.dfki.rdf.util.nice.tinyxmldoc;

import de.dfki.rdf.util.nice.TinyXMLSerializer;


public class TinyXMLCDATA   extends TinyXMLTextNode
{
//------------------------------------------------------------------------------
public TinyXMLCDATA( TinyXMLDocument doc )
{
    super( doc );
}

//------------------------------------------------------------------------------
public TinyXMLCDATA( TinyXMLDocument doc, String sText )
{
    super( doc, sText );
}

//------------------------------------------------------------------------------
public void serialize( TinyXMLSerializer s )
{
    //FIXME?
    s.putCDATA( getText() );
}

//------------------------------------------------------------------------------
} // end of class TinyXMLTextNode

