package cinemetroproject.cinemetro;

import java.lang.reflect.Field;
import java.util.ArrayList;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.FindCallback;
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
        this.fillArrays();
        if (this.db.isUpdated() || stations.isEmpty())
        {
            this.populateDB();
            this.db.setUpdated(false);
        }
        this.fillArrays();
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
     * returns -1 if the user has not voted for this station
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
     * Updates the user ratings to the parse online database
     */
    private void updateUserToParse(final User user)
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username", user.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> userList, ParseException e) {
                if (e == null) {
                    //user exists,update his profile

                } else {
                    //create a new user with this username
                    ParseObject parse_user = new ParseObject("User");
                    parse_user.put("username", user.getUsername());
                    parse_user.put("password", user.getPassword());
                    parse_user.put("redLine", 0);
                    parse_user.put("blueLine", 0);
                    parse_user.put("greenLine", 0);
                    parse_user.put("totalPoints", 0);

                    int[] redLineStations = new int[getStationByRoute(0).size()];
                    for (int i = 0; i < getStationByRoute(0).size(); i++) {
                        redLineStations[i] = 0;
                    }
                    int[] blueLineStations = new int[getStationByRoute(1).size()];
                    for (int i = 0; i < getStationByRoute(1).size(); i++) {
                        blueLineStations[i] = 0;
                    }
                    int[] greenLineStations = new int[getStationByRoute(2).size()];
                    for (int i = 0; i < getStationByRoute(2).size(); i++) {
                        greenLineStations[i] = 0;
                    }

                    parse_user.put("redLineStations", redLineStations);
                    parse_user.put("greenLineStations", greenLineStations);
                    parse_user.put("blueLineStations", blueLineStations);
                }
            }
        });
    }

    /**
     * Inserts the data into the tables
     */
    private void populateDB()
    {

        //add routes
        this.db.addRoute(new Route("Τα σινεμά της πόλης","red",1));
        this.db.addRoute(new Route("Θεσσαλονίκη μέσα απο τον ελληνικό κινηματογράφο","blue",1));
        this.db.addRoute(new Route("Timeline","green",1));


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
                        "Ταινίες που γυρίστηκαν στην Παραλία (ενδεικτική αναφορά): Η Αιωνιότητα και μια Ημερά, Ρ20, Όλα είναι δρόμος.",
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
        this.db.addStation(new Station("Κέντρο (Αγίας Σοφίας-Αλ. Σβώλου)",
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
        this.db.addStation(new Station("Η Αιωνιότητα και μια μέρα (1998)",
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
        this.db.addMovie(new Movie(12, "Η Αιωνιότητα και μια μέρα",
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
        //red line


        //TimelineStation 1
        this.db.addTimelineStation(new TimelineStation("Λευκός Πύργος-Εταιρεία Μακεδονικών Σπουδών (ΕΜΣ)", 40.6271087, 22.9497383));
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
        this.db.addTimelineStation(new TimelineStation("Σβώλου – Πλ. Ναυαρίνο (Έσπερος-Ανατόλια-Μακεδονικών – Ναυαρίνο – Βακούρα)", 40.6330961, 22.9470789));
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
        this.db.addTimelineStation(new TimelineStation("Ολύμπιον-Πλατεία Αριστοτέλους", 40.6326916, 22.9416844));
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
        this.db.addMilestone(new Milestone(4, "2014", "Απο το 1997 μέχρι και σήμερα το Ολύμπιον έχει φιλοξενήσει όλες τις διοργανώσεις του " +
                "Φεστιβάλ αλλά και πλήθος εμβληματικών προσωπικοτήτων του παγκόσμιου κινηματογράφου (Κόπολα, Στοούν, Ντεσπλά, " +
                "Βερασετάκουν, Κουστουρίτσα, Κιτάνο, Χέρτζογκ, κλπ)", "olympion39filmfestival",
                "Εξωτερική άποψη του κινηματοθεάτρου Ολύμπιον στην πλατεία Αριστοτέλους."));
        this.db.addMilestone(new Milestone(4, "2014", "Από το 2010 το Φεστιβάλ αλλάζει πορεία και επηρεασμένο και από τη σοβαρή " +
                "οικονομική κρίση της Ελλάδας στρέφεται εκ νέου σε πιο ανοιχτούς κινηματογραφικούς ορίζοντες. Στα χρόνια αυτά το " +
                "κοινό έχει την ευκαιρία να ανακαλύψει εναλλακτικούς σκηνοθέτες, όπως τους Αλέν Γκιροντί, Μπαχμάν Γκομπαντί και Όλε " +
                "Κρίστιαν Μάντσεν, αλλά και να συνδεθεί εκ νέου με διαχρονικά αγαπημένες πρωταγωνίστριες, όπως η Χάνα Σιγκούλα, και " +
                "δημιουργούς, όπως οι Άκι Καουρισμάκι, Πάολο Σορεντίνο και Κωνσταντίνος Γιάνναρης.", "p2014",
                ""));

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
        this.db.addMilestone(new Milestone(5, "2008", "Ένα από τα πιο αγαπημένα και πρωτότυπα τμήματα του Φεστιβάλ, οι Ματιές στα " +
                "Βαλκάνια, γιορτάζουν 20 χρόνια δημιουργικής παρουσίας. Μέσα σε αυτές τις δύο δεκαετίες η Θεσσαλονίκη με αυτό το" +
                " παράλληλο πανόραμα ταινιών προβάλει όλες τις νέες τάσεις και τη ματιά των σκηνοθετών από το σύνολο των Βαλκανίων. " +
                "Ταυτόχρονα, παρουσίασε ειδικές ρετροσπεκτίβες σε σημαντικούς δημιουργούς, όπως οι Λουτσιάν Πιντιλιέ, Γκόραν" +
                " Πασκάλιεβιτς,Νουρί Μπιλγκέ Τσεϊλάν και Κριστιάν Μουντζίου και νέα ρεύματα, όπως ο νέος ρουμάνικος κινηματογράφος, " +
                "που αγκαλιάστηκαν από το διεθνές κοινό.", "p2013",
                ""));
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
