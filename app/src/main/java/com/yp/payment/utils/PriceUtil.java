package com.yp.payment.utils;

public class PriceUtil {

    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    public static String changeF2Y(Long amount){
        if(!amount.toString().matches(CURRENCY_FEN_REGEX)) {
            return "0";
//            throw new Exception("金额格式有误");
        }

        int flag = 0;
        String amString = amount.toString();
        if(amString.charAt(0)=='-'){
            flag = 1;
            amString = amString.substring(1);
        }
        StringBuffer result = new StringBuffer();
        if(amString.length()==1){
            result.append("0.0").append(amString);
        }else if(amString.length() == 2){
            result.append("0.").append(amString);
        }else{
            String intString = amString.substring(0,amString.length()-2);
            for(int i=1; i<=intString.length();i++){
                if( (i-1)%3 == 0 && i !=1){
                    result.append(",");
                }
                result.append(intString.substring(intString.length()-i,intString.length()-i+1));
            }
            result.reverse().append(".").append(amString.substring(amString.length()-2));
        }
        if(flag == 1){
            return "-"+result.toString();
        }else{
            return result.toString();
        }
    }
}
