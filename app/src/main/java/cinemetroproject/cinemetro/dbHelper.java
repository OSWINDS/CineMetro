package cinemetroproject.cinemetro;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Extends SQLiteOpenHelper and is responsible for creating the tables of the db,
 * updating the db and deleting the db.
 */
public class dbHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "CineMetroDB";

    private boolean updated = false;

    /**
     *
     * @return true if the db is updated
     */
    public boolean isUpdated()
    {
        return updated;
    }

    public void setUpdated(boolean u)
    {
        this.updated = u;
    }

    public dbHelper(Context context) {
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

        // create station route
        db.execSQL(query);

        // SQL statement to create table station
        query = "CREATE TABLE IF NOT EXISTS station ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "description TEXT, " +
                "route_id INTEGER," +
                "colour TEXT )";

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


        // create fresh tables
        this.onCreate(db);
    }

    /**
     * adds a new entry station to the table station
     * @param station
     */
    public void addStation(Station station)
    {
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("name", station.getName()); // get name
        values.put("description", station.getDescription()); // get description
        values.put("route_id", station.getRoute_id());
        values.put("colour", station.getColour());

        //insert
        db.insert("station", // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        //close
        db.close();
    }

    /**
     * adds a new entry to the table route
     * @param route
     */
    public void addRoute(Route route)
    {
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("name", route.getName()); // get name
        values.put("colour", route.getColour());
        values.put("state", route.getState());

        //insert
        db.insert("route", // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        //close
        db.close();
    }

    /**
     * adds a new entry to the table photo
     * @param photo
     */
    public void addPhoto(Photo photo)
    {
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

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

        //close
        db.close();
    }

    /**
     * adds a new entry to the table movie
     * @param movie
     */
    public void addMovie(Movie movie)
    {
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

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

        //close
        db.close();
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

        String[] columns = {"id","name","description","route_id","colour"};
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
        if (cursor != null)
            cursor.moveToFirst();

        //build station object
        Station station = new Station();
        station.setId(Integer.parseInt(cursor.getString(0))); //id
        station.setName(cursor.getString(1)); //name
        station.setDescription(cursor.getString(2)); //description
        station.setRoute_id(Integer.parseInt(cursor.getString(3))); //route_id
        station.setColour(cursor.getString(4)); //colour

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
        if (cursor != null)
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
        if (cursor != null)
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
        if (cursor != null)
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
                station.setColour(cursor.getString(4)); //colour

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
        Route route = null;
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



    public void deleteStation(int id)
    {
        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //delete
        db.delete("station", //table name
                "id = ?",  // selections
                new String[] { String.valueOf(id) }); //selections args

        //close
        db.close();
    }
}
