package Vision;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;

public class Janela extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	// private TelaCadastroProdutos telaCadastroprodutos;
	private TelaLogin telaLogin;
	
	/**
	 * Create the frame.
	 */
	public Janela(TelaLogin telaLogin) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 702, 559);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		// this.telaCadastroprodutos = telacadastroprodutos;
		this.telaLogin = telaLogin;
		contentPane.add(telaLogin, "login");
	}

}
