/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dozentenfaktura.datenbank;

import java.sql.Date;
import java.time.LocalDate;
import javafx.beans.property.*;

/**
 *
 * @author amederake
 */
public final class Rechnung 
{
    private IntegerProperty rechnungNr;
    private IntegerProperty auftragNr;
    private IntegerProperty dozentId;
    private ObjectProperty<LocalDate> datum;
    private BooleanProperty teilRechnung;
    private ObjectProperty<LocalDate> von_datum;
    private ObjectProperty<LocalDate> bis_datum;
    private IntegerProperty einheiten;
    private BooleanProperty korrigiert;
    private StringProperty summe;
    private StringProperty path;
    private BooleanProperty versand;
    
    public Rechnung()
    {
	rechnungNrProperty().set(0);
	auftragNrProperty().set(0);
	dozentIdProperty().set(0);
	datumProperty().set(null);
	teilRechnungProperty().set(false);
	von_datumProperty().set(null);
	bis_datumProperty().set(null);
	einheitenProperty().set(0);
	korrigiertProperty().set(false);
	summeProperty().set("");
	pathProperty().set("");
	versandProperty().set(false);
    }
    
    public IntegerProperty rechnungNrProperty()
    {
	if(rechnungNr == null) rechnungNr = new SimpleIntegerProperty(this, "rechnungNr");
	return rechnungNr;
    }
    
    public IntegerProperty auftragNrProperty()
    {
	if(auftragNr == null) auftragNr = new SimpleIntegerProperty(this, "auftragNr");
	return auftragNr;
    }
    
    public IntegerProperty dozentIdProperty()
    {
	if(dozentId == null) dozentId = new SimpleIntegerProperty(this, "dozentenId");
	return dozentId;
    }
    
    public ObjectProperty datumProperty()
    {
	if(datum == null) datum = new SimpleObjectProperty(this, "datum");
	return datum;
    }
    
    public BooleanProperty teilRechnungProperty()
    {
	if(teilRechnung == null) teilRechnung = new SimpleBooleanProperty(this, "teilRechnung");
	return teilRechnung;
    }
    
    public ObjectProperty von_datumProperty()
    {
	if(von_datum == null) von_datum = new SimpleObjectProperty(this, "von_datum");
	return von_datum;
    }
    
    public ObjectProperty bis_datumProperty()
    {
	if(bis_datum == null) bis_datum = new SimpleObjectProperty(this, "bis_datum");
	return bis_datum;
    }
    
    public IntegerProperty einheitenProperty()
    {
	if(einheiten == null) einheiten = new SimpleIntegerProperty(this, "einheiten");
	return einheiten;
    }
    
    public BooleanProperty korrigiertProperty()
    {
	if(korrigiert == null) korrigiert = new SimpleBooleanProperty(this, "korrigiert");
	return korrigiert;
    }
    
    public StringProperty summeProperty()
    {
	if(summe == null) summe = new SimpleStringProperty(this, "honorar");
	return summe;
    }
    
    public StringProperty pathProperty()
    {
	if(path == null) path = new SimpleStringProperty(this, "path");
	return path;
    }
    
    public BooleanProperty versandProperty()
    {
	if(versand == null) versand = new SimpleBooleanProperty(this, "versand");
	return versand;
    }
    

    /**
     * set rechnungNr
     * @param n rechnungNr
     */
    public void setRechnungNr(int n)
    {
        rechnungNrProperty().set(n);
    }
    
    /**
     * get rechnungNr
     * @return rechnungNr
     */
    public int getRechnungNr()
    {
        return rechnungNrProperty().get();
    }
    
    /**
     * set auftragNr
     * @param a auftragNr
     */
    public void setAuftragNr(int a)
    {
        auftragNrProperty().set(a);
    }
    
    /**
     * get auftragNr
     * @return auftragNr
     */
    public int getAuftragNr()
    {
        return auftragNrProperty().get();
    }
    
    /**
     * set dozent id
     * @param d dozent id
     */
    public void setDozentId(int d)
    {
        dozentIdProperty().set(d);
    }
    
    /**
     * get dozent id
     * @return dozent id
     */
    public int getDozentId()
    {
        return dozentIdProperty().get();
    }
    
    /**
     * set datum
     * @param d datum
     */
    public void setDatum(Date d)
    {
        datumProperty().set(d.toLocalDate());
    }
    
    /**
     * get datum
     * @return datum
     */
    public LocalDate getDatum()
    {
        return (LocalDate) datumProperty().get();
    }
 
    /**
     * set teilrechnung flag
     * @param value true/false
     */
    public void setTeilRechnung(boolean value)
    {
	teilRechnungProperty().set(value);
    }
    
    /**
     * get teilrechnung flag
     * @return true/false
     */
    public boolean getTeilRechnung()
    {
	return teilRechnungProperty().get();
    }
    
    /**
     * set von von datum
     * @param d datum
     */
    public void setVon_Datum(Date d)
    {
        von_datumProperty().set(d.toLocalDate());
    }
    
    /**
     * get von datum
     * @return datum
     */
    public LocalDate getVon_Datum()
    {
        return (LocalDate)von_datumProperty().get();
    }
    
    /**
     * set bis datum
     * @param d datum
     */
    public void setBis_Datum(Date d)
    {
        bis_datumProperty().set(d.toLocalDate());
    }
    
    /**
     * get bis datum
     * @return datum
     */
    public LocalDate getBis_Datum()
    {
        return (LocalDate)bis_datumProperty().get();
    }
    
    /**
     * set einheiten
     * @param d einheiten
     */
    public void setEinheiten(int d)
    {
        einheitenProperty().set(d);
    }
    
    /**
     * get einheiten
     * @return einheiten
     */
    public int getEinheiten()
    {
        return einheitenProperty().get();
    }
    
    /**
     * set korrigiert flag
     * @param value true/false
     */
    public void setKorrigiert(boolean value)
    {
	korrigiertProperty().set(value);
    }
    
    /**
     * get korrigiert flag
     * @return true/false
     */
    public boolean getKorrigiert()
    {
	return korrigiertProperty().get();
    }
    
    /**
     * set summe
     * @param s summe
     */
    public void setSumme(String s)
    {
        summeProperty().set(s);
    }
    
    /**
     * get summe
     * @return summe
     */
    public String getSumme()
    {
        return summeProperty().get();
    }
    
    public void setPath(String p)
    {
        pathProperty().set(p);
    }
    
    public String getPath()
    {
        return pathProperty().get();
    }
 
    /**
     * set teilrechnung flag
     * @param value true/false
     */
    public void setVersand(boolean value)
    {
	versandProperty().set(value);
    }
    
    /**
     * get teilrechnung flag
     * @return true/false
     */
    public boolean getVersand()
    {
	return versandProperty().get();
    }
}
