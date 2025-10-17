import java.util.Random;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileWriter;

public class RehashingQuadratico {

    private int[] tabela;
    private int tamanhoTabela;
    private long colisoes;

    private int ultimoOcupado = -1;
    private int menorGap = -1;
    private int maiorGap = 0;
    private long somaGaps = 0;
    private int contGaps = 0;

    public RehashingQuadratico(int tamanho) {
        this.tamanhoTabela = tamanho;
        this.tabela = new int[tamanho];
        this.colisoes = 0;
        for (int i = 0; i < tamanho; i++) {
            tabela[i] = -1;
        }
    }

    private int funcaoHash(int valor) {
        double a = (Math.sqrt(5) - 1) / 2;
        double resultado = valor * a;
        double parteFracionaria = resultado - Math.floor(resultado);
        return (int) (tamanhoTabela * parteFracionaria);
    }

    private int calcularIndice(int chave, int tentativa) {
        int h = funcaoHash(chave);
        int indice = (h + tentativa * tentativa) % tamanhoTabela;

        if (indice < 0) {
            indice += tamanhoTabela;
        }

        return indice;
    }

    private void atualizarGaps(int indice) {
        if (ultimoOcupado != -1) {
            int gap;

            if (indice > ultimoOcupado) {
                gap = indice - ultimoOcupado - 1;
            } else {
                gap = (tamanhoTabela - ultimoOcupado - 1) + indice;
            }

            if (gap > 0) {
                contGaps++;
                somaGaps += gap;

                if (menorGap == -1 || gap < menorGap) {
                    menorGap = gap;
                }

                if (gap > maiorGap) {
                    maiorGap = gap;
                }
            }
        }

        ultimoOcupado = indice;
    }

    public boolean inserir(int chave) {
        for (int tentativa = 0; tentativa < tamanhoTabela; tentativa++) {
            int indice = calcularIndice(chave, tentativa);

            if (tabela[indice] == -1) {
                tabela[indice] = chave;
                atualizarGaps(indice);
                return true;
            }

            colisoes++;
        }

        return false;
    }

    public boolean buscar(int chave) {
        for (int tentativa = 0; tentativa < tamanhoTabela; tentativa++) {
            int indice = calcularIndice(chave, tentativa);

            if (tabela[indice] == -1) {
                return false;
            }

            if (tabela[indice] == chave) {
                return true;
            }
        }

        return false;
    }

    public int[] getGaps() {
        int mediaGap = (contGaps > 0) ? (int) (somaGaps / contGaps) : 0;

        int menorGapFinal = (menorGap == -1) ? 0 : menorGap;

        return new int[]{menorGapFinal, maiorGap, mediaGap};
    }

    public int getTotalGaps() {
        return contGaps;
    }

    public long getColisoes() {
        return colisoes;
    }

    private static int[] gerarConjunto(int tamanho, long seed) {
        Random rand = new Random(seed);
        int[] vetor = new int[tamanho];
        int min = 100_000_000;
        int max = 999_999_999;

        for (int i = 0; i < tamanho; i++) {
            vetor[i] = rand.nextInt(max - min + 1) + min;
        }

        return vetor;
    }

    public static void main(String[] args) {
        String arquivo = "resultados_rehashing_quadratico.txt";

        int[] tamanhosTabela = {1000, 10000, 100000};
        int[] tamanhosConjunto = {100000, 1000000, 10000000};
        long seed = 987654321;

        try (PrintWriter writer = new PrintWriter(new FileWriter(arquivo))) {
            writer.println("===== RESULTADOS - Rehashing Quadrático =====\n");

            for (int tamanhoTabela : tamanhosTabela) {
                writer.println("### Tamanho da tabela: " + tamanhoTabela + " ###\n");

                for (int tamanhoConjunto : tamanhosConjunto) {
                    writer.println("Conjunto de dados: " + tamanhoConjunto);

                    RehashingQuadratico rh = new RehashingQuadratico(tamanhoTabela);
                    int[] conjunto = gerarConjunto(tamanhoConjunto, seed);

                    int inseridos = 0;

                    long inicioInsercao = System.nanoTime();
                    for (int chave : conjunto) {
                        if (rh.inserir(chave)) {
                            inseridos++;
                        }
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
                    double taxaOcupacao = ((double) inseridos / tamanhoTabela) * 100.0;

                    System.out.printf("Testando Rehashing - Tabela: %d, Conjunto: %d%n",
                            tamanhoTabela, tamanhoConjunto);
                    System.out.printf("  Tempo inserção: %.3f ms | Colisões: %d%n",
                            duracaoInsercaoMs, rh.colisoes);

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