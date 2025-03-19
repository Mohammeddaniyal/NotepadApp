 package com.thinking.machines.notepad.managers;
import com.thinking.machines.notepad.Notepad;
import com.thinking.machines.notepad.io.*;
import com.thinking.machines.notepad.models.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class ViewMenuManager
{
private JTextArea textArea;
private Notepad notepad;
private JFrame fakeParent;
private JMenuBar menuBar; 
private Config config;
private FileHandler fileHandler;
private Notepad.Counter counter;
private int fontSize;
private final int maxFontSize=60;
private final int minFontSize=8;
private JMenu viewMenu;
private JMenu zoomMenu;
private JMenuItem zoomInMenuItem,zoomOutMenuItem;
private JCheckBoxMenuItem statusMenuItem;

public ViewMenuManager(Notepad notepad,JTextArea textArea,JFrame fakeParent,JMenuBar menuBar,Config config,FileHandler fileHandler,Notepad.Counter counter)
{
this.textArea=textArea;
this.notepad=notepad;
this.fakeParent=fakeParent;
this.menuBar=menuBar;
this.config=config;
this.fileHandler=fileHandler;
this.counter=counter;
initComponents();
addMenuItems();
addEventListeners();
bindShortcutKeys();
setConfigSettings();
menuBar.add(viewMenu);
}
private void initComponents()
{
statusMenuItem=new JCheckBoxMenuItem("Status");
zoomInMenuItem=new JMenuItem("");
zoomOutMenuItem=new JMenuItem("");
zoomMenu=new JMenu("Zoom");
viewMenu=new JMenu("View");
}
private void addMenuItems()
{
zoomMenu.add(zoomInMenuItem);
zoomMenu.add(zoomOutMenuItem);
viewMenu.add(zoomMenu);
viewMenu.add(statusMenuItem);
}
private void setConfigSettings()
{
if(this.config.statusBar)statusMenuItem.doClick();
}

private void bindShortcutKeys()
{
zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, KeyEvent.CTRL_DOWN_MASK));
zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, KeyEvent.CTRL_DOWN_MASK));
}
private void addEventListeners()
{
statusMenuItem.addActionListener(ev->{
boolean isSelected=statusMenuItem.isSelected();
config.statusBar=isSelected;
notepad.setStatusBarPanelVisibility(isSelected);
});



zoomInMenuItem.addActionListener(ev->{
fontSize=textArea.getFont().getSize();
if(fontSize<maxFontSize)
{
fontSize+=2;//increment font size 
Font selectedFont=textArea.getFont();
textArea.setFont(new Font(selectedFont.getName(),selectedFont.getStyle(),fontSize));
}
});
zoomOutMenuItem.addActionListener(ev->{
fontSize=textArea.getFont().getSize();
if(fontSize>minFontSize)
{
fontSize-=2;//decrement font size 
Font selectedFont=textArea.getFont();
textArea.setFont(new Font(selectedFont.getName(),selectedFont.getStyle(),fontSize));
}
});



}
}
