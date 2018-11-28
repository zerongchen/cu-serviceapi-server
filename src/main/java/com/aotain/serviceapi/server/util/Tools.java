package com.aotain.serviceapi.server.util;

import com.aotain.serviceapi.server.constant.CommonRegexEnum;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/06
 */
public class Tools {

	public static boolean isIp(String s){
		return isIpV6Address(s)||isIpAddress(s);
	}
    /**
     * 判断是否是IPV4地址
     *
     * @param s
     * @return
     */
    public static boolean isIpAddress(String s) {
        if (s == null)
            return false;
        String regex = "(((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        return m.matches();
    }
    
    /**
     * 判断是否是IPV6地址
     *
     * @param s
     * @return
     */
    public static boolean isIpV6Address(String s) {
        if (s == null)
            return false;
        boolean result = false;
        String regex = DataValidateUtils.IPV6_STD_REGEXP;
       /* Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        return m.matches();*/
        /*String regHex = "(\\p{XDigit}{1,4})";
        //没有双冒号
        String regIPv6Full = "^(" + regHex + ":){7}" + regHex + "$";
        
        //双冒号在中间或者没有双冒号
        String regIPv6AbWithColon = "^(" + regHex + "(:|::)){0,6}" + regHex
                + "$";
        
        //双冒号开头
        String regIPv6AbStartWithDoubleColon = "^(" + "::(" + regHex
                + ":){0,5}" + regHex + ")$";
        
        String regIPv6 = "^(" + regIPv6Full + ")|("
                + regIPv6AbStartWithDoubleColon + ")|(" + regIPv6AbWithColon
                + ")$";*/
        if (s.indexOf(":") != -1)
        {
            if (s.length() <= 39)
            {
                String addressTemp = s;
                int doubleColon = 0;
                if(s.equals("::"))return true;
                while (addressTemp.indexOf("::") != -1)
                {
                    addressTemp = addressTemp.substring(addressTemp
                            .indexOf("::") + 2, addressTemp.length());
                    doubleColon++;
                }
                if (doubleColon <= 1)
                {
                    result = s.matches(regex);
                }
            }
        }
       return result;
    }
    
    /**
     * 判断是否是域名
     *
     * @param s
     * @return
     */
    public static boolean isDomainName(String domain) {
        if (domain == null)
            return false;
        String ipRegex = DataValidateUtils.IPV6_STD_REGEXP;
        String domainRegex = DataValidateUtils.DATA_DOMAIN_REGEXP;
        Pattern ipP = Pattern.compile(ipRegex);
        Pattern domainP = Pattern.compile(domainRegex);
        Matcher ipM = ipP.matcher(domain);
        Matcher domainM = domainP.matcher(domain);
        
        return ipM.matches()||domainM.matches();
    }

    /**
     * ip地址转成整数.
     *
     * @param ip
     * @return
     */
    public static long ip2long(String ip) {
        if (!isIpAddress(ip))
            return -1;

        String[] ips = ip.split("[.]");
        long num = 16777216L * Long.parseLong(ips[0]) + 65536L * Long.parseLong(ips[1]) + 256 * Long.parseLong(ips[2])
                + Long.parseLong(ips[3]);

        return num;
    }
    
    /**
	 * 判断是否是合法域名
	 * 
	 * @param domain
	 * @return
	 */
	public static boolean isDomain(String domain) {
		if (domain == null) {
			return false;
		}
		String regex = RegexUtils.getRegexValueBySubType(CommonRegexEnum.DOMAIN_REGEXP.getSubType());
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(domain);
		
		String ipv4Regex = RegexUtils.getRegexValueBySubType(CommonRegexEnum.IPV4_REGEXP.getSubType());
		Pattern ipv4Pattern = Pattern.compile(ipv4Regex);
		Matcher ipMatcher = ipv4Pattern.matcher(domain);
		
		return matcher.matches()||ipMatcher.matches();
	}
	
	/**
	 * 判断是否url
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isURL(String url) {
		if (url == null) {
			return false;
		}
		String regex = "^" +
		// protocol identifier
				"(?i)(" + "(?:(?:https?|ftp)://)" +
				// user:pass authentication
				"(?:\\S+(?::\\S*)?@)?" + "(?:" +
				// IP address exclusion
				// private & local networks
				"(?!10(?:\\.\\d{1,3}){3})" + "(?!127(?:\\.\\d{1,3}){3})" + "(?!169\\.254(?:\\.\\d{1,3}){2})"
				+ "(?!192\\.168(?:\\.\\d{1,3}){2})" + "(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})" +
				// IP address dotted notation octets
				// excludes loopback network 0.0.0.0
				// excludes reserved space >= 224.0.0.0
				// excludes network & broacast addresses
				// (first & last IP address of each class)
				"(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])" + "(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}"
				+ "(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))" + "|" +
				// host name
				"(?:(?:[a-z0-9]+-?)*[a-z0-9]+)" +
				// domain name
				"(?:\\.(?:[a-z0-9]+-?)*[a-z0-9]+)*" +
				// TLD identifier
				"(?:\\.(?:[a-z]{2,}))" + ")" +
				// port number
				"(?::\\d{2,5})?" +
				// resource path
				"(?:/[^\\s]*)?" + ")$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);
		return matcher.matches();
	}
	
	/**
	 * ipv6转int
	 * 
	 */
	public static BigInteger ipv6toInt(String ipv6)
	{
 
		int compressIndex = ipv6.indexOf("::");
		if (compressIndex != -1)
		{
			String part1s = ipv6.substring(0, compressIndex);
			String part2s = ipv6.substring(compressIndex + 1);
			BigInteger part1 = ipv6toInt(part1s);
			BigInteger part2 = ipv6toInt(part2s);
			int part1hasDot = 0;
			char ch[] = part1s.toCharArray();
			for (char c : ch)
			{
				if (c == ':')
				{
					part1hasDot++;
				}
			}
			// ipv6 has most 7 dot
			return part1.shiftLeft(16 * (7 - part1hasDot )).add(part2);
		}
		String[] str = ipv6.split(":");
		BigInteger big = BigInteger.ZERO;
		for (int i = 0; i < str.length; i++)
		{
			//::1
			if (str[i].isEmpty())
			{
				str[i] = "0";
			}
			big = big.add(BigInteger.valueOf(Long.valueOf(str[i], 16))
			        .shiftLeft(16 * (str.length - i - 1)));
		}
		return big;
	}

