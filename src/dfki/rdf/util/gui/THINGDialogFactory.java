/*
 * Created on 20.09.2004
 */
package dfki.rdf.util.gui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
//import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.tree.DefaultMutableTreeNode;



import dfki.rdf.util.KnowledgeBase;
import dfki.rdf.util.PropertyInfo;
import dfki.rdf.util.PropertyStore;
import dfki.rdf.util.RDFNice;
import dfki.rdf.util.RDFResource;
import dfki.rdf.util.THING;
//import dfki.util.config.Config;
import dfki.util.debug.Debug;

import org.w3c.rdf.model.ModelException;
import org.w3c.rdf.model.Resource;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: DFKI GmbH</p>
 * @author Heiko Maus
 * @version 1.0

 * Usage<br>
*<br><code>
*    MyTestClass implements THINGDialogHelper<br>
*    {<br>
*    <br>
*    private Class[] m_allClasses =  new Class[] { Person.class, User.class,Role.class};<br>
*    private InstanceChooseDialogHelper m_instanceChooseHelper  = new MyInstanceChooseDialog();<br>
*    private String m_iconResourceBundleClass = "dfki.frodo.wwf.orgmod.icons"; // here is the own icons.properties file <br>
*    private KnowledgeBase m_myKnowledgeBase;<br>
*  <br>
*  ...<br>
*  <br>
*    // read in config file (if applicable)<br>
*     dfki.frodo.wwf.util.ConfigUtil.readConfigsFromCmdline();<br<
*    // add icons before starting anything<br>
*    THINGDialogFactory.addIcons(m_iconResourceBundleClass);   // you don't need this if you prefer a XML-file, then use dfki.util.config.Config and THINGDialogProperty and name the file in the argument -configs THINGDialog.xml on the java command line<br>
*    
*  <br>
*    ...<br>
*  <br>
*   public void doStuff()<br>
*  {<br>
*  <br>
*    THING thing =  THINGDialogFactory.chooseAnyInstance( m_myKnowledgeBase, this, parentFrame);<br>
*  <br>
*    THINGDialog thingDialog = THINGDialogFactory.createModifyDialog(thing , m_myKnowledgeBase,this,parentFrame);<br>
*    if (thingDialog == null) return;<br>
*    thingDialog.setVisible(true);<br>
*  <br>
*    // or choose a class and instantiate it<br>
*  <br>
*    Class myClass =  THINGDialogFactory.chooseClass(m_allClasses,m_parentFrame);<br>
*
*    THINGDialog thingDialog = THINGDialogFactory.createNewDialog(myClass, myKnowledgeBase,this, m_parentFrame);<br>
*    if (thingDialog == null) return;  // cancel pressed by user<br>
*    thingDialog.setVisible(true);<br>
*  <br>
*  <br>
*  <br>
*    // then test if something happened<br>
*    if (!thingDialog.hasModifiedTHINGs()) <br>
*      return;  // nothing to assign<br>
*    else<br>
*    {<br>
*       //  do assign with thingDialog.getModifiedTHINGs()<br>
*       m_myKnowledgeBase.assignAllThings(thingDialog.getModifiedTHINGs());<br>
*    }<br>
*  <br>
*  }<br>
*  <br>
*  } </code> 
 * TODO bei cancel muss anderes handling hin
 * TODO Problem: inverse Knoten : wie kann ich die behandeln???
 * 
 */
public class THINGDialogFactory
{

 protected static int MODIFY_BUTTON_WIDTH = 300;
 protected static int MODIFY_BUTTON_HEIGHT = 100;
 protected static int TEXTFIELD_COLUMNS = 35;
 protected static int PANEL_HEIGHT = 25;
 protected static int INPUTFIELD_WIDTH = 250;
 protected static int INPUTFIELD_HEIGHT = PANEL_HEIGHT;
 protected static int LABEL_WIDTH = 120;
 protected static int LABEL_HEIGHT = PANEL_HEIGHT;
 protected static int PANEL_WIDTH = 400;
 private static int MAX_DIALOG_WIDTH = 550;
 protected static Dimension m_dimPanel = new Dimension(LABEL_WIDTH,LABEL_HEIGHT);
 protected static int LISTPANEL_HEIGHT = 60;
 protected static Dimension m_dimSingleListPanel = new Dimension(INPUTFIELD_WIDTH,INPUTFIELD_HEIGHT);
 protected static Dimension m_dimMaxTextField = new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width,INPUTFIELD_HEIGHT);
 protected static Dimension m_dimMainPanel = new Dimension(MAX_DIALOG_WIDTH,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-300);
 protected static int LIST_DEFAULT_MIN_ELEMENTS = 1;
  
 
 /**
  * map of abstract classes to subclasses (for instantiation purposes) -
  * hack until a dynamic version is found
  */
 protected static HashMap m_mapAbstract2Subclasses;
 




/**
 * key for the JComponent Property, maps this to the component's PropertyInfo
 */
 public static String COMPONENT_PROPERTYINFO = "compPropInfo";
 
 
 /**
  * title of the rdfs:label slot in the dialog
  */
 public static final String RDFSLABEL_FIELD_NAME = "rdfsLabel";


public static Debug debug()
{
    return Debug.forModule( "THINGDialogFactory" );
}

/**
 * used to add icons from a java resource 
 * @param p_iconResourceBundleClass the path to the .properties file
 * @see ResourceObjectNode#ICONLOADER
 * @see ResourceObjectNode#addIconsFromResource(String)
 */
public static void addIcons(String p_iconResourceBundleClass)
{
    ResourceObjectNode.addIconsFromResource(p_iconResourceBundleClass);
}



/**
 * @param p_res for convenience it's RDFResource, but should be THING to be shown
 * @param p_knowledgeBase
 * @param p_thingDHelper
 * @param p_owner type has to be JFrame or JDialog, or  <code>null</code>
 * @see THINGDialogFactory.THINGDialogHelper
 * @see THINGDialog 
 */
public static void showViewDialog(RDFResource p_res, KnowledgeBase p_knowledgeBase, THINGDialogFactory.THINGDialogHelper p_thingDHelper, Window p_owner)
{
  if (!(p_res instanceof THING) )
  {
   // could be a class representation
   String className = getClassName(p_res);
   if (className != null)
   {
       JOptionPane.showMessageDialog(p_owner," class " + className,"View Class",JOptionPane.INFORMATION_MESSAGE,ResourceObjectNode.getIconClass());
   }
   else
       JOptionPane.showMessageDialog(p_owner,p_res,"View RDFResource",JOptionPane.INFORMATION_MESSAGE);

   return;
  }

  THINGDialog dialog = createViewDialog((THING)p_res,  p_knowledgeBase, p_thingDHelper, p_owner);

  if (dialog != null) dialog.setVisible(true);

}


