import com.thinking.machines.notepad.*;
class Main
{
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