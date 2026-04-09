package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoBD {

    // ── Configurações do banco ─────────────────────────────────────────────
    private static final String HOST    = "localhost";
    private static final String PORTA   = "3306";
    private static final String BANCO   = "supermercado";
    private static final String USUARIO = "root";
    private static final String SENHA   = "admin";  

    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORTA + "/" + BANCO
        + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo";

    private static Connection conexao;

    private ConexaoBD() {}

    public static Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                inicializarBanco(conexao);
            } catch (ClassNotFoundException e) {
                throw new SQLException(
                    "Driver MySQL não encontrado. Adicione mysql-connector-j ao classpath.", e);
            }
        }
        return conexao;
    }

    public static void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Cria as tabelas se ainda não existirem */
    private static void inicializarBanco(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS usuarios (
                id            INT          AUTO_INCREMENT PRIMARY KEY,
                nome          VARCHAR(100) NOT NULL,
                cpf           VARCHAR(14)  NOT NULL UNIQUE,
                administrador TINYINT(1)   NOT NULL DEFAULT 0
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS produtos (
                id         INT           AUTO_INCREMENT PRIMARY KEY,
                nome       VARCHAR(100)  NOT NULL,
                descricao  VARCHAR(255),
                preco      DECIMAL(10,2) NOT NULL,
                quantidade INT           NOT NULL DEFAULT 0
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
        """);

        stmt.close();
    }
}
