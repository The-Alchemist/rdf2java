package dfki.rdf.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import dfki.rdf.util.nice.tinyxmldoc.TinyXMLDocument;
import dfki.rdf.util.nice.tinyxmldoc.TinyXMLElement;
import dfki.rdf.util.nice.tinyxmldoc.TinyXMLTextNode;
import dfki.util.rdf.RDF;


public class ToStringAsRdfWalkerController extends JavaGraphWalker.WalkerController
{
    RDFResource resPredAbout = new RDFResource( RDF.DEFAULT_SYNTAX_NAMESPACE, "about" );
    RDFResource resPredResource = new RDFResource( RDF.DEFAULT_SYNTAX_NAMESPACE, "resource" );
    
    TinyXMLDocument xmlDoc;
    TinyXMLElement elDoc;
    ToStringController tsc;
    
    Map/*String->String*/ mapPkg2NS;
    Map/*RDFResource->TinyXMLElement*/ mapResource2XMLElement = new HashMap();

    
    public ToStringAsRdfWalkerController( Map/*String->String*/ mapPkg2NS, String sRdfsNamespace, 
                                          ToStringController tsc )
    {
        xmlDoc = new TinyXMLDocument( null, sRdfsNamespace );
        elDoc = xmlDoc.createElement( RDF.DEFAULT_SYNTAX_NAMESPACE + "RDF" );
        xmlDoc.setDocumentElement( elDoc );
        this.tsc = tsc;
        this.mapPkg2NS = mapPkg2NS;
    }
    
    public TinyXMLDocument getXMLDocument()
    {
        return xmlDoc;
    }
    
    public String serialzeXMLDocumentAsString()
    {
        return xmlDoc.serialize();
    }
    
    public boolean arriving( RDFResource currentResource )
    {
        TinyXMLElement elParent = elDoc;
        RDFResource penultimateResource = null;
        if( lstPath.size() > 3 ) 
        {
            penultimateResource = (RDFResource)lstPath.get( lstPath.size()-3 );
            elParent = (TinyXMLElement)mapResource2XMLElement.get( penultimateResource );
        }
        
        TinyXMLElement elAppendHere = elParent;

        Class cls = currentResource.getClass();
        String sClsPackage = RDFResource.getClassPackage( cls );
        String sClsName = RDFResource.getClassName( cls );
        String sNamespace = ( mapPkg2NS != null  ?  (String)mapPkg2NS.get( sClsPackage )  
                                                 :  "http://" + sClsPackage + "#" );

        String sLastProperty = getLastProperty();
        if( sLastProperty != null ) 
        {
            if( !tsc.expandProperty( penultimateResource, sLastProperty, currentResource ) )
            {
                arrivingAgain( currentResource );
                return false;
            }
            RDFResource resLastProperty = new RDFResource( sNamespace, sLastProperty );
            TinyXMLElement elLastProperty = xmlDoc.createElement( resLastProperty.getURI() );
            elParent.appendChild( elLastProperty );
            elAppendHere = elLastProperty;
        }

        RDFResource resCls = new RDFResource( sNamespace, sClsName );
        
        TinyXMLElement elInst = xmlDoc.createElement( resCls.getURI() );
        elInst.setAttribute( resPredAbout.getURI(), currentResource );
        elAppendHere.appendChild( elInst );
        
        mapResource2XMLElement.put( currentResource, elInst );

        if(     currentResource.getLabel() != null                              &&
                !currentResource.getLabel().equals( currentResource.getURI() )  &&
                !tsc.hideProperty( currentResource, "label" )                   )
        {
            RDFResource resProperty = new RDFResource( xmlDoc.RDFS_NAMESPACE, "label" );
            elInst.setAttribute( resProperty.getURI(), currentResource.getLabel() );
        }
        Collection/*PropertyInfo*/ collPropInfos = currentResource.getPropertyStore().getPropertyInfos();
        for( Iterator it = collPropInfos.iterator(); it.hasNext(); )
        {
            PropertyInfo pi = (PropertyInfo)it.next();
            if( pi.getValue() == null ) continue;
            if( tsc.hideProperty( currentResource, pi.getName() ) ) continue;
            if(     pi.getValueType() == PropertyInfo.VT_STRING ||
                    pi.getValueType() == PropertyInfo.VT_SYMBOL )
            {
                if( pi.hasMultiValue() )
                {
                    Collection collValues = (Collection)pi.getValue();
                    if( collValues.size() > 0 )
                    {
                        for( Iterator itValues = collValues.iterator(); itValues.hasNext(); )
                        {
                            String sValue = (String)itValues.next();
                            RDFResource resProperty = new RDFResource( sNamespace, pi.getName() );
                            appendSlot( elInst, resProperty, sValue );
                        }
                    }
                }
                else
                {
                    RDFResource resProperty = new RDFResource( sNamespace, pi.getName() );
                    appendSlot( elInst, resProperty, (String)pi.getValue() );
                }
            }
        }
        
        return true;
    }

