package com.example.picturesearch.utils;

import com.example.picturesearch.constant.CommonConstants;

import java.io.Serializable;

public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    private T data;

    public static <T> R<T> ok() {
        return restResult(null, CommonConstants.SUCCESS, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, CommonConstants.SUCCESS, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, CommonConstants.SUCCESS, msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, CommonConstants.FAIL,null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, CommonConstants.FAIL, msg);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, CommonConstants.FAIL, null);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, CommonConstants.FAIL, msg);
    }

    static <T> R<T> restResult(T data, int code, String msg) {
        R<T> apiResult = new R();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public static <T> R.RBuilder<T> builder() {
        return new R.RBuilder();
    }

    public String toString() {
        return "R(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }

    public R() {
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public int getCode() {
        return this.code;
    }

    public R<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return this.message;
    }

    public R<T> setMsg(String msg) {
        this.message = msg;
        return this;
    }

    public T getData() {
        return this.data;
    }

    public R<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static class RBuilder<T> {
        private int code;
        private String message;
        private T data;

        RBuilder() {
        }

        public R.RBuilder<T> code(int code) {
            this.code = code;
            return this;
        }

        public R.RBuilder<T> msg(String msg) {
            this.message = msg;
            return this;
        }

        public R.RBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public R<T> build() {
            return new R(this.code, this.message, this.data);
        }

        public String toString() {
            return "R.RBuilder(code=" + this.code + ", message=" + this.message + ", data=" + this.data + ")";
        }
    }
}