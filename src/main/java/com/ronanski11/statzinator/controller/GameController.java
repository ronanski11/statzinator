package com.ronanski11.statzinator.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronanski11.statzinator.model.Game;
import com.ronanski11.statzinator.model.GameStatus;
import com.ronanski11.statzinator.security.Roles;
import com.ronanski11.statzinator.service.GameService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/game")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class GameController {
	
	@Autowired
	GameService gameService;
	
	@GetMapping()
	public ResponseEntity<List<Game>> getAllGames(@RequestParam(required = false) GameStatus status) {
		return ResponseEntity.ok(gameService.getAllGames(status));
	}
	
	@RolesAllowed(Roles.Admin)
	@PostMapping()
	public ResponseEntity<Game> saveNewGame(@RequestBody Game game) {
		return ResponseEntity.ok(gameService.saveNewGame(game));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Game> getGameById(@PathVariable Integer id) {
		return ResponseEntity.ok(gameService.getById(id));
	}
	
	@GetMapping("/team/{teamId}")
	public ResponseEntity<List<Game>> getAllGamesByTeam(@PathVariable Integer teamId, @RequestParam(required = false) GameStatus status) {
		return ResponseEntity.ok(gameService.getAllGamesByTeam(teamId, status));
	}
	
	@GetMapping("/date")
	public ResponseEntity<List<Game>> getGamesByDate(@RequestParam LocalDate date) {
		return ResponseEntity.ok(gameService.getGamesByDate(date));
	}
	
}
