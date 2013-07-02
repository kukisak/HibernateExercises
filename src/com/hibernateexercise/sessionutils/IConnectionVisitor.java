package com.hibernateexercise.sessionutils;

import org.hibernate.Session;

public interface IConnectionVisitor {

	public void visit(Session session);
}
