package com.github.nhttp.core;

import com.github.nhttp.router.RouterPathException;

public class UriUtil {

    /**
     * 将路由路径处理为标准路径
     *
     * @param path 路由路径
     * @return 处理后的路由路径
     */
    public static String handlerPath(String path) {
        if (path == null || path.trim().length() == 0) {
            return "";
        }
        if ("/".equals(path)) {
            return path;
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }

    public static String mergePath(String... path) {
        StringBuilder sb = new StringBuilder();
        for (String p : path) {
            sb.append(handlerPath(p));
        }

        return sb.toString();
    }


}
