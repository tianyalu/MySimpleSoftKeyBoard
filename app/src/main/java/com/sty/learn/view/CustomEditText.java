package com.sty.learn.view;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import com.sty.learn.R;
import com.sty.learn.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by shity on 2017/6/13/0013.
 */

public class CustomEditText extends EditText {
    private SoftkeyboardSimpleStyle.KeyboardType mTypeOfKeyboard;
    Instrumentation in;

    public CustomEditText(Context context, AttributeSet attrs){
        super(context, attrs);
        in = new Instrumentation();
        this.setLongClickable(false);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyEditText);
        initKeyBoardType(context,a);
        a.recycle();
        setLongClickable(false);
        setTextIsSelectable(false);
        setCustomSelectionActionModeCallback(new ActionMode.Callback(){

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    private void initKeyBoardType(Context context,TypedArray a){
        int typeOfKeyboard = a.getInt(R.styleable.MyEditText_keyboardType, -9527);
        if(typeOfKeyboard < 0){ //默认：A920使用极简类型keyboardtype,D800使用固定在屏幕里的类型
            if(Utils.isScreenOriatationPortrait(context)) {
                mTypeOfKeyboard = SoftkeyboardSimpleStyle.KeyboardType.A920_SIMPLE;
            }else{
                mTypeOfKeyboard = SoftkeyboardSimpleStyle.KeyboardType.D800_PIN;
            }
        }else{
            /*
             * <attr name="keyboardType"> <enum name="A920_SIMPLE" value="0"/> <enum name="A920_COLOR" value="1"/> <enum
             * name="A920_COLOR_SWIP" value="2"/> <enum name="D800_PIN" value="3"/> <enum name="D800_SWIP" value="4"/>
             * <enum name="D800_BLANK" value="5"/> </attr>
             */
            switch(typeOfKeyboard){
                case 0:
                    mTypeOfKeyboard = SoftkeyboardSimpleStyle.KeyboardType.A920_SIMPLE;
                    break;
                case 1:
                    mTypeOfKeyboard = SoftkeyboardSimpleStyle.KeyboardType.A920_COLOR;
                    break;
                case 2:
                    mTypeOfKeyboard = SoftkeyboardSimpleStyle.KeyboardType.A920_COLOR_SWIP;
                    break;
                case 3:
                    mTypeOfKeyboard = SoftkeyboardSimpleStyle.KeyboardType.D800_PIN;
                    break;
                case 4:
                    mTypeOfKeyboard = SoftkeyboardSimpleStyle.KeyboardType.D800_SWIP;
                    break;
                case 5:
                    mTypeOfKeyboard = SoftkeyboardSimpleStyle.KeyboardType.D800_BLANK;
                    break;
                default:
                    break;
            }

        }
    }

    public void setIMEEnabled(boolean enable, boolean showCursor){
        int currentVersion = Build.VERSION.SDK_INT;
        String methodName = null;
        if(currentVersion >= 16){
            //4.2
            methodName = "setShowSoftInputOnFocus";
        }else if(currentVersion >= 14){
            //4.0
            methodName = "setSoftInputShownOnFocus";
        }
        if(methodName == null){
            this.setInputType(InputType.TYPE_NULL);
        }
        if(!enable){
            if(showCursor){
                ((Activity) getContext()).getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                try{
                    Class<CustomEditText> cls = CustomEditText.class;
                    Method setSoftInputShownOnFocus;
                    setSoftInputShownOnFocus = cls.getMethod(methodName, boolean.class);
                    setSoftInputShownOnFocus.setAccessible(true);
                    setSoftInputShownOnFocus.invoke(this, false);
                }catch (NoSuchMethodException e){
                    this.setInputType(InputType.TYPE_NULL);
                }catch (IllegalAccessException e){
                    e.printStackTrace();;
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }catch (InvocationTargetException e){
                    e.printStackTrace();
                }
            }else{
                this.setInputType(InputType.TYPE_NULL);
            }
        }
    }

    public SoftkeyboardSimpleStyle.KeyboardType getKeyboardType(){
        return mTypeOfKeyboard;
    }

    public void setKeyboardType(SoftkeyboardSimpleStyle.KeyboardType keyboardType){
        mTypeOfKeyboard = keyboardType;
    }
}
