package de.dfki.km.jena2java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.dfki.rdf.util.RDF2Java;
import de.dfki.rdf.util.RDFTool;
import de.dfki.util.servlet.SnoopServlet;


/**
 * @author schwarz, kiesel
 * 
 */
public class RDFS2Class
{
    public final static String OLD_RDFS_NAMESPACE   = "http://www.w3.org/TR/1999/PR-rdf-schema-19990303#";
    public final static String NEW_RDFS_NAMESPACE   = "http://www.w3.org/2000/01/rdf-schema#";
    public static String RDFS_NAMESPACE             = NEW_RDFS_NAMESPACE;
    public static String RDF_NAMESPACE              = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static String XML_SCHEMA_NAMESPACE       = "http://www.w3.org/2001/XMLSchema-datatypes#";
    public static String PROTEGE_NS                 = "http://protege.stanford.edu/system#";

    private static int m_iNrMessages = 0;
    private static int m_iNrWarnings = 0;
    private static int m_iNrErrors = 0;
    private static boolean m_bQuiet = false;
    
    private List/*String*/ m_collGeneratedJavaFiles = new LinkedList();

    private String m_sRDFSFile;
    private String m_sJenaConstantsClass;
    private boolean m_bGenJenaConstantsClass = false;
    private Map m_mapNamespaceToPackage;
    private String m_sClsPath;
    
    /** the default namespace of the given RDFS file.
     *  this will be used to discriminate internal properties from alien properties.
     *  right now, as a hack, the first namespace is taken from the namespace2package map parameters.
     */
    private String m_defaultNamespace = null;

    private Model m_modelRDFS;
    private Resource m_resRDFSClass;
    private Resource m_resRDFSResource;
    private Resource m_resRDFSLiteral;
    private Resource m_resXMLDateTime;
    private Property m_propRDFSPredSubClassOf;
    private Property m_propRDFSPredDomain;
    private Property m_propRDFSPredRange;
    private Property m_propRDFPredType;
    private Property m_resRDFResProperty;
    private Property m_propProtegeMaxCardinality;
    private Property m_propProtegeAllowedClasses;
    private Property m_propProtegeAllowedSymbols;
    private Property m_propProtegeDefaultValues;
    private Property m_propProtegePredRole;
    private Property m_propProtegeRange;

    private HashSet m_setProperties;
    private LinkedList m_lstFormerFile; // list of strings (lines) of the former java-file

    private final static String SEPARATOR = "//------------------------------------------------------------------------------";

    
    public final static Collection/*String*/ RESERVED_WORDS = Arrays.asList( new String[]{
        "public", "protected", "private", "final", "static", 
        "char", "int", "long", "short", "float", "double",
        "Character", "Integer", "Long", "Short", "Float", "Double", "Math", "Number", 
        "Object", "String", "Class", "class", "Process", "Runtime", "Exception", "Boolean", "Byte", 
        "return",
        "for", "while", "do", "if", "switch", "case", "enum",
        "instanceof",
    });

    
    public static void main( String[] args )
    {
        try
        {
            if( args.length < 1 )
                throw new Exception( "RDFS2Class: no parameters" );

            final String CHANGE_RDFS_NS     = "--rdfs=";
            final String CHANGE_RDF_NS      = "--rdf=";
            final String CHANGE_PROTEGE_NS  = "--protege=";

            RDFS2Class gen = new RDFS2Class();

            int iArg = 0;
            while( iArg < args.length )
            {
                if( args[iArg].startsWith( "-" ) )
                {
                    String sOption = args[iArg].substring( 1 );
                    while( sOption.startsWith( "-" ) ) sOption = sOption.substring( 1 );
                    
                    if( sOption.equals( "quiet" ) || sOption.equals( "q" ) ) 
                        RDFS2Class.m_bQuiet = true;
                    else if( sOption.startsWith( "use-constants-class=" ) )
                        gen.m_sJenaConstantsClass = sOption.substring( "use-constants-class=".length() );
                    else if( sOption.startsWith( "gen-constants-class" ) )
                    {
                        if( sOption.startsWith( "gen-constants-class=" ) )
                            gen.m_sJenaConstantsClass = sOption.substring( "gen-constants-class=".length() );
                        gen.m_bGenJenaConstantsClass = true;
                    }
                    else if( args[iArg].toLowerCase().startsWith( CHANGE_RDFS_NS ) )
                    {
                        RDFS_NAMESPACE = args[iArg].substring( CHANGE_RDFS_NS.length() );
                        message( "RDFS namespace: " + RDFS_NAMESPACE );
                    }
                    else if( args[iArg].toLowerCase().startsWith( CHANGE_RDF_NS ) )
                    {
                        RDF_NAMESPACE = args[iArg].substring( CHANGE_RDF_NS.length() );
                        message( "RDF namespace : " + RDF_NAMESPACE );
                    }
                    else if( args[iArg].toLowerCase().startsWith( CHANGE_PROTEGE_NS ) )
                    {
                        PROTEGE_NS = args[iArg].substring( CHANGE_PROTEGE_NS.length() );
                        message( "Protege namespace: " + PROTEGE_NS );
                    }
                    else
                        throw new Exception( "RDFS2Class: Option '" + sOption + "' unknown." );
                    iArg++;
                }
                else
                {
                    // no option recognized => finished with options
                    break;
                }
            }

            if( args.length - iArg > 0 ) gen.m_sRDFSFile = args[iArg++];
            else
                throw new Exception( "RDFS2Class: Missing parameter: RDFS file." );

            if( args.length - iArg > 0 ) gen.m_sClsPath = args[iArg++];
            else
                throw new Exception( "RDFS2Class: Missing parameter: output directory." );

            if( args.length - iArg <= 0 ) throw new Exception( "RDFS2Class: Missing mapping parameters." );
            if( ((args.length - iArg) % 2) != 0 ) throw new Exception( "RDFS2Class: Mapping parameters not correct." );

            if( !RDFS2Class.m_bQuiet )
            {
                message( "sRDFSFile             : " + gen.m_sRDFSFile );
                message( "sJenaConstantsClass   : " + gen.m_sJenaConstantsClass );
                message( "sOutputSrcDir         : " + gen.m_sClsPath );
                message( "maping:" );
            }
            while( iArg < args.length )
            {
                String sNamespace = args[iArg++];
                String sPackage = args[iArg++];
                if( !RDFS2Class.m_bQuiet ) message( "  " + sNamespace + " -> " + sPackage );
                gen.m_mapNamespaceToPackage.put( sNamespace, sPackage );
                if( gen.m_defaultNamespace == null )
                    gen.m_defaultNamespace = sNamespace;
            }
            if( !RDFS2Class.m_bQuiet ) message( "" );

            if( gen.m_bGenJenaConstantsClass && (gen.m_sJenaConstantsClass == null || gen.m_sJenaConstantsClass.length() <= 0) )
                throw new Error( "please specify constants class" );
            
            gen.createClasses();
        }
        catch( Exception ex )
        {
            String sMsg = ex.getMessage();
            if( sMsg != null && sMsg.startsWith( "RDFS2Class:" ) )
            {
                System.out.println( "\n" + sMsg 
                    + "\nusage: RDFS2Class [options] <file.rdfs> <outputSrcDir> {<namespace> <package>}+\n"
                    + "options:  --quiet: quiet operation, no output\n"
                    + "          --gen-constants-class=<JenaConstantsClass> : generate the constants class\n"
                    + "          --rdfs=<namespace>    : set different RDFS namespace\n"
                    + "          --rdf=<namespace>     : set different RDF namespace\n"
                    + "          --protege=<namespace> : set different Protege namespace\n" );
            }
            else
            {
                exception( ex );
            }
        }

        // print out #warnings and #errors
        message();
        if( m_iNrWarnings > 0 ) message( "" + m_iNrWarnings + " warnings" );
        if( m_iNrErrors > 0 ) message( "" + m_iNrErrors + " errors" );
        if( m_iNrWarnings <= 0 && m_iNrErrors <= 0 ) System.out.println( "ok (no warnings, no errors)" );
        message();
    }


