package com.nmdy.webService.service;

import javax.jws.WebService;

@WebService
public interface IHelloService {

    public String sayHello(String username);
    public String testStr(String str);
   
}

