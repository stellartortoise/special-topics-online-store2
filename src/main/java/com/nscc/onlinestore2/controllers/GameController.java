package com.nscc.onlinestore2.controllers;

import com.nscc.onlinestore2.dto.GameDTO;
import com.nscc.onlinestore2.entity.Game;
import com.nscc.onlinestore2.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // --- READ (All) ---
    @GetMapping("/")
    public List<Game> getAllGames() { // Changed to camelCase
        return gameService.getAllGames();
    }

    // --- READ (One) ---
    @GetMapping("/{id}")
    public Game getGameById(@PathVariable long id) {
        return gameService.getGameById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Game not found"));
    }

    // --- CREATE ---
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED) // Returns 201 Created
    public Game createGame(@Valid @RequestBody GameDTO gameDTO) {
        Game game = new Game();
        game.setName(gameDTO.getName());
        game.setDescription(gameDTO.getDescription());
        game.setCategory(gameDTO.getCategory());
        game.setPrice(gameDTO.getPrice());
        game.setImage(gameDTO.getImage());
        game.setCreateDate(String.valueOf(new Date()));
        game.setDeveloper(gameDTO.getDeveloper());
        game.setPlatform(gameDTO.getPlatform());
        game.setEsrbRating(gameDTO.getEsrbRating());

        return gameService.createGame(game);
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public Game updateGame(@PathVariable long id, @Valid @RequestBody GameDTO gameDTO) {
        return gameService.updateGame(id, gameDTO);
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable long id) {
        gameService.deleteGame(id);
    }
}