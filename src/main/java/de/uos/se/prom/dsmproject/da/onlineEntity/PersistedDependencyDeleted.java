package de.uos.se.prom.dsmproject.da.onlineEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.uos.se.prom.dsmproject.da.entity.PersistedDependency;

/**
 * This Class is for Serialization when Dependency is deleted
 * @author Markus Mohr
 *
 */


@XmlRootElement
public class PersistedDependencyDeleted {
	
	@XmlElement
	public PersistedDependency deleted;
	
	private PersistedDependencyDeleted() {
		//needed for JAXB
	}
	
	public PersistedDependencyDeleted(PersistedDependency pDependency) {
		this.deleted = pDependency;
	}

}
