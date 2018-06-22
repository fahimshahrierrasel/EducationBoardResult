package com.treebricks.educationboardresult.model;

public class Exam {
    public static String getExamKeyword(String examName) {
        String keyword;
        switch (examName) {
            case "SSC/Dakhil/Equivalent":
                keyword = "ssc";
                break;
            case "JSC/JDC":
                keyword = "jsc";
                break;
            case "SSC/Dakhil":
                keyword = "ssc";
                break;
            case "SSC(Vocational)":
                keyword = "ssc_voc";
                break;
            case "HSC/Alim":
                keyword = "hsc";
                break;
            case "HSC(Vocational)":
                keyword = "hsc_voc";
                break;
            case "HSC(BM)":
                keyword = "hsc_hbm";
                break;
            case "Diploma in Commerce":
                keyword = "hsc_dic";
                break;
            case "Diploma in Business Studies":
                keyword = "hsc";
                break;
            default:
                keyword = "ssc";
                break;
        }

        return  keyword;
    }
}
