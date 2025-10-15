public class Registro {
    private No[] chave;
    private No[] registro;
    private int tamanho;

    public Registro(int tamanho) {
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

                // Percorre a lista encadeada neste bucket
                while (atual != null) {
                    System.out.print(atual.getValor());
                    atual = atual.getProximo();
                    if (atual != null) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println(); // pula linha após cada bucket
            }
        }
    }

    //Verifica se o local da insersão está vazio
    public boolean verificar_se_vazio(int bucket){
        if(chave[bucket] == null){
            return true;
        }
        return false;
    }

    //Retorna o tamanho da tabela hash
    public int getTamanho(){
        return tamanho;
    }

    //retorna o array registro
    public No[] getRegistro(){
        return registro;
    }

    //Retorna o array chave
    public No[] getChave(){
        return chave;
    }

    //Atualiza o array chave por inteiro
    public void setChave(No[] chave){
        this.chave = chave;
    }

    //Atualiza o array registro por inteiro
    public void setRegistro(No[] registro){
        this.registro = registro;
    }

    public int[] contar_gap_e_maior_encadeamento(Registro tabela) {
        int tamanho = tabela.getTamanho();
        No[] vetor = tabela.getChave();

        int numero_total_gaps = 0;
        int[] tres_maiores_gaps = new int[3];
        int[] tres_maiores_encadeamentos = new int[3];

        int ultimo_ocupado = -1;

        for (int i = 0; i < tamanho; i++) {
            if (vetor[i] != null) {
                if (ultimo_ocupado != -1) {
                    int gap = i - ultimo_ocupado - 1;
                    if (gap > 0) {
                        numero_total_gaps++;

                        // Atualiza os 3 maiores gaps
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


        System.out.println("Número total de gaps: " + numero_total_gaps);
        System.out.println("Três maiores gaps: " + tres_maiores_gaps[0] + ", " + tres_maiores_gaps[1] + ", " + tres_maiores_gaps[2]);
        System.out.println("Três maiores encadeamentos: " + tres_maiores_encadeamentos[0] + ", " + tres_maiores_encadeamentos[1] + ", " + tres_maiores_encadeamentos[2]);
        return new int[] {
                numero_total_gaps,
                tres_maiores_gaps[0], tres_maiores_gaps[1], tres_maiores_gaps[2],
                tres_maiores_encadeamentos[0], tres_maiores_encadeamentos[1], tres_maiores_encadeamentos[2]
        };
    }

}
