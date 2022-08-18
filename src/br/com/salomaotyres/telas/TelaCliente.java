package br.com.salomaotyres.telas;

import java.sql.*;
import br.com.salomaotyres.dal.ModuloConexao;
import javax.swing.JOptionPane;
// A linha abaixo  importa recursos da biblioteca rs2xml.jar
import net.proteanit.sql.DbUtils;

public class TelaCliente extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaCliente() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisar_cliente();
    }

    //Criando método cadastrar
    private void cadastrar() {
        String sql = "INSERT INTO Cliente (Nome, Cpf, Cnpj, Cidade, Estado, Endereco, Numero, Bairro, Contato, Cep) Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliNome.getText());
            pst.setString(2, txtCliCpf.getText());
            pst.setString(3, txtCliCnpj.getText());
            pst.setString(4, txtCliCidade.getText());
            pst.setString(5, txtCliEstado.getText());
            pst.setString(6, txtCliEndereco.getText());
            pst.setString(7, txtCliNumero.getText());
            pst.setString(8, txtCliBairro.getText());
            pst.setString(9, txtCliContato.getText());
            pst.setString(10, txtCliCep.getText());

            //Validação dos campos obrigatórios
            if ((txtCliNome.getText().isEmpty())
                    || (txtCliContato.getText().isEmpty())) {

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
                    pesquisar_cliente();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    // Método para pesquisar clientes pelo nome com filtro
    private void pesquisar_cliente() {
        String sql = "select * from Cliente where Nome like ?";

        try {
            pst = conexao.prepareStatement(sql);

            //Passando o conteúdo da caixa de pesquisa para o ?
            //Atenção ao "%" - continuação da string sql
            pst.setString(1, txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            //A linha abaixo usa a biblioteca es2xml.jar para preenchera tabela
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void editar() {
        String sql = "update cliente set Nome=?, Cpf =?, Cnpj =?, Cidade =?, Estado =?, Endereco = ?, Numero = ?, Bairro = ?, Contato = ?, Cep = ? where Cod_Cliente = ?;";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliNome.getText());
            pst.setString(2, txtCliCpf.getText());
            pst.setString(3, txtCliCnpj.getText());
            pst.setString(4, txtCliCidade.getText());
            pst.setString(5, txtCliEstado.getText());
            pst.setString(6, txtCliEndereco.getText());
            pst.setString(7, txtCliNumero.getText());
            pst.setString(8, txtCliBairro.getText());
            pst.setString(9, txtCliContato.getText());
            pst.setString(10, txtCliCep.getText());
            pst.setString(11, lblCliId.getText());

            if ((txtCliNome.getText().isEmpty())
                    || (txtCliContato.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");

            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Alteração realizada com sucesso");
                    limpar_campos();
                    pesquisar_cliente();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    
    private void excluir() {

        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este cliente?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {
            
            String sql = "delete from cliente where Cod_Cliente = ?";
            
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, lblCliId.getText());
                int apagado = pst.executeUpdate();

                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário removido com cucesso");
                    pesquisar_cliente();
                    limpar_campos();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    private void limpar_campos() {
        lblCliId.setText(null);
        txtCliNome.setText(null);
        txtCliBairro.setText(null);
        txtCliCep.setText(null);
        txtCliCidade.setText(null);
        txtCliCnpj.setText(null);
        txtCliContato.setText(null);
        txtCliCpf.setText(null);
        txtCliEndereco.setText(null);
        txtCliEstado.setText(null);
        txtCliNumero.setText(null);
        BtnAdicionar.setEnabled(true);
        BtnEditar.setEnabled(false);
    }


    // Método para setar os campos do formulário com o conteúdo da tabela
    public void setar_campos() {
        int setar = tblClientes.getSelectedRow();
        lblCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
        txtCliNome.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
        txtCliCpf.setText(tblClientes.getModel().getValueAt(setar, 2).toString());
        txtCliCnpj.setText(tblClientes.getModel().getValueAt(setar, 3).toString());
        txtCliCidade.setText(tblClientes.getModel().getValueAt(setar, 4).toString());
        txtCliEstado.setText(tblClientes.getModel().getValueAt(setar, 5).toString());
        txtCliContato.setText(tblClientes.getModel().getValueAt(setar, 9).toString());
        txtCliEndereco.setText(tblClientes.getModel().getValueAt(setar, 6).toString());
        txtCliNumero.setText(tblClientes.getModel().getValueAt(setar, 7).toString());
        txtCliBairro.setText(tblClientes.getModel().getValueAt(setar, 8).toString());
        txtCliCep.setText(tblClientes.getModel().getValueAt(setar, 10).toString());
        BtnAdicionar.setEnabled(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCliNome = new javax.swing.JTextField();
        txtCliCpf = new javax.swing.JTextField();
        txtCliCnpj = new javax.swing.JTextField();
        txtCliCidade = new javax.swing.JTextField();
        txtCliEstado = new javax.swing.JTextField();
        txtCliEndereco = new javax.swing.JTextField();
        txtCliNumero = new javax.swing.JTextField();
        txtCliBairro = new javax.swing.JTextField();
        txtCliContato = new javax.swing.JTextField();
        txtCliCep = new javax.swing.JTextField();
        BtnAdicionar = new javax.swing.JButton();
        BtnEditar = new javax.swing.JButton();
        BtnExcluir = new javax.swing.JButton();
        txtCliPesquisar = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        lblCliId = new javax.swing.JLabel();
        btnCliLimpar = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setTitle("Clientes");
        setPreferredSize(new java.awt.Dimension(750, 525));

        jLabel1.setText("* Campos obrigatórios");

        jLabel2.setText("* Nome");

        jLabel3.setText("CPF");

        jLabel4.setText("CNPJ");

        jLabel5.setText("Cidade");

        jLabel6.setText("Estado");

        jLabel7.setText("Endereço");

        jLabel8.setText("Número");

        jLabel9.setText("Bairro");

        jLabel10.setText("* Contato");

        jLabel11.setText("CEP");

        BtnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Create.png"))); // NOI18N
        BtnAdicionar.setToolTipText("Cadastrar Cliente");
        BtnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnAdicionar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BtnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAdicionarActionPerformed(evt);
            }
        });

        BtnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Update.png"))); // NOI18N
        BtnEditar.setToolTipText("Editar Cliente");
        BtnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditarActionPerformed(evt);
            }
        });

        BtnExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Delete.png"))); // NOI18N
        BtnExcluir.setToolTipText("Excluir Cliente");
        BtnExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BtnExcluir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        BtnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnExcluirActionPerformed(evt);
            }
        });

        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Pesquisar.png"))); // NOI18N

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
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
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        jLabel13.setText("Código");

        lblCliId.setEnabled(false);

        btnCliLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Clean.png"))); // NOI18N
        btnCliLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCliLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCliLimparActionPerformed(evt);
            }
        });

        jLabel14.setText("* Campos Obrigatórios");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(5, 5, 5))
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtCliEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel8))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtCliCnpj, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtCliNome, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtCliEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(txtCliBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel11)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCliCidade, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCliContato, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCliCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCliCep, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCliNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(683, 683, 683))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(BtnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60)
                                .addComponent(BtnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60)
                                .addComponent(BtnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(60, 60, 60)
                                .addComponent(btnCliLimpar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(659, 659, 659))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(lblCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(495, 495, 495)
                                .addComponent(jLabel14)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(19, 19, 19))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCliId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel14)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtCliCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txtCliCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(txtCliCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCliEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(txtCliContato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtCliEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(txtCliNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtCliBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(txtCliCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BtnExcluir)
                            .addComponent(BtnEditar)
                            .addComponent(BtnAdicionar)))
                    .addComponent(btnCliLimpar))
                .addGap(22, 22, 22))
        );

        setBounds(0, 0, 776, 563);
    }// </editor-fold>//GEN-END:initComponents

    private void BtnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAdicionarActionPerformed
        // Chamando o método cadastrar
        cadastrar();
    }//GEN-LAST:event_BtnAdicionarActionPerformed
