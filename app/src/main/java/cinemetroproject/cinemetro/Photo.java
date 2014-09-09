package cinemetroproject.cinemetro;


public class Photo {
    private int id;
    private String name;
    private int station_id;
    private int movie_id;
    private String description;

    public Photo(){}

    public Photo(String name,int station_id, int movie_id, String des)
    {
        this.name = name;
        this.station_id = station_id;
        this.movie_id = movie_id;
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
     * @return the id of the station this photo belongs to
     * if this value is -1 the photo does not belong to a station
     */
    public int getStation_id()
    {
        return station_id;
    }

    /**
     *
     * @return the id of the movie this photo belongs to
     * if this value is -1 the photo does not belong to a movie
     */
    public int getMovie_id()
    {
        return this.movie_id;
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
     * Set the station id this photo belongs to
     * @param station_id
     */
    public void setStation_id(int station_id)
    {
        this.station_id = station_id;
    }

    /**
     * Set the movie id this photo belongs to
     * @param movie_id
     */
    public void setMovie_id(int movie_id)
    {
        this.movie_id = movie_id;
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
