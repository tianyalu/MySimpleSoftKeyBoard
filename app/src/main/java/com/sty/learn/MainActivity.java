package com.sty.learn;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.sty.learn.utils.EnterAmountTextWatcher;
import com.sty.learn.utils.SoftSimplekeyboardUtil;
import com.sty.learn.view.CustomEditText;

public class MainActivity extends AppCompatActivity{
    private SoftSimplekeyboardUtil keyboardUtil;
    private CustomEditText edtAmount; //输入金额
    private TextView app_flag;

    private static final int KEY_BOARD_CANCEL = 1;
    private static final int KEY_BOARD_OK = 2;

    protected Handler handler = new Handler(){
        public void handleMessage(Message msg){
            handleMsg(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //去掉标题栏（没用）
        setContentView(R.layout.activity_main);

        initViews();
        setListener();
    }

    protected void initViews(){
        //app名字标识
        app_flag = (TextView) findViewById(R.id.app_flag);
        app_flag.bringToFront();
        //金额输入框处理
        edtAmount = (CustomEditText) findViewById(R.id.amount_edtext);
        edtAmount.setHint(getString(R.string.amount_default));
        edtAmount.setInputType(InputType.TYPE_NULL);
        edtAmount.setIMEEnabled(false, true);

        keyboardUtil = new SoftSimplekeyboardUtil(this, new CustomEditText[]{edtAmount});
    }

    protected void setListener(){
        edtAmount.addTextChangedListener(new EnterAmountTextWatcher(){

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
            }
        });

        edtAmount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP){
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        handler.sendEmptyMessage(KEY_BOARD_CANCEL);
                    }else if(keyCode == KeyEvent.KEYCODE_ENTER){
                        handler.sendEmptyMessage(KEY_BOARD_OK);
                    }
                }
                return false;
            }
        });
    }

    protected void handleMsg(Message msg){
        switch(msg.what){
            case KEY_BOARD_OK:
                String amount = edtAmount.getText().toString().trim();
                if(amount != null && !amount.equals("0.00")){
                    // TODO: 2017/6/14/0014
                }else{
                    keyboardUtil.hideKeyboardLayout();
                }
                edtAmount.setFocusable(true);
                edtAmount.setFocusableInTouchMode(true);
                edtAmount.requestFocus();
                break;
            case KEY_BOARD_CANCEL:
                edtAmount.setText("");
                keyboardUtil.hideKeyboardLayout();
                break;
            default:
                break;
        }
    }

    private String getInputAmount(){
        String amount = edtAmount.getText().toString().trim();
        if(amount == null || amount.length() == 0 || amount.equals("0.00")){
            edtAmount.setFocusable(true);
            edtAmount.requestFocus();
            keyboardUtil.showKeyboardLayout();
            return null;
        }
        return amount;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(keyboardUtil.isShow){
                keyboardUtil.hideKeyboardLayout();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
