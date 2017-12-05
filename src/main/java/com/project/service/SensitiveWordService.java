package com.project.service;

import com.project.aspect.LogAspect;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Claire on 12/5/17.
 */
@Service
public class SensitiveWordService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                addWord(lineTxt.trim());
            }
            reader.close(); //Todo 应该需要在finally里面做判断，并检查异常
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    private class TrieNode {
        //是不是关键词结尾
        private boolean end = false;

        //当前节点的所有子节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        void setKeywordEnd(boolean end) {
            this.end = end;
        }

        boolean isKeywordEnd() {
            return end;
        }
    }

    private TrieNode rootNode = new TrieNode();

    //过滤敏感词中间的特殊符号，如空格、*等
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-ic>0x9FFF 东亚文字
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    // 增加关键词
    private void addWord(String lineTxt) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineTxt.length(); i++) {
            Character c = lineTxt.charAt(i);
            if (isSymbol(c)){
                continue;
            }

            TrieNode node = tempNode.getSubNode(c);

            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;

            if (i == lineTxt.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    //过滤text中的敏感词
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String replacement = "**";
        TrieNode tempNode = rootNode;
        StringBuffer result = new StringBuffer();
        int begin = 0, position = 0;
        while (position < text.length()) {
            char c = text.charAt(position);
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                result.append(text.charAt(begin++));
                position = begin;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                // 发现敏感词
                result.append(replacement);
                position++;
                begin = position;
                tempNode = rootNode;
            } else {
                position++;
            }
        }
        result.append(text.substring(begin));
        return result.toString();
    }

    public static void main(String[] args) {
        SensitiveWordService s = new SensitiveWordService();
        s.addWord("赌博");
        System.out.println(s.filter("你不@@能赌&博！！！"));
    }
}
