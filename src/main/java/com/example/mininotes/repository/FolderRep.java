package com.example.mininotes.repository;

import com.example.mininotes.models.Folder;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FolderRep extends JpaRepository<Folder, Long> {
}
