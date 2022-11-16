package work.moonzs.domain;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import work.moonzs.base.enums.AppHttpCodeEnum;
import work.moonzs.base.exception.BaseException;
import work.moonzs.domain.vo.PageVO;

import java.util.HashMap;

/**
 * 封装前端响应实体类
 *
 * @author Moondust月尘
 * @date 2022/10/23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult extends HashMap<String, Object> {
    /**
     * 返回状态码
     */
    public static final String CODE_TAG = "code";
    /**
     * 返回消息体
     */
    public static final String MSG_TAG = "msg";
    /**
     * 返回数据
     */
    public static final String DATA_TAG = "data";


    /**
     * 创建一个空消息响应
     */
    private ResponseResult() {

    }

    private ResponseResult(Integer code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 自定义消息
     *
     * @param responseEnum 响应枚举
     */
    private ResponseResult(AppHttpCodeEnum responseEnum) {
        super.put(CODE_TAG, responseEnum.getCode());
        super.put(MSG_TAG, responseEnum.getMsg());
    }

    private ResponseResult(AppHttpCodeEnum responseEnum, Object data) {
        super.put(CODE_TAG, responseEnum.getCode());
        super.put(MSG_TAG, responseEnum.getMsg());
        // 本来不知道为啥要加这个，知道想到，如果我data为空，取而代之的是其他数据字段，那么这个就可以用了，比如token
        if (ObjectUtil.isNotEmpty(data)) {
            super.put(DATA_TAG, data);
        }
    }

    private ResponseResult(Integer code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (ObjectUtil.isNotEmpty(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 自定义返回消息
     */
    public static ResponseResult result(AppHttpCodeEnum responseEnum) {
        return new ResponseResult(responseEnum);
    }

    public static ResponseResult result(Integer code, String msg, Object data) {
        return new ResponseResult(code, msg, data);
    }

    /**
     * 成功消息
     *
     * @return {@link ResponseResult}
     */
    public static ResponseResult success() {
        return success(AppHttpCodeEnum.SUCCESS);
    }

    public static ResponseResult success(Object data) {
        return success(AppHttpCodeEnum.SUCCESS, data);
    }

    public static ResponseResult success(AppHttpCodeEnum responseEnum) {
        return success(responseEnum, null);
    }

    public static ResponseResult success(AppHttpCodeEnum responseEnum, Object data) {
        return new ResponseResult(responseEnum, data);
    }

    /**
     * 返回分页形式的成功消息
     *
     * @param pageVO 页签证官
     * @return {@link ResponseResult}
     */
    public static ResponseResult successPageVO(PageVO<?> pageVO) {
        // try {
        //     ResponseResult success = success();
        //     Field[] declaredFields = PageVO.class.getDeclaredFields();
        //     for (Field field : declaredFields) {
        //         field.setAccessible(true);
        //         success.put(field.getName(), field.get(pageVO));
        //     }
        //     return success;
        // } catch (IllegalAccessException e) {
        //     e.printStackTrace();
        //     return fail();
        // }
        // 反射太慢了，性能不好，虽然不容易错，这里直接写死
        return success()
                .put("records", pageVO.getRecords())
                .put("total", pageVO.getTotal());
    }

    /**
     * 失败消息
     *
     * @return {@link ResponseResult}
     */
    public static ResponseResult fail() {
        return fail(AppHttpCodeEnum.SERVER_INNER_ERR);
    }

    public static ResponseResult fail(AppHttpCodeEnum responseEnum) {
        return fail(responseEnum, null);
    }

    public static ResponseResult fail(AppHttpCodeEnum responseEnum, Object data) {
        return new ResponseResult(responseEnum, data);
    }

    public static ResponseResult fail(Integer code, String msg) {
        return new ResponseResult(code, msg, null);
    }

    /**
     * 方便链式调用，也可以自己添加
     *
     * @param key   key
     * @param value value
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 构造一个异常且带数据的API返回
     *
     * @return {@link ResponseResult}
     */
    public static <T extends BaseException> ResponseResult ofException(T t, Object data) {
        return new ResponseResult(t.getCode(), t.getMsg(), data);
    }

    public static <T extends BaseException> ResponseResult ofException(T t) {
        return ofException(t, null);
    }
}
