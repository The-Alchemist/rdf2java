package dfki.rdf.util;

import java.io.*;
import java.util.*;

import dfki.util.debug.Debug;

import dfki.util.rdfs.RDFS;
import dfki.util.rdf.RDF;
import org.w3c.rdf.model.*;
import org.w3c.rdf.syntax.RDFParser;
import org.w3c.rdf.util.RDFFactory;

/** The <code>RDFS2Class</code> tool.<br>
  * This tool generates Java Classes according to a given RDFS-file.
  * <p>
  * By now only umcompiled Java files for the classes are generated.
  * Hence you need to compile them later, before you can use Michael
  * Sintek's <code>rdf2java</code> tool. But as you often enhance the Java files
  * with additional functionality, this is no real problem...
  * <p>
  * Usage (calling the {@link #main main} method):<br>
  * <code>
  * RDFS2Class [options] &lt;file.rdfs&gt; &lt;outputSrcDir&gt; {&lt;namespace&gt; &lt;package&gt;}+<br>
  * options:<br>
  *           -q: quiet operation, no output<br>
  *           -s: include toString()-stuff in generated java-files<br>
  *           -S: recursive version of toString()-stuff<br>
  *           -o: retain ordering of triples (usage of rdf:Seq in rdf-file)
  *               by using arrays instead of sets<br>
  *           -I: insert stuff for incremental file-generation
  *               (needed for potential later usage of -i);
  *               ATTENTION: this option completely re-creates java-files and erases every
  *               user-defined methods and slots, maybe you'd better use "-i"?!<br>
  *           -i: incremental generation of java-files, i.e. user added slots
  *               are kept in the re-generated java-files
  *               (this option includes already -I)
  * </code>
  * <p>
  * Of course you can also use this class and its public methods.
  * <p>
  * @author  Sven.Schwarz@dfki.de
  * @version 1.4.3
  */