    private static void message( String s )
    {
        if( !m_bQuiet ) System.out.println( s );
        m_iNrMessages++;
    }

    private static void message()
    {
        if( !m_bQuiet ) System.out.println();
    }

    private static void warning( String s )
    {
        System.out.println( "WARNING: " + s );
        m_iNrWarnings++;
    }

    private static void error( String s )
    {
        System.out.println( "ERROR: " + s );
        m_iNrErrors++;
    }

    private static void exception( Exception ex )
    {
        System.out.println( "EXCEPTION" );
        ex.printStackTrace();
        m_iNrErrors++;
    }

    private RDFS2Class()
    {
        m_mapNamespaceToPackage = new HashMap();
    }

    public RDFS2Class( String sRDFSFile, String sJenaConstantsClass, String sClsPath, Map mapNamespaceToPackage ) throws Exception
    {
        m_sRDFSFile = sRDFSFile;
        m_sJenaConstantsClass = sJenaConstantsClass;
        m_mapNamespaceToPackage = mapNamespaceToPackage;
        m_sClsPath = sClsPath;
    }
    
    private void initRdfsResources() throws Exception
    {
        m_resRDFSClass      = m_modelRDFS.createResource( RDFS_NAMESPACE + "Class" );
        m_resRDFSResource   = m_modelRDFS.createResource( RDFS_NAMESPACE + "Resource" );
        m_resRDFSLiteral    = m_modelRDFS.createResource( RDFS_NAMESPACE + "Literal" );
        m_resXMLDateTime    = m_modelRDFS.createResource( XML_SCHEMA_NAMESPACE + "dateTime" );
        
        //TODO: "Property" is a Resource, not a Property ???!!!
        m_resRDFResProperty = m_modelRDFS.createProperty( RDF_NAMESPACE  + "Property" );

        m_propRDFSPredSubClassOf = m_modelRDFS.createProperty( RDFS_NAMESPACE + "subClassOf" );
        m_propRDFSPredDomain     = m_modelRDFS.createProperty( RDFS_NAMESPACE + "domain" );
        m_propRDFSPredRange      = m_modelRDFS.createProperty( RDFS_NAMESPACE + "range" );
        m_propRDFPredType        = m_modelRDFS.createProperty( RDF_NAMESPACE  + "type" );
    }

