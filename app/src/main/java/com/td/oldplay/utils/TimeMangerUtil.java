package com.td.oldplay.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 时间工具类
 *
 * @author jwh
 */
@SuppressLint("SimpleDateFormat")
public class TimeMangerUtil {

    /**
     * 获得当前的月份
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }


    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        return format.format(new Date(time));
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String getNowDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前日期
        String date = formatter.format(curDate);
        return date;
    }

	/*public static String getTimeByType(String time,String ) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前日期
		String date = formatter.format(curDate);
		return date;
	}*/

    public static String getNowTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String date = formatter.format(curDate);
        return date;
    }

    public static String getSystemTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String date = formatter.format(curDate);
        return date;
    }

    /**
     * 时间计算——开始时间+时长
     *
     * @param startDate
     * @param time
     * @return
     */
    public static String CalculateDate(String startDate, long time) {
        String resultDate = "";
        SimpleDateFormat informat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        try {
            Date start = informat.parse(startDate);
            long endtime = start.getTime() + time;
            resultDate = getHourAndMin(endtime);
        } catch (Exception e) {
        }
        return resultDate;
    }

    public static String getDayOFWeek() {
        String mWay = "";
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay;
    }

    // 获得n周 —— 周一 的日期
    public static String getMonday(int n) {
        String monday = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        monday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return monday;
    }

    // 获得n周 —— 周二 的日期
    public static String getTuesday(int n) {
        String tuesday = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        tuesday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return tuesday;
    }

    // 获得n周 —— 周三的日期
    public static String getWednesday(int n) {
        String Wednesday = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        Wednesday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return Wednesday;
    }

    // 获得n周 —— 周四 的日期
    public static String getThursday(int n) {
        String Thursday = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        Thursday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return Thursday;
    }

    // 获得n周 —— 周五 的日期
    public static String getFriday(int n) {
        String Friday = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        Friday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return Friday;
    }

    // 获得n周 —— 周六的日期
    public static String getSaturday(int n) {
        String Saturday = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Saturday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return Saturday;
    }

    // 获得n周 —— 周日 的日期
    public static String getSunday(int n) {
        String Sunday = "";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n * 7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Sunday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return Sunday;
    }

    public static String getChatTime(long timesamp) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(sdf.format(today)) - Integer.parseInt(sdf.format(otherDay));
        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(timesamp);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(timesamp);
                break;
            case 2:
                result = "前天 " + getHourAndMin(timesamp);
                break;
            default:
                // result = temp + "天前 ";
                result = getTime(timesamp);
                break;
        }
        return result;
    }

    // 获得当前日期与本周一相差的天数
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    // 获得当前周- 周一的日期
    public static String getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = formatter.format(monday);
        return preMonday;
    }

    // 获得当前周- 周日 的日期
    public static String getPreviousSunday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String preMonday = formatter.format(monday);
        return preMonday;
    }

    public static String getWeekList(int week, int number) {
        String dateinfo = "";
        switch (number) {
            case 1:
                dateinfo = getMonday(week);
                break;
            case 2:
                dateinfo = getTuesday(week);
                break;
            case 3:
                dateinfo = getWednesday(week);
                break;
            case 4:
                dateinfo = getThursday(week);
                break;
            case 5:
                dateinfo = getFriday(week);
                break;
            case 6:
                dateinfo = getSaturday(week);
                break;
            case 7:
                dateinfo = getSunday(week + 1);
                break;
            default:
                break;
        }
        return dateinfo;
    }

    /**
     * 聊天过期的时间显示
     */
    public static String friendsCirclePastDate(String strDate) {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间
        String nowtime = d.format(new Date());// 按以上格式 将当前时间转换成字符串
        try {
            long result = (d.parse(nowtime).getTime() - d.parse(strDate).getTime()) / 1000;
            if (result > 0 && result < 60) {
                return "刚刚";
            } else if (result >= 60 && result < 3600) {
                long p = result / 60;
                return p + "分钟前";
            } else if (result >= 3600 && result < 3600 * 24) {
                long p = result / 3600;
                return p + "小时前";
            } else if (result >= 3600 * 24 && result < (3600 * 24 * 2)) {
                return "昨天";
            } else if (result >= 3600 * 24 * 2 && result < (3600 * 24 * 10)) {
                long between_days = result / (3600 * 24);
                return Integer.parseInt(String.valueOf(between_days)) + "天前";
            } else {
                SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
                return dd.format(dd.parse(strDate));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 我的朋友圈
     *
     * @param strDate
     * @return
     */
    public static String myCirclePastDate(String strDate) {
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间
        String nowtime = d.format(new Date());// 按以上格式 将当前时间转换成字符串
        try {
            long result = (d.parse(nowtime).getTime() - d.parse(strDate).getTime()) / 1000;
            if (result > 0 && result < 60 * 5) {
                return "刚刚";
            } else if (result >= 3600 * 24 && result < (3600 * 24 * 2)) {
                return "昨天";
            } else {
                return getDayFromDate(strDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 获取指定日期转换成星期几
    public static String timeToWeekDay(String time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                format);
        Date date = null;
        try {
            date = dateFormat.parse(time);
            System.out.println(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int mydate = cd.get(Calendar.DAY_OF_WEEK);
        String showDate = "";
        switch (mydate) {
            case 1:
                showDate = "星期日";
                break;
            case 2:
                showDate = "星期一";
                break;
            case 3:
                showDate = "星期二";
                break;
            case 4:
                showDate = "星期三";
                break;
            case 5:
                showDate = "星期四";
                break;
            case 6:
                showDate = "星期五";
                break;
            default:
                showDate = "星期六";
                break;
        }
        return showDate;
    }

    // 获取指定日期转换成星期几
    public static String timeToWeekDay(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(time);
            System.out.println(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int mydate = cd.get(Calendar.DAY_OF_WEEK);
        String showDate = "";
        switch (mydate) {
            case 1:
                showDate = "星期日";
                break;
            case 2:
                showDate = "星期一";
                break;
            case 3:
                showDate = "星期二";
                break;
            case 4:
                showDate = "星期三";
                break;
            case 5:
                showDate = "星期四";
                break;
            case 6:
                showDate = "星期五";
                break;
            default:
                showDate = "星期六";
                break;
        }
        return showDate;
    }

    // 获取指定日期转换成星期几
    public static String timeToWeekDayEn(String time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                format);
        Date date = null;
        try {
            date = dateFormat.parse(time);
            System.out.println(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int mydate = cd.get(Calendar.DAY_OF_WEEK);
        String showDate = "";
        switch (mydate) {
            case 1:
                showDate = "Sunday";
                break;
            case 2:
                showDate = "Monday";
                break;
            case 3:
                showDate = "Tuesday";
                break;
            case 4:
                showDate = "Wednesday";
                break;
            case 5:
                showDate = "Thursday";
                break;
            case 6:
                showDate = "Friday";
                break;
            default:
                showDate = "Saturday";
                break;
        }
        return showDate;
    }

    // 获取指定日期转换成星期几
    public static String timeToWeekDayEn(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(time);
            System.out.println(date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        int mydate = cd.get(Calendar.DAY_OF_WEEK);
        String showDate = "";
        switch (mydate) {
            case 1:
                showDate = "Sunday";
                break;
            case 2:
                showDate = "Monday";
                break;
            case 3:
                showDate = "Tuesday";
                break;
            case 4:
                showDate = "Wednesday";
                break;
            case 5:
                showDate = "Thursday";
                break;
            case 6:
                showDate = "Friday";
                break;
            default:
                showDate = "Saturday";
                break;
        }
        return showDate;
    }

    // hh:mm:ss时间转成上午或者下午、晚上
    @SuppressWarnings("deprecation")
    public static String am_pm(String yueke_time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(yueke_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int hour = date.getHours();
        if (hour >= 0 && hour < 11) {
            return "上午";
        }
        if (hour >= 11 && hour < 13) {
            return "中午";
        }
        if (hour >= 13 && hour < 18) {
            return "下午";
        }
        if (hour >= 18 && hour <= 23) {
            return "晚上";
        }
        return "";
    }

    // 取出时间中的day
    public static String getDayFromDate(String mdate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(mdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        int d = calender.get(Calendar.DAY_OF_MONTH);
        if (d < 10) {
            return "0" + d;
        }
        return d + "";
    }

    // 取出时间中的month
    public static String getMonthFromDate(String mdate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(mdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        int d = calender.get(Calendar.MONTH) + 1;
        if (d < 10) {
            return "0" + d;
        }
        return d + "";
    }

    /**
     * 取出日期中的月份（int）
     *
     * @param mdate
     * @return
     */
    public static int getYearNumberFromDate(String mdate) {
        int yearNumber = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(mdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        yearNumber = calender.get(Calendar.YEAR);
        return yearNumber;
    }


    /**
     * 取出日期中的月份（int）
     *
     * @param mdate
     * @return
     */
    public static int getMonNumberFromDate(String mdate) {
        int monthNumber = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(mdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        monthNumber = calender.get(Calendar.MONTH) + 1;
        return monthNumber;
    }


    /**
     * 取出日期中的日期（int）
     *
     * @param mdate
     * @return
     */
    public static int getDayNumberFromDate(String mdate) {
        int dayNumber = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(mdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        dayNumber = calender.get(Calendar.DAY_OF_MONTH);
        return dayNumber;
    }


    /**
     * 首页时间tip显示
     *
     * @param inTime
     * @return
     */
    public static String setHomeAdapterTip(String inTime) {
        String resultMsg = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间
        String nowtime = dateFormat.format(new Date());// 按以上格式 将当前时间转换成字符串
        int mYear = getYearNumberFromDate(inTime) - getYearNumberFromDate(nowtime);
        int mMonth = getMonNumberFromDate(inTime) - getMonNumberFromDate(nowtime);
        int mDay = getDayNumberFromDate(inTime) - getDayNumberFromDate(nowtime);
        if (mYear == 0) {
            if (mMonth == 0) {
                if (mDay == 0) {
                    resultMsg = "今天";
                } else if (mDay == 1) {
                    resultMsg = "明天";
                } else {
                    resultMsg = getMonthFromDate(inTime) + "月" + getDayFromDate(inTime) + "  " + timeToWeekDay(inTime);
                }
            } else {
                resultMsg = getMonthFromDate(inTime) + "月" + getDayFromDate(inTime) + "  " + timeToWeekDay(inTime);
            }
        } else {
            resultMsg = getMonthFromDate(inTime) + "月" + getDayFromDate(inTime) + "  " + timeToWeekDay(inTime);
        }
        return resultMsg;
    }

    /**
     * 课程详情页倒计时
     *
     * @param inTime
     * @return
     */
    public static String setClassDetailTimeCount(String inTime) {
        String resultMsg = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间
        String nowtime = dateFormat.format(new Date());// 按以上格式 将当前时间转换成字符串
        int mYear = getYearNumberFromDate(inTime) - getYearNumberFromDate(nowtime);
        int mMonth = getMonNumberFromDate(inTime) - getMonNumberFromDate(nowtime);
        int mDay = getDayNumberFromDate(inTime) - getDayNumberFromDate(nowtime);
        if (mYear == 0) {
            if (mMonth == 0) {
                if (mDay == 0) {
                    try {
                        long result = (dateFormat.parse(inTime).getTime() - dateFormat.parse(nowtime).getTime()) / (1000 * 60);
                        if (result > 0 && result < 30) {
                            resultMsg = result + "分钟后开始";
                        } else if (result < 0) {
                            resultMsg = "START";
                        } else {
                            resultMsg = "今天  " + inTime.substring(11, 16);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (mDay > 1 && mDay < 4) {
                    resultMsg = mDay + "天后开始";
                } else {
                    resultMsg = "OVER";
                }
            } else {
                resultMsg = "OVER";
            }
        } else {
            resultMsg = "OVER";
        }
        return resultMsg;
    }

    public static String getEnimeView(String inTime) {
        if (TextUtils.isEmpty(inTime)) {
            return "";
        } else {
            String resultMsg = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = dateFormat.parse(inTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            int month = calender.get(Calendar.MONTH) + 1;
            int day = calender.get(Calendar.DAY_OF_MONTH);
            resultMsg = calender.get(Calendar.YEAR) + "-";
            if (month < 10) {
                resultMsg += "0" + month + "-";
            } else {
                resultMsg += month + "-";
            }
            if (day < 10) {
                resultMsg = resultMsg + "0" + day;
            } else {
                resultMsg = resultMsg + day;
            }
            //resultMsg = resultMsg + inTime.substring(11, 16);
            return resultMsg;
        }
    }

    public static String getEnimeView2(String inTime) {
        if (TextUtils.isEmpty(inTime)) {
            return "";
        } else {
            String resultMsg = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = null;
            try {
                date = dateFormat.parse(inTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            int month = calender.get(Calendar.MONTH) + 1;
            int day = calender.get(Calendar.DAY_OF_MONTH);
            //resultMsg=calender.get(Calendar.YEAR)+"-";
            if (month < 10) {
                resultMsg += "0" + month + "-";
            } else {
                resultMsg += month + "-";
            }
            if (day < 10) {
                resultMsg = resultMsg + "0" + day;
            } else {
                resultMsg = resultMsg + day;
            }
            //resultMsg = resultMsg + inTime.substring(11, 16);
            return resultMsg;
        }
    }

    public static String getChinesetimeView(String inTime) {
        if (TextUtils.isEmpty(inTime)) {
            return "";
        } else {
            String resultMsg = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = dateFormat.parse(inTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            int month = calender.get(Calendar.MONTH) + 1;
            int day = calender.get(Calendar.DAY_OF_MONTH);
            //resultMsg=calender.get(Calendar.YEAR)+"年";
            if (month < 10) {
                resultMsg += "0" + month + "月";
            } else {
                resultMsg += month + "月";
            }
            if (day < 10) {
                resultMsg = resultMsg + "0" + day + "日 ";
            } else {
                resultMsg = resultMsg + day + "日 ";
            }
            //resultMsg = resultMsg + inTime.substring(11, 16);
            Log.e("===", resultMsg);
            return resultMsg;
        }
    }

    public static String getChinesetimeView2(String inTime) {
        if (TextUtils.isEmpty(inTime)) {
            return "";
        } else {
            String resultMsg = "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = null;
            try {
                date = dateFormat.parse(inTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calender = Calendar.getInstance();
            calender.setTime(date);
            int month = calender.get(Calendar.MONTH) + 1;
            int day = calender.get(Calendar.DAY_OF_MONTH);
            resultMsg = calender.get(Calendar.YEAR) + "年";
            if (month < 10) {
                resultMsg += "0" + month + "月";
            } else {
                resultMsg += month + "月";
            }
            if (day < 10) {
                resultMsg = resultMsg + "0" + day + "日 ";
            } else {
                resultMsg = resultMsg + day + "日 ";
            }
            //resultMsg = resultMsg + inTime.substring(11, 16);
            return resultMsg;
        }
    }

    /**
     * 活动持续时间计算,并显示
     *
     * @param start_time
     * @param end_time
     * @return
     */
    public static String getClubActDate(String start_time, String end_time) {
        String resultMsg = "";
        int mYear = getYearNumberFromDate(end_time) - getYearNumberFromDate(start_time);
        int mMonth = getMonNumberFromDate(end_time) - getMonNumberFromDate(start_time);
        int mDay = getDayNumberFromDate(end_time) - getDayNumberFromDate(start_time);
        if (mYear == 0) {
            if (mMonth == 0) {
                resultMsg = "共" + mDay + "天";
            } else {
                resultMsg = "共" + mMonth + "月" + mDay + "天";
            }
        } else if (mYear > 0) {
            resultMsg = "一年以上";
        }
        return resultMsg;
    }


    /**
     * 活动持续时间计算,并显示
     *
     * @param start_time
     * @param end_time
     * @return
     */
    public static int getClubActDateNoHMS(String start_time, String end_time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");//输入日期的格式
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = simpleDateFormat.parse(start_time);
            date2 = simpleDateFormat.parse(end_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int dayCount = (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24));//从间隔毫秒变成间隔天数
        return dayCount;

    }

    public static String getDateFromTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        } else if (time.length() > 11) {
            return time.substring(0, 11);
        } else {
            return time;
        }
    }

    /**
     * 时间计算front:08:16 +72(分钟数)=after:09:28
     * <p>
     * 开始上课时间
     * 上课时长
     *
     * @return
     */
    public static String addDateMinut(String start_time, String addMinute) {
        int minute = Integer.parseInt(addMinute);
        // 引号里面个格式也可以是 HH:mm:ss或者HH:mm等等，不过在主函数调用时，要和输入的变
        // 量time格式一致
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");// 24小时制
        Date date = null;
        try {
            date = format.parse(start_time);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);// 24小时制
        date = cal.getTime();
        cal = null;
        return format.format(date);
    }

    public static int compare_date(String DATE1, String DATE2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            }
            if (dt1.getTime() < dt2.getTime()) {
                return -1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    @SuppressWarnings("deprecation")
    public static int compare_time(String time1, String time2) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        try {
            Date dt1 = df.parse(time1);
            Date dt2 = df.parse(time2);
            long t1 = dt1.getHours() * 3600 + dt1.getMinutes() * 60;
            long t2 = dt2.getHours() * 3600 + dt2.getMinutes() * 60;
            if (t1 > t2) {
                return 1;
            }
            if (t1 < t2) {
                return -1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static long compare_days(String str1, String str2) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = dateFormat.parse(str1);
            Date date2 = dateFormat.parse(str2);
            return (date2.getTime() - date1.getTime()) / (24 * 3600 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String formatDate2(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);
    }

    public static Date getCalendarByString(String s) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            date = null;
        }
        return date;
    }

    /**
     * qTuktuk 获取日期区域
     *
     * @param startDate
     * @param endDate
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getStringDate(String startDate, String endDate, String startTime, String endTime) {
        String result = "";

        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

        Date startD = null;
        Date endD = null;

        String start, end;
        try {
            startD = format1.parse(startDate);
            endD = format1.parse(endDate);

            start = format2.format(startD);
            end = format2.format(endD);

            result = start + ":" + startTime + "#" + end + ":" + endTime;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}