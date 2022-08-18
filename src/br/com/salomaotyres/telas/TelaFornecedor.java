package br.com.salomaotyres.telas;

import java.sql.*;
import br.com.salomaotyres.dal.ModuloConexao;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class TelaFornecedor extends javax.swing.JInternalFrame {
    
    public TelaFornecedor() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisar_fornecedor();
    }
    
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    private void cadastrar() {
        String sql = "INSERT INTO fornecedor (Nome, Contato, Cidade, Cep, Endereco, Numero, Bairro) Values (?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtFornNome.getText());
            pst.setString(2, txtFornContato.getText());
            pst.setString(3, txtFornCidade.getText());
            pst.setString(4, txtFornCep.getText());
            pst.setString(5, txtFornEndereco.getText());
            pst.setString(6, txtFornNumero.getText());
            pst.setString(7, txtFornBairro.getText());

            //Validação dos campos obrigatórios
            if ((txtFornNome.getText().isEmpty())
                    || (txtFornContato.getText().isEmpty())) {
                
                JOptionPane.showMessageDialog(null, "Preencha todos o campos obrigatórios");
                
            } else {

                // A linha abaixo atualiza a tabela usuario com os dados do formulário
                //A linha abaixo confirma a inserção de dados na tabela usuario
                int adicionar = pst.executeUpdate();

                // a Linha abaixo serve de apoio ao entendimento da lógica
                //System.out.println(adicionar);
                if (adicionar > 0) {
                    JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso");
                    limpar_campos();
                    pesquisar_fornecedor();
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void editar() {
        String sql = "update fornecedor set Nome=?, Contato =?, Cidade =?, Cep = ?, Endereco = ?, Numero = ?, Bairro = ? where Cod_Fornecedor = ?;";
        
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtFornNome.getText());
            pst.setString(2, txtFornContato.getText());
            pst.setString(3, txtFornCidade.getText());
            pst.setString(4, txtFornCep.getText());
            pst.setString(5, txtFornEndereco.getText());
            pst.setString(6, txtFornNumero.getText());
            pst.setString(7, txtFornBairro.getText());
            pst.setString(8, lblFornId.getText());
            
            if ((txtFornNome.getText().isEmpty())
                    || (txtFornContato.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
                
            } else {
                
                int adicionado = pst.executeUpdate();
                
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Alteração realizada com sucesso");
                    limpar_campos();
                    btnCadastrar.setEnabled(false);
                    pesquisar_fornecedor();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    private void excluir() {
        
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este forenecedor?", "Atenção", JOptionPane.YES_NO_OPTION);
        
        if (confirma == JOptionPane.YES_OPTION) {
            
            String sql = "delete from fornecedor where Cod_Fornecedor = ?";
            
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, lblFornId.getText());
                int apagado = pst.executeUpdate();
                
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Fornecedor removido com cucesso");
                    limpar_campos();
                    pesquisar_fornecedor();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    private void pesquisar_fornecedor() {
        String sql = "select * from Fornecedor where Nome like ?";
        
        try {
            pst = conexao.prepareStatement(sql);

            //Passando o conteúdo da caixa de pesquisa para o ?
            //Atenção ao "%" - continuação da string sql
            pst.setString(1, txtFornPesquisa.getText() + "%");
            rs = pst.executeQuery();
            //A linha abaixo usa a biblioteca es2xml.jar para preenchera tabela
            tblFornecedores.setModel(DbUtils.resultSetToTableModel(rs));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    public void setar_campos() {
        int setar = tblFornecedores.getSelectedRow();
        lblFornId.setText(tblFornecedores.getModel().getValueAt(setar, 0).toString());
        txtFornNome.setText(tblFornecedores.getModel().getValueAt(setar, 1).toString());
        txtFornContato.setText(tblFornecedores.getModel().getValueAt(setar, 2).toString());
        txtFornCidade.setText(tblFornecedores.getModel().getValueAt(setar, 3).toString());
        txtFornCep.setText(tblFornecedores.getModel().getValueAt(setar, 4).toString());
        txtFornEndereco.setText(tblFornecedores.getModel().getValueAt(setar, 5).toString());
        txtFornNumero.setText(tblFornecedores.getModel().getValueAt(setar, 6).toString());
        txtFornBairro.setText(tblFornecedores.getModel().getValueAt(setar, 7).toString());
        btnCadastrar.setEnabled(false);
    }
    
    private void limpar_campos() {
        txtFornNome.setText(null);
        txtFornContato.setText(null);
        txtFornCidade.setText(null);
        txtFornCep.setText(null);
        txtFornEndereco.setText(null);
        txtFornNumero.setText(null);
        txtFornBairro.setText(null);
        btnCadastrar.setEnabled(true);
        lblFornId.setText(null);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFornecedores = new javax.swing.JTable();
        txtFornNome = new javax.swing.JTextField();
        txtFornContato = new javax.swing.JTextField();
        txtFornCidade = new javax.swing.JTextField();
        txtFornCep = new javax.swing.JTextField();
        txtFornEndereco = new javax.swing.JTextField();
        txtFornNumero = new javax.swing.JTextField();
        txtFornBairro = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        btnCadastrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        lblFornId = new javax.swing.JLabel();
        txtFornPesquisa = new javax.swing.JTextField();
        btnFornLimpar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Fornecedores");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Cadastro de Fornecedores");

        tblFornecedores.setModel(new javax.swing.table.DefaultTableModel(
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
        tblFornecedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFornecedoresMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblFornecedores);

        jLabel2.setText("* Nome");

        jLabel3.setText("* Contato");

        jLabel4.setText("Cidade");

        jLabel5.setText("CEP");

        jLabel6.setText("Endereço");

        jLabel7.setText("Número");

        jLabel8.setText("Bairro");

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Pesquisar.png"))); // NOI18N

        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Create.png"))); // NOI18N
        btnCadastrar.setToolTipText("Cadastrar Fornecedor");
        btnCadastrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Update.png"))); // NOI18N
        btnEditar.setToolTipText("Editar Fornecedor");
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Delete.png"))); // NOI18N
        btnExcluir.setToolTipText("Excluir Fornecedor");
        btnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        jLabel10.setText("Código");

        txtFornPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFornPesquisaKeyReleased(evt);
            }
        });

        btnFornLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Clean.png"))); // NOI18N
        btnFornLimpar.setToolTipText("Limpar Campos");
        btnFornLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFornLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFornLimparActionPerformed(evt);
            }
        });

        jLabel11.setText("* Campos obrigatórios");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(txtFornPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(215, 215, 215)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10)
                            .addComponent(jLabel5))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFornCep, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(lblFornId, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel11))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(65, 65, 65)
                                    .addComponent(btnCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(59, 59, 59)
                                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(60, 60, 60)
                                    .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                                    .addComponent(btnFornLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(txtFornNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(91, 91, 91)
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtFornBairro))
                                .addComponent(txtFornNome)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(txtFornContato, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                    .addGap(95, 95, 95)
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtFornCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtFornEndereco))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 642, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(txtFornPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)))
                    .addComponent(lblFornId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFornNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFornContato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(txtFornCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFornCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtFornEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(txtFornBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFornNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEditar)
                            .addComponent(btnCadastrar)
                            .addComponent(btnExcluir)))
                    .addComponent(btnFornLimpar))
                .addContainerGap(67, Short.MAX_VALUE))
        );

        setBounds(0, 0, 738, 602);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        cadastrar();
    }//GEN-LAST:event_btnCadastrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        excluir();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFornPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFornPesquisaKeyReleased
        pesquisar_fornecedor();
    }//GEN-LAST:event_txtFornPesquisaKeyReleased

    private void tblFornecedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFornecedoresMouseClicked
        setar_campos();
    }//GEN-LAST:event_tblFornecedoresMouseClicked

    private void btnFornLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFornLimparActionPerformed
        limpar_campos();
        btnCadastrar.setEnabled(true);
    }//GEN-LAST:event_btnFornLimparActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFornLimpar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFornId;
    private javax.swing.JTable tblFornecedores;
    private javax.swing.JTextField txtFornBairro;
    private javax.swing.JTextField txtFornCep;
    private javax.swing.JTextField txtFornCidade;
    private javax.swing.JTextField txtFornContato;
    private javax.swing.JTextField txtFornEndereco;
    private javax.swing.JTextField txtFornNome;
    private javax.swing.JTextField txtFornNumero;
    private javax.swing.JTextField txtFornPesquisa;
    // End of variables declaration//GEN-END:variables
}
