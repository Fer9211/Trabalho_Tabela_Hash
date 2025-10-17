/**
 * Implementação da função hash que soma partes do valor.
 * Divide o número em partes de 3 dígitos, soma essas partes e aplica módulo.
 * Fórmula: h(k) = (soma das partes de 3 dígitos) mod m
 */
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
