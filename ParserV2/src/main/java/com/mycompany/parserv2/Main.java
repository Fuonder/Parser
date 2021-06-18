package com.mycompany.parserv2;


import com.mycompany.parserv2.Parser;
import com.mycompany.parserv2.ElasticSearch;
import java.io.IOException;


public class Main {

   
    public static void main(String[] args) throws IOException {
    Parser a = new Parser("https://sportmail.ru/");
    a.Request();
    //System.out.println(a.Request());
    //a.getPage();
    a.stateRequest();
    
    ElasticSearch b = new ElasticSearch();
    b.connect();
    for(int i =0; i<8;i++){
    b.insertData(a._statesList.get(i));
    System.out.println("work in progress" + i);}
    b.Search("title", "Швеция");
    b.getAggregation();
    b.compareStates(b.getDataFromElastic(), 99);
    //b.getDataFromElastic();
    
    b.disconnect();
    }
    
}