/**
 * @param p_thing
 * @param p_knowledgeBase
 * @param p_thingDHelper
 * @param p_owner type has to be JFrame or JDialog, or <code>null</code>
 * @return
 * @see THINGDialogFactory.THINGDialogHelper
 * @see THINGDialog 
 */
public static THINGDialog createViewDialog(THING p_thing, KnowledgeBase p_knowledgeBase, THINGDialogHelper p_thingDHelper, Window p_owner)
{
   THINGDialog dialog;
   if (p_owner instanceof Dialog)
    dialog = new THINGDialog(p_thing,p_knowledgeBase, (Dialog)p_owner, p_thingDHelper);
   else
    dialog = new THINGDialog(p_thing,p_knowledgeBase, (Frame)p_owner, p_thingDHelper);

   dialog.setTitle("View " + p_thing.getClass().getName());

   try
   {
    dialog.getContentPane().add(getMainPane(p_thing,false,true,dialog), BorderLayout.CENTER);
   }
   catch (Exception e)
   {
       debug().error("createViewDialog:",e);
       dialog.getContentPane().add(new JLabel("Exception occured ("+e.getMessage()+") Please consult the system administrator"));
   }
   dialog.getContentPane().add(getMainDialogButtons(dialog,p_thing,false), BorderLayout.SOUTH);
   setDialogSize(dialog);
   dialog.setLocation();
   return dialog;
}


 /**
 * @param p_thing
 * @param p_knowledgeBase
 * @param p_thingDHelper
 * @param p_owner type has to be JFrame or JDialog, or <code>null</code>
 * @return
 * @see THINGDialogFactory.THINGDialogHelper
 * @see THINGDialog 
*/
public static THINGDialog createModifyDialog(THING p_thing, KnowledgeBase p_knowledgeBase, THINGDialogFactory.THINGDialogHelper p_thingDHelper, Window p_owner)
 {
    boolean bModifyable = true;

    THINGDialog dialog;
    if (p_owner instanceof Dialog)
     dialog = new THINGDialog(p_thing,p_knowledgeBase, (Dialog)p_owner, p_thingDHelper);
    else
     dialog = new THINGDialog(p_thing,p_knowledgeBase, (Frame)p_owner,  p_thingDHelper);
    
    
    dialog.setTitle("Modify " + p_thing.getClass().getName());

    try
    {
     dialog.getContentPane().add(getMainPane(p_thing,true,true,dialog), BorderLayout.CENTER);
    }
    catch (Exception e)
    {
        debug().error("createViewDialog:",e);
        dialog.getContentPane().add(new JLabel("Exception occured ("+e.getMessage()+") Please consult the system administrator"));

        bModifyable = false; // just View

    }
    dialog.getContentPane().add(getMainDialogButtons(dialog,p_thing,bModifyable), BorderLayout.SOUTH);

    setDialogSize(dialog);

    dialog.setLocation();
    return dialog;
 }



 /**
  * 
  * @param p_classThing
  * @param p_knowledgeBase
  * @param p_thingDHelper can be null
  * @param p_owner type has to be JFrame or JDialog, or <code>null</code>
  * @return
  * @see THINGDialogFactory.THINGDialogHelper
  * @see THINGDialog 
  */
 public static THINGDialog createNewDialog(Class p_classThing, KnowledgeBase p_knowledgeBase, THINGDialogHelper p_thingDHelper, Window p_owner) 
 {
    boolean bModifyable = true;
    THINGDialog dialog = null;
    THING thing = null;
     

    if (Modifier.isAbstract(p_classThing.getModifiers()))
    {
        p_classThing = chooseSubclassForAbstractClass(p_classThing,p_owner);
        if (p_classThing == null)
            return null;
    }
    try
    {
        Object o = p_classThing.newInstance();
        if (!(o instanceof THING))
            throw new Exception("Given class is not a subclass of THING: " + p_classThing);
        
        thing = (THING) o;
    }
    catch (Exception e)
    {
        debug().error("createViewDialog:",e);
        dialog.getContentPane().add(new JLabel("Exception occured ("+e.getMessage()+") Please consult the system administrator"));

        bModifyable = false; // just View

    }


    return createNewDialog(thing,  p_knowledgeBase, p_thingDHelper, p_owner);
 }
/**
 * 
 * @param p_thing an instance of THING to be modified now; if no URI has been generated for the THING we will do this :)
 * @param p_knowledgeBase
 * @param p_thingDHelper  can be <code>null</code>
  * @param p_owner type has to be JFrame or JDialog, or <code>null</code>
 * @return
 * @see THINGDialogFactory.THINGDialogHelper
 */
 public static THINGDialog createNewDialog(THING p_thing, KnowledgeBase p_knowledgeBase,THINGDialogHelper p_thingDHelper, Window p_owner)
 {
    boolean bModifyable = true;
    THINGDialog dialog = null;
    THING thing = null;


    try
    {
        if (thing.getURI() == null)
            thing.makeNewURI();

        if (p_owner instanceof Dialog)
         dialog = new THINGDialog(thing,p_knowledgeBase,(Dialog)p_owner, p_thingDHelper);
        else
         dialog = new THINGDialog(thing, p_knowledgeBase,(Frame)p_owner, p_thingDHelper);

        dialog.setTitle("New - (" + thing.getClass().getName() +")");


        dialog.getContentPane().add(getMainPane(thing,true,true,dialog), BorderLayout.CENTER);
    }
    catch (Exception e)
    {
        debug().error("createViewDialog:",e);
        dialog.getContentPane().add(new JLabel("Exception occured ("+e.getMessage()+") Please consult the system administrator"));

        bModifyable = false; // just View

    }
    dialog.getContentPane().add(getMainDialogButtons(dialog,thing,bModifyable), BorderLayout.SOUTH);
    setDialogSize(dialog);
    dialog.setLocation();
    return dialog;
 }


 private static void setDialogSize(JDialog p_dialog)
 {
    p_dialog.pack();
    /*int maxdialogHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()-50;

    if (p_dialog.getSize().getHeight() > maxdialogHeight )
    {
       p_dialog.setSize(MAX_DIALOG_WIDTH,maxdialogHeight);
    }*/
    p_dialog.setResizable(false);
 }


 protected static JComponent getMainPane(THING p_thing, boolean p_editable,boolean p_populate, THINGDialog p_dialog)
 {
     
    PropertyStore ps = p_thing.getPropertyStore();

    Iterator it = ps.getPropertyInfos().iterator();


    Box box = new Box(BoxLayout.Y_AXIS );
    box.setSize(PANEL_WIDTH,PANEL_HEIGHT);
   // box.add(Box.createHorizontalStrut(PANEL_WIDTH));
//      box.add(Box.createHorizontalStrut(50));
//      box.add(Box.createHorizontalStrut(50));
    
    box.add(getURIPanel(p_thing, false,p_dialog));
    
    box.add(getRDFSLabelPanel(p_thing,p_editable,p_populate,p_dialog));
    
    
    while (it.hasNext())
    {
        PropertyInfo pi = (PropertyInfo) it.next();
        box.add(getInputElementPanel(p_thing, pi, p_editable, p_populate, p_dialog ));
    }


    JScrollPane spMain = new JScrollPane(box);
    spMain.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    spMain.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    return spMain;

 }

