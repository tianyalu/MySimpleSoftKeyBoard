package com.sty.learn.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by shity on 2017/6/14/0014.
 */

public class EnterAmountTextWatcher implements TextWatcher {
   private boolean mEditing;
    private String strPre = "";
    private final int MAX_DIGITS = 9;

    public EnterAmountTextWatcher(){
        mEditing = false;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!mEditing){
            mEditing = true;
            String digits = s.toString().replace(".", "").trim().replaceAll("[^(0-9)]", "");
            String str = "";
            if(digits.length() > MAX_DIGITS){
                str = strPre;
            }else{
                if(digits == null || digits.length() == 0){
                    str = "0.00";
                }else{
                    //如果输入非法数据时，设置成0.00
                    try{
                        str = String.format("%d.%02d", Long.valueOf(digits) / 100, Long.valueOf(digits) % 100);
                    }catch (Exception e){
                        e.printStackTrace();
                        str = "0.00";
                    }
                }
            }
            try{
                s.replace(0, s.length(), str);
                strPre = str;
            }catch (NumberFormatException nfe){
                s.clear();
            }
            mEditing = false;
        }
    }
}
