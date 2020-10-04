package com.Abe.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 敏感词过滤器
 */
@WebFilter("/*")
public class SensitiveWordsFilter implements Filter {

    private List<String> sensitiveWordsList = new ArrayList<>();    // 敏感词List

    public void init(FilterConfig config) throws ServletException {
        /*加载敏感词文件*/
        try {
            // 1.获取文件真实路径
            ServletContext servletContext = config.getServletContext();
            String realPath = servletContext.getRealPath("/WEB-INF/classes/敏感词汇.txt");
            // 2.读取文件
            BufferedReader br = new BufferedReader(new FileReader(realPath));
            // 3.将文件的每一行数据添加到List集合中
            String line;
            while ((line = br.readLine()) != null) {
                sensitiveWordsList.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        // 1.创建代理对象，增强getParameter方法
        ServletRequest proxy_req = (ServletRequest) Proxy.newProxyInstance(
                req.getClass().getClassLoader(),
                req.getClass().getInterfaces(),
                (proxy, method, args) -> {      // lambda表达式

                    // 增强getParameter方法
                    if (method.getName().equals("getParameter")) {
                        // 获取返回值
                        String value = (String) method.invoke(req, args);
                        // 增强返回值
                        if (value != null) {
                            // 敏感词过滤
                            for (String word: sensitiveWordsList) {
                                if (value.contains(word)) {
                                    value = value.replaceAll(word, "***");
                                }
                            }
                        }
                        return value;
                    }

                    // 增强getParameterValues方法
                    if (method.getName().equals("getParameterValues")) {
                        // 获取返回值
                        String[] values = (String[]) method.invoke(req, args);
                        if (values != null && values.length > 0) {
                            return valuesFilter(values);
                        }
                        return values;
                    }

                    // 增强getParameterMap方法
                    if (method.getName().equals("getParameterMap")) {
                        // 获取返回值
                        Map<String, String[]> parameterMap = (Map<String, String[]>) method.invoke(req, args);

                        if (parameterMap != null && parameterMap.size() > 0) {
                            Map<String, String[]> hashMap = new HashMap<>();    // 用于承接过滤集合
                            for (String key : parameterMap.keySet()) {
                                hashMap.put(key, parameterMap.get(key));
                            }
                            for (String key : hashMap.keySet()) {
                                String[] values = hashMap.get(key);
                                if (values != null && values.length > 0) {
                                    String[] filterValues = valuesFilter(values);
                                    hashMap.put(key, filterValues);
                                }
                            }
                            return hashMap;
                        }
                        return parameterMap;
                    }

                    return method.invoke(req, args);
                }
        );
        // 2.放行
        chain.doFilter(proxy_req, resp);
    }

    public void destroy() {
    }

    private String[] valuesFilter(String[] values) {
        for (int i = 0; i < values.length; i++) {
            if(values[i] != null) {
                for (String word: sensitiveWordsList) {
                    if (values[i].contains(word)) {
                        values[i] = values[i].replaceAll(word, "***");
                    }
                }
            }
        }
        return values;
    }

}
