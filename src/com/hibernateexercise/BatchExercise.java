package com.hibernateexercise;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import com.hibernateexercise.beans.BatchEmployee;
import com.hibernateexercise.sessionutils.SimpleConnection;
import com.hibernateexercise.sessionutils.IConnectionVisitor;
import com.hibernateexercise.sessionutils.SessionManager;

public class BatchExercise {

	public void runExercise()
	{
		testInsertEmployees();
//		testUpdateEmployees();
	}
	
	public void testInsertEmployees()
	{
		class visitor implements IConnectionVisitor
		{
			@Override
			public void visit(Session session) {
				for (int i = 0; i < 100000; i++) {
					BatchEmployee employee = new BatchEmployee("first"+i, "last"+i, i*10);
					session.save(employee);
					if (i % SessionManager.BATCH_SIZE == 0)
					{
						// flush a batch of inserts and release memory
						session.flush();
						session.clear();
//						System.out.println("i: " + i);
					}
				}
			}

		}
		new SimpleConnection().accept(new visitor());
	}
	
	public void testUpdateEmployees()
	{
		class visitor implements IConnectionVisitor
		{

			@Override
			public void visit(Session session) {
				ScrollableResults employeeCursor = session.createQuery("FROM BatchEmployee").scroll();
				int count = 0;
				while (employeeCursor.next()) {
					BatchEmployee employee = (BatchEmployee) employeeCursor.get(0); 
					employee.setFirstName(employee.getFirstName()+count);
					session.update(employee);
					if (++count % SessionManager.BATCH_SIZE == 0)
					{
						session.flush();
						session.clear();
						System.out.println("count: " + count);
					}
				}
				
			}
		}
		new SimpleConnection().accept(new visitor());
	}
}
