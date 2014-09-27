package cinemetroproject.cinemetro;

import android.widget.ImageView;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

final class dbAdapter {
    private static dbAdapter ourInstance = new dbAdapter();

    private dbAdapter() {
    }

    /**
     *
     * @return instance of this class
     */
    public static dbAdapter getInstance() {
        return ourInstance;
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
     * dbHelper object to interact with the db
     */
    private dbHelper db;



    /**
     * Fills the arrays with data from the DB
     */
    private void fillArrays()

    {
        this.stations = this.db.getAllStations();
        this.routes = this.db.getAllRoutes();
        this.photos = this.db.getAllPhotos();
        this.movies = this.db.getAllMovies();
        this.users = this.db.getAllUsers();
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
    public ArrayList<Route> getRoutes() {return this.routes; }

    /**
     *
     * @return all the photos in the DB
     */
    public ArrayList<Photo> getPhotos() {return this.photos; }

    /**
     *
     * @return all the users in the DB
     */
    public ArrayList<User> getUsers() {return this.users; }

    /**
     *
     * @return all the movies in the DB
     */
    public ArrayList<Movie> getMovies() {return this.movies;}

    /**
     * fills the db if data if the tables are empty and then fill the arrays with the data from the db
     * @param db
     */
    public void setDB(dbHelper db)
    {
        this.db = db;
        this.fillArrays();
        if (this.db.isUpdated() || this.stations.isEmpty())
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
        for(Station station : this.stations)
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
        for(Photo photo : this.photos)
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
        for(Movie movie : this.movies)
        {
            if (movie.getStation_id() == station_id)
                return movie;
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
        for (User u : this.users)
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
     * @returns the id required to display the photo with that name
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
        ArrayList<String> p = new ArrayList<String>();
        for(Photo photo : this.photos)
        {
            if (photo.getMovie_id() == 0 && photo.getStation_id() == 0)
            {
                p.add(photo.getName());
            }
        }
        return p;
    }

    /**
     * Inserts the data into the tables
     */
    private void populateDB()
    {

        //add routes
        this.db.addRoute(new Route("Τα σινεμά της πόλης","χρώμα 1",1));
        this.db.addRoute(new Route("Θεσσαλονίκη μέσα απο τον ελληνικό κινηματογράφο","χρώμα 2",1));


        //add stations
        //Line 1
        this.db.addStation(new Station("Βαρδάρη",
                "Η 1η κινηματογραφική προβολή στην Θεσσαλονίκη έγινε εδώ το 1897, στο καφέ «Η Τουρκία». \n" +
                        "\n" +
                "Στην ευρύτερη περιοχή της πλατείας χτίστηκαν αρκετά σινεμά όπως: Σπλέντιντ (μετέπειτα Ίλιον), Πάνθεον, " +
                        "Αττικόν και Ολύμπιον (στη σημερινή οδό Μοναστηρίου), καθώς αργότερα και τα πορνό σινεμά της πλατείας.",
                1,
                "χρώμα 1"));
        this.db.addStation(new Station("Παραλία 1 (Λιμάνι)",
                "Μια από τις πιο κινηματογραφικές και κινηματογραφημένες γωνιές του κέντρου. \n" +
                        "\n" +
                        "Στο λιμάνι λειτουργεί σήμερα το Μουσείο Κινηματογράφου και η Ταινιοθήκη Θεσσαλονίκης, όπως και οι τέσσερις" +
                        " αίθουσες προβολών του φεστιβάλ.\n" +
                        "\n" +
                        "Ταινίες που γυρίστηκαν εδώ (ενδεικτική αναφορά): Το Λιβάδι που δακρύζει, Ταξίδι στα Κύθηρα, Τοπίο στην Ομίχλη," +
                        " Ο Κλοιός.",
                1,
                "χρώμα 1"));
        this.db.addStation(new Station("Παραλία 2 (Λεωφόρος Νίκης)",
                "Στην Λεωφόρο Νίκης δημιουργείται η πρώτη κινηματογραφική αίθουσα στα Βαλκάνια, το Ολύμπια (1903). \n" +
                        "\n" +
                        "Εδώ λειτουργεί από το 1911 ο κινηματογράφος Πατέ με ταινίες της ομώνυμης εταιρείας παραγωγής.\n" +
                        "\n" +
                        "Ταινίες που γυρίστηκαν στην Παραλία (ενδεικτική αναφορά): Η Αιωνιότητα και μια Ημερά, Ρ20, Όλα είναι δρόμος.",
                1,
                "χρώμα 1"));
        this.db.addStation(new Station("Πλατεία Αριστοτέλους",
                "Κατά τη δεκαετία του ’50 η πλατεία μετρούσε 6 θερινά σινεμά (Ρεξ, Ηλύσια, Ελληνίς, Αιγαίο, Ρίο και Ζέφυρος). \n" +
                        "\n" +
                        "Εδώ βρίσκεται και σήμερα η σημερινή έδρα του Φεστιβάλ Κινηματογράφου Θεσσαλονίκης, το κινηματοθέατρο Ολύμπιον. \n" +
                        "\n" +
                        "Μοναδικό στο είδος του επίσης το πλωτό θερινό σινεμά με την ονομασία Κουρσάλ (ή κατ΄άλλους Τζερουσαλέμ) που λειτούρησε " +
                        "μεσοπολεμικά με βάση του την Παλιά Παραλία της πόλης.\n" +
                        "\n" +
                        "Ταινίες που γυρίστηκαν εδώ (ενδεικτική αναφορά): Η Αιωνιότητα και μια Ημέρα, Φανέλα με το 9, Γεννέθλια Πόλη, Ατσίδας.",
                1,
                "χρώμα 1"));
        this.db.addStation(new Station("Κέντρο (Αγίας Σοφίας-Αλ. Σβώλου)",
                "Μια περιοχή με πολλούς κινηματογράφους: Διονύσια (Αγίας Σοφίας), Έσπερος και Ριβολί (Αλ.Σβώλου), Μακεδονικόν (Φιλ.Εταιρείας) . \n" +
                        "\n" +
                        "Εδώ λειτούργησε στη δεκαετία του ’50 παράρτημα Θεσσαλονίκης της κινηματογραφικής σχολής Σταυράκου. \n" +
                        "\n" +
                        "Κατά την δεκαετία του '80 τα βιντεοκλαμπ στο κέντρο αλλά και στις γειτονίες της πόλης αποτέλεσαν εναλλακτικό σημείο συνάντησης των σινεφιλ. \n" +
                        "\n" +
                        "Στην πορεία του χρόνου η βιντεοκασσέτα αντικαταστάθηκε απο το dvd και το blue-ray. Στην ίδια περιοχή " +
                        "(Οδός Α. Σβώλου) διατηρούνται ακόμη δύο απο τα -κάποτε αμέτρητα – βιντεο/dvd club της πόλης (AZA και Seven Film Gallery). ",
                1,
                "χρώμα 1"));
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
                1,
                "χρώμα 1"));
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
                1,
                "χρώμα 1"));

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
                2,
                "χρώμα 2"));
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
                2,
                "χρώμα 2"));
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
                2,
                "χρώμα 2"));
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
                2,
                "χρώμα 2"));
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
                2,
                "χρώμα 2"));
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
                2,
                "χρώμα 2"));
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
                2,
                "χρώμα 2"));
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
                2,
                "χρώμα 2"));

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
        this.db.addPhoto(new Photo("pantheon", 1, -1,"Ο κινηματογράφος Πάνθεον στο Βαρδάρι, λίγο πριν την κατεδάφισή του\n" +
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
        //station 7
        this.db.addPhoto(new Photo("ksypolitotagma10", 7, 1, ""));      //main
        this.db.addPhoto(new Photo("tagma", 7, 1, ""));                 //main
        this.db.addPhoto(new Photo("barefootbattalionmovieposter", 7, 1, ""));
        this.db.addPhoto(new Photo("xipolitotagma4", 7, 1, ""));
        this.db.addPhoto(new Photo("xipolitotagma8", 7, 1, ""));
        this.db.addPhoto(new Photo("kwsti_maria", -1, 1, ""));          //actor
        this.db.addPhoto(new Photo("fermas_nikos", -1, 1, ""));         //actor
        this.db.addPhoto(new Photo("fragkedakhs_vasilis", -1, 1, ""));  //actor
        //station 8
        this.db.addPhoto(new Photo("p08", 8, 2, ""));
        this.db.addPhoto(new Photo("atsidas", 8, 2, ""));
        this.db.addPhoto(new Photo("promoatsidas", 8, 2, ""));
        this.db.addPhoto(new Photo("eliopoulos", -1, 2, ""));
        this.db.addPhoto(new Photo("laskari", -1, 2, ""));
        this.db.addPhoto(new Photo("zervos", -1, 2, ""));
        this.db.addPhoto(new Photo("stratigos", -1, 2, ""));
        //station 9
        this.db.addPhoto(new Photo("katinakaiei0", 9, 3, ""));
        this.db.addPhoto(new Photo("katinakaiei4", 9, 3, ""));
        this.db.addPhoto(new Photo("katinakaieiplaz", 9, 3, ""));
        this.db.addPhoto(new Photo("vlaxopoulou", -1, 3, ""));
        this.db.addPhoto(new Photo("eliopoulos", -1, 3, ""));
        this.db.addPhoto(new Photo("karagiannh", -1, 3, ""));
        this.db.addPhoto(new Photo("nathanahl", -1, 3, ""));
        this.db.addPhoto(new Photo("voutsas", -1, 3, ""));
        //station 10
        this.db.addPhoto(new Photo("p388322", 10, 4, ""));
        this.db.addPhoto(new Photo("c72", 10, 4, ""));
        this.db.addPhoto(new Photo("ladikou", -1, 4, ""));
        this.db.addPhoto(new Photo("antwnopoulos", -1, 4, ""));
        //station 11
        this.db.addPhoto(new Photo("p64411", 11, 5, ""));
        this.db.addPhoto(new Photo("p64433", 11, 5, ""));
        this.db.addPhoto(new Photo("tzortzoglou", -1, 5, ""));
        this.db.addPhoto(new Photo("bazaka", -1, 5, ""));
        //station 12
        this.db.addPhoto(new Photo("eternityandaday", 12, 6, ""));
        this.db.addPhoto(new Photo("miaaioniotita", 12, 6, ""));
        this.db.addPhoto(new Photo("bruno", -1, 6, ""));
        this.db.addPhoto(new Photo("izampel", -1, 6, ""));
        this.db.addPhoto(new Photo("bentivolio", -1, 6, ""));
        //station 13
        this.db.addPhoto(new Photo("amaliamoutoussihoraproelefsis", 13, 7, ""));
        this.db.addPhoto(new Photo("christospassalisyoulaboudalifilia", 13, 7, ""));
        this.db.addPhoto(new Photo("homeland6", 13, 7, ""));
        this.db.addPhoto(new Photo("moutousi", -1, 7, ""));
        this.db.addPhoto(new Photo("samaras", -1, 7, ""));
        this.db.addPhoto(new Photo("tsirigkouli", -1, 7, ""));

        //station 14
        this.db.addPhoto(new Photo("p1334779197", 14, 8, ""));
        this.db.addPhoto(new Photo("souperdimitrios", 14, 8, ""));
        this.db.addPhoto(new Photo("superdemetriosstill", 14, 8, ""));
        this.db.addPhoto(new Photo("ktiriedron", 14, 8, ""));
        this.db.addPhoto(new Photo("lefkospyrgosI", 14, 8, ""));
        this.db.addPhoto(new Photo("pyrgosote", 14, 8, ""));
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
}
