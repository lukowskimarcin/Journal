package com.journal.journal.cmd.api.commands;

 
import com.journal.cqrses.commands.BaseCommand;
import com.journal.journal.common.dto.AccountType;

import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private AccountType accountType;
    private double openingBalance;
}