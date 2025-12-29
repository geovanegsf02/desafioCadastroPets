package utils;

public class ValidaPet {
    private static final String NAO_INFORMADO = "NÃO INFORMADO";

    private ValidaPet() {
    }

    public static String[] validarPet(String[] petValor) {
        for (int i = 0; i < petValor.length; i++) {
            if (i == 0) { // nome
                petValor[0] = validaNome(petValor[0]);
            } else if (i == 1) { // TipoPet
                petValor[1] = validaTipoPet(petValor[1]);
            } else if (i == 2) { // GeneroPet
                petValor[2] = validaGeneroPet(petValor[2]);
            } else if (i == 3) { // endereço
                petValor[3] = validaEndereco(petValor[3]);
            } else if (i == 4) { // idade
                petValor[4] = validaIdade(petValor[4]);
            } else if (i == 5) { // peso
                petValor[5] = validaPeso(petValor[5]);
            } else if (i == 6) { // raça
                petValor[6] = validaRaca(petValor[6]);
            }

        }

        return petValor;
    }

    public static String validaNome(String nome) {
        if (nome.isEmpty()) {
            nome = NAO_INFORMADO;
        } else if (!nome.matches("[A-Za-z ]+")) {
            throw new IllegalArgumentException("Erro! O nome precisa estar completo e não pode conter números ou caracteres especiais.");
        }

        return nome.trim();
    }

    public static String validaTipoPet(String tipo) {
        tipo = tipo.trim().toUpperCase();
        if (tipo.equals("CACHORRO") || tipo.equals("GATO")) {
            return tipo;
        }
        throw new IllegalArgumentException("Erro! O tipo deve ser CACHORRO ou GATO.");
    }

    public static String validaGeneroPet(String genero) {
        genero = genero.trim().toUpperCase();
        if (genero.equals("MASCULINO") || genero.equals("FEMININO")) {
            return genero;
        }
        throw new IllegalArgumentException("Erro! o gênero deve ser MASCULINO ou FEMININO");
    }

    public static String validaIdade(String idadePet) {
        if (idadePet.isEmpty()) {
            idadePet = NAO_INFORMADO;
            return idadePet;
        }

        double idade;

        try {
            idade = Double.parseDouble(idadePet);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Erro! A idade deve ser um número.");
        }

        if (idade < 1) {
            idade /= 12;
        } else if (idade > 20) {
            throw new IllegalArgumentException("Erro! A idade deve ser menor do que 20.");
        }

        return String.format("%.0f", idade);
    }

    public static String validaPeso(String pesoPet) {
        if (pesoPet.isEmpty()) {
            pesoPet = NAO_INFORMADO;
            return pesoPet;
        }

        pesoPet = pesoPet.replace(",", ".");
        double peso;

        try {
            peso = Double.parseDouble(pesoPet);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Erro! O peso deve ser um número.");
        }

         if (peso > 60 || peso < 0.5) {
            throw new IllegalArgumentException("Erro! O peso deve estar entre 0.5 e 60.");
        }

        return pesoPet;
    }

    public static String validaRaca(String raca) {

        if (raca.isEmpty()) {
            raca = NAO_INFORMADO;
        } else if (!raca.matches("[A-Za-z ]+")) {
            throw new IllegalArgumentException("Erro! A raça não pode conter números ou caracteres especiais.");
        }

        return raca;
    }

    public static String validaEndereco(String enderecoPet) {
        String[] endereco = enderecoPet.split(",");
        String rua = endereco[0].trim();
        String numero = endereco[1].trim();
        String cidade = endereco[2].trim();

        try {
            Integer.parseInt(numero);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Erro! O campo número aceita apenas números.");
        }

        if (rua.isEmpty() || cidade.isEmpty()) {
            throw new IllegalArgumentException("Erro! A cidade e a rua devem ser obrigatoriamente informadas.");
        } else if (numero.isEmpty()) {
            numero = NAO_INFORMADO;
        }

        return rua + ", " + numero + ", " + cidade;
    }

}
