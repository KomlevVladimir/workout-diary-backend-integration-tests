package com.vladimirkomlev.workoutdiary.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class AllMessagesResponse {
    @JsonProperty("total")
    private int total;
    @JsonProperty("count")
    private int count;
    @JsonProperty("items")
    private List<Item> items;

    @Override
    public String toString() {
        return "AllMessagesResponse{" +
                "total=" + total +
                ", count=" + count +
                ", items=" + items +
                '}';
    }

    public int getTotal() {
        return total;
    }

    public int getCount() {
        return count;
    }

    public List<Item> getItems() {
        return items;
    }
}
