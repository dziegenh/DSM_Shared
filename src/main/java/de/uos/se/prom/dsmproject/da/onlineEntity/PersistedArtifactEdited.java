package de.uos.se.prom.dsmproject.da.onlineEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.uos.se.prom.dsmproject.da.entity.PersistedArtifact;

@XmlRootElement
public class PersistedArtifactEdited {
	
	@XmlElement
	public PersistedArtifact before;
	
	@XmlElement
	public PersistedArtifact after;
	
	private PersistedArtifactEdited() {
		//needed for JAXB
	}
	
	public PersistedArtifactEdited(PersistedArtifact before, PersistedArtifact after) {
		this.before = before;
		this.after = after;
	}
	
}
