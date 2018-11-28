package com.aotain.serviceapi.server.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * 基础代码通过redis来读取<br>
 * 如果redis中有idc_jcdm_table的数据，就不加载，如果没有，就要先加载数据，通过redis中的key:BMDataLoadFlag来判断<br>
 * 文件校验的时候需要兼容CODE和MC，所以redis设计的时候需要把CODE和MC也加载进去<br>
 * 结构设计：以IDC_JCDM_YWLX（业务类型表）为例，redis里面是以IDC_JCDM_YWLX:MC和IDC_JCDM_YWLX:CODE都作为key，整条数据库记录为set的value<br>
 * @author ligh
 */
public enum BMDataUtil {

	instance(); 
	
	private Set<String> yzbmPostCodeSet = new HashSet<String>(); 
	
	private Map<String, Set<String>> yzbmMcMapForPostCodeSet = new HashMap<String, Set<String>>(); 
	
	private Map<String, Set<String>> hjxjfSetMap = new HashMap<String, Set<String>>(); 
	
	private Map<String, Boolean> isHjxjfMap = new HashMap<String, Boolean>(); 
	
	private Map<String, Map<String, Map<String, String>>> tableCodeMap = new HashMap<String, Map<String,Map<String,String>>>(); 
	
	private Map<String, Map<String, Map<String, String>>> tableCodeAndMcMap = new HashMap<String, Map<String,Map<String,String>>>(); 
	
	private RedisTemplate<String, String> redisTemplate; 
	
	private ReentrantReadWriteLock lock; 
	
	private ReadLock readLock; 
	
	private WriteLock writeLock; 
	
	private BMDataUtil() { 
		redisTemplate = (RedisTemplate<String, String>) SpringContextTool.getBean("redisTemplate"); 
		lock = new ReentrantReadWriteLock(); 
		readLock = lock.readLock(); 
		writeLock = lock.writeLock(); 
		new Thread(new Runnable() {
			@Override
			public void run() {
				loadWithLock(); 
			}
		}).start();
	} 
	
	public void reload() {
		loadWithLockForce(); 
	} 
	
	public void loadWithLock() { 
		try {
			writeLock.lock(); 
			if (!isBMDataLoaded()) { 
				//initBMData(null); 
				afterLoad(); 
			} 
		} catch(Exception e) {
			e.printStackTrace(); 
		} finally { 
			writeLock.unlock(); 
		} 
	} 
	
	public void loadWithLockForce() { 
		try {
			writeLock.lock(); 
			//initBMData(null); 
			afterLoad(); 
		} catch(Exception e) {
			e.printStackTrace(); 
		} finally { 
			writeLock.unlock(); 
		} 
	} 
	
	public void reload(String tableName) { 
		try {
			writeLock.lock(); 
			//initBMData(tableName); 
		} catch (Exception e) {
			e.printStackTrace(); 
		} finally { 
			writeLock.unlock(); 
		}
	}
	
	/**
	 * 初始化redis表码表
	 */
	/*private void initBMData(String tableName) {
		CommonService commonService = (CommonService) SpringContextTool.getBean("commonService");
		Map<String, Map<String, Map<String, String>>> bmTableMap = commonService.getBMData(tableName); 
		writeBmTableToRedis(bmTableMap); 
		initYzbmPostCodeSet(bmTableMap.get(BasicDataValidateUtils.IDC_JCDM_YZBM)); 
		initYzbmMcMapForPostCodeSet(bmTableMap.get(BasicDataValidateUtils.IDC_JCDM_YZBM));
		initGdXzqydm(bmTableMap.get(BasicDataValidateUtils.IDC_JCDM_XZQYDM)); 
		initHjxjf(bmTableMap.get(BasicDataValidateUtils.IDC_JCDM_HJXJF)); 
		// 加载完后，需要把内存清空
		clearAllData(); 
	} */
	
	private void clearAllData() {
		yzbmMcMapForPostCodeSet.clear(); 
		hjxjfSetMap.clear(); 
		isHjxjfMap.clear(); 
		tableCodeMap.clear(); 
		tableCodeAndMcMap.clear(); 
	}