// O evento abaixo é do tipo "enquanto for digitando 
    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        // Chama o método pesquisar clientes
        pesquisar_cliente();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased
//Evento que será usado para setar os campos da tabela (clicando com o mouse)
    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // Chamando o método setar os campos
        setar_campos();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void BtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditarActionPerformed
        // Chamando o método alterar clientes
        editar();
    }//GEN-LAST:event_BtnEditarActionPerformed

    private void BtnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnExcluirActionPerformed
        // Chama o método excluir
        excluir();
    }//GEN-LAST:event_BtnExcluirActionPerformed

    private void btnCliLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCliLimparActionPerformed
        // Chama o método limpar_campos
        limpar_campos();
    }//GEN-LAST:event_btnCliLimparActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnAdicionar;
    private javax.swing.JButton BtnEditar;
    private javax.swing.JButton BtnExcluir;
    private javax.swing.JButton btnCliLimpar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCliId;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtCliBairro;
    private javax.swing.JTextField txtCliCep;
    private javax.swing.JTextField txtCliCidade;
    private javax.swing.JTextField txtCliCnpj;
    private javax.swing.JTextField txtCliContato;
    private javax.swing.JTextField txtCliCpf;
    private javax.swing.JTextField txtCliEndereco;
    private javax.swing.JTextField txtCliEstado;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtCliNumero;
    private javax.swing.JTextField txtCliPesquisar;
    // End of variables declaration//GEN-END:variables
}