public class RDFS2Class
{
//----------------------------------------------------------------------------------------------------
String m_sRDFSFile;
Map m_mapNamespaceToPackage;
String m_sClsPath;
Model m_modelRDFS;
HashSet m_setProperties;

boolean m_bRetainOrdering;
String m_sCollectionType;
boolean m_bIncludeToStringStuff;
boolean m_bIncludeToStringStuffRecursive;
boolean m_bQuiet;
boolean m_bInsertIncrementalInfo;
boolean m_bIncrementalGeneration;
boolean m_bGenerateMethodsAboveUserMethods;

RDF.Syntax m_rdfURIs = RDF.syntax();
RDFS.URIs m_rdfsURIs = RDFS.getURIs();
RDFFactory m_rdfFactory;
RDFParser m_rdfParser;
NodeFactory m_nodeFactory;
Resource m_resRDFSClass;
Resource m_resRDFSResource;
Resource m_resRDFSLiteral;
Resource m_resRDFSResCls;
Resource m_resRDFSPredSubClassOf;
Resource m_resRDFSPredDomain;
Resource m_resRDFSPredRange;
Resource m_resRDFPredType;
Resource m_resRDFResProperty;
Resource m_resProtegeMaxCardinality;
Resource m_resProtegeAllowedClasses;
Resource m_resProtegeRange;
Resource m_resProtegePredRole;


LinkedList m_lstFormerFile;  // list of strings (lines) of the former java-file
boolean m_bIncrementalGenerationForActualFile;

protected static final String PROTEGE_NS = "http://protege.stanford.edu/system#";

final static String LIST_CLASS = "java.util.LinkedList";
final static String SET_CLASS  = "java.util.HashSet";


//----------------------------------------------------------------------------------------------------
/** Constructs a new RDFS2Class generator.
  * <br>
  * Start the generation using the {@link #createClasses createClasses} method.
  * @see #setRetainOrdering
  * @see #setIncludeToStringStuff
  * @see #setQuiet
  */
public RDFS2Class (String sRDFSFile, String sClsPath, Map mapNamespaceToPackage)   throws Exception
{
    m_sRDFSFile = sRDFSFile;
    m_mapNamespaceToPackage = mapNamespaceToPackage;
    m_sClsPath = sClsPath;

    m_rdfFactory = RDF.factory();
    m_rdfParser = m_rdfFactory.createParser();
    m_nodeFactory = m_rdfFactory.getNodeFactory();
    m_resRDFSClass = m_nodeFactory.createResource(m_rdfsURIs.namespace(), "Class");         //FIXME!!!
    m_resRDFSResource = m_nodeFactory.createResource(m_rdfsURIs.namespace(), "Resource");   //FIXME!!!
    m_resRDFSLiteral = m_nodeFactory.createResource(m_rdfsURIs.literal());
    m_resRDFSResCls = m_nodeFactory.createResource(m_rdfsURIs._class());
    m_resRDFSPredSubClassOf = m_nodeFactory.createResource(m_rdfsURIs.subClassOf());
    m_resRDFSPredDomain = m_nodeFactory.createResource(m_rdfsURIs.domain());
    m_resRDFSPredRange = m_nodeFactory.createResource(m_rdfsURIs.range());
    m_resRDFPredType = m_rdfURIs.type();
    m_resRDFResProperty = m_rdfURIs.property();
    m_resProtegeMaxCardinality = m_nodeFactory.createResource(PROTEGE_NS, "maxCardinality");
    m_resProtegeAllowedClasses = m_nodeFactory.createResource(PROTEGE_NS, "allowedClasses");
    m_resProtegePredRole       = m_nodeFactory.createResource(PROTEGE_NS, "role");
    m_resProtegeRange = m_nodeFactory.createResource(PROTEGE_NS, "range");

    setRetainOrdering(false);
    setIncludeToStringStuff(false);
    setIncludeToStringStuffRecursive(false);
    setQuiet(false);
    setInsertIncrementalInfo(false);
    setIncrementalGeneration(false);
    setGenerateMethodsAboveUserMethods(false);

    m_bIncrementalGenerationForActualFile = false;
}

//----------------------------------------------------------------------------------------------------
/** Specifies if ordering of triples (usage of <code>rdf:Seq</code> in rdf-file) should be contained
  * using arrays instead of sets as member variables in the generated java-files.
  */
public void setRetainOrdering (boolean bRetainOrdering)
{
    m_bRetainOrdering = bRetainOrdering;
    m_sCollectionType = ( m_bRetainOrdering  ?  LIST_CLASS  :  SET_CLASS );
}

//----------------------------------------------------------------------------------------------------
/** Specifies if some <code>toString()</code> stuff should be included
  * in the generated java-files.
  */
public void setIncludeToStringStuff (boolean bIncludeToStringStuff)
{
    m_bIncludeToStringStuff = bIncludeToStringStuff;
}

//----------------------------------------------------------------------------------------------------
/** Specifies if the <code>toString()</code> stuff should be included
  * in the generated java-files in a way that complete Java objects should
  * be printed out whenever slots have links to other Java objects.
  */
public void setIncludeToStringStuffRecursive (boolean bIncludeToStringStuffRecursive)
{
    if (bIncludeToStringStuffRecursive)
        setIncludeToStringStuff(true);
    m_bIncludeToStringStuffRecursive = bIncludeToStringStuffRecursive;
}

//----------------------------------------------------------------------------------------------------
/** Specifies if output of this tool is wanted (on the standard output channel).
  */
public void setQuiet (boolean bQuiet)
{
    m_bQuiet = bQuiet;
}

//----------------------------------------------------------------------------------------------------
/** Specifies if info for incremental generation is inserted into the java-files
  */
public void setInsertIncrementalInfo (boolean bInsertIncrementalInfo)
{
    m_bInsertIncrementalInfo = bInsertIncrementalInfo;
}

//----------------------------------------------------------------------------------------------------
public void setIncrementalGeneration (boolean bIncrementalGeneration)
{
    if (bIncrementalGeneration)
        setInsertIncrementalInfo(true);
    m_bIncrementalGeneration = bIncrementalGeneration;
}

//----------------------------------------------------------------------------------------------------
public void setGenerateMethodsAboveUserMethods (boolean bGenerateMethodsAboveUserMethods)
{
    m_bGenerateMethodsAboveUserMethods = bGenerateMethodsAboveUserMethods;
}

//----------------------------------------------------------------------------------------------------
/** Usage (the given String <code>args</code> should be like everything after "RDFS2Class"):<br>
  * <code>
  * RDFS2Class [options] &lt;file.rdfs&gt; &lt;outputSrcDir&gt; {&lt;namespace&gt; &lt;package&gt;}+<br>
  * options:<br>
  *           -q: quiet operation, no output<br>
  *           -s: include toString()-stuff in generated java-files<br>
  *           -S: (used instead of -s) include recursive toString()-stuff in generated java-files<br>
  *           -o: retain ordering of triples (usage of rdf:Seq in rdf-file)
  *               by using arrays instead of sets<br>
  *           -I: insert stuff for incremental file-generation
  *               (needed for potential later usage of -i)<br>
  *           -i: incremental generation of java-files, i.e. user added slots
  *               are kept in the re-generated java-files
  *               (this option includes already -I)
  * </code>
  */
public static void main (String[] args)
{
    try {
        boolean bQuiet = false;
        boolean bIncludeToStringStuff = false;
        boolean bIncludeToStringStuffRecursive = false;
        boolean bRetainOrdering = false;
        boolean bInsertIncrementalInfo = false;
        boolean bIncrementalGeneration = false;

        int iArg = 0;
        while (args[iArg].startsWith("-"))
        {
            String sFlags = args[iArg].substring(1);
            for (int i = 0; i < sFlags.length(); i++)
            {
                if (sFlags.charAt(i) == 'q')
                    bQuiet = true;
                else if (sFlags.charAt(i) == 's')
                    bIncludeToStringStuff = true;
                else if (sFlags.charAt(i) == 'S')
                    bIncludeToStringStuffRecursive = true;
                else if (sFlags.charAt(i) == 'o')
                    bRetainOrdering = true;
                else if (sFlags.charAt(i) == 'I')
                    bInsertIncrementalInfo = true;
                else if (sFlags.charAt(i) == 'i')
                    bIncrementalGeneration = true;
                else
                    throw new Exception("RDFS2Class: Option '"+sFlags.charAt(i)+"' unknown.");
            }
            iArg++;
        }

        String sRDFSFile = null;
        String sOutputSrcDir = null;
        if (args.length - iArg > 0)
            sRDFSFile = args[iArg++];
        else
            throw new Exception("RDFS2Class: Missing parameter: RDFS-File.");

        if (args.length - iArg > 0)
            sOutputSrcDir = args[iArg++];
        else
            throw new Exception("RDFS2Class: Missing parameter: output directory.");

        if (args.length - iArg <= 0)
            throw new Exception("RDFS2Class: Missing mapping parameters.");
        if ( ((args.length - iArg) % 2) != 0 )
            throw new Exception("RDFS2Class: Mapping parameters not correct.");

        if (!bQuiet)
        {
            System.out.println("sRDFSFile     : "+sRDFSFile);
            System.out.println("sOutputSrcDir : "+sOutputSrcDir);
            System.out.println("maping:");
        }
        HashMap mapNS2Pkg = new HashMap();
        while (iArg < args.length)
        {
            String sNamespace = args[iArg++];
            String sPackage   = args[iArg++];
            if (!bQuiet)
                System.out.println("  " + sNamespace + " -> " + sPackage);
            mapNS2Pkg.put(sNamespace, sPackage);
        }
        if (!bQuiet)
            System.out.println();

        RDFS2Class gen = new RDFS2Class(sRDFSFile, sOutputSrcDir, mapNS2Pkg);
        gen.setQuiet(bQuiet);
        gen.setIncludeToStringStuff(bIncludeToStringStuff);
        gen.setIncludeToStringStuffRecursive(bIncludeToStringStuffRecursive);
        gen.setRetainOrdering(bRetainOrdering);
        gen.setInsertIncrementalInfo(bInsertIncrementalInfo);
        gen.setIncrementalGeneration(bIncrementalGeneration);
        gen.createClasses();
    }
    catch (Exception ex)
    {
        String sMsg = ex.getMessage();
        if (sMsg != null && sMsg.startsWith("RDFS2Class:"))
        {
            System.out.println( "\n" + sMsg + "\nusage: RDFS2Class [options] <file.rdfs> <outputSrcDir> {<namespace> <package>}+\n" +
                                "options:  -q: quiet operation, no output\n" +
                                "          -s: include toString()-stuff in generated java-files\n" +
                                "          -S: include recursive toString()-stuff in generated java-files\n" +
                                "              (used instead of -s)\n" +
                                "          -o: retain ordering of triples (usage of rdf:Seq in rdf-file)\n" +
                                "              by using arrays instead of sets\n" +
                                "          -I: insert stuff for incremental file-generation\n" +
                                "              (needed for potential later usage of -i)\n" +
                                "              ATTENTION: this option completely re-creates java-files and erases every\n" +
                                "              user-defined methods and slots, maybe you'd better use \"-i\" ? !\n" +
                                "          -i: incremental generation of java-files, i.e. user added slots\n" +
                                "              are kept in the re-generated java-files\n" +
                                "              (this option includes already -I)" );
           System.exit(0);
        }
        else
            debug().error(ex);
    }
}

//----------------------------------------------------------------------------------------------------
/** Starts the generation of the class files.
  * <br>
  * By now only umcompiled Java files for the classes are generated.
  * Hence you need to compile them later, before you can use Michael
  * Sintek's <code>rdf2java</code> tool.
  */
public void createClasses ()   throws Exception
{
    if (!m_bQuiet)
        System.out.println("loading in the RDFS file...");
    loadRDFS();

    if (!m_bQuiet)
        System.out.println("examining properties...");
    examineProperties();

    if (!m_bQuiet)
        System.out.println("creating the RDFS classes...");
    createTheClasses();

    if (!m_bQuiet)
        System.out.println("ready.");
}

//----------------------------------------------------------------------------------------------------
protected void loadRDFS ()   throws Exception
{
    m_modelRDFS = m_rdfFactory.createModel();
    RDF.parse("file:" + m_sRDFSFile, m_rdfParser, m_modelRDFS);
}

//----------------------------------------------------------------------------------------------------
protected void examineProperties ()   throws Exception
{
    m_setProperties = new HashSet();

    Model modelProperties = m_modelRDFS.find(null, m_resRDFPredType, m_resRDFResProperty);
    Enumeration enumProperties = modelProperties.elements();
    while (enumProperties.hasMoreElements())
    {
        Resource resProperty = ((Statement)enumProperties.nextElement()).subject();
        PropertyInfo pi = new PropertyInfo(resProperty);
        m_setProperties.add(pi);
    }
}

//----------------------------------------------------------------------------------------------------
protected void createTheClasses ()   throws Exception
{
    Model modelClasses = m_modelRDFS.find(null, m_resRDFPredType, m_resRDFSResCls);
    Enumeration enumClasses = modelClasses.elements();
    while (enumClasses.hasMoreElements())
    {
        Resource resCls = ((Statement)enumClasses.nextElement()).subject();
        createClass(resCls);
    }
}

//----------------------------------------------------------------------------------------------------
protected void createClass (Resource resCls)   throws Exception
{
    String sClsNS = resCls.getNamespace();
    String sClsName = resCls.getLocalName();
    if (!m_bQuiet)
        System.out.print(sClsNS + sClsName);

    String sPkg = (String)m_mapNamespaceToPackage.get(sClsNS);
    if (sPkg == null)
        throw new Exception("Namespace '"+sClsNS+"' not mapped to a package");
    String sPath = packageToPath(sPkg);
    String sFile = sClsName + ".java";
    if (!m_bQuiet)
        System.out.println(" -> " + sPath + "/" + sFile);

    PrintWriter pwClsFile = null;
    String sAbsolutePath = m_sClsPath + "/" + sPath;
    if (m_bIncrementalGeneration)
    {
        loadInFormerJavaFile(sAbsolutePath + "/" + sFile);
        m_bIncrementalGenerationForActualFile = !m_lstFormerFile.isEmpty();
    }
    else
    {
        m_bIncrementalGenerationForActualFile = false;
    }
    pwClsFile = createFile(sAbsolutePath, sFile);
    fillClassFile(resCls, sPkg, sClsName, pwClsFile);
    pwClsFile.close();
}

//----------------------------------------------------------------------------------------------------
protected void fillClassFile (Resource resCls, String sPkg, String sClsName, PrintWriter pwClsFile)
    throws Exception
{
    // subclassing (prepare)
    Resource resSuperClass = null;
    String sSuperClassNS = null;
    String sSuperClassPkg = null;
    String sSuperClassName = null;
    Model modelSuperClass = m_modelRDFS.find(resCls, m_resRDFSPredSubClassOf, null);
    if (modelSuperClass.isEmpty())
        throw new Exception("class '"+sPkg+"."+sClsName+"' has no super class");
    resSuperClass = (Resource)RDF.get1(modelSuperClass).object();
    // System.out.println("resSuperClass="+resSuperClass);
    if (resSuperClass.equals(m_resRDFSClass))
    {
        sSuperClassName = "dfki.rdf.util.THING";  //// = "Class";
        sSuperClassNS = null;
        sSuperClassPkg = null;  //// = "java.lang";
        System.out.println("# super class THING (not 'Class'!!) used for class '"+resCls+"'");
        ////System.out.println("# super class 'Class' used for class '"+resCls+"'");
    }
    else
    if (!resSuperClass.equals(m_resRDFSResource))
    {
        sSuperClassName = resSuperClass.getLocalName();
        sSuperClassNS = resSuperClass.getNamespace();
        sSuperClassPkg = (String)m_mapNamespaceToPackage.get(sSuperClassNS);
        if (sSuperClassPkg == null)
            throw new Exception("Namespace '"+sSuperClassNS+"' (class '"+sSuperClassName+"') mapped to a package");
    }

    // test if class is an abstract or a "concrete" class
    boolean bClassIsAbstract = false;
    Model modelClassRole = m_modelRDFS.find(resCls, m_resProtegePredRole, null);
    if (!modelClassRole.isEmpty())
    {
        RDFNode rdfnodeClassRole = RDF.get1(modelClassRole).object();
        if (rdfnodeClassRole.getLabel().equalsIgnoreCase( "abstract" ))
            bClassIsAbstract = true;
    }
    String sClassIsAbstractSpec = ( bClassIsAbstract  ?  "abstract "  :  " " );  // trailing space is needed!!!

    // start writing the file
    pwClsFile.println("package " + sPkg + ";");
    if (m_bIncrementalGenerationForActualFile)
        copyPartOfFormerFile_package_imports(pwClsFile);
    else
        pwClsFile.println();

    if (m_bInsertIncrementalInfo)
        pwClsFile.println("// RDFS2Class: imports");
    pwClsFile.println("import java.util.*;");
    if (sSuperClassPkg != null && !sPkg.equals(sSuperClassPkg))
        pwClsFile.println("import " + sSuperClassPkg + ".*;");
    if (m_bInsertIncrementalInfo)
        pwClsFile.println("// RDFS2Class: end of imports");

    if (m_bIncrementalGenerationForActualFile)
        copyPartOfFormerFile_imports_class(pwClsFile);
    else
        pwClsFile.println("\n");
    if (m_bInsertIncrementalInfo)
        pwClsFile.println("/** RDFS2Class: class " + sClsName + "\n  * <p>\n  */");
    pwClsFile.println("public " + sClassIsAbstractSpec + "class " + sClsName);

    // subclassing
    if (sSuperClassName != null)
        pwClsFile.println("    extends " + sSuperClassName);
    else
        pwClsFile.println("    extends dfki.rdf.util.THING");

/*
    hier fehlt auch das verwenden (import) der packages von den interface classes
    // implementing interfaces
    Set setInterfaces = getInterfacesForCls(resCls);
    if (!setInterfaces.isEmpty())
    {
        pwClsFile.print("    implements ");
        Iterator itInterfaces = setInterfaces.iterator();
        while (itInterfaces.hasNext())
        {
            String sInterface = (String)itInterfaces.next();
            pwClsFile.print(sInterface);
            if (itInterfaces.hasNext()) pwClsFile.print(", ");
        }
        pwClsFile.println();
    }
*/

    pwClsFile.println("{");

    if (m_bIncrementalGenerationForActualFile && m_bGenerateMethodsAboveUserMethods)
        copyPartOfFormerFile_in_class(pwClsFile);

    // slots / properties
    Set setProperties = getPropertiesOfClass(resCls);
    Iterator itProperties = setProperties.iterator();
    StringBuffer sbToStringStuff = new StringBuffer();
    String sIndent = "    ";
    String sSeparator = "//------------------------------------------------------------------------------";
    while (itProperties.hasNext())
    {
        pwClsFile.println(sIndent + sSeparator);
        PropertyInfo pi = (PropertyInfo)itProperties.next();
        if (m_bInsertIncrementalInfo)
            pwClsFile.println(sIndent + "/** RDFS2Class: slot " + pi.sSlotName + " **/");
        String sRangePkgAndCls = pi.sRangeCls;
        if (pi.sRangeClsPkg != null && !pi.sRangeClsPkg.equals(sPkg))
            sRangePkgAndCls = pi.sRangeClsPkg + "." + sRangePkgAndCls;
        if (pi.bMultiple)
        {
            pwClsFile.println(sIndent + m_sCollectionType + " m_" + pi.sSlotName + " = new " + m_sCollectionType + "();\n");

            if (m_bInsertIncrementalInfo)
                pwClsFile.println(sIndent + "/** RDFS2Class: putter for slot " + pi.sSlotName + " **/");
            pwClsFile.println(sIndent + "public void " + RDF2Java.makeMethodName("put", pi.sSlotName) + " (" + sRangePkgAndCls + " p_" + pi.sSlotName +")\n" +
                              sIndent + "{");
            if (pi.bNeedsRangeInterface)
            {
                pwClsFile.print(sIndent + "    if (");
                Iterator it = pi.setResRange.iterator();
                while (it.hasNext())
                {
                    Resource resRCls = (Resource)it.next();
                    String sRClsNS = resRCls.getNamespace();
                    String sRClsPkg = (String)m_mapNamespaceToPackage.get(sRClsNS);
                    String sRClsName = resRCls.getLocalName();
                    if (!sRClsPkg.equals(sPkg)) sRClsName = sRClsPkg + "." + sRClsName;
                    pwClsFile.print("!(p_"+pi.sSlotName+" instanceof "+sRClsName+")");
                    if (it.hasNext()) pwClsFile.print(" && ");
                }
                pwClsFile.println(")\n"+sIndent+"        throw new Error(\"not an allowed class\");");
            }
            pwClsFile.print(sIndent + "    m_" + pi.sSlotName + ".add(p_" + pi.sSlotName + ");\n" +
                            sIndent + "}\n");
            pwClsFile.print(sIndent + "public void " + RDF2Java.makeMethodName("put", pi.sSlotName) + " (dfki.rdf.util.RDFResource p_" + pi.sSlotName +")\n" +
                            sIndent + "{\n" +
                            sIndent + "    m_" + pi.sSlotName + ".add(p_" + pi.sSlotName + ");\n" +
                            sIndent + "}\n");
            pwClsFile.print(sIndent + "public void " + RDF2Java.makeMethodName("put", pi.sSlotName) + " (java.util.Collection p_" + pi.sSlotName +")\n" +
                            sIndent + "{\n" +
                            sIndent + "    m_" + pi.sSlotName + " = new " + m_sCollectionType + "(p_" + pi.sSlotName + ");\n" +
                            sIndent + "}\n");
            pwClsFile.print(sIndent + "public void " + RDF2Java.makeMethodName("clear", pi.sSlotName) + " ()\n" +
                            sIndent + "{\n" +
                            sIndent + "    m_" + pi.sSlotName + " = new " + m_sCollectionType + "();\n" +
                            sIndent + "}");
            if (m_bInsertIncrementalInfo)
                pwClsFile.print("\n" + sIndent + "// RDFS2Class: end of putter for slot " + pi.sSlotName);
            pwClsFile.println("\n");

            if (m_bInsertIncrementalInfo)
                pwClsFile.println(sIndent + "/** RDFS2Class: getter for slot " + pi.sSlotName + " **/");
            pwClsFile.print(sIndent + "public java.util.Collection " + RDF2Java.makeMethodName("get", pi.sSlotName) + " ()\n" +
                            sIndent + "{\n" +
                            sIndent + "    return m_" + pi.sSlotName + ";\n" +
                            sIndent + "}");
            if (m_bInsertIncrementalInfo)
                pwClsFile.print("\n" + sIndent + "// RDFS2Class: end of getter for slot " + pi.sSlotName);
            pwClsFile.println("\n");

            if (m_bIncludeToStringStuff)
            {
                // toString stuff
                String sPreShort     = ( m_bIncludeToStringStuffRecursive  ?  "// "  :  ""    );
                String sPreRecursive = ( m_bIncludeToStringStuffRecursive  ?  ""     :  "// " );
                sbToStringStuff.append(sIndent + "    if (!m_"+pi.sSlotName+".isEmpty()) {\n");
                sbToStringStuff.append(sIndent + "        sb.append(sIndent+\"-> "+pi.sSlotName+":\\n\");\n");
                sbToStringStuff.append(sIndent + "        for (Iterator it_" + pi.sSlotName + " = m_" + pi.sSlotName + ".iterator(); it_" + pi.sSlotName + ".hasNext(); ) {\n");
                if (sRangePkgAndCls.equals("String"))
                    sbToStringStuff.append(sIndent + "            sb.append( (String)it_" + pi.sSlotName + ".next() );\n");
                else
                    sbToStringStuff.append(sIndent + "            " + sPreShort     + "sb.append( sIndent+\"       \" + ((dfki.rdf.util.RDFResource)it_" + pi.sSlotName + ".next()).toStringShort() + \"\\n\" );\n" +
                                           sIndent + "            " + sPreRecursive + "sb.append( ((dfki.rdf.util.RDFResource)it_" + pi.sSlotName + ".next()).toString(sIndent+\"       \") );\n");
                sbToStringStuff.append(sIndent + "        }\n" +
                                       sIndent + "    }\n");
            }
        }
        else
        {
            ////2002.02.05:old: pwClsFile.println(sIndent + sRangePkgAndCls + " m_" + pi.sSlotName + ";");
            String sRealSlotType = ( sRangePkgAndCls.equals("String")  ?  "String"  :  "dfki.rdf.util.RDFResource" );
            pwClsFile.println(sIndent + sRealSlotType + " m_" + pi.sSlotName + ";\n");

            if (m_bInsertIncrementalInfo)
                pwClsFile.println(sIndent + "/** RDFS2Class: putter for slot " + pi.sSlotName + " **/");
            pwClsFile.println(sIndent + "public void " + RDF2Java.makeMethodName("put", pi.sSlotName) + " (" + sRangePkgAndCls + " p_" + pi.sSlotName +")\n" +
                              sIndent + "{");
            if (pi.bNeedsRangeInterface)
            {
                pwClsFile.print(sIndent + "    if (");
                Iterator it = pi.setResRange.iterator();
                while (it.hasNext())
                {
                    Resource resRCls = (Resource)it.next();
                    String sRClsNS = resRCls.getNamespace();
                    String sRClsPkg = (String)m_mapNamespaceToPackage.get(sRClsNS);
                    String sRClsName = resRCls.getLocalName();
                    if (!sRClsPkg.equals(sPkg)) sRClsName = sRClsPkg + "." + sRClsName;
                    pwClsFile.print("!(p_"+pi.sSlotName+" instanceof "+sRClsName+")");
                    if (it.hasNext()) pwClsFile.print(" && ");
                }
                pwClsFile.println(")\n"+sIndent+"        throw new Error(\"not an allowed class\");");
            }
            pwClsFile.print(sIndent + "    m_" + pi.sSlotName + " = p_" + pi.sSlotName + ";\n" +
                            sIndent + "}");
            // second putter (URI)
            if (!sRealSlotType.equals("String"))
            {
                pwClsFile.print("\n" + sIndent + "public void " + RDF2Java.makeMethodName("put", pi.sSlotName) + " (dfki.rdf.util.RDFResource p_" + pi.sSlotName +")\n" +
                                sIndent + "{\n" +
                                sIndent + "    m_" + pi.sSlotName + " = p_" + pi.sSlotName + ";\n" +
                                sIndent + "}");
            }
            if (m_bInsertIncrementalInfo)
                pwClsFile.print("\n" + sIndent + "// RDFS2Class: end of putter for slot " + pi.sSlotName);
            pwClsFile.println("\n");

            if (m_bInsertIncrementalInfo)
                pwClsFile.println(sIndent + "/** RDFS2Class: getter for slot " + pi.sSlotName + " **/");
            if (!sRealSlotType.equals("String"))
            {
                pwClsFile.print(sIndent + "public " + sRangePkgAndCls + " " + RDF2Java.makeMethodName("Get", pi.sSlotName) + " ()\n" +
                                sIndent + "{\n" +
                                sIndent + "    return (" + sRangePkgAndCls + ")m_" + pi.sSlotName + ";\n" +
                                sIndent + "}\n");
            }
            pwClsFile.print(sIndent + "public " + sRealSlotType + " " + RDF2Java.makeMethodName("get", pi.sSlotName) + " ()\n" +
                            sIndent + "{\n" +
                            sIndent + "    return m_" + pi.sSlotName + ";\n" +
                            sIndent + "}");
            if (m_bInsertIncrementalInfo)
                pwClsFile.print("\n" + sIndent + "// RDFS2Class: end of getter for slot " + pi.sSlotName);
            pwClsFile.println("\n");

            if (m_bIncludeToStringStuff)
            {
                // toString stuff
                String sPreShort     = ( m_bIncludeToStringStuffRecursive  ?  "// "  :  ""    );
                String sPreRecursive = ( m_bIncludeToStringStuffRecursive  ?  ""     :  "// " );
                sbToStringStuff.append(sIndent + "    if (m_"+pi.sSlotName+" != null) {\n");
                if (sRangePkgAndCls.equals("String"))  //FIXME: hier noch nach "int" etc. testen!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    sbToStringStuff.append(sIndent + "        sb.append(sIndent+\"-> "+pi.sSlotName+": \"+m_"+pi.sSlotName+"+\"\\n\");\n");
                else
                {
                    sbToStringStuff.append(sIndent + "        " + sPreShort     + "sb.append(sIndent+\"-> "+pi.sSlotName+":\\n\"+sIndent+\"       \"+m_"+pi.sSlotName+".toStringShort()+\"\\n\");\n");
                    sbToStringStuff.append(sIndent + "        " + sPreRecursive + "sb.append(sIndent+\"-> "+pi.sSlotName+":\\n\"+m_"+pi.sSlotName+".toString(sIndent+\"       \"));\n");
                }
                sbToStringStuff.append(sIndent + "    }\n");
            }
        }
    }

    if (m_bIncludeToStringStuff && sbToStringStuff.length() > 0)
    {
        // toString stuff
        pwClsFile.println(sIndent + sSeparator);
        if (m_bInsertIncrementalInfo)
            pwClsFile.println(sIndent + "/** RDFS2Class: toString()-stuff **/");
        pwClsFile.println(sIndent + "public void toString (StringBuffer sb, String sIndent)\n" +
                          sIndent + "{\n" +
                          sIndent + "    super.toString(sb, sIndent);");
        pwClsFile.print(sbToStringStuff.toString());
        pwClsFile.println(sIndent + "}");
        if (m_bInsertIncrementalInfo)
            pwClsFile.println(sIndent + "// RDFS2Class: end of toString()-stuff");
        pwClsFile.println();
    }

    if (m_bIncrementalGenerationForActualFile && !m_bGenerateMethodsAboveUserMethods)
        copyPartOfFormerFile_in_class(pwClsFile);

    pwClsFile.println("}");
        if (m_bInsertIncrementalInfo)
            pwClsFile.println("// RDFS2Class: end of class " + sClsName);

    if (m_bIncrementalGenerationForActualFile && !m_bGenerateMethodsAboveUserMethods)
        copyPartOfFormerFile_class_EOF(pwClsFile);

    pwClsFile.println("// EOF\n");
}

//----------------------------------------------------------------------------------------------------
protected String packageToPath (String sPkg)
{
    StringBuffer sbPath = new StringBuffer();
    int left = 0;
    int pos;
    while (true)
    {
        pos = sPkg.indexOf('.', left);
        if (pos < 0) break;
        sbPath.append(sPkg.substring(left, pos) + "/");
        left = pos + 1;
    }
    if (left < sPkg.length())
        sbPath.append(sPkg.substring(left));
    return sbPath.toString();
}

//----------------------------------------------------------------------------------------------------
protected PrintWriter createFile (String sPath, String sFile)   throws Exception
{
    File fPath = new File(sPath);
    File fFile = new File(sPath + "/" + sFile);
    fPath.mkdirs();
    fFile.createNewFile();
    FileWriter fw = new FileWriter(fFile);
    PrintWriter pw = new PrintWriter(fw);
    return pw;
}

//----------------------------------------------------------------------------------------------------
protected Set objectsToSet (Model modelStatements)   throws Exception
{
    HashSet hs = new HashSet();
    Enumeration enum = modelStatements.elements();
    while (enum.hasMoreElements())
    {
        Statement st = (Statement)enum.nextElement();
        hs.add(st.object());
    }
    return hs;
}

//----------------------------------------------------------------------------------------------------
protected Set getPropertiesOfClass (Resource resCls)
{
    HashSet hs = new HashSet();
    Iterator it = m_setProperties.iterator();
    while (it.hasNext())
    {
        PropertyInfo pi = (PropertyInfo)it.next();
        if (pi.setResDomain.contains(resCls))
            hs.add(pi);
    }
    return hs;
}

//----------------------------------------------------------------------------------------------------
protected Set getInterfacesForCls (Resource resCls)
{
    HashSet hs = new HashSet();
    Iterator it = m_setProperties.iterator();
    while (it.hasNext())
    {
        PropertyInfo pi = (PropertyInfo)it.next();
        if (pi.bNeedsRangeInterface && pi.setResRange.contains(resCls))
            hs.add(pi.sRangeCls);
    }
    return hs;
}




//----------------------------------------------------------------------------------------------------
protected void loadInFormerJavaFile (String sPathAndFilename)   throws Exception
{
    m_lstFormerFile = new LinkedList();
    BufferedReader br = null;
    try { br = new BufferedReader(new FileReader(sPathAndFilename)); }
    catch (java.io.FileNotFoundException ex) { return; }
    while (br.ready())
    {
        String sLine = br.readLine();
        m_lstFormerFile.add(sLine);
    }
}

//----------------------------------------------------------------------------------------------------
public void copyPartOfFormerFile_package_imports (PrintWriter pwClsFile)
{
    ListIterator it = m_lstFormerFile.listIterator();
    while (it.hasNext())
    {
        String sLine = (String)it.next();
        if (sLine != null && sLine.startsWith("package"))
            break;
    }
    while (it.hasNext())
    {
        String sLine = (String)it.next();
        if (sLine != null && sLine.trim().startsWith("// RDFS2Class: imports"))
            break;
        else
            pwClsFile.println(sLine);
    }
}

//----------------------------------------------------------------------------------------------------
public void copyPartOfFormerFile_imports_class (PrintWriter pwClsFile)
{
    ListIterator it = m_lstFormerFile.listIterator();
    while (it.hasNext())
    {
        String sLine = (String)it.next();
        if (sLine != null && sLine.startsWith("// RDFS2Class: end of imports"))
            break;
    }
    while (it.hasNext())
    {
        String sLine = (String)it.next();
        if (sLine != null && sLine.trim().startsWith("/** RDFS2Class: class"))
            break;
        else
            pwClsFile.println(sLine);
    }
}

//----------------------------------------------------------------------------------------------------
public void copyPartOfFormerFile_in_class (PrintWriter pwClsFile)
{
    Vector vLines = new Vector();
    ListIterator it = m_lstFormerFile.listIterator();
    String sLine;
    String sLastLine;
    while (it.hasNext())
    {
        sLine = (String)it.next();
        if (sLine != null && sLine.startsWith("/** RDFS2Class: class"))
            break;
    }
    while (it.hasNext())
    {
        sLine = (String)it.next();
        if (sLine != null && sLine.indexOf('{') >= 0)
            break;
    }
    sLastLine = null;
    while (it.hasNext())
    {
        sLine = (String)it.next();
        if (sLine == null) continue;
        if (sLine.trim().startsWith("// RDFS2Class: end of class"))
            break;

        if (sLine.trim().startsWith("/** RDFS2Class: slot"))
        {
            if (it.hasNext())
                sLine = (String)it.next();  // over-read next line
            if (it.hasNext())
                sLine = (String)it.next();  // over-read next line (2nd time now)
            if (sLastLine != null && !sLastLine.trim().equals("//------------------------------------------------------------------------------")
                                  && sLastLine.length() != 0)
                vLines.add(sLastLine);
            sLastLine = null;
        }
        else
        if (sLine.trim().startsWith("/** RDFS2Class:"))
        {
            while (it.hasNext())
            {
                sLine = (String)it.next();
                if (sLine != null && sLine.trim().startsWith("// RDFS2Class:"))
                    break;
            }
            if (sLastLine != null && !sLastLine.trim().equals("//------------------------------------------------------------------------------")
                                  && sLastLine.length() != 0)
                vLines.add(sLastLine);
            sLastLine = null;
        }
        else
        {
            if (sLastLine != null)
                vLines.add(sLastLine);

//            sLastLine = null;
            sLastLine = sLine;
        }
    }

    // cut off empty lines at beginning of vLines
    while (!vLines.isEmpty())
    {
        String str = (String)vLines.get(0);
        if (str == null || str.length() == 0 || str.startsWith("\n"))
            vLines.remove(0);
        else
            break;
    }
    // cut off empty lines at end of vLines
    while (!vLines.isEmpty())
    {
        String str = (String)vLines.get(vLines.size()-1);
        if (str == null || str.length() == 0 || str.startsWith("\n"))
            vLines.remove(vLines.size()-1);
        else
            break;
    }
    // print out vLines
    Iterator it2 = vLines.iterator();
    while (it2.hasNext())
    {
        pwClsFile.println((String)it2.next());
    }
}

//----------------------------------------------------------------------------------------------------
public void copyPartOfFormerFile_class_EOF (PrintWriter pwClsFile)
{
    ListIterator it = m_lstFormerFile.listIterator();
    while (it.hasNext())
    {
        String sLine = (String)it.next();
        if (sLine != null && sLine.startsWith("// RDFS2Class: end of class"))
            break;
    }
    while (it.hasNext())
    {
        String sLine = (String)it.next();
        if (sLine == null) continue;
        if (sLine.trim().startsWith("// EOF"))
            break;
        else
            pwClsFile.println(sLine);
    }
}



//----------------------------------------------------------------------------------------------------
private static Debug debug ()
{
    return Debug.forModule("RDFS2Class");
}


//----------------------------------------------------------------------------------------------------

