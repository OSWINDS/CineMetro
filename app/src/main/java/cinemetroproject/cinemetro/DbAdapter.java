package cinemetroproject.cinemetro;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseQuery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles the communication of other classes with the database
 * @author efi
 *
 */
final class DbAdapter {
    private static DbAdapter Instance = new DbAdapter();

    private DbAdapter() {
    }

    /**
     *
     * @return instance of this class
     */
    public static DbAdapter getInstance() {
        return Instance;
    }

    /**
     * array of stations
     */
    private ArrayList<Station> stations = new ArrayList<Station>();

    /**
     * array of routes
     */
    private ArrayList<Route> routes = new ArrayList<Route>();

    /**
     * array of photos
     */
    private ArrayList<Photo> photos = new ArrayList<Photo>();

    /**
     * array of movies
     */
    private ArrayList<Movie> movies = new ArrayList<Movie>();

    /**
     * array of users
     */
    private ArrayList<User> users = new ArrayList<User>();

    /**
     * array of timeline stations
     */
    private ArrayList<TimelineStation> timelineStations = new ArrayList<TimelineStation>();

    /**
     * array of milestones
     */
    private ArrayList<Milestone> milestones = new ArrayList<Milestone>();

    /**
     * dbHelper object to interact with the db
     */
    private DbHelper db;

    /**
     * The active user after a successful login
     */
    private User activeUser;



    /**
     * Fills the arrays with data from the DB
     */
    private void fillArrays()
    {
        stations = db.getAllStations();
        routes = db.getAllRoutes();
        photos = db.getAllPhotos();
        movies = db.getAllMovies();
        users = db.getAllUsers();
        timelineStations = db.getAllTimelineStations();
        milestones = db.getAllMilestones();
        this.setMilestonesToStations();
    }

    /**
     *
     * @return all the stations in the DB
     */
    public ArrayList<Station> getStations()
    {
        return this.stations;
    }

    /**
     *
     * @return all the routes in the DB
     */
    public ArrayList<Route> getRoutes() {return routes; }

    /**
     *
     * @return all the photos in the DB
     */
    public ArrayList<Photo> getPhotos() {return photos; }

    /**
     *
     * @return all the users in the DB
     */
    public ArrayList<User> getUsers() {return users; }

    /**
     *
     * @return all the movies in the DB
     */
    public ArrayList<Movie> getMovies() {return movies;}

    /**
     *
     * @return all the timeline stations
     */
    public ArrayList<TimelineStation> getTimelineStations() {return this.timelineStations;}

    /**
     * fills the db if data if the tables are empty and then fill the arrays with the data from the db
     * @param db
     */
    public void setDB(DbHelper db)
    {
        this.db = db;
        if(!stations.isEmpty())
        {
            this.stations.clear();
            this.routes.clear();
            this.milestones.clear();
            this.timelineStations.clear();
            this.movies.clear();
            this.photos.clear();
        }
        this.fillArrays();
        if ( stations.isEmpty())
        {

            Language language = db.getLanguage();
            switch (language)
            {
                case GREEK:
                    this.populateDB();
                    break;
                case ENGLISH:
                    this.populateEnglishDB();
                    break;
                default:
                    this.populateDB();
            }
        }
        this.fillArrays();
    }

    public Language getLanguage()
    {
        return db.getLanguage();
    }

    /**
     *
     * @param route_id
     * @return the stations that belong to the route with this id
     */
    public ArrayList<Station> getStationByRoute(int route_id)
    {
        ArrayList<Station> route_stations = new ArrayList<Station>();
        for(Station station : stations)
        {
            if (station.getRoute_id() == route_id)
            {
                route_stations.add(station);
            }
        }
        return route_stations;
    }

    /**
     *
     * @param station_id
     * @return the photos that belong to the station with this id
     */
    public ArrayList<Photo> getPhotosByStation(int station_id)
    {
        ArrayList<Photo> station_photos = new ArrayList<Photo>();
        for(Photo photo : photos)
        {
            if (photo.getStation_id() == station_id)
            {
                station_photos.add(photo);
            }
        }
        return station_photos;
    }

    /**
     *
     * @param station_id
     * @return the movie that belongs to the station with this id
     */
    public Movie getMovieByStation(int station_id)
    {
        for(Movie movie : movies)
        {
            if (movie.getStation_id() == station_id)
            {
                return movie;
            }
        }
        return null;
    }

    /**
     *
     * @param movie_id
     * @return the main photos of the movie with this id
     */
    public ArrayList<Photo> getMainPhotosOfMovie(int movie_id)
    {
        ArrayList<Photo> photos = new ArrayList<Photo>();
        for (Photo p : this.photos)
        {
            if (p.getMovie_id() == movie_id && p.getStation_id() != -1 )
            {
                photos.add(p);
            }
        }
        return photos;
    }

    /**
     *
     * @param movie_id
     * @return the photos of actors of the movie with this id
     */
    public ArrayList<Photo> getActorPhotosOfMovie(int movie_id)
    {
        ArrayList<Photo> photos = new ArrayList<Photo>();
        for (Photo p : this.photos)
        {
            if (p.getMovie_id() == movie_id && p.getStation_id() == -1 )
            {
                photos.add(p);
            }
        }
        return photos;
    }

    /**
     * Add new user to db
     * @param user
     */
    public void addNewUser(User user)
    {
        if (user == null)
        {
            System.out.println("null user");
        }
        else {
            db.addUser(user, user.getPassword());
            this.users.clear();
            this.users = db.getAllUsers();
        }
    }

    /**
     *
     * @param username
     * @return the user with that username, null if username does not exists
     */
    public User getUserByUsername(String username)
    {
        for (User u : users)
        {
            if (u.getUsername().equals((username))) {
                return u;
            }
        }
        return null;
    }

    /**
     * Sets the active user to the parameter user
     * @param user
     */
    public void setActiveUser(User user)
    {
        this.activeUser = user;
    }

    /**
     * Returns the active user,could be null if there was no login
     * @return
     */
    public User getActiveUser()
    {
        return this.activeUser;
    }

    /**
     *
     * @param name
     * @return the id required to display the photo with that name
     */
    public int getPhotoDrawableID(String name)
    {
        try {
            Class res = R.drawable.class;
            Field field = res.getField(name);
            return field.getInt(null);
        } catch (Exception e) {
            return Integer.parseInt(null);
        }
    }

    public ArrayList<String> getGreenLinePhotos()
    {
        ArrayList<String> green_photos = new ArrayList<String>();
        for(Photo photo : this.photos)
        {
            if ( 0 == photo.getMovie_id() && 0 == photo.getStation_id())
            {
            	green_photos.add(photo.getName());
            }
        }
        return green_photos;
    }

    /**
     *
     * @param id
     * @return TimelineStation with this id,returns null id no station with such id is found
     */
    public TimelineStation getTimelineStationByID(int id)
    {
        for (TimelineStation station : this.timelineStations)
        {
            if (id == station.getId())
            {
                return station;
            }
        }
        return null;
    }

    /**
     *
     * @param station_id
     * @return the rating of the station with this id
     */
    public float getStationRating(int station_id)
    {
        float rating = db.getRating(station_id);
        if (rating < 0)
        {
            this.initializeRatings();
            return 0;
        }
        else
        {
            return rating;
        }
    }

    /**
     *
     * @param station_id
     * @param user_id
     * @return the rating of the station with this id from this user
     * returns 0 if the user has not voted for this station
     */
    public float getUserRatingForStation(int station_id, int user_id)
    {
        return db.getUserRating(station_id, user_id);
    }

    /**
     * Adds the param rating to the ratings of the station with this id
     * @param station_id
     */
    public void addRating(int station_id, float rating)
    {
        db.updateRatings(station_id, db.getSum(station_id) + rating, db.getCounter(station_id)+1);
    }

    /**
     * Adds the param rating to the ratings of the station with this id from this user
     * @param station_id
     */
    public void addUserRating(int station_id, int user_id, float rating)
    {
        db.addUserRating(station_id, user_id, rating);
    }

    /**
     *
     * @param timeline_station_id
     * @return arraylist with all the milestones that belong to the TimelineStation with this id
     */
    public ArrayList<Milestone> getTimelineStationMilestones(int timeline_station_id)
    {
        ArrayList<Milestone> mls = new ArrayList<Milestone>();
        for(Milestone milestone : this.milestones)
        {
            if(milestone.getStation_id() == timeline_station_id)
            {
                mls.add(milestone);
            }
        }
        return mls;
    }

    private void setMilestonesToStations()
    {
        for(int i =0; i<this.timelineStations.size(); i++)
        {
            this.timelineStations.get(i).setMilestones( getTimelineStationMilestones(this.timelineStations.get(i).getId()));
        }
    }

    /**
     * Change the language of the db to the one given in the parameter
     * @param lang
     */
    public void changeLanguage(Language lang)
    {
        db.setLanguage(lang);
        this.setDB(this.db);
    }

    /**
     * Sighs up a new user to parse
     * @param user
     */
    public void signUpUserToParse(User user)
    {
        final ArrayList<Float> blueLineStations = new ArrayList<Float>();
        final ArrayList<Float> greenLineStations = new ArrayList<Float>();
        final ArrayList<Float> redLineStations = new ArrayList<Float>();

        final float redLine;
        final float blueLine;
        final float greenLine;
        final float totalPoints;

        Float zero = new Float(0);

        //get his rating for each station for each line
        for(int i=0; i<getStationByRoute(this.routes.get(0).getId()).size(); i++)
        {
            //redLineStations.add(getUserRatingForStation(station_id, user.getId()));
            redLineStations.add(zero);
        }
        redLine = 0;
        for(int i=0; i<getStationByRoute(this.routes.get(1).getId()).size(); i++)
        {
            //blueLineStations.add(getUserRatingForStation(station_id, user.getId()));
            blueLineStations.add(zero);
        }
        blueLine = 0;
        for(int i=0; i<getStationByRoute(3).size(); i++)
        {
            greenLineStations.add(zero);
        }
        greenLine = 0;
        totalPoints = 0;

        final ParseUser parse_user = new ParseUser();

        final String username = user.getUsername();
        final String password = user.getPassword();

        //create a new user with this username
        parse_user.setPassword(password);
        parse_user.setUsername(username);
        parse_user.put("redLine", redLine);
        parse_user.put("blueLine", blueLine);
        parse_user.put("greenLine", greenLine);
        parse_user.put("totalPoints", totalPoints);
        parse_user.put("redLineStations", redLineStations);
        parse_user.put("blueLineStations", blueLineStations);
        parse_user.put("greenLineStations", greenLineStations);
        parse_user.signUpInBackground();

    }

    /**
     * Updates the user ratings to the parse online database
     */
    public void updateUserToParse(User user)
    {

        //initialize arrays for each line
        final ArrayList<Float> blueLineStations = new ArrayList<Float>();
        final ArrayList<Float> greenLineStations = new ArrayList<Float>();
        final ArrayList<Float> redLineStations = new ArrayList<Float>();

        final float redLine;
        final float blueLine;
        final float greenLine;
        final float totalPoints;

        float sum = 0;
        int station_id = 1;
        //get his rating for each station for each line
        for(int i=0; i<getStationByRoute(1).size(); i++, station_id++)
        {
            redLineStations.add(getUserRatingForStation(station_id, user.getId()));
            sum += redLineStations.get(i);
        }
        redLine = sum;
        sum = 0;
        for(int i=0; i<getStationByRoute(2).size(); i++, station_id++)
        {
            blueLineStations.add(getUserRatingForStation(station_id, user.getId()));
            sum += blueLineStations.get(i);
        }
        blueLine = sum;
        sum = 0;
        for(int i=0; i<getStationByRoute(3).size(); i++, station_id++)
        {
            greenLineStations.add(new Float(0));
            sum += greenLineStations.get(i);
        }
        greenLine = sum;
        totalPoints = redLine + blueLine + greenLine;

        final ParseUser parse_user = new ParseUser();

        final String username = user.getUsername();
        final String password = user.getPassword();

        parse_user.logInInBackground(username, password);
        parse_user.addUnique("redLine", redLine);
        parse_user.addUnique("blueLine", blueLine);
        parse_user.addUnique("greenLine", greenLine);
        parse_user.addUnique("totalPoints", totalPoints);
        parse_user.addAllUnique("redLineStations", redLineStations);
        parse_user.addAllUnique("blueLineStations", blueLineStations);
        parse_user.addAllUnique("greenLineStations", greenLineStations);
        parse_user.logInInBackground(username, password);
        parse_user.saveInBackground();
        parse_user.logOut();

    }

