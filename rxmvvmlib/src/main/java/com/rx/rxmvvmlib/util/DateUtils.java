package com.rx.rxmvvmlib.util;

import android.text.TextUtils;

import com.rx.rxmvvmlib.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by wuwei
 * 2018/6/15
 * 佛祖保佑       永无BUG
 */
public class DateUtils {

    private static String mYear; // 当前年
    private static String mMonth; // 月
    private static String mDay;
    private static String mWay;

    public static final String normal_formats = "yyyy-MM-dd HH:mm:ss";
    public static final String chat_msg_top_bar_formats = "MM-dd HH:mm";

    /**
     * Java将Unix时间戳(毫秒)转换成指定格式日期字符串
     *
     * @param timestampString 时间戳 如："1473048265";(毫秒)
     * @param formats         要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String timeStamp2Date(long timestampString, String formats) {
        if (TextUtils.isEmpty(formats))
            formats = "yyyy-MM-dd HH:mm:ss";
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestampString));
        return date;
    }

    /**
     * 日期格式字符串转换成时间戳(毫秒)
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(dateStr).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getAlia(long date) {
        //所在时区时8，系统初始时间是1970-01-01 80:00:00，注意是从八点开始，计算的时候要加回去
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis() + offSet) / 86400000;
        long start = (date + offSet) / 86400000;
        long intervalTime = start - today;
        //-2:前天,-1：昨天,0：今天,1：明天,2：后天
        String strDes = "";
        if (intervalTime == 0) {
            strDes = UIUtils.getString(R.string.today);
        } else if (intervalTime == 1) {
            strDes = UIUtils.getString(R.string.tomorrow);
        } else if (intervalTime == 2) {
            strDes = UIUtils.getString(R.string.the_day_after_tomorrow);
        } else {
            strDes = getWeek(timeStamp2Date(date, "yyyy-MM-dd"));
        }
        return strDes;
    }

    /**
     * 获取当前日期几月几号
     */
    public static String getDateString() {

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return mMonth + (LanguageUtil.isZh(UIUtils.getContext()) ? UIUtils.getString(R.string.month) : "/")
                + mDay + (LanguageUtil.isZh(UIUtils.getContext()) ? UIUtils.getString(R.string.day) : "");
    }

    /**
     * 获取当前年月日
     *
     * @return
     */
    public static String StringData() {

        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
        mMonth = (c.get(Calendar.MONTH) + 1) < 10 ? "0" + (c.get(Calendar.MONTH) + 1) : String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = c.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + c.get(Calendar.DAY_OF_MONTH) : String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return mYear + "-" + mMonth + "-" + mDay;
    }

    public static String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZZ");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static String getYear() {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.YEAR));
    }

    /**
     * 获取当前是周几
     */
    public static String getWeekString() {
        final Calendar c = Calendar.getInstance();
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = UIUtils.getString(R.string.sunday);
        } else if ("2".equals(mWay)) {
            mWay = UIUtils.getString(R.string.monday);
        } else if ("3".equals(mWay)) {
            mWay = UIUtils.getString(R.string.tuesday);
        } else if ("4".equals(mWay)) {
            mWay = UIUtils.getString(R.string.wednesday);
        } else if ("5".equals(mWay)) {
            mWay = UIUtils.getString(R.string.thursday);
        } else if ("6".equals(mWay)) {
            mWay = UIUtils.getString(R.string.friday);
        } else if ("7".equals(mWay)) {
            mWay = UIUtils.getString(R.string.saturday);
        }
        return mDay;
    }

    /**
     * 根据当前日期获得是星期几
     *
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(time));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += UIUtils.getString(R.string.sunday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += UIUtils.getString(R.string.monday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += UIUtils.getString(R.string.tuesday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += UIUtils.getString(R.string.wednesday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += UIUtils.getString(R.string.thursday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += UIUtils.getString(R.string.friday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += UIUtils.getString(R.string.saturday);
        }
        return Week;
    }

    /**
     * 获取今天往后一周的日期（年-月-日）
     */
    public static List<String> getDate(int day) {
        List<String> dates = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = sim.format(c.getTime());
        dates.add(date);
        for (int i = 0; i < day - 1; i++) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            date = sim.format(c.getTime());
            dates.add(date);
        }
        return dates;
    }

    /**
     * 获取今天往后一周的日期（几月几号）
     */
    public static List<String> getSevendate() {
        List<String> dates = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        for (int i = 0; i < 7; i++) {
            mYear = String.valueOf(c.get(Calendar.YEAR));// 获取当前年份
            mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
            mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + i);// 获取当前日份的日期号码
            String date = mMonth + (LanguageUtil.isZh(UIUtils.getContext()) ? UIUtils.getString(R.string.month) : "/") +
                    mDay + (LanguageUtil.isZh(UIUtils.getContext()) ? UIUtils.getString(R.string.day) : "");
            dates.add(date);
        }
        return dates;
    }

    public static String getHour() {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.HOUR_OF_DAY));
    }

    public static String getMinute() {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.MINUTE));
    }


    // 根据年月日计算年龄,birthTimeString:"1994-11-14"
    public static int getAgeFromBirthTime(Date date) {
        // 得到当前时间的年、月、日
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH) + 1;
            int dayNow = cal.get(Calendar.DATE);
            //得到输入时间的年，月，日
            cal.setTime(date);
            int selectYear = cal.get(Calendar.YEAR);
            int selectMonth = cal.get(Calendar.MONTH) + 1;
            int selectDay = cal.get(Calendar.DATE);
            // 用当前年月日减去生日年月日
            int yearMinus = yearNow - selectYear;
            int monthMinus = monthNow - selectMonth;
            int dayMinus = dayNow - selectDay;
            int age = yearMinus;// 先大致赋值
            if (yearMinus <= 0) {
                age = 0;
            }
            if (monthMinus < 0) {
                age = age - 1;
            } else if (monthMinus == 0) {
                if (dayMinus < 0) {
                    age = age - 1;
                }
            }
            return age;
        }
        return 0;
    }

    public static Date parse(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDate(String str) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            Date date = formatter.parse(str);
            return date;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获取某年某月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0); //输入类型为int类型
        return c.get(Calendar.DAY_OF_MONTH);
    }
}