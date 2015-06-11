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
    private Order seller;
    private Order buyer;
    private int count;
    private long time;

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
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * @return the seller
     */
    public Order getSeller() {
        return seller;
    }

    /**
     * @param seller the seller to set
     */
    public void setSeller(Order seller) {
        this.seller = seller;
    }

    /**
     * @return the buyer
     */
    public Order getBuyer() {
        return buyer;
    }

    /**
     * @param buyer the buyer to set
     */
    public void setBuyer(Order buyer) {
        this.buyer = buyer;
    }

}
