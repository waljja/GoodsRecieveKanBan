package ht.util;
import java.io.Serializable;
import java.util.List;

public class QueryResult<T> implements Serializable{
	private static final long serialVersionUID = 989481107713026338L;
	
	private List<T> resultlist;
	private long totalrecord;

	public List<T> getResultlist() {
		return resultlist;
	}

	public void setResultlist(List<T> resultlist) {
		this.resultlist = resultlist;
	}

	public long getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(long totalrecord) {
		this.totalrecord = totalrecord;
	}
}
