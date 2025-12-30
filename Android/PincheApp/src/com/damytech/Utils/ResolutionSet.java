// ResolutionSet.java
// Created by
// Maintenance by KHM. 2014.03.13

package com.damytech.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.damytech.Misc.Global;
import com.damytech.PincheApp.R;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshBase;
import com.damytech.Utils.Android_PullToRefresh.PullToRefreshListView;

public class ResolutionSet
{
	private float m_fXpro = 1;
	private float m_fYpro = 1;
	private float m_fPro  = 1;
	private int m_nWidth = 480;
	private int m_nHeight = 800;

	public static ResolutionSet instance = new ResolutionSet();

	public ResolutionSet() {}

	public static int getBaseWidth() { return 480; }
	public static int getBaseHeight() { return 800; }

	public void iterateChild(View view, int nParentWidth, int nParentHeight)
	{
		if (nParentWidth > 0 && nParentHeight > 0)
			setResolution(nParentWidth - 3, nParentHeight - 3);

		if (view instanceof ViewGroup && !(view instanceof PullToRefreshBase))
		{
			ViewGroup container = (ViewGroup)view;
			int nCount = container.getChildCount();
			for (int i = 0; i < nCount; i++)
			{
				iterateChild(container.getChildAt(i), -1, -1);
			}
		}

		UpdateLayout(view);
	}

	public void setResolution(int x, int y)
	{
		m_nWidth = x;
		m_nHeight = y;
		m_fXpro = (float)x / 480;
		m_fYpro = (float)y / 800;
		m_fPro = Math.min(m_fXpro, m_fYpro);
	}

	public float getXPro() { return m_fXpro; }
	public float getYPro() { return m_fYpro; }
	public float getPro() { return m_fPro; }

