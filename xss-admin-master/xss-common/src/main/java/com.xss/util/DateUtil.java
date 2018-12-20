package com.xss.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil
{

    private  static final String DEFALUT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private DateUtil()
	{}
	
	/**
	 * date日期所在的几号
	 * @param date
	 * @return
	 */
	public static int getDay_Of_Year(Date date)
	{
	    GregorianCalendar gC = new GregorianCalendar();
	    gC.setTime(date);
	    return gC.get(GregorianCalendar.DAY_OF_YEAR);
	}
	/**
	 * date日期所在的几号
	 * @param date
	 * @return
	 */
    public static int getDay_Of_Month(Date date)
    {
        GregorianCalendar gC = new GregorianCalendar();
        gC.setTime(date);
        return gC.get(GregorianCalendar.DAY_OF_MONTH);
    }
    
    /**
     * 获得date日期在这周是星期几
     * @param date
     * @return
     */
    public static int getDay_Of_Week(Date date){
    	 GregorianCalendar gC = new GregorianCalendar();
         gC.setTime(date);
         return gC.get(GregorianCalendar.DAY_OF_WEEK)-1;
    }
        
	/**
	 * get what hour is now
	 * @return
	 */
	public static int getCurrHour()
	{
		GregorianCalendar gC = new GregorianCalendar();
		return gC.get(GregorianCalendar.HOUR_OF_DAY);		
	}
  
    /**
     * 获得2个日期相差的天数��
     * @param start ��ʽҪ��yyyy-MM-dd
     * @param end ��ʽҪ��yyyy-MM-dd
     * @return
     */
    public static int getIntervalDays(Date start, Date end)
    {
    	start = DateUtil.parse(DateUtil.format(start, "yyyy-MM-dd"), "yyyy-MM-dd");
    	end = DateUtil.parse(DateUtil.format(end, "yyyy-MM-dd"), "yyyy-MM-dd");
        long mills_per_day = 24*3600*1000;
        long startMills = start.getTime();
        long endMills = end.getTime();
        int offset = 0;
        
        offset = (int)((endMills-startMills)/mills_per_day);

        return offset;
    }
    
    public static int getIntervalminutes(Date start, Date end)
    {
    	start = DateUtil.parse(DateUtil.format(start, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
    	end = DateUtil.parse(DateUtil.format(end, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm:ss");
        long mills_per_day = 1000*60;
        long startMills = start.getTime();
        long endMills = end.getTime();
        int offset = 0;
        
        offset = (int)((endMills-startMills)/mills_per_day);

        return offset;
    }
    
    /**
     * 获得2个日期相差的月份
     * @return
     */
    public static int getIntervalMonths(Date start, Date end)
    {
        GregorianCalendar startGC = new GregorianCalendar();
        GregorianCalendar endGC = new GregorianCalendar();
        startGC.setTime(start);
        endGC.setTime(end);
        
        int endY = endGC.get(GregorianCalendar.YEAR);
        int endM = endGC.get(GregorianCalendar.MONTH);
        //int endD = endGC.get(GregorianCalendar.DAY_OF_MONTH);

        int startY = startGC.get(GregorianCalendar.YEAR);
        int startM = startGC.get(GregorianCalendar.MONTH);
        //int startD = startGC.get(GregorianCalendar.DAY_OF_MONTH);
        
        int offset = -1;
        
        //if(endD==startD)
        //{
        	offset = endM-startM+(endY-startY)*12;
        //}
        
        return offset;
    }
    
    /**
     * 是否同天
     * @return
     */
    public static boolean isSameMonth(Date start, Date end)
    {
    	String start1 = DateUtil.format(start, "yyyy-MM");
    	String end1 = DateUtil.format(end, "yyyy-MM");
    	if(start1.equals(end1))
    		return true;
    	return false;
    }
    
    /**
     * ��ȡ��ǰ����
     * @return Date
     */
    public static Date getNow()
    {
        return new Date();
    }
    
    /**
     * 获得昨天的日期��ȡ���������
     * @return
     */
    public static Date getYesterday()
    {
        return addDays(-1);
    }
    
    /**
     * 在现有的日期增加天数��ȡ��ǰʱ�䣫增加天数
     * @param days int
     * @return Date
     */
    public static Date addDays(int days)
    {
        return addDays(new Date(), days);
    }
    public static Date addDays(int days, String pattern)
    {
        return addDays(new Date(), days, pattern);
    }
    /**
     * ��ԭ给传过来的日期增加天数
     * @param srcDate Date
     * @param days int
     * @return Date
     */
    public static Date addDays(Date srcDate, int days)
    {
        GregorianCalendar gCanlendar = new GregorianCalendar();
        gCanlendar.setTime(srcDate);
        gCanlendar.add(gCanlendar.DAY_OF_MONTH,days);
        return gCanlendar.getTime();
    }
    public static Date addDays(Date srcDate, int days, String pattern)
    {
    	GregorianCalendar gCanlendar = new GregorianCalendar();
    	gCanlendar.setTime(srcDate);
    	gCanlendar.add(gCanlendar.DAY_OF_MONTH,days);
    	Date date = gCanlendar.getTime();
    	String dateStr = format(date, pattern);
    	Date parseDate = parse(dateStr, pattern);
    	return parseDate;
    }

    /**
     * 给日期增加月份
     * @param srcDate
     * @param months
     * @return
     */
    public static Date addMonths(Date srcDate, int months)
    {
        GregorianCalendar gCanlendar = new GregorianCalendar();
        gCanlendar.setTime(srcDate);
        gCanlendar.add(gCanlendar.MONTH, months);
        return gCanlendar.getTime();        
    }
    
    /**
     * ��ԭ给日期增加年份
     * @param srcDate Date
     * @param year int
     * @return Date
     */
    public static Date addYears(Date srcDate, int year)
    {
        GregorianCalendar gCanlendar = new GregorianCalendar();
        gCanlendar.setTime(srcDate);
        gCanlendar.add(gCanlendar.YEAR,year);
        return gCanlendar.getTime();
    }

    /**
     * �ѵ�ǰʱ����dest�Ƚϣ���ȷ����
     * @param dest Date
     * @return int 0��ʾ��ȣ���1��ʾ��ǰʱ��ǰ��dest,1��ʾ��ǰʱ������dest
     */
    public static int compareTo(Date dest)
    {
        return compareTo(new Date(), dest);
    }

    /**
     * ��src��dest�Ƚ比较2个日期(字符串比较，有问题，直接用src.getTime()-dest.getTime()比较)
     */
    public static int compareTo(Date src, Date dest)
    {
        Date src1 = parse(format(src, "yyyy-MM-dd"),"yyyy-MM-dd");
        Date src2 = parse(format(dest,"yyyy-MM-dd"),"yyyy-MM-dd");
        int i = src1.compareTo(src2);
        return i;
    }

    public static String format(Date d){
        return format(d, DEFALUT_FORMAT);
    }

    /**
     * �����把日期转换成你需要的格式����pattern��ʽ���и�ʽ��
     * @param d Date
     * @param pattern String
     * @return String
     */
    public static String format(Date d, String pattern)
    {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(d);
    }

    /**
     * �����把日期转换成你需要的格式����pattern��ʽ���и�ʽ����ʧ��ʱ����null
     * @param date String
     * @param srcPattern String ԭ���ڸ�ʽ
     * @param destPattern String
     * @return String
     */
    public static String format(String date, String srcPattern, String destPattern)
    {
        Date d = parse(date, srcPattern);
        if(d == null)
        {
            return null;
        }

        return format(d, destPattern);
    }

    /**
     * ��ʱ���把字符串根据格式转换成日期型����
     * @param date String
     * @param srcPattern String
     * @return Date
     */
    public static Date parse(String date, String pattern)
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(date);
        }
        catch(java.text.ParseException e)
        {
            return null;
        }
    }
    
    /**
     * 修改日期
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date modifyDate(Date date, int hour, int minute, int second)
    {
        GregorianCalendar gC = new GregorianCalendar();
        gC.setTime(date);
        gC.set(gC.HOUR_OF_DAY, hour);
        gC.set(gC.MINUTE, minute);
        gC.set(gC.SECOND, second);
        return gC.getTime();
    }
    
    /**
     * 获得下个月的第一天
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date lastMonthFirstDay(Date date)
    {
    	Calendar c= Calendar.getInstance(); 
    	c.add(Calendar.MONTH, -1);
    	c.set(Calendar.DATE, 1);
    	return c.getTime();
    }
    
    /**
     * 获得本月的第一天
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date thisMonthFirstDay(Date date)
    {
    	Calendar c= Calendar.getInstance(); 
    	c.set(Calendar.DATE, 1);
    	return c.getTime();
    }
    
    /**
     * 获得上个月的这一天
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date lastMonthThisDay(Date date)
    {
    	Calendar c= Calendar.getInstance(); 
    	c.setTime(date);
    	c.add(Calendar.MONTH, -1);
    	return c.getTime();
    }
    
    /**
     * 合并date 和time
     * 
     * @param date
     * @param time
     * @return
     */
    public static Date merge(Date date, Date time) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(time);
    	int hour = calendar.get(Calendar.HOUR_OF_DAY);
    	int minute = calendar.get(Calendar.MINUTE);
    	int second = calendar.get(Calendar.SECOND);
    	return modifyDate(date, hour, minute, second);
    }
    
    
    public static Date parse(String date)
    {
        return parse(date, "yyyy-MM-dd HH:mm:ss");
    }
    
    public static Date parseByCondition(String date)
    {
    	if(date.indexOf(" ")>0)
    		return parse(date, "yyyy-MM-dd HH:mm:ss");
    	else
    		return parse(date, "yyyy-MM-dd");
    }
    
    /**
     * 获得当月有多少天
     * @return
     */
    public static int getDayOfMonth() { 
    	Calendar c= Calendar.getInstance(); 
    	c.set(Calendar.YEAR, (new Date()).getYear()); 
    	c.set(Calendar.MONTH, (new Date()).getMonth()); 
    	return c.getActualMaximum(Calendar.DAY_OF_MONTH); 
    }
    
    /**
     * 获得上个月有多少天
     * @return
     */
    public static int getDayOfLastMonth() { 
    	Calendar c= Calendar.getInstance(); 
    	c.set(Calendar.YEAR, (new Date()).getYear()); 
    	c.set(Calendar.MONTH,(addMonths(new Date(),-1)).getMonth()); 
    	return c.getActualMaximum(Calendar.DAY_OF_MONTH); 
    }
    /**
     * 获得7天前的日期
     * @return
     */
    public static Date getDayOfMonday() { 
    	Calendar c= Calendar.getInstance(); 
    	c.set(Calendar.YEAR, (new Date()).getYear()); 
    	c.set(Calendar.MONTH, (new Date()).getMonth());
    	c.set(Calendar.DATE, (new Date()).getDate()-7);
//    	c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//    	if(c.getTime().)
    	return c.getTime() ;
    }
    
    /**
     * 给日期增加分钟,返回string
     * @param addminutes
     * @return
     */
    public static String addMintus(int addminutes){
    	Date d = parse("2000:01:01 00:00", "yyyy:MM:dd HH:mm");
    	d = modifyDate(d,0,addminutes,0);
    	System.out.println(d);
    	return format(d,"yyyy:MM:dd HH:mm");
    }
    /**
     * 给日期增加秒,返回Date
     * @param addminutes
     * @return
     */
    public static Date addSecond(Date date, int addsecond){
        Calendar c = Calendar.getInstance();   
        c.setTime(date);
        c.add(Calendar.SECOND, addsecond); 
        System.out.println(format(c.getTime(),"yyyy-MM-dd HH:mm:ss"));
        return c.getTime();
    }
    
    /**
     * 判断时间的开始结束被包含
     * d1,d2 在 d3,d4的区间内
     */
    public static boolean contain(Date d1,Date d2 ,Date d3,Date d4){
        if(d1.compareTo(d3)>=0 && d2.compareTo(d4)<1)
        	return true;
        else
        	return false;
    }
    
    /**
     * 判断时间的开始结束被交叉或包含
     * d1，d2与d3,d4的范围存在交集
     */
    public static boolean cross(Date d1,Date d2 ,Date d3,Date d4){
        if(d2.compareTo(d4)<=0 && d2.compareTo(d3)>0)
        	return true;
        else if(d1.compareTo(d3)>=0 && d1.compareTo(d4)<0)
        	return true;
        else if(contain(d3,d4,d1,d2)){
        	return true;
        }
        return false;
    }
    
    /**
     * 判断当前日期是星期几
     * 
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static int dayForWeek(String pTime) throws Exception {
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar c = Calendar.getInstance();
    	c.setTime(format.parse(pTime));
    	int dayForWeek = 0;
    	if(c.get(Calendar.DAY_OF_WEEK) == 1){
    		dayForWeek = 7;
    	}else{
    		dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
    	}
    	return dayForWeek;
    }
    
    public static String getDiffTime(Date date) {
    	return getDiffTime(date, new Date());
    }
    /**
     * 比较时间差，返回文字信息
     * @param date
     * @return
     * @throws Exception
     */
    public static String getDiffTime(Date beforeDate, Date afterDate) {
    	String msg = "";
    	try {
			long between = afterDate.getTime() - beforeDate.getTime();
			long i = between/(1000*60);
			if (i < 60) {
				msg = i + "分钟前";
			}else{
				i = between/(1000*60*60);
				if (i < 24) {
					msg = i +  "小时前";
				}else{
					i = between/(1000*60*60*24);
					msg = i +  "天前";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return msg;
    }
    
    /**
     * 比较时间差，返回文字信息
     * @param date
     * @return
     * @throws Exception
     */
    public static long getDiffHour(Date beforeDate, Date afterDate) {
    	try {
			long between = afterDate.getTime() - beforeDate.getTime();
			long i = between/(1000*60*60);
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
    }
    
    public static void main(String[] args) {
    	try {
            //1531793493
            //1531793493776
//            Long t = 1470844800 * 1000L;
//            Date date = new Date(t);
//            System.out.println("date:" + DateUtil.format(date,"yyyyMMddHHmmss"));
//            Date date = DateUtil.parse("2016-08-11 00:00:00", "yyyy-MM-dd HH:mm:ss");
//            Long t = date.getTime()/1000L;
//            System.out.println(DateUtil.format(date,"yyyyMMddHHmmss"));
//            System.out.println("t:" + t);
//            System.out.println(System.currentTimeMillis()/1000L);
//            System.out.println(System.currentTimeMillis());
            DecimalFormat decimalFormat = new DecimalFormat("0.0#");
            System.out.println(decimalFormat.format(new BigDecimal(("3330.0001"))));

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
}
