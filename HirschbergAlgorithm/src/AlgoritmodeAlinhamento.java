import java.awt.*;

abstract class AlgoritmoDeAlinhamento extends Thread {
    static final int infinito = 10000000;
    Matriz matriz;
    String sequencia1;
    String sequencia2;
    int custoEdicao;
    int pausa = 0;
    volatile boolean sair = false;
    TextArea status;

    AlgoritmoDeAlinhamento(String seq1, String seq2, Matriz m) {
        matriz = m;
        sequencia1 = seq1;
        sequencia2 = seq2;
        custoEdicao = -1;
    }

    public abstract String nomeAlgoritmo();

    public void definirJanelaStatus(TextArea l) {
        status = l;
        status.setText(nomeAlgoritmo());
    }

    public synchronized void definirPausa(int p) {
        pausa = p;
    }

    protected void pausar() {
        if (sair) return;
        if (pausa == 0) return;

        try {
            Thread.sleep(pausa);
        } catch (InterruptedException e) {
        }
    }

    public void run() {
        status.setText(nomeAlgoritmo() + " em execução.");
        executar();

        if (sair) {
            status.setText(nomeAlgoritmo() + " interrompido.");
        } else {
            status.setText(nomeAlgoritmo() + " concluído. Custo=" + custoEdicao + "\n");
            status.append(retornarCaminho());
        }
    }

    final int minimoEntreTres(int a, int b, int c) {
        return (a < b ? (Math.min(a, c)) : (Math.min(b, c)));
    }
    public String retornarCaminho() {
        return "retornarCaminho ainda não implementado para este algoritmo";
    }

    abstract public void executar();
}
