package dfki.rdf.util;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

import org.xml.sax.*;
import org.w3c.rdf.model.*;
import org.w3c.rdf.syntax.*;
import org.w3c.rdf.util.*;
import org.w3c.rdf.vocabulary.rdf_syntax_19990222.*;
import org.w3c.rdf.vocabulary.rdf_schema_19990303.*;


public class RDFImport {

  RDFFactory _rdfFactory;
  NodeFactory _nodeFactory;
  Model _model;

  /* replaced by map below
  String _namespace; // map this namespace to
  String _package;   // this Java package
  */
  Map _namespacesPackages; // maps namespaces to (Java) packages

  final static String _damlNamespace = "http://www.daml.org/2001/03/daml+oil#";
  final static String URI = "URI";
  Resource _damlList;
  Resource _damlFirst;
  Resource _damlRest;
  Resource _damlNil;

  HashMap _objects; // maps object resources to (Java) objects


  public RDFImport(String namespace, String pkg) {
    _namespacesPackages = new HashMap();
    _namespacesPackages.put(namespace, pkg);
    _objects = new HashMap();
  }

  public RDFImport(Map namespacesPackages) {
    _namespacesPackages = namespacesPackages;
    _objects = new HashMap();
  }


  // SS:2002.01.22
  public void reinit ()
  {
    _objects = new HashMap();
  }

  // RDF basics

  void getModel(String rdfsFilename) throws Exception {
    FileReader reader = new FileReader(rdfsFilename);
    getModel(reader);
    reader.close();
  }

  void getModel(Reader reader) throws Exception {
    _rdfFactory = new RDFFactoryImpl();
    _model = _rdfFactory.createModel();
      parseStream(reader);
      _nodeFactory = _model.getNodeFactory();
    // System.out.println("Model = " + _model);
    /* show all triples:
    RDFUtil.printStatements(_model, System.out);
    System.out.println();
    */
    _damlList = resource(_damlNamespace, "List");
    _damlFirst = resource(_damlNamespace, "first");
    _damlRest = resource(_damlNamespace, "rest");
    _damlNil = resource(_damlNamespace, "nil");
  }

  void parseStream(Reader reader) throws Exception {
    RDFParser parser = _rdfFactory.createParser();
    if (!(reader instanceof BufferedReader))
      reader = new BufferedReader(reader);
    InputSource source = new InputSource(reader);
    // source.setSystemId(...);
    RDFConsumer consumer = new ModelConsumer(_model);
    parser.parse(source, consumer);
  }


  HashSet getInstances() {
    HashSet instances = new HashSet();
    try {
      Model instancesModel = _model.find(null, RDF.type, null);
      addSubjects(instances, instancesModel);
    } catch (Exception e) { System.err.println(e); }
    // remove classes and properties ... ???
    return instances;
  }

  Resource getDirectType(Resource resource) {
    Collection types = getValues(_model, resource, RDF.type);
    if (types.isEmpty())
      return RDFS.Resource; // null ???
    else if (types.size() == 1)
      // check that type is not a literal!
      return (Resource)types.iterator().next();
    else { // more than one type; pick main one ???
      /* ... !!! (check for type in triple namespace)
      Resource type = pickMainType(types);
      return type;
      */
      return RDFS.Resource;
    }
  }

  //SS:2001-12-14
  Class getClass (Resource resource) {
    try {
      String pkg = getPackage(resource.getNamespace());
      if (pkg == null) return null;
      String className = resource.getLocalName();
      String fullClassName = pkg + "." + className;
      Class cls = Class.forName(fullClassName);
      return cls;
    } catch (Exception e) {
      return null;
      ////System.err.println("No class: " + fullClassName);
    }
  }

  HashSet getProperties(Resource resource) {
    HashSet properties = new HashSet();
    try {
      Model propertiesModel = _model.find(resource, null, null);
      addPredicates(properties, propertiesModel);
    } catch (Exception e) { System.err.println(e); }
    return properties;
  }

