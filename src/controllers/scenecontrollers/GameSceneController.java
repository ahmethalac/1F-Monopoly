package controllers.scenecontrollers;

import controllers.modelcontrollers.GameManager;
import controllers.popupControllers.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Agreement;
import models.City;
import models.Player;
import storage.filemanager.DataManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameSceneController implements Initializable {
    private TableController cameraScene;

    @FXML
    private StackPane stackPane;
    @FXML
    private HBox cardsBar;
    @FXML
    private Text money;
    @FXML
    private Text playerName;
    @FXML
    private VBox gameLog;
    @FXML
    private Button rollDiceButton;
    @FXML
    private Label rollDiceLabel;
    @FXML
    private Button endTurnButton;
    @FXML
    private Label endTurnLabel;
    @FXML
    private Button agreementButton;
    @FXML
    private Label agreementLabel;
    @FXML
    private Button mortgageButton;
    @FXML
    private Label mortgageLabel;
    @FXML
    private Button buyBuildingButton;
    @FXML
    private Label buyBuildingLabel;
    @FXML
    private Button sellBuildingButton;
    @FXML
    private Label sellBuildingLabel;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cameraScene = new TableController(this);
        stackPane.getChildren().add(cameraScene);
        cameraScene.toBack();
        GameManager.getInstance().setPlayerObservers(this, gameLog);
        GameManager.getInstance().initGameScene();
        rollDiceButton.setVisible(true);
        rollDiceLabel.setVisible(true);
        sellBuildingButton.setVisible(true);
        sellBuildingLabel.setVisible(true);
        buyBuildingButton.setVisible(true);
        buyBuildingLabel.setVisible(true);
        mortgageButton.setVisible(true);
        mortgageLabel.setVisible(true);
        agreementButton.setVisible(true);
        agreementLabel.setVisible(true);
        endTurnButton.setVisible(false);
        endTurnLabel.setVisible(false);

        //test purpose
        //double[] rents = {1,2,3,4,5,6};
        //Player player1 = new Player("goktug", "blue", "car", 1);
        //City city1 = new City(100, rents, "ankara", 1);
        //City city2 = new City(100, rents, "istanbul", 2);
        //City city3 = new City(100, rents, "malata", 3);
        // player1.addCity(city1);
        //player1.addCity(city2);
        //player1.addCity(city3);
        //renderPlayer(player1);
    }

    //Update the game scene according to the current player
    public void renderPlayer(Player player) {
        playerName.setText(player.getName());
        money.setText(player.getMoney() + "$");
        cardsBar.setStyle("-fx-spacing: 10;");
        cardsBar.getChildren().clear();
        for (int i = 0; i < player.getCities().size(); i++) {
            int finalI = i;
            Button button = new Button(player.getCities().get(i).getName());
            if (player.getCities().get(finalI).isMortgaged()) {
                button.setStyle("-fx-text-fill: white; -fx-pref-height: 80; -fx-pref-width: 150 ;-fx-border-color: black; -fx-background-color: grey; -fx-border-width: 3 3 0 3");
            } else {
                String color1 = player.getColor();
                button.setStyle("-fx-background-color:" + color1 + ";" + "-fx-text-fill: white; -fx-pref-height: 80; -fx-pref-width: 150 ;-fx-border-color: black; -fx-border-width: 3 3 0 3");

            }
            button.setOnAction(actionEvent -> handleCityPopup(player.getCities().get(finalI)));
            cardsBar.setAlignment(Pos.CENTER);
            cardsBar.getChildren().add(button);

        }
    }

    //For debug purposes
    public void rotateTable(ActionEvent actionEvent) {
        cameraScene.rotateTable();
    }

    private static void handlePopup(Parent parent) {
        Scene scene = new Scene(parent, 300, 400);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void handleCityPopup(City city) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../views/popupViews/CityPopup.fxml"));
            Parent parent = fxmlLoader.load();
            CityPopupController popupController = fxmlLoader.getController();
            popupController.setCity(city);

            handlePopup(parent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAgreementPopup() throws IOException {
        if (!GameManager.getInstance().isDiceRolled()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../views/popupViews/AgreementPopup.fxml"));
            Parent parent = fxmlLoader.load();

            AgreementPopupController agreementPopupController = fxmlLoader.getController();
            handlePopup(parent);
        }
    }

    public void handleBuyBuildingPopup() throws IOException {
        if (!GameManager.getInstance().isDiceRolled()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../views/popupViews/BuyBuildingPopup.fxml"));
            Parent parent = fxmlLoader.load();
            BuyBuildingPopupController buyBuildingController = fxmlLoader.getController();
            handlePopup(parent);
        }
    }

    public void handleSellBuildingPopup() throws IOException {
        if (!GameManager.getInstance().isDiceRolled()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../views/popupViews/SellBuildingPopup.fxml"));
            Parent parent = fxmlLoader.load();
            SellBuildingPopupController sbpc = fxmlLoader.getController();
            handlePopup(parent);
        }
    }

    public void handleMortgageCityPopup() throws IOException {
        if (!GameManager.getInstance().isDiceRolled()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../views/popupViews/MortgageCityPopup.fxml"));
            Parent parent = fxmlLoader.load();
            MortgageCityPopupController mcpc = fxmlLoader.getController();
            handlePopup(parent);
        }
    }

    public void handleRollDiceButton() throws IOException {
        int[] dice = GameManager.getInstance().rollDice();
        if (dice == null) {
            System.out.println("Your turn is end");
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../views/popupViews/RollDicePopup.fxml"));
            Parent parent = fxmlLoader.load();
            RollDicePopupController rdpc = fxmlLoader.getController();
            rdpc.setDice(dice[0], dice[1]);

            handlePopup(parent);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //GameManager.getInstance().getCurrentPlayer().quarantine(true); // DEBUG
        GameManager.getInstance().moveForward(dice[2]);
        //GameManager.getInstance().moveForward(14); // DEBUG
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(GameManager.getInstance()::runPerformRegionAction, dice[2] * 300, TimeUnit.MILLISECONDS);

        // turn is end
        if (dice[0] != dice[1]) {
            rollDiceButton.setVisible(false);
            rollDiceLabel.setVisible(false);
            sellBuildingButton.setVisible(false);
            sellBuildingLabel.setVisible(false);
            buyBuildingButton.setVisible(false);
            buyBuildingLabel.setVisible(false);
            mortgageButton.setVisible(false);
            mortgageLabel.setVisible(false);
            agreementButton.setVisible(false);
            agreementLabel.setVisible(false);
            endTurnButton.setVisible(true);
            endTurnLabel.setVisible(true);
        }
    }

    public void handleEndTurnButton() {
        if (GameManager.getInstance().isDiceRolled()) {
            cameraScene.rotateTable();
            GameManager.getInstance().endTurn();
        } else {
            System.out.println("Cannot end turn without rolling a dice.");
        }
        // if new player bankrupted, disable roll dice
        if (GameManager.getInstance().getCurrentPlayer().isBankrupted()) {
            // set isDiceRolled true
            GameManager.getInstance().setDiceRolled(true);
        } else {
            rollDiceButton.setVisible(true);
            rollDiceLabel.setVisible(true);
        }
        sellBuildingButton.setVisible(true);
        sellBuildingLabel.setVisible(true);
        buyBuildingButton.setVisible(true);
        buyBuildingLabel.setVisible(true);
        mortgageButton.setVisible(true);
        mortgageLabel.setVisible(true);
        agreementButton.setVisible(true);
        agreementLabel.setVisible(true);
        endTurnButton.setVisible(false);
        endTurnLabel.setVisible(false);
    }

    public void handleSaveGameButton() {
        DataManager.saveGame("SaveGame1");

    }

    public static void handleChanceRegionPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    GameSceneController.class.getResource("../../views/popupViews/ChanceRegionPopup.fxml"));
            Parent parent = fxmlLoader.load();
            handlePopup(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleBuyCityPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    GameSceneController.class.getResource("../../views/popupViews/BuyCityPopup.fxml"));
            Parent parent = fxmlLoader.load();
            handlePopup(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleAgreementOfferPopup(Agreement agreement) {
        try {
            if (!GameManager.getInstance().isDiceRolled()) {
                FXMLLoader fxmlLoader = new FXMLLoader(GameSceneController.class.getResource("../../views/popupViews/AgreementOfferPopup.fxml"));
                Parent parent = fxmlLoader.load();
                AgreementOfferPopupController agreementOfferPopupController = fxmlLoader.getController();
                agreementOfferPopupController.setAgreement(agreement);
                handlePopup(parent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handlePirateRegionPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameSceneController.class.getResource("../../views/popupViews/PirateRegionPopup.fxml"));
            Parent parent = fxmlLoader.load();
            handlePopup(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VBox getGameLog() {
        return gameLog;
    }
}
