package com.ronanski11.statzinator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ronanski11.statzinator.model.Player;
import com.ronanski11.statzinator.repository.PlayerRepository;

@Service
public class PlayerService {
	
	@Autowired
	PlayerRepository playerRepository;

	public List<Player> getAllPlayers() {
		return playerRepository.findAll();
	}

	public Player getPlayerById(Integer id) {
		return playerRepository.findById(id).get();
	}

}
