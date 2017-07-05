package hello.services;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import hello.dao.FileDAO;
import hello.entities.FileEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final FileDAO fileDAO;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, FileDAO fileDAO) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.fileDAO = fileDAO;
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
            fileDAO.save(new FileEntity(file.getOriginalFilename(), new Date().toString()));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public List<FileEntity> loadAll() {
    	List<FileEntity> list = new ArrayList<>();
    	for (FileEntity file : fileDAO.findAll()) {
    		list.add(file);
    	}
    	return list;

    }

    @Override
    public FileEntity load(String filename) {
        return fileDAO.findOne(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = this.rootLocation.resolve(load(filename).getName());
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
