package de.dfki.rdf.util.reification.rdf;

// RDFS2Class: imports
import java.util.Iterator;
// RDFS2Class: end of imports


/** RDFS2Class: class Statement
  * <p>
  */
public  class Statement
    extends de.dfki.rdf.util.THING
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: slot subject **/
    protected de.dfki.rdf.util.PropertyInfo m_subject = de.dfki.rdf.util.PropertyInfo.createInstanceProperty( "subject", new Class[]{de.dfki.rdf.util.THING.class}, true );

    /** RDFS2Class: putter for slot subject **/
    public void putSubject (de.dfki.rdf.util.THING p_subject)
    {
        m_subject.putValue(p_subject);
    }
    public void putSubject (de.dfki.rdf.util.RDFResource p_subject)
    {
        m_subject.putValue(p_subject);
    }
    public void putSubject (java.util.Collection p_subject)
    {
        m_subject.setValues(p_subject);
    }
    public void clearSubject ()
    {
        m_subject.clearValue();
    }
    // RDFS2Class: end of putter for slot subject

    /** RDFS2Class: getter for slot subject **/
    public java.util.Collection getSubject ()
    {
        return (java.util.Collection)m_subject.getValue();
    }
    // RDFS2Class: end of getter for slot subject

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot object **/
    protected de.dfki.rdf.util.PropertyInfo m_object = de.dfki.rdf.util.PropertyInfo.createInstanceProperty( "object", new Class[]{String.class, de.dfki.rdf.util.THING.class}, true );

    /** RDFS2Class: putter for slot object **/
    public void putObject (Object p_object)
    {
        if( p_object == null || ( !(p_object instanceof de.dfki.rdf.util.RDFResource) && !(p_object instanceof String) && !(p_object instanceof de.dfki.rdf.util.THING) ) )
            throw new Error("not an allowed class");
        m_object.putValue(p_object);
    }
    public void putObject (de.dfki.rdf.util.RDFResource p_object)
    {
        m_object.putValue(p_object);
    }
    public void putObject (java.util.Collection p_object)
    {
        m_object.setValues(p_object);
    }
    public void clearObject ()
    {
        m_object.clearValue();
    }
    // RDFS2Class: end of putter for slot object

    /** RDFS2Class: getter for slot object **/
    public java.util.Collection getObject ()
    {
        return (java.util.Collection)m_object.getValue();
    }
    // RDFS2Class: end of getter for slot object

    //------------------------------------------------------------------------------
    /** RDFS2Class: slot predicate **/
    protected de.dfki.rdf.util.PropertyInfo m_predicate = de.dfki.rdf.util.PropertyInfo.createInstanceProperty( "predicate", new Class[]{String.class, Property.class}, true );

    /** RDFS2Class: putter for slot predicate **/
    public void putPredicate (Object p_predicate)
    {
        if( p_predicate == null || ( !(p_predicate instanceof de.dfki.rdf.util.RDFResource) && !(p_predicate instanceof String) && !(p_predicate instanceof Property) ) )
            throw new Error("not an allowed class");
        m_predicate.putValue(p_predicate);
    }
    public void putPredicate (de.dfki.rdf.util.RDFResource p_predicate)
    {
        m_predicate.putValue(p_predicate);
    }
    public void putPredicate (java.util.Collection p_predicate)
    {
        m_predicate.setValues(p_predicate);
    }
    public void clearPredicate ()
    {
        m_predicate.clearValue();
    }
    // RDFS2Class: end of putter for slot predicate

    /** RDFS2Class: getter for slot predicate **/
    public java.util.Collection getPredicate ()
    {
        return (java.util.Collection)m_predicate.getValue();
    }
    // RDFS2Class: end of getter for slot predicate

    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public void toString (StringBuffer sb, String sIndent)
    {
        super.toString(sb, sIndent);
        if (!m_subject.isEmpty()) {
            sb.append(sIndent+"-> subject:\n");
            for (Iterator it_subject = ((java.util.Collection)m_subject.getValue()).iterator(); it_subject.hasNext(); ) {
                sb.append( sIndent+"       " + ((de.dfki.rdf.util.RDFResource)it_subject.next()).toStringShort() + "\n" );
                // sb.append( ((dfki.rdf.util.RDFResource)it_subject.next()).toString(sIndent+"       ") );
            }
        }
        if (!m_object.isEmpty()) {
            sb.append(sIndent+"-> object:\n");
            for (Iterator it_object = ((java.util.Collection)m_object.getValue()).iterator(); it_object.hasNext(); ) {
                sb.append( sIndent+"       " + it_object.next() + "\n" );
            }
        }
        if (!m_predicate.isEmpty()) {
            sb.append(sIndent+"-> predicate:\n");
            for (Iterator it_predicate = ((java.util.Collection)m_predicate.getValue()).iterator(); it_predicate.hasNext(); ) {
                sb.append( sIndent+"       " + it_predicate.next() + "\n" );
            }
        }
    }
    // RDFS2Class: end of toString()-stuff

    //------------------------------------------------------------------------------
    /** RDFS2Class: sub class information **/
    public final static Class[] KNOWN_SUBCLASSES = {};

    // RDFS2Class: end of sub class information

    //------------------------------------------------------------------------------
    /** RDFS2Class: default constructor **/
    public Statement()
    {
        super();
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        de.dfki.rdf.util.PropertyStore ps = getPropertyStore();
        ps.addProperty( m_subject );
        ps.addProperty( m_object );
        ps.addProperty( m_predicate );
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class Statement
// EOF

