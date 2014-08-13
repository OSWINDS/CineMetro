package cinemetroproject.cinemetro;

public class Station {

    private int id;
    private String name;
    private String description;
    private int route_id;
    private String colour;
    //There is one more attribute,the coordinates of the station on the map - to be implemented

    /**
     * Constructor with parameters
     * @param name, name of the station
     * @param des, description of the station
     * @param r_id, id of the route this station belongs to
     */
    public Station(String name, String des, int r_id, String colour)
    {
        this.name = name;
        this.description = des;
        this.route_id = r_id;
        this.colour = colour;
    }

    public Station()
    {
    }

    /***************************************************************
     * Get Functions
     ***************************************************************/

    /**
     *
     * @return id of the station
     */
    public int getId()
    {
        return this.id;
    }

    /**
     *
     * @return the id of the route this station belongs to
     */
    public int getRoute_id()
    {
        return this.route_id;
    }

    /**
     *
     * @return name of the station
     */
    public String getName()
    {
        return this.name;
    }

    /**
     *
     * @return description of the station
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     *
     * @return colour of the station
     */
    public String getColour()
    {
        return this.colour;
    }

    /***************************************************************
     * Set Functions
     ***************************************************************/

    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setColour(String colour)
    {
        this.colour = colour;
    }

    public void setRoute_id(int route_id)
    {
        this.route_id = route_id;
    }
}