    private void initProtegeUris() throws Exception
    {
        m_propProtegeMaxCardinality = m_modelRDFS.createProperty( PROTEGE_NS + "maxCardinality" );
        m_propProtegeAllowedClasses = m_modelRDFS.createProperty( PROTEGE_NS + "allowedClasses" );
        m_propProtegeAllowedSymbols = m_modelRDFS.createProperty( PROTEGE_NS + "allowedValues" );
        m_propProtegeDefaultValues  = m_modelRDFS.createProperty( PROTEGE_NS + "defaultValues" );
        m_propProtegePredRole       = m_modelRDFS.createProperty( PROTEGE_NS + "role" );
        m_propProtegeRange          = m_modelRDFS.createProperty( PROTEGE_NS + "range" );
    }

    public void createClasses() throws Exception
    {
        message( "reading RDFS file..." );
        FileReader reader = new FileReader( m_sRDFSFile );
        m_modelRDFS = ModelFactory.createDefaultModel();
        m_modelRDFS.read( reader, "http://dummy.base.uri/" );

        message( "preparing..." );
        initRdfsResources();
        initProtegeUris();

        message( "examining properties..." );
        examineProperties();

        if( m_bGenJenaConstantsClass )
        {
            message( "creating the constants class..." );
            createConstantsClass();
        }

        message( "creating the JenaResourceWapper classes..." );
        createTheClasses();

        if( !m_bQuiet )
        {
            StringBuffer sb = new StringBuffer( "\ncreated the following files:\n" );
            for( Iterator it = m_collGeneratedJavaFiles.iterator(); it.hasNext(); )
            {
                sb.append( " o      " + (String)it.next() + "\n" );
            }
            System.out.println( sb.toString() + "\n" );
        }
        
        message( "ready." );
    }

    
    private void createConstantsClass()   throws Exception
    {
        String sPkg = null;
        String sPath = ".";
        String sClass = m_sJenaConstantsClass;
        int pos = m_sJenaConstantsClass.lastIndexOf( '.' );
        if( pos >= 0 )
        {
            sClass = m_sJenaConstantsClass.substring( pos+1 );
            sPkg = m_sJenaConstantsClass.substring( 0, pos );
            sPath = packageToPath( sPkg );
        }
        String sFile = sClass + ".java";

        String sAbsolutePath = m_sClsPath + "/" + sPath;

        File fPath = new File( sAbsolutePath );
        File fFile = new File( sAbsolutePath + "/" + sFile );
        fPath.mkdirs();
        fFile.createNewFile();
        FileWriter fw = new FileWriter( fFile );
        PrintWriter pw = new PrintWriter( fw );
        
        if( sPkg != null )
            pw.println( "package " + sPkg + ";\n" ); 

        pw.println( "import com.hp.hpl.jena.rdf.model.Model;" );
        pw.println( "import com.hp.hpl.jena.rdf.model.ModelFactory;" );
        pw.println( "import com.hp.hpl.jena.rdf.model.Property;" );
        pw.println( "import com.hp.hpl.jena.rdf.model.Resource;\n" );

        pw.println( "import de.dfki.km.jena2java.ObjectTracker;" );

        pw.println( "/**\n"
         + " * Vocabulary definitions for Jena/RDF resources\n" 
         + " * THIS FILE MAY BE RE-GENERATED - DO NOT EDIT!\n" 
         + " * @author Auto-generated by rdf2java on " + new Date().toString() + "\n"
         + " */\n" );

        pw.println( "public class " + sClass + "\n{" );
        
        pw.println( "    /** <p>The RDF model that holds the vocabulary terms</p> */" );
        pw.println( "    private static Model m_model = ModelFactory.createDefaultModel();\n" );

        pw.println( "    /** <p>The namespace of the vocabalary as a string</p> */" );
        pw.println( "   public static final String NS = \"" + m_defaultNamespace + "\";\n" );
        
        pw.println( "    /** <p>The namespace of the vocabalary as a resource</p> */" );
        pw.println( "    public static final Resource NAMESPACE = m_model.createResource( NS );\n" );
        
        
        // add constants for properties
        ResIterator itProp = m_modelRDFS.listSubjectsWithProperty( m_propRDFPredType, m_resRDFResProperty );
        while( itProp.hasNext() )
        {
            Resource resProperty = itProp.nextResource();
            printJavaDoc( pw, resProperty );
            
            String sFieldName = fieldName( resProperty, m_defaultNamespace );
            pw.println( "    public static final Property " + sFieldName + " = m_model.createProperty( \"" + resProperty.getURI() + "\");\n" ); 
        }
        
        // add constants for resources/classes
        ResIterator itRes = m_modelRDFS.listSubjectsWithProperty( m_propRDFPredType, m_resRDFSClass );
        while( itRes.hasNext() )
        {
            Resource resCls = itRes.nextResource();
            printJavaDoc( pw, resCls );
            String sFieldName = fieldName( resCls, m_defaultNamespace );
            pw.println( "    public static final Resource " + sFieldName + " = m_model.createResource( \"" + resCls.getURI() + "\");\n" ); 
        }

        // add initialization code for classes
        itRes = m_modelRDFS.listSubjectsWithProperty( m_propRDFPredType, m_resRDFSClass );
        boolean hasClasses = itRes.hasNext();
        if( hasClasses )
            pw.println( "    /** Registers this package's classes with an {@link ObjectTracker}.");
            pw.println( "     *  If you forget to do this, Java wrapper instances of this package's classes generated by");
            pw.println( "     *  <code>ObjectTracker.getInstance</code> will be of the generic <code>JenaResourceWrapper</code> type.");
            pw.println( "     */");
            pw.println( "    public static void register(ObjectTracker tracker)\n    {"); 
        while( itRes.hasNext() )
        {
            Resource resCls = itRes.nextResource();
            String sFieldName = fieldName( resCls, m_defaultNamespace );
            pw.println( "        tracker.addClass( " + sFieldName + " , " +  m_mapNamespaceToPackage.get( resCls.getNameSpace() )+ "." + resCls.getLocalName() + ".class );" );
        }
        if( hasClasses )
            pw.println( "    }"); 

        pw.println( "}\n" );
        pw.close();
        
        m_collGeneratedJavaFiles.add( fFile.getAbsolutePath() );
    }
        
