package de.dfki.rdf.test.staticKnowledgeBase;

import java.util.Iterator;

import com.sun.rsasign.al;

import EDU.oswego.cs.dl.util.concurrent.ThreadFactory;

import junit.framework.TestCase;
import de.dfki.rdf.util.KnowledgeBase;
import de.dfki.rdf.util.RDFResource;


public class Test extends TestCase
{
    public static void main( String[] args )
    {
        junit.textui.TestRunner.run( Test.class );
    }

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    public Test( String name )
    {
        super( name );
    }

    
    public void test1()
    {
        Thread1 t1 = new Thread1();     t1.start();
        Thread2 t2 = new Thread2();     t2.start();
        
        try{ Thread.sleep( 15000 ); } catch( InterruptedException ex ) {}
        
        assertTrue( t1.isOK()  &&  t2.isOK() );
    }


    public void test2()
    {
        ThreadFastPutter tPutter = new ThreadFastPutter();     tPutter.start();
        ThreadFastGetter tGetter = new ThreadFastGetter();     tGetter.start();
        
        try{ Thread.sleep( 25000 ); } catch( InterruptedException ex ) {}
        
        assertTrue( tPutter.isOK()  &&  tGetter.isOK() );
    }
    
    
    
    class Thread1   extends ThreadX
    {
        protected void state_1()
        {
            KnowledgeBase kb = KnowledgeBase.getStaticKnowledgeBase();

            sleepy( 1000 ); 
            System.out.println( "                               [t1] adding res1" ); 
            kb.put( new RDFResource( "res1" ) );            
            System.out.println( "                               [t1] added  res1" ); 

            sleepy( 1000 ); 
            System.out.println( "                               [t1] adding res2" ); 
            kb.put( new RDFResource( "res2" ) );
            System.out.println( "                               [t1] added  res2" ); 
            
            sleepy( 1000 ); 
            System.out.println( "                               [t1] adding res3" ); 
            kb.put( new RDFResource( "res3" ) );
            System.out.println( "                               [t1] added  res3" ); 
            
            state = 2;
        }
        
        protected void state_2()
        {
            KnowledgeBase kb = KnowledgeBase.getStaticKnowledgeBase();
            RDFResource res4 = (RDFResource)kb.get( "res4" );
            if( res4 != null )
                state = 3;
            else
                sleepy( 100 );
        }

        protected void state_3()
        {
            KnowledgeBase kb = KnowledgeBase.getStaticKnowledgeBase();
            RDFResource res4 = (RDFResource)kb.get( "res4" );
            res4.putLabel( "forth resource" );
            
            System.out.println( "                               [t1] finished" );
            state = 0;  // finish
            ok = true;
        }
        
    } // end of class Thread1

    
    class Thread2   extends ThreadX
    {
        protected void state_1()
        {
            KnowledgeBase kb = KnowledgeBase.getStaticKnowledgeBase();
            RDFResource res2 = (RDFResource)kb.get( "res2" );
            if( res2 != null )
            {
                res2.putLabel( "second resource" );
                kb.put( new RDFResource( "res4" ) );
                state = 2;
            }
            else
            {
                sleepy( 400 );
                printAll();
            }
        }
        
        protected void state_2()
        {
            KnowledgeBase kb = KnowledgeBase.getStaticKnowledgeBase();
            RDFResource res4 = (RDFResource)kb.get( "res4" );
            if( res4 != null && res4.getLabel() != null && !res4.getLabel().equals( res4.getURI() ) )
            {
                state = 3;
            }
            else
            {
                sleepy( 400 );
                printAll();
            }
        }

        protected void state_3()
        {
            sleepy( 400 );
            printAll();
            System.out.println( "[t2] finished" );
            ok = true;
            state = 0;  // finish
        }
        
