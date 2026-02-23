package com.zkteco.apizk.dto;

import java.time.LocalDateTime;

public interface AccTransactionDTO {

    String getId();

    LocalDateTime getEventTime();

    String getEventName();

    String getEventDescription();

    String getDevAlias();

    String getReaderName();

    String getVerifyModeName();

    String getPersonPin();

    String getPersonName();

    String getPersonLastName();

    String getDepartmentName();
}
