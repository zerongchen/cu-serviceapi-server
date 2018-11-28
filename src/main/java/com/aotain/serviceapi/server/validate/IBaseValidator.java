package com.aotain.serviceapi.server.validate;

import com.aotain.cu.serviceapi.dto.AjaxValidationResult;
import com.aotain.cu.serviceapi.dto.BaseDTO;
import com.aotain.cu.serviceapi.model.BaseModel;

/**
 * 校验器类的公共接口类
 *
 * @author bang
 * @date 2018/07/11
 */
public interface IBaseValidator {
    /**
     * 待各个validator实现
     *  只进行简单的属性校验(属性非空 长度 格式) 不涉及数据库相关校验
     * @param baseModel
     * @return
     */
    AjaxValidationResult validateBean(BaseModel baseModel);

    /**
     * 预审校验 唯一性 存在性校验 完整性
     * @param baseModel
     * @return
     */
    AjaxValidationResult preValidate(BaseModel baseModel);

    /**
     * 上报校验 唯一性 存在性校验 完整性
     * @param baseModel
     * @return
     */
    AjaxValidationResult repValidate(BaseModel baseModel);
}
