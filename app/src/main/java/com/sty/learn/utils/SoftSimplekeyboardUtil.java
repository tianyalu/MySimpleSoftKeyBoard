package com.sty.learn.utils;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.sty.learn.R;
import com.sty.learn.view.CustomEditText;
import com.sty.learn.view.SoftkeyboardSimpleStyle;
import com.sty.learn.view.SoftkeyboardSimpleStyle.KeyboardType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by shity on 2017/6/12/0012.
 */

public class SoftSimplekeyboardUtil {
    private SoftkeyboardSimpleStyle keyboardView;
    public boolean isShow = false; //键盘是否正在展示
    InputFinishListener inputOver;
    KeyBoardStateChangeListener keyBoardStateChangeListener;
    private int ID_container = R.id.fl_container; //container的默认ID
    private int ID_keyboard = R.id.keyboard_view; //keyboard的默认ID

    //mKeyboardType键盘的类型风格
    private SoftkeyboardSimpleStyle.KeyboardType mKeyboardType = null;
    public static final int KEYBOARD_SHOW = 1;
    public static final int KEYBOARD_HIDE = 2;

    private CustomEditText ed; //当前获取焦点的editText
    private CustomEditText[] mCustomEditTexts; //需要使用安全软键盘的editText数组
    private boolean isGetFocus[]; //mCustomEditText[]获取焦点数组
    private Handler showHandler;

    private Context mContext;
    private Activity mActivity;
    private View inflaterView; //dialog中的inflaterView
    private RelativeLayout mContainerView; //包含键盘的相对容器

    /**
     * 该构造方法请于CustomEditText findViewById之后运行
     * @param context
     * @param editTexts
     */
    public SoftSimplekeyboardUtil(Context context, CustomEditText editTexts[]){
        this.mContext = context;
        this.mCustomEditTexts = editTexts;
        this.mActivity = (Activity) mContext;
        initKeyBoardView();
    }

    /**
     *
     * @param ctx 也用于findId
     * @param editTexts
     * @param idOfContainer 包含者ID
     * @param idOfKeyboard 键盘ID
     */
    public SoftSimplekeyboardUtil(Context ctx, CustomEditText editTexts[], int idOfContainer, int idOfKeyboard){
        this.mContext = ctx;
        this.mCustomEditTexts = editTexts;
        this.mActivity = (Activity) mContext;
        this.ID_container = idOfContainer;
        this.ID_keyboard = idOfKeyboard;
        initKeyBoardView();
    }

    /**
     * 弹框类，用这个
     * @param ctx
     * @param editTexts
     * @param view 是弹框的inflaterView
     */
    public SoftSimplekeyboardUtil(Context ctx, CustomEditText editTexts[], View view){
        this.mCustomEditTexts = editTexts;
        this.mContext = ctx;
        this.mActivity = (Activity) mContext;
        this.inflaterView = view;
        initKeyBoardView();
    }

    private boolean initKeyBoardType(CustomEditText customEditText){
        boolean flag = false;
        ed = customEditText;
        SoftkeyboardSimpleStyle.KeyboardType beforeKeyboardType = mKeyboardType;
        mKeyboardType = customEditText.getKeyboardType();
        if (beforeKeyboardType != null){
            flag = !(beforeKeyboardType == mKeyboardType);
        }
        return flag;
    }

    private void initKeyBoardView() {
        isGetFocus = new boolean[mCustomEditTexts.length];
        for (int i = 0; i < mCustomEditTexts.length; i++) {
            isGetFocus[i] = false;
            mCustomEditTexts[i].setTag(i);
            setKeyBoardCursorNew(mCustomEditTexts[i]); //防止系统键盘的弹出
        }

        if (inflaterView != null) {
            mContainerView = (RelativeLayout) inflaterView.findViewById(ID_container);
        } else {
            if (mContainerView == null) {
                mContainerView = (RelativeLayout) mActivity.findViewById(ID_container);
            }
        }
        initKeyBoardType(mCustomEditTexts[0]); //默认对第一个customEditText做处理
        setListener(); //给mCustomEditText[]设置监听
        if(mKeyboardType == KeyboardType.D800_PIN){
            showKeyboardLayout(); //D800_PIN的类型，优先调用显示键盘方法
        }
    }

