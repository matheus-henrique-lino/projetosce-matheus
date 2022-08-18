package br.com.salomaotyres.telas;

import java.sql.*;
import br.com.salomaotyres.dal.ModuloConexao;
import net.proteanit.sql.DbUtils;
import javax.swing.JOptionPane;

public class TelaLoja extends javax.swing.JInternalFrame {

    public TelaLoja() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisar_loja();
        btnEditar.setEnabled(false);
    }

    PreparedStatement pst = null;
    ResultSet rs = null;
    Connection conexao = null;

    public void pesquisar_loja() {
        String select = "select * from loja where Nome like ?";

        try {

            pst = conexao.prepareStatement(select);

            pst.setString(1, txtLojPesquisa.getText() + "%");

            rs = pst.executeQuery();

            tblLoja.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }

    }

    public void cadastrar() {
        String insert = "insert into loja (Nome, Cnpj, Cep, Cidade, Estado, Endereco, Numero, Bairro, Contato) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            if (txtLojBairro.getText().isEmpty()
                    && txtLojCep.getText().isEmpty()
                    && txtLojCidade.getText().isEmpty()
                    && txtLojCnpj.getText().isEmpty()
                    && txtLojContato.getText().isEmpty()
                    && txtLojEndereco.getText().isEmpty()
                    && txtLojEstado.getText().isEmpty()
                    && txtLojNome.getText().isEmpty()
                    && txtLojNumero.getText().isEmpty()) {

                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");

            } else {

                pst = conexao.prepareStatement(insert);

                pst.setString(1, txtLojNome.getText());
                pst.setString(2, txtLojCnpj.getText());
                pst.setString(3, txtLojCep.getText());
                pst.setString(4, txtLojCidade.getText());
                pst.setString(5, txtLojEstado.getText());
                pst.setString(6, txtLojEndereco.getText());
                pst.setString(7, txtLojNumero.getText());
                pst.setString(8, txtLojBairro.getText());
                pst.setString(9, txtLojContato.getText());

                int adicionar = pst.executeUpdate();

                if (adicionar > 0) {

                    JOptionPane.showMessageDialog(null, "Loja cadastrada com sucesso!");

                    pesquisar_loja();

                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }
    }
    
    private void editar() {
        String sql = "update loja set Nome=?, Cnpj =?, Cep = ?, Cidade =?, Estado =?, Endereco = ?, Numero = ?, Bairro = ?, Contato = ? where Cod_Loja = ?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtLojNome.getText());
            pst.setString(3, txtLojCnpj.getText());
            pst.setString(4, txtLojCidade.getText());
            pst.setString(5, txtLojEstado.getText());
            pst.setString(6, txtLojEndereco.getText());
            pst.setString(7, txtLojNumero.getText());
            pst.setString(8, txtLojBairro.getText());
            pst.setString(9, txtLojContato.getText());
            pst.setString(10, txtLojCep.getText());
            pst.setString(11, lblLojCodigo.getText());

            if (txtLojBairro.getText().isEmpty()
                    && txtLojCep.getText().isEmpty()
                    && txtLojCidade.getText().isEmpty()
                    && txtLojCnpj.getText().isEmpty()
                    && txtLojContato.getText().isEmpty()
                    && txtLojEndereco.getText().isEmpty()
                    && txtLojEstado.getText().isEmpty()
                    && txtLojNome.getText().isEmpty()
                    && txtLojNumero.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");

            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Alteração realizada com sucesso");
                    limpar_campos();
                    pesquisar_loja();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    
    public void excluir (){
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover esta loja?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {
            
            String sql = "delete from loja where Cod_Loja = ?";
            
            try {
                
                pst = conexao.prepareStatement(sql);
                
                pst.setString(1, lblLojCodigo.getText());
                
                int apagado = pst.executeUpdate();

                if (apagado > 0) {
                    
                    JOptionPane.showMessageDialog(null, "Loja removida com cucesso");
                    
                    pesquisar_loja();
                    
                    limpar_campos();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public void limpar_campos(){
        
        lblLojCodigo.setText(null);
        txtLojNome.setText(null);
        txtLojBairro.setText(null);
        txtLojCep.setText(null);
        txtLojCidade.setText(null);
        txtLojCnpj.setText(null);
        txtLojContato.setText(null);
        txtLojEndereco.setText(null);
        txtLojEstado.setText(null);
        txtLojNumero.setText(null);
        btnCadastrar.setEnabled(true);
        btnEditar.setEnabled(false);
        
    }
    
    public void setar_campos() {
        int setar = tblLoja.getSelectedRow();
        lblLojCodigo.setText(tblLoja.getModel().getValueAt(setar, 0).toString());
        txtLojNome.setText(tblLoja.getModel().getValueAt(setar, 1).toString());
        txtLojCnpj.setText(tblLoja.getModel().getValueAt(setar, 2).toString());
        txtLojCep.setText(tblLoja.getModel().getValueAt(setar, 3).toString());
        txtLojCidade.setText(tblLoja.getModel().getValueAt(setar, 4).toString());
        txtLojEstado.setText(tblLoja.getModel().getValueAt(setar, 5).toString());
        txtLojEndereco.setText(tblLoja.getModel().getValueAt(setar, 6).toString());
        txtLojNumero.setText(tblLoja.getModel().getValueAt(setar, 7).toString());
        txtLojBairro.setText(tblLoja.getModel().getValueAt(setar, 8).toString());
        txtLojContato.setText(tblLoja.getModel().getValueAt(setar, 9).toString());
        btnCadastrar.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtLojPesquisa = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLoja = new javax.swing.JTable();
        btnCadastrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblLojCodigo = new javax.swing.JLabel();
        txtLojNome = new javax.swing.JTextField();
        txtLojEndereco = new javax.swing.JTextField();
        txtLojBairro = new javax.swing.JTextField();
        txtLojEstado = new javax.swing.JTextField();
        txtLojCep = new javax.swing.JTextField();
        txtLojCidade = new javax.swing.JTextField();
        txtLojCnpj = new javax.swing.JTextField();
        txtLojContato = new javax.swing.JTextField();
        txtLojNumero = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Loja");

        txtLojPesquisa.setToolTipText("");
        txtLojPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLojPesquisaKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Lojas");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Pesquisar.png"))); // NOI18N

        tblLoja.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblLoja.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLojaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblLoja);

        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Create.png"))); // NOI18N
        btnCadastrar.setToolTipText("Cadastrar");
        btnCadastrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Update.png"))); // NOI18N
        btnEditar.setToolTipText("Editar");
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditar.setPreferredSize(new java.awt.Dimension(97, 75));
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Delete.png"))); // NOI18N
        btnExcluir.setToolTipText("Excluir");
        btnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Clean.png"))); // NOI18N
        btnLimpar.setToolTipText("Limpar Campos");
        btnLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        jLabel3.setText("Código:");

        jLabel4.setText("* Nome:");

        jLabel5.setText("* CNPJ:");

        jLabel6.setText(" * Endereço:");

        jLabel7.setText("* Número:");

        jLabel8.setText("* Bairro:");

        jLabel9.setText("* Cidade:");

        jLabel10.setText("* Contato:");

        jLabel11.setText("* CEP:");

        jLabel12.setText("* Estado:");

        jLabel13.setText("* Campos Obrigatórios");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtLojPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 499, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(102, 102, 102))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(299, 299, 299))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel13)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 686, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(66, 66, 66))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtLojCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtLojBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(txtLojCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(35, 35, 35)
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtLojContato))
                                    .addComponent(txtLojNome)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lblLojCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(60, 60, 60)
                                        .addComponent(btnCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(62, 62, 62)
                                        .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(51, 51, 51)
                                        .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtLojEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtLojNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtLojCep, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel12)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtLojEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(143, 143, 143))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtLojPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLojNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtLojCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtLojContato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtLojEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLojNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel8)
                    .addComponent(txtLojBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLojEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLojCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtLojCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnCadastrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblLojCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(btnLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );

        setBounds(0, 0, 843, 607);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        // Chamando o método cadastrar
        cadastrar();
    }//GEN-LAST:event_btnCadastrarActionPerformed

    private void txtLojPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLojPesquisaKeyReleased
        // chamando o método pesquisar_loja
        pesquisar_loja();
    }//GEN-LAST:event_txtLojPesquisaKeyReleased

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // Chamando o método Editar
        editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // Chamando o método excluir
        excluir();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        // Chamando o método limpar_campos
        limpar_campos();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void tblLojaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLojaMouseClicked
        // Chamando o método setar_campos
        setar_campos();
        btnCadastrar.setEnabled(false);
        btnEditar.setEnabled(true);
    }//GEN-LAST:event_tblLojaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblLojCodigo;
    private javax.swing.JTable tblLoja;
    private javax.swing.JTextField txtLojBairro;
    private javax.swing.JTextField txtLojCep;
    private javax.swing.JTextField txtLojCidade;
    private javax.swing.JTextField txtLojCnpj;
    private javax.swing.JTextField txtLojContato;
    private javax.swing.JTextField txtLojEndereco;
    private javax.swing.JTextField txtLojEstado;
    private javax.swing.JTextField txtLojNome;
    private javax.swing.JTextField txtLojNumero;
    private javax.swing.JTextField txtLojPesquisa;
    // End of variables declaration//GEN-END:variables
}
