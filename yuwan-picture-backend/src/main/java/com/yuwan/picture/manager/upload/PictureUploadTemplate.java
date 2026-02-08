package com.yuwan.picture.manager.upload;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.yuwan.picture.config.CosClientConfig;
import com.yuwan.picture.exception.BusinessException;
import com.yuwan.picture.exception.ErrorCode;
import com.yuwan.picture.manager.CosManager;
import com.yuwan.picture.model.dto.file.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
@Slf4j
public abstract class  PictureUploadTemplate {

    @Resource
    protected CosManager cosManager;

    @Resource
    protected CosClientConfig cosClientConfig;
    /**
     * 上传图片
     * @param inputSource
     * @param uploadPathPrefix
     * @return
     */
    public final UploadPictureResult uploadPicture(Object inputSource, String uploadPathPrefix){
        //1.校验图片
        validPicture(inputSource);
        //2.图片上传地址
        String uuid = RandomUtil.randomString(8);
        String originFilename = getOriginFilename(inputSource);
        //拼接自定义上传文件名
        String uploadFilename = String.format("%s_%s.%s", DateUtil.formatDate(new Date()),uuid,
                FileUtil.getSuffix(originFilename));
        //上传文件路径
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadFilename);

        File file = null;
        try {
            //3.创建临时文件
            file = File.createTempFile(uploadPath, null);

            //处理文件来源(本地或url)
            processFile(inputSource,file);

            //4.上传图片到对象存储
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();

            //5.封装返回结果
            return buildResult(originFilename, file, uploadPath, imageInfo);
        } catch (Exception e) {
            log.error("上传图片到对象存储失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传图片失败");
        }finally{
            //6.清理临时文件
            deleteTempFile(file);
        }
    }

    /**
     *  处理输入源并生成本地临时文件
     * @param inputSource
     * @param file
     */
    protected abstract void processFile(Object inputSource, File file) throws Exception;
    /**
     * 获取输入源的原始文件名
     * @param inputSource
     * @return
     */
    protected abstract String getOriginFilename(Object inputSource);

    /**
     * 校验输入源(本地或url)
     * @param inputSource
     */
    protected abstract void validPicture(Object inputSource);

    /**
     * 删除临时文件
     * @param file
     */
    private void deleteTempFile(File file) {
        if (file == null) {
            return;
        }
        boolean delete = file.delete();
        if (!delete) {
            log.error("file delete error, filepath ={}", file.getAbsolutePath());
        }
    }

    /**
     * 封装返回结果
     * @param originFilename
     * @param file
     * @param uploadPath
     * @param imageInfo
     * @return
     */
    private UploadPictureResult buildResult(String originFilename, File file, String uploadPath, ImageInfo imageInfo) {
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
    }

}
