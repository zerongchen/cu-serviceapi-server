package com.aotain.serviceapi.server.util;

import com.aotain.cu.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IPv6地址转换工具类 IPv6地址为十六进制，由8个16位数字共128位组成，格式如下：
 *  1) 2006:DB8:2A0:2F3B:34:E35:45:1 --用16进制表示每个域的值(16位)
 *  2) 2006:DB8::E34:1 --"::" 代表0，且只能出现一次
 *  3) 2002:9D36:1:2:0:5EFE:192.168.12.9 --带有ipv4地址
 *
 * @author zcw
 */
public class IPUtils {
    private static final String split_char = ":";
    private static Map<Character, BigInteger> hexMap = new HashMap<Character, BigInteger>();
    private static Map<Integer, Character> hex_oct_Map = new HashMap<Integer, Character>();
    private static final int ip_length = 8;
    private static final int single_length = 4;

    private static Pattern pattern = Pattern
            .compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\."
                    + "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\."
                    + "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\."
                    + "((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

    private static final int[][] shift = { { 124, 120, 116, 112 },
            { 108, 104, 100, 96 }, { 92, 88, 84, 80 }, { 76, 72, 68, 64 },
            { 60, 56, 52, 48 }, { 44, 40, 36, 32 }, { 28, 24, 20, 16 },
            { 12, 8, 4, 0 } };

    static {
        hexMap.put('0', new BigInteger("0"));
        hexMap.put('1', new BigInteger("1"));
        hexMap.put('2', new BigInteger("2"));
        hexMap.put('3', new BigInteger("3"));
        hexMap.put('4', new BigInteger("4"));
        hexMap.put('5', new BigInteger("5"));
        hexMap.put('6', new BigInteger("6"));
        hexMap.put('7', new BigInteger("7"));
        hexMap.put('8', new BigInteger("8"));
        hexMap.put('9', new BigInteger("9"));
        hexMap.put('A', new BigInteger("10"));
        hexMap.put('B', new BigInteger("11"));
        hexMap.put('C', new BigInteger("12"));
        hexMap.put('D', new BigInteger("13"));
        hexMap.put('E', new BigInteger("14"));
        hexMap.put('F', new BigInteger("15"));

        hex_oct_Map.put(0, '0');
        hex_oct_Map.put(1, '1');
        hex_oct_Map.put(2, '2');
        hex_oct_Map.put(3, '3');
        hex_oct_Map.put(4, '4');
        hex_oct_Map.put(5, '5');
        hex_oct_Map.put(6, '6');
        hex_oct_Map.put(7, '7');
        hex_oct_Map.put(8, '8');
        hex_oct_Map.put(9, '9');
        hex_oct_Map.put(10, 'A');
        hex_oct_Map.put(11, 'B');
        hex_oct_Map.put(12, 'C');
        hex_oct_Map.put(13, 'D');
        hex_oct_Map.put(14, 'E');
        hex_oct_Map.put(15, 'F');
    }

    public static String ipToString(String ip) {
        if (StringUtil.isEmptyString(ip)) {
            return null;
        }
        if (Tools.isIpAddress(ip)) {
            return String.valueOf(Tools.ip2long(ip));
        } else {
            BigInteger result = new BigInteger("0");
            ip = ip.toUpperCase();
            ip = addLeadingZero(ip);
            ip = dealContainsIpv4(ip);
            ip = dealContinusZero(ip);
            String[] ipArray = ip.split(split_char);
            for (int i = 0; i < ipArray.length; i++) {
                char[] temp = ipArray[i].toCharArray();
                for (char c : temp) {
                    if (!hexMap.containsKey(c)) {
                        return null;
                    }
                }
            }
            for (int i = 0; i < ipArray.length; i++) {
                BigInteger bi = new BigInteger("2");
                char[] temp = ipArray[i].toCharArray();
                for (int j = 0; j < temp.length; j++) {
                    BigInteger t = hexMap.get(temp[j]);
                    result = result.add(t.multiply(bi.pow(shift[i][j])));
                }
            }
            return result.toString();
        }
    }

