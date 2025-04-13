package com.zzq.web.controller.admin.user.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.user.domain.bo.UserBo;
import com.zzq.user.domain.vo.PromUserVo;
import com.zzq.user.domain.vo.UserVo;

import java.util.Collection;
import java.util.List;

/**
 * 用户Service接口
 * @date 2023-02-14
 */
public interface IUserService {

    /**
     * 查询用户
     */
    UserVo queryById(Long uid);

    /**
     * 查询用户列表
     */
    TableDataInfo<UserVo> queryPageList(UserBo bo, PageQuery pageQuery);

    /**
     * 查询用户列表
     */
    List<UserVo> queryList(UserBo bo);

    /**
     * 新增用户
     */
    Boolean insertByBo(UserBo bo);

    /**
     * 修改用户
     */
    Boolean updateByBo(UserBo bo);

    /**
     * 校验并批量删除用户信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查看下级
     *
     * @return
     */
    List<PromUserVo> querySpread(UserBo bo);


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
    List<PromUserVo> getUserSpreadGrade(Long uid, int page, int limit, Integer grade, String keyword, String sort);

    /**
     * 更改状态
     */
    void onStatus(Long id, Integer status);

    /**
     * 修改余额
     */
    void updateMoney(UserBo param);

    /**
     * 通过uid获取用户信息
     *
     * @param uid
     * @return
     */
    UserVo selectByUid(Long uid);
}
