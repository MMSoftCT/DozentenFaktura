/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura.gui;

import dozentenfaktura.datenbank.Dozent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.beans.value.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author amederake
 */
public class DialogDozentController implements Initializable
{

    private Dozent data;
    private ButtonType save;
    private ButtonType cancel;
    private Button saveButton;
    @FXML
    private TextField txtSmtp;
    @FXML
    private TextField txtVorname;
    @FXML
    private TextField txtNachname;
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
    private TextField txtEUser;
    @FXML
    private PasswordField txtEPw;
    @FXML
    private Label lblTitle;
    @FXML
    private DialogPane dlgPane;
    @FXML
    private TextField txtSteuer;
    @FXML
    private TextField txtKInhaber;
    @FXML
    private TextField txtKBank;
    @FXML
    private TextField txtKIban;
    @FXML
    private TextField txtKBIC;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
	// add focus listener to fields for save button update and validation
	this.txtVorname.focusedProperty().addListener(new FocusListener(txtVorname));
	this.txtNachname.focusedProperty().addListener(new FocusListener(txtNachname));
	this.txtStrasse.focusedProperty().addListener(new FocusListener(txtStrasse));
	this.txtPlz.focusedProperty().addListener(new PlzFocusListener());
	this.txtOrt.focusedProperty().addListener(new FocusListener(txtOrt));
	this.txtTelefon.focusedProperty().addListener(new FocusListener(txtTelefon));
        this.txtSteuer.focusedProperty().addListener(new FocusListener(txtSteuer));
        this.txtKInhaber.focusedProperty().addListener(new FocusListener(txtKInhaber));
        this.txtKBank.focusedProperty().addListener(new FocusListener(txtKBank));
        this.txtKIban.focusedProperty().addListener(new IbanFocusListener());
        this.txtKBIC.focusedProperty().addListener(new FocusListener(txtKBIC));
	this.txtEmail.focusedProperty().addListener(new EmailFocusListener());
	this.txtEUser.focusedProperty().addListener(new FocusListener(txtEUser));
	this.txtEPw.focusedProperty().addListener(new FocusListener(txtEPw));
	this.txtSmtp.focusedProperty().addListener(new FocusListener(txtSmtp));

	// restrict plz and phone input to numeric
	this.txtPlz.addEventFilter(KeyEvent.KEY_TYPED, new NumericInput());
	this.txtTelefon.addEventFilter(KeyEvent.KEY_TYPED, new PhoneInput());

	// create and add buttons
	this.save = new ButtonType("Speichern", ButtonBar.ButtonData.OK_DONE);
	this.cancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);
	
	this.dlgPane.getButtonTypes().addAll(save, cancel);
	saveButton = (Button) dlgPane.lookupButton(save);
	saveButton.setDefaultButton(true);

    }

    /**
     * set textfield data from dozent objet
     *
     * @param doz dozent object
     */
    public void setData(Dozent doz)
    {
	data = doz;
	txtVorname.setText(data.getVorname());
	txtNachname.setText(data.getNachname());
	txtStrasse.setText(data.getStrasse());
	txtPlz.setText(data.getPlz());
	txtOrt.setText(data.getOrt());
	txtTelefon.setText(data.getTelefon());
        txtSteuer.setText(data.getSteuerId());
        txtKInhaber.setText(data.getKInhaber());
        txtKBank.setText(data.getKBank());
        txtKIban.setText(data.getKIban());
        txtKBIC.setText(data.getKBIC());
	txtEmail.setText(data.getEmail());
	txtEUser.setText(data.getEUser());
	txtEPw.setText(data.getEPw());
	txtSmtp.setText(data.getSmtp());
	bindToFields();
	txtVorname.requestFocus();
	UpdateUi();
    }

    /**
     * get dozent object filled from textfields
     *
     * @return
     */
    public Dozent getData()
    {
	return data;
    }

    /**
     * bind data propertys to text field propertys for automatical data update
     */
    private void bindToFields()
    {
	data.vornameProperty().bind(this.txtVorname.textProperty());
	data.nachnameProperty().bind(txtNachname.textProperty());
	data.strasseProperty().bind(txtStrasse.textProperty());
	data.plzProperty().bind(txtPlz.textProperty());
	data.ortProperty().bind(txtOrt.textProperty());
	data.telefonProperty().bind(txtTelefon.textProperty());
        data.steuer_idProperty().bind(txtSteuer.textProperty());
        data.k_inhaberProperty().bind(txtKInhaber.textProperty());
        data.k_bankProperty().bind(txtKBank.textProperty());
        data.k_ibanProperty().bind(txtKIban.textProperty());
        data.k_bicProperty().bind(txtKBIC.textProperty());
	data.emailProperty().bind(txtEmail.textProperty());
	data.email_userProperty().bind(txtEUser.textProperty());
	data.email_pwProperty().bind(txtEPw.textProperty());
	data.smtpProperty().bind(txtSmtp.textProperty());
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
	saveButton.setDisable(txtVorname.getText().isEmpty()
		|| txtNachname.getText().isEmpty()
		|| txtStrasse.getText().isEmpty()
		|| txtPlz.getText().isEmpty()
		|| txtOrt.getText().isEmpty()
		|| txtTelefon.getText().isEmpty()
		|| txtEmail.getText().isEmpty()
		|| txtEUser.getText().isEmpty()
		|| txtEPw.getText().isEmpty()
		|| txtSmtp.getText().isEmpty()
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
     * Event handler class for phone input
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
     * event handler class for IBAN focus change
     */
    private class IbanFocusListener implements ChangeListener<Boolean>
    {

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
	{
	    if (!newValue)
	    {
		// check IBAN format
		final Pattern EMAIL_REGEX = Pattern.compile("[A-Z]{2}\\d{2} ?\\d{4} ?\\d{4} ?\\d{4} ?\\d{4} ?[\\d]{0,2}");
		if (!EMAIL_REGEX.matcher(txtKIban.getText()).matches())
		{
		    Message("Das IBAN Format ist nicht gültig !");
		    txtKIban.requestFocus();
		} else
		{
		    UpdateUi();
		}
	    }
	    else
	    {
		txtKIban.selectAll();
	    }
	}
    }
}
