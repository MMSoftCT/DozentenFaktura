/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura.datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author amederake
 */
public class EinstellungList extends ArrayList<Einstellung> implements Isql
{

    private int lastIdx;
    private String dbName;

    public EinstellungList()
    {
        super();
        lastIdx = -1;
        dbName = "";
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

    public Einstellung getByDozentId(int id)
    {
        for(Einstellung d : this)
        {
            if(d.getDozent() == id)
            {
                return d;
            }
        }
        return null;
    }
    
    /**
     * override add methode
     * @param eins
     * @return true if succsessfull
     */
    @Override
    public boolean add(Einstellung eins)
    {
	boolean ret = false;
	if(super.add(eins))
	{
	    ret = insert(this.indexOf(eins));
	}
	return ret;
    }
    
    /**
     * override set methode
     * @param index index in array
     * @param eins
     * @return old dozent obect at index
     */
    @Override
    public Einstellung set(int index, Einstellung eins)
    {
	Einstellung ret = super.set(index , eins);
	update(index);
	return ret;
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
        String query = "INSERT INTO einstellung(id,Dozent,Logo,Betreff,Anrede,Einleitung,Schluss1,Schluss2,Schluss3,Schluss4,Unterschrift)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";

	Einstellung einst = this.get(i);
        Connection conn = this.connect("");

        if (conn != null)
        {
            try
            {
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, einst.getId());
                pstmt.setInt(2, einst.getDozent());
                pstmt.setString(3, einst.getLogo());
                pstmt.setString(4, einst.getBetreff());
                pstmt.setString(5, einst.getAnrede());
                pstmt.setString(6, einst.getEinleitung());
                pstmt.setString(7, einst.getSchluss1());
                pstmt.setString(8, einst.getSchluss2());
                pstmt.setString(9, einst.getSchluss3());
                pstmt.setString(10, einst.getSchluss4());
                pstmt.setString(11, einst.getUnterschrift());
                pstmt.executeUpdate();
		
		lastIdx = einst.getId();
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
        String query = "UPDATE einstellung SET Dozent = ?,"
                + "Logo = ?,"
                + "Betreff = ?,"
                + "Anrede = ?,"
                + "Einleitung = ?,"
                + "Schluss1 = ?,"
                + "Schluss2 = ?,"
                + "Schluss3 = ?,"
                + "Schluss4 = ?,"
                + "Unterschrift = ?,"
                + "WHERE id = ?";

	Einstellung einst = this.get(i);
        Connection conn = this.connect("");

        if (conn != null)
        {
            try
            {
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, einst.getDozent());
                pstmt.setString(2, einst.getLogo());
                pstmt.setString(3, einst.getBetreff());
                pstmt.setString(4, einst.getAnrede());
                pstmt.setString(5, einst.getEinleitung());
                pstmt.setString(6, einst.getSchluss1());
                pstmt.setString(7, einst.getSchluss2());
                pstmt.setString(8, einst.getSchluss3());
                pstmt.setString(9, einst.getSchluss4());
                pstmt.setString(10, einst.getUnterschrift());
                pstmt.setInt(11, einst.getId());
                pstmt.executeUpdate();
		
		lastIdx = einst.getId();
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
    public boolean delete(int i)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean create()
    {
        String table = "CREATE TABLE IF NOT EXISTS einstellung (\n"
                + "  id INT PRIMARY KEY NOT NULL UNIQUE, \n"
                + "  Dozent INT NOT NULL, \n"
                + "  Logo VARCHAR(255), \n"
                + "  Betreff VARCHAR(255) NOT NULL, \n"
                + "  Anrede VARCHAR(255) NOT NULL, \n"
                + "  Einleitung VARCHAR(255) NOT NULL, \n"
                + "  Schluss1 VARCHAR(255) NOT NULL, \n"
                + "  Schluss2 VARCHAR(255) NOT NULL, \n"
                + "  Schluss3 VARCHAR(255) NOT NULL, \n"
                + "  Schluss4 VARCHAR(255) NOT NULL, \n"
                + "  Unterschrift VARCHAR(255));";
        Connection conn = this.connect("");
        if (conn != null)
        {
            try
            {
                Statement stmt = conn.createStatement();
                // create a new table
                stmt.execute(table);
		System.out.println("Table Einstellung created!");
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
        String query = "SELECT * FROM einstellung";

        Connection conn = this.connect("");
        if (conn != null)
        {
            try
            {
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);
                while (res.next())
                {
                    Einstellung einst = new Einstellung();
                    einst.setId(res.getInt(1));
                    einst.setDozent(res.getInt(2));
                    einst.setLogo(res.getString(3));
                    einst.setBetreff(res.getString(4));
                    einst.setAnrede(res.getString(5));
                    einst.setEinleitung(res.getString(6));
                    einst.setSchluss1(res.getString(7));
                    einst.setSchluss2(res.getString(8));
                    einst.setSchluss3(res.getString(9));
                    einst.setSchluss4(res.getString(10));
                    einst.setUnterschrift(res.getString(11));
                    super.add(einst);
                    lastIdx = einst.getId();
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

}
