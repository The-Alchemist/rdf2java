package dfki.rdf.util;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

import org.w3c.rdf.model.*;
import org.w3c.rdf.syntax.*;
import org.w3c.rdf.util.*;
import org.w3c.rdf.vocabulary.rdf_syntax_19990222.*;
import org.w3c.rdf.vocabulary.rdf_schema_19990303.*;


public class RDFExport {

  RDFFactory _rdfFactory;
  NodeFactory _nodeFactory;
  Model _model;

  final static String _anonNamespacePrefix = "http://www.anonymous.org/";
  String _anonNamespace;
  String _defaultNamespace; // used when package is not in map below
  Map _packagesNamespaces;

  final static String _damlNamespace = "http://www.daml.org/2001/03/daml+oil#";
  Resource _damlList;
  Resource _damlFirst;
  Resource _damlRest;
  Resource _damlNil;

  HashMap _exportedObjects; // maps objects to resources
  int _genid;

  int _depth = -1;  // default value of -1 means infinite depth


  public RDFExport(String namespace) {
    _defaultNamespace = namespace;
    _packagesNamespaces = new HashMap();
  }

  public RDFExport(String namespace, Map packagesNamespaces) {
    _defaultNamespace = namespace;
    _packagesNamespaces = packagesNamespaces;
  }

  public void setDepth (int depth) {
    _depth = depth;
  }

  // RDF model/statement/resource handling

  void add(Statement statement) {
    if (statement != null) {
      try {
        _model.add(statement);
      } catch (Exception e) { System.err.println(e); }
    }
  }

  Statement statement(Resource subject, Resource predicate,
      RDFNode object) {
    if (subject != null && predicate != null && object != null) {
      try {
        return _nodeFactory.createStatement(subject, predicate, object);
      } catch (Exception e) { System.err.println(e); }
    }
    return null;
  }

  Resource resource(String uri) {
    try {
      return _nodeFactory.createResource(uri);
    } catch (Exception e) { System.err.println(e); }
    return null;
  }

  Resource resource(String namespace, String name) {
    try {
      return _nodeFactory.createResource(namespace, name);
    } catch (Exception e) { System.err.println(e); }
    return null;
  }

  Resource anonResource(String name) {
    return resource(_anonNamespace, name);
  }

  Resource sysResource(String classPackage, String name) {
    String namespace = (String)_packagesNamespaces.get(classPackage);
    if (namespace == null) namespace = _defaultNamespace;
    return resource(namespace, name);
  }

  Resource liResource(int i) {
    return resource(RDF._Namespace, "_" + i);
  }

  Literal literal(String string) {
    try {
      return _nodeFactory.createLiteral(string);
    } catch (Exception e) { System.err.println(e); }
    return null;
  }

  String getGenid() {
    return "genid" + _genid++;
  }


  // export