	private void writeBmTableToRedis(Map<String, Map<String, Map<String, String>>> bmTableMap) { 
		for (Entry<String, Map<String, Map<String, String>>> entry : bmTableMap.entrySet()) { 
			String tableName = entry.getKey(); 
			redisTemplate.delete(tableName); 
			if (!tableName.equals(BasicDataValidateUtils.IDC_JCDM_YZBM)) { // 邮政编码和行政区域代码不存
				for (Entry<String, Map<String, String>> recordDatas : entry.getValue().entrySet()) {
					String key = recordDatas.getKey(); 
					Map<String, String> recordData = recordDatas.getValue(); 
					if (tableName.equals(BasicDataValidateUtils.IDC_JCDM_XZQYDM)) { 
						redisTemplate.boundSetOps(tableName).add(tableName + ":" + recordData.get("CODE")); 
						if (!recordData.get("CODE").equals(recordData.get("PARENTCODE"))) { // 不设限的地级市的问题：CODE=PARENTCODE
							redisTemplate.delete(tableName + ":" + recordData.get("CODE")); 
							redisTemplate.boundHashOps(tableName + ":" + recordData.get("CODE")).putAll(recordData); 
						} 
						// 白云区有多个，所以不能按照mc来设置key，否则会覆盖
						// 名称：省：省；市：省+市；区（县）：省+市+区（县）
						if (recordData.get("CODELEVEL").equals("1")) { 
							redisTemplate.delete(tableName + ":" + recordData.get("MC")); 
							redisTemplate.boundHashOps(tableName + ":" + recordData.get("MC")).putAll(recordData); 
						} else if (recordData.get("CODELEVEL").equals("2")) { 
							String provinceMc = (String) redisTemplate.boundHashOps(tableName + ":" + recordData.get("PARENTCODE")).get("MC"); 
							redisTemplate.delete(tableName + ":" + provinceMc + recordData.get("MC")); 
							redisTemplate.boundHashOps(tableName + ":" + provinceMc + recordData.get("MC")).putAll(recordData); 
						} else if (recordData.get("CODELEVEL").equals("3")) { 
							Map<Object, Object> cityMap = redisTemplate.boundHashOps(tableName + ":" + recordData.get("PARENTCODE")).entries(); 
							String provinceMc = (String) redisTemplate.boundHashOps(tableName + ":" + cityMap.get("PARENTCODE")).entries().get("MC"); 
							redisTemplate.delete(tableName + ":" + provinceMc + cityMap.get("MC") + recordData.get("MC")); 
							redisTemplate.boundHashOps(tableName + ":" + provinceMc + cityMap.get("MC") + recordData.get("MC")).putAll(recordData); 
						} 
					} else { 
						redisTemplate.boundSetOps(tableName).add(tableName + ":" + key); 
						redisTemplate.delete(tableName + ":" + key);
						redisTemplate.boundHashOps(tableName + ":" + key).putAll(recordData); 
						redisTemplate.delete(tableName + ":" + recordData.get("MC"));
						redisTemplate.boundHashOps(tableName + ":" + recordData.get("MC")).putAll(recordData); 
					}
				} 
			}
		} 
	} 
	
	private void afterLoad() { 
		redisTemplate.boundValueOps("BMDataLoadFlag").set("true"); 
	}

	/**
	 * 初始化yzbmPostCodeSet
	 */
	private void initYzbmPostCodeSet(Map<String, Map<String, String>> map) {
		if (map != null) {
			Set<String> yzbmPostCodeSet = new HashSet<String>();
			for (Entry<String, Map<String, String>> entry : map.entrySet()) {
				Map<String, String> record = entry.getValue();
				if (!yzbmPostCodeSet.contains(record.get("POST_CODE"))) {
					yzbmPostCodeSet.add(record.get("POST_CODE"));
				} 
			} 
			redisTemplate.delete("yzbmPostCodeSet");
			for (String entry : yzbmPostCodeSet) { 
				redisTemplate.boundSetOps("yzbmPostCodeSet").add(entry); 
			} 
		} 
	} 
	
