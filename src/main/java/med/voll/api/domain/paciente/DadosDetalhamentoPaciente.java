package med.voll.api.domain.paciente;

import med.voll.api.domain.endereco.Endereco;

public record DadosDetalhamentoPaciente(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        Endereco endereco,
        Boolean ativo
) {
    public DadosDetalhamentoPaciente(Paciente dados) {
        this(
                dados.getId(), dados.getNome(), dados.getEmail(), dados.getTelefone(),
                dados.getCpf(), dados.getEndereco(), dados.getAtivo()
        );
    }
}
