package br.com.salomaotyres.telas;

import java.sql.*;
import javax.swing.JOptionPane;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class TelaVenda extends javax.swing.JInternalFrame {

    public TelaVenda() {
        initComponents();
        conexao = ModuloConexao.conector();
        listar_produtos();
        listar_cliente();
        listar_loja();
    }

    PreparedStatement pst = null;
    PreparedStatement pst2 = null;
    Connection conexao = null;
    ResultSet rs = null;
    ResultSet rs2 = null;

    private void listar_produtos() {
        String select = "select Descricao from produto;";

        try {
            pst = conexao.prepareStatement(select);
            rs = pst.executeQuery();
            while (rs.next()) {
                cboVendProduto.addItem(rs.getString("Descricao"));
            }
            cboVendProduto.updateUI();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void setar_campos_produto() {
        try {
            String selecao = (String) cboVendProduto.getSelectedItem();

            String select = "select produto.Cod_Produto, Descricao, Valor_Un, Quantidade from produto inner join estoque on produto.Cod_Produto = estoque.Cod_Produto where Descricao = ?";

            pst2 = conexao.prepareStatement(select);

            pst2.setString(1, selecao);

            rs2 = pst2.executeQuery();

            if (rs2.next()) {

                lblVendProdCódigo.setText(rs2.getString(1));

                lblVendProdDescricao.setText(rs2.getString(2));

                lblVendProdValor_Un.setText(rs2.getString(3));

                lblVendProdQuantidade.setText(rs2.getString(4));

            } else {

                JOptionPane.showMessageDialog(null, "Não há produtos na lista");

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void adicionar_a_lista() {

        int quantidade_estoque = Integer.parseInt(lblVendProdQuantidade.getText());

        if (txtVendProdQuantidade.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Informe a Quantidade!");

        } else if (quantidade_estoque == 0) {

            JOptionPane.showMessageDialog(null, "Não há este produto em estoque!");

        } else {

            if (txtVendProdDesconto.getText().isEmpty()) {

                JOptionPane.showMessageDialog(null, "Por favor, informe o desconto a ser aplicado.");

            } else {

                double vtotal = Double.parseDouble(lblVendProdValor_Un.getText()) * Double.parseDouble(txtVendProdQuantidade.getText());
                double desconto = Double.parseDouble(txtVendProdDesconto.getText());
                double vtotal_desconto = vtotal - (vtotal * (desconto / 100));

                DefaultTableModel dtmProdutos = (DefaultTableModel) tblLista.getModel();

                Object[] dados = {lblVendProdCódigo.getText(), lblVendProdDescricao.getText(), lblVendProdValor_Un.getText(), txtVendProdQuantidade.getText(), txtVendProdDesconto.getText(), vtotal_desconto};
                dtmProdutos.addRow(dados);
            }
        }
    }

    private void atualizar_total() {

        double valor = 0, soma = 0;

        for (int i = 0; i < tblLista.getRowCount(); i++) {

            valor = Double.parseDouble(tblLista.getValueAt(i, 5).toString());

            soma = soma + valor;

        }

        lblVendTotal.setText(String.valueOf(soma));
    }

    private void remover_da_lista() {

        DefaultTableModel dtm = (DefaultTableModel) tblLista.getModel();

        if (tblLista.getSelectedRow() >= 0) {

            dtm.removeRow(tblLista.getSelectedRow());

            tblLista.setModel(dtm);

        } else {

            JOptionPane.showMessageDialog(null, "Selecione uma linha");

        }

    }

    private void listar_cliente() {

        String select = "select Nome from cliente;";

        try {

            pst = conexao.prepareStatement(select);

            rs = pst.executeQuery();

            while (rs.next()) {

                cboVendCli.addItem(rs.getString("Nome"));

            }

            cboVendCli.updateUI();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void listar_loja() {

        String select = "select Nome from loja;";

        try {

            pst = conexao.prepareStatement(select);

            rs = pst.executeQuery();

            while (rs.next()) {

                cboVendLoja.addItem(rs.getString("Nome"));

            }

            cboVendLoja.updateUI();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void setar_campos_cliente() {
        String select = "select Cod_Cliente, Nome, Cpf, Cnpj, Cidade, Estado, Endereco, Numero, Bairro, Contato, Cep from cliente where Nome = ?";

        String selecao = (String) cboVendCli.getSelectedItem();

        try {

            pst2 = conexao.prepareStatement(select);

            pst2.setString(1, selecao);

            rs2 = pst2.executeQuery();

            if (rs2.next()) {
                lblVendCliCodigo.setText(rs2.getString(1));
                lblVendCliNome.setText(rs2.getString(2));
                lblVendCliCpf.setText(rs2.getString(3));
                lblVendCliCnpj.setText(rs2.getString(4));
                lblVendCliCidade_Est.setText(rs2.getString(5) + ", " + rs2.getString(6));
                lblVendCliEndereco_Num.setText(rs2.getString(7) + ", " + rs2.getString(8));
                lblVendCliBairro.setText(rs2.getString(9));
                lblVendCliContato.setText(rs2.getString(10));
                lblVendCliCep.setText(rs2.getString(11));
            } else {

                JOptionPane.showMessageDialog(null, "Não há clientes!");

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void setar_campos_loja() {

        String select = "select Cod_Loja, Nome, Cnpj, Cep, Cidade, Estado, Endereco, Numero, Bairro, Contato from loja where Nome = ?";

        String selecao = (String) cboVendLoja.getSelectedItem();

        try {

            pst2 = conexao.prepareStatement(select);

            pst2.setString(1, selecao);

            rs2 = pst2.executeQuery();

            if (rs2.next()) {

                lblVendLojCodigo.setText(rs2.getString(1));
                lblVendLojNome.setText(rs2.getString(2));
                lblVendLojCnpj.setText(rs2.getString(4));
                lblVendLojCep.setText(rs2.getString(3));
                lblVendLojCidade_Estado.setText(rs2.getString(5) + ", " + rs2.getString(6));
                lblVendLojEndereco_Numero.setText(rs2.getString(7) + ", " + rs2.getString(8));
                lblVendLojBairro.setText(rs2.getString(9));
                lblVendLojContato.setText(rs2.getString(10));

            } else {

                JOptionPane.showMessageDialog(null, "Não há loja!");

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }

    }

    private String cadastrar_saida() {
        String insert_item_saida = "insert into saida (Total, Cod_Cliente, Cod_Loja) values (?, ?, ?);";
        String select = "select Cod_Saida from saida where Cod_Saida = (select max(Cod_Saida) from saida);";
        String Cod_Saida = null;
        try {

            pst = conexao.prepareStatement(insert_item_saida);

            pst.setString(1, lblVendTotal.getText());
            pst.setString(2, lblVendCliCodigo.getText());
            pst.setString(3, lblVendLojCodigo.getText());

            pst.executeUpdate();

            pst = conexao.prepareStatement(select);

            rs = pst.executeQuery();

            if (rs.next()) {

                Cod_Saida = rs.getString(1);

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }

        return Cod_Saida;
    }

    private void baixa_estoque() {

        for (int i = 0; i < tblLista.getRowCount(); i++) {

            String codigo = tblLista.getModel().getValueAt(i, 0).toString();

            float quantidade_vendida = Float.parseFloat(tblLista.getModel().getValueAt(i, 3).toString());

            double quantidade_estoque = 0;

            try {

                String quantidade = "select Quantidade from estoque where Cod_Produto = ?";

                pst = conexao.prepareStatement(quantidade);

                pst.setString(1, codigo);

                rs = pst.executeQuery();

                if (rs.next()) {

                    quantidade_estoque = Double.parseDouble(rs.getString(1));

                    quantidade_estoque -= quantidade_vendida;

                    pst.close();

                    rs.close();

                }

                String update = "update estoque set Quantidade = ? where Cod_Produto = ?";

                pst = conexao.prepareStatement(update);

                pst.setString(1, String.valueOf(quantidade_estoque));

                pst.setString(2, codigo);

                pst.executeUpdate();

            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, e);

            }
        }
    }

    private void cadastrar_item_saida() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja realizar esta venda?", "Atenção!", JOptionPane.YES_NO_OPTION);

        int cadastrado = 0;

        String insert = "insert into item_saida(Cod_Saida, Cod_Produto, Quantidade, Desconto, Total_Item) values (?, ?, ?, ?, ?);";
        String Cod_Saida = cadastrar_saida();

        if (confirma == JOptionPane.YES_OPTION) {

            try {

                for (int i = 0; i < tblLista.getRowCount(); i++) {

                    pst = conexao.prepareStatement(insert);

                    pst.setString(1, Cod_Saida);
                    pst.setString(2, tblLista.getModel().getValueAt(i, 0).toString());
                    pst.setString(3, tblLista.getModel().getValueAt(i, 3).toString());
                    pst.setString(4, tblLista.getModel().getValueAt(i, 4).toString());
                    pst.setString(5, tblLista.getModel().getValueAt(i, 5).toString());

                    cadastrado = pst.executeUpdate();
                }

                if (cadastrado > 0) {

                    JOptionPane.showMessageDialog(null, "Venda realizada com Sucesso!");
                    baixa_estoque();

                }

            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, e);

            }
        }

    }

    public PdfPTable loja() throws DocumentException {

        PdfPTable tabela = new PdfPTable(new float[]{4f, 4f, 4f});
        PdfPCell cel1 = new PdfPCell();
        PdfPCell cel2 = new PdfPCell();
        PdfPCell cel3 = new PdfPCell();

        String num_nota = "25";

        String loja = "select loja.Nome, loja.Cnpj, loja.Cep, loja.Cidade, loja.Estado, loja.Endereco, loja.Numero, loja.Bairro, loja.Contato, "
                + "cliente.Nome, cliente.Cpf, cliente.Cnpj, cliente.Cep, cliente.Cidade, cliente.Estado, cliente.Endereco, cliente.Numero, cliente.Bairro, cliente.Contato "
                + "from saida inner join loja on saida.Cod_Loja = loja.Cod_Loja "
                + "inner join item_saida on saida.Cod_Saida = item_saida.Cod_Saida "
                + "inner join cliente on saida.Cod_Cliente = cliente.Cod_Cliente where saida.Cod_Saida = ?;";

        try {

            pst = conexao.prepareStatement(loja);
            pst.setString(1, num_nota);
            rs = pst.executeQuery();

            if (rs.next()) {
                cel1.addElement(new Paragraph(new Phrase(8f, "Loja: " + rs.getString(1), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel1.addElement(new Paragraph(new Phrase(8f, "CNPJ: " + rs.getString(2), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel1.addElement(new Paragraph(new Phrase(8f, "CEP: " + rs.getString(3), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel1.addElement(new Paragraph(new Phrase(8f, "Cidade : " + rs.getString(4), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel1.addElement(new Paragraph(new Phrase(8f, "Estado: " + rs.getString(5), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel1.addElement(new Paragraph(new Phrase(8f, "Endereço: " + rs.getString(6) + ", " + rs.getString(7), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel1.addElement(new Paragraph(new Phrase(8f, "Bairro : " + rs.getString(8), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel1.addElement(new Paragraph(new Phrase(8f, "Telefone : " + rs.getString(9), FontFactory.getFont(FontFactory.HELVETICA, 5F))));

                cel2.addElement(new Paragraph(new Phrase(8f, "Nome: " + rs.getString(10), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel2.addElement(new Paragraph(new Phrase(8f, "CPF: " + rs.getString(11), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel2.addElement(new Paragraph(new Phrase(8f, "CNPJ: " + rs.getString(12), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel2.addElement(new Paragraph(new Phrase(8f, "Cidade: " + rs.getString(14), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel2.addElement(new Paragraph(new Phrase(8f, "CEP: " + rs.getString(13), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel2.addElement(new Paragraph(new Phrase(8f, "Estado: " + rs.getString(15), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel2.addElement(new Paragraph(new Phrase(8f, "Endereço: " + rs.getString(16) + ", " + rs.getString(17), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel2.addElement(new Paragraph(new Phrase(8f, "Bairro : " + rs.getString(18), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
                cel2.addElement(new Paragraph(new Phrase(8f, "Telefone : " + rs.getString(19), FontFactory.getFont(FontFactory.HELVETICA, 5F))));
            }

            tabela.addCell(cel1);
            tabela.addCell(cel2);
            tabela.addCell(cel3);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, e);

        }
        return tabela;
    }

    public PdfPTable dados() throws DocumentException {

        PdfPTable table1 = new PdfPTable(new float[]{3f, 5f, 4f, 3f, 4f, 4f, 4f});
        try {
            String num_nota = "25";
            String nota = "select produto.Cod_Produto, produto.Descricao, Quantidade, produto.Valor_Un, Desconto, Total_Item, Total \n"
                    + "from item_saida inner join saida on item_saida.Cod_Saida = saida.Cod_Saida \n"
                    + "inner join produto on item_saida.Cod_Produto = produto.Cod_Produto where saida.Cod_Saida = ?";

            pst = conexao.prepareStatement(nota);
            pst.setString(1, num_nota);
            rs = pst.executeQuery();
            while (rs.next()) {
                PdfPCell cel1 = new PdfPCell(new Phrase(rs.getString(1)));
                PdfPCell cel2 = new PdfPCell(new Phrase(rs.getString(2)));
                PdfPCell cel3 = new PdfPCell(new Phrase(rs.getString(3)));
                PdfPCell cel4 = new PdfPCell(new Phrase(rs.getString(4)));
                PdfPCell cel5 = new PdfPCell(new Phrase(rs.getString(5) + "%"));
                PdfPCell cel6 = new PdfPCell(new Phrase(rs.getString(6)));
                PdfPCell cel7 = new PdfPCell(new Phrase(rs.getString(7)));

                table1.addCell(cel1);
                table1.addCell(cel2);
                table1.addCell(cel3);
                table1.addCell(cel4);
                table1.addCell(cel5);
                table1.addCell(cel6);
                table1.addCell(cel7);
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return table1;
    }

    public static PdfPTable criarCabecalho()
            throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{3f, 5f, 4f, 3f, 4f, 4f, 4f});
        PdfPCell celula_cod = new PdfPCell(new Phrase("Cod"));
        celula_cod.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell celula_produto = new PdfPCell(new Phrase("Produto"));
        celula_produto.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell celula_valor_un = new PdfPCell(new Phrase("Un."));
        celula_produto.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell celula_qtd = new PdfPCell(new Phrase("Qtd"));
        celula_qtd.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell celula_desconto = new PdfPCell(new Phrase("Desconto"));
        celula_desconto.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell celula_total_item = new PdfPCell(new Phrase("Total_item"));
        celula_total_item.setHorizontalAlignment(Element.ALIGN_CENTER);
        PdfPCell celula_total = new PdfPCell(new Phrase("Total"));
        celula_total.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(celula_cod);
        table.addCell(celula_produto);
        table.addCell(celula_qtd);
        table.addCell(celula_valor_un);
        table.addCell(celula_desconto);
        table.addCell(celula_total_item);
        table.addCell(celula_total);

        return table;
    }

    
    public void gerar_notafiscal() {

        String num_nota = "25";

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("Nota Fiscal.pdf"));

            Paragraph titulo = new Paragraph(new Phrase(20F, "Nota Fiscal", FontFactory.getFont(FontFactory.HELVETICA, 18F)));
            titulo.setAlignment(Element.ALIGN_CENTER);

            document.open();
            document.add(titulo);
            document.add(new Paragraph(new Phrase(" ")));

            document.add(loja());
            document.add(criarCabecalho());
            document.add(dados());

        } catch (FileNotFoundException | DocumentException ex) {

            System.out.println("Error:" + ex);

        } finally {
            document.close();
        }

        try {

            Desktop.getDesktop().open(new File("Nota Fiscal.pdf"));

        } catch (IOException ex) {

            System.out.println("Error:" + ex);

        }

    }

    public PdfPTable cabecalho_venda() {
        PdfPTable tabela = new PdfPTable(new float[]{3f, 4f, 4f, 4f, 4f, 4f});

        PdfPCell cel1 = new PdfPCell(new Phrase("Cód.", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel2 = new PdfPCell(new Phrase("Data", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel3 = new PdfPCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel4 = new PdfPCell(new Phrase("Cliente", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel5 = new PdfPCell(new Phrase("CPF", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel6 = new PdfPCell(new Phrase("CNPJ", FontFactory.getFont(FontFactory.HELVETICA, 8F)));

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

    public PdfPTable dados_vendas() {

        PdfPTable tabela = new PdfPTable(new float[]{3f, 4f, 4f, 4f, 4f, 4f});

        String select = "select Cod_Saida, Data_Saida as 'Data', Total, Nome as 'Cliente', Cpf, Cnpj from saida inner join cliente on saida.Cod_Cliente = cliente.Cod_Cliente order by Data_Saida;";

        try {

            pst = conexao.prepareStatement(select);
            rs = pst.executeQuery();

            while (rs.next()) {
                PdfPCell cel1 = new PdfPCell(new Phrase(rs.getString(1), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel2 = new PdfPCell(new Phrase(rs.getString(2), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel3 = new PdfPCell(new Phrase(rs.getString(3), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel4 = new PdfPCell(new Phrase(rs.getString(4), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel5 = new PdfPCell(new Phrase(rs.getString(5), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel6 = new PdfPCell(new Phrase(rs.getString(6), FontFactory.getFont(FontFactory.HELVETICA, 5F)));

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

    public void gerar_relatorio_venda() {

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("Vendas.pdf"));

            Paragraph titulo = new Paragraph(new Phrase(10F, "Relatório de Vendas", FontFactory.getFont(FontFactory.HELVETICA, 18F)));
            titulo.setAlignment(Element.ALIGN_CENTER);

            document.open();
            document.add(titulo);
            document.add(new Paragraph(new Phrase(" ")));

            document.add(cabecalho_venda());
            document.add(dados_vendas());

        } catch (FileNotFoundException | DocumentException ex) {

            System.out.println("Error:" + ex);

        } finally {
            document.close();
        }

        try {

            Desktop.getDesktop().open(new File("Vendas.pdf"));

        } catch (IOException ex) {

            System.out.println("Error:" + ex);

        }

    }

    private void disposeOnClosed() {
        this.dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnVendProdAdcionar = new javax.swing.JButton();
        cboVendProduto = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        lblVendProdCódigo = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblVendProdValor_Un = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblVendProdQuantidade = new javax.swing.JLabel();
        txtVendProdQuantidade = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblVendProdDescricao = new javax.swing.JLabel();
        txtVendProdDesconto = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLista = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblVendTotal = new javax.swing.JLabel();
        btnVendRemover = new javax.swing.JButton();
        btnVendConfirmar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cboVendCli = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblVendCliCodigo = new javax.swing.JLabel();
        lblVendCliNome = new javax.swing.JLabel();
        lblVendCliCpf = new javax.swing.JLabel();
        lblVendCliCnpj = new javax.swing.JLabel();
        lblVendCliEndereco_Num = new javax.swing.JLabel();
        lblVendCliBairro = new javax.swing.JLabel();
        lblVendCliCidade_Est = new javax.swing.JLabel();
        lblVendCliCep = new javax.swing.JLabel();
        lblVendCliContato = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        cboVendLoja = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        lblVendLojNome = new javax.swing.JLabel();
        lblVendLojCnpj = new javax.swing.JLabel();
        lblVendLojCep = new javax.swing.JLabel();
        lblVendLojCidade_Estado = new javax.swing.JLabel();
        lblVendLojEndereco_Numero = new javax.swing.JLabel();
        lblVendLojBairro = new javax.swing.JLabel();
        lblVendLojContato = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        lblVendLojCodigo = new javax.swing.JLabel();
        btnVendCancelar = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(700, 700));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Produto"));
        jPanel1.setToolTipText("Produto");

        btnVendProdAdcionar.setText(">>");
        btnVendProdAdcionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVendProdAdcionarActionPerformed(evt);
            }
        });

        cboVendProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboVendProdutoActionPerformed(evt);
            }
        });

        jLabel2.setText("Código:");

        jLabel3.setText("Preço:");

        jLabel5.setText("Qtd em Estoque:");

        jLabel7.setText("Quantidade: ");

        jLabel4.setText("Nome:");

        jLabel24.setText("Desc. (%):");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVendProdCódigo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblVendProdValor_Un, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(jLabel5))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(16, 16, 16)
                                .addComponent(txtVendProdQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtVendProdDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(lblVendProdQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnVendProdAdcionar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblVendProdDescricao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(cboVendProduto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cboVendProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblVendProdDescricao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblVendProdCódigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblVendProdValor_Un, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblVendProdQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVendProdAdcionar)
                    .addComponent(txtVendProdQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtVendProdDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addContainerGap())
        );

        tblLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome", "Preço", "Qtd.", "Desc. (%)", "Total"
            }
        ));
        jScrollPane1.setViewportView(tblLista);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Valor do Pedido");

        lblVendTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblVendTotal.setForeground(new java.awt.Color(51, 153, 255));
        lblVendTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addComponent(lblVendTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblVendTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnVendRemover.setBackground(new java.awt.Color(255, 153, 0));
        btnVendRemover.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnVendRemover.setForeground(new java.awt.Color(255, 255, 255));
        btnVendRemover.setText("Remover");
        btnVendRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVendRemoverActionPerformed(evt);
            }
        });

        btnVendConfirmar.setBackground(new java.awt.Color(0, 204, 51));
        btnVendConfirmar.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnVendConfirmar.setForeground(new java.awt.Color(255, 255, 255));
        btnVendConfirmar.setText("Confrmar");
        btnVendConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVendConfirmarActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        cboVendCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboVendCliActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Código:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Nome:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("CPF:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("CNPJ:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Endereço:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("Cidade:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setText("CEP:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("Bairro:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setText("Contato:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVendCliCidade_Est, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVendCliEndereco_Num, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVendCliCnpj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVendCliBairro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblVendCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cboVendCli, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblVendCliCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblVendCliContato, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblVendCliCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblVendCliCep, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cboVendCli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblVendCliCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lblVendCliContato, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblVendCliNome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addComponent(lblVendCliCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblVendCliCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(lblVendCliCep, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11)
                            .addComponent(lblVendCliEndereco_Num, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblVendCliBairro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(lblVendCliCidade_Est, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Loja"));

        cboVendLoja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboVendLojaActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("Nome:");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setText("CNPJ:");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setText("CEP:");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel19.setText("Cidade:");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel20.setText("Endereço:");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel21.setText("Bairro:");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel22.setText("Contato:");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel23.setText("Código:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVendLojContato, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVendLojBairro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVendLojEndereco_Numero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVendLojCidade_Estado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVendLojCep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblVendLojCnpj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(cboVendLoja, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 101, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblVendLojNome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lblVendLojCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cboVendLoja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblVendLojCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblVendLojNome, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblVendLojCnpj, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblVendLojCep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblVendLojCidade_Estado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(lblVendLojEndereco_Numero, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblVendLojBairro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(lblVendLojContato, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        btnVendCancelar.setBackground(new java.awt.Color(255, 51, 51));
        btnVendCancelar.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnVendCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnVendCancelar.setText("Cancelar Venda");
        btnVendCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVendCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(91, 91, 91)
                            .addComponent(btnVendCancelar))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnVendRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnVendConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnVendCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnVendRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnVendConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(0, 67, Short.MAX_VALUE))
        );

        setBounds(0, 0, 1062, 517);
    }// </editor-fold>//GEN-END:initComponents

    private void cboVendProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboVendProdutoActionPerformed
        // Chamando o método setar_campos_produto
        setar_campos_produto();
    }//GEN-LAST:event_cboVendProdutoActionPerformed

    private void btnVendProdAdcionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVendProdAdcionarActionPerformed
        // Chamando o método adicionar_a_lista
        adicionar_a_lista();
        atualizar_total();
    }//GEN-LAST:event_btnVendProdAdcionarActionPerformed

    private void btnVendRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVendRemoverActionPerformed
        // Chamando o método remover_da_lista
        remover_da_lista();
        atualizar_total();
    }//GEN-LAST:event_btnVendRemoverActionPerformed

    private void cboVendCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboVendCliActionPerformed
        // Chamando o método setar_campos_cliente
        setar_campos_cliente();
    }//GEN-LAST:event_cboVendCliActionPerformed

    private void cboVendLojaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboVendLojaActionPerformed
        // Cahmando o método setar_campos_loja
        setar_campos_loja();
    }//GEN-LAST:event_cboVendLojaActionPerformed

    private void btnVendConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVendConfirmarActionPerformed
        // TODO add your handling code here:
        cadastrar_item_saida();
        disposeOnClosed();
    }//GEN-LAST:event_btnVendConfirmarActionPerformed

    private void btnVendCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVendCancelarActionPerformed
        // Cancelando a Venda
        disposeOnClosed();
    }//GEN-LAST:event_btnVendCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnVendCancelar;
    private javax.swing.JButton btnVendConfirmar;
    private javax.swing.JButton btnVendProdAdcionar;
    private javax.swing.JButton btnVendRemover;
    private javax.swing.JComboBox<String> cboVendCli;
    private javax.swing.JComboBox<String> cboVendLoja;
    private javax.swing.JComboBox<String> cboVendProduto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblVendCliBairro;
    private javax.swing.JLabel lblVendCliCep;
    private javax.swing.JLabel lblVendCliCidade_Est;
    private javax.swing.JLabel lblVendCliCnpj;
    private javax.swing.JLabel lblVendCliCodigo;
    private javax.swing.JLabel lblVendCliContato;
    private javax.swing.JLabel lblVendCliCpf;
    private javax.swing.JLabel lblVendCliEndereco_Num;
    private javax.swing.JLabel lblVendCliNome;
    private javax.swing.JLabel lblVendLojBairro;
    private javax.swing.JLabel lblVendLojCep;
    private javax.swing.JLabel lblVendLojCidade_Estado;
    private javax.swing.JLabel lblVendLojCnpj;
    private javax.swing.JLabel lblVendLojCodigo;
    private javax.swing.JLabel lblVendLojContato;
    private javax.swing.JLabel lblVendLojEndereco_Numero;
    private javax.swing.JLabel lblVendLojNome;
    private javax.swing.JLabel lblVendProdCódigo;
    private javax.swing.JLabel lblVendProdDescricao;
    private javax.swing.JLabel lblVendProdQuantidade;
    private javax.swing.JLabel lblVendProdValor_Un;
    private javax.swing.JLabel lblVendTotal;
    private javax.swing.JTable tblLista;
    private javax.swing.JTextField txtVendProdDesconto;
    private javax.swing.JTextField txtVendProdQuantidade;
    // End of variables declaration//GEN-END:variables
}
