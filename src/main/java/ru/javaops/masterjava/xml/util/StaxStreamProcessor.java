package ru.javaops.masterjava.xml.util;

import ru.javaops.masterjava.xml.schema.FlagType;
import ru.javaops.masterjava.xml.schema.User;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StaxStreamProcessor implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private final XMLStreamReader reader;
    private final XMLEventReader eventReader;

    public StaxStreamProcessor(InputStream is) throws XMLStreamException {
        reader = FACTORY.createXMLStreamReader(is);
        eventReader = null;
    }

    public StaxStreamProcessor(InputStream is, boolean EventDriven) throws XMLStreamException {
        eventReader = FACTORY.createXMLEventReader(is);
        reader = null;
    }

    public XMLStreamReader getReader() {
        return reader;
    }

    public boolean doUntil(int stopEvent, String value) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == stopEvent) {
                if (value.equals(getValue(event))) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getValue(int event) throws XMLStreamException {
        return (event == XMLEvent.CHARACTERS) ? reader.getText() : reader.getLocalName();
    }

    public String getElementValue(String element) throws XMLStreamException {
        return doUntil(XMLEvent.START_ELEMENT, element) ? reader.getElementText() : null;
    }

    public String getProjectIdByName(String element, String projectName) throws XMLStreamException {
        String result = null;
        while (eventReader.hasNext()) {
            XMLEvent nextEvent = eventReader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals(element)) {
                    Attribute attribute = startElement.getAttributeByName(new QName("id"));
                    if (attribute != null) {
                        result = attribute.getValue();
                    }
                } else if (result != null && startElement.getName().getLocalPart().equals("name")) {
                    nextEvent = eventReader.nextEvent();
                    if (nextEvent.asCharacters().getData().equals(projectName)) {
                        return result;
                    }
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals(element)) {
                    result = null;
                }
            }
        }
        return result;
    }

    public List<User> getUsersByProjectId(String projectId) throws XMLStreamException {
        List<User> result = new ArrayList<>();
        User user = null;
        boolean addUser = false;
        while (eventReader.hasNext()) {
            XMLEvent nextEvent = eventReader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "User":
                        user = new User();
                        Attribute email = startElement.getAttributeByName(new QName("email"));
                        if (email != null) {
                            user.setEmail(email.getValue());
                        }
                        Attribute status = startElement.getAttributeByName(new QName("flag"));
                        if (status != null) {
                            user.setFlag(FlagType.fromValue(status.getValue()));
                        }
                        break;
                    case "fullName":
                        nextEvent = eventReader.nextEvent();
                        user.setFullName(nextEvent.asCharacters().getData());
                        break;
                    case "Project":
                        Attribute project = startElement.getAttributeByName(new QName("project"));
                        if (project != null && project.getValue().equals(projectId)) {
                            addUser = true;
                        }
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals("User")) {
                    if (addUser && !user.getFlag().equals(FlagType.DELETED)) {
                        result.add(user);
                    }
                    addUser = false;
                }
            }
        }
        return result;
    }

    public String getText() throws XMLStreamException {
        return reader.getElementText();
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                // empty
            }
        }
        if (eventReader != null) {
            try {
                eventReader.close();
            } catch (XMLStreamException e) {
                // empty
            }
        }
    }
}
