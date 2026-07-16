package com.nmdy.operation.evaluate.service;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nmdy.operation.evaluate.dao.EvaluateEntity;
import com.nmdy.operation.evaluate.dao.EvaluateMapper;
import com.nmdy.operation.evaluate.dao.HelpEntity;



public class EvaluateServiceImpl implements EvaluateService {
	private EvaluateMapper evaluatemapper ;


public EvaluateMapper getEvaluatemapper() {
		return evaluatemapper;
	}

	public void setEvaluatemapper(EvaluateMapper evaluatemapper) {
		this.evaluatemapper = evaluatemapper;
	}

	//params 需要提交表单之后才能获得
	@Override
	public List<EvaluateEntity> find(Map<String, Object> params) {
		System.out.println("find----begin");
		// TODO Auto-generated method stub
		return evaluatemapper.finduser(params);
	}

	@Override
	public int getCount(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return evaluatemapper.getTotal(params);
	}
	@Override
	public EvaluateEntity findbyid(int userid) {
		// TODO Auto-generated method stub
		return evaluatemapper.findbyid(userid);
	}
	@Override
	public  List<EvaluateEntity> findbyid2(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return evaluatemapper.findbyid2(params);
	}
	@Override
	public int update(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int row= evaluatemapper.update(params);

		return row;
	}
	@Override
	public int getCount2(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return evaluatemapper.getTotal2(params);
	}
	@Override
	public int updatedel(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int row= evaluatemapper.updatedel(params);
	
		return row;
	}
	@Override
	public HelpEntity findbyid1(int userid) {
		// TODO Auto-generated method stub
		return evaluatemapper.findbyid1(userid);
	}

	@Override
	public Map<String, Object> findmsg(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return evaluatemapper.findmsg(params);
	}
	
	
	
	

}
