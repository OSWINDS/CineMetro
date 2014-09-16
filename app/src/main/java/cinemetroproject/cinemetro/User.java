package cinemetroproject.cinemetro;

public class User {

    private int id;
    private String username;
    private String password;

    //achievements

    //constructors
    public User(){}
    public User(int id, String name, String pass)
    {
        this.id = id;
        this.username = name;
        this.password = pass;
    }

    /***************************************************************
     * Get Functions
     ***************************************************************/
    public int getId()
    {
        return  this.id;
    }

    public String getUsername()
    {
        return this.username;
    }

    /***************************************************************
     * Set Functions
     ***************************************************************/
    public void setId(int id)
    {
        this.id = id;
    }

    public void setUsername(String name)
    {
        this.username = name;
    }

    private void setPassword(String oldpass, String pass)
    {
        if (oldpass.equals(this.password)) {
            this.password = pass;
        }
    }

    /***************************************************************
     * Other Functions
     ***************************************************************/

    /**
     *
     * @param pass
     * @return true if the parameter pass is equal to the password of the user
     */
    public boolean checkPassword(String pass)
    {
        return pass.equals(this.password);
    }
}
