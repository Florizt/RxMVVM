package com.rx.rxmvvmlib.util;

import android.text.TextUtils;

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
public class DateUtil {

    /**
     * Java将Unix时间戳(毫秒)转换成指定格式日期字符串
     *
     * @param timestampString 时间戳 如："1473048265";(毫秒)
     * @param formats         要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String timeStamp2Date(long timestampString, String formats) {
        try {
            if (TextUtils.isEmpty(formats)) {
                formats = "yyyy-MM-dd HH:mm:ss";
            }
            return new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestampString));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public static String getDataAlia(long date) {
        //所在时区时8，系统初始时间是1970-01-01 80:00:00，注意是从八点开始，计算的时候要加回去
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis() + offSet) / 86400000;
        long start = (date + offSet) / 86400000;
        long intervalTime = start - today;
        //-2:前天,-1：昨天,0：今天,1：明天,2：后天
        String strDes = "";
        if (intervalTime == 0) {
            strDes = "今天";
        } else if (intervalTime == 1) {
            strDes = "明天";
        } else if (intervalTime == 2) {
            strDes = "后天";
        } else {
            strDes = getWeek(date);
        }
        return strDes;
    }

    /**
     * 根据当前日期获得是星期几
     *
     * @return
     */
    public static String getWeek(long date) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(date));

        String week = "";
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                week += "周日";
                break;
            case Calendar.MONDAY:
                week += "周一";
                break;
            case Calendar.TUESDAY:
                week += "周二";
                break;
            case Calendar.WEDNESDAY:
                week += "周三";
                break;
            case Calendar.THURSDAY:
                week += "周四";
                break;
            case Calendar.FRIDAY:
                week += "周五";
                break;
            case Calendar.SATURDAY:
                week += "周六";
                break;
        }
        return week;
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZZ");
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     * 获取今天往后day天的日期（年-月-日）
     */
    public static List<String> getDateByDay(int day) {
        List<String> dates = new ArrayList<String>();

        Calendar c = Calendar.getInstance();
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

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    public static String getTimeFormatText(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "现在";
    }

    public static String getTimeShowString(long milliseconds, boolean abbreviate) {
        String dataString;
        String timeStringBy24;

        Date currentTime = new Date(milliseconds);
        Date today = new Date();
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        Date todaybegin = todayStart.getTime();
        Date yesterdaybegin = new Date(todaybegin.getTime() - 3600 * 24 * 1000);
        Date preyesterday = new Date(yesterdaybegin.getTime() - 3600 * 24 * 1000);

        SimpleDateFormat timeformatter24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        timeStringBy24 = timeformatter24.format(currentTime);

        if (!currentTime.before(todaybegin)) {
            return getTodayTimeBucket(currentTime);
        } else {
            if (!currentTime.before(yesterdaybegin)) {
                dataString = "昨天" + (abbreviate ? "" : " " + timeStringBy24);
            } else if (!currentTime.before(preyesterday)) {
                dataString = "前天" + (abbreviate ? "" : " " + timeStringBy24);
            } else if (isSameWeek(currentTime, today)) {
                dataString = getWeek(currentTime.getTime()) + (abbreviate ? "" : " " + timeStringBy24);
            } else {
                SimpleDateFormat dateformatter = new SimpleDateFormat("MM月dd日", Locale.getDefault());
                String s = dateformatter.format(currentTime);
                dataString = s + (abbreviate ? "" : " " + timeStringBy24);
            }
            return dataString;
        }
    }

    /**
     * 根据不同时间段，显示不同时间
     *
     * @return
     */
    public static String getTodayTimeBucket(Date currentTime) {
        SimpleDateFormat timeformatter24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeStringBy24 = timeformatter24.format(currentTime);
        int hour = Integer.parseInt(timeStringBy24.substring(0, 2));
        if (hour >= 0 && hour < 5) {
            return "凌晨" + " " + timeStringBy24;
        } else if (hour >= 5 && hour < 12) {
            return "上午" + " " + timeStringBy24;
        } else if (hour >= 12 && hour < 18) {
            return "下午" + " " + timeStringBy24;
        } else if (hour >= 18 && hour < 24) {
            return "晚上" + " " + timeStringBy24;
        }
        return "";
    }

    /**
     * 判断两个日期是否在同一周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeek(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    /**
     * 判断两个日期是否在同一年
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameYear(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        return 0 == subYear;
    }

    /**
     * 判断两个日期是否在同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 根据秒数获取时间串
     *
     * @param second (eg: 100s)
     * @return (eg : 00 : 01 : 40)
     */
    public static String getTimeBySecond(int second) {
        if (second <= 0) {
            return "00:00:00";
        }

        StringBuilder sb = new StringBuilder();
        int hours = second / 60 * 60;
        if (hours > 0) {

            second -= hours * 60 * 60;
        }

        int minutes = second / 60;
        if (minutes > 0) {

            second -= minutes * 60;
        }

        return (hours >= 10 ? (hours + "")
                : ("0" + hours) + ":" + (minutes >= 10 ? (minutes + "") : ("0" + minutes)) + ":"
                + (second >= 10 ? (second + "") : ("0" + second)));
    }
}