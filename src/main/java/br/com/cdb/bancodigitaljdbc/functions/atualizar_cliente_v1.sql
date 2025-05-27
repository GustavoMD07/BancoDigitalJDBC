CREATE OR REPLACE FUNCTION public.atualizar_cliente_v1(
	p_id bigint,
	p_nome character varying,
	p_cpf character varying,
	p_data_nascimento date,
	p_cep character varying,
	p_rua character varying,
	p_bairro character varying,
	p_cidade character varying,
	p_estado character varying,
	p_tipo_de_cliente character varying)
    RETURNS boolean
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
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

		IF FOUND THEN
			RETURN TRUE;
		ELSE
			RETURN FALSE;
		END IF;
END;
$BODY$;
