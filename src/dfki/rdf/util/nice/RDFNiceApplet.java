package dfki.rdf.util.nice;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class RDFNiceApplet extends Applet
{
    boolean isStandalone = false;
    JPanel jPanel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JScrollPane scrDest = new JScrollPane();
    JScrollPane scrSource = new JScrollPane();
    JTextArea taDest = new JTextArea();
    JTextArea taSource = new JTextArea();
    JPanel panPredValues = new JPanel();
    JScrollPane scrPredValues = new JScrollPane();
    JTable tblPredValues = null;
    BorderLayout borderLayout2 = new BorderLayout();
    JPanel jPanel2 = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    JButton btnNice = new JButton();
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
        jPanel1.setLayout(gridBagLayout1);
        taDest.setText("Put your source RDF here!");
        taSource.setText("Resulting nice RDF will appear here.");
        panPredValues.setLayout(borderLayout2);
        Object[][] aasPredValues = { { "http://dfki.frodo.wwf/task#rootTask"    , new Double( -10000 ) },
                                     { "http://dfki.frodo.wwf/task#parentTask"  , new Double(  -1000 ) },
                                     { "http://dfki.frodo.wwf/task#object"      , new Double(     -1 ) },
                                     { "http://dfki.frodo.wwf/task#objects"     , new Double(     -1 ) },
                                     { "http://dfki.frodo.wwf/task#subTask"     , new Double(    100 ) },
                                     { "http://dfki.frodo.wwf/task#name"        , new Double( 100000 ) },
                                     { "http://dfki.frodo.wwf/task#taskState"   , new Double(  99999 ) },
                                     { ""                                       , new Double(      0 ) },
                                     { ""                                       , new Double(      0 ) },
                                     { ""                                       , new Double(      0 ) } };

        tblPredValues = new JTable( aasPredValues, new Object[]{ "predicate", "value" } );
        tblPredValues.setCellSelectionEnabled(true);
        jPanel2.setLayout(flowLayout1);
        btnNice.setText("Go!");
        btnNice.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                btnNice_actionPerformed(e);
            }
        });
        this.add(jPanel1,  BorderLayout.CENTER);
        jPanel1.add(scrDest,       new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(scrSource,     new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        scrDest.getViewport().add(taDest, null);
        scrSource.getViewport().add(taSource, null);
        jPanel1.add(panPredValues,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        panPredValues.add(scrPredValues, BorderLayout.CENTER);
        panPredValues.add(jPanel2, BorderLayout.NORTH);
        jPanel2.add(btnNice, null);
        scrPredValues.add(tblPredValues);
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
        //TODO: call RDFNice here!
    }
}