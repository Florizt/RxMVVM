package com.rx.rxmvvmlib.util;


import com.rx.rxmvvmlib.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by wuwei
 * 2018/3/16
 * 佛祖保佑       永无BUG
 */
public class TimeUtil {
    /**
     * 一小时的秒数
     */
    private static final int HOUR_SECOND = 60 * 60;

    /**
     * 一分钟的秒数
     */
    private static final int MINUTE_SECOND = 60;

    /**
     * 根据秒数获取时间串
     *
     * @param second (eg: 100s)
     * @return (eg : 00 : 01 : 40)
     */
    public static String getTimeStrBySecond(int second) {
        if (second <= 0) {

            return "00:00:00";
        }

        StringBuilder sb = new StringBuilder();
        int hours = second / HOUR_SECOND;
        if (hours > 0) {

            second -= hours * HOUR_SECOND;
        }

        int minutes = second / MINUTE_SECOND;
        if (minutes > 0) {

            second -= minutes * MINUTE_SECOND;
        }

        return (hours >= 10 ? (hours + "")
                : ("0" + hours) + ":" + (minutes >= 10 ? (minutes + "") : ("0" + minutes)) + ":"
                + (second >= 10 ? (second + "") : ("0" + second)));
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
    public static String getTimeFormatText(Date date, TypeListener typeListener) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            if (typeListener != null) {
                typeListener.type(5);
            }
            return r + UIUtils.getString(R.string.year_ago);
        }
        if (diff > month) {
            r = (diff / month);
            if (typeListener != null) {
                typeListener.type(4);
            }
            return r + UIUtils.getString(R.string.month_ago);
        }
        if (diff > day) {
            r = (diff / day);
            if (typeListener != null) {
                typeListener.type(3);
            }
            return r + UIUtils.getString(R.string.day_ago);
        }
        if (diff > hour) {
            r = (diff / hour);
            if (typeListener != null) {
                typeListener.type(2);
            }
            return r + UIUtils.getString(R.string.hour_ago);
        }
        if (diff > minute) {
            r = (diff / minute);
            if (typeListener != null) {
                typeListener.type(1);
            }
            return r + UIUtils.getString(R.string.minute_ago);
        }
        if (typeListener != null) {
            typeListener.type(0);
        }
        return UIUtils.getString(R.string.now);
    }

    public interface TypeListener {
        void type(int type);
    }

    /**
     * @param milliseconds
     * @param abbreviate
     * @return
     */
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
                dataString = UIUtils.getString(R.string.yesterday) + (abbreviate ? "" : " " + timeStringBy24);
            } else if (!currentTime.before(preyesterday)) {
                dataString = UIUtils.getString(R.string.the_day_before_yesterday) + (abbreviate ? "" : " " + timeStringBy24);
            } else if (isSameWeekDates(currentTime, today)) {
                dataString = getWeekOfDate(currentTime) + (abbreviate ? "" : " " + timeStringBy24);
            } else {
                SimpleDateFormat dateformatter = new SimpleDateFormat("MM月dd日", Locale.getDefault());
                String s = dateformatter.format(currentTime);
                if (LanguageUtil.isZh(UIUtils.getContext())) {
                    dataString = s + (abbreviate ? "" : " " + timeStringBy24);
                } else {
                    dataString = s.substring(0, 2) + "/" + s.substring(3, 5) + (abbreviate ? "" : " " + timeStringBy24);
                }
            }
            return dataString;
        }
    }

    public static String getTrackDate(long milliseconds) {
        String dataString;

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

        if (!currentTime.before(todaybegin)) {
            return UIUtils.getString(R.string.today);
        } else {
            if (!currentTime.before(yesterdaybegin)) {
                dataString = UIUtils.getString(R.string.yesterday);
            } else if (!currentTime.before(preyesterday)) {
                dataString = UIUtils.getString(R.string.the_day_before_yesterday);
            } else if (isSameYear(currentTime, today)) {
                SimpleDateFormat dateformatter = new SimpleDateFormat("MM.dd", Locale.getDefault());
                dataString = dateformatter.format(currentTime);
            } else {
                SimpleDateFormat dateformatter = new SimpleDateFormat("MM.dd\nyyyy", Locale.getDefault());
                dataString = dateformatter.format(currentTime);
            }
            return dataString;
        }
    }

    public static String getTrackTime(long milliseconds, boolean space) {
        Date currentTime = new Date(milliseconds);
        SimpleDateFormat timeformatter24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeStringBy24 = timeformatter24.format(currentTime);
        int hour = Integer.parseInt(timeStringBy24.substring(0, 2));
        return timeStringBy24 + (space ? " " : "") + (hour < 12 ? "am" : "pm");
    }

    public static String getTrackHour(long startMilliseconds, long endMilliseconds) {
        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        Date startCurrentTime = new Date(startMilliseconds);
        String startTime = simpleDateFormat.format(startCurrentTime);
        int startYear = Integer.parseInt(startTime.substring(0, 4));
        int startMonth = Integer.parseInt(startTime.substring(5, 7));
        int startDay = Integer.parseInt(startTime.substring(8, 10));
        int startHour = Integer.parseInt(startTime.substring(11, 13));
        int startMinute = Integer.parseInt(startTime.substring(14, 16));

        Date endCurrentTime = new Date(endMilliseconds);
        String endTime = simpleDateFormat.format(endCurrentTime);
        int endYear = Integer.parseInt(endTime.substring(0, 4));
        int endMonth = Integer.parseInt(endTime.substring(5, 7));
        int endDay = Integer.parseInt(endTime.substring(8, 10));
        int endHour = Integer.parseInt(endTime.substring(11, 13));
        int endMinute = Integer.parseInt(endTime.substring(14, 16));*/

        if (endMilliseconds - startMilliseconds >= 24 * 3600 * 1000) {
            return (((endMilliseconds - startMilliseconds) / (24 * 3600 * 1000))) + UIUtils.getString(R.string.track_day);
        } else if (endMilliseconds - startMilliseconds >= 3600 * 1000) {
            return (((endMilliseconds - startMilliseconds) / (3600 * 1000))) + UIUtils.getString(R.string.hour);
        } else {
            return (((endMilliseconds - startMilliseconds) / (60 * 1000))) + UIUtils.getString(R.string.minute);
        }
    }

    public static long getSecondsByMilliseconds(long milliseconds) {
        long seconds = new BigDecimal((float) ((float) milliseconds / (float) 1000)).setScale(0,
                BigDecimal.ROUND_HALF_UP).intValue();
        return seconds;
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
            return LanguageUtil.isZh(UIUtils.getContext()) ? UIUtils.getString(R.string.before_dawn) + " " + timeStringBy24 : timeStringBy24 + " " + "AM";
        } else if (hour >= 5 && hour < 12) {
            return LanguageUtil.isZh(UIUtils.getContext()) ? UIUtils.getString(R.string.morning) + " " + timeStringBy24 : timeStringBy24 + " " + "AM";
        } else if (hour >= 12 && hour < 18) {
            return LanguageUtil.isZh(UIUtils.getContext()) ? UIUtils.getString(R.string.afternoon) + " " + timeStringBy24 : timeStringBy24 + " " + "PM";
        } else if (hour >= 18 && hour < 24) {
            return LanguageUtil.isZh(UIUtils.getContext()) ? UIUtils.getString(R.string.night) + " " + timeStringBy24 : timeStringBy24 + " " + "PM";
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
    public static boolean isSameWeekDates(Date date1, Date date2) {
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
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {
                UIUtils.getString(R.string.sunday),
                UIUtils.getString(R.string.monday),
                UIUtils.getString(R.string.tuesday),
                UIUtils.getString(R.string.wednesday),
                UIUtils.getString(R.string.thursday),
                UIUtils.getString(R.string.friday),
                UIUtils.getString(R.string.saturday)};
        // String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }
}
