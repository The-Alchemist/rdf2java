/*
 * Created on 20.09.2004
 */
package dfki.rdf.util.gui;


import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;


import org.w3c.rdf.model.ModelException;
import org.w3c.rdf.model.Resource;

import dfki.rdf.util.RDFResource;
import dfki.rdf.util.THING;

public class ResourceObjectNode extends DefaultMutableTreeNode
{
    
    public static IconLoader ICONLOADER = new IconLoader();
    
    
    public static void addIconsFromResource(String p_iconResourceBundleClass)
    {
        ICONLOADER.addIconsFromResourceBundle(p_iconResourceBundleClass);
    }

    /**
     *  if <code>p_object</code> is <code>null</code> an <code>Error</code> is thrown
     * @param p_object a non-null RDFResource
     */
    public ResourceObjectNode (RDFResource p_object)
    {
        super (p_object);
        if (p_object == null)
        {
            throw new Error("ResourceObjectNode: the user object is null");
        }
  }


    public String getURI()
    {
      return ((RDFResource)getUserObject()).getURI();
    }


public String toString()
{
 return getStringRepresentation( (RDFResource)this.getUserObject() );
}

public static String getStringRepresentation(RDFResource res)
{
        if ( ! (res instanceof THING) )
        {
            // just try if there's a label
            String label = res.getLabel();           
            if (label != null) return label;

            
            Resource resClass = res.getRDFSClass();
            
            if (resClass != null)
            {
                try
                {
                    return "(" + resClass.getLocalName() + ")";
                }
                catch( ModelException e )
                {
                     e.printStackTrace();
                }
            }

         }


    return  getDisplayName(res);

    }


/**
 * provides a display name: rdfs:label if available, otherwise a combination of class and local name 
 * @param p_res
 * @return
 */
private static String getDisplayName(RDFResource p_res)
{
    if (p_res == null) return "<null>";

    String label = p_res.getLabel();
    
    if (label != null) return label;
    
    String className="";
    
    Resource resClass = p_res.getRDFSClass();
    
    if (resClass != null)
    {
        try
        {
            return "(" + resClass.getLocalName() + ")";
        }
        catch( ModelException e )
        {
             e.printStackTrace();
        }
    }

    
    return "("+ className +") " + p_res.getLocalName();
}





    public ImageIcon getIcon()
    {
        return getIcon((RDFResource) this.getUserObject());
    }


    public static ImageIcon getIconClass()
    {

        return ICONLOADER.getIcon(IconLoader.ICON_CLASS);
    }
    public static ImageIcon getIcon(RDFResource res)
    {
        ImageIcon icon=null;
        Resource resClass = res.getRDFSClass();

        if ( ! (res instanceof THING) )
        {
           
           if (resClass == null)
             return ICONLOADER.getIcon(IconLoader.ICON_RDFRESOURCE);

         }

       

        String sURI="";
        try
        {
            sURI = resClass.getURI();
        }
        catch( ModelException e )
        {
            e.printStackTrace();
            return ICONLOADER.getIcon(IconLoader.ICON_UNKNOWN);
        }

        if (resClass == null)
        {
            System.err.println("ResourceObjectNode: getIcon:RDFResource has no class, therefore no icon found for URI " + sURI);
            return ICONLOADER.getIcon(IconLoader.ICON_UNKNOWN);
        }

        icon = ICONLOADER.getIcon(sURI);
        
        if (icon == null)
        {
            System.err.println("ResourceObjectNode: getIcon: no icon found for URI " + sURI);
            icon = ICONLOADER.getIcon(IconLoader.ICON_UNKNOWN);
        }
            
        
       
       
        
        return icon;

    

    }



    public static class ResourceObjectNodeCellRenderer extends JLabel implements ListCellRenderer {
        public ResourceObjectNodeCellRenderer() {
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
            setBackground(isSelected ? Color.red : Color.white);
            setForeground(isSelected ? Color.white : Color.black);


           ResourceObjectNode node = (ResourceObjectNode) value;
           setIcon(node.getIcon());

            return this;
        }
    }

    public static class ResourceObjectNodeUserCellRenderer extends JLabel implements ListCellRenderer {
        public ResourceObjectNodeUserCellRenderer() {
            setOpaque(true);
        }
        
        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
        {
            setText(((ResourceObjectNode) value).getUserObject().toString());
            setBackground(isSelected ? Color.blue : Color.white);
            setForeground(isSelected ? Color.white : Color.black);


           ResourceObjectNode node = (ResourceObjectNode) value;
           setIcon(node.getIcon());

            return this;
        }
    }

 public static class ResourceObjectNodeTreeCellRenderer extends DefaultTreeCellRenderer {

    public ResourceObjectNodeTreeCellRenderer() {
      }

    public Component getTreeCellRendererComponent(
                          JTree tree,
                          Object value,
                          boolean sel,
                          boolean expanded,
                          boolean leaf,
                          int row,
                          boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if(value instanceof ResourceObjectNode)
        {
            ResourceObjectNode node = (ResourceObjectNode)value;
            setIcon(node.getIcon());
        }
      return this;
    }
  }

 public static class ResourceObjectNodeUserClickAdapter extends MouseAdapter{
    private JList list;
    
    
    public ResourceObjectNodeUserClickAdapter(JList list) {
        super();
        this.list = list;
        
    }
    
    public void mouseClicked(MouseEvent e)
    {
        if(e.getClickCount()>1)
        {
            showDialog();
        }
    }
    
    public void showDialog()
    {
        int i = list.getSelectedIndex();
        if(i>=0)
        {
            Object node = (ResourceObjectNode)list.getModel().getElementAt(i);
            if(node!=null && node instanceof ResourceObjectNode)
            {
                Object da = ((ResourceObjectNode)node).getUserObject();
//                if(da != null && da instanceof WebDAVObject)
//                {
//                    tama.viewFileDialog((WebDAVObject)da);
//                }
//                else
//                if(da instanceof WebObject)
//                {
//                    tama.startUrl((WebObject)da);
//                }
            }
        }
    }
    
}


}