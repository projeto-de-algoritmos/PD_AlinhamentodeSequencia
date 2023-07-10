 class Hirschberg extends AlgoritmoDeAlinhamento {

    // colunaArmazenamento e colunaAlinhamento: Constantes que representam os tipos de coluna na matriz.
    static final int colunaArmazenamento = 2;
    static final int colunaAlinhamento = 4;

    // matrizDireta e matrizReversa: Matrizes usadas para armazenar os valores dos custos.
    int[][] matrizDireta, matrizReversa;

    //alinhamento1 e alinhamento2: Strings que armazenam os alinhamentos das sequências.
    StringBuffer alinhamento1, alinhamento2;

    //gaps
    int custoDelecao = 2;
    int custoInsercao = 2;
    //mismatch
    int custoMismatch = 3;

    //Construtor Hirschberg:
    //Inicializa as variáveis e configura a matriz para o modo não linear.
    //Cria as matrizes matrizDireta e matrizReversa e os objetos alinhamento1 e alinhamento2.
    Hirschberg(String sequencia1, String sequencia2, Matriz matriz) {
        super(sequencia1, sequencia2, matriz);
        matriz.setLinear(false);

        matrizDireta = new int[2][sequencia2.length() + 1];
        matrizReversa = new int[2][sequencia2.length() + 1];
        alinhamento1 = new StringBuffer();
        alinhamento2 = new StringBuffer();
    }

    //metodo nome
    public String nomeAlgoritmo() {
        return "Hirschberg";
    }

    //Limpa o alinhamento anterior na matriz.
    //Define as colunas iniciais da matriz.
    //Chama o método alinhar para realizar o alinhamento.
    public void executar() {
        matriz.limparAlinhamento();
        matriz.setColuna(0, 0, colunaAlinhamento);
        matriz.setColuna(sequencia1.length(), sequencia2.length(), colunaAlinhamento);
        alinhar(0, sequencia1.length(), 0, sequencia2.length());

        if (sair) {
            custoEdicao = -1;
        }
    }

    //Método alinhar:
    //
    //Verifica os casos base:
    //Se a sequência 1 estiver vazia, adiciona gaps na sequência 2.
    //Se a sequência 2 estiver vazia, adiciona gaps na sequência 1.
    //Se a sequência 1 tiver apenas um caractere, faz a correspondência com a sequência 2 ou adiciona gaps.
    //Caso geral:
    //Divide a sequência em duas partes.
    //Chama os métodos dpaDireta e dpaReversa para calcular os custos de edição das partes.
    //Encontra o ponto de divisão ótimo na sequência 2.
    //Marca as células não utilizadas nas matrizes.
    //Realiza a recursão nas duas metades do alinhamento.

    void alinhar(int p1, int p2, int q1, int q2) {
        if (sair) {
            return;
        }

        // Primeiro, os casos base...

        if (p2 <= p1) { // sequência 1 está vazia
            for (int j = q1; j < q2; j++) {
                if (sair) return;
                alinhamento1.append("-");
                alinhamento2.append(sequencia2.charAt(j));
                matriz.setColuna(p1, j + 1, colunaAlinhamento);
                matriz.adicionarAlinhamento(p1, j, p1, j + 1);
                pausar();
            }
            return;
        }

        if (q2 <= q1) { // sequência 2 está vazia
            for (int i = p1; i < p2; i++) {
                if (sair) return;
                alinhamento1.append(sequencia1.charAt(i));
                alinhamento2.append("-");
                matriz.setColuna(i + 1, q2, colunaAlinhamento);
                matriz.adicionarAlinhamento(i, q2, i + 1, q2);
                pausar();
            }
            return;
        }

        if (p1 + 1 == p2) { // sequência 1 possui exatamente um caractere
            char ch = sequencia1.charAt(p1);
            int memo = q1;
            for (int j = q1 + 1; j < q2; j++) {
                if (sequencia2.charAt(j) == ch) memo = j;
            }
            // memo = um ponto de cruzamento ótimo
            for (int j = q1; j < q2; j++) {
                if (sair) return;

                if (j == memo) alinhamento1.append(ch);
                else alinhamento1.append("-");
                alinhamento2.append(sequencia2.charAt(j));

                if (j < memo)
                    matriz.setColuna(p1, j + 1, colunaAlinhamento);
                else
                    matriz.setColuna(p1 + 1, j + 1, colunaAlinhamento);

                matriz.adicionarAlinhamento((j <= memo ? p1 : p1 + 1), j,
                        (j < memo ? p1 : p1 + 1), j + 1);
                pausar();
            }

            return;
        }

        // Caso base concluído.
        // Agora, o caso geral. Divide e conquista!
        int meio = (int) (double) ((p1 + p2) / 2);
        dpaDireta(p1, meio, q1, q2);
        dpaReversa(meio, p2, q1, q2);
        if (sair) return;

        int meioSequencia2 = q1;
        int melhorCusto = infinito;
        // Encontra a divisão mais barata
        for (int j = q1; j <= q2; j++) {
            int soma = matrizDireta[meio % 2][j] + matrizReversa[meio % 2][j];
            if (soma < melhorCusto) {
                melhorCusto = soma;
                meioSequencia2 = j;
            }
        }
        if (custoEdicao == -1) {
            custoEdicao = melhorCusto;
        }

        // Marcar as matrizes como não utilizadas...
        for (int j = q1; j <= q2; j++) {
            celulaNaoUtilizada(meio, j);
            celulaNaoUtilizada(meio - 1, j);
            celulaNaoUtilizada(meio + 1, j);
        }

        matriz.setColuna(meio, meioSequencia2, colunaAlinhamento);

        // Recursão nas duas metades...
        alinhar(p1, meio, q1, meioSequencia2);
        alinhar(meio, p2, meioSequencia2, q2);
    }

    //Métodos dpaDireta e dpaReversa:
    //
    //Calculam os custos de edição nas direções direta e reversa.
    //Configuram os valores iniciais nas primeiras linhas das matrizes.
    //Percorrem as linhas e colunas da matriz, calculando os custos mínimos.
    //Armazenam os valores nas células da matriz e pausam a execução para visualização

    void dpaDireta(int p1, int p2, int q1, int q2) {
        matrizDireta[p1 % 2][q1] = 0;
        armazenarCelula(p1, q1, 0);
        pausar();

        // Configurar a primeira linha
        for (int j = q1 + 1; j <= q2; j++) {
            if (sair) return;
            matrizDireta[p1 % 2][j] = matrizDireta[p1 % 2][j - 1] + custoInsercao;
            armazenarCelula(p1, j, matrizDireta[p1 % 2][j]);

            pausar();
        }

        for (int i = p1 + 1; i <= p2; i++) {
            // Marcar a linha a ser calculada como não utilizada.
            if (i >= p1 + 2) {
                for (int j = q1; j <= q2; j++) {
                    celulaNaoUtilizada(i - 2, j);
                }
            }

            matrizDireta[i % 2][q1] = matrizDireta[(i - 1) % 2][q1] + custoDelecao;
            armazenarCelula(i, q1, matrizDireta[i % 2][q1]);

            pausar();
            for (int j = q1 + 1; j <= q2; j++) {
                if (sair) return;
                int custoDeletar = matrizDireta[(i - 1) % 2][j] + custoDelecao;
                int custoInserir = matrizDireta[i % 2][j - 1] + custoInsercao;
                int custoCorrespondencia = matrizDireta[(i - 1) % 2][j - 1] + (sequencia1.charAt(i - 1) == sequencia2.charAt(j - 1) ? 0 : custoMismatch);
                matrizDireta[i % 2][j] = minimoEntreTres(custoDeletar, custoInserir, custoCorrespondencia);
                armazenarCelula(i, j, matrizDireta[i % 2][j]);

                pausar();
            }
        }
    }

    void dpaReversa(int p1, int p2, int q1, int q2) {
        matrizReversa[p2 % 2][q2] = 0;
        armazenarCelula(p2, q2, 0);

        pausar();

        // Configurar a primeira linha
        for (int j = q2 - 1; j >= q1; j--) {
            if (sair) return;
            matrizReversa[p2 % 2][j] = matrizReversa[p2 % 2][j + 1] + custoInsercao;
            armazenarCelula(p2, j, matrizReversa[p2 % 2][j]);

            pausar();
        }

        for (int i = p2 - 1; i >= p1; i--) {
            // Marcar a linha a ser calculada como não utilizada.
            if (i <= p2 - 2) {
                for (int j = q2; j >= q1; j--) {
                    celulaNaoUtilizada(i + 2, j);
                }
            }

            matrizReversa[i % 2][q2] = matrizReversa[(i + 1) % 2][q2] + custoDelecao;
            armazenarCelula(i, q2, matrizReversa[i % 2][q2]);

            pausar();
            for (int j = q2 - 1; j >= q1; j--) {
                if (sair) return;
                int custoDeletar = matrizReversa[(i + 1) % 2][j] + custoDelecao;
                int custoInserir = matrizReversa[i % 2][j + 1] + custoInsercao;
                int custoCorrespondencia = matrizReversa[(i + 1) % 2][j + 1] + (sequencia1.charAt(i) == sequencia2.charAt(j) ? 0 : custoMismatch);
                matrizReversa[i % 2][j] = minimoEntreTres(custoDeletar, custoInserir, custoCorrespondencia);
                armazenarCelula(i, j, matrizReversa[i % 2][j]);

                pausar();
            }
        }
    }

    //Método celulaNaoUtilizada:
    //Verifica se a célula da matriz não foi utilizada e a reseta se necessário.
    void celulaNaoUtilizada(int i, int j) {
        if (matriz.getColuna(i, j) != colunaAlinhamento) {
            matriz.getCelula(i, j).resetar();
        }
    }

    //Método armazenarCelula:
    //Armazena o valor em uma célula da matriz, considerando o tipo de coluna.
    void armazenarCelula(int i, int j, int v) {
        MatrixCell c = matriz.getCelula(i, j);
        int col = colunaArmazenamento;
        if (c.getColuna() == colunaAlinhamento) col = colunaAlinhamento;
        c.setValor(v, col);
    }

     //Método retornarCaminho:
     //Retorna o alinhamento final das sequências.
     public String retornarCaminho() {
        return alinhamento1.toString() + "\n" + alinhamento2.toString();
    }
}
