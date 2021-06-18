package com.mycompany.parserv2;

import java.util.ArrayList;
import java.util.List;

public class ElasticStateData {
    
    private String _id;
    private String _text;
    public List<Integer> minimalHashes  = new ArrayList<>();;
    
    public ElasticStateData(String id, String text, int hash){
        this._id = id;
        this._text = text;
    }
    
    public ElasticStateData() {}
    
    public void setId(String id){this._id = id;}
     
    public  void setText(String text){this._text = text;}
    
    public void setMinimalHash(int min){
        minimalHashes.add(min);}
    
    public String getId(){return this._id;}
    
    public String getText(){return this._text;}
    
   
    
    public void deleteNums(){
        _text = _text.replaceAll("\\p{Punct}|\\d", "").replaceAll("\\—", "").replaceAll("[«»]", "").replaceAll("\\s+", " ");
        //System.out.println(_text);
    }
    
    public List<String> mySplit(){
        List<String> subText = new ArrayList<>();
        String[] subWord;
        String result = "";
        subWord = _text.split(" ");
        
        for(int i = 0; i< subWord.length-10;i++){
            for(int j = 0; j< 10;j++){
            result += subWord[i+j];
            result += " ";
            }
        subText.add(result);
        result = "";
        }
        /*int myIndex = subWord.length / 10;
        int j = 0;
        int k = 10;
        for(int i =0; i < myIndex;i++){
            for(; j < k;j++){
                result += subWord[j];
            }
            subText.add(result);
           // j +=15;
            k +=10;
            result = "";
        }*/
    
    return subText;} 
    
    
    public void getAllHashes(double accuracy){
        
        int functionCount = (int)(1 / (accuracy * accuracy));
        for(int i = 0; i < functionCount; i++){
            MyMinHash targetHash = new MyMinHash();
            
            //targetHash.hashText(this.mySplit());
            this.setMinimalHash(targetHash.findMinimalhash(targetHash.hashText(this.mySplit())));
            targetHash.updateSeed(i);
            
        }
        
    }
    
}
       
