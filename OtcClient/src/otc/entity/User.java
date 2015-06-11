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
public class User implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    private String traderName;
    private String userName;

    public User(String traderName, String userName)
    {
        this.traderName = traderName;
        this.userName = userName;
    }
    
    /**
     * @return the traderName
     */
    public String getTraderName() {
        return traderName;
    }

    /**
     * @param traderName the traderName to set
     */
    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
}