  public void exportObjects(Collection objects, String filename) {
    exportObjectsToModel(objects);
    try {
      RDFUtil.saveModel(_model, filename,
	new org.w3c.rdf.implementation.syntax.sirpac.SiRS());
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public void exportObjects(Collection objects, PrintStream ps) {
    exportObjectsToModel(objects);
    try {
      RDFUtil.dumpModel(_model, ps,
	new org.w3c.rdf.implementation.syntax.sirpac.SiRS());
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public String exportObjects(Collection objects) {
    // returns model as String
    exportObjectsToModel(objects);
    try {
      return RDFUtil.dumpModel(_model,
	new org.w3c.rdf.implementation.syntax.sirpac.SiRS());
    } catch (Exception e) {
      System.err.println(e);
      return null;
    }
  }

  void exportObjectsToModel(Collection objects) {
    // init
    long timestamp = new Date().getTime();
    _anonNamespace = _anonNamespacePrefix + timestamp + "#";
    _genid = 1;
    _rdfFactory = new RDFFactoryImpl();
    _model = _rdfFactory.createModel();
    try {
      _nodeFactory = _model.getNodeFactory();
    } catch (Exception e) {
      System.err.println(e);
      return;
    }
    // export to RDF model
    _exportedObjects = new HashMap();
    for (Iterator objectIterator = objects.iterator();
	 objectIterator.hasNext();) {
      Object object = objectIterator.next();
      exportObject(object);
    }
  }

  public Resource exportObject(Object object) {
    return exportObject(object, 0);
  }

  public Resource exportObject(Object object, int actualDepth) {
    Resource resource = (Resource)_exportedObjects.get(object);
    if (resource == null) { // not yet exported
      if (object instanceof Resource) {
	resource = (Resource)object;
	_exportedObjects.put(object, resource);
	// add(statement(resource, RDF.type, RDFS.Resource)); // ???
      } else {
	String uri = getURI(object);
	if (uri != null)
	  resource = resource(uri);
	else
	  resource = anonResource(getGenid());
	_exportedObjects.put(object, resource);
        if (_depth >= 0 && actualDepth > 0 && actualDepth > _depth) {
            if (uri == null) throw new Error("ERROR (rdf2java) in method RDFExport.exportObject: found object without URI at max. depth");
            return resource;
        }
	Class cls = object.getClass();
	exportType(resource, cls);
	Method[] methods = cls.getMethods();
	String classPackage = getClassPackage(cls);
	exportPropertyValues(resource, classPackage, object, methods, actualDepth);
      }
    }
    return resource;
  }

  String getURI(Object object) {
    Class cls = object.getClass();
    try {
      Method method = cls.getMethod("getURI", null);
      return (String)method.invoke(object, null);
    } catch (Exception e) {
      return null;
    }
  }

  void exportType(Resource resource, Class cls) {
    String className = getClassName(cls);
    String classPackage = getClassPackage(cls);
    add(statement(resource, RDF.type, sysResource(classPackage, className)));
  }

  String getClassPackage(Class cls) {
    String className = cls.getName();
    int p = className.lastIndexOf('.');
    return className.substring(0, p);
  }

  String getClassName(Class cls) {
    String className = cls.getName();
    int p = className.lastIndexOf('.');
    return className.substring(p+1);
  }

  void exportPropertyValues(Resource resource, String classPackage,
      Object object, Method[] methods, int actualDepth) {
    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      String methodName = method.getName();
      if (methodName.equals("getClass") || methodName.equals("getURI"))
        continue;
      if ( methodName.startsWith("get") ||
           (methodName.startsWith("Get") && methodName.endsWith("__asURI")) ) {
	Class[] parameterTypes = method.getParameterTypes();
	if (parameterTypes.length == 0) {
	  Object value = null;
	  try {
	    value = method.invoke(object, null);
	  } catch (Exception e) {
	    System.err.println("exportPropertyValues: "  + e);
	  }
	  String propertyName = getPropertyName(methodName);
	  exportPropertyValue(resource, classPackage, propertyName, value, actualDepth);
	}
      }
    }
  }

  void exportPropertyValue(Resource resource, String classPackage,
      String propertyName, Object value, int actualDepth) {
    if (value != null) {
      if (value instanceof Collection) {
	if (value instanceof List) { // list -> Seq (or daml:List)
	  exportValueList(resource, classPackage, propertyName, (List)value, actualDepth);
	} else { // set -> multiple occurences of proptery
	  exportValueSet(resource, classPackage, propertyName, (Collection)value, actualDepth);
	}
      } else { // simple value
	exportSimpleValue(resource, classPackage, propertyName, value, actualDepth);
      }
    }
  }

  void exportValueList(Resource resource, String classPackage,
      String propertyName, List values, int actualDepth) {
    String seqName = getGenid();
    Resource seqResource = anonResource(seqName);
    add(statement(resource, sysResource(classPackage, propertyName),
      seqResource));
    add(statement(seqResource, RDF.type, RDF.Seq));
    int i = 1;
    for (Iterator valueIterator = values.iterator();
	 valueIterator.hasNext();) {
      Object value = valueIterator.next();
      exportSimpleValue(seqResource, liResource(i), value, actualDepth);
      i++;
    }
  }

  void exportValueSet(Resource resource, String classPackage,
      String propertyName, Collection values, int actualDepth) {
    for (Iterator valueIterator = values.iterator();
	 valueIterator.hasNext();) {
      Object value = valueIterator.next();
      exportSimpleValue(resource, classPackage, propertyName, value, actualDepth);
    }
  }

  void exportSimpleValue(Resource resource, String classPackage,
      String propertyName, Object value, int actualDepth) {
    exportSimpleValue(resource, sysResource(classPackage, propertyName), value, actualDepth);
  }

  void exportSimpleValue(Resource subject, Resource property, Object value, int actualDepth) {
    if (value instanceof String) { // rdf:Literal
      add(statement(subject, property, literal((String)value)));
    } else { // resource
      Resource valueResource = exportObject(value, actualDepth+1);
      add(statement(subject, property, valueResource));
    }
  }

  String getPropertyName(String methodName) {
    // getProp -> prop
    // get_prop -> prop and esp. get_Prop -> Prop (upper case properties)
    if (methodName.startsWith("get"))
    {
        if (methodName.startsWith("get_"))
          return methodName.substring(4);
        else
          return Character.toLowerCase(methodName.charAt(3))
            + methodName.substring(4);
    }
    else
    if (methodName.startsWith("Get") && methodName.endsWith("__asURI"))
    {
        if (methodName.startsWith("Get_"))
          return methodName.substring(4, methodName.length()-7);
        else
          return Character.toLowerCase(methodName.charAt(3))
            + methodName.substring(4, methodName.length()-7);
    }
    else
        throw new Error("implementation error in dfki.rdf.util.RDFExport . getPropertyName");
  }


}


