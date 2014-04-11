package cdar.bll.producer.models;

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
	
	public Directory getDirectory(int id) {
		return new Directory(pdc.getDirectory(id));
	}

	public boolean removeDirectoryById(int id) {
		return pdc.getDirectory(id).delete();
	}

	public Directory addDirectory(int treeid, int parentid, String title)	
	{ 
		DirectoryDao directory = new DirectoryDao(treeid);
		directory.setParentid(parentid);
		directory.setTitle(title);
		return new Directory(directory.create());
	}

	public Directory renameDirectory(Directory d) {
		return updateDirectory(d);
	}
	
	public Directory updateDirectory(Directory d) {
		DirectoryDao dd = pdc.getDirectory(d.getId());
		dd.setKtrid(d.getKtrid());
		dd.setParentid(d.getParentid());
		dd.setTitle(d.getTitle());
		return new Directory(dd.update());
	}

	public void moveDirectory(Directory d) {
		DirectoryDao dd = pdc.getDirectory(d.getId());
		dd.setParentid(d.getParentid());
		dd.update();
	}
}