  /* handling of RDF Schema stuff; not needed at the moment ...

  HashSet getClasses() {
    HashSet classes = new HashSet();
    try {
      Model classesModel = _model.find(null, RDF.type, RDFS.Class);
      addSubjects(classes, classesModel);
    } catch (Exception e) { System.err.println(e); }
    return classes;
  }

  Collection getDirectSuperclasses(Resource cls) {
    return getValues(_model, cls, RDFS.subClassOf);
  }

  HashSet getProperties() {
    HashSet properties = new HashSet();
    try {
      Model propertiesModel = _model.find(null, RDF.type, RDF.Property);
      addSubjects(properties, propertiesModel);
    } catch (Exception e) { System.err.println(e); }
    return properties;
  }

  Collection getDomain(Resource property) {
    return getValues(_model, property, RDFS.domain);
  }

  Resource getRange(Resource property) {
    Collection ranges = getValues(_model, property, RDFS.range);
    if (ranges.isEmpty())
      return null;
    else if (ranges.size() == 1)
      return (Resource)ranges.iterator().next();
    else {
      System.err.println("property has more than one range: " + property +
      " ; ranges = " + ranges);
      return null;
    }
  }

  -------- */


  // RDF auxiliaries

  void addSubjects(HashSet set, Model model) {
    try {
      for (Enumeration tripleEnum = model.elements();
	   tripleEnum.hasMoreElements();) {
	Statement statement = (Statement)tripleEnum.nextElement();
	set.add(statement.subject());
      }
    } catch (Exception e) { System.err.println(e); }
  }

  void addPredicates(HashSet set, Model model) {
    try {
      for (Enumeration tripleEnum = model.elements();
	   tripleEnum.hasMoreElements();) {
	Statement statement = (Statement)tripleEnum.nextElement();
	set.add(statement.predicate());
      }
    } catch (Exception e) { System.err.println(e); }
  }

  void addObjects(HashSet set, Model model) {
    try {
      for (Enumeration tripleEnum = model.elements();
	   tripleEnum.hasMoreElements();) {
	Statement statement = (Statement)tripleEnum.nextElement();
	set.add(statement.object());
      }
    } catch (Exception e) { System.err.println(e); }
  }


  Collection getValues(Model model, Resource resource,
      Resource property) {
    return getValues(model, resource, property, false); // no daml:List
  }

  Collection getValues(Model model, Resource resource,
      Resource property, boolean handleLists) {
    HashSet values = new HashSet();
    try {
      Model resultModel = model.find(resource, property, null);
      addObjects(values, resultModel);
    } catch (Exception e) { System.err.println(e); }
    if (handleLists && values.size() == 1) { // check for rdf:Seq and daml:List
      RDFNode value = (RDFNode)values.iterator().next();
      if (value instanceof Resource) {
	Resource type = getDirectType((Resource)value);
	if (RDF.Seq.equals(type))
	  return getSeqList((Resource)value);
	if (_damlList.equals(type) || _damlNil.equals(value))
	  return getDamlList((Resource)value);
      }
    }
    return values;
  }

  Collection getSeqList(Resource list) {
    ArrayList values = new ArrayList();
    int index = 1;
    while (true) {
      RDFNode value =
	getValue(_model, list, resource(RDF._Namespace, "_" + index));
      if (value != null)
	values.add(value);
      else
	break;
      index++;
    }
    return values;
  }

  Collection getDamlList(Resource list) {
    ArrayList values = new ArrayList();
    getDamlList(list, values);
    return values;
  }

  void getDamlList(Resource list, ArrayList values) {
    if (list != null && !_damlNil.equals(list)) {
      Resource type = getDirectType(list);
      if (_damlList.equals(type)) {
	values.add(getValue(_model, list, _damlFirst));
	getDamlList((Resource)getValue(_model, list, _damlRest), values);
      } else
	System.err.println("daml:List malformed: " + list);
    }
  }

  RDFNode getValue(Model model, Resource resource, Resource property) {
    Collection values = getValues(model, resource, property);
    if (values.size() == 1)
      try {
	return (RDFNode)values.iterator().next();
      } catch (Exception e) { System.err.println(e); return null; }
    else
      return null;
  }

