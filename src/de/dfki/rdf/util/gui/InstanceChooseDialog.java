/*
 * Created on 20.09.2004
 */
package de.dfki.rdf.util.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import de.dfki.rdf.util.KnowledgeBase;
import de.dfki.rdf.util.RDFResource;
import de.dfki.rdf.util.THING;
import de.dfki.util.debug.Debug;




public class InstanceChooseDialog
                       implements  MouseListener,
                                   java.awt.event.ActionListener

{
//------------------------------------------------------------------------------
private KnowledgeBase m_knowledgeBase;
private JDialog m_dialog;
private JDialog m_dialogOwner;
private JFrame  m_frameOwner;

/**
 * false if multiple selections are allowed, true otherwise
 */
private boolean m_singleInstance = false;


private boolean m_hasMultiValue;



 JButton m_buttOK;

 ListCellRenderer m_listCellrenderer;
 ResourceObjectTreeCellRenderer m_treeCellRenderer;

 JTabbedPane m_classesTab;


 DefaultListModel m_currentSelectedInstances;
 Vector m_allowedClasses;
 JList m_listCurrentSelectedInstances;

 THINGDialogFactory.THINGDialogHelper m_thingDialogHelper = null;

 
 
/**
 * Choose one or multiple  instance(s) from the given classes found in the KnowledgeBase
 * @param p_allowedClasses
 * @param p_hasMultiValue select one instance or multiple instances 
 * @param p_knowledgeBase
 * @param p_thingDialogHelper
 * @param p_owner parent window; can be <code>null</code> 
 */
public InstanceChooseDialog (Class[] p_allowedClasses, boolean p_hasMultiValue, KnowledgeBase p_knowledgeBase,THINGDialogFactory.THINGDialogHelper p_thingDialogHelper, Window p_owner)
{

   m_thingDialogHelper = p_thingDialogHelper;
   m_knowledgeBase = p_knowledgeBase;
   
   
   
   if (p_owner instanceof JDialog)
   {
    m_dialogOwner = (JDialog)p_owner;
    m_frameOwner=null;
   }
   else if (p_owner instanceof JFrame)
   {
    m_dialogOwner = null;
    m_frameOwner = (JFrame)p_owner;
   }
   else
   {
     m_dialogOwner = null;
     m_frameOwner = null;
    //throw new Error("InstanceChooseDialog: window owner of dialog is neither JDialog nor JFrame");
   }

   init( p_allowedClasses,p_hasMultiValue);
}


private void init (Class[] p_allowedClasses, boolean p_hasMultiValue)
{

    
    m_currentSelectedInstances = new DefaultListModel();
//    if(mode == THINGDialogFactory.DEBUG_MODE)
        m_listCellrenderer = new ResourceObjectCellRenderer();
//    else
//        m_listCellrenderer = new ResourceObjectUserCellRenderer();
    m_treeCellRenderer = new ResourceObjectTreeCellRenderer();
    m_hasMultiValue = p_hasMultiValue;

    if (p_allowedClasses != null)
        m_allowedClasses = new Vector(Arrays.asList(p_allowedClasses));
    else
        m_allowedClasses = new Vector(Collections.EMPTY_LIST);




}

//-------------------------------------------------------------------------------------------------------------------------
private static Debug debug ()
{
    return Debug.forModule("InstanceChooseDialog");
}



//-------------------------------------------------------------------------------------------------------------------------


private void initializeDialog()
{
    try
    {
        String text = "Choose instance";
        if (m_hasMultiValue)
          text+="s";

        if (m_frameOwner == null && m_dialogOwner == null)
        {
            m_dialog = new JDialog();
            m_dialog.setTitle(text);
            m_dialog.setModal(true);
        }
        else
            m_dialog = (m_frameOwner != null) ? new JDialog(m_frameOwner,text,true) : new JDialog(m_dialogOwner,text,true);

        m_dialog.setSize(400,400);

        m_dialog.getContentPane().setLayout(new BorderLayout());

        m_listCurrentSelectedInstances = new JList(m_currentSelectedInstances);
        m_listCurrentSelectedInstances.addMouseListener(this);
        m_listCurrentSelectedInstances.setCellRenderer(m_listCellrenderer);
        m_listCurrentSelectedInstances.setBorder( BorderFactory.createBevelBorder( javax.swing.border.BevelBorder.LOWERED) );
        m_listCurrentSelectedInstances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_dialog.getContentPane().add(new JScrollPane(m_listCurrentSelectedInstances), BorderLayout.NORTH);
        //m_dialog.getContentPane().add(m_listCurrentSelectedInstances, BorderLayout.NORTH);

        m_classesTab = new JTabbedPane();

        buildInstances();
        arrangeTabs();

        m_dialog.getContentPane().add(m_classesTab, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel();

        m_buttOK = new JButton("select");
        m_buttOK.setSize(30,10);
        m_buttOK.setActionCommand("select");
        m_buttOK.addActionListener(this);
        m_buttOK.setEnabled(false);
        buttonPanel.add(m_buttOK);

        JButton cancel = new JButton("cancel");
        cancel.setSize(30,10);
        cancel.setActionCommand("cancel");
        cancel.addActionListener(this);

        buttonPanel.add(cancel);

        m_dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);


        //m_dialog.setBackground( UserManagerBrowser.BACKGROUND_COLOR );


    }
    catch (Exception e)
    {
        debug().error( e );
    }
}

//------------------------------------------------------------------------------

public void actionPerformed(ActionEvent e)
{
    if (e.getActionCommand().equals("select"))
    {

     // take the selected instance
        if ( m_currentSelectedInstances.isEmpty() ) return;
        m_dialog.setVisible(false);


    }
    else if (e.getActionCommand().equals("cancel"))
    {

      m_currentSelectedInstances.clear();
      m_dialog.setVisible(false);
     }
    else if (e.getActionCommand().equals("removeSelection"))
    {

      int i = m_listCurrentSelectedInstances.getSelectedIndex();
      if (i < 0 ) return;
      ((DefaultListModel)m_listCurrentSelectedInstances.getModel()).remove(i);

      if (m_listCurrentSelectedInstances.getModel().getSize() == 0)       m_buttOK.setEnabled(false);

    }
    else if (e.getActionCommand().equals("viewSelectionCSI"))
    {
      THING thing = null;
      if (m_currentSelectedInstances.isEmpty())
      {
        return;
      }
      else if (m_currentSelectedInstances.size() == 1)
      {
        thing = (THING)((ResourceObjectNode)m_currentSelectedInstances.firstElement()).getUserObject();
      }
      else
      {
        thing =  (THING)((ResourceObjectNode)m_listCurrentSelectedInstances.getSelectedValue()).getUserObject();
        if (thing == null) return;
       }

      THINGDialogFactory.createViewDialog(thing , m_knowledgeBase,    m_thingDialogHelper, m_dialog).setVisible(true);

    }
    else if (e.getActionCommand().equals("viewSelection"))
    {
      JScrollPane sp = (JScrollPane)m_classesTab.getSelectedComponent();
      if (sp == null)
      {
        sp = (JScrollPane)m_classesTab.getComponent(0);
        if (sp == null) return;
      }

      Iterator it = Arrays.asList(sp.getViewport().getComponents()).iterator();
      DefaultMutableTreeNode node=null;


      while (it.hasNext())
      {
        Object o = it.next();

        if (o instanceof JList)
        {
           node =(DefaultMutableTreeNode)((JList)o).getSelectedValue();
        }
        else if( o instanceof JTree)
        {
          TreePath tp = ((JTree)o).getSelectionPath();
          if (tp == null ) return;
          node =(DefaultMutableTreeNode)tp.getLastPathComponent();

        }



      }

      if (node == null) return;
      THING thing = (THING)node.getUserObject();

      THINGDialogFactory.createViewDialog(thing, m_knowledgeBase,m_thingDialogHelper, m_dialog).setVisible(true);
    }

}






// MouseListener for


public void mousePressed(MouseEvent e)
{


 if ( e.getModifiers() == InputEvent.BUTTON3_MASK ) // e.isPopupTrigger() )
  {

        final JPopupMenu popup  = new JPopupMenu();


        JMenuItem view = new JMenuItem("view");
        if (e.getSource().equals(m_listCurrentSelectedInstances))
        {
            view.setActionCommand("viewSelectionCSI");
        }
        else
        {
            view.setActionCommand("viewSelection");
        }

        view.addActionListener(this);
        popup.add( view );

        if (e.getSource().equals(m_listCurrentSelectedInstances))
        {
          JMenuItem remSelection = new JMenuItem("remove");
          remSelection.setActionCommand("removeSelection");
          remSelection.addActionListener(this);
          popup.add( remSelection );
        }

        popup.show( (Component) e.getSource(), e.getX(), e.getY() );
        popup.setVisible(true);
  }
}

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

public void mouseClicked(MouseEvent e)
{

  if ( e.getModifiers() == InputEvent.BUTTON1_MASK && e.getClickCount() == 1)
  {
   if (e.getSource() instanceof JTree || (e.getSource() instanceof JList && !(e.getSource().equals(m_listCurrentSelectedInstances))))
   {
    m_listCurrentSelectedInstances.clearSelection();
   }

  }
  else if ( e.getModifiers() == InputEvent.BUTTON1_MASK && e.getClickCount() == 2)
  {
   if (e.getSource() instanceof JList && !(e.getSource().equals(m_listCurrentSelectedInstances)))
   {
    ResourceObjectNode node =(ResourceObjectNode)((JList)e.getSource()).getSelectedValue();
    if (node != null && !m_currentSelectedInstances.contains(node))
    {
      m_buttOK.setEnabled(true);
      if (m_singleInstance)
      {
        m_currentSelectedInstances.clear();
        m_currentSelectedInstances.addElement(node);
      }
      else
       m_currentSelectedInstances.addElement(node);
    }

   }
   else if (e.getSource() instanceof JTree)
   {
    TreePath tp = ((JTree)e.getSource()).getSelectionPath();
    if (tp == null ) return;
    DefaultMutableTreeNode node =(DefaultMutableTreeNode)tp.getLastPathComponent();
    if (node != null  && !m_currentSelectedInstances.contains(node))
    {
        ResourceObjectNode selNode = new ResourceObjectNode((RDFResource)node.getUserObject());
        m_buttOK.setEnabled(true);
        if (m_singleInstance)
        {
          m_currentSelectedInstances.clear();
          m_currentSelectedInstances.addElement(selNode);
        }
        else
         m_currentSelectedInstances.addElement(selNode);
    }

   }
  }
 }



public ResourceObjectNode selectInstance()
{
    m_singleInstance = true;
    if (m_dialog == null )
      initializeDialog();

    reinit();
    m_dialog.setVisible(true);

    if (m_currentSelectedInstances.isEmpty())
     return null;
    else
     return (ResourceObjectNode)m_currentSelectedInstances.firstElement();
}

/**
 *
 * @return Collection of ResourceObjectNode; could be empty but not <code>null</code>
 */
public Collection selectInstances()
{
    m_singleInstance = false;
    if (m_dialog == null )
      initializeDialog();

    reinit();
    m_dialog.setVisible(true);

    return Arrays.asList(m_currentSelectedInstances.toArray());
}

private void reinit()
{
    //m_currentSelectedInstances.clear();
//m_currentSelectedInstances.add(new ResourceObjectNode(UIDataAccess.getInstance().getUser()));

/*    for (int i=m_classesTab.getComponentCount()-1; 0 <= i; i--)
    {

       Component com = ((JScrollPane)m_classesTab.getComponent(i)).getComponent(0);
       if (com == null) continue;
       if (com instanceof JTree)
         ((JTree)com).clearSelection();
       else
          ((JList)com).clearSelection();



    }

*/
}


private HashMap m_mapClasses2Components;


private void buildInstances()
{

    m_mapClasses2Components = new HashMap();

    Iterator it = m_allowedClasses.iterator();
    while (it.hasNext())
    {
        m_mapClasses2Components.put(it.next(),new Vector());
    }


   
    it = m_knowledgeBase.values().iterator();

    while (it.hasNext())
    {
        RDFResource res = (RDFResource) it.next();

        if ( !(res instanceof THING) )
            continue;
        THING thing = (THING) res;


        Iterator cit = m_allowedClasses.iterator();
        while (cit.hasNext())
        {
          Class cl = (Class) cit.next();
            
          if (cl.equals(thing.getClass()) )
          {
            ((Vector)m_mapClasses2Components.get(cl)).add(new ResourceObjectNode(thing));
          }

        }


    }

}

private void arrangeTabs()
{

    Iterator it = m_allowedClasses.iterator();
    while (it.hasNext())
    {
       Class cl = (Class) it.next();
       
       Vector instances =  (Vector)m_mapClasses2Components.get(cl);
       JComponent tab = null;
       String classname = cl.getName().substring(cl.getName().lastIndexOf('.')+1);
       
       if (m_thingDialogHelper != null)
       {
           InstanceChooseDialog.InstanceChooseDialogHelper dialogHelper = m_thingDialogHelper.getChooseDialogHelper();
           
           if (dialogHelper != null)
           {
               tab = dialogHelper.getTab(cl, instances, m_knowledgeBase, this);
               
               if (tab != null) 
                   m_classesTab.add(classname,tab);
           }
       }
       
       if (tab == null)
       {
        // do a normal lists ...
        JList list = new JList(instances);
        list.addMouseListener(this);
        list.setCellRenderer(m_listCellrenderer);
        m_classesTab.add(classname,new JScrollPane(list));

       }

    }
}


public ListCellRenderer getListCellRenderer()
{
    return m_listCellrenderer;
}

public ResourceObjectTreeCellRenderer getTreeCellRenderer()
{
    return m_treeCellRenderer;
}

public class ResourceObjectCellRenderer extends JLabel implements ListCellRenderer {
    public ResourceObjectCellRenderer() {
        setOpaque(true);
    }
    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
       setText(value.toString());
        setBackground(isSelected ? Color.blue : Color.white);
        setForeground(isSelected ? Color.white : Color.black);

        ResourceObjectNode node = (ResourceObjectNode) value;
        setIcon(node.getIcon());

        return this;
    }
}

