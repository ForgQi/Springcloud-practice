package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.common.errors.NonexistenceException;
import com.forgqi.resourcebaseserver.common.util.FileHandleUtil;
import com.forgqi.resourcebaseserver.common.util.UserHelper;
import com.forgqi.resourcebaseserver.entity.User;
import com.forgqi.resourcebaseserver.entity.UserFile;
import com.forgqi.resourcebaseserver.repository.FileRepository;
import com.forgqi.resourcebaseserver.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class UploadController {
    private final FileRepository fileRepository;
    private final StorageService storageService;


    @PostMapping("/image")
    public Map<String, String> receiveImage(@RequestPart("upload") MultipartFile file) {
        Map<String, String> map = new HashMap<>();
        try {
            String name = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            String hex = DigestUtils.md5DigestAsHex(inputStream);
            String type = null;
            if (name != null) {
                type = name.substring(name.lastIndexOf(".") == -1 ? name.length() - 1 : name.lastIndexOf("."));
            }
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                // 如果bufferedImage为空，证明上传的文件不是图片，获取图片流失败，不进行下面的操作
                throw new IOException("上传的不是图片，请上传图片");
            }
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            String path = FileHandleUtil.upload(file, "image/", hex + "_" + width + "x" + height + type);
            fileRepository.save(new UserFile(hex, name, path, file.getContentType(), UserHelper.getUserBySecurityContext().orElse(new User()).getUserName()));
            map.put("uploaded", "true");
//            map.put("url", "http://localhost:8080"+path);
            map.put("url", path);
        } catch (IOException e) {
            log.error("上传错误", e);
            map.put("uploaded", "false");
            map.put("url", "#");
        }
        return map;
    }

    @DeleteMapping("/image")
    public Integer deleteImage(String path) {
        boolean isDelete = FileHandleUtil.delete(path);
        if (isDelete) {
            return fileRepository.deleteUserFileByPath(path);
        }
        throw new NonexistenceException("文件不存在或删除错误:" + path);
    }

    @PostMapping("/file")
    public Map<String, String> receiveFile(@RequestPart("upload") MultipartFile file) {
        Map<String, String> map = new HashMap<>();

        String name = file.getOriginalFilename();

        String path = storageService.store("files", name, file);
        map.put("uploaded", "true");
        map.put("url", path);

        return map;
    }

    @DeleteMapping("/file")
    public void deleteFile(String path) {
        try {
            storageService.delete(path);
        } catch (IOException e) {
            throw new NonexistenceException("文件不存在或删除错误:" + path);
        }
    }
}
