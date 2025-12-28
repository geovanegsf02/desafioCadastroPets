package enums;

public enum GeneroPet {
    MASCULINO("Masculino"),
    FEMININO("Feminino");

    private final String descricao;

    GeneroPet(String descricao) {
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
