public class No {
    private int id;
    private int valor;
    private No proximo;

    public No() {
        this.tamanho = tamanho;
        this.colisoes = 0;
        this.tabelaHash = new tabelaHash[tamanho];
    }
    public No(int id, int valor) {
        this.id = id;
        this.valor = valor;
    }
    public void setProximo(No proximo){
        this.proximo = proximo;
    }
    public void getProximo(No atual){
        return atual.proximo;
    }
    public void getValor(){
        return this.valor;
    }
}