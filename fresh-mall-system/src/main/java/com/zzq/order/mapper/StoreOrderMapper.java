package com.zzq.order.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zzq.common.core.mapper.BaseMapperPlus;
import com.zzq.dashboard.domain.SalesStatementDTO;
import com.zzq.dashboard.domain.SalesTopDTO;
import com.zzq.order.domain.StoreOrder;
import com.zzq.order.domain.vo.OrderStatisticalVo;
import com.zzq.order.domain.vo.StoreOrderVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 订单Mapper接口
 */
public interface StoreOrderMapper extends BaseMapperPlus<StoreOrderMapper, StoreOrder, StoreOrderVo> {

    List<StoreOrderVo> selectOrderPages(@Param("status") List<Integer> status, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("userId") Long userId);

    Long countOrders(@Param("status") List<Integer> status, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("userId") Long userId);


    public List<String> selectExpireOrderNos(@Param("status") Integer status, @Param("time") Date time);




    /**
     * 获取一级类目销量排名
     *
     * @return
     */
    List<SalesStatementDTO> getSalesCategoryRank(@Param("storageId") Long storageId, @Param("storageIds") Set<Long> storageIds);

    /**
     * 获取商品销量排名
     *
     * @return
     */
    List<SalesTopDTO> getSalesCategoryRanTopFive(@Param("categoryId") Long categoryId, @Param("storageId") Long storageId, @Param("storageIds") Set<Long> storageIds);

    /**
     * 今日销量
     *
     * @return
     */
    SalesStatementDTO getTodaySales(@Param("storageId") Long storageId, @Param("storageIds") Set<Long> storageIds);

    /**
     * 昨日销量
     *
     * @return
     */
    SalesStatementDTO getYesterdaySales(@Param("storageId") Long storageId, @Param("storageIds") Set<Long> storageIds);

    /**
     * 按两小时统计销量
     *
     * @return
     */
    SalesStatementDTO getSalesByHour(@Param("storageId") Long storageId, @Param("start") Date start, @Param("end") Date end, @Param("storageIds") Set<Long> storageIds);

    /**
     * 计算某个仓库的统计
     * @param storageId
     * @return
     */
    OrderStatisticalVo statistical(@Param("storageId") Long storageId);


    @Select("SELECT IFNULL(sum(pay_price),0) " +
            " FROM kx_store_order ${ew.customSqlSegment}")
    Double todayPrice(@Param(Constants.WRAPPER) Wrapper<StoreOrder> wrapper);

    @Select({
            "<script>",
            "SELECT IFNULL(SUM(pay_price), 0)",
            "FROM kx_store_order",
            "WHERE status IN (40, 50)",
            "AND is_del = 0",
            "AND pay_time IS NOT NULL",
            "<if test='storageId != null'>",
            "AND store_id = #{storageId}",
            "</if>",
            "</script>"
    })
    Double sumTotalPrice(@Param("storageId") Long storageId);
}
