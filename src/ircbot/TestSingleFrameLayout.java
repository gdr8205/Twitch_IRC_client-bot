/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircbot;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Garrett
 */
public class TestSingleFrameLayout extends JFrame implements ActionListener{
    
    private JPanel mainPanel;
    
    private Vector<JComponent>panes;
    private static JTabbedPane tabbedPane;
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem settings;
    private JMenuItem connect;
    private JMenuItem disconnect_m;
    private JMenuItem exit_m;
    private JMenu infoMenu;
    private JMenuItem debug_m;
    private JMenuItem about_m;
    
    boolean connected = false;
    
    public TestSingleFrameLayout() {
        URL iconURL = getClass().getResource("P_300x300.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());
        
        mainPanel    = new JPanel(new BorderLayout(5,5));
        
        // menu ---------------------------------------------------
        menuBar        = new JMenuBar();
        fileMenu          = new JMenu("File");
        settings      = new JMenuItem("Settings");
        connect       = new JMenuItem("Connect");
        disconnect_m  = new JMenuItem("Disconnect");
        exit_m        = new JMenuItem("Exit");
        
        infoMenu          = new JMenu("Info");
        debug_m       = new JMenuItem("Debug Window");
        about_m       = new JMenuItem("About");
        
        disconnect_m.setEnabled(false);
        
        settings.addActionListener(this);
        connect.addActionListener(this);
        disconnect_m.addActionListener(this);
        exit_m.addActionListener(this);
        debug_m.addActionListener(this);
        about_m.addActionListener(this);
        
        infoMenu.add(about_m);
        infoMenu.add(debug_m);
        fileMenu.add(settings);
        fileMenu.add(connect);
        fileMenu.add(disconnect_m);
        fileMenu.add(exit_m);
        menuBar.add(fileMenu);
        menuBar.add(infoMenu);
        
        mainPanel.add(menuBar, BorderLayout.PAGE_START);
        
        disconnect_m.setVisible(false);
        // end menu -----------------------------------------------
        
        // tabs ---------------------------------------------------
        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        panes = new Vector<JComponent>(1);
        // end tabs -----------------------------------------------
        
        getContentPane().add(mainPanel);
        setSize(900, 600);
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null);
        setTitle("Substarter");
        setMinimumSize(new Dimension(500,400));
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void addPane(String name, String label) {
        panes.addElement(new IRC_Pane(true, name, 0));
        tabbedPane.addTab(name, null, panes.lastElement(), label);
    }
    
    public static void main(String[] args) {
        TestSingleFrameLayout a = new TestSingleFrameLayout();
        a.addPane("#psynaps","test tab 1");
        a.addPane("#substarterbottest","test tab 1");
        a.addPane("Raw Data","test tab 1");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == exit_m) {
            //makeNewWindow("Window " + frameCounter, JFrame.DISPOSE_ON_CLOSE, 800, 500, 2);
            //settingsPane.showPane();
            System.exit(0);
        }
        else if (e.getSource() == connect) {
            if(!connected) {
                connect.setEnabled(false);
                disconnect_m.setEnabled(true);
                connect.setVisible(false);
                disconnect_m.setVisible(true);
                connected = true;
            }
        }
        else if (e.getSource() == disconnect_m) {
            if(connected) {
                connect.setEnabled(true);
                disconnect_m.setEnabled(false);
                connect.setVisible(true);
                disconnect_m.setVisible(false);
                connected = false;
            }
        }
    }
}
