package dfki.rdf.modelrep;

import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;

import att.grappa.*;


public class GrappaFrm   extends JFrame
{
//----------------------------------------------------------------------------------------------------
JScrollPane m_scrPane = null;
Graph m_grappaGraph = null;
GrappaPanel m_grappaPanel = null;

ModelRep m_MR = null;


//----------------------------------------------------------------------------------------------------
public GrappaFrm (ModelRep MR)
{
    super("ModelRep");

    m_MR = MR;

    reinit();
}

//----------------------------------------------------------------------------------------------------
public void reload ()
{
    createGrappaGraph();
    initGraphics();
}

//----------------------------------------------------------------------------------------------------
public void reinit()
{
    if (m_scrPane != null)
        getContentPane().removeAll();
    reload();
    getContentPane().repaint();
    setVisible(true);
}

//----------------------------------------------------------------------------------------------------
public void createGrappaGraph ()
{
    // create DOT-file of the modelrep
    String sDOTFilename  = "c:/java/rdf2java/src/dfki/rdf/modelrep/mr_test.dot-input";
    String sDOTFilename2 = "c:/java/rdf2java/src/dfki/rdf/modelrep/mr_test.dot";
    System.out.println("\ncreating dot-file...");
    m_MR.createDOTFile(sDOTFilename);
    try {
        System.out.println("\nconverting dot-file to gif (using dot)...");
        String[] asDOTArgs = { "D:\\Prog\\graphviz\\bin\\dot", "-o", sDOTFilename2, sDOTFilename};
        new dfki.util.exec.Executor("cmd D:\\Prog\\graphviz\\bin\\dot -o " + sDOTFilename2 + " " + sDOTFilename).execute(System.out);
    } catch (Exception ex) {
        System.out.println("### exception occurred while calling dot: "+ex.getMessage());
    }


    // do something with the generated dot-file...
    try
    {
        System.out.println("\ndoing something with grappa...");
        FileInputStream fis = new FileInputStream(sDOTFilename2);
        Parser grappaParser = new Parser(fis, System.out);
        grappaParser.parse();
        m_grappaGraph = grappaParser.getGraph();
    }
    catch (Exception ex)
    {
        System.out.println("### exception occurred while doing something with grappa: "+ex.getMessage());
    }
}

//----------------------------------------------------------------------------------------------------
public void initGraphics ()
{
    m_grappaPanel = new GrappaPanel(m_grappaGraph);
    m_grappaPanel.setScaleToFit(false);

    m_scrPane = new JScrollPane(m_grappaPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                               JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    getContentPane().add(m_scrPane);
    int width  = 1200;
    int height = 400;
    setSize(width, height);

    m_grappaPanel.addGrappaListener(new GrappaAdapter(this));
}

//----------------------------------------------------------------------------------------------------
public void activateElement (String sName)
{
    if (m_MR.activate(sName))
        reinit();  // something changed
}

//----------------------------------------------------------------------------------------------------
public void finishElement (String sName)
{
    if (m_MR.finish(sName))
        reinit();  // something changed
}

//----------------------------------------------------------------------------------------------------
public void editElement (String sName)
{
    if (m_MR.edit(sName))
        reinit();  // something changed
}

//----------------------------------------------------------------------------------------------------
}










