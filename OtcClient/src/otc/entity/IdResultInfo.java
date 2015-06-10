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
public class IdResultInfo extends ResultInfo
{
    private static final long serialVersionUID = 1L;
    
    private int id;
    
    public IdResultInfo(int errno, String errmsg, int id)
    {
        super(errno, errmsg);
        this.id = id;
    }

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
    
}