	public Set<String> getYzbmPostCodeSet() { 
		if (!yzbmPostCodeSet.isEmpty()) { 
			return yzbmPostCodeSet; 
		} 
		try { 
			checkAndLoadBMDataWithReadLock(); 
			if (!yzbmPostCodeSet.isEmpty()) { 
				return yzbmPostCodeSet; 
			}
			yzbmPostCodeSet.addAll(redisTemplate.opsForSet().members("yzbmPostCodeSet")); 
			return yzbmPostCodeSet; 
		} finally { 
			unlockReadLock(); 
		}
	} 
	/**
	 * 初始化yzbmMcMapForPostCodeSet
	 */
	private void initYzbmMcMapForPostCodeSet(Map<String, Map<String, String>> map) {
		if (map != null) {
			Map<String, Set<String>> yzbmMcMapForPostCodeSet = new HashMap<String, Set<String>>();
			for (Entry<String, Map<String, String>> entry : map.entrySet()) {
				Map<String, String> record = entry.getValue(); 
				if (!yzbmMcMapForPostCodeSet.containsKey(record.get("MC"))) {
					Set<String> postCodes = new HashSet<String>();
					yzbmMcMapForPostCodeSet.put(record.get("MC"), postCodes);
				} 
				yzbmMcMapForPostCodeSet.get(record.get("MC")).add(record.get("POST_CODE"));
			} 
			for (Entry<String, Set<String>> entry : yzbmMcMapForPostCodeSet.entrySet()) {
				String[] type = new String[0]; 
				redisTemplate.delete("yzbmMcMapForPostCodeSet" + ":" + entry.getKey());
				redisTemplate.boundSetOps("yzbmMcMapForPostCodeSet" + ":" + entry.getKey()).add(entry.getValue().toArray(type)); 
			} 
		}
	} 
	
	public Set<String> getYzbmMcMapForPostCodeSet(String codeOrMc) { 
		if (yzbmMcMapForPostCodeSet.containsKey("yzbmMcMapForPostCodeSet:" + codeOrMc)) { 
			return yzbmMcMapForPostCodeSet.get("yzbmMcMapForPostCodeSet:" + codeOrMc); 
		} 
		try { 
			checkAndLoadBMDataWithReadLock(); 
			if (yzbmMcMapForPostCodeSet.containsKey("yzbmMcMapForPostCodeSet:" + codeOrMc)) { 
				return yzbmMcMapForPostCodeSet.get("yzbmMcMapForPostCodeSet:" + codeOrMc); 
			} 
			Set<String> set = redisTemplate.opsForSet().members("yzbmMcMapForPostCodeSet:" + codeOrMc);
			yzbmMcMapForPostCodeSet.put("yzbmMcMapForPostCodeSet:" + codeOrMc, set); 
			return yzbmMcMapForPostCodeSet.get("yzbmMcMapForPostCodeSet:" + codeOrMc); 
		} finally { 
			unlockReadLock(); 
		}
	} 
	
