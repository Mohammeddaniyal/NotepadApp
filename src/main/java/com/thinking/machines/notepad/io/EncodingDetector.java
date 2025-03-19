package com.thinking.machines.notepad.io;
import java.io.*;
import java.nio.charset.Charset;
import org.mozilla.universalchardet.UniversalDetector;
import com.thinking.machines.notepad.exceptions.*;
public class EncodingDetector
{
public static Charset detectEncoding(File file)
{
try(FileInputStream fis=new FileInputStream(file))
{
byte []buffer=new byte[4096];
UniversalDetector detector=new UniversalDetector(null);
int bytesRead;
while((bytesRead=fis.read(buffer))>0 && !detector.isDone())
{
detector.handleData(buffer,0,bytesRead);
}
detector.dataEnd();
String encodingName=detector.getDetectedCharset();
detector.reset();
if(encodingName!=null) return Charset.forName(encodingName);
else return Charset.defaultCharset(); // Default to System encoding
}catch(IOException ioException)
{
LogException.log(ioException);
return Charset.defaultCharset();
}
}//function ends
}//class ends