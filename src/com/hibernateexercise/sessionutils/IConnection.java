package com.hibernateexercise.sessionutils;

public interface IConnection {

	public void accept(IConnectionVisitor visitor);
}
