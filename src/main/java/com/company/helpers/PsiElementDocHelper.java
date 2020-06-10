package com.company.helpers;

import com.intellij.psi.*;

import java.util.List;

public class PsiElementDocHelper {

    private static String START_FOR_COMMENT = "/* \n ";
    private static String END_FOR_COMMENT = "\t*/";
    //private final PsiElementFactory elementFactory = JavaPsiFacade.getInstance(project).getElementFactory();;

    private static PsiElementDocHelper instance;

    public static PsiElementDocHelper INSTANCE() {
        if (instance == null) {
            instance = new PsiElementDocHelper();
        }
        return instance;
    }

    public String generateSignatureForMethod(PsiMethod psiMethod) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t@returns ")
                .append(psiMethod.getReturnType().getPresentableText())
                .append("\n");
        if (psiMethod.hasParameters()) {
            //stringBuilder.append("\t");
            for (PsiParameter psiParameter : psiMethod.getParameterList().getParameters()) {
                stringBuilder.append("\t@param ")
                        .append(psiParameter.getType().getPresentableText()).append(" ")
                        .append(psiParameter.getName())
                        .append("\n");
            }
        }
        return frameStringToComment(stringBuilder);
    }

    public String generateSignatureForField(PsiField psiField) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t@field ");
        stringBuilder.append(psiField.getName());
        stringBuilder.append("\n");
        return frameStringToComment(stringBuilder);

    }

    public String generateSignatureForFields(List<PsiField> fieldList) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t@fields ").append("\n");
        for (PsiField psiField: fieldList) {
            stringBuilder.append("\t");
            if (psiField.getModifierList() != null) {
                stringBuilder.append(psiField.getModifierList().getText())
                        .append(" ");
            }
            stringBuilder.append(psiField.getType().getPresentableText())
                    .append(" ")
                    .append(psiField.getName())
                    .append("\n");
        }
        return frameStringToComment(stringBuilder);
    }

    public String generateCommentForClass(String text) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/*\n ");
        stringBuilder.append(text);
        stringBuilder.append("\n*/");
        return stringBuilder.toString();
    }


    private String frameStringToComment (StringBuilder doc) {
        doc.insert(0, START_FOR_COMMENT);
        doc.insert(doc.length(), END_FOR_COMMENT);
        return doc.toString();
    }
}
