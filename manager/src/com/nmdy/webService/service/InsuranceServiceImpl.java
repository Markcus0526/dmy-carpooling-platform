package com.nmdy.webService.service;



import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import com.nmdy.webService.dao.Application;
import com.nmdy.webService.dao.Base_part;
import com.nmdy.webService.dao.Benefit_data;
import com.nmdy.webService.dao.Body;
import com.nmdy.webService.dao.Head;
import com.nmdy.webService.dao.Insurance;
import com.nmdy.webService.dao.InsuranceDTD;
import com.nmdy.webService.dao.InsuranceMapper;
import com.nmdy.webService.dao.Insured_data;
import com.nmdy.webService.dao.Insureds;

//@WebService(endpointInterface = "com.nmdy.webService.service.InsuranceService", serviceName = "InsuranceService",name="http://service.webService.nmdy.com",portName="http://service.webService.nmdy.com",targetNamespace="http://service.webService.nmdy.com")
//@WebService(endpointInterface="com.nmdy.webService.service.InsuranceService", serviceName="InsuranceService",portName="http://service.webService.nmdy.com",targetNamespace="http://service.webService.nmdy.com/")
@WebService(endpointInterface="com.nmdy.webService.service.InsuranceService", serviceName="InsuranceService",targetNamespace="http://service.webService.nmdy.com")
public class InsuranceServiceImpl implements InsuranceService {

