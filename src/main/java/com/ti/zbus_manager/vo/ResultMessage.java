package com.ti.zbus_manager.vo;

import lombok.Data;

@Data
public class ResultMessage {
    private String message;

    public ResultMessage() {
    }

    public ResultMessage(String message) {
        this.message = message;
    }
}
