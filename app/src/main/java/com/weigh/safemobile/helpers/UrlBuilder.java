package com.weigh.safemobile.helpers;

import android.text.TextUtils;

import java.util.ArrayList;

public class UrlBuilder {

    private static final String BASE_URL = "http://api.dataonesoftware.com/webservices";
    public static final String API_DECODE_VIN = "/vindecoder/decode";
    private String uri;

    private ArrayList<String> parameters;
    private ArrayList<String> sections;

    public UrlBuilder(String uri){
        this.uri = uri;
        parameters = new ArrayList<>();
    }

    public UrlBuilder addParameters(String name, String value){
        parameters.add(name + "=" + value);
        return this;
    }
    public UrlBuilder addParameters(String name, double value){
        parameters.add(name + "=" + value);
        return this;
    }
    public UrlBuilder addSection(String section){
        if(sections == null){
            sections = new ArrayList<>();
        }
        sections.add(section);
        return this;
    }

    public String build(){
        StringBuilder builder = new StringBuilder();
        builder.append(BASE_URL).append(uri);
        if(sections != null && !sections.isEmpty()){
            for(String section : sections){
                builder.append("/").append(section);
            }
        }
        if(parameters != null && !parameters.isEmpty()){
            builder.append("?").append(TextUtils.join("&",parameters));
        }
        return builder.toString();
    }

}