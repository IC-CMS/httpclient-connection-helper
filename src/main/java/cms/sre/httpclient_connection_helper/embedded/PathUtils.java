package cms.sre.httpclient_connection_helper.embedded;

import org.apache.commons.lang3.SystemUtils;

public class PathUtils {
    public static String getAbsolutePathForClasspathResource(String resource){
        String keyStoreLocation = ClassLoader.getSystemClassLoader()
                .getResource(resource)
                .getPath()
                .replace('/', '\\');

        if(SystemUtils.IS_OS_WINDOWS){
            keyStoreLocation = keyStoreLocation.substring(1);
        }

        return keyStoreLocation;
    }
}
