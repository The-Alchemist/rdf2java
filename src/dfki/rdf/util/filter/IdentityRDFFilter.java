package dfki.rdf.util.filter;

import java.util.Collection;

import dfki.rdf.util.RDFFilter;


/**
 * This RDF filter just filters nothing.
 * Everything in the given RDF data is passed through;
 * nothing is added, nothing is removed.
 */
public class IdentityRDFFilter   implements RDFFilter
{

    public IdentityRDFFilter()
    {
    }

    /** <b>Attention:</b> This method just returns the given collection
     *  of <code>RDFResource</code>s, so beware of every modification!
     */
    public Collection/*RDFResource*/ filter( Collection/*RDFResource*/ objects )
    {
        return objects;
    }

} // end of class IdentityRDFFilter

