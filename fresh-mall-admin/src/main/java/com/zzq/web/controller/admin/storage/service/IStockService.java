package com.zzq.web.controller.admin.storage.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.storage.domain.bo.StockBo;
import com.zzq.storage.domain.bo.WarningStockBo;
import com.zzq.storage.domain.vo.StockVo;

import java.util.Collection;
import java.util.List;

/**
 * 前置仓商品Service接口
 */
public interface IStockService {

    /**
     * 查询前置仓商品
     */
    StockVo queryById(Long id);

    /**
     * 查询前置仓商品列表
     */
    TableDataInfo<StockVo> queryPageList(StockBo bo, PageQuery pageQuery);

    /**
     * 查询前置仓商品列表
     */
    List<StockVo> queryList(StockBo bo);

    /**
     * 新增前置仓商品
     */
    Boolean insertByBo(StockBo bo);

    /**
     * 修改前置仓商品
     */
    Boolean updateByBo(StockBo bo);

    /**
     * 校验并批量删除前置仓商品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 上下架
     * @param bo
     * @return
     */
    Boolean freezeOrActivation(StockBo bo);


    /**
     * 逻辑删除
     */
    Boolean updateByStock(StockBo bo);

    /**
     * 更新当前价格
     * @param bo
     * @return
     */
    Boolean updatePrice(StockBo bo);

    /**
     * 查询告警分页
     * @param bo
     * @param pageQuery
     * @return
     */
    TableDataInfo<StockVo> queryPageWarningList(WarningStockBo bo, PageQuery pageQuery);

    /**
     * 告警数量更新
     * @param bo
     * @return
     */
    Boolean warningUpdate(WarningStockBo bo);

    /**
     * 获取二维码
     * @param bo
     * @return
     */
    String warehouseCode(WarningStockBo bo);
}
