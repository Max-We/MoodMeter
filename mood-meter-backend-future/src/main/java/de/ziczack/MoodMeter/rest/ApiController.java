package de.ziczack.MoodMeter.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.ziczack.MoodMeter.rest.cache.MapCache;
import de.ziczack.MoodMeter.rest.dto.AnswerDto;
import de.ziczack.MoodMeter.rest.dto.QuestionDto;

@RestController
public class ApiController {

	private static final AtomicLong ID_GENERATOR = new AtomicLong(1000);
	
	@GetMapping("/questions")
	public List<QuestionDto> getAllQuestions() {
		List<QuestionDto> qs = new ArrayList<>();
		for (QuestionDto question : MapCache.getQuestionCache().values()) {
			qs.add(question);
		}
		return qs;
	}
	
	@GetMapping("/questions/{id}")
	public QuestionDto getQuestion(@PathVariable long id) {
		QuestionDto questionDto = MapCache.getQuestionCache().get(id);
		if (questionDto == null) {
			throw new RuntimeException();
		}
		return questionDto;
	}
	
	@PostMapping("/answer/{id}")
	public AnswerDto postAnswer(@PathVariable long id, @RequestBody AnswerDto newAnswer) {
		QuestionDto questionDto = MapCache.getQuestionCache().get((Long) id);
		if (questionDto == null) {
			throw new RuntimeException();
		}
		AnswerDto answer = new AnswerDto();
		answer.setDate(newAnswer.getDate());
		answer.setId(ID_GENERATOR.incrementAndGet());
		answer.setNote(newAnswer.getNote());
		answer.setValue(newAnswer.getValue());
		MapCache.getAnswerCache().put(answer.getId(), answer);
		questionDto.addAnswer(answer);
		return answer;
	}
}
