package Vision;

import Controller.ProdutoController;
import Model.Produto;
import Model.Usuario;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class TelaCadastroProdutos extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Janela janela;
    private final ProdutoController controller = new ProdutoController();

    private Usuario usuarioLogado;

    // Campos do formulário
    private JTextField tfNome;
    private JTextField tfDescricao;
    private JTextField tfPreco;
    private JTextField tfQuantidade;

    // Tabela
    private DefaultTableModel tableModel;
    private JTable tabela;

    // Produto selecionado para edição
    private int idSelecionado = -1;

    public TelaCadastroProdutos(Janela janela) {
        this.janela = janela;
        setLayout(new MigLayout("insets 10", "[grow][grow]", "[][][grow][]"));

        // ── Cabeçalho ─────────────────────────────────────────────────────
        JLabel titulo = new JLabel("Cadastro de Produtos");
        titulo.setFont(titulo.getFont().deriveFont(16f));
        add(titulo, "span 2, center, wrap");

        // ── Formulário ────────────────────────────────────────────────────
        JPanel form = new JPanel(new MigLayout("", "[right][grow]", ""));
        form.setBorder(BorderFactory.createTitledBorder("Produto"));

        form.add(new JLabel("Nome:"), "");
        tfNome = new JTextField(20);
        form.add(tfNome, "growx, wrap");

        form.add(new JLabel("Descrição:"), "");
        tfDescricao = new JTextField(20);
        form.add(tfDescricao, "growx, wrap");

        form.add(new JLabel("Preço (R$):"), "");
        tfPreco = new JTextField(10);
        form.add(tfPreco, "growx, wrap");

        form.add(new JLabel("Quantidade:"), "");
        tfQuantidade = new JTextField(10);
        form.add(tfQuantidade, "growx, wrap");

        JButton btnSalvar   = new JButton("Salvar");
        JButton btnLimpar   = new JButton("Limpar");
        form.add(btnSalvar, "split 2");
        form.add(btnLimpar, "wrap");

        add(form, "growx, wrap");

        // ── Tabela ────────────────────────────────────────────────────────
        String[] colunas = {"ID", "Nome", "Descrição", "Preço", "Qtd"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getColumnModel().getColumn(0).setMaxWidth(40);

        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, "span 2, grow, wrap");

        // ── Botões da tabela ──────────────────────────────────────────────
        JButton btnEditar  = new JButton("Editar Selecionado");
        JButton btnRemover = new JButton("Remover Selecionado");
        JButton btnSair    = new JButton("Sair / Deslogar");
        add(btnEditar,  "split 3");
        add(btnRemover, "");
        add(btnSair,    "wrap");

        // ── Listeners ─────────────────────────────────────────────────────
        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limparFormulario());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) carregarSelecionadoNoForm();
        });

        btnEditar.addActionListener(e -> carregarSelecionadoNoForm());
        btnRemover.addActionListener(e -> remover());
        btnSair.addActionListener(e -> janela.deslogar());
    }

    public void setUsuarioLogado(Usuario u) {
        this.usuarioLogado = u;
        carregarTabela();
    }

    private void salvar() {
        try {
            String nome   = tfNome.getText().trim();
            String desc   = tfDescricao.getText().trim();
            double preco  = Double.parseDouble(tfPreco.getText().trim().replace(",", "."));
            int    qtd    = Integer.parseInt(tfQuantidade.getText().trim());

            if (idSelecionado >= 0) {
                controller.editar(idSelecionado, nome, desc, preco, qtd);
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                controller.cadastrar(nome, desc, preco, qtd);
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            limparFormulario();
            carregarTabela();

        } catch (NumberFormatException ex) {
            System.out.println("Valor inválido no formulário de produto: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Preço e quantidade devem ser números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            System.out.println("Erro ao salvar produto: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void remover() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) tableModel.getValueAt(linha, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Deseja remover o produto selecionado?", "Confirmar remoção", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.remover(id);
                JOptionPane.showMessageDialog(this, "Produto removido.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparFormulario();
                carregarTabela();
            } catch (Exception ex) {
                System.out.println("Erro ao remover produto: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Erro ao remover produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void carregarSelecionadoNoForm() {
        try {
            int linha = tabela.getSelectedRow();
            if (linha < 0) return;
            idSelecionado = (int) tableModel.getValueAt(linha, 0);
            tfNome.setText((String) tableModel.getValueAt(linha, 1));
            tfDescricao.setText((String) tableModel.getValueAt(linha, 2));
            tfPreco.setText(tableModel.getValueAt(linha, 3).toString());
            tfQuantidade.setText(tableModel.getValueAt(linha, 4).toString());
        } catch (Exception e) {
            System.out.println("Erro ao carregar produto selecionado no formulário: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Erro ao carregar produto para edição: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void carregarTabela() {
        try {
            tableModel.setRowCount(0);
            List<Produto> produtos = controller.listarTodos();
            for (Produto p : produtos) {
                tableModel.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getDescricao(),
                    String.format("%.2f", p.getPreco()), p.getQuantidade()
                });
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar tabela de produtos: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        idSelecionado = -1;
        tfNome.setText("");
        tfDescricao.setText("");
        tfPreco.setText("");
        tfQuantidade.setText("");
        tabela.clearSelection();
    }
}