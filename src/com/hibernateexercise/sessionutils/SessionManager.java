package com.hibernateexercise.sessionutils;

import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.hibernateexercise.beans.BatchEmployee;
import com.hibernateexercise.beans.Employee;
import com.hibernateexercise.beans.OldEmployee;

public class SessionManager {
	private static SessionFactory factory = null;
	/**
	 * Same as the JDBC batch_size parameter.
	 */
	public static final int BATCH_SIZE = 50;
	
	private SessionManager(){}
	
	public static Session getSession()
	{
		return getSession(null);
	}
	
	public static Session getSession(Interceptor interceptor)
	{
		if (factory == null)
		{
			try {
				factory = new Configuration().configure().
						addAnnotatedClass(Employee.class).
						addAnnotatedClass(OldEmployee.class).
						addAnnotatedClass(BatchEmployee.class).
						buildSessionFactory();
			} catch (Throwable e) {
				System.err.println("Failed tocreate session factory object" + e);
				throw new ExceptionInInitializerError(e);				
			}
		}
		if (interceptor == null)
		{
			return factory.openSession();
		}
		else
		{
			return factory.withOptions().interceptor(interceptor).openSession();
		}
		
	}
	
}
