/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickscan1.pkg0;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

/**
 *
 * @author Riddick
 */
public class InicioController implements Initializable {

    @FXML
    private Label lb_so, lb_arq, lb_cuuser, lb_ram, lb_disksize, lb_processor, lb_procesador, lb_tempCore, lb_voltCore, lb_speedFanCooler, lb_is64bits;
    @FXML
    private Button btn_goAcercade, btn_backMain, btn_salir, btn_reporte;
    String TempCore;
    String VoltCore;
    int SpeedFan;
    //Variables para navegacion entre ventanas
    Node node;
    Stage stage;
    Parent parent;
    Scene scene;
    //Variables generar reporte
    private FileWriter fw;
    private PrintWriter pw;

    //
    //Creamos la funcion que nos permite generar el txt con los datos de la maquina(BETA) a esta la llamamos campos
    @FXML
    private void Exportar() {
        try {
            JFileChooser archivo = new JFileChooser(System.getProperty("user.dir"));
            archivo.addChoosableFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
            archivo.showSaveDialog(archivo);
            if (archivo.getSelectedFile() != null) {
                try (FileWriter guardado = new FileWriter(archivo.getSelectedFile())) {
                    String SO = lb_so.getText();
                    String Arquitectura = lb_arq.getText();
                    String Usuario = lb_cuuser.getText();
                    String Ram = lb_ram.getText();
                    String HDD = lb_disksize.getText();
                    String Procesador = lb_procesador.getText();
                    String CantProc = lb_processor.getText();
                    String Temp = lb_tempCore.getText();
                    String Voltaje = lb_voltCore.getText();
                    String VelocFan = lb_speedFanCooler.getText();
                    guardado.write(SO + " Aquitectura" + Arquitectura + " " + Usuario + " " + Ram + " " + HDD + " " + Procesador + " " + CantProc + " " + Temp + " " + Voltaje + " " + VelocFan);
                    Component rootPane = null;
                    Alert save = new Alert(AlertType.INFORMATION);
                    save.setTitle("Exito");
                    save.setContentText("Reporte generado!");
                    save.setHeaderText("Resultado:");
                    save.show();
                }
            }
        } catch (IOException ex) {
        }
    }

    //Accion salir
    @FXML
    private void salir(ActionEvent event) {
        Platform.exit();
    }

    //
    //Accion para navegar de una ventana a otra en este caso nos direcciona a la ventana Acerca de los desarrolladores
    @FXML
    private void goAcercade(ActionEvent event) {
        try {
            node = (Node) event.getSource();
            stage = (Stage) node.getScene().getWindow();
            parent = FXMLLoader.load(getClass().getResource("Acercade.fxml"));
            scene = new Scene(parent);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Acerca de");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //UTILIZAMOS LA CLASE System Y OBTENEMOS LAS PROPIEDADES DE LA ARQUITECTURA DEL SO Y EL NOMBRE DE USUARIO
        String arch = System.getProperty("os.arch");
        lb_arq.setText(arch);
        String cuusr = System.getProperty("user.name");
        lb_cuuser.setText(cuusr);
        long core = Runtime.getRuntime().availableProcessors();
        String coresAvailables = String.valueOf(core);
        lb_processor.setText(coresAvailables + " Nucleos.");
        //INICIAMOS INSTANCIANDO UN METODO PARA OBTENER LAS PROPIEDADES DEL SISTEMA
        //PRIMERO PEDIMOS LA VERSION DEL SISTEMA OPERATIVO (SE CONVIERTE EL VALOR OBTENIDO A UN STRING PARA MOSTRARLO AL USUARIO)
        SystemInfo sysinf = new SystemInfo();
        OperatingSystem os = sysinf.getOperatingSystem();
        System.out.println(os);
        String oss = String.valueOf(os);
        lb_so.setText(oss);
        //SEGUNDO TENEMOS LA INFORMACION SOBRE EL PROCESADOR
        HardwareAbstractionLayer hal = sysinf.getHardware();
        //SE COLOCA LA VARIABLE DEL METODO HardwareAbstractLayer Y SE OBTIENE LA PROPIEDAD getProcessor seguido de getName para que nos muestre el nombre completo del procesador y su velocidad.
        hal.getProcessor().getName();
        System.out.println(hal.getProcessor().getName());
        lb_procesador.setText(hal.getProcessor().getName());
        lb_ram.setText(FormatUtil.formatBytes(hal.getMemory().getAvailable()) + "/" + FormatUtil.formatBytes(hal.getMemory().getTotal()));
        TempCore = String.valueOf(hal.getSensors().getCpuTemperature());
        lb_tempCore.setText(TempCore + " C.");
        VoltCore = String.valueOf(hal.getSensors().getCpuVoltage());
        System.out.println(VoltCore);
        lb_voltCore.setText(VoltCore + " Voltios.");
        int[] sps = hal.getSensors().getFanSpeeds();
        System.out.println(Arrays.toString(hal.getSensors().getFanSpeeds()));
        lb_speedFanCooler.setText(Arrays.toString(sps) + " RPM.");
        checkDisk();
    }

    /*
    La funcion checkx64 nos da la arquitectura del procesador de la maquina obtenemos esto accediendo a la clase System.
    como extra he agregado un condicional para decirle al usuario final si su cpu soporta SO de x64.
     */
 /*Esta funcion nos permite conocer el espacio total del HDD
    Utilizando la Clase File que nos sirve para coocer una direccion en nuestra maquina o bien leer un archivo
    como se puede ver le pido datos sobre la direccion C: la cual es nuestro HDD. Dado que Java nos dara el total de GB en Bytes 
    debemos convertirlo dividiendo entre 1 Millon.
     */
    public void checkDisk() {
        File hdd = new File("C:\\");
        double espaceByts = hdd.getTotalSpace() / 1000000000.00;
        Double hddDouble = redondearDecimales(espaceByts, 2);
        String hddTotal = String.valueOf(hddDouble);
        lb_disksize.setText(hddTotal + " GB");
    }

    /*
    La siguiente funcion es parte de la funcion checkDisk(). ya que el valor que nos da Java es un  valor muy grande y poco entendible
    para usuarios finales. Esta funcion lo que lleva a cabo es redondear a 2 decimales.
     */
    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado = (resultado - parteEntera) * Math.pow(10, numeroDecimales);
        resultado = Math.round(resultado);
        resultado = (resultado / Math.pow(10, numeroDecimales)) + parteEntera;
        return resultado;
    }

}