/**
 *
 * @param p_dialog
 * @param p_thing
 * @param p_editable
 * @return
 */
protected static JComponent getMainDialogButtons(final THINGDialog p_dialog, final THING p_thing, boolean p_editable)
{
  JPanel buttonPanel = new JPanel();

 if (p_editable)
 {
  final JButton  ok = new JButton("ok");
  //ok.setEnabled();
  ok.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {

        p_dialog.setDialogObject(p_thing);
        THINGDialogFactory.assignInfo(p_dialog);
        p_dialog.assign(p_thing);
        p_dialog.setVisible(false);
        p_dialog.setResult(JOptionPane.OK_OPTION);

    }
    });
  buttonPanel.add(ok);

 }


  final JButton  cancel = new JButton();
  if (p_editable)
    cancel.setText("cancel");
  else
    cancel.setText("dismiss");

  cancel.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
        p_dialog.setVisible(false);
        p_dialog.setDialogObject(null);
        p_dialog.setResult(JOptionPane.CANCEL_OPTION);
    }
   });

  buttonPanel.add(cancel);

  return buttonPanel;
}

/**
 * @param p_thing
* @param p_editable false if the element should only be for viewing purposes; true otherwise
 * @param p_dialog
 * @return
 */
public static JPanel getURIPanel(THING p_thing, boolean p_editable,JDialog p_dialog)
{

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    JLabel label = new JLabel("URI");
    label.setSize(LABEL_WIDTH,LABEL_HEIGHT);
    label.setPreferredSize(m_dimPanel);
    panel.add(label,BorderLayout.WEST);
    
    JTextField textURI = new JTextField(p_thing.getURI() );
    textURI.setEditable(p_editable);
    
    panel.add(textURI,BorderLayout.CENTER);

    panel.add(getViewRDFSourceButton(p_thing,p_dialog),BorderLayout.EAST);
    return panel;
}


public static JComponent getViewRDFSourceButton(final RDFResource p_res, final JDialog p_dialog)
{

    final JButton buttViewRDFSource = new JButton("V");
    buttViewRDFSource.setSize(MODIFY_BUTTON_WIDTH, MODIFY_BUTTON_HEIGHT);
    
    buttViewRDFSource.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {
            JDialog viewRDFSource = getRDFSourceDialog(p_res,p_dialog);
            viewRDFSource.setVisible(true);
        }

    });
    
    return buttViewRDFSource;
}


public static JDialog getRDFSourceDialog(RDFResource p_res, JDialog p_dialog)
{
    String rdfSource = p_res.toStringAsRdf();
    StringReader sr = new StringReader( rdfSource );
    RDFNice rdfNice = new RDFNice( sr );
//    for( int i = 0; i < tblPredValues.getRowCount(); i++ )
//    {
//        String sPred = (String)tblPredValues.getValueAt( i, 0 );
//        if( sPred == null  ||  sPred.length() <= 0 )
//            continue;
//        String sValue = (String)tblPredValues.getValueAt( i, 1 );
//        rdfNice.setPredValue( sPred, Double.parseDouble( sValue ) );
//    }
    rdfNice.createNiceXML();
    String sResult = rdfNice.serializeToString();

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());    

    final JDialog rdfDialog = new JDialog(p_dialog,"RDF Source: "+ p_res.getURI(),false);
    rdfDialog.getContentPane().add(panel);
    
    
    JTextArea textView = new JTextArea();
    textView.setText(sResult);
//    textView.setSize(400, 350);
    textView.setEditable(false);
    
    JScrollPane sp = new JScrollPane(textView);
    panel.add(sp, BorderLayout.CENTER);    
    panel.setPreferredSize(new Dimension(300, 350));
    
    
    final JButton  buttClose = new JButton();
    buttClose.setText("dismiss");

    buttClose.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {
            rdfDialog.setVisible(false);
        }
    });

    panel.add(buttClose, BorderLayout.SOUTH);    
    setDialogSize(rdfDialog);
    
    return rdfDialog;
}



/**
 * @param p_thing
* @param p_editable false if the element should only be for viewing purposes; true otherwise
 * @param p_populate true if the values should be inserted; false otherwise (e.g. for a creating a new element)
 * @param p_dialog
 * @return
 */
public static JPanel getRDFSLabelPanel(THING p_thing, boolean p_editable, boolean p_populate, THINGDialog p_dialog)
{

   JPanel panel = new JPanel();
   panel.setLayout(new BorderLayout());

   String fieldTitle = RDFSLABEL_FIELD_NAME;


   JLabel label = new JLabel(fieldTitle+":   ");
   label.setSize(LABEL_WIDTH,LABEL_HEIGHT);
   label.setPreferredSize(m_dimPanel);
   label.setHorizontalAlignment(SwingConstants.RIGHT);
   panel.add(label,BorderLayout.WEST);
      
   
   JTextField textField = new JTextField(TEXTFIELD_COLUMNS);
   textField.putClientProperty(COMPONENT_PROPERTYINFO,RDFSLABEL_FIELD_NAME);
   textField.setEditable(p_editable);
   if (p_populate) textField.setText(p_thing.getLabel());

     
   
   panel.add(textField,BorderLayout.CENTER);
   panel.setSize(PANEL_WIDTH,PANEL_HEIGHT);
//   panel.setPreferredSize(m_dimPanel);
   
   
   return panel;
}


/**
 * @param p_thing
 * @param p_pi
 * @param p_editable false if the element should only be for viewing purposes; true otherwise
 * @param p_populate true if the values should be inserted; false otherwise (e.g. for a creating a new element)
 * @param p_dialog
 * @return
 */
