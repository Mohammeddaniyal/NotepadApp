package com.thinking.machines.notepad.managers;
import com.thinking.machines.notepad.Notepad;
import com.thinking.machines.notepad.io.*;
import com.thinking.machines.notepad.ui.*;
import com.thinking.machines.notepad.models.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class FormatMenuManager
{
private JTextArea textArea;
private Notepad notepad;
private JFrame fakeParent;
private JMenuBar menuBar; 
private Config config;
private FileHandler fileHandler;
private Notepad.Counter counter;
private FontChooser fontChooser;
private JMenu formatMenu;
private JCheckBoxMenuItem wordWrapMenuItem;
private JMenuItem fontMenuItem;
public FormatMenuManager(Notepad notepad,JTextArea textArea,JFrame fakeParent,JMenuBar menuBar,Config config,FileHandler fileHandler,Notepad.Counter counter)
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
setConfigSettings();
menuBar.add(formatMenu);
}
private void initComponents()
{
fontChooser=new FontChooser(notepad);
wordWrapMenuItem=new JCheckBoxMenuItem("Word Wrap");
fontMenuItem=new JMenuItem("Font");
formatMenu=new JMenu("Format");
}
private void addMenuItems()
{
formatMenu.add(wordWrapMenuItem);
formatMenu.add(fontMenuItem);
}
private void setConfigSettings()
{
if(this.config.wordWrap)wordWrapMenuItem.doClick();
}

private void addEventListeners()
{
wordWrapMenuItem.addActionListener(ev->{
boolean isSelected=wordWrapMenuItem.isSelected();
config.wordWrap=isSelected;
textArea.setLineWrap(isSelected);
textArea.setWrapStyleWord(isSelected);
textArea.setCaretPosition(0);
});

fontMenuItem.addActionListener(ev->{
fontChooser.setVisible(true);
Font chosenFont=fontChooser.getSelectedFont();
if(chosenFont!=null) {
textArea.setFont(chosenFont);
notepad.updateFontSize();
}
});

}

public Font getSelectedFont()
{
return fontChooser.getSelectedFont();
}
}
