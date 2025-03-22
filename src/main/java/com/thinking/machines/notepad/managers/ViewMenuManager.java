 package com.thinking.machines.notepad.managers;
import com.thinking.machines.notepad.*;
import com.thinking.machines.notepad.io.*;
import com.thinking.machines.notepad.models.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class ViewMenuManager
{
private JTextArea textArea;
private Notepad notepad;
private StatusPanel statusPanel;
private JFrame fakeParent;
private JMenuBar menuBar; 
private Config config;
private FileHandler fileHandler;
private Notepad.Counter counter;
private int fontSize;
private final int maxFontSize=60;
private final int minFontSize=8;
private int currentZoomPercentage=100;
private int stepPercentage=25;
private JMenu viewMenu;
private JMenu zoomMenu;
private JMenuItem zoomInMenuItem,zoomOutMenuItem;
private JCheckBoxMenuItem statusMenuItem;

public ViewMenuManager(Notepad notepad,JTextArea textArea,StatusPanel statusPanel,JFrame fakeParent,JMenuBar menuBar,Config config,FileHandler fileHandler,Notepad.Counter counter)
{
this.textArea=textArea;
this.notepad=notepad;
this.statusPanel=statusPanel;
this.fakeParent=fakeParent;
this.menuBar=menuBar;
this.config=config;
this.fileHandler=fileHandler;
this.counter=counter;
initComponents();
addMenuItems();
addEventListeners();
bindShortcutKeys();
setConfigSettings();
menuBar.add(viewMenu);
}
private void initComponents()
{
statusMenuItem=new JCheckBoxMenuItem("Status");
zoomInMenuItem=new JMenuItem("Zoom In");
zoomOutMenuItem=new JMenuItem("Zoom Out");
zoomMenu=new JMenu("Zoom");
viewMenu=new JMenu("View");
}
private void addMenuItems()
{
zoomMenu.add(zoomInMenuItem);
zoomMenu.add(zoomOutMenuItem);
viewMenu.add(zoomMenu);
viewMenu.add(statusMenuItem);
}
private void setConfigSettings()
{
if(this.config.statusBar)
{
statusMenuItem.setSelected(true);
notepad.setStatusBarPanelVisibility(this.config.statusBar);
}
else
{
notepad.setStatusBarPanelVisibility(this.config.statusBar);
}
}

private void bindShortcutKeys()
{
zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, KeyEvent.CTRL_DOWN_MASK));
zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, KeyEvent.CTRL_DOWN_MASK));
}
private void addEventListeners()
{
statusMenuItem.addActionListener(ev->{
boolean isSelected=statusMenuItem.isSelected();
config.statusBar=isSelected;
notepad.setStatusBarPanelVisibility(isSelected);
});


zoomInMenuItem.addActionListener(ev->{
float currentFontSize=ViewMenuManager.this.fontSize;
int increasedFontSize=textArea.getFont().getSize();
if(increasedFontSize<maxFontSize)
{
increasedFontSize+=2;//increment font size 
Font selectedFont=textArea.getFont();
float per=(float)(increasedFontSize/currentFontSize);
textArea.setFont(new Font(selectedFont.getName(),selectedFont.getStyle(),increasedFontSize));
statusPanel.updateZoomPercent((int)(per*100));
}
});
zoomOutMenuItem.addActionListener(ev->{
float currentFontSize=ViewMenuManager.this.fontSize;
int decreasedFontSize=textArea.getFont().getSize();

if(decreasedFontSize>minFontSize)
{
decreasedFontSize-=2;//decrement font size 
Font selectedFont=textArea.getFont();
float per=(float)(decreasedFontSize/currentFontSize);
textArea.setFont(new Font(selectedFont.getName(),selectedFont.getStyle(),decreasedFontSize));
statusPanel.updateZoomPercent((int)(per*100));
}
});


/*

zoomInMenuItem.addActionListener(ev->{
float perc=(float)(currentZoomPercentage+stepPercentage)/100;
int increasedFontSize=(int)Math.round(perc*ViewMenuManager.this.fontSize);

if(increasedFontSize<maxFontSize)
{
currentZoomPercentage+=stepPercentage;
Font selectedFont=textArea.getFont();
textArea.setFont(new Font(selectedFont.getName(),selectedFont.getStyle(),increasedFontSize));
statusPanel.updateZoomPercent(currentZoomPercentage);
}
});
zoomOutMenuItem.addActionListener(ev->{
float perc=(float)(currentZoomPercentage-stepPercentage)/100;
int decreasedFontSize=(int)Math.floor(perc*ViewMenuManager.this.fontSize);

if(decreasedFontSize>minFontSize)
{
currentZoomPercentage-=stepPercentage;
Font selectedFont=textArea.getFont();
textArea.setFont(new Font(selectedFont.getName(),selectedFont.getStyle(),decreasedFontSize));
statusPanel.updateZoomPercent(currentZoomPercentage);
}
});

*/

}
public void updateFontSize(int fontSize)
{
this.fontSize=fontSize;
statusPanel.updateZoomPercent(100);
this.currentZoomPercentage=100;
}

}
