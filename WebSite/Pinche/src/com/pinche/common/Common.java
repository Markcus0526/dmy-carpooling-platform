package com.pinche.common;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.*;
import javax.swing.table.TableModel;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.struts2.ServletActionContext;
import org.omg.IOP.Encoding;

public class Common {
	
    private static Random random = new Random();
    private static final String randChars = "0123456789abcdefghigklmnopqrstuvtxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";

    public static void BeginExportCSV(String filename) {
        String C_HTTP_HEADER_CONTENT = "Content-Disposition";
        String C_HTTP_ATTACHMENT = "attachment;filename=";
        String C_HTTP_CONTENT_TYPE_OCTET = "application/text-csv; charset=utf-8";
        String C_HTTP_CACHE_CONTROL = "private";//, must-revalidate";

        HttpServletResponse response = ServletActionContext.getResponse();

        // Add the header that specifies the default filename
        // for the Download/SaveAs dialog
        //response.Charset = "shift_jis";
        response.setCharacterEncoding("utf-8");
    }
	
    public static void EndExportCSV() {
    	HttpServletResponse response = ServletActionContext.getResponse();
        // Stop execution of the current page
    }

    public static void ExportCSV(String filename, TableModel table) {
        BeginExportCSV(filename);
        ExportCSVData(table);
        EndExportCSV();
    }
	
	public static void ExportCSVData(TableModel table) {
        HttpServletResponse response = ServletActionContext.getResponse();

        // content
        String content = "";
        for (int r = 0; r < table.getRowCount(); r++)
        {
            for (int c = 0; c < table.getColumnCount(); c++)
            {
                String item = table.getValueAt(r, c).toString();
                //item = item.Replace("\"", "\"\"");

                //content += "=\"" + item + "\"";
                content += item;

                if (c != table.getColumnCount() - 1)
                    content += ",";
            }
            content += "\r\n";
        }
        response.setCharacterEncoding("utf-8");
        try {
			response.getWriter().println(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public static void ExportCSVData(Object[] data) {
        ExportCSVData(data.length, data);
    }
	
	public static void ExportCSVData(int[] except, Object[] data) {
        ExportCSVData(data.length, except, data);
    }
	
	public static void ExportCSVData(int len, Object[] data) {
        HttpServletResponse response = ServletActionContext.getResponse();

        // content
        String content = "";
        for (int i = 0; i < len; i++)
        {
            String item = data[i].toString();
            //item = item.Replace("\"", "\"\"");

            //content += "=\"" + item + "\"";
            content += item;

            if (i != len - 1)
                content += ",";
        }
        content += "\r\n";
        response.setCharacterEncoding("utf-8");
        try {
			response.getWriter().println(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public static boolean isInArray(int[] dataarray, int search) {
        for (int i = 0; i < dataarray.length; i++)
        {
            if (dataarray[i] == search)
                return true;
        }
        return false;
    }
	
	public static void ExportCSVData(int len, int[] except, Object[] data) {
        if (except == null)
        {
            ExportCSVData(len, data);
            return;
        }

        HttpServletResponse response = ServletActionContext.getResponse();

        // content
        String content = "";
        for (int i = 0; i < len; i++)
        {
            if (isInArray(except, i))
                continue;

            String item = data[i].toString();
            //item = item.Replace("\"", "\"\"");

            //content += "=\"" + item + "\"";
            content += item;

            if (i != len - 1)
                content += ",";
        }

        if (content.endsWith(","))
            content = content.substring(0, content.length() - 1);
        content += "\r\n";
        response.setCharacterEncoding("utf-8");
        try {
			response.getWriter().println(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public static String _filename(String uri) {
        String fname = "";
        int i = uri.lastIndexOf("/");
        if (i == -1) {
            return "";
        }
        fname = uri.substring(i + 1);
        i = fname.lastIndexOf(".");
        if (i == -1) {
            return fname;
        }
        return fname.substring(0, i);
    }
    
	public static Map<String, String[]> _request_all() {
        HttpServletRequest request = ServletActionContext.getRequest();
        return request.getParameterMap();
    }

    public static Timestamp _strToDate(String str) {
        try {
            return Timestamp.valueOf(str);
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }

    public static Timestamp _strToTimestamp(String str) {
        try {
            return Timestamp.valueOf(str + ":00");
        } catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
    
    public static String reduceStr(String str, int len) {
    	if (str.length() > len) {
    		return str.substring(0, len-3) + "...";
    	}
    	
    	if (str.equals(""))
    		return "没有";
    	return str;
    }
    
    public static String getCurrentDateTimeString() {
    	Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ret = sdf.format(date);
		return ret;
    }

	public static String Date2String(java.util.Date date) {
		if (date == null) {
			return "";
		} else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH':'mm':'ss");
			return simpleDateFormat.format(date);
		}
	}

	public static String genCode(int len) {
		return Common.getRandStr(len, true);
	}
	
    public static int rand(int max) {
        return rand(0, max);
    }
    
    public static int rand(int min, int max) {
        if (min < max) {
                return random.nextInt(max - min + 1) + min;
        } else {
                return min;
        }
    }
    
    public static String getRandStr(int length, boolean isOnlyNum) {
        int size = isOnlyNum ? 10 : 62;
        StringBuilder hash = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
                hash.append(randChars.charAt(random.nextInt(size)));
        }
        return hash.toString();
    }
}