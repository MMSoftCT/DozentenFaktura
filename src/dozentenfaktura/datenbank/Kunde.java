/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura.datenbank;

import javafx.beans.property.*;

/**
 *
 * @author amederake
 */
public final class Kunde
{

    // propertys
    private IntegerProperty kdNr;
    private StringProperty name;
    private StringProperty strasse;
    private StringProperty plz;
    private StringProperty ort;
    private StringProperty email;
    private StringProperty telefon;
    private StringProperty address;
    private StringProperty apAnrede;
    private StringProperty apVorname;
    private StringProperty apNachname;
    private StringProperty apTelefon;

    public Kunde()
    {
	kdNrProperty().set(0);
	nameProperty().set("");
	strasseProperty().set("");
	plzProperty().set("");
	ortProperty().set("");
	emailProperty().set("");
	telefonProperty().set("");
	apAnredeProperty().set("");
	apVornameProperty().set("");
	apNachnameProperty().set("");
	apTelefonProperty().set("");
    }
    // property constructors
    public IntegerProperty kdNrProperty()
    {
	if (kdNr == null)
	{
	    kdNr = new SimpleIntegerProperty(this, "kdNr");
	}
	return kdNr;
    }

    public StringProperty nameProperty()
    {
	if (name == null)
	{
	    name = new SimpleStringProperty(this, "name");
	}
	return name;
    }

    public StringProperty strasseProperty()
    {
	if (strasse == null)
	{
	    strasse = new SimpleStringProperty(this, "strasse");
	}
	return strasse;
    }

    public StringProperty plzProperty()
    {
	if (plz == null)
	{
	    plz = new SimpleStringProperty(this, "plz");
	}
	return plz;
    }

    public StringProperty ortProperty()
    {
	if (ort == null)
	{
	    ort = new SimpleStringProperty(this, "ort");
	}
	return ort;
    }

    public StringProperty emailProperty()
    {
	if (email == null)
	{
	    email = new SimpleStringProperty(this, "email");
	}
	return email;
    }

    public StringProperty telefonProperty()
    {
	if (telefon == null)
	{
	    telefon = new SimpleStringProperty(this, "telefon");
	}
	return telefon;
    }

    public StringProperty addressProperty()
    {
	if (address == null)
	{
	    address = new SimpleStringProperty(this, "address");
	}
	return address;
    }

    public StringProperty apAnredeProperty()
    {
	if (apAnrede == null)
	{
	    apAnrede = new SimpleStringProperty(this, "apAnrede");
	}
	return apAnrede;
    }
    
    public StringProperty apVornameProperty()
    {
	if (apVorname == null)
	{
	    apVorname = new SimpleStringProperty(this, "apVorname");
	}
	return apVorname;
    }
    
    public StringProperty apNachnameProperty()
    {
	if (apNachname == null)
	{
	    apNachname = new SimpleStringProperty(this, "apNachname");
	}
	return apNachname;
    }
    
    public StringProperty apTelefonProperty()
    {
	if (apTelefon == null)
	{
	    apTelefon = new SimpleStringProperty(this, "apTelefon");
	}
	return apTelefon;
    }
    
    // setter / getter

    public void setKdNr(int value)
    {
	kdNrProperty().set(value);
    }

    public int getKdNr()
    {
	return kdNrProperty().get();
    }

    public void setName(String value)
    {
	nameProperty().set(value);
    }

    public String getName()
    {
	return nameProperty().get();
    }

    public void setStrasse(String value)
    {
	strasseProperty().set(value);
    }

    public String getStrasse()
    {
	return strasseProperty().get();
    }

    public void setPlz(String value)
    {
	plzProperty().set(value);
	this.setAddress(String.valueOf(value) + " " + this.getOrt());
    }

    public String getPlz()
    {
	return plzProperty().get();
    }

    public void setOrt(String value)
    {
	ortProperty().set(value);
	this.setAddress(String.valueOf(this.getPlz()) + " " + value);
    }

    public String getOrt()
    {
	return ortProperty().get();
    }

    public void setTelefon(String value)
    {
	telefonProperty().set(value);
    }

    public String getTelefon()
    {
	return telefonProperty().get();
    }

    public void setEmail(String value)
    {
	emailProperty().set(value);
    }

    public String getEmail()
    {
	return emailProperty().get();
    }

    public void setAddress(String value)
    {
	addressProperty().set(value);
    }
    public String getAddress()
    {
	return addressProperty().get();
    }

    public void setApVorname(String value)
    {
	apVornameProperty().set(value);
    }

    public String getApVorname()
    {
	return apVornameProperty().get();
    }

    public void setApNachname(String value)
    {
	apNachnameProperty().set(value);
    }

    public String getApNachname()
    {
	return apNachnameProperty().get();
    }

    public void setApTelefon(String value)
    {
	apTelefonProperty().set(value);
    }

    public String getApTelefon()
    {
	return apTelefonProperty().get();
    }

    public void setApAnrede(String value)
    {
	apAnredeProperty().set(value);
    }

    public String getApAnrede()
    {
	return apAnredeProperty().get();
    }


}
