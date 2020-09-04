package com.douglei.orm.context;

import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * {@link SessionFactory} 的注册特性
 * @author DougLei
 */
public class SessionFactoryRegistrationFeature {
	private boolean allowRemove;
	private boolean allowDestroy;

	/**
	 * 
	 * @param allowRemove 注册的SessionFactory是否允许被移除, 如果允许被销毁, 则一定允许被移除
	 * @param allowDestroy 注册的SessionFactory是否允许被销毁
	 */
	public SessionFactoryRegistrationFeature(boolean allowRemove, boolean allowDestroy) {
		this.allowRemove = allowDestroy?allowDestroy:allowRemove;
		this.allowDestroy = allowDestroy;
	}

	public boolean isAllowRemove() {
		return allowRemove;
	}
	public boolean isAllowDestroy() {
		return allowDestroy;
	}

	@Override
	public String toString() {
		return "SessionFactoryRegistrationFeature [allowRemove=" + allowRemove + ", allowDestroy=" + allowDestroy + "]";
	}
}
