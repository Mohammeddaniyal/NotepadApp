package com.thinking.machines.notepad.io;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
public class LazyLoader
{
private JTextArea textArea;
private JScrollPane scrollPane;
private File file;
private static final int LINES_PER_LOAD=1000;
public LazyLoader(JTextArea textArea,JScrollPane scrollPane,File file)
{
this.textArea=textArea;
this.scrollPane=scrollPane;
this.file=file;
}
}