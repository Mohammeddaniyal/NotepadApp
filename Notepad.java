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
private FontChooser fontChooser;
private int fontSize;
private final int maxFontSize=60;
private final int minFontSize=8;
private String findPreviousSearchedText="";
private int findPreviousStartIndex=-1;
private int findPreviousEndIndex=-1;
private int selectedTextStartIndex=-1;
private int selectedTextEndIndex=-1;
private boolean emptyLineField=false;
private int i=0;
private static int frameCount=0;
private boolean firstTime=true;
private String fileName;
private RandomAccessFile randomAccessFile;
private File file;
private boolean isTextChanged;
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
private JMenuItem statusMenuItem;
private JMenuItem helpMenuItem,aboutMenuItem;
private JTextArea textArea;
private JScrollPane scrollPane;
private Container container;
public Notepad(String fileName)
{
frameCount++;
try
{
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
}catch(Exception exception)
{

}

isTextChanged=false;
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



wordWrapMenuItem.addActionListener(ev->{
boolean isSelected=wordWrapMenuItem.isSelected();
textArea.setLineWrap(isSelected);
textArea.setWrapStyleWord(isSelected);
});
fontMenuItem.addActionListener(ev->{
fontChooser.setVisible(true);
Font chosenFont=fontChooser.getSelectedFont();
if(chosenFont!=null) {
System.out.println("Selected font : "+chosenFont);
textArea.setFont(chosenFont);
}
});

statusMenuItem=new JMenuItem("Status");
zoomInMenuItem=new JMenuItem("Zoom In");
zoomOutMenuItem=new JMenuItem("Zoom Out");
zoomMenu=new JMenu("Zoom");
zoomMenu.add(zoomInMenuItem);
zoomMenu.add(zoomOutMenuItem);
viewMenu=new JMenu("View");
viewMenu.add(zoomMenu);
viewMenu.add(statusMenuItem);



//zoomInMenuItem.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control PLUS"),"zoom in");

//zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke("control PLUS"));
//zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke("control MINUS"));

zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,KeyEvent.CTRL_DOWN_MASK));
zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,KeyEvent.CTRL_DOWN_MASK));

zoomInMenuItem.addActionListener(ev->{
if(fontSize<maxFontSize)
{
fontSize+=2;//increment font size 
Font selectedFont=textArea.getFont();
textArea.setFont(new Font(selectedFont.getName(),selectedFont.getStyle(),fontSize));
}
});
zoomOutMenuItem.addActionListener(ev->{
if(fontSize>minFontSize)
{
fontSize-=2;//decrement font size 
Font selectedFont=textArea.getFont();
textArea.setFont(new Font(selectedFont.getName(),selectedFont.getStyle(),fontSize));
}
});

helpMenu=new JMenu("Help");

menuBar=new JMenuBar();
menuBar.add(fileMenu);
menuBar.add(editMenu);
menuBar.add(formatMenu);
menuBar.add(viewMenu);
menuBar.add(helpMenu);
setJMenuBar(menuBar);
setTitle("My Notepad");
Image notepadIcon=Toolkit.getDefaultToolkit().getImage("images/icon.png");
setIconImage(notepadIcon);
container=getContentPane();
textArea=new JTextArea();
fontChooser=new FontChooser(this);
Font selectedFont=fontChooser.getSelectedFont();
textArea.setFont(selectedFont);
fontSize=selectedFont.getSize();
System.out.println(fontSize);
this.fileName=fileName;
scrollPane=new JScrollPane(textArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
int width=800;
int height=600;
setSize(width,height);
int x=(d.width/2)-(width/2);
int y=(d.height/2)-(height/2);
setSize(width,height);
setLocation(x,y);
setVisible(true);
container.add(scrollPane);	
undoMenuItem.setEnabled(false);
cutMenuItem.setEnabled(false);
copyMenuItem.setEnabled(false);
pasteMenuItem.setEnabled(false);

//removing default behaviour of system of ctrl + H as backspace
textArea.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("control H"),"none");



textArea.addCaretListener((ev)->{
String selectedText=textArea.getSelectedText();
cutMenuItem.setEnabled(selectedText!=null);
copyMenuItem.setEnabled(selectedText!=null && (!selectedText.isEmpty()));
deleteMenuItem.setEnabled(selectedText!=null);
});
javax.swing.Timer clipBoardChecker=new javax.swing.Timer(500,(ev)->{
pasteMenuItem.setEnabled(isClipboardAvailable());
});
clipBoardChecker.start();
newMenuItem.addActionListener((ev)->{
System.out.println(isTextChanged);
if(isTextChanged)
{
askToSaveBeforeOpeningNewFile();
}
});

