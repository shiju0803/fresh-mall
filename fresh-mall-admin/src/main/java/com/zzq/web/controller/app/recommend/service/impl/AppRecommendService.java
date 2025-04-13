package com.zzq.web.controller.app.recommend.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.zzq.common.core.domain.entity.SysDictData;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.recommend.domain.vo.RecommendVo;
import com.zzq.recommend.mapper.RecommendMapper;
import com.zzq.system.mapper.SysDictDataMapper;
import com.zzq.web.controller.app.recommend.service.IAppRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AppRecommendService implements IAppRecommendService {

    private final RecommendMapper baseMapper;

    private final SysDictDataMapper dictDataMapper;

    @Override
    public TableDataInfo<RecommendVo> getRecommendByType(Long storageId, Integer recommendType, Integer pageNo, Integer pageSize) {
//        //缓存key
//        String keyCache = RECOMMEND_NAME + recommendType + "_" + storageId + "_" + pageNo + "_" + pageSize;
//        //若关键字为空，尝试从缓存取列表
//        TableDataInfo<RecommendVo> objFromCache = RedisUtils.getCacheObject(keyCache);
//        if (objFromCache != null) {
//            return objFromCache;
//        }
        Integer offset = (pageNo - 1) * pageSize;
        Integer size = pageSize;
        List<RecommendVo> recommendDTOList = baseMapper.getRecommendByStorage(storageId, recommendType, offset, size);
        Long count = baseMapper.getRecommendByStorageCount(storageId, recommendType);
        TableDataInfo<RecommendVo> tableDataInfo = new TableDataInfo<>(recommendDTOList, count);
//        if (!CollectionUtils.isEmpty(recommendDTOList)) {
//            RedisUtils.setCacheObject(keyCache, tableDataInfo);
//        }
        return tableDataInfo;
    }

    @Override
    public List<SysDictData> getRecommendTypeEnums() {
        List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType("recommend_type");
        dictDatas.sort((me1, me2) -> me2.getDictValue().compareTo(me1.getDictValue()));
        if (CollUtil.isNotEmpty(dictDatas)) {
            return dictDatas;
        }
        return null;
    }
}
