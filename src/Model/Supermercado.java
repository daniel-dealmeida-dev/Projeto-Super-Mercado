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

   
    public static Supermercado getInstancia() {
        if (instancia == null) {
            instancia = new Supermercado();
        }
        return instancia;
    }

    // Carrinho

    public void adicionarProduto(Produto produto, int quantidade) {
        if (produto.getQuantidade() < quantidade) {
            throw new IllegalArgumentException("Quantidade solicitada maior que o estoque disponível.");
        }
        try {
            carrinho.merge(produto, quantidade, Integer::sum);
        } catch (Exception e) {
            System.out.println("Erro ao adicionar produto ao carrinho: " + e.getMessage());
            throw new RuntimeException("Erro ao adicionar produto ao carrinho.", e);
        }
    }

    public void removerProduto(Produto produto) {
        try {
            carrinho.remove(produto);
        } catch (Exception e) {
            System.out.println("Erro ao remover produto do carrinho: " + e.getMessage());
            throw new RuntimeException("Erro ao remover produto do carrinho.", e);
        }
    }

    public void alterarQuantidade(Produto produto, int novaQuantidade) {
        try {
            if (novaQuantidade <= 0) {
                removerProduto(produto);
            } else {
                carrinho.put(produto, novaQuantidade);
            }
        } catch (Exception e) {
            System.out.println("Erro ao alterar quantidade no carrinho: " + e.getMessage());
            throw new RuntimeException("Erro ao alterar quantidade no carrinho.", e);
        }
    }

    public void limparCarrinho() {
        try {
            carrinho.clear();
        } catch (Exception e) {
            System.out.println("Erro ao limpar o carrinho: " + e.getMessage());
            throw new RuntimeException("Erro ao limpar o carrinho.", e);
        }
    }

    public Map<Produto, Integer> getCarrinho() {
        try {
            return new HashMap<>(carrinho);
        } catch (Exception e) {
            System.out.println("Erro ao obter o carrinho: " + e.getMessage());
            throw new RuntimeException("Erro ao obter o carrinho.", e);
        }
    }

    public List<Produto> getProdutosNoCarrinho() {
        try {
            return new ArrayList<>(carrinho.keySet());
        } catch (Exception e) {
            System.out.println("Erro ao listar produtos do carrinho: " + e.getMessage());
            throw new RuntimeException("Erro ao listar produtos do carrinho.", e);
        }
    }

    public int getQuantidadeNoCarrinho(Produto produto) {
        try {
            return carrinho.getOrDefault(produto, 0);
        } catch (Exception e) {
            System.out.println("Erro ao obter quantidade do produto no carrinho: " + e.getMessage());
            throw new RuntimeException("Erro ao obter quantidade do produto no carrinho.", e);
        }
    }

    public double calcularTotal() {
        try {
            double total = 0;
            for (Map.Entry<Produto, Integer> entry : carrinho.entrySet()) {
                total += entry.getKey().getPreco() * entry.getValue();
            }
            return total;
        } catch (Exception e) {
            System.out.println("Erro ao calcular total do carrinho: " + e.getMessage());
            throw new RuntimeException("Erro ao calcular total do carrinho.", e);
        }
    }

    public boolean isCarrinhoVazio() {
        try {
            return carrinho.isEmpty();
        } catch (Exception e) {
            System.out.println("Erro ao verificar se o carrinho está vazio: " + e.getMessage());
            throw new RuntimeException("Erro ao verificar carrinho.", e);
        }
    }
}