package cinemetroproject.cinemetro;

/**
 * Class for every event of the Timeline stations
 */
public class Milestone {
    private int id;
    private int station_id;
    private int year;
    private String des;
    private String photo_name;
    private String photo_des;

    public Milestone(){}
    public Milestone(int station_id, int year, String des, String p_n, String p_d)
    {
        this.station_id = station_id;
        this.year = year;
        this.des = des;
        this.photo_name = p_n;
        this.photo_des = p_d;
    }

    /***************************************************************
     * Get Functions
     ***************************************************************/

    /**
     *
     * @return id of the milestone
     */
    public int getId(){
        return this.id;
    }

    /**
     *
     * @return station id of the milestone
     */
    public int getStation_id()
    {
        return this.station_id;
    }

    /**
    *
    *@return year of the milestone
    */
    public int getYear()
    {
       return this.year;
    }

    /**
     *
     * @return description of the milestone
     */
    public String getDescription()
    {
        return this.des;
    }

    /**
     *
     * @return photo of the milestone, null if it does not have a photo
     */
    public String getPhotoName()
    {
        return this.photo_name;
    }

    /**
     *
     * @return photo of the milestone, null if it does not have a photo
     */
    public String getPhotoDescription()
    {
        return this.photo_des;
    }

    /***************************************************************
     * Set Functions
     ***************************************************************/

    /**
     * Set id of the milestone
     * @param id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Set station_id of the milestone
     * @param id
     */
    public void setStation_id(int id)
    {
        this.station_id = id;
    }

    /**
     * Set year of the milestone
     * @param year
     */
    public void setYear(int year)
    {
        this.year = year;
    }

    /**
     * Set description of this milestone
     * @param des
     */
    public void setDes(String des)
    {
        this.des = des;
    }

    /**
     * Set photo name of this milestone
     * @param name
     */
    public void setPhotoName(String name)
    {
        this.photo_name = name;
    }

    /**
     * Set photo name of this milestone
     * @param des
     */
    public void setPhotoDescription(String des)
    {
        this.photo_des = des;
    }

}
