package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Supermercado {

    private static Supermercado instancia;

    // Carrinho: produto -> quantidade desejada
    private Map<Produto, Integer> carrinho;

    private Supermercado() {
        carrinho = new HashMap<>();
    }

    /** Singleton - um único supermercado por sessão */
    public static Supermercado getInstancia() {
        if (instancia == null) {
            instancia = new Supermercado();
        }
        return instancia;
    }

    // ── Carrinho ────────────────────────────────────────────────────────────

    public void adicionarProduto(Produto produto, int quantidade) {
        if (produto.getQuantidade() < quantidade) {
            throw new IllegalArgumentException("Quantidade solicitada maior que o estoque disponível.");
        }
        carrinho.merge(produto, quantidade, Integer::sum);
    }

    public void removerProduto(Produto produto) {
        carrinho.remove(produto);
    }

    public void alterarQuantidade(Produto produto, int novaQuantidade) {
        if (novaQuantidade <= 0) {
            removerProduto(produto);
        } else {
            carrinho.put(produto, novaQuantidade);
        }
    }

    public void limparCarrinho() {
        carrinho.clear();
    }

    public Map<Produto, Integer> getCarrinho() {
        return new HashMap<>(carrinho);
    }

    public List<Produto> getProdutosNoCarrinho() {
        return new ArrayList<>(carrinho.keySet());
    }

    public int getQuantidadeNoCarrinho(Produto produto) {
        return carrinho.getOrDefault(produto, 0);
    }

    public double calcularTotal() {
        double total = 0;
        for (Map.Entry<Produto, Integer> entry : carrinho.entrySet()) {
            total += entry.getKey().getPreco() * entry.getValue();
        }
        return total;
    }

    public boolean isCarrinhoVazio() {
        return carrinho.isEmpty();
    }
}
