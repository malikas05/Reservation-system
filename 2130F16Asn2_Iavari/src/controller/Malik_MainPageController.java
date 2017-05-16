package controller;

import javafx.fxml.FXML;

public class Malik_MainPageController {

    private Malik_Main mainApp;

    public void setMainApp(Malik_Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void goToLoginPage() {
        mainApp.getPrimaryStage().setScene(mainApp.getLoginPage());
    }

    @FXML
    private void goToRegisterPage() {
        mainApp.getPrimaryStage().setScene(mainApp.getRegisterPage());
    }

    @FXML
    private void goBack() {
        mainApp.backToPage();
    }

}