    private void setListener(){
        for(int i = 0; i < mCustomEditTexts.length; i++){
            mCustomEditTexts[i].setOnFocusChangeListener(mOnFocusChangeListener);
            mCustomEditTexts[i].setOnTouchListener(mOnTouchListener);
        }
    }

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener(){

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            isGetFocus[(Integer) v.getTag()] = hasFocus;
            if(hasFocus){
                //初始化ed对应的inputType
                if(initKeyBoardType((CustomEditText) v)){
                    hideKeyboardLayout();
                    if(keyboardView != null){
                        showKeyboardLayout();
                    }
                }
            }else{
                showHandler = new Handler();
                showHandler.postDelayed(new Runnable(){
                    //两个customEditText执行此方法，可能checkFocus会出现全部是false的情况，做一个小的延时处理
                    @Override
                    public void run() {
                        checkFocus();
                    }
                },40);
            }
        }
    };

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_UP && !isShow){
                showKeyboardLayout();
            }
            return false;
        }
    };

    /**
     * 处理当界面还有非customEditText的情况下，点击这个普通的editText或者其它View获取
     * 焦点的时候，隐藏软键盘
     */
    private void checkFocus(){
        for(int i = 0; i < isGetFocus.length; i++){
            if(isGetFocus[i])
                return;
        }
        hideKeyboardLayout(true);
    }

    /**
     * 调用super.onBackPress()时判断键盘是否需要隐藏调用此方法，防止当类型为D800_PIN时按
     * 返回键不能直接返回
     * @return 键盘是否显示，是D800_PIN的话，返回false
     */
    public boolean isShow(){
        if(mKeyboardType == KeyboardType.D800_PIN){
            return false;
        }else{
            return isShow;
        }
    }

    /**
     * 判断系统输入法是否打开
     * @param editText
     * @return
     */
    private boolean setKeyBoardCursorNew(CustomEditText editText){
        boolean flag = false;
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive(); //is若返回true，则表示输入法打开
        if(isOpen){
            if(imm.hideSoftInputFromWindow(editText.getWindowToken(),0)){
                flag = true;
            }
        }
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
            editText.setInputType(InputType.TYPE_NULL);
        }else{
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            Class<CustomEditText> cls = CustomEditText.class;
            Method setShowSoftInputOnFocus;
            try{
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            }catch (NoSuchMethodException e){
                e.printStackTrace();
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }catch (InvocationTargetException e){
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 显示软键盘的方法
     */
    public void showKeyboardLayout(){
        if(mKeyboardType == KeyboardType.D800_BLANK || mKeyboardType == KeyboardType.A920_SIMPLE_NO_MANUAL){
            hideKeyboardLayout(true);
            return;
        }
        if(setKeyBoardCursorNew(ed)){
            showHandler = new Handler();
            showHandler.postDelayed(new Runnable(){

                @Override
                public void run() {
                    show();
                }
            }, 400);
        }else{
            //直接显示
            show();
        }
    }

    private void show(){
        if(mContainerView != null){
            mContainerView.setVisibility(View.VISIBLE);
        }
        if(keyboardView != null){
            keyboardView.setVisibility(View.GONE);
        }
        initKeyBoard(ID_keyboard);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setKeyBoardType(mKeyboardType); //初始化keyboard会在keyboardView里执行
        isShow = true;
        if(keyBoardStateChangeListener != null){
            keyBoardStateChangeListener.KeyBoardStateChange(KEYBOARD_SHOW, ed);
        }
    }

    private void initKeyBoard(int keyBoardViewID){
        mActivity = (Activity) mContext;
        if(inflaterView != null){
            keyboardView = (SoftkeyboardSimpleStyle) inflaterView.findViewById(keyBoardViewID);
        }else{
            keyboardView = (SoftkeyboardSimpleStyle) mActivity.findViewById(keyBoardViewID);
        }
        keyboardView.setVisibility(View.VISIBLE);
        //放置keyboardType的类型不一样，加载错误的布局
        if(ed.getKeyboardType() != mKeyboardType){
            mKeyboardType = ed.getKeyboardType();
        }
        keyboardView.setEnabled(true);
        keyboardView.setOnKeyboardActionListener(listener);
        keyboardView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 关闭软键盘的方法
     */
    public void hideKeyboardLayout(){
        hideKeyboardLayout(false);
    }

    /**
     * 强制隐藏软键盘的方法
     * @param isForceClose
     */
    private void hideKeyboardLayout(boolean isForceClose){
        if(!isForceClose){
            if(mKeyboardType == KeyboardType.D800_BLANK || mKeyboardType == KeyboardType.D800_PIN
                    || mKeyboardType == KeyboardType.A920_SIMPLE_NO_MANUAL || !isShow){
                return;
            }
        }
        isShow = false;
        if(keyboardView != null){
            if(keyboardView.getVisibility() == View.VISIBLE){
                keyboardView.setVisibility(View.INVISIBLE);
            }
        }
        if(mContainerView != null){
            mContainerView.setVisibility(View.GONE);
        }
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
            return;
        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override //Send a key press to the listener.
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            if(primaryCode == Keyboard.KEYCODE_CANCEL){ //取消
                hideKeyboardLayout();
                if(inputOver != null){
                    inputOver.inputHssOver(primaryCode, ed);
                }
            }else if(primaryCode == Keyboard.KEYCODE_DELETE) { //删除
                if(editable != null && editable.length() > 0){
                    if(start > 0){
                        editable.delete(start - 1, start);
                    }
                }
            }else if (primaryCode == 57419){ //000
                editable.insert(start, "000");
            }else if (primaryCode == 57418){ //back
                new Thread(new Runnable(){
                    public void run(){
                        new Instrumentation().sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    }
                }).start();
            }else if (primaryCode == 57420){ //confirm
                new Thread(new Runnable(){
                    public void run(){
                        new Instrumentation().sendCharacterSync(KeyEvent.KEYCODE_ENTER);
                    }
                }).start();
            }else if(primaryCode == 57421){ //00
                editable.append("00");
            }else{
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }

        @Override //Sends a sequence of characters to the listener.
        public void onText(CharSequence text) {
            if(ed == null){
                return;
            }
            Editable editable = ed.getText();
            int start = ed.getSelectionStart();
            String temp = editable.subSequence(0, start) + text.toString()
                    + editable.subSequence(start, editable.length());
            ed.setText(temp);
            Editable etext = ed.getText();
            Selection.setSelection(etext, start + 1);
        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    /**
     * 输入监听
     */
    public interface InputFinishListener{
        public void inputHssOver(int onclickType, EditText editText);
    }

    /**
     * 设置监听事件
     * @param listener
     */
    public void setInputOverListener(InputFinishListener listener){
        this.inputOver = listener;
    }

    public interface KeyBoardStateChangeListener{
        public void KeyBoardStateChange(int state, EditText editText);
    }

    public void setKeyBoardStateChangeListener(KeyBoardStateChangeListener listener){
        this.keyBoardStateChangeListener = listener;
    }
}
