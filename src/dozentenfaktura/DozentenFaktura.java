/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dozentenfaktura;

import dozentenfaktura.datenbank.*;
import dozentenfaktura.gui.FakturaController;
import dozentenfaktura.preloader.AppNotification;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author amederake
 */
public class DozentenFaktura extends Application
{
    // class variables
    private static FakturaController mainHandle;
    private final BooleanProperty ready = new SimpleBooleanProperty(false);
    private static Stage stage;

    /**
     * set main stage
     * @param primaryStage 
     */
    private void setStage(Stage primaryStage )
    {
	DozentenFaktura.stage = primaryStage;
    }
    
    /**
     * get main stage
     * @return stage
     */
    static public Stage getStage()
    {
	return DozentenFaktura.stage;
    }
    
    /**
     * set main controller
     * @param ctrl 
     */
    private void setMainHandle(FakturaController ctrl)
    {
        DozentenFaktura.mainHandle = ctrl;
    }
    
    /**
     * get main handle
     * @return main handle
     */
    static public FakturaController getMainHandle()
    {
        return DozentenFaktura.mainHandle;
    }
    
    @Override
    public void start(Stage primaryStage)
    {
	try
	{
	    setStage(primaryStage);
	    
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/Faktura.fxml"));
	    Parent root = loader.load();
	    setMainHandle((FakturaController) loader.getController());
	    
	    longInit();
	    
	    Scene scene = new Scene(root);
	    
	    stage.setScene(scene);
	    // After the app is ready, show the stage
	    ready.addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) ->
	    {
		if (Boolean.TRUE.equals(t1))
		{
		    Platform.runLater(new Runnable()
		    {
			@Override
			public void run()
			{
			    mainHandle.Start();
			}
		    });
		}
	    });
	} catch (IOException ex)
	{
	    Logger.getLogger(DozentenFaktura.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * initializing prozess
     */
    private void longInit()
    {
        Task task;
        task = new Task<Void>()
        {
            @Override
            protected Void call() throws Exception
            {
                int max = 5;
                int i = 0;
                String db = "Datenbank/DozentenFaktura.db";
                notifyPreloader(new AppNotification("Lade Datenbank", (double) i / max));

                DozentList doz = mainHandle.getDozenten();
                doz.setDB(db);
                if (!doz.load())
                {
                    doz.create();
                }
                i++;
                Thread.sleep(500);
                notifyPreloader(new Preloader.ProgressNotification((double) i / max));

                EinstellungList einst = mainHandle.getEinstellungen();
                einst.setDB(db);
                if (!einst.load())
                {
                    einst.create();
                }
                i++;
                Thread.sleep(500);
                notifyPreloader(new Preloader.ProgressNotification((double) i / max));

                KundeList kl = mainHandle.getKunden();
                kl.setDB(db);
                if (!kl.load())
                {
                    kl.create();
                }
		i++;
                Thread.sleep(500);
                notifyPreloader(new Preloader.ProgressNotification((double) i / max));

                AuftragList al = mainHandle.getAuftraege();
                al.setDB(db);
                if (!al.load())
                {
                    al.create();
                }
                i++;
                Thread.sleep(500);
                notifyPreloader(new Preloader.ProgressNotification((double) i / max));

                RechnungList rl = mainHandle.getRecnungen();
                rl.setDB(db);
                if (!rl.load())
                {
                    rl.create();
                }
                i++;
                Thread.sleep(500);
                notifyPreloader(new Preloader.ProgressNotification((double) i / max));

                // After init is ready, the app is ready to be shown
                // Do this before hiding the preloader stage to prevent the 
                // app from exiting prematurely
                Thread.sleep(500);
                ready.setValue(Boolean.TRUE);

                notifyPreloader(new Preloader.StateChangeNotification(
                        Preloader.StateChangeNotification.Type.BEFORE_START));

                return null;
            }

        };

	// create directorys
	File dir = new File("Datenbank");   // database directory
	if(!dir.exists())
	{
	    dir.mkdir();
	}
	dir = new File("Rechnungen");	    // document directory
	if(!dir.exists())
	{
	    dir.mkdir();
	}
	// start init task
        new Thread(task).start();

    }
}
