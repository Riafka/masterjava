package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class City extends BaseEntity {
    @Column("short_name")
    private @NonNull String shortName;

    @Column("full_name")
    private @NonNull String fullName;

    public City(Integer id, String shortName, String fullName) {
        this(shortName, fullName);
        this.id = id;
    }
}
