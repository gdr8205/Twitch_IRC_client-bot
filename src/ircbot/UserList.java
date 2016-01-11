/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircbot;

import java.util.ArrayList;
import static java.util.Collections.sort;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Garrett
 */
public class UserList {
    List uList = new ArrayList();
    
    int userCounter;
    
    public UserList(String compiledList) {
        boolean running = true;
        
        int dataBreakOld = 0;
        int dataBreak = 0;
        String info;
        
        userCounter = 0;
        
        while(dataBreakOld < (compiledList.length()-1)) {
            dataBreak = compiledList.indexOf(" ", dataBreakOld);
            
            info = compiledList.substring(dataBreakOld, dataBreak);
            dataBreakOld = dataBreak+1;
            //System.out.println(info);
            uList.add(info);
        }
        
        sort(uList);
    }
    
    public String returnUserList() {
        String end = "";
        Iterator iterate = uList.iterator();
        
        while(iterate.hasNext()) {
                end = end + iterate.next();
                if(iterate.hasNext())
                    end = end + "\n";
        }

        return end;
    }
}
