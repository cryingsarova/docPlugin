package com.company.model;

import com.company.helpers.PsiElementDocHelper;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;

public class ClassPsi extends ClassPsiElement {

    private PsiComment comment;
    private PsiClass psiClass;

    public ClassPsi(Project project) {
        super(project);
    }


    public void addClass(PsiClass psiClass) {
        this.psiClass = psiClass;
        //this.comment = generateComment()
    }

   /* public void addFields(PsiField[] fields) {
        if (fields.length > 0) {
            psiFieldList = Arrays.asList(fields);
            psiComment = generateComment(PsiElementDocHelper.INSTANCE().generateSignatureForFields(psiFieldList),
                    psiFieldList.get(0));
        }
    }*/

    public void addComment(PsiClass psiClass) {
        if (comment != null) {
            psiClass.addBefore(comment, this.psiClass);
        }

    }

    public void putCustomComment(PsiClass psiClass, String text) {
        if (psiClass.getName().equals(this.psiClass.getName())) {
            this.comment = generateComment(text, psiClass);
        }
    }

    /*public void addCommentForTextFile(PsiClass psiClass) {
        if (!psiFieldList.isEmpty()) {
            psiClass.addBefore(psiComment, psiFieldList.get(0));
        }
    }*/
    @Override
    protected PsiComment generateComment(String doc, PsiElement psiElement) {
        return factory.createCommentFromText
                (PsiElementDocHelper.INSTANCE().generateCommentForClass(doc), psiElement);
    }

    public PsiComment extendCommentByUser(PsiComment psiComment, String additionalComment) {
        StringBuilder text = new StringBuilder();
        text.append(psiComment.getText());
        text.insert(2, "\n" + additionalComment);
        return factory.createCommentFromText(text.toString(), null);
    }
}
