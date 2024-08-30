package com.example;

import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.ArrayList;

public class CustomTreeStructureProvider implements TreeStructureProvider {
    @NotNull
    @Override
    public Collection<AbstractTreeNode<?>> modify(@NotNull AbstractTreeNode<?> parent,
                                                  @NotNull Collection<AbstractTreeNode<?>> children,
                                                  ViewSettings settings) {
        ArrayList<AbstractTreeNode<?>> modifiedChildren = new ArrayList<>(children);

        // 在这里添加或修改树节点
        // 例如,添加一个自定义节点:
        modifiedChildren.add(new CustomTreeNode(parent.getProject(), "自定义节点"));

        return modifiedChildren;
    }
}