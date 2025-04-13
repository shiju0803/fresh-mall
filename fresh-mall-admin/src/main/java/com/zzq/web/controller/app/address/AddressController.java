package com.zzq.web.controller.app.address;

import com.zzq.address.domain.bo.AddressBo;
import com.zzq.address.domain.vo.AddressVo;
import com.zzq.common.annotation.RepeatSubmit;
import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.domain.R;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.core.validate.AddGroup;
import com.zzq.common.core.validate.EditGroup;
import com.zzq.web.controller.app.address.service.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 用户下单地址
 * @date 2023-04-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/address/app")
public class AddressController extends BaseAppController {

    private final IAddressService iAddressService;

    /**
     * 查询用户下单地址列表
     */
    @GetMapping("/list")
    public TableDataInfo<AddressVo> list(AddressBo bo, PageQuery pageQuery) {
        return iAddressService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询用户下单地址列表
     */
    @GetMapping("/getAllAddress")
    public R<List<AddressVo>> getAllAddress() {
        AddressBo bo = new AddressBo();
        bo.setUserId(getAppLoginUser().getUserId());
        return R.ok(iAddressService.queryList(bo));
    }

    /**
     * 获取用户下单地址详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<AddressVo> getInfo(@NotNull(message = "主键不能为空")
                                  @PathVariable Long id) {
        return R.ok(iAddressService.queryById(id));
    }

    /**
     * 获取默认地址
     */
    @GetMapping("/getDefAddress")
    public R<AddressVo> getDefAddress() {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(iAddressService.getDefAddress(userId));
    }

    /**
     * 新增用户下单地址
     */
    @RepeatSubmit()
    @PostMapping("/addAddress")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody AddressBo bo) {
        bo.setUserId(getAppLoginUser().getUserId());
        return toAjax(iAddressService.insertByBo(bo));
    }

    /**
     * 修改用户下单地址
     */
    @RepeatSubmit()
    @PostMapping("/updateAddress")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody AddressBo bo) {
        bo.setUserId(getAppLoginUser().getUserId());
        return toAjax(iAddressService.updateByBo(bo));
    }

    /**
     * 删除地址
     */
    @RepeatSubmit()
    @PostMapping("/deleteAddress")
    public R<Void> deleteAddress(@Validated(EditGroup.class) @RequestBody AddressBo bo) {
        bo.setUserId(getAppLoginUser().getUserId());
        return toAjax(iAddressService.deleteAddress(bo));
    }

    /**
     * 删除用户下单地址
     *
     * @param ids 主键串
     */
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iAddressService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