    private static String fieldName( Resource res, String defaultNamespace )
    {
        String sFieldName = null;
        if( res.getNameSpace().equals( defaultNamespace ) )
            sFieldName = res.getLocalName();
        else
            sFieldName = RDF2Java.namespace2abbrev( res.getNameSpace() ) + '_' + res.getLocalName();
        if( sFieldName == null ) return null;
        if( RESERVED_WORDS.contains( sFieldName ) )
            sFieldName = "_" + sFieldName;
        return sFieldName;
    }
    
    private void printJavaDoc( PrintWriter pw, Resource res )
    {
        final int MAX_CHARS_PER_LINE = 70;
        
        String sComment = RDFTool.readValue( res, RDFS.comment );
        if( sComment == null ) sComment = RDFTool.readValue( res, RDFS.label );
        if( sComment == null ) return;
        
        boolean bMultipleLines = false;
        pw.print( "    /** <p>" );                
        while( sComment.length() > MAX_CHARS_PER_LINE )
        {
            int pos = MAX_CHARS_PER_LINE;
            for( ; pos >= 0; pos-- )
            {
                if( " \t\r\n".indexOf( sComment.charAt(pos) ) >= 0 )
                    break;                       
            }
            if( pos == 0 ) break;
            pw.print( sComment.substring( 0, pos ).trim() + "\n     *  " );
            bMultipleLines = true;
            sComment = sComment.substring( pos+1 ).trim();
        }
        pw.print( sComment.trim() + "</p>" );
        if( bMultipleLines ) pw.print( "\n    " );
        pw.println( " */" );
    }


    private void examineProperties() throws Exception
    {
        m_setProperties = new HashSet();

        ResIterator it = m_modelRDFS.listSubjectsWithProperty( m_propRDFPredType, m_resRDFResProperty );
        while( it.hasNext() )
        {
            Resource resProperty = it.nextResource();
            PropertyInfo pi = new PropertyInfo( resProperty );
            m_setProperties.add( pi );
        }
    }

    private class PropertyInfo
    {
        Resource resProperty;
        boolean bMultiple = true;
        Set setResDomain;
        Set setResRange;
        String sComment;
        String sRdfsLabel;

        private PropertyInfo( Resource resProperty )
        {
            System.out.println( "PropertyInfo: Resource " + resProperty );
            this.resProperty = resProperty;

            // Protege: Cardinality constraints?
            try
            {
                int iMaxCardinality = resProperty.getProperty( m_propProtegeMaxCardinality ).getInt();
                bMultiple = (iMaxCardinality > 1);
            }
            catch( Exception ex )
            {
            }

            // RDFS domain
            setResDomain = new HashSet();
            for( StmtIterator it = resProperty.listProperties( m_propRDFSPredDomain ); it.hasNext(); )
            {
                Resource resDomain = it.nextStatement().getResource();
                setResDomain.add( resDomain );
                System.out.println( "...RDF resDomain " + resDomain );
            }

            setResRange = new HashSet();
            // Protege range
            for( StmtIterator it = resProperty.listProperties( m_propProtegeRange ); it.hasNext(); )
            {
                String pType = it.nextStatement().getLiteral().getString();
                if( pType.equals( "integer" ) ) setResRange.add( Integer.class );
                if( pType.equals( "float" ) ) setResRange.add( Float.class );
                if( pType.equals( "boolean" ) ) setResRange.add( Boolean.class );
                System.out.println( "...Protege resRange " + pType );
            }
            if( setResRange.size() == 0 ) // TODO
            // RDFS range
            for( StmtIterator it = resProperty.listProperties( m_propRDFSPredRange ); it.hasNext(); )
            {
                Resource resRange = it.nextStatement().getResource();
                setResRange.add( resRange );
                System.out.println( "...RDF resRange " + resRange );
            }
            
            sRdfsLabel = RDFTool.readValue( resProperty, RDFS.label );
            sComment = RDFTool.readValue( resProperty, RDFS.comment );
        }

    } // end of inner class PropertyInfo


    private void createTheClasses() throws Exception
    {
        ResIterator it = m_modelRDFS.listSubjectsWithProperty( m_propRDFPredType, m_resRDFSClass );
        while( it.hasNext() )
        {
            Resource resCls = it.nextResource();
            createClass( resCls );
        }
    }


    private void createClass( Resource resCls ) throws Exception
    {
        if( resCls.getURI().equals( m_resXMLDateTime.getURI() ) ) 
            return; // omitting xml schema classes

        String sClsNS = resCls.getNameSpace();
        String sClsName = resCls.getLocalName();
        message( sClsNS + sClsName );
        String sPkg = (String) m_mapNamespaceToPackage.get( sClsNS );
        if( sPkg == null ) throw new Exception( "Namespace '" + sClsNS + "' not mapped to a package" );
        String sPath = packageToPath( sPkg );
        String sFile = sClsName + ".java";
        message( " -> " + sPath + "/" + sFile );

        PrintWriter pwClsFile = null;
        String sAbsolutePath = m_sClsPath + "/" + sPath;
        loadInFormerJavaFile( sAbsolutePath + "/" + sFile );
        pwClsFile = createFile( sAbsolutePath, sFile );
        fillClassFile( resCls, sPkg, sClsName, pwClsFile );
        pwClsFile.close();
        
        File f = new File( sAbsolutePath + "/" + sFile );
        m_collGeneratedJavaFiles.add( f.getAbsolutePath() );
    }


