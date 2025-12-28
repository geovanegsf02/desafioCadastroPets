package enums;

public enum TipoPet {
    CACHORRO("Cachorro"),
    GATO("Gato");

    private final String descricao;

    TipoPet(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

}
