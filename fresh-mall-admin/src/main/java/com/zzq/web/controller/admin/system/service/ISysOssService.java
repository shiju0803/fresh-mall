package com.zzq.web.controller.admin.system.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.system.domain.bo.SysOssBo;
import com.zzq.system.domain.vo.SysOssVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 文件上传 服务层
 */
public interface ISysOssService {

    TableDataInfo<SysOssVo> queryPageList(SysOssBo sysOss, PageQuery pageQuery);

    List<SysOssVo> listByIds(Collection<Long> ossIds);

    SysOssVo getById(Long ossId);

    SysOssVo upload(MultipartFile file);

    void download(Long ossId, HttpServletResponse response) throws IOException;

    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 磁盘存放
     * @param file
     * @return
     */
    SysOssVo disk(MultipartFile file);

    /**
     * 数据库存放
     * @param file
     * @return
     */
    SysOssVo database(MultipartFile file);

    /**
     * 获取图片二进制流
     * @param configKey
     * @param path
     * @return
     */
    byte[] getFileContent(String configKey, String path);

}
