package com.ronanski11.statzinator.dbinit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ronanski11.statzinator.model.Game;
import com.ronanski11.statzinator.model.GameStatus;
import com.ronanski11.statzinator.model.Player;
import com.ronanski11.statzinator.model.Stats;
import com.ronanski11.statzinator.model.Team;
import com.ronanski11.statzinator.repository.GameRepository;
import com.ronanski11.statzinator.repository.PlayerRepository;
import com.ronanski11.statzinator.repository.StatsRepository;
import com.ronanski11.statzinator.repository.TeamRepository;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("unused")
@Slf4j
public class DatabaseInit implements CommandLineRunner {

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private StatsRepository statsRepository;

	@Value("${run.database.init}")
	private boolean runDbInit;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		if (runDbInit) {
			log.info("Beginning database initialization...");
			initTeamsAndPlayers();
			initGames();
			initStats();
			log.info("Successfuly initlialized database!");
		} else {
			log.info("Database initialization skipped.");
		}
	}

	private void initStats() throws Exception {
		log.info("Reading Stats from 'Stats.json'...");
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = new ClassPathResource("Stats.json").getInputStream();
		List<Map<String, String>> statsMap = mapper.readValue(inputStream,
				new TypeReference<List<Map<String, String>>>() {
				});

		log.info("Assiging Stats to finished Games.");
		int counter = 0;
		for (Game game : gameRepository.findAll()) {
			if (game.getStatus().equals(GameStatus.OVER)) {
				Stats stats = new Stats();
				stats.setGame(game);
				stats.setStats(statsMap.get(counter));
				statsRepository.save(stats);
				counter++;
			}
		}

	}

	public void initGames() {
		log.info("Initializing Games...");
		List<Team> teams = teamRepository.findAll();
		LocalDateTime now = LocalDateTime.now();
		int counter = 0;
		for (Team team : teams) {
			for (int i = counter + 1; i < 10; i++) {
				Game game = new Game();
				game.setTeams(Arrays.asList(team, teams.get(i)));
				LocalDateTime gameTime = now.minusMonths(5)
						.plusDays(new java.util.Random().nextInt((int) ChronoUnit.DAYS
								.between(LocalDateTime.now().minusMonths(5), LocalDateTime.now().plusMonths(3)) + 1));

				game.setTime(gameTime);

				if (gameTime.isAfter(now)) {
					game.setStatus(GameStatus.PENDING);
				} else {
					game.setStatus(GameStatus.OVER);
				}
				gameRepository.save(game);
			}
			counter++;
		}

	}

	private List<Team> loadTeams() throws Exception {
		log.info("Reading Teams from 'Team.json'...");
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = new ClassPathResource("Team.json").getInputStream();
		return mapper.readValue(inputStream, new TypeReference<List<Team>>() {
		});
	}

	private List<Player> loadPlayers() throws Exception {
		log.info("Reading Players from 'Player.json'...");
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule()); // Necessary for LocalDate
		InputStream inputStream = new ClassPathResource("Player.json").getInputStream();
		return mapper.readValue(inputStream, new TypeReference<List<Player>>() {
		});
	}

	private void distributePlayers(List<Team> teams, List<Player> players) {
		log.info("Assigning Players to Teams...");
		int teamIndex = 0;
		for (int i = 0; i < players.size(); i++) {
			Team team = teams.get(teamIndex);
			Player player = players.get(i);
			player.setTeam(team);
			teamIndex = (teamIndex + 1) % teams.size(); // Ensure circular assignment
		}
	}

	public void initTeamsAndPlayers() throws Exception {
		List<Team> teams = loadTeams();
		List<Player> players = loadPlayers();
		List<Team> savedTeams = teamRepository.saveAll(teams);
		log.info("Successfully saved Teams.");
		distributePlayers(savedTeams, players);
		playerRepository.saveAll(players);
		log.info("Successfully saved Players.");
	}
}
