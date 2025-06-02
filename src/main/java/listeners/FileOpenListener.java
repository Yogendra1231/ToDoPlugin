package listeners;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import ui.TodoToolWindowFactory;
import utils.TodoHighlighter;

import java.util.List;

public class FileOpenListener implements FileEditorManagerListener {
    @Override
    public void selectionChanged(FileEditorManagerEvent event) {
        Project project = event.getManager().getProject();
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) return;

        String fileName = event.getNewFile().getName();
        if (!fileName.endsWith(".kt")) return;

        List<String> todos = TodoHighlighter.findAndHighlightTodos(project, editor);
        TodoToolWindowFactory.updateTodoList(todos);
    }
}
