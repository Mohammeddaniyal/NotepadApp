package com.thinking.machines.notepad.models;
//find , find and replace AND goto line (Dialogs) related variables
public class SearchContext
{
public String findPreviousSearchedText="";
public String replacePreviousSearchedText="";
public boolean findDialogReset=false;
public int findPreviousStartIndex=-1;
public int findPreviousEndIndex=-1;
public int selectedTextStartIndex=-1;
public int selectedTextEndIndex=-1;
public boolean emptyLineField=false;
}
