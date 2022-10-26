package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.IntStreamEx;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {

    public City insert(City city) {
        if (city.isNew()) {
            int id = insertGeneratedId(city);
            city.setId(id);
        } else {
            insertWitId(city);
        }
        return city;
    }

    @SqlUpdate("TRUNCATE cities")
    @Override
    public abstract void clean();

    @SqlUpdate("INSERT INTO cities (short_name,full_name) VALUES (:shortName, :fullName) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean City city);

    @SqlUpdate("INSERT INTO cities (id,short_name,full_name) VALUES (:id,:shortName, :fullName) ")
    abstract void insertWitId(@BindBean City city);

    @SqlBatch("INSERT INTO cities (short_name,full_name) VALUES (:shortName, :fullName)" +
            "ON CONFLICT DO NOTHING")
    public abstract int[] insertBatch(@BindBean List<City> users, @BatchChunkSize int chunkSize);

    public List<String> insertAndGetLogs(List<City> cities) {
        int[] result = insertBatch(cities, cities.size());
        return IntStreamEx.range(0, cities.size())
                .filter(i -> result[i] != 0)
                .mapToObj(index -> cities.get(index).getFullName())
                .toList();
    }
}
