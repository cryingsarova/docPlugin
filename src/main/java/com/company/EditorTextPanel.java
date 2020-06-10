package com.company;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;

public class EditorTextPanel extends EditorTextField {

    public EditorTextPanel(Document document, Project project, FileType fileType, boolean isViewer, boolean oneLineMode) {
        super(document, project, fileType, isViewer, oneLineMode);
    }

    @Override
    protected EditorEx createEditor() {
        EditorEx editorEx = super.createEditor();
        editorEx.setVerticalScrollbarVisible(true);
        editorEx.setHorizontalScrollbarVisible(true);
        return editorEx;
    }
}
