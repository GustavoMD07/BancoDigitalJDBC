CREATE OR REPLACE FUNCTION public.encontrar_todos_cliente_v1()
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
	SELECT id, nome, cpf, data_nascimento, cep, rua, bairro, cidade, estado, tipo_de_cliente
	FROM cliente;
END;
$BODY$;