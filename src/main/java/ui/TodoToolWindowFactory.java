package ui;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TodoToolWindowFactory implements ToolWindowFactory {

    private static JBList<String> todoList;
    private static DefaultListModel<String> listModel;

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        listModel = new DefaultListModel<>();
        todoList = new JBList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(todoList);

        // 🖱️ Add mouse listener to navigate to line on click
        todoList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = todoList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    String selected = listModel.get(index);
                    int lineNumber = extractLineNumber(selected);
                    jumpToLine(project, lineNumber - 1); // Convert to 0-based
                }
            }
        });

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(scrollPane, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    public static void updateTodoList(List<String> todos) {
        if (listModel == null) return;
        listModel.clear();
        for (String todo : todos) {
            listModel.addElement(todo);
        }
    }

    private static int extractLineNumber(String todoText) {
        // Expected format: "Line X: TODO..."
        try {
            if (todoText.startsWith("Line ")) {
                int colonIndex = todoText.indexOf(":");
                return Integer.parseInt(todoText.substring(5, colonIndex).trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void jumpToLine(Project project, int line) {
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor == null) return;
        editor.getCaretModel().moveToLogicalPosition(new LogicalPosition(line, 0));
        editor.getScrollingModel().scrollToCaret(com.intellij.openapi.editor.ScrollType.CENTER);
    }
}
