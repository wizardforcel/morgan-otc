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
public class TradeResultInfo extends ResultInfo
{
    private static final long serialVersionUID = 1L;
    
    private History tradeRecord;
    private int orderId;
    
    public TradeResultInfo(int errno, String errmsg, int orderId, History tradeRecord)
    {
        super(errno, errmsg);
        this.orderId = orderId;
        this.tradeRecord = tradeRecord;
    }

    /**
     * @return the tradeRecord
     */
    public History getTradeRecord() {
        return tradeRecord;
    }

    /**
     * @param tradeRecord the tradeRecord to set
     */
    public void setTradeRecord(History tradeRecord) {
        this.tradeRecord = tradeRecord;
    }

    /**
     * @return the orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
}