UndoManager undoManager=new UndoManager();
textArea.getDocument().addUndoableEditListener(ev->{
undoManager.addEdit(ev.getEdit());
undoMenuItem.setEnabled(undoManager.canUndo());
});
undoMenuItem.addActionListener(ev->{
if(undoManager.canUndo())
{
undoManager.undo();
undoMenuItem.setEnabled(undoManager.canUndo());
}
});


newWindowMenuItem.addActionListener(ev->{
openNewWindow();
});
	
openMenuItem.addActionListener(ev->{
if(askToSaveBeforeOpenNewFile()) return;

JFileChooser fileChooser=new JFileChooser();
fileChooser.setCurrentDirectory(new File("."));
int result=fileChooser.showOpenDialog(null);
if(result==JFileChooser.APPROVE_OPTION)
{
File selectedFile=fileChooser.getSelectedFile();
if(selectedFile.isDirectory()) return;
textArea.setText("");
//Notepad.this.randomAccessFile.close();
Notepad.this.file=selectedFile;
Notepad.this.fileName=Notepad.this.file.getName();
try
{
Notepad.this.randomAccessFile=new RandomAccessFile(file,"rw");
}catch(IOException ioException)
{}
openFile();
setTitle(Notepad.this.fileName+" - My Notepad");
//done
isTextChanged=false;
}
});


