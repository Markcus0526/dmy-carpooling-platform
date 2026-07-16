package com.nmdy.webService.service;

import javax.jws.WebService;

@WebService(endpointInterface = "com.nmdy.webService.service.IHelloService", serviceName = "HelloService")
public class HelloServiceImpl implements IHelloService {

	private TestInterface testInterface;
	
	
	
	public TestInterface getTestInterface() {
		return testInterface;
	}



	public void setTestInterface(TestInterface testInterface) {
		this.testInterface = testInterface;
	}



	public String sayHello(String username) {
	       return "hello, " + username;		
	}

	public String testStr(String str){
		
		return testInterface.test("test interface");
		
	}



	
}
