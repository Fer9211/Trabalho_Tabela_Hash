public interface HashFunction {
    int calcularHash(int valor, int tamanho);

    String getNome();
}