public static JPanel getInputElementPanel( THING p_thing, PropertyInfo p_pi, boolean p_editable, boolean p_populate, THINGDialog p_dialog)
 {

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());

    String fieldTitle = p_pi.getName();
    JLabel label = new JLabel(fieldTitle + ":   ");
    label.setSize(LABEL_WIDTH,LABEL_HEIGHT);
    label.setPreferredSize(m_dimPanel);
    label.setHorizontalAlignment(SwingConstants.RIGHT);
    panel.add(label,BorderLayout.WEST);
    panel.add(getValueComponent( p_thing, p_pi,p_editable,p_populate, p_dialog),BorderLayout.CENTER);
    panel.setSize(PANEL_WIDTH,PANEL_HEIGHT);
//    panel.setPreferredSize(m_dimPanel);
    return panel;
 }


/**
 * @param p_thing
 * @param p_pi
* @param p_editable false if the element should only be for viewing purposes; true otherwise
 * @param p_populate true if the values should be inserted; false otherwise (e.g. for a creating a new element)
 * @param p_dialog
 * @return
 */
public static JPanel getValueComponent( THING p_thing, PropertyInfo  p_pi, boolean p_editable, boolean p_populate,  THINGDialog p_dialog)
{
    //Box boxValue = new Box(BoxLayout.X_AXIS);
    //boxValue.add(Box.createHorizontalStrut(PANEL_WIDTH - LABEL_WIDTH));
    JPanel boxValue = new JPanel();
    boxValue.setLayout(new BorderLayout());

    JComponent specElement = getSpecificElement(p_thing, p_pi, p_editable, p_populate);
    JPanel panelButtons = new JPanel();
    panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.Y_AXIS));
    if (p_editable && specElement instanceof JList)
    {
        // here specElement is definetly a JList
        panelButtons.add(getListModifyButtons(p_thing,(JList)specElement,p_pi, p_dialog));
    }

    if (specElement instanceof JList)
    {
        // this is the case for all instance typed properties
        // add a view and modify button
        panelButtons.add(getViewButton((JList)specElement,p_dialog));
        panelButtons.add(getHelpButton(p_pi,p_dialog));
        if (p_editable) panelButtons.add(getModifyButton((JList)specElement,p_dialog));
    }

    if (specElement instanceof JList && p_pi.hasMultiValue())
    {
        // we need a ScrollPanel
        Dimension dim = new Dimension(INPUTFIELD_WIDTH,INPUTFIELD_HEIGHT * Math.max(((JList)specElement).getModel().getSize(), LIST_DEFAULT_MIN_ELEMENTS));
        specElement = new JScrollPane(specElement);
        ((JScrollPane)specElement).setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ((JScrollPane)specElement).setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//        boxValue.add(Box.createVerticalStrut(LISTPANEL_HEIGHT));
    }
    else
    {
        boxValue.add(Box.createVerticalStrut(INPUTFIELD_HEIGHT));

    }

    if (specElement instanceof JComboBox && !p_editable)
    {
        panelButtons.add(getHelpButton(p_pi,p_dialog));
    }

    /*if (specElement instanceof JTextField)
    {
        JTextField textField = (JTextField)specElement;
        // TODO this is a quick hack, maybe a more sophisticated URL finder is required
        if (textField.getText().startsWith("http") || textField.getText().startsWith("ftp") || textField.getText().startsWith("mailto:") || textField.getText().startsWith("file:"))
        {    
         JPanel buttons = new JPanel();
         buttons.add(getBrowserButton(textField,p_dialog));
         panelButtons.add(buttons,BorderLayout.CENTER);
        }

    }*/
    
    boxValue.add( specElement, BorderLayout.CENTER);
    //boxValue.add(Box.createHorizontalGlue());
    boxValue.add( panelButtons, BorderLayout.EAST);



    return boxValue;
}

/**
 * @param p_field
 * @param p_p_dialog
 * @return
 */
private static Component getBrowserButton(final JTextField p_field, final THINGDialog p_dialog)
{
    final JButton  browse = new JButton();
    //TODO BrowseIcon ??
    //browse.setIcon(UIDataAccess.getInstance().m_iconWebDoc);
    if (p_dialog == null) browse.setEnabled(false);

    browse.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e)
        {
            String url = p_field.getText();
            

            if (url != null && url.length()>0)
            {
                p_dialog.browse2Url(url);
            
            }
        }

    });
    
    return browse;
}

/**
 * @param p_list
 * @param p_dialog
 * @return
 */
protected static JButton getViewButton(final JList p_list, final THINGDialog p_dialog)
{
   final JButton  view = new JButton("V");
   view.setMinimumSize(new Dimension(MODIFY_BUTTON_WIDTH, MODIFY_BUTTON_HEIGHT));

    view.setEnabled(p_list.getModel().getSize() > 0);

  p_list.getModel().addListDataListener(new ListDataListener()
   {
       public void intervalAdded(ListDataEvent e)
       {
           view.setEnabled(p_list.getModel().getSize() > 0);
       }

       public void intervalRemoved(ListDataEvent e)
       {
        view.setEnabled(p_list.getModel().getSize() > 0);
       }

       public void contentsChanged(ListDataEvent e)
       {
        view.setEnabled(p_list.getModel().getSize() > 0);
       }
   });

   view.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
     Object o = p_list.getSelectedValue();
     RDFResource res = null;

     if (o == null)
     {
        // nothing selected -> if only one element in the list, show that
        ListModel lm = p_list.getModel();
        if (lm.getSize() == 1)
        {
           o = lm.getElementAt(0);
        }
        else
         return;
     }

     if (o instanceof ResourceObjectNode)
     {
        res = (RDFResource)((ResourceObjectNode)o).getUserObject();

         if (res instanceof THING)
         {
          THINGDialogFactory.createViewDialog((THING)res , p_dialog.getKnowledgeBase(), p_dialog.getTHINGDialogHelper(), p_dialog).setVisible(true);
         }
         else
         {
           // could be a class representation
           String className = getClassName(res);
           if (className != null)
           {
               JOptionPane.showMessageDialog(p_dialog," class " + className,"View Class",JOptionPane.INFORMATION_MESSAGE,ResourceObjectNode.getIcon(res));
           }
           else
               JOptionPane.showMessageDialog(p_dialog,res,"View RDFResource",JOptionPane.INFORMATION_MESSAGE);
         }
    }
     else if (o instanceof String)
     {
        JOptionPane.showMessageDialog(p_dialog,(String)o,"View element",JOptionPane.INFORMATION_MESSAGE);
     }
     else if (o == null)
     {
        return;
     }
     else
     {
        debug().error("getViewButton: view unknown type of object " + o);
        JOptionPane.showMessageDialog(p_dialog,o,"View unknown type",JOptionPane.INFORMATION_MESSAGE);

     }

    }
    });

    return view;
}



