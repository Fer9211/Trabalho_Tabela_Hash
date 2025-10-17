import java.util.Random;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileWriter;

/**
 * Implementação de Tabela Hash usando Rehashing Quadrático.
 * 
 * Diferente do encadeamento, quando ocorre colisão, o rehashing quadrático
 * busca a próxima posição livre usando uma função quadrática: h(k) + i²
 * 
 * Esta implementação usa uma estrutura SEPARADA das classes de encadeamento,
 * pois rehashing não usa listas encadeadas, apenas um array simples.
 */
public class RehashingQuadratico {

    private int[] tabela;              // Vetor para tabela hash (array simples)
    private int tamanhoTabela;         // Tamanho da tabela
    private long colisoes;             // Contador de colisões

    // Variáveis para análise de gaps
    private int ultimoOcupado = -1;    // Última posição ocupada
    private int menorGap = -1;
    private int maiorGap = 0;
    private long somaGaps = 0;         // Soma para cálculo da média
    private int contGaps = 0;

    /**
     * Construtor: inicializa a tabela vazia.
     * -1 indica posição vazia.
     */
    public RehashingQuadratico(int tamanho) {
        this.tamanhoTabela = tamanho;
        this.tabela = new int[tamanho];
        this.colisoes = 0;
        for (int i = 0; i < tamanho; i++) {
            tabela[i] = -1;  // -1 = vazio
        }
    }

    /**
     * Função hash por multiplicação (Fibonacci hashing).
     * Fórmula: h(k) = ⌊m * ([k * A] mod 1)⌋
     * onde A = (√5 - 1) / 2 (constante de ouro)
     */
    private int funcaoHash(int valor) {
        double a = (Math.sqrt(5) - 1) / 2;  // Constante de Fibonacci (golden ratio)
        double resultado = valor * a;
        double parteFracionaria = resultado - Math.floor(resultado);
        return (int) (tamanhoTabela * parteFracionaria);
    }

    /**
     * Cálculo do índice com rehashing quadrático.
     * Fórmula: (h(k) + i²) mod m
     * 
     * @param chave O valor a ser inserido/buscado
     * @param tentativa Número da tentativa (0, 1, 2, 3...)
     * @return Índice calculado
     */
    private int calcularIndice(int chave, int tentativa) {
        int h = funcaoHash(chave);
        int indice = (h + tentativa * tentativa) % tamanhoTabela;
        
        // Garante que o índice seja positivo
        if (indice < 0) {
            indice += tamanhoTabela;
        }
        
        return indice;
    }

    /**
     * Atualiza estatísticas de gaps (espaços vazios entre elementos).
     * Calcula menor gap, maior gap e soma para média.
     */
    private void atualizarGaps(int indice) {
        if (ultimoOcupado != -1) {
            int gap;
            
            // Calcula gap considerando estrutura circular
            if (indice > ultimoOcupado) {
                gap = indice - ultimoOcupado - 1;
            } else {
                // Gap "dá a volta" na tabela circular
                gap = (tamanhoTabela - ultimoOcupado - 1) + indice;
            }
            
            if (gap > 0) {
                contGaps++;
                somaGaps += gap;
                
                // Atualiza menor gap
                if (menorGap == -1 || gap < menorGap) {
                    menorGap = gap;
                }
                
                // Atualiza maior gap
                if (gap > maiorGap) {
                    maiorGap = gap;
                }
            }
        }
        
        ultimoOcupado = indice;
    }

    /**
     * Insere uma chave na tabela usando rehashing quadrático.
     * Se houver colisão, tenta i² posições à frente.
     * 
     * @param chave Valor a ser inserido
     * @return true se inserido com sucesso, false se tabela cheia
     */
    public boolean inserir(int chave) {
        for (int tentativa = 0; tentativa < tamanhoTabela; tentativa++) {
            int indice = calcularIndice(chave, tentativa);
            
            // Posição vazia encontrada - insere
            if (tabela[indice] == -1) {
                tabela[indice] = chave;
                atualizarGaps(indice);
                return true;
            }
            
            // Posição ocupada - conta colisão e tenta próxima
            colisoes++;
        }
        
        // Tabela cheia - não conseguiu inserir
        return false;
    }

    /**
     * Busca uma chave na tabela.
     * Segue o mesmo caminho usado na inserção.
     * 
     * @param chave Valor a ser buscado
     * @return true se encontrado, false caso contrário
     */
    public boolean buscar(int chave) {
        for (int tentativa = 0; tentativa < tamanhoTabela; tentativa++) {
            int indice = calcularIndice(chave, tentativa);
            
            // Posição vazia - elemento não existe
            if (tabela[indice] == -1) {
                return false;
            }
            
            // Encontrou a chave
            if (tabela[indice] == chave) {
                return true;
            }
        }
        
        // Percorreu toda a tabela e não encontrou
        return false;
    }

