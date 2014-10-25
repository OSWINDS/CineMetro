package cinemetroproject.cinemetro;

import com.google.android.gms.maps.model.LatLng;

public class Station {

    private int id;
    private String name;
    private String description;
    private int route_id;
    private LatLng point;

    static int pos;
    /**
     * Constructor with parameters
     * @param name, name of the station
     * @param des, description of the station
     * @param r_id, id of the route this station belongs to
     */
    public Station(String name, String des, int r_id, double lat, double lng)
    {
        this.name = name;
        this.description = des;
        this.route_id = r_id;
        this.point = new LatLng(lat, lng);


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
     * @return latitude of station
     */
    public double getLat()
    {
        return this.point.latitude;
    }

    /**
     *
     * @return longitude of station
     */
    public double getLng()
    {
        return this.point.longitude;
    }

    public LatLng getLatpoint() {
        return this.point;
    }

    public MyPoint getMyPoint(){
        return new MyPoint(this.getName(),this.getLatpoint(),this.getId());
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

    public void setRoute_id(int route_id)
    {
        this.route_id = route_id;
    }

    public void setPoint(double lat, double lng)
    {
        this.point = new LatLng(lat, lng);
    }



}
