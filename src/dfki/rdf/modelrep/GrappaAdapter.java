package dfki.rdf.modelrep;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import att.grappa.*;


public class GrappaAdapter implements GrappaConstants, GrappaListener
{
//----------------------------------------------------------------------------------------------------

GrappaFrm m_grappaFrm = null;

//----------------------------------------------------------------------------------------------------
public GrappaAdapter(GrappaFrm grappaFrm)
{
    m_grappaFrm = grappaFrm;
}


//----------------------------------------------------------------------------------------------------
public void grappaClicked(Subgraph subg, Element elem, GrappaPoint pt, int modifiers, int clickCount, GrappaPanel panel)
{
//    if (elem != null && elem.isNode())
//        ; // hier kann dann was damit gemacht werden
}


//----------------------------------------------------------------------------------------------------
public void grappaPressed(Subgraph subg, Element elem, GrappaPoint pt,
                          int modifiers, GrappaPanel panel)
{
    final Element elemPressed = elem;

    if ( (modifiers & (InputEvent.BUTTON2_MASK|InputEvent.BUTTON3_MASK)) != 0 &&
         (modifiers & (InputEvent.BUTTON2_MASK|InputEvent.BUTTON3_MASK)) == modifiers)
    {
        // pop-up menu if button2 or button3
        JPopupMenu popup = new JPopupMenu();
        JMenuItem item = null;


        popup.add(item = new JMenuItem("activate"));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GrappaPanel gp = m_grappaFrm.m_grappaPanel;
                ModelRep MR = m_grappaFrm.m_MR;
                if (elemPressed != null && elemPressed.isNode())
                {
                    String sName = elemPressed.getName();
                    m_grappaFrm.activateElement(sName);
                }
            }
        });


        popup.add(item = new JMenuItem("finish"));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GrappaPanel gp = m_grappaFrm.m_grappaPanel;
                ModelRep MR = m_grappaFrm.m_MR;
                if (elemPressed != null && elemPressed.isNode())
                {
                    String sName = elemPressed.getName();
                    m_grappaFrm.finishElement(sName);
                }
            }
        });


        popup.add(item = new JMenuItem("edit"));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GrappaPanel gp = m_grappaFrm.m_grappaPanel;
                ModelRep MR = m_grappaFrm.m_MR;
                if (elemPressed != null && elemPressed.isNode())
                {
                    String sName = elemPressed.getName();
                    m_grappaFrm.editElement(sName);
                }
            }
        });


        popup.add(item = new JMenuItem("zoom in"));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GrappaPanel gp = m_grappaFrm.m_grappaPanel;
                gp.multiplyScaleFactor(1.25);
                gp.repaint();
            }
        });

        popup.add(item = new JMenuItem("zoom out"));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GrappaPanel gp = m_grappaFrm.m_grappaPanel;
                gp.multiplyScaleFactor(0.8);
                gp.repaint();
            }
        });

        popup.add(item = new JMenuItem("zoom in fast"));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GrappaPanel gp = m_grappaFrm.m_grappaPanel;
                gp.multiplyScaleFactor(2.0);
                gp.repaint();
            }
        });

        popup.add(item = new JMenuItem("zoom out fast"));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GrappaPanel gp = m_grappaFrm.m_grappaPanel;
                gp.multiplyScaleFactor(0.5);
                gp.repaint();
            }
        });

        popup.add(item = new JMenuItem("reset zoom"));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                GrappaPanel gp = m_grappaFrm.m_grappaPanel;
                gp.resetZoom();
                gp.repaint();
            }
        });

        /* does not work correctly (zooming disabled!)
        popup.add(item = new JMenuItem("scale to fit"));
        item.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
        GraphPanel graphPanel = itsTab.itsGraphPanel;
        GrappaPanel gp = graphPanel.itsGrappaPanel;
        gp.setScaleToSize(graphPanel.getViewport().getSize());
        gp.repaint();
        // gp.setScaleToFit(false);
          }
        });
        */


        java.awt.geom.Point2D mpt = panel.getTransform().transform(pt,null);
        popup.show(panel, (int)mpt.getX(), (int)mpt.getY());

    }
}


//----------------------------------------------------------------------------------------------------
public void grappaReleased(Subgraph subg, Element elem, GrappaPoint pt, int modifiers, Element pressedElem, GrappaPoint pressedPt, int pressedModifiers, GrappaBox outline, GrappaPanel panel)
{
}


//----------------------------------------------------------------------------------------------------
public void grappaDragged(Subgraph subg, GrappaPoint currentPt, int currentModifiers, Element pressedElem, GrappaPoint pressedPt, int pressedModifiers, GrappaBox outline, GrappaPanel panel)
{
}


//----------------------------------------------------------------------------------------------------
public String grappaTip(Subgraph subg, Element elem, GrappaPoint pt, int modifiers, GrappaPanel panel)
{
    return null;
}


//----------------------------------------------------------------------------------------------------
}

