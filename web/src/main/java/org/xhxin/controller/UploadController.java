package org.xhxin.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xhxin.common.ResultMsg;
import org.xhxin.common.util.OssBootUtil;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/upload")
public class UploadController {

    @PostMapping("oss")
    public ResultMsg<List<String>> uploadToOSS(@RequestParam MultipartFile[] files){
        ResultMsg<List<String>> result = new ResultMsg<>();
        result.setSuccess("success");
        List<String> data = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            String url = OssBootUtil.upload(file,"upload/test");
            data.add(url);
        }
        result.setData(data);
        return result;
    }

}
