package com.journal.journal.cmd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.journal.cqrses.infrastructure.CommandDispatcher;
import com.journal.journal.cmd.api.commands.CommandHandler;

@SpringBootApplication
public class JournalCmdApplication {

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	private CommandHandler commandHandler;

	public static void main(String[] args) {
		SpringApplication.run(JournalCmdApplication.class, args);
	}

}
