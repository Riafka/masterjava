package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.StreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.Project;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class ProjectDao implements AbstractDao {

    @SqlUpdate("TRUNCATE project CASCADE ")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT * FROM project ORDER BY name")
    public abstract List<Project> getAll();

    public Map<String, Project> getAsMap() {
        return StreamEx.of(getAll()).toMap(Project::getName, g -> g);
    }

    @SqlUpdate("INSERT INTO project (name, description)  VALUES (:name, :description)")
    @GetGeneratedKeys
    public abstract int insertGeneratedId(@BindBean Project project);

    @SqlBatch("INSERT INTO project (id,name, description)  VALUES (:id,:name, :description)")
    public abstract void insertBatch(@BindBean Collection<Project> projects);

    @SqlQuery("SELECT nextval('common_seq')")
    abstract int getNextVal();

    @Transaction
    public int getSeqAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("SELECT setval('common_seq', " + (id + step - 1) + ")"));
        return id;
    }


    public void insert(Project project) {
        int id = insertGeneratedId(project);
        project.setId(id);
    }
}
