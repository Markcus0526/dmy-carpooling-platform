package com.nmdy.operation.share.service;

import java.util.List;

import com.nmdy.operation.share.dao.ShareEntity;




public interface ShareService {
	public List<ShareEntity> findvalue();
	public int update1(String  value);//更新id=3
	public int update2(String  value);//更新id=4
	public int update3(String  value);//更新id=5

}
