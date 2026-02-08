package util;

import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

import com.opensymphony.xwork2.ActionSupport;
/**
 * @author  yeeku.H.lee kongyeeku@163.com
 * @version  1.0
 * <br>Copyright (C), 2005-2008, yeeku.H.Lee
 * <br>This program is protected by copyright laws.
 * <br>Program Name:
 * <br>Date: 
 */
public class ChartAction extends ActionSupport {

	private JFreeChart chart;

	public JFreeChart getChart(){
		chart = ChartFactory.createPieChart3D(
			"图书销量统计图",  // 图表标题
			getDataSet(), //数据
			true, // 是否显示图例
			false, //是否显示工具提示
			false //是否生成URL
		);
		//重新设置图标标题，改变字体
		chart.setTitle(new TextTitle("图书销量统计图", new Font("黑体", Font.ITALIC , 22))); 
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
		return chart;
	}

	private DefaultPieDataset getDataSet()
	{

		DefaultPieDataset dataset = new DefaultPieDataset();
		dataset.setValue("Spring2.0宝典",47000);
		dataset.setValue("轻量级J2EE企业实战",38000);
		dataset.setValue("基于J2EE的Ajax宝典",31000);
		dataset.setValue("JavaScript权威指南",29000);
		dataset.setValue("Ajax In Action",25000);
		return dataset;
	}
}

