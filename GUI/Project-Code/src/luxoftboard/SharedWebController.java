package luxoftboard;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;

public class SharedWebController implements Initializable {

    @FXML
    private static WebView sharedWeb = new WebView();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // This method can be left empty as the WebView is initialized statically
    }

    public void loadURL(String url) {
        sharedWeb.getEngine().load(url);
    }

    public static WebView getSharedWebView() {
        return sharedWeb;
    }
}
