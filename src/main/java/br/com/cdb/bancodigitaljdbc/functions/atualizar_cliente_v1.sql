CREATE OR REPLACE FUNCTION public.atualizar_cliente_v1(
	p_id BIGINT,
    p_nome VARCHAR,
    p_cpf VARCHAR,
    p_data_nascimento DATE,
    p_cep VARCHAR,
    p_rua VARCHAR,
    p_bairro VARCHAR,
    p_cidade VARCHAR,
    p_estado VARCHAR,
    p_tipo_de_cliente VARCHAR
)
RETURNS VOID
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	UPDATE cliente
	SET nome = p_nome,
        cpf = p_cpf,
        data_nascimento = p_data_nascimento,
        cep = p_cep,
        rua = p_rua,
        bairro = p_bairro,
        cidade = p_cidade,
        estado = p_estado,
        tipo_de_cliente = UPPER(p_tipo_de_cliente)
		WHERE id = p_id;
END;
$BODY$;