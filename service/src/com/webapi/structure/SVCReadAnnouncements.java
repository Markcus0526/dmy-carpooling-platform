package com.webapi.structure;

import java.sql.ResultSet;
import java.util.Date;

import com.webapi.common.ApiGlobal;

public class SVCReadAnnouncements {
	public long uid = 0;
	public long userid = 0;
	public long announceid = 0;
	public Date readtime = null;
	public int deleted = 0;


	public static SVCReadAnnouncements decodeFromResultSet(ResultSet resultSet)
	{
		SVCReadAnnouncements read_ann = new SVCReadAnnouncements();

		try { read_ann.uid = resultSet.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { read_ann.userid = resultSet.getLong("userid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { read_ann.announceid = resultSet.getLong("announceid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { read_ann.uid = resultSet.getLong("uid"); } catch (Exception ex) { ex.printStackTrace(); }
		try { read_ann.readtime = ApiGlobal.String2Date(resultSet.getString("readtime")); } catch (Exception ex) { ex.printStackTrace(); }
		try { read_ann.deleted = resultSet.getInt("deleted"); } catch (Exception ex) { ex.printStackTrace(); }

		return read_ann;
	}

}
