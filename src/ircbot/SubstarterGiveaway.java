/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircbot;

import static java.util.Collections.sort;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author Garrett
 */



public class SubstarterGiveaway {
    private Vector<Boolean> giveaway_active;
    private Vector<String> winners;    
    private Vector<String>giveawayLists; 
    
    public SubstarterGiveaway() { 
        // active giveaways
        giveaway_active = new Vector<Boolean>(1);
        // giveaway winners
        winners         = new Vector<String>(1);
        // giveaway user lists
        giveawayLists   = new Vector<String>(1);
        
    }
    
    public void addGiveawayList() {
        giveaway_active.addElement(false);
        winners.addElement(null);
        giveawayLists.addElement("");
    }
    
    public boolean start(int windowNum) {
        // starts a new giveaway for window x
        if(!isGiveawayActive(windowNum)) {
            
            // change giveaway to true
            changeGiveawayStatus(windowNum);
            
            // clear giveaway list
            clearGiveawayList(windowNum);
            resetWinner(windowNum);
            
            return true;
        }
        else {
            
        }
        return false;
    }
    
    public boolean end(int windowNum) {
        // ends a current giveaway for window x
        
        if(isGiveawayActive(windowNum)) {
            
            // change giveaway to false
            changeGiveawayStatus(windowNum);
            
            if(pickWinner(windowNum))
                return true;
            else 
                return false;
        }
        
        return false;
    }
    
    public boolean isGiveawayActive(int windowNum) {
        // checks to see if window x currectly has an active giveaway
        return giveaway_active.get(windowNum);
    }
    
    public boolean addUserToGiveaway(int windowNum, String user) {
        // add user to giveaway, typically after checking mysql db
        giveawayLists.setElementAt(giveawayLists.get(windowNum) + user + " ", windowNum);
        return false;
    }
    
    public String returnListString(int windowNum) {
        // returns giveaway list for window x
        return giveawayLists.get(windowNum);
    }
    
    public String returnWinner(int windowNum) {
        // returns the winner from window x
        return winners.get(windowNum);
    }
    
    public void changeGiveawayStatus(int windowNum) {
        if(isGiveawayActive(windowNum)) {
            giveaway_active.setElementAt(false, windowNum);
        }
        else {
            giveaway_active.setElementAt(true, windowNum);
        }
    }
    
    private boolean pickWinner(int windowNum) {
        // picks a random winner for window x
        
        Vector<String> uList = new Vector<String>(1);
        int dataBreakOld = 0;
        int dataBreak = 0;
        String info;
        
        // get current list
        String compiledList = returnListString(windowNum);
        // break up list into Vector
        while (dataBreakOld < (compiledList.length() - 1)) {
            dataBreak = compiledList.indexOf(" ", dataBreakOld);
            
            info = compiledList.substring(dataBreakOld, dataBreak);
            dataBreakOld = dataBreak + 1;
            uList.add(info);
        }
        
        // sort list
        sort(uList);
        
        // if ppl entered...
        if (uList.size() > 0) {
            // select random
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(uList.size());
            // actually select winner and enter into vector
            winners.setElementAt(uList.get(randomInt), windowNum);
            return true;
        }
        //no one entered
        else {
            return false;
        }
        
        //winners.get(windowNum);
        
        //return false;
    }
    
    private void clearGiveawayList(int windowNum) {
        giveawayLists.setElementAt("",windowNum);
    }
    
    private void resetWinner(int windowNum) {
        winners.setElementAt("",windowNum);
    }
}
