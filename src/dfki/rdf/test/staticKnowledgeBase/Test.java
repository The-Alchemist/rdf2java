package dfki.rdf.test.staticKnowledgeBase;

import java.util.Iterator;

import junit.framework.TestCase;
import dfki.rdf.util.KnowledgeBase;
import dfki.rdf.util.RDFResource;


public class Test extends TestCase
{
    Thread1 m_t1;
    Thread2 m_t2;

    
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
        m_t1 = new Thread1();       m_t1.start();
        m_t2 = new Thread2();       m_t2.start();
        
        try{ Thread.sleep( 15000 ); } catch( InterruptedException ex ) {}
        
        assertTrue( m_t1.isOK()  &&  m_t2.isOK() );        
    }
    
    
    
    class Thread1   extends ThreadX
    {
        protected void state_1()
        {
            KnowledgeBase kb = KnowledgeBase.getStaticKnowledgeBase();

            sleepy( 1000 ); 
            System.out.println( "                               [t1] adding res1" ); System.out.flush();
            kb.put( new RDFResource( "res1" ) );            
            System.out.println( "                               [t1] added  res1" ); System.out.flush();

            sleepy( 1000 ); 
            System.out.println( "                               [t1] adding res2" ); System.out.flush();
            kb.put( new RDFResource( "res2" ) );
            System.out.println( "                               [t1] added  res2" ); System.out.flush();
            
            sleepy( 1000 ); 
            System.out.println( "                               [t1] adding res3" ); System.out.flush();
            kb.put( new RDFResource( "res3" ) );
            System.out.println( "                               [t1] added  res3" ); System.out.flush();
            
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
            if( res4 != null && !res4.getLabel().equals( res4.getURI() ) )
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
            System.out.println( "[t2] kb: " + sb.toString() ); System.out.flush();
        }
                
    } // end of class Thread2
    

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
