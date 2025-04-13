package com.zzq.web.controller.admin.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.enums.BillEnum;
import com.zzq.common.utils.StringUtils;
import com.zzq.user.domain.UserBill;
import com.zzq.user.domain.bo.UserBillBo;
import com.zzq.user.domain.vo.UserBillVo;
import com.zzq.user.mapper.UserBillMapper;
import com.zzq.web.controller.admin.user.service.IUserBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户账单Service业务层处理
 * @date 2023-02-14
 */
@RequiredArgsConstructor
@Service
public class UserBillServiceImpl implements IUserBillService {

    private final UserBillMapper baseMapper;

    /**
     * 查询用户账单
     */
    @Override
    public UserBillVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户账单列表
     */
    @Override
    public TableDataInfo<UserBillVo> queryPageList(UserBillBo bo, PageQuery pageQuery) {
        QueryWrapper<UserBill> lqw = buildQueryWrapperQuery(bo);

        Page<UserBillVo> result = baseMapper.selectVoPageList(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户账单列表
     */
    @Override
    public List<UserBillVo> queryList(UserBillBo bo) {
        LambdaQueryWrapper<UserBill> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserBill> buildQueryWrapper(UserBillBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserBill> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUid() != null, UserBill::getUid, bo.getUid());
        lqw.eq(StringUtils.isNotBlank(bo.getLinkId()), UserBill::getLinkId, bo.getLinkId());
        lqw.eq(bo.getPm() != null, UserBill::getPm, bo.getPm());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), UserBill::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), UserBill::getCategory, bo.getCategory());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), UserBill::getType, bo.getType());
        lqw.eq(bo.getNumber() != null, UserBill::getNumber, bo.getNumber());
        lqw.eq(bo.getBalance() != null, UserBill::getBalance, bo.getBalance());
        lqw.eq(StringUtils.isNotBlank(bo.getMark()), UserBill::getMark, bo.getMark());
        lqw.eq(bo.getStatus() != null, UserBill::getStatus, bo.getStatus());
        lqw.eq(bo.getIsDel() != null, UserBill::getIsDel, bo.getIsDel());
        return lqw;
    }


    private QueryWrapper<UserBill> buildQueryWrapperQuery(UserBillBo bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<UserBill> lqw = Wrappers.query();
        lqw.eq(bo.getUid() != null, "b.uid", bo.getUid());
        lqw.eq(StringUtils.isNotBlank(bo.getLinkId()), "b.link_id", bo.getLinkId());
        lqw.eq(bo.getPm() != null, "b.pm", bo.getPm());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), "b.title", bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), "b.category", bo.getCategory());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), "b.type", bo.getType());
        lqw.eq(bo.getNumber() != null, "b.number", bo.getNumber());
        lqw.eq(bo.getBalance() != null, "b.balance", bo.getBalance());
        lqw.eq(StringUtils.isNotBlank(bo.getMark()), "b.mark", bo.getMark());
        lqw.eq(bo.getStatus() != null, "b.status", bo.getStatus());
        lqw.eq(bo.getIsDel() != null, "b.is_del", bo.getIsDel());
        lqw.likeRight(bo.getIsDel() != null, "u.nickname", bo.getNickname());
        return lqw;
    }

    /**
     * 新增用户账单
     */
    @Override
    public Boolean insertByBo(UserBillBo bo) {
        UserBill add = BeanUtil.toBean(bo, UserBill.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户账单
     */
    @Override
    public Boolean updateByBo(UserBillBo bo) {
        UserBill update = BeanUtil.toBean(bo, UserBill.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(UserBill entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户账单
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

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
    @Override
    public void expend(Long uid,String title,String category,String type,double number,double balance,String mark){
        UserBill userBill = UserBill.builder()
            .uid(uid)
            .title(title)
            .category(category)
            .type(type)
            .number(BigDecimal.valueOf(number))
            .balance(BigDecimal.valueOf(balance))
            .mark(mark)
            .pm(BillEnum.PM_0.getValue())
            .build();

        baseMapper.insert(userBill);
    }


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
    @Override
    public void income(Long uid,String title,String category,String type,double number,
                       double balance,String mark,String linkid){
        UserBill userBill = UserBill.builder()
            .uid(uid)
            .title(title)
            .category(category)
            .type(type)
            .number(BigDecimal.valueOf(number))
            .balance(BigDecimal.valueOf(balance))
            .mark(mark)
            .pm(BillEnum.PM_1.getValue())
            .linkId(linkid)
            .build();

        baseMapper.insert(userBill);
    }

}
