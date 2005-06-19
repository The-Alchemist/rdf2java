package de.dfki.km.jena2java.skos.vocabulary;

// RDFS2Class: imports
import java.io.Serializable;
import java.util.Collection;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Property;
import de.dfki.km.jena2java.JenaResourceWrapper;
import de.dfki.km.jena2java.ObjectTracker;
// RDFS2Class: end of imports


/** RDFS2Class: class Concept
  * <p>
  */
public class  Concept
    extends JenaResourceWrapper
    implements Serializable
{
    // RDFS2Class: begin constructors
    /**
     * Creates a new anonymous instance of the RDFS class <code>Concept</code> along with its wrapper and
     * register it with an {@link ObjectTracker}.
     * To get wrapper instances for existing Jena resources, use <code>ObjectTracker.getInstance</code>.
     * @param tracker The tracker the new instance shall be registered with.
     * @param model The Jena model to create the new instance in.
     */
    public Concept(ObjectTracker tracker, Model model )
    {
        super(tracker, model, de.dfki.km.jena2java.skos.vocabulary.SKOS.Concept );
    }

    /**
     * Creates a new instance of the RDFS class <code>Concept</code> along with its wrapper and
     * register it with an {@link ObjectTracker}.
     * To get wrapper instances for existing Jena resources, use <code>ObjectTracker.getInstance</code>.
     * @param tracker The tracker the new instance shall be registered with.
     * @param model The Jena model to create the new instance in.
     * @param uri The new instance's URI.
     */
    public Concept(ObjectTracker tracker, Model model, String uri)
    {
        super(tracker, model, de.dfki.km.jena2java.skos.vocabulary.SKOS.Concept, uri);
    }

    /**
     * Using this method is usually a mistake.
     * Creates a new wrapper for an existing Jena RDF instance.
     * Registration with an <code>ObjectTracker</code> must be done elsewhere.
     * Typically this constructor should be called only by the <code>ObjectTracker</code>.
     * @param res The Jena RDF instance to be wrapped.
     */
    public Concept( Resource res )
    {
        super(res);
    }
    protected Concept(ObjectTracker tracker, Model model, Resource resource)
    {
        super(tracker, model, resource);
    }

    protected Concept(ObjectTracker tracker, Model model, Resource resource, String uri)
    {
        super(tracker, model, resource, uri);
    }

    // RDFS2Class: end constructors

    //------------------------------------------------------------------------------
    // RDFS2Class: begin property http://www.w3.org/2004/02/skos/core#inScheme
    public Collection getInScheme()
    {
        return getPropertyObjects( de.dfki.km.jena2java.skos.vocabulary.SKOS.inScheme );
    }
    public void addInScheme( de.dfki.km.jena2java.skos.vocabulary.ConceptScheme _inscheme )
    {
        m_res.addProperty( de.dfki.km.jena2java.skos.vocabulary.SKOS.inScheme, _inscheme );
    }
    public void removeInScheme( de.dfki.km.jena2java.skos.vocabulary.ConceptScheme _inscheme )
    {
        m_res.getModel().remove( m_res.getModel().listStatements( m_res, de.dfki.km.jena2java.skos.vocabulary.SKOS.inScheme, _inscheme ) );
    }
    public void clearInScheme()
    {
        m_res.removeAll( de.dfki.km.jena2java.skos.vocabulary.SKOS.inScheme );
    }
    // RDFS2Class: end property http://www.w3.org/2004/02/skos/core#inScheme

    //------------------------------------------------------------------------------
    // RDFS2Class: begin property http://www.w3.org/2004/02/skos/core#related
    public Collection getRelated()
    {
        return getPropertyObjects( de.dfki.km.jena2java.skos.vocabulary.SKOS.related );
    }
    public void addRelated( Resource _related )
    {
        m_res.addProperty( de.dfki.km.jena2java.skos.vocabulary.SKOS.related, _related );
    }
    public void removeRelated( Resource _related )
    {
        m_res.getModel().remove( m_res.getModel().listStatements( m_res, de.dfki.km.jena2java.skos.vocabulary.SKOS.related, _related ) );
    }
    public void clearRelated()
    {
        m_res.removeAll( de.dfki.km.jena2java.skos.vocabulary.SKOS.related );
    }
    // RDFS2Class: end property http://www.w3.org/2004/02/skos/core#related

    //------------------------------------------------------------------------------
    // RDFS2Class: begin property http://www.w3.org/2004/02/skos/core#narrower
    public Collection getNarrower()
    {
        return getPropertyObjects( de.dfki.km.jena2java.skos.vocabulary.SKOS.narrower );
    }
    public void addNarrower( Resource _narrower )
    {
        m_res.addProperty( de.dfki.km.jena2java.skos.vocabulary.SKOS.narrower, _narrower );
    }
    public void removeNarrower( Resource _narrower )
    {
        m_res.getModel().remove( m_res.getModel().listStatements( m_res, de.dfki.km.jena2java.skos.vocabulary.SKOS.narrower, _narrower ) );
    }
    public void clearNarrower()
    {
        m_res.removeAll( de.dfki.km.jena2java.skos.vocabulary.SKOS.narrower );
    }
    // RDFS2Class: end property http://www.w3.org/2004/02/skos/core#narrower

    //------------------------------------------------------------------------------
    // RDFS2Class: begin property http://www.w3.org/2004/02/skos/core#isSubjectOf
    public Collection getIsSubjectOf()
    {
        return getPropertyObjects( de.dfki.km.jena2java.skos.vocabulary.SKOS.isSubjectOf );
    }
    public void addIsSubjectOf( Resource _issubjectof )
    {
        m_res.addProperty( de.dfki.km.jena2java.skos.vocabulary.SKOS.isSubjectOf, _issubjectof );
    }
    public void removeIsSubjectOf( Resource _issubjectof )
    {
        m_res.getModel().remove( m_res.getModel().listStatements( m_res, de.dfki.km.jena2java.skos.vocabulary.SKOS.isSubjectOf, _issubjectof ) );
    }
    public void clearIsSubjectOf()
    {
        m_res.removeAll( de.dfki.km.jena2java.skos.vocabulary.SKOS.isSubjectOf );
    }
    // RDFS2Class: end property http://www.w3.org/2004/02/skos/core#isSubjectOf

    //------------------------------------------------------------------------------
    // RDFS2Class: begin property http://www.w3.org/2004/02/skos/core#broader
    public Collection getBroader()
    {
        return getPropertyObjects( de.dfki.km.jena2java.skos.vocabulary.SKOS.broader );
    }
    public void addBroader( Resource _broader )
    {
        m_res.addProperty( de.dfki.km.jena2java.skos.vocabulary.SKOS.broader, _broader );
    }
    public void removeBroader( Resource _broader )
    {
        m_res.getModel().remove( m_res.getModel().listStatements( m_res, de.dfki.km.jena2java.skos.vocabulary.SKOS.broader, _broader ) );
    }
    public void clearBroader()
    {
        m_res.removeAll( de.dfki.km.jena2java.skos.vocabulary.SKOS.broader );
    }
    // RDFS2Class: end property http://www.w3.org/2004/02/skos/core#broader

    //------------------------------------------------------------------------------
    // RDFS2Class: begin property http://www.w3.org/2004/02/skos/core#semanticRelation
    public Collection getSemanticRelation()
    {
        return getPropertyObjects( de.dfki.km.jena2java.skos.vocabulary.SKOS.semanticRelation );
    }
    public void addSemanticRelation( de.dfki.km.jena2java.skos.vocabulary.Concept _semanticrelation )
    {
        m_res.addProperty( de.dfki.km.jena2java.skos.vocabulary.SKOS.semanticRelation, _semanticrelation );
    }
    public void removeSemanticRelation( de.dfki.km.jena2java.skos.vocabulary.Concept _semanticrelation )
    {
        m_res.getModel().remove( m_res.getModel().listStatements( m_res, de.dfki.km.jena2java.skos.vocabulary.SKOS.semanticRelation, _semanticrelation ) );
    }
    public void clearSemanticRelation()
    {
        m_res.removeAll( de.dfki.km.jena2java.skos.vocabulary.SKOS.semanticRelation );
    }
    // RDFS2Class: end property http://www.w3.org/2004/02/skos/core#semanticRelation

}
// RDFS2Class: end of class Concept
// EOF

