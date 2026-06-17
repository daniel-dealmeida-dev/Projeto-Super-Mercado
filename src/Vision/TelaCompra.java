package Vision;

import Controller.ProdutoController;
import Model.Produto;
import Model.Supermercado;
import Model.Usuario;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;

public class TelaCompra extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Janela janela;
    private final ProdutoController produtoController = new ProdutoController();
    private final Supermercado supermercado = Supermercado.getInstancia();

    private Usuario usuarioLogado;

    // Tabela de produtos disponíveis
    private DefaultTableModel modelProdutos;
    private JTable tabelaProdutos;

    // Tabela do carrinho
    private DefaultTableModel modelCarrinho;
    private JTable tabelaCarrinho;

    private JLabel lblTotal;
    private JSpinner spinnerQtd;

    public TelaCompra(Janela janela) {
        this.janela = janela;
        setLayout(new MigLayout("insets 10", "[grow][grow]", "[][grow][grow][]"));

        // ── Título ────────────────────────────────────────────────────────
        JLabel titulo = new JLabel("Compra de Produtos");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        add(titulo, "span 2, center, wrap");

        // ── Tabela produtos ───────────────────────────────────────────────
        JPanel painelProdutos = new JPanel(new MigLayout("insets 5", "[grow]", "[grow][]"));
        painelProdutos.setBorder(BorderFactory.createTitledBorder("Produtos Disponíveis"));

        String[] colProd = {"ID", "Nome", "Descrição", "Preço", "Estoque"};
        modelProdutos = new DefaultTableModel(colProd, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaProdutos = new JTable(modelProdutos);
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaProdutos.getColumnModel().getColumn(0).setMaxWidth(40);
        painelProdutos.add(new JScrollPane(tabelaProdutos), "grow, wrap");

        // Detalhe ao clicar
        tabelaProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) mostrarDetalhe();
            }
        });

        JPanel addPanel = new JPanel(new MigLayout("insets 0", "[][grow][]"));
        addPanel.add(new JLabel("Qtd:"));
        spinnerQtd = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        addPanel.add(spinnerQtd, "growx");
        JButton btnAdd = new JButton("Adicionar ao Carrinho");
        addPanel.add(btnAdd, "");
        painelProdutos.add(addPanel, "growx");

        add(painelProdutos, "grow");

        // ── Carrinho ──────────────────────────────────────────────────────
        JPanel painelCarrinho = new JPanel(new MigLayout("insets 5", "[grow]", "[grow][]"));
        painelCarrinho.setBorder(BorderFactory.createTitledBorder("Carrinho"));

        String[] colCart = {"Nome", "Qtd", "Subtotal"};
        modelCarrinho = new DefaultTableModel(colCart, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaCarrinho = new JTable(modelCarrinho);
        tabelaCarrinho.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        painelCarrinho.add(new JScrollPane(tabelaCarrinho), "grow, wrap");

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(lblTotal.getFont().deriveFont(14f));
        painelCarrinho.add(lblTotal, "");

        add(painelCarrinho, "grow, wrap");

        // ── Botões ────────────────────────────────────────────────────────
        JButton btnRemoverItem = new JButton("Remover do Carrinho");
        JButton btnFinalizar   = new JButton("Finalizar Compra / Nota Fiscal");
        JButton btnSair        = new JButton("Sair / Deslogar");

        add(btnRemoverItem, "split 3");
        add(btnFinalizar,   "");
        add(btnSair,        "wrap");

        // ── Listeners ─────────────────────────────────────────────────────
        btnAdd.addActionListener(e -> adicionarAoCarrinho());
        btnRemoverItem.addActionListener(e -> removerDoCarrinho());
        btnFinalizar.addActionListener(e -> finalizarCompra());
        btnSair.addActionListener(e -> {
            try {
                supermercado.limparCarrinho();
                atualizarCarrinho();
                janela.deslogar();
            } catch (Exception ex) {
                System.out.println("Erro ao sair / deslogar: " + ex.getMessage());
                JOptionPane.showMessageDialog(this,
                    "Erro ao sair: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void setUsuarioLogado(Usuario u) {
        this.usuarioLogado = u;
    }

    public void carregarProdutos() {
        try {
            modelProdutos.setRowCount(0);
            List<Produto> lista = produtoController.listarTodos();
            for (Produto p : lista) {
                if (p.getQuantidade() > 0) {
                    modelProdutos.addRow(new Object[]{
                        p.getId(), p.getNome(), p.getDescricao(),
                        String.format("R$ %.2f", p.getPreco()), p.getQuantidade()
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar produtos disponíveis: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDetalhe() {
        try {
            int linha = tabelaProdutos.getSelectedRow();
            if (linha < 0) return;
            String msg = "Nome: "      + modelProdutos.getValueAt(linha, 1) + "\n" +
                         "Descrição: " + modelProdutos.getValueAt(linha, 2) + "\n" +
                         "Preço: "     + modelProdutos.getValueAt(linha, 3) + "\n" +
                         "Estoque: "   + modelProdutos.getValueAt(linha, 4);
            JOptionPane.showMessageDialog(this, msg, "Detalhes do Produto", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            System.out.println("Erro ao mostrar detalhe do produto: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro ao exibir detalhes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarAoCarrinho() {
        int linha = tabelaProdutos.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idProduto = (int) modelProdutos.getValueAt(linha, 0);
        int qtdDesejada = (int) spinnerQtd.getValue();
        Produto p = produtoController.buscarPorId(idProduto);
        if (p == null) return;

        try {
            supermercado.adicionarProduto(p, qtdDesejada);
            atualizarCarrinho();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Estoque insuficiente", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            System.out.println("Erro ao adicionar produto ao carrinho: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro ao adicionar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerDoCarrinho() {
        int linha = tabelaCarrinho.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um item no carrinho.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String nomeProduto = (String) modelCarrinho.getValueAt(linha, 0);
            for (Produto p : supermercado.getProdutosNoCarrinho()) {
                if (p.getNome().equals(nomeProduto)) {
                    supermercado.removerProduto(p);
                    break;
                }
            }
            atualizarCarrinho();
        } catch (Exception e) {
            System.out.println("Erro ao remover produto do carrinho: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro ao remover produto do carrinho: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarCarrinho() {
        try {
            modelCarrinho.setRowCount(0);
            Map<Produto, Integer> carrinho = supermercado.getCarrinho();
            for (Map.Entry<Produto, Integer> entry : carrinho.entrySet()) {
                Produto p = entry.getKey();
                int qtd = entry.getValue();
                double sub = p.getPreco() * qtd;
                modelCarrinho.addRow(new Object[]{p.getNome(), qtd, String.format("R$ %.2f", sub)});
            }
            lblTotal.setText(String.format("Total: R$ %.2f", supermercado.calcularTotal()));
        } catch (Exception e) {
            System.out.println("Erro ao atualizar carrinho na tela: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro ao atualizar carrinho: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finalizarCompra() {
        if (supermercado.isCarrinhoVazio()) {
            JOptionPane.showMessageDialog(this, "O carrinho está vazio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirmar compra e emitir nota fiscal?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        // Atualiza estoque no banco
        try {
            for (Map.Entry<Produto, Integer> entry : supermercado.getCarrinho().entrySet()) {
                Produto p = entry.getKey();
                int novaQtd = p.getQuantidade() - entry.getValue();
                produtoController.atualizarEstoque(p.getId(), novaQtd);
            }
        } catch (Exception ex) {
            System.out.println("Erro ao atualizar estoque após compra: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Erro ao atualizar estoque: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Emite nota fiscal
        try {
            StringBuilder nota = new StringBuilder();
            nota.append("===== NOTA FISCAL =====\n");
            nota.append("Cliente: ").append(usuarioLogado.getNome()).append("\n");
            nota.append("CPF: ").append(usuarioLogado.getCpf()).append("\n");
            nota.append("-----------------------\n");
            for (Map.Entry<Produto, Integer> entry : supermercado.getCarrinho().entrySet()) {
                Produto p = entry.getKey();
                int qtd = entry.getValue();
                nota.append(String.format("%-20s x%d  R$ %.2f\n", p.getNome(), qtd, p.getPreco() * qtd));
            }
            nota.append("-----------------------\n");
            nota.append(String.format("TOTAL: R$ %.2f\n", supermercado.calcularTotal()));
            nota.append("=======================");

            JTextArea ta = new JTextArea(nota.toString());
            ta.setEditable(false);
            ta.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
            JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Nota Fiscal", JOptionPane.INFORMATION_MESSAGE);

            supermercado.limparCarrinho();
            atualizarCarrinho();
            carregarProdutos(); // Recarrega estoque atualizado
            JOptionPane.showMessageDialog(this, "Compra realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            System.out.println("Erro ao emitir nota fiscal: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro ao emitir nota fiscal: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}