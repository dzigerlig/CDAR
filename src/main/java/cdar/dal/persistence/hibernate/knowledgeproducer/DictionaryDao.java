package cdar.dal.persistence.hibernate.knowledgeproducer;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "dictionary")
@NamedQueries({ @NamedQuery(name = "findDictionaryById", query = "from DictionaryDao d where d.id = :id")})
public class DictionaryDao {

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name = "creation_time")
	private Date creation_time;
	
	@Column(name = "last_modification_time")
	private Date last_modification_time;
}
