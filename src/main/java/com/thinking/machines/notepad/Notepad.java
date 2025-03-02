package com.thinking.machines.notepad;
import com.thinking.machines.notepad.exceptions.*;
import com.thinking.machines.notepad.ui.*;
import com.thinking.machines.notepad.io.*;
import javax.swing.*;
import java.awt.*;
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
public int i=0;
public String originalText="";
}
private ImageIcon logoIcon;
private FontChooser fontChooser;
String originalText;
private int fontSize;
private final int maxFontSize=60;
private final int minFontSize=8;
private String findPreviousSearchedText="";
private String replacePreviousSearchedText="";
private boolean findDialogReset=false;
private int findPreviousStartIndex=-1;
private int findPreviousEndIndex=-1;
private int selectedTextStartIndex=-1;
private int selectedTextEndIndex=-1;
private boolean emptyLineField=false;
private Counter counter;
private static int frameCount=0;
private boolean firstTime=true;
private javax.swing.Timer clipBoardChecker;
private boolean isTextChanged;
private UndoManager undoManager;
private SearchManager searchManager;
private FileHandler fileHandler;
private String fileName;
private JMenuBar menuBar;
private JMenu fileMenu;
private JMenu editMenu;
private JMenu formatMenu;
private JMenu viewMenu;
private JMenu zoomMenu;
private JMenu helpMenu;
private JMenuItem newMenuItem,newWindowMenuItem,openMenuItem,saveMenuItem;
private JMenuItem saveAsMenuItem,pageSetupMenuItem,printMenuItem,exitMenuItem;
private JMenuItem undoMenuItem;
private JMenuItem cutMenuItem,copyMenuItem,pasteMenuItem;
private JMenuItem deleteMenuItem,findMenuItem,findNextMenuItem,findPreviousMenuItem;
private JMenuItem replaceMenuItem,goToMenuItem,selectAllMenuItem;
private JCheckBoxMenuItem wordWrapMenuItem;
private JMenuItem fontMenuItem;
private JMenuItem zoomInMenuItem,zoomOutMenuItem;
private JCheckBoxMenuItem statusMenuItem;

private JMenuItem helpMenuItem,aboutMenuItem;
private JTextArea textArea;
private JScrollPane scrollPane;
private Container container;
private JPanel statusbarPanel;
private JLabel statusLabel;

