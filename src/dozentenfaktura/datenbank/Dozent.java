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
public class Dozent
{

    // class variables
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty vorname = new SimpleStringProperty();
    private StringProperty nachname = new SimpleStringProperty();
    private StringProperty strasse = new SimpleStringProperty();
    private StringProperty plz = new SimpleStringProperty();
    private StringProperty ort = new SimpleStringProperty();
    private StringProperty telefon = new SimpleStringProperty();
    private StringProperty steuer_id = new SimpleStringProperty();
    private StringProperty k_inhaber = new SimpleStringProperty();
    private StringProperty k_bank = new SimpleStringProperty();
    private StringProperty k_iban = new SimpleStringProperty();
    private StringProperty k_bic = new SimpleStringProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty email_user = new SimpleStringProperty();
    private StringProperty email_pw = new SimpleStringProperty();
    private StringProperty smtp = new SimpleStringProperty();
    
    public Dozent()
    {
	setId(0);
	setVorname("");
	setNachname("");
	setStrasse("");
	setPlz("");
	setOrt("");
	setTelefon("");
        setSteuerId("");
        setKInhaber("");
        setKBank("");
        setKIban("");
        setKBIC("");
	setEmail("");
	setEUser("");
	setEPw("");
	setSmtp("");
    }

    public IntegerProperty idProperty()
    {
	return id;
    }

    public StringProperty vornameProperty()
    {
	return vorname;
    }

    public StringProperty nachnameProperty()
    {
	return nachname;
    }

    public StringProperty strasseProperty()
    {
	return strasse;
    }
    public StringProperty plzProperty()
    {
	return plz;
    }

    public StringProperty ortProperty()
    {
	return ort;
    }

    public StringProperty telefonProperty()
    {
	return telefon;
    }

    public StringProperty steuer_idProperty()
    {
	return steuer_id;
    }

    public StringProperty k_inhaberProperty()
    {
	return k_inhaber;
    }

    public StringProperty k_bankProperty()
    {
	return k_bank;
    }

    public StringProperty k_ibanProperty()
    {
	return k_iban;
    }

    public StringProperty k_bicProperty()
    {
	return k_bic;
    }

    public StringProperty emailProperty()
    {
	return email;
    }

    public StringProperty email_userProperty()
    {
	return email_user;
    }

    public StringProperty email_pwProperty()
    {
	return email_pw;
    }

    public StringProperty smtpProperty()
    {
	return smtp;
    }

    /**
     * set id
     *
     * @param i id
     */
    public final void setId(int i)
    {
	idProperty().setValue(i);
    }

    /**
     * get id
     *
     * @return id
     */
    public final int getId()
    {
	return idProperty().getValue();
    }

    /**
     * set vorname
     *
     * @param v vorname
     */
    public final void setVorname(String v)
    {
	vornameProperty().setValue(v);
    }

    /**
     * get vorname
     *
     * @return vorname
     */
    public final String getVorname()
    {
	return vornameProperty().getValue();
    }

    /**
     * set nachname
     *
     * @param n nachname
     */
    public final void setNachname(String n)
    {
	nachnameProperty().setValue(n);
    }

    /**
     * get nachname
     *
     * @return nachname
     */
    public final String getNachname()
    {
	return nachnameProperty().getValue();
    }

    /**
     * set strasse
     *
     * @param s strasse
     */
    public final void setStrasse(String s)
    {
	strasseProperty().setValue(s);
    }

    /**
     * get strasse
     *
     * @return strasse
     */
    public final String getStrasse()
    {
	return strasseProperty().getValue();
    }

    /**
     * set plz
     *
     * @param p plz
     */
    public final void setPlz(String p)
    {
	plzProperty().setValue(p);
    }

    /**
     * get plz
     *
     * @return plz
     */
    public final String getPlz()
    {
	return plzProperty().getValue();
    }

    /**
     * set ort
     *
     * @param o ort;
     */
    public final void setOrt(String o)
    {
	ortProperty().setValue(o);
    }

    /**
     * get ort
     *
     * @return ort
     */
    public final String getOrt()
    {
	return ortProperty().getValue();
    }

    /**
     * set phone number
     *
     * @param t phone number
     */
    public final void setTelefon(String t)
    {
	telefonProperty().setValue(t);
    }

    /**
     * get phone number
     *
     * @return phone number
     */
    public final String getTelefon()
    {
	return telefonProperty().getValue();
    }

    /**
     * set email address
     *
     * @param e email address
     */
    public final void setSteuerId(String e)
    {
	steuer_idProperty().setValue(e);
    }

    /**
     * get email address
     *
     * @return email address
     */
    public final String getSteuerId()
    {
	return steuer_idProperty().getValue();
    }

    /**
     * set email address
     *
     * @param e email address
     */
    public final void setKInhaber(String e)
    {
	k_inhaberProperty().setValue(e);
    }

    /**
     * get email address
     *
     * @return email address
     */
    public final String getKInhaber()
    {
	return k_inhaberProperty().getValue();
    }

    /**
     * set email address
     *
     * @param e email address
     */
    public final void setKBank(String e)
    {
	k_bankProperty().setValue(e);
    }

    /**
     * get email address
     *
     * @return email address
     */
    public final String getKBank()
    {
	return k_bankProperty().getValue();
    }

    /**
     * set email address
     *
     * @param e email address
     */
    public final void setKIban(String e)
    {
	k_ibanProperty().setValue(e);
    }

    /**
     * get email address
     *
     * @return email address
     */
    public final String getKIban()
    {
	return k_ibanProperty().getValue();
    }

    /**
     * set email address
     *
     * @param e email address
     */
    public final void setKBIC(String e)
    {
	k_bicProperty().setValue(e);
    }

    /**
     * get email address
     *
     * @return email address
     */
    public final String getKBIC()
    {
	return k_bicProperty().getValue();
    }

    /**
     * set email address
     *
     * @param e email address
     */
    public final void setEmail(String e)
    {
	emailProperty().setValue(e);
    }

    /**
     * get email address
     *
     * @return email address
     */
    public final String getEmail()
    {
	return emailProperty().getValue();
    }

    /**
     * set email user name
     *
     * @param eu user name
     */
    public final void setEUser(String eu)
    {
	email_userProperty().setValue(eu);
    }

    /**
     * get email user name
     *
     * @return user name
     */
    public final String getEUser()
    {
	return email_userProperty().getValue();
    }

    /**
     * set email password
     *
     * @param ep password
     */
    public final void setEPw(String ep)
    {
	email_pwProperty().setValue(ep);
    }

    /**
     * get email password
     *
     * @return password
     */
    public final String getEPw()
    {
	return email_pwProperty().getValue();
    }

    /**
     * set smtp server address
     *
     * @param s server address
     */
    public final void setSmtp(String s)
    {
	smtpProperty().setValue(s);
    }

    /**
     * get smtp server address
     *
     * @return server address
     */
    public final String getSmtp()
    {
	return smtpProperty().getValue();
    }
}
