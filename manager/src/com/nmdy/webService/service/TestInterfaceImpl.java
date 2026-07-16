package com.nmdy.webService.service;

public class TestInterfaceImpl implements TestInterface {

	@Override
	public String test(String str) {
		System.out.println("TestInterfaceImpl.test()*******************************"+str);
		return str;

	}

}
