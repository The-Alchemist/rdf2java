package de.dfki.rdf.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class ToStringPackedWalkerController extends JavaGraphWalker.WalkerController
{
    StringBuffer sb;
    ToStringController tsc;
    Map/*String->String*/ mapPkg2NS;

    
    public ToStringPackedWalkerController( Map/*String->String*/ mapPkg2NS, String sRdfsNamespace, 
                                           ToStringController tsc )
    {
        sb = new StringBuffer();
        this.tsc = tsc;
        this.mapPkg2NS = mapPkg2NS;
    }
    
    public String serialzeAsString()
    {
        return sb.toString();
    }
    
    private void indent( int n )
    {
        for( int i = 0; i < n; i++ )
        {
            sb.append( "    " );
        }
    }
    
    public boolean arriving( RDFResource currentResource )
    {
        RDFResource penultimateResource = null;
        if( lstPath.size() > 3 ) 
            penultimateResource = (RDFResource)lstPath.get( lstPath.size()-3 );
        
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
            indent( lstPath.size()-4 );
            sb.append( "->  " + sLastProperty + ":\n" );
        }

        indent( lstPath.size()-2 );
        sb.append( sClsName + " (" + sClsPackage + "." + sClsName + ") " + currentResource.getAddress() );
        if( currentResource.getURI() != null )
            sb.append( " URI=\"" + currentResource.getURI() + "\"" );
        sb.append( "\n" );
        
        if(     currentResource.getLabel() != null                              &&
                !currentResource.getLabel().equals( currentResource.getURI() )  &&
                !tsc.hideProperty( currentResource, "label" )                   )
        {
            indent( lstPath.size()-2 );
            sb.append( "->  label: " + currentResource.getLabel() + "\n" );
        }
        Collection/*PropertyInfo*/ collPropInfos = currentResource.getPropertyStore().getPropertyInfos();
        for( Iterator it = collPropInfos.iterator(); it.hasNext(); )
        {
            PropertyInfo pi = (PropertyInfo)it.next();
            if( pi.getValue() == null ) continue;
            if( tsc.hideProperty( currentResource, pi.getName() ) ) continue;
            if( pi.getValueType() == PropertyInfo.VT_STRING ||
                pi.getValueType() == PropertyInfo.VT_SYMBOL )
            {
                if( pi.hasMultiValue() )
                {
                    Collection collValues = (Collection)pi.getValue();
                    if( collValues.size() > 0 )
                    {
                        indent( lstPath.size()-2 );
                        sb.append( "->  " + pi.getName() + ":\n" );
                        for( Iterator itValues = collValues.iterator(); itValues.hasNext(); )
                        {
                            String sValue = (String)itValues.next();
                            indent( lstPath.size()-0 );
                            sb.append( sValue + "\n" );
                        }
                    }
                }
                else
                {
                    indent( lstPath.size()-2 );
                    sb.append( "->  " + pi.getName() + ": " + (String)pi.getValue() + "\n" );
                }
            }
        }
        
        return true;
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
        
        String sLastProperty = getLastProperty();
        if( sLastProperty != null ) 
        {
            Class cls = currentResource.getClass();
            String sClsPackage = RDFResource.getClassPackage( cls );
            String sClsName = RDFResource.getClassName( cls );
            String sNamespace = ( mapPkg2NS != null  ?  (String)mapPkg2NS.get( sClsPackage )  :  "http://" + sClsPackage + "#" );
            RDFResource resLastProperty = new RDFResource( sNamespace, sLastProperty );
            
            indent( lstPath.size()-4 );
            sb.append( "->  " + sLastProperty + ":\n" );
            
            indent( lstPath.size()-2 );
            sb.append( "<" + sClsName + " " + currentResource.getAddress() );
            if( currentResource.getURI() != null )
                sb.append( " URI=\"" + currentResource.getURI() + "\"" );
            sb.append( "\n" );
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
    
    
} // end of class ToStringPackedWalkerController

