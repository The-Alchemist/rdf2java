package dfki.rdf.util;

import de.dfki.util.rdf.RDF;

import org.w3c.rdf.model.Model;
import org.w3c.rdf.model.Statement;
import org.w3c.rdf.util.RDFFactory;
import org.w3c.rdf.syntax.RDFParser;
import org.w3c.rdf.syntax.RDFSerializer;

import java.util.*;


public class RDFDump
{
    public static void main( String[] args )
    {
        try
        {
            if( args.length < 1 )
            {
                System.out.println( "\nusage: RDFDump {<rdf-file>}+\n" );
                System.exit( 1 );
            }

            RDFFactory rdfFactory = RDF.factory();
            RDFParser rdfParser = rdfFactory.createParser();
            Model model = rdfFactory.createModel();

            // read in RDF (plain)
            for( int i = 0; i < args.length; i++ )
            {
                RDF.parse( args[i], rdfParser, model );
            }

            // print out plain RDF
            StringBuffer sb = new StringBuffer();
            for( Enumeration enum = model.elements(); enum.hasMoreElements(); )
            {
                Statement s = (Statement)enum.nextElement();
                sb.append( s.subject() + "\t" + s.predicate() + "\t" + s.object() + "\n" );
            }
            System.out.println( sb.toString() );

            ////    // print out XML serialized RDF
            ////    RDFSerializer rdfSerializer = rdfFactory.createSerializer();
            ////    RDF.dumpModel( model, System.out, rdfSerializer );
        }
        catch( Exception ex )
        {
            System.err.println( ex );
            ex.printStackTrace( System.err );
        }
    }

} // end of class RDFDump

