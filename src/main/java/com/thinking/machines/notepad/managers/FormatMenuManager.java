package com.thinking.machines.notepad.managers;
import com.thinking.machines.notepad.io.*;
import com.thinking.machines.notepad.models.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class FormatMenuManager
{
private JTextArea textArea;
private JMenuBar menuBar; 
private Config config;
private FileHandler fileHandler;
public FormatMenuManager(JTextArea textArea,JMenuBar menuBar,Config config,FileHandler fileHandler)
{
this.textArea=textArea;
this.menuBar=menuBar;
this.config=config;
this.fileHandler=fileHandler;
initComponents();
addEventListeners();
}
private void initComponents()
{}
private void addEventListeners()
{}
}
