package Controller;

import DAO.UsuarioDAO;
import Model.Usuario;

import java.sql.SQLException;

public class UsuarioController {

    private final UsuarioDAO dao = new UsuarioDAO();

    /**
     * Tenta logar o usuário pelo nome e CPF.
     * @return Usuario encontrado ou null se credenciais inválidas.
     */
    public Usuario login(String nome, String cpf) {
        if (nome == null || nome.isBlank() || cpf == null || cpf.isBlank()) {
            return null;
        }
        try {
            return dao.buscarPorNomeECpf(nome.trim(), cpf.trim());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Cadastra um novo usuário.
     * @return true se cadastrado com sucesso, false se CPF já existe.
     * @throws RuntimeException em caso de erro de banco.
     */
    public boolean cadastrar(String nome, String cpf, boolean administrador) {
        if (nome == null || nome.isBlank() || cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("Nome e CPF são obrigatórios.");
        }
        try {
            if (dao.cpfExiste(cpf.trim())) {
                return false; // CPF já cadastrado
            }
            Usuario u = new Usuario(nome.trim(), cpf.trim(), administrador);
            dao.inserir(u);
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar usuário: " + e.getMessage(), e);
        }
    }
}
