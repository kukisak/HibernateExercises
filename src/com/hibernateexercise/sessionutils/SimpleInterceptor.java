package com.hibernateexercise.sessionutils;

import java.io.Serializable;
import java.util.Iterator;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.hibernateexercise.beans.Employee;

public class SimpleInterceptor extends EmptyInterceptor
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3985634357468831103L;

	@Override
	public void onDelete(Object entity,
			Serializable id,
			Object[] state,
			String[] propertyNames,
			Type[] types)
	{
		System.out.println("Delete operation");
	}
	
	@Override
	public boolean onFlushDirty(Object entity,
			Serializable id,
			Object[] currentState,
			Object[] previousState,
			String[] propertyNames,
			Type[] types)
	{
		if (entity instanceof Employee)
		{
			System.out.println("Update operation");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) 
	{
		return true;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) 
	{
		if (entity instanceof Employee) {
			System.out.println("Create operation");
			return true;
		}
		return false;
	}

	@Override
	public void postFlush(Iterator entities) 
	{
		System.out.println("postFlush");
	}

	@Override
	public void preFlush(Iterator entities) 
	{
		System.out.println("preFlush");
	}

	
	
	
	
	
}
