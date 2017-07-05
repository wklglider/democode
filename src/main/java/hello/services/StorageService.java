package hello.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import hello.entities.FileEntity;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file);

    List<FileEntity> loadAll();

    FileEntity load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