protected static JButton getHelpButton(final PropertyInfo  p_pi, final THINGDialog p_dialog)
{
   final JButton  help = new JButton("?");
   help.setSize(MODIFY_BUTTON_WIDTH, MODIFY_BUTTON_HEIGHT);

   help.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
      if (p_pi.getValueType() == PropertyInfo.VT_INSTANCE)
      {
       JList alloedval = new JList(new Vector(p_pi.getAllowedValueClasses()));
       JOptionPane.showMessageDialog(p_dialog,alloedval,"allowed classes",JOptionPane.INFORMATION_MESSAGE,ResourceObjectNode.getIconClass());
      }
      else if (p_pi.getValueType() == PropertyInfo.VT_SYMBOL)
      {
       JList allowedval = new JList(p_pi.getAllowedSymbols());
       JOptionPane.showMessageDialog(p_dialog,allowedval,"allowed symbols",JOptionPane.INFORMATION_MESSAGE);
      }
      else if (p_pi.getValueType() == PropertyInfo.VT_STRING)
      {
       String msg = (p_pi.hasMultiValue() ? "multiple strings" : "single string");
       JOptionPane.showMessageDialog(p_dialog,msg,"allowed values",JOptionPane.INFORMATION_MESSAGE);
      }

    }
    });

    return help;
}



protected static JButton getModifyButton(final JList p_list, final THINGDialog p_dialog)
{
   final JButton  modify = new JButton("M");
   modify.setSize(MODIFY_BUTTON_WIDTH, MODIFY_BUTTON_HEIGHT);

   modify.setEnabled(p_list.getModel().getSize() > 0);

   p_list.getModel().addListDataListener(new ListDataListener()
   {
       public void intervalAdded(ListDataEvent e)
       {
        modify.setEnabled(p_list.getModel().getSize() > 0);
       }

       public void intervalRemoved(ListDataEvent e)
       {
        modify.setEnabled(p_list.getModel().getSize() > 0);
       }

       public void contentsChanged(ListDataEvent e)
       {
        modify.setEnabled(p_list.getModel().getSize() > 0);
       }
   });


   modify.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
     Object o = p_list.getSelectedValue();
     RDFResource res = null;

     if (o == null)
     {
        // nothing selected -> if only one element in the list, show that
        ListModel lm = p_list.getModel();
        if (lm.getSize() == 1)
        {
           o = lm.getElementAt(0);
        }
        else
         return;
     }

     if (o instanceof ResourceObjectNode)
     {
        res = (RDFResource)((ResourceObjectNode)o).getUserObject();

         if (res instanceof THING)
         {
           THINGDialog modDialog = THINGDialogFactory.createModifyDialog( (THING)res ,p_dialog.getKnowledgeBase(), p_dialog.getTHINGDialogHelper(), p_dialog);
           modDialog.setVisible(true);
           if (modDialog.hasModifiedTHINGs())
           {
            if (modDialog.getDialogObject() != null)            // the dialog could habve been cancelled (the the dialog object == null)
                p_dialog.addModifiedTHING(modDialog.getDialogObject());
            p_dialog.addModifiedTHING(modDialog.getModifiedTHINGs());
           }

         }
         else
         {
           // could be a class representation
           String className = getClassName(res);
           if (className != null)
           {
               JOptionPane.showMessageDialog(p_dialog," class " + className,"View Class",JOptionPane.INFORMATION_MESSAGE,ResourceObjectNode.getIcon(res));
           }
           else
               JOptionPane.showMessageDialog(p_dialog,res,"View RDFResource",JOptionPane.INFORMATION_MESSAGE);
         }
     }
     else if (o instanceof String)
     {
        JOptionPane.showMessageDialog(p_dialog,(String)o,"View element",JOptionPane.INFORMATION_MESSAGE);
     }
     else if (o == null)
     {
        return;
     }
     else
     {
        debug().error("getModifyButton: view unknown type of object " + o);
        JOptionPane.showMessageDialog(p_dialog,o,"Modify unknown type",JOptionPane.INFORMATION_MESSAGE);

     }
     //Oleg Rostanin The List must be repainted after changing the object
     p_list.repaint();
    }
    });

    return modify;
}

protected static JButton getCreateButton(final THING p_thing, final JList p_list, final PropertyInfo  p_pi, final THINGDialog p_dialog)
{
   final JButton  create = new JButton("C");
   create.setMinimumSize(new Dimension(MODIFY_BUTTON_WIDTH, MODIFY_BUTTON_HEIGHT));

   create.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
     Object[] allowedClasses = p_pi.getAllowedValueClasses().toArray();
     
     if (allowedClasses == null)
     {
      debug().error("getCreateButton: no allowed classes given for property " + p_pi.getName());
      JOptionPane.showMessageDialog(p_dialog,"no allowed classes given ! (should be an error, plase contact the system designer(s))","Create",JOptionPane.ERROR_MESSAGE);
     }
     else
     {
       // first if more than one allowed class, choose one !
       Class instanceOfClass;
       if ( allowedClasses.length > 1 )
       {
      /*  JList listClasses = new JList(allowedClasses);
        listClasses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane spClasses = new JScrollPane(listClasses);
        spClasses.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spClasses.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
*/

        instanceOfClass = (Class)JOptionPane.showInputDialog(p_dialog,"Please choose the class for an instance","Create",JOptionPane.QUESTION_MESSAGE,null,allowedClasses,allowedClasses[0]);

        if (instanceOfClass == null ) return; // user has cancelled the selection
       }
       else
        instanceOfClass = (Class)allowedClasses[0];

       THINGDialog dialog = THINGDialogFactory.createNewDialog(instanceOfClass, p_dialog.getKnowledgeBase(), p_dialog.getTHINGDialogHelper(), p_dialog);
       if (dialog == null ) return;
       dialog.setVisible(true);

       if (!dialog.hasModifiedTHINGs())
            return;

        THING t =     dialog.getDialogObject();
        if (t != null)            // the dialog could have been cancelled (the the dialog object == null)
        {
            p_dialog.addModifiedTHING(t);
            p_dialog.assign(t);
        }

        p_dialog.addModifiedTHING(dialog.getModifiedTHINGs());


       if (!p_pi.hasMultiValue()) ((DefaultListModel)p_list.getModel()).clear();
       ((DefaultListModel)p_list.getModel()).addElement(new ResourceObjectNode(t));

     }


    }
    });

    return create;
}

