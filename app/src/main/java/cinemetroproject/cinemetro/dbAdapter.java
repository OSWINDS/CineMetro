package cinemetroproject.cinemetro;

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
        System.out.println(this.stations.size());
        System.out.println(this.routes.size());
        System.out.println(this.movies.size());
        System.out.println(this.photos.size());

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
     * Inserts the data into the tables
     */
    private void populateDB()
    {

        //add routes
        this.db.addRoute(new Route("Τα σινεμά της πόλης","χρώμα 1",1));
        this.db.addRoute(new Route("Θεσσαλονίκη μέσα απο τον ελληνικό κινηματογράφο","χρώμα 2",1));


        //add stations
        this.db.addStation(new Station("Βαρδάρη",
                "Η 1η κινηματογραφική προβολή έγινε το 1897 στο καφέ «Η Τουρκία». Εδώ χτίστηκαν τα σινεμά Σπλέντιντ (μετέπειτα Ίλιον)," +
                        " Πάνθεον, Αττικόν και Ολύμπιον (στη σημερινή οδό Μοναστηρίου), καθώς και τα πορνό σινεμά της πλατείας.",
                1,
                "χρώμα 1"));
        this.db.addStation(new Station("Παραλία 1 (Λιμάνι)",
                "Μια από τις πιο κινηματογραφικές και κινηματογραφημένες γωνιές του κέντρου. Στο λιμάνι λειτουργεί σήμερα το " +
                        "Μουσείο Κινηματογράφου και η Ταινιοθήκη Θεσσαλονίκης, όπως και οι τέσσερις αίθουσες προβολών του φεστιβάλ.",
                1,
                "χρώμα 1"));
        this.db.addStation(new Station("Παραλία 2 (Λεωφόρος Νίκης)",
                "Στην Λεωφόρο Νίκης δημιουργείται η πρώτη κινηματογραφική αίθουσα στα Βαλκάνια, το Ολύμπια (1903). Εδώ λειτουργεί " +
                        "από το 1911 ο κινηματογράφος Πατέ με ταινίες της ομώνυμης εταιρείας παραγωγής και ο πλωτός κινηματογράφος Κουρσάλ.",
                1,
                "χρώμα 1"));
        this.db.addStation(new Station("Πλατεία Αριστοτέλους",
                "Στη δεκαετία του ’50 η πλατεία μετρούσε 6 θερινά σινεμά (Ρεξ, Ηλύσια, Ελληνίς, Αιγαίο, Ρίο και Ζέφυρος). Εδώ βρίσκεται" +
                        " και σήμερα η σημερινή έδρα του Φεστιβάλ Κινηματογράφου Θεσσαλονίκης, το κινηματοθέατρο Ολύμπιον.",
                1,
                "χρώμα 1"));
        this.db.addStation(new Station("Κέντρο (Αγίας Σοφίας-Αλ. Σβώλου)",
                "Μια περιοχή με πολλούς κινηματογράφους: Διονύσια (Αγίας Σοφίας), Έσπερος και Ριβολί (Αλ.Σβώλου), Μακεδονικόν " +
                        "(Φιλ.Εταιρείας) . Εδώ λειτούργησε στη δεκαετία του ’50 παράρτημα Θεσσαλονίκης της κινηματογραφικής σχολής Σταυράκου.",
                1,
                "χρώμα 1"));
        this.db.addStation(new Station("Λευκός Πύργος",
                "Από το 1905 ο κήπος του Λευκού Πύργου φιλοξενούσε αίθουσα κινηματογράφου. Εδώ βρίσκεται η Εταιρεία Μακεδονικών Σπουδών" +
                        ", παλιά έδρα του Φεστιβάλ Κινηματογράφου Θεσσαλονίκης και τα σινεμά Παλλάς (σημερινή έδρα της ΚΟΘ) και Αλέξανδρος.",
                1,
                "χρώμα 1"));
        this.db.addStation(new Station("Το ξυπόλητο τάγμα (1955)",
                "Σκηνοθεσία: Γκρεγκ Τάλλας\n" +
                        "\n" +
                        "Παίζουν: Κωστή Μαρία, Φερμας Νίκος, Φραγκεδάκης Βασίλης\n" +
                        "\n" +
                        "Info: Η αληθινή ιστορία επιβίωσης 160 παιδιών στην Θεσσαλονίκη της Κατοχής. \n" +
                        "Η ταινία περιδιαβαίνει την Θεσσαλονίκη της δεκαετίας του '50 (Κέντρο και Άνω Πόλη).",
                2,
                "χρώμα 2"));
        this.db.addStation(new Station("Ο ατσίδας (1961)",
                "Σκηνοθεσία: Γιάννης Δαλιανίδης\n" +
                        "\n" +
                        "Παίζουν: Ηλιόπουλος Ντίνος, Λάσκαρη Ζώη, Ζερβός Παντελής, Στρατηγός Στέφανος\n" +
                        "\n" +
                        "Info: Δύο αδέρφια προσπαθούν να ισορροπήσουν ανάμεσα στις αισθηματικές τους σχέσεις και την συντηρητικές αρχές της πατρικής οικίας τους. \n" +
                        "Εξ ολοκλήρου γυρισμένη στην Θεσσαλονίκη: Πανόραμα, περιοχή Ανθέων, Κέντρο Θεσσαλονίκης ",
                2,
                "χρώμα 2"));
        this.db.addStation(new Station("Κάτι να καίει (1964)",
                "Σκηνοθεσία: Γιάννης Δαλιανίδης\n" +
                        "\n" +
                        "Παίζουν: Βλαχοπούλου Ρένα, Ηλιόπουλος Ντίνος, Καραγιάννη Μάρθα, Ναθαναήλ Έλενα, Βουτσάς Κώστας\n" +
                        "\n" +
                        "Info: Κατά την διάρκεια της ΔΕΘ μια παρέα νέων ανθρώπων περιπλέκεται σε αστείες και ερωτικές περιπέτειες. \n" +
                        "Η ταινία έχει γυρίσματα σε χώρους όπως ΔΕΘ, Λευκός Πύργος, κ.α.",
                2,
                "χρώμα 2"));
        this.db.addStation(new Station("Παρένθεση (1968)",
                "Σκηνοθεσία: Τάκης Κανελλόπουλος\n" +
                        "\n" +
                        "Παίζουν: Λαδικού Αλεξάνδρα, Αντωνόπουλος Άγγελος\n" +
                        "\n" +
                        "Info: Ένα ταξίδι με τρένο, μια στάση με ολιγόωρη καθυστέρηση, ένας δυνατός έρωτας δύο ανθρώπων, μια παρένθεση. 4 βραβεία στο Φεστιβάλ Θεσσαλονίκης. \n" +
                        "Η ταινία γυρίστηκε στην νέα παραλία Θεσσαλονίκης και στην περιοχή Αρετσού (Ν. Κρήνη)",
                2,
                "χρώμα 2"));
        this.db.addStation(new Station("Η Φανέλλα με το Νο9 (1987)",
                "Σκηνοθεσία: Παντελής Βούλγαρης\n" +
                        "\n" +
                        "Παίζουν: Τζώρτζογλου Στράτος, Μπαζάκα Θέμις\n" +
                        "\n" +
                        "Info: Η πορεία ενός ταλαντούχου ποδοσφαιριστή, ο οποίος μέσα από νίκες και ήττες, τραυματισμούς και παρασκήνιο, θα καταλάβει ότι η επιτυχία έχει το τίμημά της. \n" +
                        "Μέρος της ταινίας έχει γυριστεί στην Θεσσαλονίκη: Πλ. Αριστοτέλους, Εύοσμος.",
                2,
                "χρώμα 2"));
        this.db.addStation(new Station("Η Αιωνιότητα και μια μέρα (1998)",
                "Σκηνοθεσία: Θόδωρος Αγγελόπουλος\n" +
                        "\n" +
                        "Παίζουν: Γκαντζ Μπρούνο, Ρενό Ιζαμπέλ, Μπενιβόλιο Φαμπρίτσιο\n" +
                        "\n" +
                        "Info: Ένας μεσήλικας συγγραφέας, που ασχολείται με το έργο του Δ. Σολωμού, αναζητάει τις αναμνήσεις μιας ζωής" +
                        " σε μια περιπλάνηση μιας ημέρας. Γυρισμένη σε αρχοντικό της Βασ. Όλγας, Παλιά Παραλία, Πλ. Αριστοτέλους και " +
                        "Τσιμισκή. ",
                2,
                "χρώμα 2"));
        this.db.addStation(new Station("Χώρα Προέλευσης (2010)",
                "Σκηνοθεσία: Σύλλας Τζουμέρκας\n" +
                        "\n" +
                        "Παίζουν: Μουτούση Αμαλία, Σαμαράς Θάνος, Τσιριγκούλη Ιωάννα\n" +
                        "\n" +
                        "Info: Οικογενειακό δράμα ως ακτινογραφία της νεοελληνικής κοινωνικής πραγματικότητας. \n" +
                        "Η ταινία πραγματοποιεί μεγάλο μέρος της δράσης της σε γνωστά σημεία της σημερινής Θεσσαλονίκης: Λεωφόρος " +
                        "Στρατού (ΕΡΤ3), Νέα Παραλία, Αγίος Δημήτριος. ",
                2,
                "χρώμα 2"));
        this.db.addStation(new Station("Σούπερ Δημήτριος (2011)",
                "Σκηνοθεσία: Σύλλας Τζουμέρκας\n" +
                        "\n" +
                        "Παίζουν: Μουτούση Αμαλία, Σαμαράς Θάνος, Τσιριγκούλη Ιωάννα\n" +
                        "\n" +
                        "Info: Οικογενειακό δράμα ως ακτινογραφία της νεοελληνικής κοινωνικής πραγματικότητας. \n" +
                        "Η ταινία πραγματοποιεί μεγάλο μέρος της δράσης της σε γνωστά σημεία της σημερινής Θεσσαλονίκης: Λεωφόρος " +
                        "Στρατού (ΕΡΤ3), Νέα Παραλία, Αγίος Δημήτριος. ",
                2,
                "χρώμα 2"));

        //add movies
        this.db.addMovie(new Movie(7, "Το ξυπόλητο τάγμα", "Η αληθινή ιστορία επιβίωσης 160 παιδιών στην Θεσσαλονίκη της Κατοχής. \n" +
                "Η ταινία περιδιαβαίνει την Θεσσαλονίκη της δεκαετίας του '50 (Κέντρο και Άνω Πόλη).", "Κωστή Μαρία,Φερμας Νίκος,Φραγκεδάκης Βασίλης",
                "Γκρεγκ Τάλλας", "1955"));
        this.db.addMovie(new Movie(8, "Ο ατσίδας", "Δύο αδέρφια προσπαθούν να ισορροπήσουν ανάμεσα στις αισθηματικές τους σχέσεις και την συντηρητικές αρχές της πατρικής οικίας τους. \n" +
                "Εξ ολοκλήρου γυρισμένη στην Θεσσαλονίκη: Πανόραμα, περιοχή Ανθέων, Κέντρο Θεσσαλονίκης ",
                "Ηλιόπουλος Ντίνος,Λάσκαρη Ζώη,Ζερβός Παντελής,Στρατηγός Στέφανος",
                "Γιάννης Δαλιανίδης", "1961"));
        this.db.addMovie(new Movie(9, "Κάτι να καίει", "Κατά την διάρκεια της ΔΕΘ μια παρέα νέων ανθρώπων περιπλέκεται σε αστείες και ερωτικές περιπέτειες. \n" +
                "Η ταινία έχει γυρίσματα σε χώρους όπως ΔΕΘ, Λευκός Πύργος, κ.α.",
                "Βλαχοπούλου Ρένα,Ηλιόπουλος Ντίνος,Καραγιάννη Μάρθα,Ναθαναήλ Έλενα,Βουτσάς Κώστας",
                "Γιάννης Δαλιανίδης", "1964"));
        this.db.addMovie(new Movie(10, "Παρένθεση", "Ένα ταξίδι με τρένο, μια στάση με ολιγόωρη καθυστέρηση, ένας δυνατός έρωτας δύο ανθρώπων, μια παρένθεση. 4 βραβεία στο Φεστιβάλ Θεσσαλονίκης. \n" +
                "Η ταινία γυρίστηκε στην νέα παραλία Θεσσαλονίκης και στην περιοχή Αρετσού (Ν. Κρήνη)",
                "Λαδικού Αλεξάνδρα,Αντωνόπουλος Άγγελος",
                "Τάκης Κανελλόπουλος", "1968"));
        this.db.addMovie(new Movie(11, "Η Φανέλλα με το Νο9", "Η πορεία ενός ταλαντούχου ποδοσφαιριστή, ο οποίος μέσα από νίκες και ήττες, τραυματισμούς και παρασκήνιο, θα καταλάβει ότι η επιτυχία έχει το τίμημά της. \n" +
                "Μέρος της ταινίας έχει γυριστεί στην Θεσσαλονίκη: Πλ. Αριστοτέλους, Εύοσμος.",
                "Τζώρτζογλου Στράτος,Μπαζάκα Θέμις",
                "Παντελής Βούλγαρης", "1987"));
        this.db.addMovie(new Movie(12, "Η Αιωνιότητα και μια μέρα", "Ένας μεσήλικας συγγραφέας, που ασχολείται με το έργο του Δ. Σολωμού, αναζητάει τις αναμνήσεις μιας ζωής σε μια περιπλάνηση μιας ημέρας.\n" +
                "Γυρισμένη σε αρχοντικό της Βασ. Όλγας, Παλιά Παραλία, Πλ. Αριστοτέλους και Τσιμισκή.",
                "Γκαντζ Μπρούνο,Ρενό Ιζαμπέλ,Μπενιβόλιο Φαμπρίτσιο",
                "Θόδωρος Αγγελόπουλος", "1998"));
        this.db.addMovie(new Movie(13, "Χώρα Προέλευσης", "Οικογενειακό δράμα ως ακτινογραφία της νεοελληνικής κοινωνικής πραγματικότητας. \n" +
                "Η ταινία πραγματοποιεί μεγάλο μέρος της δράσης της σε γνωστά σημεία της σημερινής Θεσσαλονίκης: Λεωφόρος Στρατού (ΕΡΤ3), Νέα Παραλία, Αγίος Δημήτριος.",
                "Μουτούση Αμαλία,Σαμαράς Θάνος,Τσιριγκούλη Ιωάννα",
                "Σύλλας Τζουμέρκας", "2010"));
        this.db.addMovie(new Movie(14, "Σούπερ Δημήτριος", "Σάτιρα θεσσαλονικιώτικων και βορειοελλαδικών κλισέ με πολύ χιούμορ. \n" +
                "Τόποι γυρισμάτων: Θεολογική Σχολή ΑΠΘ, Νέο Δημαρχειακό Μέγαρο, Λευκός Πύργος, ΟΣΕ, Τούμπα ",
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
        this.db.addPhoto(new Photo("pallaswhitetower", 6, -1,"Το παλιό κινηματοθέατρο Παλλάς κοντά στο Λευκό Πύργο, με αρχιτεκτονικό σχέδιο Ε. Μοδιάνο και 860 πολυτελείς θέσεις.\n" +
                "Αρχείο Ν.Θεοδοσίου – Ίντερνετ."));
        this.db.addPhoto(new Photo("ems", 6, -1,"Κόσμος έξω από την Εταιρεία Μακεδονικών Σπουδών, που υπήρξε έδρα του Φεστιβάλ Κινηματογράφου στη δεκαετία του ’70.\n" +
                "Αρχείο ΦΚΘ"));
        this.db.addPhoto(new Photo("theoreiaems", 6, -1,"Τα θεωρεία της Εταιρείας Μακεδονικών Σπουδών με τους διαγωνιζόμενους και τους καλεσμένους του φεστιβάλ (δεκαετία ’60) \n" +
                "Αρχείο ΦΚΘ"));
        //line2
        //station 7
        this.db.addPhoto(new Photo("ksypolitotagma10", 7, 1, ""));      //main
        this.db.addPhoto(new Photo("tagma", 7, 1, ""));                 //main
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
        this.db.addPhoto(new Photo("vainas", -1, 8, ""));
        this.db.addPhoto(new Photo("papadopoulos", -1, 8, ""));
        this.db.addPhoto(new Photo("sfetsa", -1, 8, ""));
    }
}
