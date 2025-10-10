public class No {
    private int id;
    private int valor;
    private No proximo;

    public No() {
        this.id = id;
        this.valor = valor;
        this.proximo = null;
    }
    public No(int id, int valor) {
        this.id = id;
        this.valor = valor;
    }
    public No setProximo(No proximo){
        return this.proximo = proximo;
    }
    public No getProximo(){
        return proximo;
    }
    public int getId(){return this.id;}
    public int getValor(){
        return this.valor;
    }
}