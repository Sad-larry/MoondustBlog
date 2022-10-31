package work.moonzs;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import work.moonzs.domain.entity.Menu;
import work.moonzs.service.MenuService;

import java.util.List;

/**
 * @author Moondust月尘
 */
@SpringBootTest
public class MenuTest {
    @Autowired
    private MenuService menuService;

    @Test
    public void test1() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getType, 'M');
        List<Menu> list = menuService.list(queryWrapper);
        List<Menu> collect = list.stream().map(menu -> {
            menu.setName(menu.getUrl().substring(1));
            return menu;
        }).toList();
        boolean b = menuService.updateBatchById(collect);
        if (b) {
            System.out.println("ok");
        }
    }
}
