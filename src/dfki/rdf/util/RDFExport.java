
package dfki.rdf.util;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

import org.w3c.rdf.model.*;
import org.w3c.rdf.syntax.*;
import org.w3c.rdf.util.*;
import org.w3c.rdf.vocabulary.rdf_syntax_19990222.*;
import org.w3c.rdf.vocabulary.rdf_schema_19990303.*;


public class RDFExport
{
    RDFFactory m_rdfFactory;
    NodeFactory m_nodeFactory;
    Model m_model;

    public final static String NAMESPACE_FOR_TEMPORARIES = "http://dfki.km.rdf2java/temp#";
    final static String m_anonNamespacePrefix = NAMESPACE_FOR_TEMPORARIES;
    long m_timestamp;
    String m_anonNamespace;
    String m_defaultNamespace; // used when package is not in map below
    Map m_packagesNamespaces;

    final static String m_damlNamespace = "http://www.daml.org/2001/03/daml+oil#";
    Resource m_damlList;
    Resource m_damlFirst;
    Resource m_damlRest;
    Resource m_damlNil;

    HashMap m_exportedObjects; // maps objects to resources
    int m_genid;

    int m_depth = -1; // default value of -1 means infinite depth


    public RDFExport( String namespace )
    {
        m_defaultNamespace = namespace;
        m_packagesNamespaces = new HashMap();
    }

    public RDFExport( String namespace, Map packagesNamespaces )
    {
        m_defaultNamespace = namespace;
        m_packagesNamespaces = packagesNamespaces;
    }

    public void setDepth( int depth )
    {
        m_depth = depth;
    }

    // RDF model/statement/resource handling

    void add( Statement statement )
    {
        if( statement != null )
        {
            try
            {
                m_model.add( statement );
            }
            catch( Exception e )
            {
                System.err.println( e );
            }
        }
    }

    Statement statement( Resource subject, Resource predicate, RDFNode object )
    {
        if( subject != null && predicate != null && object != null )
        {
            try
            {
                return m_nodeFactory.createStatement( subject, predicate, object );
            }
            catch( Exception e )
            {
                System.err.println( e );
            }
        }
        return null;
    }

    Resource resource( String uri )
    {
        try
        {
            return m_nodeFactory.createResource( uri );
        }
        catch( Exception e )
        {
            System.err.println( e );
        }
        return null;
    }

    Resource resource( String namespace, String name )
    {
        try
        {
            return m_nodeFactory.createResource( namespace, name );
        }
        catch( Exception e )
        {
            System.err.println( e );
        }
        return null;
    }

    Resource anonResource( String name )
    {
        return resource( m_anonNamespace, "" + m_timestamp + "_" + name );
    }

    Resource sysResource( String classPackage, String name )
    {
        String namespace = (String) m_packagesNamespaces.get( classPackage );
        if( namespace == null ) namespace = m_defaultNamespace;
        return resource( namespace, name );
    }

    Resource liResource( int i )
    {
        return resource( RDF._Namespace, "_" + i );
    }

    Literal literal( String string )
    {
        try
        {
            return m_nodeFactory.createLiteral( string );
        }
        catch( Exception e )
        {
            System.err.println( e );
        }
        return null;
    }

    String getGenid()
    {
        return "genid" + m_genid++;
    }


    // export

    public void exportObjects( Collection objects, String filename )
    {
        exportObjectsToModel( objects );
        try
        {
            RDFUtil.saveModel( m_model, filename, new org.w3c.rdf.implementation.syntax.sirpac.SiRS() );
        }
        catch( Exception e )
        {
            System.err.println( e );
        }
    }

    public void exportObjects( Collection objects, PrintStream ps )
    {
        exportObjectsToModel( objects );
        try
        {
            RDFUtil.dumpModel( m_model, ps, new org.w3c.rdf.implementation.syntax.sirpac.SiRS() );
        }
        catch( Exception e )
        {
            System.err.println( e );
        }
    }

    public String exportObjects( Collection objects )
    {
        // returns model as String
        exportObjectsToModel( objects );
        try
        {
            return RDFUtil.dumpModel( m_model, new org.w3c.rdf.implementation.syntax.sirpac.SiRS() );
        }
        catch( Exception e )
        {
            System.err.println( e );
            return null;
        }
    }

    synchronized void exportObjectsToModel( Collection objects )
    {
        // init
        m_timestamp = new Date().getTime();
        m_anonNamespace = m_anonNamespacePrefix;
        m_genid = 1;
        m_rdfFactory = new RDFFactoryImpl();
        m_model = m_rdfFactory.createModel();
        try
        {
            m_nodeFactory = m_model.getNodeFactory();
        }
        catch( Exception e )
        {
            System.err.println( e );
            return;
        }
        // export to RDF model
        m_exportedObjects = new HashMap();
        for( Iterator objectIterator = objects.iterator(); objectIterator.hasNext(); )
        {
            Object object = objectIterator.next();
            exportObject( object );
        }
    }

    public Resource exportObject( Object object )
    {
        return exportObject( object, 0 );
    }

