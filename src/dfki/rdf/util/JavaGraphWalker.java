/*
 * Created on 06.08.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dfki.rdf.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dfki.util.debug.Debug;

/**
 * Walks along a graph of RDFResources. 
 */
public class JavaGraphWalker
{
    WalkerController wc;
    

    //---------------------------------------------------------------------
    public JavaGraphWalker( WalkerController wc )
    {
        this.wc = wc;
    }

    public List/* {String,RDFResource}+ */ getWholeWalk()
    {
        return wc.lstWalk;
    }
    
    //---------------------------------------------------------------------    
    public void walk( RDFResource startResource )
    {
        walk( null, startResource );
    }


    //---------------------------------------------------------------------
    public void walk( String sLastProperty, RDFResource currentResource )
    {
        wc.lstPath.addLast( sLastProperty );
        wc.lstPath.addLast( currentResource );
        
        wc.lstWalk.addLast( sLastProperty );
        wc.lstWalk.addLast( currentResource );

        boolean bProceedFurther;
        boolean bAlreadyVisited = wc.alreadyVisitedOnWalk( currentResource ); 
        boolean bNotFirstResource = ( wc.lstPath.indexOf( currentResource ) > 1 ); 
        if( bAlreadyVisited && bNotFirstResource )
            bProceedFurther = wc.arrivingAgain( currentResource );
        else
            bProceedFurther = wc.arriving( currentResource );
        

        if( bProceedFurther )
            walk_action( sLastProperty, currentResource );
        
        
        if( bAlreadyVisited )
            wc.leavingAgain( currentResource );
        else
            wc.leaving( currentResource );
        
        if( wc.lstPath.removeLast() !=  currentResource )
            debug().error( "implementation failure in THING.walk" );
        if( wc.lstPath.removeLast() !=  sLastProperty )
            debug().error( "implementation failure in THING.walk" );
        
        wc.lstWalk.addLast( "inv(" + sLastProperty + ")" );
        if( wc.lstPath.size() > 0 ) wc.lstWalk.addLast( wc.lstPath.getLast() );
    }


    //---------------------------------------------------------------------
    public void walk_action( String sLastProperty, RDFResource currentResource )
    {
        final Map/*String->int*/ mapProperties2Importance = new HashMap();
        for( Iterator itProperties = currentResource.m_propertyStore.getProperties().iterator(); itProperties.hasNext(); )
        {
            String sPropName = (String)itProperties.next();
            int val = wc.propertyImportance( currentResource, sPropName );
            mapProperties2Importance.put( sPropName, new Integer( val ) );
        }
        LinkedList/*String*/ lstProperties = new LinkedList( mapProperties2Importance.keySet() );
        Collections.sort( lstProperties, new Comparator() {
            public int compare( Object o1, Object o2 )
            {
                int i1 = ((Integer)mapProperties2Importance.get( o1 )).intValue();
                int i2 = ((Integer)mapProperties2Importance.get( o2 )).intValue();
                if( i1 != i2 ) return i2 - i1;
                return ((String)o1).compareTo( (String)o2 );
            }
        });
        
        for( Iterator itProperties = lstProperties.iterator(); itProperties.hasNext(); )
        {
            String sPropName = (String)itProperties.next();
            if( !wc.walkingAllowed( currentResource, sPropName ) )
                continue;
            
            PropertyInfo pi = currentResource.m_propertyStore.getPropertyInfo( sPropName );
            Collection/*RDFResource*/ collDestinations = new LinkedList();
            if( pi.hasMultiValue() )
            {
                Collection collPropValues = (Collection)pi.getValue();
                for( Iterator it = collPropValues.iterator(); it.hasNext(); )
                {
                    Object value = it.next();
                    if( !(value instanceof RDFResource) )
                        continue;
                    RDFResource resDest = (RDFResource)value;
                    if( wc.walkingAllowed( currentResource, sPropName, resDest ) )
                        collDestinations.add( resDest );
                }
            }
            else
            {
                Object value = pi.getValue();
                if( value != null && (value instanceof RDFResource) )
                {
                    if( wc.walkingAllowed( currentResource, sPropName, (RDFResource)value ) )
                        collDestinations.add( value );
                }
            }

            if( collDestinations.size() > 0 )
            {
                wc.walkingAlongProperty( currentResource, sPropName, collDestinations );
                for( Iterator it = collDestinations.iterator(); it.hasNext(); )
                {
                    RDFResource resDest = (RDFResource)it.next();
                    walk( sPropName, resDest );
                }
                wc.returningFromProperty( currentResource, sPropName, collDestinations );
            }
             
        }
    }




    //---------------------------------------------------------------------
    abstract public static class WalkerController
    {
        /**
         * (shortest) path from the starting resource to the current resource
         * including the used properties (edges) between the resources.<br>
         * this list carries information about the current state of the recursion:
         * from the starting resource to the current resource.<br>
         * <b>note:</b> this path does not only grow linearly, but changes even
         * on the head!
         * @see #lstWalk
         */
        public LinkedList/*RDFResource*/ lstPath = new LinkedList();

