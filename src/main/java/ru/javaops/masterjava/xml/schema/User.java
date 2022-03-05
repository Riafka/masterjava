
package ru.javaops.masterjava.xml.schema;

import javax.xml.bind.annotation.*;
import java.util.Objects;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fullName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Projects" type="{http://javaops.ru}projectsType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="flag" use="required" type="{http://javaops.ru}flagType" />
 *       &lt;attribute name="city" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "email",
        "fullName",
        "projects"
})
@XmlRootElement(name = "User", namespace = "http://javaops.ru")
public class User {

    @XmlAttribute(name = "email", required = true)
    protected String email;
    @XmlElement(namespace = "http://javaops.ru", required = true)
    protected String fullName;
    @XmlElement(name = "Projects", namespace = "http://javaops.ru")
    protected ProjectsType projects;
    @XmlAttribute(name = "flag", required = true)
    protected FlagType flag;
    @XmlAttribute(name = "city", required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object city;

    /**
     * Gets the value of the email property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the fullName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the value of the fullName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFullName(String value) {
        this.fullName = value;
    }

    /**
     * Gets the value of the projects property.
     *
     * @return possible object is
     * {@link ProjectsType }
     */
    public ProjectsType getProjects() {
        return projects;
    }

    /**
     * Sets the value of the projects property.
     *
     * @param value allowed object is
     *              {@link ProjectsType }
     */
    public void setProjects(ProjectsType value) {
        this.projects = value;
    }

    /**
     * Gets the value of the flag property.
     *
     * @return possible object is
     * {@link FlagType }
     */
    public FlagType getFlag() {
        return flag;
    }

    /**
     * Sets the value of the flag property.
     *
     * @param value
     *     allowed object is
     *     {@link FlagType }
     *
     */
    public void setFlag(FlagType value) {
        this.flag = value;
    }

    /**
     * Gets the value of the city property.
     *
     * @return
     *     possible object is
     *     {@link Object }
     *
     */
    public Object getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     *
     * @param value
     *     allowed object is
     *     {@link Object }
     *
     */
    public void setCity(Object value) {
        this.city = value;
    }

    @Override
    public String toString() {
        //CityType cityType = (CityType)city;
        return "User{" +
                "email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                //", city=" +cityType.value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email) && fullName.equals(user.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, fullName);
    }
}