	/**
	 * 
	 * @param start   		起始记录数
	 * @param number  		每次取多少条
	 * @return insuList  	保单数据列表
	 */
	public Insurance fetcheInsuData(int insuID){
		Connection conn = null;
		ResultSet rs = null;
		Insurance insurance = null;
		Statement stmt = null;
		try {
			conn= DBManager.getConnection();
			 stmt = conn.createStatement();
			String sql = "select "
							+"insurance.REQUEST_TYPE,"// 请求类型
							+"insurance.REQUSET_TIME,"// 请求时间
							+"insurance.REQUEST_TRANSNO,"// 交易流水号，合作方自己定义
							+"insurance.USER,"// 保险公司分配给合作方的账号
							+"insurance.PASSWORD,"// 保险公司分配给合作方的密码
							
							+"insurance.APPL_NO,"// 投保单号
							+"insurance.APPL_TIME,"// 投保时间，当前时间
							+"insurance.PROD_TYPE,"// 承包产品类型
							+"insurance.PROD_CODE,"// 承包产品代码
							+"insurance.EFFECT_TIME,"// 保单生效时间
							+"insurance.INSEXPR_DATE,"// 保单失效时间
							+"insurance.BUS_BRANCH,"// 业务受理机构
							+"insurance.SALE_CHANNEL,"// 销售渠道
							+"insurance.COUNTER_CODE,"// 网店代码
							+"insurance.TOTAL_AMOUNT,"// 保额
							+"insurance.TOTAL_PREMINUM,"// 合计保费，insu_fee
							
							+"insurance.APPL_NAME,"// 投保人姓名
							+"insurance.APPL_SEX,"// 投保人性别
							+"insurance.APPL_BIRTHDAY,"// 投保人生日
							+"insurance.CERT_TYPE,"// 投保人证件类型
							+"insurance.CERT_CODE,"// 投保人证件号码
							
							+"insurance.ISD_COUNT,"// 被保险人数

							+"insurance.ISD_SERIAL,"// 被保险人顺序
							+"insurance.APPL_RELATION, "// 投保人和被保人顺序，司乘
							+"insurance.ISD_TYPE,"// 被保险人类型
							+"insurance.ISD_NAME,"// 被保人姓名
							+"insurance.ISD_SEX,"// 被保人性别
							+"insurance.ISD_BIRTHDAY,"// 被保险人生日
							+"insurance.ISD_CERT_TYPE,"// 被保险人证件类型
							+"insurance.ISD_CERT_CODE,"// 被保险人证件号码
							+"insurance.BENEF_ORDER,"// 受益顺序
							
							//+"insurance.APPL_ID,"// 投保人id
							//+"insurance.INSU_STATUS,"// 保单状态
							//+"insurance.ISD_ID,"// 被保人id
							+"insurance.id "// 保单id，用于更改保单状态
						+" from insurance  "
						+"where  id ="+insuID;
						//+"where hasFetched = 0 and id >= "+start+"  limit 0 , "+number;
			
			System.out.println("HelloWorld.fetcheInsuData()============"+sql);
			rs=stmt.executeQuery(sql);
	
				if(rs.next()) {
				
				String request_type =rs.getString("REQUEST_TYPE");
				Date requset_time =rs.getTimestamp("REQUSET_TIME");
				String request_transno =rs.getString("REQUEST_TRANSNO");
				String user =rs.getString("USER");
				String password =rs.getString("PASSWORD");
				
				String appl_no =rs.getString("APPL_NO");
				Date appl_time =rs.getTimestamp("APPL_TIME");
				String prod_type =rs.getString("PROD_TYPE");
				String prod_code =rs.getString("PROD_CODE");
				Date effect_time =rs.getTimestamp("EFFECT_TIME");
				Date insexpr_date =rs.getTimestamp("INSEXPR_DATE");
				String bus_branch =rs.getString("BUS_BRANCH");
				String sale_channel =rs.getString("SALE_CHANNEL");
				String counter_code =rs.getString("COUNTER_CODE");
				BigDecimal total_amount =rs.getBigDecimal("TOTAL_AMOUNT");
				BigDecimal total_preminum =rs.getBigDecimal("TOTAL_PREMINUM");
				
				String appl_name =rs.getString("APPL_NAME");
				String appl_sex =rs.getString("APPL_SEX");
				Date appl_birthday =rs.getDate("APPL_BIRTHDAY");
				String cert_type =rs.getString("CERT_TYPE");
				String cert_code =rs.getString("CERT_CODE");
				
				int isd_count = rs.getInt("ISD_COUNT");
				
				int isd_serial =rs.getInt("ISD_SERIAL");
				String isd_type =rs.getString("ISD_TYPE");
				String appl_relation =rs.getString("APPL_RELATION");
				String isd_name =rs.getString("ISD_NAME");
				String isd_sex =rs.getString("ISD_SEX");
				Date isd_birthday =rs.getDate("ISD_BIRTHDAY");
				String isd_cert_type =rs.getString("ISD_CERT_TYPE");
				String isd_cert_code =rs.getString("ISD_CERT_CODE");
				int benef_order =rs.getInt("BENEF_ORDER");
				

				
				long id =rs.getLong("id");
				System.out.println("=id==="+id);
				System.out.println("==appl_name=="+appl_name);
			
				
				insurance = new Insurance();
				Head head = new Head();
				Body body =new Body();
				Application application = new Application();
				Base_part base_part = new Base_part();
				Benefit_data benefit_data = new Benefit_data();
				Insured_data insured_data = new Insured_data();
				Insureds insureds = new Insureds();
				List<Insured_data> insured_List=new ArrayList<Insured_data>();
				List<Benefit_data> benefit_List = new ArrayList<Benefit_data>();
				
				//<HEAD>
				head.setRequest_type(request_type);
				head.setRequest_time(requset_time);//requset_time和request_time，字母颠倒了，保险文档里是前者，xml历史后者
				head.setRequest_transno(request_transno);
				head.setUser(user);
				head.setPassword(password);
				//</HEAD>
				//<BASE_PART>
				base_part.setAppl_no(appl_no);
				base_part.setAppl_time(appl_time);
				base_part.setBus_branch(bus_branch);
				base_part.setCounter_code(counter_code);
				base_part.setEffect_time(effect_time);
				base_part.setInsexpr_date(insexpr_date);
				base_part.setProd_code(prod_code);
				base_part.setProd_type(prod_type);
				base_part.setSale_channel(sale_channel);
				base_part.setTotal_amount(total_amount);
				base_part.setTotal_preminum(total_preminum);
				//</BASE_PART>
				//<APPLICATION>
				application.setAppl_name(appl_name);
				application.setBirthday(appl_birthday);
				application.setSex(appl_sex);
				application.setCert_type(cert_type);
				application.setCert_code(cert_code);
				//</APPLICATION>
				//<INSUREDS>
				insureds.setIsd_count(String.valueOf(isd_count));
				
				//<INSURED_DATA>
				insured_data.setIsd_serial(isd_serial);
				insured_data.setIsd_type(isd_type);
				insured_data.setAppl_relation(appl_relation);
				insured_data.setIsd_name(isd_name);
				insured_data.setSex(isd_sex);
				insured_data.setBirthday(isd_birthday);
				insured_data.setCert_type(isd_cert_type);
				insured_data.setCert_code(isd_cert_code);
				insured_data.setBenefit_List(benefit_List);
				
				//<BENEFIT_DATA>
				benefit_data.setBenef_order(benef_order);
				benefit_List.add(benefit_data);
				//</BENEFIT_DATA>
				insured_data.setBenefit_List(benefit_List);
				//</INSURED_DATA>
				insured_List.add(insured_data);
				
				insureds.setInsured_List(insured_List);
				//</INSUREDS>
				benefit_data.setBenef_order(benef_order);
				
				body.setApplication(application);
				body.setBase_part(base_part);
				body.setInsureds(insureds);
				
				insurance.setId(id);
				insurance.setHead(head);
				insurance.setBody(body);
				
				}
				stmt.close();
			
			
/*			System.out.println("ids======="+ids.toString());
			for (int j = 0; j < ids.length; j++) {
				
				String updateSQL = "update insurance set hasFetched=1 where id = "+ids[j];
				System.out.println("updateSQL===="+updateSQL);
				stmt.executeUpdate(updateSQL);
			}*/
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
		return insurance;
	
	
		
	}
	
	/**
	 * 
	 * @param number 一次取多少条数据
	 * @return insuranceDTDList  流水数据列表
	 */
	public List<InsuranceDTD> fetcheInsuDTD(int start,int end){
		
		

		Connection conn = null;
		ResultSet rs = null;
		Statement stmt=null;
		

		List<InsuranceDTD> insuranceDTDList = new ArrayList<InsuranceDTD>();
		
		try {
			conn= DBManager.getConnection();
			//conn.setAutoCommit(false);
			 stmt = conn.createStatement();
			String sql = "select "
							+"insurance_dtd.id,"// id
							+"insurance_dtd.insu_id,"// 保险id
							+"insurance_dtd.appl_no,"// 投保单号，合作方自己定义
							+"insurance_dtd.oper_type,"// 操作类型，1投保，2撤保
							+"insurance_dtd.oper_time,"
							//+"date_format(insurance_dtd.oper_time,'%Y-%m-%d %h:%m:%s') as oper_time,"// 操作时间
							+"insurance_dtd.insu_sum "// 保单金额
							//+"insurance_dtd.hasFetched"// 保险公司是否已读取，0未读取，1已读取
						+" from insurance_dtd  "
						+" where id>="+start+"   and id <= "+end;
			
			System.out.println("HelloWorld.fetcheInsuData()============"+sql);
			rs=stmt.executeQuery(sql);
			rs.last();
			int num = rs.getRow();
			rs.beforeFirst();
			System.out.println("num==="+num);
			//long[] ids = new long[num];
			for (int i = 0; i < num; i++) {
				if(rs.next()) {
				
				long id 		=rs.getLong("id");
				long insu_id 	=rs.getLong("insu_id");
				String appl_no	=rs.getString("appl_no");
				int oper_type 	=rs.getInt("oper_type");
				Date oper_time = rs.getTimestamp("oper_time");
				Date oper_time2 	=rs.getDate("oper_time");
				double insu_sum =rs.getDouble("insu_sum");
				//int hasFetched 	=rs.getInt("hasFetched");
				
				System.out.println("oper_time ======= "+oper_time);
				System.out.println("oper_time ======= "+oper_time2);
				
				
				
				InsuranceDTD insuDTD = new InsuranceDTD();
				insuDTD.setId(id);
				insuDTD.setInsu_id(insu_id);
				insuDTD.setAppl_no(appl_no);
				insuDTD.setOper_type(oper_type);
				insuDTD.setOper_time(oper_time);
				insuDTD.setInsu_sum(insu_sum);
				//insuDTD.setHasFetched(hasFetched);
				insuranceDTDList.add(insuDTD);
				
				//ids[i]=id;
				}
			}
			//rs.close();
			
/*			System.out.println("ids======="+ids.toString());
			for (int j = 0; j < ids.length; j++) {
				
				String updateSQL = "update insurance_dtd set hasFetched=1 where id = "+ids[j];
				System.out.println("updateSQL===="+updateSQL);
				stmt.executeUpdate(updateSQL);
			}*/
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
		return insuranceDTDList;
	
	
		
	
	}

	
	
	public boolean feedback(int insu_id, String appl_no, String error_msg) {
		
		Connection conn = null;
		Statement stmt=null;
		
		boolean flag = true;
		
		try {
			conn= DBManager.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			 
			String sql = "INSERT INTO  insu_error_msg"
							+"( insu_id,"// id
							+"appl_no,"// 投保单号，合作方自己定义
							+"error_msg )"// 错误类型
							+"VALUES ("
							+""+insu_id+" , "
							+"\'"+appl_no+"\' , "
							+"\'"+error_msg+"\' )";
			
			
			
			System.out.println("feedback()============"+sql);
			if(stmt.executeUpdate(sql)<1){
				
				flag = false;
			}else {
				flag = true;
			}


			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try {
				stmt.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return flag;
	}

	
	
/*	private InsuranceMapper insuranceMapper;
	
	
	public InsuranceMapper getInsuranceMapper() {
		return insuranceMapper;
	}

	public void setInsuranceMapper(InsuranceMapper insuranceMapper) {
		this.insuranceMapper = insuranceMapper;
	}
	List<Insurance> insuranceList = new ArrayList<Insurance>();
	
	
	 (non-Javadoc)
	 * @see com.nmdy.webService.service.InsuranceService#fetcheInsuData(int)
	 
	@Override
	public Insurance fetcheInsuData(int insuID){
		System.out.println("InsuranceServiceImpl.fetcheInsuData()------------------------");
			Map<String, Object> insuMap  = new HashMap<String, Object>();
			insuMap= insuranceMapper.fetcheInsuData(insuID);
				
				String request_type = (String) insuMap.get("request_type");
				Date requset_time =(Date) insuMap.get("requset_time");
				String request_transno =(String) insuMap.get("request_transno");
				String user =(String) insuMap.get("user");
				String password =(String) insuMap.get("PASSWORD");
				
				String appl_no =(String) insuMap.get("appl_no");
				Date appl_time =(Date) insuMap.get("appl_time");
				String prod_type =(String) insuMap.get("prod_type");
				String prod_code =(String) insuMap.get("prod_code");
				Date effect_time =(Date) insuMap.get("effect_time");
				Date insexpr_date =(Date) insuMap.get("insexpr_date");
				String bus_branch =(String) insuMap.get("bus_branch");
				String sale_channel =(String) insuMap.get("sale_channel");
				String counter_code =(String) insuMap.get("counter_code");
				BigDecimal total_amount =(BigDecimal) insuMap.get("total_amount");
				BigDecimal total_preminum =(BigDecimal) insuMap.get("total_preminum");
				
				String appl_name =(String) insuMap.get("appl_name");
				String appl_sex =(String) insuMap.get("appl_sex");
				Date appl_birthday =(Date) insuMap.get("appl_birthday");
				String cert_type =(String) insuMap.get("cert_type");
				String cert_code =(String) insuMap.get("cert_code");
				
				int isd_count = (int) insuMap.get("isd_count");
				
				int isd_serial =(int) insuMap.get("isd_serial");
				String isd_type =(String) insuMap.get("isd_type");
				String appl_relation =(String) insuMap.get("appl_relation");
				String isd_name =(String) insuMap.get("isd_name");
				String isd_sex =(String) insuMap.get("isd_sex");
				Date isd_birthday =(Date) insuMap.get("isd_birthday");
				String isd_cert_type =(String) insuMap.get("isd_cert_type");
				String isd_cert_code =(String) insuMap.get("isd_cert_code");
				int benef_order =(int) insuMap.get("benef_order");
				
				long id =(long) insuMap.get("id");
				
				Insurance insurance = new Insurance();
				Head head = new Head();
				Body body =new Body();
				Application application = new Application();
				Base_part base_part = new Base_part();
				Benefit_data benefit_data = new Benefit_data();
				Insured_data insured_data = new Insured_data();
				Insureds insureds = new Insureds();
				List<Insured_data> insured_List=new ArrayList<Insured_data>();
				List<Benefit_data> benefit_List = new ArrayList<Benefit_data>();
				
				//<HEAD>
				head.setRequest_type(request_type);
				head.setRequest_time(requset_time);//requset_time和request_time，字母颠倒了，保险文档里是前者，xml历史后者
				head.setRequest_transno(request_transno);
				head.setUser(user);
				head.setPassword(password);
				//</HEAD>
				//<BASE_PART>
				base_part.setAppl_no(appl_no);
				base_part.setAppl_time(appl_time);
				base_part.setBus_branch(bus_branch);
				base_part.setCounter_code(counter_code);
				base_part.setEffect_time(effect_time);
				base_part.setInsexpr_date(insexpr_date);
				base_part.setProd_code(prod_code);
				base_part.setProd_type(prod_type);
				base_part.setSale_channel(sale_channel);
				base_part.setTotal_amount(total_amount);
				base_part.setTotal_preminum(total_preminum);
				//</BASE_PART>
				//<APPLICATION>
				application.setAppl_name(appl_name);
				application.setBirthday(appl_birthday);
				application.setSex(appl_sex);
				application.setCert_type(cert_type);
				application.setCert_code(cert_code);
				//</APPLICATION>
				//<INSUREDS>
				insureds.setIsd_count(String.valueOf(isd_count));
				
				//<INSURED_DATA>
				insured_data.setIsd_serial(isd_serial);
				insured_data.setIsd_type(isd_type);
				insured_data.setAppl_relation(appl_relation);
				insured_data.setIsd_name(isd_name);
				insured_data.setSex(isd_sex);
				insured_data.setBirthday(isd_birthday);
				insured_data.setCert_type(isd_cert_type);
				insured_data.setCert_code(isd_cert_code);
				insured_data.setBenefit_List(benefit_List);
				
				//<BENEFIT_DATA>
				benefit_data.setBenef_order(benef_order);
				benefit_List.add(benefit_data);
				//</BENEFIT_DATA>
				insured_data.setBenefit_List(benefit_List);
				//</INSURED_DATA>
				insured_List.add(insured_data);
				
				insureds.setInsured_List(insured_List);
				//</INSUREDS>
				benefit_data.setBenef_order(benef_order);
				
				body.setApplication(application);
				body.setBase_part(base_part);
				body.setInsureds(insureds);
				
				insurance.setId(id);
				insurance.setHead(head);
				insurance.setBody(body);
				
				
			



	
		return insurance;
		
	}
	
	 (non-Javadoc)
	 * @see com.nmdy.webService.service.InsuranceService#fetcheInsuDTD(int, int)
	 
	@Override
	public List<InsuranceDTD> fetcheInsuDTD(int start,int end){
		System.out.println("InsuranceServiceImpl.fetcheInsuDTD()============================");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("start", start);
		params.put("end", end);
		List<Map<String, Object>> insuDTDList = insuranceMapper.fetcheInsuDTD(params);
		
		List<InsuranceDTD> insuranceDTDList = new ArrayList<InsuranceDTD>();

			for (int i = 0; i <insuDTDList.size()  ; i++) {
				long id 		= (long) insuDTDList.get(i).get("id");
				long insu_id 	= (long) insuDTDList.get(i).get("insu_id");
				String appl_no	=(String) insuDTDList.get(i).get("appl_no");
				int oper_type 	=(int) insuDTDList.get(i).get("oper_type");
				Date oper_time = (Date) insuDTDList.get(i).get("oper_time");
				Date oper_time2 	=(Date) insuDTDList.get(i).get("oper_time");
				double insu_sum =(double) insuDTDList.get(i).get("insu_sum");

				InsuranceDTD insuDTD = new InsuranceDTD();
				insuDTD.setId(id);
				insuDTD.setInsu_id(insu_id);
				insuDTD.setAppl_no(appl_no);
				insuDTD.setOper_type(oper_type);
				insuDTD.setOper_time(oper_time);
				insuDTD.setInsu_sum(insu_sum);
				insuranceDTDList.add(insuDTD);
				
				}

		return insuranceDTDList;
	}*/
	
}
