package cinemetroproject.cinemetro;


public class Photo {
    private int id;
    private String name;
    private int station_id;
    private String description;

    public Photo(){}

    public Photo(String name,int station_id, String des)
    {
        this.name = name;
        this.station_id = station_id;
        this.description = des;
    }

    /***************************************************************
     * Get Functions
     ***************************************************************/

    /**
     *
     * @return id of the photo
     */
    public int getId()
    {
        return this.id;
    }

    /**
     *
     * @return name of the photo,used for display
     */
    public String getName()
    {
        return this.name;
    }

    /**
     *
     * @return the station id this photo belongs to
     */
    public int getStation_id()
    {
        return station_id;
    }

    /**
     *
     * @return description of this photo
     */
    public String getDescription()
    {
        return this.description;
    }

    /***************************************************************
     * Set Functions
     ***************************************************************/

    /**
     * Set id of this station
     * @param id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Set the name of the photo
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Set the station id this photo belong to
     * @param station_id
     */
    public void setStation_id(int station_id)
    {
        this.station_id = station_id;
    }

    /**
     * Set the description of the photo
     * @param des
     */
    public void setDescription(String des)
    {
        this.description = des;
    }
}
