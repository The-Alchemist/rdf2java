package dfki.rdf.util.nice;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.StringReader;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import dfki.rdf.util.RDFNice;


public class RDFNiceApplet extends Applet
{
    boolean isStandalone = false;
    JPanel jPanel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JScrollPane scrSource = new JScrollPane();
    JTextArea taSource = new JTextArea();
    JPanel panPredValues = new JPanel();
    JScrollPane scrPredValues = new JScrollPane();
    BorderLayout borderLayout2 = new BorderLayout();
    JPanel jPanel2 = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    JButton btnNice = new JButton();
    Object[][] aasPredValues = { { "http://dfki.frodo.wwf/task#rootTask"    , "-10000"  },
                                 { "http://dfki.frodo.wwf/task#parentTask"  , "-1000"   },
                                 { "http://dfki.frodo.wwf/task#object"      , "-1"      },
                                 { "http://dfki.frodo.wwf/task#objects"     , "-1"      },
                                 { "http://dfki.frodo.wwf/task#subTask"     , "100"     },
                                 { "http://dfki.frodo.wwf/task#name"        , "100000"  },
                                 { "http://dfki.frodo.wwf/task#taskState"   , "99999"   },
                                 { ""                                       , ""        },
                                 { ""                                       , ""        },
                                 { ""                                       , ""        } };
    JTable tblPredValues = new JTable( aasPredValues, new Object[]{ "predicate", "value" } );
    BorderLayout borderLayout3 = new BorderLayout();
    //Get a parameter value
    public String getParameter(String key, String def)
    {
        return isStandalone ? System.getProperty(key, def) :
            (getParameter(key) != null ? getParameter(key) : def);
    }

    //Construct the applet
    public RDFNiceApplet()
    {
    }
    //Initialize the applet
    public void init()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    //Component initialization
    private void jbInit() throws Exception
    {
        this.setLayout(borderLayout1);
        jPanel1.setLayout(borderLayout3);
        taSource.setText("Resulting nice RDF will appear here.");
        panPredValues.setLayout(borderLayout2);
        jPanel2.setLayout(flowLayout1);
        btnNice.setText("Go!");
        btnNice.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                btnNice_actionPerformed(e);
            }
        });
        tblPredValues.setMaximumSize(new Dimension(2147483647, 200));
        tblPredValues.setMinimumSize(new Dimension(30, 200));
        tblPredValues.setCellSelectionEnabled(true);
        scrPredValues.setPreferredSize(new Dimension(454, 200));
        this.add(jPanel1,  BorderLayout.CENTER);
        jPanel1.add(scrSource, BorderLayout.CENTER);
        scrSource.getViewport().add(taSource, null);
        jPanel1.add(panPredValues, BorderLayout.SOUTH);
        panPredValues.add(jPanel2, BorderLayout.NORTH);
        jPanel2.add(btnNice, null);
        panPredValues.add(scrPredValues, BorderLayout.CENTER);
        scrPredValues.getViewport().add(tblPredValues, null);
    }
    //Start the applet
    public void start()
    {
    }
    //Stop the applet
    public void stop()
    {
    }
    //Destroy the applet
    public void destroy()
    {
    }
    //Get Applet information
    public String getAppletInfo()
    {
        return "Applet Information";
    }
    //Get parameter info
    public String[][] getParameterInfo()
    {
        return null;
    }
    //Main method
    public static void main(String[] args)
    {
        RDFNiceApplet applet = new RDFNiceApplet();
        applet.isStandalone = true;
        Frame frame;
        frame = new Frame()
        {
            protected void processWindowEvent(WindowEvent e)
            {
                super.processWindowEvent(e);
                if (e.getID() == WindowEvent.WINDOW_CLOSING)
                {
                    System.exit(0);
                }
            }
            public synchronized void setTitle(String title)
            {
                super.setTitle(title);
                enableEvents(AWTEvent.WINDOW_EVENT_MASK);
            }
        };
        frame.setTitle("Applet Frame");
        frame.add(applet, BorderLayout.CENTER);
        applet.init();
        applet.start();
        frame.setSize(800,620);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
        frame.setVisible(true);
    }

    void btnNice_actionPerformed(ActionEvent e)
    {
        StringReader sr = new StringReader( taSource.getText() );
        RDFNice rdfNice = new RDFNice( sr );
        for( int i = 0; i < tblPredValues.getRowCount(); i++ )
        {
            String sPred = (String)tblPredValues.getValueAt( i, 0 );
            if( sPred == null  ||  sPred.length() <= 0 )
                continue;
            String sValue = (String)tblPredValues.getValueAt( i, 1 );
            rdfNice.setPredValue( sPred, Double.parseDouble( sValue ) );
        }
        rdfNice.createNiceXML();
        String sResult = rdfNice.serializeToString();
        taSource.setText( sResult );
    }

} // end of class RDFNiceApplet

