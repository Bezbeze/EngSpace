package com.engSpace.service;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public class ButtomSeting {
	
	
	public static ReplyKeyboardMarkup getStandartKeyboard() {
		
		KeyboardRow row = new KeyboardRow();
		row.add("translater");
		row.add("repeat today");
		List<KeyboardRow> keyboardRows = new ArrayList<>();
		keyboardRows.add(row);	
		ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(keyboardRows);
		markup.setResizeKeyboard(true);
		return markup;
	}
	
	public static InlineKeyboardMarkup getkeyboardYesNoMarkup(String yes, String no) {
		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
    	List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
    	List<InlineKeyboardButton> row = new ArrayList<>();
    	InlineKeyboardButton button = new InlineKeyboardButton();
    	button.setText("Yes");
    	button.setCallbackData(yes);
    	InlineKeyboardButton noButton = new InlineKeyboardButton();
    	noButton.setText("No");
    	noButton.setCallbackData(no);
    	row.add(button);
    	row.add(noButton);
    	
    	rowsInLine.add(row);
    	keyboardMarkup.setKeyboard(rowsInLine);
		return keyboardMarkup;
	}

}
