package AppComponents;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.intellij.sdk.project.model.ProjectFileIndexSampleAction;
import org.jetbrains.annotations.NotNull;

public class InitActionsOnStart implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup toolsMenu = (DefaultActionGroup) actionManager.getAction("ToolsMenu");
        if (toolsMenu != null) {
//            ProjectFileIndexSampleAction action = new ProjectFileIndexSampleAction();
//            actionManager.registerAction("ProjectModel.SourceRoots", action);
//            toolsMenu.add(action, Constraints.FIRST);
        }
    }
}