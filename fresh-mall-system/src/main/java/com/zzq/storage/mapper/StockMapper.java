package com.zzq.storage.mapper;

import com.zzq.common.core.mapper.BaseMapperPlus;
import com.zzq.storage.domain.Stock;
import com.zzq.storage.domain.bo.WarningStockBo;
import com.zzq.storage.domain.vo.StockVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 前置仓商品Mapper接口
 */
public interface StockMapper extends BaseMapperPlus<StockMapper, Stock, StockVo> {

    /**
     * 查询预警信息
     * @param build
     * @param bo
     * @return
     */
    List<StockVo> warningListByStoragePage(@Param("offset") Integer offset, @Param("size") Integer size, @Param("bo") WarningStockBo bo);

    /**
     * 警告列表
     * @param bo
     * @return
     */
    Long warningListByStoragePageCount(@Param("bo") WarningStockBo bo);

    /**
     * 更新库存
     * @param storageId
     * @param productAttrId
     * @param inStockNum
     * @return
     */
    Integer updateSockForAdd(@Param("storageId") Long storageId, @Param("productAttrId") Long productAttrId, @Param("stockNum") Long inStockNum);

    /**
     * 出库
     * @param storageId
     * @param productAttrId
     * @param outStockNum
     * @return
     */
    Integer updateSock(@Param("storageId") Long storageId, @Param("productAttrId") Long productAttrId, @Param("stockNum") Long outStockNum);

    /**
     * 分页查询
     * @param offset
     * @param size
     * @param storageId
     * @param categoryId
     * @param keyword
     * @param status
     * @param notIds
     * @param storageIds
     * @return
     */
    List<StockVo> selectVoBySQL(@Param("offset") Integer offset,
                                @Param("size") Integer size,
                                @Param("storageId") Long storageId,
                                @Param("categoryId") Long categoryId,
                                @Param("keyword") String keyword,
                                @Param("status") Integer status,
                                @Param("notIds") List<Long> notIds,
                                @Param("storageIds") Set<Long> storageIds);

    /**
     * 计算总数
     * @param storageId
     * @param categoryId
     * @param keyword
     * @param status
     * @param notIds
     * @param storageIds
     * @return
     */
    Long selectVoBySQLCount(@Param("storageId") Long storageId,
                            @Param("categoryId") Long categoryId,
                            @Param("keyword") String keyword,
                            @Param("status") Integer status,
                            @Param("notIds") List<Long> notIds,
                            @Param("storageIds") Set<Long> storageIds);
}
