/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircbot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Garrett
 */
public class ChatDisplay {
    //Stores and returns chat screen info
    String out;
    private List cList = new ArrayList();
    
    public ChatDisplay(String beginningLines) {
        //setup
        if(!beginningLines.equals("")) {
            cList.add(beginningLines);
        }
    }
    
    public void addTo(String text) {
        Date now = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm");
        String ts = format.format(now);
        cList.add("<tr><td bgcolor=red><font color=gray>" + ts + "</font>" + " " + text);
    }
    
    public List out() {
        return cList;
    }
    
    public String printScreen() {
        String ending = "";
        
        Iterator iterate = cList.iterator();
        
        while(iterate.hasNext()) {
            ending = ending + iterate.next();
        }
        
        return ending;
    }
    
}
