package de.dfki.km.jena2java;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * The ObjectTracker keeps track of the mappings of instances
 * of RDFS classes (or, to be exact, the instances' URIs) to Java objects as
 * well of the mappings of RDFS class URIs to the corresponding Java classes.
 * @author kiesel
 */
public class ObjectTracker {
    public HashMap uri2instance = new HashMap();
    
    public void putInstance(String URI, Object o) {
        uri2instance.put(URI, o);
    }
    
    public Object getInstance(String URI) {
        return uri2instance.get(URI);
    }
    
    /**
     * Get the wrapper instance for a resource. Create the wrapper instance if not already existing.
     * @param r Resource to get the wrapper instance for.
     * @return The wrapper instance.
     */
    public Object getInstance(Resource r) {
        String sUri = r.getURI();
        if( sUri == null ) sUri = r.getId().toString();
        if( sUri == null ) return null; //TODO: is this O.K.?
        Object o = getInstance(sUri);
        if (o == null) {
            // no wrapper built for this URI yet: build one
            // first, determine the class we should use for the wrapper
            Class cls = JenaResourceWrapper.class;
            Statement s = r.getProperty(RDF.type);
            if(s != null) {
                Class fcls = getClass(s.getResource().getURI());
                if(fcls != null) cls = fcls;
            }
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
    
    public static ObjectTracker instance = null;

    /** This is a hack right now. */
    public static ObjectTracker getInstance() {
        if(instance == null) instance = new ObjectTracker();
        return instance;
    }
    
    public HashMap uri2class = new HashMap();
    
    public void addClass(String URI, Class cls) {
        uri2class.put(URI, cls);
    }
    
    public Class getClass(String URI) {
        return (Class) uri2class.get(URI);
    }
}
