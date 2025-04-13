package com.zzq.storage.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: fresh-mall
 * @date: 2020/03/08 14:25
 **/
@Data
public class RoleStorageVo implements Serializable {


    private Long roleId;

    private List<Long> storageIdList;

}
