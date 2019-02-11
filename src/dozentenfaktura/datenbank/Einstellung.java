/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dozentenfaktura.datenbank;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author amederake
 */
public final class Einstellung 
{
    private IntegerProperty id = new SimpleIntegerProperty(0);
    private IntegerProperty dozent = new SimpleIntegerProperty(0);
    private StringProperty logo = new SimpleStringProperty("");
    private StringProperty betreff = new SimpleStringProperty("");
    private StringProperty anrede = new SimpleStringProperty("");
    private StringProperty einleitung = new SimpleStringProperty("");
    private StringProperty schluss1 = new SimpleStringProperty("");
    private StringProperty schluss2 = new SimpleStringProperty("");
    private StringProperty schluss3 = new SimpleStringProperty("");
    private StringProperty schluss4 = new SimpleStringProperty("");
    private StringProperty unterschrift = new SimpleStringProperty("");
    
    public Einstellung() 
    {
        betreffProperty().setValue("Abrechnung Unterrichtsstunden freie Mitarbeit auf Grundlage der gültigen Honorarvereinbarung für die Veranstaltung [thema] ");
        anredeProperty().setValue("Sehr geehrte Damen und Herren,");
        einleitungProperty().setValue("hiermit übersende ich Ihnen die Rechnung zur Dozententätigkeit für den Zeitraum [start] bis [ende] für die o.g. Veranstalltung.");
        schluss1Property().setValue("Umsatzsteuerfrei nach § 4 Nr. 21 Buchstabe a) bb) i.V.m. § 34 AFG bzw. SGB III");
        schluss2Property().setValue("Der Rechnungsbetrag ist entsprechend der geltenden Vertragsvereinbarung ohne Abzug zu zahlen bis zum 21.ten Tag nach Seminarende auf das unten angegebene Konto.");
        schluss3Property().setValue("Bei Überweisungen bitte immer die Rechnungsnummer angeben!");
        schluss4Property().setValue("Ich danke Ihnen für Ihren Auftrag.");
    }

    public IntegerProperty idProperty()
    {
        return id;
    }
    
    public IntegerProperty dozentProperty()
    {
        return dozent;
    }
    
    public StringProperty logoProperty()
    {
        return logo;
    }
    
    public StringProperty betreffProperty()
    {
        return betreff;
    }
    
    public StringProperty anredeProperty()
    {
        return anrede;
    }
    
    public StringProperty einleitungProperty()
    {
        return einleitung;
    }
    
    public StringProperty schluss1Property()
    {
        return schluss1;
    }
    
    public StringProperty schluss2Property()
    {
        return schluss2;
    }
    
    public StringProperty schluss3Property()
    {
        return schluss3;
    }
    
    public StringProperty schluss4Property()
    {
        return schluss4;
    }
    
    public StringProperty unterschriftProperty()
    {
        return unterschrift;
    }
    
    /**
     * get id
     * @return id 
     */
    public int getId()
    {
        return idProperty().getValue();
    }
    
    /**
     * set id
     * @param value id
     */
    public void setId(int value)
    {
        idProperty().set(value);
    }
    
    /**
     * get dozent id
     * @return dozent id
     */
    public int getDozent()
    {
        return dozentProperty().getValue();
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
     * get logo
     * @return logo string
     */
    public String getLogo()
    {
        return logoProperty().getValue();
    }

    /**
     * set logo
     * @param value logo string
     */
    public void setLogo(String value)
    {
        logoProperty().set(value);
    }
    
    /**
     * get betreff
     * @return betreff string
     */
    public String getBetreff()
    {
        return betreffProperty().getValue();
    }

    /**
     * set betreff
     * @param value betreff string
     */
    public void setBetreff(String value)
    {
        betreffProperty().set(value);
    }
    
    /**
     * get anrede
     * @return anrede string
     */
    public String getAnrede()
    {
        return anredeProperty().getValue();
    }

    /**
     * set anrede
     * @param value anrede string
     */
    public void setAnrede(String value)
    {
        anredeProperty().set(value);
    }
    
    /**
     * get einleitung
     * @return einleitung string
     */
    public String getEinleitung()
    {
        return einleitungProperty().getValue();
    }

    /**
     * set einleitung
     * @param value einleitung string
     */
    public void setEinleitung(String value)
    {
        einleitungProperty().set(value);
    }
    
    /**
     * get schluss1
     * @return schluss1 string
     */
    public String getSchluss1()
    {
        return schluss1Property().getValue();
    }

    /**
     * set schluss1
     * @param value schluss1 string
     */
    public void setSchluss1(String value)
    {
        schluss1Property().set(value);
    }
    
    /**
     * get schluss2
     * @return schluss2 string
     */
    public String getSchluss2()
    {
        return schluss2Property().getValue();
    }

    /**
     * set schluss2
     * @param value schluss2 string
     */
    public void setSchluss2(String value)
    {
        schluss2Property().set(value);
    }
    
    /**
     * get schluss3
     * @return schluss3 string
     */
    public String getSchluss3()
    {
        return schluss3Property().getValue();
    }

    /**
     * set schluss3
     * @param value schluss3 string
     */
    public void setSchluss3(String value)
    {
        schluss3Property().set(value);
    }
    
    /**
     * get schluss4
     * @return schluss4 string
     */
    public String getSchluss4()
    {
        return schluss4Property().getValue();
    }

    /**
     * set schluss4
     * @param value schluss4 string
     */
    public void setSchluss4(String value)
    {
        schluss4Property().set(value);
    }
    
    /**
     * get unterschrift
     * @return unterschrift string
     */
    public String getUnterschrift()
    {
        return unterschriftProperty().getValue();
    }

    /**
     * set unterschrift
     * @param value unterschrift string
     */
    public void setUnterschrift(String value)
    {
        unterschriftProperty().set(value);
    }
    
}
