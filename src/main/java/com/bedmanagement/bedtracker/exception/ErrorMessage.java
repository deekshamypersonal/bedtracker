package com.bedmanagement.bedtracker.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ErrorMessage {

    private Date timestamp;
    private List<String> message;

    public ErrorMessage(Date timestamp, List<String> message)
    {
        this.timestamp = timestamp;
        this.message = message;
    }
}
