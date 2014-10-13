package cinemetroproject.cinemetro;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Chris on 10/13/2014.
 */
public class MyPoint {
    private String Name;
    private LatLng lng;

   public MyPoint(){};
    public MyPoint(String Name,LatLng lng){
        this.setLng(lng);
        this.setName(Name);
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
}
