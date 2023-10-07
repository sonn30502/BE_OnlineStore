package com.example.online_farm.Repository;

import com.example.online_farm.Entity.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Images, Integer> {
}
