package com.zzq.user.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.user.domain.vo.PromUserVo;
import com.zzq.user.domain.vo.UserVo;
import com.zzq.user.domain.User;
import com.zzq.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * 用户Mapper接口
 * @date 2023-02-14
 */
public interface UserMapper extends BaseMapperPlus<UserMapper, User, UserVo> {


    @Select("<script>SELECT u.uid,u.nickname,u.avatar,DATE_FORMAT(u.create_time,'%Y/%m/%d') as time," +
        "u.spread_count as childCount,COUNT(o.id) as orderCount," +
        "IFNULL(SUM(o.pay_price),0) as numberCount FROM kx_user u " +
        "LEFT JOIN kx_store_order o ON u.uid=o.uid " +
        "WHERE u.uid in <foreach item='id' index='index' collection='uids' " +
        " open='(' separator=',' close=')'>" +
        "   #{id}" +
        " </foreach> <if test='keyword != null'>" +
        " AND ( u.nickname LIKE CONCAT(CONCAT('%',#{keyword}),'%') OR u.phone LIKE CONCAT(CONCAT('%',#{keyword}),'%'))</if>" +
        " GROUP BY u.uid ORDER BY #{orderByStr} " +
        "</script>")
    List<PromUserVo> getUserSpreadCountList(Page page,
                                            @Param("uids") List uids,
                                            @Param("keyword") String keyword,
                                            @Param("orderByStr") String orderBy);



    /**
     * 总用户数
     * @param date
     * @return
     */
    Integer countTotalUser(@Param("date") Date date);

    /**
     * 新注册用户数
     * @param start
     * @param end
     * @return
     */
    Integer countNewUser(
            @Param("start") Date start,@Param("end") Date end);

    /**
     * 在线用户数
     * @param start
     * @param end
     * @return
     */
    Integer countOnlineUser(
            @Param("start") Date start,@Param("end") Date end);

    /**
     * 下单用户数
     * @param start
     * @param end
     * @return
     */
    Integer countOrderUser(
            @Param("start") Date start,@Param("end") Date end);

    /**
     * 首单用户数
     * @param start
     * @param end
     * @return
     */
    Integer countFirstOrderUser(
            @Param("start") Date start,@Param("end") Date end);
}
