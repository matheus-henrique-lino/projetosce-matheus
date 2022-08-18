package br.com.salomaotyres.telas;

import java.sql.*;
import br.com.salomaotyres.dal.ModuloConexao;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import net.proteanit.sql.DbUtils;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

public class TelaEntrada extends javax.swing.JInternalFrame {

    public TelaEntrada() {
        initComponents();
        conexao = ModuloConexao.conector();
        listar();
        Pesquisar_entrada();
        formatar_tabela();
    }

    Connection conexao = null;
    ResultSet rs = null, rs2 = null;
    PreparedStatement pst = null, pst2 = null;

    private void Pesquisar_entrada() {
        try {
            String select = "select Cod_Entrada as 'Ent.', produto.Cod_Produto as Cod, produto.Descricao as Produto, Quantidade as Qtd, Data_Entrada as 'Data' "
                    + "from produto inner join entrada on produto.Cod_Produto = entrada.Cod_Produto;";

            pst = conexao.prepareStatement(select);

            rs = pst.executeQuery();
            
            tblEntradas.setModel(DbUtils.resultSetToTableModel(rs));
            
            formatar_tabela();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void formatar_tabela() {
        tblEntradas.getColumnModel().getColumn(4).setPreferredWidth(200);
        tblEntradas.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblEntradas.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblEntradas.getColumnModel().getColumn(2).setPreferredWidth(150);
        tblEntradas.getColumnModel().getColumn(0).setPreferredWidth(70);
    }

    private void listar() {
        try {
            String select = "select Descricao from produto;";
            String produto;
            pst = conexao.prepareStatement(select);
            rs = pst.executeQuery();

            while (rs.next()) {
                produto = rs.getString("Descricao");
                cboEntProdutos.addItem(produto);
            }
            cboEntProdutos.updateUI();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void consultar_produto() {
        try {
            String selecao = (String) cboEntProdutos.getSelectedItem();
            String select = "select produto.Cod_Produto, produto.Descricao, Valor_Un, categoria.Descricao, Quantidade from produto inner join categoria on produto.Cod_Categoria = categoria.Cod_Categoria inner join estoque on estoque.Cod_Produto = produto.Cod_Produto where produto.Descricao = ?";

            pst2 = conexao.prepareStatement(select);

            pst2.setString(1, selecao);

            rs2 = pst2.executeQuery();

            if (rs2.next()) {
                lblEntProdCod.setText(rs2.getString(1));
                lblEntProdDesc.setText(rs2.getString(2));
                lblEntProdCat.setText(rs2.getString(4));
                lblEntProdVal.setText(rs2.getString(3));
                lblEntProdQuantidade.setText(rs2.getString(5));
            } else {
                JOptionPane.showMessageDialog(null, "Não há produtos na lista");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void limpar_campos() {
        txtEntQuantidade.setText(null);
    }

    private int quantidade_produto() {

        int quantidade = 0;
        String select = "select Quantidade from estoque where Cod_Produto = ?";
        int linha = tblEntradas.getSelectedRow();
        String cod = tblEntradas.getModel().getValueAt(linha, 1).toString();

        try {

            pst = conexao.prepareStatement(select);
            pst.setString(1, cod);

            rs = pst.executeQuery();

            if (rs.next()) {
                quantidade = Integer.parseInt(rs.getString(1));
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }

        return quantidade;
    }

    private void remover_entrada() {
        String delete = "delete from entrada where Cod_Entrada = ?";
        
        int linha = tblEntradas.getSelectedRow();
        String cod = tblEntradas.getModel().getValueAt(linha, 0).toString();
        
        
        try {
            
            pst = conexao.prepareStatement(delete);
            
            pst.setString(1, cod);
            
            int alterado = pst.executeUpdate();
            
            if (alterado > 0) JOptionPane.showMessageDialog(null, "Entrada removida com sucesso!");
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(null, e);
            
        }
        
    }

    private void desfazer_entrada() {

        String update = "update estoque set Quantidade = ? where Cod_Produto = ?";

        int selecao = tblEntradas.getSelectedRow();

        int entrada = Integer.parseInt(tblEntradas.getModel().getValueAt(selecao, 3).toString());

        int cod = Integer.parseInt(tblEntradas.getModel().getValueAt(selecao, 1).toString());

        int quantidade_atualizada = quantidade_produto() - entrada;

        try {
            pst = conexao.prepareStatement(update);

            pst.setString(1, String.valueOf(quantidade_atualizada));
            pst.setString(2, String.valueOf(cod));

            int adicionado = pst.executeUpdate();

            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "O estoque foi atualizado!");
                pst.close();
                remover_entrada();
                Pesquisar_entrada();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void entrar_produto() {
        try {
            if (txtEntQuantidade.getText().isEmpty()) {

                JOptionPane.showMessageDialog(null, "Informe a Quantidade!");

            } else {

                int quantidade = Integer.parseInt(txtEntQuantidade.getText());
                int quantidade_produto = Integer.parseInt(lblEntProdQuantidade.getText());
                int totalqtd = quantidade + quantidade_produto;

                String select = "update estoque set Quantidade = ? where estoque.Cod_Produto = ?";
                pst = conexao.prepareStatement(select);
                pst.setString(1, String.valueOf(totalqtd));
                pst.setString(2, lblEntProdCod.getText());

                int adicionado = pst.executeUpdate();

                String select2 = "insert into entrada (Cod_Produto, Quantidade) values (?, ?);";
                pst2 = conexao.prepareStatement(select2);
                pst2.setString(1, lblEntProdCod.getText());
                pst2.setString(2, txtEntQuantidade.getText());

                int adicionado2 = pst2.executeUpdate();

                if (adicionado > 0 && adicionado2 > 0) {
                    JOptionPane.showMessageDialog(null, "Entrada realizada com sucesso");
                    limpar_campos();
                    Pesquisar_entrada();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    
    public PdfPTable cabecalho_producao() {
        PdfPTable tabela = new PdfPTable(new float[]{3f, 4f, 4f, 4f, 4f, 4f});

        PdfPCell cel1 = new PdfPCell(new Phrase("Cód.", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel2 = new PdfPCell(new Phrase("Entrada", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel3 = new PdfPCell(new Phrase("Descricão", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel4 = new PdfPCell(new Phrase("Valor Un.", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel5 = new PdfPCell(new Phrase("Quantidade", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel6 = new PdfPCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA, 8F)));

        cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cel3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cel4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cel5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cel6.setHorizontalAlignment(Element.ALIGN_CENTER);

        tabela.addCell(cel1);
        tabela.addCell(cel2);
        tabela.addCell(cel3);
        tabela.addCell(cel4);
        tabela.addCell(cel5);
        tabela.addCell(cel6);

        return tabela;
    }

    public PdfPTable dados_producao() {

        PdfPTable tabela = new PdfPTable(new float[]{3f, 4f, 4f, 4f, 4f, 4f});

        String select = "select produto.Cod_Produto, Data_Entrada, Descricao, Valor_Un, Quantidade, (Valor_Un*Quantidade) as Total from entrada inner join produto on entrada.Cod_Produto = produto.Cod_Produto order by Data_Entrada;";

        try {

            pst = conexao.prepareStatement(select);
            rs = pst.executeQuery();

            while (rs.next()) {
                PdfPCell cel1 = new PdfPCell(new Phrase(rs.getString(1), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel2 = new PdfPCell(new Phrase(rs.getString(2), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel3 = new PdfPCell(new Phrase(rs.getString(3), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel4 = new PdfPCell(new Phrase(rs.getString(4) + " R$", FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel5 = new PdfPCell(new Phrase(rs.getString(5), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel6 = new PdfPCell(new Phrase(rs.getString(6) + " R$", FontFactory.getFont(FontFactory.HELVETICA, 5F)));

                cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel6.setHorizontalAlignment(Element.ALIGN_CENTER);

                tabela.addCell(cel1);
                tabela.addCell(cel2);
                tabela.addCell(cel3);
                tabela.addCell(cel4);
                tabela.addCell(cel5);
                tabela.addCell(cel6);

            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }

        return tabela;
    }

    public void gerar_relatorio_producao() {

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("Produção.pdf"));

            Paragraph titulo = new Paragraph(new Phrase(10F, "Relatório de Produção", FontFactory.getFont(FontFactory.HELVETICA, 18F)));
            titulo.setAlignment(Element.ALIGN_CENTER);

            document.open();
            document.add(titulo);
            document.add(new Paragraph(new Phrase(" ")));

            document.add(cabecalho_producao());
            document.add(dados_producao());

        } catch (FileNotFoundException | DocumentException ex) {

            System.out.println("Error:" + ex);

        } finally {
            document.close();
        }

        try {

            Desktop.getDesktop().open(new File("Produção.pdf"));

        } catch (IOException ex) {

            System.out.println("Error:" + ex);

        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cboEntProdutos = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblEntProdCod = new javax.swing.JLabel();
        lblEntProdDesc = new javax.swing.JLabel();
        lblEntProdCat = new javax.swing.JLabel();
        lblEntProdVal = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblEntProdQuantidade = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEntradas = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtEntQuantidade = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnEntConfirmar = new javax.swing.JButton();
        btnEntRemover_entrada = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Entrada");
        setPreferredSize(new java.awt.Dimension(800, 509));

        cboEntProdutos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEntProdutosActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Descrição: ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Categoria: ");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Valor: ");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Código: ");

        lblEntProdCod.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblEntProdDesc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblEntProdCat.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblEntProdVal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("R$");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Estoque Atual: ");

        lblEntProdQuantidade.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEntProdCod, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEntProdQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEntProdDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEntProdCat, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEntProdVal, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(lblEntProdCod, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel1))
                            .addComponent(lblEntProdDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2))
                    .addComponent(lblEntProdCat, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(lblEntProdQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel5))
                    .addComponent(lblEntProdVal, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        tblEntradas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblEntradas);

        jLabel6.setText("Quantidade:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Entradas");

        btnEntConfirmar.setBackground(new java.awt.Color(0, 153, 51));
        btnEntConfirmar.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        btnEntConfirmar.setForeground(new java.awt.Color(255, 255, 255));
        btnEntConfirmar.setText("Enviar");
        btnEntConfirmar.setToolTipText("Confirmar");
        btnEntConfirmar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEntConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntConfirmarActionPerformed(evt);
            }
        });

        btnEntRemover_entrada.setBackground(new java.awt.Color(204, 0, 0));
        btnEntRemover_entrada.setFont(new java.awt.Font("Arial Black", 1, 11)); // NOI18N
        btnEntRemover_entrada.setForeground(new java.awt.Color(255, 255, 255));
        btnEntRemover_entrada.setText("Remover Entrada");
        btnEntRemover_entrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntRemover_entradaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtEntQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(btnEntConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cboEntProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnEntRemover_entrada)
                        .addGap(85, 363, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cboEntProdutos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtEntQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEntConfirmar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEntRemover_entrada))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        setBounds(0, 0, 825, 307);
    }// </editor-fold>//GEN-END:initComponents

    private void cboEntProdutosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEntProdutosActionPerformed
        // TODO add your handling code here:
        consultar_produto();
    }//GEN-LAST:event_cboEntProdutosActionPerformed

    private void btnEntConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntConfirmarActionPerformed
        // Chamando o método entrar_produto
        entrar_produto();
        consultar_produto();
    }//GEN-LAST:event_btnEntConfirmarActionPerformed

    private void btnEntRemover_entradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntRemover_entradaActionPerformed
        // chama o método remover_entrada
        desfazer_entrada();
    }//GEN-LAST:event_btnEntRemover_entradaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntConfirmar;
    private javax.swing.JButton btnEntRemover_entrada;
    private javax.swing.JComboBox<String> cboEntProdutos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblEntProdCat;
    private javax.swing.JLabel lblEntProdCod;
    private javax.swing.JLabel lblEntProdDesc;
    private javax.swing.JLabel lblEntProdQuantidade;
    private javax.swing.JLabel lblEntProdVal;
    private javax.swing.JTable tblEntradas;
    private javax.swing.JTextField txtEntQuantidade;
    // End of variables declaration//GEN-END:variables
}
