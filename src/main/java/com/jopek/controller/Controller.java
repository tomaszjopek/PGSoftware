package com.jopek.controller;

import com.jopek.Validator;
import com.jopek.model.Car;
import com.jopek.model.Client;
import com.jopek.model.Registry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller implements Initializable {

    @FXML
    private Button clientsButton;
    @FXML
    private Button carsButton;
    @FXML
    private Button registriesButton;
    @FXML
    private Button addClientBtn;
    @FXML
    private Button addCarBtn;
    @FXML
    private Button addRegistryBtn;
    @FXML
    private TableView tableView;
    @FXML
    private AnchorPane anchorTable;

    private Stage mStage;
    private List<Client> clients;
    private List<Car> cars;
    private List<Registry> registries;
    private boolean isClientTableVisible;
    private boolean isCarTableVisible;
    private boolean isRegistryTableVisible;


    public Controller() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadHistory();
        loadCars();
        loadClients();

        clientsButton.setOnMouseClicked(event -> loadClients());
        carsButton.setOnMouseClicked(event -> loadCars());
        registriesButton.setOnMouseClicked(event -> loadHistory());
        addClientBtn.setOnMouseClicked(event -> showAddUserForm());
        addCarBtn.setOnMouseClicked(event -> showAddVehicleForm());
        addRegistryBtn.setOnMouseClicked(event -> showDialogWindow());
    }

    private void showDialogWindow() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/rentview.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Dodaj wypozyczenie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            RentWindowController controller = loader.getController();
            controller.setStage(dialogStage);
            controller.setRegistries(registries);
            dialogStage.showAndWait();

            if(isRegistryTableVisible)
                loadHistory();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAddVehicleForm() {
        Dialog<Car> dialogForm = new Dialog<>();
        dialogForm.setHeaderText("Gdy wszystkie dane beda poprawne bedzie mozliwe dodanie nowego pojazdu");
        dialogForm.setTitle("Dodaj pojazd");
        dialogForm.setResizable(true);

        Label lProducer = new Label("Producent: ");
        Label lModel = new Label("Marka: ");
        Label lYear = new Label("Rok produkcji: ");
        Label lPlate = new Label("Rejestracja: ");

        TextField tProducer = new TextField();
        TextField tModel = new TextField();
        TextField tYear = new TextField();
        TextField tPlate = new TextField();

        GridPane grid = new GridPane();
        grid.add(lProducer, 1, 1);
        grid.add(lModel, 1, 2);
        grid.add(lYear, 1, 3);
        grid.add(lPlate, 1, 4);

        grid.add(tProducer, 2, 1);
        grid.add(tModel, 2, 2);
        grid.add(tYear, 2, 3);
        grid.add(tPlate, 2, 4);

        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-font-size: 12pt; -fx-padding: 5px;");

        dialogForm.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeDecline = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogForm.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialogForm.getDialogPane().getButtonTypes().add(buttonTypeDecline);

        Button buttonOk = (Button) dialogForm.getDialogPane().lookupButton(buttonTypeOk);
        buttonOk.setDisable(true);

        tProducer.textProperty()
                .addListener((observable, oldValue, newValue) -> buttonOk
                        .setDisable(!validateWholeAddVehicleForm(tProducer.getText(),
                                tModel.getText(),
                                tYear.getText())));

        tModel.textProperty()
                .addListener((observable, oldValue, newValue) -> buttonOk
                        .setDisable(!validateWholeAddVehicleForm(tProducer.getText(),
                                tModel.getText(),
                                tYear.getText())));

        tYear.textProperty()
                .addListener((observable, oldValue, newValue) -> buttonOk
                        .setDisable(!validateWholeAddVehicleForm(tProducer.getText(),
                                tModel.getText(),
                                tYear.getText())));

        dialogForm.setResultConverter(param -> {
            if(param == buttonTypeOk) {
                Car car = new Car();

                String uniqueID = cars.stream().map(Car::getId).max(String::compareTo).get();
                int id = Integer.valueOf(uniqueID);
                car.setId(String.valueOf(++id));

                car.setProducer(tProducer.getText());
                car.setModel(tModel.getText());
                car.setProductionYear(tYear.getText());
                car.setPlates(tPlate.getText());

                cars.add(car);

                if(isCarTableVisible)
                    tableView.getItems().add(car);

                try {
                    Files.write(Paths.get(getClass().getResource("/database/cars.csv").toURI()), car.toText().getBytes(), StandardOpenOption.APPEND);
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }

                return car;
            }

            return null;
        });

        dialogForm.show();
    }

    private void showAddUserForm() {
        Dialog<Client> dialogForm = new Dialog<>();
        dialogForm.setHeaderText("Gdy wszystkie dane beda poprawne, bedzie mozliwe dodanie nowego klienta");
        dialogForm.setTitle("Dodaj klienta");
        dialogForm.setResizable(true);

        Label lSurname = new Label("Nazwisko: ");
        Label lName = new Label("Imie: ");
        Label lCity = new Label("Miejscowosc: ");
        Label lPostalCode = new Label("Kod pocztowy: ");
        Label lPhone = new Label("Telefon: ");
        Label lEmail = new Label("Email: ");

        TextField tSurname = new TextField();
        TextField tName = new TextField();
        TextField tCity = new TextField();
        TextField tPostalCode = new TextField();
        TextField tPhone = new TextField();
        TextField tEmail = new TextField();

        GridPane grid = new GridPane();
        grid.add(lSurname, 1, 1);
        grid.add(lName, 1, 2);
        grid.add(lCity, 1, 3);
        grid.add(lPostalCode, 1, 4);
        grid.add(lPhone, 1, 5);
        grid.add(lEmail, 1, 6);

        grid.add(tSurname, 2, 1);
        grid.add(tName, 2, 2);
        grid.add(tCity, 2, 3);
        grid.add(tPostalCode, 2, 4);
        grid.add(tPhone, 2, 5);
        grid.add(tEmail, 2, 6);

        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-font-size: 12pt; -fx-padding: 5px;");

        dialogForm.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeDecline = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogForm.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialogForm.getDialogPane().getButtonTypes().add(buttonTypeDecline);
        final Button buttonOk = (Button) dialogForm.getDialogPane().lookupButton(buttonTypeOk);
        buttonOk.setDisable(true);

        tSurname.textProperty()
                .addListener((observable, oldValue, newValue) -> buttonOk
                        .setDisable(!validateWholeAddClientForm(tSurname.getText(),
                                tName.getText(),
                                tCity.getText(),
                                tPostalCode.getText(),
                                tPhone.getText(),
                                tEmail.getText())));

        tName.textProperty()
                .addListener((observable, oldValue, newValue) -> buttonOk
                        .setDisable(!validateWholeAddClientForm(tSurname.getText(),
                                tName.getText(),
                                tCity.getText(),
                                tPostalCode.getText(),
                                tPhone.getText(),
                                tEmail.getText())));

        tCity.textProperty()
                .addListener((observable, oldValue, newValue) -> buttonOk
                        .setDisable(!validateWholeAddClientForm(tSurname.getText(),
                                tName.getText(),
                                tCity.getText(),
                                tPostalCode.getText(),
                                tPhone.getText(),
                                tEmail.getText())));

        tPostalCode.textProperty()
                .addListener((observable, oldValue, newValue) -> buttonOk
                        .setDisable(!validateWholeAddClientForm(tSurname.getText(),
                                tName.getText(),
                                tCity.getText(),
                                tPostalCode.getText(),
                                tPhone.getText(),
                                tEmail.getText())));

        tPhone.textProperty()
                .addListener((observable, oldValue, newValue) -> buttonOk
                        .setDisable(!validateWholeAddClientForm(tSurname.getText(),
                                tName.getText(),
                                tCity.getText(),
                                tPostalCode.getText(),
                                tPhone.getText(),
                                tEmail.getText())));

        tEmail.textProperty()
                .addListener((observable, oldValue, newValue) -> buttonOk
                        .setDisable(!validateWholeAddClientForm(tSurname.getText(),
                                tName.getText(),
                                tCity.getText(),
                                tPostalCode.getText(),
                                tPhone.getText(),
                                tEmail.getText())));

        dialogForm.setResultConverter(param -> {
            if(param == buttonTypeOk) {
                Client client = new Client();

                String uniqueID = clients.stream().map(Client::getId).max(String::compareTo).get();
                int id = Integer.valueOf(uniqueID);
                client.setId(String.valueOf(++id));

                client.setSurname(tSurname.getText());
                client.setName(tName.getText());
                client.setCity(tCity.getText());
                client.setPostalCode(tPostalCode.getText());
                client.setPhone(tPhone.getText());
                client.setEmail(tEmail.getText());

                clients.add(client);

                if(isClientTableVisible)
                    tableView.getItems().add(client);

                try {
                    File file = new File(getClass().getResource("/database/clients.csv").toURI());
                    FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                    fileOutputStream.write(client.toString().getBytes());
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }

                return client;

            }

            return null;
        });

        dialogForm.showAndWait();
    }

    private void loadClients() {
        isClientTableVisible = true;
        isCarTableVisible = false;
        isRegistryTableVisible = false;

        try(InputStream resource = getClass().getResourceAsStream("/database/clients.csv")) {
            Stream<String> stream = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)).lines();

            List<Client> cList = stream.map(line -> {
                String details[] = line.split(",");
                Client client = new Client();
                client.setId(details[0]);
                client.setSurname(details[1]);
                client.setName(details[2]);
                client.setCity(details[3]);
                client.setPostalCode(details[4]);
                client.setPhone(details[5]);
                client.setEmail(details[6]);

                return client;
            }).collect(Collectors.toList());

            clients = cList;

            ObservableList<Client> details = FXCollections.observableArrayList(cList);

            this.tableView = new TableView<>();
            TableColumn<Client, String> col1 = new TableColumn<>();
            TableColumn<Client, String> col2 = new TableColumn<>();
            TableColumn<Client, String> col3 = new TableColumn<>();
            TableColumn<Client, String> col4 = new TableColumn<>();
            TableColumn<Client, String> col5 = new TableColumn<>();
            TableColumn<Client, String> col6 = new TableColumn<>();
            TableColumn<Client, String> col7 = new TableColumn<>();

            col1.setText("ID");
            col2.setText("Nazwisko");
            col3.setText("Imię");
            col4.setText("Miasto");
            col5.setText("Kod Pocztowy");
            col6.setText("Telefon");
            col7.setText("Email");

            this.tableView.getColumns().removeAll();

            this.tableView.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7);
            col1.setCellValueFactory(param -> param.getValue().idProperty());
            col2.setCellValueFactory(param -> param.getValue().surnameProperty());
            col3.setCellValueFactory(param -> param.getValue().nameProperty());
            col4.setCellValueFactory(param -> param.getValue().cityProperty());
            col5.setCellValueFactory(param -> param.getValue().postalCodeProperty());
            col6.setCellValueFactory(param -> param.getValue().phoneProperty());
            col7.setCellValueFactory(param -> param.getValue().emailProperty());

            this.tableView.setItems(details);
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            anchorTable.getChildren().set(0, this.tableView);
            AnchorPane.setBottomAnchor(tableView, 0.0);
            AnchorPane.setTopAnchor(tableView, 0.0);
            AnchorPane.setLeftAnchor(tableView, 0.0);
            AnchorPane.setRightAnchor(tableView, 0.0);


        }catch(IOException ioex) {
            ioex.printStackTrace();
        }
    }

    private void loadCars() {
        isClientTableVisible = false;
        isCarTableVisible = true;
        isRegistryTableVisible = false;

        try(InputStream resource = getClass().getResourceAsStream("/database/cars.csv")) {
            Stream<String> stream = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)).lines();

            List<Car> cList = stream.map(line -> {
                String details[] = line.split(",");

                Car car = new Car();
                car.setId(details[0]);
                car.setProducer(details[1]);
                car.setModel(details[2]);
                car.setProductionYear(details[3]);
                car.setPlates(details[4]);
                car.setFlag(details[5]);

                return car;
            }).collect(Collectors.toList());

            cars = cList;

            ObservableList<Car> details = FXCollections.observableArrayList(cList);

            this.tableView = new TableView<>();
            TableColumn<Car, String> col1 = new TableColumn<>();
            TableColumn<Car, String> col2 = new TableColumn<>();
            TableColumn<Car, String> col3 = new TableColumn<>();
            TableColumn<Car, String> col4 = new TableColumn<>();
            TableColumn<Car, String> col5 = new TableColumn<>();
            TableColumn<Car, String> col6 = new TableColumn<>();

            col1.setText("ID");
            col2.setText("Producent");
            col3.setText("Model");
            col4.setText("Rok produkcji");
            col5.setText("Rejestracja");
            col6.setText("Wypożyczony");

            this.tableView.getColumns().removeAll();

            this.tableView.getColumns().addAll(col1, col2, col3, col4, col5, col6);
            col1.setCellValueFactory(param -> param.getValue().idProperty());
            col2.setCellValueFactory(param -> param.getValue().producerProperty());
            col3.setCellValueFactory(param -> param.getValue().modelProperty());
            col4.setCellValueFactory(param -> param.getValue().productionYearProperty());
            col5.setCellValueFactory(param -> param.getValue().platesProperty());
            col6.setCellValueFactory(param -> param.getValue().flagProperty());

            this.tableView.setItems(details);
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            anchorTable.getChildren().set(0, this.tableView);
            AnchorPane.setBottomAnchor(tableView, 0.0);
            AnchorPane.setTopAnchor(tableView, 0.0);
            AnchorPane.setLeftAnchor(tableView, 0.0);
            AnchorPane.setRightAnchor(tableView, 0.0);

        }catch(IOException ioex) {
            ioex.printStackTrace();
        }
    }

    private void loadHistory() {
        isClientTableVisible = false;
        isCarTableVisible = false;
        isRegistryTableVisible = true;

        try(InputStream resource = getClass().getResourceAsStream("/database/history.csv")) {
            Stream<String> stream = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)).lines();

            List<Registry> rList = stream.map(line -> {
                String details[] = line.split(",");

                Registry registry = new Registry();
                registry.setId(details[0]);
                registry.setClientId(details[1]);
                registry.setCarId(details[2]);
                registry.setDate(details[3]);
                registry.setrDate(details[4]);

                return registry;
            }).collect(Collectors.toList());

            registries = rList;

            ObservableList<Registry> details = FXCollections.observableArrayList(rList);

            this.tableView = new TableView<>();
            TableColumn<Registry, String> col1 = new TableColumn<>();
            TableColumn<Registry, String> col2 = new TableColumn<>();
            TableColumn<Registry, String> col3 = new TableColumn<>();
            TableColumn<Registry, String> col4 = new TableColumn<>();
            TableColumn<Registry, String> col5 = new TableColumn<>();

            col1.setText("ID");
            col2.setText("ID Klienta");
            col3.setText("ID Pojazdu");
            col4.setText("Data wypożyczenia");
            col5.setText("Data zwrotu");

            this.tableView.getColumns().removeAll();

            this.tableView.getColumns().addAll(col1, col2, col3, col4, col5);
            col1.setCellValueFactory(param -> param.getValue().idProperty());
            col2.setCellValueFactory(param -> param.getValue().clientIdProperty());
            col3.setCellValueFactory(param -> param.getValue().carIdProperty());
            col4.setCellValueFactory(param -> param.getValue().dateProperty());
            col5.setCellValueFactory(param -> param.getValue().rDateProperty());

            this.tableView.setItems(details);
            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            anchorTable.getChildren().set(0, this.tableView);
            AnchorPane.setBottomAnchor(tableView, 0.0);
            AnchorPane.setTopAnchor(tableView, 0.0);
            AnchorPane.setLeftAnchor(tableView, 0.0);
            AnchorPane.setRightAnchor(tableView, 0.0);

        }catch(IOException ioex) {
            ioex.printStackTrace();
        }
    }

    public void setmStage(Stage mStage) {
        this.mStage = mStage;
    }

    private void showWarningWindow(String contentText, Dialog dialog) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ostrzezenie");
        alert.setHeaderText("Niepoprawne dane");
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    private boolean validateWholeAddClientForm(String tSurname,
                                               String tName,
                                               String tCity,
                                               String tPostalCode,
                                               String tPhone,
                                               String tEmail) {

        return Validator.validateLetters(tSurname) &&
                Validator.validateLetters(tName) &&
                Validator.validateLetters(tCity) &&
                Validator.validatePostalCode(tPostalCode) &&
                Validator.validatePhoneNumber(tPhone) &&
                Validator.validateEmail(tEmail);
    }

    private boolean validateWholeAddVehicleForm(String tProducer,
                                                String tModel,
                                                String tYear) {
        return Validator.validateLetters(tProducer) &&
                Validator.validateLetters(tModel) &&
                Validator.validateYear(tYear);
    }

}

