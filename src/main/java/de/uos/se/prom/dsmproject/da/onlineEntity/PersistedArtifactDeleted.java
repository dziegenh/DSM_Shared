package de.uos.se.prom.dsmproject.da.onlineEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import de.uos.se.prom.dsmproject.da.entity.PersistedArtifact;

@XmlRootElement
public class PersistedArtifactDeleted {
	
	@XmlElement
	public PersistedArtifact deleted;
	
	private PersistedArtifactDeleted() {
		//needed for JAXB 
	}
	
	public PersistedArtifactDeleted(PersistedArtifact pArtifact) {
		this.deleted = pArtifact;
	}
	

}
