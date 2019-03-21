package com.jennifer.jennifer.util;

import android.util.Log;

import com.jennifer.jennifer.entity.PingNetEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

/**
 * @author jingjie
 * @date 2019/3/20
 * @desc:ping工具类
 */
public class PingNet {
    private static final String TAG = "PingNet";

    /**
     * @param pingNetEntity 检测网络实体类
     * @return 检测后的数据
     */
    public static PingNetEntity ping(PingNetEntity pingNetEntity) {
        String line;
        Process process = null;
        BufferedReader successReader = null;
        //ping -c 次数 -w 超时时间（s） ip
        String command = "ping -c " + pingNetEntity.getPingCount() + " -w " + pingNetEntity.getPingWtime() + " " + pingNetEntity.getIp();
//        String command = "ping -c " + pingCount + " " + host;
        try {
            process = Runtime.getRuntime().exec(command);
            if (process == null) {
                append(pingNetEntity.getResultBuffer(), "ping fail:process is null.");
                pingNetEntity.setPingTime(null);
                pingNetEntity.setResult(false);
                return pingNetEntity;
            }
            successReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int count = 0;
            BigDecimal sum = new BigDecimal(0);
            while ((line = successReader.readLine()) != null) {
                append(pingNetEntity.getResultBuffer(), line);
                BigDecimal time = getTime(line);
                if (time != null) {
                    sum = sum.add(time);
                    count++;
                }
            }
            //三次时间取平均值，四舍五入保留两位小数
            pingNetEntity.setPingTime((sum.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + " ms"));
            int status = process.waitFor();
            if (status == 0) {
                append(pingNetEntity.getResultBuffer(), "exec cmd success:" + command);
                pingNetEntity.setResult(true);
            } else {
                append(pingNetEntity.getResultBuffer(), "exec cmd fail.");
                pingNetEntity.setPingTime(null);
                pingNetEntity.setResult(false);
            }
            append(pingNetEntity.getResultBuffer(), "exec finished.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (successReader != null) {
                try {
                    successReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return pingNetEntity;
    }

    private static void append(StringBuffer stringBuffer, String text) {
        if (stringBuffer != null) {
            stringBuffer.append(text + "\n");
        }
    }

    /**
     * 获取ping接口耗时
     *
     * @param line
     * @return BigDecimal避免float、double精准度问题
     */
    private static BigDecimal getTime(String line) {
        String[] lines = line.split("\n");
        String time = null;
        for (String l : lines) {
            if (!l.contains("time="))
                continue;
            int index = l.indexOf("time=");
            time = l.substring(index + "time=".length());
            index = time.indexOf("ms");
            time = time.substring(0, index);
        }
        return time == null ? null : new BigDecimal(time.trim());
    }
}
