package cinemetroproject.cinemetro;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Station {

    private int id;
    private String name;
    private String description;
    private int route_id;
    private String colour;
   ArrayList<LatLng> points;

    static int pos;
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
        pos=0;
        points=new ArrayList<LatLng>();
        points.add(new LatLng(40.640799, 22.934955));
        points.add( new LatLng(40.633951, 22.936940));
        points.add( new LatLng(40.633087, 22.938378));
        points.add( new LatLng(40.631995, 22.940567));
        points.add(new LatLng(40.633533, 22.946178));
        points.add(new LatLng(40.626547, 22.948591));

    }

    public Station()
    {
        points=new ArrayList<LatLng>();
        points.add(new LatLng(40.640799, 22.934955));
        points.add( new LatLng(40.633951, 22.936940));
        points.add( new LatLng(40.633087, 22.938378));
        points.add( new LatLng(40.631995, 22.940567));
        points.add(new LatLng(40.633533, 22.946178));
        points.add(new LatLng(40.626547, 22.948591));
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

    public LatLng getLatpoint() {
        if(pos>= points.size())pos=0;
        return points.get(pos++);
    }
}
