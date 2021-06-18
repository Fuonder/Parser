
package com.mycompany.parserv2;

import static java.nio.file.Files.size;
import java.util.ArrayList;
import java.util.List;
import static org.apache.lucene.index.PointValues.size;

public class MyMinHash {
    
   
    public int seed = 2;
    
    
    public void updateSeed(int num){
   // seed = (int)(Math.random() * 10) + 32;
    seed +=num;
    }
    
    public void resetSeed(){
   // seed = (int)(Math.random() * 10) + 32;
    seed = 2;
    }
    
    public List<Integer> hashText(List<String> subText){
        List<Integer> hashTextList = new ArrayList<>();
         for(int i = 0;i< subText.size(); i++){
            //hashTextList.add(subText.get(i).hashCode());
            int result = 0;
            for(int j = 0; j < subText.get(i).length(); j++){
                result += ((seed * Character.codePointAt(subText.get(i), j)) % seed+j);
                //hashTextList.add((seed * Character.codePointAt(subText.get(i), j)) & 0xFFFFFFFF);
            }
            hashTextList.add(result);
        }
        return hashTextList;
    }

    public int findMinimalhash(List<Integer> hashTextList){
    
        Integer min = Integer.MAX_VALUE;
        for (Integer i: hashTextList){
            if(min > i){
                min = i;
            }
        }
        return min;
    }
    
    
}
