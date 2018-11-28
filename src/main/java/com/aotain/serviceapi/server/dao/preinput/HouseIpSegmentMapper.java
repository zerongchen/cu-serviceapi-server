package com.aotain.serviceapi.server.dao.preinput;

import com.aotain.common.config.annotation.MyBatisDao;
import com.aotain.cu.serviceapi.dto.HouseIPSegmentInforDTO;
import com.aotain.cu.serviceapi.model.HouseIPSegmentInformation;

import java.util.List;
import java.util.Map;

/**
 * Demo class
 *
 * @author bang
 * @date 2018/07/16
 */
@MyBatisDao
public interface HouseIpSegmentMapper {

    /**
     * 根据ipSegNo查询记录
     * @param ipSegNo
     * @return
     */
    HouseIPSegmentInformation findByIpSegNo(String ipSegNo);

    /**
     * 统计冲突ip地址段个数
     * @param dto
     * @return
     */
   int countConflictIpSegment(HouseIPSegmentInformation dto);

    /**
     * 专线或者虚拟机ip冲突
     * @param dto
     * @return
     */
   int countIpSegmentForSpecial(HouseIPSegmentInformation dto);

    /**
     * 相同名称的专线机房或者云虚拟机是否存在冲突
     * @param dto
     * @return
     */
    int countIpSegmentSpecialForSameUnit(HouseIPSegmentInformation dto);

    /**
     * 机房IP分页查询
     * @param dto
     * @return
     */
    List<HouseIPSegmentInforDTO> getIndexHouseFrame(HouseIPSegmentInforDTO dto);

    /**
     * 分页查询链路信息
     * @param query
     * @return
     */
    List<HouseIPSegmentInforDTO> getIndexHouseIpSegment(HouseIPSegmentInforDTO query);


    /**
     * 新增机房IP地址段信息
     * @param dto
     * @return
     */
    int insertHouseIpSegment(HouseIPSegmentInformation dto);

    /**
     * 更新机房IP地址段信息
     * @param dto
     * @return
     */
    int updateHouseIpSegment(HouseIPSegmentInformation dto);

    /**
     * 
     * 删除机房下的IP地址段信息
     */
	int deleteByHouseId(long houseId);

	/**
	 * 根据ipsegId删除Ip地址段信息
	 * 
	 */
	int deleteByIpsegId(long ipsegId);

	/**
	 * 根据ipsegId查询记录
	 * 
	 */
	HouseIPSegmentInformation findByIpSegId(long ipsegId);

    /**
     * 根据起始结束和ip使用方式查询记录
     *
     */
    HouseIPSegmentInformation findByIpAndType(HouseIPSegmentInforDTO dto);

	/**
	 * 
	 * 根据ipsegId更新IP地址段状态信息
	 */
	int updateIPSegtatusByIpsegId(Map<String, Object> map);

	/**
	 * 查找冲突的ip地址段集合
	 * 
	 */
	List<HouseIPSegmentInformation> findIpListExistConflick(HouseIPSegmentInformation dto);

	/**
	 * 删除用户时级联释放与用户关联的IP段信息
	 * 
	 */
	int dealIpsegWhileUserDelete(long userId);

	/**
	 * 删除用户时，根据userid修改关联已报备的操作类型和报备状态
	 * 
	 */
	int updateIpStatuByUserId(long userId);

	/**
	 * 查询与用户关联的ip地址段的机房id信息
	 * 
	 */
	List<Long> findIpsegHouseIdsByUserId(long userId);

	/**
	 * 查询与db冲突的ip地址段信息
	 * 
	 * @author : songl
	 */
	List<HouseIPSegmentInformation> findConflickIp(HouseIPSegmentInformation dto);
	
	/**
	 * 查询与db重复的ip地址段信息（专线或云虚拟ip类型）
	 * 
	 * @author : songl
	 */
	List<HouseIPSegmentInformation> findRepeatIp(HouseIPSegmentInformation dto);
	
}
