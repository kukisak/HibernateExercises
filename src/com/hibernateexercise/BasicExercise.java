package com.hibernateexercise;

import org.hibernate.Session;

import com.hibernateexercise.beans.Employee;
import com.hibernateexercise.helpers.EmployeeHelper;
import com.hibernateexercise.sessionutils.InterceptedConnection;
import com.hibernateexercise.sessionutils.IConnectionVisitor;import com.hibernateexercise.sessionutils.InterceptedConnection;


public class BasicExercise {
	
	public void runExercise()
	{
		Integer empId1 = addEmployee("Petr", "Kukral", 10000);
		Integer empId2 = addEmployee("Jana", "Slezakova", 8000);
		Integer empId3 = addEmployee("Filip", "Novak", 7000);
		
		EmployeeHelper.listEmployees();
		updateEmployee(empId2, 9000);
		deleteEmployee(empId3);
		EmployeeHelper.listEmployees();
	}
	
	/**
	 * Method to create Employee in a database.
	 * @param fName - Employee first name
	 * @param lName - Employee last name
	 * @param salary - Employee salary
	 * @return
	 */
	public Integer addEmployee(String fName, String lName, int salary)
	{
		class Visitor implements IConnectionVisitor
		{
			private Integer employeeId = null;
			private String fName = null;
			private String lName = null;
			private int salary;
			
			private Integer getEmployeeId() {
				return employeeId;
			}

			private Visitor(String fName, String lName, int salary)
			{
				this.fName = fName;
				this.lName = lName;
				this.salary = salary;
			}
			
			@Override
			public void visit(Session session) 
			{
				Employee employee = new Employee(fName, lName, salary);
				employeeId = (Integer) session.save(employee);
			}

		}
		Visitor visitor = new Visitor(fName, lName, salary);
		new InterceptedConnection().accept(visitor);
		return visitor.getEmployeeId();
	}
	
	/**
	 * Method to update salary for an employee.
	 * @param employeeId - employee's id who to be updated
	 * @param salary - new amount of salary
	 */
	public void updateEmployee(Integer employeeId, int salary)
	{
		class Visitor implements IConnectionVisitor
		{

			private Integer employeeId = null;
			private int salary;
			private Visitor(Integer employeeId, int salary)
			{
				this.employeeId = employeeId;
				this.salary = salary;
			}
			@Override
			public void visit(Session session) 
			{
				Employee employee = (Employee) session.get(Employee.class, employeeId);
				employee.setSalary(salary);
				session.update(employee);
			}
		}
		new InterceptedConnection().accept(new Visitor(employeeId, salary));
	}
	
	/**
	 * Method to delete employee from database.
	 * @param employeeId - Employee's id who to be deleted
	 */
	public void deleteEmployee(Integer employeeId)
	{
		class Visitor implements IConnectionVisitor
		{

			private Integer employeeId = null;
			private Visitor(Integer employeeId)
			{
				this.employeeId = employeeId;
			}
			@Override
			public void visit(Session session) 
			{
				Employee employee = (Employee) session.get(Employee.class, employeeId);
				session.delete(employee);
			}
		
		}
		new InterceptedConnection().accept(new Visitor(employeeId));
	}
}
