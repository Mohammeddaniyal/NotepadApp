package com.thinking.machines.notepad.io;
import com.thinking.machines.notepad.*;
import java.io.*;
import javax.swing.*;

public  class FileHandler
{
private String fileName;
private File file;
private RandomAccessFile randomAccessFile;
private Notepad notepad;
private JTextArea textArea;
public  FileHandler(Notepad notepad,JTextArea textArea,String fileName)
{
this.notepad=notepad;
this.textArea=textArea;
this.fileName=fileName;
}
public void askToSaveBeforeClose()
{
int choice;
String name="";
if(randomAccessFile!=null) name=this.fileName;
else name="Untitled";

choice=JOptionPane.showConfirmDialog(notepad,"Do you want to save changes to "+name+" ?","My Notepad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
if(choice==JOptionPane.YES_OPTION)
{
if(randomAccessFile!=null)saveFile();
else saveAs();
try
{
randomAccessFile.close();
}catch(IOException ioException)
{

}
notepad.closeFrame();
}
else if(choice==JOptionPane.NO_OPTION)
{
if(randomAccessFile!=null) 
{
try
{
randomAccessFile.close();
}catch(IOException ioException)
{

}

}
notepad.closeFrame();
}
else if(choice==JOptionPane.CANCEL_OPTION)
{
//do nothing in this dont close
notepad.setVisible(true);
}


else
{
//Exit without any confirmation changes
if(randomAccessFile!=null)
{
try
{
randomAccessFile.close();
}catch(IOException ioException)
{

}
}
notepad.closeFrame();
}
}

public boolean askToSaveBeforeOpenNewFile()
{

int choice;
String name="";
if(randomAccessFile!=null) name=fileName;
else name="Untitled";

choice=JOptionPane.showConfirmDialog(notepad,"Do you want to save changes to "+name+" ?","My Notepad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
if(choice==JOptionPane.YES_OPTION)
{
if(randomAccessFile!=null)saveFile();
else saveAs();
try
{
randomAccessFile.close();
}catch(IOException ioException)
{

}
//closeFrame();
}
else if(choice==JOptionPane.NO_OPTION)
{
if(randomAccessFile!=null) 
{
try
{
randomAccessFile.close();
}catch(IOException ioException)
{

}

}
//closeFrame();
}
else if(choice==JOptionPane.CANCEL_OPTION)
{
return true;
//do nothing in this dont open new file
}
else
{
//Exit without any confirmation changes
return true;
}


return false;
}


public void askToSaveBeforeOpeningNewFile()
{
int choice;
String name="";
if(randomAccessFile!=null) name=fileName;
else name="Untitled";

choice=JOptionPane.showConfirmDialog(notepad,"Do you want to save changes to "+name+" ?","My Notepad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
if(choice==JOptionPane.YES_OPTION)
{
if(randomAccessFile!=null)saveFile();
else saveAs();
this.textArea.setText("");
}
else if(choice==JOptionPane.NO_OPTION)
{
this.textArea.setText("");
}
else if(choice==JOptionPane.CANCEL_OPTION)
{
//do nothing in this dont close
}
}
public void saveFile()
{
try
{
randomAccessFile.setLength(0);
randomAccessFile.writeBytes(textArea.getText());
}catch(IOException io)
{
}
}
public void saveAs()
{
JFileChooser fileChooser=new JFileChooser();
fileChooser.setCurrentDirectory(new File("."));
int result=fileChooser.showSaveDialog(null);
if(result==JFileChooser.APPROVE_OPTION)
{
File selectedFile=fileChooser.getSelectedFile();
if(selectedFile.isDirectory()) return;
if(selectedFile.exists())
{
int choice;

choice=JOptionPane.showConfirmDialog(fileChooser,selectedFile.getName()+" already exists.\nDo you want to replace it?","Confirm Save As",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
if(choice==JOptionPane.YES_OPTION)
{
try
{
randomAccessFile=new RandomAccessFile(selectedFile,"rw");
randomAccessFile.writeBytes(textArea.getText());
this.file=selectedFile;
this.fileName=file.getName();
notepad.setFileName(fileName);
notepad.setTitle(this.fileName+" -My Notepad");
return;
}catch(IOException ioException)
{
}
}
else if(choice==JOptionPane.NO_OPTION)
{
saveAs();
return;
}
}
try
{
this.file=selectedFile;
this.fileName=file.getName();
randomAccessFile=new RandomAccessFile(file,"rw");
randomAccessFile.writeBytes(textArea.getText());
notepad.setFileName(fileName);
notepad.setTitle(fileName+" -My Notepad");
}catch(IOException ioException)
{
}
}
}


public int openFile()
{
int i=0;
randomAccessFile=null;
if(fileName==null)
{
notepad.setFileName(fileName);
notepad.setTitle("Untitled"+" - My Notepad");
}
else
{
try
{
file=new File(fileName);
if(!file.exists())
{
int option=JOptionPane.showConfirmDialog(notepad,"Cannot find the "+fileName+" file.\n\nDo you want to create a new file?","My Notepad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
if(option==JOptionPane.YES_OPTION)
{
randomAccessFile=new RandomAccessFile(file,"rw");
}
else if(option==JOptionPane.NO_OPTION)
{
fileName="Untitled";
}
else if(option==JOptionPane.CANCEL_OPTION)
{
notepad.closeFrame();
}
}
else
{
randomAccessFile=new RandomAccessFile(file,"rw");
int caretPosition=textArea.getCaretPosition();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
i++;
String line=randomAccessFile.readLine();
SwingUtilities.invokeLater(()->{
textArea.append(line+"\n");
textArea.setCaretPosition(caretPosition);
});
}
}
}catch(IOException exception)
{
//do nothing
}
notepad.setFileName(fileName);
notepad.setTitle(fileName+"- My Notepad");
}
return i;
}



}