	/**
	 * 初始化广东省的行政区域代码
	 */
	private void initGdXzqydm(Map<String, Map<String, String>> map) {
		if (map != null) {
			Map<String, Map<String, String>> gdXzqydmMap = new HashMap<String, Map<String, String>>(); 
			for (Entry<String, Map<String, String>> entry : map.entrySet()) {
				Map<String, String> record = entry.getValue(); 
				if ((record.containsKey("PARENTCODE") && record.get("PARENTCODE") != null  && record.get("PARENTCODE").equals(BasicDataValidateUtils.GDXZQYDM_CODE)) 
						|| (record.get("CODE").equals(BasicDataValidateUtils.GDXZQYDM_CODE))) { 
					if (gdXzqydmMap.containsKey(record.get("CODE")) == false) { 
						Map<String, String> recordMap = new HashMap<String, String>(); 
						recordMap.put("CODE", record.get("CODE")); 
						recordMap.put("MC", record.get("MC")); 
						gdXzqydmMap.put(record.get("CODE"), recordMap); 
					}
				}
			} 
			for (Entry<String, Map<String, String>> entry : gdXzqydmMap.entrySet()) { 
				redisTemplate.delete("gdXzqydmMap" + ":" + entry.getKey());
				redisTemplate.boundHashOps("gdXzqydmMap" + ":" + entry.getKey()).putAll(entry.getValue()); // 区域代码
				redisTemplate.delete("gdXzqydmMap" + ":" + entry.getValue().get("MC"));
				redisTemplate.boundHashOps("gdXzqydmMap" + ":" + entry.getValue().get("MC")).putAll(entry.getValue()); // 名称
			}
		}
	} 
	/**
	 * 初始化汇聚型机房的行政区域代码
	 * @param map
	 */
	private void initHjxjf(Map<String, Map<String, String>> map) {
		if (map != null) { 
			Map<String, Set<String>> mapSet = new HashMap<String, Set<String>>(); 
			Map<String, Map<String, String>> mapMap = new HashMap<String, Map<String,String>>(); 
			for (Entry<String, Map<String, String>> entry : map.entrySet()) { 
				Map<String, String> record = entry.getValue(); 
				String code = BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + record.get("HOUSENAME") + ":" + record.get("CODE"); 
				String name = BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + record.get("HOUSENAME") + ":" + record.get("MC"); 
				if (mapSet.containsKey(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + record.get("HOUSENAME")) == false) { 
					Set<String> set = new HashSet<String>(); 
					mapSet.put(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + record.get("HOUSENAME"), set); 
				}
				Set<String> set = mapSet.get(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + record.get("HOUSENAME")); 
				set.add(record.get("CODE")); 
				set.add(record.get("MC")); 
				mapSet.put(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + record.get("HOUSENAME"), set); 
				mapMap.put(code, record); 
				mapMap.put(name, record); 
			} 
			for (Entry<String, Set<String>> entry : mapSet.entrySet()) { 
				redisTemplate.delete(entry.getKey());
				redisTemplate.boundSetOps(entry.getKey()).add(entry.getValue().toArray(new String[0])); 
			} 
			for (Entry<String, Map<String, String>> entry : mapMap.entrySet()) { 
				redisTemplate.delete(entry.getKey());
				redisTemplate.boundHashOps(entry.getKey()).putAll(entry.getValue()); 
			}
		}
	} 
	
	public boolean isHjxjf(String houseName) { 
		if (isHjxjfMap.containsKey(houseName)) { 
			return isHjxjfMap.get(houseName); 
		} 
		try { 
			checkAndLoadBMDataWithReadLock(); 
			if (isHjxjfMap.containsKey(houseName)) { 
				return isHjxjfMap.get(houseName); 
			}
			isHjxjfMap.put(houseName, redisTemplate.hasKey(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + houseName)); 
			return isHjxjfMap.get(houseName); 
		} finally { 
			unlockReadLock(); 
		}
	} 
	
	public Set<String> getAreaCodeByHouseName(String houseName) { 
		if (hjxjfSetMap.containsKey(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + houseName)) { 
			return hjxjfSetMap.get(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + houseName); 
		} 
		try { 
			checkAndLoadBMDataWithReadLock(); 
			if (hjxjfSetMap.containsKey(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + houseName)) { 
				return hjxjfSetMap.get(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + houseName); 
			} 
			Set<String> set = redisTemplate.opsForSet().members(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + houseName); 
			hjxjfSetMap.put(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + houseName, set); 
			return hjxjfSetMap.get(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":" + houseName); 
		} finally { 
			unlockReadLock(); 
		} 
	} 
	
