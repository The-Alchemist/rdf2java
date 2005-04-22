package de.dfki.rdf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.w3c.rdf.model.Literal;
import org.w3c.rdf.model.Model;
import org.w3c.rdf.model.NodeFactory;
import org.w3c.rdf.model.RDFNode;
import org.w3c.rdf.model.Resource;
import org.w3c.rdf.model.Statement;
import org.w3c.rdf.syntax.RDFParser;
import org.w3c.rdf.util.RDFFactory;

import de.dfki.util.debug.Debug;
import de.dfki.util.rdf.RDF;

/** 
 * The <code>RDFS2Class</code> tool.<br>
 * This tool generates Java Classes according to a given RDFS-file.
 * <p>
 * By now only uncompiled Java files for the classes are generated.
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
 *           -S: (used instead of -s) include recursive toString()-stuff in generated java-files<br>
 *           -s({r}{p}): specifies further toString options:<br>
 *               r: output in RDF format<br>
 *               p: output in packed format (simpler + easier to read)<br>
 *               s: follow to subgraphs = recursion turned ON<br>
 *               example: s(rs) results in a hierarchical, recursive RDF output<br>
 *               if there's no 'r' and no 'p', then 'p' is assumed as default<br>
 *           -S: include recursive toString()-stuff in generated java-files<br>
 *               (used instead of -s)<br>
 *           -S({r}{p}): specifies further toString options see s(...) above<br>
 *           -o: retain ordering of triples (usage of rdf:Seq in rdf-file)<br>
 *               by using arrays instead of sets<br>
 *           -I: insert stuff for incremental file-generation
 *               (needed for potential later usage of -i)<br>
 *           -i: incremental generation of java-files, i.e. user added slots<br>
 *               are kept in the re-generated java-files (this option includes already -I)
 *           Order of the following options is important!<br>
 *           rdfs=&lt;namespace&gt;    : set different RDFS namespace<br>
 *           rdf=&lt;namespace&gt;     : set different RDF namespace<br>
 *           protege=&lt;namespace&gt; : set different Protege namespace<br>
 * </code>
 * <p>
 * Of course you can also use this class and its public methods.
 * <p>
 * @author  Sven.Schwarz@dfki.de
 * @version 1.4.4
 */
