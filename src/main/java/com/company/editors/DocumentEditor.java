package com.company.editors;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

public class DocumentEditor {

    private static EditorFactory documentFactory;
    private static DocumentEditor instance;

    private static PsiEditor psiEditor;

    public DocumentEditor (PsiEditor psiEditor) {

            this.psiEditor = psiEditor;

    }

    public Document getPreviewDocument (AnActionEvent event) {

        PsiFile previewPsiFile = psiEditor.getPreviewPsiFile(event);
        documentFactory = EditorFactory.getInstance();
        return documentFactory.createDocument(previewPsiFile.getText());
    }

    public Document getUpdatedDocument (Project project, Editor editor, PsiFile psiFile) {
        PsiFile psiFile_ = psiEditor.getUpdatedPsiFile(project, editor, psiFile);
        String string = psiFile_.getText();
        return documentFactory.createDocument(psiFile_.getText());
    }

    public StringBuffer getTextualFile (AnActionEvent event) {
        return psiEditor.getPreviewTextualFile(event);
    }
}