    private static String addLeadingZero(String ip) {
        String[] splitArray = ip.split(split_char);
        int length = splitArray.length;
        for (int i = 0; i < length; i++) {
            if (!StringUtil.isEmptyString(splitArray[i])) {
                if (splitArray[i].toCharArray().length != single_length) {
                    splitArray[i] = StringUtil.addLeadingZero(splitArray[i],
                            single_length);
                }
            }
        }
        return StringUtil.concat(splitArray, split_char);
    }

    private static String dealContainsIpv4(String ip) {
        String[] splitArray = ip.split(split_char);
        int length = splitArray.length;
        for (int i = 0; i < length; i++) {
            if (!StringUtil.isEmptyString(splitArray[i])) {
                if (Tools.isIpAddress(splitArray[i])) {
                    splitArray[i] = ipv4ToIpv6String(splitArray[i]);
                }
            }
        }
        return StringUtil.concat(splitArray, split_char);
    }

    private static String ipv4ToIpv6String(String string) {
        long shift[] = { 3, 2, 1, 0 };
        // 将ipv4地址转换为二进制数据
        String binary = Long.toBinaryString(Tools.ip2long(string));
        // 补齐32位
        binary = StringUtil.addLeadingZero(binary, 32);
        // 每4位切割
        String[] temp = StringUtil.tokenize(binary, 4);
        String[] tempResult = new String[temp.length];
        for (int i = 0; i < temp.length; i++) {
            char[] arr = temp[i].toCharArray();
            int t = 0;
            for (int j = 0; j < arr.length; j++) {
                t += Long.parseLong(arr[j] + "") << shift[j];
            }
            tempResult[i] = String.valueOf(hex_oct_Map.get(t));
        }
        String result = StringUtil.concat(tempResult, null);
        return StringUtil.concat(StringUtil.tokenize(result, 4), split_char);
    }

    private static String dealContinusZero(String ip) {
        int index = ip.indexOf("::");
        if (index != -1) {
            String replaceStr = "";
            String[] temp = ip.split(split_char);
            int length = temp.length;
            StringBuffer buffer = new StringBuffer();
            if (index == 0) {
                for (int i = 0; i <= ip_length - length + 1; i++) {
                    buffer.append("0000").append(split_char);
                }
            } else {
                for (int i = 0; i <= ip_length - length; i++) {
                    buffer.append(split_char).append("0000");
                }
                buffer.append(split_char);
            }
            replaceStr = buffer.toString();
            ip = ip.replaceAll("::", replaceStr);
        }
        return ip;
    }

    /**
     * 校验StartIp是否小于EndIp
     * @param startIp
     * @param endIp
     * @return
     */
    public static boolean validIpRule(String startIp, String endIp) {
        boolean flag = false;
        String startips[] = startIp.split("\\.");
        String endIps[] = endIp.split("\\.");
        for (int i = 0; i < startips.length; i++) {
            if (Integer.parseInt(endIps[i]) > Integer.parseInt(startips[i])) {
                flag = true;
                break;
            } else {
                if (Integer.parseInt(endIps[i]) == Integer.parseInt(startips[i])) {
                    if(i==3) {   //startIp和endIp可以相同
                        flag = true;
                    }
                    continue;
                } else {
                    break;
                }
            }
        }
        return flag;
    }

    public static boolean validIsIp(String ip) {
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }





//    /**
//     * 校验ip是否在机房ip范围内
//     * @param ip
//     * @param IpValueList
//     * @return
//     */
//    public static boolean validateIpRange(String ip, List<IpValue> IpValueList) {
//        String ipRange;
//        for(IpValue ipValue : IpValueList){
//            if(ipValue.getStartIp() != null && ipValue.getEndIp() != null){
//                ipRange = ipValue.getStartIp() + "-" + ipValue.getEndIp();
//                boolean flag = Tools.ipExistsInRange(ip,ipRange);
//                if(flag){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public static void main(String[] args) {
        System.out.println(ipToString("2006:DB8:2A0:2F3B:34:E35:45:1"));
    }

}
