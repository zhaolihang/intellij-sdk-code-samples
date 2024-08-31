package com.example;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.ProjectViewTree;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.ProjectViewPane;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;


class ProjectTreeUtil {
    public static ProjectViewTree getProjectViewTree(Project project) {
        ProjectView projectView = ProjectView.getInstance(project);
        ProjectViewPane currentProjectViewPane = (ProjectViewPane) projectView.getCurrentProjectViewPane();
        if (currentProjectViewPane != null) {
            return (ProjectViewTree) currentProjectViewPane.getTree();
        }
        return null;
    }
}


public class CustomProjectViewPanel extends JPanel {
    private final Project currentProject;
    private ProjectViewTree projectTree;

    public CustomProjectViewPanel(Project project) {
        this.currentProject = project;
        setLayout(new BorderLayout());
        createUIComponents();
    }

    private void createUIComponents() {

        projectTree = ProjectTreeUtil.getProjectViewTree(this.currentProject);
        JBScrollPane scrollPane = new JBScrollPane(projectTree);
        add(scrollPane, BorderLayout.CENTER);

        updateProjectTree(currentProject);
    }

    private void updateProjectTree(Project project) {
        if (project != null && project.isOpen()) {
            ProjectView projectView = ProjectView.getInstance(project);
            JTree tree = projectView.getCurrentProjectViewPane().getTree();
            projectTree.setModel(tree.getModel());
        }
    }
}