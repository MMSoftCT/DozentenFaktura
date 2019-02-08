/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura.gui;

import dozentenfaktura.*;
import dozentenfaktura.datenbank.*;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.value.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author amederake
 */
public class DialogRechnungController implements Initializable
{

    private Rechnung data;
    private ButtonType save;
    private ButtonType cancel;
    private Button saveButton;
    private double honorar;
    private int maxEinheiten;
    @FXML
    private DialogPane dlgPane;
    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtKunde;
    @FXML
    private TextField txtAuftrag;
    @FXML
    private TextField txtDozent;
    @FXML
    private DatePicker dpDatum;
    @FXML
    private CheckBox chbTeilrechnung;
    @FXML
    private DatePicker dpVon;
    @FXML
    private DatePicker dpBis;
    @FXML
    private TextField txtSumme;
    @FXML
    private TextField txtEinheiten;
    @FXML
    private CheckBox chbKorrekt;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
	// set listener to fields
	dpDatum.focusedProperty().addListener(new FocusListener());

	chbTeilrechnung.selectedProperty().addListener(new ChangeListener<Boolean>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	    {
		dpBis.setDisable(!newValue);
	    }
	});

	chbKorrekt.selectedProperty().addListener(new ChangeListener<Boolean>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	    {
		txtEinheiten.setDisable(!newValue);
		if (newValue) 
		{
		    txtEinheiten.requestFocus();
		}
	    }
	});

	dpBis.focusedProperty().addListener(new ChangeListener<Boolean>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	    {
		if (!newValue) // lost focus
		{
		    if (dpVon.getValue().isAfter(dpBis.getValue()) || dpVon.getValue().isEqual(dpBis.getValue()))
		    {
			Message("Das End-Datum muß nach dem Start-Datum liegen!");
			dpBis.requestFocus();
		    } else
		    {
			maxEinheiten = (countWorkDays(data.getVon_Datum(), data.getBis_Datum()) * 9);
			txtEinheiten.setText(String.valueOf(maxEinheiten));
			chbKorrekt.setSelected(false);
			txtSumme.setText(String.valueOf(honorar * maxEinheiten));
			UpdateUi();
		    }
		}
		else
		{
		    dpBis.getEditor().selectAll();
		}
	    }
	});

	txtEinheiten.focusedProperty().addListener(new ChangeListener<Boolean>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	    {
		if (!newValue)
		{
		    if (Integer.parseInt(txtEinheiten.getText()) > maxEinheiten)
		    {
			Message("Die Eingabe darf nicht größer als der berechnete Wert (" + maxEinheiten + ") sein!");
			txtEinheiten.requestFocus();
		    }
		    else
		    {
			txtSumme.setText(String.valueOf(honorar * Integer.parseInt(txtEinheiten.getText())));
		    }
			    

		}
		else
		{
		    txtEinheiten.selectAll();
		}
	    }
	});

	// restrict einheiten input to numbers
	txtEinheiten.addEventFilter(KeyEvent.KEY_TYPED, new NumericInput());
	// create and add buttons
	save = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
	cancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
	dlgPane.getButtonTypes().addAll(save, cancel);

	// set action event for save button
	saveButton = (Button) dlgPane.lookupButton(save);
	saveButton.setDefaultButton(true);

    }

    /**
     * set textfield data from rechnung object
     *
     * @param re rechnung object
     */
    public void setData(Rechnung re)
    {
	data = re;
	Auftrag au = DozentenFaktura.getMainHandle().getAuftraege().getAuftragByNr(re.getAuftragNr());
	Kunde kd = DozentenFaktura.getMainHandle().getKunden().getKundeByNr(au.getKdNr());
	Dozent doz = DozentenFaktura.getMainHandle().getDozenten().getDozentById(au.getDozent());
	honorar = Double.parseDouble(au.getHonorar().replace(",", "."));
	txtAuftrag.setText(au.getThema() + ", " + au.getStart());
	txtKunde.setText(kd.getName() + ", " + kd.getAddress());
	txtDozent.setText(doz.getVorname() + " " + doz.getNachname());
	if (data.getDatum() != null)
	{
	    dpDatum.setValue(data.getDatum());
	} else
	{
	    dpDatum.setValue(LocalDate.now());
	}
	chbTeilrechnung.setSelected(data.getTeilRechnung());
	chbKorrekt.setSelected(data.getKorrigiert());
	chbKorrekt.setVisible(!au.getBerechnet());  // only visible if auftrag->einheiten are corrected
	if (data.getVon_Datum() != null)
	{
	    dpVon.setValue(data.getVon_Datum());
	    dpBis.setValue(data.getBis_Datum());
	    maxEinheiten = countWorkDays(dpVon.getValue(), dpBis.getValue());
	}
	txtEinheiten.setText(String.valueOf(data.getEinheiten()));
	txtSumme.setText(data.getSumme());

	bindToFields();
	dpDatum.requestFocus();
	UpdateUi();
    }

    /**
     * get rechnung object filled from textfields
     *
     * @return rechnung object
     */
    public Rechnung getData()
    {
	return data;
    }

    /**
     * bind data propertys to field propertys
     */
    private void bindToFields()
    {
	data.datumProperty().bind(dpDatum.valueProperty());
	data.teilRechnungProperty().bind(chbTeilrechnung.selectedProperty());
	data.von_datumProperty().bind(dpVon.valueProperty());
	data.bis_datumProperty().bind(dpBis.valueProperty());
	StringConverter<Number> converter = new NumberStringConverter();
	Bindings.bindBidirectional(txtEinheiten.textProperty(), data.einheitenProperty(), converter);
	data.korrigiertProperty().bind(chbKorrekt.selectedProperty());
	data.summeProperty().bind(txtSumme.textProperty());
    }

    /**
     * set text of dialog header
     *
     * @param txt dialog title
     */
    public void setHeaderText(String txt)
    {
	lblTitle.setText(txt);
    }

    /**
     * enable save button only if all fields are filled
     */
    private void UpdateUi()
    {
	saveButton.setDisable(dpDatum.getValue().toString().isEmpty()
		|| dpVon.getValue().toString().isEmpty()
		|| dpBis.getValue().toString().isEmpty()
		|| txtEinheiten.getText().isEmpty()
		|| txtSumme.getText().isEmpty()
	);
    }

    /**
     * return working day count
     *
     * @param start start date
     * @param end   end date
     * @return vcount of working days
     */
    private int countWorkDays(LocalDate start, LocalDate end)
    {
	Calendar from = Calendar.getInstance();
	Calendar to = Calendar.getInstance();
	from.setTime(Date.valueOf(start));
	to.setTime(Date.valueOf(end));
	int wd = 0;
	while (!from.after(to))
	{
	    int day = from.get(Calendar.DAY_OF_WEEK);
	    if (day != Calendar.SATURDAY || day != Calendar.SUNDAY)
	    {
		wd++;
	    }
	    from.add(Calendar.DAY_OF_MONTH, 1);
	}
	return wd;
    }

    /**
     * show alert dialog with given message
     *
     * @param msg alert message
     */
    private void Message(String msg)
    {
	Alert dlg = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
	dlg.showAndWait();
    }

    // Event classes
    /**
     * Event handler class for numeric input
     */
    private class NumericInput implements EventHandler<KeyEvent>
    {

	@Override
	public void handle(KeyEvent event)
	{
	    if (!"0123456789".contains(event.getCharacter()))
	    {
		event.consume();
	    }
	}
    }

    /**
     * event handler class for focus change
     * <p>
     */
    private class FocusListener implements ChangeListener<Boolean>
    {

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
	    if (!newValue)
	    {
		UpdateUi();
	    }
	}
    }

}
