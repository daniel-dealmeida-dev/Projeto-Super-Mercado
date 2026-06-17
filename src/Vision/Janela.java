package Vision;

import Model.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Janela extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JPanel contentPane;
    private final CardLayout cardLayout;

    private TelaLogin telaLogin;
    private TelaCadastroUsuarios telaCadastroUsuarios;
    private TelaCadastroProdutos telaCadastroProdutos;
    private TelaCompra telaCompra;

    public Janela() {
        setTitle("Supermercado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        // Instancia as telas passando referência desta janela
        try {
            telaLogin            = new TelaLogin(this);
            telaCadastroUsuarios = new TelaCadastroUsuarios(this);
            telaCadastroProdutos = new TelaCadastroProdutos(this);
            telaCompra           = new TelaCompra(this);

            contentPane.add(telaLogin,            "login");
            contentPane.add(telaCadastroUsuarios, "cadastroUsuario");
            contentPane.add(telaCadastroProdutos, "cadastroProdutos");
            contentPane.add(telaCompra,           "compra");

            mostrarTela("login");
        } catch (Exception e) {
            System.out.println("Erro ao inicializar as telas: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                "Erro ao inicializar a aplicação: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Navega para uma tela pelo nome do card */
    public void mostrarTela(String nome) {
        try {
            cardLayout.show(contentPane, nome);
        } catch (Exception e) {
            System.out.println("Erro ao navegar para a tela '" + nome + "': " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro ao navegar para a tela: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Chamado após login bem-sucedido.
     * Redireciona conforme perfil do usuário.
     */
    public void aposLogin(Usuario usuario) {
        try {
            if (usuario.isAdministrador()) {
                telaCadastroProdutos.setUsuarioLogado(usuario);
                mostrarTela("cadastroProdutos");
            } else {
                telaCompra.setUsuarioLogado(usuario);
                telaCompra.carregarProdutos();
                mostrarTela("compra");
            }
        } catch (Exception e) {
            System.out.println("Erro ao redirecionar após login: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro ao redirecionar após login: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Desloga o usuário e volta para o login sem fechar a aplicação */
    public void deslogar() {
        try {
            telaLogin.limparCampos();
            mostrarTela("login");
        } catch (Exception e) {
            System.out.println("Erro ao deslogar: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                "Erro ao deslogar: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}