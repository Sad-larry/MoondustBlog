package work.moonzs.domain;

/**
 * @author Moondust月尘
 * 封装前端响应实体类
 */
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
}
