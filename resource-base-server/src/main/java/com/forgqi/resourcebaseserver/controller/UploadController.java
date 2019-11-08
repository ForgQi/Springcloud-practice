package com.forgqi.resourcebaseserver.controller;

import com.forgqi.resourcebaseserver.common.FileHandleUtil;
import com.forgqi.resourcebaseserver.entity.UserFile;
import com.forgqi.resourcebaseserver.repository.FileRepository;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/upload")
public class UploadController {
    private final FileRepository fileRepository;

    public UploadController(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @PostMapping("/image")
    public Map<String, String> receiveImage(@RequestPart("upload") MultipartFile file) {
        Map<String, String> map = new HashMap<>();
        try {
            String name = file.getOriginalFilename();
            InputStream inputStream = file.getInputStream();
            String hex = DigestUtils.md5DigestAsHex(inputStream);
            String type =null;
            if (name != null) {
                type = name.substring(name.lastIndexOf(".") == -1?name.length()-1:name.lastIndexOf("."));
            }
            String path = FileHandleUtil.upload(inputStream, "image/", hex);
            fileRepository.save(new UserFile(name, path, type, hex));
            map.put("uploaded","true");
//            map.put("url", "http://localhost:8080"+path);
            map.put("url", path);
        } catch (IOException e) {
            e.printStackTrace();
            map.put("uploaded","false");
            map.put("url", "#");
        }
        return map;
    }

    @DeleteMapping("/images")
    public UserFile deleteImage(@RequestBody String path) {
        FileHandleUtil.delete(path);
        return fileRepository.deleteByPath(path);
    }
}