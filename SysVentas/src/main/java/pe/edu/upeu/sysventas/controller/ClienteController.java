package pe.edu.upeu.sysventas.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.sysventas.components.ColumnInfo;
import pe.edu.upeu.sysventas.components.TableViewHelper;
import pe.edu.upeu.sysventas.components.Toast;
import pe.edu.upeu.sysventas.dto.PersonaDto;
import pe.edu.upeu.sysventas.enums.TipoDocumento;
import pe.edu.upeu.sysventas.model.Cliente;
import pe.edu.upeu.sysventas.service.IClienteService;
import pe.edu.upeu.sysventas.utils.ConsultaDNI;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Controller
public class ClienteController {

    @FXML
    TextField txtDniRuc, txtNombres, txtRepLegal, txtFiltroDato;

    @FXML
    Button btnBuscarDni;

    @FXML
    ComboBox<String> cbxTipoDoc;

    @FXML
    private TableView<Cliente> tableView;

    @FXML
    Label lbnMsg;

    @FXML
    private AnchorPane miContenedor;

    Stage stage;

    @Autowired
    IClienteService clienteService;

    private Validator validator;
    ObservableList<Cliente> listarCliente;
    Cliente formulario;
    String idClienteCE = "";

    private void filtrarClientes(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tableView.getItems().clear();
            tableView.getItems().addAll(listarCliente);
        } else {
            String lowerCaseFilter = filtro.toLowerCase();
            List<Cliente> clientesFiltrados = listarCliente.stream()
                    .filter(cliente ->
                            cliente.getDniruc().toLowerCase().contains(lowerCaseFilter) ||
                                    cliente.getNombres().toLowerCase().contains(lowerCaseFilter) ||
                                    (cliente.getRepLegal() != null && cliente.getRepLegal().toLowerCase().contains(lowerCaseFilter))
                    )
                    .collect(Collectors.toList());
            tableView.getItems().clear();
            tableView.getItems().addAll(clientesFiltrados);
        }
    }

    public void listar() {
        try {
            tableView.getItems().clear();
            listarCliente = FXCollections.observableArrayList(clienteService.findAll());
            tableView.getItems().addAll(listarCliente);
            txtFiltroDato.textProperty().addListener((observable, oldValue, newValue) -> {
                filtrarClientes(newValue);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void buscarDni() {
        String dni = txtDniRuc.getText().trim();

        if (dni.isEmpty()) {
            mostrarMensaje("Por favor ingrese un DNI", "red");
            return;
        }

        if (dni.length() != 8) {
            mostrarMensaje("El DNI debe tener 8 dígitos", "red");
            return;
        }

        btnBuscarDni.setDisable(true);
        lbnMsg.setText("Buscando...");
        lbnMsg.setStyle("-fx-text-fill: blue; -fx-font-size: 16px;");

        // Ejecutar la consulta en un hilo separado
        new Thread(() -> {
            try {
                ConsultaDNI consultaDNI = new ConsultaDNI();
                PersonaDto persona = consultaDNI.consultarDNI(dni);

                // Actualizar UI en el hilo de JavaFX
                Platform.runLater(() -> {
                    if (persona != null && persona.getNombre() != null) {
                        String nombreCompleto = persona.getNombre() + " " +
                                persona.getApellidoPaterno() + " " +
                                persona.getApellidoMaterno();
                        txtNombres.setText(nombreCompleto);
                        cbxTipoDoc.getSelectionModel().select("DNI");
                        mostrarMensaje("DNI encontrado correctamente", "green");
                    } else {
                        mostrarMensaje("No se encontró información del DNI", "orange");
                    }
                    btnBuscarDni.setDisable(false);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    mostrarMensaje("Error al consultar DNI: " + e.getMessage(), "red");
                    btnBuscarDni.setDisable(false);
                });
            }
        }).start();
    }

    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000),
                event -> {
                    stage = (Stage) miContenedor.getScene().getWindow();
                    if (stage != null) {
                        System.out.println("El título del stage es: " + stage.getTitle());
                    } else {
                        System.out.println("Stage aún no disponible.");
                    }
                }));
        timeline.setCycleCount(1);
        timeline.play();

        // Inicializar ComboBox de Tipo Documento
        cbxTipoDoc.getItems().addAll("DNI", "RUC");
        cbxTipoDoc.getSelectionModel().selectFirst();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Cliente> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("DNI/RUC", new ColumnInfo("dniruc", 120.0));
        columns.put("Nombres", new ColumnInfo("nombres", 300.0));
        columns.put("Rep. Legal", new ColumnInfo("repLegal", 200.0));
        columns.put("Tipo Doc.", new ColumnInfo("tipoDocumento", 100.0));

        Consumer<Cliente> updateAction = (Cliente cliente) -> {
            System.out.println("Actualizar: " + cliente);
            editForm(cliente);
        };

        Consumer<Cliente> deleteAction = (Cliente cliente) -> {
            clienteService.delete(cliente);
            double with = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, with, h);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
        listar();
    }

    public void limpiarError() {
        List<Control> controles = List.of(txtDniRuc, txtNombres, txtRepLegal, cbxTipoDoc);
        controles.forEach(c -> c.getStyleClass().remove("text-field-error"));
    }

    @FXML
    public void clearForm() {
        txtDniRuc.clear();
        txtNombres.clear();
        txtRepLegal.clear();
        cbxTipoDoc.getSelectionModel().selectFirst();
        idClienteCE = "";
        limpiarError();
        lbnMsg.setText("");
    }

    public void editForm(Cliente cliente) {
        txtDniRuc.setText(cliente.getDniruc());
        txtDniRuc.setDisable(true); // No permitir editar el DNI/RUC
        txtNombres.setText(cliente.getNombres());
        txtRepLegal.setText(cliente.getRepLegal());
        cbxTipoDoc.getSelectionModel().select(cliente.getTipoDocumento().name());
        idClienteCE = cliente.getDniruc();
        limpiarError();
    }

    private void mostrarErroresValidacion(Set<ConstraintViolation<Cliente>> violaciones) {
        limpiarError();

        if (!violaciones.isEmpty()) {
            ConstraintViolation<Cliente> primeraViolacion = violaciones.iterator().next();
            lbnMsg.setText(primeraViolacion.getMessage());
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");

            String propertyPath = primeraViolacion.getPropertyPath().toString();
            Control controlConError = null;

            switch (propertyPath) {
                case "dniruc":
                    controlConError = txtDniRuc;
                    break;
                case "nombres":
                    controlConError = txtNombres;
                    break;
                case "tipoDocumento":
                    controlConError = cbxTipoDoc;
                    break;
            }

            if (controlConError != null) {
                controlConError.getStyleClass().add("text-field-error");
                Control finalControl = controlConError;
                Platform.runLater(finalControl::requestFocus);
            }
        }
    }

    private void procesarFormulario() {
        lbnMsg.setText("Formulario válido");
        lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
        limpiarError();
        double width = stage.getWidth() / 1.5;
        double height = stage.getHeight() / 2;

        if (!idClienteCE.isEmpty()) {
            clienteService.update(idClienteCE, formulario);
            Toast.showToast(stage, "Se actualizó correctamente!!", 2000, width, height);
            txtDniRuc.setDisable(false);
        } else {
            clienteService.save(formulario);
            Toast.showToast(stage, "Se guardó correctamente!!", 2000, width, height);
        }
        clearForm();
        listar();
    }

    @FXML
    public void validarFormulario() {
        // Validar DNI/RUC
        String dniRuc = txtDniRuc.getText().trim();
        if (dniRuc.isEmpty()) {
            mostrarMensaje("El DNI/RUC no puede estar vacío", "red");
            txtDniRuc.getStyleClass().add("text-field-error");
            return;
        }

        TipoDocumento tipoDoc = TipoDocumento.valueOf(cbxTipoDoc.getSelectionModel().getSelectedItem());

        if (tipoDoc == TipoDocumento.DNI && dniRuc.length() != 8) {
            mostrarMensaje("El DNI debe tener 8 dígitos", "red");
            txtDniRuc.getStyleClass().add("text-field-error");
            return;
        }

        if (tipoDoc == TipoDocumento.RUC && dniRuc.length() != 11) {
            mostrarMensaje("El RUC debe tener 11 dígitos", "red");
            txtDniRuc.getStyleClass().add("text-field-error");
            return;
        }

        formulario = Cliente.builder()
                .dniruc(dniRuc)
                .nombres(txtNombres.getText().trim())
                .repLegal(txtRepLegal.getText().trim().isEmpty() ? null : txtRepLegal.getText().trim())
                .tipoDocumento(tipoDoc)
                .build();

        Set<ConstraintViolation<Cliente>> violaciones = validator.validate(formulario);

        if (violaciones.isEmpty()) {
            procesarFormulario();
        } else {
            mostrarErroresValidacion(violaciones);
        }
    }

    private void mostrarMensaje(String mensaje, String color) {
        lbnMsg.setText(mensaje);
        lbnMsg.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 16px;");
    }
}