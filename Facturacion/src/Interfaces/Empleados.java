/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Empleados.java
 *
 * Created on 01-feb-2015, 10:28:58
 */
package Interfaces;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Chris
 */
public class Empleados extends javax.swing.JFrame {

    /** Creates new form Empleados */
    DefaultTableModel model;
    private FileInputStream fis;
    private int longitudBytes;
    String nombreArchivo;
    DateFormat a = new SimpleDateFormat("dd/MMM/yyyy");
    private String id;
    private String descripcion;
    public Empleados() {
        initComponents();
        cargarComboEstadoCivil();
        //lbFoto.setIcon(new ImageIcon(getClass().getResource("/imgPred/defaultlarge.gif")));
        cargarTablaEmpleados("");
        
        tbEmpleados.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(tbEmpleados.getSelectedRow()!= -1){ // -1 devuelve cuando no esta seleccionado nada en la tabla
                    int fila = tbEmpleados.getSelectedRow();
                    txtCedula.setText(tbEmpleados.getValueAt(fila, 0).toString());
                    txtNombre.setText(tbEmpleados.getValueAt(fila, 1).toString());
                    txtApellido.setText(tbEmpleados.getValueAt(fila, 2).toString());
                    cbCargo.setSelectedItem(tbEmpleados.getValueAt(fila, 3).toString());
                    cbFecha.setDate(Date.valueOf(tbEmpleados.getValueAt(fila, 5).toString()));
                    txtDireccion.setText(tbEmpleados.getValueAt(fila, 6).toString());
                    txtTelefono.setText(tbEmpleados.getValueAt(fila, 7).toString());
                    txtMail.setText(tbEmpleados.getValueAt(fila, 8).toString());
                    cbEstCiv.setSelectedItem(tbEmpleados.getValueAt(fila, 9).toString());
                    cbGenero.setSelectedItem(tbEmpleados.getValueAt(fila, 10).toString());
                    txtFoto.setText(tbEmpleados.getValueAt(fila, 11).toString());
                    ImageIcon logo=new ImageIcon(txtFoto.getText().trim());
                    Icon icono = new ImageIcon(logo.getImage().getScaledInstance(lbFoto.getWidth(), lbFoto.getHeight(),Image.SCALE_DEFAULT));
                    lbFoto.setIcon(icono);
                    txtClave.setText(tbEmpleados.getValueAt(fila, 12).toString());
                //throw new UnsupportedOperationException("Not supported yet.");
                }
            }
        });
    }
    
    public void actualizarEmpleado(){
        String sql,dir;
        String fecha = new SimpleDateFormat("dd/MMM/yyyy").format(cbFecha.getDate());
        if(!txtFoto.getText().startsWith("src/ImagenesProductos/") ){
          nombreArchivo = txtApellido.getText()+txtNombre.getText()+".jpg";
          dir="src/ImagenesProductos/"+nombreArchivo;
          
         } else{
             dir=txtFoto.getText().trim();
         }
        
        sql="update empleados set nom_emp ='"+txtNombre.getText()+"',"
                +"ape_emp='"+txtApellido.getText()+"',"
                +"car_emp='"+cbCargo.getSelectedItem()+"',"
                +"fec_nac_emp='"+cbFecha.getDate()+"'"
                +"dir_emp='"+txtDireccion.getText()+"',"
                +"tel_emp='"+txtTelefono.getText()+"',"
                +"e_mail_emp='"+txtMail.getText()+"',"
                +"est_civ_emp='"+cbEstCiv.getSelectedItem()+"',"
                +"genero='"+cbGenero.getSelectedItem()+"',"
                +"foto='"+dir+"',"
                +"clave_emp='"+txtClave.getText()+"'"
                +"where ci_emp='"+txtCedula.getText()+"'";
        try {
            //Statements
            PreparedStatement psd=cn.prepareStatement(sql);
            int n=psd.executeUpdate();//Ejecutar el 
            psd.setDate(5, calendario(fecha));
            if(n>0){
                if(!txtFoto.getText().startsWith("src/ImagenesProductos/") ){
                    CopiarFicheros(txtFoto.getText(), dir);
                    ImageIcon logo=new ImageIcon(txtFoto.getText().trim());
                    Icon icono = new ImageIcon(logo.getImage().getScaledInstance(lbFoto.getWidth(), lbFoto.getHeight(),Image.SCALE_DEFAULT));
                    lbFoto.setIcon(icono);  
                }
//                if(!cbFecha.getDate().)
                {
                    
                }
                JOptionPane.showMessageDialog(this, "Se actualizo correctamente");
                cargarTablaEmpleados("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar");
        }
    }
    
    public Date calendario(String fech) throws ParseException{
        java.sql.Date sqlDate = null;
        try {
            java.util.Date utilDate = a.parse(fech);
            sqlDate = new java.sql.Date(utilDate.getTime());
            return sqlDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return sqlDate;
        }
        
    }
    
    private boolean controlarCampos(){
    boolean ver = true;
    if(txtCedula.getText().isEmpty() || txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty()
           || txtTelefono.getText().isEmpty() || txtDireccion.getText().isEmpty())
           ver= false;


      return ver;
    }
    
    public void cargarComboEstadoCivil(){
        try {
            Conexion cc=new Conexion();
            Connection cn= cc.conectar();
            String sql = "";
            sql = "select * from cargos";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                id = rs.getString("nom_car");
                descripcion=rs.getString("suel_emp");
                cbCargo.addItem(id);
                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"No se pudo cargar el combo");
        }
                
    }
    
    public void ColocarFoto(){
        
        String url= "";
        JFileChooser SF = new JFileChooser();
        SF.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int estado = SF.showOpenDialog(null);
        if(estado == JFileChooser.APPROVE_OPTION){
            try {
                fis = new FileInputStream(SF.getSelectedFile());
                this.longitudBytes = (int)SF.getSelectedFile().length();
                url = SF.getSelectedFile().toString();
                txtFoto.setText(url);
                Image icono = ImageIO.read(SF.getSelectedFile()).getScaledInstance(lbFoto.getWidth(), lbFoto.getHeight(), Image.SCALE_DEFAULT);
                lbFoto.setIcon(new ImageIcon(icono));
                lbFoto.updateUI();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public void CopiarFicheros(String dirOrigen, String dirDestino){
        File origen = new File(dirOrigen);
        File destino=new File(dirDestino);
        try {
            InputStream in = new FileInputStream(origen);
            OutputStream out = new FileOutputStream(destino);
            
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf)) > 0){
                out.write(buf,0,len);
                
            }
            in.close();
            out.close();
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private String encontrarTxtVacios(){
          String vac="";
          if (txtCedula.getText().isEmpty()) {
            vac=vac.concat("Cedula\n");  
          }
          
          if (txtNombre.getText().isEmpty()) {
            vac=vac.concat("Nombre\n");  
          }
          
          if (txtApellido.getText().isEmpty()) {
            vac=vac.concat("Apellido\n");  
          }
          if (cbCargo.getSelectedItem()==null) {
            vac=vac.concat("Cargo\n");  
          }
          if (cbFecha.getDate()==null) {
            vac=vac.concat("Fecha de Nacimiento\n");  
          }

          if (txtClave.getText().isEmpty()) {
            vac=vac.concat("Clave\n");  
          }
           
  return vac;
  }
    
    public void insertar(){
        
        if(controlarCampos()==true){
        PreparedStatement pst;
        String fecha;
        String sql;
        String suel;
        fecha = new SimpleDateFormat("yyyy/MM/dd").format(cbFecha.getDate());
        try {
            pst = cn.prepareStatement("INSERT INTO empleados(ci_emp, nom_emp, ape_emp, car_emp, fec_nac_emp, dir_emp, tel_emp,"
                    + " e_mail_emp, est_civ_emp, genero, foto, CLAVE_EMP)"
                        + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            nombreArchivo = txtApellido.getText()+txtNombre.getText()+".jpg";
            String dirDest="src/ImagenesProductos/"+ nombreArchivo ;
            pst.setString(1, txtCedula.getText().toString());
            pst.setString(2, txtNombre.getText().toString());
            pst.setString(3, txtApellido.getText().toString());
            pst.setString(4, cbCargo.getSelectedItem().toString());

            try {
                pst.setDate(5, calendario(fecha));
            } catch (Exception e) {
            }
            
            pst.setString(6, txtDireccion.getText().toString());
            pst.setString(7, txtTelefono.getText().toString());
            pst.setString(8, txtMail.getText().toString());
            pst.setString(9, cbEstCiv.getSelectedItem().toString());
            pst.setString(10, cbGenero.getSelectedItem().toString());
            pst.setString(11, txtFoto.getText().toString());
            pst.setString(12, txtClave.getText().toString());
            int i = pst.executeUpdate();
                if(i>0){
                    CopiarFicheros(txtFoto.getText(), dirDest);
                    cargarTablaEmpleados("");                    
                    JOptionPane.showMessageDialog(null, "EL EMPLEADO HA SIDO REGISTRADO");
                }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se ha podido guardar \n"+e); 
        }
    }else
    {
       JOptionPane.showMessageDialog(this, "Ingrese datos en los siguientes campos:\n"+encontrarTxtVacios()); 
    }
    }
    
    public void cargarTablaEmpleados(String Dato){
        String [] titulos ={"CEDULA","NOMBRE","APELLIDO","CARGO","SUELDO","FECHA","DIRECCIÓN","TELEFONO","E_MAIL","ESTADO CIVIL","GENERO","RUTA FOTO","CLAVE DEL USUARIO"};
        String [] registros=new String[13]; //# de compos que tiena la tabla
        String suel;
        String sql;
        sql="Select e.*, c.suel_emp from empleados AS e join cargos AS c ON e.car_emp=c.nom_car AND e.ci_emp  LIKE '" + Dato + "%'" + "ORDER BY e.ape_emp";
        model=new DefaultTableModel(null,titulos);
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                registros[0]=rs.getString("ci_emp"); //EN EL REGISTRO COJA LO QUE TENGO EN MI CAMPO CEDULA DE LA BDD
                registros[1]=rs.getString("nom_emp");
                registros[2]=rs.getString("ape_emp");
                registros[3]=rs.getString("car_emp");
                registros[4]=rs.getString("suel_emp");
                registros[5]=rs.getString("fec_nac_emp");
                registros[6]=rs.getString("dir_emp");
                registros[7]=rs.getString("tel_emp");
                registros[8]=rs.getString("e_mail_emp");
                registros[9]=rs.getString("est_civ_emp");
                registros[10]=rs.getString("genero");
                registros[11]=rs.getString("foto");
                registros[12]=rs.getString("CLAVE_EMP");
                model.addRow(registros); //model permite manejar la tabla 
                tbEmpleados.setModel(model); //Para modificar la tabla     
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "La tabla Empleados tiene problemas al cargarse\n"+e);
        }
    }
    
    public void borrar(){
        if(JOptionPane.showConfirmDialog(new JInternalFrame(),"Esta seguro que desea eliminar el dato","Borra registro",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
        String sql = "";
        sql = "delete from empleados where ci_emp ='"+txtCedula.getText()+"'";
        try {
            PreparedStatement psd=cn.prepareStatement(sql);
            int n=psd.executeUpdate();
            if(n>0){
                JOptionPane.showMessageDialog(null, "Se borro el registro");
                cargarTablaEmpleados("");
                //limpiar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Nose pudo borrar el registro");
        }
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbCargo = new javax.swing.JComboBox();
        txtTelefono = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cbGenero = new javax.swing.JComboBox();
        cbFecha = new com.toedter.calendar.JDateChooser();
        txtMail = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        cbEstCiv = new javax.swing.JComboBox();
        txtDireccion = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtClave = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        lbFoto = new javax.swing.JLabel();
        btFoto = new javax.swing.JButton();
        txtFoto = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btNuevo = new javax.swing.JButton();
        btGuardar = new javax.swing.JButton();
        btActualizar = new javax.swing.JButton();
        btEliminar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtBuscarCed = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbEmpleados = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24));
        jLabel1.setForeground(new java.awt.Color(0, 0, 204));
        jLabel1.setText("EMPLEADOS");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "INGRESO DE EMPLEADOS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 0, 14), java.awt.Color.darkGray)); // NOI18N

        jLabel2.setText("CEDULA");

        jLabel3.setText("NOMBRE");

        jLabel4.setText("APELLIDO");

        jLabel5.setText("CARGO");

        jLabel6.setText("FECHA DE NACIMIENTO");

        jLabel7.setText("DIRECCIÓN");

        jLabel8.setText("TELEFONO");

        jLabel9.setText("E_MAIL");

        jLabel10.setText("ESTADO CIVIL");

        cbGenero.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "...", "F", "M" }));

        jLabel11.setText("GENERO");

        cbEstCiv.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Escoja el Estado Civil", "SOLTERO(A)", "CASADO(A)", "VIUDO(A)", "DIVORSIADO(A)", "UNION LIBRE(UL)" }));

        jLabel14.setText("CLAVE");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel11)
                            .addComponent(jLabel14)
                            .addComponent(jLabel7))
                        .addGap(96, 96, 96)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(cbGenero, 0, 125, Short.MAX_VALUE)
                            .addComponent(cbEstCiv, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtMail, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(txtCedula, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(cbCargo, 0, 125, Short.MAX_VALUE)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(txtClave, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(49, 49, 49)
                        .addComponent(cbFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cbEstCiv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Foto del Empleado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 0, 14), java.awt.Color.darkGray)); // NOI18N

        btFoto.setText("Escoger Foto");
        btFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFotoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(lbFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtFoto, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btFoto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(txtFoto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btFoto)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Botones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 0, 14), java.awt.Color.darkGray)); // NOI18N

        btNuevo.setText("NUEVO");

        btGuardar.setText("GUARDAR");
        btGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGuardarActionPerformed(evt);
            }
        });

        btActualizar.setText("ACTUALIZAR");
        btActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btActualizarActionPerformed(evt);
            }
        });

        btEliminar.setText("ELIMINAR");
        btEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEliminarActionPerformed(evt);
            }
        });

        btCancelar.setText("CANCELAR");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btNuevo)
                .addGap(18, 18, 18)
                .addComponent(btGuardar)
                .addGap(18, 18, 18)
                .addComponent(btActualizar)
                .addGap(18, 18, 18)
                .addComponent(btEliminar)
                .addGap(18, 18, 18)
                .addComponent(btCancelar)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buscar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 0, 14), java.awt.Color.darkGray)); // NOI18N

        jLabel12.setText("CEDULA");

        txtBuscarCed.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarCedKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(txtBuscarCed, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(txtBuscarCed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tabla de Empleados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 0, 14), java.awt.Color.darkGray)); // NOI18N

        tbEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbEmpleados);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 774, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(387, 387, 387))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(19, 19, 19))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(52, 52, 52))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(136, 136, 136)))
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(45, 45, 45))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFotoActionPerformed
// TODO add your handling code here:
    ColocarFoto();
}//GEN-LAST:event_btFotoActionPerformed