        /**
         * list (path) of all (up to now) visited resources and
         * the used properties (edges) between the resources.<br>
         * this list carries (cronological) information about complete (up to now)
         * walk through the resources graph.
         * @see #lstPath
         */
        public LinkedList/*RDFResource*/ lstWalk = new LinkedList();

        /**
         * override this method for special walking behavior.<br>
         * e.g. always returning false will implement just no walking at all,
         * whereas always returning true will walk every way available
         * (maybe endlessly).<br>
         * when going from from resource to some other resource, this method is
         * called to check whether the outgoing property (edge) can be walked on.<br>
         * the <b>default</b> behavior (if this method is not overridden) is:
         * the ways is not blocked, try to walk further!
         */ 
        public boolean walkingAllowed( RDFResource source, String prop )
        {
            return true;
        }
        
        /**
         * override this method for special walking behavior.<br>
         * e.g. always returning false will implement just no walking at all,
         * whereas always returning true will walk every way available
         * (maybe endlessly).<br>
         * after {@link #walkingAllowed(RDFResource,String)} tells us, the road
         * is free, this method tells us if the place we want to go to is free.
         * only if this test is positive we can go further.<br>
         * the <b>default</b> behavior (if this method is not overridden) is:
         * every resource is visited exactly once, i.e. if a resource has been
         * visited, every walk to this resource is not allowed.<br>
         * another meaningful implementation would be to walk all available "ways"
         * exactly once, whereas a "way" is something like this:
         * <code>source --&gt; prop --&gt; dest</code>.
         */ 
        public boolean walkingAllowed( RDFResource source, String prop, RDFResource dest )
        {
            return !alreadyVisitedOnWalk( dest );
            // return !alreadyWalkedThatWay( source, prop, dest );
        }

        /**
         * override this method if you want to be informed about the concrete
         * roads the walker walks along.<br>
         * this method is called while walking from one resource to other resource(s).
         * the destination resource(s) is/are not yet reached, but you can be sure,
         * that <i>all</i> destinations in <code>dest</code> will be reached!<br>
         */
        public void walkingAlongProperty( RDFResource source, String prop, Collection/*RDFResources*/ dest )
        {
        }
        
        /**
         * override this method if you want to be informed about the concrete
         * roads the walker walks along.<br>
         * this method is called when walking from one resource to other resource(s) is finished.
         * the destination resource(s) have all been reached.
         */
        public void returningFromProperty( RDFResource source, String prop, Collection/*RDFResources*/ dest )
        {
        }
        
        /**
         * override this method to specify the behavior of the walker -
         * this method is what a walker is about.<br>
         * return true if walking should proceed beyond the current resource;
         * returning false will result in going back.
         */
        abstract public boolean arriving( RDFResource currentResource );

        /**
         * you only need to override this method in special cases:
         * this method is called when the walker returns and leaves
         * a resource.
         * that resource will never be arrived again, but maybe it
         * will be revisited.
         * @see #revisiting
         */
        public void leaving( RDFResource currentResource )
        {
        }

        /**
         * this method is called when a resource is visited once again.<br>
         * return true if walking should proceed beyond the current resource;
         * returning false will result in going back. 
         * default behavior is returning false, i.e. not to proceed when
         * arriving again at the same resource.
         * @see #leavingAgain
         */
        public boolean arrivingAgain( RDFResource currentResource )
        {
            return false;
        }

        /**
         * this method is called when leaving a resource which has been revisited. 
         * @see #arrivingAgain
         */
        public void leavingAgain( RDFResource currentResource )
        {
        }

        /**
         * override this method if you want to specify which properties to choose first -
         * the higher the returned value, the earlier that property is chosen.
         */
        public int propertyImportance( RDFResource source, String prop )
        {
            return 0;
        }

        //---------------------------------------------------------------------
        
        protected String getLastProperty()
        {
            if( lstPath.size() > 1 )
                return (String)lstPath.get( lstPath.size()-2 );
            else
                return null;
        }
        
        protected RDFResource getCurrentResource()
        {
            return (RDFResource)lstPath.getLast();
        }
        
        protected boolean alreadyVisitedOnPath( RDFResource res )
        {
            return ( lstPath.indexOf( res ) > 0  &&  lstPath.indexOf( res ) < lstPath.size()-1 );
        }
        
        protected boolean alreadyVisitedOnWalk( RDFResource res )
        {
            return ( lstWalk.indexOf( res ) > 0  &&  lstWalk.indexOf( res ) < lstWalk.size()-1 );
        }
        
        protected boolean alreadyWalkedThatWay( RDFResource source, String prop, RDFResource dest )
        {
            RDFResource last = null;
            for( Iterator it = lstWalk.iterator(); it.hasNext(); )
            {
                RDFResource s = last;
                if( !it.hasNext() ) break;
                String p = (String)it.next();
                if( !it.hasNext() ) break;
                RDFResource d = (RDFResource)it.next();
                if( source.equals( s ) && prop.equals( p ) && dest.equals( d ) )
                    return true;
                last = d;
            }
            return false;
        }
        
    } // end of inner class WalkerController

    

    public final static String DEBUG_MODULE = "rdf2java.JavaGraphWalker";

    public static Debug debug()
    {
        return Debug.forModule( DEBUG_MODULE );
    }


} // end of class JavaGraphWalker

