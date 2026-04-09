package Main;

import Vision.Janela;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Garante execução na Event Dispatch Thread do Swing
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            Janela janela = new Janela();
            janela.setVisible(true);
        });
    }
}
