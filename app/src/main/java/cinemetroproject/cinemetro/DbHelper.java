package cinemetroproject.cinemetro;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.sql.SQLDataException;
import java.util.ArrayList;

/**
 * 
 * Extends SQLiteOpenHelper and is responsible for creating the tables of the db,
 * updating the db and deleting the db.
 * @author efi
 *
 */
public class DbHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 19;
    // Database Name
    private static final String DATABASE_NAME = "CineMetroDB";

    private boolean updated = false;

    /**
     *
     * @return true if the db is updated
     */
    public boolean isUpdated()
    {
        return this.updated;
    }

    public void setUpdated(boolean u)
    {
        this.updated = u;
    }

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * creates the tables of the db if they dont already exist
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;


        // SQL statement to create table route
        query = "CREATE TABLE IF NOT EXISTS route ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "  +
                "colour TEXT,"+
                "state INTEGER)";

        // create route table
        db.execSQL(query);

        // SQL statement to create table station
        query = "CREATE TABLE IF NOT EXISTS station ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "description TEXT, " +
                "route_id INTEGER," +
                "lat REAL," +
                "lng REAL)";

        // create station table
        db.execSQL(query);

        // SQL statement to create table photo
        query = "CREATE TABLE IF NOT EXISTS photo ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "station_id INTEGER," +
                "movie_id INTEGER," +
                "description TEXT)";

        // create photo table
        db.execSQL(query);

        // SQL statement to create table movie
        query = "CREATE TABLE IF NOT EXISTS movie ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "station_id INTEGER," +
                "title TEXT, " +
                "description TEXT," +
                "actors TEXT," +
                "director TEXT," +
                "year TEXT)";

        // create movie table
        db.execSQL(query);

        // SQL statement to create table user
        query = "CREATE TABLE IF NOT EXISTS user ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, "  +
                "password TEXT)";

        // create user table
        db.execSQL(query);

        // SQL statement to create table timelinestation
        query = "CREATE TABLE IF NOT EXISTS timelinestation ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "lat REAL," +
                "lng REAL)";

        // create timelinestation table
        db.execSQL(query);

        // SQL statement to create table milestone
        query = "CREATE TABLE IF NOT EXISTS milestone ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "station_id INTEGER, " +
                "year INTEGER, " +
                "des TEXT, " +
                "photo_name TEXT, "  +
                "photo_des TEXT)";

        // create milestone table
        db.execSQL(query);

        // SQL statement to create table rating
        query = "CREATE TABLE IF NOT EXISTS rating ( " +
                "station_id INTEGER, " +
                "sum REAL, " +
                "counter INTEGER)";

        // create milestone table
        db.execSQL(query);

        // SQL statement to create table user_rating
        query = "CREATE TABLE IF NOT EXISTS user_rating ( " +
                "station_id INTEGER, " +
                "user_id INTEGER, " +
                "rating REAL)";

        // create milestone table
        db.execSQL(query);




        this.updated = true;
    }

    /**
     * updates the db
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older route table if exists
        db.execSQL("DROP TABLE IF EXISTS route");

        // Drop older station table if exists
        db.execSQL("DROP TABLE IF EXISTS station");

        // Drop older photo table if exists
        db.execSQL("DROP TABLE IF EXISTS photo");

        // Drop older movie table if exists
        db.execSQL("DROP TABLE IF EXISTS movie");

        // Drop older timelinestation table if exists
        db.execSQL("DROP TABLE IF EXISTS timelinestation");

        // Drop older movie milestone if exists
        db.execSQL("DROP TABLE IF EXISTS milestone");

        // create fresh tables
        this.onCreate(db);
    }

    /**
     * adds a new entry station to the table station
     * @param station
     */
    public void addStation(Station station)
    {
        SQLiteDatabase db = null;
        // get reference to writable DB
    	try{

            db = this.getWritableDatabase();

            //ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("name", station.getName()); // get name
            values.put("description", station.getDescription()); // get description
            values.put("route_id", station.getRoute_id());
            values.put("lat", station.getLat());
            values.put("lng", station.getLng());

            //insert
            db.insert("station", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values

    	}
        finally
        {
            //close
            if (null != db)
            db.close();
        }
    }

    /**
     * adds a new entry to the table route
     * @param route
     */
    public void addRoute(Route route)
    {
        SQLiteDatabase db = null;
        // get reference to writable DB
        try{
            db = this.getWritableDatabase();
            // get reference to writable DB
            db = this.getWritableDatabase();

            //ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("name", route.getName()); // get name
            values.put("colour", route.getColour());
            values.put("state", route.getState());

            //insert
            db.insert("route", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values

    	}
        finally
        {
            //close
            if (null != db)
                db.close();
        }
    	
    }

    /**
     * adds a new entry to the table photo
     * @param photo
     */
    public void addPhoto(Photo photo)
    {
        SQLiteDatabase db = null;
        // get reference to writable DB
        try{
            db = this.getWritableDatabase();
            //ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("name", photo.getName()); // get name
            values.put("station_id", photo.getStation_id());
            values.put("movie_id", photo.getMovie_id());
            values.put("description", photo.getDescription());

            //insert
            db.insert("photo", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
    	}
        finally
        {
            //close
            if (null != db)
                db.close();
        }
    }

    /**
     * adds a new entry to the table movie
     * @param movie
     */
    public void addMovie(Movie movie)
    {
        SQLiteDatabase db = null;
        // get reference to writable DB
        try{
            db = this.getWritableDatabase();
            //ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("station_id", movie.getStation_id());
            values.put("title", movie.getTitle()); // get name
            values.put("description", movie.getDescription());
            values.put("actors", movie.getActorstoString());
            values.put("director", movie.getDirector());
            values.put("year", movie.getYear());

            //insert
            db.insert("movie", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
    	}
        finally
        {
            //close
            if (null != db)
                db.close();
        }
    }

    /**
     * adds a new entry to the table user
     * @param user
     */
    public void addUser(User user, String password)
    {
        SQLiteDatabase db = null;
        // get reference to writable DB
        try{
            // get reference to writable DB
            db = this.getWritableDatabase();

            //ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("username", user.getUsername()); // get name
            values.put("password", password);

            //insert
            db.insert("user", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
    	}
        finally
        {
            //close
            if (null != db)
                db.close();
        }
    }

    /**
     * adds a new entry to the table timelinestation
     * @param station
     */
    public void addTimelineStation(TimelineStation station)
    {
        SQLiteDatabase db = null;
        // get reference to writable DB
        try{
            // get reference to writable DB
            db = this.getWritableDatabase();

            //ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("name", station.getName());
            values.put("lat", station.getLat());
            values.put("lng", station.getLng());

            //insert
            db.insert("timelinestation", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
        }
        finally
        {
            //close
            if (null != db)
                db.close();
        }
    }

    /**
     * adds a new entry to the table milestone
     * @param milestone
     */
    public void addMilestone(Milestone milestone)
    {
        SQLiteDatabase db = null;
        // get reference to writable DB
        try{
            // get reference to writable DB
            db = this.getWritableDatabase();

            //ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("station_id", milestone.getStation_id());
	        values.put("year", milestone.getYear());
            values.put("des", milestone.getDescription());
            values.put("photo_name", milestone.getPhotoName());
            values.put("photo_des", milestone.getPhotoDescription());

            //insert
            db.insert("milestone", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
    	}
        finally
        {
            //close
            if (null != db)
                db.close();
        }
    }

    /**
     * adds a new entry to the table rating
     */
    public void addRating(int station_id)
    {
        SQLiteDatabase db = null;
        // get reference to writable DB
        try{
            // get reference to writable DB
            db = this.getWritableDatabase();

            //ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("station_id", station_id);
	        values.put("sum", 0);
            values.put("counter", 0);

            //insert
            db.insert("rating", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
    	}
        finally
        {
            //close
            if (null != db)
                db.close();
        }
    }

    /**
     * adds a new entry to the table user_rating
     */
    public void addUserRating(int station_id, int user_id,float rating)
    {
        SQLiteDatabase db = null;
        // get reference to writable DB
        try{
            // get reference to writable DB
            db = this.getWritableDatabase();

            //ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put("station_id", station_id);
	        values.put("user_id", user_id);
            values.put("rating", rating);

            //insert
            db.insert("user_rating", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
    	}
        finally
        {
            //close
            if (null != db)
                db.close();
        }
    }





    /**
     * returns the station with this id
     * @param id
     * @return
     */
    public Station getStation(int id)
    {
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"id","name","description","route_id"};
        //build query
        Cursor cursor =
                db.query("station", // a. table
                        columns, // column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        //if we got results get the first one
        if (null != cursor)
            cursor.moveToFirst();

        //build station object
        Station station = new Station();
        station.setId(Integer.parseInt(cursor.getString(0))); //id
        station.setName(cursor.getString(1)); //name
        station.setDescription(cursor.getString(2)); //description
        station.setRoute_id(Integer.parseInt(cursor.getString(3))); //route_id
        station.setPoint(Double.parseDouble(cursor.getString(4)), Double.parseDouble(cursor.getString(5))); //point

        //return station
        return station;
    }

    /**
     * returns the route with this id
     * @param id
     * @return
     */
    public Route getRoute(int id)
    {
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"id","name","colour","state"};
        //build query
        Cursor cursor =
                db.query("route", // a. table
                        columns, // column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        //if we got results get the first one
        if (null != cursor)
            cursor.moveToFirst();

        //build route object
        Route route = new Route();
        route.setId(Integer.parseInt(cursor.getString(0))); //id
        route.setName(cursor.getString(1)); //name
        route.setColour(cursor.getString(2)); //colour
        route.setState(Integer.parseInt(cursor.getString(3))); //state

        //return route
        return route;
    }

    /**
     * Returns the photo with this id
     * @param id
     * @return
     */
    public Photo getPhoto(int id)
    {
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"id","name","station_id", "movie_id","description"};
        //build query
        Cursor cursor =
                db.query("photo", // a. table
                        columns, // column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        //if we got results get the first one
        if (null != cursor)
            cursor.moveToFirst();

        //build route object
        Photo photo = new Photo();
        photo.setId(Integer.parseInt(cursor.getString(0))); //id
        photo.setName(cursor.getString(1)); //name
        photo.setStation_id(Integer.parseInt(cursor.getString(2))); //station id
        photo.setMovie_id(Integer.parseInt(cursor.getString(3)));
        photo.setDescription(cursor.getString(4)); //description

        //return photo
        return photo;
    }

    /**
     * Returns the movie with this id
     * @param id
     * @return
     */
    public Movie getMovie(int id)
    {
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"id", "station_id", "title", "description", "actors", "director", "year"};
        //build query
        Cursor cursor =
                db.query("movie", // a. table
                        columns, // column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        //if we got results get the first one
        if (null != cursor)
            cursor.moveToFirst();

        //build movie object
        Movie movie = new Movie();
        movie.setId(Integer.parseInt(cursor.getString(0))); //id
        movie.setStation_id(Integer.parseInt(cursor.getString(1))); //station id
        movie.setTitle(cursor.getString(2));
        movie.setDescription(cursor.getString(3)); //description
        movie.setActors(cursor.getString(4));
        movie.setDirector(cursor.getString(5));
        movie.setYear(cursor.getString(6));

        //return movie
        return movie;
    }

    /**
     * returns the user with this id
     * @param id
     * @return
     */
    public User getUser(int id)
    {
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"id","username","password"};
        //build query
        Cursor cursor =
                db.query("user", // a. table
                        columns, // column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        //if we got results get the first one
        if (null != cursor)
            cursor.moveToFirst();

        //build user object
        String name;
        String pass;
        id   = Integer.parseInt(cursor.getString(0)); //id
        name = cursor.getString(1); //name
        pass = cursor.getString(2); //password
        User user = new User(id, name, pass);

        //return user
        return user;
    }

    /**
     * Returns the milestone with this id,without the milestones arraylist
     * @param id
     * @return
     */
    public TimelineStation getTimelineStation(int id)
    {
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"id", "name"};
        //build query
        Cursor cursor =
                db.query("timelinestation", // a. table
                        columns, // column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        //if we got results get the first one
        if (null != cursor)
            cursor.moveToFirst();

        //build route object
        TimelineStation station = new TimelineStation();
        station.setId(Integer.parseInt(cursor.getString(0))); //id
        station.setName(cursor.getString(1)); //name

        //return TimelineStation
        return station;
    }

    /**
     * Returns the milestone with this id
     * @param id
     * @return
     */
    public Milestone getMilestone(int id)
    {
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"id", "des", "year", "station_id", "photo_name", "photo_des"};
        //build query
        Cursor cursor =
                db.query("milestone", // a. table
                        columns, // column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        //if we got results get the first one
        if (null != cursor)
            cursor.moveToFirst();

        //build route object
        Milestone milestone = new Milestone();
        milestone.setId(Integer.parseInt(cursor.getString(0))); //id
        milestone.setStation_id(Integer.parseInt(cursor.getString(1))); //station id
	    milestone.setYear(cursor.getString(2));
        milestone.setDes(cursor.getString(3)); //des
        milestone.setPhotoName(cursor.getString(4)); //photo name
        milestone.setPhotoDescription(cursor.getString(5)); //photo description

        //return milestone
        return milestone;
    }

    /**
     * returns the rating for the station with this id
     * @param station_id
     * @return
     */
    public float getRating(int station_id)
    {
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"station_id","sum","counter"};
        //build query
        Cursor cursor =
                db.query("rating", // a. table
                        columns, // column names
                        " station_id = ?", // c. selections
                        new String[] { String.valueOf(station_id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        //if we got results get the first one
        if (null != cursor && cursor.moveToFirst()) {

            float rating = cursor.getFloat(1);
            int counter = cursor.getInt(2);
            if (counter > 0) {
                rating = rating / counter;
            }

            //return rating
            return rating;
        }
        else {
            return -1;
        }
    }

    /**
     * returns the rating from this user for the station with this id
     * @param station_id
     * @return
     */
    public float getUserRating(int station_id, int user_id)
    {
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"station_id","user_id","rating"};
        //build query
        Cursor cursor =
                db.query("user_rating", // a. table
                        columns, // column names
                        " station_id = ? and user_id = ?", // c. selections
                        new String[] { String.valueOf(station_id), String.valueOf(user_id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit


        //if we got results get the first one
        if (null != cursor && cursor.moveToFirst()) {

            float rating = cursor.getFloat(2);
            return rating;
        }
        else {
            return -1;
        }
    }



    /**
     * returns the sum for the station with this id
     * @param station_id
     * @return
     */
    public float getSum(int station_id)
    {
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"station_id","sum","counter"};
        //build query
        Cursor cursor =
                db.query("rating", // a. table
                        columns, // column names
                        " station_id = ?", // c. selections
                        new String[] { String.valueOf(station_id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        //if we got results get the first one
        if (null != cursor && cursor.moveToFirst()) {

            return cursor.getFloat(1);
        }
        else {
            return -1;
        }
    }

    /**
     * returns the counter for the station with this id
     * @param station_id
     * @return
     */
    public int getCounter(int station_id)
    {
        //reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"station_id","sum","counter"};
        //build query
        Cursor cursor =
                db.query("rating", // a. table
                        columns, // column names
                        " station_id = ?", // c. selections
                        new String[] { String.valueOf(station_id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        //if we got results get the first one
        if (null != cursor && cursor.moveToFirst()) {

            return cursor.getInt(2);
        }
        else {
            return -1;
        }
    }


    /**
     *
     * @return all stations in db
     */
    public ArrayList<Station> getAllStations()
    {
        ArrayList<Station> stations = new ArrayList<Station>();

        //build the query
        String query = "SELECT  * FROM station";

        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //go over each row, build station and add it to list
        Station station = null;
        if (cursor.moveToFirst()) {
            do {
                //build station object
                station = new Station();
                station.setId(Integer.parseInt(cursor.getString(0))); //id
                station.setName(cursor.getString(1)); //name
                station.setDescription(cursor.getString(2)); //description
                station.setRoute_id(Integer.parseInt(cursor.getString(3))); //route_id
                station.setPoint(Double.parseDouble(cursor.getString(4)), Double.parseDouble(cursor.getString(5))); //point
                stations.add(station);
            } while (cursor.moveToNext());
        }

        // return stations
        return stations;
    }

    /**
     *
     * @return all routes in the db
     */
    public ArrayList<Route> getAllRoutes()
    {
        ArrayList<Route> routes = new ArrayList<Route>();

        //build the query
        String query = "SELECT  * FROM route";

        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //go over each row, build route and add it to list
        Route route;
        if (cursor.moveToFirst()) {
            do {
                //build route object
                route = new Route();
                route.setId(Integer.parseInt(cursor.getString(0))); //id
                route.setName(cursor.getString(1)); //name
                route.setColour(cursor.getString(2)); //colour
                route.setState(Integer.parseInt(cursor.getString(3))); //state

                routes.add(route);
            } while (cursor.moveToNext());
        }

        // return routes
        return routes;
    }

    /**
     *
     * @return all the photos in the db
     */
    public ArrayList<Photo> getAllPhotos()
    {
        ArrayList<Photo> photos = new ArrayList<Photo>();

        //build the query
        String query = "SELECT  * FROM photo";

        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //go over each row, build station and add it to list
        Photo photo = null;
        if (cursor.moveToFirst()) {
            do {
                //build photo object
                photo = new Photo();
                photo.setId(Integer.parseInt(cursor.getString(0))); //id
                photo.setName(cursor.getString(1)); //name
                photo.setStation_id(Integer.parseInt(cursor.getString(2))); //station id
                photo.setMovie_id(Integer.parseInt(cursor.getString(3)));
                photo.setDescription(cursor.getString(4)); //description
                photos.add(photo);
            } while (cursor.moveToNext());
        }

        // return photos
        return photos;
    }

    /**
     *
     * @return all the movies in the db
     */
    public ArrayList<Movie> getAllMovies()
    {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        //build the query
        String query = "SELECT  * FROM movie";

        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //go over each row, build station and add it to list
        Movie movie = null;
        if (cursor.moveToFirst()) {
            do {
                //build movie object
                movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(0))); //id
                movie.setStation_id(Integer.parseInt(cursor.getString(1))); //station id
                movie.setTitle(cursor.getString(2));
                movie.setDescription(cursor.getString(3)); //description
                movie.setActors(cursor.getString(4));
                movie.setDirector(cursor.getString(5));
                movie.setYear(cursor.getString(6));

                movies.add(movie);
            } while (cursor.moveToNext());
        }

        // return movies
        return movies;
    }

    /**
     *
     * @return all users in db
     */
    public ArrayList<User> getAllUsers()
    {
        ArrayList<User> users = new ArrayList<User>();

        //build the query
        String query = "SELECT  * FROM user";

        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String name,pass;
            int id;
            do {
                //build user object
                id = Integer.parseInt(cursor.getString(0)); //id
                name = cursor.getString(1); //name
                pass = cursor.getString(2); //password
                User user = new User(id, name, pass);

                //add user
                users.add(user);
            } while (cursor.moveToNext());
        }

        // return users
        return users;
    }

    /**
     *
     * @return all the timelineStations in the db
     */
    public ArrayList<TimelineStation> getAllTimelineStations()
    {
        ArrayList<TimelineStation> stations = new ArrayList<TimelineStation>();

        //build the query
        String query = "SELECT  * FROM timelinestation";

        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //go over each row, build timelinestation and add it to list
        TimelineStation station;
        if (cursor.moveToFirst()) {
            do {
                //build station object
                station = new TimelineStation();
                station.setId(Integer.parseInt(cursor.getString(0))); //id
                station.setName(cursor.getString(1)); //name
                station.setPoint(Double.parseDouble(cursor.getString(2)), Double.parseDouble(cursor.getString(3))); //point
                stations.add(station);
            } while (cursor.moveToNext());
        }

        // return stations
        return stations;
    }


    /**
     *
     * @return all the milestones in the db
     */
    public ArrayList<Milestone> getAllMilestones()
    {
        ArrayList<Milestone> milestones = new ArrayList<Milestone>();

        //build the query
        String query = "SELECT  * FROM milestone";

        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //go over each row, build milestone and add it to list
        Milestone milestone;
        if (cursor.moveToFirst()) {
            do {
                //build milestone object
                milestone = new Milestone();
                milestone.setId(Integer.parseInt(cursor.getString(0))); //id
                milestone.setStation_id(Integer.parseInt(cursor.getString(1))); //station id
		        milestone.setYear(cursor.getString(2));
                milestone.setDes(cursor.getString(3));
                milestone.setPhotoName(cursor.getString(4)); //photo name
                milestone.setPhotoDescription(cursor.getString(5));
                milestones.add(milestone);
            } while (cursor.moveToNext());
        }

        // return milestones
        return milestones;
    }


    public void deleteStation(int id)
    {
        SQLiteDatabase db = null;
    	try{
            //get reference to writable DB
            db = this.getWritableDatabase();

            //delete
            db.delete("station", //table name
                    "id = ?",  // selections
                    new String[] { String.valueOf(id) }); //selections args

    	}
        finally
        {
            //close
            if (null != db)
            db.close();
        }
    }

    /**
     * Update the rating of the station with this id
     * @param station_id
     * @param sum
     * @param counter
     */
    public void updateRatings(int station_id, float sum, int counter)
    {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("station_id",station_id);
        cv.put("sum",sum);
        cv.put("counter",counter);
        db.update("rating", cv, "station_id "+"="+station_id, null);
    }
}