    private String packageToPath( String sPkg )
    {
        StringBuffer sbPath = new StringBuffer();
        int left = 0;
        int pos;
        while( true )
        {
            pos = sPkg.indexOf( '.', left );
            if( pos < 0 ) break;
            sbPath.append( sPkg.substring( left, pos ) + "/" );
            left = pos + 1;
        }
        if( left < sPkg.length() ) sbPath.append( sPkg.substring( left ) );
        return sbPath.toString();
    }


    private void loadInFormerJavaFile( String sPathAndFilename ) throws Exception
    {
        m_lstFormerFile = new LinkedList();
        BufferedReader br = null;
        try{ br = new BufferedReader( new FileReader( sPathAndFilename ) ); }
        catch( java.io.FileNotFoundException ex ) { return; }
        while( br.ready() )
        {
            String sLine = br.readLine();
            m_lstFormerFile.add( sLine );
        }
    }

    private PrintWriter createFile( String sPath, String sFile )
            throws Exception
    {
        File fPath = new File( sPath );
        File fFile = new File( sPath + "/" + sFile );
        fPath.mkdirs();
        fFile.createNewFile();
        FileWriter fw = new FileWriter( fFile );
        PrintWriter pw = new PrintWriter( fw );
        return pw;
    }


    private void copyPartOfFormerFile_package_imports( PrintWriter pwClsFile )
    {
        StringBuffer sbWhitespace = new StringBuffer();
        ListIterator it = m_lstFormerFile.listIterator();
        while( it.hasNext() )
        {
            String sLine = (String) it.next();
            if( sLine != null && sLine.startsWith( "package" ) ) break;
        }
        while( it.hasNext() )
        {
            String sLine = (String) it.next();
            if( sLine != null && sLine.trim().startsWith( "// RDFS2Class: imports" ) ) break;
            else
            {
                if( sLine.trim().length() <= 0 )
                    sbWhitespace.append( sLine + "\n" );
                else
                    pwClsFile.println( sbWhitespace.toString() + sLine );
            }
        }
    }

    private void copyPartOfFormerFile_imports_class( PrintWriter pwClsFile )
    {
        StringBuffer sbWhitespace = new StringBuffer();
        ListIterator it = m_lstFormerFile.listIterator();
        while( it.hasNext() )
        {
            String sLine = (String) it.next();
            if( sLine != null && sLine.startsWith( "// RDFS2Class: end of imports" ) ) break;
        }
        while( it.hasNext() )
        {
            String sLine = (String) it.next();
            if( sLine != null && sLine.trim().startsWith( "/** RDFS2Class: class" ) ) break;
            else
            {
                if( sLine.trim().length() <= 0 )
                    sbWhitespace.append( sLine + "\n" );
                else
                    pwClsFile.println( sbWhitespace.toString() + sLine );
            }
        }
    }

    private void copyPartOfFormerFile_in_class( PrintWriter pwClsFile )
    {
        Vector vLines = new Vector();
        ListIterator it = m_lstFormerFile.listIterator();
        String sLine;
        String sLastLine;
        while( it.hasNext() )
        {
            sLine = (String) it.next();
            if( sLine != null && sLine.startsWith( "/** RDFS2Class: class" ) ) break;
        }
        while( it.hasNext() )
        {
            sLine = (String) it.next();
            if( sLine != null && sLine.indexOf( '{' ) >= 0 ) break;
        }
        sLastLine = null;
        while( it.hasNext() )
        {
            sLine = (String) it.next();
            if( sLine == null ) continue;
            if( sLine.trim().startsWith( "// RDFS2Class: end of class" ) ) break;

            if( sLine.trim().startsWith( "// RDFS2Class: begin" ) )
            {
                while( it.hasNext() )
                {
                    sLine = (String) it.next();
                    if( sLine != null && sLine.trim().startsWith( "// RDFS2Class: end" ) ) break;
                }
                if(     sLastLine != null && sLastLine.length() != 0
                        && !sLastLine.trim().equals( SEPARATOR ) ) 
                    vLines.add( sLastLine );
                sLastLine = null;
            }
            else
            {
                if( sLastLine != null ) vLines.add( sLastLine );

                // sLastLine = null;
                sLastLine = sLine;
            }
        }

        // cut off empty lines at beginning of vLines
        while( !vLines.isEmpty() )
        {
            String str = (String) vLines.get( 0 );
            if( str == null || str.length() == 0 || str.startsWith( "\n" ) ) 
                vLines.remove( 0 );
            else
                break;
        }
        // cut off empty lines at end of vLines
        while( !vLines.isEmpty() )
        {
            String str = (String) vLines.get( vLines.size() - 1 );
            if( str == null || str.length() == 0 || str.startsWith( "\n" ) ) 
                vLines.remove( vLines.size() - 1 );
            else
                break;
        }
        // print out vLines
        Iterator it2 = vLines.iterator();
        while( it2.hasNext() )
        {
            pwClsFile.println( (String) it2.next() );
        }
    }