    private void appendSlot( TinyXMLElement elInst, RDFResource resPred, String sValue )
    {
        if( TinyXMLTextNode.containsIllegalChars( sValue ) )
        {
            TinyXMLElement elSlot = xmlDoc.createElement( resPred.getURI() );
            elInst.appendChild( elSlot );
            TinyXMLTextNode txtValue = createTextNode( sValue );
            elSlot.appendChild( txtValue );
        }
        else
        {
            elInst.setAttribute( resPred.getNamespace() + resPred.getLocalName(), sValue );
        }
    }

    private TinyXMLTextNode createTextNode( String sText )
    {
        if( TinyXMLTextNode.containsIllegalChars( sText ) )
            return xmlDoc.createCDATA( sText );
        else
            return xmlDoc.createTextNode( sText );
    }

    Set/*RDFResource*/ setProcessedResources = new HashSet();
        
    public void leaving( RDFResource currentResource )
    {
        boolean bAddResourceAsProcessed = true;
        if( lstPath.size() > 3 ) 
        {
            RDFResource penultimateResource = (RDFResource)lstPath.get( lstPath.size()-3 );
            String sLastProperty = getLastProperty();
            if( sLastProperty != null ) 
            {
                if( !tsc.expandProperty( penultimateResource, sLastProperty, currentResource ) )
                    bAddResourceAsProcessed = false;
            }
        }
        
        if( bAddResourceAsProcessed )
            setProcessedResources.add( currentResource );
    }
    
    public boolean arrivingAgain( RDFResource currentResource )
    {
        if( lstPath.size() <= 3 )
            return false;

        RDFResource penultimateResource = (RDFResource)lstPath.get( lstPath.size()-3 );
        TinyXMLElement elParent = (TinyXMLElement)mapResource2XMLElement.get( penultimateResource );
        
        String sLastProperty = getLastProperty();
        if( sLastProperty != null ) 
        {
            Class cls = currentResource.getClass();
            String sClsPackage = RDFResource.getClassPackage( cls );
            String sNamespace = ( mapPkg2NS != null  ?  (String)mapPkg2NS.get( sClsPackage )  :  "http://" + sClsPackage + "#" );
            RDFResource resLastProperty = new RDFResource( sNamespace, sLastProperty );
            
            TinyXMLElement elLastProperty = xmlDoc.createElement( resLastProperty.getURI() );
            elParent.appendChild( elLastProperty );
            
            elLastProperty.setAttribute( resPredResource.getURI(), currentResource );
        }
        else
            throw new Error( "failure in RDFResource.walk" );
        
        return false;
    }
    
    public boolean walkingAllowed( RDFResource source, String prop, RDFResource dest )
    {
        if( tsc.hideProperty( source, prop ) )
            return false;
        
        if( lstPath.size() > 3 ) 
        {
            RDFResource penultimateResource = (RDFResource)lstPath.get( lstPath.size()-3 );
            String sLastProperty = getLastProperty();
            if( sLastProperty != null ) 
            {
                if( !tsc.expandProperty( penultimateResource, sLastProperty, source ) )
                    return false;
            }
        }
        
        if( alreadyVisitedOnWalk( source ) )
        {
            if( alreadyVisitedOnPath( source ) )
                return false;
            else
                return !setProcessedResources.contains( source );
        }
        return true;
    }
    
    public int propertyImportance( RDFResource source, String prop )
    {
        return tsc.propertyImportance( source, prop );
    }


} // end of class ToStringWalkerController

