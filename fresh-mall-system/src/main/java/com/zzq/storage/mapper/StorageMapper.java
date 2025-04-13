package com.zzq.storage.mapper;

import com.zzq.common.core.mapper.BaseMapperPlus;
import com.zzq.storage.domain.Storage;
import com.zzq.storage.domain.vo.StorageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 仓库管理Mapper接口
 */
public interface StorageMapper extends BaseMapperPlus<StorageMapper, Storage, StorageVo> {


    /**
     * 批量更新前置仓库资料状态
     *
     * @param ids   前置仓库主键集合
     * @param state 前置仓库资料状态
     * @return 影响行数
     */
    Integer batchUpdateState(@Param("ids") List<Long> ids, @Param("state") int state);

    /**
     * 批量更新前置仓库资料营业状态
     *
     * @param ids            前置仓库主键集合
     * @param operatingState 前置仓库资料营业状态
     * @return 影响行数
     */
    Integer batchUpdateOperatingState(@Param("ids") List<Long> ids, @Param("operatingState") int operatingState);

    /**
     * 获取所有仓库的名称
     * @param state
     * @param storageIds
     * @return
     */
    List<StorageVo> getStorageNameAll(int state, @Param("storageIds") Set<Long> storageIds);

}
