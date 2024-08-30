package com.example;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

public class CustomTreeNode extends ProjectViewNode<String> {
    private final String nodeName;

    public CustomTreeNode(Project project, String name) {
        super(project, name, null);
        this.nodeName = name;
    }

    @Override
    public boolean contains(@NotNull VirtualFile file) {
        return false;
    }

    @NotNull
    @Override
    public Collection<? extends AbstractTreeNode<?>> getChildren() {
        return Collections.emptyList();
    }

    @Override
    protected void update(PresentationData presentation) {
        presentation.setPresentableText(nodeName);
        // 可以在这里设置图标: presentation.setIcon(...)
    }
}