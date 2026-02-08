package util;


import java.io.*;

import org.jfree.data.*;
import org.jfree.data.general.*;
import org.jfree.chart.*;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.*;

import java.awt.Font;
import java.awt.geom.Ellipse2D.Float;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.LegendTitle;

/**
 * @author  yeeku.H.lee kongyeeku@163.com
 * @version  1.0
 * <br>Copyright (C), 2005-2008, yeeku.H.Lee
 * <br>This program is protected by copyright laws.
 * <br>Program Name:
 * <br>Date: 
 */

public class PieChartDemo
{
	public static void main(String[] args) throws IOException
	{
		DefaultPieDataset data = getDataSet();
		//JFreeChart chart = ChartFactory.createPieChart(
		//生成3D饼图
		JFreeChart chart = ChartFactory.createPieChart3D(
			"今日市内订单统计",  // 图表标题
			getDataSet(), //数据
			true, // 是否显示图例
			false, //是否显示工具提示
			false //是否生成URL
		);
		//重新设置图标标题，改变字体
		chart.setTitle(new TextTitle("今日市内订单统计", new Font("黑体", Font.ITALIC , 22))); 
		setPiePoltNum(chart);
		//取得统计图标的第一个图例
		LegendTitle legend = chart.getLegend(0);
		//修改图例的字体
		legend.setItemFont(new Font("宋体", Font.BOLD, 14)); 
		//获得饼图的Plot对象
		PiePlot plot = (PiePlot)chart.getPlot();
		//设置饼图各部分的标签字体
		plot.setLabelFont(new Font("隶书", Font.BOLD, 18)); 
		//设定背景透明度（0-1.0之间）
        plot.setBackgroundAlpha(0.9f);
		//设定前景透明度（0-1.0之间）
        plot.setForegroundAlpha(0.50f);

		FileOutputStream fos = new FileOutputStream("D:/book.jpg");
		ChartUtilities.writeChartAsJPEG(
			fos, //输出到哪个输出流
			1, //JPEG图片的质量，0~1之间
			chart, //统计图标对象
			800, //宽
			600,//宽
			null //ChartRenderingInfo 信息
			);
		fos.close();
	}
	
	public static void chansheng(int[] shu,String path) throws IOException{
		DefaultPieDataset data = getDataSet(shu);
		//JFreeChart chart = ChartFactory.createPieChart(
		//生成3D饼图
		JFreeChart chart = ChartFactory.createPieChart3D(
			"今日市内订单统计",  // 图表标题
			getDataSet(shu), //数据
			true, // 是否显示图例
			false, //是否显示工具提示
			false //是否生成URL
		);
		//重新设置图标标题，改变字体
		chart.setTitle(new TextTitle("今日市内订单统计", new Font("黑体", Font.ITALIC , 22))); 
		setPiePoltNum(chart);
		//取得统计图标的第一个图例
		LegendTitle legend = chart.getLegend(0);
		//修改图例的字体
		legend.setItemFont(new Font("宋体", Font.BOLD, 14)); 
		//获得饼图的Plot对象
		PiePlot plot = (PiePlot)chart.getPlot();
		//设置饼图各部分的标签字体
		plot.setLabelFont(new Font("隶书", Font.BOLD, 18)); 
		//设定背景透明度（0-1.0之间）
        plot.setBackgroundAlpha(0.9f);
		//设定前景透明度（0-1.0之间）
        plot.setForegroundAlpha(0.50f);

		FileOutputStream fos = new FileOutputStream(path+"/book.jpg");
		ChartUtilities.writeChartAsJPEG(
			fos, //输出到哪个输出流
			1, //JPEG图片的质量，0~1之间
			chart, //统计图标对象
			800, //宽
			600,//宽
			null //ChartRenderingInfo 信息
			);
		fos.close();
	}

	private static DefaultPieDataset getDataSet(int[] shu)
	{
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("正常完成",shu[0]);
		dataset.setValue("执行中",shu[1]);
		dataset.setValue("乘客销单",shu[2]);
		dataset.setValue("待执行",shu[3]);
		dataset.setValue("未接单",shu[4]);
		return dataset;
	}
	
	private static DefaultPieDataset getDataSet()
	{
		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("正常完成",11);
		dataset.setValue("执行中",22);
		dataset.setValue("乘客销单",33);
		dataset.setValue("待执行",444);
		dataset.setValue("未接单",66);
		return dataset;
	}
	
	public static void changePic() throws IOException{
		DefaultPieDataset data = getDataSet();
		//JFreeChart chart = ChartFactory.createPieChart(
		//生成3D饼图
		JFreeChart chart = ChartFactory.createPieChart3D(
			"今日市内订单统计",  // 图表标题
			getDataSet(), //数据
			true, // 是否显示图例
			false, //是否显示工具提示
			false //是否生成URL
		);
		//重新设置图标标题，改变字体
		chart.setTitle(new TextTitle("今日市内订单统计", new Font("黑体", Font.ITALIC , 22))); 
		//取得统计图标的第一个图例
		LegendTitle legend = chart.getLegend(0);
		//修改图例的字体
		legend.setItemFont(new Font("宋体", Font.BOLD, 14)); 
		//获得饼图的Plot对象
		PiePlot plot = (PiePlot)chart.getPlot();
		//设置饼图各部分的标签字体
		plot.setLabelFont(new Font("隶书", Font.BOLD, 18)); 
		//设定背景透明度（0-1.0之间）
        plot.setBackgroundAlpha(0.9f);
		//设定前景透明度（0-1.0之间）
        plot.setForegroundAlpha(0.50f);

		FileOutputStream fos = new FileOutputStream("D:/book.jpg");
		ChartUtilities.writeChartAsJPEG(
			fos, //输出到哪个输出流
			1, //JPEG图片的质量，0~1之间
			chart, //统计图标对象
			800, //宽
			600,//宽
			null //ChartRenderingInfo 信息
			);
		fos.close();
	}
	
	  public static void setPiePoltNum(JFreeChart chart){
			// 图表（饼图）
			PiePlot piePlot = (PiePlot)chart.getPlot();
			// 设置饼图标签显示
			/**
			 * public StandardPieSectionLabelGenerator(String labelFormat)
			 * 	"{0}"：默认值，图表中显示类别名称
			 * 	"{1}"：表示图表中显示类别的具体数值
			 * 	"{2}"：表示图表中要显示当前类别在总数中的百分比
			 * 	"{3}"：表示图表中所有类别相加的总值
			 */
//			piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0},{1}:{2}:{3}"));
			piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0},{2}"));
		}
}