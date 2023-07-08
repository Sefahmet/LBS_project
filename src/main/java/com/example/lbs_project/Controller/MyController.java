package com.example.lbs_project.Controller;

public class MyController {


    public String redirectToIndex() {
        try{
            return "redirect:http://localhost:8080/routing2023_ASA/index.html";

        }catch (Exception e){
            return e.getMessage();
        }
    }
}