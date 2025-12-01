package com.chess.gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DebugPanel extends JPanel {

    private final JTextArea debugTextArea;
    private final JScrollPane scrollPane;
    private final List<String> debugMessages;
    private static final int MAX_DEBUG_MESSAGES = 100;

    public DebugPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 150));
        setBorder(BorderFactory.createTitledBorder("System Log"));
        this.debugMessages = new ArrayList<>();
        this.debugTextArea = new JTextArea();
        this.debugTextArea.setEditable(false);
        this.debugTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        this.debugTextArea.setBackground(Color.WHITE);
        this.debugTextArea.setForeground(Color.BLACK);
        this.scrollPane = new JScrollPane(debugTextArea);
        this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(this.scrollPane, BorderLayout.CENTER);

        final JPanel buttonPanel = new JPanel(new FlowLayout());
        final JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearDebugInfo());
        final JButton saveButton = new JButton("Save Log");
        saveButton.addActionListener(e -> saveDebugLog());
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        logSystemInfo("Panel initialized - Ready to track game events");
    }


    public void logGameEvent(String message) {
        SwingUtilities.invokeLater(() -> {
            appendMessage("[GAME EVENT] " + message);
        });
    }

    public void logSystemInfo(String info) {
        SwingUtilities.invokeLater(() -> {
            appendMessage("[SYSTEM] " + info);
        });
    }

    private void appendMessage(String rawText) {
        String timestampedMessage = String.format("[%tT] %s%n", System.currentTimeMillis(), rawText);

        debugMessages.add(timestampedMessage);
        if (debugMessages.size() > MAX_DEBUG_MESSAGES) {
            debugMessages.removeFirst();
        }

        debugTextArea.append(timestampedMessage);
        debugTextArea.setCaretPosition(debugTextArea.getDocument().getLength());
    }

    public void redo() {
        SwingUtilities.invokeLater(() -> {
            appendMessage("--- Board Refreshed ---");
        });
    }

    private void clearDebugInfo() {
        SwingUtilities.invokeLater(() -> {
            debugMessages.clear();
            debugTextArea.setText("");
            logSystemInfo("Logs cleared by user");
        });
    }

    private void saveDebugLog() {
        SwingUtilities.invokeLater(() -> {
            final JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Log File");
            fileChooser.setSelectedFile(new File("chess_game_log.txt"));
            final int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                final File fileToSave = fileChooser.getSelectedFile();
                try (final PrintWriter writer = new PrintWriter(fileToSave)) {
                    for (final String message : debugMessages) {
                        writer.print(message);
                    }
                    logSystemInfo("Log saved to: " + fileToSave.getName());
                } catch (final IOException e) {
                    logSystemInfo("Error saving log: " + e.getMessage());
                }
            }
        });
    }
}
