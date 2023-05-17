package com.journal.journal.cmd.api.dto;

 
import com.journal.journal.common.dto.BaseResponse;

import lombok.Data;

@Data
public class OpenAccountResponse extends BaseResponse {
    private String id;

    public OpenAccountResponse(String message, String id) {
        super(message);
        this.id = id;
    }
}
