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

    public String getDistance2(){
        if(getDistance() != 0.0){
            if((getDistance()/1000) < 1){
                return "Απόσταση: " + (String.format("%.02f", getDistance())) + " m";
            }
            return "Απόσταση: " + (String.format("%.02f", getDistance()/1000)) + " km";
        }
        return "";
    }

    public int compareTo(MyPoint ob2) {
        if(this.getLng().latitude<=ob2.getLng().latitude)
            return 1;
        return -1;
    }
}
