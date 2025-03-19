package com.thinking.machines.notepad.managers;
import com.thinking.machines.notepad.Notepad;
import com.thinking.machines.notepad.io.*;
import com.thinking.machines.notepad.models.*;
import com.thinking.machines.notepad.exceptions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.awt.print.PrinterException;
import java.awt.print.Printable;
public class FileMenuManager
{
private Notepad notepad;
private JTextArea textArea;
private JFrame fakeParent;
private JMenuBar menuBar;
private Config config;
private FileHandler fileHandler;
private Notepad.Counter counter;
private JMenu fileMenu;
private JMenuItem newMenuItem,newWindowMenuItem,openMenuItem,saveMenuItem;
private JMenuItem saveAsMenuItem,pageSetupMenuItem,printMenuItem,exitMenuItem;

public FileMenuManager(Notepad notepad,JTextArea textArea,JFrame fakeParent,JMenuBar menuBar,Config config,FileHandler fileHandler,Notepad.Counter counter)
{
this.notepad=notepad;
this.textArea=textArea;
this.fakeParent=fakeParent;
this.menuBar=menuBar;
this.config=config;
this.fileHandler=fileHandler;
this.counter=counter;
initComponents();
addMenuItems();
addEventListeners();
bindShortcutKeys();
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
private void bindShortcutKeys()
{
newMenuItem.setAccelerator(KeyStroke.getKeyStroke('N',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
newWindowMenuItem.setAccelerator(KeyStroke.getKeyStroke('N',KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
openMenuItem.setAccelerator(KeyStroke.getKeyStroke('O',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
saveMenuItem.setAccelerator(KeyStroke.getKeyStroke('S',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke('S',KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
printMenuItem.setAccelerator(KeyStroke.getKeyStroke('P',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
}
private void addEventListeners()
{

newMenuItem.addActionListener((ev)->{
boolean success=fileHandler.askToSaveBeforeOpeningNewFile(counter,counter.isTextChanged);
if(success)counter.isTextChanged=false;
});


newWindowMenuItem.addActionListener(ev->{
notepad.openNewWindow();
});
	
openMenuItem.addActionListener(ev->{
if(counter.isTextChanged) 
{
if(fileHandler.askToSaveBeforeOpenNewFile(counter)) return;
String displayFileName=fileHandler.getDisplayFileName();
if(displayFileName==null) displayFileName="Untitled";
notepad.setTitle(displayFileName+"- DaniPad");
}
boolean success=true;
success=fileHandler.openFilePrompt();
if(success==false) return;
fileHandler.openFile(counter);

counter.isTextChanged=false;
notepad.setTitle(fileHandler.getDisplayFileName()+"- DaniPad");

});


saveMenuItem.addActionListener(ev->{
boolean success=true;
if(fileHandler.getDisplayFileName()!=null)
{
success=fileHandler.saveFile(counter);
}
else
{
success=fileHandler.saveAs(counter);
}
if(success)
{
// isTextChanged equals true after saving the file make it false because now the file is saved
counter.isTextChanged=false;
if(fileHandler.getDisplayFileName()!=null)notepad.setTitle(fileHandler.getDisplayFileName()+" - DaniPad");
}
});
saveAsMenuItem.addActionListener(ev->{
if(fileHandler.saveAs(counter))
{
// isTextChanged equals true after saving the file make it false because now the file is saved
counter.isTextChanged=false;
if(fileHandler.getDisplayFileName()!=null)notepad.setTitle(fileHandler.getDisplayFileName()+" - DaniPad");
}
});

exitMenuItem.addActionListener((ev)->{
if(counter.isTextChanged) 
{
fileHandler.askToSaveBeforeClose(counter);
}
notepad.closeFrame();
});




printMenuItem.addActionListener(ev->{
PrinterJob printerJob=PrinterJob.getPrinterJob();
printerJob.setPrintable((graphics,pageFormat,pageIndex)->{

if(pageIndex>0) return Printable.NO_SUCH_PAGE; //only one page for printing now

Graphics2D g2d=(Graphics2D)graphics;

//adjust for margins
g2d.translate(pageFormat.getImageableX(),pageFormat.getImageableY());

//scale content if necessary
g2d.setFont(textArea.getFont());
textArea.printAll(graphics);//print JTextArea content

return Printable.PAGE_EXISTS;
});
//show print dialog
if(printerJob.printDialog())
{
try
{
printerJob.print();
}catch(PrinterException printerException)
{
LogException.log(printerException);
JOptionPane.showMessageDialog(fakeParent,"Printing failed, try again later","Print Error",JOptionPane.ERROR_MESSAGE);
}
}
});

pageSetupMenuItem.addActionListener(ev->{
PrinterJob printerJob=PrinterJob.getPrinterJob();
PageFormat pageFormat=printerJob.pageDialog(printerJob.defaultPage());
});


}
}
