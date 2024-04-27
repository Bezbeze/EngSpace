package com.engSpace.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.engSpace.config.BotConfig;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
public class TelegramBot extends TelegramLongPollingBot {

	final TextMessageService textMessageService;
	final BotConfig config;
	final CallBackQueryService callBackQueryService;

	@PostConstruct
	public void setCommandsMenu() {
		List<BotCommand> commands = new ArrayList<BotCommand>();
		commands.add(new BotCommand("/start", "welcom message"));
		commands.add(new BotCommand("/mydata", "get your data stored"));
		commands.add(new BotCommand("/deletedata", "delete all your data"));
		commands.add(new BotCommand("/help", "info how to use bot"));
		commands.add(new BotCommand("/settings", "set your prefetrenses"));
		try {
			this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
		} catch (TelegramApiException e) {
			log.error("Error setting bot's command list " + e.getMessage());
		}

	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage()) {
			Message message = update.getMessage();
			sendMessage(textMessageService.routeMessage(message));
		} else if (update.hasCallbackQuery()) {
			CallbackQuery hasCallback = update.getCallbackQuery();
			sendMessage(callBackQueryService.getEditMessageText(hasCallback));
		}
	}

	private void sendMessage(BotApiMethod<?> sendMessage) {

		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			log.error("Error occurred: " + e.getMessage());
		}
	}
	

	@Override
	public String getBotUsername() {
		return config.getBotName();
	}

	@Override
	public String getBotToken() {
		return config.getToken();
	}
}