/**
 * 
 * @param p_thing
 * @param p_list
 * @param p_pi
 * @param p_dialog
 * @return
 */
protected static JComponent getListModifyButtons(final THING p_thing, final JList p_list, final PropertyInfo  p_pi, final THINGDialog p_dialog)
{
  JPanel buttonPanel = new JPanel();
  buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

  final JButton  add = new JButton("+");
  add.setMinimumSize(new Dimension(MODIFY_BUTTON_WIDTH, MODIFY_BUTTON_HEIGHT));

   
  add.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
     InstanceChooseDialog dialog = new InstanceChooseDialog((Class[])p_pi.getAllowedValueClasses().toArray(), p_pi.hasMultiValue(), p_dialog.getKnowledgeBase(), p_dialog.getTHINGDialogHelper(),p_dialog);
     
     
     DefaultListModel lm = (DefaultListModel) p_list.getModel();

     if (p_pi.hasMultiValue())
     {
        Collection c = dialog.selectInstances();
        if (c != null && !c.isEmpty())
        {
            Iterator it = c.iterator();

            while (it.hasNext())
            {
                Object o = it.next();
                if (!lm.contains(o)) lm.addElement(o);
            }
        }
     }
     else
     {
        ResourceObjectNode node = dialog.selectInstance();
        if (node != null)
        {
          // remove any existing node

          if ( !lm.isEmpty() )
          {
            lm.removeAllElements();
          }

          lm.addElement(node);
        }

     }
    }
    });



  final JButton  remove = new JButton("-");
  remove.setMinimumSize(new Dimension(MODIFY_BUTTON_WIDTH, MODIFY_BUTTON_HEIGHT));
  remove.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent e)
    {
        int i = p_list.getSelectedIndex();

        if (i >= 0)
        {
            ((DefaultListModel)p_list.getModel()).remove(i);
            /**
             * @todo element in list immer noch da
             */
        }
//  for security reasons not used!
//        else if (p_list.getModel().getSize() == 1 )
//        {
//            // nothing selected -> but it is only one element in the list: remove
//            ((DefaultListModel)p_list.getModel()).remove(0);
//
//        }

    }
   });
  remove.setEnabled(p_list.getModel().getSize() > 0);
  p_list.getModel().addListDataListener(new ListDataListener()
   {
       public void intervalAdded(ListDataEvent e)
       {
           remove.setEnabled(p_list.getModel().getSize() > 0);
       }

       public void intervalRemoved(ListDataEvent e)
       {
        remove.setEnabled(p_list.getModel().getSize() > 0);
       }

       public void contentsChanged(ListDataEvent e)
       {
        remove.setEnabled(p_list.getModel().getSize() > 0);
       }
   });

  buttonPanel.add(add);
  buttonPanel.add(remove);
  buttonPanel.add(getCreateButton( p_thing,p_list,p_pi,p_dialog));
  return buttonPanel;
}


/**
 * @param p_thing
 * @param p_pi
 * @param p_editable false if the element should only be for viewing purposes; true otherwise
 * @param p_populate true if the values should be inserted; false otherwise (e.g. for a creating a new element)
 * @return
 */
protected static JComponent getSpecificElement(THING p_thing, PropertyInfo  p_pi,boolean p_editable, boolean p_populate)
{
 JComponent com = null;

 if (p_pi.hasMultiValue())
 {
    com = getListElement(p_thing,p_pi,p_populate);
 }
 else
 {
    switch (p_pi.getValueType())
    {

        case PropertyInfo.VT_INSTANCE:    // single instance fields
            com = getListElement(p_thing,p_pi,p_populate);
            break;
        case PropertyInfo.VT_SYMBOL:    // symbol fields
                com = getSymbolElement(p_pi, p_editable, p_populate);
        break;
        case PropertyInfo.VT_UNKNOWN: debug().error("getSpecificElement: PropertyInfo.VT_UNKNOWN of " + p_pi.getName());
                // go on, treat it as a String
        case PropertyInfo.VT_STRING:  // string fields
            com = getStringElement(p_pi,p_editable,p_populate);
        break;
    }
 // single case: directly add the corresponding detail
 }

 return com;

}

/**
 * @param p_thing
 * @param p_pi
 * @param p_populate true if the values should be inserted; false otherwise (e.g. for a creating a new element)
 * @return
 */
protected static JList getListElement(THING p_thing, PropertyInfo  p_pi, boolean p_populate)
{

    DefaultListModel elements =  new DefaultListModel();

    JList list = new JList(elements);

    list.putClientProperty(COMPONENT_PROPERTYINFO,p_pi);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    if (p_pi.getValueType() == PropertyInfo.VT_INSTANCE)
        list.setCellRenderer(new ResourceObjectNode.ResourceObjectNodeCellRenderer());

    if (p_populate)
    {

      if (p_pi.hasMultiValue())
      {
        Iterator it = ((Collection)p_pi.getValue()).iterator();

        while (it.hasNext())
        {
            Object element;

            if (p_pi.getValueType() == PropertyInfo.VT_INSTANCE)
            {
             element= new ResourceObjectNode((RDFResource)it.next());
            }
            else
             element = it.next();

            elements.addElement(element);
        }
      }
      else
      {
        try
        {
            RDFResource res = (RDFResource)p_pi.getValue();
            if (res != null) elements.addElement(new ResourceObjectNode(res));
        }
        catch (ClassCastException e)
        {
            elements.addElement(new javax.swing.tree.DefaultMutableTreeNode(p_pi.getValue()));
        }
      }

     if (p_pi.hasMultiValue())
     {
        Dimension dim = new Dimension(INPUTFIELD_WIDTH,INPUTFIELD_HEIGHT * Math.max(elements.size(), LIST_DEFAULT_MIN_ELEMENTS));
        list.setSize(dim);
        list.setPreferredSize(dim);
     }
     else
        list.setPreferredSize(m_dimSingleListPanel);


    }


    return list;
}

