package org.xhxin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xhxin.common.ResultMsg;
import org.xhxin.common.util.FileTypeUtil;
import org.xhxin.common.util.IDUtils;
import org.xhxin.common.util.OssBootUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/upload")
public class UploadController {

    @PostMapping("oss")
    public ResultMsg<List<String>> uploadToOSS(@RequestParam MultipartFile[] files){
        ResultMsg<List<String>> result = new ResultMsg<>();
        result.setSuccess("success");
        List<String> data = new ArrayList<>(files.length);
        String uploadPath = getUploadPath();
        for (MultipartFile file : files) {
            String url = OssBootUtil.upload(file,uploadPath);
            data.add(url);
        }
        result.setData(data);
        return result;
    }

    @PostMapping("url/oss")
    public ResultMsg<String> urlUploadToOSS(@RequestParam String url){
        ResultMsg<String> result = new ResultMsg<>();
        result.setSuccess("success");
        String uploadPath = getUploadPath();

        try (InputStream inputStream = new URL(url).openStream();){
            String urlType = FileTypeUtil.getUrlType(url);
            String relativePath = uploadPath +"/"+ IDUtils.generateId()+ "." + urlType;
            String resultUrl = OssBootUtil.upload(inputStream,relativePath);
            result.setData(resultUrl);
        }catch (IOException e) {
            log.error("获取图片流失败：{}",url,e);
            result.setFail("获取图片流失败:"+url);
        }
        return result;
    }

    private String getUploadPath(){
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
        return buf.append(monthValue < 10 ? "0" : "")
                .append(monthValue)
                .append(dayValue < 10 ? "-0" : "-")
                .append(dayValue)
                .toString();
    }

}