    class PropertyInfo
    {
        Resource resProperty;
        String sSlotPkg;
        String sSlotName;
        Set setResDomain;
        Set setResRange;
        String sRangeCls;
        String sRangeClsPkg;
        boolean bMultiple;
        int iMaxCardinality;
        boolean bNeedsRangeInterface;
        String sRangeInterfaceClsPkg;
        String sRangeInterfaceClsName;

        PropertyInfo (Resource _resProperty)   throws Exception
        {
            resProperty = _resProperty;
            String sSlotNS = resProperty.getNamespace();
            sSlotPkg = (String)m_mapNamespaceToPackage.get(sSlotNS);
            sSlotName = resProperty.getLocalName();
            Model modelDomain = m_modelRDFS.find(resProperty, m_resRDFSPredDomain, null);
            setResDomain = objectsToSet(modelDomain);
            Model modelRange = m_modelRDFS.find(resProperty, m_resRDFSPredRange, null);
            setResRange = objectsToSet(modelRange);
            RDFNode rdfnodeMaxCardinality = RDF.getObject(m_modelRDFS, resProperty, m_resProtegeMaxCardinality);
            if (rdfnodeMaxCardinality != null)
            {
                iMaxCardinality = Integer.parseInt(((Literal)rdfnodeMaxCardinality).getLabel());
                bMultiple = (iMaxCardinality > 1);
            }
            else
            {
                iMaxCardinality = -1;
                bMultiple = true;
            }
            if (!setResRange.isEmpty())
            {
                RDFNode rdfnodeRange = (RDFNode)setResRange.iterator().next();
                // System.out.println("# "+sSlotName+" -> "+rdfnodeRange);
                if (rdfnodeRange.equals(m_resRDFSResource))
                {
                    // System.out.println("# "+sSlotName+" -> "+rdfnodeRange);
                    Model modelAllowedClasses = m_modelRDFS.find(resProperty, m_resProtegeAllowedClasses, null);
                    setResRange = objectsToSet(modelAllowedClasses);
                }
            }
            if (setResRange.size() > 1)
            {
                sRangeCls = "dfki.rdf.util.THING";  // :THING in protege
                sRangeClsPkg = null;
                bNeedsRangeInterface = true;
/*
                sRangeInterfaceClsPkg = sSlotPkg;
                sRangeCls = "RangeInterface_" + sSlotName;
                sRangeClsPkg = sRangeInterfaceClsPkg;
                sRangeInterfaceClsName = sRangeCls + ".java";
                createRangeInterface();
*/
            }
            else
            {
                if (setResRange.isEmpty())
                {
                    sRangeCls = "dfki.rdf.util.THING";  // :THING in protege
                    sRangeClsPkg = null;
                }
                else
                {
                    RDFNode rdfnodeRange = (RDFNode)setResRange.iterator().next();
                    if (rdfnodeRange.equals(m_resRDFSLiteral))
                    {
                        RDFNode rdfnodeLiteralType = RDF.getObject(m_modelRDFS, resProperty, m_resProtegeRange);
                        sRangeCls = "String";
/*
                        sRangeCls = null;
                        if (rdfnodeLiteralType != null)
                        {
                            String sLiteralType = rdfnodeLiteralType.getLabel();
                            if (sLiteralType.equals("integer"))
                                sRangeCls = "int";
                        }
                        if (sRangeCls == null)
                            sRangeCls = "String";
*/
                        sRangeClsPkg = null;
                    }
                    else
                    if (rdfnodeRange.equals(m_resRDFSClass))
                    {
                        sRangeCls = "Class";
                        sRangeClsPkg = null;
                        // sRangeCls = "THING";
                        // sRangeClsPkg = "dfki.rdf.util";
                    }
                    else
                    {
                        sRangeCls = ((Resource)rdfnodeRange).getLocalName();
                        String sRangeClsNS = ((Resource)rdfnodeRange).getNamespace();
                        //// System.out.println("### slot " + sSlotName + " -> " + sRangeClsNS + " . " + sRangeCls);
                        sRangeClsPkg = (String)m_mapNamespaceToPackage.get(sRangeClsNS);
                    }
                }
                bNeedsRangeInterface = false;
                sRangeInterfaceClsPkg = null;
                sRangeInterfaceClsName = null;
            }
        }

        void createRangeInterface ()   throws Exception
        {
            String sRangeInterfaceClsPath = packageToPath(sRangeInterfaceClsPkg);
            PrintWriter pw = createFile(m_sClsPath + "/" + sRangeInterfaceClsPath, sRangeInterfaceClsName);
            pw.println("package " + sRangeInterfaceClsPkg + ";\n\n");
            pw.println("public interface " + sRangeCls);
            pw.println("{\n}\n");
            pw.close();
        }
    }


//----------------------------------------------------------------------------------------------------
}

