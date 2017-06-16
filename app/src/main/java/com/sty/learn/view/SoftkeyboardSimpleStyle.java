package com.sty.learn.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

import com.sty.learn.R;

import java.util.List;

/**
 * Created by shity on 2017/6/12/0012.
 */
//KeyboardView:A view that renders a virtual Keyboard. It handles rendering of keys and detecting key presses and touch movements
public class SoftkeyboardSimpleStyle extends KeyboardView{
    private Context mContext;
    public SoftKeyBoard mKeyBoard = null;
    private KeyboardType mKeyboardType = null;

    private int mHeightContainerView = 0;
    private int mWidthContainerView = 0;

    public enum KeyboardType{
        //默认A920简单小键盘
        A920_SIMPLE,

        //默认A920不支持手输
        A920_SIMPLE_NO_MANUAL,

        //A920彩色键盘
        A920_COLOR,
        A920_COLOR_SWIP,

        //D800固定位置的键盘
        D800_PIN,

        //D800上滑动显示出来的键盘
        D800_SWIP,

        //D800上不显示软键盘
        D800_BLANK
    }

    public SoftkeyboardSimpleStyle(Context context, AttributeSet attrs){
        super(context, attrs);
        this.mContext = context;
    }

    public SoftkeyboardSimpleStyle(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    @Override  //Called to determine the size requirements for this view and all of its children
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        mHeightContainerView = h - getTop() - getBottom();
        mWidthContainerView = w - getRight() - getLeft();
        if(mKeyboardType != null && mWidthContainerView > 0 && mHeightContainerView > 0){
            initKeyBoard();
        }
    }

    @Override //Called when this view should assign a size and position to all of its children
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void initKeyBoard(){
        switch(mKeyboardType){
            case A920_COLOR:
            case A920_COLOR_SWIP:
                mKeyBoard = new SoftKeyBoard(mContext, R.xml.other_keyboard_num, 0, mWidthContainerView,
                        mHeightContainerView);
                break;
            case A920_SIMPLE:
                mKeyBoard = new SoftKeyBoard(mContext, R.xml.symbols_keyboard_num, 0, mWidthContainerView,
                        mHeightContainerView);
                break;
            case D800_BLANK:
            case D800_PIN:
            case D800_SWIP:
                mKeyBoard = new SoftKeyBoard(mContext, R.xml.pax_keyboard_num, 0, mWidthContainerView,
                        mHeightContainerView);
                break;
            default:
                break;
        }
        setKeyboard(mKeyBoard);
    }

    @Override
    public void setKeyboard(Keyboard keyboard) {
        super.setKeyboard(keyboard);
    }

    public void setKeyBoardType(KeyboardType keyboardType){
        mKeyboardType = keyboardType;
    }

    /**
     * 重画一些按键
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Key> keys = mKeyBoard.getKeys();
        for(Key key : keys){
            //数字键盘处理
            switch(mKeyboardType){
                case A920_SIMPLE:
                    drawSimpleNumSpecialKey(key, canvas);
                    break;
                case D800_BLANK:
                    break;
                case A920_COLOR:
                case A920_COLOR_SWIP:
                case D800_PIN:
                case D800_SWIP:
                    drawPaxStyleNumSpecialKey(key, canvas);
                    break;
                default:
                    break;
            }
        }
    }

    //百富风格键盘
    private void drawPaxStyleNumSpecialKey(Key key, Canvas canvas){
        switch(key.codes[0]){
            case 48:
                drawKeyBackground(R.drawable.selection_button_0, canvas, key);
                break;
            case 49:
                drawKeyBackground(R.drawable.selection_button_1, canvas, key);
                break;
            case 50:
                drawKeyBackground(R.drawable.selection_button_2, canvas, key);
                break;
            case 51:
                drawKeyBackground(R.drawable.selection_button_3, canvas, key);
                break;
            case 52:
                drawKeyBackground(R.drawable.selection_button_4, canvas, key);
                break;
            case 53:
                drawKeyBackground(R.drawable.selection_button_5, canvas, key);
                break;
            case 54:
                drawKeyBackground(R.drawable.selection_button_6, canvas, key);
                break;
            case 55:
                drawKeyBackground(R.drawable.selection_button_7, canvas, key);
                break;
            case 56:
                drawKeyBackground(R.drawable.selection_button_8, canvas, key);
                break;
            case 57:
                drawKeyBackground(R.drawable.selection_button_9, canvas, key);
                break;
            case -5:
                drawKeyBackground(R.drawable.selection_button_delete, canvas, key);
                break;
            case 57418:
                drawKeyBackground(R.drawable.selection_button_cancel, canvas, key);
                break;
            case 57419:
                drawKeyBackground(R.drawable.selection_button_000, canvas, key);
                break;
            case 57420:
                drawKeyBackground(R.drawable.selection_button_confirm, canvas, key);
                break;
            case 57421:
                drawKeyBackground(R.drawable.selection_button_00, canvas, key);
                break;
            default:
                break;
        }
    }

    //数字键盘
    private void drawSimpleNumSpecialKey(Key key, Canvas canvas){
        switch(key.codes[0]){
            case 48:
                drawKeyBackground(R.drawable.selection_btn_0, canvas, key);
                break;
            case 49:
                drawKeyBackground(R.drawable.selection_btn_1, canvas, key);
                break;
            case 50:
                drawKeyBackground(R.drawable.selection_btn_2, canvas, key);
                break;
            case 51:
                drawKeyBackground(R.drawable.selection_btn_3, canvas, key);
                break;
            case 52:
                drawKeyBackground(R.drawable.selection_btn_4, canvas, key);
                break;
            case 53:
                drawKeyBackground(R.drawable.selection_btn_5, canvas, key);
                break;
            case 54:
                drawKeyBackground(R.drawable.selection_btn_6, canvas, key);
                break;
            case 55:
                drawKeyBackground(R.drawable.selection_btn_7, canvas, key);
                break;
            case 56:
                drawKeyBackground(R.drawable.selection_btn_8, canvas, key);
                break;
            case 57:
                drawKeyBackground(R.drawable.selection_btn_9, canvas, key);
                break;
            case -5:
                drawKeyBackground(R.drawable.selection_btn_delete, canvas, key);
                break;
            case -3:
                drawKeyBackground(R.drawable.selection_btn_back, canvas, key);
                break;
            default:
                break;
        }
    }

    private void drawKeyBackground(int drawableId, Canvas canvas, Key key){
        Drawable npd = (Drawable) mContext.getResources().getDrawable(drawableId);
        int[] drawableState = key.getCurrentDrawableState();
        if(key.codes[0] != 0){
            npd.setState(drawableState);
        }
        //Specify a bounding rectangle for the Drawable
        npd.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        npd.draw(canvas);
    }
}
