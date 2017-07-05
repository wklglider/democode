package hello.dao;

import org.springframework.data.repository.CrudRepository;

import hello.entities.FileEntity;

public interface FileDAO extends CrudRepository<FileEntity, String> {

}
