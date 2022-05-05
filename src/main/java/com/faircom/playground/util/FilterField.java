package com.faircom.playground.util;

import com.faircom.playground.data.ComparisonOperator;

public class FilterField<T> {

    private T value;

    private ComparisonOperator operator = ComparisonOperator.EQ;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    public void setOperator(ComparisonOperator operator) {
        this.operator = operator;
    }
}
