package com.home.DongFangHongRpc.test.server.bean;

/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月20日 下午4:18:48
 */
public class Person {
	private int age;
	private String name;
	public Person(){}
	public Person(int age, String name) {
		this.age = age;
		this.name = name;
	}
	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
