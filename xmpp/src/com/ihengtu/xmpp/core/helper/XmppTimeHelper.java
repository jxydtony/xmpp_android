package com.ihengtu.xmpp.core.helper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class XmppTimeHelper {

	public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_FORMAT2 = "yyyy-MM-dd";
    
	public static String getStringTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":"
				+ c.get(Calendar.SECOND);
	}
    
	/**
	 * �õ�У������ȷʱ��
	 * ��ʽ���磺yyyy-MM-dd HH:mm:ss
	 * @param timedifference ����λ��long ֵ
	 * @return
	 */
	public static String getStringTime(long timedifference){
		String time=null;
		long currtime=System.currentTimeMillis();
		long result=currtime+timedifference;
		time=getLongtoString(result);
		return time;
	}
	
	/**
     * ��long ��ʱ��ֵת��Ϊ�̶���ʽΪ:yyyy-MM-dd HH:mm:ss���
     * @param str
     * @return
     */
    public static String getLongtoString(long time){
		String str=null;
		Date date=new Date(time);
		SimpleDateFormat sdf=new SimpleDateFormat(DEFAULT_FORMAT);
		str=sdf.format(date);
		return str;
	}
	
    /**
     * �õ���ǰ������ʱ����ϵͳʱ���ֵ
     * @param serverTime
     * @return
     */
    public static long getTimeDifference(long serverTime){
    	long timedifference=0L;
    	long curr=System.currentTimeMillis();
    	timedifference=serverTime-curr;
    	return timedifference;
    }
    
	/**
	 * return format time string
	 * @return
	 */
	public static String getFormatTimeString()
	{
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return format.format(new Date());
	}
	
	/**
	 * ��Timestamp ת��Ϊ�ַ�
	 * 
	 * @param time
	 * @return
	 */
	public static String TimestampToStr(Timestamp time) {
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_FORMAT);// �����ʽ������ʾ����
		String str = df.format(time);
		System.out.println(str);
		return str;
	}

	/**
	 * ���ַ�ת��ΪTimestamp
	 * 
	 * @param str
	 * @return
	 */
	public static Timestamp StrToTimestamp(String str) {
		Timestamp time = Timestamp.valueOf(str);
		System.out.println(time);
		return time;
	}

	/**
	 * �ַ�ת��������
	 * 
	 * @param stringdate
	 *            Ҫת�����ַ�(�ַ��ʽ yyyy-MM-dd HH:mm:ss)
	 * @return
	 */
	public static Date getStringtoDate(String stringdate) {
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_FORMAT);
		Date cDate = null;
		try {
			cDate = df.parse(stringdate);
		}
		catch (ParseException e1) {
		}
		return cDate;
	}
	/**
	 * �ַ�ת��������
	 * 
	 * @param stringdate
	 *            Ҫת�����ַ�(�ַ��ʽ yyyy-MM-dd HH:mm:ss)
	 * @return
	 */
	public static Date getStringtoDate2(String stringdate) {
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_FORMAT2);
		Date cDate = null;
		try {
			cDate = df.parse(stringdate);
		}
		catch (ParseException e1) {
		}
		return cDate;
	}

	/**
	 * �õ�long����ʱ��ֵ
	 * 
	 * @param stringdate
	 * @return
	 */
	public static long getStringtoLong(String stringdate) {
		long time = 0L;
		if(stringdate==null) return time;
		Date date = getStringtoDate(stringdate);
		if (date != null) time = date.getTime();
		return time;
	}

	/**
	 * ����ļ���������
	 * 
	 * @return
	 */
	public static String getFileStringTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		String tempString = String.format("obd%s%s%s%s%s%s", c.get(Calendar.YEAR), (c.get(Calendar.MONTH) + 1), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
				c.get(Calendar.SECOND),c.get(Calendar.MILLISECOND));
		return tempString;
	}

	/**
	 * �������
	 */
	public static String getStringTimeYYMM() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		String tempString = String.format("%s-%s-%s", c.get(Calendar.YEAR), (c.get(Calendar.MONTH) + 1), c.get(Calendar.DAY_OF_MONTH));
		return tempString;
	}

	/**
	 * ���ʱ��
	 */
	public static String getStringTimeHHmm() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		String tempString = String.format("%s:%s", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
		return tempString;
	}
	
	/**
	 * ��������ˢ��ʱ��
	 * @return ���� 10:52
	 */
	public static String friendly_time(){
		int nowtime=new Date().getHours();
		String time=null;
		if(nowtime<=6){
			time="�賿"+dateFormater3.get().format(new Date());
		}
		else if(nowtime<=11){
			time="����"+dateFormater3.get().format(new Date());
		}
		else if(nowtime<=13){
			time="����"+dateFormater3.get().format(new Date());
		}
		else if(nowtime<=17){
			time="����"+dateFormater3.get().format(new Date());
		}
		else{
			time="����"+dateFormater3.get().format(new Date());
		}
		
		return time;
	}

	/**
     * ���Ѻõķ�ʽ��ʾʱ��
     * @param sdate
     * @return
     */
    public static String friendly_time(String sdate) {
        Date time = toDate(sdate); 
        if(time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();
        
        //�ж��Ƿ���ͬһ��
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if(curDate.equals(paramDate)){
            int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
            if(hour<0){
            	ftime=dateFormater3.get().format(time);
            }
            else if(hour == 0){
            	long minutes=Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1);
            	if(minutes<5)
            		ftime="�ո�";
            	else if(minutes>5 &&minutes<30){
            		ftime=minutes+"����ǰ";
            	}
            	else
            		ftime=dateFormater3.get().format(time);
            }
            else if(hour>0 &&hour<2){
            	ftime = hour+"Сʱǰ";
            }
            else
            	ftime=dateFormater3.get().format(time);
                
            return ftime;
        }
        
        long lt = time.getTime()/86400000;
        long ct = cal.getTimeInMillis()/86400000;
        int days = (int)(ct - lt); 
        if(days<0){
        	ftime=dateFormater2.get().format(time);
        }
        else if(days == 0){
            int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
            if(hour<0){
            	ftime=dateFormater3.get().format(time);
            }
            else if(hour == 0){
            	long minutes=Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1);
            	if(minutes<5)
            		ftime="�ո�";
            	else if(minutes>5 &&minutes<30){
            		ftime=minutes+"����ǰ";
            	}
            	else
            		ftime=dateFormater3.get().format(time);
                
            }
            else if(hour>0 &&hour<2){
            	 ftime = hour+"Сʱǰ";
            }
            else 
                ftime=dateFormater3.get().format(time);
                
        }
        else if(days == 1){
            ftime = "����"+dateFormater3.get().format(time);
        }
        else if(days == 2){
            ftime = "ǰ��"+dateFormater3.get().format(time);
        }
