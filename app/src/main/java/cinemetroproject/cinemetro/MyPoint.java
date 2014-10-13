package cinemetroproject.cinemetro;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Chris on 10/13/2014.
 */
public class MyPoint {
    private String Name;
    private LatLng lng;
    private int id;
    private float distance;

    public MyPoint(){};
    public MyPoint(String Name,LatLng lng,int id){
        this.setLng(lng);
        this.setName(Name);
        this.setId(id);
    }

    public LatLng getLng() {
        return lng;
    }

    public void setLng(LatLng lng) {
        this.lng = lng;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
