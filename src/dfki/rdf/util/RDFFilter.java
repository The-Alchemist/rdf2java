package dfki.rdf.util;

import java.util.Collection;


/**
 * Implementation of this interface filters RDF data.
 * @see dfki.rdf.util.filter#IdentityRDFFilter
 */
public interface RDFFilter
{
    /**
     * Filters the given RDF data, i.e. a collection of <code>RDFResource</code>s.
     * <b>Note:</b> Modifying the returned collection may modify the original data!
     */
    public Collection/*RDFResource*/ filter( Collection/*RDFResource*/ objects );

} // end of interface RDFFilter

