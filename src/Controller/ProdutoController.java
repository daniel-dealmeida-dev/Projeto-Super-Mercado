package Controller;

import DAO.ProdutoDAO;
import Model.Produto;

import java.sql.SQLException;
import java.util.List;

public class ProdutoController {

    private final ProdutoDAO dao = new ProdutoDAO();

    public void cadastrar(String nome, String descricao, double preco, int quantidade) {
        validar(nome, preco, quantidade);
        try {
            dao.inserir(new Produto(nome.trim(), descricao, preco, quantidade));
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar produto: " + e.getMessage(), e);
        }
    }

    public void editar(int id, String nome, String descricao, double preco, int quantidade) {
        validar(nome, preco, quantidade);
        try {
            Produto p = new Produto(id, nome.trim(), descricao, preco, quantidade);
            dao.atualizar(p);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao editar produto: " + e.getMessage(), e);
        }
    }

    public void remover(int id) {
        try {
            dao.remover(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover produto: " + e.getMessage(), e);
        }
    }

    public List<Produto> listarTodos() {
        try {
            return dao.listarTodos();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos: " + e.getMessage(), e);
        }
    }

    public Produto buscarPorId(int id) {
        try {
            return dao.buscarPorId(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produto: " + e.getMessage(), e);
        }
    }

    /** Desconta estoque após compra confirmada */
    public void atualizarEstoque(int idProduto, int novaQtd) {
        try {
            dao.atualizarEstoque(idProduto, novaQtd);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar estoque: " + e.getMessage(), e);
        }
    }

    private void validar(String nome, double preco, int quantidade) {
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        if (preco < 0)
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        if (quantidade < 0)
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");
    }
}
