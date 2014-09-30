package cinemetroproject.cinemetro;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by hara on 30/9/2014.
 */
public class Point {
    private LatLng position;
    private String address;

    public Point(LatLng pos, String adr){
        this.position=pos;
        this.address=adr;
    }

    public LatLng getPosition(){
        return position;
    }

    public String getAddress(){
        return address;
    }
}
