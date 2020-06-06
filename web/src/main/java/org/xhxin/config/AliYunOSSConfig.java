package org.xhxin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xhxin.common.util.OssBootUtil;

@Data
@Configuration
@ConfigurationProperties(prefix = "ali-yun.oss")
public class AliYunOSSConfig {
    private String bucketName;
    private String endpoint;
    private String keyId;
    private String keySecret;
    private String fileHost;


    @Bean
    public void initOSSConfig(){
        OssBootUtil.setBucketName(bucketName);
        OssBootUtil.setEndPoint(endpoint);
        OssBootUtil.setAccessKeyId(keyId);
        OssBootUtil.setAccessKeySecret(keySecret);
        OssBootUtil.setStaticDomain(fileHost);
    }
}