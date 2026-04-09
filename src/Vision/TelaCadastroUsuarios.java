package Vision;

import Controller.UsuarioController;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class TelaCadastroUsuarios extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Janela janela;
    private final UsuarioController controller = new UsuarioController();

    private JTextField tfNome;
    private JTextField tfCpf;
    private JCheckBox  cbAdmin;

    public TelaCadastroUsuarios(Janela janela) {
        this.janela = janela;
        setLayout(new MigLayout("insets 40", "[right][grow]", "[]20[]20[]20[]20[]"));

        JLabel titulo = new JLabel("Cadastro de Usuário");
        titulo.setFont(titulo.getFont().deriveFont(18f));
        add(titulo, "span 2, center, wrap 30");

        add(new JLabel("Nome:"), "");
        tfNome = new JTextField(20);
        add(tfNome, "growx, wrap");

        add(new JLabel("CPF:"), "");
        tfCpf = new JTextField(20);
        add(tfCpf, "growx, wrap");

        add(new JLabel("Administrador?"), "");
        cbAdmin = new JCheckBox();
        add(cbAdmin, "wrap 30");

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnVoltar    = new JButton("Voltar ao Login");

        add(btnCadastrar, "split 2, center");
        add(btnVoltar,    "wrap");

        btnCadastrar.addActionListener(e -> cadastrar());
        btnVoltar.addActionListener(e -> janela.mostrarTela("login"));
    }

    private void cadastrar() {
        String nome = tfNome.getText().trim();
        String cpf  = tfCpf.getText().trim();
        boolean admin = cbAdmin.isSelected();

        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nome e CPF são obrigatórios.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean sucesso = controller.cadastrar(nome, cpf, admin);
            if (sucesso) {
                JOptionPane.showMessageDialog(this,
                    "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limpar();
                janela.mostrarTela("login");
            } else {
                JOptionPane.showMessageDialog(this,
                    "CPF já cadastrado. Tente fazer login.", "CPF duplicado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao cadastrar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpar() {
        tfNome.setText("");
        tfCpf.setText("");
        cbAdmin.setSelected(false);
    }
}
