package com.thinking.machines.notepad.managers;
import com.thinking.machines.notepad.io.*;
import com.thinking.machines.notepad.models.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class FileMenuManager
{
private JTextArea textArea;
private JMenuBar menuBar;
private Config config;
private FileHandler fileHandler;
private JMenu fileMenu;
private JMenuItem newMenuItem,newWindowMenuItem,openMenuItem,saveMenuItem;
private JMenuItem saveAsMenuItem,pageSetupMenuItem,printMenuItem,exitMenuItem;

public FileMenuManager(JTextArea textArea,JMenuBar menuBar,Config config,FileHandler fileHandler)
{
this.textArea=textArea;
this.menuBar=menuBar;
this.config=config;
this.fileHandler=fileHandler;
initComponents();
addMenuItems();
addEventListeners();
menuBar.add(fileMenu);
}
private void initComponents()
{
newMenuItem=new JMenuItem("New");
newWindowMenuItem=new JMenuItem("New Window");
openMenuItem=new JMenuItem("Open");
saveMenuItem=new JMenuItem("Save");
saveAsMenuItem=new JMenuItem("Save As...");
pageSetupMenuItem=new JMenuItem("Page Setup...");
printMenuItem=new JMenuItem("Print");
exitMenuItem=new JMenuItem("Exit");
fileMenu=new JMenu("File");
}
private void addMenuItems()
{
fileMenu.add(newMenuItem);
fileMenu.add(newWindowMenuItem);
fileMenu.add(openMenuItem);
fileMenu.add(saveMenuItem);
fileMenu.add(saveAsMenuItem);
fileMenu.add(pageSetupMenuItem);
fileMenu.add(printMenuItem);
fileMenu.add(exitMenuItem);
}
private void addEventListeners()
{}
}
