package com.company.helpers;

import com.intellij.ui.EditorTextField;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBTextArea;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogHelper {

    private static CardLayout cardLayout = new CardLayout();
    static JBPanel  jbPanel1= new JBPanel(cardLayout);

    public  JBPanel getMainJBPanel (EditorTextField editorTextFieldCurrent,
                                   EditorTextField editorTextFieldPreview) {


        JBRadioButton classEditButton = new JBRadioButton("Создание документации в классе", true);
        classEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        JBPanel jbPanel = new JBPanel(new FlowLayout());
        final JBPanel classPanel = getInnerJBPanelForClassEdit(editorTextFieldCurrent, editorTextFieldPreview);
        jbPanel.add( classPanel, "Class");
        //jbPanel.add( getInnerJBPanelForFileCreating(editorTextFieldCurrent), "File");


        JBPanel mainPanel = new JBPanel(new BorderLayout());
        mainPanel.add(jbPanel, BorderLayout.CENTER);
        return mainPanel;
    }

    public static JBPanel getInnerJBPanelForClassEdit (EditorTextField editorTextFieldCurrent,
                                                 EditorTextField editorTextFieldPreview) {
        GridLayout gridLayout = new GridLayout();

        gridLayout.setColumns(2);
        gridLayout.setHgap(5);
        gridLayout.setVgap(5);
        JBPanel jbPanel = new JBPanel(gridLayout);
        jbPanel.setMaximumSize(new Dimension(600,400));
        jbPanel.add(setParameters(editorTextFieldCurrent));
        jbPanel.add(setParameters(editorTextFieldPreview));

        return jbPanel;
    }

    public static JBPanel getInnerJBPanelForFileCreating (EditorTextField editorTextField, JBTextArea textArea) {
        GridLayout gridLayout = new GridLayout(1,2);
        gridLayout.setHgap(5);
        gridLayout.setVgap(5);
        JBPanel jbPanel = new JBPanel(gridLayout);
        jbPanel.setMaximumSize(new Dimension(600,400));
        //editorTextField.add(new Label("current"));
        jbPanel.add(setParameters(editorTextField));
        textArea.setPreferredSize(new Dimension(300,400));
        jbPanel.add(textArea);
        return jbPanel;
    }

    public static JBPanel getPanel(EditorTextField editorTextField) {

        //CardLayout cardLayout = new CardLayout();
        //JBPanel jbPanel1= new JBPanel(cardLayout);
        jbPanel1.add(editorTextField, "1");
        jbPanel1.add(new TextArea(), "2");
        return jbPanel1;

    }

    private static EditorTextField setParameters (EditorTextField editorTextField) {
        editorTextField.setPreferredSize(new Dimension(300,400));
        editorTextField.setCaretPosition(0);
        editorTextField.setAutoscrolls(true);
        editorTextField.setSize(100,200);
        return editorTextField;
    }

    public static CardLayout getCardLayout() {
        return cardLayout;
    }

    public static void setCardLayout(CardLayout cardLayout) {
        DialogHelper.cardLayout = cardLayout;
    }

    public static JBPanel getJbPanel1() {
        return jbPanel1;
    }

    public static void setJbPanel1(JBPanel jbPanel1) {
        DialogHelper.jbPanel1 = jbPanel1;
    }
}
