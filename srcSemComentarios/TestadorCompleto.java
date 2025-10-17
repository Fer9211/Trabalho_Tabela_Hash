import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class TestadorCompleto {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  TESTADOR COMPLETO - 3 FUNÇÕES HASH");
        System.out.println("========================================\n");

        int[] tamanhosTabela = {1000, 10000, 100000};
        int[] tamanhosConjunto = {100000, 1000000, 10000000};
        long seed = 987654321;

        System.out.println("ETAPA 1/3: Testando métodos de ENCADEAMENTO...\n");

        System.out.println("→ Executando Fibonacci Hash (encadeamento)...");
        HashFunction fibonacci = new FibonacciHash();
        testarEncadeamento(fibonacci, tamanhosTabela, tamanhosConjunto,
                "resultados_fibonacci_hash_(golden_ratio).txt");

        System.out.println("→ Executando Soma de Partes (encadeamento)...");
        HashFunction somaParts = new SomaPartesHash();
        testarEncadeamento(somaParts, tamanhosTabela, tamanhosConjunto,
                "resultados_soma_de_partes_(3_dígitos).txt");

        System.out.println("\nETAPA 2/3: Testando REHASHING QUADRÁTICO...\n");
        System.out.println("→ Executando Rehashing Quadrático...");
        testarRehashing(tamanhosTabela, tamanhosConjunto, seed);

        System.out.println("\nETAPA 3/3: Gerando relatório comparativo completo...\n");
        gerarComparativoCompleto(fibonacci, somaParts, tamanhosTabela, tamanhosConjunto, seed);

        System.out.println("\n========================================");
        System.out.println("  ✓ TODOS OS TESTES CONCLUÍDOS!");
        System.out.println("========================================");
        System.out.println("\nArquivos gerados:");
        System.out.println("  1. resultados_fibonacci_hash_(golden_ratio).txt");
        System.out.println("  2. resultados_soma_de_partes_(3_dígitos).txt");
        System.out.println("  3. resultados_rehashing_quadratico.txt");
        System.out.println("  4. comparacao_completa_3_metodos.txt");
    }

    private static void testarEncadeamento(HashFunction hashFunction,
                                           int[] tamanhosTabela,
                                           int[] tamanhosConjunto,
                                           String nomeArquivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            writer.println("===== RESULTADOS - " + hashFunction.getNome() + " =====\n");

            for (int tamanhoTabela : tamanhosTabela) {
                writer.println("### Tamanho da tabela: " + tamanhoTabela + " ###\n");

                for (int tamanhoConjunto : tamanhosConjunto) {
                    System.out.printf("   Tabela: %d | Conjunto: %d%n", tamanhoTabela, tamanhoConjunto);

                    writer.println("Conjunto de dados: " + tamanhoConjunto);

                    VetorValores vetorValores = new VetorValores(tamanhoConjunto);
                    TabelaHash tabela = new TabelaHash(tamanhoTabela);
                    HashTableManager manager = new HashTableManager(hashFunction);

                    long tempoInsercao = manager.medir_tempo_insercao(vetorValores, tabela);
                    writer.printf("Tempo total de inserção: %.3f ms%n", tempoInsercao / 1_000_000.0);
                    writer.println("Número total de colisões: " + manager.getColisoes());

                    long tempoBusca = manager.medir_tempo_busca(vetorValores, tabela);
                    writer.printf("Tempo total de busca: %.3f ms%n", tempoBusca / 1_000_000.0);

                    int[] analise = tabela.contar_gap_e_maior_encadeamento(tabela);
                    writer.println("Número total de gaps: " + analise[0]);
                    writer.println("Menor gap: " + analise[1]);
                    writer.println("Maior gap: " + analise[2]);
                    writer.println("Média de gap: " + analise[3]);
                    writer.println("Três maiores gaps: " + analise[4] + ", " + analise[5] + ", " + analise[6]);
                    writer.println("Três maiores encadeamentos: " + analise[7] + ", " + analise[8] + ", " + analise[9]);

                    writer.println("----------------------------------------\n");
                }
            }

            System.out.println("   ✓ Resultados salvos em " + nomeArquivo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testarRehashing(int[] tamanhosTabela, int[] tamanhosConjunto, long seed) {
        String arquivo = "resultados_rehashing_quadratico.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo))) {
            writer.println("===== RESULTADOS - Rehashing Quadrático =====\n");

            for (int tamanhoTabela : tamanhosTabela) {
                writer.println("### Tamanho da tabela: " + tamanhoTabela + " ###\n");

                for (int tamanhoConjunto : tamanhosConjunto) {
                    System.out.printf("   Tabela: %d | Conjunto: %d%n", tamanhoTabela, tamanhoConjunto);

                    writer.println("Conjunto de dados: " + tamanhoConjunto);

                    RehashingQuadratico rh = new RehashingQuadratico(tamanhoTabela);
                    VetorValores vetorValores = new VetorValores(tamanhoConjunto);
                    int[] conjunto = vetorValores.getVetor();

                    int inseridos = 0;

                    long inicioInsercao = System.nanoTime();
                    for (int chave : conjunto) {
                        if (rh.inserir(chave)) inseridos++;
                    }
                    long fimInsercao = System.nanoTime();

                    long inicioBusca = System.nanoTime();
                    for (int chave : conjunto) {
                        rh.buscar(chave);
                    }
                    long fimBusca = System.nanoTime();

                    double duracaoInsercaoMs = (fimInsercao - inicioInsercao) / 1_000_000.0;
                    double duracaoBuscaMs = (fimBusca - inicioBusca) / 1_000_000.0;

                    int[] gaps = rh.getGaps();
                    int totalGaps = rh.getTotalGaps();
                    double taxaOcupacao = ((double) inseridos / tamanhoTabela) * 100.0;

                    writer.printf("Tempo total de inserção: %.3f ms%n", duracaoInsercaoMs);
                    writer.printf("Número total de colisões: %d%n", rh.getColisoes());
                    writer.printf("Tempo total de busca: %.3f ms%n", duracaoBuscaMs);
                    writer.printf("Elementos inseridos: %d de %d (taxa de ocupação: %.2f%%)%n", inseridos, tamanhoTabela, taxaOcupacao);
                    writer.printf("Número total de gaps: %d%n", totalGaps);
                    writer.printf("Menor gap: %d%n", gaps[0]);
                    writer.printf("Maior gap: %d%n", gaps[1]);
                    writer.printf("Média de gaps: %d%n", gaps[2]);

                    writer.println("----------------------------------------\n");
                }
            }

            System.out.println("   ✓ Resultados salvos em " + arquivo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void gerarComparativoCompleto(HashFunction fibonacci,
                                                 HashFunction somaParts,
                                                 int[] tamanhosTabela,
                                                 int[] tamanhosConjunto,
                                                 long seed) {
        String arquivo = "comparacao_completa_3_metodos.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo))) {
            writer.println("===== COMPARAÇÃO COMPLETA - 3 MÉTODOS DE HASH =====\n");
            writer.println("Métodos testados:");
            writer.println("  1. Fibonacci Hash (encadeamento)");
            writer.println("  2. Soma de Partes (encadeamento)");
            writer.println("  3. Rehashing Quadrático\n");
            writer.println("=======================================================\n");

            for (int tamanhoTabela : tamanhosTabela) {
                for (int tamanhoConjunto : tamanhosConjunto) {
                    System.out.printf("   Comparando: Tabela %d | Conjunto %d%n", tamanhoTabela, tamanhoConjunto);

                    writer.println("Tamanho Tabela: " + tamanhoTabela + " | Conjunto: " + tamanhoConjunto);

                    VetorValores vetorValores = new VetorValores(tamanhoConjunto);

                    writer.println("--- Fibonacci Hash (encadeamento) ---");
                    TabelaHash tabela1 = new TabelaHash(tamanhoTabela);
                    HashTableManager manager1 = new HashTableManager(fibonacci);
                    long tempo1 = manager1.medir_tempo_insercao(vetorValores, tabela1);
                    long busca1 = manager1.medir_tempo_busca(vetorValores, tabela1);
                    writer.printf("  Inserção: %.3f ms | Colisões: %d%n", tempo1 / 1_000_000.0, manager1.getColisoes());
                    writer.printf("  Busca: %.3f ms%n", busca1 / 1_000_000.0);
                    writer.println();

                    writer.println("--- Soma de Partes (encadeamento) ---");
                    TabelaHash tabela2 = new TabelaHash(tamanhoTabela);
                    HashTableManager manager2 = new HashTableManager(somaParts);
                    long tempo2 = manager2.medir_tempo_insercao(vetorValores, tabela2);
                    long busca2 = manager2.medir_tempo_busca(vetorValores, tabela2);
                    writer.printf("  Inserção: %.3f ms | Colisões: %d%n", tempo2 / 1_000_000.0, manager2.getColisoes());
                    writer.printf("  Busca: %.3f ms%n", busca2 / 1_000_000.0);
                    writer.println();

                    writer.println("--- Rehashing Quadrático ---");
                    RehashingQuadratico rh = new RehashingQuadratico(tamanhoTabela);
                    int[] conjunto = vetorValores.getVetor();
                    int inseridos = 0;

                    long inicio3 = System.nanoTime();
                    for (int chave : conjunto) {
                        if (rh.inserir(chave)) inseridos++;
                    }
                    long fim3 = System.nanoTime();

                    long inicioBusca3 = System.nanoTime();
                    for (int chave : conjunto) {
                        rh.buscar(chave);
                    }
                    long fimBusca3 = System.nanoTime();

                    int[] gaps3 = rh.getGaps();
                    int totalGaps3 = rh.getTotalGaps();
                    double taxaOcupacao3 = ((double) inseridos / tamanhoTabela) * 100.0;
                    writer.printf("  Inserção: %.3f ms | Colisões: %d%n", (fim3 - inicio3) / 1_000_000.0, rh.getColisoes());
                    writer.printf("  Busca: %.3f ms%n", (fimBusca3 - inicioBusca3) / 1_000_000.0);
                    writer.printf("  Elementos inseridos: %d de %d (taxa de ocupação: %.2f%%)%n", inseridos, tamanhoTabela, taxaOcupacao3);
                    writer.printf("  Total de gaps: %d%n", totalGaps3);
                    writer.printf("  Gaps - Menor: %d | Maior: %d | Média: %d%n", gaps3[0], gaps3[1], gaps3[2]);

                    writer.println("\n================================================================\n");
                }
            }

            System.out.println("   ✓ Comparação salva em " + arquivo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}