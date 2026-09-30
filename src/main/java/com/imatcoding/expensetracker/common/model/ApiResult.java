package com.imatcoding.expensetracker.common.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApiResult<T> {
    private T data;
    private List<ResultMessage> messages = new ArrayList<>();

    public ApiResult(T data) {
        this.data = data;
    }
}