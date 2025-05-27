CREATE OR REPLACE FUNCTION public.encontrar_todos_cartao_v1()
RETURNS TABLE (
	id BIGINT,
	senha character varying,
	status boolean,
	num_cartao character varying,
	tipo_de_cartao character varying,
	conta_id bigint,
	fatura numeric,
	limite_credito numeric,
	limite_diario numeric
)
LANGUAGE 'plpgsql'
AS $BODY$
BEGIN
	RETURN QUERY
	SELECT id, senha, status, num_cartao, tipo_de_cartao, conta_id, fatura, limite_credito, limite_diario
	FROM cartao;
END;
$BODY$;