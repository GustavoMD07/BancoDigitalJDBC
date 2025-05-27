CREATE OR REPLACE FUNCTION public.encontrar_todos_cliente_v1(
	)
    RETURNS TABLE(id bigint, nome character varying, cpf character varying, data_nascimento date, cep character varying, rua character varying, bairro character varying, cidade character varying, estado character varying, tipo_de_cliente character varying) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
BEGIN	
	RETURN QUERY
	SELECT s.id, s.nome, s.cpf, s.data_nascimento, s.cep, s.rua, s.bairro, s.cidade, s.estado, s.tipo_de_cliente
	FROM cliente s;
END;
$BODY$;
