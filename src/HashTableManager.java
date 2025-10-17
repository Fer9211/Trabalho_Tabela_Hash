import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Classe que gerencia operações comuns de tabela hash,
 * independente da função hash utilizada.
 */
public class HashTableManager {
    private HashFunction hashFunction;
    private int colisoes;

    public HashTableManager(HashFunction hashFunction) {
        this.hashFunction = hashFunction;
        this.colisoes = 0;
    }

    /**
     * Adiciona um valor na tabela hash usando a função hash configurada.
     * Mantém a lista encadeada ordenada em ordem crescente.
     */
    public void adicionar_valor_tabela(int valor, TabelaHash tabela) {
        int bucket = hashFunction.calcularHash(valor, tabela.getTamanho());
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

    /**
     * Realiza buscas de múltiplos valores na tabela hash.
     */
    public void realizar_buscas(VetorValores vetor_busca, TabelaHash tabela) {
        No[] chave = tabela.getChave();
        int tamanhoBusca = vetor_busca.getTamanho();
        int tamanhoTabela = tabela.getTamanho();
        int[] vetor = vetor_busca.getVetor();

        for (int i = 0; i < tamanhoBusca; i++) {
            int codigo = vetor[i];
            int indice = hashFunction.calcularHash(codigo, tamanhoTabela);

            No atual = chave[indice];
            boolean encontrado = false;

            while (atual != null) {
                if (atual.getValor() == codigo) {
                    // valor encontrado
                    encontrado = true;
                    break;
                }
                atual = atual.getProximo();
            }
        }
    }

    /**
     * Mede o tempo de inserção de todos os valores na tabela.
     */
    public long medir_tempo_insercao(VetorValores valores, TabelaHash tabela) {
        this.colisoes = 0;
        int[] vetor = valores.getVetor();

        long inicio = System.nanoTime();

        for (int i = 0; i < valores.getTamanho(); i++) {
            adicionar_valor_tabela(vetor[i], tabela);
        }

        long fim = System.nanoTime();
        long duracaoNs = fim - inicio;
        double duracaoMs = duracaoNs / 1_000_000.0;

        System.out.println("Inserção - tempo total: " + duracaoNs + " ns (" + duracaoMs + " ms)");

        return duracaoNs;
    }

    /**
     * Mede o tempo de busca de todos os valores na tabela.
     */
    public long medir_tempo_busca(VetorValores valores, TabelaHash tabela) {
        long inicio = System.nanoTime();
        realizar_buscas(valores, tabela);
        long fim = System.nanoTime();
        long duracaoNs = fim - inicio;

        System.out.printf("Tempo total de busca: %d ns%n", duracaoNs);

        return duracaoNs;
    }

    /**
     * Retorna o número total de colisões ocorridas.
     */
    public int getColisoes() {
        return colisoes;
    }

    /**
     * Reseta o contador de colisões.
     */
    public void resetColisoes() {
        this.colisoes = 0;
    }

    /**
     * Retorna a função hash sendo utilizada.
     */
    public HashFunction getHashFunction() {
        return hashFunction;
    }
}
