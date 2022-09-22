package work.moonzs.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import work.moonzs.enums.AppHttpCodeEnum;

/**
 * 封装前端响应实体类
 *
 * @author Moondust月尘
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    /**
     * 返回数据
     */
    private T data;
    /**
     * 返回消息体
     */
    private String msg;
    /**
     * 返回状态码
     */
    private Integer code;

    /**
     * 自定义消息
     *
     * @param responseEnum 响应枚举
     * @return {@link ResponseResult}<{@link T}>
     */
    private static <T> ResponseResult<T> rspMsg(AppHttpCodeEnum responseEnum) {
        ResponseResult<T> message = new ResponseResult<>();
        message.setCode(responseEnum.getCode());
        message.setMsg(responseEnum.getMsg());
        return message;
    }

    /**
     * 成功消息
     *
     * @return {@link ResponseResult}<{@link T}>
     */
    public static <T> ResponseResult<T> success() {
        return rspMsg(AppHttpCodeEnum.SUCCESS);
    }

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> rspMsg = success();
        rspMsg.setData(data);
        return rspMsg;
    }

    public static <T> ResponseResult<T> success(AppHttpCodeEnum responseEnum) {
        return rspMsg(responseEnum);
    }

    /**
     * 失败消息
     *
     * @return {@link ResponseResult}<{@link T}>
     */
    public static <T> ResponseResult<T> fail() {
        return rspMsg(AppHttpCodeEnum.SERVER_INNER_ERR);
    }

    public static <T> ResponseResult<T> fail(AppHttpCodeEnum responseEnum) {
        return rspMsg(responseEnum);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
