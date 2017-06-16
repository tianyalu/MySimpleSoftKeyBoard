package com.sty.learn.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;

import java.util.List;

/**
 * Created by shity on 2017/6/12/0012.
 */
//Keyboard:Loads an XML description of a keyboard and stores the attributes of the keys. A keyboard consists of rows of keys.
public class SoftKeyBoard extends Keyboard {

    public SoftKeyBoard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public SoftKeyBoard(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
        setSpecialKey(57420);
    }

    public SoftKeyBoard(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
    }

    public SoftKeyBoard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }

    @Override
    public List<Key> getKeys() {
        return super.getKeys();
    }

    @Override
    public List<Key> getModifierKeys() {
        return super.getModifierKeys();
    }

    @Override
    protected int getHorizontalGap() {
        return super.getHorizontalGap();
    }

    @Override
    protected void setHorizontalGap(int gap) {
        super.setHorizontalGap(gap);
    }

    @Override
    protected int getVerticalGap() {
        return super.getVerticalGap();
    }

    @Override
    protected void setVerticalGap(int gap) {
        super.setVerticalGap(gap);
    }

    @Override
    protected int getKeyHeight() {
        return super.getKeyHeight();
    }

    @Override
    protected void setKeyHeight(int height) {
        super.setKeyHeight(height);
    }

    @Override
    protected int getKeyWidth() {
        return super.getKeyWidth();
    }

    @Override
    protected void setKeyWidth(int width) {
        super.setKeyWidth(width);
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    @Override
    public int getMinWidth() {
        return super.getMinWidth();
    }

    @Override
    public boolean setShifted(boolean shiftState) {
        return super.setShifted(shiftState);
    }

    @Override
    public boolean isShifted() {
        return super.isShifted();
    }

    @Override
    public int getShiftKeyIndex() {
        return super.getShiftKeyIndex();
    }

    @Override
    public int[] getNearestKeys(int x, int y) {
        return super.getNearestKeys(x, y);
    }

    @Override
    protected Row createRowFromXml(Resources res, XmlResourceParser parser) {
        return super.createRowFromXml(res, parser);
    }

    @Override
    protected Key createKeyFromXml(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
        return super.createKeyFromXml(res, parent, x, y, parser);
    }

    private void setSpecialKey(int keyCode){
        for(int i = 0; i < getKeys().size(); i++){
            if(getKeys().get(i).codes[0] == keyCode){
                getKeys().get(i).height += getVerticalGap();
                return;
            }
        }
    }
}
