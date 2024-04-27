package com.engSpace.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import static com.engSpace.service.ButtomSeting.*;

import com.engSpace.dto.UserState;
import com.engSpace.dto.response.WordTranslateResponse;
import com.engSpace.entity.UserEntity;
import com.engSpace.entity.WordEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static com.engSpace.dto.Constants.*;
@Service
@RequiredArgsConstructor
@Getter
public class TextMessageService {

	final private UserService userService;
	final private ApiTranslaterSerser translaterSerser;

	public SendMessage routeMessage(Message message) {
		String messageText = message.getText();
	    long chatId = message.getChatId();
	    UserState userState = userService.getUserState(chatId);
	    
	    if (userState == UserState.AWAITING_TRANSLATION_TEXT) {
	        userService.setUserState(chatId, UserState.NONE);
	        return handleTranslation(messageText, chatId);
	    }
	    
		switch (messageText) {
		case "/start":
			return startCommandReceived(message, chatId);

		case "translater":{
			return translateCommandReceived(chatId);
		}
		
		case "repeat today":{
			return getWordsForRepeadToday(chatId);
		}
		default:
			return SendMessage.builder()
					.chatId(String.valueOf(chatId))
					.text("sorry commad was unrecognized")
					.replyMarkup(getStandartKeyboard())
					.build();
		}
	}



	private SendMessage getWordsForRepeadToday(long chatId) {
		List<WordEntity> words = userService.getRepeatToday(chatId);
		if(words == null || words.isEmpty())
			return new SendMessage(String.valueOf(chatId), "сегодня нечего повторять");
		WordTranslateResponse res;
		String answer = "";
		for (int i = 0; i < words.size(); i++) {
			
			res = words.get(i).getWordData();
			answer += res.getUserWord() + "\n <tg-spoiler>"  + res.toString()+ "</tg-spoiler> \n\n" ;
		}
		
		SendMessage sendMessage = new SendMessage(String.valueOf(chatId), answer);
		sendMessage.setParseMode(ParseMode.HTML);
		return sendMessage;
	}



	private SendMessage handleTranslation(String messageText, long chatId) {
	    WordTranslateResponse translateResponse = translaterSerser.getTranslate(messageText);
	    translateResponse.setUserWord(messageText);
	    userService.setlastestUserResponse(chatId, translateResponse);
	    String answer = translateResponse.getTrans();
	    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), answer);
	    if(translateResponse.getDict()!=null) {
	    	answer += "\n would you like more info?";
		    sendMessage.setText(answer);
	    	sendMessage.setReplyMarkup(getkeyboardYesNoMarkup(MORE_DITALSE, LESS_DITALSE));
	    }
	    else sendMessage.setReplyMarkup(getStandartKeyboard());

		return sendMessage;
	}

	private SendMessage translateCommandReceived(long chatId) {
	    String answer = "Enter word or sentence to translate:";
	    SendMessage sendMessage = new SendMessage(String.valueOf(chatId), answer); 
	    userService.setUserState(chatId, UserState.AWAITING_TRANSLATION_TEXT);
	    return sendMessage;
	}

	private SendMessage startCommandReceived(Message message, long chatId) {
		if (!userService.isExists(chatId)) {
			String firstName = message.getChat().getFirstName();
			String secondName = message.getChat().getLastName();
			String userName = message.getChat().getUserName();
			UserEntity userEntity = 
					new UserEntity(chatId, firstName, secondName, userName, LocalDate.now(), new ArrayList<WordEntity>());
			userService.addUser(userEntity);
		}

		String answer = "hi " +  message.getChat().getFirstName() + ", nice to meet you";
		SendMessage sendMessage = new SendMessage(String.valueOf(chatId), answer);
		sendMessage.setReplyMarkup(getStandartKeyboard());
		return 	sendMessage;
	}
	

}
