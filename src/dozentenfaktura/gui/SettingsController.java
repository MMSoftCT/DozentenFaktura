/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura.gui;

import dozentenfaktura.*;
import dozentenfaktura.datenbank.Einstellung;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author amederake
 */
public class SettingsController implements Initializable
{

    private Einstellung data;

    @FXML
    private Label lblTitle;
    @FXML
    private ImageView imgLogo;
    @FXML
    private Button btnLogo;
    @FXML
    private ImageView imgSign;
    @FXML
    private Button btnSign;
    @FXML
    private Label lblTitle1;
    @FXML
    private TextArea taBetreff;
    @FXML
    private TextArea taAnrede;
    @FXML
    private TextArea taEinleitung;
    @FXML
    private TextArea taSchluss1;
    @FXML
    private TextArea taSchluss2;
    @FXML
    private TextArea taSchluss3;
    @FXML
    private TextArea taSchluss4;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    public void setData(Einstellung d)
    {
        data = d;
        if (data.getLogo().isEmpty())
        {

        }
        else
        {
            imgLogo.setImage(getImage(data.getLogo()));
        }

        if (data.getUnterschrift().isEmpty())
        {

        }
        else
        {
            imgSign.setImage(getImage(data.getUnterschrift()));
        }

        taBetreff.setText(data.getBetreff());
        data.betreffProperty().bind(taBetreff.textProperty());

        taAnrede.setText(data.getAnrede());
        data.anredeProperty().bind(taAnrede.textProperty());

        taEinleitung.setText(data.getEinleitung());
        data.einleitungProperty().bind(taEinleitung.textProperty());

        taSchluss1.setText(data.getSchluss1());
        data.schluss1Property().bind(taSchluss1.textProperty());

        taSchluss2.setText(data.getSchluss2());
        data.schluss2Property().bind(taSchluss2.textProperty());

        taSchluss3.setText(data.getSchluss3());
        data.schluss3Property().bind(taSchluss3.textProperty());

        taSchluss4.setText(data.getSchluss4());
        data.schluss4Property().bind(taSchluss4.textProperty());

    }

    @FXML
    private void loadLogo(ActionEvent event)
    {
        String logo = selectFile("Ihr Logo auswählen");
    }

    @FXML
    private void loadSign(ActionEvent event)
    {
        String sign = selectFile("Ihre Unterschift auswählen");
    }

    private Image getImage(String path)
    {
        Image img = null;
        img = new Image(path);
        return img;
    }

    private String selectFile(String title)
    {
        String file = "";
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File("/"));
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        File ret = chooser.showOpenDialog(DozentenFaktura.getStage());
        if(ret != null)
        {
            file = ret.getPath();
        }
        return file;
    }
}
