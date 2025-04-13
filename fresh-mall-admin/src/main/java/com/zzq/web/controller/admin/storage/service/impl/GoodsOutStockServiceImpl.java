package com.zzq.web.controller.admin.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.enums.GoodsInStockType;
import com.zzq.common.enums.StorageStatusType;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.helper.LoginHelper;
import com.zzq.common.utils.StringUtils;
import com.zzq.storage.domain.GoodsOutStock;
import com.zzq.storage.domain.OutStockProduct;
import com.zzq.storage.domain.Storage;
import com.zzq.storage.domain.bo.GoodsOutStockBo;
import com.zzq.storage.domain.vo.GoodsOutStockVo;
import com.zzq.storage.domain.vo.OutStockProductVo;
import com.zzq.storage.domain.vo.StorageVo;
import com.zzq.storage.mapper.GoodsOutStockMapper;
import com.zzq.storage.mapper.OutStockProductMapper;
import com.zzq.storage.mapper.StockMapper;
import com.zzq.storage.mapper.StorageMapper;
import com.zzq.web.controller.admin.storage.service.IGoodsOutStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品出库Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class GoodsOutStockServiceImpl implements IGoodsOutStockService {

    private final GoodsOutStockMapper baseMapper;
    private final StorageMapper storageMapper;
    private final OutStockProductMapper outStockProductMapper;
    private final StockMapper stockMapper;

    /**
     * 查询商品出库
     */
    @Override
    public GoodsOutStockVo queryById(Long id) {
        GoodsOutStockVo kxGoodsOutStockVo = baseMapper.selectVoById(id);
        List<OutStockProductVo> outStockProductVos = outStockProductMapper.selectVoList(new LambdaQueryWrapper<OutStockProduct>().eq(OutStockProduct::getOutStockNumbers, kxGoodsOutStockVo.getOutStockNumbers()));
        kxGoodsOutStockVo.setOutStockProductVoList(outStockProductVos);
        return kxGoodsOutStockVo;
    }

    /**
     * 查询商品出库列表
     */
    @Override
    public TableDataInfo<GoodsOutStockVo> queryPageList(GoodsOutStockBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<GoodsOutStock> lqw = buildQueryWrapper(bo);
        Page<GoodsOutStockVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<Storage> storages = storageMapper.selectList();
        result.getRecords().stream()
            .filter(record -> storages.stream().anyMatch(storage -> storage.getId().equals(record.getStorageId())))
            .forEach(record -> record.setStorageName(storages.stream()
                .filter(storage -> storage.getId().equals(record.getStorageId()))
                .findFirst()
                .map(Storage::getName)
                .orElse(null)));
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品出库列表
     */
    @Override
    public List<GoodsOutStockVo> queryList(GoodsOutStockBo bo) {
        LambdaQueryWrapper<GoodsOutStock> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<GoodsOutStock> buildQueryWrapper(GoodsOutStockBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<GoodsOutStock> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getStorageId() != null, GoodsOutStock::getStorageId, bo.getStorageId());
        lqw.eq(StringUtils.isNotBlank(bo.getOutStockNumbers()), GoodsOutStock::getOutStockNumbers, bo.getOutStockNumbers());
        lqw.eq(bo.getStates() != null, GoodsOutStock::getStates, bo.getStates());
        lqw.eq(StringUtils.isNotBlank(bo.getOutgoingPerson()), GoodsOutStock::getOutgoingPerson, bo.getOutgoingPerson());
        if(bo.getOutgoingTime() != null){
            lqw.between(bo.getOutgoingTime() != null, GoodsOutStock::getOutgoingTime, bo.getOutgoingTime().atStartOfDay(),bo.getOutgoingTime().atTime(LocalTime.MAX));
        }
        lqw.eq(StringUtils.isNotBlank(bo.getRemarks()), GoodsOutStock::getRemarks, bo.getRemarks());
        lqw.eq(StringUtils.isNotBlank(bo.getOutgoingDay()), GoodsOutStock::getOutgoingDay, bo.getOutgoingDay());
        lqw.in(CollectionUtils.isNotEmpty(bo.getStorageIds()), GoodsOutStock::getStorageId, bo.getStorageIds());
        lqw.orderByDesc(GoodsOutStock::getId);
        return lqw;
    }

    /**
     * 新增商品出库
     */
    @Override
    public Boolean insertByBo(GoodsOutStockBo bo) {
        //自动生成入库单号,O+年月日+流水号
        //查询数据库最新生成的编号
        GoodsOutStock selectByMax = baseMapper.selectByMax();
        String max_code = "";//定义数据库的截取的数据
        String out_skock = "";//定义拼接好的字符串
        if (selectByMax != null) {
            max_code = selectByMax.getOutStockNumbers();
        }
        //定义时间字符串拼接
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String uid_pfix = simpleDateFormat.format(new Date());
        //判断数据库是否有数据
        if (max_code != null && max_code.contains(uid_pfix)) {
            String uid_end = max_code.substring(9, 14);
            Integer endNum = Integer.parseInt(uid_end);
            //100001
            endNum = 100000 + endNum + 1;
            String num = endNum + "";
            //去掉100001中的首位1
            String numm = num.substring(1);
            out_skock = "I" + uid_pfix + numm;
        } else {
            //数据库没数据时
            out_skock = "I" + uid_pfix + "00001";
        }
        //入库商品加入数据库
        List<OutStockProductVo> productVoList = bo.getOutStockProductVoList();
        if (!CollectionUtils.isEmpty(productVoList)) {
            for (OutStockProductVo stockProductVo : productVoList) {
                stockProductVo.setOutStockNumbers(out_skock);
                OutStockProduct stockProduct = BeanUtil.toBean(stockProductVo, OutStockProduct.class);
                if (outStockProductMapper.insert(stockProduct) <= 0) {
                    throw new ServiceException("入库商品添加失败");
                }
            }
        }
        //入库添加
        GoodsOutStock goodsInStock = BeanUtil.toBean(bo, GoodsOutStock.class);
        goodsInStock.setOutStockNumbers(out_skock);
        goodsInStock.setStates(GoodsInStockType.TO_BE_FOR_STOCK.getCode());
        goodsInStock.setUpdateTime(new Date());
        if (baseMapper.insert(goodsInStock) <= 0) {
            throw new ServiceException("管理员系统未知异常");
        }
        return true;

    }

    /**
     * 修改商品出库
     */
    @Override
    public Boolean updateByBo(GoodsOutStockBo bo) {
        LambdaQueryWrapper<OutStockProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(OutStockProduct::getOutStockNumbers, bo.getOutStockNumbers());
        if (outStockProductMapper.delete(wrapper) <= 0) {
            throw new ServiceException("出库商品更新失败");
        }
        //出库商品加入数据库
        List<OutStockProductVo> stockProductVoList = bo.getOutStockProductVoList();
        if (!CollectionUtils.isEmpty(stockProductVoList)) {
            for (OutStockProductVo goodsOutStockVo : stockProductVoList) {
                OutStockProduct stockProduct = BeanUtil.toBean(goodsOutStockVo, OutStockProduct.class);
                if (outStockProductMapper.insert(stockProduct) <= 0) {
                    throw new ServiceException("出库商品添加失败");
                }
            }
        }
        GoodsOutStock goodsOutStock = BeanUtil.toBean(bo, GoodsOutStock.class);
        goodsOutStock.setUpdateTime(new Date());
        if (baseMapper.updateById(goodsOutStock) > 0) {
            return true;
        }
        throw new ServiceException("管理员系统未知异常");

    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(GoodsOutStock entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品出库
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        List<GoodsOutStockVo> goodsOutStockVos = baseMapper.selectVoBatchIds(ids);
        //删除入库信息
        if (baseMapper.deleteBatchIds(ids) <= 0) {
            throw new ServiceException("出库商品删除失败");
        }
        for (GoodsOutStockVo outStockVo : goodsOutStockVos) {
            //批量删除入库商品
            LambdaQueryWrapper<OutStockProduct> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(OutStockProduct::getOutStockNumbers, outStockVo.getOutStockNumbers());
            if (outStockProductMapper.delete(wrapper) <= 0) {
                throw new ServiceException("出库商品删除失败");
            }
        }
        return true;
    }

    @Override
    public Boolean updateOutOfStock(GoodsOutStockBo bo) {
        if (ObjectUtils.isEmpty(bo.getStorageId()) && StringUtils.isEmpty(bo.getOutStockNumbers())) {
            throw new ServiceException("参数不能为空");
        }
        //根据入库的商品数量更新仓库的数量
        LambdaQueryWrapper<OutStockProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OutStockProduct::getOutStockNumbers, bo.getOutStockNumbers());
        List<OutStockProduct> outStockProducts = outStockProductMapper.selectList(wrapper);
        Long outStockNum;//入库数量
        for (OutStockProduct outStockProduct : outStockProducts) {
            outStockNum = outStockProduct.getOutStockNum();
            Long productAttrId = outStockProduct.getProductAttrId();
            if (stockMapper.updateSock(bo.getStorageId(), productAttrId, outStockNum) <= 0) {
                throw new ServiceException("更新商品库存失败");
            }
        }
        //更新入库状态
        GoodsOutStock kxGoodsOutStock = new GoodsOutStock();
        kxGoodsOutStock.setStates(GoodsInStockType.IN_FOR_STOCK.getCode());
        kxGoodsOutStock.setOutgoingPerson(LoginHelper.getUsername());
        kxGoodsOutStock.setOutgoingTime(new Date());
        kxGoodsOutStock.setUpdateTime(new Date());
        LambdaQueryWrapper<GoodsOutStock> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(GoodsOutStock::getOutStockNumbers, bo.getOutStockNumbers());
        if (baseMapper.update(kxGoodsOutStock, wrapper1) <= 0) {
            throw new ServiceException("商品出库失败");
        }
        return true;
    }

    @Override
    public List<StorageVo> storagAllName(GoodsOutStockBo bo) {
        int state = StorageStatusType.NOMRAL.getCode();
        return storageMapper.getStorageNameAll(state, bo.getStorageIds());
    }
}