        private void printAll()
        {
            KnowledgeBase kb = KnowledgeBase.getStaticKnowledgeBase();
            StringBuffer sb = new StringBuffer();
            for( Iterator it = kb.values().iterator(); it.hasNext(); )
            {
                RDFResource res = (RDFResource)it.next();
                sb.append( "    <" + res.getURI() + "/" + res.getLabel() + ">" );
            }
            System.out.println( "[t2] kb: " + sb.toString() ); ;
        }
                
    } // end of class Thread2
    

    class ThreadFastPutter   extends ThreadX
    {
        int nr = 100;
        boolean[] alreadyChecked;   
        int nrChecked = 0;
     
        public ThreadFastPutter()
        {
            alreadyChecked = new boolean[ nr ];
            for( int i = 0; i < nr; i++ )
            {
                alreadyChecked[i] = false;
            }
        }

        protected void state_1()
        {
            if( nrChecked >= nr )
            {
                ok = true;
                state = 0;
                System.out.println( "### put finished" );
                return;
            }
            
            KnowledgeBase kb = KnowledgeBase.getStaticKnowledgeBase();
            int i = (int)( Math.random() * (double)nr ); 
            String sURI = "res__" + i;            
            System.out.println( "### get " + sURI + " (" + nrChecked + " / " + nr + ")" );
            ;
            RDFResource res2 = (RDFResource)kb.get( sURI );
            if( res2 != null ) 
            {
                System.out.println( "### get " + sURI + " -> already checked " +
                                    "(" + nrChecked + " / " + nr + ")" );
                sleepy( 10 );
                return;
            }
            
            System.out.println( "### put " + sURI + " (" + nrChecked + " / " + nr + ")" );
            ;
            RDFResource res = new RDFResource( sURI );
            kb.put( res );
            alreadyChecked[i] = true;
            nrChecked++;
            System.out.println( "### put " + sURI + "\t[ready] (" + nrChecked + " / " + nr + ")" );
            ;
            sleepy( 10 );
        }
        
        protected void state_2()
        {
        }

        protected void state_3()
        {
        }
        
    } // end of class ThreadPutter
    

    class ThreadFastGetter   extends ThreadX
    {
        int nr = 100;
        boolean[] alreadyChecked;
        int nrChecked = 0;
        
        ThreadFastGetter()
        {
            alreadyChecked = new boolean[ nr ];
            for( int i = 0; i < nr; i++ )
            {
                alreadyChecked[i] = false;
            }
        }
        
        protected void state_1()
        {
            KnowledgeBase kb = KnowledgeBase.getStaticKnowledgeBase();
            int i = (int)( Math.random() * (double)nr ); 
            String sURI = "res__" + i;            
            System.out.println( "--- get " + sURI );
            RDFResource res = (RDFResource)kb.get( sURI );
            if( res != null )
            {
                if( !alreadyChecked[i] )
                    nrChecked++;
                alreadyChecked[i] = true;
            }
            System.out.println( "--- get " + sURI + "\t: " + (res != null ? "found    " : "not found") + 
                                "(" + nrChecked + " / " + nr + ")" );

            if( nrChecked >= nr )
            {
                ok = true;
                state = 0;
                System.out.println( "--- get finished" );
                return;
            }
            
            sleepy( 10 );
        }
        
        protected void state_2()
        {
        }

        protected void state_3()
        {
        }
        
    } // end of class ThreadFastGetter
    

    abstract class ThreadX   extends Thread
    {
        protected int state = 1;
        protected boolean ok = false;
        
        public void run()
        {
            while( state > 0 )
            {
                switch( state )
                {
                case 1: state_1(); break;
                case 2: state_2(); break;
                case 3: state_3(); break;
                }
            }
        }
        
        protected void sleepy( long millis)
        {
            try{ sleep( millis ); }
            catch( InterruptedException ex ) {}
        }
        
        abstract protected void state_1();
        abstract protected void state_2();        
        abstract protected void state_3();
        
        public boolean isOK()
        {
            return ok;
        }
        
    } // end of class ThreadX
    


}
