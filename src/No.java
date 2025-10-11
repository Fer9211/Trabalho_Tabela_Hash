public class No {
    private int valor;
    private No proximo;

    public No(int valor) {
        this.valor = valor;
    }
    public No setProximo(No proximo){
        return this.proximo = proximo;
    }
    public No getProximo(){
        return proximo;
    }
    public int getValor(){
        return this.valor;
    }

    public String toString() {
        return "" + this.valor + "";
    }
}