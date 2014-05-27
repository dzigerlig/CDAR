package cdar.bll.entity;
 
import java.util.Date;

/**
 * Class which represents a WikiEntity, containing the Title and the WikiTitle of the specified Wiki-Page.
 *
 * @author dzigerli
 * @authro mtinner
 */
public class WikiEntity extends BasicEntity {
    
    /** The title. */
    private String title;
    
    /** The wikititle. */
    private String wikititle;
    
    /**
     * Default Constructor which calls the Constructor of the BasicEntity.
     */
    public WikiEntity() {
        super();
    }
 
    /**
     * Constructor passing all values which represents a WikiEntity.
     *
     * @param id as int
     * @param creationTime as Date Value
     * @param lastModification as Date Value
     * @param title of the specified Wiki-Page
     * @param wikititle of the specified Wiki-Page
     */
    public WikiEntity(int id, Date creationTime, Date lastModification,
            String title, String wikititle) {
        super(id, creationTime, lastModification);
        setTitle(title);
        setWikititle(wikititle);
    }   
     
    /**
     * Gets the title.
     *
     * @return String value containing the title of the Page
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title.
     *
     * @param title to be set as String Value
     */
    public void setTitle(String title) {
        this.title = title;
    }
     
    /**
     * Gets the wikititle.
     *
     * @return String value containing the Wiki Title of the specified Wiki Page
     */
    public String getWikititle() {
        return wikititle;
    }
     
    /**
     * Sets the wikititle.
     *
     * @param wikititle of the specified Wiki Page as String Value
     */
    public void setWikititle(String wikititle) {
        this.wikititle = wikititle;
    }
}