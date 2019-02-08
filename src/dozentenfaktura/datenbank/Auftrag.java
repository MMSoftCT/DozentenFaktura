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
public final class Auftrag
{
    // class variables
    private IntegerProperty auftragNr;
    private IntegerProperty kdNr;
    private IntegerProperty dozent;
    private StringProperty thema;
    private ObjectProperty<LocalDate> start;
    private ObjectProperty<LocalDate> ende;
    private IntegerProperty einheiten;
    private BooleanProperty berechnet;
    private StringProperty honorar;
    private ObjectProperty<LocalDate> abgerechnetBis;

    public Auftrag()
    {
	auftragNrProperty().set(0);
	kdNrProperty().set(0);
	dozentProperty().set(0);
	themaProperty().set("");
	startProperty().set(LocalDate.now());
	endeProperty().set(LocalDate.now().plusDays(7));
	einheitenProperty().set(45);
        berechnetProperty().set(true);
	honorarProperty().set("");
	abgerechnetBisProperty().set(null);
    }
    
    // property constructor

    public IntegerProperty auftragNrProperty()
    {
	if(auftragNr == null) auftragNr = new SimpleIntegerProperty(this, "auftragNr");
	return auftragNr;
    }
    
    public IntegerProperty kdNrProperty()
    {
	if (kdNr == null)
	{
	    kdNr = new SimpleIntegerProperty(this, "kdNr");
	}
	return kdNr;
    }

    public IntegerProperty dozentProperty()
    {
	if (dozent == null)
	{
	    dozent = new SimpleIntegerProperty(this, "kdNr");
	}
	return dozent;
    }

    public StringProperty themaProperty()
    {
	if(thema == null) thema = new SimpleStringProperty(this, "thema");
	return thema;
    }
    
    public ObjectProperty startProperty()
    {
	if(start == null) start = new SimpleObjectProperty(this, "start");
	return start;
    }
    
    public ObjectProperty endeProperty()
    {
	if(ende == null) ende = new SimpleObjectProperty(this, "ende");
	return ende;
    }
    
    public IntegerProperty einheitenProperty()
    {
	if(einheiten == null) einheiten = new SimpleIntegerProperty(this, "einheiten");
	return einheiten;
    }
    
    public BooleanProperty berechnetProperty()
    {
        if(berechnet == null) berechnet = new SimpleBooleanProperty(this, "berechnet");
        return berechnet;
    }
    
    public StringProperty honorarProperty()
    {
	if(honorar == null) honorar = new SimpleStringProperty(this, "honorar");
	return honorar;
    }

    public ObjectProperty abgerechnetBisProperty()
    {
	if(abgerechnetBis == null) abgerechnetBis = new SimpleObjectProperty(this, "abgerechnetBis");
	return abgerechnetBis;
    }
    
    // setter & getter
    
    /**
     * set auftragNr
     * @param value auftragNr 
     */
    public void setAuftragNr(int value)
    {
        auftragNrProperty().set(value);
    }
    
    /**
     * get auftragNr
     * @return auftrageNr
     */
    public int getAuftragNr()
    {
        return auftragNrProperty().get();
    }
    
    /**
     * set kdNr
     * @param value kdNr 
     */
    public void setKunde(int value)
    {
        kdNrProperty().set(value);
    }
    
    /**
     * get kdNr
     * @return kdNr
     */
    public int getKdNr()
    {
        return kdNrProperty().get();
    }
    
    /**
     * set dozent id
     * @param value dozent id
     */
    public void setDozent(int value)
    {
        dozentProperty().set(value);
    }
    
    /**
     * get dozent id
     * @return dozent id
     */
    public int getDozent()
    {
        return dozentProperty().get();
    }
    
    /**
     * get thema
     * @param value thema
     */
    public void setThema(String value)
    {
        themaProperty().set(value);
    }
    
    /**
     * get thema
     * @return thema
     */
    public String getThema()
    {
        return themaProperty().get();
    }
    
    /**
     * set start date
     * @param s start date
     */
    public void setStart(Date s)
    {
        startProperty().set(s.toLocalDate());
    }
    
    /**
     * get start date
     * @return start date
     */
    public LocalDate getStart()
    {
        return (LocalDate) startProperty().get();
    }
    
    /**
     * set end date
     * @param e end date
     */
    public void setEnd(Date e)
    {
        endeProperty().set(e.toLocalDate());
    }
    
    /**
     * get end date
     * @return end date
     */
    public LocalDate getEnd()
    {
        return (LocalDate) endeProperty().get();
    }
    
    /**
     * set einheiten
     * @param value einheiten 
     */
    public void setEinheiten(int value)
    {
        einheitenProperty().set(value);
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
     * set berechnet flag
     * @param value true/false
     */
    public void setBerechnet(boolean value)
    {
        berechnetProperty().set(value);
    }
    
    /**
     * get berechnet flag
     * @return true/false
     */
    public boolean getBerechnet()
    {
        return berechnetProperty().get();
    }
    
    /**
     * set honorar
     * @param value honorar 
     */
    public void setHonorar(String value)
    {
        honorarProperty().set(value);
    }
    
    /**
     * get honorar
     * @return honorar
     */
    public String getHonorar()
    {
        return honorarProperty().get();
    }

     /**
     * set start date
     * @param s start date
     */
    public void setAbgerechnetBis(Date s)
    {
        abgerechnetBisProperty().set(s.toLocalDate());
    }
    
    /**
     * get start date
     * @return start date
     */
    public LocalDate getAbgerechnetBis()
    {
        return (LocalDate) abgerechnetBisProperty().get();
    }
    
}
