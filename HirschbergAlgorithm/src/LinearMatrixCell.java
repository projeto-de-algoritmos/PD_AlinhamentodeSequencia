import java.awt.*;

class LinearMatrixCell extends MatrixCell {

    int[] valores = {grandeInteiro, grandeInteiro, grandeInteiro};
    int largura = -1, altura = -1;
    double xpos = 0, ypos = 0;

    public LinearMatrixCell(Matriz matriz, int i, int j) {
        super(matriz, i, j);
    }

    public void resetar() {
        boolean repintar = col != 0 || valores[0] != grandeInteiro || valores[1] != grandeInteiro || valores[2] != grandeInteiro;
        col = 0;
        valores[0] = grandeInteiro;
        valores[1] = grandeInteiro;
        valores[2] = grandeInteiro;
        if (repintar && !precisaRepintar) {
            precisaRepintar = true;
            matriz.modificado(i, j);
        }
    }

    public void setValor(int a, int b) {
        System.err.println("ERRO: Chamada inválida de setValor em LinearMatrixCell");
    }

    public void paint(Graphics g, int x, int y, int largura, int altura) {
        precisaRepintar = false;

        if (largura != this.largura || altura != this.altura) {
            // Mudou desde o último cálculo desses valores...
            this.largura = largura;
            this.altura = altura;
            xpos = ((double) largura / 2 - matriz.fLargura) / 2;
            ypos = ((double) altura / 2 - matriz.fAltura) / 2 + matriz.fAltura;
        }

        g.setColor(cores[Math.min(col, maxCol - 1)]);
        g.fillRect(x, y, largura, altura);

        if (largura < 2 * matriz.fLargura || altura < 2 * matriz.fAltura) {
            return;
        }

        g.setColor(Color.white);

        String v1 = (valores[0] > 1000 ? "" : String.valueOf(valores[0]));
        String v2 = (valores[1] > 1000 ? "" : String.valueOf(valores[1]));
        String v3 = (valores[2] > 1000 ? "" : String.valueOf(valores[2]));
        g.drawString(v1, (int) (x + xpos), (int) (y + ypos));
        g.drawString(v2, (int) (x + xpos + largura / 2), (int) (y + ypos));
        g.drawString(v3, (int) (x + xpos), (int) (y + ypos + altura / 2));
    }
}