  String getStringValue(Model model, Resource resource,
      Resource property) {
    RDFNode value = getValue(model, resource, property);
    if (value != null)
      return getLabel(value);
    else
      return null;
  }

  String getLabel(RDFNode node) {
    try {
      return node.getLabel();
    } catch (Exception e) {
      System.err.println(e);
      return "?";
    }
  }

  public String getNamespace(Resource resource) {
    // be careful: this method might return null
    try {
      return resource.getNamespace();
    } catch (Exception e) {
      System.err.println(e);
      return "?";
    }
  }

  String getLocalName(Resource resource) {
    try {
      return resource.getLocalName();
    } catch (Exception e) {
      System.err.println(e);
      return "?";
    }
  }

  Resource resource(String namespace, String name) {
    try {
      return _nodeFactory.createResource(namespace, name);
    } catch (Exception e) { System.err.println(e); }
    return null;
  }


  // import

  public Map importObjects(String filename) throws Exception {
    return importObjects(filename, new KnowledgeBase());
  }

  public Map importObjects(String filename, KnowledgeBase cachedObjects) throws Exception {
    getModel(filename);
    importObjects(cachedObjects);
    return _objects;
  }

  public Map importObjects(Reader reader) throws Exception {
    return importObjects(reader, new KnowledgeBase());
  }

  public Map importObjects(Reader reader, KnowledgeBase cachedObjects) throws Exception {
    getModel(reader);
    importObjects(cachedObjects);
    return _objects;
  }


  void importObjects(KnowledgeBase cachedObjects) {

    if (_model == null) return;

    // create objects and add attribute values
    Collection instances = getInstances();
    for (Iterator objIterator = instances.iterator();
	 objIterator.hasNext();) {
      Resource resource = (Resource)objIterator.next();
      Object object = getObject(resource, cachedObjects);
      // String pkg = getPackage(...) ??? package of class!!!
      HashSet properties = getProperties(resource);
      for (Iterator propIterator = properties.iterator();
	   propIterator.hasNext();) {
	Resource property = (Resource)propIterator.next();
	if (property.equals(RDF.type))
	  continue; // we never care about rdf:type
	String propertyNamespace = getNamespace(property);
	String propertyName = getLocalName(property);
	String propertyPackage = getPackage(propertyNamespace);
	if (propertyPackage != null) { // ??? propertyPackage.equals(pkg)
	  Collection values = getValues(_model, resource, property, true);
	  for (Iterator valueIterator = values.iterator();
	       valueIterator.hasNext();) {
	    RDFNode valueNode = (RDFNode)valueIterator.next();
	    Object value = null;
	    if (valueNode instanceof Resource)
	      value = getObject((Resource)valueNode, cachedObjects);
	    else // Literal (or other, irrelevant cases)
	      value = getLabel(valueNode);
	    if (value != null)
	      putValue(object, propertyName, value, resource);
	    else
	      System.err.println("Unhandled value: " + resource + " . " +
	      property + " = " + valueNode);
	  }
	}
      }
    }

  }

  Object getObject(Resource resource, KnowledgeBase cachedObjects) {
    Object object = _objects.get(resource);
    ////System.out.println("### getObject(" + resource + "); object=" + object);
    if (object != null) return object;
    Resource directType = getDirectType(resource);
    String namespace = getNamespace(directType);
//    ////SS:2001-12-12
//    try {
//        ////if (directType.getLocalName().equals("Resource"))
//        {   System.out.println("" + resource + " --directType--> " + directType);
//            directType = getDirectType(resource);  // nochmal
//        }
//    } catch (Exception ex) { }
    String pkg = getPackage(namespace);
    if (pkg != null) {
      // String localName = getLocalName(resource);
      String typeName = getLocalName(directType);
      // System.out.println("instance " + localName + " : " + typeName);
      object = getObject(resource, pkg, typeName);
    }
    if (object == null)
    {
      // maybe object is a class?
      if (directType.equals(RDFS.Class))
        object = getClass(resource);
      if (object == null)  // object still null?
      {
        // SS:2002.01.22 [begin]
        // try cached objects
        object = cachedObjects.get(resource);
        ////System.out.println("### ---> object=" + object);
        if (object != null)
            return object;
        // SS:2002.01.22 [end]
        ////System.out.println("### --->  :-(  ok, take RDFResource");
        try { object = new RDFResource(resource); }
        catch (org.w3c.rdf.model.ModelException modelex) {
          System.err.println("rdf2java-ERROR: failed to create an dfki.rdf.util.RDFResource");
          return null;
        }
      }
      ////SS:2001-12-12: object = resource; // object simply is rdfs:Resource ... !!!
      ////System.out.println("object=" + object);
    }
    _objects.put(resource, object);
    return object;
  }

