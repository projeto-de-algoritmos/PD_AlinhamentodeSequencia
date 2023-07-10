import java.awt.*;

class MatrixCell {
    static final int grandeInteiro = 100000;
    static final int maxCol = 5;
    static final Color[] cores = {
            new Color(50, 50, 50),
            new Color(102, 0, 0),
            new Color(150, 10, 100),
            new Color(0, 0, 102),
            new Color(0, 150, 0)
    };
    protected int col;
    protected int valor;

    int i, j;
    Matriz matriz;
    boolean precisaRepintar;

    public MatrixCell(Matriz matriz, int i, int j) {
        col = 0;
        valor = grandeInteiro;
        this.i = i;
        this.j = j;
        this.matriz = matriz;
    }

    public void resetar() {
        boolean repintar = (col != 0 || valor != grandeInteiro);
        col = 0;
        valor = grandeInteiro;
        if (repintar && !precisaRepintar) {
            precisaRepintar = true;
            matriz.modificado(i, j);
        }
    }

    public int getColuna() {
        return col;
    }

    public void setColuna(int c) {
        boolean repintar = (c != col);
        col = c;
        if (repintar && !precisaRepintar) {
            precisaRepintar = true;
            matriz.modificado(i, j);
        }
    }

    public void setValor(int v, int c) {
        boolean repintar = (valor != v || col != c);
        valor = v;
        col = c;
        if (repintar && !precisaRepintar) {
            precisaRepintar = true;
            matriz.modificado(i, j);
        }
    }

    public void paint(Graphics g, int x, int y, int largura, int altura) {
        precisaRepintar = false;

        g.setColor(cores[Math.min(col, maxCol - 1)]);
        g.fillRect(x, y, largura, altura);

        if (largura < 10 || altura < matriz.fAltura - 4) {
            return; // Não desenhar o número se for muito pequeno
        }

        g.setColor(Color.white);
        String s = (valor > 1000 ? "" : String.valueOf(valor));
        int larguraString = matriz.fm.stringWidth(s);
        g.drawString(s, x + (largura - larguraString) / 2, y + altura / 2 + (int) (matriz.fAltura / 4));
    }
}
