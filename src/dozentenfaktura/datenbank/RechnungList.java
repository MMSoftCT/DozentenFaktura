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
public class RechnungList extends ArrayList<Rechnung> implements Isql
{

    private int lastIdx;
    private String dbName;

    public RechnungList()
    {
        super();
        lastIdx = -1;
        dbName = "";
    }
    
    @Override
    public boolean add(Rechnung re)
    {
        boolean ret = false;
        if(super.add(re))
        {
            ret = insert(indexOf(re));
        }
        return ret;
    }
    
    @Override
    public Rechnung set(int index, Rechnung re)
    {
        Rechnung ret = super.set(index, re);
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
     * get rechnungen list with given auftrag nr
     * @param auNr auftrag nr
     * @return list of rechnung objects
     */
    public ArrayList<Rechnung> getAuftragRe(int auNr)
    {
        ArrayList<Rechnung> ret = new ArrayList<>();
        for(int i=0; i < this.size(); i++)
        {
            if(this.get(i).getAuftragNr() == auNr)
            {
                ret.add(this.get(i));
            }
        }
        return ret;
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
        String query = "INSERT INTO rechnung(rechnungNr,auftragNr,Datum,teil,von_Datum,bis_Datum, Einheiten,Korrigiert,Summe,Datei,versand)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        
        Rechnung re = this.get(i);
        Connection conn = this.connect("");
        if (conn != null)
        {
            try
            {
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, re.getRechnungNr());
                pstmt.setInt(2, re.getAuftragNr());
                pstmt.setDate(3, Date.valueOf(re.getDatum()));
		pstmt.setBoolean(4, re.getTeilRechnung());
		pstmt.setDate(5, Date.valueOf(re.getVon_Datum()));
		pstmt.setDate(6, Date.valueOf(re.getBis_Datum()));
                pstmt.setInt(7, re.getEinheiten());
		pstmt.setBoolean(8, re.getKorrigiert());
                pstmt.setDouble(9, Double.parseDouble(re.getSumme().replace(",", ".")));
                pstmt.setString(10, re.getPath());
		pstmt.setBoolean(11, re.getVersand());
                pstmt.executeUpdate();
                
                lastIdx = re.getRechnungNr();
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
        String query = "UPDATE rechnung SET "
		+ "auftragNr = ?,"
                + "Datum = ?,"
		+ "teil = ?,"
		+ "von_Datum = ?,"
		+ "bis_Datum = ?"
                + "Einheiten = ?,"
                + "Korrigiert = ?,"
                + "Summe = ?,"
                + "Datei = ?,"
		+ "versand = ?"
                + "WHERE rechnungNr = ?";
        
        Rechnung re = this.get(i);
        Connection conn = this.connect("");
        if (conn != null)
        {
            try
            {
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, re.getAuftragNr());
                pstmt.setDate(2, Date.valueOf(re.getDatum()));
		pstmt.setBoolean(3, re.getTeilRechnung());
		pstmt.setDate(4, Date.valueOf(re.getVon_Datum()));
		pstmt.setDate(5, Date.valueOf(re.getBis_Datum()));
                pstmt.setInt(6, re.getEinheiten());
		pstmt.setBoolean(7, re.getKorrigiert());
                pstmt.setDouble(8, Double.parseDouble(re.getSumme().replace(",", ".")));
                pstmt.setString(9, re.getPath());
		pstmt.setBoolean(10, re.getVersand());
                pstmt.setInt(11, re.getRechnungNr());
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
        String table = "CREATE TABLE IF NOT EXISTS rechnung(\n"
                + "  rechnungNr INT PRIMARY KEY NOT NULL, \n"
                + "  auftragNr INT NOT NULL, \n"
                + "  Datum DATE NOT NULL, \n"
                + "  teil BOOLEAN NOT NULL, \n"
                + "  von_Datum DATE , \n"
                + "  bis_Datum DATE , \n"
                + "  Einheiten INT NOT NULL, \n"
                + "  Korrigiert BOOLEAN NOT NULL, \n"
                + "  Summe DECIMAL NOT NULL, \n"
                + "  Datei VARCHAR(255) NOT NULL, \n"
		+ "  versand BOOLEAN NOT NULL);";

        Connection conn = this.connect("");
        if (conn != null)
        {
            try
            {
                Statement stmt = conn.createStatement();
                // create a new table
                stmt.execute(table);
		System.out.println("Table Rechnung created!");
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
        String query = "SELECT * FROM rechnung";

        Connection conn = this.connect("");
        if (conn != null)
        {
            try
            {
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);
                while (res.next())
                {
                    Rechnung re = new Rechnung();
                    re.setRechnungNr(res.getInt(1));
                    re.setAuftragNr(res.getInt(2));
                    re.setDatum(res.getDate(3));
		    re.setTeilRechnung(res.getBoolean(4));
		    re.setVon_Datum(res.getDate(5));
		    re.setBis_Datum(res.getDate(6));
		    re.setEinheiten(res.getInt(7));
		    re.setKorrigiert(res.getBoolean(8));
                    re.setSumme(String.format("%.2f",res.getDouble(9)).replace(".", ","));
                    re.setPath(res.getString(10));
		    re.setVersand(res.getBoolean(11));
                    super.add(re);
                    lastIdx = re.getRechnungNr();
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
