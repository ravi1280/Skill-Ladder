package com.example.skill_ladder.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CodeGenarete {
    public static String generateUniqueCode() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8).toUpperCase();

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//        return "SKILL" + sdf.format(new Date()); // Prefix with project name
    }
}