  Object getObject(Resource resource, String pkg, String className) {
    String fullClassName = pkg + "." + className;
    Object object = null;
    ////System.out.println("### getObject(" + resource + ", " + pkg + ", " + className + ")");
    try {
      Class cls = Class.forName(fullClassName);
      //SS:2001-12-14
      ////System.out.println("cls="+cls);
      object = cls.newInstance();
      String uri = resource.getURI();
      if (uri.indexOf("#genid") == -1) // remove genids ... ???
	putValue(object, URI, resource.getURI(), resource);
    } catch (Exception e) {
      System.err.println("Unhandled class: " + className);
    }
    return object;
  }

  void putValue(Object object, String property, Object value, Resource objectResourceForDebug) {
    // create method name as "put" + property with first letter capitalized
    // this version ignores package incompatibilities ... !!!
    String methodName;
    if (Character.isLowerCase(property.charAt(0)) || property.equals(URI))  // "putURI" w/o "_"
        methodName = "put"  + Character.toUpperCase(property.charAt(0))
                            + property.substring(1);
    else
        methodName = "put_" + property;
    Class objectClass = object.getClass();
    Class valueClass = value.getClass();
    Method method = findMethod(objectClass, methodName, valueClass);
    if (method == null) {
      if (!property.equals(URI))
	System.err.println("\nUnhandled property: " + property + " => " + object.getClass().getName() + "." + methodName + "(" + value.getClass().getName() + "); resource was " + objectResourceForDebug);
    } else {
      try {
        method.invoke(object, new Object[] { value });
      } catch (Exception e) {
	System.err.println("Error for property " + property + " => " + object.getClass().getName() + "." + methodName + "(" + value.getClass().getName() + "):\n\t " + e);
      }
    }
  }

  Method findMethod(Class cls, String methodName, Class parameterClass) {
    // check if cls has a method with the specified parameter class
    // or one of its superclasses
    Method method = null;
    try {
      Class[] parameterTypes = new Class[] { parameterClass };
      method = cls.getMethod(methodName, parameterTypes);
    } catch (NoSuchMethodException nsme) { // go to superclass
      Class superclass = parameterClass.getSuperclass();
      if (superclass != null)
	return findMethod(cls, methodName, superclass);
      else
	return null;
    } catch (Exception e) {
      System.err.println("Error for method " + methodName + ": " + e);
      return null;
    }
    return method;
  }

  String getPackage(String namespace) {
    // returns Java package (or null) for given namespace
    return (String)_namespacesPackages.get(namespace);
  }


  // main

  public static void main(String args[]) {
    // arg 1: rdf file
    // arg 2: namespace
    // arg 3: package

    if (args.length >= 3) {

      String rdfFilename = args[0];
      String namespace = args[1];
      String pkg = args[2];

      try {
	Map objects =
	  (new RDFImport(namespace, pkg)).importObjects(rdfFilename);
	for (Iterator objIterator = objects.keySet().iterator();
	     objIterator.hasNext();) {
	  Resource resource = (Resource)objIterator.next();
	  Object object = objects.get(resource);
	  System.out.println(resource + ": " + object);
	}
      } catch (Exception e) {
	e.printStackTrace();
      }

    } else {

      System.err.println("Usage: <rdf file> <namespace> <package>");

    }

  }


}


