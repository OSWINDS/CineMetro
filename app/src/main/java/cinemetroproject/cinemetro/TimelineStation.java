package cinemetroproject.cinemetro;



import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.ArrayList;

public class TimelineStation {

    private int id;
    private String name;
    private LatLng point;
    /**
     * Milestones on that Timeline Station
     */
    private ArrayList<Milestone> milestones;

    public TimelineStation(String name, double lat, double lng)
    {

        this.name = name;
        this.point = new LatLng(lat, lng);
    }

    public TimelineStation() {}

    /***************************************************************
     * Get Functions
     ***************************************************************/

    /**
     *
     * @return the id of the TimelineStation
     */
    public int getId()
    {
        return this.id;
    }

    /**
     *
     * @return the name of the TimelineStation
     */
    public String getName()
    {
        return this.name;
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

    public MyPoint getMyPoint() {

        return new MyPoint(this.getName(),this.getLatpoint(),this.getId());
    }

    /**
     *
     * @return the milestones of the TimelineStation
     */
    public ArrayList<Milestone> getMilestones()
    {
        return this.milestones;
    }


    /***************************************************************
     * Set Functions
     ***************************************************************/

    /**
     * Set name of TimelineStation
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Set id of the TimelineStation
     * @param id
     */
    public void setId(int id)
    {
        this.id = id;
    }


    public void setPoint(double lat, double lng)
    {
        this.point = new LatLng(lat, lng);
    }

    /**
     * Set the milestones array of the TimelineStation
     * @param milestones
     */
    public void setMilestones(ArrayList<Milestone> milestones)
    {
        this.milestones = milestones;
    }

}
