/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircbot;

import java.awt.*;
import java.awt.event.*;
import static java.awt.event.KeyEvent.VK_ENTER;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Collections.sort;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.*;

/**
 *
 * @author Garrett
 */
public class IRCBOT extends JFrame implements ActionListener, KeyListener {

    private JTextArea  chatInput;
    private JTextPane  chatScreen;
    private JButton    sendButton;
    private JTextPane  userList;
    private JTextPane  registeredUserList;
    
    private String nick;
    private String channel;
    
    
    //private BufferedReader input;
    //private PrintWriter   output;
    
    private static connection sock;
    //private static boolean give_away;
    
    //boolean running = true;
    
    //private JScrollPane scrollChat;
    
    boolean standardWindow;
    
    ChatDisplay chatter;
    
    //private StyledDocument document;
    private HTMLEditorKit kit = new HTMLEditorKit(); // needs to be relocated
    private HTMLDocument doc = new HTMLDocument();   // ''
    
    JMenuItem settings;
    //private JSplitPane chat_userList;
    
    String channelName;
    
    //private static Vector<String> regCompUList;
    private static Vector<String> windowNames;
    
    JButton g_start;
    JButton g_end;
    JButton g_reroll;
    
    private int winNum;
    private static SubstarterGiveaway substarter;
    
    private static SettingsPane settingsPane = new SettingsPane();
    
    public IRCBOT(boolean uList, String channel, int chatNum) {
        
        //chatter = new ChatDisplay("<html><body bgcolor='black'><table border=0pt width=100%>");
        winNum = chatNum;
        URL iconURL = getClass().getResource("P_300x300.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());
        
        Action doNothing = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
            
        };
        
        channelName = channel;
        standardWindow = uList;
        
        chatInput = new JTextArea(4,55);
            chatInput.setWrapStyleWord(true);
            chatInput.setLineWrap(true);
            chatInput.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "doNothing");
        chatScreen = new JTextPane();
            chatScreen.setContentType("text/html");
            chatScreen.setEditorKit(kit);
            chatScreen.setDocument(doc);
        userList   = new JTextPane();
        registeredUserList = new JTextPane();
        sendButton = new JButton("SEND");
        g_start = new JButton("START");
        g_end = new JButton("END");
        g_reroll = new JButton("secret");
        
        JLabel l_giveaway = new JLabel("Giveaway", JLabel.CENTER);
        JLabel l_chat = new JLabel("Chat", JLabel.CENTER);
        JLabel l_uList = new JLabel("User List", JLabel.CENTER);
        
        sendButton.addActionListener(this);
        g_start.addActionListener(this);
        g_end.addActionListener(this);
        g_reroll.addActionListener(this);
        
        g_start.setEnabled(false);  // disabled for basic irc client
        g_end.setEnabled(false);
        g_reroll.setEnabled(false);
        
        //String start = "<html><body>";
        
        Dimension min = new Dimension(100,1);
        Dimension pref = new Dimension(150,1);
        Dimension max = new Dimension(600,600);
        //chatScreen.setMinimumSize(min);
        //userList.setMinimumSize(min);
        //chatInput.addKeyListener(KeyBoardListener);
        
        JPanel mainPanel    = new JPanel(new BorderLayout(5,5));
        JPanel left         = new JPanel(new BorderLayout(5,5));
        JPanel mid         = new JPanel(new BorderLayout(5,5));
        JPanel right         = new JPanel(new BorderLayout(5,5));
        JPanel giveaway_buttons         = new JPanel(new BorderLayout(5,5));
        
        JPanel sendBar      = new JPanel();
        JScrollPane scrollChat  = new JScrollPane(chatScreen);
        JScrollPane scrollChatInput  = new JScrollPane(chatInput);
        JScrollPane userListScroll   = new JScrollPane(userList);
        JScrollPane regUserListScroll = new JScrollPane(registeredUserList);
        
        userListScroll.setMinimumSize(min);
        //userListScroll.setPreferredSize(pref);
        //userListScroll.setMaximumSize(new Dimension(800,600));
        //userListScroll.setPreferredSize(new Dimension(100,100));
        //scrollChat.setPreferredSize(pref);
        scrollChat.setMinimumSize(min);
        //scrollChat.setMaximumSize(new Dimension(500,500));
        regUserListScroll.setPreferredSize(pref);
        
