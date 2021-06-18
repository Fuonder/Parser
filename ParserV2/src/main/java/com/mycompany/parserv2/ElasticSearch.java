
package com.mycompany.parserv2;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.*;
import org.elasticsearch.common.xcontent.XContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class ElasticSearch {
    RestHighLevelClient restClient;
    
    public void connect(){
        restClient = new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.101.130", 9200, "http")));
    }
    
    public void disconnect() throws IOException{
        restClient.close();
        restClient = null;
    }

    String convertObject(State state) throws JsonProcessingException {
        ObjectWriter mineObjWrt = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return mineObjWrt.writeValueAsString(state);
    }        
    
    public void insertData(State insState) throws JsonProcessingException, IOException{
        IndexRequest request = new IndexRequest("sites");
        request.id(sha256Hex(insState.getText())); 
        request.source(convertObject(insState), XContentType.JSON);
    
        IndexResponse response = restClient.index(request, RequestOptions.DEFAULT);
    };
    
    public void Search(String type, String element) throws IOException{
        SearchRequest searchRequest = new SearchRequest("sites");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery(type, element));
        searchRequest.source(builder.size(1000));
        SearchResponse response = restClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println("ЭТО РЕСПОНС " + response);
    }
    
    public void getAggregation() throws IOException{
       // String mainTarget = "Источник: " + target;
        
       /* CardinalityAggregationBuilder aggregation = 
                AggregationBuilders
                    .cardinality("authorSSSS")
                    .field("author");*/
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("authors").field("author");
      
        SearchRequest searchRequest = new SearchRequest("sites");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery()); 
        builder.aggregation(aggregation);
        searchRequest.source(builder.size(1000));
        
        SearchResponse response = restClient.search(searchRequest, RequestOptions.DEFAULT);
        Terms termAuth = response.getAggregations().get("authors");

        for (Terms.Bucket entry : termAuth.getBuckets()) {
            System.out.println(entry.getKey());      // Название источника
            System.out.println(entry.getDocCount()); // Количество доков для этого источника
        }
      // Cardinality cardinality = response.getAggregations().get("authorSSSS");
       // long a = cardinality.getValue();
       // System.out.println(a);
    }
    
    public List<ElasticStateData> getDataFromElastic() throws IOException{
        SearchRequest searchRequest = new SearchRequest("sites");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(builder.size(10000));
        SearchResponse response = restClient.search(searchRequest, RequestOptions.DEFAULT);
        
        List<ElasticStateData> statesData = new ArrayList<>();
        
        for(SearchHit hit : response.getHits()){
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            ElasticStateData targetObj = new ElasticStateData();
            targetObj.setId(hit.getId());
            targetObj.setText((String) sourceAsMap.get("text"));
            statesData.add(targetObj);
            targetObj.deleteNums();
            targetObj.mySplit();
            targetObj.getAllHashes(0.1);
            //MyMinHash targetObjHash = new MyMinHash();
           
            //targetObj.setHash(targetObjHash.findMinimalhash(targetObjHash.hashText(targetObj.mySplit())));
           
        }
    return statesData;
    }
    
    public void compareStates(List<ElasticStateData> satesData, int functionCount){
        int equalCount = 0;
        for(int i = 0; i < satesData.size(); i++){
            for(int j = i+1; j < satesData.size(); j++){
                for(int k = 0; k < satesData.get(i).minimalHashes.size(); k++){
                    /*if(satesData.get(i).minimalHashes.get(k) == satesData.get(j).minimalHashes.get(k)){
                    equalCount++;
                    }*/
                    if(satesData.get(i).minimalHashes.get(k).equals(satesData.get(j).minimalHashes.get(k))){
                    equalCount++;
                    }
                }
            System.out.println("схожесть текста "+ (i+1) + " и текста "+ (j+1) + " равна = " + (equalCount/functionCount));
            equalCount  = 0;
            }
        }
    //return equalCount/functionCount;
    }
    
    
    /*public void analyzer(){
    AnalyzeRequest request = AnalyzeRequest.withIndexAnalyzer("sites", jmorphy2_russian, text);
    }*/
    
   
    
}
