package dfki.rdf.util.nice.tinyxmldoc;

import dfki.rdf.util.nice.TinyXMLSerializer;


public abstract class TinyXMLNode
{
//------------------------------------------------------------------------------
protected TinyXMLDocument m_doc;
private TinyXMLNode m_parent;

//------------------------------------------------------------------------------
public TinyXMLNode( TinyXMLDocument doc )
{
    m_doc = doc;
}

//------------------------------------------------------------------------------
public TinyXMLDocument getDocument()
{
    return m_doc;
}

//------------------------------------------------------------------------------
public void setParent( TinyXMLNode parent )
{
    m_parent = parent;
}

//------------------------------------------------------------------------------
public TinyXMLNode getParent()
{
    return m_parent;
}

//------------------------------------------------------------------------------
abstract void serialize( TinyXMLSerializer s );

//------------------------------------------------------------------------------
} // end of class TinyXMLNode

