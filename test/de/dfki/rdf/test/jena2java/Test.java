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


public class Test
{
	Model m_model;
	Resource resRoot, res1, res2;
	DomainConcept dcRoot, dc1, dc2;
	
	
	public static void main(String[] args) 
	{
		new Test().go();
	}

	public Test()
	{
		initJenaModel();
		initJavaObjects();
	}
	
	private void initJenaModel()
	{
        // neues model erstellen:
        m_model = ModelFactory.createDefaultModel();

        String NS   = "http://km.dfki.de/concepts#";
        
        // möglichkeiten, Resourcen zu erstellen (eine property ist auch eine Resource!)
        resRoot = m_model.createResource( NS + "ROOT_CATEGORY" ); 	// mit URI
        res1 = m_model.createResource( new AnonId() );         	// anonymous resource
        res2 = m_model.createResource( DOMAIN.DomainConcept );     // anonymous resource von einem typ

        // properties (kanten) hinzufügen
        resRoot.addProperty( RDFS.label, "ROOT_CATEGORY" );
        resRoot.addProperty( DOMAIN.name, "ROOT_CATEGORY" );
        resRoot.addProperty( RDF.type, DOMAIN.DomainConcept );

        res1.addProperty( RDFS.label, "DFKI" );
        res1.addProperty( DOMAIN.name, "DFKI" );
        res1.addProperty( RDF.type, DOMAIN.DomainConcept );
        
        res2.addProperty( DOMAIN.name, "EPOS" );
        res2.addProperty( RDFS.label, "EPOS" );

        
        resRoot.addProperty( DOMAIN.directSubConcepts, res1 );
        resRoot.addProperty( DOMAIN.directSubConcepts, res2 );
        res1.addProperty( DOMAIN.directSubConcepts, res2 );
	}
	
	private void initJavaObjects()
	{
		dcRoot = new DomainConcept( resRoot );
		dc1 = new DomainConcept( res1 );
		dc2 = new DomainConcept( res2 );
	}

	public void go()
	{
		System.out.println( "dcRoot->uri  : " + dcRoot.getResource().getURI() );
		System.out.println( "dcRoot->name : " + dcRoot.getName() );
		dcRoot.setName( "CONCEPTS_ROOT" );
		System.out.println( "dcRoot->name : " + dcRoot.getName() );
		System.out.println( "dcRoot.toString(): " + dcRoot.toString() );
        
        System.out.println();
        printDomainConceptTree( "", " +  ", dcRoot );
        
        //System.out.println( "\nmodel:\n" + RDFTool.modelToString( dcRoot.getModel() ) );
        
        DomainConcept dcSmartFlow = new DomainConcept();
        dcSmartFlow.setName( "SmartFlow" );
        dc1.addDirectSubConcept( dcSmartFlow );
        System.out.println();
        printDomainConceptTree( "", " +  ", dcRoot );        
	}
    
    public void printDomainConceptTree( String sCurrentIndent, String sIndent, DomainConcept dc )
    {
        System.out.println( sCurrentIndent + dc.toString() );
        Collection/*DomainConcept*/ collSubConcepts = dc.getDirectSubConcepts();
        for( Iterator it = collSubConcepts.iterator(); it.hasNext(); )
        {
            DomainConcept dcSubConcept = (DomainConcept)it.next();
            printDomainConceptTree( sCurrentIndent + sIndent, sIndent, dcSubConcept );
        }
    }   
	
}

