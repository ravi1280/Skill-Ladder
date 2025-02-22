package com.example.skill_ladder.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CodeGenarete {
    public static String generateUniqueCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return "SKILL" + sdf.format(new Date()); // Prefix with project name
    }
}
