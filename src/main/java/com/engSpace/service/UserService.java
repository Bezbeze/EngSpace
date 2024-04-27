package com.engSpace.service;



import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.engSpace.dto.UserState;
import com.engSpace.dto.response.WordTranslateResponse;
import com.engSpace.entity.UserEntity;
import com.engSpace.repository.UserRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Getter
public class UserService {
	
	final private UserRepository repository;
	private Map<Long, UserState> usersStatus = new HashMap<>();
	private Map<Long, WordTranslateResponse> lastestUserResponse = new HashMap<>();
	
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
		lastestUserResponse.put(chatId, word);
	}

	public WordTranslateResponse getlastestUserResponse(long chatId) {
		return lastestUserResponse.get(chatId);
	}
}
