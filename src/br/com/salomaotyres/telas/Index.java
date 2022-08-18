package br.com.salomaotyres.telas;

import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;

public class Index extends javax.swing.JFrame {

    public Index() {
        initComponents();
    }

    public JDesktopPane getDesktop() {
        return desktop;
    }

    public void setDesktop(JDesktopPane desktop) {
        this.desktop = desktop;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktop = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        lblUsuario = new javax.swing.JLabel();
        lblData = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        Menu = new javax.swing.JMenuBar();
        menCad = new javax.swing.JMenu();
        menCadCli = new javax.swing.JMenuItem();
        MenCadUsu = new javax.swing.JMenuItem();
        MenCadForn = new javax.swing.JMenuItem();
        MenCadProd = new javax.swing.JMenuItem();
        MenCadLoja = new javax.swing.JMenuItem();
        MenCadDespesa = new javax.swing.JMenu();
        MenCadDespViagem = new javax.swing.JMenuItem();
        MenCadDespMaterial = new javax.swing.JMenuItem();
        MenRel = new javax.swing.JMenu();
        MenRelEst = new javax.swing.JMenuItem();
        MenRelVenda = new javax.swing.JMenuItem();
        MenRelProducao = new javax.swing.JMenuItem();
        MenRelDespesa = new javax.swing.JMenu();
        MenRelDespViagem = new javax.swing.JMenuItem();
        MenRelDespMaterial = new javax.swing.JMenuItem();
        MenAjuSob = new javax.swing.JMenu();
        MenAjuSob1 = new javax.swing.JMenuItem();
        MenOpc = new javax.swing.JMenu();
        MenOpcSai = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema para controle de Estoque");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        desktop.setPreferredSize(new java.awt.Dimension(800, 480));

        javax.swing.GroupLayout desktopLayout = new javax.swing.GroupLayout(desktop);
        desktop.setLayout(desktopLayout);
        desktopLayout.setHorizontalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1063, Short.MAX_VALUE)
        );
        desktopLayout.setVerticalGroup(
            desktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        lblUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUsuario.setText("Usuário");

        lblData.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblData.setText("Data");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUsuario)
                    .addComponent(lblData))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblData)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Inventory.png"))); // NOI18N
        jButton1.setToolTipText("Inventário");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setPreferredSize(new java.awt.Dimension(70, 60));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Confimar Entrada.png"))); // NOI18N
        jButton2.setToolTipText("Entrada de Produtos");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setPreferredSize(new java.awt.Dimension(70, 60));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/salomaotyres/icones/Store.png"))); // NOI18N
        jButton3.setToolTipText("Vender Produtos");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setPreferredSize(new java.awt.Dimension(70, 60));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        menCad.setText("Cadastro");

        menCadCli.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        menCadCli.setText("Cliente");
        menCadCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menCadCliActionPerformed(evt);
            }
        });
        menCad.add(menCadCli);

        MenCadUsu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.ALT_MASK));
        MenCadUsu.setText("Usuários");
        MenCadUsu.setEnabled(false);
        MenCadUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenCadUsuActionPerformed(evt);
            }
        });
        menCad.add(MenCadUsu);

        MenCadForn.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.ALT_MASK));
        MenCadForn.setText("Fornecedor");
        MenCadForn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenCadFornActionPerformed(evt);
            }
        });
        menCad.add(MenCadForn);

        MenCadProd.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        MenCadProd.setText("Produto");
        MenCadProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenCadProdActionPerformed(evt);
            }
        });
        menCad.add(MenCadProd);

        MenCadLoja.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.ALT_MASK));
        MenCadLoja.setText("Loja");
        MenCadLoja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenCadLojaActionPerformed(evt);
            }
        });
        menCad.add(MenCadLoja);

        MenCadDespesa.setText("Despesa");

        MenCadDespViagem.setText("Viagem");
        MenCadDespViagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenCadDespViagemActionPerformed(evt);
            }
        });
        MenCadDespesa.add(MenCadDespViagem);

        MenCadDespMaterial.setText("Material");
        MenCadDespMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenCadDespMaterialActionPerformed(evt);
            }
        });
        MenCadDespesa.add(MenCadDespMaterial);

        menCad.add(MenCadDespesa);

        Menu.add(menCad);

        MenRel.setText("Relatório");
        MenRel.setEnabled(false);

        MenRelEst.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK));
        MenRelEst.setText("Estoque");
        MenRelEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenRelEstActionPerformed(evt);
            }
        });
        MenRel.add(MenRelEst);

        MenRelVenda.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK));
        MenRelVenda.setText("Venda");
        MenRelVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenRelVendaActionPerformed(evt);
            }
        });
        MenRel.add(MenRelVenda);

        MenRelProducao.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK));
        MenRelProducao.setText("Produção");
        MenRelProducao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenRelProducaoActionPerformed(evt);
            }
        });
        MenRel.add(MenRelProducao);

        MenRelDespesa.setText("Despesa");

        MenRelDespViagem.setText("Viagem");
        MenRelDespViagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenRelDespViagemActionPerformed(evt);
            }
        });
        MenRelDespesa.add(MenRelDespViagem);

        MenRelDespMaterial.setText("Material");
        MenRelDespMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenRelDespMaterialActionPerformed(evt);
            }
        });
        MenRelDespesa.add(MenRelDespMaterial);

        MenRel.add(MenRelDespesa);

        Menu.add(MenRel);

        MenAjuSob.setText("Ajuda");

        MenAjuSob1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.ALT_MASK));
        MenAjuSob1.setText("Sobre");
        MenAjuSob1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenAjuSob1ActionPerformed(evt);
            }
        });
        MenAjuSob.add(MenAjuSob1);

        Menu.add(MenAjuSob);

        MenOpc.setText("Opções");

        MenOpcSai.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        MenOpcSai.setText("Sair");
        MenOpcSai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenOpcSaiActionPerformed(evt);
            }
        });
        MenOpc.add(MenOpcSai);

        Menu.add(MenOpc);

        setJMenuBar(Menu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)))
                .addComponent(desktop, javax.swing.GroupLayout.PREFERRED_SIZE, 1063, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 261, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1217, 671));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // As linhas abaixo substituem a label lblData pela data Atual do 
        //sistema ao iniciar o form
        Date data = new Date();
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.SHORT);
        lblData.setText(formatador.format(data));

    }//GEN-LAST:event_formWindowActivated

    private void MenOpcSaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenOpcSaiActionPerformed
        // Exibe uma caixa de diálogo
        int sair = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja sair?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (sair == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_MenOpcSaiActionPerformed

    private void MenAjuSob1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenAjuSob1ActionPerformed
        // Chamando a Tela Sobre
        TelaSobre telasobre = new TelaSobre();
        telasobre.setVisible(true);
    }//GEN-LAST:event_MenAjuSob1ActionPerformed

    private void MenCadUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenCadUsuActionPerformed
        // As linhas abaixo vão abrir o form TelaUsuario dentro do desktop pane

        TelaUsuario usuario = new TelaUsuario();
        usuario.setVisible(true);
        desktop.add(usuario);
    }//GEN-LAST:event_MenCadUsuActionPerformed

    private void menCadCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menCadCliActionPerformed
        // Chamando a TelaCliente

        TelaCliente cliente = new TelaCliente();
        cliente.setVisible(true);
        desktop.add(cliente);
    }//GEN-LAST:event_menCadCliActionPerformed

    private void MenCadFornActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenCadFornActionPerformed
        // Chamando a tela Fornecedor

        TelaFornecedor fornecedor = new TelaFornecedor();
        fornecedor.setVisible(true);
        desktop.add(fornecedor);

    }//GEN-LAST:event_MenCadFornActionPerformed

    private void MenCadProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenCadProdActionPerformed
        // Chamando a TelaProduto

        TelaProduto produto = new TelaProduto();
        produto.setVisible(true);
        desktop.add(produto);

    }//GEN-LAST:event_MenCadProdActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Chamando a TelaEstoque
        TelaEstoque estoque = new TelaEstoque();
        desktop.add(estoque);
        estoque.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //Chamando a TelaEntrada
        TelaEntrada entrada = new TelaEntrada();
        desktop.add(entrada);
        entrada.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Chamando a TelaVenda
        TelaVenda venda = new TelaVenda();
        venda.setVisible(true);
        desktop.add(venda);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void MenCadLojaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenCadLojaActionPerformed
        // Chamando a TelaLoja

        TelaLoja loja = new TelaLoja();
        loja.setVisible(true);
        desktop.add(loja);
    }//GEN-LAST:event_MenCadLojaActionPerformed

    private void MenRelEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenRelEstActionPerformed
        TelaEstoque estoque = new TelaEstoque();
        
        estoque.gerar_relatorio_estoque();
    }//GEN-LAST:event_MenRelEstActionPerformed

    private void MenRelVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenRelVendaActionPerformed
        TelaVenda venda = new TelaVenda();

        venda.gerar_relatorio_venda();
    }//GEN-LAST:event_MenRelVendaActionPerformed

    private void MenRelProducaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenRelProducaoActionPerformed
        TelaEntrada producao = new TelaEntrada();

        producao.gerar_relatorio_producao();
    }//GEN-LAST:event_MenRelProducaoActionPerformed

    private void MenRelDespViagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenRelDespViagemActionPerformed
        // Chama o método gerar relatório de despesa de viagem
        TelaDespesaViagem despviagem = new TelaDespesaViagem();
        despviagem.gerar_relatorio_despesa();
    }//GEN-LAST:event_MenRelDespViagemActionPerformed

    private void MenRelDespMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenRelDespMaterialActionPerformed
        // Chama o método gerar relatório de despesa de material
        TelaDespesaMaterial despmaterial = new TelaDespesaMaterial();
        despmaterial.gerar_relatorio_despesa();
    }//GEN-LAST:event_MenRelDespMaterialActionPerformed

    private void MenCadDespViagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenCadDespViagemActionPerformed
        //Chamando a TelaDespesaViagem
        TelaDespesaViagem despviagem = new TelaDespesaViagem();
        despviagem.setVisible(true);
        desktop.add(despviagem);
    }//GEN-LAST:event_MenCadDespViagemActionPerformed

    private void MenCadDespMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenCadDespMaterialActionPerformed
        //Chamando a TelaDespesaMaterial
        TelaDespesaMaterial despmaterial = new TelaDespesaMaterial();
        despmaterial.setVisible(true);
        desktop.add(despmaterial);
    }//GEN-LAST:event_MenCadDespMaterialActionPerformed

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
            java.util.logging.Logger.getLogger(Index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Index().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu MenAjuSob;
    private javax.swing.JMenuItem MenAjuSob1;
    private javax.swing.JMenuItem MenCadDespMaterial;
    private javax.swing.JMenuItem MenCadDespViagem;
    private javax.swing.JMenu MenCadDespesa;
    private javax.swing.JMenuItem MenCadForn;
    private javax.swing.JMenuItem MenCadLoja;
    private javax.swing.JMenuItem MenCadProd;
    public static javax.swing.JMenuItem MenCadUsu;
    private javax.swing.JMenu MenOpc;
    private javax.swing.JMenuItem MenOpcSai;
    public static javax.swing.JMenu MenRel;
    private javax.swing.JMenuItem MenRelDespMaterial;
    private javax.swing.JMenuItem MenRelDespViagem;
    private javax.swing.JMenu MenRelDespesa;
    private javax.swing.JMenuItem MenRelEst;
    private javax.swing.JMenuItem MenRelProducao;
    private javax.swing.JMenuItem MenRelVenda;
    private javax.swing.JMenuBar Menu;
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblData;
    public static javax.swing.JLabel lblUsuario;
    private javax.swing.JMenu menCad;
    private javax.swing.JMenuItem menCadCli;
    // End of variables declaration//GEN-END:variables
}
