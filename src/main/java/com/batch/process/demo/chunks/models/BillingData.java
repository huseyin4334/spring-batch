package com.batch.process.demo.chunks.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingData {

    int dataYear;
    int dataMonth;
    int accountId;
    String phoneNumber;
    float dataUsage;
    int callDuration;
    int smsCount;
}
