package com.hibernateexercise;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernateexercise.helpers.EmployeeHelper;
import com.hibernateexercise.helpers.PrintHelper;
import com.hibernateexercise.sessionutils.SimpleConnection;
import com.hibernateexercise.sessionutils.IConnectionVisitor;
import com.hibernateexercise.sessionutils.SessionManager;

public class HqlExercise {

	public void runExercise()
	{
		HqlExercise he = new HqlExercise();
		String hql = null; 
//		hql = "FROM com.hibernateexercise.beans.Employee";
//		he.testHql(hql);
//		hql = "FROM Employee AS E";
//		he.testHql(hql);
//		hql = "SELECT E.firstName, E.lastName FROM Employee E";
//		he.testHqlSelect(hql);
//		hql = "SELECT E.firstName, E.lastName, E.salary FROM Employee E";
//		he.testHqlSelect(hql);
//		hql = "SELECT E.firstName FROM Employee E";
//		he.testHqlSelect(hql);
//		hql = "FROM Employee E WHERE E.id = 4";
//		he.testHql(hql);
//		hql = "FROM Employee E WHERE E.id > 5 ORDER BY E.salary, E.id";
//		he.testHql(hql);
//		hql = "FROM Employee E WHERE E.id > 5 GROUP BY E.lastName";
//		he.testHql(hql);
//		hql = "SELECT COUNT(E.lastName), E.firstName, SUM(E.salary) " +
//			  "FROM Employee E WHERE E.id > 5 GROUP BY E.lastName";
//		he.testHqlSelect(hql);
//		hql = "FROM Employee E WHERE E.id > :idParam";
//		he.testHqlParameter(hql, "idParam", 10);
		hql = "UPDATE Employee E set salary = :salary " +
			  "WHERE E.id = :id";
		he.testHqlUpdateParameterList(hql, new Object[][] {{"salary", 3000},{"id", 7}});
		EmployeeHelper.listEmployees();
//		hql = "DELETE FROM Employee WHERE id = :id";
//		he.testHqlUpdateParameterList(hql, new Object[][] {{"id", 13}});
//		EmployeeHelper.listEmployees();
//		hql = "INSERT INTO Employee (firstName, lastName, salary) "+
//			  "SELECT firstName, lastName, salary FROM OldEmployee";
//		he.testHqlUpdateParameterList(hql, new Object[0][0]);
//		EmployeeHelper.listEmployees();
//		hql = "SELECT COUNT(DISTINCT E.firstName) FROM Employee E";
//		he.testHqlSelect(hql);
//		hql = "FROM Employee";
//		he.testHqlPaging(hql, 4);
	}
	
	public void testHqlSelect(String hql)
	{
		class Visitor implements IConnectionVisitor
		{
			String hql = null;
			public Visitor(String hql)
			{
				this.hql = hql;
			}
			
			@Override
			public void visit(Session session) {
				Query query = session.createQuery(hql);
				List employees = query.list();
				PrintHelper.printResultsItems(employees);
			}
		}
		new SimpleConnection().accept(new Visitor(hql));

		
	}
	public void testHql(String hql)
	{
		class Visitor implements IConnectionVisitor
		{
			String hql = null;
			public Visitor(String hql)
			{
				this.hql = hql;
			}
			
			@Override
			public void visit(Session session) {
				Query query = session.createQuery(hql);
				List employees = query.list();
				EmployeeHelper.printEmployeeList(employees);
			}
		}
		new SimpleConnection().accept(new Visitor(hql));
	}
	
	public void testHqlPaging(String hql, int pageSize)
	{
		class Visitor implements IConnectionVisitor
		{
			private String hql = null;
			private int pageSize;
			
			public Visitor(String hql, int pageSize)
			{
				this.hql = hql;
				this.pageSize = pageSize;
			}
			
			@Override
			public void visit(Session session) {
				Query query = session.createQuery(hql);
				int firstResult = 0;
				query.setMaxResults(pageSize);
				boolean continueListing = true;
				do {
					query.setFirstResult(firstResult);
					List employees = query.list();
					if (employees.isEmpty())
					{
						continueListing = false;
						continue;
					}
					EmployeeHelper.printEmployeeList(employees);
					System.out.println("--- Page results from " + (firstResult+1) + " to " + (firstResult+pageSize) + " ---");
					firstResult += pageSize;
				} while (continueListing);			}
		}
		new SimpleConnection().accept(new Visitor(hql, pageSize));
	}
	public void testHqlParameter(String hql, String parameterName, Object parameterValue)
	{
		
		class Visitor implements IConnectionVisitor
		{
			private String hql = null;
			private String parameterName = null;
			private Object parameterValue = null; 
			
			public Visitor(String hql, String parameterName, Object parameterValue)
			{
				this.hql = hql;
				this.parameterName = parameterName;
				this.parameterValue = parameterValue;
			}
			
			@Override
			public void visit(Session session) {
				Query query = session.createQuery(hql);
				query.setParameter(parameterName, parameterValue);
				List employees = query.list();
				EmployeeHelper.printEmployeeList(employees);
			}
		}
		new SimpleConnection().accept(new Visitor(hql, parameterName, parameterValue));		
	}
	
	public void testHqlUpdateParameterList(String hql, Object[][] parametersNameValue)
	{
		class Visitor implements IConnectionVisitor
		{
			private String hql = null;
			private Object[][] parametersNameValue = null; 
			
			public Visitor(String hql, Object[][] parametersNameValue)
			{
				this.hql = hql;
				this.parametersNameValue = parametersNameValue;
			}
			
			@Override
			public void visit(Session session) 
			{
				Query query = session.createQuery(hql);
				for (Object[] objects : parametersNameValue) {
					System.out.println("String: " + objects[0] + ", Object: " + objects[1]);
					query.setParameter((String)objects[0], objects[1]);
				}
				int results = query.executeUpdate();
				System.out.println("Rows affected: " + results);
			}
		}
		new SimpleConnection().accept(new Visitor(hql, parametersNameValue));	
	}
	
}
