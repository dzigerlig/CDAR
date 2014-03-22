package cdar.dal.persistence.hibernate.knowledgeproducer;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "nodelink")
@NamedQueries({ @NamedQuery(name = "findKnowledgeNodeLinkById", query = "from KnowledgeNodeLinkDao knl where knl.id = :id" )})
public class KnowledgeNodeLinkDao {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name = "creation_time")
	private Date creation_time;
	
	@Column(name = "last_modification_time")
	private Date last_modification_time;
	
	@ManyToOne
	@JoinColumn(name="ktrid")
	private KnowledgeTreeDao knowledgeTree;
	
	@ManyToOne
	@JoinColumn(name = "sourceid")
	private KnowledgeNodeDao knowledgeSourceNode;
	
	@ManyToOne
	@JoinColumn(name = "targetid")
	private KnowledgeNodeDao knowledgeTargetNode;
	
	@ManyToOne
	@JoinColumn(name = "ksnid", nullable = true)
	private KnowledgeSubNodeDao knowledgeSubNode;
	
	public KnowledgeNodeLinkDao() {
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreationTime() {
		return creation_time;
	}

	public void setCreationTime(Date creation_time) {
		this.creation_time = creation_time;
	}

	public Date getLastModificationTime() {
		return last_modification_time;
	}

	public void setLastModificationTime(Date last_modification_time) {
		this.last_modification_time = last_modification_time;
	}

	public KnowledgeTreeDao getKnowledgeTree() {
		return knowledgeTree;
	}

	public void setKnowledgeTree(KnowledgeTreeDao knowledgeTree) {
		this.knowledgeTree = knowledgeTree;
	}

	public KnowledgeNodeDao getKnowledgeSourceNode() {
		return knowledgeSourceNode;
	}

	public void setKnowledgeSourceNode(KnowledgeNodeDao knowledgeSourceNode) {
		this.knowledgeSourceNode = knowledgeSourceNode;
	}

	public KnowledgeNodeDao getKnowledgeTargetNode() {
		return knowledgeTargetNode;
	}

	public void setKnowledgeTargetNode(KnowledgeNodeDao knowledgeTargetNode) {
		this.knowledgeTargetNode = knowledgeTargetNode;
	}

	public KnowledgeSubNodeDao getKnowledgeSubNode() {
		return knowledgeSubNode;
	}

	public void setKnowledgeSubNode(KnowledgeSubNodeDao knowledgeSubNode) {
		this.knowledgeSubNode = knowledgeSubNode;
	}
}
