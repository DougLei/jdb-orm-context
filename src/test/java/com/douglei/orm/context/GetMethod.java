package com.douglei.orm.context;

import com.douglei.orm.context.transaction.component.Transaction;

public class GetMethod {
	
	public void public_method() {
		
	}
	
	@Transaction
	protected void protected_method() {
		
	}
	
	@Transaction
	private void private_method() {
		
	}
	
	void default_method() {
		
	}
}
