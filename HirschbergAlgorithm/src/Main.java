import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends Applet {
    Matriz matriz;
    ControlesDeAlinhamento controles;

    //iniciar o layout
    public void init() {
        setLayout(new BorderLayout());
        setFont(new Font("Arial", Font.PLAIN, 12));
        setBackground(new Color(1232312));
        matriz = new Matriz("","");
        controles = new ControlesDeAlinhamento(matriz);

        add("South", controles);
        add("Center", matriz);
    }

    //main
    public static void main(String[] args) {
        Frame f = new Frame("Alinhamento de Sequências");
        Main t = new Main();

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        t.init();
        f.add("Center", t);
        f.setSize(900, 800);
        f.show();
    }
}

//botoes e controles
class ControlesDeAlinhamento extends Panel implements ActionListener {
    TextField sequencia1, sequencia2;
    volatile TextArea txtStatus;

    Matriz matriz;
    AlgoritmoDeAlinhamento algoritmo;

    ControlesDeAlinhamento(Matriz matriz) {
        sequencia1 = new TextField("CTACCG", 20);
        sequencia2 = new TextField("TACATG", 20);
        add(sequencia1);
        add(sequencia2);

        Button botaoIniciar = new Button("Hirschberg");
        botaoIniciar.addActionListener(this);
        add(botaoIniciar);

        Button botaoParar = new Button("Parar");
        botaoParar.addActionListener(this);
        add(botaoParar);

        add(new Label("Status:"));
        txtStatus = new TextArea("", 6, 60, TextArea.SCROLLBARS_NONE);
        txtStatus.setEditable(false);

        add(txtStatus);

        this.matriz = matriz;
        matriz.setSequencias(sequencia1.getText(), sequencia2.getText());
    }

    public void pararAlgoritmo() {
        while (algoritmo != null && algoritmo.isAlive()) {
            algoritmo.sair = true;
        }
        algoritmo = null;
    }

    public void actionPerformed(ActionEvent ev) {
        String label = ev.getActionCommand();

        if (label.equalsIgnoreCase("Parar")) {
            pararAlgoritmo();
            return;
        }

        pararAlgoritmo();

        matriz.setSequencias(sequencia1.getText(), sequencia2.getText());
        matriz.limparCelulas();

        algoritmo = new Hirschberg(sequencia1.getText(), sequencia2.getText(), matriz);
        algoritmo.definirJanelaStatus(txtStatus);
        algoritmo.definirPausa(40);
        algoritmo.start();
    }
}
