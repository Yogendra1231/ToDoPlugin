package utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.editor.colors.TextAttributesKey;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TodoHighlighter {

    /**
     * Finds all TODO comments in the given editor and highlights them.
     * Returns list of TODO lines like "lineNumber: TODO text"
     */
    public static List<String> findAndHighlightTodos(Project project, Editor editor) {
        List<String> todos = new ArrayList<>();

        Document document = editor.getDocument();
        MarkupModel markupModel = editor.getMarkupModel();

        // Clear previous TODO highlights
        markupModel.removeAllHighlighters();

        int lineCount = document.getLineCount();

        for (int i = 0; i < lineCount; i++) {
            int lineStartOffset = document.getLineStartOffset(i);
            int lineEndOffset = document.getLineEndOffset(i);

            String lineText = document.getText(new TextRange(lineStartOffset, lineEndOffset));

            int todoIndex = lineText.indexOf("TODO");
            if (todoIndex >= 0) {
                // Highlight the "TODO" word in the editor
                int start = lineStartOffset + todoIndex;
                int end = start + 4; // length of "TODO"

                TextAttributes attributes = new TextAttributes();
                attributes.setForegroundColor(Color.RED);
                attributes.setEffectColor(Color.PINK);
                attributes.setEffectType(EffectType.LINE_UNDERSCORE);

                markupModel.addRangeHighlighter(
                        start,
                        end,
                        HighlighterLayer.ADDITIONAL_SYNTAX,
                        attributes,
                        HighlighterTargetArea.EXACT_RANGE
                );

                // Add to TODO list with line number (1-based)
                String todoText = lineText.substring(todoIndex).trim();
                todos.add("Line " + (i + 1) + ": " + todoText);
            }
        }

        return todos;
    }
}
