package com.company.model;

import com.company.helpers.PsiElementDocHelper;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import java.util.*;

public class ClassPsiFields extends ClassPsiElement{

    Map<PsiField, PsiComment> fieldCommentMap = new HashMap<>();

   // private List<PsiField> psiFieldList = new ArrayList<>();
    private PsiComment psiComment;


    public ClassPsiFields(Project project) {
        super(project);
    }

    public void addField(PsiField psiField) {
        fieldCommentMap.put(psiField, generateComment(PsiElementDocHelper.INSTANCE()
                .generateSignatureForField(psiField),psiField));
    }

   /* public void addFields(PsiField[] fields) {
        if (fields.length > 0) {
            psiFieldList = Arrays.asList(fields);
            psiComment = generateComment(PsiElementDocHelper.INSTANCE().generateSignatureForFields(psiFieldList),
                    psiFieldList.get(0));
        }
    }*/

    public void addComment(PsiClass psiClass) {
        if (!fieldCommentMap.isEmpty()) {
            for (PsiField psiField: fieldCommentMap.keySet()) {
                    psiClass.addBefore(fieldCommentMap.get(psiField), psiField);
            }
        }

    }

    public void putCustomComment(PsiField psiField, String text) {
        //methodCustomCommentMap.put(psiMethod, text);
        for (PsiField psiField1: fieldCommentMap.keySet()){
            if (psiField.getName().equals(psiField1.getName())){
                fieldCommentMap.replace(psiField1, extendCommentByUser(fieldCommentMap.get(psiField1), text));
            }
        }
    }

    /*public void addCommentForTextFile(PsiClass psiClass) {
        if (!psiFieldList.isEmpty()) {
            psiClass.addBefore(psiComment, psiFieldList.get(0));
        }
    }*/

    @Override
    public PsiComment generateComment(String doc, PsiElement psiField) {
        return factory.createCommentFromText(doc, psiField);
    }

    public PsiComment extendCommentByUser(PsiComment psiComment, String additionalComment) {
        StringBuilder text = new StringBuilder();
        text.append(psiComment.getText());
        text.insert(2,"\n\t" + additionalComment);
        return factory.createCommentFromText(text.toString(), null);
    }
}
