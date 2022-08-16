package com.base.java.project;

import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Solution {
    ExecutorService executor = Executors.newFixedThreadPool(10);

    public void runQueriesAndPrint() {
        List<String> queries = readFile();
        for (String url : queries) {
            String urlContent = callUrl(url);
            System.out.println(urlContent);
        }
        System.out.println(queries);

    }

    private String callUrl(String q) {
        String result = "";
        CallUrl taskCallToGetTotalItems = new CallUrl(q, 1);
        Future<String> taskCallToGetTotalItemsResult = executor.submit(taskCallToGetTotalItems);
        int total_items =2; //will do first call to get the right value
        try {
            String jsonResult = (String) (taskCallToGetTotalItemsResult.get());
            JSONObject obj = new JSONObject(jsonResult);
            total_items = obj.getInt("total_count");
        }catch (Exception e){
            System.out.println(e);
        }

        List<Future> calls = new ArrayList<>();
        int pages = total_items / 100 +1;// divide by number of items per page I requested 100 for example 408 /100 +1 =5 pages
        for (int page = 1; page < pages; page++) {
            try {
                CallUrl task = new CallUrl(q, page);
                Future<String> callResult = executor.submit(task);
                calls.add(callResult);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
                break;

            }
        }
        for(Future call:calls){
            try {
                String jsonResult = (String) (call.get());
                result= result+jsonResult;
            }catch (Exception e){
                System.out.println(e);
            }
        }


        return result;
    }

    private List<String> readFile() {
        List<String> result = new ArrayList<>();

        try {
            File myObj = new File("github_queries.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                result.add(line);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return result;
    }
}
