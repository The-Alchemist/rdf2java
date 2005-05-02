package de.dfki.km.jena2java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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
    private static boolean m_bQuiet;

    private String m_sRDFSFile;
    private String m_sJenaConstantsClass;
    private Map m_mapNamespaceToPackage;
    private String m_sClsPath;

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

    
    public static void main( String[] args )
    {
        try
        {
            if( args.length < 1 )
                throw new Exception( "RDFS2Class: no parameters" );

            boolean bQuiet = false;

            int iArg = 0;
            while( args[iArg].startsWith( "-" ) )
            {
                String sFlags = args[iArg].substring( 1 );
                for( int i = 0; i < sFlags.length(); i++ )
                {
                    if( sFlags.charAt( i ) == 'q' ) 
                        bQuiet = true;
                    else
                        throw new Exception( "RDFS2Class: Option '" + sFlags.charAt( i ) + "' unknown." );
                }
                iArg++;
            }

            final String CHANGE_RDFS_NS = "rdfs=";
            if( args[iArg].toLowerCase().startsWith( CHANGE_RDFS_NS ) )
            {
                RDFS_NAMESPACE = args[iArg++].substring( CHANGE_RDFS_NS .length() );
                message( "RDFS namespace: " + RDFS_NAMESPACE );
            }

            final String CHANGE_RDF_NS = "rdf=";
            if( args[iArg].toLowerCase().startsWith( CHANGE_RDF_NS ) )
            {
                RDF_NAMESPACE = args[iArg++].substring( CHANGE_RDF_NS.length() );
                message( "RDF namespace : " + RDF_NAMESPACE );
            }

            final String CHANGE_PROTEGE_NS = "protege=";
            if( args[iArg].toLowerCase().startsWith( CHANGE_PROTEGE_NS ) )
            {
                PROTEGE_NS = args[iArg++].substring( CHANGE_PROTEGE_NS.length() );
                message( "Protege namespace: " + PROTEGE_NS );
            }

            String sRDFSFile = null;
            String sJenaConstantsClass = null;
            String sOutputSrcDir = null;

            if( args.length - iArg > 0 ) sRDFSFile = args[iArg++];
            else
                throw new Exception( "RDFS2Class: Missing parameter: RDFS file." );

            if( args.length - iArg > 0 ) sJenaConstantsClass = args[iArg++];
            else
                throw new Exception( "RDFS2Class: Missing parameter: Jena constants class." );

            if( args.length - iArg > 0 ) sOutputSrcDir = args[iArg++];
            else
                throw new Exception( "RDFS2Class: Missing parameter: output directory." );

            if( args.length - iArg <= 0 ) throw new Exception( "RDFS2Class: Missing mapping parameters." );
            if( ((args.length - iArg) % 2) != 0 ) throw new Exception( "RDFS2Class: Mapping parameters not correct." );

            if( !bQuiet )
            {
                message( "sRDFSFile             : " + sRDFSFile );
                message( "sJenaConstantsClass   : " + sJenaConstantsClass );
                message( "sOutputSrcDir         : " + sOutputSrcDir );
                message( "maping:" );
            }
            HashMap mapNS2Pkg = new HashMap();
            while( iArg < args.length )
            {
                String sNamespace = args[iArg++];
                String sPackage = args[iArg++];
                if( !bQuiet ) message( "  " + sNamespace + " -> " + sPackage );
                mapNS2Pkg.put( sNamespace, sPackage );
            }
            if( !bQuiet ) message( "" );

            // generate is near!
            RDFS2Class gen = new RDFS2Class( sRDFSFile, sJenaConstantsClass, sOutputSrcDir, mapNS2Pkg );
            gen.setQuiet( bQuiet );
            gen.createClasses();
        }
        catch( Exception ex )
        {
            String sMsg = ex.getMessage();
            if( sMsg != null && sMsg.startsWith( "RDFS2Class:" ) )
            {
                System.out.println( "\n" + sMsg 
                    + "\nusage: RDFS2Class [options] <file.rdfs> <JenaConstantsClass> <outputSrcDir> {<namespace> <package>}+\n"
                    + "options:  -q: quiet operation, no output\n"
                    + "          rdfs=<namespace>    : set different RDFS namespace\n"
                    + "          rdf=<namespace>     : set different RDF namespace\n"
                    + "          protege=<namespace> : set different Protege namespace\n" );
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

    public void setQuiet( boolean bQuiet )
    {
        m_bQuiet = bQuiet;
    }


    public RDFS2Class( String sRDFSFile, String sJenaConstantsClass, String sClsPath, Map mapNamespaceToPackage ) throws Exception
    {
        m_sRDFSFile = sRDFSFile;
        m_sJenaConstantsClass = sJenaConstantsClass;
        m_mapNamespaceToPackage = mapNamespaceToPackage;
        m_sClsPath = sClsPath;

        FileReader reader = new FileReader( m_sRDFSFile );
        m_modelRDFS = ModelFactory.createDefaultModel();
        m_modelRDFS.read( reader, "http://dummy.base.uri/" );

        initRdfsResources();
        initProtegeUris();
    }

    protected void initRdfsResources() throws Exception
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

    protected void initProtegeUris() throws Exception
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
        message( "examining properties..." );
        examineProperties();

        message( "creating the RDFS classes..." );
        createTheClasses();

        message( "ready." );
    }

    protected void examineProperties() throws Exception
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

    protected class PropertyInfo
    {
        Resource resProperty;
        boolean bMultiple = true;
        Set setResDomain;
        Set setResRange;

        protected PropertyInfo( Resource resProperty )
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
        }

    } // end of inner class PropertyInfo


    protected void createTheClasses() throws Exception
    {
        ResIterator it = m_modelRDFS.listSubjectsWithProperty( m_propRDFPredType, m_resRDFSClass );
        while( it.hasNext() )
        {
            Resource resCls = it.nextResource();
            createClass( resCls );
        }
    }


    protected void createClass( Resource resCls ) throws Exception
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
    }


    protected String packageToPath( String sPkg )
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


    protected void loadInFormerJavaFile( String sPathAndFilename ) throws Exception
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

    protected PrintWriter createFile( String sPath, String sFile )
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


    public void copyPartOfFormerFile_package_imports( PrintWriter pwClsFile )
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

    public void copyPartOfFormerFile_imports_class( PrintWriter pwClsFile )
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

    public void copyPartOfFormerFile_in_class( PrintWriter pwClsFile )
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

    public void copyPartOfFormerFile_class_EOF( PrintWriter pwClsFile )
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

    protected Set getPropertiesOfClass( Resource resCls )
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


    protected void fillClassFile( Resource resCls, String sPkg,
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
        pwClsFile.println( sIndent + "/**" );
        pwClsFile.println( sIndent + " * Create a new " + sClsName + " instance including its wrapper" );
        pwClsFile.println( sIndent + " */" );
        pwClsFile.println( sIndent + "public " + sClsName + "( Model model )" );
        pwClsFile.println( sIndent + "{" );
        pwClsFile.println( sIndent + "    super( model );" );
        pwClsFile.println( sIndent + "}" );

        pwClsFile.println( sIndent + "/**" );
        pwClsFile.println( sIndent + " * Create a new " + sClsName + " wrapping instance for an existing Jena resource" );
        pwClsFile.println( sIndent + " */" );
        pwClsFile.println( sIndent + "public " + sClsName + "( Resource res )" );
        pwClsFile.println( sIndent + "{" );
        pwClsFile.println( sIndent + "    super( res );" );
        pwClsFile.println( sIndent + "}" );
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

    protected void fillClassFile_property( PropertyInfo pi, Resource resCls,
                                           PrintWriter pwClsFile, String sIndent ) 
            throws Exception
    {
        pwClsFile.println( sIndent + "// RDFS2Class: begin property " + pi.resProperty.getURI() );
        String propertyMethodName = RDF2Java.makeMethodName( "", pi.resProperty.getLocalName() );
        
        String sPropertyConstant = null;
        if( m_sJenaConstantsClass != null )
            sPropertyConstant = m_sJenaConstantsClass + "." + pi.resProperty.getLocalName();
        else
        {
            sPropertyConstant = "PROPERTY_" + pi.resProperty.getLocalName();
            pwClsFile.println( sIndent + "Property " + sPropertyConstant + " = JenaResourceWrapper.m_defaultModel.createProperty( \"" + pi.resProperty.getURI() + "\" );" );
        }
        
        Object range = null;
        if( pi.setResRange.size() == 0 )
        {
            System.out.println( "Strange: " + pi.resProperty.getLocalName() + " does not have a range?!" );
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
        String rangeVariableName = propertyMethodName.toLowerCase();

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
            else
                // return (float)
                // readProperty(Constants.CONFIDENCE_PROPERTY)).floatValue();
                pwClsFile.println( sIndent + "    return ((" + rangeTypeName
                        + ") getPropertyObject( " + sPropertyConstant + " )."
                        + rangeVariableType + "Value();\n" 
                        + sIndent + "}" );

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
            // public void putContainer(Container container)
            pwClsFile.println( sIndent + "public void put" + propertyMethodName
                    + "( " + rangeVariableType + " " + rangeVariableName
                    + " )\n" + sIndent + "{" );
            if( rangeIsObject )
                // m_res.addProperty(Constants.CONTAINER_PROPERTY, container);
                pwClsFile.println( sIndent
                        + "    m_res.addProperty( " + sPropertyConstant + ", "
                        + rangeVariableName + " );\n" 
                        + sIndent + "}" );
            else
                throw new RuntimeException( "Multiple non-objects as put values not supported yet." ); //TODO

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


    /**
     * @param resource
     * @return
     */
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



