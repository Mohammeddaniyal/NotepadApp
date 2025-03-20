package com.thinking.machines.notepad.managers;
import java.awt.event.*;
import javax.swing.*;
import com.thinking.machines.notepad.exceptions.*;
import com.thinking.machines.notepad.io.FileHandler;
import com.thinking.machines.notepad.Notepad;
public class SettingMenuManager
{
private JMenuBar menuBar;
private JMenu settingMenu;
private JMenu encodingMenu;
private JMenu autoSaveMenu;
private JMenuItem advancedAutoSaveMenuItem;
private JMenu themeMenu;
private ThemeManager themeManager;
private Notepad notepad;
private FileHandler fileHandler;
private Notepad.Counter counter;
private JFrame fakeParent;
private Timer autoSaveTimer;
public SettingMenuManager(Notepad notepad,JMenuBar menuBar,FileHandler fileHandler,ThemeManager themeManager,Notepad.Counter counter,JFrame fakeParent)
{
this.notepad=notepad;
this.menuBar=menuBar;
this.fileHandler=fileHandler;
this.themeManager=themeManager;
this.counter=counter;
this.fakeParent=fakeParent;
initComponents();
addMenuItems();
addEventListeners();
setAutoSaveInterval(1000);
menuBar.add(settingMenu);
}
private void initComponents()
{
settingMenu=new JMenu("Setting");
encodingMenu=new JMenu("Encoding");
autoSaveMenu=new JMenu("Auto-Save Interval");
advancedAutoSaveMenuItem=new JMenuItem("Advanced...");
themeMenu=themeManager.createThemeMenu();
}
private void addMenuItems()
{
settingMenu.add(encodingMenu);
settingMenu.add(autoSaveMenu);
settingMenu.add(themeMenu);
}
private void addEventListeners()
{
String []encodings={"UTF-8","ISO-8859-1","Windows-1252","US-ASCII"};
ButtonGroup encodingButtonGroup=new ButtonGroup();
JRadioButtonMenuItem item;
for(String enc : encodings)
{
item=new JRadioButtonMenuItem(enc);
encodingButtonGroup.add(item);
encodingMenu.add(item);
if(enc.equals(fileHandler.getCurrentEncoding())) item.setSelected(true);
item.addActionListener(ev->fileHandler.setSelectedEncoding(enc));
}

//for auto save
ButtonGroup autoSaveIntervalButtonGroup=new ButtonGroup();
String intervals[]={"30 sec","1 min","5 min","10 min","Off"};
int intervalValues[]={30,60,300,600,0};//in seconds

for(int i=0;i<intervals.length;i++)
{
item=new JRadioButtonMenuItem(intervals[i]);
autoSaveIntervalButtonGroup.add(item);
autoSaveMenu.add(item);
int interval=intervalValues[i];
item.addActionListener(ev->setAutoSaveInterval(interval));
if(i==1) item.setSelected(true); //default 1 min
}
autoSaveMenu.addSeparator();
autoSaveMenu.add(advancedAutoSaveMenuItem);
advancedAutoSaveMenuItem.addActionListener(ev->showCustomAutoSaveDialog());

//for theme


}
private void setAutoSaveInterval(int seconds)
{
if(autoSaveTimer!=null) autoSaveTimer.stop();
if(seconds>0)
{
autoSaveTimer=new Timer(seconds*1000,ev->
{
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
autoSaveTimer.start();
}
}
private void showCustomAutoSaveDialog()
{
String input=JOptionPane.showInputDialog(fakeParent,"Enter Auto-Save interval (seconds) : ","Custom Auto-Save",JOptionPane.PLAIN_MESSAGE);
try
{
int seconds=Integer.parseInt(input);
if(seconds>0) setAutoSaveInterval(seconds);
}catch(NumberFormatException numberFormatException)
{
JOptionPane.showMessageDialog(fakeParent,"Invalid input, Please enter a valid number.","Error",JOptionPane.ERROR_MESSAGE);
LogException.log(numberFormatException);
return;
}
}
}//class ends