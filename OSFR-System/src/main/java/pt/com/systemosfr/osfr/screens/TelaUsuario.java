/*
 * The MIT License
 *
 * Copyright 2025 Simão Ferro Rodrigues.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pt.com.systemosfr.screens;

import java.sql.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import pt.com.systemosfr.dal.ModuloConexao;

/**
 * Tela de gestão de utilizadores
 *
 * @author Simão Ferro Rodrigues
 * @version 1.1
 */
public class TelaUsuario extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    /**
     * Método que abre a tela de utilizador
     *
     */
    public TelaUsuario() {
        initComponents();
        setFrameIcon(new ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/icon.png")));
        conexao = ModuloConexao.conector();
    }
    /**
     * Método que limpa os campos de utilizador
     *
     */
    private void limparCampos() {
        lblUsuId.setText(null);
        lblUsuNome.setText(null);
        lblUsuTele.setText(null);
        lblUsuLogin.setText(null);
        lblUsuPass.setText(null);
        cboUsuPerfil.setSelectedItem(null);
    }
    /**
     * Método que consulta os utilizadores
     *
     */
    private void consultar() {
        String sql = "select * from tabusuarios where iduser=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblUsuId.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                lblUsuNome.setText(rs.getString(2));
                lblUsuTele.setText(rs.getString(3));
                lblUsuLogin.setText(rs.getString(4));
                lblUsuPass.setText(rs.getString(5));
                cboUsuPerfil.setSelectedItem(rs.getString(6));
            } else {
                JOptionPane.showMessageDialog(null, "Este utilizador não está registado");
                limparCampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    /**
     * Método que adiciona um utilizador
     *
     */
    private void adicionar() {
        String sql = "insert into tabusuarios(iduser,usuario,telefone,login,password,perfil) values(?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblUsuId.getText());
            pst.setString(2, lblUsuNome.getText());
            pst.setString(3, lblUsuTele.getText());
            pst.setString(4, lblUsuLogin.getText());
            pst.setString(5, lblUsuPass.getText());
            pst.setString(6, cboUsuPerfil.getSelectedItem().toString());
            if ((lblUsuId.getText().isEmpty()) || (lblUsuNome.getText().isEmpty()) || (lblUsuLogin.getText().isEmpty()) || (lblUsuPass.getText().isEmpty()) || (cboUsuPerfil.getSelectedItem().toString().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Utilizador registado com sucesso.");
                    limparCampos();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    /**
     * Método que altera um utilizador
     *
     */
    private void alterar() {
        String sql = "update tabusuarios set usuario=?,telefone=?,login=?,password=?,perfil=? where iduser=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblUsuNome.getText());
            pst.setString(2, lblUsuTele.getText());
            pst.setString(3, lblUsuLogin.getText());
            pst.setString(4, lblUsuPass.getText());
            pst.setString(5, cboUsuPerfil.getSelectedItem().toString());
            pst.setString(6, lblUsuId.getText());
            if ((lblUsuId.getText().isEmpty()) || (lblUsuNome.getText().isEmpty()) || (lblUsuLogin.getText().isEmpty()) || (lblUsuPass.getText().isEmpty()) || (cboUsuPerfil.getSelectedItem() == null)) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do utilizador alterados com sucesso");
                    limparCampos();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    /**
     * Método que elimina um utilizador
     *
     */
    private void eliminar() {
        int confirmacao = JOptionPane.showConfirmDialog(null, "Tem a certeza que deseja eliminar este utilizador?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            String sql = "delete from tabusuarios where iduser=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, lblUsuId.getText());
                int removido = pst.executeUpdate();
                if (removido > 0) {
                    JOptionPane.showMessageDialog(null, "Utilizador eliminado com sucesso");
                    limparCampos();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblUsuId = new javax.swing.JTextField();
        lblUsuNome = new javax.swing.JTextField();
        lblUsuTele = new javax.swing.JTextField();
        lblUsuPass = new javax.swing.JTextField();
        lblUsuLogin = new javax.swing.JTextField();
        cboUsuPerfil = new javax.swing.JComboBox<>();
        btnUsuCreate = new javax.swing.JButton();
        btnUsuRead = new javax.swing.JButton();
        btnUsuDelete = new javax.swing.JButton();
        btnUsuUpdate = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnClean = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(null);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Utilizadores");
        setToolTipText("");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel1.setText("* ID");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(70, 101, 30, 20);

        jLabel2.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel2.setText("* Nome");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(70, 190, 90, 30);

        jLabel3.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel3.setText("* Login");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(70, 271, 70, 30);

        jLabel4.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel4.setText("* Password");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(70, 361, 80, 30);

        jLabel5.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel5.setText("* Perfil");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(400, 280, 100, 30);

        jLabel6.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel6.setText("Telefone/Telemóvel");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(320, 100, 200, 21);
        jPanel1.add(lblUsuId);
        lblUsuId.setBounds(160, 100, 120, 22);

        lblUsuNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblUsuNomeActionPerformed(evt);
            }
        });
        jPanel1.add(lblUsuNome);
        lblUsuNome.setBounds(160, 190, 480, 30);

        lblUsuTele.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblUsuTeleActionPerformed(evt);
            }
        });
        jPanel1.add(lblUsuTele);
        lblUsuTele.setBounds(470, 100, 170, 22);
        jPanel1.add(lblUsuPass);
        lblUsuPass.setBounds(160, 362, 160, 30);

        lblUsuLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblUsuLoginActionPerformed(evt);
            }
        });
        jPanel1.add(lblUsuLogin);
        lblUsuLogin.setBounds(160, 272, 160, 30);

        cboUsuPerfil.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        cboUsuPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "user" }));
        cboUsuPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboUsuPerfilActionPerformed(evt);
            }
        });
        jPanel1.add(cboUsuPerfil);
        cboUsuPerfil.setBounds(490, 280, 160, 30);

        btnUsuCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/Criar.png"))); // NOI18N
        btnUsuCreate.setToolTipText("Adicionar");
        btnUsuCreate.setBorderPainted(false);
        btnUsuCreate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUsuCreate.setPreferredSize(new java.awt.Dimension(80, 80));
        btnUsuCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuCreateActionPerformed(evt);
            }
        });
        jPanel1.add(btnUsuCreate);
        btnUsuCreate.setBounds(120, 490, 70, 80);

        btnUsuRead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/Consultar.png"))); // NOI18N
        btnUsuRead.setToolTipText("Consultar");
        btnUsuRead.setBorderPainted(false);
        btnUsuRead.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUsuRead.setPreferredSize(new java.awt.Dimension(80, 80));
        btnUsuRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuReadActionPerformed(evt);
            }
        });
        jPanel1.add(btnUsuRead);
        btnUsuRead.setBounds(280, 490, 80, 80);

        btnUsuDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/Delete.png"))); // NOI18N
        btnUsuDelete.setToolTipText("Eliminar");
        btnUsuDelete.setBorderPainted(false);
        btnUsuDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUsuDelete.setPreferredSize(new java.awt.Dimension(80, 80));
        btnUsuDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuDeleteActionPerformed(evt);
            }
        });
        jPanel1.add(btnUsuDelete);
        btnUsuDelete.setBounds(610, 490, 80, 80);

        btnUsuUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/Update.png"))); // NOI18N
        btnUsuUpdate.setToolTipText("Alterar");
        btnUsuUpdate.setBorderPainted(false);
        btnUsuUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUsuUpdate.setPreferredSize(new java.awt.Dimension(80, 80));
        btnUsuUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuUpdateActionPerformed(evt);
            }
        });
        jPanel1.add(btnUsuUpdate);
        btnUsuUpdate.setBounds(450, 490, 80, 80);

        jLabel7.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel7.setText("* Campos obrigatórios");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(670, 30, 360, 21);

        btnClean.setFont(new java.awt.Font("Aspekta 250", 0, 12)); // NOI18N
        btnClean.setText("Limpar dados");
        btnClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCleanActionPerformed(evt);
            }
        });
        jPanel1.add(btnClean);
        btnClean.setBounds(70, 40, 120, 25);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
        );

        setBounds(0, 0, 840, 680);
    }// </editor-fold>//GEN-END:initComponents

    private void lblUsuLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblUsuLoginActionPerformed

    }//GEN-LAST:event_lblUsuLoginActionPerformed

    private void lblUsuNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblUsuNomeActionPerformed

    }//GEN-LAST:event_lblUsuNomeActionPerformed

    private void lblUsuTeleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblUsuTeleActionPerformed

    }//GEN-LAST:event_lblUsuTeleActionPerformed

    private void btnUsuCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuCreateActionPerformed

        adicionar();
    }//GEN-LAST:event_btnUsuCreateActionPerformed

    private void btnUsuUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuUpdateActionPerformed

        alterar();
    }//GEN-LAST:event_btnUsuUpdateActionPerformed

    private void btnUsuDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuDeleteActionPerformed

        eliminar();
    }//GEN-LAST:event_btnUsuDeleteActionPerformed

    private void btnUsuReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuReadActionPerformed

        consultar();
    }//GEN-LAST:event_btnUsuReadActionPerformed

    private void cboUsuPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboUsuPerfilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboUsuPerfilActionPerformed

    private void btnCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleanActionPerformed

        limparCampos();
    }//GEN-LAST:event_btnCleanActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClean;
    private javax.swing.JButton btnUsuCreate;
    private javax.swing.JButton btnUsuDelete;
    private javax.swing.JButton btnUsuRead;
    private javax.swing.JButton btnUsuUpdate;
    private javax.swing.JComboBox<String> cboUsuPerfil;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField lblUsuId;
    private javax.swing.JTextField lblUsuLogin;
    private javax.swing.JTextField lblUsuNome;
    private javax.swing.JTextField lblUsuPass;
    private javax.swing.JTextField lblUsuTele;
    // End of variables declaration//GEN-END:variables
}
