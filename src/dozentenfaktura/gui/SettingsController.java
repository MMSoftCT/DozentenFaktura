/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura.gui;

import dozentenfaktura.*;
import dozentenfaktura.datenbank.Einstellung;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        taBetreff.setWrapText(true);
        taEinleitung.setWrapText(true);
        taSchluss1.setWrapText(true);
        taSchluss2.setWrapText(true);
        taSchluss3.setWrapText(true);
    }

    /**
     * set data to input fields
     * @param d 
     */
    public void setData(Einstellung d)
    {
        data = d;
        if (!data.getLogo().isEmpty())
        {
            imgLogo.setImage(getImage(data.getLogo()));
        }

        if (!data.getUnterschrift().isEmpty())
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

    /**
     * get data
     * @return settings
     */
    public Einstellung getData()
    {
        return data;
    }
    
    @FXML
    private void loadLogo(ActionEvent event)
    {
        String logo = selectFile("Ihr Logo auswählen");
        imgLogo.setImage(getImage(logo));
        data.setLogo(logo);
    }

    @FXML
    private void loadSign(ActionEvent event)
    {
        String sign = selectFile("Ihre Unterschift auswählen");
        imgSign.setImage(getImage(sign));
        data.setUnterschrift(sign);
    }

    /**
     * get a image object fron filepath
     * @param path
     * @return 
     */
    private Image getImage(String path)
    {
        Image img = null;
        File file = new File(path);
        img = new Image(file.toURI().toString());
        return img;
    }

    /**
     * file selector for images
     * @param title
     * @return selected filename
     */
    private String selectFile(String title)
    {
        String file = "";
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File("."));
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Bilder", "*.png", "*.jpg")
        );
        File ret = chooser.showOpenDialog(DozentenFaktura.getStage());
        if (ret != null)
        {
            try
            {
                String wd = new File(".").getCanonicalPath() + "\\Images";
                if (!ret.getCanonicalPath().contains(wd))
                {
                    ret = copyFile(ret);
                }
                file = "Images\\" + ret.getName();
            } catch (IOException ex)
            {
                Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return file;
    }

    private File copyFile(File from) throws FileNotFoundException, IOException
    {
        File to = new File("Images\\" + from.getName());
        if (!to.exists())
        {
            System.out.println("Copy file from : " + from.getAbsolutePath() + "\n to : " + to.getCanonicalPath());
            InputStream in = new FileInputStream(from);
            OutputStream out = new FileOutputStream(to);
            byte buf[] = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        return to;
    }
}
