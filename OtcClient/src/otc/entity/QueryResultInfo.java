package otc.entity;


public class QueryResultInfo<T> extends ResultInfo 
{

	private static final long serialVersionUID = 1L;
	
	private T[] list;
	
	public QueryResultInfo(int errno_, String errmsg_, T[] list_) 
	{
		super(errno_, errmsg_);
		list = list_;
	}
	
	public T[] getList() { return list; }
	public void setList(T[] list_) { list = list_; }
}
