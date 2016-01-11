/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircbot;

import java.io.BufferedReader;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Garrett
 */
public class connection {
    
    // Main purpose of this class is to make a new connection AND to make input/output of socket possible...
    
    
    private BufferedReader input;
    private PrintWriter   output;
    
    private String channelUsed;
    private String oauth;
    
    boolean running = false;
    private Socket s;
    
    private String userName;
    
    boolean safe;
    
    String server, channel, nickName;
    int port;
    
    public connection(String IRC_server, int IRC_port, String IRC_nickName, String IRC_auth, String IRC_channel) {
        server = IRC_server;
        port = IRC_port;
        userName = IRC_nickName;
        nickName = IRC_nickName;
        oauth = IRC_auth;
        channel = IRC_channel;
        safe = true;
        
        
//        while(running) {
//            runner();
//        }
        
        
    }
    
    public void connect() {
        try {
            s = new Socket(server, port);
        } catch (IOException ex) {
            //Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
            safe = false;
            System.out.println("\n\n\n\nConnection ERROR: " + ex + "\n\n\n\n\n\n");
            JOptionPane.showMessageDialog(null, ex);
            
        }
        try {
            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        } catch (IOException ex) {
            safe = false;
            System.out.println("\n\n\n\nBuffered Reader ERROR: " + ex + "\n\n\n\n\n\n");
            JOptionPane.showMessageDialog(null, ex);
            
        }
        try {
            output = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException ex) {
            safe = false;
            System.out.println("\n\n\n\nPrintWriter ERROR: " + ex + "\n\n\n\n\n\n");
            JOptionPane.showMessageDialog(null, ex);
            
        }
        
        channelUsed = channel;
        if(safe) {
            
            System.out.println("\n\n\n\nSAFE_ZONE\n\n\n\n\n\n");
            output.println("PASS " + oauth);




            output.println("NICK " + nickName + "\r\n");
            //output.println("twitchclient 1\r\n");                                 // seems to be disabled by twitch now...
            output.println("CAP REQ :twitch.tv/membership\r\n");
            output.println("CAP REQ :twitch.tv/commands\r\n");
            //output.println("CAP REQ :twitch.tv/tags\r\n");
            output.println("USER " + nickName + " 8 * :noone\r\n");
            //output.println("JOIN " + channel + "\r\n");
        } 
        else {
            try {
                closeSock();
            } catch (IOException ex) {
                Logger.getLogger(connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        running = true;
    }
    
    public void outputToChannel(String out, String channel) {
        if(safe)
            output.println("PRIVMSG " + channel + " :" + out + "\r\n");
    }
    
    public String rawFeedback() throws IOException{
        String answer = input.readLine();
        
        return answer;
    }
    
    public void outputText(String out) {
        if(safe)
            output.println(out + "\r\n");
    }
    
    public String toChatScreen() throws IOException {  // not used...
        //System.out.println(rawFeedback());
        
        String finalData = "";
        String data = rawFeedback();
        
        
        if(data.startsWith(":")) {
            if(data.contains(" PRIVMSG ")) {
                if(data.contains("!")) {
                    String username = "";
                    String comment  = "";
                    
                    data = data.substring(1);

                    int num = 0;

                    num = data.indexOf("!");

                    username = data.substring(0, num);
                    
                    num = data.indexOf(" :");
                    
                    comment = data.substring(num + 2);

                    data = "[" + username + "]: " + comment;
                }
            }
        }
        else if (data.startsWith("PING :")) {
            String pong = "";
            pong = data.substring(6);
            
            outputText("PONG " + pong);
        }
        return data;
    }
    
    public boolean pingPong() throws IOException {  // also not used...
        String answer = rawFeedback();
        
        if(answer.startsWith("PING :")) {
            String pong = "";
            pong = answer.substring(6);
            output.println("PONG " + pong + "\r\n");
            return true;
        }
        
        return false;
    }
    
    public void closeSock() throws IOException {
        s.close();
    }
    
    public String returnUserName() {
        return userName;
    }
    
    public boolean isSafe() {
        return safe;
    }
}
