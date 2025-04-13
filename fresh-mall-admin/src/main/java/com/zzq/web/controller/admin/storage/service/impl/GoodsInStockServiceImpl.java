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
import com.zzq.storage.domain.GoodsInStock;
import com.zzq.storage.domain.InStockProduct;
import com.zzq.storage.domain.Storage;
import com.zzq.storage.domain.bo.GoodsInStockBo;
import com.zzq.storage.domain.vo.GoodsInStockVo;
import com.zzq.storage.domain.vo.InStockProductVo;
import com.zzq.storage.domain.vo.StorageVo;
import com.zzq.storage.mapper.GoodsInStockMapper;
import com.zzq.storage.mapper.InStockProductMapper;
import com.zzq.storage.mapper.StockMapper;
import com.zzq.storage.mapper.StorageMapper;
import com.zzq.web.controller.admin.storage.service.IGoodsInStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品入库Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class GoodsInStockServiceImpl implements IGoodsInStockService {

    private final GoodsInStockMapper baseMapper;
    private final InStockProductMapper inStockProductMapper;
    private final StorageMapper storageMapper;
    private final StockMapper stockMapper;

    /**
     * 查询商品入库
     */
    @Override
    public GoodsInStockVo queryById(Long id) {
        GoodsInStockVo kxGoodsInStockVo = baseMapper.selectVoById(id);
        List<InStockProductVo> inStockProductVoList = inStockProductMapper.selectVoList(new LambdaQueryWrapper<InStockProduct>().eq(InStockProduct::getInStockNumbers, kxGoodsInStockVo.getInStockNumbers()));
        kxGoodsInStockVo.setInStockProductVoList(inStockProductVoList);
        return kxGoodsInStockVo;
    }

    /**
     * 查询商品入库列表
     */
    @Override
    public TableDataInfo<GoodsInStockVo> queryPageList(GoodsInStockBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<GoodsInStock> lqw = buildQueryWrapper(bo);
        Page<GoodsInStockVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
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
     * 查询商品入库列表
     */
    @Override
    public List<GoodsInStockVo> queryList(GoodsInStockBo bo) {
        LambdaQueryWrapper<GoodsInStock> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<GoodsInStock> buildQueryWrapper(GoodsInStockBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<GoodsInStock> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getStorageId() != null, GoodsInStock::getStorageId, bo.getStorageId());
        lqw.eq(StringUtils.isNotBlank(bo.getInStockNumbers()), GoodsInStock::getInStockNumbers, bo.getInStockNumbers());
        lqw.eq(bo.getStates() != null, GoodsInStock::getStates, bo.getStates());
        lqw.eq(StringUtils.isNotBlank(bo.getIngoingPerson()), GoodsInStock::getIngoingPerson, bo.getIngoingPerson());
        if(bo.getIngoingTime() != null){
            lqw.between(bo.getIngoingTime() != null, GoodsInStock::getIngoingTime, bo.getIngoingTime().atStartOfDay(),bo.getIngoingTime().atTime(LocalTime.MAX));
        }
        lqw.eq(StringUtils.isNotBlank(bo.getRemarks()), GoodsInStock::getRemarks, bo.getRemarks());
        lqw.eq(StringUtils.isNotBlank(bo.getOutgoingDay()), GoodsInStock::getOutgoingDay, bo.getOutgoingDay());
        lqw.in(CollectionUtils.isNotEmpty(bo.getStorageIds()), GoodsInStock::getStorageId, bo.getStorageIds());
        lqw.orderByDesc(GoodsInStock::getId);
        return lqw;
    }

    /**
     * 新增商品入库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(GoodsInStockBo bo) {
        //自动生成入库单号,O+年月日+流水号
        //查询数据库最新生成的编号
        GoodsInStock selectByMax = baseMapper.selectByMax();
        String max_code = "";//定义数据库的截取的数据
        String in_skock = "";//定义拼接好的字符串
        if (selectByMax != null) {
            max_code = selectByMax.getInStockNumbers();
        }
        //定义时间字符串拼接
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String uid_pfix = simpleDateFormat.format(new Date());
        //判断数据库是否有数据
        if (max_code != null && max_code.contains(uid_pfix)) {
            String uid_end = max_code.substring(9, 14);
            int endNum = Integer.parseInt(uid_end);
            //100001
            endNum = 100000 + endNum + 1;
            String num = endNum + "";
            //去掉100001中的首位1
            String numm = num.substring(1);
            in_skock = "I" + uid_pfix + numm;
        } else {
            //数据库没数据时
            in_skock = "I" + uid_pfix + "00001";
        }
        //入库商品加入数据库
        List<InStockProductVo> productVoList = bo.getInStockProductVoList();
        if (!CollectionUtils.isEmpty(productVoList)) {
            for (InStockProductVo stockProductVo : productVoList) {
                stockProductVo.setInStockNumbers(in_skock);
                InStockProduct stockProduct = BeanUtil.toBean(stockProductVo, InStockProduct.class);
                if (inStockProductMapper.insert(stockProduct) <= 0) {
                    throw new ServiceException("入库商品添加失败");
                }
            }
        }
        //入库添加
        GoodsInStock goodsInStock = BeanUtil.toBean(bo, GoodsInStock.class);
        goodsInStock.setInStockNumbers(in_skock);
        goodsInStock.setStates(GoodsInStockType.TO_BE_FOR_STOCK.getCode());
        goodsInStock.setUpdateTime(new Date());
        if (baseMapper.insert(goodsInStock) <= 0) {
            throw new ServiceException("管理员系统未知异常");
        }
        return true;
    }

    /**
     * 修改商品入库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(GoodsInStockBo bo) {
        LambdaQueryWrapper<InStockProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(InStockProduct::getInStockNumbers, bo.getInStockNumbers());
        if (inStockProductMapper.delete(wrapper) <= 0) {
            throw new ServiceException("入库商品更新失败");
        }
        //入库商品加入数据库
        List<InStockProductVo> stockProductVoList = bo.getInStockProductVoList();
        if (!CollectionUtils.isEmpty(stockProductVoList)) {
            for (InStockProductVo inStockProductVo : stockProductVoList) {
                InStockProduct stockProduct = BeanUtil.toBean(inStockProductVo, InStockProduct.class);
                if (inStockProductMapper.insert(stockProduct) <= 0) {
                    throw new ServiceException("入库商品添加失败");
                }
            }
        }
        GoodsInStock goodsInStock = BeanUtil.toBean(bo, GoodsInStock.class);
        goodsInStock.setUpdateTime(new Date());
        if (baseMapper.updateById(goodsInStock) > 0) {
            return true;
        }
        throw new ServiceException("管理员系统未知异常");

    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(GoodsInStock entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品入库
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        List<GoodsInStockVo> goodsInStockVos = baseMapper.selectVoBatchIds(ids);
        //删除入库信息
        if (baseMapper.deleteBatchIds(ids) <= 0) {
            throw new ServiceException("入库商品删除失败");
        }
        for (GoodsInStockVo inStockVo : goodsInStockVos) {
            //批量删除入库商品
            LambdaQueryWrapper<InStockProduct> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(InStockProduct::getInStockNumbers, inStockVo.getInStockNumbers());
            if (inStockProductMapper.delete(wrapper) <= 0) {
                throw new ServiceException("入库商品删除失败");
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateInOfStock(GoodsInStockBo bo) {
        if (ObjectUtils.isEmpty(bo.getStorageId()) && StringUtils.isEmpty(bo.getInStockNumbers())) {
            throw new ServiceException("参数不能为空");
        }
        //根据入库的商品数量更新仓库的数量
        LambdaQueryWrapper<InStockProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InStockProduct::getInStockNumbers, bo.getInStockNumbers());
        List<InStockProduct> inStockProducts = inStockProductMapper.selectList(wrapper);
        Long inStockNum;//入库数量
        for (InStockProduct inStockProduct : inStockProducts) {
            inStockNum = inStockProduct.getInStockNum();
            Long productAttrId = inStockProduct.getProductAttrId();
            if (stockMapper.updateSockForAdd(bo.getStorageId(), productAttrId, inStockNum) <= 0) {
                throw new ServiceException("更新商品库存失败");
            }
        }
        //更新入库状态
        GoodsInStock kxGoodsInStock = new GoodsInStock();
        kxGoodsInStock.setStates(GoodsInStockType.IN_FOR_STOCK.getCode());
        kxGoodsInStock.setIngoingPerson(LoginHelper.getUsername());
        kxGoodsInStock.setIngoingTime(new Date());
        kxGoodsInStock.setUpdateTime(new Date());
        LambdaQueryWrapper<GoodsInStock> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(GoodsInStock::getInStockNumbers, bo.getInStockNumbers());
        if (baseMapper.update(kxGoodsInStock, wrapper1) <= 0) {
            throw new ServiceException("商品入库失败");
        }
        return true;
    }

    /**
     * 获取所有仓库的名称
     *
     * @param bo
     * @return
     */
    @Override
    public List<StorageVo> storagAllName(GoodsInStockBo bo) {
        int state = StorageStatusType.NOMRAL.getCode();
        return storageMapper.getStorageNameAll(state, bo.getStorageIds());
    }
}
