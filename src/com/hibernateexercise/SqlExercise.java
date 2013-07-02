package com.hibernateexercise;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.ResultTransformer;

import com.hibernateexercise.beans.Employee;
import com.hibernateexercise.helpers.EmployeeHelper;
import com.hibernateexercise.helpers.PrintHelper;
import com.hibernateexercise.sessionutils.SimpleConnection;
import com.hibernateexercise.sessionutils.IConnectionVisitor;
import com.hibernateexercise.sessionutils.SessionManager;

public class SqlExercise {

	public void runExercise()
	{
		String sql = null;
		sql = "SELECT first_name, last_name FROM EMPLOYEE";
		testSqlScalar(sql);
//		testSqlScalar(sql, Criteria.ROOT_ENTITY);
//		testSqlScalar(sql, Criteria.DISTINCT_ROOT_ENTITY);
//		testSqlScalar(sql, Criteria.PROJECTION);
		sql = "SELECT * FROM EMPLOYEE";
//		testSqlEntity(sql);
		sql = "SELECT * FROM EMPLOYEE WHERE id = :id";
//		testSqlEntityParameters(sql, new Object[][] {{"id", 10}});
	}
	
	/**
	 * Create read-only SQL query with results transformed by the specified transformer.
	 * @param sql - read-only SQL
	 * @param transformer - ResultTransformer transformer
	 */
	public void testSqlScalar(String sql, ResultTransformer transformer)
	{
		class Visitor implements IConnectionVisitor
		{

			private String sql = null;
			private ResultTransformer transformer;
			private Visitor(String sql, ResultTransformer transformer)
			{
				this.sql = sql;
				this.transformer = transformer;
			}
			@Override
			public void visit(Session session) 
			{
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(transformer);
				List results = query.list();
				PrintHelper.printResultsItems(results);
			}
		}
		new SimpleConnection().accept(new Visitor(sql, transformer));
	}
	/**
	 * Create read-only SQL query with results in map.
	 * @param sql - native read-only SQL  
	 */
	public void testSqlScalar(String sql)
	{
		class Visitor implements IConnectionVisitor
		{
			private String sql = null;
			private Visitor(String sql)
			{
				this.sql = sql;
			}
			@Override
			public void visit(Session session) 
			{
				SQLQuery query = session.createSQLQuery(sql);
				query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				List results = query.list();
				PrintHelper.printResultsScalarMap(results);
			}
		}
		new SimpleConnection().accept(new Visitor(sql));
	}
	/**
	 * Create read-only SQL query to read each result row as an Employee entity.
	 * @param sql - read-only SQL query for Employee entity
	 */
	public void testSqlEntity(String sql)
	{
		class Visitor implements IConnectionVisitor
		{
			private String sql = null;
			private Visitor(String sql)
			{
				this.sql = sql;
			}
			@Override
			public void visit(Session session) {
				SQLQuery query = session.createSQLQuery(sql);
				query.addEntity(Employee.class);
				List results = query.list();
				EmployeeHelper.printEmployeeList(results);
			}
		}
		new SimpleConnection().accept(new Visitor(sql));
	}
	/**
	 * Create read-only SQL query to read each result row as an Employee entity with specified parameters as placeholders.
	 * @param sql - read-only SQL query for Employee entity
	 * @param parametersNameValue - 2D Objects array with first value as String type parameter name and second as Object type parameter value 
	 */
	public void testSqlEntityParameters(String sql, Object[][] parametersNameValue )
	{
		class Visitor implements IConnectionVisitor
		{
			private String sql = null;
			private Object[][] parametersNameValue = null;
			private Visitor(String sql, Object[][] parametersNameValue)
			{
				this.sql = sql;
				this.parametersNameValue = parametersNameValue;
			}
			@Override
			public void visit(Session session) 
			{
				SQLQuery query = session.createSQLQuery(sql);
				query.addEntity(Employee.class);
				for (Object[] objects : parametersNameValue) 
				{
					System.out.println("Parameter name: " + objects[0] + ", Value: " + objects[1]);
					query.setParameter((String)objects[0], objects[1]);
				}
				
				List results = query.list();
				EmployeeHelper.printEmployeeList(results);
				
			}
		}
		new SimpleConnection().accept(new Visitor(sql, parametersNameValue));
	}
}
