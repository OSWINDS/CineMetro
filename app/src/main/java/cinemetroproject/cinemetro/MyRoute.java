package cinemetroproject.cinemetro;

import java.util.ArrayList;

/**
 * Created by Chris on 12/19/2014.
 */
public class MyRoute {
    private int id;
    private int name;
    private int sortName;
    private int color;
    private Class routeClass;


    public MyRoute() {
    }

    public MyRoute(int id, int name, int SortName, int color, Class c) {
        this.setColor(color);
        this.setId(id);
        this.setName(name);
        this.setSortName(SortName);
        this.setRouteClass(c);


    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static ArrayList<MyRoute> getAllRoutes() {
        ArrayList<MyRoute> rts = new ArrayList();
        rts.add(new MyRoute(MapActivity.LINE1, R.string.line1_title, R.string.tab1, R.color.line1, ViewCinema.class));
        rts.add(new MyRoute(MapActivity.LINE2, R.string.line2_title, R.string.tab2, R.color.line2, ViewStation.class));
        rts.add(new MyRoute(MapActivity.LINE3, R.string.line3_title, R.string.tab3, R.color.line3, Timeline.class));
        return rts;
    }

    public int getSortName() {
        return sortName;
    }

    public void setSortName(int sortName) {
        this.sortName = sortName;
    }

    public void setRouteClass(Class routeClass) {
        this.routeClass = routeClass;
    }

    public Class getRouteClass() {
        return routeClass;
    }
}
