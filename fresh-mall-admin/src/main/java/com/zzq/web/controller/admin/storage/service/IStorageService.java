package com.zzq.web.controller.admin.storage.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.storage.domain.bo.StorageBo;
import com.zzq.storage.domain.vo.StorageVo;

import java.util.Collection;
import java.util.List;

/**
 * 仓库管理Service接口
 */
public interface IStorageService {

    /**
     * 查询仓库管理
     */
    StorageVo queryById(Long id);

    /**
     * 查询仓库管理列表
     */
    TableDataInfo<StorageVo> queryPageList(StorageBo bo, PageQuery pageQuery);

    /**
     * 查询仓库管理列表
     */
    List<StorageVo> queryList(StorageBo bo);

    /**
     * 新增仓库管理
     */
    Boolean insertByBo(StorageBo bo);

    /**
     * 修改仓库管理
     */
    Boolean updateByBo(StorageBo bo);

    /**
     * 校验并批量删除仓库管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 前置仓状态批量更新为正常
     * @param bo
     * @return
     */
    Boolean updateStateToNomral(StorageBo bo);

    Boolean updateStateToAbort(StorageBo bo);

    /**
     * 前置仓营业状态批量更新为营业中
     * @param bo
     * @return
     */
    Boolean updateBusinessStateToOpen(StorageBo bo);

    /**
     * 前置仓营业状态批量更新为休息中
     * @param bo
     * @return
     */
    Boolean updateBusinessStateToRest(StorageBo bo);

    List<StorageVo> options();

    /**
     * 获取指定仓库的推送订阅二维码
     * @return
     */
    String getStorageQrcodeImage(Long storageId);

    String printTest(StorageBo bo);
}
