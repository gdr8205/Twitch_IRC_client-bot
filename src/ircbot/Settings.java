/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircbot;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Garrett
 */
public class Settings {
    
    protected String IRC_server;
    protected String IRC_port;
    protected String IRC_nickName;
    protected String IRC_defaultChannel;
    protected String IRC_OAUTH;
    
    protected static String[] settings;
    protected static String[] settingsTitles;
    private String settingsFileName;
    
    public Settings() {
        
        // tell program all known settings
        settingsTitles = new String[] {"IRC_ip", 
                                       "IRC_port", 
                                       "IRC_nick", 
                                       "IRC_oauth", 
                                       "IRC_defaultChannel", 
                                       "MySQL_addr", 
                                       "MySQL_port",
                                       "MySQL_userName", 
                                       "MySQL_password", 
                                       "MySQL_dbName"};
        
        // set setting array length BASED ON number of titles known...
        settings       = new String[settingsTitles.length];
        
        // initiate all settings to ""
        for(int x = 0; x < settingsTitles.length; x++) {
            settings[x] = "";
        }
        
        // set the settings filename that we search for and save/load
        settingsFileName = "settings.conf";
    }
    
    // Cehck if settings file exists on local computer
    public boolean checkFileExists() throws IOException{
        FileReader in = null;
        try {
            in = new FileReader(settingsFileName);
        } finally {
            if(in != null) {
                in.close();
                return true;
            }
            else {
                //in.close();
                return false;
            }
        }
    }
    
    // if settings exist, load current.  Else Load default empty settings and save that file...
    public boolean loadSettings() throws IOException{
        if(checkFileExists()) {
            FileInputStream fis = new FileInputStream(settingsFileName);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            
            while ((line = br.readLine()) != null) {
                setSettingFromFile(line);
            }
            br.close();
            return true;
        }
        else {
            PrintWriter writer = new PrintWriter(settingsFileName, "UTF-8");
            writer.print(
            "IRC_ip: \n" +
            "IRC_port: \n" +
            "IRC_nick: \n" +
            "IRC_oauth: \n" +
            "IRC_defaultChannel: \n" +
            "MySQL_addr: \n" +
            "MySQL_port: \n" +
            "MySQL_userName: \n" +
            "MySQL_password: \n" +
            "MySQL_dbName: " 
            );
            writer.close();
        }
        return false;
        
    }
    
    // java pbr workaround...
    // takes a string from settings file and sifts out the setting and assigns it to the correct array node.
    public boolean setSettingFromFile(String line) {
        
        if(line.contains(": ")) {
            int pos = line.indexOf(": ");
            if(line.length() >= pos+3) {
                String[] parts = line.split(": ");
                String settingTitle = parts[0];
                String setting      = parts[1];

                int settingNum = setSetting(settingTitle, setting);

                if(settingNum == -1) {
                    // setup for error catching/reporting later... (aka: is config file corrupt?)
                    return false;
                }
            }
        }
        // failsafe return
        return false;
    }
    
    public String returnSetting(String settingTitle) {
        for(int x = 0; x<settingsTitles.length; x++) {
            if(settingsTitles[x].equalsIgnoreCase(settingTitle)) {
                return settings[x];
            }
        }
        
        return "ERROR_IN_CONFIG_OR_SOMETHING";
    }
    
    public int setSetting(String settingTitle, String setting) {
        int settingNum = -1;
        
        for(int x=0; x<settingsTitles.length; x++) {
            if(settingsTitles[x].equalsIgnoreCase(settingTitle)) {
                settingNum = x;
                settings[x] = setting;
                
                break;
            }
        }
        
        return settingNum;
    }
    
    public boolean saveSettings() {
        return false;
    }
}
