public class SomaPartesHash implements HashFunction {

    @Override
    public int calcularHash(int valor, int tamanho) {
        String valorStr = String.valueOf(valor);
        int soma = 0;
        int tamanhoParte = 3;

        for (int i = 0; i < valorStr.length(); i += tamanhoParte) {
            int fim = Math.min(i + tamanhoParte, valorStr.length());
            String parte = valorStr.substring(i, fim);
            soma += Integer.parseInt(parte);
        }

        return soma % tamanho;
    }

    @Override
    public String getNome() {
        return "Soma de Partes (3 dígitos)";
    }
}