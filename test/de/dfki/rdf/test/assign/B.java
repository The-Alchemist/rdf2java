package de.dfki.rdf.test.assign;

// RDFS2Class: imports
import java.util.*;
import java.io.Serializable;
// RDFS2Class: end of imports


/** RDFS2Class: class B
  * <p>
  */
public  class B
    extends de.dfki.rdf.util.THING
    implements Serializable
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: toString()-stuff **/
    public String toString()
    {
        return toStringPacked();
    }
    // RDFS2Class: end of toString()-stuff

    //------------------------------------------------------------------------------
    /** RDFS2Class: sub class information **/
    public final static Class[] KNOWN_SUBCLASSES = {};

    // RDFS2Class: end of sub class information

    //------------------------------------------------------------------------------
    /** RDFS2Class: default constructor **/
    public B()
    {
        super();
        putRDFSClass( new de.dfki.rdf.util.RDFResource( "http://dfki.rdf.test/assign#", "B" ) );
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        de.dfki.rdf.util.PropertyStore ps = getPropertyStore();
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class B
// EOF

