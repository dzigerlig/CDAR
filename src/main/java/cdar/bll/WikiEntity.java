package cdar.bll;
 
import java.util.Date;
 
public class WikiEntity extends BasicEntity {
    private String title;
    private String wikititle;
     
    public WikiEntity() {
        super();
    }
 
    public WikiEntity(int id, Date creationTime, Date lastModification,
            String title, String wikititle) {
        super(id, creationTime, lastModification);
        setTitle(title);
        setWikiTitle(wikititle);
    }   
     
    public String getTitle() {
        return title;
    }
     
    public void setTitle(String title) {
        this.title = title;
    }
     
    public String getWikiTitle() {
        return wikititle;
    }
     
    public void setWikiTitle(String wikititle) {
        this.wikititle = wikititle;
    }
}