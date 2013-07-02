package com.hibernateexercise.helpers;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernateexercise.beans.Employee;
import com.hibernateexercise.sessionutils.SessionManager;

public class EmployeeHelper {

	/**
	 * Method to read all employees from database.
	 */
	public static void listEmployees()
	{
		Session session = SessionManager.getSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			List employees = session.createQuery("FROM Employee").list();
			printEmployeeList(employees);
			tx.commit();
		} catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Method to print all employees in list.
	 * @param employees
	 */
	public static void printEmployeeList(List employees) {
		for (Iterator iterator = employees.iterator(); iterator.hasNext();) {
			Employee employee = (Employee) iterator.next();
			System.out.println("Id: " + employee.getId());
			System.out.println("First name: " + employee.getFirstName());
			System.out.println("Last name: " + employee.getLastName());
			System.out.println("Salary: " + employee.getSalary());
		}
	}

}
