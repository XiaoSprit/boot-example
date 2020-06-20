import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.xhxin.common.util.FileTypeUtil;
import org.xhxin.common.util.GsonBeans;
import org.xhxin.common.util.GsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TestUtil {

    @Test
    public void dateOut(){
        StringBuilder buf = new StringBuilder(10);
        buf.append("upload/images/");
        LocalDate now = LocalDate.now();
        int yearValue = now.getYear();
        int monthValue = now.getMonthValue();
        int dayValue = now.getDayOfMonth();
        int absYear = Math.abs(yearValue);
        if (absYear < 1000) {
            if (yearValue < 0) {
                buf.append(yearValue - 10000).deleteCharAt(1);
            } else {
                buf.append(yearValue + 10000).deleteCharAt(0);
            }
        } else {
            if (yearValue > 9999) {
                buf.append('+');
            }
            buf.append(yearValue);
        }
        buf.append("/");
        String s = buf.append(monthValue < 10 ? "0" : "")
                .append(monthValue)
                .append(dayValue < 10 ? "-0" : "-")
                .append(dayValue)
                .toString();
        System.out.println(s);
    }


    @Test
    public void fileTypeTest(){
        String url = "https://statics.xiumi.us/stc/images/templates-assets/tpl-paper/image/94531d0b2bb32e0cd6cd91a493998fa2-sz_634246.png";
        try (InputStream inputStream = new URL(url).openStream();){
            String type = FileTypeUtil.getType(inputStream);
            System.out.println(type);
        }catch (IOException e) {
            System.out.println("获取图片流失败"+e);
        }
    }


    @Test
    public void  testGson(){

        Gson gson = GsonUtils.getGson();

        GsonBeans gsonBeans = new GsonBeans();
        gsonBeans.setId(System.currentTimeMillis());
        gsonBeans.setTime(new Date());
        gsonBeans.setCreateTime(new Date());
        String s = gson.toJson(gsonBeans);
        System.out.println(s);

        GsonBeans gsonBeans1 = gson.fromJson(s, GsonBeans.class);
        System.out.println(gsonBeans1);


    }
}
