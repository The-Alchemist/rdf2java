package de.dfki.rdf.util.nice.tinyxmldoc;

import de.dfki.rdf.util.nice.TinyXMLSerializer;


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
public static boolean containsIllegalChars( String sText )
{
    char[] acText = sText.toCharArray();
    for( int i = 0; i < acText.length; i++ )
    {
        char ch = acText[i];
        if(     ch != ' '
                &&  !( ch >= '0'  &&  ch <= '9' )
                &&  !( ch >= 'A'  &&  ch <= 'Z' )
                &&  !( ch >= 'a'  &&  ch <= 'z' ) )
            return true;  // bad characters found
    }
    return false;  // seems to be ok
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

