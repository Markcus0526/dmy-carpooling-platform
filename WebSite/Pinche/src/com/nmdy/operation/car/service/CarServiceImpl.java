package com.nmdy.operation.car.service;


import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.nmdy.operation.car.dao.CarMapper;
import com.nmdy.operation.car.dao.carEntity;




public class CarServiceImpl implements CarService {
	private CarMapper carmapper;
	
	
	public CarMapper getCarmapper() {
		return carmapper;
	}

	public void setCarmapper(CarMapper carmapper) {
		this.carmapper = carmapper;
	}

	
//参照mapper方法名
	@Override
	public List<carEntity> findcarbrand() {
		// TODO Auto-generated method stub
		return carmapper.findcarbrand();
	}

	@Override
	public List<carEntity> findstylebybrand(String brand) {
		// TODO Auto-generated method stub
		return carmapper.findstylebybrand(brand);
	}

	@Override
	public List<carEntity> findcolordesc() {
		// TODO Auto-generated method stub
		return carmapper.findcolordesc();
	}

	@Override
	public int addColor(String color_desc) {
		// TODO Auto-generated method stub
		int row= carmapper.addColor(color_desc);

		return row;
	}

	@Override
	public int addBrand(String brand) {
		// TODO Auto-generated method stub
		int r= carmapper.addBrand(brand);
	
		return r;
	}

	@Override
	public int addStyle(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int r= carmapper.addStyle(params);
	
		return r;
	}

	@Override
	public int deleteBrand(String brand) {
		// TODO Auto-generated method stub
		int r= carmapper.deleteBrand(brand);
	
		return r;
	}

	@Override
	public int deleteStyle(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int r= carmapper.deleteStyle(params);

		return r;
	}

	@Override
	public int deleteColor(String color_desc) {
		// TODO Auto-generated method stub
		int r= carmapper.deleteColor(color_desc);

		return r;
	}

	@Override
	public int updateSave(Map<String, Object> params) {
		// TODO Auto-generated method stub
		int r= carmapper.updatesave(params);

		return r;
	}

	@Override
	public int getTotal() {
		// TODO Auto-generated method stub
		return carmapper.getTotal();
	}

	@Override
	public List<carEntity> outto(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return carmapper.outto(params);
	}
	//@Override
//	public int test() {
//		CarServiceImpl a = new CarServiceImpl();
//		// TODO Auto-generated method stub
//		
//		Connection b=a.sqlsession1.getConnection();
//		
//		try {
//			ps=b.prepareStatement("");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	@Override
	public carEntity findtype(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return carmapper.findtype(params);
	}

	@Override
	public int samebrand(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return carmapper.samebrand(params);
	}

	@Override
	public int samestyle(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return carmapper.samestyle(params);
	}

	@Override
	public int samecolor(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return carmapper.samecolor(params);
	}

	@Override
	public int insamebrand(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return carmapper.insamebrand(params);
	}

	@Override
	public int insamestyle(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return carmapper.insamestyle(params);
	}

	@Override
	public int insamecolor(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return carmapper.insamecolor(params);
	}


}
