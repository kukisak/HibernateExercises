package com.hibernateexercise;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.hibernateexercise.beans.Employee;
import com.hibernateexercise.helpers.EmployeeHelper;
import com.hibernateexercise.helpers.PrintHelper;
import com.hibernateexercise.sessionutils.SimpleConnection;
import com.hibernateexercise.sessionutils.IConnectionVisitor;

public class CriteriaExercise {
	
	public void runExercise()
	{
		testCriteria();
		testCriteriaRestrictions();
		testCriteriaRestrictionsLtGt();
		testCriteriaRestrictionsILike();
		testCriteriaRestrictionsLike();
		testCriteriaLogicalExpressions();
		testCriteriaPaging(2);
		testCriteriaOdering();
		testCriteriaProjections();
	}
	/**
	 * Create filters to display: number of objects, number of unique objects by the firstName field, 
	 * object with minimum salary, object with maximum salary, object with sum of all salary.
	 */
	public void testCriteriaProjections()
	{
		class Visitor implements IConnectionVisitor
		{
			@Override
			public void visit(Session session) 
			{
				Criteria cr = session.createCriteria(Employee.class);
				List results = null;
				
				cr.setProjection(Projections.rowCount());
				results = cr.list();
				PrintHelper.printResultsItems(results);			

				cr.setProjection(Projections.countDistinct("firstName"));
				results = cr.list();
				PrintHelper.printResultsItems(results);
				
				cr.setProjection(Projections.min("salary"));
				results = cr.list();
				PrintHelper.printResultsItems(results);
				
				cr.setProjection(Projections.max("salary"));
				results = cr.list();
				PrintHelper.printResultsItems(results);
				
				cr.setProjection(Projections.sum("salary"));
				results = cr.list();
				PrintHelper.printResultsItems(results);
			}
		}
		new SimpleConnection().accept(new Visitor());
	}

	/**
	 * Create filter to return objects of type Employee sorted by the field firstName.
	 */
	public void testCriteriaOdering()
	{
		class Visitor implements IConnectionVisitor
		{
			@Override
			public void visit(Session session) 
			{
				Criteria cr = session.createCriteria(Employee.class);
				cr.addOrder(Order.desc("firstName"));
				List results = cr.list();
				EmployeeHelper.printEmployeeList(results);
			}
		}
		new SimpleConnection().accept(new Visitor());		
	}
	/**
	 * Create filter to return objects of type Employee page by page.
	 * @param pageSize - number of entities on one page
	 */
	public void testCriteriaPaging(int pageSize)
	{
		class Visitor implements IConnectionVisitor
		{
			private int pageSize;
			private Visitor(int pageSize)
			{
				this.pageSize = pageSize;
			}
			
			@Override
			public void visit(Session session) 
			{
				Criteria cr = session.createCriteria(Employee.class);
				int firstResult = 0;
				cr.setMaxResults(pageSize);
				boolean continueListing = true;
				do {
					cr.setFirstResult(firstResult);
					List employees = cr.list();
					if (employees.isEmpty())
					{
						continueListing = false;
						continue;
					}
					EmployeeHelper.printEmployeeList(employees);
					System.out.println("--- Criteria page results from " + (firstResult+1) + " to " + (firstResult+pageSize) + " ---");
					firstResult += pageSize;
				} while (continueListing);				
			}
		}
		new SimpleConnection().accept(new Visitor(pageSize));		
		
	}
	/**
	 * Create filter to return only objects where fields salary is greater than 4000 and field firsName equals "Petr".
	 */
	public void testCriteriaLogicalExpressions()
	{
		class Visitor implements IConnectionVisitor
		{
			@Override
			public void visit(Session session) 
			{
				Criteria cr = session.createCriteria(Employee.class);
				Criterion salary = Restrictions.gt("salary", 4000);
				Criterion name = Restrictions.eq("firstName", "Petr");
				LogicalExpression expAnd = Restrictions.and(salary, name);
				cr.add(expAnd);
				List results = cr.list();
				EmployeeHelper.printEmployeeList(results);
			}
		}
		new SimpleConnection().accept(new Visitor());			
	}
	/**
	 * Create filter to return only objects where fields firstName have "case sensitive" value "Petr".
	 * In case of MySQL DB the like operator works like ignore-case sensitive.
	 */
	public void testCriteriaRestrictionsLike()
	{
		class Visitor implements IConnectionVisitor
		{
			@Override
			public void visit(Session session) 
			{
				Criteria cr = session.createCriteria(Employee.class);
				cr.add(Restrictions.like("firstName", "petr"));
				List results = cr.list();
				EmployeeHelper.printEmployeeList(results);
			}
		}
		new SimpleConnection().accept(new Visitor());			
	}
	/**
	 * Create filter to return only objects where fields firstName have ignore-case sensitive value "petr".
	 */
	public void testCriteriaRestrictionsILike()
	{
		class Visitor implements IConnectionVisitor
		{
			@Override
			public void visit(Session session) 
			{
				Criteria cr = session.createCriteria(Employee.class);
				cr.add(Restrictions.ilike("firstName", "petr"));
				List results = cr.list();
				EmployeeHelper.printEmployeeList(results);
			}
		}
		new SimpleConnection().accept(new Visitor());			
	}
	/**
	 * Create filter to return only objects where field salary has value in range (4000, 10000).
	 */
	public void testCriteriaRestrictionsLtGt()
	{
		class Visitor implements IConnectionVisitor
		{
			@Override
			public void visit(Session session) 
			{
				Criteria cr = session.createCriteria(Employee.class);
				cr.add(Restrictions.gt("salary", 4000));
				cr.add(Restrictions.lt("salary", 10000));
				List results = cr.list();
				EmployeeHelper.printEmployeeList(results);
			}
		}
		new SimpleConnection().accept(new Visitor());			
	}
	/**
	 * Create filter to return only objects where field salary has value equal to 9000. 
	 */
	public void testCriteriaRestrictions()
	{
		class Visitor implements IConnectionVisitor
		{
			@Override
			public void visit(Session session) 
			{
				Criteria cr = session.createCriteria(Employee.class);
				cr.add(Restrictions.eq("salary", 9000));
				List results = cr.list();
				EmployeeHelper.printEmployeeList(results);
			}
		}
		new SimpleConnection().accept(new Visitor());			
	}
	/**
	 * Create filter to return only objects of type Employee.
	 */
	public void testCriteria()
	{
		class Visitor implements IConnectionVisitor
		{
			@Override
			public void visit(Session session) 
			{			
				Criteria cr = session.createCriteria(Employee.class);
				List results = cr.list();
				EmployeeHelper.printEmployeeList(results);
			}
		}
		new SimpleConnection().accept(new Visitor());			
	}
	
}
