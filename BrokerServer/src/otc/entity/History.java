/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package otc.entity;

import java.io.Serializable;

/**
 *
 * @author Wizard
 */
public class History implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private int id;
    private Good good;
    private String trader;
    private String user;
    private int count;
    private int price;
    private long timestamp;     

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the good
     */
    public Good getGood() {
        return good;
    }

    /**
     * @return the trader
     */
    public String getTrader() {
        return trader;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param good the good to set
     */
    public void setGood(Good good) {
        this.good = good;
    }

    /**
     * @param trader the trader to set
     */
    public void setTrader(String trader) {
        this.trader = trader;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
}
