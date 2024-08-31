package org.zlh.BookmarkPro.UI;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class ProjectTreeModel extends DefaultTreeModel {
    public ProjectTreeModel(Project project) {
        super(new DefaultMutableTreeNode(project.getName()));
        VirtualFile projectDir = project.getBaseDir();
        if (projectDir != null) {
            addNodes((DefaultMutableTreeNode) getRoot(), projectDir);
        }
    }

    private void addNodes(DefaultMutableTreeNode parent, VirtualFile file) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
        parent.add(node);

        if (file.isDirectory()) {
            for (VirtualFile child : file.getChildren()) {
                addNodes(node, child);
            }
        }
    }
}