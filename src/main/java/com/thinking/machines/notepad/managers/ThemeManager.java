package com.thinking.machines.notepad.managers;
import com.thinking.machines.notepad.models.Config;
import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
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
public JMenu createThemeMenu()
{
JRadioButtonMenuItem item;
JMenu themeMenu=new JMenu("Theme");
String[] themes={"Light Mode","Dark Mode","Solarized Light","Solarized Dark","Monokai"};
ButtonGroup themesButtonGroup=new ButtonGroup();
for(String theme:themes)
{
item=new JRadioButtonMenuItem(theme);
themesButtonGroup.add(item);
themeMenu.add(item);
if(config.selectedTheme.equals(theme)) item.setSelected(true);
item.addActionListener(e->{
((JRadioButtonMenuItem)e.getSource()).setSelected(true);
changeTheme(theme);
});
}
return themeMenu;
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
}//switch ends
config.selectedTheme=theme;
}
private void applySavedTheme()
{
changeTheme(config.selectedTheme);
}
}
