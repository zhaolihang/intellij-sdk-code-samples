package org.zlh.BookmarkPro.UI;

import com.intellij.icons.AllIcons;
import com.intellij.ide.dnd.DnDDragStartBean;
import com.intellij.ide.dnd.DnDEvent;
import com.intellij.ide.dnd.DnDSupport;
import com.intellij.ide.dnd.FileCopyPasteUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.tree.TreeUtil;
import indi.bookmarkx.MyPersistent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.diagnostic.Logger;

public class ProjectTreePanel extends JPanel {
    private final Tree tree;
    private final Project project;
    private final ProjectTreeModel treeModel;

    private static final Logger LOG = Logger.getInstance(ProjectTreePanel.class);


    public ProjectTreePanel(Project project) {
        this.project = project;
        setLayout(new BorderLayout());

        treeModel = new ProjectTreeModel(project);
        tree = new Tree(treeModel);
        tree.setCellRenderer(new ProjectTreeCellRenderer());
        new TreeSpeedSearch(tree);

        TreeUtil.expandAll(tree);

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane, BorderLayout.CENTER);

        initializeDragAndDrop();
    }

    private void initializeDragAndDrop() {
        DnDSupport.createBuilder(tree)
                .setDropHandler(this::handleDrop)
                .setTargetChecker(this::checkDropTarget)
                .enableAsNativeTarget()
                .install();
    }


    private boolean checkDropTarget(@NotNull DnDEvent event) {
        Object attachedObject = event.getAttachedObject();
        if (attachedObject != null) {
            LOG.info("checkDropTarget " + attachedObject.toString());
        } else {
            LOG.info("checkDropTarget null");
        }

        event.setDropPossible(true);
        return true;


//        Object attachedObject = event.getAttachedObject();
//        if (attachedObject instanceof List) {
//            List<?> draggedItems = (List<?>) attachedObject;
//            for (Object item : draggedItems) {
//                if (item instanceof VirtualFile) {
//                    VirtualFile file = (VirtualFile) item;
//                    LOG.info("Dragged item type: " + file.getFileType().getName());
//                    LOG.info("Dragged item name: " + file.getName());
//
//                    // 获取当前指针指向的树节点
//                    TreePath path = tree.getPathForLocation(event.getPoint().x, event.getPoint().y);
//                    if (path != null) {
//                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
//                        Object userObject = node.getUserObject();
//                        if (userObject instanceof VirtualFile) {
//                            VirtualFile targetFile = (VirtualFile) userObject;
//                            LOG.info("Target item: " + targetFile.getName());
//                        }
//                    }
//
//                    event.setDropPossible(true);
//                    return true;
//                }
//            }
//        }
//        event.setDropPossible(false);
//        return false;
    }

    private void handleDrop(@NotNull DnDEvent event) {
        LOG.info("handleDrop eventType=" + event.getClass());
        LOG.info("handleDrop event=" + event.toString());
        Object attachedObject = event.getAttachedObject();
        if (attachedObject != null) {
            LOG.info("handleDrop " + attachedObject.toString());
        } else {
            LOG.info("handleDrop null");
        }
        if (event != null) {
            DataFlavor[] flavors = event.getTransferDataFlavors();
            for (DataFlavor flavor : flavors) {
                try {
                    // 使用 getTransferData 获取文件列表
                    Object obj = event.getTransferData(flavor);
                    if (obj != null) {
                        LOG.info("flavor type" + flavor.toString());
                        LOG.info("flavor objStr=" + obj.toString());
                        LOG.info("flavor objType=" + obj.getClass());
                    } else {
                        LOG.info("flavor null");
                    }
                } catch (UnsupportedFlavorException | IOException e) {
                    LOG.error("Error processing dropped files", e);
                }
            }

            try {
                // 使用 getTransferData 获取文件列表
//                bookmark/ui/DragAndDropHandler.kt
//                FileCopyPasteUtil.getFileListFromAttachedObject() 可以获得拖拽的东西
                List<?> file =(List<?>) event.getTransferData(DataFlavor.javaFileListFlavor) ;
                if (file != null) {
                    LOG.info("flavor fileStr=" + file.toString());
                    LOG.info("flavor fileType=" + file.getClass());
                    LOG.info("flavor fileType=" + file.get(0).getClass());
                } else {
                    LOG.info("flavor file null");
                }
            } catch (UnsupportedFlavorException | IOException e) {
                LOG.error("Error processing dropped files", e);
            }
        }

        if (attachedObject instanceof List) {
            List<?> draggedItems = (List<?>) attachedObject;
            for (Object item : draggedItems) {
                if (item instanceof VirtualFile) {
                    VirtualFile file = (VirtualFile) item;

                    // 获取拖放目标位置
                    TreePath targetPath = tree.getPathForLocation(event.getPoint().x, event.getPoint().y);
                    DefaultMutableTreeNode targetNode = (targetPath != null)
                            ? (DefaultMutableTreeNode) targetPath.getLastPathComponent()
                            : (DefaultMutableTreeNode) tree.getModel().getRoot();

                    handleDroppedFile(file, targetNode);
                }
            }
        }
    }

    private void handleDroppedFile(@NotNull VirtualFile file, DefaultMutableTreeNode targetNode) {
        ApplicationManager.getApplication().invokeLater(() -> {
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file);

            if (file.isDirectory()) {
                LOG.info("Dropped directory: " + file.getPath());
                addChildrenRecursively(newNode, file);
            } else {
                LOG.info("Dropped file: " + file.getPath());
            }
            treeModel.insertNodeInto(newNode, targetNode, targetNode.getChildCount());
//            TreeUtil.expandPath(tree, TreeUtil.getPathFromRoot(newNode));

            // 在这里可以添加更多的自定义逻辑
        });
    }


    //    private boolean checkDropTarget(@NotNull DnDEvent event) {
