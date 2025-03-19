package com.thinking.machines.notepad.managers;
import com.thinking.machines.notepad.io.*;
import com.thinking.machines.notepad.*;
import com.thinking.machines.notepad.models.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class HelpMenuManager
{
private Notepad notepad;
private JMenuBar menuBar; 
private JMenu helpMenu;

public HelpMenuManager(Notepad notepad,JMenuBar menuBar)
{
this.notepad=notepad;
this.menuBar=menuBar;
initComponents();
addEventListeners();
menuBar.add(helpMenu);
}
private void initComponents()
{
helpMenu=new JMenu("Help");
}
private void addEventListeners()
{}
}