public void setFileName(String fileName)
{
this.fileName=fileName;
}
private void initMenus()
{

newMenuItem=new JMenuItem("New");
newWindowMenuItem=new JMenuItem("New Window");
openMenuItem=new JMenuItem("Open");
saveMenuItem=new JMenuItem("Save");
saveAsMenuItem=new JMenuItem("Save As...");
pageSetupMenuItem=new JMenuItem("Page Setup...");
printMenuItem=new JMenuItem("Print");
exitMenuItem=new JMenuItem("Exit");

newMenuItem.setAccelerator(KeyStroke.getKeyStroke('N',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
newWindowMenuItem.setAccelerator(KeyStroke.getKeyStroke('N',KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
openMenuItem.setAccelerator(KeyStroke.getKeyStroke('O',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
saveMenuItem.setAccelerator(KeyStroke.getKeyStroke('S',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke('S',KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
printMenuItem.setAccelerator(KeyStroke.getKeyStroke('P',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));




fileMenu=new JMenu("File");
fileMenu.add(newMenuItem);
fileMenu.add(newWindowMenuItem);
fileMenu.add(openMenuItem);
fileMenu.add(saveMenuItem);
fileMenu.add(saveAsMenuItem);
fileMenu.add(pageSetupMenuItem);
fileMenu.add(printMenuItem);
fileMenu.add(exitMenuItem);

undoMenuItem=new JMenuItem("Undo");
cutMenuItem=new JMenuItem("Cut");
copyMenuItem=new JMenuItem("Copy");
pasteMenuItem=new JMenuItem("Paste");
deleteMenuItem=new JMenuItem("Delete");
findMenuItem=new JMenuItem("Find...");
findNextMenuItem=new JMenuItem("Find Next");
findPreviousMenuItem=new JMenuItem("Find Previous");
replaceMenuItem=new JMenuItem("Replace");
goToMenuItem=new JMenuItem("Go To...");
selectAllMenuItem=new JMenuItem("Select All");

undoMenuItem.setAccelerator(KeyStroke.getKeyStroke('Z',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
cutMenuItem.setAccelerator(KeyStroke.getKeyStroke('X',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
copyMenuItem.setAccelerator(KeyStroke.getKeyStroke('C',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke('V',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
findMenuItem.setAccelerator(KeyStroke.getKeyStroke('F',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
findNextMenuItem.setAccelerator(KeyStroke.getKeyStroke("F3"));
findPreviousMenuItem.setAccelerator(KeyStroke.getKeyStroke("shift F3"));
replaceMenuItem.setAccelerator(KeyStroke.getKeyStroke('H',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
goToMenuItem.setAccelerator(KeyStroke.getKeyStroke('G',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke('A',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
 

editMenu=new JMenu("Edit");
editMenu.add(undoMenuItem);
editMenu.add(cutMenuItem);
editMenu.add(copyMenuItem);
editMenu.add(pasteMenuItem);
editMenu.add(deleteMenuItem);
editMenu.add(findMenuItem);
editMenu.add(findNextMenuItem);
editMenu.add(findPreviousMenuItem);
editMenu.add(replaceMenuItem);
editMenu.add(goToMenuItem);
editMenu.add(selectAllMenuItem);


wordWrapMenuItem=new JCheckBoxMenuItem("Word Wrap");
fontMenuItem=new JMenuItem("Font");

formatMenu=new JMenu("Format");
formatMenu.add(wordWrapMenuItem);
formatMenu.add(fontMenuItem);

statusMenuItem=new JCheckBoxMenuItem("Status");
zoomInMenuItem=new JMenuItem("");
zoomOutMenuItem=new JMenuItem("");

zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, KeyEvent.CTRL_DOWN_MASK));
zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, KeyEvent.CTRL_DOWN_MASK));



zoomMenu=new JMenu("Zoom");
zoomMenu.add(zoomInMenuItem);
zoomMenu.add(zoomOutMenuItem);
viewMenu=new JMenu("View");
viewMenu.add(zoomMenu);
viewMenu.add(statusMenuItem);

helpMenu=new JMenu("Help");

menuBar=new JMenuBar();
menuBar.add(fileMenu);
menuBar.add(editMenu);
menuBar.add(formatMenu);
menuBar.add(viewMenu);
menuBar.add(helpMenu);
setJMenuBar(menuBar);


}
private void initComponents()
{
counter=new Counter();
counter.i=0;
textArea=new JTextArea();
fontChooser=new FontChooser(this);
scrollPane=new JScrollPane(textArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
container=getContentPane();
statusbarPanel=new JPanel(new BorderLayout());
undoManager=new UndoManager();
searchManager=new SearchManager();
fileHandler=new FileHandler(this,textArea,fileName);
logoIcon=new ImageIcon(this.getClass().getResource("/icons/icon.png"));
}
private void addEventListeners()
{


textArea.addCaretListener((ev)->{
String selectedText=textArea.getSelectedText();
cutMenuItem.setEnabled(selectedText!=null);
copyMenuItem.setEnabled(selectedText!=null && (!selectedText.isEmpty()));
deleteMenuItem.setEnabled(selectedText!=null);
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


newMenuItem.addActionListener((ev)->{
boolean success=fileHandler.askToSaveBeforeOpeningNewFile(counter,isTextChanged);
if(success)isTextChanged=false;
});


newWindowMenuItem.addActionListener(ev->{
openNewWindow();
});
	
openMenuItem.addActionListener(ev->{
if(isTextChanged) if(fileHandler.askToSaveBeforeOpenNewFile(counter)) return;
boolean success=fileHandler.openFilePrompt();

if(success==false) return;
fileHandler.openFile(counter);
//originalText=textArea.getText();
isTextChanged=false;
Notepad.this.setTitle(fileHandler.getDisplayFileName()+"- My Notepad");

});


exitMenuItem.addActionListener((ev)->{
if(isTextChanged) 
{
fileHandler.askToSaveBeforeClose(counter);
}
});

cutMenuItem.addActionListener((ev)->{
textArea.cut();
});
copyMenuItem.addActionListener((ev)->{
textArea.copy();
});
pasteMenuItem.addActionListener((ev)->{
textArea.paste();
});
deleteMenuItem.addActionListener(ev->{
int start=textArea.getSelectionStart();
int end=textArea.getSelectionEnd();
if(start!=end)//if there's a selection
{
textArea.replaceRange("",start,end);
}
});



wordWrapMenuItem.addActionListener(ev->{
boolean isSelected=wordWrapMenuItem.isSelected();
textArea.setLineWrap(isSelected);
textArea.setWrapStyleWord(isSelected);
textArea.setCaretPosition(0);
});

fontMenuItem.addActionListener(ev->{
fontChooser.setVisible(true);
Font chosenFont=fontChooser.getSelectedFont();
if(chosenFont!=null) {
textArea.setFont(chosenFont);
}
});



statusMenuItem.addActionListener(ev->{
boolean isSelected=statusMenuItem.isSelected();
if(isSelected)statusbarPanel.setVisible(true);
else statusbarPanel.setVisible(false);
});


zoomInMenuItem.addActionListener(ev->{
zoomIn();
});
zoomOutMenuItem.addActionListener(ev->{
if(fontSize>minFontSize)
{
fontSize-=2;//decrement font size 
Font selectedFont=textArea.getFont();
textArea.setFont(new Font(selectedFont.getName(),selectedFont.getStyle(),fontSize));
}
});



}
private void zoomIn()
{
if(fontSize<maxFontSize)
{
fontSize+=2;//increment font size 
Font selectedFont=textArea.getFont();
textArea.setFont(new Font(selectedFont.getName(),selectedFont.getStyle(),fontSize));
}
}
private void setupLayout()
{
//setTitle("My Notepad");
//Image notepadIcon=Toolkit.getDefaultToolkit().getImage("images/icon.png");


setIconImage(logoIcon.getImage());
container.setLayout(new BorderLayout());

Font selectedFont=fontChooser.getSelectedFont();
textArea.setFont(selectedFont);
fontSize=selectedFont.getSize();
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

statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
statusbarPanel.add(statusLabel,BorderLayout.WEST);
container.add(scrollPane,BorderLayout.CENTER);	
container.add(statusbarPanel,BorderLayout.SOUTH);


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
initMenus();
addEventListeners();
setupLayout();
isTextChanged=false;






undoMenuItem.setEnabled(false);
cutMenuItem.setEnabled(false);
copyMenuItem.setEnabled(false);
pasteMenuItem.setEnabled(false);

//removing default behaviour of system of ctrl + H as backspace
textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("control H"),"none");


clipBoardChecker=new javax.swing.Timer(500,(ev)->{
pasteMenuItem.setEnabled(isClipboardAvailable());
});

clipBoardChecker.start();


textArea.getDocument().addUndoableEditListener(ev->{
undoManager.addEdit(ev.getEdit());
undoMenuItem.setEnabled(undoManager.canUndo());
}
);

undoMenuItem.addActionListener(ev->{
if(undoManager.canUndo())
{
undoManager.undo();
undoMenuItem.setEnabled(undoManager.canUndo());
}
});


replaceMenuItem.addActionListener(ev->{
JDialog replaceDialog=new JDialog(Notepad.this,"Replace",false);
//adding escape key for closing the dialog
replaceDialog.getRootPane().registerKeyboardAction(ef->
replaceDialog.dispose(),KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),JComponent.WHEN_IN_FOCUSED_WINDOW
);
replaceDialog.setSize(415,200);

replaceDialog.setLayout(null);
JPanel mainPanel=new JPanel();
mainPanel.setLayout(null);
mainPanel.setBounds(0,0,295,250);
JLabel findWhatLabel=new JLabel("Find what : ");
findWhatLabel.setBounds(10,10,80,20);
JTextField findField=new JTextField();
findField.setBounds(90,10,200,20);
JLabel replaceWithLabel=new JLabel("Replace with : ");
replaceWithLabel.setBounds(10,40,80,20);
JTextField replaceField=new JTextField();
replaceField.setBounds(90,40,200,20);
mainPanel.add(findWhatLabel);
mainPanel.add(findField);
mainPanel.add(replaceWithLabel);
mainPanel.add(replaceField);
replaceDialog.add(mainPanel);

JPanel buttonPanel=new JPanel();
buttonPanel.setLayout(null);
buttonPanel.setBounds(305,10,100,150);
JButton findNextButton=new JButton("Find Next");
findNextButton.setBounds(5,0,85,20);
JButton replaceButton=new JButton("Replace");
replaceButton.setBounds(5,30,85,20);
JButton replaceAllButton=new JButton("Replace All");
replaceAllButton.setBounds(5,30*2,85,20);
JButton cancelButton=new JButton("Cancel");
cancelButton.setBounds(5,30*3,85,20);

buttonPanel.add(findNextButton);
buttonPanel.add(replaceButton);
buttonPanel.add(replaceAllButton);
buttonPanel.add(cancelButton);

replaceDialog.add(buttonPanel);

replaceDialog.getRootPane().setDefaultButton(findNextButton);


JCheckBox matchCaseCheckBox=new JCheckBox("Match Case");
matchCaseCheckBox.setBounds(10,40+65,100,20);
JCheckBox wrapAroundCheckBox=new JCheckBox("Wrap Around");
wrapAroundCheckBox.setBounds(10,40+90,100,20);

mainPanel.add(matchCaseCheckBox);
mainPanel.add(wrapAroundCheckBox);


Runnable handleButtonPanel=()->{
String text=findField.getText();
if(text.length()!=0) 
{
findNextButton.setEnabled(true);
replaceButton.setEnabled(true);
replaceAllButton.setEnabled(true);
}
else 
{
findNextButton.setEnabled(false);
replaceButton.setEnabled(false);
replaceAllButton.setEnabled(false);
}
};

findField.getDocument().addDocumentListener(
new DocumentListener(){
@Override
public void insertUpdate(DocumentEvent de)
{
handleButtonPanel.run();
}
@Override
public void removeUpdate(DocumentEvent de)
{
handleButtonPanel.run();
}
@Override
public void changedUpdate(DocumentEvent de)
{
handleButtonPanel.run();
}
});




findField.addFocusListener(new FocusAdapter(){
public void focusGained(FocusEvent fe)
{
findField.selectAll();
}
});

replaceField.addFocusListener(new FocusAdapter(){
public void focusGained(FocusEvent fe)
{
replaceField.selectAll();
}
});



findNextButton.addActionListener(e->{
String searchText=findField.getText();
Notepad.this.findPreviousSearchedText=searchText;
boolean matchCase=matchCaseCheckBox.isSelected();
boolean wrapAround=wrapAroundCheckBox.isSelected();
boolean found=searchManager.performFind(searchText,matchCase,wrapAround,false,true);//passing false for up direction
});

replaceButton.addActionListener(e->{
String findText=findField.getText();
String replaceText=replaceField.getText();
replacePreviousSearchedText=replaceText;

if(selectedTextStartIndex==-1 && selectedTextEndIndex==-1)
{
boolean matchCase=matchCaseCheckBox.isSelected();
boolean wrapAround=wrapAroundCheckBox.isSelected();
boolean found=searchManager.performFind(findText,matchCase,wrapAround,false,true);//passing false for up direction
if(found==false)return;
replaceButton.requestFocusInWindow();
}
textArea.replaceRange(replaceText,selectedTextStartIndex,selectedTextEndIndex);
textArea.setCaretPosition(selectedTextStartIndex+replaceText.length());
selectedTextStartIndex=-1;
selectedTextEndIndex=-1;
});

replaceAllButton.addActionListener(e->{
textArea.setCaretPosition(0);
String findText=findField.getText();
boolean matchCase=matchCaseCheckBox.isSelected();
boolean wrapAround=wrapAroundCheckBox.isSelected();
boolean found=searchManager.performFind(findText,matchCase,wrapAround,false,false);//passing false for up direction, 5th parameter is for enabling text selection
if(found==false) return;
textArea.setSelectionStart(0);
textArea.setSelectionEnd(0);
String text=textArea.getText();
String replaceText=replaceField.getText();
if(matchCase)text=text.replace(findText,replaceText);
else 
{
text=text.replaceAll(findText,replaceText);
}
textArea.setText(text);
textArea.setCaretPosition(0);
}
);

cancelButton.addActionListener(e->{
replaceDialog.dispose();
});

int parentX=Notepad.this.getX();
int parentY=Notepad.this.getY();
int parentWidth=Notepad.this.getWidth();
int parentHeight=Notepad.this.getHeight();

int dialogX=parentX+(parentWidth-replaceDialog.getWidth())/8;
int dialogY=parentY+(parentHeight-replaceDialog.getHeight())/4;

replaceDialog.setLocation(dialogX,dialogY);

//replaceDialog.setLocationRelativeTo(Notepad.this);
replaceDialog.setVisible(true);


findField.setText(findPreviousSearchedText);
replaceField.setText(replacePreviousSearchedText);

String selectedText=textArea.getSelectedText();
if(selectedText!=null && !selectedText.isEmpty())
{
selectedTextStartIndex=textArea.getSelectionStart();
selectedTextEndIndex=textArea.getSelectionEnd();
findField.setText(selectedText);
findPreviousSearchedText=selectedText;
findField.selectAll();
}


if(findPreviousSearchedText.trim().length()==0) 
{
findNextButton.setEnabled(false);
replaceButton.setEnabled(false);
replaceAllButton.setEnabled(false);
}
});


findMenuItem.addActionListener(ev->{
JDialog findDialog=new JDialog(Notepad.this,"Find",false);
//registerKeyboardAction binds the ESC key to an action
//When the ESC key is pressed, the dialog is sent a WINDOW_CLOSING event, 
//which will trigger the windowClosing method of any attached WindowListener
findDialog.getRootPane().registerKeyboardAction(
ef->findDialog.dispatchEvent(new WindowEvent(findDialog,WindowEvent.WINDOW_CLOSING)),
KeyStroke.getKeyStroke("ESCAPE"),
JComponent.WHEN_IN_FOCUSED_WINDOW);

findDialogReset=false;
findDialog.setSize(400,160);

findDialog.setLayout(null);
JPanel mainPanel=new JPanel();
mainPanel.setLayout(null);
mainPanel.setBounds(0,0,290,160);
JLabel findWhatLabel=new JLabel("Find What : ");
findWhatLabel.setBounds(10,10,70,20);
JTextField findField=new JTextField();
findField.setBounds(90,10,200,20);
mainPanel.add(findWhatLabel);
mainPanel.add(findField);
findDialog.add(mainPanel);
//findDialog.add(findWhatLabel);
//findDialog.add(findField);

JPanel buttonPanel=new JPanel();
buttonPanel.setLayout(null);
buttonPanel.setBounds(295,10,90,50);
JButton findNextButton=new JButton("Find Next");
findNextButton.setBounds(5,0,80,20);
JButton cancelButton=new JButton("Cancel");
cancelButton.setBounds(5,30,80,20);
buttonPanel.add(findNextButton);
buttonPanel.add(cancelButton);
findDialog.add(buttonPanel);

findDialog.getRootPane().setDefaultButton(findNextButton);


JCheckBox matchCaseCheckBox=new JCheckBox("Match Case");
matchCaseCheckBox.setBounds(10,65,100,20);
JCheckBox wrapAroundCheckBox=new JCheckBox("Wrap Around");
wrapAroundCheckBox.setBounds(10,90,100,20);

mainPanel.add(matchCaseCheckBox);
mainPanel.add(wrapAroundCheckBox);
//findDialog.add(matchCaseCheckBox);
//findDialog.add(wrapAroundCheckBox);

JPanel directionPanel=new JPanel();
directionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
directionPanel.setBounds(143,40,150,50);
directionPanel.setBorder(BorderFactory.createTitledBorder("Direction"));
//JLabel directionLabel=new JLabel("Direction:");
//directionLabel.setBounds(10,80,60,20);
JRadioButton upRadioButton=new JRadioButton("Up");
upRadioButton.setBounds(20+30+10,80,40,20);
JRadioButton downRadioButton=new JRadioButton("Down",true);
downRadioButton.setBounds(10+30+20+30+10,80,60,20);


ButtonGroup directionGroup=new ButtonGroup();
directionGroup.add(upRadioButton);
directionGroup.add(downRadioButton);
//findDialog.add(directionLabel);
directionPanel.add(upRadioButton);
directionPanel.add(downRadioButton);
//findDialog.add(directionPanel);
mainPanel.add(directionPanel);


findField.getDocument().addDocumentListener(
new DocumentListener(){
@Override
public void insertUpdate(DocumentEvent de)
{
String text=findField.getText();
if(text.length()!=0) findNextButton.setEnabled(true);
else findNextButton.setEnabled(false);
}
@Override
public void removeUpdate(DocumentEvent de)
{
String text=findField.getText();
if(text.length()!=0) findNextButton.setEnabled(true);
else findNextButton.setEnabled(false);
}
@Override
public void changedUpdate(DocumentEvent de)
{
String text=findField.getText();
if(text.length()!=0) findNextButton.setEnabled(true);
else findNextButton.setEnabled(false);
}
});




findField.addFocusListener(new FocusAdapter(){
public void focusGained(FocusEvent fe)
{
findField.selectAll();
}
});


findNextButton.addActionListener(e->{
String searchText=findField.getText();
boolean matchCase=matchCaseCheckBox.isSelected();
boolean wrapAround=wrapAroundCheckBox.isSelected();
boolean directionUp=upRadioButton.isSelected();
boolean b=searchManager.performFind(searchText,matchCase,wrapAround,directionUp,true);
});

cancelButton.addActionListener(e->{
findDialogReset=true;
findDialog.dispose();
});

findDialog.addWindowListener(new WindowAdapter(){
@Override
public void windowClosing(WindowEvent e)
{
findDialogReset=true;
}
});


int parentX=Notepad.this.getX();
int parentY=Notepad.this.getY();
int parentWidth=Notepad.this.getWidth();
int parentHeight=Notepad.this.getHeight();

int dialogX=parentX+(parentWidth-findDialog.getWidth())/8;
int dialogY=parentY+(parentHeight-findDialog.getHeight())/4;

findDialog.setLocation(dialogX,dialogY);
//findDialog.setLocationRelativeTo(Notepad.this);
findDialog.setVisible(true);


findField.setText(findPreviousSearchedText);


String selectedText=textArea.getSelectedText();
if(selectedText!=null && !selectedText.isEmpty())
{
findField.setText(selectedText);
findPreviousSearchedText=selectedText;
findField.selectAll();
}


if(findPreviousSearchedText.trim().length()==0) findNextButton.setEnabled(false);
/*
NO NEED WILL REMOVE LATER
if(findField.getText().trim().length()!=0)
{
//SwingUtilities.invokeLater(()->findNextButton.requestFocusInWindow());
}
*/	
});

findNextMenuItem.addActionListener(ev->{
boolean b=searchManager.performFind(findPreviousSearchedText,false,false,false,true);
});

findPreviousMenuItem.addActionListener(ev->{
boolean b=searchManager.performFind(findPreviousSearchedText,false,false,true,true);
});


selectAllMenuItem.addActionListener(ev->{
textArea.selectAll();
});


goToMenuItem.addActionListener(ev->{
JDialog goToDialog=new JDialog(Notepad.this,"Go To Line",false);
goToDialog.getRootPane().registerKeyboardAction(ef->
goToDialog.dispose(),KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),JComponent.WHEN_IN_FOCUSED_WINDOW
);

goToDialog.setSize(275,150);
goToDialog.setLayout(null);

JLabel lineLabel=new JLabel("Line number:");
lineLabel.setBounds(7,5,200,25);
JTextField lineField=new JTextField();
lineField.setBounds(5,30,240,25);
JButton goToButton=new JButton("Go to");
goToButton.setBounds(80,70,80,25);
JButton cancelButton=new JButton("Cancel");
cancelButton.setBounds(165,70,80,25);
JToolTip errorToolTip=new JToolTip();
//Point location=lineField.getLocationOnScreen();
//errorToolTip.setBounds(location.x,location.y+lineField.getHeight(),100,100);
errorToolTip.setBounds(20,60,200,20);
errorToolTip.setTipText("UNACCEPTABLE CHARACTER");
errorToolTip.setBackground(Color.WHITE);
errorToolTip.setForeground(Color.RED);
errorToolTip.setVisible(false);
goToDialog.add(lineLabel);
goToDialog.add(lineField);
goToDialog.add(goToButton);
goToDialog.add(cancelButton);
int parentX=Notepad.this.getX();
int parentY=Notepad.this.getY();
int parentWidth=Notepad.this.getWidth();
int parentHeight=Notepad.this.getHeight();

int dialogX=parentX+(parentWidth-goToDialog.getWidth())/8;
int dialogY=parentY+(parentHeight-goToDialog.getHeight())/4;

goToDialog.setLocation(dialogX,dialogY);

//goToDialog.setLocationRelativeTo(Notepad.this);
goToDialog.setVisible(true);
goToDialog.getRootPane().setDefaultButton(goToButton);
goToButton.addActionListener(e->{
try
{
String input=lineField.getText().trim();
int lineNumber=Integer.parseInt(input);

//validate the line number
int totalLines=textArea.getLineCount();
if(lineNumber<1 || lineNumber>totalLines)
{
JOptionPane.showMessageDialog(Notepad.this,"The line number is beyond the total line number","My Notepad - Goto Line",JOptionPane.ERROR_MESSAGE);
return;
}
//calculate postion and move caret
int lineStartOffset=textArea.getLineStartOffset(lineNumber-1);//Line number is 1-based
textArea.setCaretPosition(lineStartOffset);
goToDialog.dispose();
}catch(NumberFormatException numberFormatException)
{
//show a tooltip or a popup window
JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
LogException.log(numberFormatException);
}
catch(BadLocationException badLocationException)
{
JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
LogException.log(badLocationException);
}
});
cancelButton.addActionListener(e->{
goToDialog.dispose();
});

lineField.addFocusListener(new FocusAdapter(){
@Override
public void focusGained(FocusEvent fe)
{
if(Notepad.this.emptyLineField)
{
Notepad.this.emptyLineField=false;
lineField.setText("");	
}
}
});


lineField.getDocument().addDocumentListener(new DocumentListener(){
@Override
public void insertUpdate(DocumentEvent de)
{
char c=lineField.getText().charAt(0);
if(!(c>=48 && c<=57))
{
emptyLineField=true;
JOptionPane.showMessageDialog(goToDialog,"Unacceptable Character\nOnly numbers are allowed","My Notepad - Goto Line",JOptionPane.ERROR_MESSAGE);
return;
}
}
@Override
public void removeUpdate(DocumentEvent de)
{
}
@Override
public void changedUpdate(DocumentEvent de)
{
}
});

});
fileHandler.openFile(counter);	//opening file and appending in textArea
saveMenuItem.addActionListener(ev->{
boolean success=true;
if(fileHandler.getDisplayFileName()!=null)
{
String s=counter.originalText;
success=fileHandler.saveFile(counter);
}
else
{
success=fileHandler.saveAs(counter);
}
if(success)
{
// isTextChanged equals true after saving the file make it false because now the file is saved
this.isTextChanged=false;
if(fileHandler.getDisplayFileName()!=null)setTitle(fileHandler.getDisplayFileName()+" - My Notepad");
}
});
saveAsMenuItem.addActionListener(ev->{


if(fileHandler.saveAs(counter))
{
// isTextChanged equals true after saving the file make it false because now the file is saved
this.isTextChanged=false;
if(fileHandler.getDisplayFileName()!=null)setTitle(fileHandler.getDisplayFileName()+" - My Notepad");
}

});

textArea.getDocument().addDocumentListener(
new DocumentListener(){
@Override
public void insertUpdate(DocumentEvent de)
{
if(counter.i==0) firstTime=false;
if(firstTime==false) updateTitle();
counter.i--;

}
@Override
public void removeUpdate(DocumentEvent de)
{
updateTitle();
}
@Override
public void changedUpdate(DocumentEvent de)
{
updateTitle();
}
});

addWindowListener(new WindowAdapter(){
@Override
public void windowClosing(WindowEvent we)
{
boolean close=true;
if(isTextChanged)close=fileHandler.askToSaveBeforeClose(counter);
if(close)
{
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
private void openNewWindow()
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
try
{
if(fileHandler.getDisplayFileName()!=null )
{
if(counter.originalText.equals(textArea.getText())==false) 
{
isTextChanged=true;
setTitle("*"+fileHandler.getDisplayFileName()+" - My Notepad");
}
else 
{

setTitle(fileHandler.getDisplayFileName()+" - My Notepad");
isTextChanged=false;
}
}
else 
{
if(counter.originalText.equals(textArea.getText())==false) 
{
setTitle("*Untitled - My Notepad");
isTextChanged=true;
}
else
{
setTitle("Untitled - My Notepad");
isTextChanged=false;
}
}
}catch(Exception exception)
{
JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
LogException.log(exception);
}
}


class SearchManager
{

public boolean performFind(String searchText,boolean matchCase,boolean wrapAround,boolean directionUp,boolean highlight)
{
String text=textArea.getText();
if(!matchCase)
{
searchText=searchText.toLowerCase();
text=text.toLowerCase();
}
int pos=0;
int index;
if(directionUp)
{
//int k=textArea.getCaretPosition()-searchText.length();
//if(k>=0)textArea.setCaretPosition(k);
//use the start of the current selection (or caret position if nothing is selected)
pos=textArea.getSelectedText()!=null?textArea.getSelectionStart():textArea.getCaretPosition();
index=text.lastIndexOf(searchText,pos-1);
if(index==-1 && wrapAround)
{
index=text.lastIndexOf(searchText,text.length());//for starting finding from last
}
}else
{
//int k=textArea.getCaretPosition()+searchText.length();
//if(k<text.length())textArea.setCaretPosition(k);

//textArea.setCaretPosition(textArea.getCaretPosition()+searchText.length());
pos=textArea.getCaretPosition();
index=text.indexOf(searchText,pos);
if(index==-1 && wrapAround)
{
index=text.indexOf(searchText,0);
}
}
if(index!=-1)
{
int s=index;
int end=index+searchText.length();
findPreviousStartIndex=s;
findPreviousEndIndex=end;
Notepad.this.selectedTextStartIndex=s;
Notepad.this.selectedTextEndIndex=end;
int l=index;
int y=searchText.length();
SwingUtilities.invokeLater(()->{
if(highlight)
{
textArea.setSelectionStart(s);
textArea.setSelectionEnd(end);
textArea.requestFocusInWindow();
}
});

if(highlight)
{
textArea.setCaretPosition(l+(directionUp?0:y));
textArea.getCaret().setSelectionVisible(true);
}


//if(highlight)textArea.setCaretPosition(index+(directionUp?0:searchText.length()));

}else
{
if(findPreviousStartIndex!=-1 && findPreviousSearchedText.equals(searchText) && highlight && findDialogReset==false)
{
//textArea.select(findPreviousStartIndex,findPreviousEndIndex);
textArea.setSelectionStart(findPreviousStartIndex);
textArea.setSelectionEnd(findPreviousEndIndex);

textArea.requestFocusInWindow();
}
else
{
findPreviousStartIndex=-1;
findPreviousEndIndex=-1;
}
JOptionPane.showMessageDialog(Notepad.this,"Not Found","My Notepad",JOptionPane.INFORMATION_MESSAGE);
Notepad.this.findPreviousSearchedText=searchText;
return false;
}
Notepad.this.findPreviousSearchedText=searchText;
return true;
}
}

}
