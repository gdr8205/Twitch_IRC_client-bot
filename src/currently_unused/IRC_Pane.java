/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currently_unused;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
/**
 *
 * @author Garrett
 */
public final class IRC_Pane extends JPanel implements ActionListener{
    private JTextArea  chatInput;
    private JTextPane  chatScreen;
    private JButton    sendButton;
    private JTextPane  userList;
    private JTextPane  registeredUserList;
    
    private String channelName;
    boolean standardWindow;
    
    private JButton g_start;
    private JButton g_end;
    private JButton g_reroll;
    
    private HTMLEditorKit kit = new HTMLEditorKit(); // needs to be relocated
    private HTMLDocument doc = new HTMLDocument();   // ''
    
    
    public IRC_Pane(boolean uList, String channel, int chatNum) {
        this.setLayout(new BorderLayout(5,5));
        Dimension min = new Dimension(100,1);
        Dimension pref = new Dimension(150,1);
        Dimension max = new Dimension(600,600);
        
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
        JLabel l_chat = new JLabel(channelName + " Chat", JLabel.CENTER);
        JLabel l_uList = new JLabel("User List", JLabel.CENTER);
        //JLabel l_title = new JLabel(channelName, JLabel.CENTER);
        
        //this.add(l_title, BorderLayout.PAGE_START);
        
        sendButton.addActionListener(this);
        g_start.addActionListener(this);
        g_end.addActionListener(this);
        g_reroll.addActionListener(this);
        
        g_start.setEnabled(true);
        g_end.setEnabled(false);
        g_reroll.setEnabled(false);
        
        JPanel left                     = new JPanel(new BorderLayout(5,5));
        JPanel mid                      = new JPanel(new BorderLayout(5,5));
        JPanel right                    = new JPanel(new BorderLayout(5,5));
        JPanel giveaway_buttons         = new JPanel(new BorderLayout(5,5));
        
        JPanel sendBar                  = new JPanel();
        JScrollPane scrollChat          = new JScrollPane(chatScreen);
        JScrollPane scrollChatInput     = new JScrollPane(chatInput);
        JScrollPane userListScroll      = new JScrollPane(userList);
        JScrollPane regUserListScroll   = new JScrollPane(registeredUserList);
        
        userListScroll.setMinimumSize(min);
        scrollChat.setMinimumSize(min);
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
           //chat_userList.setPreferredSize(new Dimension(500,100));
            chat_userList.setMinimumSize(min);
            //chat_userList.setMaximumSize(max);
        
        userListScroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        //scroll.add(chatScreen);
        
        sendBar.add(scrollChatInput);
        sendBar.add(sendButton);
        
        if(standardWindow) {
            
            this.add(chat_userList, BorderLayout.CENTER);
            this.add(sendBar, BorderLayout.PAGE_END);
            
            left.add(l_giveaway, BorderLayout.NORTH);
            left.add(regUserListScroll, BorderLayout.CENTER);
            ///left.setBorder(new EmptyBorder(0,5,0,0));
            ///chat_userList.setBorder(new EtchedBorder(0,0,0,5));
            this.add(left, BorderLayout.WEST);
            ///mainPanel.setBorder(new EmptyBorder(0,10,10,10));
        }
        else {
            this.add(scrollChat, BorderLayout.CENTER);
        }
        
        registeredUserList.setEditable(false);
        userList.setEditable(false);
        chatScreen.setEditable(false);
        
        registeredUserList.setFont(new Font("Courier New", Font.PLAIN, 12));
        chatScreen.setFont(new Font("Courier New", Font.PLAIN, 12));
        chatInput.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        //this.setPreferredSize(new Dimension(800,500));
    }
    
    public static void main(String[] args) {
        IRC_Pane a = new IRC_Pane(true, "#name", 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

// makeNewWindow("Window " + frameCounter, JFrame.DISPOSE_ON_CLOSE, 800, 500, 2);