package de.dfki.rdf.util;


public class ToStringController
{
    /**
     * the higher the property the earlier this property is printed.
     * you can distinguish properties outgoing from different sources.
     */
    public int propertyImportance( RDFResource source, String prop )
    {
        return 0;
    }    
    
    /**
     * returns true if that property shall be hidden, false otherwise.
     * you can distinguish properties outgoing from different sources.
     */
    public boolean hideProperty( RDFResource source, String prop )
    {
        return false;
    }

    /**
     * returns true if that property shall be expanded (recursion), false otherwise.
     * you can distinguish properties outgoing from different sources and incoming
     * to specific destinations.
     */
    public boolean expandProperty( RDFResource source, String prop, RDFResource dest )
    {
        return true;
    }
    
    //-------------------------------------------------------------------------
    
    /**
     * typical ToStringController: does not allow any recursion, and hence prints out
     * only the properties of the start resource.
     */
    public final static ToStringController NONRECURSIVE_TOSTRING_CONTROLLER = new ToStringController() {
        public boolean expandProperty( RDFResource source, String prop, RDFResource dest )
        {
            return false;
        }    
    };

    /**
     * typical ToStringController: follows every property and resource 
     * reachable from the start resource.
     */
    public final static ToStringController RECURSIVE_TOSTRING_CONTROLLER = new ToStringController() {
        public boolean expandProperty( RDFResource source, String prop, RDFResource dest )
        {
            return true;
        }    
    };
    
} // end of class ToStringController

