package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class ResourceUtil {
	private static Properties props;
	public static Properties  getProperties(String propFileName) {
        if(props == null) {
            props = new Properties();
            try {
//                String path =ResourceUtil.class.getResource("/").getPath();    
//                String websiteUrl = path.replace("/WEB-INF/classes","");
//                String filePath = websiteUrl + "WEB-INF/config/" + propFileName;
                
                String filePath =ResourceUtil.class.getResource("/").getPath();   
                filePath = filePath.replace("bin", "src")+"/"+propFileName;
                System.out.println(filePath);
                props.load(new FileInputStream(filePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }           
        }
        return props;
    }
	
	static{
		ResourceUtil.getProperties("createcode.properties");
	}

	public static String getValue(String key){
		return props.getProperty(key);
	}
}
