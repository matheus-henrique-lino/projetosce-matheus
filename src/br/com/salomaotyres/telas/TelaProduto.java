package br.com.salomaotyres.telas;

import javax.swing.JOptionPane;
import java.sql.*;
import br.com.salomaotyres.dal.ModuloConexao;
import net.proteanit.sql.DbUtils;

public class TelaProduto extends javax.swing.JInternalFrame {

    public TelaProduto() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisar_produto();
        btrProdCategoria.setSelected(true);
        btrProdCategoria2.setEnabled(false);
        btrProdCategoria3.setEnabled(false);
    }

    Connection conexao = null;
    PreparedStatement pst = null, pst2 = null;
    ResultSet rs = null, rs2 = null;

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        txtProdPesquisa = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduto = new javax.swing.JTable();
        txtProdDescricao = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btrProdCategoria = new javax.swing.JRadioButton();
        btrProdCategoria2 = new javax.swing.JRadioButton();
        btrProdCategoria3 = new javax.swing.JRadioButton();
        txtProdValor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblProdCódigo = new javax.swing.JLabel();
        btnCadastrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnProdLimpar = new javax.swing.JButton();
        btnProdEnviar_Estoque = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Produtos");

        txtProdPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProdPesquisaKeyReleased(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Pesquisar.png"))); // NOI18N

        tblProduto.setModel(new javax.swing.table.DefaultTableModel(
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
        tblProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProdutoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProduto);

        jLabel2.setText("* Descrição");

        jLabel3.setText("* Categoria");

        buttonGroup1.add(btrProdCategoria);
        btrProdCategoria.setText("Produção");

        buttonGroup1.add(btrProdCategoria2);
        btrProdCategoria2.setText("Revenda");

        btrProdCategoria3.setText("Material");

        jLabel5.setText("* Valor");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Cadastro de Produtos");

        jLabel8.setText("*Campos Obrigatórios");

        jLabel4.setText("Código");

        btnCadastrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Create.png"))); // NOI18N
        btnCadastrar.setToolTipText("Cadastrar Produto");
        btnCadastrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarActionPerformed(evt);
            }
        });

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Update.png"))); // NOI18N
        btnEditar.setToolTipText("Editar Produto");
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Delete.png"))); // NOI18N
        btnExcluir.setToolTipText("Excluir Produto");
        btnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnProdLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Clean.png"))); // NOI18N
        btnProdLimpar.setToolTipText("Limpar Campos");
        btnProdLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProdLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdLimparActionPerformed(evt);
            }
        });

        btnProdEnviar_Estoque.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/enviar_para_estoque.png"))); // NOI18N
        btnProdEnviar_Estoque.setToolTipText("Enviar ao Inventário");
        btnProdEnviar_Estoque.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProdEnviar_Estoque.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdEnviar_EstoqueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(271, 271, 271)
                .addComponent(jLabel7)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btrProdCategoria)
                            .addComponent(btrProdCategoria2)
                            .addComponent(txtProdValor, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblProdCódigo, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btrProdCategoria3)
                            .addComponent(txtProdDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(txtProdPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)))
                .addContainerGap(206, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(22, 22, 22))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(btnCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(btnProdLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnProdEnviar_Estoque, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtProdPesquisa))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtProdDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(btrProdCategoria))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btrProdCategoria2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btrProdCategoria3)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtProdValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(lblProdCódigo, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnExcluir, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCadastrar)
                                        .addComponent(btnEditar))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnProdLimpar))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnProdEnviar_Estoque, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(52, 52, 52))
        );

        setBounds(0, 0, 760, 602);
    }// </editor-fold>//GEN-END:initComponents

    private void txtProdPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProdPesquisaKeyReleased
        // Chamando o método pesquisar_produto
        pesquisar_produto();
    }//GEN-LAST:event_txtProdPesquisaKeyReleased

    private void btnCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarActionPerformed
        // Chamando o método Cadastrar
        cadastrar();
    }//GEN-LAST:event_btnCadastrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // Chamando o método Editar
        editar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // Chamando o método excluir
        excluir();
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnProdLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdLimparActionPerformed
        // Chamando o método Limpar
        limpar_campos();
    }//GEN-LAST:event_btnProdLimparActionPerformed

    private void tblProdutoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProdutoMouseClicked
        // Chamando o método setar_camos
        setar_campos();
    }//GEN-LAST:event_tblProdutoMouseClicked

    private void btnProdEnviar_EstoqueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdEnviar_EstoqueActionPerformed
        // Chamando o método enviar_ao_invetario
        enviar_para_invetario();
    }//GEN-LAST:event_btnProdEnviar_EstoqueActionPerformed

    private void cadastrar() {

        String sql = "INSERT INTO produto (Descricao, Cod_Categoria, Valor_Un) Values (?, ?, ?)";
        String categoria = null;

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdDescricao.getText());
            if (btrProdCategoria.isSelected()) {
                categoria = "1";
                pst.setString(2, categoria);
            }
            pst.setString(3, txtProdValor.getText());

            //Validação dos campos obrigatórios
            if ((txtProdDescricao.getText().isEmpty())
                    || txtProdValor.getText().isEmpty()) {

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
                    pesquisar_produto();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void enviar_para_invetario() {
        try {
            String insert = "insert into estoque (Cod_Produto, Quantidade) values(?, 0);";
            
            pst = conexao.prepareStatement(insert);
            
            pst.setString(1, lblProdCódigo.getText());
            
            int adicionado = pst.executeUpdate();
            
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Produto adicionado ao inventário");
            }
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void editar() {

        String sql = "update produto set Descricao = ?, Cod_Categoria = ?, Valor_Un = ? where Cod_Produto = ?;";
        String categoria = null;

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtProdDescricao.getText());
            if (btrProdCategoria.isSelected()) {
                categoria = "1";
                pst.setString(2, categoria);
            }
            pst.setString(3, txtProdValor.getText());
            pst.setString(4, lblProdCódigo.getText());

            if ((txtProdDescricao.getText().isEmpty())
                    || txtProdValor.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");

            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Alteração realizada com sucesso");
                    limpar_campos();
                    pesquisar_produto();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void excluir() {

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este produto?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {

            String sql = "delete from produto where Cod_Produto = ?";

            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, lblProdCódigo.getText());
                int apagado = pst.executeUpdate();

                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Produto removido com cucesso");
                    limpar_campos();
                    pesquisar_produto();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void pesquisar_produto() {
        String sql = "select Cod_Produto, produto.Descricao as Produto, categoria.Descricao as Categoria, Valor_Un from produto inner join categoria on produto.Cod_Categoria = categoria.Cod_Categoria where produto.Descricao like ?";

        try {
            pst = conexao.prepareStatement(sql);

            //Passando o conteúdo da caixa de pesquisa para o ?
            //Atenção ao "%" - continuação da string sql
            pst.setString(1, txtProdPesquisa.getText() + "%");
            rs = pst.executeQuery();
            //A linha abaixo usa a biblioteca es2xml.jar para preenchera tabela
            tblProduto.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void setar_campos() {
        String categoria = null;
        int setar = tblProduto.getSelectedRow();
        lblProdCódigo.setText(tblProduto.getModel().getValueAt(setar, 0).toString());
        txtProdDescricao.setText(tblProduto.getModel().getValueAt(setar, 1).toString());
        btrProdCategoria.setSelected(true);
        txtProdValor.setText(tblProduto.getModel().getValueAt(setar, 3).toString());
        btnCadastrar.setEnabled(false);
        btnEditar.setEnabled(true);
    }

    private void limpar_campos() {
        lblProdCódigo.setText(null);
        txtProdDescricao.setText(null);
        txtProdValor.setText(null);
        btnCadastrar.setEnabled(true);
        btnEditar.setEnabled(false);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnProdEnviar_Estoque;
    private javax.swing.JButton btnProdLimpar;
    private javax.swing.JRadioButton btrProdCategoria;
    private javax.swing.JRadioButton btrProdCategoria2;
    private javax.swing.JRadioButton btrProdCategoria3;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblProdCódigo;
    private javax.swing.JTable tblProduto;
    private javax.swing.JTextField txtProdDescricao;
    private javax.swing.JTextField txtProdPesquisa;
    private javax.swing.JTextField txtProdValor;
    // End of variables declaration//GEN-END:variables
}
