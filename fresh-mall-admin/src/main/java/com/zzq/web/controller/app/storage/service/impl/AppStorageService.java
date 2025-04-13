package com.zzq.web.controller.app.storage.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzq.carousel.domain.Carousel;
import com.zzq.carousel.domain.vo.CarouselVo;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.enums.AdvertisementType;
import com.zzq.common.enums.RecommendType;
import com.zzq.common.enums.StorageBusinessStatusType;
import com.zzq.common.enums.StorageStatusType;
import com.zzq.product.domain.vo.StoreProductVo;
import com.zzq.recommend.domain.vo.RecommendVo;
import com.zzq.storage.domain.Storage;
import com.zzq.storage.domain.vo.IntegralIndexDataVo;
import com.zzq.storage.domain.vo.RecentlyStorageVo;
import com.zzq.storage.domain.vo.StorageVo;
import com.zzq.storage.mapper.StorageMapper;
import com.zzq.web.controller.app.carousel.service.IAppCarouselService;
import com.zzq.web.controller.app.product.service.IAppProductService;
import com.zzq.web.controller.app.recommend.service.IAppRecommendService;
import com.zzq.web.controller.app.storage.service.IAppStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AppStorageService implements IAppStorageService {


    private final StorageMapper storageMapper;

    private final IAppRecommendService recommendService;
    private final IAppProductService productService;
    private final IAppCarouselService carouselService;

    @Override
    public RecentlyStorageVo getRecentlyStorage(BigDecimal longitude, BigDecimal latitude) {
        RecentlyStorageVo recentlyStorageVo = new RecentlyStorageVo();
        // 获取当前区域范围的仓库
//        List<StorageVo> storageList = RedisUtils.getCacheList(STORAGE_INFO_PREFIX);
        List<StorageVo> storageList = new ArrayList<>();
        if (storageList == null) {
            LambdaQueryWrapper<Storage> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Storage::getState, StorageStatusType.NOMRAL.getCode());
            storageList = storageMapper.selectVoList(wrapper);
//            if (storageList != null && storageList.size() > 0) {
//                RedisUtils.setCacheList(STORAGE_INFO_PREFIX, storageList);
//            }
        }
        if (storageList != null && storageList.size() > 0) {
            // 获取有效配送范围内的仓库
            double userStorgaeDistance;
            double[] userGps = {latitude.doubleValue(), longitude.doubleValue()};
            Map<Double, StorageVo> distanceStorageMap = new HashMap<>(storageList.size());
            for (StorageVo storageDO : storageList) {
                userStorgaeDistance = calculationDistance(new double[]{storageDO.getLatitude().doubleValue(), storageDO.getLongitude().doubleValue()}, userGps);
                if (userStorgaeDistance <= storageDO.getDeliveryRadius() * 1000) {
                    distanceStorageMap.put(userStorgaeDistance, storageDO);
                }
            }
            if (distanceStorageMap.size() > 0) {
                StorageVo storageDO;
                double distance = 0D;
                StorageVo recentlyStorage = null;
                Map.Entry<Double, StorageVo> currentEntry;
                distanceStorageMap = sortMapByValue(distanceStorageMap);
                // 获取有效仓库
                Iterator<Map.Entry<Double, StorageVo>> iterator = distanceStorageMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    currentEntry = iterator.next();
                    storageDO = currentEntry.getValue();
                    distance = currentEntry.getKey();
                    if (StorageBusinessStatusType.BUSINESS.getCode() == storageDO.getOperatingState()) {
                        recentlyStorage = storageDO;
                        recentlyStorage.setDistance(BigDecimal.valueOf(distance));
                        break;
                    }
                }
                if (recentlyStorage != null) {
                    // 有仓库，营业中
                    recentlyStorageVo.setId(recentlyStorage.getId());
                    recentlyStorageVo.setHaveStorage(true);
                    recentlyStorageVo.setBusinessState(true);
                    recentlyStorageVo.setBusinessStartTime(recentlyStorage.getBusinessStartTime());
                    recentlyStorageVo.setBusinessStopTime(recentlyStorage.getBusinessStopTime());
                    recentlyStorageVo.setDeliveryStartTime(recentlyStorage.getDeliveryStartTime());
                    recentlyStorageVo.setDeliveryStopTime(recentlyStorage.getDeliveryStopTime());
                    recentlyStorageVo.setDistance(recentlyStorage.getDistance());
                } else {
                    // 有仓库，没营业
                    Collection<Double> collection = distanceStorageMap.keySet();
                    Double mapKeyMin = Collections.min(collection);
                    recentlyStorage = distanceStorageMap.get(mapKeyMin);
                    recentlyStorageVo.setId(recentlyStorage.getId());
                    recentlyStorageVo.setHaveStorage(true);
                    recentlyStorageVo.setBusinessState(false);
                    recentlyStorageVo.setBusinessStartTime(recentlyStorage.getBusinessStartTime());
                    recentlyStorageVo.setBusinessStopTime(recentlyStorage.getBusinessStopTime());
                    recentlyStorageVo.setDeliveryStartTime(recentlyStorage.getDeliveryStartTime());
                    recentlyStorageVo.setDeliveryStopTime(recentlyStorage.getDeliveryStopTime());
                    recentlyStorageVo.setDistance(recentlyStorage.getDistance());
                }
            } else {
                recentlyStorageVo.setHaveStorage(false);
            }
        } else {
            // 没仓库
            recentlyStorageVo.setHaveStorage(false);
        }
        // TODO 测试数据写死仓库
        // ==============================
        recentlyStorageVo.setId(11L);
        recentlyStorageVo.setHaveStorage(true);
        recentlyStorageVo.setBusinessState(true);
        //===============================
        return recentlyStorageVo;
    }

    @Override
    public IntegralIndexDataVo getIndexDataByStorage(Long storageId) {
        //分类
        List<Carousel> carouselList = carouselService.listAll(99);
        Map<String, List<CarouselVo>> listMap = carouselList.stream().map(item -> {
            CarouselVo kxCarouselVo = new CarouselVo();
            BeanUtils.copyProperties(item, kxCarouselVo);
            return kxCarouselVo;
        }).collect(Collectors.groupingBy(item -> "t" + item.getAdType()));

        List<CarouselVo> categoryPickAd = listMap.get("t" + AdvertisementType.CATEGORY_PICK.getCode());
        //封装 分类精选 商品
        if (!CollUtil.isEmpty(categoryPickAd)) {
            TableDataInfo<StoreProductVo> pickPage;
            for (CarouselVo item : categoryPickAd) {
                pickPage = productService.getGoodsPage(1, 10, new Long(item.getUrl().substring(item.getUrl().lastIndexOf("=") + 1)), "sales", false, null);
                item.setData(pickPage.getRows());
            }
        }

        IntegralIndexDataVo integralIndexDataVo = new IntegralIndexDataVo();
        integralIndexDataVo.setCarouseList(listMap);

        // 销量冠军
        List<RecommendVo> salesRecommend = recommendService.getRecommendByType(storageId, RecommendType.HOT.getCode(), 1, 10).getRows();
        integralIndexDataVo.setSalesTop(salesRecommend);

        // 特价推荐
        List<RecommendVo> cheapRecommend = recommendService.getRecommendByType(storageId, RecommendType.CHEAP.getCode(), 1, 10).getRows();
        integralIndexDataVo.setCheapRecommend(cheapRecommend);

        // 最近上新
        List<StoreProductVo> newTop = productService.getGoodsPageByStorage(storageId, 1, 10, null, "id", false, null,0).getRows();
        integralIndexDataVo.setNewTop(newTop);

        // 新鲜时报
        integralIndexDataVo.setNewTimesContent("新鲜时报！");
        return integralIndexDataVo;
    }

    @Override
    public StorageVo getStorage(Long storageId) {
        return storageMapper.selectVoById(storageId);
    }


    @Override
    public TableDataInfo<RecommendVo> getRecommendByStorage(Long storageId, Integer recommendType, Integer pageNo, Integer pageSize) {
        return recommendService.getRecommendByType(storageId, recommendType, pageNo, pageSize);
    }


    /**
     * 计算两点距离
     *
     * @param point1 点1
     * @param point2 点2
     * @return 两点之间的距离(米)
     */
    public static double calculationDistance(double[] point1, double[] point2) {
        double lat1 = point1[0];
        double lat2 = point2[0];
        double lng1 = point1[1];
        double lng2 = point2[1];
        double radLat1 = lat1 * Math.PI / 180.0;
        double radLat2 = lat2 * Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = (lng1 * Math.PI / 180.0) - (lng2 * Math.PI / 180.0);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        return s * 6370996.81;
    }

    /**
     * 根据Key-Map排序
     *
     * @param oriMap 需要排序的Map
     * @return 排序后的Map
     */
    public static Map<Double, StorageVo> sortMapByValue(Map<Double, StorageVo> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<Double, StorageVo> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<Double, StorageVo>> entryList = new ArrayList<>(oriMap.entrySet());
        entryList.sort((me1, me2) -> me2.getKey().compareTo(me1.getKey()));
        Iterator<Map.Entry<Double, StorageVo>> iter = entryList.iterator();
        Map.Entry<Double, StorageVo> tmpEntry;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }
}
