package com.faircom.playground.data;

import FairCom.CtreeDb.Types.OPERATOR_TYPE;

public enum ComparisonOperator {

    EQ(OPERATOR_TYPE.EQ),
    GT(OPERATOR_TYPE.GT),
    GTE(OPERATOR_TYPE.GE),
    LT(OPERATOR_TYPE.LT),
    LTE(OPERATOR_TYPE.LE),
    NE(OPERATOR_TYPE.NE);

    public final int code;

    ComparisonOperator(int code) {

        this.code = code;
    }
}
