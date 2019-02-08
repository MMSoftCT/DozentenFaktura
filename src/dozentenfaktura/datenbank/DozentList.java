/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura.datenbank;

import java.sql.*;
import java.util.ArrayList;

/**
 * spezial ArrayList for Dozent with sql interface
 * @author amederake
 */
public class DozentList extends ArrayList<Dozent> implements Isql
{

    private int lastIdx;
    private String dbName;

    public DozentList()
    {
        super();
        lastIdx = -1;
        dbName = "";
    }
    
    public Dozent getDozentById(int id)
    {
        for(Dozent d : this)
        {
            if(d.getId() == id)
            {
                return d;
            }
        }
        return null;
    }
    
    /**
     * override add methode
     * @param doz dozent object
     * @return true if succsessfull
     */
    @Override
    public boolean add(Dozent doz)
    {
	boolean ret = false;
	if(super.add(doz))
	{
	    ret = insert(this.indexOf(doz));
	}
	return ret;
    }
    
    /**
     * override set methode
     * @param index index in array
     * @param doz doznt object
     * @return old dozent obect at index
     */
    @Override
    public Dozent set(int index, Dozent doz)
    {
	Dozent ret = super.set(index , doz);
	update(index);
	return ret;
    }

    /**
     * get a list of dozent first- and lastname
     * @return list of first- and lastname
     */
    public ArrayList<String> getDozentenNames()
    {
	ArrayList<String> ret = new ArrayList<>();
	
	this.forEach((aThi) ->
	{
	    ret.add(aThi.getVorname() + " " + aThi.getNachname());
	});
	return ret;
    }

    /**
     * get the last index in database
     * @return index
     */
    public int getIndex()
    {
	return lastIdx;
    }

    /**
     * set path to database
     *
     * @param db path to database
     */
    public void setDB(String db)
    {
        dbName = db;
    }

