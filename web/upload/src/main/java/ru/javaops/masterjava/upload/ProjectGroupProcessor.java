package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class ProjectGroupProcessor {
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);

    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);

    public Map<String, Group> process(StaxStreamProcessor processor) throws XMLStreamException {
        val projectMap = projectDao.getAsMap();
        val newProjects = new ArrayList<Project>();

        val groupMap = groupDao.getAsMap();
        val newGroups = new ArrayList<Group>();

        while (processor.startElement("Project", "Projects")) {
            int projectId;
            val name = processor.getAttribute("name");
            val description = processor.getElementValue("description");
            if (!projectMap.containsKey(name)) {
                projectId = projectDao.getSeqAndSkip(1);
                newProjects.add(new Project(projectId, name, description));
            } else {
                projectId = projectMap.get(name).getId();
            }
            while (processor.startElement("Group", "Project")) {
                val groupName = processor.getAttribute("name");
                val groupType = GroupType.valueOf(processor.getAttribute("type"));
                if (!groupMap.containsKey(groupName)) {
                    newGroups.add(new Group(groupName, groupType, projectId));
                }
            }
        }
        log.info("Insert projects: " + newProjects);
        projectDao.insertBatch(newProjects);
        log.info("Insert groups: " + newGroups);
        groupDao.insertBatch(newGroups);

        return groupDao.getAsMap();
    }
}
