package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;
import ru.javaops.masterjava.xml.util.XsltProcessor;

import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

public class MainXml {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    public static List<User> getUsersByProject(String projectName) throws Exception {
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        ProjectType project =
                payload.getProjects().getProject().stream()
                        .filter(projectType -> projectType.getName().equals(projectName))
                        .findFirst()
                        .orElseThrow(() -> new Exception("Project with name " + projectName + " is not exist"));

        return payload.getUsers().getUser().stream()
                .filter(Objects::nonNull)
                .filter(user -> !user.getFlag().equals(FlagType.DELETED))
                .filter(user -> user.getProjects().getProject().stream()
                        .anyMatch(projectReference -> {
                            if (projectReference.getProject() instanceof ProjectType) {
                                ProjectType projectType = (ProjectType) projectReference.getProject();
                                return projectType.getId().equals(project.getId());
                            } else {
                                return false;
                            }
                        }))
                .sorted(Comparator.comparing(User::getEmail))
                .collect(Collectors.toList());
    }

    public static List<User> getUsersByProjectStax(String projectName) throws Exception {
        String projectId;
        List<User> userList;
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream(), true)) {
            projectId = processor.getProjectIdByName("Project", projectName);
        }
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream(), true)) {
            userList = processor.getUsersByProjectId(projectId);
        }
        return userList.stream()
                .sorted(Comparator.comparing(User::getEmail))
                .collect(Collectors.toList());
    }

    public static String getUsersByProjectHtml(String projectName) throws Exception {
        List<User> usersByProject = getUsersByProject(projectName);
        return
                body(
                        h2("Users"),
                        table(attrs("#users-table"),
                                tbody(
                                        th("FullName"),
                                        th("email"),
                                        each(usersByProject, user -> tr(
                                                td(user.getFullName()),
                                                td(user.getEmail())
                                        ))
                                )
                        )
                ).render();
    }

    public static String getGroupsByProjectHtml(String projectName) throws Exception {
        try (InputStream xslInputStream = Resources.getResource("project_groups_to_html.xsl").openStream();
             InputStream xmlInputStream = Resources.getResource("payload.xml").openStream()) {

            XsltProcessor processor = new XsltProcessor(xslInputStream);
            processor.setParameter("projectName", projectName);
            return processor.transform(xmlInputStream);
        }
    }
}
