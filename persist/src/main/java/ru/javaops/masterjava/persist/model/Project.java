package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Project extends BaseEntity {

    @NonNull
    private String name;
    @NonNull
    private String description;

    public Project(Integer id, @NonNull String name, @NonNull String description) {
        super(id);
        this.name = name;
        this.description = description;
    }
}
