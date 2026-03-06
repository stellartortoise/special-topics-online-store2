package com.nscc.onlinestore2.service;

import com.nscc.onlinestore2.dto.GameDTO;
import com.nscc.onlinestore2.entity.Game;
import com.nscc.onlinestore2.repository.GameRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {

        this.gameRepository = gameRepository;
    }

    @Override
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @Override
    public Optional<Game> getGameById(Long id) {
        return gameRepository.findById(id);
    }

    @Override
    public Game createGame(Game Game) {
        return gameRepository.save(Game);
    }

    @Override
    public Game updateGame(long id, GameDTO gameDTO) {
        Game existingGame = gameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));

        existingGame.setName(gameDTO.getName());
        existingGame.setDescription(gameDTO.getDescription());
        existingGame.setCategory(gameDTO.getCategory());
        existingGame.setPrice(gameDTO.getPrice());
        existingGame.setImage(gameDTO.getImage());

        existingGame.setDeveloper(gameDTO.getDeveloper());
        existingGame.setPlatform(gameDTO.getPlatform());
        existingGame.setEsrbRating(gameDTO.getEsrbRating());

        return gameRepository.save(existingGame);
    }

    @Override
    public void deleteGame(long id) {
        if (!gameRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found");
        }

        gameRepository.deleteById(id);
    }
}
