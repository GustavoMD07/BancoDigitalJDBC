CREATE OR REPLACE FUNCTION public.encontrar_cartao_v1(p_id BIGINT)
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
	SELECT s.id, s.senha, s.status, s.num_cartao, s.tipo_de_cartao, s.conta_id, s.fatura, s.limite_credito, s.limite_diario
	FROM cartao s
	WHERE s.id = p_id;
END;
$BODY$;