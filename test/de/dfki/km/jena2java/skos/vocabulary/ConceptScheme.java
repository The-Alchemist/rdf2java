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


/** RDFS2Class: class ConceptScheme
  * <p>
  */
public class  ConceptScheme
    extends JenaResourceWrapper
    implements Serializable
{
    // RDFS2Class: begin constructors
    /**
     * Creates a new anonymous instance of the RDFS class <code>ConceptScheme</code> along with its wrapper and
     * register it with an {@link ObjectTracker}.
     * To get wrapper instances for existing Jena resources, use <code>ObjectTracker.getInstance</code>.
     * @param tracker The tracker the new instance shall be registered with.
     * @param model The Jena model to create the new instance in.
     */
    public ConceptScheme(ObjectTracker tracker, Model model )
    {
        super(tracker, model, de.dfki.km.jena2java.skos.vocabulary.SKOS.ConceptScheme );
    }

    /**
     * Creates a new instance of the RDFS class <code>ConceptScheme</code> along with its wrapper and
     * register it with an {@link ObjectTracker}.
     * To get wrapper instances for existing Jena resources, use <code>ObjectTracker.getInstance</code>.
     * @param tracker The tracker the new instance shall be registered with.
     * @param model The Jena model to create the new instance in.
     * @param uri The new instance's URI.
     */
    public ConceptScheme(ObjectTracker tracker, Model model, String uri)
    {
        super(tracker, model, de.dfki.km.jena2java.skos.vocabulary.SKOS.ConceptScheme, uri);
    }

    /**
     * Using this method is usually a mistake.
     * Creates a new wrapper for an existing Jena RDF instance.
     * Registration with an <code>ObjectTracker</code> must be done elsewhere.
     * Typically this constructor should be called only by the <code>ObjectTracker</code>.
     * @param res The Jena RDF instance to be wrapped.
     */
    public ConceptScheme( Resource res )
    {
        super(res);
    }
    protected ConceptScheme(ObjectTracker tracker, Model model, Resource resource)
    {
        super(tracker, model, resource);
    }

    protected ConceptScheme(ObjectTracker tracker, Model model, Resource resource, String uri)
    {
        super(tracker, model, resource, uri);
    }

    // RDFS2Class: end constructors

    //------------------------------------------------------------------------------
    // RDFS2Class: begin property http://www.w3.org/2004/02/skos/core#hasTopConcept
    public Collection getHasTopConcept()
    {
        return getPropertyObjects( de.dfki.km.jena2java.skos.vocabulary.SKOS.hasTopConcept );
    }
    public void addHasTopConcept( de.dfki.km.jena2java.skos.vocabulary.Concept _hastopconcept )
    {
        m_res.addProperty( de.dfki.km.jena2java.skos.vocabulary.SKOS.hasTopConcept, _hastopconcept );
    }
    public void clearHasTopConcept()
    {
        m_res.removeAll( de.dfki.km.jena2java.skos.vocabulary.SKOS.hasTopConcept );
    }
    // RDFS2Class: end property http://www.w3.org/2004/02/skos/core#hasTopConcept

    //------------------------------------------------------------------------------
    // RDFS2Class: begin property http://schwarz.km.dfki.de/dummy#dummyProperty
    public Collection getDummy_dummyProperty()
    {
        return getPropertyObjects( de.dfki.km.jena2java.skos.vocabulary.SKOS.dummy_dummyProperty );
    }
    public void addDummy_dummyProperty( de.dfki.km.jena2java.skos.vocabulary.Concept _dummy_dummyproperty )
    {
        m_res.addProperty( de.dfki.km.jena2java.skos.vocabulary.SKOS.dummy_dummyProperty, _dummy_dummyproperty );
    }
    public void clearDummy_dummyProperty()
    {
        m_res.removeAll( de.dfki.km.jena2java.skos.vocabulary.SKOS.dummy_dummyProperty );
    }
    // RDFS2Class: end property http://schwarz.km.dfki.de/dummy#dummyProperty

}
// RDFS2Class: end of class ConceptScheme
// EOF

