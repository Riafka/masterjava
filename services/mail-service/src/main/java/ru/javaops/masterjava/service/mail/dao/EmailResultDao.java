package ru.javaops.masterjava.service.mail.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.dao.AbstractDao;
import ru.javaops.masterjava.service.mail.model.EmailResult;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class EmailResultDao implements AbstractDao {
    @Override
    @SqlUpdate("TRUNCATE email")
    public abstract void clean();

    @SqlUpdate("INSERT INTO email_result (\"to\", copy,subject,body,result) VALUES (:emailTo, :emailCc,:subject,:body,:result)")
    public abstract void insert(@BindBean EmailResult emailResult);
}
