package org.xhxin.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ResultMsg<T> {
    public static final Integer OK = 200;
    public static final Integer ERROR = 100;

    private Integer code;
    private String message;
    private String url;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;


    public static ResultMsg<String> createResult() {
        return new ResultMsg<>();
    }

    public void setSuccess(String message) {
        this.code = OK;
        this.message = message;
    }

    public void setFail(String message) {
        this.code = ERROR;
        this.message = message;
    }
}