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

import java.awt.Image;
import java.awt.Toolkit;
import java.io.InputStream;
import java.sql.*;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pt.com.systemosfr.dal.ModuloConexao;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * Tela de gestão das Ordens de Serviço
 *
 * @author Simão Ferro Rodrigues
 * @version 1.1
 */
public class TelaOS extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    /**
     * Método que reseta os campos, na tela de OS
     *
     */
    private void resetarCampos() {
        lblOsId.setText(null);
        lblOsEquip.setText(null);
        lblOsProb.setText(null);
        lblOsServ.setText(null);
        lblOsTec.setText(null);
        lblOsVal.setText("0");
        lblOsNum.setText(null);
        lblOsData.setText(null);
        cboOsSit.setSelectedItem(" ");
        ((DefaultTableModel) tblClientes.getModel()).setRowCount(0);
        btnOsAdicionar.setEnabled(true);
        btnOsConsultar.setEnabled(true);
        lblOsPesquisa.setEnabled(true);
        tblClientes.setVisible(true);
        btnOsEliminar.setEnabled(false);
        btnOsAlterar.setEnabled(false);
        btnOsImprimir.setEnabled(false);

    }

    private String tipo;
    /**
     * Método que abre a tela das OS
     *
     */
    public TelaOS() {
        initComponents();
        setFrameIcon(new ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/icon.png")));
        conexao = ModuloConexao.conector();
    }
    /**
     * Método que pesquisa clientes, na tela de OS
     *
     */
    private void pesquisarClienteOs() {
        String sql = "select idclient as ID, nomecliente as Nome, telefonecli as Telefone from tabclientes where nomecliente like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, lblOsPesquisa.getText() + "%");
            rs = pst.executeQuery();
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    /**
     * Método que reseta o ID, na tela de OS
     *
     */
    private void setarId() {
        int setar = tblClientes.getSelectedRow();
        lblOsId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
    }
    /**
     * Método que emite as OS
     *
     */
    private void emitirOs() {
        String sql = "insert into tbos(tipo,situacao,equip,problema,servico,tecnico,valor,idcli) values(?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cboOsSit.getSelectedItem().toString());
            pst.setString(3, lblOsEquip.getText());
            pst.setString(4, lblOsProb.getText());
            pst.setString(5, lblOsServ.getText());
            pst.setString(6, lblOsTec.getText());
            pst.setString(7, lblOsVal.getText().replace(",", "."));
            pst.setString(8, lblOsId.getText());

            if ((lblOsId.getText().isEmpty()) || (lblOsEquip.getText().isEmpty()) || (lblOsProb.getText().isEmpty()) || (cboOsSit.getSelectedItem().equals(" "))) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Ordem de serviço emitida com sucesso.");
                    recuperarOs();
                    btnOsAdicionar.setEnabled(false);
                    btnOsConsultar.setEnabled(false);
                    btnOsImprimir.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    /**
     * Método que pesquisa as OS
     *
     */
    private void pesquisarOs() {
        String num_os = JOptionPane.showInputDialog("Número da OS");
        String sql = "select os,date_format(data_os,'%d/%m/%Y - %H:%i'),tipo,situacao,equip,problema,servico,tecnico,valor,idcli from tbos where os= " + num_os;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                lblOsNum.setText(rs.getString(1));
                lblOsData.setText(rs.getString(2));
                String rbtTipo = rs.getString(3);
                if (rbtTipo.equals("OS")) {
                    rbtOs.setSelected(true);
                    tipo = "OS";
                } else {
                    rbtOrc.setSelected(true);
                    tipo = "Orçamento";
                }
                cboOsSit.setSelectedItem(rs.getString(4));
                lblOsEquip.setText(rs.getString(5));
                lblOsProb.setText(rs.getString(6));
                lblOsServ.setText(rs.getString(7));
                lblOsTec.setText(rs.getString(8));
                lblOsVal.setText(rs.getString(9));
                lblOsId.setText(rs.getString(10));
                btnOsAdicionar.setEnabled(false);
                lblOsPesquisa.setEnabled(false);
                tblClientes.setVisible(false);
                btnOsConsultar.setEnabled(false);
                btnOsAlterar.setEnabled(true);
                btnOsEliminar.setEnabled(true);
                btnOsImprimir.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "Ordem de serviço não registada.");
            }
        } catch (java.sql.SQLSyntaxErrorException e) {
            JOptionPane.showMessageDialog(null, "OS inválida");
        } catch (Exception e2) {
            JOptionPane.showMessageDialog(null, e2);
        }
    }
    /**
     * Método que altera as OS
     *
     */
    private void alterarOs() {
        String sql = "update tbos set tipo=?,situacao=?,equip=?,problema=?,servico=?,tecnico=?,valor=? where os=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tipo);
            pst.setString(2, cboOsSit.getSelectedItem().toString());
            pst.setString(3, lblOsEquip.getText());
            pst.setString(4, lblOsProb.getText());
            pst.setString(5, lblOsServ.getText());
            pst.setString(6, lblOsTec.getText());
            pst.setString(7, lblOsVal.getText().replace(",", "."));
            pst.setString(8, lblOsNum.getText());

            if ((lblOsId.getText().isEmpty()) || (lblOsEquip.getText().isEmpty()) || (lblOsProb.getText().isEmpty()) || (cboOsSit.getSelectedItem().equals(" "))) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios.");
            } else {
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Ordem de serviço atualizada com sucesso.");
                    resetarCampos();

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    /**
     * Método que elimina uma OS
     *
     */
    private void eliminarOs() {
        int confirmacao = JOptionPane.showConfirmDialog(null, "Tem a certeza que deseja excluir esta OS?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            String sql = "delete from tbos where os=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, lblOsNum.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "OS eliminada com sucesso");
                    resetarCampos();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    /**
     * Método que imprime uma OS
     *
     */
    private void imprimirOs() {
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão deste relatório de serviços?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                HashMap<String, Object> filtro = new HashMap<>();
                filtro.put("os", Integer.valueOf(lblOsNum.getText()));
                InputStream inputStream = getClass().getResourceAsStream("/reports/os.jasper");
                if (inputStream == null) {
                    JOptionPane.showMessageDialog(null, "Relatório não encontrado!");
                    return;
                }
                JasperPrint print = JasperFillManager.fillReport(inputStream, filtro, conexao);
                JasperViewer.viewReport(print, false);
            } catch (JRException e) {
                JOptionPane.showMessageDialog(null, "Erro ao carregar o relatório: " + e.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro inesperado: " + e.getMessage());
            }
        }
    }
    /**
     * Método que recupera o ID de cliente de uma OS criada
     *
     */
    private void recuperarOs() {
        String sql = "select max(os) from tbos";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                lblOsNum.setText(rs.getString(1));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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

        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        rbtOrc = new javax.swing.JRadioButton();
        rbtOs = new javax.swing.JRadioButton();
        lblOsData = new javax.swing.JTextField();
        lblOsNum = new javax.swing.JTextField();
        cboOsSit = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblOsPesquisa = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        lblOsId = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblOsTec = new javax.swing.JTextField();
        lblOsEquip = new javax.swing.JTextField();
        lblOsProb = new javax.swing.JTextField();
        lblOsServ = new javax.swing.JTextField();
        lblOsVal = new javax.swing.JTextField();
        btnOsEliminar = new javax.swing.JButton();
        btnOsAdicionar = new javax.swing.JButton();
        btnOsConsultar = new javax.swing.JButton();
        btnOsAlterar = new javax.swing.JButton();
        btnOsImprimir = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setBorder(null);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("OS");
        setPreferredSize(new java.awt.Dimension(840, 680));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });
        getContentPane().setLayout(null);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel1.setText("Data");

        jLabel2.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel2.setText("Número OS");

        buttonGroup2.add(rbtOrc);
        rbtOrc.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        rbtOrc.setText("Orçamento");
        rbtOrc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOrcActionPerformed(evt);
            }
        });

        buttonGroup2.add(rbtOs);
        rbtOs.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        rbtOs.setText("Ordem de serviço");
        rbtOs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOsActionPerformed(evt);
            }
        });

        lblOsData.setEditable(false);
        lblOsData.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblOsData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblOsDataActionPerformed(evt);
            }
        });

        lblOsNum.setEditable(false);
        lblOsNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblOsNumActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblOsNum, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblOsData)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(rbtOrc, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addComponent(rbtOs, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOsData, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOsNum, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtOrc)
                    .addComponent(rbtOs))
                .addGap(20, 20, 20))
        );

        jPanel3.add(jPanel1);
        jPanel1.setBounds(30, 70, 340, 197);

        cboOsSit.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        cboOsSit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Em diagnóstico", "Entrega concluída", "Orçamento aprovado", "A aguardar aprovação", "A aguardar peças/equipamento", "Não reclamado pelo cliente", "Em reparação", "Devolvido" }));
        cboOsSit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboOsSitActionPerformed(evt);
            }
        });
        jPanel3.add(cboOsSit);
        cboOsSit.setBounds(130, 300, 247, 31);

        jLabel3.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel3.setText("* Situação");
        jPanel3.add(jLabel3);
        jLabel3.setBounds(50, 300, 66, 31);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));
        jPanel2.setLayout(null);

        lblOsPesquisa.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        lblOsPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lblOsPesquisaActionPerformed(evt);
            }
        });
        lblOsPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lblOsPesquisaKeyReleased(evt);
            }
        });
        jPanel2.add(lblOsPesquisa);
        lblOsPesquisa.setBounds(11, 39, 213, 27);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/search.png"))); // NOI18N
        jPanel2.add(jLabel4);
        jLabel4.setBounds(236, 42, 24, 24);

        lblOsId.setEditable(false);
        jPanel2.add(lblOsId);
        lblOsId.setBounds(350, 40, 64, 22);

        jLabel5.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel5.setText("* ID");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(320, 40, 29, 20);

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Nome", "Telefone"
            }
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(30, 80, 380, 221);

        jPanel3.add(jPanel2);
        jPanel2.setBounds(392, 22, 442, 317);

        jLabel6.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel6.setText("* Valor");
        jPanel3.add(jLabel6);
        jLabel6.setBounds(470, 490, 50, 30);

        jLabel7.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel7.setText("* Equipamento");
        jPanel3.add(jLabel7);
        jLabel7.setBounds(20, 350, 110, 30);

        jLabel8.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel8.setText("* Problema");
        jPanel3.add(jLabel8);
        jLabel8.setBounds(40, 390, 71, 30);

        jLabel9.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel9.setText("Serviço");
        jPanel3.add(jLabel9);
        jLabel9.setBounds(60, 440, 73, 30);

        jLabel10.setFont(new java.awt.Font("Aspekta 250", 0, 14)); // NOI18N
        jLabel10.setText("Técnico");
        jPanel3.add(jLabel10);
        jLabel10.setBounds(60, 490, 50, 30);
        jPanel3.add(lblOsTec);
        lblOsTec.setBounds(130, 490, 270, 30);
        jPanel3.add(lblOsEquip);
        lblOsEquip.setBounds(130, 350, 670, 30);
        jPanel3.add(lblOsProb);
        lblOsProb.setBounds(130, 390, 670, 30);
        jPanel3.add(lblOsServ);
        lblOsServ.setBounds(130, 440, 670, 30);

        lblOsVal.setText("0");
        jPanel3.add(lblOsVal);
        lblOsVal.setBounds(520, 490, 280, 30);

        btnOsEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/Delete.png"))); // NOI18N
        btnOsEliminar.setToolTipText("Eliminar");
        btnOsEliminar.setBorder(null);
        btnOsEliminar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOsEliminar.setEnabled(false);
        btnOsEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsEliminarActionPerformed(evt);
            }
        });
        jPanel3.add(btnOsEliminar);
        btnOsEliminar.setBounds(400, 560, 64, 64);

        btnOsAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/Criar.png"))); // NOI18N
        btnOsAdicionar.setToolTipText("Emitir");
        btnOsAdicionar.setBorder(null);
        btnOsAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOsAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsAdicionarActionPerformed(evt);
            }
        });
        jPanel3.add(btnOsAdicionar);
        btnOsAdicionar.setBounds(30, 560, 70, 70);

        btnOsConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/Consultar.png"))); // NOI18N
        btnOsConsultar.setToolTipText("Consultar");
        btnOsConsultar.setBorder(null);
        btnOsConsultar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOsConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsConsultarActionPerformed(evt);
            }
        });
        jPanel3.add(btnOsConsultar);
        btnOsConsultar.setBounds(150, 560, 64, 64);

        btnOsAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/Update.png"))); // NOI18N
        btnOsAlterar.setToolTipText("Alterar");
        btnOsAlterar.setBorder(null);
        btnOsAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOsAlterar.setEnabled(false);
        btnOsAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsAlterarActionPerformed(evt);
            }
        });
        jPanel3.add(btnOsAlterar);
        btnOsAlterar.setBounds(280, 560, 64, 64);

        btnOsImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pt/com/systemosfr/icons/imprimir.png"))); // NOI18N
        btnOsImprimir.setToolTipText("Imprimir OS");
        btnOsImprimir.setBorder(null);
        btnOsImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnOsImprimir.setEnabled(false);
        btnOsImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOsImprimirActionPerformed(evt);
            }
        });
        jPanel3.add(btnOsImprimir);
        btnOsImprimir.setBounds(710, 560, 70, 70);

        jButton1.setFont(new java.awt.Font("Aspekta 250", 0, 12)); // NOI18N
        jButton1.setText("Resetar tudo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);
        jButton1.setBounds(30, 30, 100, 25);

        getContentPane().add(jPanel3);
        jPanel3.setBounds(0, 0, 840, 650);

        setBounds(0, 0, 840, 672);
    }// </editor-fold>//GEN-END:initComponents

    private void rbtOrcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOrcActionPerformed

        tipo = "Orçamento";
    }//GEN-LAST:event_rbtOrcActionPerformed

    private void rbtOsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOsActionPerformed

        tipo = "OS";
    }//GEN-LAST:event_rbtOsActionPerformed

    private void lblOsDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblOsDataActionPerformed

    }//GEN-LAST:event_lblOsDataActionPerformed

    private void lblOsNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblOsNumActionPerformed

    }//GEN-LAST:event_lblOsNumActionPerformed

    private void cboOsSitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboOsSitActionPerformed

    }//GEN-LAST:event_cboOsSitActionPerformed

    private void lblOsPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lblOsPesquisaActionPerformed

    }//GEN-LAST:event_lblOsPesquisaActionPerformed

    private void lblOsPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblOsPesquisaKeyReleased

        pesquisarClienteOs();
    }//GEN-LAST:event_lblOsPesquisaKeyReleased

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked

        setarId();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

        rbtOrc.setSelected(true);
        tipo = "Orçamento";
    }//GEN-LAST:event_formInternalFrameOpened

    private void btnOsAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsAdicionarActionPerformed

        emitirOs();
    }//GEN-LAST:event_btnOsAdicionarActionPerformed

    private void btnOsConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsConsultarActionPerformed

        pesquisarOs();
    }//GEN-LAST:event_btnOsConsultarActionPerformed

    private void btnOsAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsAlterarActionPerformed

        alterarOs();
    }//GEN-LAST:event_btnOsAlterarActionPerformed

    private void btnOsEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsEliminarActionPerformed

        eliminarOs();
    }//GEN-LAST:event_btnOsEliminarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        resetarCampos();
        lblOsVal.setText("0");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnOsImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOsImprimirActionPerformed

        imprimirOs();
    }//GEN-LAST:event_btnOsImprimirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOsAdicionar;
    private javax.swing.JButton btnOsAlterar;
    private javax.swing.JButton btnOsConsultar;
    private javax.swing.JButton btnOsEliminar;
    private javax.swing.JButton btnOsImprimir;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cboOsSit;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField lblOsData;
    private javax.swing.JTextField lblOsEquip;
    private javax.swing.JTextField lblOsId;
    private javax.swing.JTextField lblOsNum;
    private javax.swing.JTextField lblOsPesquisa;
    private javax.swing.JTextField lblOsProb;
    private javax.swing.JTextField lblOsServ;
    private javax.swing.JTextField lblOsTec;
    private javax.swing.JTextField lblOsVal;
    private javax.swing.JRadioButton rbtOrc;
    private javax.swing.JRadioButton rbtOs;
    private javax.swing.JTable tblClientes;
    // End of variables declaration//GEN-END:variables
}
