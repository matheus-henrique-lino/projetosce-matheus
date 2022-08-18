package br.com.salomaotyres.telas;

import br.com.salomaotyres.dal.ModuloConexao;
import com.itextpdf.text.Chunk;
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
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.proteanit.sql.DbUtils;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class TelaEstoque extends javax.swing.JInternalFrame {

    public TelaEstoque() {
        initComponents();
        conexao = ModuloConexao.conector();
        pesquisar_por_produtos();
        mostrar_quantidade_total();
    }

    PreparedStatement pst = null;
    ResultSet rs = null;
    Connection conexao = null;

    private void pesquisar_por_produtos() {
        try {
            String select = "select produto.Cod_Produto, produto.Descricao as Produto, categoria.Descricao as Categoria, Valor_Un as 'Valor Unitário', Quantidade, (Valor_Un*Quantidade) as 'Valor_Total' from produto "
                    + "inner join estoque on produto.Cod_Produto = estoque.Cod_Produto "
                    + "inner join categoria on categoria.Cod_Categoria = produto.Cod_Categoria order by produto.Descricao;";
            pst = conexao.prepareStatement(select);
            rs = pst.executeQuery();
            tblEstoque.setModel(DbUtils.resultSetToTableModel(rs));
            mostrar_quantidade_total();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void pesquisar_por_valor_un() {
        try {
            String ordem = null;
            String select = "select produto.Cod_Produto, produto.Descricao as Produto, categoria.Descricao as Categoria, Valor_Un as 'Valor Unitário', Quantidade, (Valor_Un*Quantidade) as 'Valor_Total' from produto "
                    + "inner join estoque on produto.Cod_Produto = estoque.Cod_Produto "
                    + "inner join categoria on categoria.Cod_Categoria = produto.Cod_Categoria order by Valor_Un;";
            pst = conexao.prepareStatement(select);
            rs = pst.executeQuery();
            tblEstoque.setModel(DbUtils.resultSetToTableModel(rs));
            mostrar_quantidade_total();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void pesquisar_por_categoria() {
        try {
            String ordem = null;
            String select = "select produto.Cod_Produto, produto.Descricao as Produto, categoria.Descricao as Categoria, Valor_Un as 'Valor Unitário', Quantidade, (Valor_Un*Quantidade) as 'Valor_Total' from produto "
                    + "inner join estoque on produto.Cod_Produto = estoque.Cod_Produto "
                    + "inner join categoria on categoria.Cod_Categoria = produto.Cod_Categoria order by categoria.Descricao;";
            pst = conexao.prepareStatement(select);
            rs = pst.executeQuery();
            tblEstoque.setModel(DbUtils.resultSetToTableModel(rs));
            mostrar_quantidade_total();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void pesquisar_por_valor_total() {
        try {
            String ordem = null;
            String select = "select produto.Cod_Produto, produto.Descricao as Produto, categoria.Descricao as Categoria, Valor_Un as 'Valor Unitário', Quantidade, (Valor_Un*Quantidade) as 'Valor_Total' from produto "
                    + "inner join estoque on produto.Cod_Produto = estoque.Cod_Produto "
                    + "inner join categoria on categoria.Cod_Categoria = produto.Cod_Categoria order by Valor_Total;";
            pst = conexao.prepareStatement(select);
            rs = pst.executeQuery();
            tblEstoque.setModel(DbUtils.resultSetToTableModel(rs));
            mostrar_quantidade_total();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void pesquisar_por_quantidade() {
        try {
            String ordem = null;
            String select = "select produto.Cod_Produto, produto.Descricao as Produto, categoria.Descricao as Categoria, Valor_Un as 'Valor Unitário', Quantidade, (Valor_Un*Quantidade) as 'Valor_Total' from produto "
                    + "inner join estoque on produto.Cod_Produto = estoque.Cod_Produto "
                    + "inner join categoria on categoria.Cod_Categoria = produto.Cod_Categoria order by Quantidade;";
            pst = conexao.prepareStatement(select);
            rs = pst.executeQuery();
            tblEstoque.setModel(DbUtils.resultSetToTableModel(rs));
            mostrar_quantidade_total();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void mostrar_codigo() {
        int setar = tblEstoque.getSelectedRow();
        lblEstCódigoProduto.setText(tblEstoque.getModel().getValueAt(setar, 0).toString());
    }

    private void retirar_do_estoque() {
        try {
            int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja retirar este produto do estoque?", "Atenção", JOptionPane.YES_NO_OPTION);

            if (confirma == JOptionPane.YES_OPTION) {
                String delete = "delete from estoque where Cod_Produto = ?";

                pst = conexao.prepareStatement(delete);

                pst.setString(1, lblEstCódigoProduto.getText());

                int apagado = pst.executeUpdate();

                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Produto retirado");
                    lblEstCódigoProduto.setText(null);
                    mostrar_quantidade_total();
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void mostrar_quantidade_total() {
        try {
            String select = "select sum(Quantidade), sum(Valor_Un * Quantidade) from estoque inner join produto on produto.Cod_Produto = estoque.Cod_Produto;";

            pst = conexao.prepareStatement(select);

            rs = pst.executeQuery();

            if (rs.next()) {
                lblEstQuantidade.setText(rs.getString(1));
                lblEstValorGeral.setText(rs.getString(2));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public PdfPTable cabecalho_estoque() {
        PdfPTable tabela = new PdfPTable(new float[]{3f, 4f, 4f, 4f, 4f, 4f});

        PdfPCell cel1 = new PdfPCell(new Phrase("Cod.", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel2 = new PdfPCell(new Phrase("Descricão", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel3 = new PdfPCell(new Phrase("Categoria", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
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

    public PdfPTable dados_estoque() {

        PdfPTable tabela = new PdfPTable(new float[]{3f, 4f, 4f, 4f, 4f, 4f});

        String select = "select produto.Cod_Produto, produto.Descricao as Produto, categoria.Descricao as Categoria, Valor_Un as 'Valor Unitário', Quantidade, (Valor_Un*Quantidade) as 'Valor_Total' from produto "
                    + "inner join estoque on produto.Cod_Produto = estoque.Cod_Produto "
                    + "inner join categoria on categoria.Cod_Categoria = produto.Cod_Categoria order by produto.Descricao;";

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

    public void gerar_relatorio_estoque() {

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("Estoque.pdf"));

            Paragraph titulo = new Paragraph(new Phrase(10F, "Relatório de Estoque", FontFactory.getFont(FontFactory.HELVETICA, 18F)));
            titulo.setAlignment(Element.ALIGN_CENTER);

            document.open();
            document.add(titulo);
            document.add(new Paragraph(new Phrase(" ")));

            document.add(cabecalho_estoque());
            document.add(dados_estoque());

        } catch (FileNotFoundException | DocumentException ex) {

            System.out.println("Error:" + ex);

        } finally {
            document.close();
        }

        try {

            Desktop.getDesktop().open(new File("Estoque.pdf"));

        } catch (IOException ex) {

            System.out.println("Error:" + ex);

        }

    }

        

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        GrupoOrdenação = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEstoque = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cbnIvenProduto = new javax.swing.JRadioButton();
        cbnIvenCategoria = new javax.swing.JRadioButton();
        cbnIvenValorUnitario = new javax.swing.JRadioButton();
        cbnIvenQuantidade = new javax.swing.JRadioButton();
        cbnIvenValorTotal = new javax.swing.JRadioButton();
        btnEstRetirar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lblEstCódigoProduto = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblEstQuantidade = new javax.swing.JLabel();
        lblEstValorGeral = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnPdf = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Tela de Estoque");

        tblEstoque.setModel(new javax.swing.table.DefaultTableModel(
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
        tblEstoque.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEstoqueMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblEstoque);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Iventário");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ordenar:"));

        GrupoOrdenação.add(cbnIvenProduto);
        cbnIvenProduto.setText("Produto");
        cbnIvenProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbnIvenProdutoMouseClicked(evt);
            }
        });

        GrupoOrdenação.add(cbnIvenCategoria);
        cbnIvenCategoria.setText("Categoria");
        cbnIvenCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbnIvenCategoriaMouseClicked(evt);
            }
        });

        GrupoOrdenação.add(cbnIvenValorUnitario);
        cbnIvenValorUnitario.setText("Valor Unitário");
        cbnIvenValorUnitario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbnIvenValorUnitarioMouseClicked(evt);
            }
        });

        GrupoOrdenação.add(cbnIvenQuantidade);
        cbnIvenQuantidade.setText("Quantidade");
        cbnIvenQuantidade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbnIvenQuantidadeMouseClicked(evt);
            }
        });

        GrupoOrdenação.add(cbnIvenValorTotal);
        cbnIvenValorTotal.setText("Valor Total");
        cbnIvenValorTotal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbnIvenValorTotalMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbnIvenCategoria)
                    .addComponent(cbnIvenProduto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbnIvenValorUnitario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbnIvenValorTotal)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cbnIvenQuantidade)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbnIvenProduto)
                    .addComponent(cbnIvenValorUnitario)
                    .addComponent(cbnIvenValorTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbnIvenCategoria)
                    .addComponent(cbnIvenQuantidade)))
        );

        btnEstRetirar.setBackground(new java.awt.Color(255, 204, 0));
        btnEstRetirar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnEstRetirar.setForeground(new java.awt.Color(255, 255, 255));
        btnEstRetirar.setText("Retirar");
        btnEstRetirar.setToolTipText("Retirar produtos da lista de estoque");
        btnEstRetirar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEstRetirar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEstRetirarActionPerformed(evt);
            }
        });

        jLabel2.setText("Código do Produto");

        lblEstCódigoProduto.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(153, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setText("Quantidade Total");

        jLabel5.setText("Geral");

        lblEstQuantidade.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        lblEstValorGeral.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("R$");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(lblEstQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEstValorGeral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addGap(47, 47, 47))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEstQuantidade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEstValorGeral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        btnPdf.setBackground(new java.awt.Color(153, 204, 255));
        btnPdf.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPdf.setForeground(new java.awt.Color(255, 255, 255));
        btnPdf.setText("PDF");
        btnPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(324, 324, 324)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(btnEstRetirar))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(lblEstCódigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(5, 5, 5)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblEstCódigoProduto, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEstRetirar)
                        .addGap(16, 16, 16))))
        );

        setBounds(0, 0, 800, 600);
    }// </editor-fold>//GEN-END:initComponents

    private void cbnIvenProdutoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbnIvenProdutoMouseClicked
        pesquisar_por_produtos();
    }//GEN-LAST:event_cbnIvenProdutoMouseClicked

    private void cbnIvenValorUnitarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbnIvenValorUnitarioMouseClicked
        pesquisar_por_valor_un();
    }//GEN-LAST:event_cbnIvenValorUnitarioMouseClicked

    private void cbnIvenValorTotalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbnIvenValorTotalMouseClicked
        pesquisar_por_valor_total();
    }//GEN-LAST:event_cbnIvenValorTotalMouseClicked

    private void cbnIvenCategoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbnIvenCategoriaMouseClicked
        pesquisar_por_categoria();
    }//GEN-LAST:event_cbnIvenCategoriaMouseClicked

    private void cbnIvenQuantidadeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbnIvenQuantidadeMouseClicked
        pesquisar_por_quantidade();
    }//GEN-LAST:event_cbnIvenQuantidadeMouseClicked

    private void tblEstoqueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEstoqueMouseClicked
        // Chamando o método mostra_codigo
        mostrar_codigo();
    }//GEN-LAST:event_tblEstoqueMouseClicked

    private void btnEstRetirarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstRetirarActionPerformed
        // Chamando o método restirar_do estoque
        retirar_do_estoque();
    }//GEN-LAST:event_btnEstRetirarActionPerformed

    private void btnPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfActionPerformed
        gerar_relatorio_estoque();
    }//GEN-LAST:event_btnPdfActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup GrupoOrdenação;
    private javax.swing.JButton btnEstRetirar;
    private javax.swing.JButton btnPdf;
    private javax.swing.JRadioButton cbnIvenCategoria;
    private javax.swing.JRadioButton cbnIvenProduto;
    private javax.swing.JRadioButton cbnIvenQuantidade;
    private javax.swing.JRadioButton cbnIvenValorTotal;
    private javax.swing.JRadioButton cbnIvenValorUnitario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblEstCódigoProduto;
    private javax.swing.JLabel lblEstQuantidade;
    private javax.swing.JLabel lblEstValorGeral;
    private javax.swing.JTable tblEstoque;
    // End of variables declaration//GEN-END:variables
}
