package com.company;

import com.company.editors.DocumentEditor;
import com.company.editors.PsiEditor;
import com.company.helpers.DialogHelper;
import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

import com.intellij.psi.*;
import com.intellij.ui.components.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocGenerationDialogWindow extends DialogWrapper {

    private Document currentDocument;
    private Document previewDocument;
    private PsiFile psiFile;
    private Editor editor;

    public String getDirectory() {
        return directory;
    }

    private String directory;
    private PsiEditor psiEditor;
    private DocumentEditor documentEditor;
    private StringBuffer textBuffer;
    private Project project;


    public DocGenerationDialogWindow(AnActionEvent event) {
        super(event.getProject());
        setProperties(event);
        init();
    }

    private void setProperties(AnActionEvent event) {
        setTitle("Documentation");
        setProject(event.getProject());
        setPsiFile(event.getData(CommonDataKeys.PSI_FILE));
        setEditor(event.getData(CommonDataKeys.EDITOR));
        setCurrentDocument(editor.getDocument());
        setPsiEditor(new PsiEditor(project));
        documentEditor = new DocumentEditor(psiEditor);
        setPreviewDocument(documentEditor.getPreviewDocument(event));
        setTextBuffer(documentEditor.getTextualFile(event));
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        EditorTextPanel leftField = new EditorTextPanel(currentDocument, project,
                JavaClassFileType.INSTANCE, true, false);
        EditorTextPanel rightField = new EditorTextPanel(previewDocument, project,
                JavaClassFileType.INSTANCE, true, false);
        JBTextArea textArea = new JBTextArea(textBuffer.toString());
        /*textArea.setPreferredSize(new Dimension(300,400));
        leftField.setPreferredSize(new Dimension(300,400));
        leftField.setPreferredSize(new Dimension(300,400));*/
        GridLayout gridLayout = new GridLayout(1, 1);
        JBPanel mainPanel = new JBPanel(gridLayout);
        //mainPanel.setBorder(BorderFactory.createLineBorder(Color.magenta));
        mainPanel.setSize(600, 500);
        mainPanel.add(DialogHelper.getInnerJBPanelForClassEdit(leftField, rightField), 0);
        //mainPanel.add(DialogHelper.getInnerJBPanelForFileCreating(leftField),1);
        //cardLayout.

        JBPanel rightPanel = new JBPanel();
        BoxLayout boxLayout = new BoxLayout(rightPanel, BoxLayout.Y_AXIS);
        rightPanel.setLayout(boxLayout);

        JBTextField jbTextField = new JBTextField();
        jbTextField.setTextToTriggerEmptyTextStatus("Directory...");
        jbTextField.setFocusable(false);
        jbTextField.setColumns(50);

        //rightPanel.add(jbTextField);
        // mainPanel.add(fileChoosePanel,2);

        JBRadioButton classEditButton = new JBRadioButton("Class edition", true);
        classEditButton.setFont(classEditButton.getFont().deriveFont(14.0f));
        classEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                psiEditor.setClassEdition(true);
                mainPanel.add(DialogHelper.getInnerJBPanelForClassEdit(leftField, rightField), 0);
                mainPanel.remove(1);
                jbTextField.setText("");
                jbTextField.setFocusable(false);
                //mainPanel.revalidate();
                //mainPanel.repaint();
                //leftPanel.revalidate();
                //leftPanel.repaint();
            }
        });

        JBRadioButton fileCreationButton = new JBRadioButton("New file creation");
        fileCreationButton.setFont(fileCreationButton.getFont().deriveFont(14.0f));
        fileCreationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                psiEditor.setClassEdition(false);
                mainPanel.add(DialogHelper.getInnerJBPanelForFileCreating(leftField, textArea), 1);
                mainPanel.remove(0);
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int ret = jFileChooser.showDialog(null, "Choose directory");
                if (ret == jFileChooser.APPROVE_OPTION) {
                    File file = jFileChooser.getSelectedFile();
                    jbTextField.setFocusable(true);
                    jbTextField.setText(file.getAbsolutePath());
                    directory = jbTextField.getText();
                    //jbTextField.updateUI();
                }
            }
        });
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(classEditButton);
        buttonGroup.add(fileCreationButton);

        JLabel label = new JLabel("Format of editing:");
        label.setFont(classEditButton.getFont());

        JButton methodCommentAddingButton = new JButton("Add comments for methods");
        JButton fieldsCommentAddingButton = new JButton("Add comments for fields");
        JButton classCommentAddingButton = new JButton("Add comment for class");

        ActionListener psiMethodListener = e -> {
            addingUserCommentForMethodAction();
            rightField.setDocument(previewDocument);
            rightField.repaint();
        };

        ActionListener psiFieldListener = e -> {
            addingUserCommentForFieldAction();
            rightField.setDocument(previewDocument);
            rightField.repaint();
        };

        ActionListener psiClassListener = e -> {
            addingUserCommentForClassAction();
            rightField.setDocument(previewDocument);
            rightField.setCaretPosition(0);
            rightField.repaint();
        };

        fieldsCommentAddingButton.addActionListener(psiFieldListener);
        methodCommentAddingButton.addActionListener(psiMethodListener);
        classCommentAddingButton.addActionListener(psiClassListener);


        JBPanel buttonPanel = new JBPanel(new GridLayout(6, 1, 25, 0));
        buttonPanel.add(label);
        buttonPanel.add(classEditButton);
        buttonPanel.add(fileCreationButton);
        buttonPanel.add(methodCommentAddingButton);
        buttonPanel.add(fieldsCommentAddingButton);
        buttonPanel.add(classCommentAddingButton);
        //jPanel.add(buttonPanel, gridBagConstraints);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        //leftPanel.setSize(800,600);
        /*leftPanel.setMinimumSize(new Dimension(300,500));
        leftPanel.setMaximumSize(new Dimension(300,500));*/

        leftPanel.add(buttonPanel);

        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        //centralPanel.setBorder(BorderFactory.createLineBorder(Color.green));
        centralPanel.add(mainPanel);
        JLabel chosenDirectoryLabel = new JLabel();
        chosenDirectoryLabel.setFont(classEditButton.getFont());
        chosenDirectoryLabel.setText("Chosen directory:");
        centralPanel.add(chosenDirectoryLabel, BorderLayout.WEST);

        JBPanel fileChosePanel = new JBPanel(new FlowLayout(FlowLayout.LEFT));
        fileChosePanel.add(jbTextField);
        JButton fileButton = new JButton("New...");
        fileButton.setSize(5, 5);
        fileButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int ret = jFileChooser.showDialog(null, "Choose directory");
            if (ret == jFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();

                jbTextField.setFocusable(true);
                jbTextField.setText(file.getName() + " " + file.toString());
                directory = jbTextField.getText();
            }
        });

        fileChosePanel.add(fileButton);


        centralPanel.add(fileChosePanel);


        JPanel upPanel = new JPanel();
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.X_AXIS));
        ///upPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        upPanel.add(leftPanel);
        upPanel.add(centralPanel);


        return upPanel;
    }

    private void addingUserCommentForMethodAction() {
        List<JComponent> jComponents = new ArrayList<>();
        Map<PsiMethod, JTextField> commentMapping = new HashMap<>();
        for (PsiMethod psiMethod : psiEditor.getMethodsList(psiFile, editor)) {
            JTextField textField = new JTextField();
            commentMapping.put(psiMethod, textField);
            jComponents.add(new JLabel("Method: " + psiMethod.getName()));
            jComponents.add(textField);
        }
        int result = JOptionPane.showConfirmDialog(null, jComponents.toArray(),
                "Add comments", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            for (PsiMethod psiMethod: commentMapping.keySet()) {
                psiEditor.updatePsiFile(psiMethod, commentMapping.get(psiMethod).getText());
            }
            setPreviewDocument(documentEditor.getUpdatedDocument(project, editor, psiFile));
        }
    }

    private void addingUserCommentForFieldAction() {
        List<JComponent> jComponents = new ArrayList<>();
        Map<PsiField, JTextField> commentMapping = new HashMap<>();
        for (PsiField psiField : psiEditor.getFieldsList(psiFile, editor)) {
            JTextField textField = new JTextField();
            commentMapping.put(psiField, textField);
            String text = "";
            text.concat(psiField.getType().getPresentableText());
            jComponents.add(new JLabel("Field: " + psiField.getName()));
            jComponents.add(textField);
        }
        int result = JOptionPane.showConfirmDialog(null, jComponents.toArray(),
                "Add comments", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            for (PsiField psiField: commentMapping.keySet()) {
                psiEditor.updatePsiFile(psiField, commentMapping.get(psiField).getText());
            }
            setPreviewDocument(documentEditor.getUpdatedDocument(project, editor, psiFile));
        }
    }

    private void addingUserCommentForClassAction() {
        PsiClass psiClass = psiEditor.getClass(psiFile, editor);
        JLabel label = new JLabel("Class: " + psiClass.getName());
        JTextField jTextField = new JTextField();
        int result = JOptionPane.showConfirmDialog(null,
                new JComponent[]{label, jTextField}, "Add comment", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            psiEditor.updatePsiFile(psiClass, jTextField.getText());
            setPreviewDocument(documentEditor.getUpdatedDocument(project, editor, psiFile));
        }
    }


    public StringBuffer getTextBuffer() {
        return textBuffer;
    }

    public void setTextBuffer(StringBuffer textBuffer) {
        this.textBuffer = textBuffer;
    }

    public Document getCurrentDocument() {
        return currentDocument;
    }

    public void setCurrentDocument(Document currentDocument) {
        this.currentDocument = currentDocument;
    }

    public Document getPreviewDocument() {
        return previewDocument;
    }

    public void setPreviewDocument(Document previewDocument) {
        this.previewDocument = previewDocument;
    }

    public PsiEditor getPsiEditor() {
        return psiEditor;
    }

    public void setPsiEditor(PsiEditor psiEditor) {
        this.psiEditor = psiEditor;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public void setPsiFile(PsiFile psiFile) {
        this.psiFile = psiFile;
    }
}
