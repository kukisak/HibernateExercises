package com.hibernateexercise.helpers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hibernateexercise.beans.Employee;

public class PrintHelper {

	public static void printResultsItems(List results) {
		for (Iterator iterator = results.iterator(); iterator.hasNext();) {
			System.out.println("***Row***");
			Object tempObject = iterator.next();
			if (tempObject instanceof Object[])
			{
				Object[] employee = (Object[]) tempObject;
				for (int i = 0; i < employee.length; i++) {
					System.out.println("Item " + i + " = " + employee[i].toString());
				}
			}
			else 
			{
				System.out.println("Item = " + tempObject.toString());
			}
		}
	}
	
	public static void printResultsScalarMap(List results) 
	{
		for (Object object : results) {
			System.out.println("***Row***");
			Map row = (Map)object;
			Set keys = row.keySet();
			for (Object object2 : keys) {
				System.out.println("Key " + object2.toString() + " has value " + row.get(object2));
			}
		}
	}
	

}
