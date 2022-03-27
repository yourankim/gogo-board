package com.youran.gogoboard.post;

import lombok.Data;

@Data
public class PageVO {
	
	private int limit = 10;
	private int page;
	private int offset;
	
	public void setPage(int page) {
		this.page = page;
		this.offset = limit * page;
		
	}
	
	

}
