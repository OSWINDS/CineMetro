package cinemetroproject.cinemetro;


import java.util.ArrayList;

public class Movie {

    private int id;
    private int station_id;
    private String title;
    private String description;
    private ArrayList<String> actors;
    private String director;
    private String year;

    public Movie(){}

    public Movie(int station_id, String title, String description, String actors, String director, String year)
    {
        this.station_id = station_id;
        this.title = title;
        this.description = description;
        this.director = director;
        this.year = year;
        this.setActors(actors);
    }

    /***************************************************************
     * Get Functions
     ***************************************************************/

    /**
     *
     * @return id of the movie
     */
    public int getId(){return this.id;}

    /**
     *
     * @return station id of the movie
     */
    public int getStation_id(){return this.station_id;}

    /**
     *
     * @return title of the movie
     */
    public String getTitle(){return this.title;}

    /**
     *
     * @return description of the movie
     */
    public String getDescription(){return this.description;}

    /**
     *
     * @return director of the movie
     */
    public String getDirector(){return this.director;}

    /**
     *
     * @return year of the movie
     */
    public String getYear(){return this.year;}

    /**
     *
     * @return list of actors of the movie
     */
    public ArrayList<String> getActors(){return this.actors;}

    /***************************************************************
     * Set Functions
     ***************************************************************/

    /**
     * Set id of the movie
     * @param id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Set station id of the movie
     * @param station_id
     */
    public void setStation_id(int station_id)
    {
        this.station_id = station_id;
    }

    /**
     * Set title of the movie
     * @param title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Set description of the movie
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Set list of actors
     * @param actors
     */
    public void setActors(String actors)
    {
        String[]  split_string = actors.split(",");
        this.actors = new ArrayList<String>();
        for (int i=0; i<split_string.length; i++)
        {
            this.actors.add(split_string[i]);
        }
    }

    /**
     * Set director of the movie
     * @param director
     */
    public void setDirector(String director)
    {
        this.director = director;
    }

    /**
     * Set year of the movie
     * @param year
     */
    public void setYear(String year)
    {
        this.year = year;
    }
}