exitMenuItem.addActionListener((ev)->{
askToSaveBeforeClose();
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



findNextButton.addActionListener(e->{
String searchText=findField.getText();
Notepad.this.findPreviousSearchedText=searchText;
boolean matchCase=matchCaseCheckBox.isSelected();
boolean wrapAround=wrapAroundCheckBox.isSelected();
boolean found=performFind(searchText,matchCase,wrapAround,false,true);//passing false for up direction
System.out.printf("(%d,%d)",selectedTextStartIndex,selectedTextEndIndex);
});

replaceButton.addActionListener(e->{
String findText=findField.getText();
String replaceText=replaceField.getText();
System.out.println(replaceText);
System.out.printf("(%d,%d)",selectedTextStartIndex,selectedTextEndIndex);

if(selectedTextStartIndex==-1 && selectedTextEndIndex==-1)
{
boolean matchCase=matchCaseCheckBox.isSelected();
boolean wrapAround=wrapAroundCheckBox.isSelected();
boolean found=performFind(findText,matchCase,wrapAround,false,true);//passing false for up direction
if(found==false)return;
replaceButton.requestFocusInWindow();
}
System.out.printf("(%d,%d)",selectedTextStartIndex,selectedTextEndIndex);
textArea.replaceRange(replaceText,selectedTextStartIndex,selectedTextEndIndex);
textArea.setCaretPosition(selectedTextStartIndex+replaceText.length());
System.out.println("Replaced");
selectedTextStartIndex=-1;
selectedTextEndIndex=-1;
});

replaceAllButton.addActionListener(e->{
textArea.setCaretPosition(0);
String findText=findField.getText();
boolean matchCase=matchCaseCheckBox.isSelected();
boolean wrapAround=wrapAroundCheckBox.isSelected();
boolean found=performFind(findText,matchCase,wrapAround,false,false);//passing false for up direction, 5th parameter is for enabling text selection
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


replaceDialog.setLocationRelativeTo(Notepad.this);
replaceDialog.setVisible(true);


findField.setText(findPreviousSearchedText);


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
findDialog.getRootPane().registerKeyboardAction(ef->
findDialog.dispose(),KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),JComponent.WHEN_IN_FOCUSED_WINDOW
);
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
Notepad.this.findPreviousSearchedText=searchText;
boolean matchCase=matchCaseCheckBox.isSelected();
boolean wrapAround=wrapAroundCheckBox.isSelected();
boolean directionUp=upRadioButton.isSelected();
boolean b=performFind(searchText,matchCase,wrapAround,directionUp,true);
});

cancelButton.addActionListener(e->{

findDialog.dispose();
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
boolean b=performFind(findPreviousSearchedText,false,false,false,true);
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
//goToDialog.add(errorToolTip);
goToDialog.setLocationRelativeTo(Notepad.this);
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
}
catch(BadLocationException badLocationException)
{
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

openFile();	//opening file and appending in textArea


saveMenuItem.addActionListener(ev->{
if(randomAccessFile!=null)
{
saveFile();
}
else
{
saveAs();
}
});
saveAsMenuItem.addActionListener(ev->{
saveAs();
});

textArea.getDocument().addDocumentListener(
new DocumentListener(){
@Override
public void insertUpdate(DocumentEvent de)
{
if(i==0) firstTime=false;
if(firstTime==false)
{
isTextChanged=true;
}
if(i!=0)i--;
if(i==0)
{
//document is loaded
firstTime=false;
}
}
@Override
public void removeUpdate(DocumentEvent de)
{
isTextChanged=true;
}
@Override
public void changedUpdate(DocumentEvent de)
{
isTextChanged=true;
}
});

addWindowListener(new WindowAdapter(){
@Override
public void windowClosing(WindowEvent we)
{
askToSaveBeforeClose();
}
});

SwingUtilities.invokeLater(()->{
textArea.requestFocusInWindow();
});

}
private void askToSaveBeforeClose()
{
if(isTextChanged)
{
int choice;
String name="";
if(randomAccessFile!=null) name=Notepad.this.fileName;
else name="Untitled";

choice=JOptionPane.showConfirmDialog(Notepad.this,"Do you want to save changes to "+name+" ?","My Notepad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
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
closeFrame();
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
closeFrame();
}
else if(choice==JOptionPane.CANCEL_OPTION)
{
//do nothing in this dont close
setVisible(true);
}
}//change in text part done
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
closeFrame();
}
}

private boolean askToSaveBeforeOpenNewFile()
{
System.out.println(isTextChanged);
if(isTextChanged)
{

int choice;
String name="";
if(randomAccessFile!=null) name=Notepad.this.fileName;
else name="Untitled";

choice=JOptionPane.showConfirmDialog(Notepad.this,"Do you want to save changes to "+name+" ?","My Notepad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
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
}//change in text part done


return false;
}


private void askToSaveBeforeOpeningNewFile()
{
if(isTextChanged)
{
int choice;
String name="";
if(randomAccessFile!=null) name=Notepad.this.fileName;
else name="Untitled";

choice=JOptionPane.showConfirmDialog(Notepad.this,"Do you want to save changes to "+name+" ?","My Notepad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
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
}//change in text part done
else
{
//Exit without any confirmation changes
//do nothing
}
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
private void saveFile()
{
try
{
randomAccessFile.setLength(0);
randomAccessFile.writeBytes(textArea.getText());
}catch(IOException io)
{
}
isTextChanged=false;
}
private void saveAs()
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
setTitle(this.fileName+" -My Notepad");
isTextChanged=false;
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
setTitle(fileName+" -My Notepad");
}catch(IOException ioException)
{
}
}
}
private void openNewWindow()
{
SwingUtilities.invokeLater(()->{
Notepad newNotepad=new Notepad(null);
}
);
}
private void closeFrame()
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
private boolean performFind(String searchText,boolean matchCase,boolean wrapAround,boolean directionUp,boolean highlight)
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
int k=textArea.getCaretPosition()-searchText.length();
if(k>=0)textArea.setCaretPosition(k);
pos=textArea.getCaretPosition();
index=text.lastIndexOf(searchText,pos-1);
if(index==-1 && wrapAround)
{
index=text.lastIndexOf(searchText,text.length());//for starting finding from last
}
}else
{
textArea.setCaretPosition(textArea.getCaretPosition()+searchText.length());
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
System.out.printf("(%d,%d)",selectedTextStartIndex,selectedTextEndIndex);
SwingUtilities.invokeLater(()->{
if(highlight)
{
textArea.select(s,end);
textArea.requestFocusInWindow();
}
});
if(highlight)textArea.setCaretPosition(index+(directionUp?0:searchText.length()));
}else
{
JOptionPane.showMessageDialog(Notepad.this,"Not Found","My Notepad",JOptionPane.INFORMATION_MESSAGE);
return false;
}
return true;
}
private void openFile()
{
randomAccessFile=null;
if(fileName==null)
{
setTitle("Untitled"+" - My Notepad");
}
else
{
try
{
file=new File(fileName);
if(!file.exists())
{
int option=JOptionPane.showConfirmDialog(this,"Cannot find the "+fileName+" file.\n\nDo you want to create a new file?","My Notepad",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
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
closeFrame();
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
setTitle(fileName+"- My Notepad");
}

}
public static void main(String gg[])
{
Notepad n;
if(gg.length!=0) 
{
String fileName=gg[0];
n=new Notepad(fileName);
}
else
{
n=new Notepad(null);
}
}
}
