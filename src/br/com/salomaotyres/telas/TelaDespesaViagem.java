package br.com.salomaotyres.telas;

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
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

public class TelaDespesaViagem extends javax.swing.JInternalFrame {

    public TelaDespesaViagem() {
        initComponents();
        conexao = ModuloConexao.conector();
        atualizar_campos_data();
        ajustar_data();
    }

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public void ajustar_data() {
        Date data = new Date();
        
        SimpleDateFormat formato_dia = new SimpleDateFormat("dd");
        SimpleDateFormat formato_mes = new SimpleDateFormat("MM");
        SimpleDateFormat formato_ano = new SimpleDateFormat("yyy");
        
        cboDespDia.setSelectedItem(formato_dia.format(data));
        cboDespMes.setSelectedItem(formato_mes.format(data));
        cboDespAno.setSelectedItem(formato_ano.format(data));
        
        cboCadDespDia.setSelectedItem(formato_dia.format(data));
        cboCadDespMes.setSelectedItem(formato_mes.format(data));
        cboCadDespAno.setSelectedItem(formato_ano.format(data));
    }

    public void atualizar_campos_data() {
        Date data = new Date();

    }

    public void mostrar_despesas() {
        String data_filtro = cboDespAno.getSelectedItem() + "-" + cboDespMes.getSelectedItem() + "-" + cboDespDia.getSelectedItem();

        String select = "select Cod_Item_Despesa as 'Cód.', Descricao, Tipo_Despesa, Pagamento, Qntd, Total_Item as Subtotal, Data_Despesa from item_despesa inner join despesa on item_despesa.Cod_despesa = despesa.Cod_Despesa where Data_Despesa = ?;";

        try {

            pst = conexao.prepareStatement(select);
            pst.setString(1, data_filtro);

            rs = pst.executeQuery();

            tblDespViagem.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
    }

    public void cadastrar_despesa() {
        //----------------------Etapa para verificar se a despesa já existe----------------------

        String cod = null;

        try {

            String data_cad_despesa = cboCadDespAno.getSelectedItem().toString() + "-" + cboCadDespMes.getSelectedItem().toString() + "-" + cboCadDespDia.getSelectedItem().toString();

            String select = "select min(Cod_Despesa) from despesa where Data_Despesa = ?";

            pst = conexao.prepareStatement(select);
            pst.setString(1, data_cad_despesa);

            rs = pst.executeQuery();

            //-----------------------------Etapa para criar item na despesa caso ela já exista----------------------
            
            if (rs.next()) {
                cod = rs.getString(1);

                String insert = "insert into item_despesa (Cod_Despesa, Descricao, Tipo_Despesa, Pagamento, Qntd, Total_Item) values (?, ?, ?, ?, ?, ?);";

                try {

                    pst = conexao.prepareStatement(insert);

                    pst.setString(1, cod);
                    pst.setString(2, txtCadDespDescricao.getText());
                    pst.setString(3, cboCadDespGasto.getSelectedItem().toString());
                    pst.setString(4, cboCadDespPagamento.getSelectedItem().toString());
                    pst.setString(5, txtCadDespQuantidade.getText());
                    pst.setString(6, txtCadDespTotal.getText());

                    if (txtCadDespDescricao.getText().isEmpty()
                            || txtCadDespQuantidade.getText().isEmpty()
                            || txtCadDespTotal.getText().isEmpty()) {

                        JOptionPane.showMessageDialog(null, "Preencha todos os Campos Obrigatórios");

                    } else {

                        int adicionar = pst.executeUpdate();

                        if (adicionar > 0) {

                            JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!");

                        }
                    }

                } catch (SQLException ex) {

                    JOptionPane.showMessageDialog(null, ex);

                }

//------------------------------Etapa para criar uma despesa e item/s na despesa caso a despesa não esteja criada----------------------
            } else {

                String criando_despesa = "insert into despesa (Data_Despesa) values (?);";

                try {

                    pst = conexao.prepareStatement(criando_despesa);
                    pst.setString(1, data_cad_despesa);

                    int adicionado = pst.executeUpdate();

                    if (adicionado > 0) {

                        JOptionPane.showMessageDialog(null, "Despesa criada!");

                    }

                } catch (Exception e) {

                    JOptionPane.showMessageDialog(null, e);

                }

                String cod_despesa = "select min(Cod_Despesa) from despesa where Data_Despesa = ?";

                pst = conexao.prepareStatement(cod_despesa);
                pst.setString(1, data_cad_despesa);
                rs = pst.executeQuery();

                if (rs.next()) {
                    cod = rs.getString(1);
                }

                String insert = "insert into item_despesa (Cod_Despesa, Descricao, Tipo_Despesa, Pagamento, Qntd, Total_Item) values (?, ?, ?, ?, ?, ?);";

                try {

                    pst = conexao.prepareStatement(insert);

                    pst.setString(1, cod);
                    pst.setString(2, txtCadDespDescricao.getText());
                    pst.setString(3, cboCadDespGasto.getSelectedItem().toString());
                    pst.setString(4, cboCadDespPagamento.getSelectedItem().toString());
                    pst.setString(5, txtCadDespQuantidade.getText());
                    pst.setString(6, txtCadDespTotal.getText());

                    if (txtCadDespDescricao.getText().isEmpty()
                            || txtCadDespQuantidade.getText().isEmpty()
                            || txtCadDespTotal.getText().isEmpty()) {

                        JOptionPane.showMessageDialog(null, "Preencha todos os Campos Obrigatórios");

                    } else {

                        int adicionar = pst.executeUpdate();

                        if (adicionar > 0) {

                            JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!");

                        }
                    }

                } catch (SQLException ex) {

                    JOptionPane.showMessageDialog(null, ex);

                }

            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }

    }
    
    public void editar (){
        String update = "update item_despesa set Descricao = ?, Tipo_Despesa = ?, Pagamento = ?, Qntd = ?, Total_Item = ? where Cod_Item_Despesa = ?";
        
        try {
            
            pst = conexao.prepareStatement(update);
            pst.setString(1, txtCadDespDescricao.getText());
            pst.setString(2, cboCadDespGasto.getSelectedItem().toString());
            pst.setString(3, cboCadDespPagamento.getSelectedItem().toString());
            pst.setString(4, txtCadDespQuantidade.getText());
            pst.setString(5, txtCadDespTotal.getText());
            pst.setString(6, lblDespCodigo.getText());
            
            int adicionado = pst.executeUpdate();
            
            if(adicionado > 0){
                JOptionPane.showMessageDialog(null, "Despesa alterada com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Falha na alteração!");
            }
         
            
        } catch (SQLException ex) {
            
            JOptionPane.showMessageDialog(null, ex);
            
        }
        
    }

    private void setar_campos_tblDespViagem() {
        String categoria = null;

        int setar = tblDespViagem.getSelectedRow();

        //Cod_Item_Despesa as 'Cód.', Descricao, Tipo_Despesa, Pagamento, Qntd, Total_Item as Subtotal, Data_Despesa
        lblDespDescricao.setText(tblDespViagem.getModel().getValueAt(setar, 1).toString());
        lblDespQuantidade.setText(tblDespViagem.getModel().getValueAt(setar, 4).toString());
        lblDespValor.setText(tblDespViagem.getModel().getValueAt(setar, 5).toString());
        lblDespData.setText(tblDespViagem.getValueAt(setar, 6).toString());
        lblDespTipo_pagamento.setText(tblDespViagem.getValueAt(setar, 3).toString());
        lblDespCodigo.setText(tblDespViagem.getValueAt(setar, 0).toString());
        
        txtCadDespDescricao.setText(tblDespViagem.getModel().getValueAt(setar, 1).toString());
        txtCadDespQuantidade.setText(tblDespViagem.getModel().getValueAt(setar, 4).toString());
        txtCadDespTotal.setText(tblDespViagem.getModel().getValueAt(setar, 5).toString());
        cboCadDespPagamento.setSelectedItem(tblDespViagem.getValueAt(setar, 3).toString());
        cboCadDespGasto.setSelectedItem(tblDespViagem.getValueAt(setar, 2).toString());
        cboCadDespDia.setSelectedItem(tblDespViagem.getValueAt(setar, 6).toString().substring(8, 10));
        cboCadDespMes.setSelectedItem(tblDespViagem.getValueAt(setar, 6).toString().substring(5, 7));
        cboCadDespAno.setSelectedItem(tblDespViagem.getValueAt(setar, 6).toString().substring(0, 4));
        
    }
    
    public PdfPTable cabecalho_despesa() {
        PdfPTable tabela = new PdfPTable(new float[]{3f, 4f, 4f, 4f, 4f, 4f});

        PdfPCell cel1 = new PdfPCell(new Phrase("Data", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel2 = new PdfPCell(new Phrase("Rota", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel3 = new PdfPCell(new Phrase("Tipo", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel4 = new PdfPCell(new Phrase("Pagamento", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
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

    public PdfPTable dados_despesa() {

        PdfPTable tabela = new PdfPTable(new float[]{3f, 4f, 4f, 4f, 4f, 4f});

        String select = "select Data_Despesa, Descricao, Tipo_Despesa, Pagamento, Qntd, Total_Item from despesa inner join Item_Despesa on despesa.Cod_Despesa = item_despesa.Cod_Despesa order by Data_Despesa;";

        try {

            pst = conexao.prepareStatement(select);
            rs = pst.executeQuery();

            while (rs.next()) {
                PdfPCell cel1 = new PdfPCell(new Phrase(rs.getString(1), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel2 = new PdfPCell(new Phrase(rs.getString(2), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel3 = new PdfPCell(new Phrase(rs.getString(3), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel4 = new PdfPCell(new Phrase(rs.getString(4), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
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

    public void gerar_relatorio_despesa() {

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("Despesa de Viagem.pdf"));

            Paragraph titulo = new Paragraph(new Phrase(10F, "Despesa de Viagem", FontFactory.getFont(FontFactory.HELVETICA, 18F)));
            titulo.setAlignment(Element.ALIGN_CENTER);

            document.open();
            document.add(titulo);
            document.add(new Paragraph(new Phrase(" ")));

            document.add(cabecalho_despesa());
            document.add(dados_despesa());

        } catch (FileNotFoundException | DocumentException ex) {

            System.out.println("Error:" + ex);

        } finally {
            document.close();
        }

        try {

            Desktop.getDesktop().open(new File("Despesa de Viagem.pdf"));

        } catch (IOException ex) {

            System.out.println("Error:" + ex);

        }

    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cboDespDia = new javax.swing.JComboBox<>();
        cboDespMes = new javax.swing.JComboBox<>();
        cboDespAno = new javax.swing.JComboBox<>();
        btnDespProcurar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCadDespDescricao = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cboCadDespPagamento = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        cboCadDespGasto = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        txtCadDespQuantidade = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCadDespTotal = new javax.swing.JTextField();
        btnCadDespCadastrar = new javax.swing.JButton();
        btnCadDespEditar = new javax.swing.JButton();
        cboCadDespDia = new javax.swing.JComboBox<>();
        cboCadDespMes = new javax.swing.JComboBox<>();
        cboCadDespAno = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblDespDescricao = new javax.swing.JLabel();
        lblDespQuantidade = new javax.swing.JLabel();
        lblDespValor = new javax.swing.JLabel();
        lblDespTipo_pagamento = new javax.swing.JLabel();
        lblDespData = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblDespCodigo = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDespViagem = new javax.swing.JTable();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);
        setTitle("Cadastro de Despesa");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Depesas de Viagem");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setText("Data: ");

        cboDespDia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        cboDespMes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", " " }));

        cboDespAno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050", "2051", "2052", "2053", "2054", "2055", "2056", "2057", "2058", "2059", "2060", "2061", "2062", "2063", "2064", "2065", "2066", "2067", "2068", "2069", "2070", "2071", "2072", "2073", "2074", "2075", "2076", "2077", "2078", "2079", "2080", "2081", "2082", "2083", "2084", "2085", "2086", "2087", "2088", "2089", "2090", "2091", "2092", "2093", "2094", "2095", "2096", "2097", "2098", "2099", "2100", " " }));

        btnDespProcurar.setBackground(new java.awt.Color(0, 153, 255));
        btnDespProcurar.setText("Procurar");
        btnDespProcurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDespProcurarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnDespProcurar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboDespDia, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboDespMes, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboDespAno, 0, 97, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboDespDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDespMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDespAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDespProcurar)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cadastrar Despesa"));

        jLabel3.setText("* Data: ");

        jLabel4.setText("* Rota: ");

        jLabel5.setText("* Pagamento: ");

        cboCadDespPagamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dinheiro", "Crédito", "Débito" }));

        jLabel6.setText(" * Gasto: ");

        cboCadDespGasto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alimentação", "Combustível", "Veículo", "Pedágio", "Geral" }));

        jLabel7.setText("* Quantidade: ");

        jLabel8.setText("* Valor R$: ");

        btnCadDespCadastrar.setBackground(new java.awt.Color(0, 204, 102));
        btnCadDespCadastrar.setText("Cadastrar");
        btnCadDespCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadDespCadastrarActionPerformed(evt);
            }
        });

        btnCadDespEditar.setBackground(new java.awt.Color(255, 153, 0));
        btnCadDespEditar.setText("Editar");
        btnCadDespEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadDespEditarActionPerformed(evt);
            }
        });

        cboCadDespDia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        cboCadDespMes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", " " }));

        cboCadDespAno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050", "2051", "2052", "2053", "2054", "2055", "2056", "2057", "2058", "2059", "2060", "2061", "2062", "2063", "2064", "2065", "2066", "2067", "2068", "2069", "2070", "2071", "2072", "2073", "2074", "2075", "2076", "2077", "2078", "2079", "2080", "2081", "2082", "2083", "2084", "2085", "2086", "2087", "2088", "2089", "2090", "2091", "2092", "2093", "2094", "2095", "2096", "2097", "2098", "2099", "2100", " " }));

        jLabel9.setText("Campos Obrigatórios *");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtCadDespDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(cboCadDespDia, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cboCadDespMes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtCadDespTotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                        .addComponent(txtCadDespQuantidade, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboCadDespAno, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 176, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCadDespEditar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCadDespCadastrar, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboCadDespGasto, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(cboCadDespPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(18, 18, 18))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(btnCadDespCadastrar)
                    .addComponent(cboCadDespDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCadDespMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCadDespAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtCadDespDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCadDespEditar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtCadDespQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cboCadDespPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cboCadDespGasto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCadDespTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalhes"));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Desc: ");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Qntd: ");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("R$: ");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setText("Pag: ");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setText("Data: ");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("Cód.:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDespValor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDespDescricao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDespQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel15)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblDespData, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblDespTipo_pagamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDespCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDespTipo_pagamento, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                    .addComponent(lblDespDescricao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDespData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                    .addComponent(lblDespQuantidade, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDespValor, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                    .addComponent(lblDespCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)))
        );

        tblDespViagem.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDespViagem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDespViagemMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblDespViagem);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(330, 330, 330)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCadDespCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadDespCadastrarActionPerformed
        //Chama o método cadastrar_despesa e atualiza as tabelas
        cadastrar_despesa();
        mostrar_despesas();
    }//GEN-LAST:event_btnCadDespCadastrarActionPerformed

    private void btnDespProcurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDespProcurarActionPerformed

        mostrar_despesas();
    }//GEN-LAST:event_btnDespProcurarActionPerformed

    private void btnCadDespEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadDespEditarActionPerformed
        editar();
        mostrar_despesas();
    }//GEN-LAST:event_btnCadDespEditarActionPerformed

    private void tblDespViagemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDespViagemMouseClicked
        setar_campos_tblDespViagem();
    }//GEN-LAST:event_tblDespViagemMouseClicked

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaDespesaViagem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaDespesaViagem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaDespesaViagem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaDespesaViagem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaDespesaViagem().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadDespCadastrar;
    private javax.swing.JButton btnCadDespEditar;
    private javax.swing.JButton btnDespProcurar;
    private javax.swing.JComboBox<String> cboCadDespAno;
    private javax.swing.JComboBox<String> cboCadDespDia;
    private javax.swing.JComboBox<String> cboCadDespGasto;
    private javax.swing.JComboBox<String> cboCadDespMes;
    private javax.swing.JComboBox<String> cboCadDespPagamento;
    private javax.swing.JComboBox<String> cboDespAno;
    private javax.swing.JComboBox<String> cboDespDia;
    private javax.swing.JComboBox<String> cboDespMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblDespCodigo;
    private javax.swing.JLabel lblDespData;
    private javax.swing.JLabel lblDespDescricao;
    private javax.swing.JLabel lblDespQuantidade;
    private javax.swing.JLabel lblDespTipo_pagamento;
    private javax.swing.JLabel lblDespValor;
    private javax.swing.JTable tblDespViagem;
    private javax.swing.JTextField txtCadDespDescricao;
    private javax.swing.JTextField txtCadDespQuantidade;
    private javax.swing.JTextField txtCadDespTotal;
    // End of variables declaration//GEN-END:variables
}
