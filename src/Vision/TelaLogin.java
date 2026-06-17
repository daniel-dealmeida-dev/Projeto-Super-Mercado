package Vision;

import Controller.UsuarioController;
import Model.Usuario;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class TelaLogin extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Janela janela;
    private final UsuarioController controller = new UsuarioController();

    private JTextField tfNome;
    private JTextField tfCpf;

    public TelaLogin(Janela janela) {
        this.janela = janela;
        setLayout(new MigLayout("insets 40", "[right][grow]", "[]20[]20[]20[]"));

        // Título
        JLabel titulo = new JLabel("Login - Supermercado");
        titulo.setFont(titulo.getFont().deriveFont(18f));
        add(titulo, "span 2, center, wrap 30");

        // Nome
        add(new JLabel("Nome:"), "");
        tfNome = new JTextField(20);
        add(tfNome, "growx, wrap");

        // CPF
        add(new JLabel("CPF:"), "");
        tfCpf = new JTextField(20);
        add(tfCpf, "growx, wrap 30");

        // Botões
        JButton btnLogin = new JButton("Entrar");
        JButton btnCadastro = new JButton("Cadastrar-se");

        add(btnLogin,    "split 2, center");
        add(btnCadastro, "wrap");

        // ── Actions ────────────────────────────────────────────────────────

        btnLogin.addActionListener(e -> realizarLogin());

        // Também permite Enter no campo CPF
        tfCpf.addActionListener(e -> realizarLogin());

        btnCadastro.addActionListener(e -> janela.mostrarTela("cadastroUsuario"));
    }

    private void realizarLogin() {
        try {
            String nome = tfNome.getText().trim();
            String cpf  = tfCpf.getText().trim();

            if (nome.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Preencha o nome e o CPF para entrar.",
                    "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Usuario usuario = controller.login(nome, cpf);
            if (usuario == null) {
                JOptionPane.showMessageDialog(this,
                    "Usuário não encontrado. Verifique os dados ou cadastre-se.",
                    "Login inválido", JOptionPane.ERROR_MESSAGE);
            } else {
                limparCampos();
                janela.aposLogin(usuario);
            }
        } catch (Exception e) {
            System.out.println("Erro ao realizar login: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro inesperado ao realizar login: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limparCampos() {
        try {
            tfNome.setText("");
            tfCpf.setText("");
        } catch (Exception e) {
            System.out.println("Erro ao limpar campos do login: " + e.getMessage());
        }
    }
}