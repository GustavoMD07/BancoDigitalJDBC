CREATE OR REPLACE FUNCTION public.encontrar_cliente_v1(p_id BIGINT)
RETURNS TABLE (
	id BIGINT,
	nome character varying,
	cpf character varying,
	data_nascimento date,
	cep character varying,
	rua character varying,
	bairro character varying,
	cidade character varying,
	estado character varying,
	tipo_de_cliente character varying
)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN	
	RETURN QUERY
	SELECT s.id, s.nome, s.cpf, s.data_nascimento, s.cep, s.rua, s.bairro, s.cidade, s.estado, s.tipo_de_cliente
	FROM cliente s
	WHERE s.id = p_id;
END;
$BODY$;