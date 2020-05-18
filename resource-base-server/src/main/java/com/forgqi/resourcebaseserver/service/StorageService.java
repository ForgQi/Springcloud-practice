package com.forgqi.resourcebaseserver.service;

import com.forgqi.resourcebaseserver.common.errors.UnsupportedOperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class StorageService implements InitializingBean {
    @Value("${storage.location}")
    private final String location = "upload-dir";

    private Path uploadDir;

    void init(Path path) {
        try {
            Path directories = Files.createDirectories(path);
            log.info(directories.toAbsolutePath().toString());
        } catch (IOException e) {
            log.error("Could not initialize storage", e);
        }

    }

    public String store(String path, String filename, MultipartFile file) {
        try {
            Files.createDirectories(uploadDir.resolve(path));
            file.transferTo(uploadDir.resolve(Path.of(path, filename)));
            return "/upload/" + path + "/" + filename;
        } catch (IOException e) {
            log.error("Failed to store file{} ", filename, e);
            throw new UnsupportedOperationException("Failed to store file " + filename, e);
        }
    }

    public void delete(String path) throws IOException {
        Files.delete(uploadDir.resolve(path));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        uploadDir = Paths.get(location);
        init(uploadDir);
    }
}
