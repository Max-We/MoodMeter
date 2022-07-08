package de.ziczack.moodmeter.team;

import static de.ziczack.moodmeter.MoodMeterConstants.ID_PATH;

import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.ziczack.moodmeter.user.MoodMeterUserRespository;

@RestController
public class MoodMeterTeamController {

	private static final String TEAM = "/team";
	private static final AtomicLong ID_GENERATOR = new AtomicLong(1000);
	private static final Random RANDOM = new Random();
	
	@Autowired
	private MoodMeterTeamRepository teamRepo;
	
	@Autowired
	private MoodMeterUserRespository userRepo;
	
	@PostMapping(TEAM)
	MoodMeterTeam newTeam(@RequestBody MoodMeterTeamDto newTeam) {
		MoodMeterTeam team = new MoodMeterTeam();
		team.setTeamId(ID_GENERATOR.incrementAndGet());
		team.setTeamName(newTeam.getTeamName());
		team.setTeamLink(generateTeamLink());
		team.setTeamOwner(userRepo.findById(newTeam.getTeamOwner()).orElseThrow());
		return teamRepo.save(team);
	}
	
	@GetMapping(TEAM + ID_PATH)
	MoodMeterTeam getTeam(@PathVariable long id) {
		return teamRepo.findById(id).orElseThrow();
	}
	
	@PutMapping(TEAM + ID_PATH)
	MoodMeterTeam updateTeam(@PathVariable long id, @RequestBody MoodMeterTeamDto newTeam) {
		return teamRepo.findById(id).map(team -> {
			team.setTeamName(newTeam.getTeamName());
			return teamRepo.save(team);
		}).orElseThrow();
	}
	
	@DeleteMapping(TEAM + ID_PATH)
	public void deleteTeam(@PathVariable long id) {
		teamRepo.deleteById(id);
	}

	private String generateTeamLink() {
		byte[] random = new byte[16];
		RANDOM.nextBytes(random);
		return Base64.getEncoder().encodeToString(random);
	}
}
