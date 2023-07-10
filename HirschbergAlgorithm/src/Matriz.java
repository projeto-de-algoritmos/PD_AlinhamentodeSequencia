import java.util.*;
import java.lang.*;
import java.awt.*;

class Matriz extends Canvas {

    String sequencia1, sequencia2;
    int dimensaoX, dimensaoY;
    MatrixCell[][] celulas;
    Vector<Rectangle> paresAlinhados = new Vector<>();
    boolean linear = false;

    public Matriz(String sequencia1, String sequencia2) {
        this.sequencia1 = sequencia1;
        this.sequencia2 = sequencia2;
        alocarEstruturas();
    }

    public void setLinear(boolean lin) {
        if (lin == linear) return;
        linear = lin;
        alocarEstruturas();
        repaint();
    }

    public void setSequencias(String sequencia1, String sequencia2) {
        if (sequencia1.equals(this.sequencia1) && sequencia2.equals(this.sequencia2))
            return;

        this.sequencia1 = sequencia1;
        this.sequencia2 = sequencia2;
        alocarEstruturas();
        repaint();
    }

    protected void alocarEstruturas() {
        dimensaoX = sequencia1.length() + 1;
        dimensaoY = sequencia2.length() + 1;

        if (linear)
            celulas = new LinearMatrixCell[dimensaoX][dimensaoY];
        else
            celulas = new MatrixCell[dimensaoX][dimensaoY];

        paresAlinhados.removeAllElements();

        for (int i = 0; i < dimensaoX; i++) {
            for (int j = 0; j < dimensaoY; j++) {
                if (linear)
                    celulas[i][j] = new LinearMatrixCell(this, i, j);
                else
                    celulas[i][j] = new MatrixCell(this, i, j);
            }
        }
    }

    public void limparCelulas() {
        for (int i = 0; i < dimensaoX; i++) {
            for (int j = 0; j < dimensaoY; j++) {
                celulas[i][j].resetar();
            }
        }
    }

    public MatrixCell getCelula(int i, int j) {
        return celulas[i][j];
    }

    public int getColuna(int i, int j) {
        return celulas[i][j].getColuna();
    }

    public void setColuna(int i, int j, int c) {
        celulas[i][j].setColuna(c);
    }

    public void limparAlinhamento() {
        paresAlinhados.removeAllElements();
    }

    public void adicionarAlinhamento(int i1, int j1, int i2, int j2) {
        paresAlinhados.addElement(new Rectangle(i1, j1, i2, j2));
    }

    FontMetrics fm;
    double fAltura, fLargura;
    int linha1 = 0, coluna1 = 0;
    int espacamentoCelula = 1;

    Dimension getTamanhoCelula() {
        int largura = getWidth();
        int altura = getHeight();
        double larguraCelula = (double) (largura - coluna1) / dimensaoY - espacamentoCelula;
        double alturaCelula = (double) (altura - linha1) / dimensaoX - espacamentoCelula;
        return new Dimension((int) larguraCelula, (int) alturaCelula);
    }

    public void modificado(int i, int j) {
        Dimension tamanhoCelula = getTamanhoCelula();
        int x = coluna1 + j * (tamanhoCelula.width + espacamentoCelula);
        int y = linha1 + i * (tamanhoCelula.height + espacamentoCelula);
        repaint(x, y, tamanhoCelula.width, tamanhoCelula.height);
    }

    public void paint(Graphics g) {
        if (fm == null) {
            fm = g.getFontMetrics();
            fAltura = fm.getHeight();
            fLargura = fm.stringWidth("X");
            linha1 = (int) (fAltura * 2);
            coluna1 = (int) (fLargura * 2);
        }

        Dimension tamanhoCelula = getTamanhoCelula();
        Rectangle clip = g.getClipBounds();

        // Desenhar as células que precisam ser redesenhadas
        for (int i = 0; i < dimensaoX; i++) {
            for (int j = 0; j < dimensaoY; j++) {
                int x = coluna1 + j * (tamanhoCelula.width + espacamentoCelula);
                int y = linha1 + i * (tamanhoCelula.height + espacamentoCelula);
                if (clip == null || clip.intersects(new Rectangle(x, y, tamanhoCelula.width, tamanhoCelula.height))) {
                    celulas[i][j].paint(g, x, y, tamanhoCelula.width, tamanhoCelula.height);
                }
            }
        }

        // Desenhar as sequências
        g.setColor(Color.darkGray);
        for (int i = 0; i < sequencia1.length(); i++) {
            g.drawString(String.valueOf(sequencia1.charAt(i)), 0, (int) (linha1 + fAltura / 2 + (i + 1) * (tamanhoCelula.height + espacamentoCelula)));
        }

        for (int j = 0; j < sequencia2.length(); j++) {
            g.drawString(String.valueOf(sequencia2.charAt(j)), (int) (coluna1 - fLargura / 2 + (j + 1) * (tamanhoCelula.width + espacamentoCelula)), (int) (fAltura * 1.5));
        }
    }

}
