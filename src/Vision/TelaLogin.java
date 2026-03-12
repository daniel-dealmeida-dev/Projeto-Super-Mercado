package Vision;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TelaLogin extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnLogin;
	private JButton btnCadastro;
			
	

		

	/**
	 * Create the panel.
	 */
	public TelaLogin() {
		setLayout(new MigLayout("", "[][][][][][][grow]", "[][][][][][][][][][]"));
		
		JLabel lbNome = new JLabel("Insira seu nome: ");
		add(lbNome, "cell 0 1");
		
		textField = new JTextField();
		add(textField, "cell 4 1,growx");
		textField.setColumns(10);
		
		JLabel lbCpf = new JLabel("Insira seu CPF:");
		add(lbCpf, "cell 0 3");
		
		textField_1 = new JTextField();
		add(textField_1, "cell 4 3,growx");
		textField_1.setColumns(10);
		
		btnCadastro = new JButton("Cadastro");
		btnCadastro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TelaCadastroUsuarios Cadastro = new TelaCadastroUsuarios();
				Cadastro.setVisible(true);
			}
		});
		
		btnLogin = new JButton("Login");
		add(btnLogin, "cell 2 7");
		add(btnCadastro, "cell 5 7");

	}

}
