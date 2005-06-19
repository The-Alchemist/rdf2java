package de.dfki.km.jena2java;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * The ObjectTracker keeps track of the mappings of instances
 * of RDFS classes (or, to be exact, the instances' URIs) to Java objects as
 * well of the mappings of RDFS class URIs to the corresponding Java classes.
 * @author kiesel
 */
public class ObjectTracker {
    public HashMap uri2instance = new HashMap();

    /**
     * Register a wrapper instance for a given URI. 
     * @param URI the URI.
     * @param o the wrapper instance for given URI.
     */
    public void putInstance(String URI, Object o) {
        uri2instance.put(URI, o);
    }
    
    /**
     * Unregisters a wrapper instance for this object tracker -
     * no further action is taken regarding the model. Use the
     * {@link #deleteInstance deleteInstance} if you want to
     * really delete stuff.
     */ 
    public void removeInstance(String URI) {
        uri2instance.remove(URI);
    }
    
    /**
     * Return a wrapper instance for a resource with the given URI, 
     * <b>if</b> the object tracker already knows that resource;
     * if not, null will be returned.
     * @param URI the URI of the requested resource.
     * @return a wrapper instance for the given URI; 
     *         null if that resource is not yet known.
     */
    public Object getInstance(String URI) {
        return uri2instance.get(URI);
    }
    
    /**
     * Get the wrapper instance for a resource. Create the wrapper instance
     * if not already existing.
     * @param r Resource to get the wrapper instance for.
     * @return The wrapper instance.
     */
    public Object getInstance(Resource r) {
        return getInstance(r, JenaResourceWrapper.class);
    }
    
    /**
     * Get the wrapper instance for a resource. Create the wrapper instance 
     * if not already existing.
     * If there is no rdf:type information available for the resource, the 
     * <code>defaultClass</code> parameter is used for creating a wrapper.
     * <br/>
     * Only use subclasses of <code>JenaResourceWrapper</code>.
     * @param r Resource to get the wrapper instance for.
     * @param defaultClass the wrapper class that is used if no <code>rdf:type</code> 
     *                     information is available 
     * @return The wrapper instance.
     */
    public Object getInstance(Resource r, Class defaultClass) {
        String sUri = r.getURI();
        if( sUri == null ) sUri = r.getId().toString();
        if( sUri == null ) return null; //TODO: is this O.K.?
        Object o = getInstance(sUri);
        if (o == null) {
            // no wrapper built for this URI yet: build one
            // first, determine the class we should use for the wrapper
            Class cls = null;
            for( NodeIterator it = r.getModel().listObjectsOfProperty(r, RDF.type); it.hasNext(); )
            {
                Resource resClass = (Resource)it.nextNode();
                Class fcls = getClass(resClass);
                if(fcls != null) 
                {
                    if(!defaultClass.isAssignableFrom(fcls))
                        continue;  // bad class
                    cls = fcls;
                    break;
                }
            }
            if( cls == null ) cls = defaultClass;
            // second, build the wrapping instance
            Class jenaResourceWrapperDefinition;
            Class[] argsClasses = new Class[] { Resource.class };
            Object[] args = new Object[] { r };
            Constructor argsConstructor;
            try {
                argsConstructor = cls.getConstructor(argsClasses);
                o = argsConstructor.newInstance(args);
                uri2instance.put(sUri, o);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return o;
    }
    
    /**
     * "Delete" or "cut out" a wrapper instance<br/>
     * All triples containing the given resource (edges to and from) 
     * are removed. Furthermore, the given resource is unregistered from this 
     * object tracker.<br/>
     * After deletion of the wrapper instance, the parameters <code>removedStatements</code>
     * and <code>neighboringResource</code> contain potentially interesting information:
     * <ul>
     * <b>removedStatements</b> a model containing the removed statements.<br/>
     * <b>neighboringResources</b> a collection of neighboring <code>Resource</code>s.
     * The neighboring resources can be used to realize some recursive deletion.
     * </ul>
     * <b>Note</b> each of both parameters may be null if that respective information 
     * is not needed.<br/>
     * <b>Note</b> inverse properties (inverse edges) are removed from the model, too.
     */
    public void deleteInstance(Resource r, Model removedStatements, Collection neighboringResources) {
        if( r instanceof JenaResourceWrapper ) 
            r = ((JenaResourceWrapper)r).getResource();
        Model model = r.getModel();
        
        Model removeModel = (removedStatements != null) ? removedStatements : ModelFactory.createDefaultModel(); 
        removeModel.add( model.listStatements( r   , null, (RDFNode)null ) );
        removeModel.add( model.listStatements( null, null, r             ) );
        // for( StmtIterator it = removeModel.listStatements(); it.hasNext(); )
        //     System.out.println( it.nextStatement() );
        
        if( neighboringResources != null ) {
            for( NodeIterator it = removeModel.listObjects(); it.hasNext(); ) {
                RDFNode o = it.nextNode();
                if( o.equals( r ) ) continue;
                if( o instanceof Resource )
                    neighboringResources.add( o );
            }
            for( ResIterator it = removeModel.listSubjects(); it.hasNext(); ) {
                Resource s = it.nextResource();
                if( s.equals( r ) ) continue;
                neighboringResources.add( s );
            }
        }
        
        model.remove( removeModel );
        if( r.getURI() != null )
            removeInstance( r.getURI() );
    }
    
    public static ObjectTracker instance = null;

    /**
     * Returns "one" instance of the object tracker. 
     * Theoretically, you can have more than just one, 
     * but right now, we use this method to work with the
     * one and only object tracker. 
     * <br/>   
     * Well... <i>This is still a hack right now.</i>
     */
    public static ObjectTracker getInstance() {
        if(instance == null) instance = new ObjectTracker();
        return instance;
    }
    
    public HashMap resource2class = new HashMap();
    
    /**
     * Register a wrapper class for a given RDF Schema class (resource)
     * @param res the resource of an RDF Schema class.
     * @param cls the wrapper class (this will be a subclass of <code>JenaResourceWrapper</code>).
     */
    public void addClass(Resource res, Class cls) {
        resource2class.put(res, cls);
    }
    
    /**
     * Returns the wrapper class for a given RDF Schema class (resource).
     * @param res the resource of an RDF Schema class.
     * @return the wrapper class (this will be a subclass of <code>JenaResourceWrapper</code>).
     */
    public Class getClass(Resource res) {
        if(resource2class.size() == 0) throw new RuntimeException("RDF2Java ObjectTracker not initialized!");
        return (Class) resource2class.get(res);
    }
}
