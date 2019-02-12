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
public class AuftragList extends ArrayList<Auftrag> implements Isql
{

    private int lastIdx;
    private String dbName;

    public AuftragList()
    {
        super();
        lastIdx = -1;
        dbName = "";
    }
    
    @Override
    public boolean add(Auftrag au)
    {
        boolean ret = false;
        if(super.add(au))
        {
            ret = insert(this.indexOf(au));
        }
        return ret;
    }
    
    @Override
    public Auftrag set(int index, Auftrag au)
    {
        Auftrag ret = super.set(index, au);
        update(index);
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

    /**
     * get auftrag with given nr
     *
     * @param nr auftrag nr
     * @return auftrag or null
     */
    public Auftrag getAuftragByNr(int nr)
    {
        for (Auftrag aThi : this)
        {
            if (aThi.getAuftragNr() == nr)
            {
                return aThi;
            }
        }
        return null;
    }

    /**
     * returns a list of auftrag with given dozent
     *
     * @param d dozent id
     * @return list of auftrag
     */
    public ArrayList<Auftrag> getAuftragByDoz(int d)
    {
        ArrayList<Auftrag> list = new ArrayList<>();
        for (Auftrag aThi : this)
        {
            if (aThi.getDozent() == d)
            {
                list.add(aThi);
            }
        }
        return list;
    }

    /**
     * returns a list with given dozent and kdNr
     *
     * @param kd kdNr
     * @param d dozent id
     * @return list of auftrag
     */
    public ArrayList<Auftrag> getAuftragByKundeAndDozent(int kd, int d)
    {
        ArrayList<Auftrag> list = new ArrayList<Auftrag>();
        for (Auftrag aThi : this)
        {
            if (aThi.getKdNr() == kd && aThi.getDozent() == d)
            {
                list.add(aThi);
            }
        }
        return list;
    }

    /**
     * returns a list with given kdNr
     *
     * @param kd kdNr
     * @return list of auftrag
     */
    public ArrayList<Auftrag> getAuftragByKunde(int kd)
    {
        ArrayList<Auftrag> list = new ArrayList<>();
        for (Auftrag aThi : this)
        {
            if (aThi.getKdNr() == kd)
            {
                list.add(aThi);
            }
        }
        return list;
    }

    // Isql implementations
    
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
        String query = "INSERT INTO auftrag(auftragNr,kdNr,dozentId,Thema,Start,Ende,Einheiten, Berechnet,Honorar,abgerechnetBis)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?)";
        
        Auftrag au = this.get(i);
        Connection conn = this.connect("");

        if (conn != null)
        {
            try
            {
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, au.getAuftragNr());
                pstmt.setInt(2, au.getKdNr());
                pstmt.setInt(3, au.getDozent());
                pstmt.setString(4, au.getThema());
                pstmt.setDate(5, Date.valueOf(au.getStart()));
                pstmt.setDate(6,Date.valueOf(au.getEnd()));
                pstmt.setInt(7, au.getEinheiten());
                pstmt.setBoolean(8, au.getBerechnet());
                pstmt.setDouble(9, Double.parseDouble(au.getHonorar().replace(",", ".")));
		pstmt.setDate(10, Date.valueOf(au.getAbgerechnetBis()));
                pstmt.executeUpdate();
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next())
                {
                    lastIdx = au.getAuftragNr();
                }
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
        String query = "UPDATE auftrag SET "
		+ "kdNr = ?,"
                + "dozentId = ?,"
                + "Thema = ?,"
                + "Start = ?,"
                + "Ende = ?,"
                + "Einheiten = ?,"
                + "Berechnet = ?,"
                + "Honorar = ?,"
		+ "abgerechnetBis = ?"
                + "WHERE auftragNr = ?";

        Auftrag au = this.get(i);
        Connection conn = this.connect("");

        if (conn != null)
        {
            try
            {
                PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, au.getKdNr());
                pstmt.setInt(2,au.getDozent());
                pstmt.setString(3, au.getThema());
                pstmt.setDate(4, Date.valueOf(au.getStart()));
                pstmt.setDate(5, Date.valueOf(au.getEnd()));
                pstmt.setInt(6, au.getEinheiten());
                pstmt.setBoolean(7, au.getBerechnet());
                pstmt.setDouble(8, Double.parseDouble(au.getHonorar().replace(",", ".")));
		pstmt.setDate(9, Date.valueOf(au.getAbgerechnetBis()));
                pstmt.setInt(10,au.getAuftragNr());
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
        String table = "CREATE TABLE IF NOT EXISTS auftrag(\n"
                + "  auftragNr INT PRIMARY KEY NOT NULL, \n"
                + "  kdNr INT NOT NULL, \n"
                + "  dozentId INT NOT NULL, \n"
                + "  Thema VARCHAR(255) NOT NULL, \n"
                + "  Start DATE NOT NULL, \n"
                + "  Ende DATE NOT NULL, \n"
                + "  Einheiten INT NOT NULL, \n"
                + "  Berechnet BOOLEAN NOT NULL, \n"
                + "  Honorar DECIMAL NOT NULL, \n"
		+ "  abgerechnetBis DATE);";

        Connection conn = this.connect("");
        if (conn != null)
        {
            try
            {
                Statement stmt = conn.createStatement();
                // create a new table
                stmt.execute(table);
		System.out.println("Table Auftrag created!");
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
        String query = "SELECT * FROM auftrag";

        Connection conn = this.connect("");
        if (conn != null)
        {
            try
            {
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);
                while (res.next())
                {
                    Auftrag au = new Auftrag();
                    au.setAuftragNr(res.getInt(1));
                    au.setKunde(res.getInt(2));
                    au.setDozent(res.getInt(3));
                    au.setThema(res.getString(4));
                    au.setStart(res.getDate(5));
                    au.setEnd(res.getDate(6));
                    au.setEinheiten(res.getInt(7));
                    au.setBerechnet(res.getBoolean(8));
                    au.setHonorar(String.format("%.2f",res.getDouble(9)).replace(".", ","));
		    au.setAbgerechnetBis(res.getDate(10));
                    super.add(au);
                    lastIdx = au.getAuftragNr();
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
