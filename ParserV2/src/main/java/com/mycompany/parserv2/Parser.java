package com.mycompany.parserv2;
import com.mycompany.parserv2.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
//import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.apache.http.util.EntityUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
    private String _link;
    private List<State> _statesList = new ArrayList<>();
    //private CloseableHttpResponse _response;
   // private int _statusCode;
    
    
    public Parser(String link){
        this._link = link;}
    
    //public void setStatusCode(int statusCode){
        //this._statusCode = statusCode;}
    
    //public void setResp(CloseableHttpResponse response){
        //this._response = response;}
    
    public boolean checkStatusCode(int statusCode){
        if(statusCode  >= 100 & statusCode < 200){
            System.out.println("u got Infotmation response. Status code: " + statusCode); return false;} 
        if (statusCode >= 200 & statusCode < 300) {
            System.out.println("u got Successful response. Status code: " + statusCode); return true;}
        if (statusCode >= 300 & statusCode < 400) {
            System.out.println("u got Redirected. Status code: " + statusCode);return false;} 
        if (statusCode >= 400 & statusCode < 500) {
            System.out.println("u got Client error. Status code: " + statusCode);return false;} 
        if (statusCode >= 500 & statusCode < 600) {
            System.out.println("u got Server error. Status code: " + statusCode);return false;} 
        else {System.out.println("smf went wrong, try again. :( "); return false;}
        }
    
    public int Request(){
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
        CloseableHttpResponse response = httpClient.execute(new HttpGet(_link));
        
        //setResp(response);
        int statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(response.getStatusLine().getStatusCode())){
            Elements nextStepLinks;
            Document startPage = Jsoup.parse(EntityUtils.toString(response.getEntity()));
            //nextStepLinks = startPage.getElementsByAttributeValue("class", "list__item hidden_medium hidden_large");
            nextStepLinks = startPage.getElementsByAttributeValue("class", "list__text");
            //State states = new State();
            //List<State> statesList = new ArrayList<>();
            int  i = 0;
            for(Element hrefElement: nextStepLinks){
                State stateObj = new State();
                stateObj.setUrl(hrefElement.attr("href"));
                stateObj.setTitle(hrefElement.text());
                _statesList.add(i, stateObj);
                System.out.println(_statesList.get(i).getUrl());
                //System.out.println(_statesList.get(i).getTitle());
                i++;
                if(i == 8 ){
                    System.out.println("--------------1--------------");
                    return 0;}
            }
        }
        else {return 2;}
    }catch (Exception e) {}
    return 0;}
    
    
    public int stateRequest(){
    for(int i = 0; i < 8; i++){
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
        CloseableHttpResponse response = httpClient.execute(new HttpGet(_statesList.get(i).getUrl()));
        
        //setResp(response);
        int statusCode = response.getStatusLine().getStatusCode();
        if (checkStatusCode(response.getStatusLine().getStatusCode())){
            Elements titleDateTime;
            Elements titleAuthor;
            Elements titleName;
            Elements textIntro, textData;
           
            Document startPage = Jsoup.parse(EntityUtils.toString(response.getEntity()));
            //nextStepLinks = startPage.getElementsByAttributeValue("class", "list__item hidden_medium hidden_large");
            titleDateTime = startPage.getElementsByAttributeValue("class", "note__text breadcrumbs__text js-ago");
            titleAuthor = startPage.getElementsByAttributeValue("class", "link color_gray breadcrumbs__link");
            titleName = startPage.getElementsByAttributeValue("class", "hdr hdr_collapse hdr_bold_huge hdr_lowercase");
            textIntro = startPage.getElementsByAttributeValue("class", "article__intro");
            textData = startPage.getElementsByAttributeValue("class", "article__item article__item_alignment_left article__item_html");
            //State states = new State();
            //List<State> statesList = new ArrayList<>();
            //_statesList.get(i).setText(textIntro.text());
            
            
            for(Element titleD: titleName){
                //State stateObj = new State();
                _statesList.get(i).setTitle(titleD.text());
                
                //stateObj.setTitle(titleD.text());
                //statesList.add(i, stateObj);
                System.out.println(_statesList.get(i).getTitle());
                //System.out.println(statesList.get(i).getTitle());
            }
            
            
            for(Element titleD: titleDateTime){
                //State stateObj = new State();
                _statesList.get(i).setDateTime(titleD.attr("datetime"));
                
                //stateObj.setTitle(titleD.text());
                //statesList.add(i, stateObj);
                System.out.println(_statesList.get(i).getDateTime());
                //System.out.println(statesList.get(i).getTitle());
            }
            for(Element titleD: titleAuthor){
                //State stateObj = new State();
                _statesList.get(i).setAuthor(titleD.text());
                
                //stateObj.setTitle(titleD.text());
                //statesList.add(i, stateObj);
                System.out.println(_statesList.get(i).getAuthor());
                //System.out.println(statesList.get(i).getTitle());
            }
            
            String result = textIntro.text();
            for(Element el: textData){
                //State stateObj = new State();
                result +=el.getElementsByTag("p").text();//.before("unprinted");
                //result = result.replace("[?]","-");
                result +=el.getElementsByTag("li").text();//.first().text();
               
                
                //stateObj.setTitle(titleD.text());
                //statesList.add(i, stateObj);
               // System.out.println(_statesList.get(i).getTitle());
                //System.out.println(statesList.get(i).getTitle());
            }
             _statesList.get(i).setText(result);
             System.out.println(_statesList.get(i).getText());
        
            
        }
        else {return 3;}
    }catch (Exception e) {}
    }
    return 0;}
    }
   /* public int getPage() throws IOException{
        Elements nextStepLinks;
        Document startPage = Jsoup.parse(EntityUtils.toString(_response.getEntity()));
        nextStepLinks = startPage.getElementsByAttributeValue("class", "list__text");
        
        //State states = new State();
        List<String> links = new ArrayList<>();
        int  i = 0; 
        for(Element link: nextStepLinks){
            State states = new State();
            states.setUrl(link.attr("href"));
            //System.out.println(states[i].getUrl());
            i++;
        }*/
       
        //getElementByTag("href");
        //System.out.println(startPage);
   // return 0;}
  
  

   

