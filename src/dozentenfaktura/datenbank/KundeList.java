/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura.datenbank;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author amederake
 */
public class KundeList extends ArrayList<Kunde> implements Isql
{

    private int lastIdx;
    private String dbName;

    public KundeList()
    {
        super();
        lastIdx = -1;
        dbName = "";
    }
    
    /**
     * override add methode
     * @param kd kunde object
     * @return true if succsessfull
     */
    @Override
    public boolean add(Kunde kd)
    {
	boolean ret = false;
	if(super.add(kd))
	{
	    ret = insert(this.indexOf(kd));
	}
	return ret;
    }
    
    /**
     * override set methode
     * @param index index in array
     * @param kd kunde objet
     * @return old kunde object
     */
    @Override
    public Kunde set(int index, Kunde kd)
    {
	Kunde ret = super.set(index,kd);
	update(index);
	return ret;
    }
    
    // class methodes
    
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
    
    // list methodes
    
    /**
     * get kunde oject where matches given kdNr
     * @param kdNr 
     * @return Kunde
     */
    public Kunde getKundeByNr(int kdNr)
    {
        for (Kunde aThi : this)
        {
            if (aThi.getKdNr() == kdNr)
            {
                return aThi;
            }
        }
        return null;
    }
    
    /**
     * get a list of kunde name and location
     * @return list of name and location
     */
    public ArrayList<String> getKundenNames()
    {
	ArrayList<String> ret = new ArrayList<>();
	
	this.forEach((aThi) ->
	{
	    ret.add(aThi.getName() + ", " + aThi.getOrt());
	});
	return ret;
    }
    
    // Isql implentations
    
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
        String query = "INSERT INTO kunden(kdNr,Unternehmen,Strasse,PLZ,Ort,email,Telefon,ApAnrede,ApVorname,ApNachname,ApTelefon)"
		+ " VALUES(?,?,?,?,?,?,?,?,?,?,?)";

        Kunde kd = this.get(i);
        Connection conn = this.connect("");

        if (conn != null)
        {
            try
            {
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, kd.getKdNr());
                pstmt.setString(2, kd.getName());
                pstmt.setString(3, kd.getStrasse());
                pstmt.setString(4, kd.getPlz());
                pstmt.setString(5, kd.getOrt());
                pstmt.setString(6, kd.getEmail());
                pstmt.setString(7, kd.getTelefon());
		pstmt.setString(8, kd.getApAnrede());
		pstmt.setString(9, kd.getApVorname());
		pstmt.setString(10, kd.getApNachname());
		pstmt.setString(11, kd.getApTelefon());
                pstmt.executeUpdate();
		
		lastIdx = kd.getKdNr();
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
        String query = "UPDATE kunden SET Unternehmen = ?," 
                + "Strasse = ?,"
                + "PLZ = ?,"
                + "Ort = ?,"
                + "email = ?,"
                + "Telefon = ?"
		+ "ApAnrede = ?"
		+ "ApVorname = ?"
		+ "ApNachname = ?"
		+ "ApTelefon = ?"
                + "WHERE kdNr = ?";

        Kunde kd = this.get(i);
        Connection conn = this.connect("");

        if (conn != null)
        {
            try
            {
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, kd.getName());
                pstmt.setString(2, kd.getStrasse());
                pstmt.setString(3, kd.getPlz());
                pstmt.setString(4, kd.getOrt());
                pstmt.setString(5, kd.getEmail());
                pstmt.setString(6, kd.getTelefon());
                pstmt.setString(7, kd.getApAnrede());
                pstmt.setString(8, kd.getApVorname());
                pstmt.setString(9, kd.getApNachname());
                pstmt.setString(10, kd.getApTelefon());
                pstmt.setInt(11, kd.getKdNr());
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
        String table = "CREATE TABLE IF NOT EXISTS kunden(\n"
                + "  kdNr INT PRIMARY KEY NOT NULL, \n"
                + "  Unternehmen VARCHAR(255) NOT NULL, \n"
                + "  Strasse VARCHAR(255) NOT NULL, \n"
                + "  PLZ VARCHAR(5) NOT NULL, \n"
                + "  Ort VARCHAR(255) NOT NULL, \n"
                + "  email VARCHAR(255) NOT NULL, \n"
                + "  Telefon VARCHAR(255) NOT NULL, \n"
                + "  ApAnrede VARCHAR(255), \n"
                + "  ApVorname VARCHAR(255), \n"
                + "  ApNachname VARCHAR(255), \n"
                + "  ApTelefon VARCHAR(255));";

        Connection conn = this.connect("");
        if (conn != null)
        {
            try
            {
                Statement stmt = conn.createStatement();
                // create a new table
                stmt.execute(table);
		System.out.println("Table Kunde created!");
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
        String query = "SELECT * FROM kunden";

        Connection conn = this.connect("");
        if (conn != null)
        {
            try
            {
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);
                while (res.next())
                {
                    Kunde kd = new Kunde();
                    kd.setKdNr(res.getInt("kdNr"));
                    kd.setName(res.getString("Unternehmen"));
                    kd.setStrasse(res.getString("Strasse"));
                    kd.setPlz(res.getString("PLZ"));
                    kd.setOrt(res.getString("Ort"));
                    kd.setEmail(res.getString("email"));
                    kd.setTelefon(res.getString("Telefon"));
                    kd.setApAnrede(res.getString("ApAnrede"));
                    kd.setApVorname(res.getString("ApVorname"));
                    kd.setApNachname(res.getString("ApNachname"));
                    kd.setApTelefon(res.getString("ApTelefon"));
                    super.add(kd);
                    lastIdx = kd.getKdNr();
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
