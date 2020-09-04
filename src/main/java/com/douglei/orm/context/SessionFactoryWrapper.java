package com.douglei.orm.context;

import com.douglei.orm.sessionfactory.SessionFactory;

/**
 * {@link SessionFactory} 的包装
 * @author DougLei
 */
class SessionFactoryWrapper {
	private SessionFactory sessionFactory;
	private SessionFactoryRegistrationFeature feature;
	
	public SessionFactoryWrapper(SessionFactory sessionFactory, SessionFactoryRegistrationFeature feature) {
		this.sessionFactory = sessionFactory;
		this.feature = feature;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public SessionFactoryRegistrationFeature getFeature() {
		return feature;
	}
	public void setFeature(SessionFactoryRegistrationFeature feature) {
		this.feature = feature;
	}
}
