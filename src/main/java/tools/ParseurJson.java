/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

/**
 *
 * @author martinj
 */
public class ParseurJson {

    private List<JsonElement> listJsonElement;
    private List<JsonElement> listRefToRemove;
    
    private List<String> listKey;
    private List<String> listKeyToChange;
 
    
    private boolean isInit;
    
    public ParseurJson(){
        listJsonElement = new ArrayList<>();
        listKey = new ArrayList<>();
        listRefToRemove = new ArrayList<>(); 
        initKeyChange();
        isInit = false;
    }
    
    
    private void addKeyRow(CSVRecord record) {
         for(String f : record)
                listKey.add(f);
         
         isInit = true;
    }

    private void initKeyChange() {
        listKeyToChange = new ArrayList<>();
        listKeyToChange.add("icon");
        listKeyToChange.add("listChildren");
        listKeyToChange.add("parameters");
        listKeyToChange.add("padding");
    }
    
    public void parse(String pathFile) throws FileNotFoundException, IOException {
        Reader ihmFile = new FileReader(pathFile);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(ihmFile);
        
        for (CSVRecord record : records) {
            if(!isInit) addKeyRow(record);
            else {
                JsonElement json = convertToJsonElement(record);
                if(json!=null) listJsonElement.add(json);
            }
          
        }
        
        for(JsonElement jsonElement : listJsonElement)
            System.out.println(jsonElement.toString());
    }

    public JsonElement extractIHM() {
        for(JsonElement element : listJsonElement)
            linkRef(element);                
        
        listJsonElement.removeAll(listRefToRemove);
        JsonElement ihm = new JsonObject();
        ihm.getAsJsonObject().add("listElements", convertToJsonArray(listJsonElement));
        return ihm;
    }

    private JsonElement convertToJsonElement(CSVRecord record) {
        JsonElement element = new JsonObject();
        int count = 0;
        System.out.println("----- " + record);
        for(String data : record){
            System.out.println(data + " * " + (data.equals("")&&count==0));
            if(data.equals("")&&count==0) return null;
            if(!data.equalsIgnoreCase("")){
                String key = listKey.get(count);
                String value = data;

                if(data.matches("-?\\d+"))
                    element.getAsJsonObject().add(key, new JsonPrimitive(Integer.parseInt(value)));
                else if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))
                    element.getAsJsonObject().add(key, new JsonPrimitive(Boolean.parseBoolean(value)));
                else 
                    element.getAsJsonObject().add(key, new JsonPrimitive(value));
            }
            count++;
        }
        return element;
    }

    private JsonElement convertToJsonArray(List<JsonElement> listJsonElement) {
        JsonArray jsonArray = new JsonArray();
        listJsonElement.forEach((element) -> {
            jsonArray.add(element);
        });
        
        return jsonArray;
    }

    private List<String> getListKey(JsonElement element) {
        List<String> list = new ArrayList<>();
        element.getAsJsonObject().entrySet().forEach((e) -> {
            list.add(e.getKey());
        });
        
        return list;
    }

    private JsonElement getJsonElementRef(int ref) {
        for(JsonElement element : listJsonElement)
            if(element.getAsJsonObject().get("id").getAsInt() == ref){
                linkRef(element);
                listRefToRemove.add(element);
                return element;
            }       
        return null; 
    }

    private void linkRef(JsonElement element) {
        List<String> keys = getListKey(element);
        for(String key : keys){
            if(listKeyToChange.contains(key))
                if(!key.equals("listChildren")){
                    if(element.getAsJsonObject().get(key).isJsonPrimitive())
                        element.getAsJsonObject().add(key, getJsonElementRef(element.getAsJsonObject().get(key).getAsInt()));
                }  
                else{
                    getChildren(element);
                }
                   
        }
    }

    private void getChildren(JsonElement element) {
       
        if(element.getAsJsonObject().get("listChildren").isJsonPrimitive()){
            String[] allChildrenRef = element.getAsJsonObject().get("listChildren").getAsString().split("/");

            for(String childRef : allChildrenRef)
                System.out.println(childRef);

            JsonArray jsonArray = new JsonArray();
            for(String childRef : allChildrenRef)
                jsonArray.add(getJsonElementRef(Integer.parseInt(childRef)));

            element.getAsJsonObject().add("listChildren", jsonArray);
        }
    }
    
}
