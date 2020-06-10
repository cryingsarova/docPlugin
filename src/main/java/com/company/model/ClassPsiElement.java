package com.company.model;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

public abstract class ClassPsiElement {

    protected PsiElementFactory factory;

    protected abstract PsiComment generateComment(String doc, PsiElement psiElement);

    public ClassPsiElement(Project project) {
        factory = JavaPsiFacade.getInstance(project).getElementFactory();
    }
}
