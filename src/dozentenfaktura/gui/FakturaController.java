/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura.gui;

import dozentenfaktura.*;
import dozentenfaktura.datenbank.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * FXML Controller class
 *
 * @author amederake
 */
public class FakturaController implements Initializable
{

    // class variables
    private DozentList dozenten;
    private KundeList kunden;
    private AuftragList auftraege;
    private RechnungList rechnungen;
    private EinstellungList einstellungen;
    private int currDozent;
    @FXML
    private Tab TabKunde;
    @FXML
    private Button KdBtnNeu;
    @FXML
    private Button KdBtnEdit;
    @FXML
    private TableView<Kunde> tblKunde;
    @FXML
    private TableColumn<Kunde, Integer> colKdNr;
    @FXML
    private TableColumn<Kunde, String> colKdName;
    @FXML
    private TableColumn<Kunde, String> colKdAddress;
    @FXML
    private MenuItem kdNeu;
    @FXML
    private MenuItem kdEdit;
    @FXML
    private MenuItem kdAuftrag;
    @FXML
    private Tab TabAuftrag;
    @FXML
    private Button AuBtnNeu;
    @FXML
    private Button AuBtnEdit;
    @FXML
    private TableView<Auftrag> tblAuftrag;
    @FXML
    private TableColumn<Auftrag, Integer> colAuNr;
    @FXML
    private TableColumn<Auftrag, String> colAuThema;
    @FXML
    private TableColumn<Auftrag, LocalDate> colAuVon;
    @FXML
    private TableColumn<Auftrag, LocalDate> colAuBis;
    @FXML
    private MenuItem auNeu;
    @FXML
    private MenuItem auEdit;
    @FXML
    private MenuItem auRechnung;
    @FXML
    private Tab TabRechnung;
    @FXML
    private Button ReBtnNeu;
    @FXML
    private Button ReBtnEdit;
    @FXML
    private TableView<Rechnung> tblRechnung;
    @FXML
    private TableColumn<Rechnung, Integer> colReNr;
    @FXML
    private TableColumn<Rechnung, LocalDate> colReDatum;
    @FXML
    private TableColumn<Rechnung, String> colReAuftrag;
    @FXML
    private TableColumn<Rechnung, Boolean> colReVersand;
    @FXML
    private MenuItem reNeu;
    @FXML
    private MenuItem reEdit;
    @FXML
    private MenuItem reSend;
    @FXML
    private TabPane main;
    @FXML
    private MenuItem doNeu;
    @FXML
    private MenuItem doEdit;
    @FXML
    private MenuItem doSettings;
    @FXML
    private MenuItem quit;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        dozenten = new DozentList();
        kunden = new KundeList();
        auftraege = new AuftragList();
        rechnungen = new RechnungList();
        einstellungen = new EinstellungList();
        currDozent = -1;