        mid.add(l_chat, BorderLayout.NORTH);
        mid.add(scrollChat, BorderLayout.CENTER);
        
        right.add(l_uList, BorderLayout.NORTH);
        right.add(userListScroll, BorderLayout.CENTER);
        
        giveaway_buttons.add(g_start,BorderLayout.WEST);
        giveaway_buttons.add(g_end,BorderLayout.CENTER);
        giveaway_buttons.add(g_reroll,BorderLayout.PAGE_END);
        
        left.add(giveaway_buttons, BorderLayout.PAGE_END);
        
        JSplitPane chat_userList = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mid, right);
            chat_userList.setOneTouchExpandable(true);
            chat_userList.setDividerLocation(500);
            chat_userList.setResizeWeight(0.5);
            chat_userList.setContinuousLayout(true);
           //chat_userList.setPreferredSize(new Dimension(100,100));
            chat_userList.setMinimumSize(min);
            //chat_userList.setMaximumSize(max);
        
        userListScroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        //scroll.add(chatScreen);
        
        sendBar.add(scrollChatInput);
        sendBar.add(sendButton);
        
        
        if(standardWindow) {
            
            mainPanel.add(chat_userList, BorderLayout.CENTER);
            mainPanel.add(sendBar, BorderLayout.PAGE_END);
            
            JMenuBar menuBar    = new JMenuBar();
            JMenu fileMenu       = new JMenu("File");
            settings        = new JMenuItem("Settings");
        
            settings.addActionListener(this);
        
            fileMenu.add(settings);
            menuBar.add(fileMenu);
            
            left.add(l_giveaway, BorderLayout.NORTH);
            left.add(regUserListScroll, BorderLayout.CENTER);
            //left.setBorder(new EmptyBorder(0,5,0,0));
            //chat_userList.setBorder(new EtchedBorder(0,0,0,5));
            mainPanel.add(left, BorderLayout.WEST);
            mainPanel.add(menuBar, BorderLayout.PAGE_START);
            //mainPanel.setBorder(new EmptyBorder(0,10,10,10));
        }
        else {
            mainPanel.add(scrollChat, BorderLayout.CENTER);
        }
        registeredUserList.setEditable(false);
        userList.setEditable(false);
        chatScreen.setEditable(false);
        
        registeredUserList.setFont(new Font("Courier New", Font.PLAIN, 12));
        chatScreen.setFont(new Font("Courier New", Font.PLAIN, 12));
        chatInput.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        getContentPane().add(mainPanel);
        setSize(800, 500);
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null);
        setTitle("IRC Chatter");
        setMinimumSize(new Dimension(500,200));
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //System.out.println("\n\n\n\n\n\\n\nCLOSEDWINDOW\n\n\n\n\n\n\n\n");
                if(sock.isSafe() == true)
                    sock.outputText("PART " + channelName + "\r\n");
                    //System.out.println("\n\n\n\n\n\\n\nCLOSEDWINDOW\n\n\n\n\n\n\n\n");
            }
        });
        chatInput.addKeyListener(this);
        chatInput.requestFocus();

    }
    public void setWindowTitle(String set) {
        setTitle(set);
    }
    
    public void substarterBegin(String prefix, String userName, String input) {
            if (substarter.start(winNum)) {
                System.out.println("Starting give");
                g_start.setEnabled(false);
                g_end.setEnabled(true);

                registeredUserList.setText("");

                prefix = "<b><font color=blue>[" + userName + "]:</font></b> ";
                input = "Substarter giveaway is starting, make sure to register at http://substarter.com/my-account to participate.  Type !substarter for a chance to win a free sub!";
                sock.outputToChannel(input, channelName);
            }
            else {
                input = "<font color='red'>[WARNING]: Giveaway already started, perhaps in another chat window.</font>";
            }

            try {
                toChatScreen(prefix + input, false);
            } catch (IOException | BadLocationException ex) {
                Logger.getLogger(IRCBOT.class.getName()).log(Level.SEVERE, null, ex);
            }      
    }
    
    public void substarterEnd(String prefix, String userName, String input) {
            System.out.println("ending give");
            if (substarter.end(winNum)) {
                
                g_start.setEnabled(true);
                g_end.setEnabled(false);

                String winner = substarter.returnWinner(winNum);
                
                
                input = "Congrats to the new sub winner: " + winner + "!";
                prefix = "<b><font color=blue>[" + userName + "]:</font></b> ";
                    

                registeredUserList.setText(winner + "\n");

            } else {
                g_start.setEnabled(true);
                g_end.setEnabled(false);
                
                prefix = "<b><font color=blue>[" + userName + "]:</font></b> ";
                input = "No one entered giveaway, ending giveaway.";
            }
            sock.outputToChannel(input, channelName);
            try {
                toChatScreen(prefix + input, false);
            } catch (IOException | BadLocationException ex) {
                Logger.getLogger(IRCBOT.class.getName()).log(Level.SEVERE, null, ex);
            }     
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String prefix = "";
        String userName = settingsPane.returnSetting("IRC_nick");
        String input = "<b><font color=blue>[" + userName + "]:</font></b> " + chatInput.getText();
        
        if (e.getSource() == g_start) {
            
            substarterBegin(prefix, userName, input);
        }
        
        if(e.getSource() == g_end) {
            substarterEnd(prefix, userName, input);
        }
        
        if(e.getSource() == sendButton) {
            
            if(!chatInput.getText().equals("")) {
                
                
                //toChatScreen(input);
                
                //chatScreen.append(input + "\n");
                if(chatInput.getText().startsWith("/")) {
                    if(chatInput.getText().equals("/substart")) {
                        substarterBegin(prefix, userName, input);
                    }
                    if (chatInput.getText().equals("/substartend")) {
                        substarterEnd(prefix, userName, input);
                    }
                }
                else {
                    sock.outputToChannel(chatInput.getText(), channelName);
                    
                    try {
                    //chatter.printScreen();
                    
                    toChatScreen(prefix + input, false);
                    } catch (IOException | BadLocationException ex) {
                        Logger.getLogger(IRCBOT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                chatInput.setText("");
                //chatter.addTo(input);
                
                
            }
            else {
               chatInput.setText(""); 
            }
            chatInput.requestFocus();
        }
        
        
        if(e.getSource() == settings) {
            //makeNewWindow("Window " + frameCounter, JFrame.DISPOSE_ON_CLOSE, 800, 500, 2);
            settingsPane.showPane();
        }
    }
    
    public void toChatScreen(String toScreen, boolean selfSeen) throws IOException, BadLocationException {
        //Timestamp ts = new Timestamp();
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        String ts = format.format(now);
        
        //chatScreen.append(ts + " " + toScreen + "\n");
        
        if (selfSeen)  
            toScreen = "<font color=gray>" + ts + "</font> <font color=red>" + toScreen + "</font>";
        else 
            toScreen = "<font color=gray>" + ts + "</font> " + toScreen;
        
        //chatter.addTo(toScreen);
        kit.insertHTML(doc, doc.getLength(), toScreen, 0, 0, null);
        if(standardWindow) {
            chatScreen.setCaretPosition(chatScreen.getDocument().getLength());
        }

    }
    
    public void setUserList(String list) {
        userList.setText(list);
    }
    
    public void setRegUserList(String list) {
        registeredUserList.setText(list);
    }
    
    public void setChatScreen() {
        chatScreen.setText(chatter.printScreen());
        chatScreen.setCaretPosition(chatScreen.getDocument().getLength());
    }
    
    public void sendToChatArray(String text) {
        chatter.addTo(text);
    }

    
    public static void main(String[] args) throws BadLocationException {
        // TODO code application logic here
        
        ///IRCBOT[] windows = new IRCBOT[3];
        
        ///String[] windowNames = new String[3];
        int chat = 0;
        substarter = new SubstarterGiveaway();
        
        //give_away = false;
        
        try {
            settingsPane.loadSettings();
        } catch (IOException ex) {
            Logger.getLogger(IRCBOT.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        while(settingsPane.returnSetting("IRC_ip").equalsIgnoreCase("")) {
            if(settingsPane.isPaneOpen() == false){
                    settingsPane.showPane();
                    settingsPane.setPaneStatus(true);
            }
        }
        
        JavaToMysql mysql = new JavaToMysql(settingsPane.returnSetting("MySQL_addr"), 
                                            settingsPane.returnSetting("MySQL_port"), 
                                            settingsPane.returnSetting("MySQL_userName"),
                                            settingsPane.returnSetting("MySQL_password"),
                                            settingsPane.returnSetting("MySQL_dbName"));
        
        Vector<String>rUList;
        //rUList = mysql.select();
        
        //for(int i=0; i<rUList.size();i++){
        //    System.out.println(rUList.get(i));
        //}
        
        Vector<IRCBOT> windows = new Vector<IRCBOT>(1);
        windowNames = new Vector<String>(1);

        boolean running = true;
        
        
        
        String server = settingsPane.getServerField();
        
        int port = Integer.parseInt(settingsPane.getPortField());
        String nick = settingsPane.getNickNameField();
        String oauth = settingsPane.getOAuth();
        String channel = JOptionPane.showInputDialog("Greetings " + nick + "!\nEnter channel: ");
        
        ///windowNames[0] = "Raw Data";
        ///windowNames[1] = "#seriousgaming";
        ///windowNames[2] = channel;
        
        
        windowNames.addElement("Raw Data");
        windowNames.addElement("#" + nick);
        windowNames.addElement(channel);
        
        /*
        windows[0] = new IRCBOT(false, windowNames[0]); // setup raw data window
            windows[0].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windows[1] = new IRCBOT(true, windowNames[1]);  // setup first channel window
            windows[1].setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        windows[2] = new IRCBOT(true, windowNames[2]);  // setup first channel window
            windows[2].setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        */
        
        
        
        windows.addElement(new IRCBOT(false, windowNames.get(0), chat));
            //windows.lastElement().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            windows.lastElement().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            substarter.addGiveawayList();
            chat++;
        windows.addElement(new IRCBOT(true, windowNames.get(1), chat));
            windows.lastElement().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            substarter.addGiveawayList();
            chat++;
        windows.addElement(new IRCBOT(true, windowNames.get(2), chat));
            windows.lastElement().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            substarter.addGiveawayList();
            chat++;
            
        for (int x=0; x<windows.size();x++){
            windows.get(x).setTitle("Substarter Client: " + windowNames.get(x));
        }
        
        sock = new connection(server, port, nick, oauth, channel);
        sock.connect();
        Vector<String> compUList = new Vector<String>(1); 
        
        compUList.addElement("");
        compUList.addElement("");
        compUList.addElement("");
        
        //regCompUList = new Vector<String>(1); 
        
        //regCompUList.addElement("");
        //regCompUList.addElement("");
        //regCompUList.addElement("");
        
        IrcParser parse;
        
        while(running) {
            
            
            try {
                parse = new IrcParser(sock.rawFeedback());
                
                String parsed = parse.parsed();
                String code = parse.returnCode();
                String nickName = parse.returnUserInfo();
                String comment = parse.returnMisc();
                String channelParsed = parse.returnChannel();
                
                int nickNameBegin = nickName.indexOf(":")+1;
                int nickNameEnd   = nickName.indexOf("!");
                
                
                if(nickName.contains("!")) {
                    nickName = nickName.substring(nickNameBegin, nickNameEnd);
                }
            
                if(!parsed.equals("null")) {
                    
                    System.out.println(parse.returnRawData());
                    windows.get(0).toChatScreen(parse.parsed(), false);
                    
                    IRC_DeCoder irc = new IRC_DeCoder(sock, channel, nickName, code, comment);
                    
                    String o = irc.decode();
                    
                    if(irc.isUserListing()) {
                        
                        int windowNum = 0;
                        for(int x=0; x<windowNames.size(); x++) {
                            if(windowNames.get(x).equals(channelParsed)) {
                                windowNum = x;
                                break;
                            }
                        }
                        
                        int placer = parse.returnMisc().indexOf(":");
                        //System.out.println(parse.parsed());
                        compUList.setElementAt(compUList.get(windowNum) + parse.returnMisc().substring(placer+1) + " ",windowNum);
                    }
                    else if (irc.isUserListingEnd()) {
                        int windowNum = 0;
                        for(int x=0; x<windowNames.size(); x++) {
                            if(windowNames.get(x).equals(channelParsed)) {
                                windowNum = x;
                                break;
                            }
                        }
                        
                        UserList listPane = new UserList(compUList.get(windowNum));
                        windows.get(windowNum).setUserList(listPane.returnUserList());
                    }
                    else if((code.equals("JOIN")) && (!nickName.equals(nick))) {
                        int windowNum = 0;
                        for(int x=0; x<windowNames.size(); x++) {
                            if(windowNames.get(x).equals(channelParsed)) {
                                windowNum = x;
                                break;
                            }
                        }
                        
                        System.out.println("[" + channelParsed + "] [list+joined] " + compUList.get(windowNum));
                        
                        if(!compUList.get(windowNum).contains(nickName)) {
                            compUList.setElementAt(compUList.get(windowNum) + nickName + " ", windowNum);
                            UserList listPane = new UserList(compUList.get(windowNum));
                            windows.get(windowNum).setUserList(listPane.returnUserList());
                        }
                        
                        //compUList.setElementAt(compUList.get(windowNum) + parse.returnMisc().substring(placer+1) + " ",windowNum);
                    }
                    
                    else if (code.equals("PART")) {
                        int windowNum = 0;
                        for(int x=0; x<windowNames.size(); x++) {
                            if(windowNames.get(x).equals(channelParsed)) {
                                windowNum = x;
                                break;
                            }
                        }
                        
                        System.out.println("[" + channelParsed + "] [list+parted] " + compUList.get(windowNum));
                        
                        if(compUList.get(windowNum).contains(nickName)) {
                            compUList.setElementAt(compUList.get(windowNum).replace(nickName + " ", ""),windowNum);
                            UserList listPane = new UserList(compUList.get(windowNum));
                            windows.get(windowNum).setUserList(listPane.returnUserList());
                        }
                    }
                    
                    else if(!o.equals("")) {
                        //String channelName = "";
                        int dataBreak = 0;
                        int windowNum = 0;
                        
                        //dataBreak = comment.indexOf(" :");
                        //channelName = comment.substring(0, dataBreak);
                        
                        for(int x=0; x<windowNames.size(); x++) {
                            if(windowNames.get(x).equals(channelParsed)) {
                                windowNum = x;
                                break;
                            }
                        }
                        if(windowNum != 0) {
                            if((o.toLowerCase().contains(nick.toLowerCase())) && (!nickName.toLowerCase().contains(nick.toLowerCase()))) {
                                windows.get(windowNum).toChatScreen(o,true);
                            }
                            else {
                                windows.get(windowNum).toChatScreen(o,false);
                            }
                            
                            if(code.equals("PRIVMSG") && comment.contains("!substarter") && substarter.isGiveawayActive(windowNum)) {
                                if(!substarter.returnListString(windowNum).contains(nickName)) {
                                    if(mysql.selectuser(nickName.toLowerCase())) {
                                        ///regCompUList.setElementAt(regCompUList.get(windowNum) + nickName + " ", windowNum);
                                        
                                        substarter.addUserToGiveaway(windowNum, nickName);
                                        
                                        UserList listPane = new UserList(substarter.returnListString(windowNum));
                                        windows.get(windowNum).setRegUserList(listPane.returnUserList());
                                        sock.outputToChannel("Added " + nickName + "!", "#substarterbottest");
                                    }
                                    else {
                                        sock.outputToChannel(nickName + " Please register at http://substarter.com/my-account to participate in giveaway!  If you have registered, please make sure to create a funding page!", windowNames.get(windowNum));
                                    }
                                }
                            }
                           
                            if(!compUList.get(windowNum).contains(nickName)) {
                                compUList.setElementAt(compUList.get(windowNum) + nickName + " ", windowNum);
                                UserList listPane = new UserList(compUList.get(windowNum));
                                windows.get(windowNum).setUserList(listPane.returnUserList());
                            }
                            
                        }
                    }
                    
                    else if (nickName.equals("PING")) {
                        windows.get(0).toChatScreen("\r\n\r\n\r\nPONG\r\n\r\n", false);
                        //sock.outputText("/NAMES\r\n");
                    }
                    
                    
                }
                else
                    running = false;
            } catch (IOException ex) {
                System.out.println("\n\n\n\nMain ERROR: " + ex + "\n\n\n\n\n\n");
                running = false;
            }
        }
        try {
            sock.closeSock();
        } catch (IOException ex) {
            System.out.println("\n\n\n\nsockClose ERROR: " + ex + "\n\n\n\n\n\n");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        ;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        ;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            String prefix = "";
            if(!chatInput.getText().equals("")) {
                String userName = settingsPane.returnSetting("IRC_nick");
                String input = "<b><font color=blue>[" + userName + "]:</font></b> " + chatInput.getText();
                
                //toChatScreen(input);
                
                //chatScreen.append(input + "\n");
                if(chatInput.getText().startsWith("/")) {
                    if(chatInput.getText().equals("/substart")) {
                        substarterBegin(prefix, userName, input);
                    }
                    if (chatInput.getText().equals("/substartend")) {
                        substarterEnd(prefix, userName, input);
                    }
                }
                else {
                    
                    try {
                    //chatter.printScreen();
                    
                        toChatScreen(prefix + input, false);
                    } catch (IOException | BadLocationException ex) {
                        Logger.getLogger(IRCBOT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    sock.outputToChannel(chatInput.getText(), channelName);
                }
                chatInput.setText("");
                //chatter.addTo(input);
                
                
            }
            else {
               chatInput.setText(""); 
            }
            chatInput.requestFocus();
        }
    }


}

/*---------------------------- older irc decoder -----------------------------*/
////////////////////////////////////////////////////////////////////////////////
//                    if(parse.returnCode().equals("353")){
//                        int placer = parse.returnMisc().indexOf(":");
//
//                        compUList = compUList + parse.returnMisc().substring(placer+1) + " ";
//                    }
//                    else if(parse.returnCode().equals("366")){
//                        UserList listPane = new UserList(compUList);
//                        s.setUserList(listPane.returnUserList());
//                    }
//                    else if(parse.returnCode().equals("PRIVMSG")) {
//                        String nickName = parse.returnUserInfo();
//                        String comment  = parse.returnMisc();
//                        int dataBreakPoint;
//
//                        if(nickName.contains("!")) {
//                            dataBreakPoint = nickName.indexOf("!");
//                            nickName = nickName.substring(1,dataBreakPoint);
//                        }
//                        dataBreakPoint = comment.indexOf(":");
//                        comment = comment.substring(dataBreakPoint+1);
//
//                        chatter.addTo("[" + nickName + "]: " + comment);
//                        //s.toChatScreen("[" + nickName + "]: " + comment);
//                    }
////////////////////////////////////////////////////////////////////////////////

/*---------------------------- old irc decoder -------------------------------*/
////////////////////////////////////////////////////////////////////////////////
//                    switch(parse.returnCode()) {
//                        case "001":
//                            s.toChatScreen(parse.returnMisc());
//                            break;
//                        case "002":
//                            s.toChatScreen(parse.returnMisc());
//                            break;
//                        case "003":
//                            s.toChatScreen(parse.returnMisc());
//                            break;
//                        case "004":
//                            sock.outputText("JOIN " + channel + "\r\n");
//                            s.toChatScreen(parse.returnMisc());
//                            break;
//                        case "353":
//                            int placer = parse.returnMisc().indexOf(":");
//
//                            compUList = compUList + parse.returnMisc().substring(placer+1) + " ";
//                            break;
//                        case "366":
//                            UserList listPane = new UserList(compUList);
//                            s.setUserList(listPane.returnUserList());
//                            break;
//                        case "PRIVMSG":
//                            //String nickName = parse.returnUserInfo();
//                            //String comment  = parse.returnMisc();
//                            int dataBreakPoint;
//
//                            if(nickName.contains("!")) {
//                                dataBreakPoint = nickName.indexOf("!");
//                                nickName = nickName.substring(1,dataBreakPoint);
//                            }
//                            dataBreakPoint = comment.indexOf(":");
//                            comment = comment.substring(dataBreakPoint+1);
//
//                            s.toChatScreen("<b><font color=green>[" + nickName + "]:</font></b> " + comment + "</td></tr>");
//                            //s.toChatScreen("[" + nickName + "]: " + comment);
//                            break;
//                            
//                        default:
//                            
//                    }
//                    
//
//                    if (parse.returnUserInfo().equals("PING")) {
//                        sock.outputText("PONG " + parse.returnCode());
//                        s.toChatScreen("PONG " + parse.returnCode());
//                    }
////////////////////////////////////////////////////////////////////////////////