    @Override
    public Connection connect(String url)
    {
        Connection conn = null;
        // set dbName for furture use
        if (dbName.equals("") && !url.equals(""))
        {
            dbName = url;
        }

        if (!dbName.equals(""))
        {
            String conUrl = "jdbc:sqlite:" + dbName;
            try
            {
                conn = DriverManager.getConnection(conUrl);
            } catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        return conn;
    }

    @Override
    public boolean insert(int i)
    {
        String query = "INSERT INTO dozenten(id,Vorname,Nachname,Strasse,PLZ,Ort,Telefon,SteuerID,K_Inhaber,K_Bank,K_IBAN,K_BIC,email,email_user,email_pw,smtp)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	Dozent doz = this.get(i);
        Connection conn = this.connect("");

        if (conn != null)
        {
            try
            {
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, doz.getId());
                pstmt.setString(2, doz.getVorname());
                pstmt.setString(3, doz.getNachname());
                pstmt.setString(4, doz.getStrasse());
                pstmt.setString(5, doz.getPlz());
                pstmt.setString(6, doz.getOrt());
                pstmt.setString(7, doz.getTelefon());
                pstmt.setString(8, doz.getSteuerId());
                pstmt.setString(9, doz.getKInhaber());
                pstmt.setString(10, doz.getKBank());
                pstmt.setString(11, doz.getKIban());
                pstmt.setString(12, doz.getKBIC());
                pstmt.setString(13, doz.getEmail());
                pstmt.setString(14, doz.getEUser());
                pstmt.setString(15, doz.getEPw());
                pstmt.setString(16, doz.getSmtp());
                pstmt.executeUpdate();
		
		lastIdx = doz.getId();
                pstmt.close();
                conn.close();
                return true;
            } catch (SQLException e)
            {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean update(int i)
    {
        String query = "UPDATE dozenten SET Vorname = ?,"
                + "Nachname = ?,"
                + "Strasse = ?,"
                + "PLZ = ?,"
                + "Ort = ?,"
                + "Telefon = ?,"
                + "SteuerId = ?,"
                + "K_Inhaber = ?,"
                + "K_Bank = ?,"
                + "K_IBAN = ?,"
                + "K_BIC = ?,"
                + "email = ?,"
                + "email_user = ?,"
                + "email_pw = ?,"
                + "smtp = ?,"
                + "WHERE id = ?";

        Dozent doz = this.get(i);
        Connection conn = this.connect("");

        if (conn != null)
        {
            try
            {
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, doz.getVorname());
                pstmt.setString(2, doz.getNachname());
                pstmt.setString(3, doz.getStrasse());
                pstmt.setString(4, doz.getPlz());
                pstmt.setString(5, doz.getOrt());
                pstmt.setString(6, doz.getTelefon());
                pstmt.setString(7, doz.getEmail());
                pstmt.setString(8, doz.getEUser());
                pstmt.setString(9, doz.getEPw());
                pstmt.setString(10, doz.getSmtp());
                pstmt.setInt(11, doz.getId());
                pstmt.executeUpdate();

                pstmt.close();
                conn.close();
                return true;
            } catch (SQLException e)
            {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean create()
    {
        String table = "CREATE TABLE IF NOT EXISTS dozenten(\n"
                + "  id INT PRIMARY KEY NOT NULL UNIQUE, \n"
                + "  Vorname VARCHAR(255) NOT NULL, \n"
                + "  Nachname VARCHAR(255) NOT NULL, \n"
                + "  Strasse VARCHAR(255) NOT NULL, \n"
                + "  PLZ VARCHAR(5) NOT NULL, \n"
                + "  Ort VARCHAR(255) NOT NULL, \n"
                + "  Telefon VARCHAR(255) NOT NULL, \n"
                + "  SteuerId VARCHAR(255) NOT NULL, \n"
                + "  K_Inhaber VARCHAR(255) NOT NULL, \n"
                + "  K_Bank VARCHAR(255) NOT NULL, \n"
                + "  K_IBAN VARCHAR(255) NOT NULL, \n"
                + "  K_BIC VARCHAR(255) NOT NULL, \n"
                + "  email VARCHAR(255) NOT NULL, \n"
                + "  email_user VARCHAR(255) NOT NULL, \n"
                + "  email_pw VARCHAR(255) NOT NULL, \n"
                + "  smtp VARCHAR(255) NOT NULL);";

        Connection conn = this.connect("");
        if (conn != null)
        {
            try
            {
                Statement stmt = conn.createStatement();
                // create a new table
                stmt.execute(table);
		System.out.println("Table Dozent created!");
                stmt.close();
                conn.close();
                return true;
            } catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean load()
    {
        String query = "SELECT * FROM dozenten";

        Connection conn = this.connect("");
        if (conn != null)
        {
            try
            {
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);
                while (res.next())
                {
                    Dozent doz = new Dozent();
                    doz.setId(res.getInt("id"));
                    doz.setVorname(res.getString("Vorname"));
                    doz.setNachname(res.getString("Nachname"));
                    doz.setStrasse(res.getString("Strasse"));
                    doz.setPlz(res.getString("PLZ"));
                    doz.setOrt(res.getString("Ort"));
                    doz.setTelefon(res.getString("Telefon"));
                    doz.setSteuerId(res.getString("SteuerId"));
                    doz.setKInhaber(res.getString("K_Inhaber"));
                    doz.setKBank(res.getString("K_Bank"));
                    doz.setKIban(res.getString("K_IBAN"));
                    doz.setKBIC(res.getString("K_BIC"));
                    doz.setEmail(res.getString("email"));
                    doz.setEUser(res.getString("email_user"));
                    doz.setEPw(res.getString("email_pw"));
                    doz.setSmtp(res.getString("smtp"));
                    super.add(doz);
                    lastIdx = doz.getId();
                }
                stmt.close();
                conn.close();
                return true;
            } catch (SQLException e)
            {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int i)
    {
        return false;
    }

}
