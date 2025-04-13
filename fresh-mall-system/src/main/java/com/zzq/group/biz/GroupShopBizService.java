package com.zzq.group.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzq.common.enums.StatusType;
import com.zzq.group.domain.GroupShopProduct;
import com.zzq.group.domain.vo.GroupShopVo;
import com.zzq.group.mapper.GroupShopMapper;
import com.zzq.group.mapper.GroupShopProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Description:
 * User: admin
 * Date: 2019/11/25
 * Time: 11:30
 */
@Service
public class GroupShopBizService {

    @Autowired
    private GroupShopMapper groupShopMapper;

    @Autowired
    private GroupShopProductMapper groupShopProductMapper;

    public GroupShopVo getGroupShopById(Long id, Long storageId) {
        GroupShopVo detail = groupShopMapper.detail(id,storageId);
        if (detail == null || detail.getStatus() == StatusType.LOCK.getCode()) {
            return null;
        }
        List<GroupShopProduct> groupShopSkuList = groupShopProductMapper.selectList(new QueryWrapper<GroupShopProduct>().eq("group_shop_id", id));
        detail.setGroupShopSkuList(groupShopSkuList);
        return detail;
    }

}