public class ResourceObjectUserCellRenderer extends JLabel implements ListCellRenderer {
    public ResourceObjectUserCellRenderer() {
        setOpaque(true);
    }
    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
       ResourceObjectNode node = (ResourceObjectNode) value;
       setText(node.getUserObject().toString());
        setBackground(isSelected ? Color.blue : Color.white);
        setForeground(isSelected ? Color.white : Color.black);


        setIcon(node.getIcon());

        return this;
    }
}

  public class ResourceObjectTreeCellRenderer extends DefaultTreeCellRenderer {

                  public ResourceObjectTreeCellRenderer() {

                  }

                  public Component getTreeCellRendererComponent(
                                      JTree tree,
                                      Object value,
                                      boolean sel,
                                      boolean expanded,
                                      boolean leaf,
                                      int row,
                                      boolean hasFocus) {


        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;


          super.getTreeCellRendererComponent(tree, node, sel, expanded, leaf, row, hasFocus);

          setIcon(ResourceObjectNode.getIcon((RDFResource)node.getUserObject()));




              return this;
          }
      }

  
  public interface InstanceChooseDialogHelper
  {
      /**
       * this is called if  the p_class should be shown with the instances in p_instances. 
       * If you want to handle the class (e.g., with a tree) return a JComponent (e.g., a JScrollPane)<br>
       * Set p_instanceChooseDialog as mouse listener for your JComponent in order to select things<br>
       * Also have a look at the offered renderers for trees and lists, i.e. use them :).
       * if <code>null</code> is returned you don't want to handle the class, then a normal list tab is used.
       * @param p_class the class of all instances
       * @param p_instances   list of instances of type ResourceObjectNode
       * @param p_knowledgeBase the used knowledge base
       * @param p_instanceChooseDialog the calling InstanceChooseDialog
       * @return a JComponent which will be added as a tab, or <code>null</code> if no special treatment from your side is needed
       * @see InstanceChooseDialog#getListCellRenderer()
       * @see InstanceChooseDialog#getTreeCellRenderer()
       * @see ResourceObjectNode
       */
    public JComponent getTab(Class p_class, List p_instances, KnowledgeBase p_knowledgeBase, InstanceChooseDialog p_instanceChooseDialog);
  }
} // end of class

