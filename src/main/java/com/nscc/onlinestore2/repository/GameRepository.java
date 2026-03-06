package com.nscc.onlinestore2.repository;

import com.nscc.onlinestore2.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

//    @EntityGraph(attributePaths = {"voiceActors"})
//    Optional<Game> findGameWithVoiceActorsById(Long id);
}
