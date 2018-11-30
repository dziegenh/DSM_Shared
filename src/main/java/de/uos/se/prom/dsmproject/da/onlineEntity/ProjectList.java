package de.uos.se.prom.dsmproject.da.onlineEntity;

import java.util.List;

import javax.xml.bind.annotation.*;

/**
 * This Class is for Serialization of the Project List
 * @author Markus Mohr
 *
 */

@XmlRootElement(name = "projectList")
public class ProjectList {
	
    private List<String> projects;
    

    @XmlElement(name = "project")
	public List<String> getProjects() {
        return projects;
    }
 
    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

}
