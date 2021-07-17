package com.tl.toc.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

/**
 * This is the common util and it will hold commmon methods whi will support
 * and provide information to all tests
 */
public class CommonUtil {

    /**
     * This method is to validate if a text is a json or not
     * @param data
     * @return
     */
    public boolean isJson(String data){
        try{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(data);
        }catch (Exception ex){
            return false;
        }
        return true;
    }

    /**
     * This method will convert text into array list of string
     * @param texts
     * @return
     */
    public ArrayList<String> readTextAsList(String texts){
        ArrayList<String> data = new ArrayList<>();
        Scanner scanner = new Scanner(texts);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            data.add(line);
        }
        scanner.close();
        return data;
    }

    /**
     * This method will convert json string into json node
     * @param data
     * @return
     */
    public JsonNode getJsonBody(String data){
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(data);
        }catch (Exception ex){
            return null;
        }
    }

    /**
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public Properties readPropertiesFile(String fileName){
        InputStream fis = null;
        Properties prop = null;

        try {
            fis = this.getClass().getClassLoader().getResourceAsStream(fileName);
            prop = new Properties();
            prop.load(fis);
        } catch(FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return prop;
    }
}
