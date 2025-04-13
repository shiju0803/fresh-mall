package com.zzq.web.controller.app.order.service;

import com.zzq.common.core.page.TableDataInfo;
import com.zzq.order.domain.bo.AppraiseRequestBo;
import com.zzq.order.domain.vo.StoreAppraiseVo;

public interface IAppAppraiseService {

    /**
     * 获取评论
     * @param producId
     * @param pageNo
     * @param pageSize
     * @param state
     * @return
     */
    TableDataInfo<StoreAppraiseVo> getProductAppraiseByPage(Long producId, Integer pageNo, Integer pageSize, Integer state);


    /**
     * 增加评价
     * @param bo
     */
    Boolean addAppraise(AppraiseRequestBo bo, Long userId);

}
