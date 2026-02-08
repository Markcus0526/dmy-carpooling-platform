//package util;
//
//import java.io.IOException;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.struts2.ServletActionContext;
//
//public class WebUtil {
//
//    public static Object _session(String id) {
//        Map<String, Object> session = ServletActionContext.getContext().getSession();
//        if (session.containsKey(id)) {
//            return session.get(id);
//        } else {
//            return null;
//        }
//    }
//
//    public static void _session(String id, Object val) {
//        Map<String, Object> session = ServletActionContext.getContext().getSession();
//        session.put(id, val);
//    }
//
//    public static String _remote_ip() {
//        HttpServletRequest request = ServletActionContext.getRequest();
//        return request.getRemoteAddr();
//    }
//
//    public static String _request(String id) {
//        HttpServletRequest request = ServletActionContext.getRequest();
//        return request.getParameter(id);
//    }
//
//    public static String[] _getRequestParameterMap(String key) {
//		HttpServletRequest request = ServletActionContext.getRequest();
//		return request.getParameterMap().get(key);
//    }
//    
//    public static void _setRequestAttribute(String key, Object value) {
//        HttpServletRequest request = ServletActionContext.getRequest();
//        request.setAttribute(key, value);
//    }
//    
//    public static Map<String, String[]> _request_all() {
//        HttpServletRequest request = ServletActionContext.getRequest();
//        return request.getParameterMap();
//    }
//
//    public static void _clear_session() {
//        Map<String, Object> session = ServletActionContext.getContext().getSession();
//        session.clear();
//    }
//
//    public static void _delete_key_session(String key) {
//        Map<String, Object> session = ServletActionContext.getContext().getSession();
//        if (_session(key) != null) {
//            session.remove(key);
//        }
//    }
//    
//    public static void _go(String url) {
//        HttpServletResponse response = ServletActionContext.getResponse();
//        try {
//            response.sendRedirect(url);
//        } catch (IOException e) {
//        	e.printStackTrace();
//        }
//    }
//    
//    public static void _setResponseContentType(String contentType) {
//		HttpServletResponse resp = ServletActionContext.getResponse();
//		resp.setContentType(contentType);
//    }
//}
