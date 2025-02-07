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
import pt.com.systemosfr.dal.ModuloConexao;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 * Tela de gestão de clientes
 *
 * @author Simão Ferro Rodrigues
 * @version 1.1
 */
public class TelaCliente extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    /**
     * Método que abre a Tela de clientes
     *
     */
    public TelaCliente() {
        initComponents();
        setFrameIcon(new ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/icon.png")));
        conexao = ModuloConexao.conector();
    }
    /**
     * Método que limpa os campos, na tela de clientes
     *
     */
    private void limparCamposCli() {
        lblCliId.setText(null);
        lblCliPesquisar.setText(null);
        lblCliNome.setText(null);
        lblCliEndereco.setText(null);
        lblCliTelefone.setText(null);
        lblCliEmail.setText(null);
        lblCliCidade.setText(null);
        lblCliCp.setText(null);
        ((DefaultTableModel) tblClientes.getModel()).setRowCount(0);
    }
    /**
     * Método que adiciona os clientes, na tela de clientes
     *
     */
    private void adicionarCli() {
        String sql = "insert into tabclientes(nomecliente,enderecocli,telefonecli,emailcli,cidadecli,cpcli) values(?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblCliNome.getText());
            pst.setString(2, lblCliEndereco.getText());
            pst.setString(3, lblCliTelefone.getText());
            pst.setString(4, lblCliEmail.getText());
            pst.setString(5, lblCliCidade.getText());
            pst.setString(6, lblCliCp.getText());
            if ((lblCliNome.getText().isEmpty()) || (lblCliTelefone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente registado com sucesso.");
                    limparCamposCli();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    /**
     * Método que altera os clientes, na tela de clientes
     *
     */
    private void alterarCli() {
        String sql = "update tabclientes set nomecliente=?,enderecocli=?,telefonecli=?,emailcli=?,cidadecli=?,cpcli=? where idclient=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblCliNome.getText());
            pst.setString(2, lblCliEndereco.getText());
            pst.setString(3, lblCliTelefone.getText());
            pst.setString(4, lblCliEmail.getText());
            pst.setString(5, lblCliCidade.getText());
            pst.setString(6, lblCliCp.getText());
            pst.setString(7, lblCliId.getText());
            if ((lblCliNome.getText().isEmpty()) || (lblCliTelefone.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do cliente atualizados com sucesso");
                    limparCamposCli();
                    btnCliAdicionar.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    /**
     * Método que elimina um cliente, na tela de clientes
     *
     */
    private void eliminarCli() {
        int confirmacao = JOptionPane.showConfirmDialog(null, "Tem a certeza que deseja eliminar este cliente?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            String sql = "delete from tabclientes where idclient=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, lblCliId.getText());
                int removido = pst.executeUpdate();
                if (removido > 0) {
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso");
                    limparCamposCli();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    /**
     * Método que pesquisa clientes, na tela de clientes
     *
     */
    private void pesquisarCliente() {
        String sql = "select * from tabclientes where nomecliente like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
            tblClientes.getColumnModel().getColumn(0).setHeaderValue("ID");
            tblClientes.getColumnModel().getColumn(1).setHeaderValue("Nome");
            tblClientes.getColumnModel().getColumn(2).setHeaderValue("Morada");
            tblClientes.getColumnModel().getColumn(3).setHeaderValue("Telefone");
            tblClientes.getColumnModel().getColumn(4).setHeaderValue("E-mail");
            tblClientes.getColumnModel().getColumn(5).setHeaderValue("Cidade");
            tblClientes.getColumnModel().getColumn(6).setHeaderValue("Cód.Postal");
            tblClientes.getTableHeader().repaint();
            tblClientes.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblClientes.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblClientes.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblClientes.getColumnModel().getColumn(3).setPreferredWidth(60);
            tblClientes.getColumnModel().getColumn(4).setPreferredWidth(178);
            tblClientes.getColumnModel().getColumn(5).setPreferredWidth(80);
            tblClientes.getColumnModel().getColumn(6).setPreferredWidth(60);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    /**
     * Método que seta os campos, na tela de clientes
     *
     */
    public void setarCampos() {
        int setar = tblClientes.getSelectedRow();
        lblCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
        lblCliNome.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
        lblCliEndereco.setText(tblClientes.getModel().getValueAt(setar, 2).toString());
        lblCliTelefone.setText(tblClientes.getModel().getValueAt(setar, 3).toString());
        lblCliEmail.setText(tblClientes.getModel().getValueAt(setar, 4).toString());
        lblCliCidade.setText(tblClientes.getModel().getValueAt(setar, 5).toString());
        lblCliCp.setText(tblClientes.getModel().getValueAt(setar, 6).toString());
        btnCliAdicionar.setEnabled(false);
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
        jLabel7 = new javax.swing.JLabel();
        lblCliEmail = new javax.swing.JTextField();
        lblCliNome = new javax.swing.JTextField();
        lblCliEndereco = new javax.swing.JTextField();
        lblCliCp = new javax.swing.JTextField();
        lblCliTelefone = new javax.swing.JTextField();
        lblCliCidade = new javax.swing.JTextField();
        btnCliAdicionar = new javax.swing.JButton();
        btnCliAlterar = new javax.swing.JButton();
        btnCliRemover = new javax.swing.JButton();
        lblCliPesquisar = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        btnCleanCli = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        lblCliId = new javax.swing.JTextField();

        setBorder(null);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Clientes");
        setPreferredSize(new java.awt.Dimension(840, 656));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel1.setText("* Campos Obrigatórios");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(678, 32, 142, 21);

        jLabel2.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel2.setText("* Nome");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(30, 310, 60, 30);

        jLabel3.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel3.setText("Morada");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(30, 370, 60, 30);

        jLabel4.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel4.setText("* Telefone/Telemóvel");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(460, 260, 150, 30);

        jLabel5.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel5.setText("Cidade");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(540, 350, 70, 30);

        jLabel6.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel6.setText("Código Postal");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(500, 440, 120, 30);

        jLabel7.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel7.setText("E-mail");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(30, 440, 50, 30);
        jPanel1.add(lblCliEmail);
        lblCliEmail.setBounds(100, 440, 330, 30);
        jPanel1.add(lblCliNome);
        lblCliNome.setBounds(100, 310, 330, 30);
        jPanel1.add(lblCliEndereco);
        lblCliEndereco.setBounds(100, 370, 330, 30);
        jPanel1.add(lblCliCp);
        lblCliCp.setBounds(630, 440, 170, 30);
        jPanel1.add(lblCliTelefone);
        lblCliTelefone.setBounds(630, 260, 170, 30);

        lblCliCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblCliCidadeActionPerformed(evt);
            }
        });
        jPanel1.add(lblCliCidade);
        lblCliCidade.setBounds(630, 350, 170, 30);

        btnCliAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/Criar.png"))); // NOI18N
        btnCliAdicionar.setToolTipText("Adicionar");
        btnCliAdicionar.setBorderPainted(false);
        btnCliAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliAdicionarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCliAdicionar);
        btnCliAdicionar.setBounds(150, 550, 70, 70);

        btnCliAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/Update.png"))); // NOI18N
        btnCliAlterar.setToolTipText("Alterar");
        btnCliAlterar.setBorderPainted(false);
        btnCliAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliAlterarActionPerformed(evt);
            }
        });
        jPanel1.add(btnCliAlterar);
        btnCliAlterar.setBounds(390, 550, 70, 70);

        btnCliRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/Delete.png"))); // NOI18N
        btnCliRemover.setToolTipText("Eliminar");
        btnCliRemover.setBorderPainted(false);
        btnCliRemover.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliRemoverActionPerformed(evt);
            }
        });
        jPanel1.add(btnCliRemover);
        btnCliRemover.setBounds(610, 550, 70, 70);

        lblCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lblCliPesquisarKeyReleased(evt);
            }
        });
        jPanel1.add(lblCliPesquisar);
        lblCliPesquisar.setBounds(30, 40, 410, 22);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/search.png"))); // NOI18N
        jPanel1.add(jLabel8);
        jLabel8.setBounds(440, 40, 24, 20);

        jLabel9.setFont(new java.awt.Font("Aspekta 250", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 204, 204));
        jLabel9.setText("Pesquisar...");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(30, 20, 170, 18);

        tblClientes = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID Cliente", "Nome", "Morada", "Telefone", "E-mail", "Cidade", "Código Postal"
            }
        ));
        tblClientes.setFocusable(false);
        tblClientes.getTableHeader().setReorderingAllowed(false);
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(30, 70, 780, 150);

        btnCleanCli.setFont(new java.awt.Font("Aspekta 250", 0, 12)); // NOI18N
        btnCleanCli.setText("Limpar dados");
        btnCleanCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCleanCliActionPerformed(evt);
            }
        });
        jPanel1.add(btnCleanCli);
        btnCleanCli.setBounds(20, 500, 110, 25);

        jLabel10.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel10.setText("ID Cliente");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(30, 250, 70, 30);

        lblCliId.setEnabled(false);
        jPanel1.add(lblCliId);
        lblCliId.setBounds(100, 250, 110, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
        );

        setBounds(0, 0, 840, 673);
    }// </editor-fold>//GEN-END:initComponents

    private void lblCliCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblCliCidadeActionPerformed

    }//GEN-LAST:event_lblCliCidadeActionPerformed

    private void btnCliAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliAdicionarActionPerformed

        adicionarCli();
    }//GEN-LAST:event_btnCliAdicionarActionPerformed

    private void btnCleanCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleanCliActionPerformed

        limparCamposCli();
        btnCliAdicionar.setEnabled(true);
    }//GEN-LAST:event_btnCleanCliActionPerformed

    private void lblCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblCliPesquisarKeyReleased

        pesquisarCliente();
    }//GEN-LAST:event_lblCliPesquisarKeyReleased

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked

        setarCampos();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnCliAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliAlterarActionPerformed

        alterarCli();
    }//GEN-LAST:event_btnCliAlterarActionPerformed

    private void btnCliRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliRemoverActionPerformed

        eliminarCli();
    }//GEN-LAST:event_btnCliRemoverActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCleanCli;
    private javax.swing.JButton btnCliAdicionar;
    private javax.swing.JButton btnCliAlterar;
    private javax.swing.JButton btnCliRemover;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField lblCliCidade;
    private javax.swing.JTextField lblCliCp;
    private javax.swing.JTextField lblCliEmail;
    private javax.swing.JTextField lblCliEndereco;
    private javax.swing.JTextField lblCliId;
    private javax.swing.JTextField lblCliNome;
    private javax.swing.JTextField lblCliPesquisar;
    private javax.swing.JTextField lblCliTelefone;
    private javax.swing.JTable tblClientes;
    // End of variables declaration//GEN-END:variables
}
