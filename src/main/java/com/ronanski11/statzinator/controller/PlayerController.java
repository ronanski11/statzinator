package com.ronanski11.statzinator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ronanski11.statzinator.model.Player;
import com.ronanski11.statzinator.service.PlayerService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

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
	
	@GetMapping("/{id}")
	public ResponseEntity<Player> getPlayerById(@PathVariable Integer id) {
		return ResponseEntity.ok(playerService.getPlayerById(id));
	}

}
