package com.example.myapplication.home;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class SensitiveWordManager {
    private Context context;
    private Set<String> sensitiveWords;

    public SensitiveWordManager(Context context) {
        this.context = context;
        this.sensitiveWords = new HashSet<>();
    }

    // 加载敏感词文件
    public void loadSensitiveWords(String fileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                sensitiveWords.add(line.trim()); // 添加到 HashSet，自动去重
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 检查文本是否包含敏感词
    public Boolean containsSensitiveWord(String text) {
        for (String word : sensitiveWords) {
            if (text.contains(word) && !word.isEmpty()) {
//                Log.e("PRINT" , String.format("<%s>:<%s>", text, word));
                return true; // 返回第一个匹配结果即可
            }
        }
        return false;
    }
}