//        event.setDropPossible(true);
//        return true;
//
////        Object attachedObject = event.getAttachedObject();
////        if (attachedObject instanceof DnDDragStartBean) {
////            DnDDragStartBean bean = (DnDDragStartBean) attachedObject;
////            Object transferData = bean.getAttachedObject();
////            if (transferData instanceof List) {
////                List<?> draggedItems = (List<?>) transferData;
////                for (Object item : draggedItems) {
////                    if (item instanceof VirtualFile) {
////                        event.setDropPossible(true);
////                        return true;
////                    }
////                }
////            }
////        }
////        event.setDropPossible(false);
////        return false;
//    }
//
//    private void handleDrop(@NotNull DnDEvent event) {
//        Object attachedObject = event.getAttachedObject();
//        if (attachedObject instanceof DnDDragStartBean) {
//            DnDDragStartBean bean = (DnDDragStartBean) attachedObject;
//            Object transferData = bean.getAttachedObject();
//            if (transferData instanceof List) {
//                List<?> draggedItems = (List<?>) transferData;
//                for (Object item : draggedItems) {
//                    if (item instanceof VirtualFile) {
//                        VirtualFile file = (VirtualFile) item;
//                        handleDroppedFile(file);
//                    }
//                }
//            }
//        }
//    }
//
//    private void handleDroppedFile(@NotNull VirtualFile file) {
//        ApplicationManager.getApplication().invokeLater(() -> {
//            DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
//            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(file);
//
//            if (file.isDirectory()) {
//                LOG.info("Dropped directory: " + file.getPath());
//                addChildrenRecursively(newNode, file);
//            } else {
//                LOG.info("Dropped file: " + file.getPath());
//            }
//
//            treeModel.insertNodeInto(newNode, root, root.getChildCount());
////            TreeUtil.expandPath(tree, TreeUtil.getPathFromRoot(newNode));
//
//            // 在这里可以添加更多的自定义逻辑
//        });
//    }
//
    private void addChildrenRecursively(DefaultMutableTreeNode parent, VirtualFile directory) {
        VirtualFile[] children = directory.getChildren();
        for (VirtualFile child : children) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
            treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
            if (child.isDirectory()) {
                addChildrenRecursively(childNode, child);
            }
        }
    }

    private static class ProjectTreeCellRenderer extends ColoredTreeCellRenderer {
        @Override
        public void customizeCellRenderer(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (value instanceof DefaultMutableTreeNode) {
                Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
                if (userObject instanceof VirtualFile) {
                    VirtualFile file = (VirtualFile) userObject;
                    setIcon(file.isDirectory() ? AllIcons.Nodes.Folder : file.getFileType().getIcon());
                    append(file.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                } else {
                    append(String.valueOf(userObject), SimpleTextAttributes.REGULAR_ATTRIBUTES);
                }
            }
        }
    }
}