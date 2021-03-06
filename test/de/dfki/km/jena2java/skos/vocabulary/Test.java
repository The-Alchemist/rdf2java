package de.dfki.km.jena2java.skos.vocabulary;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.dfki.km.jena2java.ObjectTracker;
import de.dfki.rdf.util.RDFTool;


public class Test
{
	final static boolean DO_WRITE_READ = false;
	
    ConceptScheme schemeTopics;
    Concept cTopics;
    Concept cDFKI;
    Concept cEPOS;
    Concept cFRODO;
    Concept cBadConcept;
    
    ObjectTracker OT;
    Model model;
    
	
	public static void main(String[] args) 
	{
		new Test().go();
	}

	public Test()
	{
		initJavaObjects();
	}
	
	private void initJavaObjects()
	{
        String NS   = "http://km.dfki.de/concepts#";

        OT = ObjectTracker.getInstance();
        SKOS.register( OT );
        model = ModelFactory.createDefaultModel();
        
        schemeTopics = new ConceptScheme( OT, model, NS + "TopicsSchmeme" );
        schemeTopics.setRdfsLabel( "TopicsSchmeme" );
        
        // generate some topics using jena2java
        
        cTopics = new Concept( OT, model, NS + "TOPICS" );
        cTopics.setRdfsLabel( "TOPICS" );
        cTopics.addInScheme( schemeTopics );
        schemeTopics.addHasTopConcept( cTopics );
        
        cDFKI = new Concept( OT, model );
        cDFKI.setRdfsLabel( "DFKI" );
        cDFKI.addInScheme( schemeTopics );
        
        cEPOS = new Concept( OT, model );
        cEPOS.setRdfsLabel( "EPOS" );
        cEPOS.addInScheme( schemeTopics );
        
        cFRODO = new Concept( OT, model );
        cFRODO.setRdfsLabel( "FRODO" );
        cFRODO.addInScheme( schemeTopics );
        
        cTopics.addNarrower( cDFKI );
        cDFKI.addNarrower( cEPOS );
        cDFKI.addNarrower( cFRODO );        

        // generate a topic using pure jena
        
        Resource resDecor = model.createResource();
        resDecor.addProperty( RDF.type, SKOS.Concept );
        resDecor.addProperty( RDFS.label, "DECOR" );
        resDecor.addProperty( SKOS.inScheme, schemeTopics.getResource() );
        cDFKI.getResource().addProperty( SKOS.narrower, resDecor );
        
        // generate a topic that will be deleted soon
        
        cBadConcept = new Concept( OT, model );
        cBadConcept.setRdfsLabel( "BAD CONCEPT" );
        cBadConcept.addInScheme( schemeTopics );
        
        cTopics.addNarrower( cBadConcept );
        
	}
	
	public void go()
	{
		System.out.println( "schemeTopics: " + schemeTopics );
        Collection collTopConcepts = schemeTopics.getHasTopConcept();
        for( Iterator it = collTopConcepts.iterator(); it.hasNext(); )
        {
            Object o = it.next();
            Concept concept = (Concept)o;
            System.out.println( "\ttop concept: " + concept );
        }        
        System.out.println();
        
        printConceptTree( "", " +  ", cTopics );        

        System.out.println( RDFTool.modelToString( model ) );
        // cTopics.removeNarrower( cBadConcept );

        Model removeModel = ModelFactory.createDefaultModel();
        Set/*Resource*/ setNeighboringResources = new HashSet();
        OT.deleteInstance( cBadConcept, removeModel, setNeighboringResources );
        System.out.println( "removeModel:" + RDFTool.modelToString( removeModel ) );
        System.out.println( "neighboring resources:" );
        for( Iterator it = setNeighboringResources.iterator(); it.hasNext(); )
        	System.out.println( " -  " + it.next() );
        System.out.println();

        printConceptTree( "", " +  ", cTopics );        
        System.out.println( RDFTool.modelToString( model ) );
		
        if( DO_WRITE_READ )
        {
	        try
	        {
	            // write jena model
	            String sFilename = "test/de/dfki/rdf/test/jena2java/testdata.rdf";
	            String sModelAsString = RDFTool.modelToString( model );
	            System.out.println( "\nmodel:\n" + sModelAsString );
	            FileWriter writer = new FileWriter( sFilename );       
	            writer.write( sModelAsString );
	            writer.flush(); writer.close();
	            
	            // read jena model
	            FileReader reader = new FileReader( sFilename );
	            Model newModel = ModelFactory.createDefaultModel();
	            newModel.read( reader, "http://dummy.base.uri/" );
	            String sNewModelAsString = RDFTool.modelToString( newModel );
	            System.out.println( "\nmodel:\n" + sNewModelAsString );
	        }
	        catch( Exception ex ) 
	        {
	            ex.printStackTrace();
	        }
        }
	}
    
    public void printConceptTree( String sCurrentIndent, String sIndent, Concept concept )
    {
        System.out.println( sCurrentIndent + concept.toString() );
        Collection/*Concept*/ collNarrower = concept.getNarrower();
        for( Iterator it = collNarrower.iterator(); it.hasNext(); )
        {
            Concept subConcept = (Concept)it.next();
            printConceptTree( sCurrentIndent + sIndent, sIndent, subConcept );
        }
    }   
	
}