    synchronized public Resource exportObject( Object object, int actualDepth )
    {
        Resource resource = (Resource) m_exportedObjects.get( object );
        if( resource == null || actualDepth == 0 )
        { // not yet exported OR only exported as resource due to a cut-by-depth
            if( !(object instanceof THING) )
            {
                if( resource == null )
                {
                    resource = (Resource) object;
                    m_exportedObjects.put( object, resource );
                    // add(statement(resource, RDF.type, RDFS.Resource)); // ???
                }
            }
            else
            { // object instanceof THING
                String uri = getURI( object );
                if( resource == null )
                {
                    if( uri != null ) 
                        resource = resource( uri );
                    else
                        resource = anonResource( getGenid() );
                    m_exportedObjects.put( object, resource );
                }
                if( m_depth >= 0 && actualDepth > 0 && actualDepth > m_depth )
                {
                    if( uri == null ) 
                        throw new Error( "ERROR (rdf2java) in method RDFExport.exportObject: found object without URI at max. depth" );
                    return resource;
                }
                Class cls = object.getClass();
                exportType( resource, cls );
                ////2002.02.07 Method[] methods = cls.getMethods();
                Method[] methods = RDF2Java.getPropertyMethods( cls );
                String classPackage = getClassPackage( cls );
                exportPropertyValues( resource, classPackage, object, methods, actualDepth );
            }
        }
        return resource;
    }

    String getURI( Object object )
    {
        Class cls = object.getClass();
        try
        {
            Method method = cls.getMethod( "getURI", null );
            return (String) method.invoke( object, null );
        }
        catch( Exception e )
        {
            return null;
        }
    }

    void exportType( Resource resource, Class cls )
    {
        String className = getClassName( cls );
        String classPackage = getClassPackage( cls );
        add( statement( resource, RDF.type, sysResource( classPackage, className ) ) );
    }

    static String getClassPackage( Class cls )
    {
        String className = cls.getName();
        int p = className.lastIndexOf( '.' );
        return className.substring( 0, p );
    }

    static String getClassName( Class cls )
    {
        String className = cls.getName();
        int p = className.lastIndexOf( '.' );
        return className.substring( p + 1 );
    }

    void exportPropertyValues( Resource resource, String classPackage,
            Object object, Method[] methods, int actualDepth )
    {
        for( int i = 0; i < methods.length; i++ )
        {
            Method method = methods[i];
            String methodName = method.getName();
            if( methodName.equals( "getClass" ) || methodName.equals( "getURI" ) ) 
                continue;
            if( methodName.startsWith( "get" ) )
            {
                Class[] parameterTypes = method.getParameterTypes();
                if( parameterTypes.length == 0 )
                {
                    Object value = null;
                    try
                    {
                        value = method.invoke( object, null );
                    }
                    catch( Exception e )
                    {
                        System.err.println( "exportPropertyValues: " + e );
                    }
                    String propertyName = RDF2Java.extractPropertyName( methodName );
                    exportPropertyValue( resource, classPackage, propertyName, value, actualDepth );
                }
            }
        }
    }

    void exportPropertyValue( Resource resource, String classPackage,
            String propertyName, Object value, int actualDepth )
    {
        if( value != null )
        {
            if( value instanceof Collection )
            {
                if( value instanceof List )
                {   
                    // list -> Seq (or daml:List)
                    exportValueList( resource, classPackage, propertyName, (List) value, actualDepth );
                }
                else
                {   
                    // set -> multiple occurences of proptery
                    exportValueSet( resource, classPackage, propertyName, (Collection) value, actualDepth );
                }
            }
            else
            { // simple value
                exportSimpleValue( resource, classPackage, propertyName, value, actualDepth );
            }
        }
    }

    void exportValueList( Resource resource, String classPackage,
            String propertyName, List values, int actualDepth )
    {
        String seqName = getGenid();
        Resource seqResource = anonResource( seqName );
        add( statement( resource, sysResource( classPackage, propertyName ), seqResource ) );
        add( statement( seqResource, RDF.type, RDF.Seq ) );
        int i = 1;
        for( Iterator valueIterator = values.iterator(); valueIterator.hasNext(); )
        {
            Object value = valueIterator.next();
            exportSimpleValue( seqResource, liResource( i ), value, actualDepth );
            i++;
        }
    }

    void exportValueSet( Resource resource, String classPackage,
            String propertyName, Collection values, int actualDepth )
    {
        for( Iterator valueIterator = values.iterator(); valueIterator.hasNext(); )
        {
            Object value = valueIterator.next();
            exportSimpleValue( resource, classPackage, propertyName, value, actualDepth );
        }
    }

    void exportSimpleValue( Resource resource, String classPackage,
            String propertyName, Object value, int actualDepth )
    {
        exportSimpleValue( resource, sysResource( classPackage, propertyName ), value, actualDepth );
    }

    void exportSimpleValue( Resource subject, Resource property, Object value,
            int actualDepth )
    {
        if( value instanceof String )
        { 
            // rdf:Literal
            add( statement( subject, property, literal( (String) value ) ) );
        }
        else
        { 
            // resource
            Resource valueResource = exportObject( value, actualDepth + 1 );
            add( statement( subject, property, valueResource ) );
        }
    }


}


