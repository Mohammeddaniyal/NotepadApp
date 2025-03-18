package com.thinking.machines.notepad.manager;
import com.thinking.machines.notepad.models.*;
import com.thinking.machines.notepad.exceptions.*;
import com.google.gson.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
public class ConfigManager
{
private static final String CONFIG_DIR=System.getProperty("user.home")+File.separator+"DaniPadConfig";
private static final String CONFIG_FILE=CONFIG_DIR+File.separator+"setting.json";
private static final Gson gson=new GsonBuilder().setPrettyPrinting().create();
private static Config config=new Config();

static
{
try
{
File dir=new File(CONFIG_DIR);
if(!dir.exists()) dir.mkdirs();
File file=new File(CONFIG_FILE);
if(!file.exists()) 
{
saveConfig();
}
else
{
String json=new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
config=gson.fromJson(json,Config.class);
}
}catch(Exception e)
{
LogException.log(e);
}
}//static initializer ends

public static void saveConfig()
{
try(Writer writer=new FileWriter(CONFIG_FILE))
{
gson.toJson(config,writer);
}catch(IOException io)
{
LogException.log(io);
}
}
public static void setConfig(Config c)
{
config=c;
}
public static Config getConfig()
{
System.out.println("1212"+config.matchCase);
return config;
}
}//class ends
