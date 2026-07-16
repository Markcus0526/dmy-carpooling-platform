package util.excel.service;

import java.io.InputStream;
import java.util.List;

public interface ExcelUserService {
	public InputStream exportPointDetails(List list);
	public InputStream exportPointUserDetails(List list);
	public InputStream exportPointMyFormDetails(List list);
	public InputStream exportPointGroupDetails(List list);
	public InputStream exportPointGroupGetMoneyDetails(List list);
	public InputStream exportPointGroupShowMoneyDetails(List list);
}
