/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura.gui;

import dozentenfaktura.datenbank.Kunde;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author amederake
 */
public class DialogKundeController implements Initializable
{

    private Kunde data;
    private ButtonType save;
    private ButtonType cancel;
    private Button saveButton;
    @FXML
    private Label lblTitle;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtStrasse;
    @FXML
    private TextField txtPlz;
    @FXML
    private TextField txtOrt;
    @FXML
    private TextField txtTelefon;
    @FXML
    private TextField txtEmail;
    @FXML
    private ComboBox<String> cbAnrede;
    @FXML
    private TextField txtAnVorname;
    @FXML
    private TextField txtAnTelefon;
    @FXML
    private TextField txtAnNachname;
    @FXML
    private DialogPane dlgPane;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
	// add focus listener for save button update
	txtName.focusedProperty().addListener(new FocusListener(txtName));
	txtStrasse.focusedProperty().addListener(new FocusListener(txtStrasse));
	txtPlz.focusedProperty().addListener(new PlzFocusListener());
	txtOrt.focusedProperty().addListener(new FocusListener(txtOrt));
	txtTelefon.focusedProperty().addListener(new FocusListener(txtTelefon));
	txtEmail.focusedProperty().addListener(new EmailFocusListener());

	// fill anrede combobox
	ArrayList<String> tmp = new ArrayList<>();
	tmp.add("Frau");
	tmp.add("Herr");
	cbAnrede.setItems(FXCollections.observableArrayList(tmp));
	cbAnrede.focusedProperty().addListener(new AnredeFocusListener());

	// restrict plz and phone input to numeric
	txtPlz.addEventFilter(KeyEvent.KEY_TYPED, new NumericInput());
	txtTelefon.addEventFilter(KeyEvent.KEY_TYPED, new PhoneInput());
	txtAnTelefon.addEventFilter(KeyEvent.KEY_TYPED, new PhoneInput());

	// create and add buttons
	save = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
	cancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
	dlgPane.getButtonTypes().addAll(save, cancel);

	saveButton = (Button) dlgPane.lookupButton(save);
    }

    /**
     * set textfield data from kunde object
     *
     * @param kd kunde object
     */
    public void setData(Kunde kd)
    {
	data = kd;
	txtName.setText(data.getName());
	txtStrasse.setText(data.getStrasse());
	txtPlz.setText(data.getPlz());
	txtOrt.setText(data.getOrt());
	txtTelefon.setText(data.getTelefon());
	txtEmail.setText(data.getEmail());
	if (data.getApAnrede() != null)
	{
	    cbAnrede.setValue(data.getApAnrede());
	}
	txtAnVorname.setText(data.getApVorname());
	txtAnNachname.setText(data.getApNachname());
	txtAnTelefon.setText(data.getApTelefon());
	bindToFields();
	txtName.requestFocus();
	UpdateUi();
    }

    /**
     * get kunde object filled from textfields
     *
     * @return kunde object
     */
    public Kunde getData()
    {
	return data;
    }

    /**
     * bind data propertys to text field propertys for automatical data update
     */
    private void bindToFields()
    {
	data.nameProperty().bind(txtName.textProperty());
	data.strasseProperty().bind(txtStrasse.textProperty());
	data.plzProperty().bind(txtPlz.textProperty());
	data.ortProperty().bind(txtOrt.textProperty());
	data.telefonProperty().bind(txtTelefon.textProperty());
	data.emailProperty().bind(txtEmail.textProperty());
	data.apAnredeProperty().bind(cbAnrede.valueProperty());
	data.apVornameProperty().bind(txtAnVorname.textProperty());
	data.apNachnameProperty().bind(txtAnNachname.textProperty());
	data.apTelefonProperty().bind(txtAnTelefon.textProperty());
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
	saveButton.setDisable(txtName.getText().isEmpty()
		|| txtStrasse.getText().isEmpty()
		|| txtPlz.getText().isEmpty()
		|| txtOrt.getText().isEmpty()
		|| txtTelefon.getText().isEmpty()
		|| txtEmail.getText().isEmpty()
	);
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
     * Event handler class for numeric input
     */
    private class PhoneInput implements EventHandler<KeyEvent>
    {

	@Override
	public void handle(KeyEvent event)
	{
	    if (!"() 0123456789".contains(event.getCharacter()))
	    {
		event.consume();
	    }
	}
    }

    /**
     * event handler class for focus change
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
	    }
	    else
	    {
		field.selectAll();
	    }
	}
    }

    /**
     * event handler class for plz focus change
     */
    private class PlzFocusListener implements ChangeListener<Boolean>
    {

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
	    if (!newValue)
	    {
		if (txtPlz.getText().length() != 5)
		{
		    Message("Die Postleitzahl muß 5 stellig eingegeben werden !");
		    txtPlz.requestFocus();
		} else
		{
		    UpdateUi();
		}
	    }
	    else
	    {
		txtPlz.selectAll();
	    }
	}

    }

    /**
     * event handler class for email focus change
     */
    private class EmailFocusListener implements ChangeListener<Boolean>
    {

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
	    if (!newValue)
	    {
		// check email format
		final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
		if (!EMAIL_REGEX.matcher(txtEmail.getText()).matches())
		{
		    Message("Das EmailFormat ist nicht gültig !");
		    txtEmail.requestFocus();
		} else
		{
		    UpdateUi();
		}
	    }
	    else
	    {
		txtEmail.selectAll();
	    }
	}
    }

   /**
     * event handler class for combobox anrede focus change
     */
    private class AnredeFocusListener implements ChangeListener<Boolean>
    {

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
	    if (!newValue)
	    {
		boolean enable = cbAnrede.getSelectionModel().selectedIndexProperty().get() > -1;
		txtAnVorname.setDisable(enable);
		txtAnNachname.setDisable(enable);
		txtAnTelefon.setDisable(enable);
	    }
	    else
	    {
		cbAnrede.getEditor().selectAll();
	    }
	}
    }

    
}
