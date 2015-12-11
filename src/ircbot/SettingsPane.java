/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircbot;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import layout.SpringUtilities;

/**
 *
 * @author Garrett
 */
public class SettingsPane extends Settings implements ActionListener {
    JFrame     mainFrame;
    //JTextField server;
    //JTextField port;
    //JTextField nickName;
    //JTextField defaultChan;
    JButton    okButton;
    JButton    cancelButton;
    
    JTextField[] textField;
    
    String[] labels;
    int labelNum;
    
    boolean settingsPaneOpen;
    
    public SettingsPane() {
        //JLabel servText = new JLabel("Server", JLabel.CENTER);
        labels = new String[]{"IRC Server: ", 
                              "IRC Port: ", 
                              "IRC Username: ", 
                              "IRC OAuth Key: ", 
                              "IRC Default Channel: ", 
                              "MySQL Server",
                              "MySQL Port",
                              "MySQL Username",
                              "MySQL Password",
                              "MySQL Database",
        };
        labelNum = labels.length;
        
        settingsPaneOpen = false;
        
    }
    
    public void showPane() {
        
        textField = new JTextField[labelNum];
        
        //server = new JTextField(super.server);
        //port   = new JTextField(super.port);
        //nickName = new JTextField(super.nickName);
        //defaultChan = new JTextField(super.defaultChannel);
        
        mainFrame = new JFrame();
        cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(this);
        okButton = new JButton("Apply");
            okButton.addActionListener(this);
        
        JPanel form              = new JPanel(new SpringLayout());
        JPanel buttons           = new JPanel();
        JPanel mainPanel         = new JPanel(new BorderLayout(5,5));
        
        buttons.add(cancelButton);
        buttons.add(okButton);
        
        for(int i=0; i < labelNum; i++) {
            JLabel l = new JLabel(labels[i], JLabel.TRAILING);
            form.add(l);
            if((i == 3)||(i == 8))  
                textField[i] = new JPasswordField(super.settings[i]);
            else 
                textField[i] = new JTextField(super.settings[i]);
            textField[i].setEditable(false);
            l.setLabelFor(textField[i]);
            form.add(textField[i]);
        }
        
        //SpringUtilities some = new SpringUtilities();
        
        SpringUtilities.makeCompactGrid(form,
                labelNum, 2,
                10,10,
                10,10);
        
        mainPanel.add(form, BorderLayout.CENTER);
        mainPanel.add(buttons, BorderLayout.PAGE_END);
        
        /*
        mainPanel.add(servText);
        mainPanel.add(server);
        mainPanel.add(port);
        mainPanel.add(nickName);
        mainPanel.add(defaultChan);
        mainPanel.add(cancelButton);
        mainPanel.add(okButton);
        */
        
        mainFrame.getContentPane().add(mainPanel);
        mainFrame.setSize(400, 600);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setTitle("Settings");
    }
    
    public void setPaneStatus(boolean status) {
        settingsPaneOpen = status;
    }
    
    public boolean isPaneOpen() {
        return settingsPaneOpen;
    }
    
    public String getServerField() {
        return settings[0];
    }
    
    public String getPortField() {
        return settings[1];
    }
    
    public String getNickNameField() {
        return settings[2];
    }
    
    public String getOAuth() {
        return settings[3];
    }
    
    public String getDefaultChanField() {
        return settings[4];
    }
    
    public void setServerField() {
        settings[0] = textField[0].getText();
    }
    
    public void setPortField() {
        settings[1] = textField[1].getText();
    }
    
    public void setNickNameField() {
        settings[2] = textField[2].getText();
    }
    
    public void setOAuth() {
        settings[3] = textField[3].getText();
    }
    
    public void setDefaultChanField() {
        settings[4] = textField[4].getText();
    }
    
    public static void main(String[] args) {
        SettingsPane a = new SettingsPane();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cancelButton) {
            setPaneStatus(false);
            mainFrame.dispose();
        }
        else if (e.getSource() == okButton) {
            setServerField();
            setPortField();
            setNickNameField();
            setOAuth();
            setDefaultChanField();
            setPaneStatus(false);
            mainFrame.dispose();
        }
    }
}