	/**
	 * 获取服务器名+IP
	 * @return
	 */
	public static String getHostAddressAndIp() {
		String ip = "";
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ip = addr.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}
	/**
	 * int 转ipv6
	 * 
	 * @author : songl
	 * @since:2018年7月11日 下午8:00:43
	 */
	public static String int2ipv6(BigInteger big)
	{
		String str = "";
		BigInteger ff = BigInteger.valueOf(0xffff);
		for (int i = 0; i < 8 ; i++)
		{
			str = big.and(ff).toString(16) + ":" + str;
			
			big = big.shiftRight(16);
		}
		//the last :
		str = str.substring(0, str.length() - 1);
		
		return str.replaceFirst("(^|:)(0+(:|$)){2,8}", "::");
	}
	
	//IPV6缩写补全
	public  static String ipV6Completed(String address){
		return "";
	}
	
	public static void main(String[] args) {
		/*String stringArray[] = {"240E:0083:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0083:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0000:97FF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0000:9FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0083:07FF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:0FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:8FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0054:BFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D0:3FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D6:0FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:1FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:9FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0055:3FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D2:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D6:1FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:37FF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:B7FF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D0:BFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D6:37FF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:3FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:BFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D0:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:2FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:AFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0055:BFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D4:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D6:2FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:47FF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:C7FF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D2:BFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D6:47FF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:4FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:CFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D2:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D6:4FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:57FF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:D7FF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D4:BFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D6:57FF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:5FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0014:DFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00D4:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:001E:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:001E:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:001F:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:001F:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0027:9FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0027:BFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0027:DFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0027:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:002F:9FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:002F:BFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:002F:DFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:002F:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005E:3FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005E:BFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005F:3FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005F:BFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005E:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005E:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005F:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005F:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005E:5FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005E:DFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005F:5FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005F:DFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005E:4FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005E:CFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005F:4FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:005F:CFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00F8:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FA:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FC:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0060:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0061:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0062:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0063:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:0064:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00F9:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FB:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FD:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00F9:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FB:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FD:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FF:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00F9:3FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FB:3FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FD:3FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FF:3FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:001E:1FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FC:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FC:8FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FC:9FFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FC:AFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FC:BFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FC:CFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FC:DFFF:FFFF:FFFF:FFFF:FFFF:FFFF"
				,"240E:00FC:EFFF:FFFF:FFFF:FFFF:FFFF:FFFF"};

		long startTime = System.currentTimeMillis();
		int count = 0;
		for(int k=0; k<10000;k++) {
			for (int i = 0; i < stringArray.length; i++) {
				//BigInteger ip = ipv6toInt("AD80:0000:0000:0000:ABAA:0000:00C2:0002");
				BigInteger ip = ipv6toInt(stringArray[i]);
				//System.out.println(ip);
				//long ips = ip.longValue();
				//System.out.println(ip+"--"+ips+"=="+Tools.ip2long("2006:DB8:2A0:2F3B:34:E35:45:1"));
			}
			count += stringArray.length;
		}
		long endTime = System.currentTimeMillis();

		System.out.println("Total use time:" + (endTime - startTime) + "ms, record:"+ count);*/
		//不缩写的IPV6
		System.out.println(isIpV6Address("240E:0083:7FFF:FFFF:FFFF:FFFF:FFFF:FFFF"));
		//缩写的IpV6
		System.out.println(isIpV6Address("0::FFFF:ffff"));
		System.out.println(isIpV6Address("0:0:0:0:0:0:0:1"));
		System.out.println(ipv6toInt("1:0:0:0:0:0:0:1"));
		System.out.println(ipv6toInt("1::1"));
		
		String domain = ".";
		String eReg = "^(([A-Za-z0-9\u4E00-\u9FA5-~]+)\\.)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
		String reg = "/^[0-9a-zA-Z\u4e00-\u9faf]+[0-9a-zA-Z\u4e00-\u9faf\\.-]*\\.[a-zA-Z\u4e00-\u9faf]{2,4}$/";
		String regText = "/^[0-9a-zA-Z\\u4e00-\\u9faf]+[0-9a-zA-Z\\u4e00-\\u9faf\\.-]*\\.[a-zA-Z\\u4e00-\\u9faf]{2,4}$/";
		Pattern domainP = Pattern.compile(eReg);
		System.out.println(domainP.matcher(domain).matches());
	}
}
