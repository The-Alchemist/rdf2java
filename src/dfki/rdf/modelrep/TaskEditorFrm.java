package dfki.rdf.modelrep;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TaskEditorFrm extends JDialog
{
//----------------------------------------------------------------------------------------------------
Frame m_frmOwner = null;
ModelRep m_MR = null;
Task m_task = null;

JTextField m_tfName = null;
JTextField m_tfState = null;
JButton m_btnOK = null;
JButton m_btnCancel = null;

boolean m_bPressedOK = false;

//----------------------------------------------------------------------------------------------------
public TaskEditorFrm (Frame frmOwner, ModelRep MR, Task task)
{
    super(frmOwner, "Edit task \""+task.getName()+"\"", true /*modal*/);

    m_frmOwner = frmOwner;
    m_MR = MR;
    m_task = task;

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 2));
    getContentPane().add(panel);

    m_tfName = new JTextField(m_task.getName());
    m_tfState = new JTextField(m_task.m_sState);
    panel.add(new JLabel("Name"));
    panel.add(m_tfName);
    panel.add(new JLabel("State"));
    panel.add(m_tfState);

    m_btnOK = new JButton("OK");
    m_btnCancel = new JButton("Cancel");
    panel.add(m_btnOK);
    panel.add(m_btnCancel);


    m_btnOK.addActionListener(new ActionListener() {
        public void actionPerformed (ActionEvent event) {
            pressedOK();
        }
    });

    m_btnCancel.addActionListener(new ActionListener() {
        public void actionPerformed (ActionEvent event) {
            pressedCancel();
        }
    });


    pack();
    // setSize(300, 300);
}

//----------------------------------------------------------------------------------------------------
public void setVisible (boolean bVisible)
{
    if (bVisible)
        m_bPressedOK = false;
    super.setVisible(bVisible);
}

//----------------------------------------------------------------------------------------------------
protected void pressedOK ()
{
    m_bPressedOK = true;
    setVisible(false);
}

//----------------------------------------------------------------------------------------------------
protected void pressedCancel ()
{
    setVisible(false);
}

//----------------------------------------------------------------------------------------------------
public boolean closedWithOK ()
{
    return m_bPressedOK;
}

//----------------------------------------------------------------------------------------------------
public String getValue (String sAttribute)
{
    if (sAttribute.equals("name"))
        return m_tfName.getText();
    else
    if (sAttribute.equals("state"))
        return m_tfState.getText();
    else
        return null;
}

//----------------------------------------------------------------------------------------------------
}

