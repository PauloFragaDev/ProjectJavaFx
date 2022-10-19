/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import dao.AtraccionDAO;
import dao.PuestoDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Atraccion;
import model.Puesto;
import model.Usuario;

/**
 * FXML Controller class
 *
 * @author polfr
 */
public class CrearAtraccionPuestoController implements Initializable {
    
    // OBJETOS / ARRAYLIST / OBSERVABLELIST / MORE --> QUE UTILIZO EN ESTA CLASE
    
    Usuario usuarioLogin;
    ArrayList<Atraccion> atraccionArList;
    AtraccionDAO atraccionDAO;
    PuestoDAO puestoDAO;
    String accion;
    
    // BOTONES / TXTFIELDS / TABLAS / CHOICEBOX / MORE --> DE ESTA CLASE
    
    @FXML
    private Button btnSalir;
    @FXML
    private TextField txtNombreAtraccion;
    @FXML
    private TextField txtPrecioAtraccion;
    @FXML
    private TextField txtEdadMinima;
    @FXML
    private Button btnCrearAtraccion;
    @FXML
    private TextField txtPremio;
    
    // CONSTRUCTORES DE LA CLASE
    
    public CrearAtraccionPuestoController(Usuario usuarioIn) {
        this.usuarioLogin = usuarioIn;
        this.atraccionDAO = new AtraccionDAO();
        this.puestoDAO = new PuestoDAO();
        this.atraccionArList = new ArrayList(atraccionDAO.selectAll());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    // FUNCION PARA CREAR UNA ATRACCION DE TIPO PUESTO
    
    @FXML
    private void crearAtraccion(ActionEvent event) {
        try {
            // RECOJO LOS NUEVOS VALORES QUE SE HAYAN ESCOGIDO
            String nombreAtraccion = this.txtNombreAtraccion.getText();
            String precioAtraccionAAA = this.txtPrecioAtraccion.getText();
            String edadMinimaAAA = this.txtEdadMinima.getText();
            String premio = this.txtPremio.getText();
            int precioAtraccion, edadMinima, alturaMinima, numeroPasajeros;
            
            // COMPRUEBO QUE LOS CAMPOS NO ESTEN VACIOS
            
            if (!nombreAtraccion.isEmpty() && !precioAtraccionAAA.isEmpty() && !edadMinimaAAA.isEmpty() && !premio.isEmpty()) {
                
            // COMPRUEBO QUE SE PUEDA REALIZAR LA CONVERSION DE STRING A INTEGER    
                
                if (isNumero(precioAtraccionAAA, edadMinimaAAA) == true) {
                    precioAtraccion = Integer.parseInt(precioAtraccionAAA);
                    edadMinima = Integer.parseInt(edadMinimaAAA);
                    
                    // CREO EL OBJETO Y LO INSERTO EN LA BASE DE DATOS
                    
                    Atraccion atraccion = new Atraccion(nombreAtraccion, precioAtraccion, edadMinima);

                    atraccionDAO.insert(atraccion);
                    
                    // OBTENGO EL ID DE LA ATRACCION QUE ACABO DE AÑADIR EN LA BASE DE DATOS
                    
                    Atraccion atracId = obtenerIdAtraccion(atraccion);
                    
                    // ESTABLEZCO EL MISMO ID A LA ATRACCION PUESTO
                    
                    int idPuesto = atracId.getIdAtraccion();
                    
                    // CREO EL OBJETO DE TIPO PUESTO
                    
                    Puesto puesto = new Puesto(idPuesto, nombreAtraccion, precioAtraccion, edadMinima, premio);
                    
                    // LLAMO A LA FUNCION QUE HARA LA SENTENCIA PARA INSERTAR EL PEUSTO EN LA BASE DE DATOS
                    
                    puestoDAO.insertId(puesto);
                    limpiarCampos();

                    // APARECERA UN ALERT INFORMANDO QUE SE A AÑADIDO LA ATRACCION
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText(null);
                    alert.setTitle("Informacion");
                    alert.setContentText("Atraccion -> Puesto Creado");
                    alert.showAndWait();
                    
                    //APARECERA UNA VENTANA DONDE EL USUARIO TENDRA QUE PONER UNA DESCRIPCION DE LO QUE ACABA DE REALIZAR
                    
                    accion = "CrearAtraccionPuesto";
                    Stage loginUsuario = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/asignarDescripcion.fxml"));
                    loader.setControllerFactory(t -> new AsignarDescripcionController(accion, usuarioLogin));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    scene.setFill(Color.TRANSPARENT);
                    loginUsuario.initStyle(StageStyle.TRANSPARENT);
                    loginUsuario.setScene(scene);
                    loginUsuario.show();
                    Stage stage = (Stage) this.btnCrearAtraccion.getScene().getWindow();

                } else {
                    
                    // EN CASO QUE NO SE PUEDA REALIZAR LA CONVERSION APARECERA ESTE ALERT
                    
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setTitle("Error");
                    alert.setContentText("Uno de los campos contiene un caracter no valido");
                    alert.showAndWait();
                }
            } 
            
            // EN CASO QUE LOS CAMPOS ESTEN VACIOS APARECERA ESTE ALERT 
            
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Error");
                alert.setContentText("Campos incorrectos");
                alert.showAndWait();
            }

        } catch (Exception e) {
            System.out.println("Error");
        }
    }
    
    // FUNCION PARA SALIR DE LA VIEW
    
    @FXML
    private void salirAplicacion(ActionEvent event) throws IOException {
        Stage loginUsuario = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/gestionAtraccion.fxml"));
        loader.setControllerFactory(t -> new GestionAtraccionController(usuarioLogin));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        loginUsuario.initStyle(StageStyle.TRANSPARENT);
        loginUsuario.setScene(scene);
        loginUsuario.show();
        Stage stage = (Stage) this.btnSalir.getScene().getWindow();
        stage.close();
    }
    
    // FUNCION PARA COMPROBAR QUE SE PUEDA REALIZAR LA CONVERSION DE STRING A INTEGER
    
    private boolean isNumero(String texto, String texto2) {
        boolean resultado;
        try {
            Integer.parseInt(texto);
            resultado = true;
            try {
                Integer.parseInt(texto2);
                resultado = true;
            } catch (NumberFormatException e) {
                resultado = false;
            }
        } catch (NumberFormatException e) {
            resultado = false;
        }
        return resultado;
    }
    
    // LIMPIO LOS CAMPÒS 
    
    private void limpiarCampos() {
        txtNombreAtraccion.setText("");
        txtPrecioAtraccion.setText("");
        txtEdadMinima.setText("");
        txtPremio.setText("");
    }
    
    // OBTENGO LA ATRACCION PERTINENTE A LA DEMANDADA EN LA FUNCION
    
    private Atraccion obtenerIdAtraccion(Atraccion atraccionIn) {
        this.atraccionArList = new ArrayList(atraccionDAO.selectAll());
        Atraccion atraccionDev = null;
        for (Atraccion atraccion : atraccionArList) {
            if (atraccion.getNombreAtraccion().equals(atraccionIn.getNombreAtraccion())) {
                atraccionDev = atraccion;
            }
        }
        return atraccionDev;
    }
}
