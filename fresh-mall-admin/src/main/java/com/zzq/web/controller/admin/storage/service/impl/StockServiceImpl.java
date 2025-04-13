package com.zzq.web.controller.admin.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.QRCodeGenerator;
import com.zzq.product.domain.StoreCategory;
import com.zzq.product.mapper.StoreCategoryMapper;
import com.zzq.storage.domain.Stock;
import com.zzq.storage.domain.bo.StockBo;
import com.zzq.storage.domain.bo.WarningStockBo;
import com.zzq.storage.domain.vo.StockVo;
import com.zzq.storage.mapper.StockMapper;
import com.zzq.web.controller.admin.storage.service.IStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 前置仓商品Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StockServiceImpl implements IStockService {

    private final StockMapper baseMapper;

    private final StoreCategoryMapper categoryMapper;


    @Value("${com.zzq.domainName}")
    private String domainName;

    /**
     * 查询前置仓商品
     */
    @Override
    public StockVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询前置仓商品列表
     */
    @Override
    public TableDataInfo<StockVo> queryPageList(StockBo bo, PageQuery pageQuery) {
        //LambdaQueryWrapper<Stock> lqw = buildQueryWrapper(bo);
        Integer offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        Integer size = pageQuery.getPageSize();
        List<StockVo> result = baseMapper.selectVoBySQL(offset,size,bo.getStorageId(),bo.getCategoryId()
            ,bo.getKeyword(),bo.getStatus(),bo.getNotIds(),bo.getStorageIds());
        Long count = baseMapper.selectVoBySQLCount(bo.getStorageId(),bo.getCategoryId()
            ,bo.getKeyword(),bo.getStatus(),bo.getNotIds(),bo.getStorageIds());
        return new TableDataInfo<StockVo>(result,count);
    }

    /**
     * 查询前置仓商品列表
     */
    @Override
    public List<StockVo> queryList(StockBo bo) {
        LambdaQueryWrapper<Stock> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Stock> buildQueryWrapper(StockBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Stock> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, Stock::getProductId, bo.getProductId());
        lqw.eq(bo.getProductAttrId() != null, Stock::getProductAttrId, bo.getProductAttrId());
        lqw.eq(bo.getStorageId() != null, Stock::getStorageId, bo.getStorageId());
        lqw.eq(bo.getStatus() != null, Stock::getStatus, bo.getStatus());
        lqw.eq(bo.getStock() != null, Stock::getStock, bo.getStock());
        lqw.eq(bo.getSales() != null, Stock::getSales, bo.getSales());
        lqw.eq(bo.getFrezzStock() != null, Stock::getFrezzStock, bo.getFrezzStock());
        lqw.eq(bo.getPrice() != null, Stock::getPrice, bo.getPrice());
        lqw.eq(bo.getWarningNum() != null, Stock::getWarningNum, bo.getWarningNum());
        return lqw;
    }

    /**
     * 新增前置仓商品
     */
    @Override
    public Boolean insertByBo(StockBo bo) {
        Stock add = BeanUtil.toBean(bo, Stock.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改前置仓商品
     */
    @Override
    public Boolean updateByBo(StockBo bo) {
        Stock update = BeanUtil.toBean(bo, Stock.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Stock entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除前置仓商品
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean freezeOrActivation(StockBo bo) {
        Stock add = new Stock();
        add.setStatus(bo.getStatus());
        add.setUpdateTime(new Date());
        add.setId(bo.getId());
        return baseMapper.update(add, new LambdaQueryWrapper<Stock>().eq(Stock::getId, bo.getId())) > 0;
    }

    @Override
    public Boolean updateByStock(StockBo bo) {
        return null;
    }

    @Override
    public Boolean updatePrice(StockBo bo) {
        Stock update = new Stock();
        update.setUpdateTime(new Date());
        update.setPrice(bo.getPrice());
        update.setId(bo.getId());
        return baseMapper.update(update, new LambdaQueryWrapper<Stock>().eq(Stock::getId, bo.getId())) > 0;
    }

    @Override
    public TableDataInfo<StockVo> queryPageWarningList(WarningStockBo bo, PageQuery pageQuery) {
        //类目查询
        LinkedList<Long> childrenIds = new LinkedList<>();
        if (bo.getCategoryId() != null && bo.getCategoryId() != 0L) {
            List<StoreCategory> childrenList = categoryMapper.selectList(new LambdaQueryWrapper<StoreCategory>().eq(StoreCategory::getPid, bo.getCategoryId()));
            if (!CollectionUtils.isEmpty(childrenList)) {
                //一级分类
                childrenList.forEach(item -> childrenIds.add(item.getId()));
                //使用in查询，就不需要等于查询
                bo.setCategoryId(null);
            }
        }
        Integer offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        Integer size = pageQuery.getPageSize();
        //查询制定页数的库存预警
        List<StockVo> result = baseMapper.warningListByStoragePage(offset,size, bo);
        Long count = baseMapper.warningListByStoragePageCount(bo);
        return new TableDataInfo<StockVo>(result,count);
    }

    @Override
    public Boolean warningUpdate(WarningStockBo bo) {
        Stock update = new Stock();
        update.setUpdateTime(new Date());
        update.setWarningNum(bo.getNum());
        return baseMapper.update(update, new LambdaQueryWrapper<Stock>()
                                        .eq(Stock::getStorageId, bo.getStorageId())
                                        .eq(Stock::getProductId,bo.getProductId())
                                        .eq(Stock::getProductAttrId,bo.getProductAttrId())) > 0;
    }

    @Override
    public String warehouseCode(WarningStockBo bo) {
        String base64QRCode;
        try {
            base64QRCode = QRCodeGenerator.generateQRCodeBase64(domainName+"?storageId="+bo.getStorageId(), 350, 350);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(),500);
        }
        return base64QRCode;
    }


}
