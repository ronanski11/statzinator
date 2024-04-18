package com.ronanski11.statzinator.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ronanski11.statzinator.model.MessageResponse;
import com.ronanski11.statzinator.model.Player;
import com.ronanski11.statzinator.security.Roles;
import com.ronanski11.statzinator.service.PlayerService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/player")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class PlayerController {

	@Autowired
	PlayerService playerService;

	@GetMapping()
	public ResponseEntity<List<Player>> getAllPlayers() {
		return ResponseEntity.ok(playerService.getAllPlayers());
	}

	@PostMapping()
	@RolesAllowed(Roles.Admin)
	public ResponseEntity<Player> saveNewPlayer(@RequestBody Player player, @RequestParam int teamId) {
		return ResponseEntity.ok(playerService.saveNewPlayer(player, teamId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Player> getPlayerById(@PathVariable Integer id) {
		return ResponseEntity.ok(playerService.getPlayerById(id));
	}

	@PutMapping("/{id}")
	@RolesAllowed(Roles.Admin)
	public ResponseEntity<Player> updatePlayer(@RequestBody Player player, @PathVariable int id) {
		return ResponseEntity.ok(playerService.updatePlayer(player, id));
	}

	@DeleteMapping("/{id}")
	@RolesAllowed(Roles.Admin)
	public ResponseEntity<MessageResponse> deletePlayer(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok(playerService.deletePlayer(id));
		} catch (Throwable t) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/search")
	public ResponseEntity<List<Player>> searchForPlayer(@RequestParam(required = false) String playerName,
			@RequestParam(required = false) LocalDate date, @RequestParam(required = false) LocalDate startRange,
			@RequestParam(required = false) LocalDate endRange, @RequestParam(required = false) String teamName,
			@RequestParam(required = false) Integer teamId) {
		return ResponseEntity.ok(playerService.searchForPlayer(playerName, date, startRange, endRange, teamName, teamId));
	}
	
}
