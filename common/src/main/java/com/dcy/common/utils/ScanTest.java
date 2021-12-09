package com.dcy.common.utils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author：dcy
 * @Description: 根据类名扫描子包下所有RestController 的接口
 * @Date: 2021/5/25 14:41
 */
@Slf4j
public class ScanTest {

    public void getAnnotationInfo(List<Class<?>> clsList) {
        if (clsList != null && clsList.size() > 0) {
            for (Class<?> cls : clsList) {
                boolean exits = cls.isAnnotationPresent(RestController.class);
                if (!exits) {
                    continue;
                }
                Api api = cls.getAnnotation(Api.class);
                String apiName = "";
                if (api != null) {
                    apiName = api.tags()[0];
                }
                RequestMapping requestMapping1 = cls.getAnnotation(RequestMapping.class);
                String prefix = "";
                if (api != null && requestMapping1 != null) {
                    prefix = requestMapping1.value()[0];
                }
                log.info("java文件名称 {} 接口文档名称 {}================================", cls, apiName);
                //获取类中的所有的方法
                Method[] methods = cls.getDeclaredMethods();
                if (methods.length > 0) {
                    for (Method method : methods) {
                        boolean mExits = method.isAnnotationPresent(GetMapping.class);
                        boolean mExits2 = method.isAnnotationPresent(PostMapping.class);
                        if (!mExits && !mExits2) {
                            continue;
                        }
                        GetMapping getMapping = method.getAnnotation(GetMapping.class);
                        PostMapping postMapping = method.getAnnotation(PostMapping.class);
                        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);

                        String httpMethod = getMapping != null ? "GET" : "POST";
                        String resPath = prefix + getResPath(getMapping, postMapping);
                        if (apiOperation != null) {
                            log.info("ResName:{} ResPath:{} ResCode:{} HttpMethod:{}",
                                    apiOperation.notes(),
                                    resPath,
                                    resPath.replace("/", ":"),
                                    httpMethod);
                        } else {
                            log.info("待添加 ResPath:{}", prefix + resPath);
                        }
                    }
                }
            }
        }
    }

    private String getResPath(GetMapping getMapping, PostMapping postMapping) {
        String path = "";
        if (getMapping != null) {
            path = getMapping.value()[0];
        }
        if (postMapping != null) {
            path = postMapping.value()[0];
        }
        return path;
    }

    public static List<Class<?>> getAllClassByPackageName(Package pkg) {
        String packageName = pkg.getName();
        // 获取当前包下以及子包下所以的类
        List<Class<?>> returnClassList = getClasses(packageName);
        return returnClassList;
    }

    /**
     * 从包package中获取所有的Class
     *
     * @param packageName
     * @return
     */
    private static List<Class<?>> getClasses(String packageName) {
        // 第一个class类的集合
        List<Class<?>> classes = new ArrayList<>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                System.out.println(url.getFile());
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                // 如果是一个.class文件 而且不是目录
                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    // 去掉后面的".class" 获取真正的类名
                                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                                    try {
                                        // 添加到classes
                                        classes.add(Class.forName(packageName + '.' + className));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
        File[] dirFiles = dir.listFiles(file -> (recursive && file.isDirectory()) || (file.getName().endsWith(".class")));

        assert dirFiles != null;
        // 循环所有文件
        for (File file : dirFiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    classes.add(Class.forName(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /*public static void main(String[] args) {
        ScanTest scanTest = new ScanTest();
        List<Class<?>> classList = scanTest.getAllClassByPackageName(WebApplication.class.getPackage());
        scanTest.getAnnotationInfo(classList);
    }*/
}
