/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura.datenbank;

import java.sql.Connection;

/**
 *
 * @author amederake
 */
public interface Isql
{

    /**
     * create a connection to given url
     * @param url database url
     * @return connection or null
     */
    public Connection connect(String url);
    
    /**
     * insert a new row
     * @param i index
     * @return true if sucsessfull
     */
    public boolean insert(int i);
    
    /**
     * update a row on given index
     * @param i index
     * @return true if sucsessfull
     */
    public boolean update(int i);
    
    /**
     * delete the row with the given index
     * @param i index
     * @return true if sucsessfull
     */
    public boolean delete(int i);
    
    /**
     * create the table
     * @return true if sucsessfull
     */
    public boolean create();
    
    /**
     * read all table entries
     * @return true if sucsessfull
     */
    public boolean load();
}
