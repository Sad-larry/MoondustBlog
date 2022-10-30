package work.moonzs.base.validate;

/**
 * VG:全称ValidateGroup，为了简化代码，虽然VG看起来有点迷惑，但是其后面还带着标识字段，绝对能看懂
 * 分组校验接口
 * 实现效果为：
 * validation注解的groups传入ValidateGroup时，表示添加、更新、删除、查询都不允许为空
 * validation注解的groups传入Insert时，表示添加时不允许为空
 * validation注解的groups传入Update时，表示修改时不允许为空（比如Id字段）
 * validation注解的groups传入Delete时，表示删除时不允许为空
 * validation注解的groups传入Select时，表示查询时不允许为空
 */
public interface VG {

    /**
     * 分组校验，用于标记新建时字段校验
     */
    interface Insert extends VG {
    }

    /**
     * 分组校验，用于标记更新时字段校验
     */
    interface Update extends VG {
    }

    /**
     * 分组校验，用于标记删除时字段校验
     */
    interface Delete extends VG {
    }

    /**
     * 分组校验，用于标记查询时字段校验
     */
    interface Select extends VG {
    }
}

