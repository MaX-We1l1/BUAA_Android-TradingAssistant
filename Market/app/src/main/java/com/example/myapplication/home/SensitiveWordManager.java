package com.example.myapplication.home;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

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
            InputStream inputStream = context.getAssets().open(fileName); // 打开文件
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // 去掉空格和换行符
                if (!line.isEmpty()) {
                    sensitiveWords.add(line); // 添加到集合中
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 检查文本是否包含敏感词（完整匹配）
    public Boolean containsSensitiveWord(String text) {
        for (String word : sensitiveWords) {
            // 检查是否包含完整的敏感词
            if (!word.isEmpty() && text.matches(".*\\b" + Pattern.quote(word) + "\\b.*")) {
                Log.e("SensitiveMatch", String.format("匹配成功：<%s>:<%s>", text, word));
                return true; // 只需找到第一个匹配的词
            }
        }
        return false; // 没有匹配的敏感词
    }
}
