package com.thinking.machines.notepad.io;
import com.thinking.machines.notepad.exceptions.*;
import com.thinking.machines.notepad.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
public  class FileHandler
{
private final static int LINES_PER_LOAD=1000; // load 1000 lines per batch
private int lastLineRead=0; // Track last loaded line
private List<String> cachedLines=new ArrayList<>(); // Cache loaded lines
private static final List<String> knownExtensions = Arrays.asList("txt", "java", "c", "cpp", "py", "html", "css");
private String extension;
private String fileName;
private String baseFileName;
private String displayFileName;
private File file;
private Notepad notepad;
private JTextArea textArea;
public  FileHandler(Notepad notepad,JTextArea textArea,String fileName)
{
this.notepad=notepad;
this.textArea=textArea;
this.fileName=fileName;
if(this.fileName!=null)
{
this.file=new File(this.fileName);
this.baseFileName=this.file.getName();
}
}
public String getDisplayFileName()
{
return this.displayFileName;
}
public void setExtension()
{
int dotIndex=fileName.lastIndexOf(".");
if(dotIndex!=-1)
{
this.extension=fileName.substring(dotIndex+1);
if(dotIndex==this.fileName.length()-1)
{
this.extension="txt";
this.fileName=this.fileName+this.extension;
this.baseFileName=this.file.getName()+this.extension;
}
}
else 
{
this.extension="txt";
this.fileName=this.fileName+"."+this.extension;
this.baseFileName=this.file.getName()+"."+this.extension;
}

}
public void setDisplayFileName()
{
String extension=this.extension.toLowerCase();
if(knownExtensions.contains(extension))
{
this.displayFileName=this.baseFileName.substring(0,this.baseFileName.lastIndexOf("."));
}
else
{
this.displayFileName=this.baseFileName;
}


}
public boolean checkFileNameValidity(String fileName)
{
fileName=fileName.trim();
if(fileName.length()==0) return false;
String invalidChars="[\\\\/:*?\"<>|]";
for(int x=0;x<invalidChars.length();x++)
{
if(fileName.indexOf(invalidChars.charAt(x))!=-1)
{
System.out.println("Invalid file name contains : "+invalidChars.charAt(x));
System.out.println(fileName);
return false;
}
}
return true;
}
public boolean askToSaveBeforeClose(Notepad.Counter c)
{
int choice;
String name="";
if(this.fileName!=null) name=this.fileName;
else name="Untitled";

choice=JOptionPane.showConfirmDialog(null,"Do you want to save changes to "+name+" ?","Danipad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
if(choice==JOptionPane.YES_OPTION)
{
if(this.fileName!=null) saveFile(c);
else saveAs(c);
notepad.closeFrame();
}
else if(choice==JOptionPane.NO_OPTION)
{
//do nothing
notepad.closeFrame();
}
else if(choice==JOptionPane.CANCEL_OPTION)
{
//do nothing in this dont close
notepad.setVisible(true);
}
else
{
//do nothing
notepad.setVisible(true);
}
return false;
}

public boolean askToSaveBeforeOpenNewFile(Notepad.Counter c)
{
int choice;
String name="";
if(this.fileName!=null) name=fileName;
else name="Untitled";

choice=JOptionPane.showConfirmDialog(null,"Do you want to save changes to "+name+" ?","Danipad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
if(choice==JOptionPane.YES_OPTION)
{
if(this.fileName!=null)saveFile(c);
else saveAs(c);
//closeFrame();
}
else if(choice==JOptionPane.NO_OPTION)
{
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


public boolean askToSaveBeforeOpeningNewFile(Notepad.Counter c,boolean isTextChanged)
{
boolean success=true;
if(isTextChanged)
{
int choice;
String name="";
if(this.fileName!=null) name=displayFileName;
else name="Untitled";

choice=JOptionPane.showConfirmDialog(null,"Do you want to save changes to "+name+" ?","Danipad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
if(choice==JOptionPane.YES_OPTION)
{
if(this.fileName!=null)success=saveFile(c);
else success=saveAs(c);
if(success==false) return success;
this.textArea.setText("");
}
else if(choice==JOptionPane.NO_OPTION)
{
this.textArea.setText("");
}
else if(choice==JOptionPane.CANCEL_OPTION)
{
//do nothing in this dont close
return false;
}
}
else
{
this.textArea.setText("");
}
this.fileName=null;
this.extension=null;
this.displayFileName=null;
notepad.setFileName(this.fileName);
notepad.setTitle("Untitled - Danipad");
return success;
}
public boolean openFilePrompt()
{
JFileChooser fileChooser=new JFileChooser();
fileChooser.setCurrentDirectory(new File("."));
int result=fileChooser.showOpenDialog(null);
if(result==JFileChooser.APPROVE_OPTION)
{
File selectedFile=fileChooser.getSelectedFile();
if(selectedFile.isDirectory()) return false;
textArea.setText("");
this.fileName=selectedFile.getName();
return true;
}
return false;
}
public boolean saveFile(Notepad.Counter c)
{
try
{
RandomAccessFile randomAccessFile=new RandomAccessFile(this.file,"rw");
randomAccessFile.setLength(0);
randomAccessFile.writeBytes(textArea.getText());
c.originalText=textArea.getText();
randomAccessFile.close();
}catch(IOException io)
{
JOptionPane.showMessageDialog(null,"Failed to save the file. Please check permissions and try again.", "Save Error",JOptionPane.ERROR_MESSAGE);
LogException.log(io);
return false;
}
return true;
}
public boolean saveAs(Notepad.Counter c)
{
boolean saved=false;
try
{
JFileChooser fileChooser=new JFileChooser();
fileChooser.setCurrentDirectory(new File("."));
int result=fileChooser.showSaveDialog(null);
if(result==JFileChooser.APPROVE_OPTION)
{
File selectedFile=fileChooser.getSelectedFile();
if(selectedFile.isDirectory()) return false;
if(selectedFile.exists())
{
int choice;

choice=JOptionPane.showConfirmDialog(fileChooser,selectedFile.getName()+" already exists.\nDo you want to replace it?","Confirm Save As",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
if(choice==JOptionPane.YES_OPTION)
{
RandomAccessFile randomAccessFile=new RandomAccessFile(selectedFile,"rw");
randomAccessFile.writeBytes(textArea.getText());
this.file=selectedFile;
this.fileName=selectedFile.getName();
setExtension();
setDisplayFileName();
randomAccessFile.close();
notepad.setFileName(this.fileName);
notepad.setTitle(this.displayFileName+" - Danipad");
saved=true;
}
else if(choice==JOptionPane.NO_OPTION)
{
saveAs(c);
}
}// selectedFile exists ends
else
{
checkFileNameValidity(selectedFile.getName());
this.file=selectedFile;
this.fileName=file.getName();
RandomAccessFile randomAccessFile=new RandomAccessFile(this.file,"rw");
randomAccessFile.writeBytes(textArea.getText());
randomAccessFile.close();
notepad.setFileName(this.fileName);
notepad.setTitle(fileName+" - Danipad");
saved=true;

}
}
}catch(IOException ioException)
{
saved=false;
JOptionPane.showMessageDialog(null,"Failed to save the file. Please check permissions and try again.", "Save Error",JOptionPane.ERROR_MESSAGE);
LogException.log(ioException);
}

if(saved==true) c.originalText=textArea.getText();
return saved;
}


public void openFile(Notepad.Counter c)
{
try
{
if(cachedLines!=null) cachedLines.clear();
c.i=0;
c.originalText="";
BufferedReader bufferedReader=null;
if(this.fileName==null)
{
notepad.setTitle("Untitled"+" - Danipad");
}
else
{
setExtension();
setDisplayFileName();
this.file=new File(fileName);
if(!this.file.exists())
{
int option=JOptionPane.showConfirmDialog(null,"Cannot find the "+fileName+" file.\n\nDo you want to create a new file?","Danipad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
if(option==JOptionPane.YES_OPTION)
{
this.file.createNewFile();
textArea.setText("");
//new file created
}
else if(option==JOptionPane.NO_OPTION)
{
this.fileName="Untitled";
}
else if(option==JOptionPane.CANCEL_OPTION)
{
notepad.closeFrame();
}
}
else
{
bufferedReader=new BufferedReader(new FileReader(file));

SwingWorker<Void,String> worker=new SwingWorker<>(){
@Override
protected void doInBackground() throws Exception
{
String line;
int count=0;
while((line=bufferedReader.readLine())!=null && count<LINES_PER_LOAD)
{
cachedLines.add(line); //store in cache
publish(line); //publish the readed line to append it on textArea
count++;
}
lastLineRead+=count; //update the last line read
}


};// SwingWorker anonynmous class ends here


randomAccessFile=new RandomAccessFile(file,"rw");
int caretPosition=textArea.getCaretPosition();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
c.i++;
String line=randomAccessFile.readLine();
c.originalText=c.originalText+line+"\n";
SwingUtilities.invokeLater(()->{
textArea.append(line+"\n");
textArea.setCaretPosition(caretPosition);
});
}
randomAccessFile.close();


}
notepad.setTitle(this.displayFileName+"- Danipad");
}
notepad.setFileName(this.fileName);
}catch(IOException exception)
{
JOptionPane.showMessageDialog(null, "Failed to open file. Please check if the file exists and try again.", "File Error", JOptionPane.ERROR_MESSAGE);
notepad.setTitle("Untitled"+" - Danipad");
notepad.setFileName(null);
LogException.log(exception);
}

}

}
