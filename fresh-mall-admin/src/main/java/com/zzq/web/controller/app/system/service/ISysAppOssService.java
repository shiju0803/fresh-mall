package com.zzq.web.controller.app.system.service;

import com.zzq.system.domain.vo.SysOssVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传 服务层
 */
public interface ISysAppOssService {


    SysOssVo upload(MultipartFile file);


}