//protected static void getAccByListDialog(Document doc)
//{
//
//    DefaultListModel elements =  new DefaultListModel();
//
//    JList list = new JList(elements);
//
//    list.putClientProperty(COMPONENT_PROPERTYINFO,doc);
//    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//    list.setCellRenderer(new ResourceObjectNode.ResourceObjectNodeUserCellRenderer());
//
//    Iterator it = doc.getAccessibleBy().iterator();
//
//       while (it.hasNext())
//       {
//           Object element;
//            element= new ResourceObjectNode((RDFResource)it.next());
//
//           elements.addElement(element);
//       }
//
//    Dimension dim = new Dimension(INPUTFIELD_WIDTH,INPUTFIELD_HEIGHT * Math.max(elements.size(), LIST_DEFAULT_MIN_ELEMENTS));
//    list.setSize(dim);
//    list.setPreferredSize(dim);
//    list.addMouseListener(new ResourceObjectNode.ResourceObjectNodeUserClickAdapter(list));
//    //list.setBackground(UserManagerBrowser.BACKGROUND_COLOR);
//    JOptionPane.showMessageDialog(null, list, "", JOptionPane.PLAIN_MESSAGE);
//}


/**
 * @param p_pi
 * @param p_editable false if the element should only be for viewing purposes; true otherwise
 * @param p_populate true if the values should be inserted; false otherwise (e.g. for a creating a new element)
 * @return
 */
protected static JTextField getStringElement(PropertyInfo  p_pi, boolean p_editable, boolean p_populate)
{
    JTextField textField = new JTextField(TEXTFIELD_COLUMNS);
    textField.putClientProperty(COMPONENT_PROPERTYINFO,p_pi);
    textField.setEditable(p_editable);
    if (p_populate) textField.setText((String)p_pi.getValue());

    return textField;
}


public static java.io.File getFile(JComponent frame)
{
    java.io.File newFile=null;
    JFileChooser chooser = new JFileChooser();
    int i = chooser.showOpenDialog(frame); 
    if(i==JFileChooser.APPROVE_OPTION)
      newFile = chooser.getSelectedFile();
    return newFile;
}


/**
 * @param p_pi
 * @param p_editable false if the element should only be for viewing purposes; true otherwise
 * @param p_populate true if the values should be inserted; false otherwise (e.g. for a creating a new element)
 * @return
 */
protected static JComboBox getSymbolElement(PropertyInfo  p_pi, boolean p_editable, boolean p_populate)
{
    JComboBox comboBox = new JComboBox(p_pi.getAllowedSymbols());
    comboBox.setEnabled(p_editable);
    comboBox.putClientProperty(COMPONENT_PROPERTYINFO,p_pi);
     if (p_populate) comboBox.setSelectedItem(p_pi.getValue());

    return comboBox;

}

/**
 * Assigns the information in the THINGDialog to the PropertyInfos attached to the components
 * @param p_dialog
 */
protected static void assignInfo(THINGDialog p_dialog)
{
 Iterator it = Arrays.asList(p_dialog.getContentPane().getComponents()).iterator();
 THING thing = p_dialog.getDialogObject();
 int i = 0;
 while (it.hasNext())
 {
  JComponent comp = (JComponent)it.next();
  findPropertyInfo(thing, comp);
 }
}

protected static void findPropertyInfo(THING p_thing, Component p_com)
{
   PropertyInfo pi = null;

   // due to rdfs:label is no PropertyInfo, we need to test also for String  
   if (p_com instanceof JComponent)
   {
       Object obj = ((JComponent)p_com).getClientProperty(COMPONENT_PROPERTYINFO);
       if (obj instanceof PropertyInfo)
       {
           pi = (PropertyInfo)obj;    
       }
       else if (obj instanceof String)
       {
           String componentLabel = (String)obj;
           
           if (componentLabel.equals(RDFSLABEL_FIELD_NAME))
           {
               // found rdfs:label
               p_thing.putLabel(((JTextField)p_com).getText());
               return;
           }
       }
       
   }

   if (pi == null)
   {
    // there is no property info - try if there are PropertyInfos in subComponents ...
    Object[] components = null;


    if (p_com instanceof Box)
    {
        components = ((Box)p_com).getComponents();
    }
    else if (p_com instanceof JComponent)
    {
        components = ((JComponent)p_com).getComponents();
    }


    if (components == null) return;

    Iterator it = Arrays.asList(components).iterator();

     while (it.hasNext())
     {
      findPropertyInfo(p_thing,(Component)it.next());
     }

     return;
   }
   else
   {
    // found one !
    fillPropertyInfo(p_com, pi);
   }

}

protected static void fillPropertyInfo(Component p_com, PropertyInfo p_pi)
{
    // components can be JTextField, JList and JComboBox
    if (p_com instanceof JList)
    {
        DefaultListModel lm = (DefaultListModel)((JList)p_com).getModel();

        if (p_pi.hasMultiValue())
        {
             Vector values = new Vector();
             Enumeration e = lm.elements();
             while (e.hasMoreElements())
             {
                values.add( ((DefaultMutableTreeNode)e.nextElement()).getUserObject() );
             }
             if (values.isEmpty())
               p_pi.clearValue();
             else
               p_pi.setValues( values );
        }
        else
        {
            if (lm.size() > 0)
              p_pi.putValue( ((DefaultMutableTreeNode)lm.firstElement()).getUserObject() );
            else
              p_pi.clearValue();
        }
    }
    else if (p_com instanceof JTextField)
    {
         String value = ((JTextField)p_com).getText();

         if (value != null && value.length() > 0)
           p_pi.putValue( value );
         else
           p_pi.clearValue();

    }
//    else if (p_com instanceof JFilePanel)
//    {
//        String value = ((JFilePanel)p_com).getFileName();
//        String path = ((JFilePanel)p_com).getFilePath();
//
//         if (value != null && value.length() > 0)
//         {
//            dfki.km.model.infotype.File file = (dfki.km.model.infotype.File)p_pi.getValue();
//            file.putValue(value);
//            file.putLocPath(path);
//         }
//         else
//           p_pi.clearValue();
//
//    }
    else if (p_com instanceof JComboBox)
    {
        p_pi.putValue( ((JComboBox)p_com).getSelectedItem() );
   }
}

 /**
  * todo is there any dynamic version of this method? -> find all subclasses of this class, e.g. by starting
  *  only within the package, or list all classes and find the subclasses etc.
  * @param p_classThing
  * @param p_owner
  * @return one class, null if nothing found
  */
