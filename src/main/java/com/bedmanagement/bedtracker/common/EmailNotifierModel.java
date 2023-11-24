package com.bedmanagement.bedtracker.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmailNotifierModel {

    private String receiverMail;
    private String subject;
    private String message;

    public EmailNotifierModel(String receiverMail, String subject, String message) {
        this.receiverMail = receiverMail;
        this.subject = subject;
        this.message = message;
    }
}
