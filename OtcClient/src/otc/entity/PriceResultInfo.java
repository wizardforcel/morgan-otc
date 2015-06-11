/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package otc.entity;

/**
 *
 * @author Wizard
 */
public class PriceResultInfo extends ResultInfo
{
    private static final long serialVersionUID = 1L;
    
    private int price;
    
    public PriceResultInfo(int errno, String errmsg, int price)
    {
        super(errno, errmsg);
        this.price = price;
    }

    /**
     * @return the id
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param id the id to set
     */
    public void setPrice(int price) {
        this.price = price;
    }
    
}