        // initalize gui objects
        initTabs();
        initTableViews();
        initButtons();
        initCtxMenus();

    }

    /**
     * update the user interface
     */
    public void UpdateUi()
    {
        // enable/disable buttons
        KdBtnEdit.setDisable(kunden.isEmpty()
                || tblKunde.getSelectionModel().selectedIndexProperty().get() == -1);
        AuBtnEdit.setDisable(auftraege.isEmpty()
                || tblAuftrag.getSelectionModel().selectedIndexProperty().get() == -1);
        ReBtnEdit.setDisable(rechnungen.isEmpty()
                || tblRechnung.getSelectionModel().selectedIndexProperty().get() == -1);

        // enable/disable context menue entrys
        kdAuftrag.setDisable(tblKunde.getSelectionModel().selectedIndexProperty().get() == -1);

        if (tblAuftrag.getSelectionModel().selectedIndexProperty().get() > -1)
        {
            Auftrag au = tblAuftrag.getSelectionModel().selectedItemProperty().getValue();
            auRechnung.setDisable(au.getAbgerechnetBis() == au.getEnd());
        }

        if (tblRechnung.getSelectionModel().selectedIndexProperty().get() > -1)
        {
            Rechnung re = tblRechnung.getSelectionModel().selectedItemProperty().get();
            reSend.setDisable(!re.getVersand());
        }
    }

    /**
     * start routine
     */
    public void Start()
    {
        // show main view
        DozentenFaktura.getStage().show();

        if (dozenten.isEmpty())  // the first dozent must be initialized
        {
            addDozent();
            if (dozenten.isEmpty())
            {
                System.exit(0);
            }
            currDozent = 0;
	    Einstellung set = new Einstellung();
	    set.setId(einstellungen.getIndex() + 1);
	    set.setDozent(currDozent);
	    einstellungen.add(set);
        } // this is prepaired for multi user
        //	else if (dozenten.size() > 1) // this is only for multi user
        //	{
        //	    Dozent doz = selectDozent();
        //	    if (doz != null)
        //	    {
        //		currDozent = dozenten.indexOf(doz);
        //	    } else
        //	    {
        //		System.exit(0);
        //	    }
        //	} 
        else
        {
            currDozent = 0;
        }

        // update table views items
        if (tblKunde.getItems().size() != kunden.size())
        {
            tblKunde.setItems(FXCollections.observableArrayList(kunden));
        }

        if (tblAuftrag.getItems().size() != auftraege.size())
        {
            tblAuftrag.setItems(FXCollections.observableArrayList(auftraege));
        }

        if (tblRechnung.getItems().size() != rechnungen.size())
        {
            tblRechnung.setItems(FXCollections.observableArrayList(rechnungen));
        }

        UpdateUi();
    }

    // initialize methodes
    /**
     * initialize tab pane
     */
    private void initTabs()
    {
        // set change listener for tabs
        main.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>()
        {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue)
            {
                if (newValue.equals(TabKunde))
                {
                    if (kunden.size() > 0)
                    {
                        tblKunde.getSelectionModel().select(0);
                    }
                }
                if (newValue.equals(TabAuftrag))
                {
                    if (auftraege.size() > 0)
                    {
                        tblAuftrag.getSelectionModel().select(0);
                    }
                }
                if (newValue.equals(TabRechnung))
                {
                    if (rechnungen.size() > 0)
                    {
                        tblRechnung.getSelectionModel().select(0);
                    }
                }
            }
        });
    }

    /**
     * initialize all table views
     */
    private void initTableViews()
    {
        // TableView Kunden
        tblKunde.setItems(FXCollections.observableArrayList(kunden));
        colKdNr.setCellValueFactory(new PropertyValueFactory<>("kdNr"));
        colKdName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colKdAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tblKunde.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Kunde>()
                {
                    @Override
                    public void changed(ObservableValue<? extends Kunde> observable, Kunde oldValue, Kunde newValue)
                    {
                        UpdateUi();
                    }
                });

        // TableView Aufträge
        tblAuftrag.setItems(FXCollections.observableArrayList(auftraege));
        colAuNr.setCellValueFactory(new PropertyValueFactory<>("auftragNr"));
        colAuThema.setCellValueFactory(new PropertyValueFactory<>("thema"));
        colAuVon.setCellValueFactory(new PropertyValueFactory<>("start"));
        colAuVon.setCellFactory(column
                -> new TableCell<Auftrag, LocalDate>()
        {
            @Override
            protected void updateItem(LocalDate item, boolean empty)
            {
                super.updateItem(item, empty);
                if (item == null || empty)
                {
                    setText(null);
                }
                else
                {
                    setText(item.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                }
            }
        });

        colAuBis.setCellValueFactory(
                new PropertyValueFactory<>("ende"));
        colAuBis.setCellFactory(column
                -> new TableCell<Auftrag, LocalDate>()
        {
            @Override
            protected void updateItem(LocalDate item, boolean empty
            )
            {
                super.updateItem(item, empty);
                if (item == null || empty)
                {
                    setText(null);
                }
                else
                {
                    setText(item.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                }
            }
        });

        tblAuftrag.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<Auftrag>()
                {
                    @Override
                    public void changed(ObservableValue<? extends Auftrag> observable, Auftrag oldValue,
                            Auftrag newValue
                    )
                    {
                        UpdateUi();
                    }
                }
                );

        // TableView Rechnungen
        tblRechnung.setItems(FXCollections.observableArrayList(rechnungen));
        colReNr.setCellValueFactory(
                new PropertyValueFactory<>("rechnungNr"));
        colReDatum.setCellValueFactory(
                new PropertyValueFactory<>("datum"));
        colReDatum.setCellFactory(column
                -> new TableCell<Rechnung, LocalDate>()
        {
            @Override
            protected void updateItem(LocalDate item, boolean empty
            )
            {
                super.updateItem(item, empty);
                if (item == null || empty)
                {
                    setText(null);
                }
                else
                {
                    setText(item.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                }
            }
        }
        );

        colReAuftrag.setCellValueFactory(
                new PropertyValueFactory<>("auftragNr"));
        colReVersand.setCellValueFactory(
                new PropertyValueFactory<>("versand"));
        colReVersand.setCellFactory(col
                -> new TableCell<Rechnung, Boolean>()
        {
            @Override
            protected void updateItem(Boolean item, boolean empty
            )
            {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "Ja" : "Nein");
            }
        }
        );
        tblRechnung.getSelectionModel()
                .selectedItemProperty()
                .addListener(new ChangeListener<Rechnung>()
                {
                    @Override
                    public void changed(ObservableValue<? extends Rechnung> observable, Rechnung oldValue,
                            Rechnung newValue
                    )
                    {
                        UpdateUi();
                    }
                }
                );

    }

    /**
     * inititalize all button handler
     */
    private void initButtons()
    {
        // set button action listener
        KdBtnNeu.setOnAction((ActionEvent event) ->
        {
            addKunde();
        });

        KdBtnEdit.setOnAction((ActionEvent event) ->
        {
            if (tblKunde.getSelectionModel().selectedIndexProperty().get() > -1)
            {
                editKunde(tblKunde.getSelectionModel().selectedItemProperty().get());
            }
            else
            {
                editKunde(null);
            }
        });

        AuBtnNeu.setOnAction((ActionEvent event) ->
        {
            addAuftrag(null);
        });

        AuBtnEdit.setOnAction((ActionEvent event) ->
        {
            if (tblAuftrag.getSelectionModel().selectedIndexProperty().get() > -1)
            {
                editAuftrag(tblAuftrag.getSelectionModel().selectedItemProperty().get());
            }
            else
            {
                editAuftrag(null);
            }
        });

        ReBtnNeu.setOnAction((ActionEvent event) ->
        {
            addRechnung(null);
        });

        ReBtnEdit.setOnAction((ActionEvent event) ->
        {
            if (tblRechnung.getSelectionModel().selectedIndexProperty().get() > -1)
            {
                editRechnung(tblRechnung.getSelectionModel().selectedItemProperty().get());
            }
            editRechnung(null);
        });

    }

    /**
     * initialize context menues
     */
    private void initCtxMenus()
    {
        // setup main context menue actions

        // multi user is disabled
        doNeu.setVisible(false);
//	doNeu.setOnAction((ActionEvent event) ->
//	{
//	    addDozent();
//	});

        doEdit.setOnAction((ActionEvent event) ->
        {
            Dozent doz = null;
            if (dozenten.size() == 1)
            {
                doz = dozenten.get(0);
            }
            // prepaired for multi user
//	    else
//	    {
//		doz = selectDozent();
//	    }
            if (doz != null)
            {
                editDozent(doz);
            }
        });

        doSettings.setOnAction((event) ->
        {
            editSettings();
        });
        // quit the programm
        quit.setOnAction((ActionEvent event) ->
        {
            System.exit(0);
        });

        // setup kunden context menue actions
        // add kunde
        kdNeu.setOnAction((ActionEvent event) ->
        {
            addKunde();
        });

        // edit kunde
        kdEdit.setOnAction((ActionEvent event) ->
        {
            if (tblKunde.getSelectionModel().selectedIndexProperty().get() > -1)
            {
                editKunde(tblKunde.getSelectionModel().selectedItemProperty().get());
            }
            else
            {
                editKunde(null);
            }
        });

        // add auftrag to kunde
        kdAuftrag.setOnAction((event) ->
        {
            if (tblKunde.getSelectionModel().selectedIndexProperty().get() > -1)
            {
                addAuftrag(tblKunde.getSelectionModel().selectedItemProperty().get());
            }
            else
            {
                addAuftrag(null);
            }
        });

        // setup auftrag context menue actions
        // add new auftrag with kunde selection
        auNeu.setOnAction((ActionEvent event) ->
        {
            addAuftrag(null);
        });

        // edit auftrag include kunde and auftrag selection if nothing selected
        auEdit.setOnAction((ActionEvent event) ->
        {
            if (tblAuftrag.getSelectionModel().selectedIndexProperty().get() > -1)
            {
                editAuftrag(tblAuftrag.getSelectionModel().selectedItemProperty().get());
            }
            else
            {
                editAuftrag(null);
            }
        });

        // add rechnung
        auRechnung.setOnAction((event) ->
        {
            if (tblAuftrag.getSelectionModel().selectedIndexProperty().get() > -1)
            {
                addRechnung(tblAuftrag.getSelectionModel().selectedItemProperty().get());
            }
            else
            {
                addRechnung(null);
            }
        });
        // setup Rechnung context menue actions

        // add new rechnung wit kunde and auftrrag selection
        reNeu.setOnAction((event) ->
        {
            addRechnung(null);
        });

        // edit rechnung with selection
        reEdit.setOnAction((ActionEvent event) ->
        {
            if (tblRechnung.getSelectionModel().selectedIndexProperty().get() > -1)
            {
                editRechnung(tblRechnung.getSelectionModel().selectedItemProperty().get());
            }
            else
            {
                editRechnung(null);
            }
        });

        // send rechnung via email
        reSend.setOnAction((ActionEvent event) ->
        {
            if (tblRechnung.getSelectionModel().selectedIndexProperty().get() > -1)
            {
                sendRechnung(tblRechnung.getSelectionModel().selectedItemProperty().get());
            }
        });

    }

    // dozent methodes
    /**
     * add a new dozent
     */
    private void addDozent()
    {
        try
        {
            Dozent doz = new Dozent();
            doz.setId(dozenten.getIndex() + 1);
            Dialog<ButtonType> dlg = new Dialog<>();
            FXMLLoader dlgLoader = new FXMLLoader(DozentenFaktura.class
                    .getResource("gui/DialogDozent.fxml"));
            DialogPane pane = (DialogPane) dlgLoader.load();
            dlg.setDialogPane(pane);
            DialogDozentController dlgHandle = (DialogDozentController) dlgLoader.getController();
            dlgHandle.setData(doz);
            dlgHandle.setHeaderText("Dozent anlegen");
            dlg.initOwner(DozentenFaktura.getStage());

            Optional<ButtonType> result = dlg.showAndWait();

            if (result.isPresent())
            {
                if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
                {
                    dozenten.add(dlgHandle.getData());
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * edit dozent data
     */
    private void editDozent(Dozent doz)
    {

        if (doz != null)
        {
            try
            {
                int index = dozenten.indexOf(doz);
                Dialog<ButtonType> dlg = new Dialog<>();
                FXMLLoader dlgLoader = new FXMLLoader(DozentenFaktura.class
                        .getResource("gui/DialogDozent.fxml"));
                DialogPane pane = (DialogPane) dlgLoader.load();
                dlg.setDialogPane(pane);
                DialogDozentController dlgHandle = (DialogDozentController) dlgLoader.getController();
                dlgHandle.setData(doz);
                dlgHandle.setHeaderText("Dozent bearbeiten");
                dlg.initOwner(DozentenFaktura.getStage());

                Optional<ButtonType> result = dlg.showAndWait();

                if (result.isPresent())
                {
                    if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
                    {
                        dozenten.set(index, dlgHandle.getData());
                    }
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void editSettings()
    {
        try
        {
            Einstellung set = einstellungen.getByDozentId(currDozent);
            if (set == null)
            {
                set = new Einstellung();
                set.setId(einstellungen.getIndex() + 1);
                set.setDozent(currDozent);
                einstellungen.add(set);
            }
            int index = einstellungen.indexOf(set);
            Dialog<ButtonType> dlg = new Dialog();
            FXMLLoader dlgLoader = new FXMLLoader(DozentenFaktura.class
                    .getResource("gui/Settings.fxml"));
            DialogPane pane = (DialogPane) dlgLoader.load();
            dlg.setDialogPane(pane);
            SettingsController ctrl = (SettingsController) dlgLoader.getController();
            ctrl.setData(set);
            dlg.initOwner(DozentenFaktura.getStage());
            
            Optional<ButtonType> result = dlg.showAndWait();
            
            if(result.isPresent())
            {
                if(result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
                {
                    einstellungen.set(index, set);
                }
            }
            
        } catch (IOException ex)
        {
            Logger.getLogger(FakturaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // button action methode
    /**
     * add a new kunde
     */
    private void addKunde()
    {
        try
        {
            Kunde kd = new Kunde();
            kd.setKdNr(kunden.getIndex() + 1);
            Dialog<ButtonType> dlg = new Dialog<>();
            FXMLLoader dlgLoader = new FXMLLoader(DozentenFaktura.class
                    .getResource("gui/DialogKunde.fxml"));
            DialogPane pane = (DialogPane) dlgLoader.load();
            dlg.setDialogPane(pane);
            DialogKundeController dlgHandle = (DialogKundeController) dlgLoader.getController();
            dlgHandle.setData(kd);
            dlgHandle.setHeaderText("Neuen Kunden anlegen");
            dlg.initOwner(DozentenFaktura.getStage());

            Optional<ButtonType> result = dlg.showAndWait();

            if (result.isPresent())
            {
                if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
                {
                    if (kunden.add(dlgHandle.getData()))
                    {
                        tblKunde.setItems(FXCollections.observableArrayList(kunden));
                    }
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        UpdateUi();
    }

    /**
     * edit kunde data
     */
    private void editKunde(Kunde kd)
    {
        if (kd == null)
        {
            kd = selectKunde();
        }

        if (kd != null)
        {
            int index = kunden.indexOf(kd);
            try
            {
                Dialog<ButtonType> dlg = new Dialog<>();
                FXMLLoader dlgLoader = new FXMLLoader(DozentenFaktura.class
                        .getResource("gui/DialogKunde.fxml"));
                DialogPane pane = (DialogPane) dlgLoader.load();
                dlg.setDialogPane(pane);
                DialogKundeController dlgHandle = (DialogKundeController) dlgLoader.getController();
                dlgHandle.setData(kd);
                dlgHandle.setHeaderText("Kunde bearbeiten");
                dlg.initOwner(DozentenFaktura.getStage());

                Optional<ButtonType> result = dlg.showAndWait();

                if (result.isPresent())
                {
                    if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
                    {
                        kunden.set(index, dlgHandle.getData());
                        tblKunde.setItems(FXCollections.observableArrayList(kunden));
                    }
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * ad a new auftrag
     */
    private void addAuftrag(Kunde kd)
    {
        Dozent doz = dozenten.get(currDozent);

        // select kunde for auftrag if more than one or none selected
        if (kd == null)
        {
            if (kunden.size() == 1)
            {
                kd = kunden.get(0);
            }
            else
            {
                kd = selectKunde();
            }
        }

        // create auftrag dialog
        if (kd != null)
        {
            try
            {
                // set basics for auftrag
                Auftrag au = new Auftrag();
                au.setAuftragNr(auftraege.getIndex() + 1);
                au.setKunde(kd.getKdNr());
                au.setDozent(doz.getId());

                // create dialog
                Dialog<ButtonType> dlg = new Dialog<>();
                FXMLLoader dlgLoader = new FXMLLoader(DozentenFaktura.class
                        .getResource("gui/DialogAuftrag.fxml"));
                DialogPane pane = (DialogPane) dlgLoader.load();
                dlg.setDialogPane(pane);
                DialogAuftragController dlgHandle = (DialogAuftragController) dlgLoader.getController();
                dlgHandle.setData(au);
                dlgHandle.setHeaderText("Neuen Auftrag anlegen");
                dlg.initOwner(DozentenFaktura.getStage());

                // show dialog and recieve result
                Optional<ButtonType> result = dlg.showAndWait();

                // check result and act on it
                if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
                {
                    // required setting for creating rechnung
                    au = dlgHandle.getData();
                    au.setAbgerechnetBis(Date.valueOf(au.getStart()));

                    // add auftrag to database and update table view
                    if (auftraege.add(au))
                    {
                        tblAuftrag.setItems(FXCollections.observableArrayList(auftraege));
                    }
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }
            UpdateUi();
        }
    }

    /**
     * edit auftrag data
     */
    private void editAuftrag(Auftrag au)
    {
        if (au == null)
        {
            Kunde kd = selectKunde();
            if (kd != null)
            {
                au = selectAuftrag(kd);
            }
        }

        if (au != null)
        {
            int index = auftraege.indexOf(au);
            try
            {
                Dialog<ButtonType> dlg = new Dialog<>();
                FXMLLoader dlgLoader = new FXMLLoader(DozentenFaktura.class
                        .getResource("gui/DialogAuftrag.fxml"));
                DialogPane pane = (DialogPane) dlgLoader.load();
                dlg.setDialogPane(pane);
                DialogAuftragController dlgHandle = (DialogAuftragController) dlgLoader.getController();
                dlgHandle.setData(au);
                dlgHandle.setHeaderText("Neuen Auftrag bearbeiten");
                dlg.initOwner(DozentenFaktura.getStage());

                Optional<ButtonType> result = dlg.showAndWait();

                if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
                {
                    auftraege.set(index, dlgHandle.getData());
                    tblAuftrag.setItems(FXCollections.observableArrayList(auftraege));
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        UpdateUi();

    }

    /**
     * add a new recnung
     */
    private void addRechnung(Auftrag au)
    {
        Rechnung re = new Rechnung();
        if (au == null)
        {
            Kunde kd = selectKunde();
            if (kd != null)
            {
                au = selectAuftrag(kd);
            }
        }

        if (au.getAbgerechnetBis() == au.getEnd())
        {
            Message("Der Auftrag ist bereits vollständig abgerechnet !");
            return;
        }
        if (au != null)
        {
            try
            {
                // set rechnung base data
                re.setRechnungNr(rechnungen.getIndex() + 1);
                re.setAuftragNr(au.getAuftragNr());
                re.setDozentId(dozenten.get(currDozent).getId());

                // set rechnung start date
                if (au.getAbgerechnetBis().isEqual(au.getStart()))
                {
                    re.setVon_Datum(Date.valueOf(au.getStart()));
                }
                else
                {
                    re.setVon_Datum(Date.valueOf(au.getAbgerechnetBis().plusDays(1)));
                }

                // set rechnung end date
                if (au.getEnd().isAfter(LocalDate.now()))
                {
                    re.setBis_Datum(Date.valueOf(LocalDate.now())); // it is not allowed to create invoice  the future
                }
                else
                {
                    re.setBis_Datum(Date.valueOf(au.getEnd()));
                }

                // set teilrechnung flag
                if (re.getVon_Datum().isEqual(au.getStart()) || re.getBis_Datum().isEqual(au.getEnd()))
                {
                    re.setTeilRechnung(false);
                }
                else
                {
                    re.setTeilRechnung(true);
                }

                // set einheiten
                if (!re.getKorrigiert() || re.getEinheiten() == 0)
                {
                    re.setEinheiten(countWorkDays(re.getVon_Datum(), re.getBis_Datum()) * 9);
                }

                // calculate rechnung summe
                re.setSumme(String.valueOf(Double.parseDouble(au.getHonorar().replace(",", ".")) * re.getEinheiten()));

                // create dialog
                Dialog<ButtonType> dlg = new Dialog<>();
                FXMLLoader dlgLoader = new FXMLLoader(DozentenFaktura.class
                        .getResource("gui/DialogRechnung.fxml"));
                DialogPane pane = (DialogPane) dlgLoader.load();
                dlg.setDialogPane(pane);
                DialogRechnungController dlgHandle = (DialogRechnungController) dlgLoader.getController();
                dlgHandle.setData(re);
                dlgHandle.setHeaderText("Neue Rechnung anlegen");
                dlg.initOwner(DozentenFaktura.getStage());

                Optional<ButtonType> result = dlg.showAndWait();

                if (result.isPresent())
                {
                    if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
                    {
                        re = dlgHandle.getData();
                        String file = "Rechnungen/Rechnung_" + String.format("%02d", re.getDatum().getMonthValue()) + "-" + String.valueOf(re.getDatum().getYear()) + ".pdf";
                        Invoice document = new Invoice(re);
                        if (document.createDocument(file))
                        {
                            re.setPath(file);
                        }
                        if (rechnungen.add(re))
                        {
                            // update table view rechnung
                            tblRechnung.setItems(FXCollections.observableArrayList(rechnungen));
                            // update auftrag data
                            au.setAbgerechnetBis(Date.valueOf(re.getBis_Datum()));
                            auftraege.set(auftraege.indexOf(au), au);
                            tblAuftrag.setItems(FXCollections.observableArrayList(auftraege));
                        }
                    }
                }

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        UpdateUi();
    }

    /**
     * edit rechnung data
     */
    private void editRechnung(Rechnung re)
    {
        try
        {
            int index = rechnungen.indexOf(re);
            Dialog<ButtonType> dlg = new Dialog<>();
            FXMLLoader dlgLoader = new FXMLLoader(DozentenFaktura.class
                    .getResource("gui/DialogRechnung.fxml"));
            DialogPane pane = (DialogPane) dlgLoader.load();
            dlg.setDialogPane(pane);
            DialogRechnungController dlgHandle = (DialogRechnungController) dlgLoader.getController();
            dlgHandle.setData(re);
            dlgHandle.setHeaderText("Rechnung bearbeiten");
            dlg.initOwner(DozentenFaktura.getStage());

            Optional<ButtonType> result = dlg.showAndWait();

            if (result.isPresent())
            {
                if (result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
                {
                    rechnungen.set(index, dlgHandle.getData());
                    tblRechnung.setItems(FXCollections.observableArrayList(rechnungen));
                    auftraege.getAuftragByNr(re.getAuftragNr()).setAbgerechnetBis(Date.valueOf(dlgHandle.getData().getBis_Datum()));
                    auftraege.set(auftraege.indexOf(auftraege.getAuftragByNr(re.getAuftragNr())), auftraege.getAuftragByNr(re.getAuftragNr()));
                    tblAuftrag.setItems(FXCollections.observableArrayList(auftraege));
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        UpdateUi();
    }

    /**
     * send rechnung via email
     */
    private void sendRechnung(Rechnung re)
    {
        SendMail mail = new SendMail();
        String empfaenger = kunden.getKundeByNr(auftraege.getAuftragByNr(re.getAuftragNr()).getKdNr()).getEmail();
        String betreff = "Rechnung für die Veranstaltung " + auftraege.getAuftragByNr(re.getAuftragNr()).getThema();
        String text = "Sehr geerhrte Damen und Herren,\n"
                + "anbei übersende ich Ihnen meine Rechnng für die im Betreff angegebene Veranstaltung. \n"
                + "/n"
                + "Mit freundlichen Grüßen\n"
                + "\n"
                + dozenten.get(currDozent).getVorname()
                + dozenten.get(currDozent).getNachname();

        String[] anhang =
        {
            re.getPath()
        };

        if (mail.sendMail(empfaenger, betreff, text, anhang))
        {
            Message("Rechnung wurder per email versand!");
        }
        else
        {
            Message("Der email versand ist gescheitert!");
        }

    }

    // helper methodes
    /**
     * selection dialog dozenten
     *
     * @return dozent object or null
     */
    private Dozent selectDozent()
    {
        Dozent ret = null;
        ArrayList<String> lst = dozenten.getDozentenNames();
        SelectionDialog dlg = new SelectionDialog();
        dlg.setHeaderText("Dozenten auswählen");
        dlg.setCbPrompt("Dozenten auswählen");
        dlg.setLabel("Dozenten :");
        dlg.setCbItems(lst);

        Optional<ButtonType> result = dlg.showAndWait();

        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
        {
            String sel = dlg.getSel();
            int idx = lst.indexOf(sel);
            ret = dozenten.get(idx);
        }

        return ret;
    }

    /**
     * selection dialog kunde
     *
     * @return kunde object or null
     */
    private Kunde selectKunde()
    {
        Kunde ret = null;
        ArrayList<String> lst = kunden.getKundenNames();
        SelectionDialog dlg = new SelectionDialog();
        dlg.setHeaderText("Kunde auswählen");
        dlg.setCbPrompt("Kunde auswählen");
        dlg.setLabel("Kunden :");
        dlg.setCbItems(lst);

        Optional<ButtonType> result = dlg.showAndWait();

        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
        {
            String sel = dlg.getSel();
            int idx = lst.indexOf(sel);
            ret = kunden.get(idx);
        }

        return ret;
    }

    /**
     * selection dialog auftrag from given kunde
     *
     * @param kd kunde object
     * @return auftrag object or null
     */
    private Auftrag selectAuftrag(Kunde kd)
    {
        Auftrag ret = null;
        ArrayList<Auftrag> lst = auftraege.getAuftragByKundeAndDozent(kd.getKdNr(), dozenten.get(currDozent).getId());
        HashMap<String, Integer> auMap = new HashMap<>();
        lst.forEach((t) ->
        {
            auMap.put(t.getThema() + ", " + t.getStart().toString(), t.getAuftragNr());
        });
        ArrayList<String> cbLst = (ArrayList) auMap.keySet();
        SelectionDialog dlg = new SelectionDialog();
        dlg.setHeaderText("Auftrag auswählen");
        dlg.setCbPrompt("Auftrag auswählen");
        dlg.setLabel("Aufträge :");
        dlg.setCbItems(cbLst);

        Optional<ButtonType> result = dlg.showAndWait();

        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE)
        {
            ret = auftraege.getAuftragByNr(auMap.get(dlg.getSel()));

        }

        return ret;
    }

    /**
     * return working day count
     *
     * @param start start date
     * @param end end date
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

    // setter / getter
    /**
     * get dozenten list
     *
     * @return dozent list
     */
    public DozentList getDozenten()
    {
        return dozenten;
    }

    /**
     * get kunden list
     *
     * @return kunden list
     */
    public KundeList getKunden()
    {
        return kunden;
    }

    /**
     * get auftraege list
     *
     * @return auftraege list
     */
    public AuftragList getAuftraege()
    {
        return auftraege;
    }

    /**
     * get rechnung list
     *
     * @return rechnung list
     */
    public RechnungList getRecnungen()
    {
        return rechnungen;

    }

    /**
     * get einstellungen list
     *
     * @return einstellungen list
     */
    public EinstellungList getEinstellungen()
    {
        return einstellungen;
    }

    // Helper classes
    /**
     * selection dialog with labeled combobox
     */
    private class SelectionDialog extends Dialog<ButtonType>
    {

        private HBox content;
        private Label lblSel;
        private ComboBox<String> cbSel;

        SelectionDialog()
        {
            cbSel = new ComboBox<>();
            lblSel = new Label();
            lblSel.setLabelFor(cbSel);
            lblSel.setPrefSize(120.0, 25.0);
            lblSel.setTextAlignment(TextAlignment.RIGHT);
            content = new HBox(10.0, lblSel, cbSel);
            this.getDialogPane().setContent(content);
            this.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        }

        public void setLabel(String txt)
        {
            lblSel.setText(txt);
        }

        public void setCbPrompt(String prompt)
        {
            cbSel.setPromptText(prompt);
        }

        public void setCbItems(ArrayList<String> items)
        {
            ObservableList<String> lst = FXCollections.observableArrayList(items);
            cbSel.setItems(lst);
        }

        public String getSel()
        {
            return cbSel.getSelectionModel().getSelectedItem();
        }
    }

    /**
     * send mail class
     */
    public class SendMail
    {

        /**
         * Liefert die E-Mail Konfiguration für das versenden von E-Mails.
         *
         * @return - {@link Properties} das befüllte Properties Objekt.
         */
        private Properties getMailProperties()
        {

            // Die Properties der JVM holen
            Properties properties = System.getProperties();

            // Postausgangsserver
            properties.setProperty("mail.smtp.host", dozenten.get(currDozent).getSmtp());

            // Benutzername
            properties.setProperty("mail.user", dozenten.get(currDozent).getEUser());

            // Passwort
            properties.setProperty("mail.password", dozenten.get(currDozent).getEPw());

            // Einstellungen für die SSL Verschlüsselte übermittlung von E-Mails
            properties.put("mail.smtps.auth", "true");
            properties.put("mail.smtps.**ssl.enable", "true");
            properties.put("mail.smtps.**ssl.required", "true");
            return properties;
        }

        public boolean sendMail(String empfaenger, String betreff, String text, String[] anhang)
        {
            // Versender der E-Mail
            String versender = dozenten.get(currDozent).getEmail();

            // Erstellt ein Session Objekt mit der E-Mail Konfiguration
            Session session = Session.getDefaultInstance(getMailProperties());
            // Optional, schreibt auf die Konsole / in das Log, die Ausgabe des
            // E-Mail Servers, dieses kann bei einer Fehleranalyse sehr Hilfreich
            // sein.
            session.setDebug(true);
            try
            {
                // Erstellt ein MimeMessage Objekt.
                MimeMessage message = new MimeMessage(session);

                // Setzt die E-Mail Adresse des Versenders in den E-Mail Header
                message.setFrom(new InternetAddress(versender));

                // Setzt die E-Mail Adresse des Empfängers in den E-Mail Header
                // hier können beliegig viele E-Mail Adressen hinzugefügt werden
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(empfaenger));

                // Der Empfänger erhält eine Kopie dieser E-Mail
                // message.addRecipient(Message.RecipientType.CC, new
                // InternetAddress(empfaenger));
                // Der Empfänger erhält eine "Blindkopie" dieser E-Mail d.h. er
                // sieht nicht wer diese E-Mail noch erhalten hat.
                // message.addRecipient(Message.RecipientType.BCC, new
                // InternetAddress(empfaenger));
                // Setzt den Betreff der E-Mail
                message.setSubject(betreff);

                // Erstellen des "Containers" für die Nachricht
                BodyPart messageBodyPart = new MimeBodyPart();

                // Setzen des Textes
                messageBodyPart.setText(text);

                // Erstellen eines Multipart Objektes für das ablegen des Textes
                Multipart multipart = new MimeMultipart();
                // Setzen des Textes
                multipart.addBodyPart(messageBodyPart);

                for (String anhang1 : anhang)
                {
                    // Erstellen eines Multipart Objektes für das ablegen einer Datei
                    messageBodyPart = new MimeBodyPart();
                    // Eine Datenquelle für die Datei anlegen und dem BodyPart zuweisen
                    DataSource source = new FileDataSource(anhang1);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(anhang1);
                    // Dem Mulipart Objekt den erstellen BodyPart hinzufügen
                    // dieses wird für jede Datei benötig welche der E-Mail hinzugefügt werden soll.
                    multipart.addBodyPart(messageBodyPart);
                }

                // Setzt den Inhalt der E-Mail, Text + Dateianhänge
                message.setContent(multipart);

                // E-Mail versenden
                Transport.send(message);
            } catch (MessagingException mex)
            {
                mex.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
