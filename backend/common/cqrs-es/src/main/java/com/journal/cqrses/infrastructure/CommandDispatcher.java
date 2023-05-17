package com.journal.cqrses.infrastructure;

import com.journal.cqrses.commands.BaseCommand;
import com.journal.cqrses.commands.CommandHandlerMethod;

public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);
    void send(BaseCommand command);
}
