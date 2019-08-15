package util;

import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {


    /**
     * 字符串超过一定长度加省略号
     *
     * @param str
     * @param size
     * @return
     */
    public static String spilt(String str, int size) {
        if (str != null && !str.equals("")) {
            if (str.length() > size) {
                str = str.substring(0, size) + " ...";
            }
            return str;
        }
        return str;
    }

    /**
     * 金额格式化
     *
     * @param s   要格式化的金额
     * @param len 保留的小数位 四舍五入
     * @return
     */
    public static String spiltAmt(String s, int len) {
        if (s == null || s.equals("") || s.equals("null")) {
            s = "0";
        }
        NumberFormat formater;
        double num = Double.parseDouble(s);
        if (len == 0) {
            formater = new DecimalFormat("###,###");
        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,##0.");
            for (int i = 0; i < len; i++) {
                buff.append("0");
            }
            formater = new DecimalFormat(buff.toString());
        }
        formater.setRoundingMode(RoundingMode.HALF_UP);

        return formater.format(num);

    }

    /**
     * 金额格式化
     * 无逗号
     *
     * @param s   要格式化的金额
     * @param len 保留的小数位 四舍五入
     */
    public static String spiltAmt2(String s, int len) {
        if (s == null || s.equals("") || s.equals("null")) {
            s = "0";
        }
        NumberFormat formater;
        double num = Double.parseDouble(s);
        if (len == 0) {
            formater = new DecimalFormat("#");
        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("#0.");
            for (int i = 0; i < len; i++) {
                buff.append("0");
            }
            formater = new DecimalFormat(buff.toString());
        }
        formater.setRoundingMode(RoundingMode.HALF_UP);

        return formater.format(num);

    }

    /**
     * 金额四舍五入,保留两位小数
     */
    public static String round(String s, int len) {
        if (s == null || s.equals("") || s.equals("null")) {
            s = "0";
        }

        String result;
        try {
            result = decimalUp(Double.parseDouble(s), len) + "";
        } catch (IllegalArgumentException e) {
            result = "";
        }
        return result;
    }

    /**
     * 金额格式化
     *
     * @param num 要格式化的金额
     * @param len 保留的小数位 四舍五入
     * @return
     */
    public static String spiltAmt(double num, int len) {
        NumberFormat formater;
        if (len == 0) {
            formater = new DecimalFormat("###,###");
        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,##0.");
            for (int i = 0; i < len; i++) {
                buff.append("0");
            }
            formater = new DecimalFormat(buff.toString());
        }
        formater.setRoundingMode(RoundingMode.HALF_UP);

        return formater.format(num);

    }

    /**
     * 金额格式化
     * 无逗号
     *
     * @param num 要格式化的金额
     * @param len 保留的小数位 四舍五入
     */
    public static String spiltAmt2(double num, int len) {
        NumberFormat formater;
        if (len == 0) {
            formater = new DecimalFormat("#");
        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("#0.");
            for (int i = 0; i < len; i++) {
                buff.append("0");
            }
            formater = new DecimalFormat(buff.toString());
        }
        formater.setRoundingMode(RoundingMode.HALF_UP);

        return formater.format(num);

    }

    /**
     * 保留两位小数
     * 描 述： 四舍五入
     * 作 者：qin
     *
     * @param d
     * @return
     */
    public static double decimalUp(double d) {
        return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 保留两位小数
     * 描 述：两位后舍
     * 作 者：qin
     *
     * @param d
     * @return
     */
    public static double decimalDown(double d) {
        return new BigDecimal(d).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
    }

    public static double decimalUp(double d, int len) {
        return new BigDecimal(d).setScale(len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double decimalDown(double d, int len) {
        return new BigDecimal(d).setScale(len, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 金额去格式化
     *
     * @param s
     * @return
     */
    public static String delComma(String s) {
        String formatString = "";
        if (s != null && s.length() >= 1) {
            formatString = s.replaceAll(",", "");
        }

        return formatString;
    }

    /**
     * 半角转全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 字符串为空时的处理
     *
     * @param str
     * @Description:
     */
    public static String getString(String str) {
        if (str == null || str.equals("null")) {
            return "";
        } else {
            return str;
        }
    }

    /**
     * 密码规则 4.0.0  限制新密码,旧密码不做限制
     * <p>
     * 6-12位数字、字母和字符混排，种类组合不的少于两种
     */
    public static Boolean isPsRight(String string) {
        String str = "^(?![\\d]+$)(?![a-zA-Z]+$)(?![^\\da-zA-Z]+$).{6,12}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(string);
        return m.matches();
    }

    /**
     * 判断数字是否为空，若为空，置为0
     *
     * @param str
     * @return
     */
    public static String numberIsNull(String str) {
        if (str == null || str.equals("") || str.equals("null")) {
            return "0";
        } else {
            return str;
        }
    }

    /**
     * 现在号码段分配如下：
     * <p/>
     * 移动： 139 138 137 136 135 134 147 150 151 152 157 158 159 182 183 187 188
     * <p/>
     * 联通： 130 131 132 155 156 185 186 145
     * <p/>
     * 电信： 133 153 180 181 189 177
     * <p/>
     * 参考：http://www.jihaoba.com/tools/?com=haoduan
     * <p>
     * 新规则：
     * 客户端不做校验
     * 1开头，11位即可
     *
     * @param number
     * @return
     * @Description:
     */
    public static boolean isPhoneNumberFormat(String number) {
        // ^((13[0-9])|(14[5,7])|(15[^4,\\D])|(18[^4,\\D]))\\d{8}$
//        Pattern p = Pattern.compile("^0?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$");
//        Matcher m = p.matcher(number);
//        return m.matches();
        return !TextUtils.isEmpty(number) && number.startsWith("1") && number.length() == 11;
    }

    /**
     * 组装url请求参数
     *
     * @param parameterMap
     * @return
     */
    public static String assembleUrlParameter(Map<String, String> parameterMap) {
        StringBuffer buffer = new StringBuffer();
        if (parameterMap != null) {
            Iterator<Entry<String, String>> iter = parameterMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry entry = iter.next();
                buffer.append("&");
                buffer.append(entry.getKey().toString());
                buffer.append("=");
                try {
                    buffer.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }
        return buffer.toString();
    }

    /**
     * 通过{n},格式化.
     *
     * @param src
     * @param objects
     * @return
     */
    public static String format(String src, Object... objects) {
        int k = 0;
        for (Object obj : objects) {
            src = src.replace("{" + k + "}", obj.toString());
            k++;
        }
        return src;
    }

    public static boolean isChinese(String src) {
        String regx = "[\u4e00-\u9fa5]";
        return src.matches(regx);
    }

    /**
     * 是否不为空
     *
     * @param s
     * @return
     */
    public static boolean isNotEmpty(String s) {
        return s != null && !"".equals(s.trim());
    }

    /**
     * 是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return s == null || "".equals(s.trim()) || s.trim().length() == 0;
    }

    public static String checkNull(String s) {
        return s != null ? s : "";
    }

    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    /**
     * 身份证号验证
     */
    public static boolean personIdValidation(String text) {
        String regx = "[0-9]{17}X";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex);
    }

    /**
     * 过滤Editable空格
     *
     * @param edit
     */
    public static void inputFilterSpace(final Editable edit) {
        edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16), new InputFilter() {

            private char results[];

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.length() < 1) {
                    return null;
                } else {
                    char temp[] = (source.toString()).toCharArray();
                    results = new char[temp.length];
                    for (int i = 0, j = 0; i < temp.length; i++) {
                        if (temp[i] == ' ') {
                        } else {
                            results[j++] = temp[i];
                        }
                    }
                }
                return String.valueOf(results).trim();
            }

        }});
    }

    /**
     * 将金额格式化为0,000.00元
     *
     * @return
     */
    public static String moneyFormat(String money) {
        try {
            double up = decimalUp(Double.valueOf(money));
            DecimalFormat a = new DecimalFormat("##,###,###,##0.00元");
            return a.format(up);
        } catch (NumberFormatException e) {
            //如果出现异常,直接返回原字段
            return money;
        }
    }

    /**
     * 将金额格式化为0,000.00元
     *
     * @return
     */
    public static String moneyFormat(Double money) {
        double up = decimalUp(money);
        DecimalFormat a = new DecimalFormat("##,###,###,##0.00元");
        return a.format(up);
    }

    /**
     * 将金额格式化为0,000.00
     * 不带单位元
     *
     * @return
     */
    public static String moneyFormatNoUnit(String money) {
        try {
            double up = decimalUp(Double.valueOf(money));
            DecimalFormat a = new DecimalFormat("##,###,###,##0.00");
            return a.format(up);
        } catch (NumberFormatException e) {
            //如果出现异常,直接返回原字段
            return money;
        }
    }


    /**
     * 利息格式化
     */
    public static String rateFormat(String rate) {
        if (TextUtils.isEmpty(rate)) {
            rate = "0";
        }
        double up = decimalUp(Double.valueOf(rate));
        DecimalFormat a = new DecimalFormat("#0.00%");
        return a.format(up / 100);
    }

    /**
     * 利息格式化
     */
    public static String rateFormat(String rate, int len) {
        double up = decimalUp(Double.valueOf(rate));
        StringBuilder stringBuilder = new StringBuilder("#0.");
        for (int i = 0; i < len; i++) {
            stringBuilder.append("0");
        }
        stringBuilder.append("%");
        DecimalFormat a = new DecimalFormat(stringBuilder.toString());
        return a.format(up / 100);
    }

    /**
     * 利息格式化
     *
     * @param rate
     * @return
     */
    public static String rateFormat(Double rate) {
        try {
            double up = decimalUp(rate);
            DecimalFormat a = new DecimalFormat("#0.00%");
            return a.format(up / 100);
        } catch (Exception e) {
            return "";
        }
    }

    public static String addPercent(double rate) {
        return "";
    }

    /**
     * 利息格式化
     */
    public static String rateFormat(Double rate, int len) {
        double up = decimalUp(rate);
        StringBuilder stringBuilder = new StringBuilder("#0.");
        for (int i = 0; i < len; i++) {
            stringBuilder.append("0");
        }
        stringBuilder.append("%");
        DecimalFormat a = new DecimalFormat(stringBuilder.toString());
        return a.format(up / 100);
    }

    /**
     * 将一个字符串分成两部分，左右边大小不同，颜色不同
     *
     * @param str
     * @param mid
     * @param leftSize
     * @param leftColor
     * @param rightSize
     * @param rightColor
     * @return
     */
    public static SpannableStringBuilder getTwoTypeTxtString(String str, int mid, int leftSize, int leftColor, int rightSize, int rightColor) {
        if (TextUtils.isEmpty(str) || mid < 0 || mid > str.length()) {
            return new SpannableStringBuilder("");
        }
        SpannableStringBuilder sb = new SpannableStringBuilder(str);
        sb.setSpan(new AbsoluteSizeSpan(leftSize), 0, mid, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(leftColor), 0, mid, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new AbsoluteSizeSpan(rightSize), mid, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(rightColor), mid, str.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return sb;
    }

    /**
     * 手机号脱敏
     *
     * @param phone
     * @return
     */
    public static String sensitivePhone(String phone) {
        if (!TextUtils.isEmpty(phone) && phone.length() >= 11) {
            return hideNumber(phone, 3, 6);
        }
        return "";
    }

    /**
     * 身份证脱敏
     *
     * @param idcard
     * @return
     */
    public static String sentiveIDCard(String idcard) {
        if (!TextUtils.isEmpty(idcard) && idcard.length() >= 14) {
            return hideNumber(idcard, 5, 12);
        }
        return "";
    }

    /**
     * 银行卡号脱敏
     *
     * @param bankCard
     * @return
     */
    public static String sentiveBankCardNum(String bankCard) {
        if (!TextUtils.isEmpty(bankCard) && bankCard.length() >= 4) {
            return hideNumber(bankCard, 0, bankCard.length() - 5);
        }
        return "";
    }

    /**
     * 字符串替换成*
     *
     * @param str
     * @param start 开始位置
     * @param end   结束位置
     * @return
     */
    public static String hideNumber(String str, int start, int end) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (i >= start && i <= end) {
                sb.append("*");
            } else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    /**
     * 获取字符串最后len位
     *
     * @param len
     * @return
     */
    public static String getLastNum(String str, int len) {
        if (!TextUtils.isEmpty(str)) {
            if (str.length() <= len) {
                return str;
            } else {
                return str.substring(str.length() - len, str.length());
            }
        }
        return "";
    }

    /**
     * @param str 1234567895515562
     * @return 1234 5678 9551 5562
     */
    public static String formatBankCard(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (i < str.length() - 4) {
                if (i % 4 == 0) {
                    sb.append(" ");
                }
                sb.append("*");
                if (i == str.length() - 5) {
                    sb.append(" ");
                }
            } else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }
}