public class RDFS2Class
{
//----------------------------------------------------------------------------------------------------
static String RDFS_NAMESPACE          = "http://www.w3.org/2000/01/rdf-schema#";
static String RDF_NAMESPACE           = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
static boolean PRINTOUT_RDFS_URIS     = false;

static String XML_SCHEMA_NAMESPACE    = "http://www.w3.org/2001/XMLSchema-datatypes#";

static String PROTEGE_NS              = "http://protege.stanford.edu/system#";
static boolean PRINTOUT_PROTEGE_URIS  = false;

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

String m_sIncludeToStringStuffOptions = "";
final static public char TOSTRING_PACKED        = 'p';      // opposite of RDF
final static public char TOSTRING_RDF           = 'r';      // opposite of PACKED
final static public char TOSTRING_RECURSIVE     = 's';

RDFFactory m_rdfFactory;
RDFParser m_rdfParser;
NodeFactory m_nodeFactory;
Resource m_resRDFSClass;
Resource m_resRDFSResource;
Resource m_resRDFSLiteral;
Resource m_resXMLDateTime;
Resource m_resRDFSPredSubClassOf;
Resource m_resRDFSPredDomain;
Resource m_resRDFSPredRange;
Resource m_resRDFPredType;
Resource m_resRDFResProperty;
Resource m_resProtegeMaxCardinality;
Resource m_resProtegeAllowedClasses;
Resource m_resProtegeAllowedSymbols;
Resource m_resProtegeDefaultValues;
Resource m_resProtegeRange;
Resource m_resProtegePredRole;


LinkedList m_lstFormerFile;  // list of strings (lines) of the former java-file
boolean m_bIncrementalGenerationForActualFile;

final static String LIST_CLASS = "java.util.LinkedList";
final static String SET_CLASS  = "java.util.HashSet";

private static int m_iNrMessages = 0;
private static int m_iNrWarnings = 0;
private static int m_iNrErrors = 0;
private static int m_iNrClassesCreated = 0;


//----------------------------------------------------------------------------------------------------
/** 
 * Usage (the given String <code>args</code> should be like everything after "RDFS2Class"):<br>
 * <code>
 * RDFS2Class [options] &lt;file.rdfs&gt; &lt;outputSrcDir&gt; {&lt;namespace&gt; &lt;package&gt;}+<br>
 * options:<br>
 *           -q: quiet operation, no output<br>
 *           -s: include toString()-stuff in generated java-files<br>
 *           -S: (used instead of -s) include recursive toString()-stuff in generated java-files<br>
 *           -s({r}{p}): specifies further toString options:<br>
 *               r: output in RDF format<br>
 *               p: output in packed format (simpler + easier to read)<br>
 *               s: follow to subgraphs = recursion turned ON<br>
 *               example: s(rs) results in a hierarchical, recursive RDF output<br>
 *               if there's no 'r' and no 'p', then 'p' is assumed as default<br>
 *           -S: include recursive toString()-stuff in generated java-files<br>
 *               (used instead of -s)<br>
 *           -S({r}{p}): specifies further toString options see s(...) above<br>
 *           -o: retain ordering of triples (usage of rdf:Seq in rdf-file)<br>
 *               by using arrays instead of sets<br>
 *           -I: insert stuff for incremental file-generation
 *               (needed for potential later usage of -i)<br>
 *           -i: incremental generation of java-files, i.e. user added slots<br>
 *               are kept in the re-generated java-files (this option includes already -I)
 *           Order of the following options is important!<br>
 *           rdfs=&lt;namespace&gt;    : set different RDFS namespace<br>
 *           rdf=&lt;namespace&gt;     : set different RDF namespace<br>
 *           protege=&lt;namespace&gt; : set different Protege namespace<br>
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
       
       String sIncludeToStringStuffOptions = "";

       int iArg = 0;
       while (args[iArg].startsWith("-"))
       {
           String sFlags = args[iArg].substring(1);
           for (int i = 0; i < sFlags.length(); i++)
           {
               if (sFlags.charAt(i) == 'q')
                   bQuiet = true;
               else if (sFlags.charAt(i) == 's')
               {
                   bIncludeToStringStuff = true;
                   if( sFlags.length() > i+1  &&  sFlags.charAt(i+1) == '(' )
                   {
                       int j = sFlags.indexOf( ')', i+2 );
                       if( j < 0 )
                           throw new Exception("RDFS2Class: Closing bracket missing for option "+sFlags.charAt(i)+"(...");
                       sIncludeToStringStuffOptions = sFlags.substring( i+2, j );
                       i = j;
                   }
               }
               else if (sFlags.charAt(i) == 'S')
               {
                   bIncludeToStringStuffRecursive = true;
                   if( sFlags.length() > i+1  &&  sFlags.charAt(i+1) == '(' )
                   {
                       int j = sFlags.indexOf( ')', i+2 );
                       if( j < 0 )
                           throw new Exception("RDFS2Class: Closing bracket missing for option "+sFlags.charAt(i)+"(...");
                       sIncludeToStringStuffOptions = sFlags.substring( i+2, j );
                       i = j;
                   }
                   if( sIncludeToStringStuffOptions.indexOf( TOSTRING_RECURSIVE ) < 0 )
                       sIncludeToStringStuffOptions += TOSTRING_RECURSIVE;
               }
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
       
       final String CHANGE_RDFS_NS = "rdfs=";
       if( args[iArg].toLowerCase().startsWith( CHANGE_RDFS_NS ) )
       {
           RDFS_NAMESPACE = args[ iArg++ ].substring( CHANGE_RDFS_NS.length() );
           System.out.println( "RDFS namespace: " + RDFS_NAMESPACE );
       }

       final String CHANGE_RDF_NS = "rdf=";
       if( args[iArg].toLowerCase().startsWith( CHANGE_RDF_NS ) )
       {
           RDF_NAMESPACE = args[ iArg++ ].substring( CHANGE_RDF_NS.length() );
           System.out.println( "RDF namespace : " + RDF_NAMESPACE );
       }

       final String CHANGE_PROTEGE_NS = "protege=";
       if( args[iArg].toLowerCase().startsWith( CHANGE_PROTEGE_NS ) )
       {
           PROTEGE_NS = args[ iArg++ ].substring( CHANGE_PROTEGE_NS.length() );
           System.out.println( "Protege namespace: " + PROTEGE_NS );
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
           message("sRDFSFile     : "+sRDFSFile);
           message("sOutputSrcDir : "+sOutputSrcDir);
           message("Namespace mappings:");
       }
       HashMap mapNS2Pkg = new HashMap();
       while (iArg < args.length)
       {
           String sNamespace = args[iArg++];
           String sPackage   = args[iArg++];
           if (!bQuiet)
               message("  " + sNamespace + " -> " + sPackage);
           mapNS2Pkg.put(sNamespace, sPackage);
       }
       if (!bQuiet)
           message("");

       RDFS2Class gen = new RDFS2Class(sRDFSFile, sOutputSrcDir, mapNS2Pkg);
       gen.setQuiet(bQuiet);
       gen.setIncludeToStringStuff(bIncludeToStringStuff);
       gen.setIncludeToStringStuffRecursive(bIncludeToStringStuffRecursive);
       gen.m_sIncludeToStringStuffOptions = sIncludeToStringStuffOptions;       //TODO: nice this!
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
                               "          -s({r}{p}): specifies further toString options:\n" +
                               "              r: output in RDF format\n" +
                               "              p: output in packed format (simpler + easier to read)\n" +
                               "              s: follow to subgraphs = recursion turned ON\n" +
                               "              example: s(rs) results in a hierarchical, recursive RDF output\n" +
                               "              if there's no 'r' and no 'p', then 'p' is assumed as default\n" +
                               "          -S: include recursive toString()-stuff in generated java-files\n" +
                               "              (used instead of -s)\n" +
                               "          -S({r}{p}): specifies further toString options see s(...) above\n" +
                               "          -o: retain ordering of triples (usage of rdf:Seq in rdf-file)\n" +
                               "              by using arrays instead of sets\n" +
                               "          -I: insert stuff for incremental file-generation\n" +
                               "              (needed for potential later usage of -i)\n" +
                               "              ATTENTION: this option completely re-creates java-files and erases every\n" +
                               "              user-defined methods and slots, maybe you'd better use \"-i\" ? !\n" +
                               "          -i: incremental generation of java-files, i.e. user added slots\n" +
                               "              are kept in the re-generated java-files\n" +
                               "          Order of the following options is important!\n" +
                               "          rdfs=<namespace>    : set different RDFS namespace\n" +
                               "          rdf=<namespace>     : set different RDF namespace\n" +
                               "          protege=<namespace> : set different Protege namespace\n" );
          // System.exit(0);
       }
       else
           debug().error(ex);
   }

   // print out #warnings and #errors
   System.out.println();
   if( m_iNrWarnings > 0 )
       System.out.println( "" + m_iNrWarnings + " warnings" );
   if( m_iNrErrors > 0 )
       System.out.println( "" + m_iNrErrors + " errors" );
   if( m_iNrWarnings <= 0 && m_iNrErrors <= 0 )
       System.out.println( "ok (no warnings, no errors)" );
   System.out.println( "" + m_iNrClassesCreated + " classes created" + (m_iNrClassesCreated == 0 ? " - check RDF/RDFS namespace settings" : "") );
   System.out.println();

   //// System.exit(0);
}

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
    
    initRdfsUris();
    if( PRINTOUT_RDFS_URIS ) printoutRdfsUris();

    initProtegeUris();
    if( PRINTOUT_PROTEGE_URIS ) printoutProtegeUris();

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
protected void initRdfsUris()   throws Exception
{
    m_resRDFSClass          = m_nodeFactory.createResource( RDFS_NAMESPACE + "Class"        );
    m_resRDFSResource       = m_nodeFactory.createResource( RDFS_NAMESPACE + "Resource"     );
    m_resRDFSLiteral        = m_nodeFactory.createResource( RDFS_NAMESPACE + "Literal"      );
    m_resXMLDateTime        = m_nodeFactory.createResource( XML_SCHEMA_NAMESPACE + "dateTime"      );
    
    m_resRDFSPredSubClassOf = m_nodeFactory.createResource( RDFS_NAMESPACE + "subClassOf"   );
    m_resRDFSPredDomain     = m_nodeFactory.createResource( RDFS_NAMESPACE + "domain"       );
    m_resRDFSPredRange      = m_nodeFactory.createResource( RDFS_NAMESPACE + "range"        );
    
    m_resRDFPredType        = m_nodeFactory.createResource( RDF_NAMESPACE  + "type"         );
    m_resRDFResProperty     = m_nodeFactory.createResource( RDF_NAMESPACE  + "Property"     );
}

//----------------------------------------------------------------------------------------------------
protected void printoutRdfsUris()
{
    System.out.println( "RDF/S URIs are as follows:\n" );
    System.out.println( "m_resRDFSClass          = \"" + m_resRDFSClass          + "\";" );
    System.out.println( "m_resRDFSResource       = \"" + m_resRDFSResource       + "\";" );
    System.out.println( "m_resRDFSLiteral        = \"" + m_resRDFSLiteral        + "\";" );
    System.out.println( "m_resRDFSPredSubClassOf = \"" + m_resRDFSPredSubClassOf + "\";" );
    System.out.println( "m_resRDFSPredDomain     = \"" + m_resRDFSPredDomain     + "\";" );
    System.out.println( "m_resRDFSPredRange      = \"" + m_resRDFSPredRange      + "\";" );
    System.out.println( "m_resRDFPredType        = \"" + m_resRDFPredType        + "\";" );
    System.out.println( "m_resRDFResProperty     = \"" + m_resRDFResProperty     + "\";" );
    System.out.println();
}

//----------------------------------------------------------------------------------------------------
protected void initProtegeUris()   throws Exception
{
    m_resProtegeMaxCardinality = m_nodeFactory.createResource( PROTEGE_NS, "maxCardinality" );
    m_resProtegeAllowedClasses = m_nodeFactory.createResource( PROTEGE_NS, "allowedClasses" );
    m_resProtegeAllowedSymbols = m_nodeFactory.createResource( PROTEGE_NS, "allowedValues"  );
    m_resProtegeDefaultValues  = m_nodeFactory.createResource( PROTEGE_NS, "defaultValues"  );
    m_resProtegePredRole       = m_nodeFactory.createResource( PROTEGE_NS, "role"           );
    m_resProtegeRange          = m_nodeFactory.createResource( PROTEGE_NS, "range"          );
}

//----------------------------------------------------------------------------------------------------
protected void printoutProtegeUris()
{
    System.out.println( "Protege URIs are as follows:\n" );
    System.out.println( "m_resProtegeMaxCardinality = \"" + m_resProtegeMaxCardinality  + "\";" );
    System.out.println( "m_resProtegeAllowedClasses = \"" + m_resProtegeAllowedClasses  + "\";" );
    System.out.println( "m_resProtegeAllowedSymbols = \"" + m_resProtegeAllowedSymbols  + "\";" );
    System.out.println( "m_resProtegeDefaultValues  = \"" + m_resProtegeDefaultValues   + "\";" );
    System.out.println( "m_resProtegePredRole       = \"" + m_resProtegePredRole        + "\";" );
    System.out.println( "m_resProtegeRange          = \"" + m_resProtegeRange           + "\";" );
    System.out.println();
}

//----------------------------------------------------------------------------------------------------
private static void message( String s )
{
    System.out.println( s );
    m_iNrMessages++;
}

//----------------------------------------------------------------------------------------------------
private static void warning( String s )
{
    System.out.println( "WARNING: " + s );
    m_iNrWarnings++;
}

//----------------------------------------------------------------------------------------------------
private static void error( String s )
{
    System.out.println( "ERROR: " + s );
    m_iNrErrors++;
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
/** Starts the generation of the class files.
  * <br>
  * By now only umcompiled Java files for the classes are generated.
  * Hence you need to compile them later, before you can use Michael
  * Sintek's <code>rdf2java</code> tool.
  */
public void createClasses ()   throws Exception
{
    if (!m_bQuiet)
        message("loading in the RDFS file...");
    loadRDFS();

    if (!m_bQuiet)
        message("examining properties...");
    examineProperties();

    if (!m_bQuiet)
        message("creating the RDFS classes...");
    createTheClasses();

    if (!m_bQuiet)
        message("ready.");
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
    Model modelClasses = m_modelRDFS.find(null, m_resRDFPredType, m_resRDFSClass);
    Enumeration enumClasses = modelClasses.elements();
    while (enumClasses.hasMoreElements())
    {
        Resource resCls = ((Statement)enumClasses.nextElement()).subject();
        createClass(resCls);
		m_iNrClassesCreated++;
    }
}

//----------------------------------------------------------------------------------------------------
protected void createClass (Resource resCls)   throws Exception
{
    if( resCls.getURI().equals( m_resXMLDateTime.getURI() ) )
        return;  //ss:2004-11-22: omitting xml schema classes
    
    String sClsNS = resCls.getNamespace();
    String sClsName = resCls.getLocalName();
    if (!m_bQuiet)
        message(sClsNS + sClsName);    

    String sPkg = (String)m_mapNamespaceToPackage.get(sClsNS);
    if (sPkg == null)
        throw new Exception("Namespace '"+sClsNS+"' not mapped to a package");
    String sPath = packageToPath(sPkg);
    String sFile = sClsName + ".java";
    if (!m_bQuiet)
        message(" -> " + sPath + "/" + sFile);

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
    {
        resSuperClass = null;
        sSuperClassName = "de.dfki.rdf.util.THING";   // or, maybe, better:   de.dfki.rdf.util.RDFResource
        sSuperClassNS = null;
        sSuperClassPkg = null;
        //SS.2003-03-14: throw new Exception("class '"+sPkg+"."+sClsName+"' has no super class");
    }
    else
    {
        resSuperClass = (Resource)RDF.get1(modelSuperClass).object();

        // message("resSuperClass="+resSuperClass);
        if (resSuperClass.equals(m_resRDFSClass))
        {
            sSuperClassName = "de.dfki.rdf.util.THING";  //// = "Class";
            sSuperClassNS = null;
            sSuperClassPkg = null;  //// = "java.lang";
            warning("# super class THING (not 'Class'!!) used for class '"+resCls+"'");
            ////warning("# super class 'Class' used for class '"+resCls+"'");
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
    pwClsFile.println("import java.io.Serializable;");
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
        pwClsFile.println("    extends de.dfki.rdf.util.THING");

    pwClsFile.print("    implements Serializable");
    /***
    hier fehlt auch das verwenden (import) der packages von den interface classes
    // implementing interfaces
    Set setInterfaces = getInterfacesForCls(resCls);
    if (!setInterfaces.isEmpty())
    {
        Iterator itInterfaces = setInterfaces.iterator();
        while (itInterfaces.hasNext())
        {
            pwClsFile.print(", ");
            String sInterface = (String)itInterfaces.next();
            pwClsFile.print(sInterface);
        }
    }
    ***/
    pwClsFile.println();    // end "implements" line


    pwClsFile.println("{");

    if (m_bIncrementalGenerationForActualFile && m_bGenerateMethodsAboveUserMethods)
        copyPartOfFormerFile_in_class(pwClsFile);

    // slots / properties
    Set setProperties = getPropertiesOfClass(resCls);
    Iterator itProperties = setProperties.iterator();
    StringBuffer sbToStringStuff = new StringBuffer();
    StringBuffer sbPropertyStoreStuff = new StringBuffer();
    String sIndent = "    ";
    String sSeparator = "//------------------------------------------------------------------------------";
    while (itProperties.hasNext())
    {
        pwClsFile.println(sIndent + sSeparator);
        PropertyInfo pi = (PropertyInfo)itProperties.next();
        
        String sMembervarName = pi.sSlotName;
        if( !resCls.getNamespace().equals( pi.sSlotNS ) )
            sMembervarName = RDF2Java.namespace2abbrev( pi.sSlotNS ) + '_' + pi.sSlotName;
        
        if( sMembervarName.equals( "dateOfBirth" ) )
            System.out.println( "break here" );
        
        if (m_bInsertIncrementalInfo)
            pwClsFile.println(sIndent + "/** RDFS2Class: slot " + sMembervarName + " **/");
        String sRangePkgAndCls = pi.sRangeCls;
        if (pi.sRangeClsPkg != null && !pi.sRangeClsPkg.equals(sPkg))
            sRangePkgAndCls = pi.sRangeClsPkg + "." + sRangePkgAndCls;

        sbPropertyStoreStuff.append( sIndent + "    ps.addProperty( m_" + sMembervarName + " );\n" );


        //--- BEGIN --- property info (variable) for this slot
        String sPropInfoHasMultiValue = ( pi.bMultiple  ?  "true"  :  "false" );
        if( sRangePkgAndCls.equals("Date") )
        {
            pwClsFile.println(sIndent + "protected de.dfki.rdf.util.PropertyInfo m_" + sMembervarName + " = de.dfki.rdf.util.PropertyInfo.createStringProperty( \"" + pi.sSlotNS + "\", \"" + pi.sSlotName + "\", " + sPropInfoHasMultiValue + " );\n");
        }
        else
        if( sRangePkgAndCls.equals("String") )
        {
            if( pi.setAllowedSymbols == null || pi.setAllowedSymbols.isEmpty() )
                pwClsFile.println(sIndent + "protected de.dfki.rdf.util.PropertyInfo m_" + sMembervarName + " = de.dfki.rdf.util.PropertyInfo.createStringProperty( \"" + pi.sSlotNS + "\", \"" + pi.sSlotName + "\", " + sPropInfoHasMultiValue + " );\n");
            else
            {   // symbol type
                StringBuffer sb = new StringBuffer( "new String[]{" );
                for( Iterator it = pi.setAllowedSymbols.iterator(); it.hasNext(); )
                {
                    String symbol = ((RDFNode)it.next()).getLabel();
                    sb.append( "\"" + symbol + "\"" );
                    if( it.hasNext() ) sb.append( ", " );
                }
                sb.append( "}" );
                String sPropInfoAllowedSymbols = sb.toString();

                String sPropInfoDefaultValues = "null";
                if( pi.setDefaultValues != null && pi.setDefaultValues.size() > 0 )
                {
                    sb = new StringBuffer( "new String[]{" );
                    for( Iterator it = pi.setDefaultValues.iterator(); it.hasNext(); )
                    {
                        String symbol = ((RDFNode)it.next()).getLabel();
                        sb.append( "\"" + symbol + "\"" );
                        if( it.hasNext() ) sb.append( ", " );
                    }
                    sb.append( "}" );
                    sPropInfoDefaultValues = sb.toString();
                }

                pwClsFile.println(sIndent + "protected de.dfki.rdf.util.PropertyInfo m_" + sMembervarName + " = de.dfki.rdf.util.PropertyInfo.createSymbolProperty( \"" + pi.sSlotNS + "\", \"" + pi.sSlotName + "\", " + sPropInfoAllowedSymbols + ", " + sPropInfoDefaultValues + ", " + sPropInfoHasMultiValue + " );\n");
            }
        }
        else
        {
            String sPropInfoAllowedValueClasses = "null";
            if( pi.bNeedsRangeInterface )
            {
                StringBuffer sb = new StringBuffer( "new Class[]{" );
                for( Iterator it = pi.setResRange.iterator(); it.hasNext(); )
                {
                    Resource resRCls = (Resource)it.next();
                    ////message( " --- resRCls: " + resRCls );
                    String sRClsName = null;
                    if( resRCls.equals( m_resRDFSLiteral ) )
                    {
                        sRClsName = "String";
                    }
                    else if( resRCls.equals( m_resRDFSResource ) )
                    {
                        sRClsName = "de.dfki.rdf.util.THING";
                    }
                    else
                    {
                        String sRClsNS = resRCls.getNamespace();
                        String sRClsPkg = (String)m_mapNamespaceToPackage.get(sRClsNS);
                        sRClsName = resRCls.getLocalName();
                        ////message( " --- sRClsNS: " + sRClsNS );
                        ////message( " --- sRClsPkg: " + sRClsPkg );
                        ////message( " --- sRClsName: " + sRClsName );
                        if (!sRClsPkg.equals(sPkg)) sRClsName = sRClsPkg + "." + sRClsName;
                    }
                    sb.append( sRClsName + ".class" );
                    if( it.hasNext() ) sb.append( ", " );
                }
                sb.append( "}" );
                sPropInfoAllowedValueClasses = sb.toString();
            }
            else  // no range interface needed
            {
                sPropInfoAllowedValueClasses = "new Class[]{" + sRangePkgAndCls + ".class}";
            }
            pwClsFile.println(sIndent + "protected de.dfki.rdf.util.PropertyInfo m_" + sMembervarName + " = de.dfki.rdf.util.PropertyInfo.createInstanceProperty( \"" + pi.sSlotNS + "\", \"" + pi.sSlotName + "\", " + sPropInfoAllowedValueClasses + ", " + sPropInfoHasMultiValue + " );\n");
        }
        //--- END --- property info (variable) for this slot


        if (pi.bMultiple)
        {
            if (m_bInsertIncrementalInfo)
                pwClsFile.println(sIndent + "/** RDFS2Class: putter for slot " + sMembervarName + " **/");
            if( !sRangePkgAndCls.equals("de.dfki.rdf.util.RDFResource") )
            {
                if( pi.bNeedsRangeInterface )
                    sRangePkgAndCls = "Object";
                pwClsFile.println(sIndent + "public void " + RDF2Java.makeMethodName("put", sMembervarName) + " (" + sRangePkgAndCls + " p_" + sMembervarName +")\n" +
                                  sIndent + "{");
                if (pi.bNeedsRangeInterface)
                {
                    pwClsFile.print(sIndent + "    if( p_" + sMembervarName + " == null || ( !(p_" + sMembervarName + " instanceof de.dfki.rdf.util.RDFResource) && " );
                    Iterator it = pi.setResRange.iterator();
                    while (it.hasNext())
                    {
                        Resource resRCls = (Resource)it.next();
                        String sRClsName = null;
                        if( resRCls.equals( m_resRDFSLiteral ) )
                        {
                            sRClsName = "String";
                        }
                        else if( resRCls.equals( m_resRDFSResource ) )
                        {
                            sRClsName = "de.dfki.rdf.util.THING";
                        }
                        else
                        {
                            String sRClsNS = resRCls.getNamespace();
                            String sRClsPkg = (String)m_mapNamespaceToPackage.get(sRClsNS);
                            sRClsName = resRCls.getLocalName();
                            if (!sRClsPkg.equals(sPkg)) sRClsName = sRClsPkg + "." + sRClsName;
                        }
                        pwClsFile.print("!(p_"+sMembervarName+" instanceof "+sRClsName+")");
                        if (it.hasNext()) pwClsFile.print(" && ");
                    }
                    pwClsFile.println(" ) )\n"+sIndent+"        throw new Error(\"not an allowed class\");");
                }
                pwClsFile.print(sIndent + "    m_" + sMembervarName + ".putValue(p_" + sMembervarName + ");\n" +
                                sIndent + "}\n");
            }
            pwClsFile.print(sIndent + "public void " + RDF2Java.makeMethodName("put", sMembervarName) + " (de.dfki.rdf.util.RDFResource p_" + sMembervarName +")\n" +
                            sIndent + "{\n" +
                            sIndent + "    m_" + sMembervarName + ".putValue(p_" + sMembervarName + ");\n" +
                            sIndent + "}\n");
            pwClsFile.print(sIndent + "public void " + RDF2Java.makeMethodName("put", sMembervarName) + " (java.util.Collection p_" + sMembervarName +")\n" +
                            sIndent + "{\n" +
                            sIndent + "    m_" + sMembervarName + ".setValues(p_" + sMembervarName + ");\n" +
                            sIndent + "}\n");
            pwClsFile.print(sIndent + "public void " + RDF2Java.makeMethodName("clear", sMembervarName) + " ()\n" +
                            sIndent + "{\n" +
                            sIndent + "    m_" + sMembervarName + ".clearValue();\n" +
                            sIndent + "}");
            if (m_bInsertIncrementalInfo)
                pwClsFile.print("\n" + sIndent + "// RDFS2Class: end of putter for slot " + sMembervarName);
            pwClsFile.println("\n");

            if (m_bInsertIncrementalInfo)
                pwClsFile.println(sIndent + "/** RDFS2Class: getter for slot " + sMembervarName + " **/");
            pwClsFile.print(sIndent + "public java.util.Collection " + RDF2Java.makeMethodName("get", sMembervarName) + " ()\n" +
                            sIndent + "{\n" +
                            sIndent + "    return (java.util.Collection)m_" + sMembervarName + ".getValue();\n" +
                            sIndent + "}");
            if (m_bInsertIncrementalInfo)
                pwClsFile.print("\n" + sIndent + "// RDFS2Class: end of getter for slot " + sMembervarName);
            pwClsFile.println("\n");

            /***
            if (m_bIncludeToStringStuff)
            {
                // toString stuff
                String sPreShort     = ( m_bIncludeToStringStuffRecursive  ?  "// "  :  ""    );
                String sPreRecursive = ( m_bIncludeToStringStuffRecursive  ?  ""     :  "// " );
                sbToStringStuff.append(sIndent + "    if (!m_"+sMembervarName+".isEmpty()) {\n");
                sbToStringStuff.append(sIndent + "        sb.append(sIndent+\"-> "+sMembervarName+":\\n\");\n");
                sbToStringStuff.append(sIndent + "        for (Iterator it_" + sMembervarName + " = ((java.util.Collection)m_" + sMembervarName + ".getValue()).iterator(); it_" + sMembervarName + ".hasNext(); ) {\n");
                if (sRangePkgAndCls.equals("String"))
                    sbToStringStuff.append(sIndent + "            sb.append( (String)it_" + sMembervarName + ".next() + \"\\n\" );\n");
                else
                if (sRangePkgAndCls.equals("Object"))
                    sbToStringStuff.append(sIndent + "            " + sPreShort     + "sb.append( sIndent+\"       \" + it_" + sMembervarName + ".next() + \"\\n\" );\n" );
                else
                    sbToStringStuff.append(sIndent + "            " + sPreShort     + "sb.append( sIndent+\"       \" + ((de.dfki.rdf.util.RDFResource)it_" + sMembervarName + ".next()).toStringShort() + \"\\n\" );\n" +
                                           sIndent + "            " + sPreRecursive + "sb.append( ((de.dfki.rdf.util.RDFResource)it_" + sMembervarName + ".next()).toString(sIndent+\"       \") );\n");
                sbToStringStuff.append(sIndent + "        }\n" +
                                       sIndent + "    }\n");
            }
            ***/
        }
        else  // !(pi.bMultiple) => single value slot
        {
            String sRealSlotType = ( ( sRangePkgAndCls.equals("String") || sRangePkgAndCls.equals("Date") )  
                                   ?  "String"  
                                   :  "de.dfki.rdf.util.RDFResource" );

            if (m_bInsertIncrementalInfo)
                pwClsFile.println(sIndent + "/** RDFS2Class: putter for slot " + sMembervarName + " **/");
            if( !sRangePkgAndCls.equals("de.dfki.rdf.util.RDFResource") )
            {
                if( pi.bNeedsRangeInterface )
                    sRangePkgAndCls = "Object";

                if( sRangePkgAndCls.equals("Date") )
                {
                    pwClsFile.println(  sIndent + "public void " + RDF2Java.makeMethodName("put", sMembervarName) + " (" + sRangePkgAndCls + " p_" + sMembervarName +")\n" +
                                        sIndent + "{\n" +
                                        sIndent + "    m_" + sMembervarName + ".putValue(de.dfki.rdf.util.RDFTool.dateTime2String(p_" + sMembervarName + "));\n" +
                                        sIndent + "}" );
                    pwClsFile.println(sIndent + "public void " + RDF2Java.makeMethodName("put", sMembervarName) + " (" + sRealSlotType + " p_" + sMembervarName +")\n" +
                            sIndent + "{");
                }
                else
                {
                    pwClsFile.println(sIndent + "public void " + RDF2Java.makeMethodName("put", sMembervarName) + " (" + sRangePkgAndCls + " p_" + sMembervarName +")\n" +
                                      sIndent + "{");
                }
                
                if (pi.bNeedsRangeInterface)
                {
                    pwClsFile.print(sIndent + "    if( p_" + sMembervarName + " != null && !(p_" + sMembervarName + " instanceof de.dfki.rdf.util.RDFResource) && " );
                    Iterator it = pi.setResRange.iterator();
                    while (it.hasNext())
                    {
                        Resource resRCls = (Resource)it.next();
                        String sRClsName = null;
                        if( resRCls.equals( m_resRDFSLiteral ) )
                        {
                            sRClsName = "String";
                        }
                        else if( resRCls.equals( m_resRDFSResource ) )
                        {
                            sRClsName = "de.dfki.rdf.util.THING";
                        }
                        else
                        {
                            String sRClsNS = resRCls.getNamespace();
                            String sRClsPkg = (String)m_mapNamespaceToPackage.get(sRClsNS);
                            sRClsName = resRCls.getLocalName();
                            if (!sRClsPkg.equals(sPkg)) sRClsName = sRClsPkg + "." + sRClsName;
                        }
                        pwClsFile.print("!(p_"+sMembervarName+" instanceof "+sRClsName+")");
                        if (it.hasNext()) pwClsFile.print(" && ");
                    }
                    pwClsFile.println(")\n"+sIndent+"        throw new Error(\"not an allowed class\");");
                }
                pwClsFile.print(sIndent + "    m_" + sMembervarName + ".putValue(p_" + sMembervarName + ");\n" +
                                sIndent + "}");
            }
            // second putter (URI)
//            if (sRangePkgAndCls.equals("Date"))
//            {
//                pwClsFile.print(sIndent + "public void " + RDF2Java.makeMethodName("put", sMembervarName) + " (String p_" + sMembervarName +")\n" +
//                        sIndent + "{\n" +
//                        sIndent + "    m_" + sMembervarName + ".putValue(p_" + sMembervarName + ");\n" +
//                        sIndent + "}");
//                pwClsFile.print(sIndent + "public void " + RDF2Java.makeMethodName("Put", sMembervarName) + " (Date p_" + sMembervarName +")\n" +
//                        sIndent + "{\n" +
//                        sIndent + "    //TODO\n" +
//                        sIndent + "}");
//            }
//            else
            if (!sRealSlotType.equals("String"))
            {
                if( !sRangePkgAndCls.equals("de.dfki.rdf.util.RDFResource") )
                    pwClsFile.print( "\n" );
                pwClsFile.print(sIndent + "public void " + RDF2Java.makeMethodName("put", sMembervarName) + " (de.dfki.rdf.util.RDFResource p_" + sMembervarName +")\n" +
                                sIndent + "{\n" +
                                sIndent + "    m_" + sMembervarName + ".putValue(p_" + sMembervarName + ");\n" +
                                sIndent + "}");
            }
            if (m_bInsertIncrementalInfo)
                pwClsFile.print("\n" + sIndent + "// RDFS2Class: end of putter for slot " + sMembervarName);
            pwClsFile.println("\n");

            if (m_bInsertIncrementalInfo)
                pwClsFile.println(sIndent + "/** RDFS2Class: getter for slot " + sMembervarName + " **/");
//            if (sRangePkgAndCls.equals("Date"))
//            {
//                pwClsFile.print(sIndent + "public " + sRangePkgAndCls + " " + RDF2Java.makeMethodName("Get", sMembervarName) + " ()\n" +
//                        sIndent + "{\n" +
//                        sIndent + "    //TODO\n" +
//                        sIndent + "}\n");
//            }
//            else
            if (!sRealSlotType.equals("String"))
            {
                pwClsFile.print(sIndent + "public " + sRangePkgAndCls + " " + RDF2Java.makeMethodName("Get", sMembervarName) + " ()\n" +
                                sIndent + "{\n" +
                                sIndent + "    return (" + sRangePkgAndCls + ")m_" + sMembervarName + ".getValue();\n" +
                                sIndent + "}\n");
            }
            if( sRangePkgAndCls.equals("Date") )
            {
                pwClsFile.print(sIndent + "public " + sRangePkgAndCls + " " + RDF2Java.makeMethodName("Get", sMembervarName) + " ()\n" +
                        sIndent + "{\n" +
                        sIndent + "    return de.dfki.rdf.util.RDFTool.string2Date((String)m_" + sMembervarName + ".getValue());\n" +
                        sIndent + "}\n");
            }
            pwClsFile.print(sIndent + "public " + sRealSlotType + " " + RDF2Java.makeMethodName("get", sMembervarName) + " ()\n" +
                            sIndent + "{\n" +
                            sIndent + "    return (" + sRealSlotType + ")m_" + sMembervarName + ".getValue();\n" +
                            sIndent + "}");
            if (m_bInsertIncrementalInfo)
                pwClsFile.print("\n" + sIndent + "// RDFS2Class: end of getter for slot " + sMembervarName);
            pwClsFile.println("\n");

            /***
            if (m_bIncludeToStringStuff)
            {
                // toString stuff
                String sPreShort     = ( m_bIncludeToStringStuffRecursive  ?  "// "  :  ""    );
                String sPreRecursive = ( m_bIncludeToStringStuffRecursive  ?  ""     :  "// " );
                sbToStringStuff.append(sIndent + "    if (m_"+sMembervarName+".getValue() != null) {\n");
                if (sRangePkgAndCls.equals("String"))  //FIXME: hier noch nach "int" etc. testen!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    sbToStringStuff.append(sIndent + "        sb.append(sIndent+\"-> "+sMembervarName+": \"+m_"+sMembervarName+".getValue() + \"\\n\");\n");
                else
                if (sRangePkgAndCls.equals("Object"))
                    sbToStringStuff.append(sIndent + "        sb.append(sIndent+\"-> "+sMembervarName+": \"+m_"+sMembervarName+".getValue() + \"\\n\");\n");
                else
                {
                    sbToStringStuff.append(sIndent + "        " + sPreShort     + "sb.append(sIndent+\"-> "+sMembervarName+":\\n\"+sIndent+\"       \"+((de.dfki.rdf.util.RDFResource)m_"+sMembervarName+".getValue()).toStringShort() + \"\\n\");\n");
                    sbToStringStuff.append(sIndent + "        " + sPreRecursive + "sb.append(sIndent+\"-> "+sMembervarName+":\\n\"+((de.dfki.rdf.util.RDFResource)m_"+sMembervarName+".getValue()).toString(sIndent+\"       \"));\n");
                }
                sbToStringStuff.append(sIndent + "    }\n");
            }
            ***/
        }
    }

    // toStringStuff comes here
    if( m_bIncludeToStringStuff )
    {
        // toString stuff
        pwClsFile.println(sIndent + sSeparator);
        if( m_bInsertIncrementalInfo )
            pwClsFile.println( sIndent + "/** RDFS2Class: toString()-stuff **/" );
        pwClsFile.println( sIndent + "public String toString()\n" +
                           sIndent + "{" );
        
        if( m_sIncludeToStringStuffOptions.indexOf( 'r' ) >= 0 )
        {
            if( m_sIncludeToStringStuffOptions.indexOf( 's' ) >= 0 )
                pwClsFile.println( sIndent + "    return toStringAsRdfRecursive();" );
            else
                pwClsFile.println( sIndent + "    return toStringAsRdf();" );
        }
        else if( m_sIncludeToStringStuffOptions.indexOf( 'p' ) >= 0 )
        {
            if( m_sIncludeToStringStuffOptions.indexOf( 's' ) >= 0 )
                pwClsFile.println( sIndent + "    return toStringPackedRecursive();" );
            else
                pwClsFile.println( sIndent + "    return toStringPacked();" );
        }
        else
        {
            if( m_sIncludeToStringStuffOptions.indexOf( 's' ) >= 0 )
                pwClsFile.println( sIndent + "    return toStringPackedRecursive();" );
            else
                pwClsFile.println( sIndent + "    return toStringPacked();" );
        }
                
        pwClsFile.println( sIndent + "}" );
        if( m_bInsertIncrementalInfo )
            pwClsFile.println( sIndent + "// RDFS2Class: end of toString()-stuff" );
        pwClsFile.println();
    }
    //old:
    //    if (m_bIncludeToStringStuff && sbToStringStuff.length() > 0)
    //    {
    //        // toString stuff
    //        pwClsFile.println(sIndent + sSeparator);
    //        if (m_bInsertIncrementalInfo)
    //            pwClsFile.println(sIndent + "/** RDFS2Class: toString()-stuff **/");
    //        pwClsFile.println(sIndent + "public void toString (StringBuffer sb, String sIndent)\n" +
    //                          sIndent + "{\n" +
    //                          sIndent + "    super.toString(sb, sIndent);");
    //        pwClsFile.print(sbToStringStuff.toString());
    //        pwClsFile.println(sIndent + "}");
    //        if (m_bInsertIncrementalInfo)
    //            pwClsFile.println(sIndent + "// RDFS2Class: end of toString()-stuff");
    //        pwClsFile.println();
    //    }
    
    // information about sub classes are coming here
    Model modelSubClasses = m_modelRDFS.find( null, m_resRDFSPredSubClassOf, resCls );
    Set setSubClasses = subjectsToSet( modelSubClasses );
    pwClsFile.println( sIndent + sSeparator );
    if (m_bInsertIncrementalInfo)
        pwClsFile.println(sIndent + "/** RDFS2Class: sub class information **/");
    StringBuffer sbSubClasses = new StringBuffer();
    for( Iterator it = setSubClasses.iterator(); it.hasNext(); )
    {
        Resource resSubCls = (Resource)it.next();
        String sSubClsNS = resSubCls.getNamespace();
        String sSubClsPkg = (String)m_mapNamespaceToPackage.get(sSubClsNS);
        String sSubClsName = resSubCls.getLocalName();
        if (!sSubClsPkg.equals(sPkg)) sSubClsName = sSubClsPkg + "." + sSubClsName;
        sbSubClasses.append( sSubClsName + ".class" );
        if( it.hasNext() ) sbSubClasses.append( ", " );
    }
    pwClsFile.println( sIndent + "public final static Class[] KNOWN_SUBCLASSES = {" + sbSubClasses.toString() + "};\n" );
//                       sIndent + "public Class[] GetKnownJavaSubClasses()\n" +
//                       sIndent + "{\n" +
//                       sIndent + "    return KNOWN_SUBCLASSES;\n" +
//                       sIndent + "}" );
    if (m_bInsertIncrementalInfo)
        pwClsFile.println(sIndent + "// RDFS2Class: end of sub class information");
    pwClsFile.println();

    // default constructor comes here
    pwClsFile.println( sIndent + sSeparator );
    if (m_bInsertIncrementalInfo)
        pwClsFile.println(sIndent + "/** RDFS2Class: default constructor **/");
    pwClsFile.println( sIndent + "public " + sClsName + "()\n" +
                       sIndent + "{\n" +
                       sIndent + "    super();\n" +
                       sIndent + "    putRDFSClass( new de.dfki.rdf.util.RDFResource( \"" + resCls.getNamespace() + "\", \"" + resCls.getLocalName() + "\" ) );\n" +
                       sIndent + "    initPropertyStore();\n" +
                       sIndent + "}" );
    if (m_bInsertIncrementalInfo)
        pwClsFile.println(sIndent + "// RDFS2Class: end of default constructor");
    pwClsFile.println();

    // PriorityInfoStuff comes here
    pwClsFile.println( sIndent + sSeparator );
    if (m_bInsertIncrementalInfo)
        pwClsFile.println(sIndent + "/** RDFS2Class: PropertyStore-stuff **/");
    pwClsFile.println( sIndent + "private void initPropertyStore()\n" +
                       sIndent + "{\n" +
                       sIndent + "    de.dfki.rdf.util.PropertyStore ps = getPropertyStore();\n" +
                       sbPropertyStoreStuff +
                       sIndent + "}" );
    if (m_bInsertIncrementalInfo)
        pwClsFile.println(sIndent + "// RDFS2Class: end of PropertyStore-stuff");
    pwClsFile.println();


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
    Enumeration en = modelStatements.elements();
    while (en.hasMoreElements())
    {
        Statement st = (Statement)en.nextElement();
        hs.add(st.object());
    }
    return hs;
}

//----------------------------------------------------------------------------------------------------
protected Set subjectsToSet (Model modelStatements)   throws Exception
{
    HashSet hs = new HashSet();
    Enumeration en = modelStatements.elements();
    while (en.hasMoreElements())
    {
        Statement st = (Statement)en.nextElement();
        hs.add(st.subject());
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
        String sSlotNS;
        String sSlotName;
        Set setResDomain;
        Set setResRange;
        Set setAllowedSymbols;
        Set setDefaultValues;
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
            sSlotNS = resProperty.getNamespace();
            sSlotPkg = (String)m_mapNamespaceToPackage.get(sSlotNS);
            sSlotName = resProperty.getLocalName();
            Model modelDomain = m_modelRDFS.find(resProperty, m_resRDFSPredDomain, null);
            setResDomain = objectsToSet(modelDomain);
            Model modelRange = m_modelRDFS.find(resProperty, m_resRDFSPredRange, null);
            setResRange = objectsToSet(modelRange);
            Model modelAllowedSymbols = m_modelRDFS.find(resProperty, m_resProtegeAllowedSymbols, null);
            setAllowedSymbols = objectsToSet(modelAllowedSymbols);
            Model modelDefaultValues = m_modelRDFS.find(resProperty, m_resProtegeDefaultValues, null);
            setDefaultValues = objectsToSet(modelDefaultValues);

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
                // message("# "+sSlotName+" -> "+setResRange);
//SS:2003-03-31: wofür war das (hatte was mit heiko's kram zu tun -> wwf/model...)
//FIXME: das ist der absolute hack hier:
                if( setResRange.size() == 1 && setResRange.contains( m_resRDFSResource ) )
                    setResRange.clear();
                Model modelAllowedClasses = m_modelRDFS.find(resProperty, m_resProtegeAllowedClasses, null);
                if( !modelAllowedClasses.isEmpty() )
                    setResRange = objectsToSet( modelAllowedClasses );
                // message("# (after a:allowed): -> "+setResRange);
            }

            if (setResRange.size() > 1)
            {
                sRangeCls = "de.dfki.rdf.util.THING";  // :THING in protege
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
                    sRangeCls = "de.dfki.rdf.util.THING";   // or, maybe, better:   de.dfki.rdf.util.RDFResource
                    sRangeClsPkg = null;
                }
                else
                {
                    RDFNode rdfnodeRange = (RDFNode)setResRange.iterator().next();
                    if (rdfnodeRange.equals(m_resXMLDateTime))
                    {
                        sRangeCls = "Date";
                        sRangeClsPkg = null;
                    }
                    else
                    if (rdfnodeRange.equals(m_resRDFSLiteral))
                    {
                        sRangeCls = "String";
/*
                        RDFNode rdfnodeLiteralType = RDF.getObject(m_modelRDFS, resProperty, m_resProtegeRange);
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
                        sRangeCls = "RDFResource";
                        sRangeClsPkg = "de.dfki.rdf.util";
                        // sRangeCls = "Class";
                        // sRangeClsPkg = null;
                    }
                    else
                    {
                        sRangeCls = ((Resource)rdfnodeRange).getLocalName();
                        String sRangeClsNS = ((Resource)rdfnodeRange).getNamespace();
                        sRangeClsPkg = (String)m_mapNamespaceToPackage.get(sRangeClsNS);
                        // message( "### slot " + sSlotName + " -> " + sRangeClsNS + " . " + sRangeCls + " -> " + sRangeClsPkg );
                        if( sRangeClsPkg == null )
                            error( "# no package mapping for namespace " + sRangeClsNS + " (slot " + sSlotName + ")" );
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

