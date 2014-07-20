import cinemetroproject.cinemetro.util.Colours;

public class Station {

    private int id;
    private String name;
    private String description;
    private int route_id;
    private Colours colour;
    //There is one more attribute,the coordinates of the station on the map - to be implemented

    /**
     * Constructor with parameters
     * @param id,id of the station
     * @param name, name of the station
     * @param des, description of the station
     * @param r_id, id of the route this station belongs to
     */
    public Station(int id, String name, String des, int r_id, Colours colour)
    {
        this.id = id;
        this.name = name;
        this.description = des;
        this.route_id = r_id;
        this.colour = colour;
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
    public Colours getColour()
    {
        return this.colour;
    }
}
