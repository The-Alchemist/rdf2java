/*
 * Created on 20.09.2004
 */
package dfki.rdf.util.gui;

import java.awt.Toolkit;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import dfki.util.config.Config;
import dfki.util.debug.Debug;

/**
 * @author maus
 */

/**
 * the icons for the THINGDialog suite<br>
 * In the ResourceObjectNode an icon for each RDFSClass is used for showing instances.
 * he mapping can be done in an config XML-file (Config) as a mapping of URI (of the RDFSClass) to path to the icon
 * as well as in a Java properties file. The class name of the properties-file can be given in the constructor or added separately<br>
 * Because in the Java properties file URIs with <code>:</code> (as in <code>http://...</code>) make problems, the following convenience method is used:<br>
 * if a loadIcon with a given string doesn't find an icon, the string is tested if it is a URI with a local name (test for  <code>#</code>), in this case the 
 * local name is used for retrieving an icon. If still no icon is found the  <code>ICON_UNKNOWN</code> is returned.
 * @see dfki.util.config.Config
 * @see dfki.rdf.util.gui.THINGDialogProperty.Property#URI_2_ICONS_PATH_MAP
 * @see dfki.rdf.util.gui.resources.icons
 * @see #addIconsFromResourceBundle(String)
 * @see #ICON_UNKNOWN
 */
public class IconLoader
{
    /**
     * internal map from strings to icons
     */
    protected static ResourceBundle m_resourceBundle;
    private String m_customResourceBundleClass;

    protected static final String m_localResourceBundleClass = "dfki.rdf.util.gui.resources.icons";
    
    
    private HashMap m_uri2iconsMap;
    private Config m_cfg;   
    
    public static final String ICON_UNKNOWN = "unknown";
    public static final String ICON_CLASS   = "class";
    public static final String ICON_RDFRESOURCE  = "rdfresource";
    public static final String ICON_BUTTONBROWSE  = "buttonBrowse";
   
    
    
    public IconLoader(String p_customResourceBundleClass)
    {

        m_customResourceBundleClass  = p_customResourceBundleClass;
         m_cfg = Config.cfg();
         initImageIcons();
   
    }

    
    public IconLoader()
    {
        m_customResourceBundleClass = null;
        
        m_cfg = Config.cfg();
        initImageIcons();
    }
    
    
    
       
    public static Debug debug()
    {
        return Debug.forModule( "IconLoader" );
    }
   

    
   
    private void initImageIcons()
    {
        HashMap guiIcons = new HashMap();
        m_uri2iconsMap = new HashMap();
        
        
        addIconsFromResourceBundle(m_localResourceBundleClass);
        if (m_customResourceBundleClass != null) addIconsFromResourceBundle(m_customResourceBundleClass);
        
        

        
        String[] guiPicsURI = m_cfg.getPropertyValues( THINGDialogProperty.Property.URI_2_ICONS_PATH_MAP );

        if (guiPicsURI == null)
        {    
            debug().message("initImageIcons: no icons listed in config XML-file for Property: THINGDialogProperty.Property.URI_2_ICONS_PATH_MAP: " +  THINGDialogProperty.Property.URI_2_ICONS_PATH_MAP);
            return;
        }
        
        try
        {
            for( int i = 0; i < guiPicsURI.length; i += 2 )
            {

                m_uri2iconsMap.put( guiPicsURI[ i ], loadIcon(guiPicsURI[ i + 1]) );
            }
        }
        catch( Exception ex )
        {
            debug().error("initImageIcons:",ex);

        }


    }

   /**
    * loads the icon with the given icon path<br>
     * @param string icon path 
     * @return ImageIcon if found, <code>null</code> otherwise
     */
    private ImageIcon loadIcon( String iconPath )
    {
        if (iconPath == null) return null;
        java.net.URL url = IconLoader.class.getResource( iconPath );
        if( url == null ) 
        {
           debug().error("initImageIcons: no icon found for " +  iconPath);
           return null;
        }
        return new ImageIcon( Toolkit.getDefaultToolkit().getImage( url ) );
    }


/**
    * delivers the ImageIcon associated with the given URI resp. string<br>
     * if no icon is found, the iconPath is tested if it is a URI with a local name, then the local name is taken as<br>
     * see also class documentation
    * @param sURI URI or string 
    * @return the respective icon
    * @see #ICON_UNKNOWN
    * @see #ICON_CLASS
    * @see #ICON_RDFRESOURCE
    * @see #setIcon
    * @see IconLoader
    */
    public ImageIcon getIcon(String sURI) 
    {
        Object icon =  m_uri2iconsMap.get(sURI);
        
        if( icon == null && sURI != null) 
        {
            int i = sURI.indexOf('#');
            
            if (i != -1 )
            {
                // seems to be a URI, let's try its local name
                String localName = sURI.substring(i+1);
                icon =  m_uri2iconsMap.get(localName);
            }
        }
        
        if (icon == null) 
        {
            debug().error("IconNotFound for URI: " + sURI);
            if (!sURI.equals(ICON_UNKNOWN))
                return getIcon(ICON_UNKNOWN);
            else 
                return null;
        }
        
        return (ImageIcon)icon;
    }

    
    public void setIcon(String sURI, ImageIcon icon)
    {
      m_uri2iconsMap.put( sURI , icon );
    }
    
    
    public void addIconsFromResourceBundle(String  p_iconResourceBundleClass )
    {
        ResourceBundle resourceBundle = null;
        
        if (p_iconResourceBundleClass == null)
        {
            debug().error("addIconsFromResourceBundle: parameter p_iconResourceBundleClass is null.");
            return;

        } 
        try
        {
            resourceBundle = ResourceBundle.getBundle(p_iconResourceBundleClass);
        }
        catch(Exception x)
        {
            debug().error("addIconsFromResourceBundle: load resource bundle: " + p_iconResourceBundleClass,x);
            return;
        }
       
        
        if (resourceBundle != null)
        {
            Enumeration enum =  resourceBundle.getKeys();
            
            while (enum.hasMoreElements())
            {
                String iconName = (String)enum.nextElement();
                String iconPath = resourceBundle.getString(iconName);
//                debug().message("iconName: " + iconName +"  iconPath: " + iconPath);
                
                m_uri2iconsMap.put( iconName, loadIcon(iconPath) );
                
            }
        }
      
    }
}
