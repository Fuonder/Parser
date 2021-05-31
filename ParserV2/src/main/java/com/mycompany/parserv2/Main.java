package com.mycompany.parserv2;


import com.mycompany.parserv2.Parser;
import java.io.IOException;


public class Main {

   
    public static void main(String[] args) throws IOException {
    Parser a = new Parser("https://sportmail.ru/");
    a.Request();
    //System.out.println(a.Request());
    //a.getPage();
    a.stateRequest();
    }
    
}
