package com.derdiedas.vendr.germanapp;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class JsonParser {
    public static void main(String[] args) throws Exception {
        File f = new File("GermanNounDictionary.json");
        if (f.exists()) {
            InputStream is = new FileInputStream("GermanNounDictionary.json");
            String jsonTxt = IOUtils.toString(is);
            JSONObject json = new JSONObject(jsonTxt);
            String a = json.getString("1000");
        }
    }
}
