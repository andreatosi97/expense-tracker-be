package com.imatcoding.expensetracker.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Message (error, info, warning) of the api results
 */
@Getter
@Setter
@AllArgsConstructor
public class ResultMessage {
    private long code = 0;
    private MessageSeverity severity =  MessageSeverity.ERROR;
    private String text;
    private List<String> relatedFields = new ArrayList<>();

    public ResultMessage(String text) {
        this.text = text;
    }

    public ResultMessage(String text, List<String> relatedFields) {
        this.text = text;
        this.relatedFields = relatedFields;
    }
}