private static Class chooseSubclassForAbstractClass(Class p_classThing, Window p_owner)
{
    // p_class is abstract, therefore find subclasses to instantiate
    // collect all subclasses
    Class[] subClasses = null;

    try
    {
      java.lang.reflect.Field field = p_classThing.getField("KNOWN_SUBCLASSES");
      subClasses = (Class[])field.get(null);
    }
    catch (Exception e)
    {
        debug().error("chooseSubclassForAbstractClass:",e);
        return null;
    }

    if (subClasses == null || subClasses.length == 0)
    {
        return null;
    }

    Class selectedClass=null;

    if (subClasses.length > 1)
    {
      selectedClass = (Class)JOptionPane.showInputDialog(p_owner,"Please choose the class for an instance","Create",JOptionPane.QUESTION_MESSAGE,null,subClasses,subClasses[0]);

     if (selectedClass == null )
        return null; // user has cancelled the selection

    }
    else
        selectedClass = subClasses[0];

    if (Modifier.isAbstract(selectedClass.getModifiers()) )
        return chooseSubclassForAbstractClass(selectedClass, p_owner);

    return selectedClass;
}



/**
 * select one class from the given array of classes via showInputDialog
 * @param p_classes
 * @param p_owner
 * @return chosen class
 * @see JOptionPane#showInputDialog
 */
public static Class chooseClass(Class[] p_classes, Window p_owner)
{
 return (Class)JOptionPane.showInputDialog(p_owner,"Please choose a class to instantiate","Choose class",JOptionPane.QUESTION_MESSAGE,ResourceObjectNode.getIconClass(),p_classes,p_classes[0]);
}


/**
 * Choose an instance from the KnowledgeBase. Allowed classes to choose from are retrieved from the THINGDialogHelper
 * @param p_knowledgeBase
 * @param p_thingDHelper  must not be null, from here the allowed classes are retrieved
 * @param p_owner
 * @return
 * @see THINGDialogFactory.THINGDialogHelper
 * @see InstanceChooseDialog 
 */
public static THING chooseAnyInstance(KnowledgeBase p_knowledgeBase, THINGDialogFactory.THINGDialogHelper p_thingDHelper, Window p_owner)
{
  InstanceChooseDialog dialog = new InstanceChooseDialog( p_thingDHelper.getAllClasses(),false,p_knowledgeBase, p_thingDHelper,p_owner);

  DefaultMutableTreeNode node = dialog.selectInstance();

  if (node == null) return null;

  return (THING)node.getUserObject();


}

/**
 * Choose an instance of the given class from the KnowledgeBase
 * @param p_class
 * @param p_knowledgeBase
 * @param p_thingDHelper
 * @param p_owner
 * @return
 * @see InstanceChooseDialog
 */
public static THING chooseInstanceOfClass(Class p_class,KnowledgeBase p_knowledgeBase,THINGDialogFactory.THINGDialogHelper p_thingDHelper, Window p_owner)
{
  InstanceChooseDialog dialog = new InstanceChooseDialog( new Class[]{ p_class },false,p_knowledgeBase, p_thingDHelper, p_owner);

  DefaultMutableTreeNode node = dialog.selectInstance();

  if (node == null) return null;

  return (THING)node.getUserObject();


}




/**
 * Choose an instance of the given classes from the KnowledgeBase
 * @param p_colClasses classes as Collection of Class
 * @param p_knowledgeBase
 * @param p_thingDHelper
 * @param p_owner
 * @return
 * @see InstanceChooseDialog
 */
public static THING chooseInstanceOfClasses(Collection p_colClasses,KnowledgeBase p_knowledgeBase, THINGDialogFactory.THINGDialogHelper p_thingDHelper, Window p_owner)
{
    InstanceChooseDialog dialog = new InstanceChooseDialog((Class[]) p_colClasses.toArray(),false,p_knowledgeBase, p_thingDHelper,p_owner);
    
   DefaultMutableTreeNode node = dialog.selectInstance();
        
   if (node == null) return null;

   return (THING)node.getUserObject();


}


/**
 * Choose multiple instances of classes from the KnowledgeBase
 * @param p_classes array of Class
 * @param p_knowledgeBase
 * @param p_thingDHelper
 * @param p_owner
 * @return Collection of selected instances; could be empty but not <code>null</code>
 * @see InstanceChooseDialog
 * 
 */
public static Collection chooseInstancesOfClasses(Class[] p_classes,KnowledgeBase p_knowledgeBase, THINGDialogFactory.THINGDialogHelper p_thingDHelper, Window p_owner)
{
    InstanceChooseDialog dialog  = new InstanceChooseDialog(p_classes,true, p_knowledgeBase, p_thingDHelper, p_owner);
    
    Collection nodes = dialog.selectInstances();
    
    if (nodes == null) return Collections.EMPTY_LIST;

    Iterator it = nodes.iterator();
    
    ArrayList result = new ArrayList();
    
    while (it.hasNext()){
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)it.next();
        
        result.add((THING)node.getUserObject());
    }
    
    
    return result;


}


/**
 * delivers the full classname  if the given resource is a representation of a class
 * @param p_res
 * @return full classname if it's a class; <code>null</code> otherwise
 */
public static String getClassName(RDFResource p_res)
{
  String className = "";

  
  
  if (p_res instanceof THING)
  {
      className =  p_res.getClass().getPackage() + "." + p_res.getClass().getName();
  }
  else
  {
   // try to figure out the RDFSClass
    Resource resClass = p_res.getRDFSClass();
 
    if (resClass != null)
    {
        try
        {
            className = "(" + resClass.getLocalName() + ")";
        }
        catch( ModelException e )
        {
            e.printStackTrace();
        }
    }
  }  
  
 return className;
}


    /**
     * an interface with helper functions in THINGDialogs
     * 
     */

    public interface THINGDialogHelper
    {

        /**
         *  browser registration for use in THINGDialogs: browse to the url given in <code>p_url</code>
         * @param p_url
         */
        public void setURL(String p_url);
        
        /**
         * flag if browse functionality is enabled, i.e., if setURL is implemented and browsing allowed
         * @return <code>true</code> if browse functionality is enabled, <code>false</code> otherwise
         */
        public boolean isBrowserEnabled();
        /**
         * lists all classes from which new instances can be created (e.g., in the New dialog
         * @return array of classes
         */
        public Class[] getAllClasses();
        
        /**
         * if wanted, delivers an instance of the InstanceChooseDialog.InstanceChooseDialogHelper which allows to provide custom tabs for selecting instances 
         * @return an instance, or <code>null</code> if no customization is wanted
         * @see InstanceChooseDialog.InstanceChooseDialogHelper
         */
        public InstanceChooseDialog.InstanceChooseDialogHelper getChooseDialogHelper();
    }

}