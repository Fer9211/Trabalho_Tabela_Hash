public class Encadeamento {
    private int colisoes;

    public Encadeamento() {
        this.colisoes = 0;
    }

    public void adicionar_valor_tabela(int valor, TabelaHash tabela) {
        int bucket = conta_funcao_hash(valor, tabela.getTamanho());
        No novo_valor = new No(valor);

        No[] registro = tabela.getRegistro();
        No[] chave = tabela.getChave();

        // Caso o bucket esteja vazio
        if (tabela.verificar_se_vazio(bucket)) {
            registro[bucket] = novo_valor;
            chave[bucket] = registro[bucket];
        } else {
            No head = registro[bucket];

            // Inserir no início (valor menor que o primeiro nó)
            if (valor < head.getValor()) {
                novo_valor.setProximo(head);
                registro[bucket] = novo_valor;
                chave[bucket] = registro[bucket];
            } else {
                // Percorre até achar a posição correta
                No prev = head;
                No atual = head.getProximo();

                while (atual != null && atual.getValor() < valor) {
                    prev = atual;
                    atual = atual.getProximo();
                }

                // Insere entre prev e atual (no fim, atual pode ser null)
                prev.setProximo(novo_valor);
                novo_valor.setProximo(atual);
            }

            // Conta uma colisão (já havia elemento no bucket)
            this.colisoes += 1;
        }

        tabela.setRegistro(registro);
        tabela.setChave(chave);
    }

    //Formula da funcao = h(k) = m([k . A] mod 1)
    //Essa é a conta usada para encontrar o incie
    //A é uma constante, use a constante de Fibonacci hashing
    public int conta_funcao_hash(int valor, int tamanho){
        double a = (Math.sqrt(5) - 1) / 2;
        double resultado = valor * a;
        double parteFracionaria = resultado - Math.floor(resultado);
        return (int) (tamanho * parteFracionaria);
    }


    public static void main(String[] args) {
        int[] valores = {128945376, 903176452, 572684913, 416839725, 739520186, 285617439, 654392871, 871236549, 398761254, 520948367};

        TabelaHash tabela_10 = new TabelaHash(9);
        Encadeamento encadeamento = new Encadeamento();

        for(int i = 0; i < 9; i++){  // usa 0 até 8 para não estourar o array
            encadeamento.adicionar_valor_tabela(valores[i], tabela_10);
        }

        tabela_10.imprimir_tabela_hash();
    }
}