//        else if(days > 2 && days <= 10){ 
//            ftime = days+"��ǰ";            
//        }
        else if(days > 2){            
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

	/**
	 * ���ַ�תλ��������
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		}
		catch (ParseException e) {
			return null;
		}
	}
	/**
	 * ���ַ�תλ��������
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate2(String sdate) {
		try {
			return dateFormater2.get().parse(sdate);
		}
		catch (ParseException e) {
			return null;
		}
	}
	/**
	 * ���ַ�תλ��������
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate4(String sdate) {
		try {
			return dateFormater4.get().parse(sdate);
		}
		catch (ParseException e) {
			return null;
		}
	}
	/**
	 * ���ַ�תλ��������
	 * 
	 * @param sdate
	 * @return
	 */
	 /**
	  * ��ȡ����ʱ��
	  * 
	  * @return�����ַ��ʽ yyyy-MM-dd HH:mm:ss
	  */
	public static String toDateYYYYMMDD(String dateString) {
		String result="";
		try {
			DateFormat formatBefore = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			date = formatBefore.parse(dateString);
			SimpleDateFormat formatAfter = new SimpleDateFormat("yyyy/MM/dd");
			result = formatAfter.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	  return result;
	}
	
	/**
	 * ���ַ�תλ��������
	 * 
	 * @param sdate
	 * @return
	 */
	 /**
	  * ��ȡ����ʱ��
	  * 
	  * @return�����ַ��ʽ yyyy-MM-dd HH:mm:ss
	  */
	public static String toDateYYYYMMDD2(String dateString) {
		String result = XmppTimeHelper.formatToString(dateString,"yyyy/MM/dd HH:mm:ss");
	  return result;
	}
	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return�����ַ��ʽ yyyy-MM-dd HH:mm:ss
	 */
	public static String toDateYYYYMMDD3(Date date) {
		String result="";
		SimpleDateFormat temp=new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
		result = temp.format(date);
		return result;
	}
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	
	private final static ThreadLocal<SimpleDateFormat> dateFormater3 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm");
		}
	};
	
	private final static ThreadLocal<SimpleDateFormat> dateFormater4 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		}
	};
	 // ��δָ����ʽ���ַ�ת������������
	 public static Date parseStringToDate(String date) throws ParseException{     
		  Date result=null;     
		  String parse=date;     
		  parse=parse.replaceFirst("^[0-9]{4}([^0-9]?)", "yyyy$1");     
		  parse=parse.replaceFirst("^[0-9]{2}([^0-9]?)", "yy$1");     
		  parse=parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1MM$2");     
		  parse=parse.replaceFirst("([^0-9]?)[0-9]{1,2}( ?)", "$1dd$2");     
		  parse=parse.replaceFirst("( )[0-9]{1,2}([^0-9]?)", "$1HH$2");     
		  parse=parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1mm$2");     
		  parse=parse.replaceFirst("([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1ss$2");     
		             
		  SimpleDateFormat format= new SimpleDateFormat(parse);     
		    
		  result=format.parse(date);     
	        
	  return result;     
	 } 
	// �����ڸ�ʽ���ַ���ָ����ʽ���
	 public static String formatToString(String date, String format) {
	  try {
	   Date dt = parseStringToDate(date);
	   java.text.SimpleDateFormat sdf = new SimpleDateFormat(format);
	   return sdf.format(dt);
	  } catch (ParseException e) {
	   e.printStackTrace();
	  }
	  return date;
	 }
