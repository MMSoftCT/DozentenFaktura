/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dozentenfaktura.gui;

import javafx.beans.property.*;

/**
 *
 * @author amederake
 */
public class TblKunde 
{
    private IntegerProperty kdNr;
    public void setKdNr(int value) {kdNrProperty().set(value);}
    public Integer getKdNr() { return kdNrProperty().get();};
    public IntegerProperty kdNrProperty()
    {
        if(kdNr == null) kdNr = new SimpleIntegerProperty(this,"kdNr");
        return kdNr;
    }

    private StringProperty name;
    public void setName(String value) {nameProperty().set(value);}
    public String getName() { return nameProperty().get();};
    public StringProperty nameProperty()
    {
        if(name == null) name = new SimpleStringProperty(this,"name");
        return name;
    }

    private StringProperty address;
    public void setAddress(String value) {addressProperty().set(value);}
    public String getAddress() { return addressProperty().get();};
    public StringProperty addressProperty()
    {
        if(address == null) address = new SimpleStringProperty(this,"address");
        return address;
    }
}
