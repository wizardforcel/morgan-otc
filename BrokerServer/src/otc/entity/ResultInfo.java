package otc.entity;

import java.io.Serializable;

public class ResultInfo implements Serializable 
{

	private static final long serialVersionUID = 1L;
	
	private int errno;
	private String errmsg;
	
	public ResultInfo(int errno_, String errmsg_)
	{
		errno = errno_;
		errmsg = errmsg_;
	}
	
	public int getErrno() { return errno; }
	public void setErrno(int errno_) { errno = errno_; }
	public String getErrmsg() { return errmsg; }
	public void setErrmsg(String errmsg_) { errmsg = errmsg_; }
}
