package cinemetroproject.cinemetro;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.util.Formatter;

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

    public User( String name, String pass)
    {
        this.username = name;
        this.password = this.encryptPassword(pass);
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

    public String getPassword() {return this.password; }

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
        if (this.encryptPassword(oldpass).equals(this.password)) {
            this.password = this.encryptPassword(pass);
        }
    }

    public void setPassword(String pass)
    {
        this.password = pass;
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
        String hash = encryptPassword(pass);
        return hash.equals(this.password);
    }

    private static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}