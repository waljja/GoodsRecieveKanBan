package ht.util;

import java.util.List;

/**
 * 封装分页信息
 * 
 * @author michelle chan
 * 
 * @param <E>
 */
public class PageModel<E>
{
	/** 结果集 **/
	private List<E> list;

	/** 总记录数 **/
	private int totalRecords;

	/** 当前页 **/
	private int pageNo;

	/** 每页显示记录数 **/
	private int pageSize;

	public PageModel(){
	}

	public PageModel(int pageNo, int pageSize){
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	/**
	 * 总页数
	 * 
	 * @return
	 */
	public int getTotalPages(){
		return (totalRecords + pageSize - 1) / pageSize;
	}

	/**
	 * 首页
	 * 
	 * @return
	 */
	public int getTopPage(){
		return 1;
	}

	/**
	 * 上一页
	 * 
	 * @return
	 */
	public int getPreviousPage(){
		if (pageNo <= 1){
			return 1;
		}
		return pageNo - 1;
	}

	/**
	 * 下一页
	 * 
	 * @return
	 */
	public int getNextPage(){
		if (pageNo >= getBottomPage()){
			return getBottomPage();
		}
		return pageNo + 1;
	}

	/**
	 * 尾页
	 * 
	 * @return
	 */
	public int getBottomPage(){
		return getTotalPages();
	}

	public List<E> getList(){
		return list;
	}

	public void setList(List<E> list){
		this.list = list;
	}

	public int getTotalRecords(){
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords){
		this.totalRecords = totalRecords;
	}

	public int getPageNo(){
		return pageNo;
	}

	public void setPageNo(int pageNo){
		this.pageNo = pageNo;
	}

	public int getPageSize(){
		return pageSize;
	}

	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}
}
