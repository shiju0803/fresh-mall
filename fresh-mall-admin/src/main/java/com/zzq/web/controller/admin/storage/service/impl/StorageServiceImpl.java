package com.zzq.web.controller.admin.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.enums.StorageBusinessStatusType;
import com.zzq.common.enums.StorageStatusType;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.FeieyunPrint;
import com.zzq.common.utils.StringUtils;
import com.zzq.common.utils.file.FileUtils;
import com.zzq.common.utils.redis.RedisUtils;
import com.zzq.storage.domain.Storage;
import com.zzq.storage.domain.bo.StorageBo;
import com.zzq.storage.domain.vo.StorageVo;
import com.zzq.storage.mapper.StorageMapper;
import com.zzq.system.service.ISysConfigService;
import com.zzq.web.controller.admin.storage.service.IStorageService;
import com.zzq.wechat.WxMpConfiguration;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

import static com.zzq.common.constant.ProductCacheConstants.STORAGE_INFO_PREFIX;

/**
 * 仓库管理Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StorageServiceImpl implements IStorageService {


    private final StorageMapper baseMapper;

    private final ISysConfigService configService;

    /**
     * 查询仓库管理
     */
    @Override
    public StorageVo queryById(Long id) {
        StorageVo kxStorageVo = baseMapper.selectVoById(id);
        if (CollectionUtils.isEmpty(kxStorageVo.getPaths())) {
            kxStorageVo.setPaths(new ArrayList<>());
        }
        return kxStorageVo;
    }

    /**
     * 查询仓库管理列表
     */
    @Override
    public TableDataInfo<StorageVo> queryPageList(StorageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Storage> lqw = buildQueryWrapper(bo);
        Page<StorageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询仓库管理列表
     */
    @Override
    public List<StorageVo> queryList(StorageBo bo) {
        LambdaQueryWrapper<Storage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Storage> buildQueryWrapper(StorageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Storage> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), Storage::getName, bo.getName());
        lqw.eq(bo.getProvince() != null, Storage::getProvince, bo.getProvince());
        lqw.eq(bo.getCity() != null, Storage::getCity, bo.getCity());
        lqw.eq(bo.getCounty() != null, Storage::getCounty, bo.getCounty());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), Storage::getAddress, bo.getAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getAdcode()), Storage::getAdcode, bo.getAdcode());
        lqw.eq(bo.getState() != null, Storage::getState, bo.getState());
        lqw.eq(bo.getLongitude() != null, Storage::getLongitude, bo.getLongitude());
        lqw.eq(bo.getLatitude() != null, Storage::getLatitude, bo.getLatitude());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), Storage::getPhone, bo.getPhone());
        lqw.like(StringUtils.isNotBlank(bo.getLeaderName()), Storage::getLeaderName, bo.getLeaderName());
        lqw.eq(bo.getOperatingState() != null, Storage::getOperatingState, bo.getOperatingState());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessStartTime()), Storage::getBusinessStartTime, bo.getBusinessStartTime());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliveryStartTime()), Storage::getDeliveryStartTime, bo.getDeliveryStartTime());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessStopTime()), Storage::getBusinessStopTime, bo.getBusinessStopTime());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliveryStopTime()), Storage::getDeliveryStopTime, bo.getDeliveryStopTime());
        lqw.eq(bo.getDeliveryRadius() != null, Storage::getDeliveryRadius, bo.getDeliveryRadius());
        lqw.eq(bo.getAutomatic() != null, Storage::getAutomatic, bo.getAutomatic());
        lqw.eq(bo.getPrintSwitch() != null, Storage::getPrintSwitch, bo.getPrintSwitch());
        lqw.eq(StringUtils.isNotBlank(bo.getPrintAcount()), Storage::getPrintAcount, bo.getPrintAcount());
        lqw.eq(StringUtils.isNotBlank(bo.getPrintUkey()), Storage::getPrintUkey, bo.getPrintUkey());
        lqw.eq(StringUtils.isNotBlank(bo.getPrintSn()), Storage::getPrintSn, bo.getPrintSn());
        lqw.in(CollectionUtils.isNotEmpty(bo.getStorageIds()), Storage::getId, bo.getStorageIds());
        return lqw;
    }

    /**
     * 新增仓库管理
     */
    @Override
    public Boolean insertByBo(StorageBo bo) {

        Storage add = BeanUtil.toBean(bo, Storage.class);
        add.setState(StorageStatusType.NOMRAL.getCode());
        add.setOperatingState(StorageBusinessStatusType.REST.getCode());
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            RedisUtils.deleteKeys(STORAGE_INFO_PREFIX + "*");
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改仓库管理
     */
    @Override
    public Boolean updateByBo(StorageBo bo) {
        Storage update = BeanUtil.toBean(bo, Storage.class);
        validEntityBeforeSave(update);
        boolean b = baseMapper.updateById(update) > 0;
        if (b) {
            RedisUtils.deleteKeys(STORAGE_INFO_PREFIX + "*");
        }
        return b;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Storage entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除仓库管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean updateStateToNomral(StorageBo bo) {
        if (ObjectUtils.isEmpty(bo.getId())) {
            throw new ServiceException("前置仓资料不存在");
        }
        if (baseMapper.batchUpdateState(Collections.singletonList(bo.getId()), StorageStatusType.NOMRAL.getCode()) <= 0) {
            throw new ServiceException("前置仓资料不存在");
        } else {
            RedisUtils.deleteKeys(STORAGE_INFO_PREFIX + "*");
        }
        return true;
    }

    @Override
    public Boolean updateStateToAbort(StorageBo bo) {
        if (ObjectUtils.isEmpty(bo.getId())) {
            throw new ServiceException("前置仓资料不存在");
        }
        if (baseMapper.batchUpdateState(Collections.singletonList(bo.getId()), StorageStatusType.ABORT.getCode()) <= 0) {
            throw new ServiceException("前置仓资料不存在");
        } else {
            RedisUtils.deleteKeys(STORAGE_INFO_PREFIX + "*");
        }
        return true;
    }

    @Override
    public Boolean updateBusinessStateToOpen(StorageBo bo) {
        if (ObjectUtils.isEmpty(bo.getId())) {
            throw new ServiceException("前置仓资料不存在");
        }
        if (baseMapper.batchUpdateOperatingState(Collections.singletonList(bo.getId()), StorageBusinessStatusType.BUSINESS.getCode()) <= 0) {
            throw new ServiceException("前置仓资料不存在");
        } else {
            RedisUtils.deleteKeys(STORAGE_INFO_PREFIX + "*");
        }
        return true;
    }

    @Override
    public Boolean updateBusinessStateToRest(StorageBo bo) {
        if (ObjectUtils.isEmpty(bo.getId())) {
            throw new ServiceException("前置仓资料不存在");
        }
        if (baseMapper.batchUpdateOperatingState(Collections.singletonList(bo.getId()), StorageBusinessStatusType.REST.getCode()) <= 0) {
            throw new ServiceException("前置仓资料不存在");
        } else {
            RedisUtils.deleteKeys(STORAGE_INFO_PREFIX + "*");
        }
        return true;
    }

    @Override
    public List<StorageVo> options() {
        return null;
    }

    @Override
    public String getStorageQrcodeImage(Long storageId) {
        try {
            if (org.springframework.util.ObjectUtils.isEmpty(storageId)) {
                throw new ServiceException("管理员系统未知异常");
            }
            Map<String, Object> sendObject = new HashMap<>(1);
            sendObject.put("storageId", storageId);
            //判断一下h5是否配置
            String appid = configService.selectConfigByKeyNoCache("h5_appid");
            if (StringUtils.isEmpty(appid)) {
                return "";
            }
            WxMpQrCodeTicket mpQrCodeTicket = WxMpConfiguration.getWxMpService().getQrcodeService().qrCodeCreateTmpTicket(JSONObject.toJSONString(sendObject), 7200);
            File file = WxMpConfiguration.getWxMpService().getQrcodeService().qrCodePicture(mpQrCodeTicket);
            return FileUtils.fileToBase64(file);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(),500);
        }
    }

    @Override
    public String printTest(StorageBo bo) {
        if (StringUtils.isEmpty(bo.getPrintSn())) {
            throw new ServiceException("sn不能为空",500);
        }
        if (StringUtils.isEmpty(bo.getPrintUkey())) {
            throw new ServiceException("Ukey不能为空",500);
        }
        if (StringUtils.isEmpty(bo.getPrintAcount())) {
            throw new ServiceException("Acount不能为空",500);
        }
        return FeieyunPrint.printTset(bo.getPrintSn(),bo.getPrintUkey(),bo.getPrintAcount());
    }


}
