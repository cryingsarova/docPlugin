package com.company.editors;

import com.company.helpers.PsiElementDocHelper;
import com.company.model.ClassPsi;
import com.company.model.ClassPsiFields;
import com.company.model.ClassPsiMethods;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PsiEditor {

    private static PsiFileFactory psiFileFactory;
    private Map<PsiElement, String> updatedComments = new HashMap<>();
    private boolean isClassEdition = true;
    private PsiFile previewPsiFile_;

    public String getClassName() {
        return className;
    }

    private String className;

    public PsiEditor (Project project) {
        psiFileFactory = PsiFileFactory.getInstance(project);

    }

    /*public static PsiEditor getInstance() {
        if (instance == null) {
            instance = new PsiEditor();
        }
        return instance;
    }*/

    public PsiFile getPreviewPsiFile(AnActionEvent event) {

        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        psiFileFactory = PsiFileFactory.getInstance(project);
        PsiFile currentPsiFile = event.getData(CommonDataKeys.PSI_FILE);
        previewPsiFile_=currentPsiFile;
        if (editor == null || currentPsiFile == null) return currentPsiFile;

        PsiFile previewPsiFile = psiFileFactory
                .createFileFromText("temporaryPsiFile", currentPsiFile.getLanguage(), currentPsiFile.getText());

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = previewPsiFile.findElementAt(offset);
        PsiClass mainClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        List<PsiMethod> methodList = Arrays.asList(mainClass.getMethods());

        //ClassPsiMethods classPsiMethods = new ClassPsiMethods(project);
        ClassPsiMethods classPsiMethods = new ClassPsiMethods(project);
        for (PsiMethod psiMethod : methodList) {
            classPsiMethods.addMethod(psiMethod);
        }
        ClassPsiFields classPsiFields = new ClassPsiFields(project);
        for (PsiField psiField: Arrays.asList(mainClass.getFields())) {
            classPsiFields.addField(psiField);
        }
        ClassPsi classPsi = new ClassPsi(project);
        classPsi.addClass(mainClass);
        //classPsiFields.addFields(mainClass.getFields());

        WriteCommandAction.runWriteCommandAction(project, () -> {
            classPsiFields.addComment(mainClass);
            classPsiMethods.addComment(mainClass);
            classPsi.addComment(mainClass);
        });
        className = mainClass.getName();
        previewPsiFile_ = previewPsiFile;
        return previewPsiFile;

    }

    public void updatePsiFile(PsiElement psiElement, String text) {
        updatedComments.put(psiElement, text);

    }

    public PsiFile getUpdatedPsiFile(Project project, Editor editor, PsiFile psiFile) {

        psiFileFactory = PsiFileFactory.getInstance(project);
        if (editor == null || previewPsiFile_ == null) return previewPsiFile_;

        PsiFile updatedPreviewPsiFile = psiFileFactory
                .createFileFromText("temporaryPsiFile2", psiFile.getLanguage(), psiFile.getText());

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = updatedPreviewPsiFile.findElementAt(offset);
        PsiClass mainClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        List<PsiMethod> methodList = Arrays.asList(mainClass.getMethods());

        ClassPsiMethods classPsiMethods = new ClassPsiMethods(project);
        for (PsiMethod psiMethod : methodList) {
            classPsiMethods.addMethod(psiMethod);
        }
        ClassPsiFields classPsiFields = new ClassPsiFields(project);
        for (PsiField psiField: Arrays.asList(mainClass.getFields())) {
            classPsiFields.addField(psiField);
        }
        ClassPsi classPsi = new ClassPsi(project);
        classPsi.addClass(mainClass);

        for (PsiElement psiElement: updatedComments.keySet()) {
            if (psiElement instanceof PsiMethod) {
                classPsiMethods.putCustomComment((PsiMethod)psiElement, updatedComments.get(psiElement));
            }
            else if (psiElement instanceof PsiField) {
                classPsiFields.putCustomComment((PsiField)psiElement, updatedComments.get(psiElement));
            }
            else if (psiElement instanceof PsiClass) {
                classPsi.putCustomComment((PsiClass)psiElement, updatedComments.get(psiElement));
            }
        }

        WriteCommandAction.runWriteCommandAction(project, () -> {
            classPsiFields.addComment(mainClass);
            classPsiMethods.addComment(mainClass);
            classPsi.addComment(mainClass);
        });
        className = mainClass.getName();
        previewPsiFile_ = updatedPreviewPsiFile;
        return updatedPreviewPsiFile;
    }

    public StringBuffer getPreviewTextualFile(AnActionEvent event) {

        StringBuffer stringBuffer = new StringBuffer();
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        psiFileFactory = PsiFileFactory.getInstance(project);
        PsiFile currentPsiFile = event.getData(CommonDataKeys.PSI_FILE);
        if (editor == null || currentPsiFile == null) return null;

        PsiFile previewPsiFile = psiFileFactory
                .createFileFromText("temporaryPsiFile", currentPsiFile.getLanguage(), currentPsiFile.getText());

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = previewPsiFile.findElementAt(offset);
        PsiClass mainClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        List<PsiMethod> methodList = Arrays.asList(mainClass.getMethods());
        ClassPsiFields classPsiFields = new ClassPsiFields(project);
        ClassPsiMethods classPsiMethods = new ClassPsiMethods(project);
        for (PsiField psiField: Arrays.asList(mainClass.getFields())) {
            classPsiFields.addField(psiField);
        }

        /*for (PsiElement psiElement: updatedComments.keySet()) {
            if (psiElement instanceof PsiMethod) {
                classPsiMethods.putCustomComment((PsiMethod)psiElement, updatedComments.get(psiElement));
            }
            else if (psiElement instanceof PsiField) {
                classPsiFields.putCustomComment((PsiField)psiElement, updatedComments.get(psiElement));
            }
            else if (psiElement instanceof PsiClass) {
                classPsi.putCustomComment((PsiClass)psiElement, updatedComments.get(psiElement));
            }
        }*/

        stringBuffer.append(classPsiFields.generateComment(PsiElementDocHelper.INSTANCE().
                generateSignatureForFields(Arrays.asList(mainClass.getFields())), mainClass.getFields()[0]).getText())
                .append("\n");


        for (PsiMethod psiMethod : methodList) {
            stringBuffer.append(classPsiMethods.generateComment(PsiElementDocHelper.INSTANCE().
                    generateSignatureForMethod(psiMethod), psiMethod).getText()).append("\n");
        }

        return stringBuffer;


    }

    public void updateCurrentFile(AnActionEvent event) {

        StringBuffer stringBuffer = new StringBuffer();
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        psiFileFactory = PsiFileFactory.getInstance(project);
        PsiFile currentPsiFile = event.getData(CommonDataKeys.PSI_FILE);
        if (editor == null || currentPsiFile == null) return;
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = currentPsiFile.findElementAt(offset);
        PsiClass mainClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        List<PsiMethod> methodList = Arrays.asList(mainClass.getMethods());

        ClassPsiMethods classPsiMethods = new ClassPsiMethods(project);
        for (PsiMethod psiMethod : methodList) {
            classPsiMethods.addMethod(psiMethod);
        }
        ClassPsiFields classPsiFields = new ClassPsiFields(project);
        for (PsiField psiField: Arrays.asList(mainClass.getFields())) {
            classPsiFields.addField(psiField);
        }
        ClassPsi classPsi = new ClassPsi(project);
        classPsi.addClass(mainClass);

        for (PsiElement psiElement: updatedComments.keySet()) {
            if (psiElement instanceof PsiMethod) {
                classPsiMethods.putCustomComment((PsiMethod)psiElement, updatedComments.get(psiElement));
            }
            else if (psiElement instanceof PsiField) {
                classPsiFields.putCustomComment((PsiField)psiElement, updatedComments.get(psiElement));
            }
            else if (psiElement instanceof PsiClass) {
                classPsi.putCustomComment((PsiClass)psiElement, updatedComments.get(psiElement));
            }
        }

        WriteCommandAction.runWriteCommandAction(project, () -> {
            classPsiFields.addComment(mainClass);
            classPsiMethods.addComment(mainClass);
            classPsi.addComment(mainClass);
        });

    }



    public List<PsiMethod> getMethodsList(PsiFile psiFile, Editor editor) {
        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass mainClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        return Arrays.asList(mainClass.getMethods());
    }

    public List<PsiField> getFieldsList(PsiFile psiFile, Editor editor){
        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        PsiClass mainClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        return Arrays.asList(mainClass.getFields());
    }

    public PsiClass getClass(PsiFile psiFile, Editor editor){
        PsiElement element = psiFile.findElementAt(editor.getCaretModel().getOffset());
        return PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }

    public void setClassEdition(boolean classEdition) {
        isClassEdition = classEdition;
    }

    public boolean isClassEdition() {
        return isClassEdition;
    }
}
