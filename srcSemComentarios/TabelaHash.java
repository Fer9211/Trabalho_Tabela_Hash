public class TabelaHash {
    private No[] chave;
    private No[] registro;
    private int tamanho;

    public TabelaHash(int tamanho) {
        this.tamanho = tamanho;
        this.chave = new No[tamanho];
        this.registro = new No[tamanho];
    }

    public void imprimir_tabela_hash() {
        for (int i = 0; i < tamanho; i++) {
            System.out.print("Índice -> " + i + " | ");

            if (registro[i] == null) {
                System.out.println("Vazio");
            } else {
                No atual = registro[i];
                System.out.print("Chave = " + chave[i].getValor() + " | Registro = ");

                while (atual != null) {
                    System.out.print(atual.getValor());
                    atual = atual.getProximo();
                    if (atual != null) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println();
            }
        }
    }

    public boolean verificar_se_vazio(int bucket) {
        if (chave[bucket] == null) {
            return true;
        }
        return false;
    }

    public int getTamanho() {
        return tamanho;
    }

    public No[] getRegistro() {
        return registro;
    }

    public No[] getChave() {
        return chave;
    }

    public void setChave(No[] chave) {
        this.chave = chave;
    }

    public void setRegistro(No[] registro) {
        this.registro = registro;
    }

    public int[] contar_gap_e_maior_encadeamento(TabelaHash tabela) {
        int tamanho = tabela.getTamanho();
        No[] vetor = tabela.getChave();

        int numero_total_gaps = 0;
        int[] tres_maiores_gaps = new int[3];
        int[] tres_maiores_encadeamentos = new int[3];

        int menor_gap = Integer.MAX_VALUE;
        int maior_gap = 0;
        int soma_gaps = 0;

        int ultimo_ocupado = -1;

        for (int i = 0; i < tamanho; i++) {
            if (vetor[i] != null) {
                if (ultimo_ocupado != -1) {
                    int gap = i - ultimo_ocupado - 1;
                    if (gap > 0) {
                        numero_total_gaps++;
                        soma_gaps += gap;

                        if (gap < menor_gap) {
                            menor_gap = gap;
                        }

                        if (gap > maior_gap) {
                            maior_gap = gap;
                        }

                        if (gap > tres_maiores_gaps[0]) {
                            tres_maiores_gaps[2] = tres_maiores_gaps[1];
                            tres_maiores_gaps[1] = tres_maiores_gaps[0];
                            tres_maiores_gaps[0] = gap;
                        } else if (gap > tres_maiores_gaps[1]) {
                            tres_maiores_gaps[2] = tres_maiores_gaps[1];
                            tres_maiores_gaps[1] = gap;
                        } else if (gap > tres_maiores_gaps[2]) {
                            tres_maiores_gaps[2] = gap;
                        }
                    }
                }
                ultimo_ocupado = i;
            }
        }

        for (int i = 0; i < tamanho; i++) {
            if (vetor[i] != null) {
                No atual = vetor[i];
                int contagem_encadeamentos = 0;

                while (atual.getProximo() != null) {
                    contagem_encadeamentos++;
                    atual = atual.getProximo();
                }

                if (contagem_encadeamentos > tres_maiores_encadeamentos[0]) {
                    tres_maiores_encadeamentos[2] = tres_maiores_encadeamentos[1];
                    tres_maiores_encadeamentos[1] = tres_maiores_encadeamentos[0];
                    tres_maiores_encadeamentos[0] = contagem_encadeamentos;
                } else if (contagem_encadeamentos > tres_maiores_encadeamentos[1]) {
                    tres_maiores_encadeamentos[2] = tres_maiores_encadeamentos[1];
                    tres_maiores_encadeamentos[1] = contagem_encadeamentos;
                } else if (contagem_encadeamentos > tres_maiores_encadeamentos[2]) {
                    tres_maiores_encadeamentos[2] = contagem_encadeamentos;
                }
            }
        }

        double media_gap = (numero_total_gaps > 0) ? (double) soma_gaps / numero_total_gaps : 0;

        if (menor_gap == Integer.MAX_VALUE) {
            menor_gap = 0;
        }

        System.out.println("Número total de gaps: " + numero_total_gaps);
        System.out.println("Menor gap: " + menor_gap);
        System.out.println("Maior gap: " + maior_gap);
        System.out.printf("Média de gap: %.2f%n", media_gap);
        System.out.println("Três maiores gaps: " + tres_maiores_gaps[0] + ", " + tres_maiores_gaps[1] + ", " + tres_maiores_gaps[2]);
        System.out.println("Três maiores encadeamentos: " + tres_maiores_encadeamentos[0] + ", " + tres_maiores_encadeamentos[1] + ", " + tres_maiores_encadeamentos[2]);

        return new int[]{
                numero_total_gaps,
                menor_gap,
                maior_gap,
                (int) media_gap,
                tres_maiores_gaps[0], tres_maiores_gaps[1], tres_maiores_gaps[2],
                tres_maiores_encadeamentos[0], tres_maiores_encadeamentos[1], tres_maiores_encadeamentos[2]
        };
    }
}