package com.engSpace.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.engSpace.dto.UserState;
import com.engSpace.dto.response.WordTranslateResponse;
import com.engSpace.entity.UserEntity;
import com.engSpace.entity.WordEntity;
import com.engSpace.repository.UserRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Getter
public class UserService {

	final private UserRepository repository;
	final private MongoTemplate mongoTemplate;
	private Map<Long, UserState> usersStatus = new HashMap<>();
	private Map<Long, WordTranslateResponse> lastestUserResponseTranslate = new HashMap<>();
	private Map<Long, List<WordEntity>> repeatToday = new HashMap<>();

	public List<WordEntity> getRepeatToday(Long chatId) {
		if(repeatToday.get(chatId) != null && !repeatToday.get(chatId).isEmpty()
				&& repeatToday.get(chatId).get(0).getNextReview().equals(LocalDate.now()))
			return repeatToday.get(chatId);
		UserEntity user = repository.findById(chatId).get();
		if (user != null && user.getWords() != null) {
			List<WordEntity>list = user.getWords()
				.stream()
				.filter(w -> w.getNextReview() != null && w.getNextReview().isEqual(LocalDate.now()))
				.toList();
			if (!list.isEmpty()) {
				repeatToday.put(chatId, list);
				changeDateForRepeate(chatId, list);
			}
		}
		return repeatToday.getOrDefault(chatId, new ArrayList<>());
	}

	private void changeDateForRepeate(Long chatId, List<WordEntity> words) {
		for (int i = 0; i < words.size(); i++) {
			WordEntity word = words.get(i);
			if(word != null && word.getReviewCount() > 0) {
				updateWord(chatId, word.getWord(), LocalDate.now().plusDays(3), word.getReviewCount()-1);
			}
		}
		
	}

	public Boolean isExists(Long chatId) {
		return repository.existsById(chatId);
	}

	public void addUser(UserEntity entity) {
		repository.save(entity);
	}

	public void setUserState(long chatId, UserState awaitingTranslationText) {
		usersStatus.put(chatId, awaitingTranslationText);
	}

	public UserState getUserState(long chatId) {
		return usersStatus.get(chatId);
	}

	public void setlastestUserResponse(long chatId, WordTranslateResponse word) {
		lastestUserResponseTranslate.put(chatId, word);
	}

	public WordTranslateResponse getlastestUserResponse(long chatId) {
		return lastestUserResponseTranslate.get(chatId);
	}

	public void addWord(long chatId, WordEntity word) {
		Query query = new Query(Criteria.where("_id").is(chatId));
		Update update = new Update().push("words", word);
		mongoTemplate.updateFirst(query, update, UserEntity.class);
	}

	public void updateWord(long userId, String word, LocalDate nextReview, int reviewCount) {
		Query query = new Query(
				new Criteria().andOperator(Criteria.where("_id").is(userId), Criteria.where("words.word").is(word)));
		Update update = new Update().set("words.$.nextReview", nextReview).set("words.$.reviewCount", reviewCount);
		mongoTemplate.updateFirst(query, update, UserEntity.class);
	}

}
