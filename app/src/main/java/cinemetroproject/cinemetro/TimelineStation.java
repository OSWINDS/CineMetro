package cinemetroproject.cinemetro;



import com.google.android.gms.maps.model.LatLng;

import java.sql.Time;
import java.util.ArrayList;

public class TimelineStation {

    private int id;
    private String name;
    /**
     * Milestones on that Timeline Station
     */
    private ArrayList<Milestone> milestones;

    public TimelineStation(String name)
    {
        this.name = name;
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

    /**
     * Set the milestones array of the TimelineStation
     * @param milestones
     */
    public void setMilestones(ArrayList<Milestone> milestones)
    {
        this.milestones = milestones;
    }

    public MyPoint getMyPoint() {

        return new MyPoint(this.getName(),this.getLng(),this.getId());
    }

    public LatLng getLng() {
        return new LatLng(0,0);
    }
}
