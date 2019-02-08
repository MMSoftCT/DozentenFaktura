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
import javafx.event.*;
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
public class DialogAuftragController implements Initializable
{

    private Auftrag data;
    private ButtonType save;
    private ButtonType cancel;
    private Button saveButton;
    @FXML
    private TextField txtKunde;
    @FXML
    private TextField txtDozent;
    @FXML
    private TextField txtThema;
    @FXML
    private DatePicker dpStart;
    @FXML
    private DatePicker dpEnde;
    @FXML
    private TextField txtEinheiten;
    @FXML
    private TextField txtHonorar;
    @FXML
    private DialogPane dlgPane;
    @FXML
    private Label lblTitle;
    @FXML
    private CheckBox chbBerechnet;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
	data = new Auftrag();

	// set listener for fields
	txtThema.focusedProperty().addListener(new FocusListener(txtThema));

	// special change events for DatePicker
	dpStart.focusedProperty().addListener(new ChangeListener<Boolean>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	    {
		if (!newValue)
		{
		    // check if start date is before end date
		    if (dpStart.getValue().isBefore(dpEnde.getValue()))
		    {
			txtEinheiten.setText(String.valueOf(countWorkDays(dpStart.getValue(), dpEnde.getValue()) * 9));
		    }
		} else
		{
		    dpStart.getEditor().selectAll();
		}
	    }
	});

	dpEnde.focusedProperty().addListener(new ChangeListener<Boolean>()
	{
	    @Override
	    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	    {
		if (!newValue)
		{
		    // check if start date is greater than end date
		    if (dpStart.getValue().isAfter(dpEnde.getValue()) || dpStart.getValue().isEqual(dpEnde.getValue()))
		    {
			Message("Das End-Datum muß nach dem Start-Datum liegen!");
			dpEnde.requestFocus();
		    } else    // update eineiten value
		    {
			txtEinheiten.setText(String.valueOf(countWorkDays(dpStart.getValue(), dpEnde.getValue()) * 9));
		    }

		}
		else
		{
		    dpEnde.getEditor().selectAll();
		}
	    }
	});

	txtEinheiten.focusedProperty().addListener(new FocusListener(txtEinheiten));
	txtEinheiten.addEventFilter(KeyEvent.KEY_TYPED, new NumericInput());

	chbBerechnet.selectedProperty().addListener((observable, oldValue, newValue) ->
	{
	    txtEinheiten.setDisable(newValue);
	    if(!newValue)
	    {
		txtEinheiten.requestFocus();
	    }
	});
	txtHonorar.focusedProperty().addListener(new FocusListener(txtHonorar));
	txtHonorar.addEventFilter(KeyEvent.KEY_TYPED, new DoubleInput());

	// create and add buttons
	save = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
	cancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
	dlgPane.getButtonTypes().addAll(save, cancel);

	saveButton = (Button) dlgPane.lookupButton(save);
	saveButton.setDefaultButton(true);
    }

    // dialog methodes
    /**
     * set textfield data from auftrag object
     *
     * @param au auftrag object
     */
    public void setData(Auftrag au)
    {
	Kunde kd = DozentenFaktura.getMainHandle().getKunden().getKundeByNr(au.getKdNr());
	Dozent doz = DozentenFaktura.getMainHandle().getDozenten().getDozentById(au.getKdNr());
	txtKunde.setText(kd.getName() + ", " + kd.getAddress());
	txtDozent.setText(doz.getVorname() + " " + doz.getNachname());
	txtThema.setText(au.getThema());
	if (au.startProperty().get() != null)
	{
	    dpStart.setValue(au.getStart());
	    dpEnde.setValue(au.getEnd());
	    txtEinheiten.setText(String.valueOf(au.getEinheiten()));
	}
	chbBerechnet.setSelected(au.getBerechnet());
	txtHonorar.setText(au.getHonorar());
	bindToFields();
	txtThema.requestFocus();
	UpdateUi();
    }

    /**
     * get auftrag object filled from textfields
     *
     * @return auftrag object
     */
    public Auftrag getData()
    {
	return data;
    }

    private void bindToFields()
    {
	data.themaProperty().bind(txtThema.textProperty());
	data.startProperty().bind(dpStart.valueProperty());
	data.endeProperty().bind(dpEnde.valueProperty());
	data.berechnetProperty().bind(chbBerechnet.selectedProperty());
	data.honorarProperty().bind(txtHonorar.textProperty());

	StringConverter<Number> converter = new NumberStringConverter();
	Bindings.bindBidirectional(txtEinheiten.textProperty(), data.einheitenProperty(), converter);
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
	saveButton.setDisable(txtThema.getText().isEmpty()
		|| dpStart.getValue() == null
		|| dpEnde.getValue() == null
		|| txtEinheiten.getText().isEmpty()
		|| txtHonorar.getText().isEmpty()
	);
    }

    // helper methodes
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
     * event handler class for focus change
     * <p>
     */
    private class FocusListener implements ChangeListener<Boolean>
    {

	private final TextField field;

	public FocusListener(TextField field)
	{
	    this.field = field;
	}

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
	    if (!newValue)
	    {
		UpdateUi();
	    } else
	    {
		field.selectAll();
	    }
	}
    }

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
     * Event handler class for double input
     */
    private class DoubleInput implements EventHandler<KeyEvent>
    {

	@Override
	public void handle(KeyEvent event)
	{
	    if (!"0123456789,".contains(event.getCharacter()))
	    {
		event.consume();
	    }
	}
    }

}
