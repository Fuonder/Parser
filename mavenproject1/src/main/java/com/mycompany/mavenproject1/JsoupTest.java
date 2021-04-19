/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JsoupTest{

    public static void main(String[] args) {
        String Summary = new JsoupTest().getWikipediaSummary("Parser");
        System.out.println(Summary);
    }

    private String getWikipediaSummary(String keyword) {

        Document document;
        try {
            document = Jsoup.connect("https://en.wikipedia.org/wiki/" + keyword).get();

        } catch (IOException e) {
            return String.format("Sorry, I couldn't find '%s' on Wikipedia", keyword);
        }

        Elements paragraphs = document.select("div#mw-content-text > div:first-of-type > p");

        String backupSentence = String.format("I couldn't find info on '%s' on Wikipedia - this bot works best if you provide a noun.", keyword);

        String wikipediaSummary = paragraphs.stream()
            .filter(e -> !e.text().isBlank())
            .findFirst()
            .map(para -> {
                    para.select("sup").remove(); 
                    para.select(".nowrap").remove(); 
                return para.text().replace("() ", "");}) 
            .orElse(backupSentence);

        String firstSentence = wikipediaSummary.substring(0, wikipediaSummary.indexOf(".") + 1);

        if (firstSentence.isBlank()) {
            firstSentence = backupSentence;
        }

        return firstSentence;
    }

}

