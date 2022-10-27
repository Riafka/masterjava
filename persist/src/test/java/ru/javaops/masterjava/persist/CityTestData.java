package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {
    public static City moscow;
    public static City spb;
    public static City kiev;
    public static City minsk;

    public static List<City> cities;

    public static void init() {
        moscow = new City(1, "msk", "Москва");
        spb = new City(2, "spb", "Санкт-Петербург");
        kiev = new City(3, "kiev", "Киев");
        minsk = new City(4, "mnsk", "Минск");
        cities = ImmutableList.of(moscow, spb, kiev, minsk);
    }

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> cities.forEach(dao::insert));
    }
}
