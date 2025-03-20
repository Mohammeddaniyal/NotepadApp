package com.thinking.machines.notepad.io;
import com.thinking.machines.notepad.exceptions.*;
import com.thinking.machines.notepad.*;
import com.thinking.machines.notepad.ui.*;
import java.io.*;
import java.nio.charset.Charset;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.filechooser.*;
public  class FileHandler
{
private JFileChooser fileChooser=new JFileChooser();
private static final List<String> knownExtensions = Arrays.asList("txt", "java", "c", "cpp", "py", "html", "css");
private StatusPanel statusPanel;
private Charset encoding;
private String extension;
private String filePath;
private String fileName;
private String baseFileName;
private String displayFileName;
private File file;
private Notepad notepad;
private JFrame fakeParent;
private JTextArea textArea;
private JScrollPane scrollPane;
public  FileHandler(Notepad notepad,JFrame fakeParent,JTextArea textArea,JScrollPane scrollPane,String filePath)
{
this.notepad=notepad;
this.fakeParent=fakeParent;
this.textArea=textArea;
this.scrollPane=scrollPane;
this.filePath=filePath;
if(this.filePath!=null)
{
this.file=new File(this.filePath);
this.baseFileName=this.file.getName();
}
fileChooser.setFileView(new FileView() {
@Override
public Icon getIcon(File f) {
try {
// Get the system icon safely (avoiding null)
Icon icon = FileSystemView.getFileSystemView().getSystemIcon(f);
return (icon != null) ? icon : UIManager.getIcon("FileView.fileIcon"); // Fallback icon
} catch (Exception e) {
return UIManager.getIcon("FileView.fileIcon"); // Fallback to default icon
}
}
});

}
public void setStatusPanelProperty(StatusPanel statusPanel)
{
this.statusPanel=statusPanel;
}
public String getDisplayFileName()
{
return this.displayFileName;
}
public void setExtension()
{
int dotIndex=this.filePath.lastIndexOf(".");
if(dotIndex!=-1)
{
this.extension=this.filePath.substring(dotIndex+1);
if(dotIndex==this.filePath.length()-1)
{
this.extension="txt";
this.filePath=this.filePath+this.extension;
this.baseFileName=this.file.getName()+this.extension;
}
this.baseFileName=this.file.getName();
}
else 
{
this.extension="txt";
this.filePath=this.filePath+"."+this.extension;
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
//System.out.println("Invalid file name contains : "+invalidChars.charAt(x));
//System.out.println(fileName);
return false;
}
}
return true;
}
public boolean askToSaveBeforeClose(Notepad.Counter c)
{
int choice;
String name="";
if(this.filePath!=null) name=this.filePath;
else name="Untitled";

choice=JOptionPane.showConfirmDialog(notepad,"Do you want to save changes to "+name+" ?","Danipad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
if(choice==JOptionPane.YES_OPTION)
{
if(this.filePath!=null) saveFile(c);
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
if(this.filePath!=null) name=filePath;
else name="Untitled";

choice=JOptionPane.showConfirmDialog(notepad,"Do you want to save changes to "+name+" ?","Danipad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
if(choice==JOptionPane.YES_OPTION)
{
if(this.filePath!=null)saveFile(c);
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
if(this.filePath!=null) name=displayFileName;
else name="Untitled";

choice=JOptionPane.showConfirmDialog(notepad,"Do you want to save changes to "+name+" ?","Danipad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
if(choice==JOptionPane.YES_OPTION)
{
if(this.filePath!=null)success=saveFile(c);
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
this.filePath=null;
this.extension=null;
this.displayFileName=null;
notepad.setFileName(this.filePath);
notepad.setTitle("Untitled - Danipad");
return success;
}
public boolean openFilePrompt()
{
fileChooser.setCurrentDirectory(new File("."));

int result=fileChooser.showOpenDialog(notepad);
if(result==JFileChooser.APPROVE_OPTION)
{
File selectedFile=fileChooser.getSelectedFile();
if(selectedFile.isDirectory()) return false;
textArea.setText("");
this.filePath=selectedFile.getAbsolutePath();
this.fileName=selectedFile.getName();
this.file=selectedFile;
return true;
}
return false;
}
public boolean saveFile(Notepad.Counter c)
{
try(BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file),this.encoding)))
{
bufferedWriter.write(textArea.getText());
}catch(IOException io)
{
JOptionPane.showMessageDialog(notepad,"Failed to save the file. Please check permissions and try again.", "Save Error",JOptionPane.ERROR_MESSAGE);
LogException.log(io);
return false;
}
statusPanel.setSaved();
return true;
}
public boolean saveAs(Notepad.Counter c)
{
boolean saved=false;
try
{

fileChooser.setCurrentDirectory(new File("."));
 int result=fileChooser.showSaveDialog(notepad);
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
this.file=selectedFile;
this.filePath=selectedFile.getAbsolutePath();
this.fileName=selectedFile.getName();
this.encoding=Charset.forName("UTF-8");
this.encoding=EncodingDetector.detectEncoding(file);
setExtension();
setDisplayFileName();
saveFile(c);
notepad.setFileName(this.filePath);
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
this.fileName=selectedFile.getName();
this.filePath=selectedFile.getAbsolutePath();
setExtension();
setDisplayFileName();
this.file=new File(this.filePath);
this.file.createNewFile();
this.encoding=Charset.forName("UTF-8");
this.encoding=EncodingDetector.detectEncoding(file);
saveFile(c);
notepad.setFileName(this.filePath);
notepad.setTitle(this.displayFileName+" - Danipad");
saved=true;
}
}
}catch(Exception exception)
{
saved=false;
JOptionPane.showMessageDialog(notepad,"Failed to save the file. Please check permissions and try again.", "Save Error",JOptionPane.ERROR_MESSAGE);
System.out.println(exception);
LogException.log(exception);
}

if(saved) statusPanel.setSaved();
return saved;
}


public void openFile(Notepad.Counter c)
{
try
{
if(this.filePath==null)
{
notepad.setTitle("Untitled"+" - Danipad");
}
else
{
this.file=new File(filePath);
boolean success=setupFileLocation();
setExtension();
setDisplayFileName();
this.file=new File(filePath);


if(!success)
{
this.filePath="Untitled";
this.displayFileName=null;
this.extension=null;
this.encoding=Charset.forName("UTF-8");
}

else if(!this.file.exists())
{

int option=JOptionPane.showConfirmDialog(notepad,"Cannot find the "+filePath+" file.\n\nDo you want to create a new file?","Danipad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
if(option==JOptionPane.YES_OPTION)
{
this.file.createNewFile();
textArea.setText("");
this.encoding=EncodingDetector.detectEncoding(file);
//new file created
}
else if(option==JOptionPane.NO_OPTION)
{
this.filePath="Untitled";
this.displayFileName=null;
this.extension=null;
this.encoding=Charset.forName("UTF-8");
}
else if(option==JOptionPane.CANCEL_OPTION)
{
notepad.closeFrame();
}
else
{
notepad.closeFrame();
}
}
else
{
this.encoding=EncodingDetector.detectEncoding(file);

BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(new FileInputStream(file),encoding));
c.suppressChangeEvents=true;
SwingWorker<Void,String> worker=new SwingWorker<>(){
@Override
protected Void doInBackground() throws Exception
{
String line;
int count=0;
List<String> batch=new ArrayList<>();
while((line=bufferedReader.readLine())!=null)
{
batch.add(line);
if(count%100==0)
{
publish(batch.toArray(new String[0])); //publish the readed line to append it on textArea
batch.clear();
}
count++;
}
if(!batch.isEmpty())
{
publish(batch.toArray(new String[0])); //publish remaining lines
batch.clear();
}
return null;
}
@Override
protected void process(List<String> chunks)
{
for(String line:chunks)
{
textArea.append(line+"\n");
}
//setting caret position at the begining
textArea.setCaretPosition(0);
}
@Override 
protected void done()
{
try
{
if(bufferedReader!=null) bufferedReader.close();
c.suppressChangeEvents=false;
//to ensure the newly loaded content isn't treated as an "Undoable" change
notepad.discardUndoManagerAllEdits();
}catch(IOException ioException)
{
LogException.log(ioException);
}
}
};// SwingWorker anonynmous class ends here

worker.execute();

}
if(this.displayFileName!=null)notepad.setTitle(this.displayFileName+"- Danipad");
else notepad.setTitle("Untitled - Danipad");

}
notepad.setFileName(this.filePath);
}catch(IOException exception)
{
JOptionPane.showMessageDialog(notepad, "Failed to open file. Please check if the file exists and try again.", "File Error", JOptionPane.ERROR_MESSAGE);
notepad.setTitle("Untitled"+" - Danipad");
notepad.setFileName(null);
LogException.log(exception);
}

}
private boolean setupFileLocation()
{
File parentDir=this.file.getParentFile();
if(parentDir==null) return true;
if(this.file.exists())
{
if(this.file.isDirectory())
{
JOptionPane.showMessageDialog(notepad,"The given path refers to a directory not a file. Cannot create file","Error",JOptionPane.ERROR_MESSAGE);
return false;
}
}
else
{
if(parentDir!=null && parentDir.exists())
{
//file dont exists create file
}
else
{
//parent dir not exists create the missing directories
if(parentDir!=null && parentDir.mkdir())
{
return true;
}
else 
{
//failed
JOptionPane.showMessageDialog(notepad,"The System cannot find the specified path","Warning",JOptionPane.WARNING_MESSAGE);
return false;
}
}
}
return true;
}
public String getCurrentEncoding()
{
return ((this.encoding!=null)?this.encoding.name():"UTF-8");
}
public void setSelectedEncoding(String encodingName)
{
this.encoding=Charset.forName(encodingName);
}
}
