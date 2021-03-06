/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package scratch2littlebits;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author eduardo
 */
public class Scratch2LittleBits extends javax.swing.JFrame {


    private ScratchArduino moArduino;
    private ScratchHttp moHTTP;
    private ScratchHttp moHTTPAndroid;
    private Thread moThrHTTP;
    private Thread moThrHTTPAndroid;
    private final Timer moTimer;
    private boolean mbConectados = false;
    private boolean mbDesactivarEvent  = false;
    private boolean mbClick=false;
    /**
     * Creates new form Scratch2LittleBits
     */
    public Scratch2LittleBits() {
        initComponents();
        moTimer = new Timer();
        moTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {

                        if(moArduino!=null && moArduino.isConnected()){
                            lblLittleBits.setText("LittleBits Conectado");
                        }else{
                            lblLittleBits.setText("LittleBits DESCONECTADO");
                            recargarPuertos();
                        }
                        if(moHTTP!=null && moHTTP.isConectado()){
                            lblScratch.setText("Scratch Conectado");
                        }else{
                            lblScratch.setText("Scratch DESCONECTADO");
                        }
                        if(moHTTPAndroid!=null && moHTTPAndroid.isConectado()){
                            lblAndroid.setText("Android Conectado");
                        }else{
                            lblAndroid.setText("Android DESCONECTADO");
                        }
                        if(moHTTP!=null && moHTTP.isConectado() && moArduino!=null && moArduino.isConnected()){
                            if(!mbConectados){
                                lblConexion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon_connected.png")));
                                setAlwaysOnTop(false);
                            }
                            mbConectados = true;
                        }else{
                            if(mbConectados){
                                lblConexion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon.png")));
                                setAlwaysOnTop(true);
                            }
                            mbConectados = false;
                        }
                        if(moArduino!=null){
                            jComboBox1.setSelectedItem(moArduino.getPuerto());                            
                        }
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }, 50, 2000);
    }

    public void conectarHTTP() throws IOException {
        moHTTP = new ScratchHttp(this, ScratchHttp.PORT);
        moThrHTTP = new Thread(moHTTP);
        moThrHTTP.start();

        moHTTPAndroid = new ScratchHttp(this, ScratchHttp.PORTAndroid);
        moThrHTTPAndroid = new Thread(moHTTPAndroid);
        moThrHTTPAndroid.start();
    }

    public void conectarArduino(String psPuerto) throws Exception {
        if(moArduino!=null){
            moArduino.close();
        }
        moArduino = new ScratchArduino(psPuerto);
        recargarPuertos();
    }
    public ScratchArduino getArduino(){
        return moArduino;
    } 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblConexion = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        lblLittleBits = new javax.swing.JLabel();
        btnReAbrir = new javax.swing.JButton();
        lblScratch = new javax.swing.JLabel();
        lblAndroid = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Scratch + Littlebits");
        setAlwaysOnTop(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblConexion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        getContentPane().add(lblConexion, gridBagConstraints);

        jLabel1.setText("Puerto littlebits");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        getContentPane().add(jLabel1, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBox1FocusGained(evt);
            }
        });
        jComboBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox1MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        getContentPane().add(jComboBox1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        lblLittleBits.setBackground(new java.awt.Color(255, 204, 204));
        lblLittleBits.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLittleBits.setText(" ");
        lblLittleBits.setOpaque(true);
        lblLittleBits.setPreferredSize(new java.awt.Dimension(4, 25));
        lblLittleBits.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLittleBitsMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(lblLittleBits, gridBagConstraints);

        btnReAbrir.setText("R");
        btnReAbrir.setMaximumSize(new java.awt.Dimension(25, 25));
        btnReAbrir.setMinimumSize(new java.awt.Dimension(25, 25));
        btnReAbrir.setPreferredSize(new java.awt.Dimension(25, 25));
        btnReAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReAbrirActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        jPanel1.add(btnReAbrir, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        getContentPane().add(jPanel1, gridBagConstraints);

        lblScratch.setBackground(new java.awt.Color(255, 204, 51));
        lblScratch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblScratch.setText(" ");
        lblScratch.setMaximumSize(new java.awt.Dimension(4, 25));
        lblScratch.setMinimumSize(new java.awt.Dimension(4, 25));
        lblScratch.setOpaque(true);
        lblScratch.setPreferredSize(new java.awt.Dimension(4, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        getContentPane().add(lblScratch, gridBagConstraints);

        lblAndroid.setBackground(new java.awt.Color(0, 255, 51));
        lblAndroid.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAndroid.setText(" ");
        lblAndroid.setMaximumSize(new java.awt.Dimension(4, 25));
        lblAndroid.setMinimumSize(new java.awt.Dimension(4, 25));
        lblAndroid.setOpaque(true);
        lblAndroid.setPreferredSize(new java.awt.Dimension(4, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        getContentPane().add(lblAndroid, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox1MouseClicked
        mbClick = true;
    }//GEN-LAST:event_jComboBox1MouseClicked

    private void jComboBox1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBox1FocusGained
        try {
            recargarPuertos();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jComboBox1FocusGained

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if(moArduino!=null && jComboBox1.getSelectedItem()!=null && !mbDesactivarEvent && mbClick){
            mbClick=false;
            if(!jComboBox1.getSelectedItem().toString().equals(moArduino.getPuerto())){
                try {
                    moArduino.setPuerto(jComboBox1.getSelectedItem().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(rootPane, ex.toString());
                }
            }
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void btnReAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReAbrirActionPerformed
        try {
            recargarPuertos();
            moArduino.closePort();
            moArduino.openPort();
        } catch (Exception ex) {
            ex.printStackTrace();
        }         
    }//GEN-LAST:event_btnReAbrirActionPerformed

    private void lblLittleBitsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLittleBitsMouseClicked
        btnReAbrirActionPerformed(null);
    }//GEN-LAST:event_lblLittleBitsMouseClicked

    private void recargarPuertos(){
        if(moArduino!=null){
            mbDesactivarEvent  = true;
            try{
                String[] lasLista = moArduino.getListaPuertos();
                jComboBox1.removeAllItems();
                for (String lasLista1 : lasLista) {
                    jComboBox1.addItem(lasLista1);
                }
                jComboBox1.setSelectedIndex(lasLista.length-1);
            }finally{
                mbDesactivarEvent  = false;
            }
        }
    }
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
            java.util.logging.Logger.getLogger(Scratch2LittleBits.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Scratch2LittleBits.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Scratch2LittleBits.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Scratch2LittleBits.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Scratch2LittleBits lo = new Scratch2LittleBits();
                    lo.conectarHTTP();
                    lo.conectarArduino("");
                    lo.setVisible(true);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(new JLabel(), ex.toString());
                }
            }
        });
    }


    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReAbrir;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblAndroid;
    private javax.swing.JLabel lblConexion;
    private javax.swing.JLabel lblLittleBits;
    private javax.swing.JLabel lblScratch;
    // End of variables declaration//GEN-END:variables
}
