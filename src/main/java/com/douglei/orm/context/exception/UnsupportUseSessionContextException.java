package com.douglei.orm.context.exception;

import com.douglei.orm.context.SessionContext;
import com.douglei.orm.context.SimpleSessionContext;
import com.douglei.orm.context.transaction.component.Transaction;

/**
 * 不支持的使用SessionContext异常
 * @author DougLei
 */
public class UnsupportUseSessionContextException extends RuntimeException{
	private static final long serialVersionUID = -2218855071627407626L;

	public UnsupportUseSessionContextException() {
		super("没有扫描到任何[" + Transaction.class.getName() + "]修饰的方法, 所有不支持使用["+SessionContext.class.getName()+"]获取session, 请检查相关的[@Transaction]配置和扫描配置; 或者使用["+SimpleSessionContext.class.getName()+"]获取session, 并手动处理session的commit/rollback/close等");
	}
}
