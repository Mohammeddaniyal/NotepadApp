package com.thinking.machines.notepad;
import com.thinking.machines.notepad.exceptions.*;
import com.thinking.machines.notepad.io.*;
import com.thinking.machines.notepad.models.*;
import com.thinking.machines.notepad.managers.*;
import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.awt.print.PrinterException;
import java.awt.print.Printable;
import java.awt.event.*;
import java.awt.datatransfer.*;
import javax.swing.undo.*;
import java.io.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.util.*;
public class Notepad extends JFrame
{
public class Counter
{
public boolean suppressChangeEvents=false;
public boolean isTextChanged=false;
}
private JFrame fakeParent;
private ImageIcon logoIcon;
public Config config=ConfigManager.getConfig();
private Counter counter;
private static int frameCount=0;
private boolean firstTime=true;
private javax.swing.Timer clipBoardChecker;
private UndoManager undoManager;
private FileHandler fileHandler;
private String fileName;
private JMenuBar menuBar;
private JTextArea textArea;
private JScrollPane scrollPane;
private Container container;
private JPanel statusbarPanel;
private JLabel statusLabel;

private FileMenuManager fileMenuManager;
private EditMenuManager editMenuManager;
private FormatMenuManager formatMenuManager;
private ViewMenuManager viewMenuManager;
private HelpMenuManager helpMenuManager;

public void setFileName(String fileName)
{
this.fileName=fileName;
}
private void bindShortcutKeys()
{
// adding KeyBindings for offerning better control for handling keyboard events and
// are more suitable for complexr editors like Notepad


InputMap inputMap=textArea.getInputMap();
ActionMap actionMap=textArea.getActionMap();

// ctrl + backspace (delete previous word)

inputMap.put(KeyStroke.getKeyStroke("control BACK_SPACE"),"delete-previous-word");

actionMap.put("delete-previous-word",new AbstractAction(){
@Override
public void actionPerformed(ActionEvent ev)
{
try
{
int caretPos=textArea.getCaretPosition();
int prevWordPos=Utilities.getWordStart(textArea,caretPos-1);
textArea.replaceRange("",prevWordPos,caretPos);
}catch(BadLocationException badLocationException)
{
LogException.log(badLocationException);
}
}
});

}

private void initComponents()
{
counter=new Counter();
textArea=new JTextArea();
scrollPane=new JScrollPane(textArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
container=getContentPane();
 statusbarPanel=new JPanel(new BorderLayout());
undoManager=new UndoManager();
logoIcon=new ImageIcon(this.getClass().getResource("/icons/icon.png"));

fileHandler=new FileHandler(this,fakeParent,textArea,scrollPane,fileName);
fakeParent=new JFrame();
fakeParent.setIconImage(logoIcon.getImage());

menuBar=new JMenuBar();
fileMenuManager=new FileMenuManager(this,textArea,fakeParent,menuBar,config,fileHandler,counter);
editMenuManager=new EditMenuManager(this,textArea,scrollPane,fakeParent,menuBar,config,fileHandler,counter,undoManager);
formatMenuManager=new FormatMenuManager(this,textArea,fakeParent,menuBar,config,fileHandler,counter);
viewMenuManager=new ViewMenuManager(this,textArea,fakeParent,menuBar,config,fileHandler,counter);
helpMenuManager=new HelpMenuManager(this,menuBar);
setJMenuBar(menuBar);
}
private void addEventListeners()
{


textArea.addCaretListener((ev)->{
String selectedText=textArea.getSelectedText();


editMenuManager.setEnabledCutMenuItem(selectedText!=null);
editMenuManager.setEnabledCopyMenuItem(selectedText!=null && (!selectedText.isEmpty()));
editMenuManager.setEnabledDeleteMenuItem(selectedText!=null);


try
{
int caretPosition=textArea.getCaretPosition();
int lineNumber=textArea.getLineOfOffset(caretPosition)+1;//0 based
//caretPosition-actualPosition gives the column no. because it's continues offset no reset of number from new line it continues, suppose at line one there are 10 char then from line it start counting from 11
int columnNumber=caretPosition-textArea.getLineStartOffset(lineNumber-1)+1;//-1 because 0 based
statusLabel.setText("Line: "+lineNumber+", Column: "+columnNumber);
}catch(BadLocationException badLocationException)
{
JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
LogException.log(badLocationException);
}
});

clipBoardChecker=new javax.swing.Timer(500,(ev)->{
editMenuManager.setEnabledPasteMenuItem(isClipboardAvailable());
});


textArea.getDocument().addUndoableEditListener(ev->{
undoManager.addEdit(ev.getEdit());
editMenuManager.setEnabledUndoMenuItem(undoManager.canUndo());
}
);




}
private void setupLayout()
{
setIconImage(logoIcon.getImage());
container.setLayout(new BorderLayout());
Font selectedFont=formatMenuManager.getSelectedFont();
textArea.setFont(selectedFont);

Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int width=800;
int height=600;
setSize(width,height);
int x=(d.width/2)-(width/2);
int y=(d.height/2)-(height/2);
setSize(width,height);
setLocation(x,y);
setVisible(true);
statusbarPanel.setVisible(false);
statusLabel=new JLabel("Line: 1, Column: 1");
statusLabel.setFont(new Font("Segoe UI",Font.PLAIN,13));
statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
statusbarPanel.add(statusLabel,BorderLayout.WEST);
container.add(scrollPane,BorderLayout.CENTER);	
container.add(statusbarPanel,BorderLayout.SOUTH);
}
private void initUIState()
{

//removing default behaviour of system of ctrl + H as backspace
textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("control H"),"none");

}
public Notepad(String fileName)
{
this.fileName=fileName;
frameCount++;
try
{
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
}catch(Exception exception)
{
JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
LogException.log(exception);
}
initComponents();
bindShortcutKeys();
addEventListeners();
initUIState();
setupLayout();
counter.isTextChanged=false;
clipBoardChecker.start();
fileHandler.openFile(counter);	//opening file and appending in textArea

//set default close operation as do nothing

setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

// important for tracking changes in the textArea
textArea.getDocument().addDocumentListener(
new DocumentListener(){
@Override
public void insertUpdate(DocumentEvent de)
{
if(!counter.suppressChangeEvents) updateTitle();
}
@Override
public void removeUpdate(DocumentEvent de)
{
if(!counter.suppressChangeEvents) updateTitle();
}
@Override
public void changedUpdate(DocumentEvent de)
{
if(!counter.suppressChangeEvents) updateTitle();
}
});

addWindowListener(new WindowAdapter(){
@Override
public void windowClosing(WindowEvent we)
{
boolean close=true;
if(counter.isTextChanged)close=fileHandler.askToSaveBeforeClose(counter);
if(close)
{
ConfigManager.setConfig(config);
ConfigManager.saveConfig();
closeFrame();
}
else
{
Notepad.this.setVisible(true);
}
}
});

SwingUtilities.invokeLater(()->{
textArea.requestFocusInWindow();
});

}
private boolean isClipboardAvailable()
{
try
{
Clipboard clipboard=Toolkit.getDefaultToolkit().getSystemClipboard();
return  clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor);
}catch(Exception e)
{
return false;
}
}
public void setStatusBarPanelVisibility(boolean visible)
{
statusbarPanel.setVisible(visible);
}
public void openNewWindow()
{
SwingUtilities.invokeLater(()->{
Notepad newNotepad=new Notepad(null);
}
);
}
public void closeFrame()
{
frameCount--;
if(frameCount==0)
{
System.exit(0);
}
else
{
dispose();
}
}
private void updateTitle()
{
System.out.println("Bye");
counter.isTextChanged=true;
if(fileHandler.getDisplayFileName()!=null )
{
setTitle("*"+fileHandler.getDisplayFileName()+" - DaniPad");
}
else
{
setTitle("*Untitled - DaniPad");
}
}



}
