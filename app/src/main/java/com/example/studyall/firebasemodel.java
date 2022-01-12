package com.example.studyall;

public class firebasemodel {
    private static String title;
    private static String content;


    public firebasemodel(){

    }

    public firebasemodel (String title, String content){
        this.title=title;
        this.content=content;
    }

    public static String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
