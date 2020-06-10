package com.company;

import com.company.editors.FileEditor;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

public class DocGenerationAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();

        //PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        //FileType fileType = psiFile.getFileType();

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Document document = editor.getDocument();


        DocGenerationDialogWindow docGenerationDialogWindow =
                new DocGenerationDialogWindow(e);
        docGenerationDialogWindow.createCenterPanel();
//docGenerationDialogWindow.
        if (docGenerationDialogWindow.showAndGet()) {
            if (docGenerationDialogWindow.getPsiEditor().isClassEdition()) {
                docGenerationDialogWindow.getPsiEditor().updateCurrentFile(e);
            } else {
                FileEditor.createAndUpdateFile(docGenerationDialogWindow.getDirectory(),
                        docGenerationDialogWindow.getPsiEditor().getClassName(),
                        docGenerationDialogWindow.getTextBuffer().toString());
            }

        }

        //Messages.showMessageDialog(e.getProject(), stringBuilder.toString(), "PSI Info", null);*/
    }

    @Override
    public void update(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        e.getPresentation().setEnabled(editor != null && psiFile != null);
    }


}
