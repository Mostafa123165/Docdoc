package com.spring.Docdoc.repository;

import com.spring.Docdoc.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image,Long> {

    @Query("SELECT i FROM Image i WHERE i.name LIKE concat(:name,'%') ")
    Optional<Image> findByNameStartingWith(String name) ;

    Optional<Image> findByName(String name);

    void deleteByName(String name) ;
}
