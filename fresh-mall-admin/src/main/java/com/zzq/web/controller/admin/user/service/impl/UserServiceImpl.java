package com.zzq.web.controller.admin.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.enums.BillDetailEnum;
import com.zzq.common.enums.ShopCommonEnum;
import com.zzq.common.utils.StringUtils;
import com.zzq.user.domain.User;
import com.zzq.user.domain.bo.UserBo;
import com.zzq.user.domain.vo.PromUserVo;
import com.zzq.user.domain.vo.UserVo;
import com.zzq.user.mapper.UserMapper;
import com.zzq.web.controller.admin.user.service.IUserBillService;
import com.zzq.web.controller.admin.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户Service业务层处理
 * @date 2023-02-14
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final UserMapper baseMapper;

    private final IUserBillService userBillService;

    /**
     * 查询用户
     */
    @Override
    public UserVo queryById(Long uid) {
        return baseMapper.selectVoById(uid);
    }

    /**
     * 查询用户列表
     */
    @Override
    public TableDataInfo<UserVo> queryPageList(UserBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<User> lqw = buildQueryWrapper(bo);
        Page<UserVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户列表
     */
    @Override
    public List<UserVo> queryList(UserBo bo) {
        LambdaQueryWrapper<User> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<User> buildQueryWrapper(UserBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<User> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getUsername()), User::getUsername, bo.getUsername());
        lqw.eq(StringUtils.isNotBlank(bo.getPassword()), User::getPassword, bo.getPassword());
        lqw.like(StringUtils.isNotBlank(bo.getRealName()), User::getRealName, bo.getRealName());
        lqw.eq(bo.getBirthday() != null, User::getBirthday, bo.getBirthday());
        lqw.eq(StringUtils.isNotBlank(bo.getCardId()), User::getCardId, bo.getCardId());
        lqw.eq(StringUtils.isNotBlank(bo.getMark()), User::getMark, bo.getMark());
        lqw.eq(bo.getPartnerId() != null, User::getPartnerId, bo.getPartnerId());
        lqw.eq(bo.getGroupId() != null, User::getGroupId, bo.getGroupId());
        lqw.like(StringUtils.isNotBlank(bo.getNickname()), User::getNickname, bo.getNickname());
        lqw.eq(StringUtils.isNotBlank(bo.getAvatar()), User::getAvatar, bo.getAvatar());
        lqw.likeRight(StringUtils.isNotBlank(bo.getPhone()), User::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getAddIp()), User::getAddIp, bo.getAddIp());
        lqw.eq(StringUtils.isNotBlank(bo.getLastIp()), User::getLastIp, bo.getLastIp());
        lqw.eq(bo.getNowMoney() != null, User::getNowMoney, bo.getNowMoney());
        lqw.eq(bo.getBrokeragePrice() != null, User::getBrokeragePrice, bo.getBrokeragePrice());
        lqw.eq(bo.getIntegral() != null, User::getIntegral, bo.getIntegral());
        lqw.eq(bo.getSignNum() != null, User::getSignNum, bo.getSignNum());
        lqw.eq(bo.getStatus() != null, User::getStatus, bo.getStatus());
        lqw.eq(bo.getLevel() != null, User::getLevel, bo.getLevel());
        lqw.eq(bo.getSpreadUid() != null, User::getSpreadUid, bo.getSpreadUid());
        lqw.eq(bo.getSpreadTime() != null, User::getSpreadTime, bo.getSpreadTime());
        lqw.eq(StringUtils.isNotBlank(bo.getUserType()), User::getUserType, bo.getUserType());
        lqw.eq(bo.getIsPromoter() != null, User::getIsPromoter, bo.getIsPromoter());
        lqw.eq(bo.getPayCount() != null, User::getPayCount, bo.getPayCount());
        lqw.eq(bo.getSpreadCount() != null, User::getSpreadCount, bo.getSpreadCount());
        lqw.eq(StringUtils.isNotBlank(bo.getAddres()), User::getAddres, bo.getAddres());
        lqw.eq(bo.getAdminid() != null, User::getAdminid, bo.getAdminid());
        lqw.eq(ObjectUtils.isNotEmpty(bo.getLoginType()), User::getLoginType, bo.getLoginType());
        lqw.eq(StringUtils.isNotBlank(bo.getWxProfile()), User::getWxProfile, bo.getWxProfile());
        lqw.eq(bo.getIsDel() != null, User::getIsDel, bo.getIsDel());
        return lqw;
    }

    /**
     * 新增用户
     */
    @Override
    public Boolean insertByBo(UserBo bo) {
        User add = BeanUtil.toBean(bo, User.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setUid(add.getUid());
        }
        return flag;
    }

    /**
     * 修改用户
     */
    @Override
    public Boolean updateByBo(UserBo bo) {
        User update = BeanUtil.toBean(bo, User.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(User entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 查看下级
     *
     * @return
     */
    @Override
    public List<PromUserVo> querySpread(UserBo bo) {
        return this.getUserSpreadGrade(bo.getUid(), 1, 999, bo.getGrade(), "", "");
    }


    /**
     * 获取我的分销下人员列表
     *
     * @param uid     uid
     * @param page    page
     * @param limit   limit
     * @param grade   ShopCommonEnum.GRADE_0
     * @param keyword 关键字搜索
     * @param sort    排序
     * @return list
     */
    @Override
    public List<PromUserVo> getUserSpreadGrade(Long uid, int page, int limit, Integer grade,
                                                 String keyword, String sort) {
        List<User> userList = baseMapper.selectList(new LambdaQueryWrapper<User>()
            .eq(User::getSpreadUid, uid));
        List<Long> userIds = userList.stream()
            .map(User::getUid)
            .collect(Collectors.toList());

        List<PromUserVo> list = new ArrayList<>();
        if (userIds.isEmpty()) {
            return list;
        }

        if (StrUtil.isBlank(sort)) {
            sort = "u.uid desc";
        }

        Page<User> pageModel = new Page<>(page, limit);
        if (ShopCommonEnum.GRADE_0.getValue().equals(grade)) {//-级
            list = baseMapper.getUserSpreadCountList(pageModel, userIds,
                keyword, sort);
        } else {//二级
            List<User> userListT = baseMapper.selectList(new LambdaQueryWrapper<User>()
                .in(User::getSpreadUid, userIds));
            List<Long> userIdsT = userListT.stream()
                .map(User::getUid)
                .collect(Collectors.toList());
            if (userIdsT.isEmpty()) {
                return list;
            }
            list = baseMapper.getUserSpreadCountList(pageModel, userIdsT,
                keyword, sort);

        }
        return list;
    }

    @Override
    public void onStatus(Long id, Integer status) {
        if (ShopCommonEnum.IS_STATUS_1.getValue().equals(status)) {
            status = ShopCommonEnum.IS_STATUS_1.getValue();
        } else {
            status = ShopCommonEnum.IS_STATUS_0.getValue();
        }
        User user = new User();
        user.setStatus(status);
        baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUid, id));
    }

    @Override
    public void updateMoney(UserBo param) {
        User kxUser = baseMapper.selectById(param.getUid());
        double newMoney = 0d;
        String mark = "";
        if (param.getPtype() == 1) {
            mark = "系统增加了" + param.getMoney() + "余额";
            newMoney = NumberUtil.add(kxUser.getNowMoney(), param.getMoney()).doubleValue();
            userBillService.income(kxUser.getUid(), "系统增加余额", BillDetailEnum.CATEGORY_1.getValue(),
                BillDetailEnum.TYPE_6.getValue(), param.getMoney(), newMoney, mark, "");
        } else {
            mark = "系统扣除了" + param.getMoney() + "余额";
            newMoney = NumberUtil.sub(kxUser.getNowMoney(), param.getMoney()).doubleValue();
            if (newMoney < 0) {
                newMoney = 0d;
            }
            userBillService.expend(kxUser.getUid(), "系统减少余额",
                BillDetailEnum.CATEGORY_1.getValue(),
                BillDetailEnum.TYPE_7.getValue(),
                param.getMoney(), newMoney, mark);
        }
        kxUser.setNowMoney(BigDecimal.valueOf(newMoney));
        baseMapper.insertOrUpdate(kxUser);
    }

    @Override
    public UserVo selectByUid(Long uid) {
        return baseMapper.selectVoOne(new LambdaQueryWrapper<User>().eq(User::getUid, uid));
    }

}
