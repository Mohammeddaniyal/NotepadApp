package com.thinking.machines.notepad.exceptions;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;
public class LogException
{
private static final String LOG_FILE=System.getProperty("user.home")+File.separator+"notepadAppData"+File.separator+"logs"+File.separator+"error.log";
public static void log(Exception e)
{
try
{
File logFile=new File(LOG_FILE);
if(!logFile.getParentFile().exists())
{
logFile.getParentFile().mkdirs();
}
try(PrintWriter writer=new PrintWriter(new FileWriter(logFile,true)))
{
String timeStamp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
writer.println("["+timeStamp+"] "+e.toString());
//e.toString() → Gives the exception class name +
// message (java.io.FileNotFoundException: data/config.txt (No such file or directory))
for(StackTraceElement element:e.getStackTrace())
{
writer.println("/t at "+element); // tab character + at + element
}
writer.println();
}
}catch(IOException ioException)
{
ioException.printStackTrace();
}
}
}