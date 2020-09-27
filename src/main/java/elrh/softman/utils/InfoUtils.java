package elrh.softman.utils;

import elrh.softman.Softman;
import javafx.scene.control.*;

public class InfoUtils {
    
    public static void showMessage(String message) {
            Alert a = new Alert(Alert.AlertType.NONE, message, ButtonType.OK); 
            a.initOwner(Softman.getPrimaryStage());
            a.setTitle("SOFTMAN");
            a.showAndWait();
    }
    
    public static String getErrorMessage(Exception ex) {
        return ex.getMessage() != null ? ex.getMessage() : ex.toString();
    }
    
}
