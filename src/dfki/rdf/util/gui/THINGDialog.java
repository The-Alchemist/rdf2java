/*
 * Created on 20.09.2004
 */
package dfki.rdf.util.gui;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;

import dfki.rdf.util.KnowledgeBase;
import dfki.rdf.util.THING;


/**
 * <p>Title: THINGDialog</p>
 * <p>Description: the Dialog object returned by THINGDialogFactory<b>
 *  *  please note that an instance of this object can only used once if you want to
 *  rely on the modified THING, Class or if the dialog has been cancelled (== dialogObject is <code>null</code>) </b></p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: DFKI </p>
 * @author Heiko Maus
 * @version 1.0
 */
public class THINGDialog extends JDialog

{
//------------------------------------------------------------------------------
private THING m_thingObject;
private HashSet m_modifiedTHINGs;
private HashSet m_modifiedClasses;
private THINGDialogFactory.THINGDialogHelper m_thingDHelper;
private KnowledgeBase m_knowledgeBase; 

private int m_result = JOptionPane.CANCEL_OPTION;

//------------------------------------------------------------------------------
/**
 * @param p_thing
 * @param p_knowledgeBase
 * @param p_owner
 * @param p_thingDHelper
 */
public THINGDialog (THING p_thing, KnowledgeBase p_knowledgeBase, Frame p_owner, THINGDialogFactory.THINGDialogHelper p_thingDHelper)
{
    this(p_thing, p_knowledgeBase, p_owner);
    m_thingDHelper = p_thingDHelper;
    
}


/**
 * @param p_thing
 * @param p_knowledgeBase
 * @param p_owner
 * @param p_thingDHelper
 */
public THINGDialog (THING p_thing, KnowledgeBase p_knowledgeBase, Dialog p_owner, THINGDialogFactory.THINGDialogHelper p_thingDHelper)
{
    this( p_thing, p_knowledgeBase, p_owner);
    m_thingDHelper = p_thingDHelper;
}


/**
 * @param p_thing
 * @param p_knowledgeBase
 * @param p_owner
 */
public THINGDialog (THING p_thing, KnowledgeBase p_knowledgeBase, Frame p_owner)
{
    super(  p_owner, true );
    initialize(p_knowledgeBase);
}


/**
 * @param p_thing
 * @param p_knowledgeBase
 * @param p_owner
 */
public THINGDialog (THING p_thing, KnowledgeBase p_knowledgeBase, Dialog p_owner)
{
    super(p_owner, true );
    initialize(p_knowledgeBase);
}


public KnowledgeBase getKnowledgeBase()
{
    return m_knowledgeBase;
}


private void initialize(KnowledgeBase p_knowledgeBase )
{
    m_knowledgeBase = p_knowledgeBase; 
    m_thingObject = null;
    m_modifiedTHINGs = new HashSet();
    m_modifiedClasses = new HashSet();
    getContentPane().setLayout(new BorderLayout());
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
}

public void setLocation()
{
    int x = 0;
    int y = 0;
    if(getParent()!=null)
    {
        x = getParent().getX()+(getParent().getWidth()-getWidth())/2;
        y = getParent().getY()+(getParent().getHeight()-getHeight())/2;
    }
    else
    {
        x = (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth())/2;
        y = (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight())/2;
    }
    this.setLocation(x,y);
}
/**
 * sets the dialogs object, also the p_thing is added to the modified objects
 * @param p_thing
 */
public void setDialogObject(THING p_thing)
{
    m_thingObject = p_thing;
    addModifiedTHING(p_thing);
}

/**
 * if the dialog has been cancelled, this will return <code>null</code>
 * @return the THING of this dialog or <code>null</code> if it was cancelled by the user
 */
public THING getDialogObject()
{
    return m_thingObject;
}

/**
 * this includes the dialog object (if the dialog was _not_ cancelled) and any modified objects
 * @return <code>Set</code> of <code>THING</code> which have been modified in this dialog instance
 */
public Set /*of THING*/getModifiedTHINGs()
{
    return m_modifiedTHINGs;
}

/**
 * this includes the <code>Class</code> of the dialog object (if the dialog was _not_ cancelled) and any modified objects
 * @return <code>Set</code> of <code>Class</code> of <code>THING</code> which have
 * been modified in this dialog instance
 */
public Set /*of Class*/getModifiedClasses()
{
    return m_modifiedClasses;
}

public void addModifiedTHING(THING thing)
{
   if (thing == null) return;

   m_modifiedTHINGs.add(thing);
   addModifiedClass(thing.getClass());

}

public void addModifiedTHING(Collection /*of THING*/cThing)
{
   Iterator it = cThing.iterator();
   while (it.hasNext()) {
       addModifiedTHING((THING)it.next());
   }
}

private void addModifiedClass(Class modClass)
{
   m_modifiedClasses.add(modClass);
}


/**
 * tells if the dialog modified anything
 * @return true if the <code>Set</code> of modifiedTHINGs is not empty or the dialog object is not null<p>
 * false otherwise
 */
public boolean hasModifiedTHINGs()
{
 return !m_modifiedTHINGs.isEmpty();
}

/**
 * browses to the given url
 * @param p_url
 */
public void browse2Url(String p_url)
{
    if (m_thingDHelper != null) m_thingDHelper.setURL(p_url);
    
}

/**
 * delivers the THINGDialogHelper of this THINGDialog instance
 * @return
 */
public THINGDialogFactory.THINGDialogHelper getTHINGDialogHelper()
{
    return m_thingDHelper;
}

public void setResult(int result)
{
    m_result = result;
}

public int getResult()
{
    return m_result;
}




/**
 * @param p_thing
 */
public void assign( THING p_thing )
{
 m_knowledgeBase.assign(p_thing);   
    
}

} // end of class