private void btGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGuardarActionPerformed
// TODO add your handling code here:
    insertar();
    cargarTablaEmpleados("");
}//GEN-LAST:event_btGuardarActionPerformed

private void txtBuscarCedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarCedKeyReleased
// TODO add your handling code here:
    cargarTablaEmpleados(txtBuscarCed.getText());
}//GEN-LAST:event_txtBuscarCedKeyReleased

private void btActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btActualizarActionPerformed
    actualizarEmpleado();
}//GEN-LAST:event_btActualizarActionPerformed

private void btEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEliminarActionPerformed
// TODO add your handling code here:
    borrar();
}//GEN-LAST:event_btEliminarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Empleados().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btActualizar;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEliminar;
    private javax.swing.JButton btFoto;
    private javax.swing.JButton btGuardar;
    private javax.swing.JButton btNuevo;
    private javax.swing.JComboBox cbCargo;
    private javax.swing.JComboBox cbEstCiv;
    private com.toedter.calendar.JDateChooser cbFecha;
    private javax.swing.JComboBox cbGenero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbFoto;
    private javax.swing.JTable tbEmpleados;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBuscarCed;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtClave;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtFoto;
    private javax.swing.JTextField txtMail;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
Conexion cc = new Conexion();
Connection cn=cc.conectar();
}
