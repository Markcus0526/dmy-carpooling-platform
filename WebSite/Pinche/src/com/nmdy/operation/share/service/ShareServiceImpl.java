package com.nmdy.operation.share.service;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.nmdy.operation.share.dao.ShareEntity;
import com.nmdy.operation.share.dao.ShareMapper;



public class ShareServiceImpl implements ShareService {
	private ShareMapper sharemapper;
	private SqlSession sqlsession;

	public ShareServiceImpl() {
		System.out.println("impl");
		sqlsession = com.nmdy.common.SqlSessionHelper.getSession();
		sharemapper = sqlsession.getMapper(ShareMapper.class);

	}

	@Override
	public List<ShareEntity> findvalue() {
		// TODO Auto-generated method stub
		return sharemapper.findvalue();
	}

	@Override
	public int update1(String  value) {
		// TODO Auto-generated method stub
		int r = sharemapper.update1(value);
		if(r>0){
			sqlsession.commit();
		}
		return r;
	}

	@Override
	public int update2(String  value) {
		// TODO Auto-generated method stub
		int r=sharemapper.update2(value);
		if(r>0){
			sqlsession.commit();
			
		}
		return r;
	}

	@Override
	public int update3(String  value) {
		// TODO Auto-generated method stub
		int r = sharemapper.update3(value);
		if(r>0){
			sqlsession.commit();
		}
		return r;
	}

}