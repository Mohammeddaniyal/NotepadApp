import java.util.*;
import java.text.*;
class psp
{
public static void wewemain(String gg[])
{
String date="01/"+gg[0]+"/"+gg[1];
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
Date d=new Date();
try
{
d=sdf.parse(date);
}catch(ParseException p)
{
System.out.println(p.getMessage());
}
int year=0;
int month=d.getMonth()+1;
int days=0;
int rows=0;
int columns=7;
if(month==1||month==3||month==5||month==7||month==8||month==10||month==12)
{
days=31;
}
else if(month==2)
{
year=d.getYear()+1900;
if(year%400==0) days=29;
else if(year%100==0) days=28;
else if(year%4==0) days=29;
else days=28;
}
else 
{
days=30;
}
int firstDay=d.getDay();
if(days==31)
{
if(firstDay>=0 && firstDay<=4)
{
rows=5;
}
else if(firstDay>=5 && firstDay<=6)
{
rows=6;
}
}
else if(days==30)
{
if(firstDay==6)
{
rows=6;
}
else
{
rows=5;
}
}
else if(days==29)
{
rows=5;
}
else if(days==28)
{
if(firstDay==0)
{
rows=4;
}
else
{
rows=5;
}
}
System.out.println("First Day : "+d.getDay());
System.out.println("Rows : "+rows);
System.out.println("Month : "+month);
System.out.println("Days : "+days);
int [][] calender;
calender=new int[rows][columns];
boolean fDay=false;
boolean lDay=true;
int x=0;
int i=1;
while(x<7)
{
if(firstDay==x) fDay=true;
if(fDay) calender[0][x]=i++;
else calender[0][x]=0;
x++;
}
int r=1;
while(r<rows-1)
{
x=0;
while(x<7)
{
calender[r][x++]=i++;
}
r++;
}
x=0;
while(x<7)
{
if(lDay) calender[rows-1][x]=i;
else calender[rows-1][x]=0;
if(days==i) lDay=false;
i++;
x++;
}
String monthName="";
if(month==1)
{
monthName="January";
}else if(month==2)
{
monthName="Febuary";
}else if(month==3)
{
monthName="March";
}else if(month==4)
{
monthName="April";
}else if(month==5)
{
monthName="May";
}else if(month==6)
{
monthName="June";
}else if(month==7)
{
monthName="July";
}else if(month==8)
{
monthName="August";
}else if(month==9)
{
monthName="September";
}else if(month==10)
{
monthName="October";
}else if(month==11)
{
monthName="November";
}else if(month==12)
{
monthName="December";
}

System.out.println("---------------------------------------------------");
System.out.printf("|%40d %-7s |\n",d.getYear()+1900,monthName);
System.out.println("---------------------------------------------------");
System.out.printf("|%5s  %5s  %5s  %5s  %5s  %5s  %5s  |\n","Sun","Mon","Tue","Wed","Thus","Fri","Sat");
System.out.println("---------------------------------------------------");
r=0;
while(r<rows)
{
x=0;
System.out.print("|");
while(x<7)
{
if(calender[r][x]!=0) System.out.printf("%5d  ",calender[r][x]);
else System.out.printf("       ");
x++;
}
System.out.println("|");
r++;
}
System.out.println("---------------------------------------------------");
System.out.println(d.getDay()+1);
System.out.println(d.getDate());
System.out.println(d.getMonth()+1);
System.out.println(d.getYear()+1900);
}
}