	private void UpdateLayout(View pView)
	{
		if (pView == null)
			return;

		LayoutParams pLayoutParams = pView.getLayoutParams();
		if (pLayoutParams == null)
			return;

		int nOrgWidth = 0, nOrgHeight = 0;                                                                  // Width, height values
		boolean existOrgWidth = false, existOrgHeight = false;
		int nOrgMinWidth = 0, nOrgMinHeight = 0;                                                                  // Width, height values
		boolean existOrgMinWidth = false, existOrgMinHeight = false;
		int nOrgPaddingLeft = 0, nOrgPaddingRight = 0, nOrgPaddingTop = 0, nOrgPaddingBottom = 0;           // Padding values
		boolean existLPadding = false, existTPadding = false, existRPadding = false, existBPadding = false;
		int nOrgLMargin = 0, nOrgTMargin = 0, nOrgRMargin = 0, nOrgBMargin = 0;                             // Margin values
		boolean existLMargin = false, existTMargin = false, existRMargin = false, existBMargin = false;
		float fOrgFontSize = 0;                                                                             // Font Value

		// Look for presaved data to avoid duplicated zooming
		{
			nOrgWidth = getIntValueFromTag(pView, R.string.TAG_KEY_WIDTH);
			nOrgHeight = getIntValueFromTag(pView, R.string.TAG_KEY_HEIGHT);

			nOrgMinWidth = getIntValueFromTag(pView, R.string.TAG_KEY_MINWIDTH);
			nOrgMinHeight = getIntValueFromTag(pView, R.string.TAG_KEY_MINHEIGHT);

			existOrgWidth = isExistTag(pView, R.string.TAG_KEY_WIDTH);
			existOrgHeight = isExistTag(pView, R.string.TAG_KEY_HEIGHT);

			existOrgMinWidth = isExistTag(pView, R.string.TAG_KEY_MINWIDTH);
			existOrgMinHeight = isExistTag(pView, R.string.TAG_KEY_MINHEIGHT);

			existLPadding = isExistTag(pView, R.string.TAG_KEY_PADDINGLEFT);
			existTPadding = isExistTag(pView, R.string.TAG_KEY_PADDINGTOP);
			existRPadding = isExistTag(pView, R.string.TAG_KEY_PADDINGRIGHT);
			existBPadding = isExistTag(pView, R.string.TAG_KEY_PADDINGBOTTOM);

			existLMargin = isExistTag(pView, R.string.TAG_KEY_MARGINLEFT);
			existTMargin = isExistTag(pView, R.string.TAG_KEY_MARGINTOP);
			existRMargin = isExistTag(pView, R.string.TAG_KEY_MARGINRIGHT);
			existBMargin = isExistTag(pView, R.string.TAG_KEY_MARGINBOTTOM);

			fOrgFontSize = getFloatValueFromTag(pView, R.string.TAG_KEY_FONTSIZE);
		}

		// setting with and height
		{
			if (pLayoutParams.width > 0)
			{
				int nWidth = !existOrgWidth ? pLayoutParams.width : nOrgWidth;
				if (!existOrgWidth)
					pView.setTag(R.string.TAG_KEY_WIDTH, "" + pLayoutParams.width);
				pLayoutParams.width = (int)(nWidth * m_fXpro + 0.50001);
				pView.setLayoutParams(pLayoutParams);
			}

			if (pLayoutParams.height > 0)
			{
				int nHeight = !existOrgHeight ? pLayoutParams.height : nOrgHeight;
				if (!existOrgHeight)
					pView.setTag(R.string.TAG_KEY_HEIGHT, "" + pLayoutParams.height);
				pLayoutParams.height = (int)(nHeight * m_fYpro + 0.50001);
				pView.setLayoutParams(pLayoutParams);
			}

			if (Global.getApiVersion() >= Build.VERSION_CODES.JELLY_BEAN)
			{
				if (pView.getMinimumWidth() > 0)
				{
					int nMinWidth = !existOrgMinWidth ? pView.getMinimumWidth() : nOrgMinWidth;
					if (!existOrgMinWidth)
						pView.setTag(R.string.TAG_KEY_MINWIDTH, "" + pView.getMinimumWidth());
					pView.setMinimumWidth((int)(nMinWidth * m_fXpro + 0.50001));
				}

				if (pView.getMinimumHeight() > 0)
				{
					int nMinHeight = !existOrgMinHeight ? pView.getMinimumHeight() : nOrgMinHeight;
					if (!existOrgMinHeight)
						pView.setTag(R.string.TAG_KEY_MINHEIGHT, "" + pView.getMinimumHeight());
					pView.setMinimumHeight((int)(nMinHeight * m_fYpro + 0.50001));
				}
			}
		}

		// Setting padding values
		{
			if (!existLPadding)
				pView.setTag(R.string.TAG_KEY_PADDINGLEFT, "" + pView.getPaddingLeft());
			if (!existTPadding)
				pView.setTag(R.string.TAG_KEY_PADDINGTOP, "" + pView.getPaddingTop());
			if (!existRPadding)
				pView.setTag(R.string.TAG_KEY_PADDINGRIGHT, "" + pView.getPaddingRight());
			if (!existBPadding)
				pView.setTag(R.string.TAG_KEY_PADDINGBOTTOM, "" + pView.getPaddingBottom());

			int nLeft = getIntValueFromTag(pView, R.string.TAG_KEY_PADDINGLEFT);
			int nTop = getIntValueFromTag(pView, R.string.TAG_KEY_PADDINGTOP);
			int nRight = getIntValueFromTag(pView, R.string.TAG_KEY_PADDINGRIGHT);
			int nBottom = getIntValueFromTag(pView, R.string.TAG_KEY_PADDINGBOTTOM);

			nLeft = (int)(nLeft * m_fXpro);
			nRight = (int)(nRight * m_fXpro);
			nTop = (int)(nTop * m_fYpro);
			nBottom = (int)(nBottom * m_fYpro);

			pView.setPadding(nLeft, nTop, nRight, nBottom);
		}

		if (pLayoutParams instanceof ViewGroup.MarginLayoutParams)
		{
			int nLeft, nRight, nTop, nBottom;
			ViewGroup.MarginLayoutParams pMarginLP = (ViewGroup.MarginLayoutParams)pLayoutParams;

			if (!existLMargin)
				pView.setTag(R.string.TAG_KEY_MARGINLEFT, "" + pMarginLP.leftMargin);
			if (!existTMargin)
				pView.setTag(R.string.TAG_KEY_MARGINTOP, "" + pMarginLP.topMargin);
			if (!existRMargin)
				pView.setTag(R.string.TAG_KEY_MARGINRIGHT, "" + pMarginLP.rightMargin);
			if (!existBMargin)
				pView.setTag(R.string.TAG_KEY_MARGINBOTTOM, "" + pMarginLP.bottomMargin);

			nLeft = getIntValueFromTag(pView, R.string.TAG_KEY_MARGINLEFT);
			nTop = getIntValueFromTag(pView, R.string.TAG_KEY_MARGINTOP);
			nRight = getIntValueFromTag(pView, R.string.TAG_KEY_MARGINRIGHT);
			nBottom = getIntValueFromTag(pView, R.string.TAG_KEY_MARGINBOTTOM);

			//if(pMarginLP.leftMargin > 0)
				pMarginLP.leftMargin = (int)(nLeft * m_fXpro + 0.50001);
			//if(pMarginLP.rightMargin > 0)
				pMarginLP.rightMargin = (int)(nRight * m_fXpro + 0.50001);
			//if(pMarginLP.topMargin > 0)
				pMarginLP.topMargin = (int)(nTop * m_fYpro + 0.50001);
			//if(pMarginLP.bottomMargin > 0)
				pMarginLP.bottomMargin = (int)(nBottom * m_fYpro + 0.50001);
			pView.setLayoutParams(pMarginLP);
		}

		if (pView instanceof TextView)
		{
			TextView pLabel = (TextView)pView;
			float fTxtSize = 0;

			if (fOrgFontSize < 0)
			{
				fTxtSize = pLabel.getTextSize();
				pView.setTag(R.string.TAG_KEY_FONTSIZE, "" + fTxtSize);
			}
			else
				fTxtSize = fOrgFontSize;

			fTxtSize *= m_fPro;

			pLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, fTxtSize);
		}
	}

	public static boolean isExistTag(View pView, int nTagKey)
	{
		return pView.getTag(nTagKey) != null;
	}

	public static int getIntValueFromTag(View pView, int nTagKey)
	{
		int nRetVal = 0;
		Object objTemp = pView.getTag(nTagKey);
		String szTemp = "";

		if (objTemp == null)
		{
			nRetVal = -1;
		}
		else
		{
			szTemp = (String)objTemp;

			try {
				nRetVal = (int)Double.parseDouble(szTemp);
			} catch (Exception ex) {
				ex.printStackTrace();
				nRetVal = -1;
			}
		}

		return nRetVal;
	}

	public static float getFloatValueFromTag(View pView, int nTagKey)
	{
		float fRetVal = 0;
		Object objTemp = pView.getTag(nTagKey);
		String szTemp = "";

		if (objTemp == null)
		{
			fRetVal = -1;
		}
		else
		{
			szTemp = (String)objTemp;

			try {
				fRetVal = (float)Double.parseDouble(szTemp);
			} catch (Exception ex) {
				ex.printStackTrace();
				fRetVal = -1;
			}
		}

		return fRetVal;
	}
}