    private void copyPartOfFormerFile_class_EOF( PrintWriter pwClsFile )
    {
        ListIterator it = m_lstFormerFile.listIterator();
        while( it.hasNext() )
        {
            String sLine = (String) it.next();
            if( sLine != null && sLine.startsWith( "// RDFS2Class: end of class" ) ) break;
        }
        while( it.hasNext() )
        {
            String sLine = (String) it.next();
            if( sLine == null ) continue;
            if( sLine.trim().startsWith( "// EOF" ) ) 
                break;
            else
                pwClsFile.println( sLine );
        }
    }

    private Set getPropertiesOfClass( Resource resCls )
    {
        HashSet hs = new HashSet();
        Iterator it = m_setProperties.iterator();
        while( it.hasNext() )
        {
            PropertyInfo pi = (PropertyInfo) it.next();
            if( pi.setResDomain.contains( resCls ) ) hs.add( pi );
        }
        return hs;
    }


    private void fillClassFile( Resource resCls, String sPkg,
                                String sClsName, PrintWriter pwClsFile ) 
            throws Exception
    {
        // super class preparation
        String sSuperClassNS = null;
        String sSuperClassPkg = null;
        String sSuperClassName = null;
        Resource resSuperClass = null;
        Statement stmtSuperClass = resCls.getProperty( m_propRDFSPredSubClassOf );
        if( stmtSuperClass != null )
        {
            resSuperClass = stmtSuperClass.getResource();
            if( resSuperClass.equals( m_resRDFSClass ) )
            {
                warning( "# super class THING (not 'Class'!!) used for class '" + resCls + "'" );
            }
            else if( !resSuperClass.equals( m_resRDFSResource ) )
            {
                sSuperClassName = resSuperClass.getLocalName();
                sSuperClassNS = resSuperClass.getNameSpace();
                sSuperClassPkg = (String) m_mapNamespaceToPackage.get( sSuperClassNS );
                if( sSuperClassPkg == null ) 
                    throw new Exception( "Namespace '"
                        + sSuperClassNS + "' (class '" + sSuperClassName
                        + "') mapped to a package" );
            }
        }


        // package
        pwClsFile.println( "package " + sPkg + ";" );

        copyPartOfFormerFile_package_imports( pwClsFile );
        pwClsFile.println();

        // imports
        pwClsFile.println( "// RDFS2Class: imports" );
        pwClsFile.println( "import java.io.Serializable;" );
        pwClsFile.println( "import java.util.Collection;" );
        pwClsFile.println();
        pwClsFile.println( "import com.hp.hpl.jena.rdf.model.Model;" );
        pwClsFile.println( "import com.hp.hpl.jena.rdf.model.Resource;" );
        pwClsFile.println( "import com.hp.hpl.jena.rdf.model.Property;" );
        pwClsFile.println( "import de.dfki.km.jena2java.JenaResourceWrapper;" );
        pwClsFile.println( "import de.dfki.km.jena2java.ObjectTracker;" );
        if( sSuperClassPkg != null && !sPkg.equals( sSuperClassPkg ) ) 
            pwClsFile.println( "import " + sSuperClassPkg + ".*;" );
        pwClsFile.println( "// RDFS2Class: end of imports" );

        copyPartOfFormerFile_imports_class( pwClsFile );
        pwClsFile.println( "\n" );

        // class declaration
        String sClassDeclSpec = "public class ";
        pwClsFile.println( "/** RDFS2Class: class " + sClsName + "\n  * <p>\n  */" );
        pwClsFile.println( sClassDeclSpec + " " + sClsName );

        // super class declaration
        if( sSuperClassName != null ) 
            pwClsFile.println( "    extends " + sSuperClassName );
        else
            pwClsFile.println( "    extends JenaResourceWrapper" );

        pwClsFile.println( "    implements Serializable" );
        pwClsFile.println( "{" );

        String sIndent = "    ";
        String sSeparator = SEPARATOR;

        // constructors
        pwClsFile.println( sIndent + "// RDFS2Class: begin constructors" );

        String sFieldNameCls = fieldName( resCls, m_defaultNamespace );

        String sRdfType = null;
        if( m_sJenaConstantsClass != null )
            sRdfType = m_sJenaConstantsClass + "." + sFieldNameCls;
        else
        {
            sRdfType = "RESOURCE_" + sFieldNameCls;
            pwClsFile.println( sIndent + "Property " + sRdfType + " = JenaResourceWrapper.m_defaultModel.createProperty( \"" + resCls.getURI() + "\" );" );
        }

        pwClsFile.println( sIndent + "/**" );
        pwClsFile.println( sIndent + " * Creates a new anonymous instance of the RDFS class <code>" + sClsName + "</code> along with its wrapper and");
        pwClsFile.println( sIndent + " * register it with an {@link ObjectTracker}.");
        pwClsFile.println( sIndent + " * To get wrapper instances for existing Jena resources, use <code>ObjectTracker.getInstance</code>.");
        pwClsFile.println( sIndent + " * @param tracker The tracker the new instance shall be registered with.");
        pwClsFile.println( sIndent + " * @param model The Jena model to create the new instance in.");
        pwClsFile.println( sIndent + " */" );
        pwClsFile.println( sIndent + "public " + sClsName + "(ObjectTracker tracker, Model model )" );
        pwClsFile.println( sIndent + "{" );
        pwClsFile.println( sIndent + "    super(tracker, model, " + sRdfType + " );");
        pwClsFile.println( sIndent + "}\n" );

		pwClsFile.println( sIndent + "/**" );
        pwClsFile.println( sIndent + " * Creates a new instance of the RDFS class <code>" + sClsName + "</code> along with its wrapper and");
        pwClsFile.println( sIndent + " * register it with an {@link ObjectTracker}.");
        pwClsFile.println( sIndent + " * To get wrapper instances for existing Jena resources, use <code>ObjectTracker.getInstance</code>.");
        pwClsFile.println( sIndent + " * @param tracker The tracker the new instance shall be registered with.");
        pwClsFile.println( sIndent + " * @param model The Jena model to create the new instance in.");
        pwClsFile.println( sIndent + " * @param uri The new instance's URI.");
        pwClsFile.println( sIndent + " */" );
        pwClsFile.println( sIndent + "public " + sClsName + "(ObjectTracker tracker, Model model, String uri)" );
        pwClsFile.println( sIndent + "{" );
        pwClsFile.println( sIndent + "    super(tracker, model, " + sRdfType + ", uri);" );
        pwClsFile.println( sIndent + "}\n" );
        
        pwClsFile.println( sIndent + "/**");
        pwClsFile.println( sIndent + " * Using this method is usually a mistake.");
        pwClsFile.println( sIndent + " * Creates a new wrapper for an existing Jena RDF instance.");
        pwClsFile.println( sIndent + " * Registration with an <code>ObjectTracker</code> must be done elsewhere.");
        pwClsFile.println( sIndent + " * Typically this constructor should be called only by the <code>ObjectTracker</code>.");
        pwClsFile.println( sIndent + " * @param res The Jena RDF instance to be wrapped.");
        pwClsFile.println( sIndent + " */");
        pwClsFile.println( sIndent + "public " + sClsName + "( Resource res )");
        pwClsFile.println( sIndent + "{" );
        pwClsFile.println( sIndent + "    super(res);");
        pwClsFile.println( sIndent + "}");

		pwClsFile.println( sIndent + "protected " + sClsName + "(ObjectTracker tracker, Model model, Resource resource)" );
        pwClsFile.println( sIndent + "{" );
        pwClsFile.println( sIndent + "    super(tracker, model, resource);" );
        pwClsFile.println( sIndent + "}\n" );

		pwClsFile.println( sIndent + "protected " + sClsName + "(ObjectTracker tracker, Model model, Resource resource, String uri)" );
        pwClsFile.println( sIndent + "{" );
        pwClsFile.println( sIndent + "    super(tracker, model, resource, uri);" );
        pwClsFile.println( sIndent + "}\n" );

        pwClsFile.println( sIndent + "// RDFS2Class: end constructors" );
        pwClsFile.println();


        // getter and setter properties
        Set setProperties = getPropertiesOfClass( resCls );
        for( Iterator itProperties = setProperties.iterator(); itProperties.hasNext(); )
        {
            pwClsFile.println( sIndent + sSeparator );
            PropertyInfo pi = (PropertyInfo) itProperties.next();
            fillClassFile_property( pi, resCls, pwClsFile, sIndent );
        }

        // user code
        copyPartOfFormerFile_in_class( pwClsFile );

        // end of class file
        pwClsFile.println( "}" );
        pwClsFile.println( "// RDFS2Class: end of class " + sClsName );

        copyPartOfFormerFile_class_EOF( pwClsFile );

        pwClsFile.println( "// EOF\n" );
    }

