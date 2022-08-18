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

public class TelaDespesaMaterial extends javax.swing.JInternalFrame {

    public TelaDespesaMaterial() {
        initComponents();
        conexao = ModuloConexao.conector();
        atualizar_campos_data();
        ajustar_data();
        listar_fornecedores();
    }

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    
    private void listar_fornecedores() {
        String select = "select Cod_Fornecedor, Nome from fornecedor;";

        try {
            pst = conexao.prepareStatement(select);
            rs = pst.executeQuery();
            
            while (rs.next()) {
                
                cboCadDespFornecedor.addItem(rs.getString(2));
                
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

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

        String select = "select Cod_DespesaMaterial as 'Cód.', Data_Despesa as 'Data', Nome as 'Fornecedor', Descricao as 'Descrição', despesamaterial.Contato, Quantidade as 'Qntd', Valor_Un as 'Valor Un.', (Quantidade * Valor_Un) as 'Total' "
                + "from despesamaterial inner join despesa on despesamaterial.Cod_Despesa = despesa.Cod_Despesa "
                + "inner join fornecedor on despesamaterial.Cod_Fornecedor = fornecedor.Cod_Fornecedor where Data_Despesa = ?;";

        try {

            pst = conexao.prepareStatement(select);
            pst.setString(1, data_filtro);

            rs = pst.executeQuery();

            tblDespMaterial.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
    }

    public String cod_fornecedor(){
        String cod_fornecedor = null;
        
        String fornecedor = "select min(Cod_Fornecedor) from fornecedor where Nome = ?";
        
        try {
            
            pst = conexao.prepareStatement(fornecedor);
            pst.setString(1, cboCadDespFornecedor.getSelectedItem().toString());
            
            rs = pst.executeQuery();
            
            if (rs.next()) {
                
                cod_fornecedor = rs.getString(1);
                
            }
            
        } catch (SQLException ex) {
            
            JOptionPane.showMessageDialog(null, ex);
            
        }

        return cod_fornecedor;
        
    }
    
    public void cadastrar_despesa() {
        //----------------------Etapa para verificar se a despesa já existe----------------------

        String cod = null;

        try {

            String data_cad_despesa = cboCadDespAno.getSelectedItem().toString() + "-" + cboCadDespMes.getSelectedItem().toString() + "-" + cboCadDespDia.getSelectedItem().toString();

            String select = "select Cod_Despesa from despesa where Data_Despesa = ?";

            pst = conexao.prepareStatement(select);
            pst.setString(1, data_cad_despesa);

            rs = pst.executeQuery();
            
            //-----------------------------Etapa para criar item na despesa caso ela já exista----------------------
            
            if (rs.next()) {
                cod = rs.getString(1);
                
                String insert = "insert into despesamaterial (Cod_Fornecedor, Cod_Despesa, Descricao, Contato, Quantidade, Valor_Un) values (?, ?, ?, ?, ?, ?);";
                
                //O erro começa aqui ********
                
                try {

                    pst = conexao.prepareStatement(insert);

                    pst.setString(1, cod_fornecedor());
                    pst.setString(2, cod);
                    pst.setString(3, txtCadDespDescricao.getText());
                    pst.setString(4, txtCadDespContato.getText());
                    pst.setString(5, txtCadDespQuantidade.getText());
                    pst.setString(6, txtCadDespValor_Un.getText());

                    if (txtCadDespDescricao.getText().isEmpty()
                            || txtCadDespContato.getText().isEmpty()
                            || txtCadDespQuantidade.getText().isEmpty()) {

                        JOptionPane.showMessageDialog(null, "Preencha todos os Campos Obrigatórios");
                        
// e termina aqui *********
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

                String insert = "insert into despesamaterial (Cod_Fornecedor, Cod_Despesa, Descricao, Contato, Quantidade, Valor_Un) values (?, ?, ?, ?, ?, ?);";

                try {

                    pst = conexao.prepareStatement(insert);

                    pst.setString(1, cod_fornecedor());
                    pst.setString(2, cod);
                    pst.setString(3, txtCadDespDescricao.getText());
                    pst.setString(4, txtCadDespContato.getText());
                    pst.setString(5, txtCadDespQuantidade.getText());
                    pst.setString(6, txtCadDespValor_Un.getText());

                    if (txtCadDespDescricao.getText().isEmpty()
                            || txtCadDespContato.getText().isEmpty()
                            || txtCadDespQuantidade.getText().isEmpty()) {

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
        String update = "update despesamaterial set Cod_Fornecedor = ?, Descricao = ?, Contato = ?, Quantidade = ?, Valor_Un = ? where Cod_DespesaMaterial = ?";
        
        try {
            
            pst = conexao.prepareStatement(update);
            pst.setString(1, cod_fornecedor());
            pst.setString(2, txtCadDespDescricao.getText());
            pst.setString(3, txtCadDespContato.getText());
            pst.setString(4, txtCadDespQuantidade.getText());
            pst.setString(5, txtCadDespValor_Un.getText());
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

    private void setar_campos_tblDespMaterial() {
        String categoria = null;

        int setar = tblDespMaterial.getSelectedRow();
        
        lblDespCodigo.setText(tblDespMaterial.getValueAt(setar, 0).toString());
        lblDespData.setText(tblDespMaterial.getValueAt(setar, 1).toString());
        jLabel16.setText(tblDespMaterial.getValueAt(setar, 2).toString());
        lblDespDescricao.setText(tblDespMaterial.getModel().getValueAt(setar, 3).toString());
        lblDespContato.setText(tblDespMaterial.getValueAt(setar, 4).toString());
        lblDespQuantidade.setText(tblDespMaterial.getModel().getValueAt(setar, 5).toString());
        lblDespValor.setText(tblDespMaterial.getModel().getValueAt(setar, 7).toString());
        
        cboCadDespDia.setSelectedItem(tblDespMaterial.getValueAt(setar, 1).toString().substring(8, 10));
        cboCadDespMes.setSelectedItem(tblDespMaterial.getValueAt(setar, 1).toString().substring(5, 7));
        cboCadDespAno.setSelectedItem(tblDespMaterial.getValueAt(setar, 1).toString().substring(0, 4));
        txtCadDespDescricao.setText(tblDespMaterial.getModel().getValueAt(setar, 3).toString());
        txtCadDespContato.setText(tblDespMaterial.getModel().getValueAt(setar, 4).toString());
        txtCadDespQuantidade.setText(tblDespMaterial.getModel().getValueAt(setar, 5).toString());
        
    }
    
    public PdfPTable cabecalho_despesa() {
        PdfPTable tabela = new PdfPTable(new float[]{3f, 4f, 4f, 4f, 4f, 4f, 4f, 4f});

        PdfPCell cel1 = new PdfPCell(new Phrase("Cod.", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel2 = new PdfPCell(new Phrase("Data", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel3 = new PdfPCell(new Phrase("Forn.", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel4 = new PdfPCell(new Phrase("Descrição", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel5 = new PdfPCell(new Phrase("Contato", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel6 = new PdfPCell(new Phrase("Quantidade", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel7 = new PdfPCell(new Phrase("Valor_Un", FontFactory.getFont(FontFactory.HELVETICA, 8F)));
        PdfPCell cel8 = new PdfPCell(new Phrase("Total", FontFactory.getFont(FontFactory.HELVETICA, 8F)));

        cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cel3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cel4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cel5.setHorizontalAlignment(Element.ALIGN_CENTER);
        cel6.setHorizontalAlignment(Element.ALIGN_CENTER);
        cel7.setHorizontalAlignment(Element.ALIGN_CENTER);
        cel8.setHorizontalAlignment(Element.ALIGN_CENTER);

        tabela.addCell(cel1);
        tabela.addCell(cel2);
        tabela.addCell(cel3);
        tabela.addCell(cel4);
        tabela.addCell(cel5);
        tabela.addCell(cel6);
        tabela.addCell(cel7);
        tabela.addCell(cel8);

        return tabela;
    }

    public PdfPTable dados_despesa() {

        PdfPTable tabela = new PdfPTable(new float[]{3f, 4f, 4f, 4f, 4f, 4f, 4f, 4f});

        String select = "select Cod_DespesaMaterial, despesa.Data_Despesa, Nome, Descricao, despesamaterial.Contato, Quantidade, Valor_Un, (Quantidade * Valor_Un) as Total "
                + "from despesamaterial inner join despesa on despesamaterial.Cod_Despesa = despesa.Cod_Despesa "
                + "inner join fornecedor on despesamaterial.Cod_Fornecedor = fornecedor.Cod_Fornecedor;";

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
                PdfPCell cel7 = new PdfPCell(new Phrase(rs.getString(6), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                PdfPCell cel8 = new PdfPCell(new Phrase(rs.getString(6), FontFactory.getFont(FontFactory.HELVETICA, 5F)));
                
                cel1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel6.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel7.setHorizontalAlignment(Element.ALIGN_CENTER);
                cel8.setHorizontalAlignment(Element.ALIGN_CENTER);

                tabela.addCell(cel1);
                tabela.addCell(cel2);
                tabela.addCell(cel3);
                tabela.addCell(cel4);
                tabela.addCell(cel5);
                tabela.addCell(cel6);
                tabela.addCell(cel7);
                tabela.addCell(cel8);

            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }

        return tabela;
    }

    public void gerar_relatorio_despesa() {

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("Despesa de Material.pdf"));

            Paragraph titulo = new Paragraph(new Phrase(10F, "Despesa de Material", FontFactory.getFont(FontFactory.HELVETICA, 18F)));
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

            Desktop.getDesktop().open(new File("Despesa de Material.pdf"));

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
        jLabel7 = new javax.swing.JLabel();
        txtCadDespContato = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCadDespQuantidade = new javax.swing.JTextField();
        btnCadDespCadastrar = new javax.swing.JButton();
        btnCadDespEditar = new javax.swing.JButton();
        cboCadDespDia = new javax.swing.JComboBox<>();
        cboCadDespMes = new javax.swing.JComboBox<>();
        cboCadDespAno = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        txtCadDespValor_Un = new javax.swing.JTextField();
        cboCadDespFornecedor = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblDespDescricao = new javax.swing.JLabel();
        lblDespQuantidade = new javax.swing.JLabel();
        lblDespValor = new javax.swing.JLabel();
        lblDespContato = new javax.swing.JLabel();
        lblDespData = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblDespCodigo = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDespMaterial = new javax.swing.JTable();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);
        setTitle("Cadastro de Despesa");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Depesas de Material");

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
                        .addComponent(cboDespAno, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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

        jLabel4.setText("Descricao: ");

        jLabel5.setText("Valor Un: ");

        jLabel7.setText("Contato: ");

        jLabel8.setText("Quantidade: ");

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

        cboCadDespFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCadDespFornecedorActionPerformed(evt);
            }
        });

        jLabel6.setText("Fornecedor: ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(18, 18, 18))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6)
                        .addComponent(jLabel3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtCadDespDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(cboCadDespDia, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cboCadDespMes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(txtCadDespContato, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel5)
                                .addComponent(cboCadDespAno, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnCadDespCadastrar))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtCadDespValor_Un)
                                    .addComponent(txtCadDespQuantidade, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                                .addComponent(btnCadDespEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(cboCadDespFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboCadDespFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCadDespDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCadDespEditar)
                    .addComponent(txtCadDespQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtCadDespContato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtCadDespValor_Un, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalhes"));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Desc: ");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Qntd: ");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setText("R$: ");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel13.setText("Contato: ");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setText("Data: ");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setText("Cód.:");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setText("Forn: ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDespData, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDespCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDespQuantidade, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDespContato, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDespDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDespValor, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lblDespData, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblDespQuantidade, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14)
                            .addComponent(lblDespCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDespDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblDespContato, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDespValor, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );

        tblDespMaterial.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDespMaterial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDespMaterialMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblDespMaterial);

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

    private void tblDespMaterialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDespMaterialMouseClicked
        setar_campos_tblDespMaterial();
    }//GEN-LAST:event_tblDespMaterialMouseClicked

    private void cboCadDespFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCadDespFornecedorActionPerformed

    }//GEN-LAST:event_cboCadDespFornecedorActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaDespesaMaterial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaDespesaMaterial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaDespesaMaterial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaDespesaMaterial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaDespesaMaterial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadDespCadastrar;
    private javax.swing.JButton btnCadDespEditar;
    private javax.swing.JButton btnDespProcurar;
    private javax.swing.JComboBox<String> cboCadDespAno;
    private javax.swing.JComboBox<String> cboCadDespDia;
    private javax.swing.JComboBox<String> cboCadDespFornecedor;
    private javax.swing.JComboBox<String> cboCadDespMes;
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
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JLabel lblDespContato;
    private javax.swing.JLabel lblDespData;
    private javax.swing.JLabel lblDespDescricao;
    private javax.swing.JLabel lblDespQuantidade;
    private javax.swing.JLabel lblDespValor;
    private javax.swing.JTable tblDespMaterial;
    private javax.swing.JTextField txtCadDespContato;
    private javax.swing.JTextField txtCadDespDescricao;
    private javax.swing.JTextField txtCadDespQuantidade;
    private javax.swing.JTextField txtCadDespValor_Un;
    // End of variables declaration//GEN-END:variables
}
