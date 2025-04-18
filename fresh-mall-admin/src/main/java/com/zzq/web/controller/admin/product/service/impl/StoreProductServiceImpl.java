package com.zzq.web.controller.admin.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.enums.ShopCommonEnum;
import com.zzq.common.enums.SpecTypeEnum;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.RegexUtil;
import com.zzq.common.utils.StringUtils;
import com.zzq.product.domain.StoreCategory;
import com.zzq.product.domain.StoreProduct;
import com.zzq.product.domain.StoreProductAttrResult;
import com.zzq.product.domain.StoreProductAttrValue;
import com.zzq.product.domain.bo.StoreProductBo;
import com.zzq.product.domain.vo.*;
import com.zzq.product.mapper.StoreCategoryMapper;
import com.zzq.product.mapper.StoreProductAttrResultMapper;
import com.zzq.product.mapper.StoreProductAttrValueMapper;
import com.zzq.product.mapper.StoreProductMapper;
import com.zzq.storage.domain.Stock;
import com.zzq.storage.mapper.StockMapper;
import com.zzq.web.controller.admin.product.service.IStoreProductAttrService;
import com.zzq.web.controller.admin.product.service.IStoreProductRuleService;
import com.zzq.web.controller.admin.product.service.IStoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StoreProductServiceImpl implements IStoreProductService {

    private final StoreProductMapper baseMapper;

    private final StoreCategoryMapper storeCategoryMapper;

    private final IStoreProductRuleService kxStoreProductRuleService;

    private final IStoreProductAttrService kxStoreProductAttrService;

    private final StoreProductAttrValueMapper kxStoreProductAttrValueMapper;

    private final StoreProductAttrResultMapper kxStoreProductAttrResultMapper;

    private final StockMapper kxStockMapper;

    private final StoreCategoryMapper categoryMapper;

    /**
     * 查询商品
     */
    @Override
    public Map<String, Object> queryById(Long id) {
        Map<String, Object> map = new LinkedHashMap<>(3);


        //商品规格
        map.put("ruleList", kxStoreProductRuleService.queryListAll());


        if (id == 0) {
            return map;
        }

        //处理商品详情
        StoreProduct kxStoreProduct = baseMapper.selectById(id);
        ProductVo productVo = new ProductVo();
        BeanUtil.copyProperties(kxStoreProduct, productVo, "sliderImage");
        productVo.setImage(JSONArray.parseArray(kxStoreProduct.getImage()));
        productVo.setSliderImage(JSONArray.parseArray(kxStoreProduct.getSliderImage()));
        StoreProductAttrResult storeProductAttrResult = kxStoreProductAttrResultMapper
            .selectOne(new LambdaQueryWrapper<StoreProductAttrResult>()
                .eq(StoreProductAttrResult::getProductId, id).last("limit 1"));
        JSONObject result = JSON.parseObject(storeProductAttrResult.getResult());
        List<StoreProductAttrValue> attrValues = kxStoreProductAttrValueMapper.selectList(new LambdaQueryWrapper<StoreProductAttrValue>().eq(StoreProductAttrValue::getProductId, kxStoreProduct.getId()));
        List<ProductFormatVo> productFormatDtos = attrValues.stream().map(i -> {
            ProductFormatVo productFormatDto = new ProductFormatVo();
            BeanUtils.copyProperties(i, productFormatDto);
            productFormatDto.setPic(i.getImage());
            return productFormatDto;
        }).collect(Collectors.toList());
        if (SpecTypeEnum.TYPE_1.getValue().equals(kxStoreProduct.getSpecType())) {
            productVo.setAttr(new ProductFormatVo());
            productVo.setAttrs(productFormatDtos);
            productVo.setItems(result.getObject("attr", ArrayList.class));
        } else {

            productFromat(productVo, result);
        }

        map.put("productInfo", productVo);

        return map;
    }


    /**
     * 获取商品属性
     *
     * @param productDto
     * @param result
     */
    private void productFromat(ProductVo productDto, JSONObject result) {
        Map<String, Object> mapAttr = (Map<String, Object>) result.getObject("value", ArrayList.class).get(0);
        ProductFormatVo productFormatDto = ProductFormatVo.builder()
            .pic(mapAttr.get("pic").toString())
            .price(Double.valueOf(mapAttr.get("price").toString()))
            .cost(Double.valueOf(mapAttr.get("cost").toString()))
            .otPrice(Double.valueOf(mapAttr.get("otPrice").toString()))
            .stock(Long.valueOf(mapAttr.get("stock").toString()))
            .barCode(mapAttr.get("barCode").toString())
            .weight(Double.valueOf(mapAttr.get("weight").toString()))
            .volume(Double.valueOf(mapAttr.get("volume").toString()))
            .value1(mapAttr.get("value1").toString())
            .integral(mapAttr.get("integral") != null ? Long.parseLong(mapAttr.get("integral").toString()) : 0)
            .brokerage(Double.valueOf(mapAttr.get("brokerage").toString()))
            .brokerageTwo(Double.valueOf(mapAttr.get("brokerageTwo").toString()))
            .pinkPrice(Double.valueOf(mapAttr.get("pinkPrice").toString()))
            .pinkStock(Integer.valueOf(mapAttr.get("pinkStock").toString()))
            .seckillPrice(Double.valueOf(mapAttr.get("seckillPrice").toString()))
            .seckillStock(Integer.valueOf(mapAttr.get("seckillStock").toString()))
            .build();
        productDto.setAttr(productFormatDto);
    }


    /**
     * 查询商品列表
     */
    @Override
    public TableDataInfo<StoreProductVo> queryPageList(StoreProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreProduct> lqw = buildQueryWrapper(bo);
        Page<StoreProductVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<StoreProductVo> records = result.getRecords();
        records.forEach(kxStoreProductVo -> {
            kxStoreProductVo.setStoreCategory(storeCategoryMapper.selectVoById(kxStoreProductVo.getCateId()));
        });
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品列表
     */
    @Override
    public List<StoreProductVo> queryList(StoreProductBo bo) {
        LambdaQueryWrapper<StoreProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StoreProduct> buildQueryWrapper(StoreProductBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<StoreProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getMerId() != null, StoreProduct::getMerId, bo.getMerId());
        lqw.eq(CollectionUtils.isNotEmpty(bo.getImage()), StoreProduct::getImage, bo.getImage());
        lqw.eq(CollectionUtils.isNotEmpty(bo.getSliderImage()), StoreProduct::getSliderImage, bo.getSliderImage());
        lqw.like(StringUtils.isNotBlank(bo.getStoreName()), StoreProduct::getStoreName, bo.getStoreName());
        lqw.eq(StringUtils.isNotBlank(bo.getStoreInfo()), StoreProduct::getStoreInfo, bo.getStoreInfo());
        lqw.eq(StringUtils.isNotBlank(bo.getKeyword()), StoreProduct::getKeyword, bo.getKeyword());
        lqw.eq(StringUtils.isNotBlank(bo.getBarCode()), StoreProduct::getBarCode, bo.getBarCode());
        lqw.eq(bo.getCateId() != null, StoreProduct::getCateId, bo.getCateId());
        lqw.eq(bo.getPrice() != null, StoreProduct::getPrice, bo.getPrice());
        lqw.eq(bo.getVipPrice() != null, StoreProduct::getVipPrice, bo.getVipPrice());
        lqw.eq(bo.getOtPrice() != null, StoreProduct::getOtPrice, bo.getOtPrice());
        lqw.eq(bo.getPostage() != null, StoreProduct::getPostage, bo.getPostage());
        lqw.like(StringUtils.isNotBlank(bo.getUnitName()), StoreProduct::getUnitName, bo.getUnitName());
        lqw.eq(bo.getSort() != null, StoreProduct::getSort, bo.getSort());
        lqw.eq(bo.getSales() != null, StoreProduct::getSales, bo.getSales());
        lqw.eq(bo.getStock() != null, StoreProduct::getStock, bo.getStock());
        lqw.eq(bo.getIsShow() != null, StoreProduct::getIsShow, bo.getIsShow());
        lqw.eq(bo.getIsHot() != null, StoreProduct::getIsHot, bo.getIsHot());
        lqw.eq(bo.getIsBenefit() != null, StoreProduct::getIsBenefit, bo.getIsBenefit());
        lqw.eq(bo.getIsBest() != null, StoreProduct::getIsBest, bo.getIsBest());
        lqw.eq(bo.getIsNew() != null, StoreProduct::getIsNew, bo.getIsNew());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), StoreProduct::getDescription, bo.getDescription());
        lqw.eq(bo.getIsPostage() != null, StoreProduct::getIsPostage, bo.getIsPostage());
        lqw.eq(bo.getIsDel() != null, StoreProduct::getIsDel, bo.getIsDel());
        lqw.eq(bo.getMerUse() != null, StoreProduct::getMerUse, bo.getMerUse());
        lqw.eq(bo.getGiveIntegral() != null, StoreProduct::getGiveIntegral, bo.getGiveIntegral());
        lqw.eq(bo.getCost() != null, StoreProduct::getCost, bo.getCost());
        lqw.eq(bo.getIsSeckill() != null, StoreProduct::getIsSeckill, bo.getIsSeckill());
        lqw.eq(bo.getIsBargain() != null, StoreProduct::getIsBargain, bo.getIsBargain());
        lqw.eq(bo.getIsGood() != null, StoreProduct::getIsGood, bo.getIsGood());
        lqw.eq(bo.getFicti() != null, StoreProduct::getFicti, bo.getFicti());
        lqw.eq(bo.getBrowse() != null, StoreProduct::getBrowse, bo.getBrowse());
        lqw.eq(StringUtils.isNotBlank(bo.getCodePath()), StoreProduct::getCodePath, bo.getCodePath());
        lqw.eq(bo.getIsSub() != null, StoreProduct::getIsSub, bo.getIsSub());
        lqw.eq(bo.getTempId() != null, StoreProduct::getTempId, bo.getTempId());
        lqw.eq(bo.getSpecType() != null, StoreProduct::getSpecType, bo.getSpecType());
        lqw.eq(bo.getIsIntegral() != null, StoreProduct::getIsIntegral, bo.getIsIntegral());
        lqw.eq(bo.getIntegral() != null, StoreProduct::getIntegral, bo.getIntegral());
        return lqw;
    }

    /**
     * 新增商品
     */
    @Override
    public Boolean insertAndupdateByBo(StoreProductBo bo) {
        //将商品编码拷贝到商品列表
        bo.setBarCode(bo.getAttrs().get(0).getBarCode());
        if (StringUtils.isNotEmpty(bo.getDescription())) {
            bo.setDescription(RegexUtil.converProductDescription(bo.getDescription()));
        }
        ProductResultVo resultDTO = this.computedProduct(bo.getAttrs());
        //只能是二级分类
        Long count = categoryMapper.selectCount(new LambdaQueryWrapper<StoreCategory>().ne(StoreCategory::getPid, 0L).eq(StoreCategory::getId, bo.getCateId()));
        if (count == 0L) {
            throw new ServiceException("请选择二级分类");
        }
        //添加商品
        StoreProduct kxStoreProduct = new StoreProduct();
        BeanUtil.copyProperties(bo, kxStoreProduct, "sliderImage");
        if (bo.getSliderImage().isEmpty()) {
            throw new ServiceException("请上传轮播图");
        }
        if (ObjectUtils.isEmpty(bo.getImage())) {
            throw new ServiceException("请上传商品封面图");
        }

        kxStoreProduct.setPrice(BigDecimal.valueOf(resultDTO.getMinPrice()));
        kxStoreProduct.setOtPrice(BigDecimal.valueOf(resultDTO.getMinOtPrice()));
        kxStoreProduct.setCost(BigDecimal.valueOf(resultDTO.getMinCost()));
        kxStoreProduct.setIntegral(resultDTO.getMinIntegral());
        kxStoreProduct.setStock(resultDTO.getStock());
        kxStoreProduct.setSliderImage(JSONArray.toJSONString(bo.getSliderImage()));

        baseMapper.insertOrUpdate(kxStoreProduct);

        //属性处理
        //处理单sKu
        if (SpecTypeEnum.TYPE_0.getValue().equals(bo.getSpecType())) {
            FromatDetailVo fromatDetailDto = FromatDetailVo.builder()
                .value("规格")
                .detailValue("")
                .attrHidden("")
                .detail(ListUtil.toList("默认"))
                .build();
            List<ProductFormatVo> attrs = bo.getAttrs();
            ProductFormatVo productFormatDto = attrs.get(0);
            productFormatDto.setValue1("规格");
            Map<String, String> map = new HashMap<>();
            map.put("规格", "默认");
            productFormatDto.setDetail(map);
            kxStoreProductAttrService.insertYxStoreProductAttr(ListUtil.toList(fromatDetailDto),
                ListUtil.toList(productFormatDto), kxStoreProduct.getId());
        } else {
            kxStoreProductAttrService.insertYxStoreProductAttr(bo.getItems(),
                bo.getAttrs(), kxStoreProduct.getId());
        }
        return true;
    }

    /**
     * 计算产品数据
     *
     * @param attrs attrs
     * @return ProductResultVo
     */
    private ProductResultVo computedProduct(List<ProductFormatVo> attrs) {
        //取最小价格
        Double minPrice = attrs
            .stream()
            .map(ProductFormatVo::getPrice)
            .min(Comparator.naturalOrder())
            .orElse(0d);

        //取最小积分
        Long minIntegral = attrs
            .stream()
            .map(ProductFormatVo::getIntegral)
            .min(Comparator.naturalOrder())
            .orElse(0L);

        Double minOtPrice = attrs
            .stream()
            .map(ProductFormatVo::getOtPrice)
            .min(Comparator.naturalOrder())
            .orElse(0d);

        Double minCost = attrs
            .stream()
            .map(ProductFormatVo::getCost)
            .min(Comparator.naturalOrder())
            .orElse(0d);
        //计算库存
        Long stock = attrs
            .stream()
            .map(ProductFormatVo::getStock)
            .reduce(Long::sum)
            .orElse(0L);

        if (stock <= 0) {
            throw new ServiceException("库存不能低于0");
        }

        return ProductResultVo.builder()
            .minPrice(minPrice)
            .minOtPrice(minOtPrice)
            .minCost(minCost)
            .stock(stock)
            .minIntegral(minIntegral)
            .build();
    }


    /**
     * 修改商品
     */
    @Override
    public Boolean updateByBo(StoreProductBo bo) {
        StoreProduct update = BeanUtil.toBean(bo, StoreProduct.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(StoreProduct entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Long selectCountByCateId(Long cateId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<StoreProduct>()
            .eq(StoreProduct::getCateId, cateId));
    }

    @Override
    public void onSale(Long id, Integer status) {
        if (ShopCommonEnum.SHOW_1.getValue().equals(status)) {
            status = ShopCommonEnum.SHOW_1.getValue();
        } else {
            status = ShopCommonEnum.SHOW_0.getValue();
        }
        StoreProduct update = new StoreProduct();
        update.setId(id);
        update.setIsShow(status);
        baseMapper.updateById(update);
    }

    /**
     * 获取生成的属性
     *
     * @param id
     * @param jsonStr
     * @param isActivity
     * @return
     */
    @Override
    public Map<String, Object> getFormatAttr(Long id, String jsonStr, boolean isActivity) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Map<String, Object> resultMap = new LinkedHashMap<>(3);

        if (jsonObject == null || jsonObject.get("attrs") == null || jsonObject.getJSONArray("attrs").isEmpty()) {
            resultMap.put("attr", new ArrayList<>());
            resultMap.put("value", new ArrayList<>());
            resultMap.put("header", new ArrayList<>());
            return resultMap;
        }

        List<FromatDetailVo> fromatDetailDTOList = JSON.parseArray(jsonObject.get("attrs").toString(),
            FromatDetailVo.class);

        //fromatDetailDTOList
        DetailVo detailVo = this.attrFormat(fromatDetailDTOList);

        List<Map<String, Object>> headerMapList = null;
        List<Map<String, Object>> valueMapList = new ArrayList<>();
        String align = "center";
        Map<String, Object> headerMap = new LinkedHashMap<>();
        for (Map<String, Map<String, String>> map : detailVo.getRes()) {
            Map<String, String> detail = map.get("detail");
            String[] detailArr = detail.values().toArray(new String[]{});
            Arrays.sort(detailArr);

            String sku = String.join(",", detailArr);

            Map<String, Object> valueMap = new LinkedHashMap<>();

            List<String> detailKeys =
                detail.entrySet()
                    .stream()
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            int i = 0;
            headerMapList = new ArrayList<>();
            for (String title : detailKeys) {
                headerMap.put("title", title);
                headerMap.put("minWidth", "130");
                headerMap.put("align", align);
                headerMap.put("key", "value" + (i + 1));
                headerMap.put("slot", "value" + (i + 1));
                headerMapList.add(ObjectUtil.clone(headerMap));
                i++;
            }

            String[] detailValues = detail.values().toArray(new String[]{});
            for (int j = 0; j < detailValues.length; j++) {
                String key = "value" + (j + 1);
                valueMap.put(key, detailValues[j]);
            }

            valueMap.put("detail", detail);
            valueMap.put("sku", sku);
            valueMap.put("pic", "");
            valueMap.put("price", 0);
            valueMap.put("cost", 0);
            valueMap.put("otPrice", 0);
            valueMap.put("stock", 0);
            valueMap.put("barCode", "");
            valueMap.put("weight", 0);
            valueMap.put("volume", 0);
            valueMap.put("brokerage", 0);
            valueMap.put("brokerageTwo", 0);
            valueMap.put("pinkPrice", 0);
            valueMap.put("seckillPrice", 0);
            valueMap.put("pinkStock", 0);
            valueMap.put("seckillStock", 0);
            valueMap.put("integral", 0);
            if (id > 0) {
                StoreProductAttrValue storeProductAttrValue = kxStoreProductAttrValueMapper
                    .selectOne(new LambdaQueryWrapper<StoreProductAttrValue>()
                        .eq(StoreProductAttrValue::getProductId, id)
                        .eq(StoreProductAttrValue::getSku, sku));
                if (storeProductAttrValue != null) {
                    valueMap.put("sku", storeProductAttrValue.getSku());
                    valueMap.put("pic", storeProductAttrValue.getImage());
                    valueMap.put("price", storeProductAttrValue.getPrice());
                    valueMap.put("cost", storeProductAttrValue.getCost());
                    valueMap.put("otPrice", storeProductAttrValue.getOtPrice());
                    valueMap.put("stock", storeProductAttrValue.getStock());
                    valueMap.put("barCode", storeProductAttrValue.getBarCode());
                    valueMap.put("weight", storeProductAttrValue.getWeight());
                    valueMap.put("volume", storeProductAttrValue.getVolume());
                    valueMap.put("brokerage", storeProductAttrValue.getBrokerage());
                    valueMap.put("brokerageTwo", storeProductAttrValue.getBrokerageTwo());
                    valueMap.put("pinkPrice", storeProductAttrValue.getPinkPrice());
                    valueMap.put("seckillPrice", storeProductAttrValue.getSeckillPrice());
                    valueMap.put("pinkStock", storeProductAttrValue.getPinkStock());
                    valueMap.put("seckillStock", storeProductAttrValue.getSeckillStock());
                    valueMap.put("integral", storeProductAttrValue.getIntegral());
                }
            }

            valueMapList.add(ObjectUtil.clone(valueMap));

        }

        this.addMap(headerMap, headerMapList, align, isActivity);


        resultMap.put("attr", fromatDetailDTOList);
        resultMap.put("value", valueMapList);
        resultMap.put("header", headerMapList);

        return resultMap;
    }

    @Override
    public Long selectCountById(Long productId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<StoreProduct>().eq(StoreProduct::getId, productId));
    }

    @Override
    public Boolean batchAuthorizeGoods(StoreProductBo bo) {

        if (CollectionUtils.isEmpty(bo.getIds())) {
            throw new ServiceException("商品规格不存在!");
        }
        List<Stock> list = new ArrayList<>();
        bo.getIds().stream().forEach(id -> {
            StoreProduct storeProducts = baseMapper.selectOne(new LambdaQueryWrapper<StoreProduct>().eq(StoreProduct::getId, id));
            Stock kxStock = new Stock();
            kxStock.setPrice(storeProducts.getPrice());
            kxStock.setProductId(id);
            kxStock.setProductAttrId(id);
            kxStock.setStorageId(bo.getStorageId());
            kxStock.setStock(0L);
            kxStock.setStatus(1);
            list.add(kxStock);
        });
        //错误数据
        for (Stock kxStock : list) {
            Long productId = kxStock.getProductId();
            Long storageId = kxStock.getStorageId();
            //判断此仓库中是否有该商品
            List<Stock> stockList = kxStockMapper.selectList(new LambdaQueryWrapper<Stock>()
                .eq(Stock::getProductId, productId)
                .eq(Stock::getStorageId, storageId));
            if (CollectionUtils.isEmpty(stockList)) {
                Stock stockdo = new Stock();
                stockdo.setProductId(productId);
                stockdo.setStorageId(storageId);
                stockdo.setProductAttrId(productId);
                Integer status = kxStock.getStatus();
                BigDecimal price = kxStock.getPrice();
                Long stock = kxStock.getStock();
                if (status != null && price != null && stock != null) {
                    stockdo.setStatus(status);
                    stockdo.setPrice(price);
                    stockdo.setStock(0L);
                    stockdo.setFrezzStock(0L);
                    stockdo.setSales(0L);
                    Date now = new Date();
                    stockdo.setCreateTime(now);
                    stockdo.setUpdateTime(now);
                    kxStockMapper.insert(stockdo);
                } else {
                    throw new ServiceException("信息不全!", 500);
                }
            }
        }
        return true;
    }

    @Override
    public List<ProductTreeNodeVo> getProductBigTree() {
        List<StoreCategory> storeCategoryList = storeCategoryMapper.selectList(new LambdaQueryWrapper<StoreCategory>().orderByAsc(StoreCategory::getPid));
        if (storeCategoryList != null && storeCategoryList.size() > 0) {
            Integer recordLevelOne = 0;
            List<StoreProduct> storeProductList = baseMapper.getProductTitleAll();
            List<ProductTreeNodeVo> list = new ArrayList<>();
            for (StoreCategory categoryDO : storeCategoryList) {
                if (categoryDO.getPid() == 0L) {
                    recordLevelOne++;
                }
            }
            Integer recordLevelTwo = storeCategoryList.size();
            for (int i = 0; i < recordLevelOne; i++) {
                StoreCategory categoryOnI = storeCategoryList.get(i);    //一级类目
                ProductTreeNodeVo dtoOnI = new ProductTreeNodeVo();
                dtoOnI.setLabel(categoryOnI.getCateName());
                dtoOnI.setValue("C_" + categoryOnI.getId());
                dtoOnI.setId(categoryOnI.getId());
                dtoOnI.setChildren(new LinkedList<>());
                for (int j = recordLevelOne; j < recordLevelTwo; j++) {
                    StoreCategory categoryOnJ = storeCategoryList.get(j);    //二级类目
                    if (!categoryOnJ.getPid().equals(dtoOnI.getId())) {
                        continue;
                    }
                    ProductTreeNodeVo dtoOnJ = new ProductTreeNodeVo();
                    dtoOnJ.setLabel(categoryOnJ.getCateName());
                    dtoOnJ.setValue("C_" + categoryOnJ.getId());
                    dtoOnJ.setId(categoryOnJ.getId());
                    dtoOnJ.setChildren(new LinkedList<>());
                    for (int k = 0; k < storeProductList.size(); k++) {
                        if (k != 0 && storeProductList.get(k - 1).getCateId().equals(dtoOnJ.getId()) && !storeProductList.get(k).getCateId().equals(dtoOnJ.getId())) {
                            break;
                        }
                        StoreProduct storeProduct = storeProductList.get(k);        //商品
                        if (storeProduct.getCateId().equals(dtoOnJ.getId())) {
                            ProductTreeNodeVo dtoOnK = new ProductTreeNodeVo();
                            dtoOnK.setLabel(storeProduct.getStoreName());
                            dtoOnK.setValue("G_" + storeProduct.getId());
                            dtoOnK.setId(storeProduct.getId());
                            dtoOnJ.getChildren().add(dtoOnK);
                        }
                    }
                    dtoOnI.getChildren().add(dtoOnJ);
                }
                list.add(dtoOnI);
            }
            return list;
        }
        return null;
    }

    /**
     * 组合规则属性算法
     *
     * @param fromatDetailDTOList
     * @return DetailDto
     */
    private DetailVo attrFormat(List<FromatDetailVo> fromatDetailDTOList) {

        List<String> data = new ArrayList<>();
        List<Map<String, Map<String, String>>> res = new ArrayList<>();

        fromatDetailDTOList.stream()
            .map(FromatDetailVo::getDetail)
            .forEach(i -> {
                if (i == null || i.isEmpty()) {
                    throw new ServiceException("请至少添加一个规格值哦");
                }
                String str = ArrayUtil.join(i.toArray(), ",");
                if (str.contains("-")) {
                    throw new ServiceException("规格值里包含'-',请重新添加");
                }
            });

        if (fromatDetailDTOList.size() > 1) {
            for (int i = 0; i < fromatDetailDTOList.size() - 1; i++) {
                if (i == 0) {
                    data = fromatDetailDTOList.get(i).getDetail();
                }
                List<String> tmp = new LinkedList<>();
                for (String v : data) {
                    for (String g : fromatDetailDTOList.get(i + 1).getDetail()) {
                        String rep2 = "";
                        if (i == 0) {
                            rep2 = fromatDetailDTOList.get(i).getValue() + "_" + v + "-"
                                + fromatDetailDTOList.get(i + 1).getValue() + "_" + g;
                        } else {
                            rep2 = v + "-"
                                + fromatDetailDTOList.get(i + 1).getValue() + "_" + g;
                        }

                        tmp.add(rep2);

                        if (i == fromatDetailDTOList.size() - 2) {
                            Map<String, Map<String, String>> rep4 = new LinkedHashMap<>();
                            Map<String, String> reptemp = new LinkedHashMap<>();
                            for (String h : Arrays.asList(rep2.split("-"))) {
                                List<String> rep3 = Arrays.asList(h.split("_"));
                                if (rep3.size() > 1) {
                                    reptemp.put(rep3.get(0), rep3.get(1));
                                } else {
                                    reptemp.put(rep3.get(0), "");
                                }
                            }
                            rep4.put("detail", reptemp);

                            res.add(rep4);
                        }
                    }

                }

                if (!tmp.isEmpty()) {
                    data = tmp;
                }
            }
        } else {
            List<String> dataArr = new ArrayList<>();
            for (FromatDetailVo fromatDetailVo : fromatDetailDTOList) {
                for (String str : fromatDetailVo.getDetail()) {
                    Map<String, Map<String, String>> map2 = new LinkedHashMap<>();
                    dataArr.add(fromatDetailVo.getValue() + "_" + str);
                    Map<String, String> map1 = new LinkedHashMap<>();
                    map1.put(fromatDetailVo.getValue(), str);
                    map2.put("detail", map1);
                    res.add(map2);
                }
            }
            String s = StrUtil.join("-", dataArr);
            data.add(s);
        }

        DetailVo detailDto = new DetailVo();
        detailDto.setData(data);
        detailDto.setRes(res);

        return detailDto;
    }

    /**
     * 增加表头
     *
     * @param headerMap     headerMap
     * @param headerMapList headerMapList
     * @param align         align
     */
    private void addMap(Map<String, Object> headerMap, List<Map<String, Object>> headerMapList, String align, boolean isActivity) {
        headerMap.put("title", "图片");
        headerMap.put("slot", "pic");
        headerMap.put("align", align);
        headerMap.put("minWidth", 80);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "售价");
        headerMap.put("slot", "price");
        headerMap.put("align", align);
        headerMap.put("minWidth", 120);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "成本价");
        headerMap.put("slot", "cost");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "原价");
        headerMap.put("slot", "otPrice");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "库存");
        headerMap.put("slot", "stock");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "产品编号");
        headerMap.put("slot", "barCode");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "重量(KG)");
        headerMap.put("slot", "weight");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "体积(m³)");
        headerMap.put("slot", "volume");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        headerMap.put("title", "所需兑换积分");
        headerMap.put("slot", "integral");
        headerMap.put("align", align);
        headerMap.put("minWidth", 140);
        headerMapList.add(ObjectUtil.clone(headerMap));

        if (isActivity) {
            headerMap.put("title", "拼团价");
            headerMap.put("slot", "pinkPrice");
            headerMap.put("align", align);
            headerMap.put("minWidth", 140);
            headerMapList.add(ObjectUtil.clone(headerMap));

            headerMap.put("title", "拼团活动库存");
            headerMap.put("slot", "pinkStock");
            headerMap.put("align", align);
            headerMap.put("minWidth", 140);
            headerMapList.add(ObjectUtil.clone(headerMap));

            headerMap.put("title", "秒杀价");
            headerMap.put("slot", "seckillPrice");
            headerMap.put("align", align);
            headerMap.put("minWidth", 140);
            headerMapList.add(ObjectUtil.clone(headerMap));

            headerMap.put("title", "秒杀活动库存");
            headerMap.put("slot", "seckillStock");
            headerMap.put("align", align);
            headerMap.put("minWidth", 140);
            headerMapList.add(ObjectUtil.clone(headerMap));
        }

        headerMap.put("title", "操作");
        headerMap.put("slot", "action");
        headerMap.put("align", align);
        headerMap.put("minWidth", 70);
        headerMapList.add(ObjectUtil.clone(headerMap));
    }


}
