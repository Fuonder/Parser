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
    
    public List<State> _statesList = new ArrayList<>();

    public Parser(String link){
        this._link = link;}
    
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
        
        if (checkStatusCode(response.getStatusLine().getStatusCode())){
            Elements nextStepLinks;
            Document startPage = Jsoup.parse(EntityUtils.toString(response.getEntity()));
            nextStepLinks = startPage.getElementsByAttributeValue("class", "list__text");

            int  i = 0;
            for(Element hrefElement: nextStepLinks){
                State stateObj = new State();
                stateObj.setUrl(hrefElement.attr("href"));
                stateObj.setTitle(hrefElement.text());
                _statesList.add(i, stateObj);
                System.out.println(_statesList.get(i).getUrl());
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
        

        if (checkStatusCode(response.getStatusLine().getStatusCode())){
            Elements titleDateTime;
            Elements titleAuthor;
            Elements titleName;
            Elements textIntro, textData;
           
            Document startPage = Jsoup.parse(EntityUtils.toString(response.getEntity()));

            titleDateTime = startPage.getElementsByAttributeValue("class", "note__text breadcrumbs__text js-ago");
            titleAuthor = startPage.getElementsByAttributeValue("class", "link color_gray breadcrumbs__link");
            titleName = startPage.getElementsByAttributeValue("class", "hdr hdr_collapse hdr_bold_huge hdr_lowercase");
            textIntro = startPage.getElementsByAttributeValue("class", "article__intro");
            textData = startPage.getElementsByAttributeValue("class", "article__item article__item_alignment_left article__item_html");
            
            for(Element titleD: titleName){
                _statesList.get(i).setTitle(titleD.text());
                System.out.println(_statesList.get(i).getTitle());
            }
            
            for(Element titleD: titleDateTime){
                _statesList.get(i).setDateTime(titleD.attr("datetime"));
                System.out.println(_statesList.get(i).getDateTime());
            }
            
            for(Element titleD: titleAuthor){
                _statesList.get(i).setAuthor(titleD.text());
                System.out.println(_statesList.get(i).getAuthor());
            }
            
            String result = textIntro.text();
            for(Element el: textData){
                result +=el.getElementsByTag("p").text();
                result +=el.getElementsByTag("li").text();
            }
             _statesList.get(i).setText(result);
             System.out.println(_statesList.get(i).getText());
        
            
        }
        else {return 3;}
    }catch (Exception e) {}
    }
    return 0;}
    }
   
  

   

