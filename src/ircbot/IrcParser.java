/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircbot;

/**
 *
 * @author Garrett
 */
public class IrcParser {
    // takes raw irc responses and breaks them down section by section
    private String rawData;
    
    private String userInfo;
    private String code;
    private String misc;
    
    private String channel;
    
    public IrcParser(String data) {
        int dataBreak = 0;
        String breaker = "";
        
        rawData = data;
        
        // break down info
        dataBreak = rawData.indexOf(" ");
        userInfo = rawData.substring(0,dataBreak);
        
        if(!userInfo.equals("PING")) {
            int dataBreak2 = rawData.indexOf(" ", dataBreak+1);

            code = rawData.substring(dataBreak+1, dataBreak2);

            misc = rawData.substring(dataBreak2+1);
            //System.out.println(misc);
            if(misc.contains(":")) {
                dataBreak = misc.indexOf(" :");
                channel = misc.substring(0, dataBreak);
                if(channel.contains("#")) {
                    dataBreak = misc.indexOf("#");
                    channel = channel.substring(dataBreak);
                }
                System.out.println(channel);
            }
            else {
                channel = misc;
            }
        }
        else {
            code = rawData.substring(dataBreak+2);
            misc = "";
        }
    }
    
    public String parsed() {
        return (userInfo + " " + code + " " + misc);
    }
    
    public String returnMisc() {
        return misc;
    }
    
    public String returnCode() {
        return code;
    }
    
    public String returnUserInfo() {
        return userInfo;
    }
    
    public String returnChannel() {
        return channel;
    }
    
    public String returnRawData() {
        return rawData;
    }
}
