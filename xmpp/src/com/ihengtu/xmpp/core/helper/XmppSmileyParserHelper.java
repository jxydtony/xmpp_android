package com.ihengtu.xmpp.core.helper;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

public class XmppSmileyParserHelper {
	private Context mContext;  
    private String[] mSmileyTexts;
    private Pattern mPattern;  
    private HashMap<String, Integer> mSmileyToRes;  
    public static final int[] DEFAULT_SMILEY_RES_IDS = {  
//        R.drawable.smile,  
//        R.drawable.img1,  
//        R.drawable.smile,  
//        R.drawable.img1,
//        R.drawable.smile,  
//        R.drawable.img1,
//        R.drawable.smile,  
//        R.drawable.img1,
//        R.drawable.smile,  
//        R.drawable.img1,
//        R.drawable.smile,
    };  
  
    public XmppSmileyParserHelper(Context context) {  
        mContext = context;  
        mSmileyTexts = mContext.getResources().getStringArray(DEFAULT_SMILEY_TEXTS);  
        mSmileyToRes = buildSmileyToRes();  
        mPattern = buildPattern();  
    }  
    
//    public static final int DEFAULT_SMILEY_TEXTS = R.array.default_smiley_texts;  
    public static final int DEFAULT_SMILEY_TEXTS = 0;  
    
    private HashMap<String, Integer> buildSmileyToRes() {  
        if (DEFAULT_SMILEY_RES_IDS.length != mSmileyTexts.length) {  
//          Log.w("XmppSmileyParserHelper", "Smiley resource ID/text mismatch");  
            //�����������Ҫ�����鶨��ĳ���һ�£�  
            throw new IllegalStateException("Smiley resource ID/text mismatch");  
        }
        HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(mSmileyTexts.length);  
        for (int i = 0; i < mSmileyTexts.length; i++) {  
            smileyToRes.put(mSmileyTexts[i], DEFAULT_SMILEY_RES_IDS[i]);  
        }  
        return smileyToRes;  
    }  
  
    //����������ʽ  
    private Pattern buildPattern() {  
        StringBuilder patternString = new StringBuilder(mSmileyTexts.length * 3);  
        patternString.append('(');  
        for (String s : mSmileyTexts) {  
            patternString.append(Pattern.quote(s));  
            patternString.append('|');  
        }  
        patternString.replace(patternString.length() - 1, patternString.length(), ")");  
  
        return Pattern.compile(patternString.toString());  
    }  
  
    //����ı��滻��ͼƬ  
    public CharSequence replace(CharSequence text) {  
        SpannableStringBuilder builder = new SpannableStringBuilder(text);  
        Matcher matcher = mPattern.matcher(text);  
        while (matcher.find()) {  
            int resId = mSmileyToRes.get(matcher.group());  
            builder.setSpan(new ImageSpan(mContext, resId),matcher.start(), matcher.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
        }  
        return builder;  
    }  
}
