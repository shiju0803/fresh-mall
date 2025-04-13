package com.zzq.web.controller.admin.user.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.user.domain.bo.UserBillBo;
import com.zzq.user.domain.vo.UserBillVo;

import java.util.Collection;
import java.util.List;

/**
 * 用户账单Service接口
 * @date 2023-02-14
 */
public interface IUserBillService {

    /**
     * 查询用户账单
     */
    UserBillVo queryById(Long id);

    /**
     * 查询用户账单列表
     */
    TableDataInfo<UserBillVo> queryPageList(UserBillBo bo, PageQuery pageQuery);

    /**
     * 查询用户账单列表
     */
    List<UserBillVo> queryList(UserBillBo bo);

    /**
     * 新增用户账单
     */
    Boolean insertByBo(UserBillBo bo);

    /**
     * 修改用户账单
     */
    Boolean updateByBo(UserBillBo bo);

    /**
     * 校验并批量删除用户账单信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 增加支出流水
     * @param uid uid
     * @param title 账单标题
     * @param category 明细种类
     * @param type 明细类型
     * @param number 明细数字
     * @param balance 剩余
     * @param mark 备注
     */
    void expend(Long uid,String title,String category,String type,double number,double balance,String mark);

    /**
     * 增加收入/支入流水
     * @param uid uid
     * @param title 账单标题
     * @param category 明细种类
     * @param type 明细类型
     * @param number 明细数字
     * @param balance 剩余
     * @param mark 备注
     * @param linkid 关联id
     */
    void income(Long uid,String title,String category,String type,double number,
                double balance,String mark,String linkid);
}
