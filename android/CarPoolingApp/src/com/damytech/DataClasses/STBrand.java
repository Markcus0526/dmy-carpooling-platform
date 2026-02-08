package com.damytech.DataClasses;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: KHM
 * Date: 14-8-6
 * Time: 上午9:37
 * To change this template use File | Settings | File Templates.
 */
public class STBrand
{
	public static class STStyle
	{
		public long uid = 0;
		public String name = "";
		public int style = 0;
	}

	public long uid = 0;
	public String name = "";
	public ArrayList<STStyle> arrTypes = new ArrayList<STStyle>();
}