    private void fillClassFile_property( PropertyInfo pi, Resource resCls,
                                         PrintWriter pwClsFile, String sIndent ) 
            throws Exception
    {
        pwClsFile.println( sIndent + "// RDFS2Class: begin property " + pi.resProperty.getURI() );
        String propertyMethodName = ( pi.resProperty.getNameSpace().equals( resCls.getNameSpace() ) )
                                    ? RDF2Java.makeMethodName( "", pi.resProperty.getLocalName() )
                                    : RDF2Java.makeMethodName( "", pi.resProperty.getNameSpace(), pi.resProperty.getLocalName() );
        
        String sPropertyConstant = null;
        String sFieldName = fieldName( pi.resProperty, m_defaultNamespace );
        if( m_sJenaConstantsClass != null )
            sPropertyConstant = m_sJenaConstantsClass + "." + sFieldName;
        else
        {
            sPropertyConstant = "PROPERTY_" + sFieldName;
            pwClsFile.println( sIndent + "Property " + sPropertyConstant + " = JenaResourceWrapper.m_defaultModel.createProperty( \"" + pi.resProperty.getURI() + "\" );" );
        }
        
        Object range = null;
        if( pi.setResRange.size() == 0 )
        {
            System.out.println( "Strange: " + sFieldName + " does not have a range?!" );
            range = m_modelRDFS.createResource( "http://whatever/bla#JenaResourceWrapper" );
        }
        else
            range = pi.setResRange.iterator().next();

        String rangeTypeName = null;
        boolean rangeIsObject = false;
        boolean rangeIsResource = false;
        if( range instanceof Resource )
        {
            if( range.equals( m_resRDFSLiteral ) ) rangeTypeName = "String";
            else
            {
                rangeTypeName = getRangeTypeName( (Resource)range );
                // rangeTypeName = ((Resource) range).getLocalName();
                rangeIsResource = true;
            }
            rangeIsObject = true;
        }
        else if( range.equals( Float.class ) ) rangeTypeName = "Float";
        else if( range.equals( Integer.class ) ) rangeTypeName = "Integer";
        else if( range.equals( Boolean.class ) ) rangeTypeName = "Boolean";
        String rangeVariableType = rangeIsObject
                                   ? rangeTypeName
                                   : rangeTypeName.toLowerCase();
        if( rangeVariableType.equals( "integer" ) ) rangeVariableType = "int";
        String rangeVariableName = "_" + propertyMethodName.toLowerCase();

        if( !pi.bMultiple )   // *** single value slots ***
        {
            // GETTER
            // public float getConfidence()
            pwClsFile.println( sIndent + "public " + rangeVariableType + " get" + propertyMethodName + "()\n" + sIndent + "{" );
            if( rangeIsObject )
                // return (String) readProperty(Constants.NAME_PROPERTY);
                pwClsFile.println( sIndent + "    return (" + rangeTypeName
                        + ") getPropertyObject( " + sPropertyConstant + " );\n"
                        + sIndent + "}" );
            else // range is a primitive type
            {
                // int needs special treatment as the method is Statement.getInt(), not Statement.getInteger()
                // return getProperty(Constants.SOMEINTEGER_PROPERTY).getInt();
                if (rangeTypeName.equals("Integer"))
                    pwClsFile.println( sIndent + "    return getProperty( " + sPropertyConstant + " ).getInt();\n" 
                        + sIndent + "}" );
                // return getProperty(Constants.CONFIDENCE_PROPERTY).getFloat();
                else // primitive types != int
                    pwClsFile.println( sIndent + "    return getProperty( " + sPropertyConstant + " ).get"
                        + rangeTypeName + "();\n" 
                        + sIndent + "}" );
            }
            // SETTER
            // public void setConfidence(float confidence)
            pwClsFile.println( sIndent + "public void set" + propertyMethodName
                    + "( " + rangeVariableType + " " + rangeVariableName
                    + " )\n" + sIndent + "{" );
            if( rangeIsObject )
                // setProperty(Constants.NAME_PROPERTY, name);
                pwClsFile.println( sIndent + "    setProperty( " + sPropertyConstant + ", "
                        + rangeVariableName + " );\n" 
                        + sIndent + "}" );
            else
                // setProperty(Constants.CONFIDENCE_PROPERTY, new
                // Float(confidence));
                pwClsFile.println( sIndent
                        + "    setProperty( " + sPropertyConstant + ", new "
                        + rangeTypeName + "(" + rangeVariableName + ") );\n"
                        + sIndent + "}" );
        }
        else   // *** multi value slots ***
        {
            // GETTER
            // public Collection getContainer()
            pwClsFile.println( sIndent + "public Collection get" + propertyMethodName + "()\n" 
                    + sIndent + "{" );
            if( rangeIsObject )
                // return
                // getPropertyInstanceReferences(Constants.CONTAINER_PROPERTY);
                pwClsFile.println( sIndent
                        + "    return getPropertyObjects( " + sPropertyConstant + " );\n"
                        + sIndent + "}" );
            else
                throw new RuntimeException( "Multiple non-objects as return values not supported yet." ); //TODO

            // SETTER
            // public void addContainer(Container container)
            pwClsFile.println( sIndent + "public void add" + propertyMethodName
                    + "( " + rangeVariableType + " " + rangeVariableName
                    + " )\n" + sIndent + "{" );
            if( rangeIsObject )
                // m_res.addProperty(Constants.CONTAINER_PROPERTY, container);
                pwClsFile.println( sIndent
                        + "    m_res.addProperty( " + sPropertyConstant + ", "
                        + rangeVariableName + " );\n" 
                        + sIndent + "}" );
            else
                throw new RuntimeException( "Adding Multiple non-Object values not supported yet." ); //TODO

            // CLEARER
            // public void clearContainer ()
            pwClsFile.println( sIndent + "public void clear" + propertyMethodName + "()\n" 
                    + sIndent + "{" );
            // m_res.removeAll(Constants.CONTAINER_PROPERTY);
            pwClsFile.println( sIndent + "    m_res.removeAll( " + sPropertyConstant + " );\n"
                            + sIndent + "}" );
        }

        pwClsFile.println( sIndent + "// RDFS2Class: end property " + pi.resProperty.getURI() );
        pwClsFile.println();
    }


    private String getRangeTypeName( Resource resource ) throws Exception
    {
        String sResourceNamespace = null;
        if( resource != null ) sResourceNamespace = resource.getNameSpace();
        String sResourcePackage = null;
        for( Iterator it = m_mapNamespaceToPackage.keySet().iterator(); it.hasNext(); )
        {
            String sNamespace = (String)it.next();
            if( sNamespace.equals( sResourceNamespace ) )
            {
                sResourcePackage = (String)m_mapNamespaceToPackage.get( sNamespace );
                break;
            }
        }
        if( sResourcePackage == null )
        {
            // use an "untyped" RDF resource
            warning( "no namespace-to-package mapping for range resource " + resource.getURI() );
            return "Resource";   // "com.hp.hpl.jena.rdf.model.Resource";
        }
        return sResourcePackage + "." + resource.getLocalName();
    }
    
    
} // end of class RDFS2Class