    /**
     * Get the user ratings from parse for a specific user and for each one add them to the db
     */
    private void getUserFromParse(final User user)
    {
            final int id = user.getId();

            //query parse to get the user
            ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
            query.whereEqualTo("username", user.getUsername());
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> userList, ParseException e) {
                    if (userList.size() > 0) {
                        String stations =  userList.get(0).getString("redLineStations");
                        addRatingsFromString(id, stations, 0);

                        stations =  userList.get(0).getString("blueLineStations");
                        addRatingsFromString(id, stations, getStationByRoute(0).size());

                        stations =  userList.get(0).getString("greenLineStations");
                        addRatingsFromString(id, stations, getStationByRoute(0).size() + getStationByRoute(1).size());
                    } else {
                        updateUserToParse(user);
                    }
                }
            });

    }



    /**
     * adds to the db the ratings of this user for the stations from string
     * @param user_id
     * @param stations
     * @param previous_stations, the stations before this line, needed for the station_id param
     */
    private void addRatingsFromString(int user_id, String stations, int previous_stations) {
        System.out.println(stations);
        stations = stations.replace("[", "");
        stations = stations.replace("]", "");
        String[] numbers = stations.split(",");
        int i = 1;
        for (String number : numbers) {
            float rating = Float.parseFloat(number);
            if (rating != 0) {
                int station_id = previous_stations + i;
                if(getUserRatingForStation(station_id, user_id) == 0) {
                    this.addUserRating(station_id, user_id, rating);
                }
            }
            i++;
        }
    }

    /**
     * Returns the name of the photo from the first milestone of the timeline station with this id
     * @param id
     * @return
     */
    public String getTimelineStationBackground(int id)
    {
        for(TimelineStation station : this.timelineStations)
        {
            if (station.getId() == id)
            {
                return station.getMilestones().get(0).getPhotoName();
            }
        }
        return null;
    }

    /**
     * Inserts the data into the tables
     */
    private void populateDB()
    {

        //add routes
        this.db.addRoute(new Route("Τα σινεμά της πόλης","red",1));
        this.db.addRoute(new Route("Θεσσαλονίκη μέσα απο τον ελληνικό κινηματογράφο","blue",1));
        //this.db.addRoute(new Route("Φεστιβάλ Κινηματογράφου Θεσσαλονίκης","green",1));


        //add stations
        //Line 1
        this.db.addStation(new Station("Βαρδάρη",
                "Η 1η κινηματογραφική προβολή στην Θεσσαλονίκη έγινε εδώ το 1897, στο καφέ «Η Τουρκία». \n" +
                        "\n" +
                "Στην ευρύτερη περιοχή της πλατείας χτίστηκαν αρκετά σινεμά όπως: Σπλέντιντ (μετέπειτα Ίλιον), Πάνθεον, " +
                        "Αττικόν και Ολύμπιον (στη σημερινή οδό Μοναστηρίου), καθώς αργότερα και τα πορνό σινεμά της πλατείας.",
                1, 40.641142,  22.934721));
        this.db.addStation(new Station("Παραλία 1 (Λιμάνι)",
                "Μια από τις πιο κινηματογραφικές και κινηματογραφημένες γωνιές του κέντρου. \n" +
                        "\n" +
                        "Στο λιμάνι λειτουργεί σήμερα το Μουσείο Κινηματογράφου και η Ταινιοθήκη Θεσσαλονίκης, όπως και οι τέσσερις" +
                        " αίθουσες προβολών του φεστιβάλ.\n" +
                        "\n" +
                        "Ταινίες που γυρίστηκαν εδώ (ενδεικτική αναφορά): Το Λιβάδι που δακρύζει, Ταξίδι στα Κύθηρα, Τοπίο στην Ομίχλη," +
                        " Ο Κλοιός.",
                1, 40.635650,  22.935431));
        this.db.addStation(new Station("Παραλία 2 (Λεωφόρος Νίκης)",
                "Στην Λεωφόρο Νίκης δημιουργείται η πρώτη κινηματογραφική αίθουσα στα Βαλκάνια, το Ολύμπια (1903). \n" +
                        "\n" +
                        "Εδώ λειτουργεί από το 1911 ο κινηματογράφος Πατέ με ταινίες της ομώνυμης εταιρείας παραγωγής.\n" +
                        "\n" +
                        "Ταινίες που γυρίστηκαν στην Παραλία (ενδεικτική αναφορά): Μια Αιωνιότητα και μια μέρα, Ρ20, Όλα είναι δρόμος.",
                1, 40.630440,  22.942912));
        this.db.addStation(new Station("Πλατεία Αριστοτέλους",
                "Κατά τη δεκαετία του ’50 η πλατεία μετρούσε 6 θερινά σινεμά (Ρεξ, Ηλύσια, Ελληνίς, Αιγαίο, Ρίο και Ζέφυρος). \n" +
                        "\n" +
                        "Εδώ βρίσκεται και σήμερα η σημερινή έδρα του Φεστιβάλ Κινηματογράφου Θεσσαλονίκης, το κινηματοθέατρο Ολύμπιον. \n" +
                        "\n" +
                        "Μοναδικό στο είδος του επίσης το πλωτό θερινό σινεμά με την ονομασία Κουρσάλ (ή κατ΄άλλους Τζερουσαλέμ) που λειτούρησε " +
                        "μεσοπολεμικά με βάση του την Παλιά Παραλία της πόλης.\n" +
                        "\n" +
                        "Ταινίες που γυρίστηκαν εδώ (ενδεικτική αναφορά): Η Αιωνιότητα και μια Ημέρα, Φανέλα με το 9, Γεννέθλια Πόλη, Ατσίδας.",
                1, 40.632804,  22.941331));
        this.db.addStation(new Station("Κέντρο "+" (Αγίας Σοφίας-Αλ. Σβώλου)",
                "Μια περιοχή με πολλούς κινηματογράφους: Διονύσια (Αγίας Σοφίας), Έσπερος και Ριβολί (Αλ.Σβώλου), Μακεδονικόν (Φιλ.Εταιρείας) . \n" +
                        "\n" +
                        "Εδώ λειτούργησε στη δεκαετία του ’50 παράρτημα Θεσσαλονίκης της κινηματογραφικής σχολής Σταυράκου. \n" +
                        "\n" +
                        "Κατά την δεκαετία του '80 τα βιντεοκλαμπ στο κέντρο αλλά και στις γειτονίες της πόλης αποτέλεσαν εναλλακτικό σημείο συνάντησης των σινεφιλ. \n" +
                        "\n" +
                        "Στην πορεία του χρόνου η βιντεοκασσέτα αντικαταστάθηκε απο το dvd και το blue-ray. Στην ίδια περιοχή " +
                        "(Οδός Α. Σβώλου) διατηρούνται ακόμη δύο απο τα -κάποτε αμέτρητα – βιντεο/dvd club της πόλης (AZA και Seven Film Gallery). ",
                1,  40.632511,  22.947489));
        this.db.addStation(new Station("Κέντρο 2 (Καμάρα - Ναυαρίνο)",
                "Στο κέντρο της Θεσσαλονίκης υπήρξαν αρκετοί κινηματογράφοι που για χρόνια αποτέλεσαν σημείο αναφοράς στην " +
                        "καθημερινότητα της πόλης τόσο λόγω της αρχιτεκτονικής όσο και λόγω της αναφοράς τους μέσα σε αυτήν \n" +
                        "\n" +
                        "Ενδεικτική αναφορά μερικών που αφορούν την ευρύτερη περιοχή: Ηλύσια, Ναυαρίνο, Έλση, Κλειώ, Ριβολί, Θυμέλη," +
                        " Φαργκάνη. \n" +
                        "\n" +
                        "Οι κινηματογράφοι αλλά και τα κάφε-μπαρ του κέντρου της πόλης αποτέλεσαν, από την δεκαετία του '80 και έπειτα" +
                        ", σημείο συνάντησης των φανατικών κινηματογραφόφιλων με τα κινηματογραφικά free press της Θεσσαλονίκης (Εξώστης, " +
                        "Φιξ Καρέ, Παράλλαξη, κλπ).\n" +
                        "\n" +
                        "O Εξώστης υπήρξε πανελλαδικά το πρώτο free press. Κυκλοφόρησε το 1987.\n" +
                        "\n" +
                        "Στα σινεμά της περιοχής κατά τις δεκαετίες του '80 και '90 διοργανώθηκαν αρκετές πρωτότυπες προβολές, " +
                        "μεταμεσονύκτιες και κινηματογραφικών λεσχών.",
                1, 40.6319738, 22.952104));
        this.db.addStation(new Station("Λευκός Πύργος",
                "Από το 1905 ο κήπος του Λευκού Πύργου φιλοξενούσε αίθουσα κινηματογράφου. \n" +
                        "\n" +
                        "Εδώ βρίσκεται η Εταιρεία Μακεδονικών Σπουδών, παλιά έδρα του Φεστιβάλ Κινηματογράφου Θεσσαλονίκης και " +
                        "τα σινεμά Παλλάς (σημερινή έδρα της ΚΟΘ) και Αλέξανδρος. \n" +
                        "\n" +
                        "Στην ΕΜΣ φιλοξενήθηκε για αρκετά χρόνια το Φεστιβάλ Κινηματογράφου Θεσσαλονίκης. \n" +
                        "\n" +
                        "Στον Αλέξανδρο φιλουξενούνται κάθε Οκτώβριο οι προβολές του Thessaloniki International Short Film Festival " +
                        "(TISFF).\n" +
                        "\n" +
                        "Η Νέα Παραλία, όπως και ο Λευκός Πύργος αποτέλεσαν επίσης εξαιρετικό σκηνικό φόντο για αρκετές ταινίες του " +
                        "παλιού αλλά και του νέου ελληνικού κινηματογράφου (ενδεικτική αναφορά): Ο Φάκελος Πολκ στον αέρα, Κάτι να καίει," +
                        " Το βλέμμα του Οδυσσέα, Η Αιωνιότητα και μια Ημέρα, Το Λιβάδι που δακρύζει, Γεννέθλια Πόλη, Παρένθεση, " +
                        "Χώμα και Νερό",
                1, 40.62638,  22.948306));

        //Line 2
        this.db.addStation(new Station("Το ξυπόλητο τάγμα (1955)",
                "Σκηνοθεσία: Γκρεγκ Τάλλας\n" +
                        "\n" +
                        "Παίζουν: Κωστή Μαρία, Φερμας Νίκος, Φραγκεδάκης Βασίλης\n" +
                        "\n" +
                        "Info: \n" +
                        "\n" +
                        "Η αληθινή ιστορία επιβίωσης 160 παιδιών στην Θεσσαλονίκη της Κατοχής. \n" +
                        "\n" +
                        "Η ταινία έχει σαν χώρο δράσης την Θεσσαλονίκη μεταξύ των δεκαετιών 1940-50.\n" +
                        "\n" +
                        "Μέρος των γυρισμάτων έγινε στο Επταπύργιο-Άνω Πόλη. \n" +
                        "\n" +
                        "Οι μικροί πρωταγωνιστές προέρχονται απο το Ορφανοτροφείο Αθηνών.\n" +
                        "\n" +
                        "Χρυσό Βραβείο στο Φεστιβάλ Κινηματογράφου του Εδιμβούργου (1955).",
                2, 40.6197797, 22.9638766));
        this.db.addStation(new Station("Ο ατσίδας (1961)",
                "Σκηνοθεσία: Γιάννης Δαλιανίδης\n" +
                        "\n" +
                        "Παίζουν: Ηλιόπουλος Ντίνος, Λάσκαρη Ζώη, Ζερβός Παντελής, Στρατηγός Στέφανος\n" +
                        "\n" +
                        "Info: \n" +
                        "\n" +
                        "Δύο αδέρφια προσπαθούν να ισορροπήσουν ανάμεσα στις αισθηματικές τους σχέσεις και την συντηρητικές αρχές της πατρικής οικίας τους. \n" +
                        "\n" +
                        "Εξ ολοκλήρου γυρισμένη στην Θεσσαλονίκη: Πανόραμα, περιοχή Ανθέων, Πλ. Αριστοτέλους\n" +
                        "\n" +
                        "Μεταφορά του θεατρικού έργου του Δ. Ψαθά “Εξοχικόν Κέντρον ο Έρως",
                2, 40.632804, 22.941331));
        this.db.addStation(new Station("Κάτι να καίει (1964)",
                "Σκηνοθεσία: Γιάννης Δαλιανίδης\n" +
                        "\n" +
                        "Παίζουν: Βλαχοπούλου Ρένα, Ηλιόπουλος Ντίνος, Καραγιάννη Μάρθα, Ναθαναήλ Έλενα, Βουτσάς Κώστας\n" +
                        "\n" +
                        "Info: \n" +
                        "\n" +
                        "Ένα νεανικό μουσικό συγκρότημα, ένας προσωπάρχης που συνοδέυει την κόρη του αφεντικού του στη Θεσσαλονίκη, μια κληρονομιά που περιμένει έναν γάμο. Μια παρέα νέων ανθρώπων σε αστείες και ρομαντικές περιπέτειες. \n" +
                        "\n" +
                        "Η ταινία έχει γυρίσματα σε χώρους όπως ΔΕΘ, Λευκός Πύργος, κ.α.\n" +
                        "\n" +
                        "Το πρώτο μιούζικαλ του Γιάννη Δαλιανίδη και παράλληλα η πρώτη ελληνική ταινία σινεμασκόπ\n" +
                        "\n" +
                        "Πρώτη κινηματογραφική εμφάνιση για την 17χρόνη Έλενα Ναθαναήλ",
                2,40.628192, 22.9561));
        this.db.addStation(new Station("Παρένθεση (1968)",
                "Σκηνοθεσία: Τάκης Κανελλόπουλος\n" +
                        "\n" +
                        "Παίζουν: Λαδικού Αλεξάνδρα, Αντωνόπουλος Άγγελος\n" +
                        "\n" +
                        "Info: \n" +
                        "\n" +
                        "Ένα ταξίδι με τρένο, μια στάση με ολιγόωρη καθυστέρηση, ένας δυνατός έρωτας δύο ανθρώπων, μια παρένθεση. \n" +
                        "\n" +
                        "Βασισμένη στο θεατρικό έργο του Νοέλ Κάουαρντ “Ασάλευτη Ζωή”\n" +
                        "\n" +
                        "4 βραβεία στο Φεστιβάλ Θεσσαλονίκης\n" +
                        "\n" +
                        "Η ταινία γυρίστηκε σε: Νέο Σιδηροδρομικό Σταθμό, Νέα Παραλία Θεσσαλονίκης και στην περιοχή Αρετσού (Ν. Κρήνη)",
                2,40.6223568, 22.9512486));
        this.db.addStation(new Station("Η Φανέλλα με το Νο9 (1987)",
                "Σκηνοθεσία: Παντελής Βούλγαρης\n" +
                        "\n" +
                        "Παίζουν: Τζώρτζογλου Στράτος, Μπαζάκα Θέμις\n" +
                        "\n" +
                        "Info: \n" +
                        "\n" +
                        "Η πορεία ενός ταλαντούχου ποδοσφαιριστή, ο οποίος μέσα από νίκες και ήττες, τραυματισμούς και παρασκήνιο, θα καταλάβει ότι η επιτυχία έχει το τίμημά της. \n" +
                        "\n" +
                        "Μέρος της ταινίας έχει γυριστεί στην Θεσσαλονίκη: Πλ. Αριστοτέλους, Εύοσμος.\n" +
                        "\n" +
                        "Βασισμενη στο ομόνυμο μυθιστόρημα του Μένη Κουμανταρέα\n" +
                        "\n" +
                        "2 βραβεία στο Φεστιβάλ Θεσσαλονίκης\n" +
                        "\n" +
                        "Ο πρωταγωνιστής της ταινίας Στρ. Τζώρτζογλου την ίδια χρονιά θα συμμετέχει στα γυρίσματα της ταινίας " +
                        "του Θ. Αγγελόπουλου “Τοπίο στην Ομίχλη” που γίνονται στην Θεσσαλονίκη.",
                2, 40.632804, 22.941331));
        this.db.addStation(new Station("Μια Αιωνιότητα και μια μέρα (1998)",
                "Σκηνοθεσία: Θόδωρος Αγγελόπουλος\n" +
                        "\n" +
                        "Παίζουν: Γκαντζ Μπρούνο, Ρενό Ιζαμπέλ, Μπενιβόλιο Φαμπρίτσιο\n" +
                        "\n" +
                        "Info: \n" +
                        "\n" +
                        "Ένας μεσήλικας συγγραφέας, που ασχολείται με το έργο του Δ. Σολωμού, αναζητάει τις αναμνήσεις μιας ζωής σε μια περιπλάνηση μιας ημέρας. \n" +
                        "\n" +
                        "Γυρισμένη σε αρχοντικό της Βασ. Όλγας, στην Παλιά Παραλία, Πλ. Αριστοτέλους και σε χώρους της οδού Τσιμισκή. \n" +
                        "\n" +
                        "Ο Θόδωρος Αγγελόπουλος γύρισε στην Θεσσαλονίκη μέρος ή εξ ολοκλήρου 4 ταινίες. Ανάμεσα τους και την “Αιωνιότητα”\n" +
                        "\n" +
                        "Χρυσός Φοίνικας στο Φεστιβάλ Καννών του 1998",
                2,40.6148604, 22.954036));
        this.db.addStation(new Station("Χώρα Προέλευσης (2010)",
                "Σκηνοθεσία: Σύλλας Τζουμέρκας\n" +
                        "\n" +
                        "Παίζουν: Μουτούση Αμαλία, Σαμαράς Θάνος, Τσιριγκούλη Ιωάννα\n" +
                        "\n" +
                        "Info: \n" +
                        "\n" +
                        "Οικογενειακό δράμα ως ακτινογραφία της νεοελληνικής κοινωνικής πραγματικότητας. \n" +
                        "\n" +
                        "Η ταινία πραγματοποιεί μεγάλο μέρος της δράσης της σε γνωστά σημεία της σημερινής Θεσσαλονίκης: Λεωφόρος Στρατού (ΕΡΤ3), Νέα Παραλία, Αγίος Δημήτριος. \n" +
                        "\n" +
                        "Συμμετοχή στην Εβδομάδα Κριτικής του Φεστιβάλ Βενετίας",
                2,40.638799, 22.947769));
        this.db.addStation(new Station("Σούπερ Δημήτριος (2011)",
                "Σκηνοθεσία: Γιώργος Παπαιωάνου\n" +
                        " \n" +
                        "Παίζουν: Βαϊνας Δημήτρης, Παπαδόπουλος Πάρης, Σφέτσα Όλγα\n" +
                        "\n" +
                        "Info: \n" +
                        "\n" +
                        "Όταν ο Κάπτεν Φ.Ρομ, απαιτώντας να τον φωνάζουν με το πραγματικό του όνομα, μετατρέπει τον Λευκό Πύργο σε γιγάντιο φραπέ κι εξαφανίζει όλο το γύρο από τα σουβλατζίδικα της Θεσσαλονίκης, ο Σούπερ Δημήτριος αναλαμβάνει να σώσει τη συμπρωτεύουσα.\n" +
                        "\n" +
                        "Σάτιρα θεσσαλονικιώτικων και βορειοελλαδικών κλισέ με της Μεταπολίτευσης με πολύ χιούμορ. \n" +
                        "\n" +
                        "Τόποι γυρισμάτων: Θεολογική Σχολή ΑΠΘ, Νέο Δημαρχειακό Μέγαρο, Λευκός Πύργος, ΟΣΕ, Τούμπα \n" +
                        "\n" +
                        "Βραβείο κοινού στο Φεστιβάλ Θεσσαλονίκης",
                2,40.6308363, 22.9543887));

        //add movies
        this.db.addMovie(new Movie(7, "Το ξυπόλητο τάγμα",
                "Η αληθινή ιστορία επιβίωσης 160 παιδιών στην Θεσσαλονίκη της Κατοχής. \n" +
                "\n" +
                "Η ταινία έχει σαν χώρο δράσης την Θεσσαλονίκη μεταξύ των δεκαετιών 1940-50.\n" +
                "\n" +
                "Μέρος των γυρισμάτων έγινε στο Επταπύργιο-Άνω Πόλη. \n" +
                "\n" +
                "Οι μικροί πρωταγωνιστές προέρχονται απο το Ορφανοτροφείο Αθηνών.\n" +
                "\n" +
                "Χρυσό Βραβείο στο Φεστιβάλ Κινηματογράφου του Εδιμβούργου (1955).",
                "Κωστή Μαρία,Φερμας Νίκος,Φραγκεδάκης Βασίλης",
                "Γκρεγκ Τάλλας", "1955"));
        this.db.addMovie(new Movie(8, "Ο ατσίδας",
                "Δύο αδέρφια προσπαθούν να ισορροπήσουν ανάμεσα στις αισθηματικές τους σχέσεις και την συντηρητικές αρχές της πατρικής οικίας τους. \n" +
                "\n" +
                "Εξ ολοκλήρου γυρισμένη στην Θεσσαλονίκη: Πανόραμα, περιοχή Ανθέων, Πλ. Αριστοτέλους\n" +
                "\n" +
                "Μεταφορά του θεατρικού έργου του Δ. Ψαθά “Εξοχικόν Κέντρον ο Έρως”",
                "Ηλιόπουλος Ντίνος,Λάσκαρη Ζώη,Ζερβός Παντελής,Στρατηγός Στέφανος",
                "Γιάννης Δαλιανίδης", "1961"));
        this.db.addMovie(new Movie(9, "Κάτι να καίει",
                "Ένα νεανικό μουσικό συγκρότημα, ένας προσωπάρχης που συνοδέυει την κόρη του αφεντικού του στη Θεσσαλονίκη, μια κληρονομιά που περιμένει έναν γάμο. Μια παρέα νέων ανθρώπων σε αστείες και ρομαντικές περιπέτειες. \n" +
                "\n" +
                "Η ταινία έχει γυρίσματα σε χώρους όπως ΔΕΘ, Λευκός Πύργος, κ.α.\n" +
                "\n" +
                "Το πρώτο μιούζικαλ του Γιάννη Δαλιανίδη και παράλληλα η πρώτη ελληνική ταινία σινεμασκόπ\n" +
                "\n" +
                "Πρώτη κινηματογραφική εμφάνιση για την 17χρόνη Έλενα Ναθαναήλ",
                "Βλαχοπούλου Ρένα,Ηλιόπουλος Ντίνος,Καραγιάννη Μάρθα,Ναθαναήλ Έλενα,Βουτσάς Κώστας",
                "Γιάννης Δαλιανίδης", "1964"));
        this.db.addMovie(new Movie(10, "Παρένθεση",
                "Ένα ταξίδι με τρένο, μια στάση με ολιγόωρη καθυστέρηση, ένας δυνατός έρωτας δύο ανθρώπων, μια παρένθεση. \n" +
                "\n" +
                "Βασισμένη στο θεατρικό έργο του Νοέλ Κάουαρντ “Ασάλευτη Ζωή”\n" +
                "\n" +
                "4 βραβεία στο Φεστιβάλ Θεσσαλονίκης\n" +
                "\n" +
                "Η ταινία γυρίστηκε σε: Νέο Σιδηροδρομικό Σταθμό, Νέα Παραλία Θεσσαλονίκης και στην περιοχή Αρετσού (Ν. Κρήνη))",
                "Λαδικού Αλεξάνδρα,Αντωνόπουλος Άγγελος",
                "Τάκης Κανελλόπουλος", "1968"));
        this.db.addMovie(new Movie(11, "Η Φανέλλα με το Νο9",
                "Η πορεία ενός ταλαντούχου ποδοσφαιριστή, ο οποίος μέσα από νίκες και ήττες, τραυματισμούς και παρασκήνιο, " +
                "θα καταλάβει ότι η επιτυχία έχει το τίμημά της. \n" +
                "\n" +
                "Μέρος της ταινίας έχει γυριστεί στην Θεσσαλονίκη: Πλ. Αριστοτέλους, Εύοσμος.\n" +
                "\n" +
                "Βασισμενη στο ομόνυμο μυθιστόρημα του Μένη Κουμανταρέα\n" +
                "\n" +
                "2 βραβεία στο Φεστιβάλ Θεσσαλονίκης\n" +
                "\n" +
                "Ο πρωταγωνιστής της ταινίας Στρ. Τζώρτζογλου την ίδια χρονιά θα συμμετέχει στα γυρίσματα της ταινίας του Θ. Αγγελόπουλου “Τοπίο στην Ομίχλη” που γίνονται στην Θεσσαλονίκη.",
                "Τζώρτζογλου Στράτος,Μπαζάκα Θέμις",
                "Παντελής Βούλγαρης", "1987"));
        this.db.addMovie(new Movie(12, "Μια Αιωνιότητα και μια μέρα",
                "Ένας μεσήλικας συγγραφέας, που ασχολείται με το έργο του Δ. Σολωμού, αναζητάει τις αναμνήσεις μιας ζωής σε μια περιπλάνηση μιας ημέρας. \n" +
                "\n" +
                "Γυρισμένη σε αρχοντικό της Βασ. Όλγας, στην Παλιά Παραλία, Πλ. Αριστοτέλους και σε χώρους της οδού Τσιμισκή. \n" +
                "\n" +
                "Ο Θόδωρος Αγγελόπουλος γύρισε στην Θεσσαλονίκη μέρος ή εξ ολοκλήρου 4 ταινίες. Ανάμεσα τους και την “Αιωνιότητα”\n" +
                "\n" +
                "Χρυσός Φοίνικας στο Φεστιβάλ Καννών του 1998",
                "Γκαντζ Μπρούνο,Ρενό Ιζαμπέλ,Μπενιβόλιο Φαμπρίτσιο",
                "Θόδωρος Αγγελόπουλος", "1998"));
        this.db.addMovie(new Movie(13, "Χώρα Προέλευσης", "Οικογενειακό δράμα ως ακτινογραφία της νεοελληνικής κοινωνικής πραγματικότητας. \n" +
                "\n" +
                "Η ταινία πραγματοποιεί μεγάλο μέρος της δράσης της σε γνωστά σημεία της σημερινής Θεσσαλονίκης: Λεωφόρος Στρατού (ΕΡΤ3), Νέα Παραλία, Αγίος Δημήτριος. \n" +
                "\n" +
                "Συμμετοχή στην Εβδομάδα Κριτικής του Φεστιβάλ Βενετίας",
                "Μουτούση Αμαλία,Σαμαράς Θάνος,Τσιριγκούλη Ιωάννα",
                "Σύλλας Τζουμέρκας", "2010"));
        this.db.addMovie(new Movie(14, "Σούπερ Δημήτριος", "Όταν ο Κάπτεν Φ.Ρομ, απαιτώντας να τον φωνάζουν με το πραγματικό του " +
                "όνομα, μετατρέπει τον Λευκό Πύργο σε γιγάντιο φραπέ κι εξαφανίζει όλο το γύρο από τα σουβλατζίδικα της Θεσσαλονίκης, ο Σούπερ Δημήτριος αναλαμβάνει να σώσει τη συμπρωτεύουσα.\n" +
                "\n" +
                "Σάτιρα θεσσαλονικιώτικων και βορειοελλαδικών κλισέ με της Μεταπολίτευσης με πολύ χιούμορ. \n" +
                "\n" +
                "Τόποι γυρισμάτων: Θεολογική Σχολή ΑΠΘ, Νέο Δημαρχειακό Μέγαρο, Λευκός Πύργος, ΟΣΕ, Τούμπα \n" +
                "\n" +
                "Βραβείο κοινού στο Φεστιβάλ Θεσσαλονίκης",
                "Βαϊνας Δημήτρης,Παπαδόπουλος Πάρης,Σφέτσα Όλγα",
                "Γιώργος Παπαιωάνου", "2011"));


        //add photos

        //line 1
        // station 1
        this.db.addPhoto(new Photo("pantheon", 1, -1,"Ο κινηματογράφος Πάνθεον στο Βαρδάρη, λίγο πριν την κατεδάφισή του\n" +
                "(Αρχείο Ν. Θεοδοσίου – Σινέ Θεσσαλονίκη, σ. 33)"));
        this.db.addPhoto(new Photo("splendid", 1, -1,"Ο κινηματογράφος Σπλέντιτ όπως απεικονίζεται σε καρτ ποστάλ\n" +
                "(Αρχείο Ν. Θεοδοσίου)"));
        this.db.addPhoto(new Photo("pantheon_markiza", 1, -1,"Ο κινηματογράφος Πάνθεον στις δόξες του\n" +
                "Αρχείο Ν.Θεοδοσίου"));
        //station 2
        this.db.addPhoto(new Photo("redmoterlimani", 2, -1,"Ο κόκκινος μοτέρ του 11ου Φεστιβάλ Ντοκιμαντέρ Θεσσαλονίκης στρέφει την κάμερα στο λιμάνι της πόλης \n" +
                "Αρχείο ΦΚΘ – Σινέ Θεσσαλονίκη, σ.158)"));
        this.db.addPhoto(new Photo("apothikia", 2, -1,"Η είσοδος της Αποθήκης Γ στο Λιμάνι\n" +
                "Αρχείο ΦΚΘ)"));
        this.db.addPhoto(new Photo("cinemuseuminside", 2, -1,"Το εσωτερικό του Μουσείου Κινηματογράφου στο λιμάνι.\n" +
                "Αρχείο ΦΚΘ"));
        //station 3
        this.db.addPhoto(new Photo("cinemaolympia", 3, -1,"Ο κινηματογράφος Ολύμπια (1917), όπου συγκεντρώνονται μαθήτριες του γαλλικού σχολείου πριν την προβολή\n" +
                "Αρχείο Κέντρου Ιστορίας Θεσσαλονίκης – Σινέ Θεσσαλονίκη, σ. 40)"));
        this.db.addPhoto(new Photo("olympia", 3, -1,"Ο κινηματογράφος Ολύμπια στην προκυμαία της Θεσσαλονίκης – Καρτ ποστάλ των αρχών του 20ού αιώνα\n" +
                "(Αρχείο Ν. Θεοδοσίου- Σινέ Θεσσαλονίκη, σ.29)"));
        this.db.addPhoto(new Photo("pathe", 3, -1,"Ο κινηματογράφος Πατέ στη Λεωφόρο Νίκης, επιταγμένος από τους Γερμανούς στην Κατοχή\n" +
                "Σινέ Θεσσαλονίκη, σ.31"));
        this.db.addPhoto(new Photo("salonica_pathe_1918", 3, -1,"Ο κινηματογράφος Πατέ στην παραλία σε καρτ ποστάλ των αρχών του 20ού αιώνα"));
        this.db.addPhoto(new Photo("olympiaview", 3, -1,"Άποψη από την είσοδο του κινηματογράφου Ολύμπια στην παραλία\n" +
                "(Αρχείο Ν.Θεοδοσίου – Ίντερνετ)"));
        //station 4
        this.db.addPhoto(new Photo("hlysia", 4, -1,"Ο παλιός κινηματογράφος Ηλύσια στην Αριστοτέλους την ημέρα της απελευθέρωσης της Θεσσαλονίκης (δεκαετία ’40) \n" +
                "(Αναδημοσίευση από το Σινέ Θεσσαλονίκη, σ.32)"));
        this.db.addPhoto(new Photo("festivaltree02", 4, -1,"Κινηματογραφικά καρέ σχηματίζουν το δέντρο του σινεμά στην πλατεία Αριστοτέλους\n" +
                "Σινέ Θεσσαλονίκη, σ.157, Αρχείο ΦΚΘ"));
        this.db.addPhoto(new Photo("olympionnight", 4, -1,"Νυχτερινή άποψη του κινηματογράφου Ολύμπιον, της έδρας του Φεστιβάλ Κινηματογράφου Θεσσαλονίκης\n" +
                "Αρχείο ΦΚΘ"));
        this.db.addPhoto(new Photo("olympioninside", 4, -1,"Εσωτερικό του κινηματογράφου Ολύμπιον\n" +
                "Αρχείο ΦΚΘ"));
        //station 5
        this.db.addPhoto(new Photo("dionyssia", 5, -1,"Το κινηματοθέατρο Διονύσια, που εγκαινιάστηκε στις 26 Νοεμβρίου 1926\n" +
                "(Βιβλίο Κ. Τομανά –Οι κινηματογράφοι της παλιάς Θεσσαλονίκης/ Αναδημοσίευση στο Σινέ Θεσσαλονίκη σ. 41)"));
        this.db.addPhoto(new Photo("makedonikon", 5, -1,"Νυχτερινή άποψη του σινεμά Μακεδονικόν\n" +
                "(Αρχείο ΚΙΘ-Σινέ Θεσσαλονίκη, σ.46)"));
        this.db.addPhoto(new Photo("esperos", 5, -1,"Άποψη του κινηματογράφου Έσπερος, που ταυτίστηκε συχνά με τις προβολές του Φεστιβάλ Κινηματογράφου Θεσσαλονίκης\n" +
                "(Αρχείο ΦΚΘ – Σινέ Θεσσαλονίκη σ.47)"));
        //station 6
        this.db.addPhoto(new Photo("nauarino", 6, -1,"Εξωτερική όψη του κινηματογράφου Ναυαρίνον την περίοδο του Φεστιβάλ " +
                "Κινηματογράφου (1998)"));
        this.db.addPhoto(new Photo("efimerida", 6, -1,"Διαφημιση ταινίας που προβλήθηκε στον κινηματογράφο Θυμέλη " +
                "(Πηγή: Εφημερίδα Μακεδονία)"));
        this.db.addPhoto(new Photo("exostis", 6, -1,"Πρωτοσέλιδο του πρώτου τεύχους του ιστορικού free press Εξώστης)"));
        //station 7
        this.db.addPhoto(new Photo("pallaswhitetower", 7, -1,"Το παλιό κινηματοθέατρο Παλλάς κοντά στο Λευκό Πύργο, με αρχιτεκτονικό σχέδιο Ε. Μοδιάνο και 860 πολυτελείς θέσεις.\n" +
                "Αρχείο Ν.Θεοδοσίου – Ίντερνετ."));
        this.db.addPhoto(new Photo("ems", 7, -1,"Κόσμος έξω από την Εταιρεία Μακεδονικών Σπουδών, που υπήρξε έδρα του Φεστιβάλ Κινηματογράφου στη δεκαετία του ’70.\n" +
                "Αρχείο ΦΚΘ"));
        this.db.addPhoto(new Photo("theoreiaems", 7, -1,"Τα θεωρεία της Εταιρείας Μακεδονικών Σπουδών με τους διαγωνιζόμενους και τους καλεσμένους του φεστιβάλ (δεκαετία ’60) \n" +
                "Αρχείο ΦΚΘ"));
        //line2
        //station 8
        this.db.addPhoto(new Photo("ksypolitotagma10", 8, 1, ""));      //main
        this.db.addPhoto(new Photo("tagma", 8, 1, ""));                 //main
        this.db.addPhoto(new Photo("barefootbattalionmovieposter", 8, 1, ""));
        this.db.addPhoto(new Photo("xipolitotagma4", 8, 1, ""));
        this.db.addPhoto(new Photo("xipolitotagma8", 8, 1, ""));
        this.db.addPhoto(new Photo("greggntallas", -1, 1, ""));
        this.db.addPhoto(new Photo("kwsti_maria", -1, 1, ""));          //actor
        this.db.addPhoto(new Photo("fermas_nikos", -1, 1, ""));         //actor
        this.db.addPhoto(new Photo("fragkedakhs_vasilis", -1, 1, ""));  //actor
        //station 9
        this.db.addPhoto(new Photo("p08", 9, 2, ""));
        this.db.addPhoto(new Photo("atsidas", 9, 2, ""));
        this.db.addPhoto(new Photo("promoatsidas", 9, 2, ""));
        this.db.addPhoto(new Photo("dalianidis", -1, 2, ""));
        this.db.addPhoto(new Photo("eliopoulos", -1, 2, ""));
        this.db.addPhoto(new Photo("laskari", -1, 2, ""));
        this.db.addPhoto(new Photo("zervos", -1, 2, ""));
        this.db.addPhoto(new Photo("stratigos", -1, 2, ""));
        //station 10
        this.db.addPhoto(new Photo("katinakaiei0", 10, 3, ""));
        this.db.addPhoto(new Photo("katinakaiei4", 10, 3, ""));
        this.db.addPhoto(new Photo("katinakaieiplaz", 10, 3, ""));
        this.db.addPhoto(new Photo("dalianidis", -1, 3, ""));
        this.db.addPhoto(new Photo("vlaxopoulou", -1, 3, ""));
        this.db.addPhoto(new Photo("eliopoulos", -1, 3, ""));
        this.db.addPhoto(new Photo("karagiannh", -1, 3, ""));
        this.db.addPhoto(new Photo("nathanahl", -1, 3, ""));
        this.db.addPhoto(new Photo("voutsas", -1, 3, ""));
        //station 11
        this.db.addPhoto(new Photo("p388322", 11, 4, ""));
        this.db.addPhoto(new Photo("c72", 11, 4, ""));
        this.db.addPhoto(new Photo("kanelopoulos", -1, 4, ""));
        this.db.addPhoto(new Photo("ladikou", -1, 4, ""));
        this.db.addPhoto(new Photo("antwnopoulos", -1, 4, ""));
        //station 12
        this.db.addPhoto(new Photo("p64411", 12, 5, ""));
        this.db.addPhoto(new Photo("p64433", 12, 5, ""));
        this.db.addPhoto(new Photo("voulgaris", -1, 5, ""));
        this.db.addPhoto(new Photo("tzortzoglou", -1, 5, ""));
        this.db.addPhoto(new Photo("bazaka", -1, 5, ""));
        //station 13
        this.db.addPhoto(new Photo("eternityandaday", 13, 6, ""));
        this.db.addPhoto(new Photo("miaaioniotita", 13, 6, ""));
        this.db.addPhoto(new Photo("aggelopoulos", -1, 6, ""));
        this.db.addPhoto(new Photo("bruno", -1, 6, ""));
        this.db.addPhoto(new Photo("isarenauld", -1, 6, ""));
        this.db.addPhoto(new Photo("bentivolio", -1, 6, ""));
        //station 14
        this.db.addPhoto(new Photo("amaliamoutoussihoraproelefsis", 14, 7, ""));
        this.db.addPhoto(new Photo("christospassalisyoulaboudalifilia", 14, 7, ""));
        this.db.addPhoto(new Photo("homeland6", 14, 7, ""));
        this.db.addPhoto(new Photo("syllastzoumerkas", -1, 7, ""));
        this.db.addPhoto(new Photo("moutousi", -1, 7, ""));
        this.db.addPhoto(new Photo("samaras", -1, 7, ""));
        this.db.addPhoto(new Photo("tsirigkouli", -1, 7, ""));

        //station 15
        this.db.addPhoto(new Photo("p1334779197", 15, 8, ""));
        this.db.addPhoto(new Photo("souperdimitrios", 15, 8, ""));
        this.db.addPhoto(new Photo("superdemetriosstill", 15, 8, ""));
        this.db.addPhoto(new Photo("lefkospyrgos", 15, 8, ""));
        this.db.addPhoto(new Photo("pyrgosote", 15, 8, ""));
        this.db.addPhoto(new Photo("papaiwannou", -1, 8, ""));
        this.db.addPhoto(new Photo("vainas", -1, 8, ""));
        this.db.addPhoto(new Photo("papadopoulos", -1, 8, ""));
        this.db.addPhoto(new Photo("sfetsa", -1, 8, ""));

        //green line
        this.db.addPhoto(new Photo("green1", 0, 0, ""));
        this.db.addPhoto(new Photo("green2", 0, 0, ""));
        this.db.addPhoto(new Photo("green3", 0, 0, ""));
        this.db.addPhoto(new Photo("green4", 0, 0, ""));
        this.db.addPhoto(new Photo("green5", 0, 0, ""));
        this.db.addPhoto(new Photo("green6", 0, 0, ""));
        this.db.addPhoto(new Photo("green7", 0, 0, ""));
        this.db.addPhoto(new Photo("green8", 0, 0, ""));

        //TimelineStation 1
        this.db.addTimelineStation(new TimelineStation("Λευκός Πύργος-\n"+"Εταιρεία Μακεδονικών Σπουδών (ΕΜΣ)", 40.6271087, 22.9497383));
        //Milestones
        this.db.addMilestone(new Milestone(1, "1964", "Η έδρα του φεστιβάλ μεταφέρεται από τον κινηματογράφο «Ολύμπιον» στην Εταιρεία " +
                "Μακεδονικών Σπουδών, χώρο με τον οποίο το φεστιβάλ θα συνδεθεί ιστορικά για περίπου 3,5 δεκαετίες (5η Εβδομάς " +
                "Ελληνικού Κινηματογράφου, 1964)", "theatesems", "Θεατές στην Εταιρεία Μακεδονικών Σπουδών"));
        this.db.addMilestone(new Milestone(1, "1966", "Ο παραγωγός Τζεϊμς Παρις, εμβληματική μορφή του Φεστιβάλ κατά την περίοδο 1966-1972," +
                " «κατεβαζει» ενα τανκ στην πλατεια του Λευκου Πυργου για διαφημιστικους σκοπους της ταινίας «Ξεχασμενοι ηρωες», σε δική" +
                " του παραγωγή (7η Φεστιβάλ Ελληνικού Κινηματογράφου, 1966). \n" +
                "Οι εκπληξεις συνεχιζονται και μεσα στην αιθουσα, οπου οι 1.500 θεατες βρισκουν στη θεση τους απο ενα πακετο " +
                "τσιγαρα, δωρο του παραγωγου στο κοινο.", "paris_ems",  "Οι Γιώργος Τζαβέλλας και Τζαίημς Πάρις σε φεστιβαλική προβολή στην ΕΜΣ"));
        this.db.addMilestone(new Milestone(1, "1970", "Ο Β Εξώστης, το δυναμικό νεανικό κοινό του φεστιβάλ κάνει δυναμικά την εμφάνιση του " +
                "για να διαδραματίσει σημαντικό ρόλο τα επόμενα χρόνια (11ο ΦΚΘ, 1970): \n" +
                "\n" +
                "Ο «Β΄εξώστης» παίρνει το όνομά του από την ομώνυμη κατηγορία θέσεων στην αίθουσα της Εταιρείας Μακεδονικών " +
                "σπουδών, όπου συνήθως συγκεντρώνονται οι φοιτητές και η νεολαία λόγω του φθηνού εισιτηρίου.", "exostes",
                "Άποψη της Εταιρείας Μακεδονικών Σπουδών, όπου διακρίνεται ο Β΄εξώστης."));
        this.db.addMilestone(new Milestone(1,"1972", "Πραγματοποιείται για πρώτη φορά το 1ο Διεθνές Φεστιβάλ Κινηματογράφου Θεσσαλονίκης " +
                "στην Εταιρεία Μακεδονικών Σπουδών (13ο ΦΚΘ, 1972)\n" +
                "\n" +
                "( Το διεθνές φεστιβάλ διεξάγεται μία βδομάδα μετά το επίσημο διαγωνιστικό του ελληνικού προγράμματος, αφορά σε " +
                "ταινίες μικρού μήκους και διοργανώνεται υπό την ευθύνη της ΔΕΘ και της Διεθνούς Ομοσπονδίας Παραγωγών Κινηματογράφου " +
                "(FIAPF).", "diethnesfilmfestival",
                "Άποψη της Εταιρείας Μακεδονικών Σπουδών, στην επέτειο των δέκα χρόνων του Διεθνούς Φεστιβάλ Κινηματογράφου."));
        this.db.addMilestone(new Milestone(1,"1992","Το Φεστιβάλ γίνεται και επισήμως Διεθνές. Το επίσημο διαγωνιστικό φιλοξενεί " +
                "ταινίες πρωτοεμφανιζομενων δημιουργών από όλο τον κόσμο. Παράλληλα φιλοξενεί μέχρι και το 1997 το Φεστιβάλ " +
                "Ελληνικού Κινηματογράφου.", "p1992", ""));
        this.db.addMilestone(new Milestone(1,"1995","Το Φεστιβάλ φιλοξενείται για τελευταία φορά στους χώρους της ΕΜΣ. Το κτίριο θα" +
                " ανακαινιστεί ριζικά στο εσωτερικό του από τον Οργανισμό Πολιτιστικής Πρωτεύουσας Θεσσαλονίκη 1997, αφήνοντας πίσω του " +
                "μια λαμπρή κινηματογραφική ιστορία για την πόλη.", "p1995", ""));




        //TimelineStation 2
        this.db.addTimelineStation(new TimelineStation("Ντορέ – Αλέξανδρος", 40.6269345, 22.9493905));
        //Milestones

        this.db.addMilestone(new Milestone(2, "1963",
                "Το Σινέ Αλέξανδρος, βρίσκεται κάτω απο την Λέσχη Αξιωματικών Θεσσαλονίκης, γειτνίαζε με την ΕΜΣ, έδρα " +
                "του Φεστιβάλ επί πολλά χρόνια, και αποτέλεσε χώρο αρκετών προβολών και δράσεων μεταξύ των οποίων προβολές " +
                 "ταινιών μικρού μήκους."+ "\n"+
                 "Εδώ βρίσκεται και ένα γλυπτό μνημείο του σκηνοθέτη Τάκη Κανελλόπουλου, ο οποίος " +
                "συνέδεσε το όνομα του με το Φεστιβάλ Θεσσαλονίκης και το τότε στέκι των φεστιβαλιστών, το Ντορέ.", "cinealexandros02",
                "Άποψη του κινηματογράφου \"Αλέξανδρος\", που φιλοξένησε προβολές του Φεστιβάλ."));
        this.db.addMilestone(new Milestone(2, "1990", "Το καφενείο «Ντορέ» απέναντι από το Λευκό Πύργο αποτέλεσε ιστορικό στέκι, όπου " +
                "συγκεντρώνονται καλλιτέχνες και κοινό απο την δεκαετία του '60 εως και την διεθνοποίηση του Φεστιβάλ (δεκαετία '90)" +
                "\n" +
                "Οι συζητήσεις μεταξύ σκηνοθετών και θεατών στο «Ντορέ» ξεκινούν συνήθως μετά το τέλος των προβολών και " +
                "κρατούν μέχρι τα ξημερώματα, διαμορφώνοντας φεστιβαλική ατμόσφαιρα.", "jamesparis",
                "Ο Ελληνοαμερικανός παραγωγός Τζέιμς Πάρις σε πηγαδάκι του Φεστιβάλ Κινηματογράφου Θεσσαλονίκης στο Ντορέ"));


        //TimelineStation 3
        this.db.addTimelineStation(new TimelineStation("Σβώλου – Πλ. Ναυαρίνο", 40.6330961, 22.9470789));
        //Milestones
        this.db.addMilestone(new Milestone(3, "1987", "Ο κινηματογράφος Έσπερος φιλοξενεί τις παράλληλες εκδηλώσεις του φεστιβάλ. \n" +
                ".Ο Έσπερος θα λειτουργήσει ως αίθουσα προβολών για το φεστιβάλ ως το 1996. Στις ξεχωριστές προβολές " +
                "συγκαταλέγεται αυτή των ταινιών της Germaine Dulac με ζωντανή μουσική, με τον Σάκη Παπαδημητρίου στο πιάνο και " +
                "τη Γεωργία Συλλαίου στο τραγούδι (28ο ΦΚΘ, 1987)", "esperos28filmfestival",
                "Ο κινηματογράφος Έσπερος στην οδό Αλεξάνδρου Σβώλου.(Αρχείο ΦΚΘ)"));
        this.db.addMilestone(new Milestone(3, "1994", "Κυρίαρχη μορφή του 35ου Φεστιβάλ Κινηματογράφου Θεσσαλονίκης αναδεικνύεται ο Ιάπωνας " +
                "δημιουργός Ναγκίσα Όσιμα, το συνολικό έργο του οποίου προβάλλεται σε αναδρομικό αφιέρωμα στον κινηματογράφο «Έσπερος» " +
                "(35ο ΦΚΘ, 1994): \n" +
                "Ο ίδιος ο Όσιμα έρχεται για λίγες μέρες στη Θεσσαλονίκη, προλογίζει ταινίες του σε κατάμεστες αίθουσες, συναντιέται" +
                " με το φίλο του Θόδωρο Αγγελόπουλο και διασκεδάζει στο κέντρο που τραγουδά η Δήμητρα Γαλάνη. ", "",
                ""));
        this.db.addMilestone(new Milestone(3, "1996", "Το 1996 για πρώτη –και μοναδική – χρονιά η έδρα των εκδηλώσεων του Φεστιβάλ " +
                "Κινηματογράφου Θεσσαλονίκης μεταφέρεται στο «Ανατόλια».\n" +
                "Μπροστά στον κινηματογράφο στήνεται μια ειδική κατασκευή, ένα μεγάλου γυάλινου κουτιού, που σηματοδοτεί " +
                "την εκεί παρουσία του φεστιβάλ.", "anatolia",
                "Ο κινηματογράφος «Ανατόλια», λίγο πριν \"αλλάξει\", για να φιλοξενήσει τις προβολές του φεστιβάλ." +
                        "(ΠΗΓΗ: 37ο ΦΚΘ, Κωδ. Φωτο. 01.000639)"));


        //TimelineStation 4
        this.db.addTimelineStation(new TimelineStation("Ολύμπιον-\n"+"Πλατεία Αριστοτέλους", 40.6326916, 22.9416844));
        //Milestones
        this.db.addMilestone(new Milestone(4, "1960", "H Διεθνής Έκθεση Θεσσαλονίκης διοργανώνει σειρά πολιτιστικών εκδηλώσεων για το σινεμά" +
                " στη Θεσσαλονίκη με αφορμή τα 25 χρόνια λειτουργίας της (1η Εβδομάς Ελληνικού Κινηματογράφου, 1960) \n" +
                "Σε συνεργασία με την κινηματογραφική λέσχη της Μακεδονικής Καλλιτεχνικής Εταιρείας «Τέχνη».\n" +
                "Η 1η Εβδομάς Ελληνικού Κινηματογράφου εγκαινιάζεται επίσημα στις 20 Σεπτεμβρίου 1960, στο κινηματοθέατρο Ολύμπιον, από " +
                "τον υπουργό Βιομηχανίας Νικόλαο Μάρτη, που έχει την εποπτεία του θεσμού.", "protievdomas1960",
                "Στιγμιότυπο από την τελετή λήξης της 1ης Εβδομάδας Ελληνικού Κινηματογράφου, με τον Πρόεδρο της ΔΕΘ Γιώργο " +
                        "Γεωργιάδη, που ανάβει το τσιγάρο της ηθοποιού Μάρως Κοντού. "));
        this.db.addMilestone(new Milestone(4, "1997", "H ιστορική κινηματογραφική αίθουσα θα ταυτιστεί με την ιστορία του Φεστιβάλ, " +
                "το οποίο θα αλλάξει αρκετές έδρες για να επιστρέψει στο ανακαινισμένο Ολύμπιον το 1997.", "olympion02",
                "Το κτιριακό συγκρότημα του Ολύμπιον στην πλατεία Αριστοτέλους."));
        this.db.addMilestone(new Milestone(4, "1997", "Η πλήρης ανακαίνιση του Ολύμπιον έγινε στο πλαίσιο της Πολιτιστικής Πρωτεύουσας " +
                "της Ευρώπη «Θεσσαλονίκη ΄97».", "olympion38filmfestival",
                "Άποψη από το εσωτερικό του ανακαινισμένου κινηματογράφου Ολύμπιον.\n" +
                        "(Αρχείο ΦΚΘ)"));
        this.db.addMilestone(new Milestone(4, "2000", "Την άνοιξη του 2000 ο Δημήτρης Εϊπίδης καθιερώνει ένα νέο πρωτότυπο Φεστιβάλ, που " +
                "έμελλε να αγαπηθεί εξίσου απο το κοινό της πόλης, το “Φεστιβάλ Ντοκιμαντέρ Θεσσαλονίκης– Εικόνες του 21ου αιώνα”. " +
                "Κάθε διοργάνωση συγκεντρώνει κατά μ.ο. 30 χιλ. θεατές στις προβολές της. ", "peopleolympion38filmfestival",
                "Πλήθος κόσμου έξω από το Ολύμπιον κατά τη διάρκεια του 38ου Φεστιβάλ Κινηματογράφου Θεσσαλονίκης.\n" +
                        "(Αρχείο ΦΚΘ)"));
        this.db.addMilestone(new Milestone(4, "2010", "Από το 2010 το Φεστιβάλ αλλάζει πορεία και επηρεασμένο και από τη σοβαρή " +
                "οικονομική κρίση της Ελλάδας στρέφεται εκ νέου σε πιο ανοιχτούς κινηματογραφικούς ορίζοντες. Στα χρόνια αυτά το " +
                "κοινό έχει την ευκαιρία να ανακαλύψει εναλλακτικούς σκηνοθέτες, όπως τους Αλέν Γκιροντί, Μπαχμάν Γκομπαντί και Όλε " +
                "Κρίστιαν Μάντσεν, αλλά και να συνδεθεί εκ νέου με διαχρονικά αγαπημένες πρωταγωνίστριες, όπως η Χάνα Σιγκούλα, και " +
                "δημιουργούς, όπως οι Άκι Καουρισμάκι, Πάολο Σορεντίνο και Κωνσταντίνος Γιάνναρης.", "p2014",
                ""));
        this.db.addMilestone(new Milestone(4, "2014", "Απο το 1997 μέχρι και σήμερα το Ολύμπιον έχει φιλοξενήσει όλες τις διοργανώσεις του " +
                "Φεστιβάλ αλλά και πλήθος εμβληματικών προσωπικοτήτων του παγκόσμιου κινηματογράφου (Κόπολα, Στοούν, Ντεσπλά, " +
                "Βερασετάκουν, Κουστουρίτσα, Κιτάνο, Χέρτζογκ, κλπ)", "olympion39filmfestival",
                "Εξωτερική άποψη του κινηματοθεάτρου Ολύμπιον στην πλατεία Αριστοτέλους."));


        //TimelineStation 5
        this.db.addTimelineStation(new TimelineStation("Λιμάνι", 40.631721, 22.93505));
        //Milestones


        this.db.addMilestone(new Milestone(5, "1994", "Οι Αποθήκες στο Λιμάνι φιλοξενούν ενδιαφέρουσες εκθέσεις όπως \n" +
                "η «Ζωγραφική στον κινηματογράφο: Γιγαντοαφίσες στην 7η Τέχνη», έκθεση κινηματογραφικής αφίσας της συλλογής Hellafi." +
                " (35ο ΦΚΘ, 1994) αλλά και η έκθεση σκηνικών και κοστουμιών των Μικέ Καραπιπέρη – Γιώργου Ζιάκα – Γιώργου Πατσά από τα " +
                "γυρίσματα των ταινιών του Θόδωρου Αγγελόπουλου. Η Αποθήκη Δ΄ φιλοξενεί έκθεση κινηματογραφικής αφίσας από ταινίες του " +
                "μεγάλου δημιουργού (41ο ΦΚΘ, 2000)",
                "apothikesinside",
                "Άποψη του εσωτερικού της Αποθήκης Γ' κατά την διάρκεια μιας Συνέντευξης Τύπου\n" +
                        "(41o ΦΚΘ, Κωδ. Φωτο 01.017108)"));
        this.db.addMilestone(new Milestone(5, "1996", "Δημοσιογράφοι επισκέπτονται τα σκηνικά της ταινίας «Μια αιωνιότητα και μια μέρα» του " +
                "Θόδωρου Αγγελόπουλου, η οποία γυρίστηκε στη Θεσσαλονίκη (37ο ΦΚΘ, 1996): \n" +
                "Λίγους μήνες αργότερα η ταινία θα κερδίσει το «Χρυσό Φοίνικα» στο Φεστιβάλ Κινηματογράφου των Καννών και έτσι" +
                " θα γίνει η πρώτη –και η μοναδική μέχρι σήμερα- ελληνική ταινία που έχει κατακτήσει τη συγκεκριμένη διάκριση.",
                "eternityandaday1",
                "Σκηνή από την πολυβραβευμένη ταινία «Μια αιωνιότητα και μια μέρα» του Θ. Αγγελόπουλου\n" +
                        "(Αρχείο ΦΚΘ, Κωδικός 01.001704)"));
        this.db.addMilestone(new Milestone(5, "2001", "Οι τέσσερις αίθουσες προβολών στο λιμάνι αποκτούν κινηματογραφικά ονόματα και " +
                "γίνονται τμήμα του Φεστιβάλ Κινηματογράφου. (42ο ΦΚΘ, 2001)\n" +
                "Βαφτίζονται «Τώνια Μαρκετάκη» και «Τζον Κασσαβέτης» (Αποθήκη Α΄) και «Φρίντα Λιάππα» και «Τάκης " +
                "Κανέλλοπουλος» (Αποθήκη Δ’). Η τελευταία μετονομάζεται την επόμενη χρονιά σε αίθουσα «Σταύρος Τορνές», προκειμένου" +
                " να αποφεύγεται η σύγχυση με την αίθουσα «Κανελλόπουλος» του παρακείμενου Μουσείου Κινηματογράφου.",
                "cineprovlita01",
                "Εξωτερική άποψη της Αποθήκης A, οι αίθουσες της οποίας μετονομάστηκε σε «Τώνια Μαρκετάκη» και «Τζον Κασσαβέτης».\n" +
                        "(40ο ΦΚΘ, Αρ. Καταγραφής 01.002826)"));
        this.db.addMilestone(new Milestone(5, "2008", "Oliver Stone και Emir Kusturica ενώνουν τις δυνάμεις τους στη συναυλία των " +
                "No Smoking Band στην Αποθήκη Γ΄ στο Λιμάνι (49ο ΦΚΘ, 2008)", "stonekusturica",
                "O Oliver Stone και ο Emir Kusturica μαζί στη σκηνή.\n" +
                        "(Motionteam, κωδικός: 445484)"));
        this.db.addMilestone(new Milestone(5, "2013", "Ένα από τα πιο αγαπημένα και πρωτότυπα τμήματα του Φεστιβάλ, οι Ματιές στα " +
                "Βαλκάνια, γιορτάζουν 20 χρόνια δημιουργικής παρουσίας. Μέσα σε αυτές τις δύο δεκαετίες η Θεσσαλονίκη με αυτό το" +
                " παράλληλο πανόραμα ταινιών προβάλει όλες τις νέες τάσεις και τη ματιά των σκηνοθετών από το σύνολο των Βαλκανίων. " +
                "Ταυτόχρονα, παρουσίασε ειδικές ρετροσπεκτίβες σε σημαντικούς δημιουργούς, όπως οι Λουτσιάν Πιντιλιέ, Γκόραν" +
                " Πασκάλιεβιτς,Νουρί Μπιλγκέ Τσεϊλάν και Κριστιάν Μουντζίου και νέα ρεύματα, όπως ο νέος ρουμάνικος κινηματογράφος, " +
                "που αγκαλιάστηκαν από το διεθνές κοινό.", "p2013",
                ""));
    }

    private void populateEnglishDB()
    {
        //add routes
        this.db.addRoute(new Route("Cinema theatres of the city","red",1));
        this.db.addRoute(new Route("Thessaloniki in Greek films","blue",1));
        //this.db.addRoute(new Route("Thessaloniki International Film Festival","green",1));

        //add stations
        //Line 1
        this.db.addStation(new Station("Vardaris Square",
                "The first film screening was held at  the“Turkey” caffé in 1897. Many cinemas have been developed around Vardaris " +
                "square: Cinema “Splendind” (which was renamed “Ilion”), “Pantheon”, “Atticon” and “Olympion (at Monastiriou Street)" +
                ", together with the porn cinemas of the square.",
                1, 40.641142,  22.934721));
        this.db.addStation(new Station("Sea front (the port)",
                "The sea front is one of the most filmed spots of the city centre." +
                        " The Cinema Museum is located inside the port, together with Thessaloniki Cinemateque, as well as with the" +
                        " four modern festival venues at the port’s warehouse.",
                1, 40.635650,  22.935431));
        this.db.addStation(new Station("Sea front (Leoforos Nikis Str.)",
                "The first cinema in the Balkans, the cinema theatre “Olympia” (1903), is situated in Leoforos Nikis Str. The sea " +
                        "front appears to be an ideal place for cinemas, like “Pathé” cinema (1911), run by the namesake film company, " +
                        "and the boat cinema “Koursal”.",
                1, 40.630440,  22.942912));
        this.db.addStation(new Station("Aristotelous Square",
                "During the 1950s, Aristotelous Square hosted 6 open air cinemas (“Rex”, “Ilissia”, “Ellinis”, “Aegean”, “Rio”" +
                        " and “Zefyros”). Today, the Olympion Theatre, located in the middle of Aristotelous Square, is the main " +
                        "venue of the Thessaloniki International Film Festival.",
                1, 40.632804,  22.941331));
        this.db.addStation(new Station("City centre (Aghias Sofias Str-Alexandrou Svolou Str.)",
                "An area with many cinema theatres, such as “Dionysia” (in Aghias Sofias Str.), “Esperos” and “Rivoli” (in " +
                        "Alexandrou Svolou Str.), Makedonikon (in Filikis Eterias Str.). The Stavrakos Film School operated its " +
                        "subsidiary film school in Thessaloniki, which was located in the same area.",
                1,  40.632511,  22.947489));
        this.db.addStation(new Station("Centre 2 (Kamara – Navarinon)",
                "Many cinema theatres at the city centre (Ilyssia, Navarinon, Elsi, Kleo, Rivoli, Thimeli, Fargani) have functioned " +
                        "both as architectural landmarks of Thessaloniki as well as venues of its everyday life.\n" +
                        "Cinema theatres, together with coffee shops and bars, have been celebrated haunts for both film lovers and " +
                        "cinema free press magazines, such as Exostis, Fix Carre, Parallaxi etc.\n" +
                        "Exostis, which was first published in 1987, has been the first free press magazine in Greece.\n" +
                        "Cinemas of the city centre have hosted various special screenings, midnight screenings and film clubs " +
                        "during the 1980’s and the 1990’s.",
                1, 40.6319738, 22.952104));
        this.db.addStation(new Station("The White Tower",
                "In 1905 the White Tower’s garden hosted an open air cinema. Opposite to it, there was the EMS building, the main " +
                        "venue of the Society of Macedonian Studies, as well as the cinema theatres “Pallas” (now operating as " +
                        "Thessaloniki State Symphony Orchestra’s concert hall) and “Alexandros”.",
                1, 40.62638,  22.948306));

        //Line 2
        this.db.addStation(new Station("The Barefoot Badallion (1955)",
                "Director: Greg Tallas\n" +
                        "\n" +
                        "Cast: Maria Kosti, Nikos Fermas, Vassilis Fraghedakis\n" +
                        "\n" +
                        "Info: Based on a true story of 160 orphans, who live in Thessaloniki under the German Occupation during " +
                        "the 1940s. The film wanders around the centre and the acropolis of Thessaloniki.",
                2, 40.6197797, 22.9638766));
        this.db.addStation(new Station("The cutie  (1961)",
                "Director: Yannis Dalianidis\n" +
                        "\n" +
                        "Cast: Dinos Iliopoulos, Zoe Laskari, Pandelis Zervos, Stefanos Stratigos\n" +
                        "\n" +
                        "Info: Two brothers try to balance between the emotional relations and the conservative principles of a" +
                        " paternal family. The film is totally shot in Thessaloniki (Panorama, Antheon area, city centre).ς",
                2, 40.632804, 22.941331));
        this.db.addStation(new Station("Some like it hot (1964)",
                "Director: Yannis Dalianidis\n" +
                        "\n" +
                        "Cast: Rena Vlachopoulou, Dinos Iliopoulos, Martha Karagianni, Elena Nathanael,Kostas Voutsas\n" +
                        "\n" +
                        "Info: During the Thessaloniki International Fair, a group of young people gets involved in funny love affairs.\n" +
                        "The film is shot in many areas of the city, such as the Thessaloniki International Fair and the White Tower.",
                2,40.628192, 22.9561));
        this.db.addStation(new Station("Parenthesis (1968)",
                "Director: Takis Kanellopoulos\n" +
                 "Cast: Alexandra Ladikou, Angelos Antonopoulos\n" +
                 "Info: A train journey with a delayed stop. A strong love affair, a parenthesis in the lives of two people. " +
                        "The film won 4 prizes in Thessaloniki Film Festival. \n" +
                 "It was shot in the sea front of Thessaloniki and the area of Aretsou (Nea Krini).",
                2,40.6223568, 22.9512486));
        this.db.addStation(new Station("The striker with number 9 (1987)",
                "Director: Pantelis Voulgaris\n" +
                        "Cast: Stratos Tzortzoglou, Themis Bazaka\n" +
                        "Info: The journey of a talented football player  goes through defeats, injuries and background, which will" +
                        " make him understand that success comes at a price.\n" +
                        "Part of the film was shot in Thessaloniki (Aristotelous Square, Evosmos).",
                2, 40.632804, 22.941331));
        this.db.addStation(new Station("Eternity and a day (1998)",
                "Director: Theo Angelopoulos\n" +
                        "Cast: Bruno Ganz, Fabrizio Bentivoglio, Isabelle Renauld\n" +
                        "\n" +
                        "Info: The film traces the final days of Alexandre, a celebrated Greek writer as he prepares to leave his " +
                        "seaside home forever. It is shot in Thessaloniki, in a villa located in Vassilisis Olgas Avenue, as well as " +
                        "in the city’s sea front, Aristotelous Square and Tsimiski Street.",
                2,40.6148604, 22.954036));
        this.db.addStation(new Station("Homeland (2010)",
                "Director: Syllas Tzoumerkas\n" +
                        "Cast: Amalia Moutoussi, Thanos Samaras, Ioanna Tsiringhouli\n" +
                        "Info: A story about a family and a country falling apart, inspired by true events. A large part of the film" +
                        " is shot in central places of Thessaloniki (Leoforos Stratou Str, the sea front, Saint Demetrius Church).",
                2,40.638799, 22.947769));
        this.db.addStation(new Station("Super Demetrios (2011)",
                "Director: Yorgos Papaioannou\n" +
                        "Cast: Dimitris Vainas, Paris Papadopoulos, Olga Sfetsa\n" +
                        "Info: In a surreal, parallel universe, Thessaloniki has its very own superhero: Super Demetrios.\n" +
                        "A film shot around the city of Thessaloniki, showcasing many of the city’s public buildings (the " +
                        "University, the City Hall, the White Tower, the Railway Station).",
                2,40.6308363, 22.9543887));

        //add movies
        this.db.addMovie(new Movie(7, "The Barefoot Badallion",
                "Based on a true story of 160 orphans, who live in Thessaloniki under the German Occupation during the 1940s. " +
                        "The film wanders around the centre and the acropolis of Thessaloniki.",
                "Maria Kosti, Nikos Fermas, Vassilis Fraghedakis",
                "Greg Tallas", "1955"));
        this.db.addMovie(new Movie(8, "The cutie",
                "Two brothers try to balance between the emotional relations and the conservative principles of a paternal family." +
                        " The film is totally shot in Thessaloniki (Panorama, Antheon area, city centre).",
                "Dinos Iliopoulos, Zoe Laskari, Pandelis Zervos, Stefanos Stratigos",
                "Yannis Dalianidis", "1961"));
        this.db.addMovie(new Movie(9, "Some like it hot",
                "During the Thessaloniki International Fair, a group of young people gets involved in funny love affairs.\n" +
                        "The film is shot in many areas of the city, such as the Thessaloniki International Fair and the White Tower.",
                "Rena Vlachopoulou, Dinos Iliopoulos, Martha Karagianni, Elena Nathanael, Kostas Voutsas",
                "Yannis Dalianidis", "1964"));
        this.db.addMovie(new Movie(10, "Parenthesis",
                "A train journey with a delayed stop. A strong love affair, a parenthesis in the lives of two people. The film won 4" +
                        " prizes in Thessaloniki Film Festival. \n" +
                        "It was shot in the sea front of Thessaloniki and the area of Aretsou (Nea Krini).",
                "Alexandra Ladikou, Angelos Antonopoulos",
                "Takis Kanellopoulos", "1968"));
        this.db.addMovie(new Movie(11, "The striker with number 9",
                "The journey of a talented football player  goes through defeats, injuries and background, which will make him " +
                        "understand that success comes at a price.\n" +
                        "Part of the film was shot in Thessaloniki (Aristotelous Square, Evosmos).",
                "Stratos Tzortzoglou, Themis Bazaka",
                "Pantelis Voulgaris", "1987"));
        this.db.addMovie(new Movie(12, "Eternity and a day",
                "The film traces the final days of Alexandre, a celebrated Greek writer as he prepares to leave his seaside home " +
                        "forever. It is shot in Thessaloniki, in a villa located in Vassilisis Olgas Avenue, as well as in the " +
                        "city’s sea front, Aristotelous Square and Tsimiski Street.",
                "Bruno Ganz, Fabrizio Bentivoglio, Isabelle Renauld",
                "Theo Angelopoulos", "1998"));
        this.db.addMovie(new Movie(13, "Homeland",
                "A story about a family and a country falling apart, inspired by true events. A large part of the film is shot in " +
                        "central places of Thessaloniki (Leoforos Stratou Str, the sea front, Saint Demetrius Church).",
                "Amalia Moutoussi, Thanos Samaras, Ioanna Tsiringhouli",
                "Syllas Tzoumerkas", "2010"));
        this.db.addMovie(new Movie(14, "Super Demetrios",
                "In a surreal, parallel universe, Thessaloniki has its very own superhero: Super Demetrios.\n" +
                        "A film shot around the city of Thessaloniki, showcasing many of the city’s public buildings (the University" +
                        ", the City Hall, the White Tower, the Railway Station).",
                "Dimitris Vainas, Paris Papadopoulos, Olga Sfetsa",
                "Yorgos Papaioannou", "2011"));

        //TimelineStation 1
        this.db.addTimelineStation(new TimelineStation("White Tower – Society of Macedonian Studies Hall (EMS)", 40.6271087, 22.9497383));
        //Milestones
        this.db.addMilestone(new Milestone(1, "1964", " The film festival’s headquarters move from the Olympion" +
                " theatre to the EMS hall, a place, which will be closely linked to the festival’s history for more" +
                " than three decades (5th Greek Film Week, 1964)","theatesems", "Viewers attend a festival screening at the EMS hall"));
        this.db.addMilestone(new Milestone(1, "1966", "Greek-American film producer James Paris, a famous festival figure during the years" +
                " 1966-1972, hires a tank to march around the White Tower’s square in order to publicize his film " +
                "“Forgotten Heores” (7th Greek Film Week, 1966). \n" +
                "The screening is full of surprises. More than 1.500 viewers that attend Paris’ film find a pack of cigarettes in" +
                " their seat, a promo gift on behalf of the producer.","paris_ems",  "Yorgos Tzavellas and James Paris attend a festival screening at the EMS hall"));
        this.db.addMilestone(new Milestone(1, "1970", "The “2nd Balcony” is a special place in EMS hall, which will be identified with the dynamic" +
                " young public of the Thessaloniki Film Festival that playσ an important role in the forming of a local festival culture" +
                " (11th Greek Film Week, 1970). \n" +
                "\n" +
                " The “2nd Balcony” is named after the homonymous seat category at the EMS hall, which is  the students’ favorite place because" +
                " of cheap entrance fees..", "exostes",
                "A view of the EMS hall and its “2nd Balcony”"));
        this.db.addMilestone(new Milestone(1,"1972", "The 1st Thessaloniki International Film Festival is held at the EMS hall in 1972," +
                " in the occasion of the 13th Greek Film Week in 1972.\n" +
                "\n" +
                "The festival’s international section concerns foreign short films and it is held a week after the official competition" +
                " of the Greek section, under the auspices of the Thessaloniki" +
                " International Fair Trade and the International Federation of Film Producers Associations (FIAPF). "
                , "diethnesfilmfestival",
                " A view of the EMS hall, during the 10th anniversary of the Thessaloniki International Film Festival for Short Films. "));
        this.db.addMilestone(new Milestone(1,"1992","Film Festival becomes officially International . Official competition hosts movies from new " +
                "and emerging creators from all over the world . \n" +
                "In parallel hosts until 1997 the Greek Film Festival .", "p1992", ""));
        this.db.addMilestone(new Milestone(1,"1995","Festival hosted for last time at the EMS hall . The building will be refurbished radically " +
                "within it from Cultural Capital Organization Thessaloniki 1997 , leaving behind a bright " +
                "cinematic history for the city . ", "p1995", ""));




        //TimelineStation 2
        this.db.addTimelineStation(new TimelineStation("Caffé “Do-re” & “Alexandros”", 40.6269345, 22.9493905));
        //Milestones

        this.db.addMilestone(new Milestone(2, "1963",
                "“Alexandros” cinema theatre is located just below the Thessaloniki Military Club and it is very close to the EMS hall," +
                        " which serves as the festival’s seat and a place for short film screenings for many years.\n"+ "\n"+
                        "Here is a sculptured marble in the honor of filmmaker Takis Kanellopoulos, whose name was particularly connected" +
                        " to the Thessaloniki Film Festival and to the caffé “Do-re”, one of the festival’s most popular haunts", "cinealexandros02",
                "A view of the “Alexandros” cinema theatre, which hosted festival screenings."));
        this.db.addMilestone(new Milestone(2, "1990", "The caffé “Do-re”, opposite the White Tower, was a popular haunt, which was frequented by " +
                "artists as well as festival viewers during the 60s and onwards, since the festival’s internationalization in the 1990s." +
                "\n" +
                "Debates between filmmakers and viewers at the caffé “Do-re” usually begin after the screenings" +
                " and carry on until dawn, adding to a festive atmosphere.", "jamesparis",
                "Greek-American film producer James Paris chats with colleagues at the caffe “Do-re” after a festival screening."));


        //TimelineStation 3
        this.db.addTimelineStation(new TimelineStation("Alexandrou Svolou Str. – Navarinon Square ", 40.6330961, 22.9470789));
        //Milestones
        this.db.addMilestone(new Milestone(3, "1987", "«Esperos” cinema theatre hosts the festival’s sidebar events. . \n" +
                ".Among the popular screenings which were held at “Esperos”, there is a film music concert of Germaine Dula’s films" +
                ", with pianist-composer Sakis Papadimitriou and singer Georgia Silleou (28th Thessaloniki Film Festival, 1987).",
                "esperos28filmfestival",
                "“Esperos” cinema theatre at Alexandrou Svolou str."));
        this.db.addMilestone(new Milestone(3, "1994", "Japanese filmmaker Nagisa Osima is the dominant figure in the 35th edition of the Thessaloniki Film Festival," +
                " which organizes a retrospective of his work in “Esperos” cinema theatre. \n" +
                "Nagisa Osima visits Thessaloniki for a few days in order to prologue his films, which are screened in crowded halls. Osima meets with his friend Theo"+
                "Angelopoulos and entertains himself at a concert with singer Dimitra Galani.", "",
                ""));
        this.db.addMilestone(new Milestone(3, "1996", "In 1996 the Thessaloniki Film Festival is held in “Anatolia” cinema theatre," +
                " for the first –and last- time in its history.\n" +
                "In front of “Anatolia” cinema theatre there is a special construction which marks the festival’s presence on the spot.", "anatolia",
                "The “Anatolia” cinema theatre before its reconstruction in order to host the festival’s screenings."));


        //TimelineStation 4
        this.db.addTimelineStation(new TimelineStation("Ολύμπιον-\n"+"Πλατεία Αριστοτέλους", 40.6326916, 22.9416844));
        //Milestones
        this.db.addMilestone(new Milestone(4, "1960", "The Thessaloniki International Trade Fair organizes a series of" +
                " film events in Thessaloniki, in the occasion of its 25th anniversary (1st Greek Film Week, 1960) " +
                "in collaboration with the Film Club of the Macedonian Society “Techne”.\n" +
                "The 1st Greek Film Week is officially inaugurated on September 20th, 1960, in the Olympion cinema theatre, by the" +
                " Minister of Industry Nikolaos Martis, who supervises the event.", "protievdomas1960",
                "A snapshot during the opening ceremony of the 1st Greek Film Week, with the president of the Thessaloniki International Trade Fair " +
                        "Yorgos Georgiadis, who lights a cigarette for actress Maro Kontou.\n "));
        this.db.addMilestone(new Milestone(4, "1997", "It is a traditional cinema theatre which will be identified with the festival’s history, that will change " +
                "many seats to finally return to the refurbished “Olympion” venue in 1997.\n", "olympion02",
                "The “Olympion” complex, located at the Aristotelous Square."));
        this.db.addMilestone(new Milestone(4, "1997", "Olympion cinema theatre was totally renovated in 1997, the year when Thessaloniki" +
                " was the Cultural Capital of Europe.", "olympion38filmfestival",
                "A view of the renovated “Olympion” cinema theatre."));
        this.db.addMilestone(new Milestone(4, "2000", "In the spring of 2000, film festival curator Dimitris Eipidis establishes a new film event," +
                " the Thessaloniki Documentary Festival" +
                " – Images of the 21st century”, which gathers an average of 30.000 viewers every year in its screenings. ", "peopleolympion38filmfestival",
                "People crowding outside the “Olympion” cinema theatre during the 38th Thessaloniki International Film Festival\n"));
        this.db.addMilestone(new Milestone(4, "2010", "From 2010 Festival changing line and influenced from the serious financial crisis of \n" +
                "Greece turned to afresh open cinematic horizons . \n" +
                "To these years the audience has the opportunity to discover alternative directors , like \n" +
                "Alain Guiraudie , Bahman Ghobadi and Ole Christian Madsen but also to connect afresh \n" +
                "with timeless favorite stars like Hanna Schygulla , and creators , like Aki Kaurismäki , \n" +
                "Paolo Sorrentino , and Constantine Giannaris .", "p2014",
                ""));
        this.db.addMilestone(new Milestone(4, "2014", "From its re-opening in 1997 until today, the “Olympion” cinema theatre hosts the main corpus" +
                " of film festival activities. It has been a popular venue, which was visited by acclaimed personalities of world cinema (Coppola, " +
                "Stone, Desplas, Kusturica, Kitano, Herzog etc).)", "olympion39filmfestival",
                "Exterior view of the “Olympion” cinema theatre at the Aristotelous Square"));


        //TimelineStation 5
        this.db.addTimelineStation(new TimelineStation("Λιμάνι", 40.631721, 22.93505));
        //Milestones


        this.db.addMilestone(new Milestone(5, "1994", "Warehouses at the city’s port host interesting art exhibitions \n" +
                "such as “Painting in film: Giant film posters”, a film poster exhibition of Hellafi collection (35th TIFF, 1994)," +
                " as well as the settings and costumes exhibitions of Mikes Karapiperis-Giorgos Ziakas-Giorgos Patsas from Theo" +
                " Angelopoulos’ film shootings. Warehouse D hosts a film poster exhibition, inspired by films of the famous Greek auteur.",
                "apothikesinside",
                "The “Hellafi” collection exhibition at the port \n" +
                        "(41o ΦΚΘ, Κωδ. Φωτο 01.017108)"));
        this.db.addMilestone(new Milestone(5, "1996", "Journalists visit the settings of Theo Angelopoulos’ film “Eternity and a day”, " +
                "which was shot in Thessaloniki (37th TIFF, 1996) \n" +
                "A few months later the film wins the “Palme d’ Or” at the Cannes Film Festival and thus becomes " +
                "the first –and the last, up until now- Greek film, which has achieved this prominent award.",
                "eternityandaday1",
                "Scene from Theo Angelopoulos’ film “Eternity and a day”"));
        this.db.addMilestone(new Milestone(5, "2001", "he four warehouses at the city’s port are renovated into cinema theatres and become a" +
                " vital part of Thessaloniki Film Festival (42nd TIFF, 2001).\n" +
                "hey are named after four prominent Greek filmmakers: “Tonia Marketaki” and “John Cassavetes” (Warehouse A’) and “Frida Liappa” " +
                "and “Takis Kanellopoulos” (Warehouse D’). The “Takis Kanellopoulos” hall is later renamed as “Stavros Tornes” hall in order to avoid" +
                " confusion with “Kanellopoulos” hall, the main venue of the neighboring Cinema Museum.",
                "cineprovlita01",
                "Exterior view of Warehouse A’, the halls of which are named “Tonia Marketaki” and “John Cassavetes” (40th TIFF)"));
        this.db.addMilestone(new Milestone(5, "2008", "Oliver Stone and Emir Kusturica join No Smoking Band’s concert at the Warehouse " +
                "C, held at the city’s port (49th TIFF, 2008).", "stonekusturica",
                "Oliver Stone and Emir Kusturica together on stage.\n" +
                        "(Motionteam, κωδικός: 445484)"));
        this.db.addMilestone(new Milestone(5, "2013", "One of all favorites and innovative sections of Festival , the Balkan Survey , celebrating 20 \n" +
                "years of creative presence . " +
                "Between these two decades Thessaloniki with this parallel panorama of films shows all new " +
                "trends and look of directors from the whole Balkan . " +
                "At once it presented special retro-spectives to important creators , like Lucian Pintilie , " +
                "Goran Paskaljevic , Nuri Bilge Ceylan , and Cristian Mungiu and new flows , like new " +
                "Romanian cinema , that embraced from the international audience .", "p2013",
                ""));


        //add photos

        //line 1
        // station 1
        this.db.addPhoto(new Photo("pantheon", 1, -1,"“Pantheon” cinema theatre in Vardaris Square, some years before its " +
                "demolition"));
        this.db.addPhoto(new Photo("splendid", 1, -1,"“Splendid” cinema theatre in a vintage postcard"));
        this.db.addPhoto(new Photo("pantheon_markiza", 1, -1,"“Pantheon” cinema Theatre in its glorious years"));
        //station 2
        this.db.addPhoto(new Photo("redmoterlimani", 2, -1,"The Red Moter of the 11th Thessaloniki Documentary Film Festival turns" +
                " his camera towards the city’s port."));
        this.db.addPhoto(new Photo("apothikia", 2, -1,"he entrance of Warehouse C venue at the city’s port."));
        this.db.addPhoto(new Photo("cinemuseuminside", 2, -1,"nterior of the Cinema Museum of Thessaloniki in the city’s port."));
        //station 3
        this.db.addPhoto(new Photo("cinemaolympia", 3, -1,"The Olympia cinema theatre (1917), where schoolgirls of the French " +
                "School were gathered before a screening."));
        this.db.addPhoto(new Photo("olympia", 3, -1,"The Olympia cinema theatre at the Thessaloniki sea front, as shown in a " +
                "postcard (beginning of the 20th century)"));
        this.db.addPhoto(new Photo("pathe", 3, -1,"The “Pathe” cinema theatre in Leoforos Nikis Str, which was ordained by the" +
                " Germans during the occupation years."));
        this.db.addPhoto(new Photo("salonica_pathe_1918", 3, -1,"The “Pathe” cinema theatre in the sea front, as shown in postcard" +
                " (beginning of the 20th century)"));
        this.db.addPhoto(new Photo("olympiaview", 3, -1,"A view of “Olympia”’s entrance in the sea front."));
        //station 4
        this.db.addPhoto(new Photo("hlysia", 4, -1,"The old cinema theatre “Ilissia” in Aristotelous, in the day of Thessaloniki’s" +
                " Liberation (1940s)"));
        this.db.addPhoto(new Photo("festivaltree02", 4, -1,"Film frames build a “cinema tree” in Aristotelous Square"));
        this.db.addPhoto(new Photo("olympionnight", 4, -1,"A night view of “Olympion” cinema theatre, the main venue of the " +
                "Thessaloniki International Film Festival."));
        this.db.addPhoto(new Photo("olympioninside", 4, -1,"Interior of “Olympion” cinema theatre"));
        //station 5
        this.db.addPhoto(new Photo("dionyssia", 5, -1,"“Dionissia” cinema theatre, which was inaugurated in 1926 (on November 26th)."));
        this.db.addPhoto(new Photo("makedonikon", 5, -1,"A night view of the “Makedonikon” cinema theatre."));
        this.db.addPhoto(new Photo("esperos", 5, -1,"A view of “Esperos” cinema theatre, which was identified with the special " +
                "screenings of the Thessaloniki International Film Festival."));
        //station 6
        this.db.addPhoto(new Photo("nauarino", 6, -1,"Exterior view of Navarinon cinema theatre during the 39th Thessaloniki International Film Festival (1998)"));
        this.db.addPhoto(new Photo("efimerida", 6, -1,"A film advertisement for a screening at Thymeli cinema theatre (Credits: Macedonia newspaper)"));
        this.db.addPhoto(new Photo("exostis", 6, -1,"Front page for the first issue of “Exostis” free press"));
        //station 7
        this.db.addPhoto(new Photo("pallaswhitetower", 7, -1,"The old cinema theatre “Pallas” near the White Tower, designed by E. Modiano, which hosted 860 viewers."));
        this.db.addPhoto(new Photo("ems", 7, -1,"Crowds outside the Society for Macedonian Studies, which was the Thessaloniki" +
                " Film Festival’s main venue during the 1970s."));
        this.db.addPhoto(new Photo("theoreiaems", 7, -1,"The balconies in the Society for Macedonian Studies with the festival’s " +
                "contestants and guests (the 1960s)."));

        //line2
        //station 8
        this.db.addPhoto(new Photo("ksypolitotagma10", 8, 1, ""));      //main
        this.db.addPhoto(new Photo("tagma", 8, 1, ""));                 //main
        this.db.addPhoto(new Photo("barefootbattalionmovieposter", 8, 1, ""));
        this.db.addPhoto(new Photo("xipolitotagma4", 8, 1, ""));
        this.db.addPhoto(new Photo("xipolitotagma8", 8, 1, ""));
        this.db.addPhoto(new Photo("greggntallas", -1, 1, ""));
        this.db.addPhoto(new Photo("kwsti_maria", -1, 1, ""));          //actor
        this.db.addPhoto(new Photo("fermas_nikos", -1, 1, ""));         //actor
        this.db.addPhoto(new Photo("fragkedakhs_vasilis", -1, 1, ""));  //actor
        //station 9
        this.db.addPhoto(new Photo("p08", 9, 2, ""));
        this.db.addPhoto(new Photo("atsidas", 9, 2, ""));
        this.db.addPhoto(new Photo("promoatsidas", 9, 2, ""));
        this.db.addPhoto(new Photo("dalianidis", -1, 2, ""));
        this.db.addPhoto(new Photo("eliopoulos", -1, 2, ""));
        this.db.addPhoto(new Photo("laskari", -1, 2, ""));
        this.db.addPhoto(new Photo("zervos", -1, 2, ""));
        this.db.addPhoto(new Photo("stratigos", -1, 2, ""));
        //station 10
        this.db.addPhoto(new Photo("katinakaiei0", 10, 3, ""));
        this.db.addPhoto(new Photo("katinakaiei4", 10, 3, ""));
        this.db.addPhoto(new Photo("katinakaieiplaz", 10, 3, ""));
        this.db.addPhoto(new Photo("dalianidis", -1, 3, ""));
        this.db.addPhoto(new Photo("vlaxopoulou", -1, 3, ""));
        this.db.addPhoto(new Photo("eliopoulos", -1, 3, ""));
        this.db.addPhoto(new Photo("karagiannh", -1, 3, ""));
        this.db.addPhoto(new Photo("nathanahl", -1, 3, ""));
        this.db.addPhoto(new Photo("voutsas", -1, 3, ""));
        //station 11
        this.db.addPhoto(new Photo("p388322", 11, 4, ""));
        this.db.addPhoto(new Photo("c72", 11, 4, ""));
        this.db.addPhoto(new Photo("kanelopoulos", -1, 4, ""));
        this.db.addPhoto(new Photo("ladikou", -1, 4, ""));
        this.db.addPhoto(new Photo("antwnopoulos", -1, 4, ""));
        //station 12
        this.db.addPhoto(new Photo("p64411", 12, 5, ""));
        this.db.addPhoto(new Photo("p64433", 12, 5, ""));
        this.db.addPhoto(new Photo("voulgaris", -1, 5, ""));
        this.db.addPhoto(new Photo("tzortzoglou", -1, 5, ""));
        this.db.addPhoto(new Photo("bazaka", -1, 5, ""));
        //station 13
        this.db.addPhoto(new Photo("eternityandaday", 13, 6, ""));
        this.db.addPhoto(new Photo("miaaioniotita", 13, 6, ""));
        this.db.addPhoto(new Photo("aggelopoulos", -1, 6, ""));
        this.db.addPhoto(new Photo("bruno", -1, 6, ""));
        this.db.addPhoto(new Photo("isarenauld", -1, 6, ""));
        this.db.addPhoto(new Photo("bentivolio", -1, 6, ""));
        //station 14
        this.db.addPhoto(new Photo("amaliamoutoussihoraproelefsis", 14, 7, ""));
        this.db.addPhoto(new Photo("christospassalisyoulaboudalifilia", 14, 7, ""));
        this.db.addPhoto(new Photo("homeland6", 14, 7, ""));
        this.db.addPhoto(new Photo("syllastzoumerkas", -1, 7, ""));
        this.db.addPhoto(new Photo("moutousi", -1, 7, ""));
        this.db.addPhoto(new Photo("samaras", -1, 7, ""));
        this.db.addPhoto(new Photo("tsirigkouli", -1, 7, ""));

        //station 15
        this.db.addPhoto(new Photo("p1334779197", 15, 8, ""));
        this.db.addPhoto(new Photo("souperdimitrios", 15, 8, ""));
        this.db.addPhoto(new Photo("superdemetriosstill", 15, 8, ""));
        this.db.addPhoto(new Photo("lefkospyrgos", 15, 8, ""));
        this.db.addPhoto(new Photo("pyrgosote", 15, 8, ""));
        this.db.addPhoto(new Photo("papaiwannou", -1, 8, ""));
        this.db.addPhoto(new Photo("vainas", -1, 8, ""));
        this.db.addPhoto(new Photo("papadopoulos", -1, 8, ""));
        this.db.addPhoto(new Photo("sfetsa", -1, 8, ""));

        //green line
        this.db.addPhoto(new Photo("green1", 0, 0, ""));
        this.db.addPhoto(new Photo("green2", 0, 0, ""));
        this.db.addPhoto(new Photo("green3", 0, 0, ""));
        this.db.addPhoto(new Photo("green4", 0, 0, ""));
        this.db.addPhoto(new Photo("green5", 0, 0, ""));
        this.db.addPhoto(new Photo("green6", 0, 0, ""));
        this.db.addPhoto(new Photo("green7", 0, 0, ""));
        this.db.addPhoto(new Photo("green8", 0, 0, ""));
        //red line


    }

    public void Unlock(int Line_id, int Station_id) {
        // if Line_id==-1 then it's a Timeline;
        //if Line_id>0 then it's a Line
        //the id represents the id of the station/TimelineStation
    }

    /**
     * Initialize the ratings of every station to 0
     */
    private void initializeRatings()
    {
        for(Station station : this.stations)
        {
            db.addRating(station.getId());
        }
    }
}
