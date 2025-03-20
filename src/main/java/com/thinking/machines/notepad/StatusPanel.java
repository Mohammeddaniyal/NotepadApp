package com.thinking.machines.notepad;
import com.thinking.machines.notepad.exceptions.*;
import com.thinking.machines.notepad.models.Config;
import javax.swing.*;
import java.awt.*;
public class StatusPanel extends JPanel
{
private JTextArea textArea;
private JLabel encodingLabel;
private JLabel cursorLabel;
private JLabel saveStatusLabel;
private JLabel themeLabel;
private JLabel autoSaveLabel;
public StatusPanel(JTextArea textArea,Config config)
{
setLayout(new FlowLayout(FlowLayout.LEFT));
setBorder(BorderFactory.createEtchedBorder());

encodingLabel = new JLabel("Encoding: UTF-8");
cursorLabel = new JLabel("Line 1, Column 1");
saveStatusLabel = new JLabel("Status: Saved");
themeLabel = new JLabel("Theme: "+config.selectedTheme);
autoSaveLabel = new JLabel("Auto-Save: "+config.autoSaveStatus);

// Add labels to the panel
add(new JLabel("	"));
add(new JSeparator(SwingConstants.VERTICAL)); // Separator
add(encodingLabel);
add(new JSeparator(SwingConstants.VERTICAL)); // Separator
add(cursorLabel);
add(new JSeparator(SwingConstants.VERTICAL));
add(saveStatusLabel);
add(new JSeparator(SwingConstants.VERTICAL));
add(themeLabel);
add(new JSeparator(SwingConstants.VERTICAL));
add(autoSaveLabel);
}

public void setCursorPosition(int line,int column)
{
cursorLabel.setText("Line "+line+", Column "+column);
}
public void setUnsaved()
{
saveStatusLabel.setText("Status: Unsaved");
}
public void setSaved()
{
saveStatusLabel.setText("Status: Saved");
}

public void updateEncoding(String encoding)
{
encodingLabel.setText("Encoding: " + encoding);
}
public void updateTheme(String theme)
{
themeLabel.setText("Theme: " + theme);
}
public void updateAutoSaveStatus(String status)
{
autoSaveLabel.setText("Auto-Save: " +status);
}
}
