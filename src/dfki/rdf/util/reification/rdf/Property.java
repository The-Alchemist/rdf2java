package dfki.rdf.util.reification.rdf;

// RDFS2Class: imports
import java.util.*;
// RDFS2Class: end of imports


/** RDFS2Class: class Property
  * <p>
  */
public  class Property
    extends dfki.rdf.util.THING
{
    //------------------------------------------------------------------------------
    /** RDFS2Class: sub class information **/
    public final static Class[] KNOWN_SUBCLASSES = {};

    // RDFS2Class: end of sub class information

    //------------------------------------------------------------------------------
    /** RDFS2Class: default constructor **/
    public Property()
    {
        super();
        initPropertyStore();
    }
    // RDFS2Class: end of default constructor

    //------------------------------------------------------------------------------
    /** RDFS2Class: PropertyStore-stuff **/
    private void initPropertyStore()
    {
        dfki.rdf.util.PropertyStore ps = getPropertyStore();
    }
    // RDFS2Class: end of PropertyStore-stuff

}
// RDFS2Class: end of class Property
// EOF

