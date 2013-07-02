package com.hibernateexercise.sessionutils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SimpleConnection implements IConnection {

	@Override
	public void accept(IConnectionVisitor visitor) 
	{
		Session session = SessionManager.getSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			visitor.visit(session);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
			{
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}