	public Map<String, Map<String, String>> getTableDataMap(String tableName) {
		if (tableCodeMap.containsKey(tableName)) { 
			return tableCodeMap.get(tableName); 
		} 
		try { 
			checkAndLoadBMDataWithReadLock(); 
			if (tableCodeMap.containsKey(tableName)) { 
				return tableCodeMap.get(tableName); 
			}
			Set<String> set = redisTemplate.boundSetOps(tableName).members(); 
			Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>(set.size()); 
			for (String key : set) { 
				HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash(); 
				Map<String, String> keyMap = hashOperations.entries(key); 
				map.put(key, keyMap); 
			} 
			if (!tableName.equals(BasicDataValidateUtils.IDC_JCDM_YZBM) && !tableName.equals(BasicDataValidateUtils.IDC_JCDM_XZQYDM)) { 
				for (Entry<String, Map<String, String>> entry : map.entrySet()) { 
					String key = entry.getKey(); 
					if (!tableCodeMap.containsKey(tableName)) { 
						tableCodeMap.put(tableName, new HashMap<String, Map<String,String>>()); 
					}
					tableCodeMap.get(tableName).put(key, entry.getValue());
				} 
			}
			return map;
		} finally { 
			unlockReadLock(); 
		}
	} 
	
	public Map<String, String> getDataMapByTableNameAndId(String tableName, String codeOrMc) {
		if (tableCodeAndMcMap.containsKey(tableName)) { 
			if (tableCodeAndMcMap.get(tableName).containsKey((tableName + ":" + codeOrMc).intern())) { 
				return tableCodeAndMcMap.get(tableName).get(tableName + ":" + codeOrMc); 
			} 
		} 
		try { 
			checkAndLoadBMDataWithReadLock(); 
			if (tableCodeAndMcMap.containsKey(tableName)) { 
				if (tableCodeAndMcMap.get(tableName).containsKey((tableName + ":" + codeOrMc).intern())) { 
					return tableCodeAndMcMap.get(tableName).get(tableName + ":" + codeOrMc); 
				} 
			} 
			HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash(); 
			Map<String, String> entries = hashOperations.entries(tableName + ":" + codeOrMc); 
			if (!tableName.equals(BasicDataValidateUtils.IDC_JCDM_YZBM)) { 
				if (!tableCodeAndMcMap.containsKey(tableName)) { 
					tableCodeAndMcMap.put(tableName, new HashMap<String, Map<String,String>>()); 
				} 
				if (tableName.equals(BasicDataValidateUtils.IDC_JCDM_PPFW) 
						|| tableName.equals("gdXzqydmMap") 
						|| tableName.startsWith(BasicDataValidateUtils.IDC_JCDM_HJXJF + ":")) { 
					tableCodeAndMcMap.get(tableName).put(tableName + ":" + entries.get("CODE"), entries); 
					tableCodeAndMcMap.get(tableName).put(tableName + ":" + entries.get("MC"), entries); 
				} else if (tableName.equals(BasicDataValidateUtils.IDC_JCDM_XZQYDM)) {
					tableCodeAndMcMap.get(tableName).put(tableName + ":" + entries.get("CODE"), entries); 
					tableCodeAndMcMap.get(tableName).put(tableName + ":" + codeOrMc, entries); 
				} else { 
					tableCodeAndMcMap.get(tableName).put(tableName + ":" + entries.get("ID"), entries); 
					tableCodeAndMcMap.get(tableName).put(tableName + ":" + entries.get("MC"), entries); 
				} 
			} 
			return tableCodeAndMcMap.get(tableName).get(tableName + ":" + codeOrMc); 
		} finally { 
			unlockReadLock(); 
		}
	} 
	
	private boolean isBMDataLoaded() {
		if (redisTemplate.opsForValue().get("BMDataLoadFlag") != null && redisTemplate.opsForValue().get("BMDataLoadFlag").equals("true")) {
			return true; 
		} 
		return false; 
	}
	
	private void checkAndLoadBMDataWithReadLock() { 
		readLock.lock(); 
		if (!isBMDataLoaded()) {
			readLock.unlock(); 
			loadWithLock(); 
			readLock.lock(); 
		} 
	} 
	
	private void unlockReadLock() { 
		readLock.unlock(); 
	}
}
