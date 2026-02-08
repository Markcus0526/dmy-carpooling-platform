package util.excel.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import util.excel.pojo.ExcelUser;
import util.excel.service.ExcelUserService;

import com.nmdy.financial.manage.dao.UsersEntity;

public class ExcelUserServiceImpl implements ExcelUserService{
	public InputStream exportPointDetails(List list) {
		// 创建一个HSSFWorkbook
		HSSFWorkbook wb = new HSSFWorkbook();
		// 由HSSFWorkbook创建一个HSSFSheet
		HSSFSheet sheet = wb.createSheet();
		// 由HSSFSheet创建HSSFRow
		HSSFRow row = sheet.createRow((short) 0);
		HSSFCell cell = row.createCell((short) 0);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("用户ID");
		cell = row.createCell((short) 1);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("用户名");
		cell = row.createCell((short) 2);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("姓名");
		cell = row.createCell((short) 3);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("所属集团");
		cell = row.createCell((short) 4);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("手机号码");
		cell = row.createCell((short) 5);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("个人信息认证状态");
		cell = row.createCell((short) 6);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellValue("个人信息认证状态");
		cell = row.createCell((short) 7);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		// /下面的是根据list 进行遍历循环 想下面的单元格 塞值（这篇笔记之前发表不了，后来发现是我的表头汉字 有敏感字，故改成标题）
		for (int i = 1; i < list.size() + 1; i++) {
			ExcelUser dto = (ExcelUser) list.get(i - 1);
			row = sheet.getRow(i);
			if (row == null)
				row = sheet.createRow((short) i);
			cell = row.getCell((short) 0);
			if (cell == null)
				cell = row.createCell((short) 0);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(dto.getId() == null ? "" : dto.getId().toString());
			cell = row.getCell((short) 1);
			if (cell == null) cell = row.createCell((short) 1);
			
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(dto.getUsercode() == null ? "" : dto.getUsercode().toString());
			cell = row.getCell((short) 2);
			if (cell == null) cell = row.createCell((short) 2);
			
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(dto.getId() == null ? "" : dto.getUsername().toString());
			cell = row.getCell((short) 3);
			if (cell == null) cell = row.createCell((short) 3);
			
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(dto.getGroup_name() == null ? "" : dto.getGroup_name().toString());
			cell = row.getCell((short) 4);
			if (cell == null) cell = row.createCell((short) 4);
			
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(dto.getPhone().toString() == null ? "" : dto.getPhone().toString());
			cell = row.getCell((short) 5);
			if (cell == null) cell = row.createCell((short) 5);
			
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(dto.getInvitecode_self() == null ? "" : dto.getInvitecode_self().toString());
			cell = row.getCell((short) 6);
			if (cell == null) cell = row.createCell((short) 6);
			
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(dto.getInvitecode_regist() == null ? "" : dto.getInvitecode_regist().toString());
			cell = row.getCell((short) 7);
			if (cell == null) cell = row.createCell((short) 7);
		}
		// 使用apache的commons-lang.jar产生随机的字符串作为文件名
		String fileName = "个人客户管理";
		// 生成xls文件名必须要是随机的，确保每个线程访问都产生不同的文件
		StringBuffer sb = new StringBuffer(fileName);
		final File file = new File(sb.append(".xls").toString());
		try {
			OutputStream os = new FileOutputStream(file);
			try {
				wb.write(os);
				os.close();
			} catch (IOException e) {
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return is;// 返回的是一个输入流

	}
	
	/**
	 * 用户管理
	 */
	public InputStream exportPointUserDetails(List list) {
		// 创建一个HSSFWorkbook
				HSSFWorkbook wb = new HSSFWorkbook();
				// 由HSSFWorkbook创建一个HSSFSheet
				HSSFSheet sheet = wb.createSheet();
				// 由HSSFSheet创建HSSFRow
				HSSFRow row = sheet.createRow((short) 0);
				HSSFCell cell = row.createCell((short) 0);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("用户ID");
				cell = row.createCell((short) 1);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("用户名");
				cell = row.createCell((short) 2);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("姓名");
				cell = row.createCell((short) 3);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("所属集团");
				cell = row.createCell((short) 4);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("手机号码");
				cell = row.createCell((short) 5);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				// /下面的是根据list 进行遍历循环 想下面的单元格 塞值（这篇笔记之前发表不了，后来发现是我的表头汉字 有敏感字，故改成标题）
				for (int i = 1; i < list.size() + 1; i++) {
					ExcelUser dto = (ExcelUser) list.get(i - 1);
					row = sheet.getRow(i);
					if (row == null)
						row = sheet.createRow((short) i);
					cell = row.getCell((short) 0);
					if (cell == null)
						cell = row.createCell((short) 0);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getId() == null ? "" : dto.getId().toString());
					cell = row.getCell((short) 1);
					if (cell == null) cell = row.createCell((short) 1);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getUsercode() == null ? "" : dto.getUsercode().toString());
					cell = row.getCell((short) 2);
					if (cell == null) cell = row.createCell((short) 2);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getId() == null ? "" : dto.getUsername().toString());
					cell = row.getCell((short) 3);
					if (cell == null) cell = row.createCell((short) 3);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getGroup_name() == null ? "" : dto.getGroup_name().toString());
					cell = row.getCell((short) 4);
					if (cell == null) cell = row.createCell((short) 4);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getPhone().toString() == null ? "" : dto.getPhone().toString());
					cell = row.getCell((short) 5);
					if (cell == null) cell = row.createCell((short) 5);
					
				}
				// 使用apache的commons-lang.jar产生随机的字符串作为文件名
				String fileName = "用户管理";
				// 生成xls文件名必须要是随机的，确保每个线程访问都产生不同的文件
				StringBuffer sb = new StringBuffer(fileName);
				final File file = new File(sb.append(".xls").toString());
				try {
					OutputStream os = new FileOutputStream(file);
					try {
						wb.write(os);
						os.close();
					} catch (IOException e) {
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				InputStream is = null;
				try {
					is = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return is;// 返回的是一个输入流
	}
	
	/**
	 *我的表单
	 */
	public InputStream exportPointMyFormDetails(List list) {
		// 创建一个HSSFWorkbook
				HSSFWorkbook wb = new HSSFWorkbook();
				// 由HSSFWorkbook创建一个HSSFSheet
				HSSFSheet sheet = wb.createSheet();
				// 由HSSFSheet创建HSSFRow
				HSSFRow row = sheet.createRow((short) 0);
				HSSFCell cell = row.createCell((short) 0);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("表单号");
				cell = row.createCell((short) 1);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("用户ID");
				cell = row.createCell((short) 2);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("客户类别");
				cell = row.createCell((short) 3);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("客户名称");
				cell = row.createCell((short) 4);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("联系电话");
				cell = row.createCell((short) 5);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				
				cell.setCellValue("余额");
				cell = row.createCell((short) 6);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				
				cell.setCellValue("审核金额");
				cell = row.createCell((short) 7);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				
				cell.setCellValue("操作原因");
				cell = row.createCell((short) 8);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				
				cell.setCellValue("相关订单");
				cell = row.createCell((short) 9);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				
				cell.setCellValue("详细说明");
				cell = row.createCell((short) 10);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				
				cell.setCellValue("驳回理由");
				cell = row.createCell((short) 11);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				
				// /下面的是根据list 进行遍历循环 想下面的单元格 塞值（这篇笔记之前发表不了，后来发现是我的表头汉字 有敏感字，故改成标题）
				for (int i = 1; i < list.size() + 1; i++) {
					UsersEntity dto = (UsersEntity) list.get(i - 1);
					row = sheet.getRow(i);
					if (row == null)
						row = sheet.createRow((short) i);
					cell = row.getCell((short) 0);
					if (cell == null)
						cell = row.createCell((short) 0);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue( dto.getFormid());
					cell = row.getCell((short) 1);
					if (cell == null) cell = row.createCell((short) 1);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getUserid());
					cell = row.getCell((short) 2);
					if (cell == null) cell = row.createCell((short) 2);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getAccount_type());
					cell = row.getCell((short) 3);
					if (cell == null) cell = row.createCell((short) 3);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getUsername());
					cell = row.getCell((short) 4);
					if (cell == null) cell = row.createCell((short) 4);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getPhone().toString() == null ? "" : dto.getPhone().toString());
					cell = row.getCell((short) 5);
					if (cell == null) cell = row.createCell((short) 5);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue("null".equals(String.valueOf(dto.getBalance()))?0:dto.getBalance());
					cell = row.getCell((short) 6);
					if (cell == null) cell = row.createCell((short) 6);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getSum().toString() == null ? "" : dto.getSum().toString());
					cell = row.getCell((short) 7);
					if (cell == null) cell = row.createCell((short) 7);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getReq_cause().toString() == null ? "" : dto.getReq_cause().toString());
					cell = row.getCell((short) 8);
					if (cell == null) cell = row.createCell((short) 8);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getOrder_cs_id());
					cell = row.getCell((short) 9);
					if (cell == null) cell = row.createCell((short) 9);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getRemark().toString() == null ? "" : dto.getRemark().toString());
					cell = row.getCell((short) 10);
					if (cell == null) cell = row.createCell((short) 10);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getStatus().toString() == null ? "" : dto.getStatus().toString());
					cell = row.getCell((short) 11);
					if (cell == null) cell = row.createCell((short) 11);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.getReject_cause().toString() == null ? "" : dto.getReject_cause().toString());
					cell = row.getCell((short) 12);
					if (cell == null) cell = row.createCell((short) 12);
					
				}
				// 使用apache的commons-lang.jar产生随机的字符串作为文件名
				String fileName = "我的表单";
				// 生成xls文件名必须要是随机的，确保每个线程访问都产生不同的文件
				StringBuffer sb = new StringBuffer(fileName);
				final File file = new File(sb.append(".xls").toString());
				try {
					OutputStream os = new FileOutputStream(file);
					try {
						wb.write(os);
						os.close();
					} catch (IOException e) {
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				InputStream is = null;
				try {
					is = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return is;// 返回的是一个输入流
	}
	
	
	/**
	 *集团客户管理
	 */
	public InputStream exportPointGroupDetails(List list) {
		// 创建一个HSSFWorkbook
				HSSFWorkbook wb = new HSSFWorkbook();
				// 由HSSFWorkbook创建一个HSSFSheet
				HSSFSheet sheet = wb.createSheet();
				// 由HSSFSheet创建HSSFRow
				HSSFRow row = sheet.createRow((short) 0);
				HSSFCell cell = row.createCell((short) 0);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("id");
				cell = row.createCell((short) 1);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("用户名");
				cell = row.createCell((short) 2);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("姓名");
				cell = row.createCell((short) 3);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("手机号码");
				cell = row.createCell((short) 4);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("分成比例");
				cell = row.createCell((short) 5);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				
				// /下面的是根据list 进行遍历循环 想下面的单元格 塞值（这篇笔记之前发表不了，后来发现是我的表头汉字 有敏感字，故改成标题）
				for (int i = 1; i < list.size() + 1; i++) {
					Map<String,Object> dto = (Map<String,Object>) list.get(i - 1);
					row = sheet.getRow(i);
					if (row == null)
						row = sheet.createRow((short) i);
					cell = row.getCell((short) 0);
					if (cell == null)
						cell = row.createCell((short) 0);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.get("id").toString());
					cell = row.getCell((short) 1);
					if (cell == null) cell = row.createCell((short) 1);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(String.valueOf(dto.get("usercode")));
					cell = row.getCell((short) 2);
					if (cell == null) cell = row.createCell((short) 2);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(String.valueOf(dto.get("username")));
					cell = row.getCell((short) 3);
					if (cell == null) cell = row.createCell((short) 3);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(String.valueOf(dto.get("phone")));
					cell = row.getCell((short) 4);
					if (cell == null) cell = row.createCell((short) 4);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(String.valueOf(dto.get("reallocateProfit")));
					cell = row.getCell((short) 5);
					if (cell == null) cell = row.createCell((short) 5);
				}
				// 使用apache的commons-lang.jar产生随机的字符串作为文件名
				String fileName = "我的表单";
				// 生成xls文件名必须要是随机的，确保每个线程访问都产生不同的文件
				StringBuffer sb = new StringBuffer(fileName);
				final File file = new File(sb.append(".xls").toString());
				try {
					OutputStream os = new FileOutputStream(file);
					try {
						wb.write(os);
						os.close();
					} catch (IOException e) {
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				InputStream is = null;
				try {
					is = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return is;// 返回的是一个输入流
	}
	
	/**
	 *集团客户管理返利信息
	 */
	public InputStream exportPointGroupGetMoneyDetails(List list) {
		// 创建一个HSSFWorkbook
				HSSFWorkbook wb = new HSSFWorkbook();
				// 由HSSFWorkbook创建一个HSSFSheet
				HSSFSheet sheet = wb.createSheet();
				// 由HSSFSheet创建HSSFRow
				HSSFRow row = sheet.createRow((short) 0);
				HSSFCell cell = row.createCell((short) 0);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("被邀请客户ID");
				cell = row.createCell((short) 1);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("被邀请客户姓名");
				cell = row.createCell((short) 2);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("返利次数");
				cell = row.createCell((short) 3);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("累计返利");
				cell = row.createCell((short) 4);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("邀请时间");
				cell = row.createCell((short) 5);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				
				// /下面的是根据list 进行遍历循环 想下面的单元格 塞值（这篇笔记之前发表不了，后来发现是我的表头汉字 有敏感字，故改成标题）
				for (int i = 1; i < list.size() + 1; i++) {
					Map<String,Object> dto = (Map<String,Object>) list.get(i - 1);
					row = sheet.getRow(i);
					if (row == null)
						row = sheet.createRow((short) i);
					cell = row.getCell((short) 0);
					if (cell == null)
						cell = row.createCell((short) 0);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.get("id").toString());
					cell = row.getCell((short) 1);
					if (cell == null) cell = row.createCell((short) 1);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(String.valueOf(dto.get("username")));
					cell = row.getCell((short) 2);
					if (cell == null) cell = row.createCell((short) 2);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(String.valueOf(dto.get("rpcount")));
					cell = row.getCell((short) 3);
					if (cell == null) cell = row.createCell((short) 3);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(String.valueOf(dto.get("rpsum")));
					cell = row.getCell((short) 4);
					if (cell == null) cell = row.createCell((short) 4);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(String.valueOf(dto.get("reg_date")));
					cell = row.getCell((short) 5);
					if (cell == null) cell = row.createCell((short) 5);
				}
				// 使用apache的commons-lang.jar产生随机的字符串作为文件名
				String fileName = "我的表单";
				// 生成xls文件名必须要是随机的，确保每个线程访问都产生不同的文件
				StringBuffer sb = new StringBuffer(fileName);
				final File file = new File(sb.append(".xls").toString());
				try {
					OutputStream os = new FileOutputStream(file);
					try {
						wb.write(os);
						os.close();
					} catch (IOException e) {
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				InputStream is = null;
				try {
					is = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return is;// 返回的是一个输入流
	}
	
	/**
	 *集团客户管理查看返利详情
	 */
	public InputStream exportPointGroupShowMoneyDetails(List list) {
		// 创建一个HSSFWorkbook
				HSSFWorkbook wb = new HSSFWorkbook();
				// 由HSSFWorkbook创建一个HSSFSheet
				HSSFSheet sheet = wb.createSheet();
				// 由HSSFSheet创建HSSFRow
				HSSFRow row = sheet.createRow((short) 0);
				HSSFCell cell = row.createCell((short) 0);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("被邀请客户姓名 ");
				cell = row.createCell((short) 1);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("订单编号");
				cell = row.createCell((short) 2);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("返利时间");
				cell = row.createCell((short) 3);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("身份");
				cell = row.createCell((short) 4);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue("返利金额 ");
				cell = row.createCell((short) 5);
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				
				// /下面的是根据list 进行遍历循环 想下面的单元格 塞值（这篇笔记之前发表不了，后来发现是我的表头汉字 有敏感字，故改成标题）
				for (int i = 1; i < list.size() + 1; i++) {
					Map<String,Object> dto = (Map<String,Object>) list.get(i - 1);
					row = sheet.getRow(i);
					if (row == null)
						row = sheet.createRow((short) i);
					cell = row.getCell((short) 0);
					if (cell == null)
						cell = row.createCell((short) 0);
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dto.get("username").toString());
					cell = row.getCell((short) 1);
					if (cell == null) cell = row.createCell((short) 1);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(String.valueOf(dto.get("order_cs_id")));
					cell = row.getCell((short) 2);
					if (cell == null) cell = row.createCell((short) 2);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(String.valueOf(dto.get("ts_date")));
					cell = row.getCell((short) 3);
					if (cell == null) cell = row.createCell((short) 3);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(String.valueOf(dto.get("identityStatus")));
					cell = row.getCell((short) 4);
					if (cell == null) cell = row.createCell((short) 4);
					
					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(String.valueOf(dto.get("sum")));
					cell = row.getCell((short) 5);
					if (cell == null) cell = row.createCell((short) 5);
				}
				// 使用apache的commons-lang.jar产生随机的字符串作为文件名
				String fileName = "我的表单";
				// 生成xls文件名必须要是随机的，确保每个线程访问都产生不同的文件
				StringBuffer sb = new StringBuffer(fileName);
				final File file = new File(sb.append(".xls").toString());
				try {
					OutputStream os = new FileOutputStream(file);
					try {
						wb.write(os);
						os.close();
					} catch (IOException e) {
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				InputStream is = null;
				try {
					is = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return is;// 返回的是一个输入流
	}
}
