package de.dfki.rdf.util.nice;

/**
 * This XML serializer is intended to serialize RDF data (only write, no read),
 * it can be used write arbitrary XML, however.
 * <p>
 * The importance for this tiny XML writer is (1) hierarchy and (2) <i>full</i>
 * namespace support.
 * For the namespace support this means: all names (elements, attributes, etc.)
 * are qualified names (qnames) having the form <code>prefix:localName</code>.
 * Thereas the <code>prefix</code> can be ommitted. If it isn't, it is a
 * 'shortcut' for a namespace.
 */
public interface TinyXMLSerializer
{
//------------------------------------------------------------------------------
/**
 * Declares a prefix for a namespace.
 */
public void declareNamespacePrefix( String prefix, String namespace );

//------------------------------------------------------------------------------
/**
 * Starts an element node. This description has to be 'closed' by
 * a respective call to {@link #endElement}.<br>
 * This method has to be called, too, to create the 'document element'.
 */
public void startElement( String qname );

/**
 * Ends an element node.<br>
 * The given parameter is just for testing purpose. The nesting has to be
 * correct.
 */
public void endElement( String qname );

/**
 * Puts an attribute (the value is always a string).
 */
public void putAttribute( String qname, String value );

/**
 * Puts a new element containing <i>one</i> attribute with the given value
 * (that value is always a string).
 */
public void putAttributeElement( String qnameElement, String qnameAttr, String value );

/**
 * Puts a text node (child).
 */
public void putText( String text );

/**
 * Puts a CDATA node (child).
 */
public void putCDATA( String text );

/**
 * Puts a new element containing the given text --
 * so this adds two children really.
 */
public void putTextElement( String qnameElement, String text );

//------------------------------------------------------------------------------
/**
 * Flushes all data -- this is needed as a final step.
 */
public void flush();

//------------------------------------------------------------------------------
} // end of interface TinyXMLSerializer

