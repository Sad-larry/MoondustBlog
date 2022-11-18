package work.moonzs.base.validate;

/**
 * 扩展分组校验接口，感觉原来的VG已经不太够用了，应该在不修改原先的类基础上，扩展一下
 *
 * @author Moondust月尘
 */
public interface VG2 {
    interface Insert2 extends VG2 {
    }

    interface Update2 extends VG2 {
    }

    interface Delete2 extends VG2 {
    }

    interface Select2 extends VG2 {
    }
}
