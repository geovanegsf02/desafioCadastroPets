package entities;

import enums.GeneroPet;
import enums.TipoPet;

public class Pet {
    private String nome;
    private TipoPet tipo;
    private GeneroPet genero;
    private String endereco;
    private String idade;
    private String peso;
    private String raca;

    public Pet(String nome, TipoPet tipo, GeneroPet genero, String endereco, String idade, String peso, String raca) {
        this.nome = nome;
        this.tipo = tipo;
        this.genero = genero;
        this.endereco = endereco;
        this.idade = idade;
        this.peso = peso;
        this.raca = raca;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public GeneroPet getGenero() {
        return genero;
    }

    public void setGenero(GeneroPet genero) {
        this.genero = genero;
    }

    public TipoPet getTipo() {
        return tipo;
    }

    public void setTipo(TipoPet tipo) {
        this.tipo = tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "nome='" + nome + '\'' +
                ", tipo=" + tipo +
                ", genero=" + genero +
                ", endereco='" + endereco + '\'' +
                ", idade=" + idade +
                ", peso=" + peso +
                ", raca='" + raca + '\'' +
                '}';
    }
}
