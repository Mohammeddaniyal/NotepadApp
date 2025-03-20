package com.thinking.machines.notepad.managers;
import com.thinking.machines.notepad.models.Config;
//import com.thinking.machines.notepad.ui.StatusPanel;
import com.thinking.machines.notepad.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
public class ThemeManager
{
private JTextArea textArea;
private Config config;
public ThemeManager(JTextArea textArea,Config config)
{
this.textArea=textArea;
this.config=config;
applySavedTheme();
}
public JPanel createThemePanel(StatusPanel statusPanel)
{
JPanel themePanel=new JPanel();
themePanel.add(new JLabel("Theme: "));
String[] themes={"Light Mode","Dark Mode","Solarized Light","Solarized Dark","Monokai","Dracula", "Gruvbox", "Nord", "Tokyo Night", "Cyberpunk"};
JComboBox<String> themeComboBox=new JComboBox<String>(themes);
themeComboBox.setSelectedItem(config.selectedTheme);
themeComboBox.addActionListener(e->{
JComboBox<?> source = (JComboBox<?>) e.getSource(); // Cast source to JComboBox
String selectedTheme = (String) source.getSelectedItem(); // Get selected theme
statusPanel.updateTheme(selectedTheme);
changeTheme(selectedTheme);
});
themePanel.add(themeComboBox);
return themePanel;
}
private void changeTheme(String theme)
{
switch(theme){
case "Light Mode":
textArea.setBackground(Color.WHITE);
textArea.setForeground(Color.BLACK);
break;

case "Dark Mode":
textArea.setBackground(Color.BLACK);
textArea.setForeground(Color.WHITE);
break;

case "Solarized Light":
textArea.setBackground(new Color(253,246,227));
textArea.setForeground(new Color(101,123,131));
break;

case "Solarized Dark":
textArea.setBackground(new Color(0,43,54));
textArea.setForeground(new Color(131,148,150));
break;

case "Monokai":
textArea.setBackground(new Color(39,40,34));
textArea.setForeground(new Color(249,38,114));//pinkish color
break;
case "Dracula":
textArea.setBackground(new Color(40, 42, 54));
textArea.setForeground(new Color(189, 147, 249)); // Purple text
textArea.setCaretColor(new Color(80, 250, 123)); // Green cursor
break;
case "Gruvbox":
textArea.setBackground(new Color(40, 40, 40));
textArea.setForeground(new Color(235, 219, 178)); // Soft yellow
break;
case "Nord":
textArea.setBackground(new Color(46, 52, 64));
textArea.setForeground(new Color(216, 222, 233)); // Light blue
break;
case "Tokyo Night":
textArea.setBackground(new Color(15, 18, 32));
textArea.setForeground(new Color(139, 233, 253)); // Cyan text
textArea.setCaretColor(new Color(255, 121, 198)); // Pink cursor
break;
case "Cyberpunk":
textArea.setBackground(Color.BLACK);
textArea.setForeground(new Color(0, 255, 0)); // Neon green
textArea.setCaretColor(new Color(255, 20, 147)); // Neon pink
break;
        

}//switch ends
config.selectedTheme=theme;
}
private void applySavedTheme()
{
changeTheme(config.selectedTheme);
}
}
