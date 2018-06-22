package com.treebricks.educationboardresult.model;

import java.util.Calendar;

public class Year {
    public static String[] getExamYears(){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int firstYear = 1996;
        int yearSize = currentYear - firstYear + 1;
        String[] years = new String[yearSize];

        for (int i = 0; i < yearSize; i++) {
            years[i] = String.valueOf(currentYear-i);
        }
        return years;
    }
}
