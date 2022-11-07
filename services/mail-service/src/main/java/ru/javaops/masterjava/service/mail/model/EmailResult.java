package ru.javaops.masterjava.service.mail.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class EmailResult {
    private Integer id;

    @Column("to")
    private @NonNull String emailTo;

    @Column("copy")
    private @NonNull String emailCc;

    private @NonNull String subject;

    private @NonNull String body;

    private @NonNull String result;
}
