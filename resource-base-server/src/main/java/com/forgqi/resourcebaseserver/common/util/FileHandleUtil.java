package com.forgqi.resourcebaseserver.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHandleUtil {
    private static final Logger log = LoggerFactory.getLogger(FileHandleUtil.class);

    /**
     * 绝对路径
     **/
    public static String absolutePath = "";

    /**
     * 静态目录
     **/
    public static String staticDir = "static";

    /**
     * 文件存放的目录
     **/
    private static final String fileDir = "/upload/";

    static {
        try {
            createDirIfNotExists();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("获取根目录失败，无法创建上传目录！");
        }
    }

    /**
     * 上传单个文件
     * 最后文件存放路径为：static/upload/image/test.jpg
     * 文件访问路径为：http://127.0.0.1:8080/upload/image/test.jpg
     * 该方法返回值为：/upload/image/test.jpg
     *
     * @param file     文件
     * @param path     文件路径，如：image/
     * @param filename 文件名，如：test.jpg
     * @return 成功：上传后的文件访问路径，失败返回：null
     */
    public static String upload(MultipartFile file, String path, String filename) throws IOException {
        //第一次会创建文件夹
//        createDirIfNotExists();

        String resultPath = fileDir + path + filename;

        //存文件
        File uploadFile = new File(absolutePath, staticDir + resultPath);
        file.transferTo(uploadFile);
//        FileUtils.copyInputStreamToFile(inputStream, uploadFile);

        return resultPath;
    }

    /**
     * 创建文件夹路径
     */
    private static void createDirIfNotExists() throws FileNotFoundException {
        if (!absolutePath.isEmpty()) {
            return;
        }

        //获取跟目录
        File file = new File(ResourceUtils.getURL("classpath:").getPath());

        if (!file.exists()) {
            // “”代表当前工作目录
            file = new File("");
        }

        absolutePath = file.getAbsolutePath();

        File upload = new File(absolutePath, staticDir + fileDir);
        if (!upload.exists()) {
            if (upload.mkdirs()) {
                log.debug(upload.getAbsolutePath());
            }
        }
        log.info(upload.getAbsolutePath());
    }

    /**
     * 删除文件
     *
     * @param path 文件访问的路径upload开始 如： /upload/image/test.jpg
     * @return true 删除成功； false 删除失败
     */
    public static boolean delete(String path) {
        File file = new File(absolutePath, staticDir + path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
