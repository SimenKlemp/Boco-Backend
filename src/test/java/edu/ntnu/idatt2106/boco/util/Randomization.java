package edu.ntnu.idatt2106.boco.util;

import java.util.Random;

public class Randomization {
    public static String getStringRandomly(int length){

        String upperCaseLetters=("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuilder stringBuilder=new StringBuilder();
        Random random=new Random();

        for(int i=0;i<length;i++){
            int position= random.nextInt(upperCaseLetters.length());
            stringBuilder.append(upperCaseLetters.charAt(position));
        }
        return stringBuilder.toString();
    }

    public static String getEmailRandomly(){
        return getStringRandomly(10)+ "@" + getStringRandomly(5)+".com";
    }
}
