package com.thinking.machines.notepad.managers;
import com.thinking.machines.notepad.exceptions.*;
import com.thinking.machines.notepad.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import javax.swing.*;
public class HelpMenuManager
{
private Notepad notepad;
private JMenuBar menuBar;
private JFrame fakeParent; 
private JMenu helpMenu;
private JMenuItem aboutMenuItem,shortcutMenuItem,websiteMenuItem;
private JMenuItem updateMenuItem;
public HelpMenuManager(Notepad notepad,JMenuBar menuBar,JFrame fakeParent)
{
this.notepad=notepad;
this.menuBar=menuBar;
this.fakeParent=fakeParent;
initComponents();
addMenuItems();
addEventListeners();
menuBar.add(helpMenu);
}
private void initComponents()
{
helpMenu=new JMenu("Help");
aboutMenuItem = new JMenuItem("About Notepad");
shortcutMenuItem=new JMenuItem("Keyboard Shortcuts");
websiteMenuItem=new JMenuItem("Visit Website");
updateMenuItem=new JMenuItem("Check for Updates");        
}
private void addMenuItems()
{
helpMenu.add(aboutMenuItem);
helpMenu.add(shortcutMenuItem);
helpMenu.add(websiteMenuItem);
helpMenu.add(updateMenuItem);
}
private void addEventListeners()
{
aboutMenuItem.addActionListener(e -> showAboutDialog());
shortcutMenuItem.addActionListener(e -> showShortcutsDialog());
websiteMenuItem.addActionListener(e -> openWebsite("https://github.com/Mohammeddaniyal"));
updateMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(notepad, "You're using the latest version!", "Update Check", JOptionPane.INFORMATION_MESSAGE));
}

private void showAboutDialog() 
{
JOptionPane.showMessageDialog(notepad,
"<html><b>DaniPad</b><br>Version 1.0<br>Developed by Mohammed Daniyal Ali</html>",
"About DaniPad",
JOptionPane.INFORMATION_MESSAGE);
}

    // Show Keyboard Shortcuts
private void showShortcutsDialog() 
{
JOptionPane.showMessageDialog(notepad,
"<html><b>Keyboard Shortcuts:</b><br>"
+ "Ctrl + N → New File<br>"
+ "Ctrl + O → Open File<br>"
+ "Ctrl + S → Save File<br>"
+ "Ctrl + Shift + S → Save As<br>"
+ "Ctrl + Z → Undo<br>"
+ "Ctrl + Y → Redo<br>"
+ "Ctrl + F → Find Text<br>"
+ "Ctrl + R → Replace Text<br>"
+ "Ctrl + Q → Quit</html>",
"Keyboard Shortcuts",
JOptionPane.INFORMATION_MESSAGE);
}

    // Open website in browser
private void openWebsite(String url) {
try 
{
Desktop.getDesktop().browse(new URI(url));
} catch (Exception ex) 
{
JOptionPane.showMessageDialog(notepad, "Unable to open website!", "Error", JOptionPane.ERROR_MESSAGE);
LogException.log(ex);
}
}


}
