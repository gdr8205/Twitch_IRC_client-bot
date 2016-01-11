/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircbot;

import java.io.IOException;
import java.net.Socket;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Garrett
 */
public class IRC_DeCoder{
    // idea is to decode all IRC returnCodes
    
    // Setup Reply Codes...
    private static final String RPL_WELCOME = "001";
    private static final String RPL_YourHost = "002";
    private static final String RPL_CREATED = "003";
    private static final String RPL_MyInfo = "004";
    private static final String RPL_NameReply = "353";
    private static final String RPL_EndOfNames = "366";
    private static final String RPL_MOTD = "372";
    private static final String RPL_MOTD_Start = "375";
    private static final String RPL_EndOf_MOTD = "376";
    private static final String RPL_PING = "PING";
    private static final String RPL_Away = "301";
    private static final String RPL_UnAway = "305";
    private static final String RPL_NowAway = "306";
    
    private static final String RPL_TWITCH_CAP = "CAP";
    private static final String RPL_TWITCH_USERSTATE = "USERSTATE";
    private static final String RPL_TWITCH_ROOMSTATE = "ROOMSTATE";
    private static final String RPL_TWITCH_MODE = "MODE";
   
    // Error Codes...
    private static final String ERR_UNKNOWN = "400";
    private static final String ERR_NoSuchNick = "401";
    private static final String ERR_NoSuchChannel = "403";
    private static final String ERR_CannotSendToChannel = "404";
    private static final String ERRTooManyChannels_ = "405";
    private static final String ERR_NicknameInUse = "433";
    private static final String ERR_NickCollision = "436";
    private static final String ERR_NotRegistered = "451";
    private static final String ERR_NeedMoreParams = "461";
    private static final String ERR_AlreadyRegistered = "462";
    private static final String ERR_PasswdMissmatch = "464";
    private static final String ERR_YoureBannedCreep = "465";
    private static final String ERR_ChannelIsFull = "471";
    private static final String ERR_BannedFromChan = "474";
    private static final String ERR_NoPrivileges = "481";
    private static final String ERR_ChanOprivNeeded = "482";
    
    private String reply;
    
    private String nickName;
    private String code;
    private String comment;
    private String userName;
    
    private String userList;
    private IRCBOT window;
    private connection sock;
    private String defaultChannel;
    
    private boolean userListing;
    private boolean userListingEnd;
    private boolean joins;
    private boolean parts;
    
    public IRC_DeCoder(connection connect, String channel, String IRC_user_info, String IRC_code, String IRC_misc){
        sock           = connect;
        nickName       = IRC_user_info;
        code           = IRC_code;
        comment        = IRC_misc;
        defaultChannel = channel;
        
        userListing    = false;
        userListingEnd = false;
        joins          = false;
        parts          = false;
        
        userName       = sock.returnUserName();
    }
    
    public String decode() throws IOException, BadLocationException {
        String result = "";
        
        switch(code) {
                        case RPL_WELCOME:
                            //window.toChatScreen(comment);
                            result = comment;
                            break;
                        case RPL_YourHost:
                            //window.toChatScreen(comment);
                            result = comment;
                            break;
                        case RPL_CREATED:
                            //window.toChatScreen(comment);
                            result = comment;
                            break;
                        case "004":
                            sock.outputText("JOIN " + "#" + userName + "\r\n");
                            sock.outputText("JOIN " + defaultChannel + "\r\n");
                            break;
                        case "353":
                            //int placer = comment.indexOf(":");

                            //userList = userList + comment.substring(placer+1) + " ";
                            //sock.outputText("CAP REQ :twitch.tv/membership");
                            setUserListing();
                            break;
                        case "366":
                            //System.out.println("\n\n\n\n\nPENIS\n" + userList + "\n\n\n\n");
                            //UserList listPane = new UserList(userList);
                            //window.setUserList(listPane.returnUserList());
                            setUserListingEnd();
                            break;
                        case "PRIVMSG":
                            
                            int dataBreakPoint;

                            if(nickName.contains("!")) {
                                dataBreakPoint = nickName.indexOf("!");
                                nickName = nickName.substring(1,dataBreakPoint);
                            }
                            dataBreakPoint = comment.indexOf(":");
                            comment = comment.substring(dataBreakPoint+1);

                            result = "<b><font color=green>[" + nickName + "]:</font></b> " + comment + "</td></tr>";
                            //s.toChatScreen("[" + nickName + "]: " + comment);
                            break;
                            
                        case RPL_TWITCH_MODE:
                            //System.out.println("\n\n\n\n\nPENIS\n MODE SEDDDDDD " + "\n\n\n\n");
                            
                        default:
                            
                    }
        
                    if (nickName.equals("PING")) {
                        sock.outputText("PONG " + code);
                        result = "PONG " + code;
                    }
                    if(comment.contains("!ello o/")){
                        sock.outputToChannel("ello, " + nickName + "\r\n", "#psynaps");
                    }
                    
                    
        
        return result;
    }
    
    private void setUserListing() {
        userListing = true;
    }
    
    private void setUserListingEnd() {
        userListingEnd = true;
    }
    
    public boolean isUserListing() {
        return userListing;
    }
    
    public boolean isUserListingEnd() {
        return userListingEnd;
    }
    
}
