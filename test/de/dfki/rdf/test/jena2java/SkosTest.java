package de.dfki.rdf.test.jena2java;

import java.util.Collection;
import java.util.Iterator;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.dfki.rdf.util.RDFTool;


public class SkosTest
{
    SkosConceptScheme schemeTopics;
    SkosConcept cTopics;
    SkosConcept cDFKI;
    SkosConcept cEPOS;
    SkosConcept cFRODO;
	
	public static void main(String[] args) 
	{
		new SkosTest().go();
	}

	public SkosTest()
	{
		initJavaObjects();
	}
	
	private void initJavaObjects()
	{
        String NS   = "http://km.dfki.de/concepts#";

        
        schemeTopics = new SkosConceptScheme( NS + "TopicsSchmeme" );
        schemeTopics.setPrefLabel( "TopicsSchmeme" );
        
        
        cTopics = new SkosConcept( NS + "TOPICS" );
        cTopics.setPrefLabel( "TOPICS" );
        cTopics.setInScheme( schemeTopics );
        schemeTopics.addHasTopConcept( cTopics );
        
        cDFKI = new SkosConcept();
        cDFKI.setPrefLabel( "DFKI" );
        cDFKI.setInScheme( schemeTopics );
        
        cEPOS = new SkosConcept();
        cEPOS.setPrefLabel( "EPOS" );
        cEPOS.setInScheme( schemeTopics );
        
        cFRODO = new SkosConcept();
        cFRODO.setPrefLabel( "FRODO" );
        cFRODO.setInScheme( schemeTopics );
        
        cTopics.addNarrower( cDFKI );
        cDFKI.addNarrower( cEPOS );
        cDFKI.addNarrower( cFRODO );        
	}
	
	public void go()
	{
		System.out.println( "schemeTopics: " + schemeTopics );
        for( Iterator it = schemeTopics.listHasTopConcept().iterator(); it.hasNext(); )
        {
            SkosConcept concept = (SkosConcept)it.next();
            System.out.println( "\ttop concept: " + concept );
        }        
        System.out.println();
        
        printConceptTree( "", " +  ", cTopics );        
	}
    
    public void printConceptTree( String sCurrentIndent, String sIndent, SkosConcept concept )
    {
        System.out.println( sCurrentIndent + concept.toString() );
        Collection/*SkosConcept*/ collNarrower = concept.listNarrower();
        for( Iterator it = collNarrower.iterator(); it.hasNext(); )
        {
            SkosConcept subConcept = (SkosConcept)it.next();
            printConceptTree( sCurrentIndent + sIndent, sIndent, subConcept );
        }
    }   
	
}

