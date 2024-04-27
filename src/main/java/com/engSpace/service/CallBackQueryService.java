package com.engSpace.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import com.engSpace.dto.response.WordTranslateResponse;
import com.engSpace.entity.WordEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static com.engSpace.dto.Constants.*;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Getter
public class CallBackQueryService {
	
	final private UserService userService;
	
	public EditMessageText getEditMessageText(CallbackQuery callback) {
		String callBackData = callback.getData();
		
		if(callBackData.equals(MORE_DITALSE)||callBackData.equals(LESS_DITALSE))
			return moreLessInfoCallBack(callback);
		else if(callBackData.equals(SAVE_WORD)||callBackData.equals(NOT_SAVE_WORD))
			return saveWordInfoCallBack(callback);
		return new EditMessageText();
	}

	private EditMessageText saveWordInfoCallBack(CallbackQuery callback) {
		long messageId = callback.getMessage().getMessageId();
		long chatId = callback.getMessage().getChatId();
		EditMessageText editMessageText = new EditMessageText("ну и пожалуйста,  ну и не надо");
		editMessageText.setChatId(String.valueOf(chatId));
		editMessageText.setMessageId((int) messageId);
		if (callback.getData().equals(SAVE_WORD)) {
			WordTranslateResponse res = userService.getlastestUserResponse(chatId);
			WordEntity wordEntity = new WordEntity(res.getUserWord(), res, LocalDate.now().plusDays(1), 5);
			userService.addWord(chatId, wordEntity);
			editMessageText.setText("Катя добавила в базу");
		}
		return editMessageText;
	}

	private EditMessageText moreLessInfoCallBack(CallbackQuery callback) {
		long messageId = callback.getMessage().getMessageId();
		long chatId = callback.getMessage().getChatId();
		WordTranslateResponse res = userService.getlastestUserResponse(chatId);
		EditMessageText editMessageText = new EditMessageText(res.getTrans()+ "\n\n save words?");
		editMessageText.setChatId(String.valueOf(chatId));
		editMessageText.setMessageId((int) messageId);
		editMessageText.setReplyMarkup(ButtomSeting.getkeyboardYesNoMarkup(SAVE_WORD, NOT_SAVE_WORD));
		if (callback.getData().equals(MORE_DITALSE))
			editMessageText.setText(res.toString() + "\n\n save words?");

		return editMessageText;
	}
	
	

}
