package work.moonzs.controller.system;

import org.springframework.web.bind.annotation.*;
import work.moonzs.domain.ResponseResult;

/**
 * @author Moondust月尘
 */
@RestController("SystemResourceC")
@RequestMapping("/system/resource")
public class ResourceController {

    // TODO
    @GetMapping("/list")
    public ResponseResult<?> listResources() {
        return null;
    }


    // TODO
    @PostMapping
    public ResponseResult<?> addResource() {
        return null;
    }


    // TODO
    @PutMapping
    public ResponseResult<?> updateResource() {
        return null;
    }

    // TODO
    @DeleteMapping("/{id}")
    public ResponseResult<?> deleteResource(@PathVariable(value = "id") Long resourceId) {
        return null;
    }
}
