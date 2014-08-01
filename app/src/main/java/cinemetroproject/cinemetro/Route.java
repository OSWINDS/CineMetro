package cinemetroproject.cinemetro;

public class Route {

    private int id;
    private String name;
    private String colour;
    private int state;

    /**
     * Constructor with parameters,id is auto incremented by db
     * @param name
     * @param colour
     * @param state
     */
    public Route(String name, String colour, int state)
    {
        this.name = name;
        this.colour = colour;
        this.state = state;
    }

    public Route()
    {
    }

    /***************************************************************
     * Get Functions
     ***************************************************************/

    /**
     *
     * @return id of the route
     */
    public int getId()
    {
        return  this.id;
    }

    /**
     *
     * @return name of the route
     */
    public String getName()
    {
        return this.name;
    }

    /**
     *
     * @return colour of the route
     */
    public String getColour()
    {
        return this.colour;
    }

    /**
     *
     * @return state of the route,1 is open - 0 is closed
     */
    public int getState()
    {
        return this.state;
    }

    /***************************************************************
     * Set Functions
     ***************************************************************/

    /**
     * Set id of the route
     * @param id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Set name of the route
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Set colour of the route
     * @param colour
     */
    public void setColour(String colour)
    {
        this.colour = colour;
    }

    /**
     * Set state of the route,1 is open - 0 is closed
     * @param state
     */
    public void setState(int state)
    {
        this.state = state;
    }
}
