package com.thinking.machines.notepad.managers;
import javax.swing.*;
import com.thinking.machines.notepad.Notepad;
import com.thinking.machines.notepad.models.*;
class SearchManager
{
public boolean performFind(Notepad notepad,JTextArea textArea,JScrollPane scrollPane,SearchContext searchContext,String searchText,boolean matchCase,boolean wrapAround,boolean directionUp,boolean highlight)
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
scrollPane.getHorizontalScrollBar().setValue(0);
int s=index;
int end=index+searchText.length();
searchContext.findPreviousStartIndex=s;
searchContext.findPreviousEndIndex=end;
searchContext.selectedTextStartIndex=s;
searchContext.selectedTextEndIndex=end;
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
if(searchContext.findPreviousStartIndex!=-1 && searchContext.findPreviousSearchedText.equals(searchText) && highlight && searchContext.findDialogReset==false)
{
textArea.setCaretPosition(searchContext.findPreviousStartIndex);
textArea.setSelectionStart(searchContext.findPreviousStartIndex);
textArea.setSelectionEnd(searchContext.findPreviousEndIndex);

textArea.requestFocusInWindow();
}
else
{
searchContext.findPreviousStartIndex=-1;
searchContext.findPreviousEndIndex=-1;
}
JOptionPane.showMessageDialog(notepad,"Not Found","DaniPad",JOptionPane.INFORMATION_MESSAGE);
searchContext.findPreviousSearchedText=searchText;
return false;
}
searchContext.findPreviousSearchedText=searchText;
return true;
}
}
