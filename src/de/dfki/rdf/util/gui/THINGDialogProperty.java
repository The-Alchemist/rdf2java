/*
 * Created on 20.09.2004
 */
package de.dfki.rdf.util.gui;

import de.dfki.util.config.Config;


/**
 * @author maus
 */

public class THINGDialogProperty
{
    public static class Property   extends Config.Property
    {
    //----------------------------------------------------------------------------------------------------------------------------

    public final static Property URI_2_ICONS_PATH_MAP     = new Property( new String[]{ "THINGDialog", "icons"},                "maps URIs to image paths(filenames)" );

    //----------------------------------------------------------------------------------------------------------------------------
    public Property (String[] asPath, String sDesc)
    {
        super( pathToString( asPath ), sDesc );
    }

    public Property (String[] asPath, String sDesc, boolean bMultiVal)
    {
        super( pathToString( asPath ), sDesc, bMultiVal );
    }

    } // end of class Property

}