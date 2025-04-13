package com.zzq.web.controller.admin.system.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.system.domain.bo.SysExpressBo;
import com.zzq.system.domain.vo.SysExpressVo;

import java.util.Collection;
import java.util.List;

/**
 * 快递公司Service接口
 * @date 2023-02-17
 */
public interface ISysExpressService {

    /**
     * 查询快递公司
     */
    SysExpressVo queryById(Long id);

    /**
     * 查询快递公司列表
     */
    TableDataInfo<SysExpressVo> queryPageList(SysExpressBo bo, PageQuery pageQuery);

    /**
     * 查询快递公司列表
     */
    List<SysExpressVo> queryList(SysExpressBo bo);

    /**
     * 新增快递公司
     */
    Boolean insertByBo(SysExpressBo bo);

    /**
     * 修改快递公司
     */
    Boolean updateByBo(SysExpressBo bo);

    /**
     * 校验并批量删除快递公司信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