//	private final static ThreadLocal<SimpleDateFormat> dateFormater4 = new ThreadLocal<SimpleDateFormat>() {
//		@Override
//		protected SimpleDateFormat initialValue() {
//			return new SimpleDateFormat("yyyy");
//		}
//	};
//	private final static ThreadLocal<SimpleDateFormat> dateFormater5 = new ThreadLocal<SimpleDateFormat>() {
//		@Override
//		protected SimpleDateFormat initialValue() {
//			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		}
//	};

	/**
	 * ��ȡʱ��
	 * 
	 * @return
	 */
	public static MyDate GetMyDate() {
		MyDate mMyDate = new MyDate();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		mMyDate.setYear(c.get(Calendar.YEAR));
		mMyDate.setMonth(c.get(Calendar.MONTH) + 1);
		mMyDate.setDay(c.get(Calendar.DAY_OF_MONTH));
		mMyDate.setHour(c.get(Calendar.HOUR_OF_DAY));
		mMyDate.setMinute(c.get(Calendar.MINUTE));
		mMyDate.setSecond(c.get(Calendar.SECOND));
		return mMyDate;
	}

	public static class MyDate {
		private int year, month, day, hour, minute, second;

		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(getYearStr());
			builder.append(getMonthStr());
			builder.append(getDayStr());
			builder.append(getHourStr());
			builder.append(getMinStr());
			builder.append(getSecondStr());
			return builder.toString();
		}

		public int getHour() {
			return hour;
		}

		public void setHour(int hour) {
			this.hour = hour;
		}

		public int getMinute() {
			return minute;
		}

		public void setMinute(int minute) {
			this.minute = minute;
		}

		public int getSecond() {
			return second;
		}

		public void setSecond(int second) {
			this.second = second;
		}

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public int getDay() {
			return day;
		}

		public void setDay(int day) {
			this.day = day;
		}

		public String getYearStr() {
			return String.valueOf(year);
		}

		public String getMonthStr() {
			String s = String.valueOf(month);
			if (s.length() < 2) {
				s = '0' + s;
			}
			return s;
		}

		public String getDayStr() {
			String s = String.valueOf(day);
			if (s.length() < 2) {
				s = '0' + s;
			}
			return s;
		}

		public String getHourStr() {
			String s = String.valueOf(hour);
			if (s.length() < 2) {
				s = '0' + s;
			}
			return s;
		}

		public String getMinStr() {
			String s = String.valueOf(minute);
			if (s.length() < 2) {
				s = '0' + s;
			}
			return s;
		}

		public String getSecondStr() {
			String s = String.valueOf(second);
			if (s.length() < 2) {
				s = '0' + s;
			}
			return s;
		}
	}
	public static String getDateFormatYYYYMMDD(String time)
	 {
		DateFormat format_before = new SimpleDateFormat("yyyy-MM-dd");
		Date dDate = null;
		try {
			dDate = format_before.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    DateFormat format_after = new SimpleDateFormat("yyyy-MM-dd");
	    String reTime = format_after.format(dDate);
		return reTime;
	 }
}
