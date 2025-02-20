package com.Gen2Play.VideoService.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.Gen2Play.VideoService.model.entity.Save;

@Repository
public interface SaveRepository extends GenericRepository<Save, UUID>{
    List<Save> findByUserId (UUID owner);
}