    /**
     * Retorna estatísticas de gaps.
     * @return Array com [menorGap, maiorGap, mediaGap]
     */
    public int[] getGaps() {
        int mediaGap = (contGaps > 0) ? (int) (somaGaps / contGaps) : 0;

        // Se não houver gaps, retorna 0 para menor gap
        int menorGapFinal = (menorGap == -1) ? 0 : menorGap;

        return new int[]{menorGapFinal, maiorGap, mediaGap};
    }

    /**
     * Retorna o número total de gaps encontrados.
     */
    public int getTotalGaps() {
        return contGaps;
    }

    /**
     * Retorna o número de colisões ocorridas.
     */
    public long getColisoes() {
        return colisoes;
    }

    /**
     * Geração de conjunto de dados com seed fixa.
     * IMPORTANTE: Usa a MESMA seed (987654321) que os outros métodos!
     */
    private static int[] gerarConjunto(int tamanho, long seed) {
        Random rand = new Random(seed);
        int[] vetor = new int[tamanho];
        int min = 100_000_000;  // Números de 9 dígitos
        int max = 999_999_999;
        
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = rand.nextInt(max - min + 1) + min;
        }
        
        return vetor;
    }

    /**
     * Método main para executar os testes de Rehashing Quadrático.
     * Gera resultados no formato similar aos outros métodos.
     */
    public static void main(String[] args) {
        String arquivo = "resultados_rehashing_quadratico.txt";

        // Mesmas configurações dos outros métodos
        int[] tamanhosTabela = {1000, 10000, 100000};
        int[] tamanhosConjunto = {100000, 1000000, 10000000};
        long seed = 987654321;  // SEED FIXA - mesma dos outros métodos!

        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo))) {
            writer.println("===== RESULTADOS - Rehashing Quadrático =====\n");

            // Loop pelos tamanhos de tabela e conjunto
            for (int tamanhoTabela : tamanhosTabela) {
                writer.println("### Tamanho da tabela: " + tamanhoTabela + " ###\n");

                for (int tamanhoConjunto : tamanhosConjunto) {
                    writer.println("Conjunto de dados: " + tamanhoConjunto);

                    // Cria nova tabela e gera dados
                    RehashingQuadratico rh = new RehashingQuadratico(tamanhoTabela);
                    int[] conjunto = gerarConjunto(tamanhoConjunto, seed);

                    int inseridos = 0;

                    // Mede tempo de inserção
                    long inicioInsercao = System.nanoTime();
                    for (int chave : conjunto) {
                        if (rh.inserir(chave)) {
                            inseridos++;
                        }
                    }
                    long fimInsercao = System.nanoTime();

                    // Mede tempo de busca
                    long inicioBusca = System.nanoTime();
                    for (int chave : conjunto) {
                        rh.buscar(chave);
                    }
                    long fimBusca = System.nanoTime();

                    // Conversão para milissegundos
                    double duracaoInsercaoMs = (fimInsercao - inicioInsercao) / 1_000_000.0;
                    double duracaoBuscaMs = (fimBusca - inicioBusca) / 1_000_000.0;

                    // Estatísticas de gaps
                    int[] gaps = rh.getGaps();
                    double taxaOcupacao = ((double) inseridos / tamanhoTabela) * 100.0;

                    // Saída no console
                    System.out.printf("Testando Rehashing - Tabela: %d, Conjunto: %d%n", 
                                     tamanhoTabela, tamanhoConjunto);
                    System.out.printf("  Tempo inserção: %.3f ms | Colisões: %d%n", 
                                     duracaoInsercaoMs, rh.colisoes);

                    // Escrita no arquivo (formato igual aos outros)
                    writer.printf("Tempo total de inserção: %.3f ms%n", duracaoInsercaoMs);
                    writer.printf("Número total de colisões: %d%n", rh.colisoes);
                    writer.printf("Tempo total de busca: %.3f ms%n", duracaoBuscaMs);
                    writer.printf("Elementos inseridos: %d de %d (taxa de ocupação: %.2f%%)%n", 
                                 inseridos, tamanhoConjunto, taxaOcupacao);
                    writer.println("Número total de gaps: " + (gaps[0] > 0 ? "calculado" : "0"));
                    writer.printf("Menor gap: %d%n", gaps[0]);
                    writer.printf("Maior gap: %d%n", gaps[1]);
                    writer.printf("Média de gaps: %d%n", gaps[2]);
                    writer.println("----------------------------------------\n");
                }
            }

            System.out.println("\nResultados salvos em " + arquivo);

        } catch (IOException e) {
            System.err.println("Erro ao salvar resultados: " + e.getMessage());
        }
    }
}
