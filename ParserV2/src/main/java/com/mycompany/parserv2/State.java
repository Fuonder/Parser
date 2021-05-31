package com.mycompany.parserv2;

public class State {
    private String _url;
    private String _dateTime;
    private String _title;
    private String _author;
    private String _text;
    
    public State(String url,String dateTime,String title,String author,String text){
        this._url = url;
        this._dateTime = dateTime;
        this._title = title;
        this._author = author;
        this._text = text;
    }
    
    public State() {}
    
    public void setUrl(String url){this._url = url;}
    
    public void setDateTime(String dateTime){this._dateTime = dateTime;}
    
    public void setTitle(String title){this._title = title;}
    
    public void setAuthor(String author){this._author = "Источник: " + author;}
    
    public  void setText(String text){this._text = text;}
    
    public String getUrl(){return this._url;}
    
    public String getDateTime(){return this._dateTime;}
    
    public String getTitle(){return this._title;}
    
    public String getAuthor(){return this._author;}
    
    public String getText(){return this._text;}
    
       
    
   
}

    