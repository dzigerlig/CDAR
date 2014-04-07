package cdar.bll.model;

import java.util.HashSet;
import java.util.Set;

import cdar.bll.producer.Directory;
import cdar.dal.persistence.jdbc.producer.DirectoryDao;
import cdar.dal.persistence.jdbc.producer.ProducerDaoController;

public class DirectoryModel {
	private ProducerDaoController pdc = new ProducerDaoController();


	public Set<Directory> getDirectories(int treeid) {
		Set<Directory> ln = new HashSet<Directory>();
		for (DirectoryDao dd : pdc.getDirectories(treeid)) {
			ln.add(new Directory(dd));			
		}
		return ln;
	}

	public boolean removeDirectoryById(int id) {
		return pdc.getDirectory(id).delete();
	}

	public Directory addDirectory(Directory d)	
	{ 
		DirectoryDao directory = new DirectoryDao(d.getKtrid());
		directory.setParentid(d.getParentid());
		directory.setTitle(null);
		return new Directory(directory.create());
	}

	public Directory renameDirectory(Directory d) {
		DirectoryDao dd = pdc.getDirectory(d.getId());
		dd.setTitle(d.getTitle());
		return new Directory(dd.update());
	}

	public void moveDirectory(Directory d) {
		System.out.println("dictionary moved");		
	}
}
