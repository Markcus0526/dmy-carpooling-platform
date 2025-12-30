package com.damytech.Utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KimHM on 2014/11/7.
 */
public class CouponCodeFilter implements InputFilter {
	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
	{
		for (int i = start; i < end; i++)
		{
			if (!Character.isLetterOrDigit(source.charAt(i)) && source.charAt(i) != '-')
			{
				return "";
			}
		}

		return null;
	}

}
