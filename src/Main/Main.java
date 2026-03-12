package Main;

import Vision.Janela;
import Vision.TelaCadastroProdutos;
import Vision.TelaLogin;

public class Main {

	public static void main(String[] args) {
	
	//	TelaCadastroProdutos cadastroProdutos = new TelaCadastroProdutos();
		TelaLogin telaLogin = new TelaLogin();
		
		Janela janela = new Janela(telaLogin);
		janela.setVisible(true);

	}

}
