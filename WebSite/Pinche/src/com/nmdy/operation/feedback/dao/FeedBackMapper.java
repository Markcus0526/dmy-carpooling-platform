package com.nmdy.operation.feedback.dao;

import java.util.List;
import java.util.Map;

import com.nmdy.base.SupperMapper;

/**
 * ç”¨æˆ·å��é¦ˆä¿¡æ�¯æ˜ å°„æŽ¥å�£
 * @author è‚–å¨œå¨œ
 * @Date 2014-08-09
 */
public interface FeedBackMapper extends SupperMapper{
	//é€šè¿‡æ�¡ä»¶æŸ¥è¯¢å��é¦ˆä¿¡æ�¯
	public  List<FeedBack> findByCondition(Map<String,Object> map);

	public int delete(long id);

	public int getCount(Map<String, Object> params);

	public FeedBack findById(long id);

	public int countByCondition(Map<String, Object> params);
	
}
