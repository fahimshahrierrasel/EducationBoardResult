package com.treebricks.educationboardresult.model;

public class Board {
    public static String getBoardKeyword(String boardName){
        String keyword;
        switch (boardName){
            case "Technical":
                keyword = boardName.substring(0, 3).toLowerCase();
                break;
            case "DIBS(DHAKA)":
                keyword = boardName.substring(0, 4).toLowerCase();
                break;
            default:
                keyword = boardName.toLowerCase();
        }
        return keyword;
    }
}
