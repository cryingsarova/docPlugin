package com.company.model;

import com.company.helpers.PsiElementDocHelper;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import java.util.HashMap;
import java.util.Map;

public class ClassPsiMethods extends ClassPsiElement{

    private Project project;
   // private final PsiElementFactory factory;

    Map<PsiMethod, PsiComment> methodCommentMap = new HashMap<>();
    Map<PsiMethod, String> methodCustomCommentMap = new HashMap<>();


    public ClassPsiMethods (Project project) {
        super(project);
    }

    public void addCustomMethod(PsiMethod method, PsiComment psiComment) {
        methodCommentMap.put(method, psiComment);
    }

    public void addMethod(PsiMethod psiMethod) {
        methodCommentMap.put(psiMethod,
                generateComment(PsiElementDocHelper.INSTANCE().generateSignatureForMethod(psiMethod),
                        psiMethod));

    }

    public void addComment(PsiClass psiClass) {
        if (!methodCommentMap.isEmpty()) {
            for (PsiMethod psiMethod: methodCommentMap.keySet()) {
                    psiClass.addBefore(methodCommentMap.get(psiMethod), psiMethod);

            }
        }
    }

    public void putCustomComment(PsiMethod psiMethod, String text) {
        methodCustomCommentMap.put(psiMethod, text);
        for (PsiMethod psiMethod1: methodCommentMap.keySet()){
            if (psiMethod.getName().equals(psiMethod1.getName())){
                methodCommentMap.replace(psiMethod1, extendCommentByUser(methodCommentMap.get(psiMethod1), text));
            }
        }
    }

    @Override
    public PsiComment generateComment(String doc, PsiElement psiMethod) {
        PsiComment psiComment = factory.createCommentFromText(doc, psiMethod);
        if (methodCustomCommentMap.containsKey(psiMethod)) {
           return extendCommentByUser(psiComment, methodCustomCommentMap.get(psiMethod));
        }
        return psiComment;
    }

    public String getCommentForFile() {
        StringBuilder stringBuilder = new StringBuilder();
        if (!methodCommentMap.isEmpty()) {
            for (PsiMethod psiMethod: methodCommentMap.keySet()) {
                stringBuilder.append(psiMethod.getName()).append(" ");
                stringBuilder.append(methodCommentMap.get(psiMethod));

            }
        }
        return stringBuilder.toString();
    }



    public PsiComment extendCommentByUser(PsiComment psiComment, String additionalComment) {
        StringBuilder text = new StringBuilder();
        text.append(psiComment.getText());
        text.insert(2,"\n\t" + additionalComment);
        return factory.createCommentFromText(text.toString(), null);
    }


}
