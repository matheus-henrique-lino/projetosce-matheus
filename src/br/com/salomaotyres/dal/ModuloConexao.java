package br.com.salomaotyres.dal;

import java.sql.*;

public class ModuloConexao {
    //Método responsável por estabelecer a conexão com o Banco de Dados
    public static Connection conector(){
        java.sql.Connection conexao = null;
        // A linha abaixo "chama" o driver
        String driver = "com.mysql.cj.jdbc.Driver";
        // Armazenando informações referente ao banco
        String endereco = "jdbc:mysql://localhost:3306/salomaotyres";
        String usuario = "root";
        String senha = "admin";
        
        // Estabelecendo a conexão com o Banco de Dados
        
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(endereco, usuario, senha);
            return conexao;
        } catch (Exception e) {
            System.out.println(e);
            // A linha abaixo serve para exclarecer o erro de conexão como banco de dados
            //System.out.println(e);
            return null;
        }
        
    }
    
}
