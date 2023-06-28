package com.example.mininotes.repository;

import com.example.mininotes.models.Note;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface NoteRep extends JpaRepository<Note, Long>{
}
