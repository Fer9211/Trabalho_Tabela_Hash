import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;


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

    public void realizar_buscas(VetorValores vetor_busca, TabelaHash tabela) {
        No[] chave = tabela.getChave();
        int tamanhoBusca = vetor_busca.getTamanho();
        int tamanhoTabela = tabela.getTamanho();
        int[] vetor = vetor_busca.getVetor();

        for (int i = 0; i < tamanhoBusca; i++) {
            int codigo = vetor[i];
            int indice = conta_funcao_hash(codigo, tamanhoTabela);

            No atual = chave[indice];
            boolean encontrado = false;

            while (atual != null) {
                if (atual.getValor() == codigo) {
                    // valor encontrado
                    // System.out.println("Valor encontrado: " + atual.getValor());
                    encontrado = true;
                    break; // <-- IMPORTANTE! Sai do while quando achar o valor
                }
                atual = atual.getProximo();
            }

            if (!encontrado) {
                // System.out.println("Valor " + codigo + " não encontrado.");
            }
        }
    }


    public long medir_tempo_insercao(VetorValores valores, TabelaHash tabela) {
        this.colisoes = 0;

        int[] vetor =  valores.getVetor();

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
    public long medir_tempo_busca(VetorValores valores, TabelaHash tabela){
        long inicio = System.nanoTime();
        realizar_buscas(valores, tabela);
        long fim = System.nanoTime();
        long duracaoNs = fim - inicio;

        System.out.printf("Tempo total de busca: %d ns%n", duracaoNs);

        return duracaoNs;
    }



    public static void main(String[] args) {

        // Definir tamanhos das tabelas e dos conjuntos de dados
        int[] tamanhosTabela = {1000, 10000, 100000}; // exemplos de tamanhos de tabela
        int[] tamanhosConjunto = {100000, 1000000, 10000000}; // conjuntos de dados

        try (PrintWriter writer = new PrintWriter(new FileWriter("resultados_encadeamento.txt"))) {

            writer.println("===== RESULTADOS COMPLETOS - ENCADEAMENTO =====\n");

            // Loop pelas variações
            for (int tamanhoTabela : tamanhosTabela) {
                writer.println("### Tamanho da tabela: " + tamanhoTabela + " ###\n");

                for (int tamanhoConjunto : tamanhosConjunto) {
                    writer.println("Conjunto de dados: " + tamanhoConjunto);

                    // Criar vetor de valores e tabela hash
                    VetorValores vetorValores = new VetorValores(tamanhoConjunto);
                    TabelaHash tabela = new TabelaHash(tamanhoTabela);
                    Encadeamento encadeamento = new Encadeamento();

                    // Medir tempo de inserção
                    long tempoInsercao = encadeamento.medir_tempo_insercao(vetorValores, tabela);
                    writer.printf("Tempo total de inserção: %.3f ms%n", tempoInsercao / 1_000_000.0);
                    writer.println("Número total de colisões: " + encadeamento.colisoes);

                    // Medir tempo de busca
                    long tempoBusca = encadeamento.medir_tempo_busca(vetorValores, tabela);
                    writer.printf("Tempo total de busca: %.3f ms%n", tempoBusca / 1_000_000.0);

                    // Análise de gaps e encadeamentos
                    int[] analise = tabela.contar_gap_e_maior_encadeamento(tabela);
                    writer.println("Número total de gaps: " + analise[0]);
                    writer.println("Três maiores gaps: " + analise[1] + ", " + analise[2] + ", " + analise[3]);
                    writer.println("Três maiores encadeamentos: " + analise[4] + ", " + analise[5] + ", " + analise[6]);

                    writer.println("----------------------------------------\n");
                }
            }

            System.out.println("Todos os resultados foram salvos em resultados_encadeamento.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
