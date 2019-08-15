package util;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常规名字输入过滤器（中、英文，和·）
 */
public class NameInputFilter implements InputFilter {
    int maxLen = 0;

    /**
     * 输入英文的最大长度 。比如你想要限制40个汉字，80个英文字符，传入的值就是80
     * 使用方式：mEdit.setFilters(new InputFilter[]{filter});
     */
    public NameInputFilter(int len) {
        maxLen = len;
    }

    public static int calculateLength(CharSequence c, boolean english) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            char cc = c.charAt(i);
            if ((cc & 0xffff) <= 0xff) {
                len += 0.5;
            } else {
                len++;
            }
        }
        if (english) {
            len = len * 2;
        }
        return (int) Math.round(len);
    }

    @Override
    public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
        int dindex = 0;
        int count = 0;
        Pattern p = Pattern.compile("[a-zA-Z·|•|\u4e00-\u9fa5]+");
        Matcher m = p.matcher(src.toString());
        if (!m.matches())
            return "";

        while (count <= maxLen && dindex < dest.length()) {
            char c = dest.charAt(dindex++);
            if ((c & 0xffff) <= 0xff) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > maxLen) {
            return dest.subSequence(0, dindex - 1);
        }

        int sindex = 0;
        while (count <= maxLen && sindex < src.length()) {
            char c = src.charAt(sindex++);
            if ((c & 0xffff) <= 0xff) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }

        if (count > maxLen) {
            sindex--;
            return src.subSequence(0, sindex);
        }
        return null;
    }

}