package com.yuwan.picture.manager;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.yuwan.picture.config.CosClientConfig;
import com.yuwan.picture.exception.BusinessException;
import com.yuwan.picture.exception.ErrorCode;
import com.yuwan.picture.exception.ThrowUtils;
import com.yuwan.picture.model.dto.file.UploadPictureResult;
import io.github.classgraph.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FileManager {

    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private CosManager cosManager;

    /**
     * 上传图片
     * @param multipartFile  文件
     * @param uploadPathPrefix 上传路径前缀
     * @return
     */
    public UploadPictureResult uploadPicture(MultipartFile multipartFile, String uploadPathPrefix) {
        //校验图片
        vaildPicture(multipartFile);
        //图片上传地址
        String uuid = RandomUtil.randomString(8);
        String originFilename = multipartFile.getOriginalFilename();
        //拼接自定义上传文件名
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()),uuid,
                FileUtil.getSuffix(originFilename));
        //上传文件路径
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFilename);
        //创建临时文件
        File file = null;
        try {
            //创建临时文件
            file = File.createTempFile(uploadPath, null);
            multipartFile.transferTo(file);
            //上传图片
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            //封装返回结果
            UploadPictureResult uploadPictureResult = new UploadPictureResult();
            int picWidth = imageInfo.getWidth();
            int picHeight = imageInfo.getHeight();
            double picScale = NumberUtil.round(picWidth * 1.0 / picHeight,2).doubleValue();
            uploadPictureResult.setUrl(cosClientConfig.getHost()+ "/" + uploadPath);
            uploadPictureResult.setPicName(FileUtil.mainName(originFilename));
            uploadPictureResult.setPicSize(FileUtil.size(file));
            uploadPictureResult.setPicWidth(picWidth);
            uploadPictureResult.setPicHeight(picHeight);
            uploadPictureResult.setPicScale(picScale);
            uploadPictureResult.setPicFormat(imageInfo.getFormat());
            return uploadPictureResult;
        } catch (IOException e) {
           log.error("上传图片到对象存储失败", e);
           throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传图片失败");
        }finally{
            this.deleteTempFile(file);
        }
    }

    private void vaildPicture(MultipartFile multipartFile) {
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.PARAMS_ERROR,"文件不能为空");
        //1.校验文件大小
        final long ONE_M = 1024 * 1024L;
        ThrowUtils.throwIf(multipartFile.getSize() > 2 * ONE_M, ErrorCode.PARAMS_ERROR,"文件大小不能超过2M");
        //2.校验文件后缀类型
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        //定义允许上传文件后缀类型
        final List<String> ALLOWED_FORMAT_List = Arrays.asList("jpg", "jpeg", "png", "webp");
        ThrowUtils.throwIf(!ALLOWED_FORMAT_List.contains(fileSuffix), ErrorCode.PARAMS_ERROR,"文件格式不支持");
    }
    private void deleteTempFile(File file) {
        if (file == null) {
            return;
        }
        boolean delete = file.delete();
        if (!delete) {
            log.error("file delete error, filepath ={}", file.getAbsolutePath());
        }
    }
}
