package com.thinking.machines.notepad.managers;
import com.thinking.machines.notepad.Notepad;
import com.thinking.machines.notepad.io.*;
import com.thinking.machines.notepad.models.*;
import com.thinking.machines.notepad.exceptions.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.undo.*;
import javax.swing.text.*;
import javax.swing.event.*;
public class EditMenuManager
{
private Notepad notepad;
private JTextArea textArea;
private JScrollPane scrollPane;
private JFrame fakeParent;
private JMenuBar menuBar; 
private Config config;
private FileHandler fileHandler;
private Notepad.Counter counter;
private UndoManager undoManager;
private SearchManager searchManager;
private JMenu editMenu;
private JMenuItem undoMenuItem,redoMenuItem;
private JMenuItem cutMenuItem,copyMenuItem,pasteMenuItem;
private JMenuItem deleteMenuItem,findMenuItem,findNextMenuItem,findPreviousMenuItem;
private JMenuItem replaceMenuItem,goToMenuItem,selectAllMenuItem;

private SearchContext searchContext;
public EditMenuManager(Notepad notepad,JTextArea textArea,JScrollPane scrollPane,JFrame fakeParent,JMenuBar menuBar,Config config,FileHandler fileHandler,Notepad.Counter counter,UndoManager undoManager)
{
this.notepad=notepad;
this.textArea=textArea;
this.scrollPane=scrollPane;
this.fakeParent=fakeParent;
this.menuBar=menuBar;
this.config=config;
this.fileHandler=fileHandler;
this.counter=counter;
this.undoManager=undoManager;
this.searchManager=new SearchManager();
searchContext=new SearchContext();
searchContext.findPreviousSearchedText=config.lastSearchedText;
initComponents();
addMenuItems();
addEventListeners();
bindShortcutKeys();
menuBar.add(editMenu);
}
private void initComponents()
{
undoMenuItem=new JMenuItem("Undo");
redoMenuItem=new JMenuItem("Redo");
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

editMenu=new JMenu("Edit");
}
private void addMenuItems()
{
editMenu.add(undoMenuItem);
editMenu.add(redoMenuItem);
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

undoMenuItem.setEnabled(false);
cutMenuItem.setEnabled(false);
copyMenuItem.setEnabled(false);
pasteMenuItem.setEnabled(false);



}
private void bindShortcutKeys()
{
undoMenuItem.setAccelerator(KeyStroke.getKeyStroke('Z',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
redoMenuItem.setAccelerator(KeyStroke.getKeyStroke('Y',Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
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

}
private void addEventListeners()
{
undoMenuItem.addActionListener(ev->{
if(undoManager.canUndo())
{
int oldPos=textArea.getCaretPosition();
undoManager.undo();
int newPos=textArea.getCaretPosition();
textArea.select(Math.min(oldPos,newPos),Math.max(oldPos,newPos));
redoMenuItem.setEnabled(true);
undoMenuItem.setEnabled(undoManager.canUndo());
}
});

redoMenuItem.addActionListener(ev->{
if(undoManager.canRedo())
{
int oldPos=textArea.getCaretPosition();
undoManager.redo();
int newPos=textArea.getCaretPosition();
textArea.select(Math.min(oldPos,newPos),Math.max(oldPos,newPos));
undoMenuItem.setEnabled(undoManager.canUndo());
redoMenuItem.setEnabled(undoManager.canRedo());
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



// replaceMenuItem action listener starts here
replaceMenuItem.addActionListener(ev->{
JDialog replaceDialog=new JDialog(notepad,"Replace",false);
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

matchCaseCheckBox.setSelected(config.matchCase);
wrapAroundCheckBox.setSelected(config.wrapAround);
matchCaseCheckBox.addActionListener(ev1->{
config.matchCase=matchCaseCheckBox.isSelected();
});

wrapAroundCheckBox.addActionListener(ev1->{
config.wrapAround=wrapAroundCheckBox.isSelected();
});



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
searchContext.findPreviousSearchedText=searchText;
config.lastSearchedText=searchContext.findPreviousSearchedText;
boolean matchCase=matchCaseCheckBox.isSelected();
boolean wrapAround=wrapAroundCheckBox.isSelected();
boolean found=searchManager.performFind(notepad,textArea,scrollPane,searchContext,searchText,matchCase,wrapAround,false,true);//passing false for up direction
});

replaceButton.addActionListener(e->{
String findText=findField.getText();
String replaceText=replaceField.getText();
searchContext.replacePreviousSearchedText=replaceText;

if(searchContext.selectedTextStartIndex==-1 && searchContext.selectedTextEndIndex==-1)
{
boolean matchCase=matchCaseCheckBox.isSelected();
boolean wrapAround=wrapAroundCheckBox.isSelected();
boolean found=searchManager.performFind(notepad,textArea,scrollPane,searchContext,findText,matchCase,wrapAround,false,true);//passing false for up direction
if(found==false)return;
replaceButton.requestFocusInWindow();
}
textArea.replaceRange(replaceText,searchContext.selectedTextStartIndex,searchContext.selectedTextEndIndex);
textArea.setCaretPosition(searchContext.selectedTextStartIndex+replaceText.length());
searchContext.selectedTextStartIndex=-1;
searchContext.selectedTextEndIndex=-1;
});

replaceAllButton.addActionListener(e->{
textArea.setCaretPosition(0);
String findText=findField.getText();
boolean matchCase=matchCaseCheckBox.isSelected();
boolean wrapAround=wrapAroundCheckBox.isSelected();
boolean found=searchManager.performFind(notepad,textArea,scrollPane,searchContext,findText,matchCase,wrapAround,false,false);//passing false for up direction, 5th parameter is for enabling text selection
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

int parentX=notepad.getX();
int parentY=notepad.getY();
int parentWidth=notepad.getWidth();
int parentHeight=notepad.getHeight();

int dialogX=parentX+(parentWidth-replaceDialog.getWidth())/8;
int dialogY=parentY+(parentHeight-replaceDialog.getHeight())/4;

replaceDialog.setLocation(dialogX,dialogY);

//replaceDialog.setLocationRelativeTo(notepad);
replaceDialog.setVisible(true);


findField.setText(searchContext.findPreviousSearchedText);
replaceField.setText(searchContext.replacePreviousSearchedText);

String selectedText=textArea.getSelectedText();
if(selectedText!=null && !selectedText.isEmpty())
{
searchContext.selectedTextStartIndex=textArea.getSelectionStart();
searchContext.selectedTextEndIndex=textArea.getSelectionEnd();
findField.setText(selectedText);
searchContext.findPreviousSearchedText=selectedText;
config.lastSearchedText=searchContext.findPreviousSearchedText;


findField.selectAll();
}


if(searchContext.findPreviousSearchedText.trim().length()==0) 
{
findNextButton.setEnabled(false);
replaceButton.setEnabled(false);
replaceAllButton.setEnabled(false);
}
});
// replaceMenuItem actionListener ends here

//findMenuItem actionListener starts here
findMenuItem.addActionListener(ev->{
if(textArea.getDocument().getLength()==0) return;
JDialog findDialog=new JDialog(notepad,"Find",false);
//registerKeyboardAction binds the ESC key to an action
//When the ESC key is pressed, the dialog is sent a WINDOW_CLOSING event, 
//which will trigger the windowClosing method of any attached WindowListener
findDialog.getRootPane().registerKeyboardAction(
ef->findDialog.dispatchEvent(new WindowEvent(findDialog,WindowEvent.WINDOW_CLOSING)),
KeyStroke.getKeyStroke("ESCAPE"),
JComponent.WHEN_IN_FOCUSED_WINDOW);

searchContext.findDialogReset=false;
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

matchCaseCheckBox.setSelected(config.matchCase);
wrapAroundCheckBox.setSelected(config.wrapAround);


matchCaseCheckBox.addActionListener(ev1->{
config.matchCase=matchCaseCheckBox.isSelected();
});

wrapAroundCheckBox.addActionListener(ev1->{
config.wrapAround=wrapAroundCheckBox.isSelected();
});


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

if(config.upDirection) upRadioButton.setSelected(true);
else downRadioButton.setSelected(true);

upRadioButton.addActionListener(ev2->{
config.upDirection=upRadioButton.isSelected();
});

downRadioButton.addActionListener(ev2->{
config.upDirection=(downRadioButton.isSelected())?false:true;
});


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
config.lastSearchedText=searchText;
boolean matchCase=matchCaseCheckBox.isSelected();
boolean wrapAround=wrapAroundCheckBox.isSelected();
boolean directionUp=upRadioButton.isSelected();
boolean b=searchManager.performFind(notepad,textArea,scrollPane,searchContext,searchText,matchCase,wrapAround,directionUp,true);
});

cancelButton.addActionListener(e->{
searchContext.findDialogReset=true;
findDialog.dispose();
});

findDialog.addWindowListener(new WindowAdapter(){
@Override
public void windowClosing(WindowEvent e)
{
searchContext.findDialogReset=true;
}
});


int parentX=notepad.getX();
int parentY=notepad.getY();
int parentWidth=notepad.getWidth();
int parentHeight=notepad.getHeight();

int dialogX=parentX+(parentWidth-findDialog.getWidth())/8;
int dialogY=parentY+(parentHeight-findDialog.getHeight())/4;

findDialog.setLocation(dialogX,dialogY);
//findDialog.setLocationRelativeTo(notepad);
findDialog.setVisible(true);


findField.setText(searchContext.findPreviousSearchedText);


String selectedText=textArea.getSelectedText();
if(selectedText!=null && !selectedText.isEmpty())
{
findField.setText(selectedText);
searchContext.findPreviousSearchedText=selectedText;
config.lastSearchedText=searchContext.findPreviousSearchedText;


findField.selectAll();
}


if(searchContext.findPreviousSearchedText.trim().length()==0) findNextButton.setEnabled(false);
/*
NO NEED WILL REMOVE LATER
if(findField.getText().trim().length()!=0)
{
//SwingUtilities.invokeLater(()->findNextButton.requestFocusInWindow());
}
*/	
});


//findMenuItem actionListener ends here



findNextMenuItem.addActionListener(ev->{
String selectedText=textArea.getSelectedText();
if(selectedText!=null && selectedText.trim().length()>0)
{
 searchContext.findPreviousSearchedText=selectedText;
config.lastSearchedText=searchContext.findPreviousSearchedText;

}
boolean b=searchManager.performFind(notepad,textArea,scrollPane,searchContext,searchContext.findPreviousSearchedText,config.matchCase,config.wrapAround,false,true);
});

findPreviousMenuItem.addActionListener(ev->{
String selectedText=textArea.getSelectedText();
if(selectedText!=null && selectedText.trim().length()>0)
{
 searchContext.findPreviousSearchedText=selectedText;
config.lastSearchedText=searchContext.findPreviousSearchedText;


}
boolean b=searchManager.performFind(notepad,textArea,scrollPane,searchContext,searchContext.findPreviousSearchedText,config.matchCase,config.wrapAround,true,true);
});
selectAllMenuItem.addActionListener(ev->{
textArea.selectAll();
});




goToMenuItem.addActionListener(ev->{
JDialog goToDialog=new JDialog(notepad,"Go To Line",false);
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
int parentX=notepad.getX();
int parentY=notepad.getY();
int parentWidth=notepad.getWidth();
int parentHeight=notepad.getHeight();

int dialogX=parentX+(parentWidth-goToDialog.getWidth())/8;
int dialogY=parentY+(parentHeight-goToDialog.getHeight())/4;

goToDialog.setLocation(dialogX,dialogY);
//goToDialog.setLocationRelativeTo(notepad);
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
JOptionPane.showMessageDialog(goToDialog,"The line number is beyond the total line number","DaniPad - Goto Line",JOptionPane.ERROR_MESSAGE);
return;
}
//calculate postion and move caret
int lineStartOffset=textArea.getLineStartOffset(lineNumber-1);//Line number is 1-based
textArea.setCaretPosition(lineStartOffset);
goToDialog.dispose();
}catch(NumberFormatException numberFormatException)
{
//show a tooltip or a popup window
JOptionPane.showMessageDialog(goToDialog, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
LogException.log(numberFormatException);
}
catch(BadLocationException badLocationException)
{
JOptionPane.showMessageDialog(goToDialog, "An unexpected error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
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
if(searchContext.emptyLineField)
{
searchContext.emptyLineField=false;
lineField.setText("");	
}
}
});


lineField.getDocument().addDocumentListener(new DocumentListener(){
@Override
public void insertUpdate(DocumentEvent de)
{
char c=lineField.getText().charAt(lineField.getText().length()-1);
if(!(c>=48 && c<=57))
{
searchContext.emptyLineField=true;
JOptionPane.showMessageDialog(goToDialog,"Unacceptable Character\nOnly numbers are allowed","DaniPad - Goto Line",JOptionPane.ERROR_MESSAGE);
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


}
public void setEnabledUndoMenuItem(boolean enable)
{
undoMenuItem.setEnabled(enable);
}
public void setEnabledRedoMenuItem(boolean enable)
{
redoMenuItem.setEnabled(enable);
}
public void setEnabledCutMenuItem(boolean enable)
{
cutMenuItem.setEnabled(enable);
}
public void setEnabledCopyMenuItem(boolean enable)
{
copyMenuItem.setEnabled(enable);

}
public void setEnabledDeleteMenuItem(boolean enable)
{
deleteMenuItem.setEnabled(enable);
}
public void setEnabledPasteMenuItem(boolean enable)
{
pasteMenuItem.setEnabled(enable);
}
}

